/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package freemind.view.mindmapview;

import freemind.main.SwingUtils;

import freemind.controller.NodeKeyListener;
import freemind.controller.NodeMotionListener;
import freemind.controller.NodeMouseMotionListener;
import freemind.main.*;
import freemind.model.MindMap;
import freemind.model.MindMapLink;
import freemind.model.MindMapNode;
import freemind.modes.MindMapArrowLink;
import freemind.preferences.FreemindPropertyListener;
import freemind.view.mindmapview.services.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * This class represents the view of a whole MindMap (in analogy to class JTree).
 */
@Slf4j
public class MapView extends JPanel implements Printable, Autoscroll {

    /**
     * Currently, this listener does nothing. But it should move the map
     * according to the resize event, such that the current map's center stays
     * at the same location (seen relative).
     */
    private final class ResizeListener extends ComponentAdapter {
        Dimension mSize;

        ResizeListener() {
            mSize = getSize();
        }

        public void componentResized(ComponentEvent event) {
            // int deltaWidth = mSize.width - getWidth();
            // int deltaHeight = mSize.height - getHeight();
            // Point viewPosition = getViewPosition();
            // viewPosition.x += deltaWidth/2;
            // viewPosition.y += deltaHeight/2;
            // mapViewport.setViewPosition(viewPosition);
            mSize = getSize();

        }
    }

    static public class ScrollPane extends JScrollPane {
        public ScrollPane() {
            super();
            // /*
            // * Diagnosis for the input map, but I haven't
            // * managed to remove the ctrl pageup/down keys
            // * from it.
            // */
            // InputMap inputMap =
            // getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            // KeyStroke[] keys = inputMap.allKeys();
            // if (keys != null) {
            // for (int i = 0; i < keys.length; i++) {
            // KeyStroke stroke = keys[i];
            // log.trace("Stroke: " + stroke);
            // }
            // } else {
            // log.trace("No keys in input map");
            // }
        }

        protected boolean processKeyBinding(KeyStroke pKs, KeyEvent pE, int pCondition, boolean pPressed) {
            /*
             * the scroll pane eats control page up and down. Moreover, the page
             * up and down itself is not very useful, as the map hops away too
             * far.
             */
            if (pE.getKeyCode() == KeyEvent.VK_PAGE_DOWN || pE.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                return false;
            }
            return super.processKeyBinding(pKs, pE, pCondition, pPressed);
        }

        protected void validateTree() {
            final Component view = getViewport().getView();
            if (view != null) {
                view.validate();
            }
            super.validateTree();
        }
    }

    // Selected inner class moved to MapSelectionService

    @Getter
    private final MindMap model;
    private NodeView rootView = null;

    @Getter
    private float zoom = 1F;
    private boolean disableMoveCursor = true;
    @Setter
    @Getter
    private int siblingMaxLevel;
    private int maxNodeWidth = 0;

    static boolean printOnWhiteBackground;
    static Color standardMapBackgroundColor;
    public static Color standardSelectColor;
    public static Color standardSelectRectangleColor;
    public static Color standardNodeTextColor;
    public static boolean standardDrawRectangleForSelection;
    private static FreemindPropertyListener propertyChangeListener;

    public static final boolean NEED_PREF_SIZE_BUG_FIX = System.getProperty("java.version").compareTo("1.5.0") < 0;

    private final ViewFeedback mFeedback;

    // Services
    private final ViewerRegistryService viewerRegistryService;
    private final MapGeometryService geometryService;
    private final MapPrintingService printingService;
    private final LinkRenderingService linkRenderingService;
    private final MapRenderingService renderingService;
    private final MapSelectionService selectionService;
    private final ScrollService scrollService;
    private final NavigationService navigationService;

