package freemind.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {

    @Nested
    class ColorConversion {

        @Test
        void colorToXml_standardColor() {
            assertEquals("#ff0000", Tools.colorToXml(Color.RED));
            assertEquals("#00ff00", Tools.colorToXml(Color.GREEN));
            assertEquals("#0000ff", Tools.colorToXml(Color.BLUE));
        }

        @Test
        void colorToXml_black() {
            assertEquals("#000000", Tools.colorToXml(Color.BLACK));
        }

        @Test
        void colorToXml_white() {
            assertEquals("#ffffff", Tools.colorToXml(Color.WHITE));
        }

        @Test
        void colorToXml_null_returnsNull() {
            assertNull(Tools.colorToXml(null));
        }

        @Test
        void colorToXml_customColor() {
            assertEquals("#0a1b2c", Tools.colorToXml(new Color(10, 27, 44)));
        }

        @Test
        void xmlToColor_standardColors() {
            assertEquals(Color.RED, Tools.xmlToColor("#ff0000"));
            assertEquals(Color.GREEN, Tools.xmlToColor("#00ff00"));
            assertEquals(Color.BLUE, Tools.xmlToColor("#0000ff"));
        }

        @Test
        void xmlToColor_caseInsensitive() {
            assertEquals(Color.RED, Tools.xmlToColor("#FF0000"));
        }

        @Test
        void xmlToColor_invalidFormat_throws() {
            assertThrows(IllegalArgumentException.class, () -> Tools.xmlToColor("invalid"));
            assertThrows(IllegalArgumentException.class, () -> Tools.xmlToColor("#fff"));
        }

        @Test
        void colorRoundtrip() {
            Color original = new Color(128, 64, 255);
            Color roundtripped = Tools.xmlToColor(Tools.colorToXml(original));
            assertEquals(original, roundtripped);
        }
    }

    @Nested
    class PointConversion {

        @Test
        void pointToXml_basic() {
            String xml = Tools.PointToXml(new Point(10, 20));
            assertEquals("10;20", xml);
        }

        @Test
        void pointToXml_negative() {
            String xml = Tools.PointToXml(new Point(-5, -10));
            assertEquals("-5;-10", xml);
        }

        @Test
        void pointToXml_null_returnsNull() {
            assertNull(Tools.PointToXml(null));
        }

        @Test
        void xmlToPoint_basic() {
            Point p = Tools.xmlToPoint("10;20");
            assertEquals(10, p.x);
            assertEquals(20, p.y);
        }

        @Test
        void xmlToPoint_negative() {
            Point p = Tools.xmlToPoint("-5;-10");
            assertEquals(-5, p.x);
            assertEquals(-10, p.y);
        }

        @Test
        void xmlToPoint_null_returnsNull() {
            assertNull(Tools.xmlToPoint(null));
        }

        @Test
        void xmlToPoint_legacyFormat() {
            Point p = Tools.xmlToPoint("java.awt.Point[x=100,y=200]");
            assertEquals(100, p.x);
            assertEquals(200, p.y);
        }

        @Test
        void xmlToPoint_wrongNumberOfElements_throws() {
            assertThrows(IllegalArgumentException.class, () -> Tools.xmlToPoint("1;2;3"));
        }

        @Test
        void pointRoundtrip() {
            Point original = new Point(42, -7);
            Point roundtripped = Tools.xmlToPoint(Tools.PointToXml(original));
            assertEquals(original, roundtripped);
        }
    }

    @Nested
    class BooleanConversion {

        @Test
        void booleanToXml() {
            assertEquals("true", Tools.BooleanToXml(true));
            assertEquals("false", Tools.BooleanToXml(false));
        }

        @Test
        void xmlToBoolean() {
            assertTrue(Tools.xmlToBoolean("true"));
            assertFalse(Tools.xmlToBoolean("false"));
            assertFalse(Tools.xmlToBoolean(null));
            assertFalse(Tools.xmlToBoolean(""));
            assertFalse(Tools.xmlToBoolean("TRUE"));
        }

        @Test
        void booleanRoundtrip() {
            assertTrue(Tools.xmlToBoolean(Tools.BooleanToXml(true)));
            assertFalse(Tools.xmlToBoolean(Tools.BooleanToXml(false)));
        }
    }

    @Nested
    class StringListConversion {

        @Test
        void stringToList_basic() {
            List<String> result = Tools.stringToList("a;b;c");
            assertEquals(Arrays.asList("a", "b", "c"), result);
        }

        @Test
        void stringToList_single() {
            List<String> result = Tools.stringToList("hello");
            assertEquals(Collections.singletonList("hello"), result);
        }

        @Test
        void listToString_basic() {
            assertEquals("a;b;c", Tools.listToString(Arrays.asList("a", "b", "c")));
        }

        @Test
        void listToString_empty() {
            assertEquals("", Tools.listToString(Collections.emptyList()));
        }

        @Test
        void listToString_null() {
            assertEquals("", Tools.listToString(null));
        }

        @Test
        void stringListRoundtrip() {
            List<String> original = Arrays.asList("one", "two", "three");
            List<String> roundtripped = Tools.stringToList(Tools.listToString(original));
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
                ".hidden, ''"
        })
        void getExtension(String input, String expected) {
            assertEquals(expected, Tools.getExtension(input));
        }

        @ParameterizedTest
        @CsvSource({
                "file.txt, file",
                "archive.tar.gz, archive.tar",
                "noext, ''"
        })
        void removeExtension(String input, String expected) {
            assertEquals(expected, Tools.removeExtension(input));
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
            String compressed = Tools.compress(original);
            assertNotNull(compressed);
            assertNotEquals(original, compressed);
            assertEquals(original, Tools.decompress(compressed));
        }

        @Test
        void roundtrip_longText() {
            String original = "A".repeat(10000);
            assertEquals(original, Tools.decompress(Tools.compress(original)));
        }

        @Test
        void roundtrip_unicode() {
            String original = "Привет мир! 你好世界 🌍";
            assertEquals(original, Tools.decompress(Tools.compress(original)));
        }

        @Test
        void roundtrip_empty() {
            assertEquals("", Tools.decompress(Tools.compress("")));
        }
    }

    @Nested
    class Base64 {

        @Test
        void roundtrip_bytes() {
            byte[] original = {0, 1, 2, 127, -128, -1};
            String encoded = Tools.toBase64(original);
            assertArrayEquals(original, Tools.fromBase64(encoded));
        }

        @Test
        void toBase64_string() {
            String encoded = Tools.toBase64("Hello");
            assertNotNull(encoded);
            byte[] decoded = Tools.fromBase64(encoded);
            assertEquals("Hello", new String(decoded));
        }
    }

    @Nested
    class Utf8Conversion {

        @Test
        void byteArrayToUTF8String() {
            byte[] bytes = "Hello".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            assertEquals("Hello", Tools.byteArrayToUTF8String(bytes));
        }

        @Test
        void uTF8StringToByteArray() {
            byte[] result = Tools.uTF8StringToByteArray("Hello");
            assertArrayEquals("Hello".getBytes(java.nio.charset.StandardCharsets.UTF_8), result);
        }

        @Test
        void roundtrip_unicode() {
            String original = "日本語テスト";
            assertEquals(original, Tools.byteArrayToUTF8String(Tools.uTF8StringToByteArray(original)));
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

        @SuppressWarnings("deprecation")
        @Test
        void stringEquals() {
            assertTrue(Tools.safeEquals("a", "a"));
            assertFalse(Tools.safeEquals("a", "b"));
            assertTrue(Tools.safeEquals((String) null, (String) null));
            assertFalse(Tools.safeEquals("a", null));
            assertFalse(Tools.safeEquals(null, "a"));
        }

        @SuppressWarnings("deprecation")
        @Test
        void objectEquals() {
            assertTrue(Tools.safeEquals((Object) "x", (Object) "x"));
            assertTrue(Tools.safeEquals((Object) null, (Object) null));
            assertFalse(Tools.safeEquals((Object) "x", null));
        }

        @SuppressWarnings("deprecation")
        @Test
        void colorEquals() {
            assertTrue(Tools.safeEquals(Color.RED, Color.RED));
            assertTrue(Tools.safeEquals((Color) null, (Color) null));
            assertFalse(Tools.safeEquals(Color.RED, Color.BLUE));
            assertFalse(Tools.safeEquals(Color.RED, null));
        }
    }

    @Nested
    class CountOccurrences {

        @Test
        void basic() {
            assertEquals(3, Tools.countOccurrences("abcabcabc", "abc"));
        }

        @Test
        void notFound() {
            assertEquals(0, Tools.countOccurrences("hello", "xyz"));
        }

        @Test
        void singleChar() {
            assertEquals(3, Tools.countOccurrences("aaa", "a"));
        }

        @Test
        void overlapping_countsNonOverlapping() {
            assertEquals(2, Tools.countOccurrences("aaaa", "aa"));
        }
    }

    @Nested
    class ExpandFileName {

        @Test
        void tildeExpansion() {
            String result = Tools.expandFileName("~/documents");
            assertFalse(result.startsWith("~"));
            assertTrue(result.endsWith("documents"));
        }

        @Test
        void noTilde_unchanged() {
            assertEquals("/some/path", Tools.expandFileName("/some/path"));
        }
    }

    @Nested
    class RestorableStringParsing {

        @Test
        void getFileNameFromRestorable() {
            String result = Tools.getFileNameFromRestorable("MindMap:C:/path/to/file.mm");
            assertEquals("C:/path/to/file.mm", result);
        }

        @Test
        void getModeFromRestorable() {
            String result = Tools.getModeFromRestorable("MindMap:C:/path/to/file.mm");
            assertEquals("MindMap", result);
        }

        @Test
        void getModeFromRestorable_noToken() {
            assertNull(Tools.getModeFromRestorable(""));
        }
    }

    @Nested
    class RemoveMnemonic {

        @Test
        void removesAmpersand() {
            assertEquals("File", Tools.removeMnemonic("&File"));
        }

        @Test
        void removesMiddleAmpersand() {
            assertEquals("Save As", Tools.removeMnemonic("Save &As"));
        }

        @Test
        void noAmpersand_unchanged() {
            assertEquals("Exit", Tools.removeMnemonic("Exit"));
        }
    }

    @Nested
    class PlatformDetection {

        @Test
        void isWindows_returnsBoolean() {
            // Just verify it runs without error and returns consistent result
            boolean result = Tools.isWindows();
            assertEquals(System.getProperty("os.name").startsWith("Win"), result);
        }
    }

    @Nested
    class DesEncrypter {

        @Test
        void encryptDecryptRoundtrip() {
            Tools.SingleDesEncrypter encrypter = new Tools.SingleDesEncrypter(new StringBuffer("password123"));
            String original = "Hello, encrypted world!";
            String encrypted = encrypter.encrypt(original);
            assertNotNull(encrypted);
            assertNotEquals(original, encrypted);

            Tools.SingleDesEncrypter decrypter = new Tools.SingleDesEncrypter(new StringBuffer("password123"));
            assertEquals(original, decrypter.decrypt(encrypted));
        }

        @Test
        void decryptNull_returnsNull() {
            Tools.SingleDesEncrypter encrypter = new Tools.SingleDesEncrypter(new StringBuffer("pass"));
            assertNull(encrypter.decrypt(null));
        }
    }
}
