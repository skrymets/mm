package freemind.common;

public class RemindValueProperty extends ThreeCheckBoxProperty {

    private final TextTranslator mTextTranslator;

    public RemindValueProperty(String pDescription, String pLabel, TextTranslator pTextTranslator) {
        super(pDescription, pLabel);
        mTextTranslator = pTextTranslator;
        mDontTouchValue = "";
    }

    protected void setState(int newState) {
        state = newState;
        String[] strings;
        strings = new String[3]; // {MINUS_IMAGE, PLUS_IMAGE, NO_IMAGE};
        strings[TRUE_VALUE_INT] = mTextTranslator.getText("OptionalDontShowMeAgainDialog.ok").replaceFirst("&", "");
        strings[FALSE_VALUE_INT] = mTextTranslator.getText("OptionalDontShowMeAgainDialog.cancel").replaceFirst("&", "");
        strings[DON_T_TOUCH_VALUE_INT] = mTextTranslator.getText("OptionPanel.ask").replaceFirst("&", "");
        mButton.setText(strings[state]);
    }

}
