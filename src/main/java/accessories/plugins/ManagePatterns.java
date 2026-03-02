package accessories.plugins;

import accessories.plugins.dialogs.ManagePatternsPopupDialog;
import freemind.main.FreeMind;
import freemind.modes.mindmapmode.hooks.MindMapHookAdapter;

public class ManagePatterns extends MindMapHookAdapter {

    public ManagePatterns() {
        super();
    }

    public void startupMapHook() {
        super.startupMapHook();
        // start dialog:
        FreeMind frame = (FreeMind) getController().getFrame();
        ManagePatternsPopupDialog formatDialog = new ManagePatternsPopupDialog(frame.getJFrame(), getMindMapController());
        // formatDialog.pack();
        formatDialog.setModal(true);
        formatDialog.setVisible(true);
        // process result:
//        if (formatDialog.getResult() == ChooseFormatPopupDialog.OK) {
//            try {
//                // Save patterns in private pattern list:
//                File patternFile = getController().getFrame().getPatternsXML();
//                StylePatternFactory.savePatterns(new FileWriter(patternFile), formatDialog.getPatternList());
//                getMindMapController().loadPatterns(getMindMapController().getPatternsXML());
//                getMindMapController().getFrame().getFreeMindMenuBar().updateMenus(getMindMapController());
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
//            }
//        }
    }
}
