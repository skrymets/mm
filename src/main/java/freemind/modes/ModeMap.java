package freemind.modes;

import freemind.model.MindMap;
import freemind.model.NodeAdapter;

/**
 * Modes-layer view of a MindMap that exposes the modes-side adapter factory.
 * Model code interacts with {@link MindMap}; modes code that needs to create
 * concrete arrow/cloud adapters obtains a {@code ModeMap} reference (typically
 * via cast from a {@code MindMap} returned by a feedback API).
 */
public interface ModeMap extends MindMap {

    CloudAdapter createCloudAdapter(NodeAdapter node);

    ArrowLinkAdapter createArrowLinkAdapter(NodeAdapter source, NodeAdapter target);

    ArrowLinkTarget createArrowLinkTarget(NodeAdapter source, NodeAdapter target);
}
