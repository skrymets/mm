package freemind.modes.mindmapmode.actions;

import freemind.main.HtmlTools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MapView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class JoinNodesAction extends MindmapAction {
    private final MindMapController controller;

    public JoinNodesAction(MindMapController controller) {
        super("join_nodes", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode selectedNode = controller.getView().getSelectionService().getSelected()
                .getModel();
        ArrayList<MindMapNode> selectedNodes = controller.getView()
                .getSelectionService().getSelectedNodesSortedByY();
        joinNodes(selectedNode, selectedNodes);
    }

    public void joinNodes(MindMapNode selectedNode, List<MindMapNode> selectedNodes) {
        String newContent = "";
        // Make sure the selected node do not have children
        final MapView mapView = controller.getView();
        for (MindMapNode node : selectedNodes) {
            if (node.hasChildren()) {
                JOptionPane.showMessageDialog(mapView,
                        controller.getText("cannot_join_nodes_with_children"),
                        "FreeMind", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Join
        boolean isHtml = false;
        for (MindMapNode node : selectedNodes) {
            final String nodeContent = node.toString();
            final boolean isHtmlNode = HtmlTools.isHtmlNode(nodeContent);
            newContent = addContent(newContent, isHtml, nodeContent, isHtmlNode);
            if (node != selectedNode) {
                controller.deleteNode(node);
            }
            isHtml = isHtml || isHtmlNode;
        }

        mapView.getSelectionService().selectAsTheOnlyOneSelected(mapView.getViewerRegistryService().getNodeView(selectedNode));
        controller.setNodeText(selectedNode, newContent);
    }

    final static Pattern BODY_START = Pattern.compile("<body>",
            Pattern.CASE_INSENSITIVE);
    final static Pattern BODY_END = Pattern.compile("</body>",
            Pattern.CASE_INSENSITIVE);

    private String addContent(String content, boolean isHtml,
                              String nodeContent, boolean isHtmlNode) {
        if (isHtml) {
            final String[] start = BODY_END.split(content, -2);
            content = start[0];
            if (!isHtmlNode) {
                final String[] end = BODY_START.split(content, 2);
                nodeContent = end[0] + "<body><p>" + nodeContent + "</p>";
            }
        }
        if (isHtmlNode & !content.isEmpty()) {
            final String[] end = BODY_START.split(nodeContent, 2);
            nodeContent = end[1];
            if (!isHtml) {
                content = end[0] + "<body><p>" + content + "</p>";
            }
        }
        if (!(isHtml || isHtmlNode || content.isEmpty())) {
            content += " ";
        }
        content += nodeContent;
        return content;
    }
}
