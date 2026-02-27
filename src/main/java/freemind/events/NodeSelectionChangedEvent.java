package freemind.events;

import freemind.model.MindMapNode;

public record NodeSelectionChangedEvent(MindMapNode node, boolean selected) {}
