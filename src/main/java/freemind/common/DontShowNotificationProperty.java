package freemind.common;

public class DontShowNotificationProperty extends BooleanProperty {
    public DontShowNotificationProperty(String description, String label) {
        super(description, label);
        mTrueValue = "true";
        mFalseValue = "";
    }

}
