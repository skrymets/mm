package freemind.modes.mindmapmode.services;

import freemind.extensions.ModeControllerHook;
import freemind.model.MindMapNode;
import freemind.modes.MapFeedback;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

/**
 * Service for hook (plugin/extension) operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
@Slf4j
public class HookService {

    private final XmlActorFactory actorFactory;

    public HookService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Adds a hook to the specified nodes.
     */
    public void addHook(MindMapNode focussed, List<MindMapNode> selecteds, String hookName, Properties hookProperties) {
        actorFactory.getAddHookActor().addHook(focussed, selecteds, hookName, hookProperties);
    }

    /**
     * Removes a hook from the specified nodes.
     */
    public void removeHook(MindMapNode focussed, List<MindMapNode> selecteds, String hookName) {
        actorFactory.getAddHookActor().removeHook(focussed, selecteds, hookName);
    }

    /**
     * Invokes a mode controller hook: initializes it, runs it, and shuts it down.
     */
    public void invokeHook(ModeControllerHook hook, MapFeedback controller) {
        try {
            hook.setController(controller);
            hook.startupMapHook();
            hook.shutdownMapHook();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
