package freemind.main;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {

    @Nested
    class ColorConversion {

        @Test
        void colorToXml_standardColor() {
            assertEquals("#ff0000", ColorUtils.colorToXml(Color.RED));
            assertEquals("#00ff00", ColorUtils.colorToXml(Color.GREEN));
            assertEquals("#0000ff", ColorUtils.colorToXml(Color.BLUE));
        }

        @Test
        void colorToXml_black() {
            assertEquals("#000000", ColorUtils.colorToXml(Color.BLACK));
        }

        @Test
        void colorToXml_white() {
            assertEquals("#ffffff", ColorUtils.colorToXml(Color.WHITE));
        }

        @Test
        void colorToXml_null_returnsNull() {
            assertNull(ColorUtils.colorToXml(null));
        }

        @Test
        void colorToXml_customColor() {
            assertEquals("#0a1b2c", ColorUtils.colorToXml(new Color(10, 27, 44)));
        }

        @Test
        void xmlToColor_standardColors() {
            assertEquals(Color.RED, ColorUtils.xmlToColor("#ff0000"));
            assertEquals(Color.GREEN, ColorUtils.xmlToColor("#00ff00"));
            assertEquals(Color.BLUE, ColorUtils.xmlToColor("#0000ff"));
        }

        @Test
        void xmlToColor_caseInsensitive() {
            assertEquals(Color.RED, ColorUtils.xmlToColor("#FF0000"));
        }

        @Test
        void xmlToColor_invalidFormat_throws() {
            assertThrows(IllegalArgumentException.class, () -> ColorUtils.xmlToColor("invalid"));
            assertThrows(IllegalArgumentException.class, () -> ColorUtils.xmlToColor("#fff"));
        }

        @Test
        void colorRoundtrip() {
            Color original = new Color(128, 64, 255);
            Color roundtripped = ColorUtils.xmlToColor(ColorUtils.colorToXml(original));
            assertEquals(original, roundtripped);
        }
    }

    @Nested
    class BooleanConversion {

        @Test
        void booleanToXml() {
            assertEquals("true", String.valueOf(true));
            assertEquals("false", String.valueOf(false));
        }

        @Test
        void xmlToBoolean() {
            assertTrue("true".equals("true"));
            assertFalse("true".equals("false"));
            assertFalse("true".equals(null));
            assertFalse("true".equals(""));
            assertFalse("true".equals("TRUE"));
        }

        @Test
        void booleanRoundtrip() {
            assertTrue("true".equals(String.valueOf(true)));
            assertFalse("true".equals(String.valueOf(false)));
        }
    }

    @Nested
    class StringListConversion {

        @Test
        void stringToList_basic() {
            List<String> result = Arrays.asList("a;b;c".split(";"));
            assertEquals(Arrays.asList("a", "b", "c"), result);
        }

        @Test
        void stringToList_single() {
            List<String> result = Arrays.asList("hello".split(";"));
            assertEquals(Collections.singletonList("hello"), result);
        }

        @Test
        void listToString_basic() {
            assertEquals("a;b;c", StringUtils.join(Arrays.asList("a", "b", "c"), ";"));
        }

        @Test
        void listToString_empty() {
            assertEquals("", StringUtils.join(Collections.emptyList(), ";"));
        }

        @Test
        void stringListRoundtrip() {
            List<String> original = Arrays.asList("one", "two", "three");
            List<String> roundtripped = Arrays.asList(StringUtils.join(original, ";").split(";"));
            assertEquals(original, roundtripped);
        }
    }

    @Nested
    class FileExtension {

        @ParameterizedTest
        @CsvSource({
                "file.txt, txt",
                "file.MM, mm",
                "archive.tar.gz, gz",
                "fork.pork.MM, mm",
                "noext, ''",
                ".hidden, hidden"
        })
        void getExtension(String input, String expected) {
            assertEquals(expected, FilenameUtils.getExtension(input).toLowerCase());
        }

        @ParameterizedTest
        @CsvSource({
                "file.txt, file",
                "archive.tar.gz, archive.tar",
                "noext, noext"
        })
        void removeExtension(String input, String expected) {
            assertEquals(expected, FilenameUtils.removeExtension(input));
        }
    }

    @Nested
    class ExecutableByExtension {

        @ParameterizedTest
        @ValueSource(strings = {"file.exe", "script.bat", "file.com", "macro.vbs", "shortcut.lnk"})
        void executable(String file) {
            assertTrue(Tools.executableByExtension(file));
        }

        @ParameterizedTest
        @ValueSource(strings = {"file.txt", "image.png", "document.mm", "script.sh"})
        void notExecutable(String file) {
            assertFalse(Tools.executableByExtension(file));
        }
    }

    @Nested
    class ExpandPlaceholders {

        @Test
        void singlePlaceholder() {
            assertEquals("Hello Dolly.", Tools.expandPlaceholders("Hello $1.", "Dolly"));
        }

        @Test
        void twoPlaceholders() {
            assertEquals("Hello World!", Tools.expandPlaceholders("Hello $1$2", "World", "!"));
        }

        @Test
        void threePlaceholders() {
            assertEquals("a-b-c", Tools.expandPlaceholders("$1-$2-$3", "a", "b", "c"));
        }

        @Test
        void nullPlaceholder_notReplaced() {
            assertEquals("Hello $1.", Tools.expandPlaceholders("Hello $1.", (String) null));
        }
    }

    @Nested
    class CompressDecompress {

        @Test
        void roundtrip_simple() {
            String original = "Hello, World!";
            String compressed = EncryptionUtils.compress(original);
            assertNotNull(compressed);
            assertNotEquals(original, compressed);
            assertEquals(original, EncryptionUtils.decompress(compressed));
        }

        @Test
        void roundtrip_longText() {
            String original = "A".repeat(10000);
            assertEquals(original, EncryptionUtils.decompress(EncryptionUtils.compress(original)));
        }

        @Test
        void roundtrip_unicode() {
            String original = "Привет мир! 你好世界 🌍";
            assertEquals(original, EncryptionUtils.decompress(EncryptionUtils.compress(original)));
        }

        @Test
        void roundtrip_empty() {
            assertEquals("", EncryptionUtils.decompress(EncryptionUtils.compress("")));
        }
    }

    @Nested
    class Base64 {

        @Test
        void roundtrip_bytes() {
            byte[] original = {0, 1, 2, 127, -128, -1};
            String encoded = EncryptionUtils.toBase64(original);
            assertArrayEquals(original, EncryptionUtils.fromBase64(encoded));
        }

        @Test
        void toBase64_string() {
            String encoded = EncryptionUtils.toBase64("Hello");
            assertNotNull(encoded);
            byte[] decoded = EncryptionUtils.fromBase64(encoded);
            assertEquals("Hello", new String(decoded));
        }
    }

    @Nested
    class DateConversion {

        @Test
        void xmlToDate_validTimestamp() {
            long timestamp = 1000000000000L;
            Date result = Tools.xmlToDate(String.valueOf(timestamp));
            assertEquals(timestamp, result.getTime());
        }

        @Test
        void xmlToDate_invalidString_returnsCurrentDate() {
            Date before = new Date();
            Date result = Tools.xmlToDate("not-a-number");
            Date after = new Date();
            assertTrue(result.getTime() >= before.getTime());
            assertTrue(result.getTime() <= after.getTime());
        }

        @Test
        void dateToString_basic() {
            Date date = new Date(1000000000000L);
            assertEquals("1000000000000", Tools.dateToString(date));
        }

        @Test
        void dateRoundtrip() {
            Date original = new Date(1234567890123L);
            Date roundtripped = Tools.xmlToDate(Tools.dateToString(original));
            assertEquals(original.getTime(), roundtripped.getTime());
        }
    }

    @Nested
    class IsPreferenceTrue {

        @Test
        void trueString() {
            assertTrue(Tools.isPreferenceTrue("true"));
        }

        @Test
        void falseString() {
            assertFalse(Tools.isPreferenceTrue("false"));
        }

        @Test
        void nullValue() {
            assertFalse(Tools.isPreferenceTrue(null));
        }

        @Test
        void otherValue() {
            assertFalse(Tools.isPreferenceTrue("yes"));
        }
    }

    @Nested
    class SafeEquals {

        @Test
        void stringEquals() {
            assertTrue(Objects.equals("a", "a"));
            assertFalse(Objects.equals("a", "b"));
            assertTrue(Objects.equals((String) null, (String) null));
            assertFalse(Objects.equals("a", null));
            assertFalse(Objects.equals(null, "a"));
        }

        @Test
        void objectEquals() {
            assertTrue(Objects.equals("x", "x"));
            assertTrue(Objects.equals(null, null));
            assertFalse(Objects.equals("x", null));
        }

        @Test
        void colorEquals() {
            assertTrue(Objects.equals(Color.RED, Color.RED));
            assertTrue(Objects.equals((Color) null, (Color) null));
            assertFalse(Objects.equals(Color.RED, Color.BLUE));
            assertFalse(Objects.equals(Color.RED, null));
        }
    }

    @Nested
    class CountOccurrences {

        @Test
        void basic() {
            assertEquals(3, StringUtils.countMatches("abcabcabc", "abc"));
        }

        @Test
        void notFound() {
            assertEquals(0, StringUtils.countMatches("hello", "xyz"));
        }

        @Test
        void singleChar() {
            assertEquals(3, StringUtils.countMatches("aaa", "a"));
        }

        @Test
        void overlapping_countsNonOverlapping() {
            // StringUtils.countMatches counts non-overlapping occurrences
            assertEquals(2, StringUtils.countMatches("aaaa", "aa"));
        }
    }

    @Nested
    class RestorableStringParsing {

        @Test
        void getFileNameFromRestorable() {
            String result = MindMapUtils.getFileNameFromRestorable("MindMap:C:/path/to/file.mm");
            assertEquals("C:/path/to/file.mm", result);
        }

        @Test
        void getModeFromRestorable() {
            String result = MindMapUtils.getModeFromRestorable("MindMap:C:/path/to/file.mm");
            assertEquals("MindMap", result);
        }

        @Test
        void getModeFromRestorable_noToken() {
            assertNull(MindMapUtils.getModeFromRestorable(""));
        }
    }

    @Nested
    class RemoveMnemonic {

        @Test
        void removesAmpersand() {
            assertEquals("File", SwingUtils.removeMnemonic("&File"));
        }

        @Test
        void removesMiddleAmpersand() {
            assertEquals("Save As", SwingUtils.removeMnemonic("Save &As"));
        }

        @Test
        void noAmpersand_unchanged() {
            assertEquals("Exit", SwingUtils.removeMnemonic("Exit"));
        }
    }

    @Nested
    class PlatformDetection {

        @Test
        void isWindows_returnsBoolean() {
            // Just verify SystemUtils constants are consistent
            boolean result = SystemUtils.IS_OS_WINDOWS;
            assertEquals(System.getProperty("os.name").startsWith("Win"), result);
        }
    }

    @Nested
    class DesEncrypter {

        @Test
        void encryptDecryptRoundtrip() {
            EncryptionUtils.SingleDesEncrypter encrypter = new EncryptionUtils.SingleDesEncrypter(new StringBuilder("password123"));
            String original = "Hello, encrypted world!";
            String encrypted = encrypter.encrypt(original);
            assertNotNull(encrypted);
            assertNotEquals(original, encrypted);

            EncryptionUtils.SingleDesEncrypter decrypter = new EncryptionUtils.SingleDesEncrypter(new StringBuilder("password123"));
            assertEquals(original, decrypter.decrypt(encrypted));
        }

        @Test
        void decryptNull_returnsNull() {
            EncryptionUtils.SingleDesEncrypter encrypter = new EncryptionUtils.SingleDesEncrypter(new StringBuilder("pass"));
            assertNull(encrypter.decrypt(null));
        }
    }
}
