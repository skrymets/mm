package freemind.main;

import java.time.Instant;

public final class DateTimeTools {

    private DateTimeTools() {
    }

    /**
     * Extracts a long from xml. Only useful for dates.
     */
    public static Instant xmlToInstant(String xmlString) {
        try {
            return Instant.ofEpochMilli(Long.parseLong(xmlString));
        } catch (NumberFormatException e) {
            return Instant.now();
        }
    }

    public static String dateToString(Instant instant) {
        return Long.toString(instant.toEpochMilli());
    }
}
