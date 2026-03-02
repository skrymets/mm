package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.RevertXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class RevertActor extends XmlActorAdapter {

    public RevertActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void revertMap(MindMap map, File file) throws IOException {
        RevertXmlAction doAction = createRevertXmlAction(file);
        RevertXmlAction undoAction = createRevertXmlAction(
                map, null, file.getName());
        execute(new ActionPair(doAction, undoAction));
    }

    public void openXmlInsteadOfMap(String xmlFileContent) {
        try {
            RevertXmlAction doAction = createRevertXmlAction(xmlFileContent,
                    null, null);
            RevertXmlAction undoAction = createRevertXmlAction(
                    getExMapFeedback().getMap(), null, null);
            execute(new ActionPair(doAction, undoAction));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public RevertXmlAction createRevertXmlAction(File file) throws IOException {
        String fileName = file.getAbsolutePath();
        FileReader f = new FileReader(file);
        StringBuilder buffer = new StringBuilder();
        for (int c; (c = f.read()) != -1; )
            buffer.append((char) c);
        f.close();
        return createRevertXmlAction(buffer.toString(), fileName, null);
    }

    public RevertXmlAction createRevertXmlAction(MindMap map, String fileName,
                                                 String filePrefix) throws IOException {
        StringWriter writer = new StringWriter();
        map.getXml(writer);
        return createRevertXmlAction(writer.getBuffer().toString(), fileName,
                filePrefix);
    }

    /**
     * @param filePrefix is used to generate the name of the reverted map in case that
     *                   fileName is null.
     */
    public RevertXmlAction createRevertXmlAction(String xmlPackedFile,
                                                 String fileName, String filePrefix) {
        RevertXmlAction revertXmlAction = new RevertXmlAction();
        revertXmlAction.setLocalFileName(fileName);
        revertXmlAction.setMap(xmlPackedFile);
        revertXmlAction.setFilePrefix(filePrefix);
        return revertXmlAction;
    }

    public void act(XmlAction action) {
        if (action instanceof RevertXmlAction) {
            try {
                RevertXmlAction revertAction = (RevertXmlAction) action;

                // close the old map.
                getExMapFeedback().close(true);
                if (revertAction.getLocalFileName() != null) {
                    getExMapFeedback().load(new File(revertAction
                            .getLocalFileName()));
                } else {
                    // the map is given by xml. we store it and open it.
                    String filePrefix = getExMapFeedback()
                            .getResourceString("freemind_reverted");
                    if (revertAction.getFilePrefix() != null) {
                        filePrefix = revertAction.getFilePrefix();
                    }
                    String xmlMap = revertAction.getMap();
                    File tempFile = File.createTempFile(filePrefix,
                            freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION,
                            new File(getExMapFeedback().getResources().getFreemindDirectory()));
                    FileWriter fw = new FileWriter(tempFile);
                    fw.write(xmlMap);
                    fw.close();
                    getExMapFeedback().load(tempFile);
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public Class<RevertXmlAction> getDoActionClass() {
        return RevertXmlAction.class;
    }

}
