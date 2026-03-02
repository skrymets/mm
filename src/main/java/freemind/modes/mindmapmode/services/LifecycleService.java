package freemind.modes.mindmapmode.services;

import freemind.extensions.HookFactory;
import freemind.extensions.HookFactory.RegistrationContainer;
import freemind.extensions.HookRegistration;
import freemind.modes.MindIcon;
import freemind.model.MindMap;
import freemind.modes.ModeController;
import freemind.modes.StylePatternFactory;
import freemind.modes.common.CommonNodeKeyListener;
import freemind.modes.common.listeners.CommonNodeMouseMotionListener;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.*;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;
import freemind.modes.mindmapmode.listeners.MindMapMouseMotionManager;
import freemind.modes.mindmapmode.listeners.MindMapNodeDropListener;
import freemind.modes.mindmapmode.listeners.MindMapNodeMotionListener;
import freemind.controller.actions.Pattern;
import freemind.controller.actions.PatternIcon;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages MindMapController lifecycle: icon/hook/pattern creation,
 * plugin registration/deregistration, startup/shutdown listeners.
 * Extracted from MindMapController to reduce god-object complexity.
 */
@Slf4j
public class LifecycleService {

    private final MindMapController controller;
    private final List<HookRegistration> registrations = new ArrayList<>();
    @Getter
    private List<Pattern> patternsList = new ArrayList<>();
    private long getEventIfChangedAfterThisTimeInMillies = 0;

    public LifecycleService(MindMapController controller) {
        this.controller = controller;
    }

    public void createIconActions() {
        List<String> iconNames = MindIcon.getAllIconNames();
        File iconDir = new File(controller.getResources().getFreemindDirectory(), "icons");
        if (iconDir.exists()) {
            String[] userIconArray = iconDir.list((dir, name) -> name.matches(".*\\.png"));
            if (userIconArray != null) {
                for (String s : userIconArray) {
                    String iconName = s;
                    iconName = iconName.substring(0, iconName.length() - 4);
                    if (iconName.isEmpty()) {
                        continue;
                    }
                    iconNames.add(iconName);
                }
            }
        }
        for (String iconName : iconNames) {
            MindIcon myIcon = MindIcon.factory(iconName);
            IconAction myAction = new IconAction(controller, myIcon, controller.getActions().removeLastIconAction);
            controller.getActions().iconActions.add(myAction);
        }
    }

    public void createNodeHookActions() {
        MindMapHookFactory factory = (MindMapHookFactory) controller.getHookFactory();
        List<String> nodeHookNames = factory.getPossibleNodeHooks();
        for (String hookName : nodeHookNames) {
            NodeHookAction action = new NodeHookAction(hookName, controller);
            controller.getActions().hookActions.add(action);
        }
        List<String> modeControllerHookNames = factory.getPossibleModeControllerHooks();
        for (String hookName : modeControllerHookNames) {
            MindMapControllerHookAction action = new MindMapControllerHookAction(hookName, controller);
            controller.getActions().hookActions.add(action);
        }
    }

    /**
     * Tries to load the user patterns and proposes an update to the new format.
     */
    public void loadPatternActions() {
        try {
            loadPatterns(controller.getPatternsXML());
        } catch (Exception ex) {
            log.error("Patterns not loaded", ex);
        }
    }

    public void loadPatterns(String patternsXML) throws Exception {
        createPatterns(StylePatternFactory.loadPatterns(patternsXML, controller.getResources()));
    }

    public void createPatterns(List<Pattern> patternsList) {
        this.patternsList = patternsList;
        controller.getActions().patterns = new ApplyPatternAction[patternsList.size()];
        for (int i = 0; i < controller.getActions().patterns.length; i++) {
            Pattern actualPattern = patternsList.get(i);
            controller.getActions().patterns[i] = new ApplyPatternAction(controller, actualPattern);

            // search icons for patterns:
            PatternIcon patternIcon = actualPattern.getPatternIcon();
            if (patternIcon != null && patternIcon.getValue() != null) {
                controller.getActions().patterns[i].putValue(Action.SMALL_ICON, MindIcon.factory(patternIcon.getValue()).getIcon());
            }
        }
    }

    /**
     * Registers plugins and listeners during controller startup.
     */
    public void startup() {
        HookFactory hookFactory = controller.getHookFactory();
        List<RegistrationContainer> pluginRegistrations = hookFactory.getRegistrations();
        log.trace("mScheduledActions are executed: {}", pluginRegistrations.size());
        for (RegistrationContainer container : pluginRegistrations) {
            try {
                Class registrationClass = container.hookRegistrationClass;
                Constructor hookConstructor = registrationClass.getConstructor(ModeController.class, MindMap.class);
                HookRegistration registrationInstance = (HookRegistration) hookConstructor.newInstance(new Object[]{controller, controller.getMap()});
                hookFactory.registerRegistrationContainer(container, registrationInstance);
                registrationInstance.register();
                registrations.add(registrationInstance);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        controller.invokeHooksRecursively(controller.getRootNode(), controller.getMap());

        // register mouse motion handlers:
        controller.getMapMouseMotionListener().register(new MindMapMouseMotionManager(controller));
        controller.getNodeDropListener().register(new MindMapNodeDropListener(controller));
        controller.getNodeKeyListener().register(new CommonNodeKeyListener(controller, controller::edit));
        controller.getNodeMotionListener().register(new MindMapNodeMotionListener(controller));
        controller.getNodeMouseMotionListener().register(new CommonNodeMouseMotionListener(controller));
        controller.getMap().registerMapSourceChangedObserver(controller, getEventIfChangedAfterThisTimeInMillies);
    }

    /**
     * Deregisters plugins and listeners during controller shutdown.
     */
    public void shutdown() {
        for (HookRegistration registrationInstance : registrations) {
            registrationInstance.deRegister();
        }
        controller.getHookFactory().deregisterAllRegistrationContainer();
        registrations.clear();
        // deregister motion handlers
        controller.getMapMouseMotionListener().deregister();
        controller.getNodeDropListener().deregister();
        controller.getNodeKeyListener().deregister();
        controller.getNodeMotionListener().deregister();
        controller.getNodeMouseMotionListener().deregister();
        getEventIfChangedAfterThisTimeInMillies = controller.getMap().deregisterMapSourceChangedObserver(controller);
    }
}
