package freemind.main;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.split;

@RequiredArgsConstructor
@Getter
public class VersionInformation {

    public enum Type {

        ALPHA("alpha"),
        BETA("beta"),
        RC("rc"),
        RELEASE(""),
        OTHER(null);

        private final String label;

        Type(String label) {
            this.label = label;
        }

        static Type fromLabel(String label) {
            return asList(Type.values())
                    .stream()
                    .filter(type -> defaultString(label).toLowerCase(Locale.ROOT).equals(type.label))
                    .findFirst()
                    .orElse(OTHER);
        }
    }

    private final int mMaj;
    private final int mMid;
    private final int mMin;
    private final Type mType;
    private final int mNum;

    private transient String[] rawInfo;

    /**
     * Sets the version number from a string.
     *
     * @param version The version number coding. Example "0.9.0 Beta 1" Keywords are "Alpha", "Beta", "RC".
     *                Separation by " " or by ".".
     */

    public VersionInformation(String version) {
        rawInfo = split(defaultString(version), "\\. ");

        if (rawInfo.length != 3 && rawInfo.length != 5)
            throw new IllegalArgumentException("Wrong number of tokens for version information: " + version);

        mMaj = parseInt(rawInfo[0]);
        mMid = parseInt(rawInfo[1]);
        mMin = parseInt(rawInfo[2]);

        if (rawInfo.length == 3) {
            // release.
            mType = Type.RELEASE;
            mNum = 0;
            return;
        }
        // here,we have info.length == 5!
        mType = Type.fromLabel(rawInfo[3]);
        mNum = parseInt(rawInfo[4]);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer()
                .append(mMaj).append('.')
                .append(mMid).append('.')
                .append(mMin);
        switch (mType) {
            case ALPHA:
                buf.append(' ').append("Alpha");
                break;
            case BETA:
                buf.append(' ').append("Beta");
                break;
            case RC:
                buf.append(' ').append("RC");
                break;
            case RELEASE:
                break;
            case OTHER:
                buf.append(' ').append(rawInfo[3]);
        }

        if (mType != Type.RELEASE) {
            buf.append(' ').append(mNum);
        }

        return buf.toString();
    }
}
