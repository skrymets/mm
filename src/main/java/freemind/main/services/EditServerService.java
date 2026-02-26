/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
package freemind.main.services;

import freemind.main.EditServer;
import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.main.Resources;
import freemind.main.Tools;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.Socket;

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

    private EditServer editServer;

    public EditServerService(FreeMindMain frame) {
        this.frame = frame;
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
            try (BufferedReader in = new BufferedReader(new FileReader(portFile))) {
                String check = in.readLine();
                if (!"b".equals(check)) {
                    throw new Exception("Wrong port file format");
                }

                int port = parseInt(in.readLine());
                int key = parseInt(in.readLine());

                Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
                try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    out.writeInt(key);

                    String script = Tools.arrayToUrls(pArgs);
                    out.writeUTF(script);

                    log.info("Waiting for server");
                    try {
                        socket.getInputStream().read();
                    } catch (Exception ignored) {
                    }

                    in.close();
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
                && Resources.getInstance().getBoolProperty(FreeMind.RESOURCES_DON_T_OPEN_PORT)) {
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
