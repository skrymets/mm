package freemind.modes;

import java.util.EventListener;

public interface NodeViewEventListener extends EventListener {
    void nodeViewCreated(NodeViewEvent nodeViewEvent);

    void nodeViewRemoved(NodeViewEvent nodeViewEvent);

}
