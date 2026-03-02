package freemind.modes.mindmapmode.actions;

import freemind.controller.MapModuleManager;
import freemind.model.MindMapNode;
import freemind.modes.ControllerAdapter;
import freemind.modes.FreemindAction;
import freemind.modes.mindmapmode.services.SearchService;
import freemind.modes.mindmapmode.services.SearchService.SearchResult;
import freemind.view.MapModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Action that searches across all open maps using the SearchService.
 * Results are displayed in a non-modal dialog; clicking a result navigates to that node.
 */
@SuppressWarnings("serial")
public class SearchAction extends FreemindAction {

    private final ControllerAdapter controller;
    private final SearchService searchService;

    public SearchAction(ControllerAdapter controller) {
        super("search_all_maps", "images/filefind.png", controller);
        this.controller = controller;
        this.searchService = new SearchService();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = controller.getController().getJFrame();
        String query = JOptionPane.showInputDialog(
                frame,
                controller.getText("search_query_prompt"),
                controller.getText("search_all_maps"),
                JOptionPane.QUESTION_MESSAGE);

        if (query == null || query.trim().isEmpty()) {
            return;
        }

        MapModuleManager mapModuleManager = controller.getController().getMapModuleManager();
        List<MapModule> modules = mapModuleManager.getMapModuleList();
        List<MindMapNode> rootNodes = new ArrayList<>();
        for (MapModule module : modules) {
            if (module.getModel() != null && module.getModel().getRootNode() != null) {
                rootNodes.add(module.getModel().getRootNode());
            }
        }

        List<SearchResult> results = searchService.searchNodes(rootNodes, query.trim(), false);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    controller.getText("search_no_results"),
                    controller.getText("search_all_maps"),
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showResultsDialog(frame, results, mapModuleManager);
    }

    private void showResultsDialog(JFrame frame, List<SearchResult> results,
                                   MapModuleManager mapModuleManager) {
        JDialog dialog = new JDialog(frame, controller.getText("search_all_maps"), false);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (SearchResult result : results) {
            String nodeText = result.node().getPlainTextContent();
            if (nodeText == null) {
                nodeText = "(no text)";
            }
            String display = "[" + result.matchLocation() + "] " + nodeText;
            listModel.addElement(display);
        }

        JList<String> resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = resultList.getSelectedIndex();
                    if (index >= 0 && index < results.size()) {
                        navigateToNode(results.get(index).node(), mapModuleManager);
                    }
                }
            }
        });

        JButton goButton = new JButton("Go");
        goButton.addActionListener(evt -> {
            int index = resultList.getSelectedIndex();
            if (index >= 0 && index < results.size()) {
                navigateToNode(results.get(index).node(), mapModuleManager);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(goButton);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new JScrollPane(resultList), BorderLayout.CENTER);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void navigateToNode(MindMapNode node, MapModuleManager mapModuleManager) {
        // Find which map module contains this node by walking up to the root
        MindMapNode root = node;
        while (root.getParentNode() != null) {
            root = root.getParentNode();
        }

        // Switch to the correct map module
        for (MapModule module : mapModuleManager.getMapModuleList()) {
            if (module.getModel() != null && module.getModel().getRootNode() == root) {
                mapModuleManager.changeToMapModule(module);
                break;
            }
        }

        // Center and select the node
        controller.centerNode(node);
    }
}
