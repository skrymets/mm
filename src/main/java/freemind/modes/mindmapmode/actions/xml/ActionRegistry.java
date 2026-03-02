package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.XmlAction;
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter;
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FirstActionFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages the actors and filters for xml transactions inside FreeMind.
 */
@Slf4j
public class ActionRegistry {

    /**
     * This Vector denotes all handler of the action to be called for each
     * action.
     */
    private final List<ActionHandler> registeredHandler = new ArrayList<>();
    /**
     * This set denotes all filters for XmlActions.
     */
    private final List<ActionFilter> registeredFilters = new ArrayList<>();
    /**
     * HashMap of Action class -> actor instance.
     */
    private final HashMap<Class<?>, ActorXml> registeredActors = new HashMap<>();
    private UndoActionHandler undoActionHandler;

    public ActionRegistry() {
        // No special action is required
    }

    /**
     * The handler is put in front. Thus, it is called before others are called.
     */
    public void registerHandler(ActionHandler newHandler) {
        // if it is present, put it in front:
        if (!registeredHandler.contains(newHandler)) {
            registeredHandler.remove(newHandler);
        }
        registeredHandler.add(0, newHandler);
    }

    public void deregisterHandler(ActionHandler newHandler) {
        registeredHandler.remove(newHandler);
    }

    public void registerFilter(ActionFilter newFilter) {
        if (!registeredFilters.contains(newFilter)) {
            if (newFilter instanceof FinalActionFilter) {
                /* Insert as the last one here. */
                registeredFilters.add(newFilter);
            } else if (newFilter instanceof FirstActionFilter) {
                /* Insert as the first one here. */
                registeredFilters.add(0, newFilter);
            } else {
                /* Insert before FinalActionFilters */
                int index = 0;
                for (ActionFilter filter : registeredFilters) {
                    if (filter instanceof FinalActionFilter) {
                        break;
                    }
                    index++;
                }
                registeredFilters.add(index, newFilter);
            }
        }
    }

    public void deregisterFilter(ActionFilter newFilter) {
        registeredFilters.remove(newFilter);
    }

    private void startTransaction(String name) {
        for (ActionHandler handler : registeredHandler) {
            handler.startTransaction(name);
        }
    }

    private void endTransaction(String name) {
        for (ActionHandler handler : registeredHandler) {
            handler.endTransaction(name);
        }
    }

    public boolean doTransaction(String pName, ActionPair pPair) {
        this.startTransaction(pName);
        boolean result = this.executeAction(pPair);
        this.endTransaction(pName);
        return result;
    }

    /**
     * @return the success of the action. If an exception arises, the method
     * returns false.
     */
    private boolean executeAction(ActionPair pair) {
        if (pair == null)
            return false;
        boolean returnValue = true;
        // register for undo first, as the filter things are repeated when the
        // undo is executed as well!
        if (undoActionHandler != null) {
            try {
                undoActionHandler.executeAction(pair);
            } catch (Exception e) {
                log.error(e.toString());
                returnValue = false;
            }
        }

        ActionPair filteredPair = pair;
        // first filter:
        for (ActionFilter filter : registeredFilters) {
            filteredPair = filter.filterAction(filteredPair);
        }

        Object[] aArray = registeredHandler.toArray();
        for (Object o : aArray) {
            ActionHandler handler = (ActionHandler) o;
            try {
                handler.executeAction(filteredPair.getDoAction());
            } catch (Exception e) {
                log.error(e.toString());
                returnValue = false;
                // to break or not to break. this is the question here...
            }
        }
        return returnValue;
    }

    public void registerActor(ActorXml actor, Class<?> action) {
        registeredActors.put(action, actor);
    }

    public void deregisterActor(Class<?> action) {
        registeredActors.remove(action);
    }

    public ActorXml getActor(XmlAction action) {
        for (Class<?> actorClass : registeredActors.keySet()) {
            if (actorClass.isInstance(action)) {
                return registeredActors.get(actorClass);
            }
        }
        // Class actionClass = action.getClass();
        // if(registeredActors.containsKey(actionClass)) {
        // return (ActorXml) registeredActors.get(actionClass);
        // }
        throw new IllegalArgumentException("No actor present for xmlaction"
                + action.getClass());
    }

    public ActorXml getActor(Class<?> actionClass) {
        if (registeredActors.containsKey(actionClass)) {
            return registeredActors.get(actionClass);
        }
        throw new IllegalArgumentException("No actor present for xmlaction"
                + actionClass);
    }

    public void registerUndoHandler(UndoActionHandler undoActionHandler) {
        this.undoActionHandler = undoActionHandler;
    }
}
