package freemind.events;

import freemind.model.MindMap;

public record MapLoadedEvent(MindMap map, String fileName) {}
