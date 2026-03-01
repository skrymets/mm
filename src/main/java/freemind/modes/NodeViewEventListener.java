package freemind.modes;

import java.util.EventListener;

/**
 * @author Dimitri Polivaev 31.10.2005
 */
public interface NodeViewEventListener extends EventListener {
    void nodeViewCreated(NodeViewEvent nodeViewEvent);

    void nodeViewRemoved(NodeViewEvent nodeViewEvent);

}
