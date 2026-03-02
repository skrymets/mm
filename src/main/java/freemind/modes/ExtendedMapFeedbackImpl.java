package freemind.modes;

import freemind.controller.MindMapNodesSelection;
import freemind.main.FreeMindXml;
import freemind.main.Tools;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapNodeModel;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

public final class ExtendedMapFeedbackImpl extends ExtendedMapFeedbackAdapter {
    private MindMap mMap;

    @Override
    public MindMap getMap() {
        return mMap;
    }

    @Override
    public MindMapNode newNode(Object pUserObject, MindMap pMap) {
        return new MindMapNodeModel(pUserObject, pMap);
    }

    @Override
    public Font getDefaultFont() {
        int fontSize = 12;
        int fontStyle = 0;
        String fontFamily = "SansSerif";

        return getFontThroughMap(new Font(fontFamily, fontStyle, fontSize));
    }

    @Override
    public Transferable copy(MindMapNode pNode, boolean pSaveInvisible) {
        StringWriter stringWriter = new StringWriter();
        try {
            Document doc = FreeMindXml.newDocument();
            pNode.save(stringWriter, doc, getMap()
                    .getLinkRegistry(), pSaveInvisible, true);
        } catch (IOException ignored) {
        }
        List<String> nodeList = Collections.singletonList(getNodeID(pNode));
        return new MindMapNodesSelection(stringWriter.toString(), null,
                null, null, null, null, null, nodeList);
    }

    public void setMap(MindMap pMap) {
        mMap = pMap;
    }
}