    public MapView(MindMap model, ViewFeedback pFeedback) {

        super();
        this.model = model;
        mFeedback = pFeedback;
        // Initialize services
        viewerRegistryService = new ViewerRegistryService(this);
        geometryService = new MapGeometryService(this);
        printingService = new MapPrintingService(this);
        linkRenderingService = new LinkRenderingService(this);
        renderingService = new MapRenderingService(this, linkRenderingService);
        selectionService = new MapSelectionService(this, printingService);
        scrollService = new ScrollService(this);
        navigationService = new NavigationService(this, selectionService, scrollService);
        // initialize the standard colors.
        if (standardNodeTextColor == null) {
            try {
                String stdcolor = mFeedback.getProperty(FreeMind.RESOURCES_BACKGROUND_COLOR);
                standardMapBackgroundColor = ColorUtils.xmlToColor(stdcolor);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                standardMapBackgroundColor = Color.WHITE;
            }
            try {
                String stdcolor = mFeedback.getProperty(FreeMind.RESOURCES_NODE_TEXT_COLOR);
                standardNodeTextColor = ColorUtils.xmlToColor(stdcolor);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                standardSelectColor = Color.WHITE;
            }
            // initialize the selectedColor:
            try {
                String stdcolor = mFeedback.getProperty(FreeMind.RESOURCES_SELECTED_NODE_COLOR);
                standardSelectColor = ColorUtils.xmlToColor(stdcolor);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                standardSelectColor = Color.BLUE.darker();
            }

            // initialize the selectedTextColor:
            try {
                String stdtextcolor = mFeedback.getProperty(FreeMind.RESOURCES_SELECTED_NODE_RECTANGLE_COLOR);
                standardSelectRectangleColor = ColorUtils.xmlToColor(stdtextcolor);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                standardSelectRectangleColor = Color.WHITE;
            }
            try {
                String drawCircle = mFeedback.getProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION);
                standardDrawRectangleForSelection = "true".equals(drawCircle);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                standardDrawRectangleForSelection = false;
            }

            try {
                String printOnWhite = mFeedback.getProperty(FreeMind.RESOURCE_PRINT_ON_WHITE_BACKGROUND);
                MapPrintingService.setPrintOnWhiteBackground("true".equals(printOnWhite));
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                MapPrintingService.setPrintOnWhiteBackground(true);
            }
            // only created once:
            createPropertyChangeListener();
            // initialize antializing:
            propertyChangeListener.propertyChanged(
                    FreeMindCommon.RESOURCE_ANTIALIAS,
                    mFeedback.getProperty(FreeMindCommon.RESOURCE_ANTIALIAS),
                    null);
        }
        this.setAutoscrolls(true);

        this.setLayout(new MindMapLayout());

        initRoot();

        setBackground(standardMapBackgroundColor);
        addMouseListener(pFeedback.getMapMouseMotionListener());
        addMouseMotionListener(pFeedback.getMapMouseMotionListener());
        addMouseWheelListener(pFeedback.getMapMouseWheelListener());
        addKeyListener(getNodeKeyListener());

