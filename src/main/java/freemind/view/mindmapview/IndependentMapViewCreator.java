package freemind.view.mindmapview;

import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHookSubstituteUnknown;
import freemind.frok.patches.FreeMindMainMock;
import freemind.main.FreeMindMain;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.model.MapAdapter;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.MapFeedbackAdapter;
import freemind.modes.mindmapmode.MindMapMapModel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class IndependentMapViewCreator extends MapFeedbackAdapter {

    private MindMapMapModel mMap;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        if (args.length != 2) {
            log.info("Export map to png.\nUsage:\n java -jar lib/freemind.jar freemind.view.mindmapview.IndependentMapViewCreator <map_path>.mm <picture_path>.png");
            System.exit(0);
        }
        FreeMindMainMock freeMindMain = new FreeMindMainMock();
        IndependentMapViewCreator creator = new IndependentMapViewCreator();
        try {
            String outputFileName = args[1];
            creator.exportFileToPng(args[0], outputFileName, freeMindMain);
            log.info("Export to {} done.", outputFileName);
            System.exit(0);
        } catch (URISyntaxException | IOException e) {
            log.error(e.getLocalizedMessage(), e);

        }
        log.error("Error.");
        System.exit(1);
    }

    public MapView createMapViewForFile(String inputFileName, JPanel parent,
                                        FreeMindMain pFreeMindMain) throws
            IOException {
        mMap = new MindMapMapModel(this);
        Tools.FileReaderCreator readerCreator = new Tools.FileReaderCreator(new File(inputFileName));
        MindMapNode node = mMap.loadTree(readerCreator, MapAdapter.sDontAskInstance);
        mMap.setRoot(node);
        MapView mapView = new MapView(mMap, this);
        parent.add(mapView, BorderLayout.CENTER);
        mapView.setBounds(parent.getBounds());
        SwingUtils.waitForEventQueue();
        mapView.addNotify();
        return mapView;
    }

    public void exportFileToPng(String inputFileName, String outputFileName,
                                FreeMindMain pFreeMindMain) throws
            IOException, URISyntaxException {
        JPanel parent = new JPanel();
        Rectangle bounds = new Rectangle(0, 0, 400, 600);
        parent.setBounds(bounds);
        MapView mapView = createMapViewForFile(inputFileName, parent,
                pFreeMindMain);
        // layout components:
        mapView.getRoot().getMainView().doLayout();
        parent.setOpaque(true);
        parent.setDoubleBuffered(false); // for better performance
        parent.doLayout();
        parent.validate(); // this might not be necessary
        mapView.preparePrinting();
        parent.setBounds(mapView.getBounds());
        printToFile(mapView, outputFileName, false, 0);
    }

    public static void printToFile(MapView mapView,
                                   String outputFileName, boolean scale, int destSize) throws FileNotFoundException, IOException {
        Container parent = mapView.getParent();
        Rectangle dimI = mapView.getInnerBounds();
        Rectangle dim = mapView.getBounds();
        // do print
        BufferedImage backBuffer = new BufferedImage(dimI.width, dimI.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = backBuffer.createGraphics();
        int newX = -dim.x - dimI.x;
        int newY = -dim.y - dimI.y;
        g.translate(newX, newY);
        g.clipRect(-newX, -newY, dimI.width, dimI.height);
        parent.print(g);
        g.dispose();
        if (scale) {
            double maxDim = Math.max(dimI.getHeight(), dimI.getWidth());
            int newWidth = (int) (dimI.getWidth() * destSize / maxDim);
            int newHeight = (int) (dimI.getHeight() * destSize / maxDim);
            BufferedImage resized = new BufferedImage(newWidth, newHeight,
                    backBuffer.getType());
            Graphics2D g2 = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(backBuffer, 0, 0, newWidth, newHeight, 0, 0,
                    backBuffer.getWidth(), backBuffer.getHeight(), null);
            g2.dispose();
            backBuffer = resized;
        }
        FileOutputStream out1 = new FileOutputStream(outputFileName);
        ImageIO.write(backBuffer, "png", out1);
        out1.close();
    }

    @Override
    public MindMap getMap() {
        return mMap;
    }

    @Override
    public NodeHook createNodeHook(String pLoadName, MindMapNode pNode) {
        return new PermanentNodeHookSubstituteUnknown(pLoadName);
    }

}
