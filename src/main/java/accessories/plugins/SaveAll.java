/*
 * Created on 12.03.2004
 *
 */
package accessories.plugins;

import freemind.controller.Controller;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.MapModule;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaveAll extends ModeControllerHookAdapter {

    public SaveAll() {
        super();
    }

    public void startupMapHook() {
        super.startupMapHook();
        // store initial mapModule:
        Controller mainController = getController().getController();
        MapModule initialMapModule = mainController.getMapModule();
        Map<String, MapModule> modules = getMapModules();
        // to prevent concurrent modification:
        List<MapModule> v = new ArrayList<>(modules.values());
        for (MapModule module : v) {
            // change to module to display map properly.
            mainController.getMapModuleManager().changeToMapModule(module.toString());
            if (!module.getModeController().save()) {
                // if not successfully, break the action.
                JOptionPane
                        .showMessageDialog(
                                getController().getFrame().getContentPane(),
                                "FreeMind",
                                getResourceString("accessories/plugins/SaveAll.properties_save_all_cancelled"),
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        mainController.getMapModuleManager().changeToMapModule(
                initialMapModule.toString());
    }

    private Map<String, MapModule> getMapModules() {
        return getController().getController().getMapModuleManager()
                .getMapModules();
    }

}
