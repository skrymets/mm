package freemind.controller.printpreview;

import freemind.view.mindmapview.MapView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

class Preview extends JComponent {
    private final static int DEFAULT_PREVIEW_SIZE = 300;
    private final static double MINIMUM_ZOOM_FACTOR = 0.1;
    private BufferedImage previewPageImage = null;
    private final PageFormat mPageFormat;

    public Preview(MapView view, double zoom, PageFormat pPageFormat) {
        this.view = view;
        mPageFormat = pPageFormat;
        PageFormat format = getPageFormat();
        if (zoom == 0.0) {
            if (format.getOrientation() == PageFormat.PORTRAIT)
                this.zoom = DEFAULT_PREVIEW_SIZE / format.getHeight();
            else
                this.zoom = DEFAULT_PREVIEW_SIZE / format.getWidth();
        } else
            this.zoom = zoom;
        resize();
    }

    protected void paintPaper(Graphics g, PageFormat format) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getPageWidth(format), getPageHeight(format));
        g.setColor(Color.black);
        g.drawRect(0, 0, getPageWidth(format) - 1, getPageHeight(format) - 1);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        PageFormat format = getPageFormat();
        paintPaper(g, format);
        if (previewPageImage == null) {
            previewPageImage = (BufferedImage) createImage(
                    getPageWidth(format) - 1, getPageHeight(format) - 1);
            Graphics2D imageGraphics = previewPageImage.createGraphics();
            imageGraphics.scale(zoom, zoom);
            while (Printable.NO_SUCH_PAGE == view.print(imageGraphics, format,
                    index) && index > 0) {
                index -= 1;
            }
        }
        g2d.drawImage(previewPageImage, 0, 0, this);
    }

    private int getPageHeight(PageFormat format) {
        return (int) (format.getHeight() * zoom);
    }

    private int getPageWidth(PageFormat format) {
        return (int) (format.getWidth() * zoom);
    }

    public void moveIndex(int indexStep) {
        int newIndex = index + indexStep;
        if (newIndex >= 0) {
            index = newIndex;
            previewPageImage = null;
        }
    }

    public void changeZoom(double zoom) {
        this.zoom = Math.max(MINIMUM_ZOOM_FACTOR, this.zoom + zoom);
        resize();
    }

    public void resize() {
        int size = (int) Math.max(getPageFormat().getWidth() * zoom,
                getPageFormat().getHeight() * zoom);
        setPreferredSize(new Dimension(size, size));
        previewPageImage = null;
        revalidate();
    }

    private PageFormat getPageFormat() {
        return mPageFormat;
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    protected final MapView view;
    protected int index = 0;
    protected double zoom = 0.0;

    public int getPageIndex() {
        return index;
    }
}
