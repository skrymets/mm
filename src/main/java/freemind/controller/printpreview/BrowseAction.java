package freemind.controller.printpreview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
class BrowseAction extends AbstractAction {
    private final JLabel pageNumber;

    public BrowseAction(Preview preview, JLabel pageNumber, int pageStep) {
        super();
        this.preview = preview;
        this.pageStep = pageStep;
        this.pageNumber = pageNumber;
        pageIndexPainter = this::paintPageIndex;
    }

    public void actionPerformed(ActionEvent e) {
        preview.moveIndex(pageStep);
        paintPageIndex();
        preview.repaint();
        EventQueue.invokeLater(pageIndexPainter);
    }

    private void paintPageIndex() {
        pageNumber.setText(getPageIndexText());
        pageNumber.paintImmediately(0, 0, pageNumber.getWidth(),
                pageNumber.getHeight());
    }

    private String getPageIndexText() {
        return "- " + (1 + preview.getPageIndex()) + " -";
    }

    protected final Preview preview;
    protected final int pageStep;
    private final Runnable pageIndexPainter;
}
