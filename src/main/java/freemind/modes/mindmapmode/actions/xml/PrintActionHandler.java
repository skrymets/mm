/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *
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
 *
 * Created on 24.04.2004
 */


package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.generated.instance.XmlAction;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.log4j.Log4j2;

/**
 * @author foltin
 */
@Log4j2
public class PrintActionHandler implements ActionHandler {

    private MindMapController c;

    public PrintActionHandler(MindMapController c) {
        super();
        this.c = c;

    }

    public void startTransaction(String name) {

    }

    public void endTransaction(String name) {

    }

    public void executeAction(XmlAction action) {
        String s = c.marshall(action);
        log.info(s);
        // Tools.printStackTrace();
    }

}
