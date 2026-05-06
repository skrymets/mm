package freemind.diagram.persistence;

/** Thrown when an envelope file declares a {@code formatVersion} the reader doesn't support. */
public class UnsupportedFormatVersionException extends RuntimeException {

    private final int formatVersion;

    public UnsupportedFormatVersionException(int formatVersion) {
        super("Envelope formatVersion " + formatVersion + " is not supported by this reader");
        this.formatVersion = formatVersion;
    }

    public int formatVersion() { return formatVersion; }
}
