package freemind.modes.mindmapmode.actions.xml;

/**
 * <p>
 * Filter serve for the intersection of commands to be executed.
 * <p>
 * The most useful scenario for these classes is the intersection of the
 * command flow, eg. for collaboration or for storage of the map
 * creation procedure ("map's story").
 */
public interface ActionFilter {
    /**
     * Each filter receives the action pair and its result is taken as the new
     * action pair.
     */
    ActionPair filterAction(ActionPair pair);

    /**
     * called last and *should* not alter the action pair.
     */
    interface FinalActionFilter extends ActionFilter {

    }

    /**
     * called first and *should* not alter the action pair.
     */
    interface FirstActionFilter extends ActionFilter {

    }
}
