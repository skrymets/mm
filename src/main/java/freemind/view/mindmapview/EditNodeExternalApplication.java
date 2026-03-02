package freemind.view.mindmapview;

import freemind.main.Tools;
import freemind.modes.ModeController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Slf4j
public class EditNodeExternalApplication extends EditNodeBase {

    private final KeyEvent firstEvent;

    public EditNodeExternalApplication(final NodeView node, final String text,
                                       final KeyEvent firstEvent, ModeController controller,
                                       EditControl editControl) {
        super(node, text, controller, editControl);
        this.firstEvent = firstEvent;
    }

    public void show() {
        getFrame();
        // final Controller controller = getController();
        // mainWindow.setEnabled(false);
        new Thread(() -> {
            FileWriter writer = null;
            try {

                File temporaryFile = File.createTempFile("tmm", ".html");

                // a. Write the text of the long node to the temporary file
                writer = new FileWriter(temporaryFile);
                writer.write(text);
                writer.close();

                // b. Call the editor
                String htmlEditingCommand = getFrame().getProperty(
                        "html_editing_command");
                String expandedHtmlEditingCommand = new MessageFormat(
                        htmlEditingCommand)
                        .format(new String[]{temporaryFile.toString()});
                // System.out.println("External application:"+expandedHtmlEditingCommand);
                Process htmlEditorProcess = Runtime.getRuntime().exec(
                        expandedHtmlEditingCommand);
                htmlEditorProcess.waitFor(); // Here we wait
                // until the
                // editor ends
                // up itself
                // Waiting does not work if the process starts another
                // process,
                // like in case of Microsoft Word. It works with certain
                // versions of FrontPage,
                // and with Vim though.

                // c. Get the text from the temporary file
                String content = FileUtils.readFileToString(temporaryFile, StandardCharsets.UTF_8);
                if (content == null) {
                    getEditControl().cancel();
                }
                getEditControl().ok(content);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    // if (bufferedReader != null) {
                    // bufferedReader.close();
                    // }
                } catch (IOException ignored) {
                }
            }
            // setBlocked(false);
            // mainWindow.setEnabled(true); // Not used as it loses focus on
            // the window
            // controller.obtainFocusForSelected(); }
        }).start();
    }

    protected KeyEvent getFirstEvent() {
        return firstEvent;
    }
}
