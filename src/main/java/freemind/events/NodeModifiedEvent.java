package freemind.events;

import freemind.model.MindMapNode;

public record NodeModifiedEvent(MindMapNode node, String property, Object oldValue, Object newValue) {}
