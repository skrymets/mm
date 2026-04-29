package freemind.main;

public final class MessageTools {

    private MessageTools() {
    }

    /**
     * Example: expandPlaceholders("Hello $1.","Dolly"); => "Hello Dolly."
     */
    public static String expandPlaceholders(String message, String s1) {
        String result = message;
        if (s1 != null) {
            s1 = s1.replaceAll("\\\\", "\\\\\\\\"); // Replace \ with \\
            result = result.replaceAll("\\$1", s1);
        }
        return result;
    }

    public static String expandPlaceholders(String message, String s1, String s2) {
        String result = message;
        if (s1 != null) {
            result = result.replaceAll("\\$1", s1);
        }
        if (s2 != null) {
            result = result.replaceAll("\\$2", s2);
        }
        return result;
    }

    public static String expandPlaceholders(String message, String s1, String s2, String s3) {
        String result = expandPlaceholders(message, s1, s2);
        if (s3 != null) {
            result = result.replaceAll("\\$3", s3);
        }
        return result;
    }

    /**
     * Removes the "TranslateMe" sign from the end of not translated texts.
     */
    public static String removeTranslateComment(String inputString) {
        if (inputString != null
                && inputString.endsWith(FreeMindCommon.POSTFIX_TRANSLATE_ME)) {
            // remove POSTFIX_TRANSLATE_ME:
            inputString = inputString.substring(0, inputString.length()
                    - FreeMindCommon.POSTFIX_TRANSLATE_ME.length());
        }
        return inputString;
    }

    public static String compareText(String pText1, String pText2) {
        if (pText1 == null || pText2 == null) {
            return "One of the Strings is null " + pText1 + ", " + pText2;
        }
        var b = new StringBuilder();
        if (pText1.length() > pText2.length()) {
            b.append("First string is longer :").append(pText1.substring(pText2.length())).append("\n");
        }
        if (pText1.length() < pText2.length()) {
            b.append("Second string is longer :").append(pText2.substring(pText1.length())).append("\n");
        }
        for (int i = 0; i < Math.min(pText1.length(), pText2.length()); i++) {
            if (pText1.charAt(i) != pText2.charAt(i)) {
                b.append("Difference at ").append(i).append(": ").append(pText1.charAt(i)).append("!=").append(pText2.charAt(i)).append("\n");
            }
        }
        return b.toString();
    }
}
