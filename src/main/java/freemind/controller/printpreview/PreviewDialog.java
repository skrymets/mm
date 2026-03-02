package freemind.controller.printpreview;

import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.view.mindmapview.MapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.net.URL;

public class PreviewDialog extends JDialog implements ActionListener {
    private final static double DEFAULT_ZOOM_FACTOR_STEP = 0.1;

    public PreviewDialog(String title, MapView view, PageFormat pPageFormat) {
        super(JOptionPane.getFrameForComponent(view), title, true);
        this.view = view;
        Preview preview = new Preview(view, 1, pPageFormat);
        JScrollPane scrollPane = new JScrollPane(preview);
        getContentPane().add(scrollPane, "Center");
        JToolBar toolbar = new JToolBar();
        // toolbar.setRollover(true);
        getContentPane().add(toolbar, "North");
        JLabel pageNumber = new JLabel("- 1 -");
        final JButton button = getButton("Back24.gif", new BrowseAction(
                preview, pageNumber, -1));
        toolbar.add(button);
        pageNumber.setPreferredSize(button.getPreferredSize());
        pageNumber.setHorizontalAlignment(JLabel.CENTER);
        toolbar.add(pageNumber);
        toolbar.add(getButton("Forward24.gif", new BrowseAction(preview,
                pageNumber, 1)));
        toolbar.add(new JToolBar.Separator());
        toolbar.add(getButton("ZoomIn24.gif", new ZoomAction(preview,
                DEFAULT_ZOOM_FACTOR_STEP)));
        toolbar.add(getButton("ZoomOut24.gif", new ZoomAction(preview,
                -DEFAULT_ZOOM_FACTOR_STEP)));
        toolbar.add(new JToolBar.Separator());
        JPanel dialog = new JPanel();
        dialog.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        ok.addActionListener(this);
        dialog.add(ok);
        getContentPane().add(dialog, "South");
        SwingUtils.addEscapeActionToDialog(this);
    }

    private JButton getButton(String iconName, AbstractAction action) {
        return getButton(null, iconName, action);
    }

    private JButton getButton(String name, String iconName,
                              AbstractAction action) {
        JButton result = null;

        ImageIcon icon = null;
        URL imageURL = getClass().getClassLoader().getResource(
                "images/" + iconName);
        if (imageURL != null)
            icon = freemind.view.ImageFactory.getInstance().createIcon(imageURL);

        if (action != null) {
            if (icon != null)
                action.putValue(Action.SMALL_ICON, freemind.view.ImageFactory.getInstance().createIcon(imageURL));
            if (name != null)
                action.putValue(Action.NAME, name);
            result = new JButton(action);
        } else
            result = new JButton(name, icon);

        return result;
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    protected final MapView view;
}
