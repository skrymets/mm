package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.Resources;
import freemind.main.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class PageAction extends AbstractAction {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String RESOURCE_FIT_TO_PAGE = "fit_to_page";
    private static final String RESOURCE_USER_ZOOM = "user_zoom";

    private final Controller controller;

    public PageAction(Controller controller) {
        super(controller.getResourceString("page"));
        setEnabled(false);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        if (!controller.getPrintService().acquirePrinterJobAndPageFormat()) {
            return;
        }

        // Ask about custom printing settings
        final JDialog dialog = new JDialog((JFrame) controller.getFrame(), controller.getResourceString("printing_settings"), /* modal= */true);
        final JCheckBox fitToPage = new JCheckBox(controller.getResourceString(RESOURCE_FIT_TO_PAGE), Resources.getInstance().getBoolProperty(RESOURCE_FIT_TO_PAGE));
        final JLabel userZoomL = new JLabel(controller.getResourceString(RESOURCE_USER_ZOOM));
        final JTextField userZoom = new JTextField(controller.getProperty(RESOURCE_USER_ZOOM), 3);
        userZoom.setEditable(!fitToPage.isSelected());
        final JButton okButton = new JButton();
        SwingUtils.setLabelAndMnemonic(okButton, controller.getResourceString("ok"));

        JPanel panel = new JPanel();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        final Integer[] eventSource = new Integer[]{0};
        okButton.addActionListener(event -> {
            eventSource[0] = 1;
            dialog.dispose();
        });
        fitToPage.addItemListener(e12 -> userZoom.setEditable(e12.getStateChange() == ItemEvent.DESELECTED));

        // c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        gridbag.setConstraints(fitToPage, c);
        panel.add(fitToPage);
        c.gridy = 1;
        c.gridwidth = 1;
        gridbag.setConstraints(userZoomL, c);
        panel.add(userZoomL);
        c.gridx = 1;
        c.gridwidth = 1;
        gridbag.setConstraints(userZoom, c);
        panel.add(userZoom);
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(10, 0, 0, 0);
        gridbag.setConstraints(okButton, c);
        panel.add(okButton);
        panel.setLayout(gridbag);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.setLocationRelativeTo((JFrame) controller.getFrame());
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.pack(); // calculate the size
        dialog.setVisible(true);

        if (eventSource[0] == 1) {
            controller.setProperty(RESOURCE_USER_ZOOM, userZoom.getText());
            controller.setProperty(RESOURCE_FIT_TO_PAGE, fitToPage.isSelected() ? TRUE : FALSE);
        } else
            return;

        // Ask user for page format (e.g., portrait/landscape)
        controller.getPrintService().setPageFormat(controller.getPrintService().getPrinterJob().pageDialog(controller.getPrintService().getPageFormat()));
        controller.getPrintService().storePageFormat(controller::setProperty);
    }
}
