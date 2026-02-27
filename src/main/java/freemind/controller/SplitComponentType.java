package freemind.controller;

public enum SplitComponentType {
    NOTE_PANEL(0),
    ATTRIBUTE_PANEL(1);

    private final int mIndex;

    SplitComponentType(int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
