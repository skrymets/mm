package freemind.main;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

/**
 * Partial implementation of a Preference node.
 *
 * <p>
 * 22.3.2008: FC: Changed name from AbstractPreferences to the current,
 * Removed all but base64 coding.
 * @since 1.4
 */
@Slf4j
public class Base64Coding {
    /**
     * Helper method for decoding a Base64 string as an byte array. Returns null
     * on encoding error. This method does not allow any other characters
     * present in the string than the 65 special base64 chars.
     */
    public static byte[] decode64(String s) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream(
                (s.length() / 4) * 3);
        char[] c = new char[s.length()];
        s.getChars(0, s.length(), c, 0);

        // Convert from base64 chars
        int endchar = -1;
        for (int j = 0; j < c.length && endchar == -1; j++) {
            if (c[j] >= 'A' && c[j] <= 'Z') {
                c[j] -= 'A';
            } else if (c[j] >= 'a' && c[j] <= 'z') {
                c[j] = (char) (c[j] + 26 - 'a');
            } else if (c[j] >= '0' && c[j] <= '9') {
                c[j] = (char) (c[j] + 52 - '0');
            } else if (c[j] == '+') {
                c[j] = 62;
            } else if (c[j] == '/') {
                c[j] = 63;
            } else if (c[j] == '=') {
                endchar = j;
            } else {
                log.error("Found illegal character in base64 coding: '{}'", c[j]);
                return null; // encoding exception
            }
        }

        int remaining = endchar == -1 ? c.length : endchar;
        int i = 0;
        while (remaining > 0) {
            // Four input chars (6 bits) are decoded as three bytes as
            // 000000 001111 111122 222222

            byte b0 = (byte) (c[i] << 2);
            if (remaining >= 2) {
                b0 += (c[i + 1] & 0x30) >> 4;
            }
            bs.write(b0);

            if (remaining >= 3) {
                byte b1 = (byte) ((c[i + 1] & 0x0F) << 4);
                b1 += (byte) ((c[i + 2] & 0x3C) >> 2);
                bs.write(b1);
            }

            if (remaining >= 4) {
                byte b2 = (byte) ((c[i + 2] & 0x03) << 6);
                b2 += c[i + 3];
                bs.write(b2);
            }

            i += 4;
            remaining -= 4;
        }

        return bs.toByteArray();
    }

    /**
     * Helper method for encoding an array of bytes as a Base64 String.
     */
    public static String encode64(byte[] b) {
        StringBuilder sb = new StringBuilder((b.length / 3) * 4);

        int i = 0;
        int remaining = b.length;
        char[] c = new char[4];
        while (remaining > 0) {
            // Three input bytes are encoded as four chars (6 bits) as
            // 00000011 11112222 22333333

            c[0] = (char) ((b[i] & 0xFC) >> 2);
            c[1] = (char) ((b[i] & 0x03) << 4);
            if (remaining >= 2) {
                c[1] += (char) ((b[i + 1] & 0xF0) >> 4);
                c[2] = (char) ((b[i + 1] & 0x0F) << 2);
                if (remaining >= 3) {
                    c[2] += (char) ((b[i + 2] & 0xC0) >> 6);
                    c[3] = (char) (b[i + 2] & 0x3F);
                } else {
                    c[3] = 64;
                }
            } else {
                c[2] = 64;
                c[3] = 64;
            }

            // Convert to base64 chars
            for (int j = 0; j < 4; j++) {
                if (c[j] < 26) {
                    c[j] += 'A';
                } else if (c[j] < 52) {
                    c[j] = (char) (c[j] - 26 + 'a');
                } else if (c[j] < 62) {
                    c[j] = (char) (c[j] - 52 + '0');
                } else if (c[j] == 62) {
                    c[j] = '+';
                } else if (c[j] == 63) {
                    c[j] = '/';
                } else {
                    c[j] = '=';
                }
            }

            sb.append(c);
            i += 3;
            remaining -= 3;
        }

        return sb.toString();
    }

}
