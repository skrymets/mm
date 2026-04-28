package freemind.main.services;

import freemind.main.EditServer;
import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.main.Resources;
import freemind.main.Tools;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Service for managing the EditServer and single-instance detection.
 * Extracted from FreeMind to reduce class complexity.
 */
@Slf4j
public class EditServerService {

    private static final String PORT_FILE = "portFile";

    private final FreeMindMain frame;
    private final Resources resources;

    private EditServer editServer;

    public EditServerService(FreeMindMain frame, Resources resources) {
        this.frame = frame;
        this.resources = resources;
    }

    /**
     * Starts the EditServer for inter-process communication.
     */
    public void initServer() {
        String portFile = getPortFile();
        if (portFile == null) {
            return;
        }
        editServer = new EditServer(portFile, frame);
        editServer.start();
    }

    /**
     * Checks whether another FreeMind instance is already running.
     * If found, sends the file paths to that instance and exits.
     */
    public void checkForAnotherInstance(String[] pArgs) {
        String portFile = getPortFile();
        if (isEmpty(portFile) || !new File(portFile).exists()) {
            return;
        }

        try {
            String[] lines = Files.readString(Paths.get(portFile), StandardCharsets.UTF_8)
                    .split("\\R", -1);
            if (lines.length < 3 || !"b".equals(lines[0])) {
                throw new Exception("Wrong port file format");
            }

            int port = parseInt(lines[1]);
            int key = parseInt(lines[2]);

            var socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
            try (var out = new DataOutputStream(socket.getOutputStream())) {
                out.writeInt(key);

                String script = Tools.arrayToUrls(pArgs);
                out.writeUTF(script);

                log.info("Waiting for server");
                try {
                    socket.getInputStream().read();
                } catch (Exception ignored) {
                }
            }

            System.exit(0);
        } catch (Exception e) {
            log.info("An error occurred while connecting to the FreeMind server instance. "
                    + "This probably means that FreeMind crashed and/or exited abnormally "
                    + "the last time it was run. If you don't know what this means, don't worry. "
                    + "Exception: {}", String.valueOf(e));
        }
    }

    /**
     * Returns the port file path, or null if no port should be opened.
     */
    public String getPortFile() {
        if (editServer == null
                && resources.getBoolProperty(FreeMind.RESOURCES_DON_T_OPEN_PORT)) {
            return null;
        }
        return frame.getFreemindDirectory() + File.separator + frame.getProperty(PORT_FILE);
    }

    /**
     * Stops the EditServer if running.
     */
    public void stopServer() {
        if (editServer != null) {
            editServer.stopServer();
        }
    }
}