        // fc, 20.6.2004: to enable tab for insert.
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.emptySet());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.emptySet());
        setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, Collections.emptySet());
        // end change.

        // fc, 31.3.2013: set policy to achive that after note window close, the
        // current node is selected.
        setFocusTraversalPolicy(new FocusTraversalPolicy() {

            public Component getLastComponent(Container pAContainer) {
                return getDefaultComponent(pAContainer);
            }

            public Component getFirstComponent(Container pAContainer) {
                return getDefaultComponent(pAContainer);
            }

            public Component getDefaultComponent(Container pAContainer) {
                Component defaultComponent = selectionService.getSelected();
                log.trace("Focus traversal to: {}", defaultComponent);
                return defaultComponent;
            }

            public Component getComponentBefore(Container pAContainer, Component pAComponent) {
                return getDefaultComponent(pAContainer);
            }

            public Component getComponentAfter(Container pAContainer, Component pAComponent) {
                return getDefaultComponent(pAContainer);
            }
        });
        this.setFocusTraversalPolicyProvider(true);
        // like in excel - write a letter means edit (PN)
        // on the other hand it doesn't allow key navigation (sdfe)
        disableMoveCursor = mFeedback.getResources().getBoolProperty("disable_cursor_move_paper");

        addComponentListener(new ResizeListener());
    }

    /**
     * @return the belonging instance of a ViewFeedback (in fact, a ModeController)
     */
    public ViewFeedback getViewFeedback() {
        return mFeedback;
    }

    public ViewerRegistryService getViewerRegistryService() {
        return viewerRegistryService;
    }

    public MapGeometryService getGeometryService() {
        return geometryService;
    }

    public MapPrintingService getPrintingService() {
        return printingService;
    }

    public LinkRenderingService getLinkRenderingService() {
        return linkRenderingService;
    }

    public MapRenderingService getRenderingService() {
        return renderingService;
    }

    public MapSelectionService getSelectionService() {
        return selectionService;
    }

    public ScrollService getScrollService() {
        return scrollService;
    }

    public NavigationService getNavigationService() {
        return navigationService;
    }


    private void createPropertyChangeListener() {

        propertyChangeListener = (propertyName, newValue, oldValue) -> {

            switch (propertyName) {
                case FreeMind.RESOURCES_NODE_TEXT_COLOR:
                    standardNodeTextColor = ColorUtils.xmlToColor(newValue);
                    MapView.this.getRoot().updateAll();
                    break;
                case FreeMind.RESOURCES_BACKGROUND_COLOR:
                    standardMapBackgroundColor = ColorUtils.xmlToColor(newValue);
                    MapView.this.setBackground(standardMapBackgroundColor);
                    break;
                case FreeMind.RESOURCES_SELECTED_NODE_COLOR:
                    standardSelectColor = ColorUtils.xmlToColor(newValue);
                    MapView.this.printingService.repaintSelecteds();
                    break;
                case FreeMind.RESOURCES_SELECTED_NODE_RECTANGLE_COLOR:
                    standardSelectRectangleColor = ColorUtils.xmlToColor(newValue);
                    MapView.this.printingService.repaintSelecteds();
                    break;
                case FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION:
                    standardDrawRectangleForSelection = "true".equals(newValue);
                    MapView.this.printingService.repaintSelecteds();
                    break;
                case FreeMind.RESOURCE_PRINT_ON_WHITE_BACKGROUND:
                    MapPrintingService.setPrintOnWhiteBackground("true".equals(newValue));
                    break;
                case FreeMindCommon.RESOURCE_ANTIALIAS:
                    if ("antialias_none".equals(newValue)) {
                        MapRenderingService.setAntialiasEdges(false);
                        MapRenderingService.setAntialiasAll(false);
                    }
                    if ("antialias_edges".equals(newValue)) {
                        MapRenderingService.setAntialiasEdges(true);
                        MapRenderingService.setAntialiasAll(false);
                    }
                    if ("antialias_all".equals(newValue)) {
                        MapRenderingService.setAntialiasEdges(true);
                        MapRenderingService.setAntialiasAll(true);
                    }
                    break;
            }

        };
        Resources.addPropertyChangeListener(propertyChangeListener);
    }

    public Object setEdgesRenderingHint(Graphics2D g) {
        return renderingService.setEdgesRenderingHint(g);
    }

    public void setTextRenderingHint(Graphics2D g) {
        renderingService.setTextRenderingHint(g);
    }

    public void initRoot() {
        scrollService.setRootContentLocation(new Point());
        rootView = NodeViewFactory.getInstance().newNodeView(getModel().getRootNode(), 0, this, this);
        rootView.insert();

        revalidate();
    }

    public int getMaxNodeWidth() {
        if (maxNodeWidth == 0) {
            try {
                maxNodeWidth = Integer.parseInt(mFeedback.getProperty("max_node_width"));
            } catch (NumberFormatException e) {
                log.error(e.getLocalizedMessage(), e);
                maxNodeWidth = Integer.parseInt(mFeedback.getProperty("el__max_default_window_width"));
            }
        }
        return maxNodeWidth;
    }

    //
    // Navigation
    //

    public void centerNode(final NodeView node) {
        scrollService.centerNode(node);
    }

    public void scrollNodeToVisible(NodeView node) {
        scrollService.scrollNodeToVisible(node);
    }

    public void scrollNodeToVisible(NodeView node, int extraWidth) {
        scrollService.scrollNodeToVisible(node, extraWidth);
    }

    public void scrollBy(int x, int y) {
        scrollService.scrollBy(x, y);
    }

    public void setViewLocation(int x, int y) {
        scrollService.setViewLocation(x, y);
    }

    protected void setViewPosition(Point currentPoint) {
        scrollService.setViewPosition(currentPoint);
    }

    //
    // Node Navigation
    //

    public void move(KeyEvent e) {
        navigationService.move(e);
    }

    public void moveToRoot() {
        navigationService.moveToRoot();
    }


    //
    // get/set methods
    //

    // e.g. for dragging cursor (PN)
    public void setMoveCursor(boolean isHand) {
        int requiredCursor = (isHand && !disableMoveCursor) ? Cursor.MOVE_CURSOR
                : Cursor.DEFAULT_CURSOR;
        if (getCursor().getType() != requiredCursor) {
            setCursor(requiredCursor != Cursor.DEFAULT_CURSOR ? new Cursor(
                    requiredCursor) : null);
        }
    }

    NodeMouseMotionListener getNodeMouseMotionListener() {
        return getViewFeedback().getNodeMouseMotionListener();
    }

    NodeMotionListener getNodeMotionListener() {
        return getViewFeedback().getNodeMotionListener();
    }

    NodeKeyListener getNodeKeyListener() {
        return getViewFeedback().getNodeKeyListener();
    }

    DragGestureListener getNodeDragListener() {
        return getViewFeedback().getNodeDragListener();
    }

    DropTargetListener getNodeDropListener() {
        return getViewFeedback().getNodeDropListener();
    }

    public int getZoomed(int number) {
        return (int) (number * zoom);
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        getRoot().updateAll();
        revalidate();
        scrollService.setNodeToBeVisible(selectionService.getSelected());
    }

    protected void validateTree() {
        selectionService.validateSelecteds();
        super.validateTree();
        scrollService.setViewPositionAfterValidate();
    }

    /*****************************************************************
     ** P A I N T I N G **
     *****************************************************************/

    // private static Image image = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        long startMilli = System.currentTimeMillis();
        if (isValid()) {
            Point rootContentLocation = scrollService.getRootContentLocation();
            getRoot().getContent().getLocation(rootContentLocation);
            PointUtils.convertPointToAncestor(getRoot(), rootContentLocation, getParent());
        }
        final Graphics2D g2 = (Graphics2D) g;
        final Object renderingHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        final Object renderingTextHint = g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        renderingService.setTextRenderingHint(g2);
        final Object oldRenderingHintFM = g2.getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS);
        final Object newRenderingHintFM = getZoom() != 1F
                ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
                : RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
        if (oldRenderingHintFM != newRenderingHintFM) {
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, newRenderingHintFM);
        }
        super.paint(g);
        if (oldRenderingHintFM != newRenderingHintFM && RenderingHints.KEY_FRACTIONALMETRICS.isCompatibleValue(oldRenderingHintFM)) {
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oldRenderingHintFM);
        }
        if (RenderingHints.KEY_ANTIALIASING.isCompatibleValue(renderingHint)) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint);
        }
        if (RenderingHints.KEY_TEXT_ANTIALIASING.isCompatibleValue(renderingTextHint)) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, renderingTextHint);
        }
        long localTime = System.currentTimeMillis() - startMilli;
        renderingService.recordPaintTime(localTime);
        log.trace("End paint of {} in {}. Mean time:{}", getModel().getRestorable(), localTime,
                renderingService.getPaintingTime() / renderingService.getPaintingAmount());
    }

    public void paintChildren(Graphics graphics) {
        HashMap<String, NodeView> labels = new HashMap<>();
        linkRenderingService.resetArrowLinkViews();
        linkRenderingService.collectLabels(rootView, labels);
        super.paintChildren(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;
        Object renderingHint = renderingService.setEdgesRenderingHint(graphics2d);
        linkRenderingService.paintLinks(rootView, graphics2d, labels, null);
        SwingUtils.restoreAntialiasing(graphics2d, renderingHint);
        renderingService.paintSelecteds(graphics2d);
    }

    public MindMapArrowLink detectCollision(Point p) {
        return linkRenderingService.detectCollision(p);
    }

    public void preparePrinting() {
        printingService.preparePrinting();
    }

    public void endPrinting() {
        printingService.endPrinting();
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        return printingService.print(graphics, pageFormat, pageIndex);
    }

    public boolean isCurrentlyPrinting() {
        return printingService.isCurrentlyPrinting();
    }

    public Rectangle getInnerBounds() {
        return geometryService.getInnerBounds(linkRenderingService.getArrowLinkViews());
    }

    public NodeView getRoot() {
        return rootView;
    }

    // this property is used when the user navigates up/down using cursor keys
    // (PN)
    // it will keep the level of nodes that are understand as "siblings"

    private static final int margin = 20;

    /*
     * (non-Javadoc)
     *
     * @see java.awt.dnd.Autoscroll#getAutoscrollInsets()
     */
    public Insets getAutoscrollInsets() {
        Rectangle outer = getBounds();
        Rectangle inner = getParent().getBounds();
        return new Insets(inner.y - outer.y + margin, inner.x - outer.x
                + margin, outer.height - inner.height - inner.y + outer.y
                + margin, outer.width - inner.width - inner.x + outer.x
                + margin);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.dnd.Autoscroll#autoscroll(java.awt.Point)
     */
    public void autoscroll(Point cursorLocn) {
        Rectangle r = new Rectangle((int) cursorLocn.getX() - margin,
                (int) cursorLocn.getY() - margin, 1 + 2 * margin,
                1 + 2 * margin);
        scrollRectToVisible(r);
    }

    public NodeView getNodeView(MindMapNode node) {
        return viewerRegistryService.getNodeView(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        if (!getParent().isValid()) {
            final Dimension preferredLayoutSize = getLayout()
                    .preferredLayoutSize(this);
            return preferredLayoutSize;
        }
        return super.getPreferredSize();
    }

    public Point getNodeContentLocation(NodeView nodeView) {
        return geometryService.getNodeContentLocation(nodeView);
    }

    public Dimension getViewportSize() {
        return geometryService.getViewportSize();
    }

    public Point getViewPosition() {
        return geometryService.getViewPosition();
    }

    public Collection<NodeView> getViewers(MindMapNode mindMapNode) {
        return viewerRegistryService.getViewers(mindMapNode);
    }

    public void addViewer(MindMapNode mindMapNode, NodeView viewer) {
        viewerRegistryService.addViewer(mindMapNode, viewer);
    }

    public void removeViewer(MindMapNode mindMapNode, NodeView viewer) {
        viewerRegistryService.removeViewer(mindMapNode, viewer);
    }

    public void acceptViewVisitor(MindMapNode mindMapNode, NodeViewVisitor visitor) {
        viewerRegistryService.acceptViewVisitor(mindMapNode, visitor);
    }


}
