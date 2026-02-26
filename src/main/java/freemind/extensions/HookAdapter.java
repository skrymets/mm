/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
 */
/*
 * Created on 29.02.2004
 *
 */
package freemind.extensions;

import freemind.modes.MapFeedback;
import freemind.modes.ModeController;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Properties;

/**
 * Implments MindMapHook as an Adapter class. Implementation is straight
 * forward.
 *
 * @author foltin
 */
@Slf4j
public class HookAdapter implements MindMapHook {

    @Setter
    @Getter
    private String name;

    @Setter
    private Properties properties;
    private ModeController controller;

    /**
     * Stores the plugin base class as declared by the
     * plugin_registration/isBaseClass attribute.
     */
    private PluginBaseClassSearcher baseClass;
    protected MapFeedback mapFeedback;

    public HookAdapter() {
        baseClass = null;
    }

    public void startupMapHook() {
    }

    public void shutdownMapHook() {
        controller = null;
    }

    protected ModeController getController() {
        return controller;
    }

    protected Properties getProperties() {
        return properties;
    }

    public void setController(MapFeedback controller) {
        this.mapFeedback = controller;
        if (controller instanceof ModeController) {
            this.controller = (ModeController) controller;
        }
    }

    public String getResourceString(String property) {
        String result = properties.getProperty(property);
        if (result == null) {
            result = getController().getText(property);
        }
        if (result == null) {
            log.warn("The following property was not found:{}", property);
        }
        return result;
    }

    public URL getResource(String resourceName) {
        return this.getClass().getClassLoader().getResource(resourceName);
    }

    public Object getPluginBaseClass() {
        return baseClass.getPluginBaseObject();
    }

    public void setPluginBaseClass(PluginBaseClassSearcher baseClass) {
        this.baseClass = baseClass;
    }

    /**
     * After tree node change, the focus must be obtained as it is invalid.
     */
    protected void obtainFocusForSelected() {
        // Focus fix
        getController().getController().obtainFocusForSelected();
    }

}
