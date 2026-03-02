package accessories.plugins.dialogs;

import freemind.common.TextTranslator;
import java.util.Collections;
import freemind.controller.actions.Pattern;
import freemind.controller.actions.WindowConfigurationStorage;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.model.MapAdapter;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedbackAdapter;
import freemind.modes.StylePatternFactory;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapMapModel;
import freemind.modes.mindmapmode.dialogs.StylePatternFrame;
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.StylePatternFrameType;
import freemind.view.mindmapview.MapView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

@SuppressWarnings("serial")
@Slf4j
public class ChooseFormatPopupDialog extends JDialog implements TextTranslator, KeyListener {

    private final class DemoMapFeedback extends ExtendedMapFeedbackAdapter {
        MindMap mMap;

        @Override
        public MindMap getMap() {
            return mMap;
        }

        @Override
        public Font getDefaultFont() {
            return mController.getController().getDefaultFont();
        }

    }

    public static final int CANCEL = -1;

    public static final int OK = 1;

    private static final String WINDOW_PREFERENCE_STORAGE_PROPERTY = "accessories.plugins.dialogs.ChooseFormatPopupDialog.window_storage";

    @Getter
    private int result = CANCEL;

    private JPanel jContentPane = null;

    private final MindMapController mController;

    private JButton jCancelButton;

    private JButton jOKButton;

    private StylePatternFrame mStylePatternFrame;

    private MapView mDemoFrame;

    private MindMapNode mDemoNode;

    private final MindMapNode mNode;

    private DemoMapFeedback mDemoNodeMapFeedback;

    /**
     * This constructor is used, if you need the user to enter a pattern
     * generally.
     *
     * @param pNode if this not null, the text resp. children are taken for format demonstration.
     */
    public ChooseFormatPopupDialog(JFrame caller, MindMapController controller, String dialogTitle, Pattern pattern, MindMapNode pNode) {
        super(caller);
        this.mController = controller;
        mNode = pNode;

        initialize(dialogTitle);
        mStylePatternFrame.setPattern(pattern);
        mStylePatternFrame.addListeners();
    }

    /**
     * This method initializes this
     *
     */
    private void initialize(String dialogTitle) {

        this.setTitle(mController.getText(dialogTitle));
        JPanel contentPane = getJContentPane();
        this.setContentPane(contentPane);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                cancelPressed();
            }
        });
        addKeyListener(this);
        Action action = new AbstractAction() {

            public void actionPerformed(ActionEvent arg0) {
                cancelPressed();
            }
        };
        SwingUtils.addEscapeActionToDialog(this, action);
        pack();
        mController.decorateDialog(this, WINDOW_PREFERENCE_STORAGE_PROPERTY);

    }

    private void close() {
        WindowConfigurationStorage storage = new WindowConfigurationStorage();
        mController.storeDialogPositions(this, storage,
                WINDOW_PREFERENCE_STORAGE_PROPERTY);
        setVisible(false);
        this.dispose();
    }

    private void okPressed() {
        result = OK;
        close();
    }

    private void cancelPressed() {
        result = CANCEL;
        close();
    }

    /**
     * This method initializes jContentPane
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            /*
             * public GridBagConstraints(int gridx, int gridy, int gridwidth,
             * int gridheight, double weightx, double weighty, int anchor, int
             * fill, Insets insets, int ipadx, int ipady)
             */
            jContentPane.add(new JScrollPane(getStylePatternFrame()),
                    new GridBagConstraints(0, 0, 2, 1, 2.0, 8.0,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
            jContentPane.add(new JScrollPane(getDemoFrame()),
                    new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
            jContentPane.add(getJOKButton(), new GridBagConstraints(0, 2, 1, 1,
                    1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            jContentPane.add(getJCancelButton(), new GridBagConstraints(1, 2,
                    1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            getRootPane().setDefaultButton(getJOKButton());
        }
        return jContentPane;
    }

    private Component getDemoFrame() {
        if (mDemoFrame == null) {
            mDemoNodeMapFeedback = new DemoMapFeedback();
            final MindMapMapModel mMap = new MindMapMapModel(mDemoNodeMapFeedback);
            mDemoNodeMapFeedback.mMap = mMap;
            Tools.StringReaderCreator readerCreator = new Tools.StringReaderCreator(
                    mController.getText("accessories/plugins/dialogs/ChooseFormatPopupDialog.DemoNode"));
            try {
                MindMapNode root = mMap.loadTree(readerCreator,
                        MapAdapter.sDontAskInstance);
                mMap.setRoot(root);
                mDemoNode = (MindMapNode) root.getChildAt(0);
            } catch (RuntimeException | IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            mDemoFrame = new MapView(mMap, mDemoNodeMapFeedback);
            mDemoFrame.getScrollService().centerNode(mDemoFrame.getViewerRegistryService().getNodeView(mDemoNode));
        }
        return mDemoFrame;
    }

    private Component getStylePatternFrame() {
        if (mStylePatternFrame == null) {
            mStylePatternFrame = new StylePatternFrame(this, mController, StylePatternFrameType.WITHOUT_NAME_AND_CHILDS) {
                final Pattern mResetPattern = StylePatternFactory.getRemoveAllPattern();

                @Override
                public void propertyChange(PropertyChangeEvent pEvt) {
                    super.propertyChange(pEvt);
                    if (mNode != null) {
                        mDemoNode.setText(mNode.getText());
                    }
                    mDemoNodeMapFeedback.select(mDemoNode, Collections.singletonList(mDemoNode));
                    mDemoNodeMapFeedback.applyPattern(mDemoNode, mResetPattern);
                    Pattern pattern = mStylePatternFrame.getResultPattern();
                    mDemoNodeMapFeedback.applyPattern(mDemoNode, pattern);
                    mDemoFrame.getViewerRegistryService().getNodeView(mDemoNode).updateAll();
                    mDemoFrame.doLayout();
                }
            };
            mStylePatternFrame.init();

        }
        return mStylePatternFrame;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJOKButton() {
        if (jOKButton == null) {
            jOKButton = new JButton();

            jOKButton.setAction(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    okPressed();
                }

            });

            SwingUtils.setLabelAndMnemonic(jOKButton, mController.getText("ok"));
        }
        return jOKButton;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJCancelButton() {
        if (jCancelButton == null) {
            jCancelButton = new JButton();
            jCancelButton.setAction(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    cancelPressed();
                }
            });
            SwingUtils.setLabelAndMnemonic(jCancelButton,
                    mController.getText(("cancel")));
        }
        return jCancelButton;
    }

    public String getText(String pKey) {
        return mController.getText(pKey);
    }

    public Pattern getPattern() {
        return mStylePatternFrame.getResultPattern();
    }

    public Pattern getPattern(Pattern copyIntoPattern) {
        return mStylePatternFrame.getResultPattern(copyIntoPattern);
    }

    public void keyPressed(KeyEvent keyEvent) {
        log.debug("key pressed: {}", keyEvent);
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                cancelPressed();
                keyEvent.consume();
                break;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        log.debug("keyReleased: {}", keyEvent);
    }

    public void keyTyped(KeyEvent keyEvent) {
        log.debug("keyTyped: {}", keyEvent);
    }

}
