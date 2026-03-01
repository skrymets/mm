package freemind.view.mindmapview.services;

import freemind.main.Resources;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Iterator;
import java.util.LinkedList;

@Slf4j
public class MapPrintingService {

    private final MapView mapView;
    private boolean isPrinting = false;
    private Color background = null;
    private Rectangle boundingRectangle = null;
    private boolean fitToPage = true;
    static boolean printOnWhiteBackground;

    public MapPrintingService(MapView mapView) {
        this.mapView = mapView;
    }

    public static void setPrintOnWhiteBackground(boolean value) {
        printOnWhiteBackground = value;
    }

    public static boolean isPrintOnWhiteBackground() {
        return printOnWhiteBackground;
    }

    public void preparePrinting() {
        if (isPrinting) {
            log.warn("Called preparePrinting although isPrinting is true.");
            return;
        }

        isPrinting = true;
        if (MapView.NEED_PREF_SIZE_BUG_FIX) {
            mapView.getRoot().updateAll();
            mapView.validate();
        } else {
            repaintSelecteds();
        }
        if (printOnWhiteBackground) {
            background = mapView.getBackground();
            mapView.setBackground(Color.WHITE);
        }
        boundingRectangle = mapView.getInnerBounds();
        fitToPage = mapView.getViewFeedback().getResources().getBoolProperty("fit_to_page");
    }

    public void endPrinting() {
        if (!isPrinting) {
            log.warn("Called endPrinting although isPrinting is false.");
            return;
        }

        isPrinting = false;
        if (printOnWhiteBackground) {
            mapView.setBackground(background);
        }
        if (MapView.NEED_PREF_SIZE_BUG_FIX) {
            mapView.getRoot().updateAll();
            mapView.validate();
        } else {
            repaintSelecteds();
        }
    }

    public boolean isCurrentlyPrinting() {
        return isPrinting;
    }

    public void repaintSelecteds() {
        final Iterator<NodeView> iterator = mapView.getSelectionService().getSelecteds().iterator();
        while (iterator.hasNext()) {
            NodeView next = iterator.next();
            next.repaintSelected();
        }
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        double userZoomFactor = 1;
        try {
            userZoomFactor = Double.parseDouble(mapView.getViewFeedback()
                    .getProperty("user_zoom"));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        userZoomFactor = Math.max(0, userZoomFactor);
        userZoomFactor = Math.min(2, userZoomFactor);

        if (fitToPage && pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D graphics2D = (Graphics2D) graphics;

        try {
            preparePrinting();

            double zoomFactor = 1;
            if (fitToPage) {
                double zoomFactorX = pageFormat.getImageableWidth()
                        / boundingRectangle.getWidth();
                double zoomFactorY = pageFormat.getImageableHeight()
                        / boundingRectangle.getHeight();
                zoomFactor = Math.min(zoomFactorX, zoomFactorY);
            } else {
                zoomFactor = userZoomFactor;

                int nrPagesInWidth = (int) Math.ceil(zoomFactor
                        * boundingRectangle.getWidth()
                        / pageFormat.getImageableWidth());
                int nrPagesInHeight = (int) Math.ceil(zoomFactor
                        * boundingRectangle.getHeight()
                        / pageFormat.getImageableHeight());
                if (pageIndex >= nrPagesInWidth * nrPagesInHeight) {
                    return Printable.NO_SUCH_PAGE;
                }
                int yPageCoord = (int) Math.floor((double) pageIndex / nrPagesInWidth);
                int xPageCoord = pageIndex - yPageCoord * nrPagesInWidth;

                graphics2D.translate(-pageFormat.getImageableWidth()
                        * xPageCoord, -pageFormat.getImageableHeight()
                        * yPageCoord);
            }

            graphics2D.translate(pageFormat.getImageableX(),
                    pageFormat.getImageableY());
            graphics2D.scale(zoomFactor, zoomFactor);
            graphics2D.translate(-boundingRectangle.getX(),
                    -boundingRectangle.getY());

            mapView.print(graphics2D);
        } finally {
            endPrinting();
        }
        return Printable.PAGE_EXISTS;
    }
}
