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
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter;
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FirstActionFilter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * Manages the actors and filters for xml transactions inside FreeMind.
 *
 * @author foltin
 */
@Log4j2
public class ActionRegistry {

    /**
     * This Vector denotes all handler of the action to be called for each
     * action.
     */
    private Vector<ActionHandler> registeredHandler;
    /**
     * This set denotes all filters for XmlActions.
     */
    private Vector<ActionFilter> registeredFilters;
    /**
     * HashMap of Action class -> actor instance.
     */
    private HashMap<Class<?>, ActorXml> registeredActors;
    private UndoActionHandler undoActionHandler;

    /**
     *
     */
    public ActionRegistry() {
        super();
        registeredHandler = new Vector<ActionHandler>();
        registeredFilters = new Vector<ActionFilter>();
        registeredActors = new HashMap<Class<?>, ActorXml>();
    }

    /**
     * The handler is put in front. Thus it is called before others are called.
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
                registeredFilters.insertElementAt(newFilter,
                        registeredFilters.size());
            } else if (newFilter instanceof FirstActionFilter) {
                /* Insert as the first one here. */
                registeredFilters.insertElementAt(newFilter, 0);
            } else {
                /* Insert before FinalActionFilters */
                int index = 0;
                for (Iterator<ActionFilter> it = registeredFilters.iterator(); it.hasNext(); ) {
                    ActionFilter filter = it.next();
                    if (filter instanceof FinalActionFilter) {
                        break;
                    }
                    index++;
                }
                registeredFilters.insertElementAt(newFilter, index);
            }
        }
        // int count = 0;
        // for (Iterator it = registeredFilters.iterator(); it.hasNext();) {
        // ActionFilter filter = (ActionFilter) it.next();
        // log.info("Filter " + count + ": " + filter.getClass().getName());
        // count++;
        // }
    }

    public void deregisterFilter(ActionFilter newFilter) {
        registeredFilters.remove(newFilter);
    }

    private void startTransaction(String name) {
        for (Iterator<ActionHandler> i = registeredHandler.iterator(); i.hasNext(); ) {
            ActionHandler handler = i.next();
            handler.startTransaction(name);
        }
    }

    private void endTransaction(String name) {
        for (Iterator<ActionHandler> i = registeredHandler.iterator(); i.hasNext(); ) {
            ActionHandler handler = i.next();
            handler.endTransaction(name);
        }
    }

    /**
     * @return see {@link #executeAction(ActionPair)}
     */
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
                freemind.main.Resources.getInstance().logException(e);
                returnValue = false;
            }
        }

        ActionPair filteredPair = pair;
        // first filter:
        for (Iterator<ActionFilter> i = registeredFilters.iterator(); i.hasNext(); ) {
            ActionFilter filter = i.next();
            filteredPair = filter.filterAction(filteredPair);
        }

        Object[] aArray = registeredHandler.toArray();
        for (int i = 0; i < aArray.length; i++) {
            ActionHandler handler = (ActionHandler) aArray[i];
            try {
                handler.executeAction(filteredPair.getDoAction());
            } catch (Exception e) {
                freemind.main.Resources.getInstance().logException(e);
                returnValue = false;
                // to break or not to break. this is the question here...
            }
        }
        return returnValue;
    }

    /**
     *
     */
    public void registerActor(ActorXml actor, Class<?> action) {
        registeredActors.put(action, actor);
    }

    /**
     *
     */
    public void deregisterActor(Class<?> action) {
        registeredActors.remove(action);
    }

    public ActorXml getActor(XmlAction action) {
        for (Iterator<Class<?>> i = registeredActors.keySet().iterator(); i.hasNext(); ) {
            Class<?> actorClass = i.next();
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
