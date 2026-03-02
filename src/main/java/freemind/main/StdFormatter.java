package freemind.main;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

@SuppressWarnings("serial")
class StdFormatter extends SimpleFormatter {

    private static class StdOutErrLevel extends Level {
        public StdOutErrLevel(String name, int value) {
            super(name, value);
        }
    }

    /**
     * Level for STDOUT activity.
     */
    final static Level STDOUT = new StdOutErrLevel("STDOUT",
            Level.WARNING.intValue() + 53);

    /**
     * Level for STDERR activity
     */
    final static Level STDERR = new StdOutErrLevel("STDERR",
            Level.SEVERE.intValue() + 53);

    // Line separator string. This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private final String lineSeparator = System.getProperty("line.separator");

    /**
     * Format the given LogRecord.
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
        if (!STDERR.getName().equals(record.getLoggerName())
                && !STDOUT.getName().equals(record.getLoggerName())) {
            return super.format(record);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(lineSeparator);
        String message = formatMessage(record);
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");
        sb.append(message);
        return sb.toString();
    }
}
