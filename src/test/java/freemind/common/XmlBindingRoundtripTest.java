package freemind.common;

import freemind.controller.actions.*;
import freemind.main.Tools;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Roundtrip tests for JAXB XML serialization: create object -> marshall to XML -> unmarshall -> verify.
 */
class XmlBindingRoundtripTest {

    @BeforeAll
    static void ensureBindingFactoryInitialized() {
        assertNotNull(XmlBindingTools.getInstance(), "XmlBindingTools singleton must be available");
    }

    @Nested
    class SimpleActions {

        @Test
        void boldNodeAction_roundtrip() {
            BoldNodeAction action = new BoldNodeAction();
            action.setNode("ID_12345");
            action.setBold(true);

            String xml = Tools.marshall(action);
            assertNotNull(xml);
            assertTrue(xml.contains("bold"));

            XmlAction deserialized = Tools.unMarshall(xml);
            assertInstanceOf(BoldNodeAction.class, deserialized);

            BoldNodeAction result = (BoldNodeAction) deserialized;
            assertEquals("ID_12345", result.getNode());
            assertTrue(result.isBold());
        }

        @Test
        void boldNodeAction_false_roundtrip() {
            BoldNodeAction action = new BoldNodeAction();
            action.setNode("ID_99");
            action.setBold(false);

            BoldNodeAction result = (BoldNodeAction) Tools.unMarshall(Tools.marshall(action));
            assertFalse(result.isBold());
            assertEquals("ID_99", result.getNode());
        }

        @Test
        void italicNodeAction_roundtrip() {
            ItalicNodeAction action = new ItalicNodeAction();
            action.setNode("ID_456");
            action.setItalic(true);

            ItalicNodeAction result = (ItalicNodeAction) Tools.unMarshall(Tools.marshall(action));
            assertEquals("ID_456", result.getNode());
            assertTrue(result.isItalic());
        }

        @Test
        void addIconAction_roundtrip() {
            AddIconAction action = new AddIconAction();
            action.setNode("ID_789");
            action.setIconName("button_ok");
            action.setIconPosition(0);

            AddIconAction result = (AddIconAction) Tools.unMarshall(Tools.marshall(action));
            assertEquals("ID_789", result.getNode());
            assertEquals("button_ok", result.getIconName());
            assertEquals(0, result.getIconPosition());
        }

        @Test
        void editNodeAction_roundtrip() {
            EditNodeAction action = new EditNodeAction();
            action.setNode("ID_100");
            action.setText("Hello World");

            EditNodeAction result = (EditNodeAction) Tools.unMarshall(Tools.marshall(action));
            assertEquals("ID_100", result.getNode());
            assertEquals("Hello World", result.getText());
        }

        @Test
        void editNodeAction_withSpecialChars_roundtrip() {
            EditNodeAction action = new EditNodeAction();
            action.setNode("ID_101");
            action.setText("Text with <xml> & \"special\" 'chars'");

            EditNodeAction result = (EditNodeAction) Tools.unMarshall(Tools.marshall(action));
            assertEquals("Text with <xml> & \"special\" 'chars'", result.getText());
        }

        @Test
        void editNodeAction_withUnicode_roundtrip() {
            EditNodeAction action = new EditNodeAction();
            action.setNode("ID_102");
            action.setText("Привет мир 你好世界");

            EditNodeAction result = (EditNodeAction) Tools.unMarshall(Tools.marshall(action));
            assertEquals("Привет мир 你好世界", result.getText());
        }
    }

    @Nested
    class CompoundActions {

        @Test
        void emptyCompoundAction_roundtrip() {
            CompoundAction action = new CompoundAction();
            action.setNode("ID_500");

            CompoundAction result = (CompoundAction) Tools.unMarshall(Tools.marshall(action));
            assertEquals("ID_500", result.getNode());
            // JiBX sets choiceList to null for empty lists (not an empty list)
            assertTrue(result.getChoiceList() == null || result.getChoiceList().isEmpty());
        }

        @Test
        void compoundAction_withSingleChild_roundtrip() {
            CompoundAction compound = new CompoundAction();
            compound.setNode("ID_600");

            BoldNodeAction bold = new BoldNodeAction();
            bold.setNode("ID_601");
            bold.setBold(true);

            CompoundAction.Choice choice = new CompoundAction.Choice();
            choice.setBoldNodeAction(bold);
            compound.addChoice(choice);

            String xml = Tools.marshall(compound);
            assertNotNull(xml);

            CompoundAction result = (CompoundAction) Tools.unMarshall(xml);
            assertEquals("ID_600", result.getNode());
            assertEquals(1, result.sizeChoiceList());

            BoldNodeAction childBold = result.getChoice(0).getBoldNodeAction();
            assertNotNull(childBold);
            assertEquals("ID_601", childBold.getNode());
            assertTrue(childBold.isBold());
        }

        @Test
        void compoundAction_withMultipleChildren_roundtrip() {
            CompoundAction compound = new CompoundAction();
            compound.setNode("ID_700");

            BoldNodeAction bold = new BoldNodeAction();
            bold.setNode("ID_701");
            bold.setBold(true);
            CompoundAction.Choice choice1 = new CompoundAction.Choice();
            choice1.setBoldNodeAction(bold);
            compound.addChoice(choice1);

            ItalicNodeAction italic = new ItalicNodeAction();
            italic.setNode("ID_702");
            italic.setItalic(true);
            CompoundAction.Choice choice2 = new CompoundAction.Choice();
            choice2.setItalicNodeAction(italic);
            compound.addChoice(choice2);

            CompoundAction result = (CompoundAction) Tools.unMarshall(Tools.marshall(compound));
            assertEquals(2, result.sizeChoiceList());
            assertNotNull(result.getChoice(0).getBoldNodeAction());
            assertNotNull(result.getChoice(1).getItalicNodeAction());
        }
    }

    @Nested
    class DeepCopy {

        @Test
        void deepCopy_boldAction() {
            BoldNodeAction original = new BoldNodeAction();
            original.setNode("ID_COPY");
            original.setBold(true);

            XmlAction copy = Tools.deepCopy(original);
            assertInstanceOf(BoldNodeAction.class, copy);
            assertNotSame(original, copy);

            BoldNodeAction copyBold = (BoldNodeAction) copy;
            assertEquals("ID_COPY", copyBold.getNode());
            assertTrue(copyBold.isBold());

            // Verify independence: modifying original doesn't affect copy
            original.setBold(false);
            assertTrue(copyBold.isBold());
        }

        @Test
        void deepCopy_editAction_withText() {
            EditNodeAction original = new EditNodeAction();
            original.setNode("ID_TEXT");
            original.setText("Some content");

            EditNodeAction copy = (EditNodeAction) Tools.deepCopy(original);
            assertEquals("Some content", copy.getText());

            original.setText("Modified");
            assertEquals("Some content", copy.getText());
        }
    }

    @Nested
    class WindowStorage {

        @Test
        void normalWindowConfigurationStorage_roundtrip() {
            NormalWindowConfigurationStorage storage = new NormalWindowConfigurationStorage();
            storage.setX(100);
            storage.setY(200);
            storage.setWidth(800);
            storage.setHeight(600);

            String xml = XmlBindingTools.getInstance().marshall(storage);
            assertNotNull(xml);

            XmlAction deserialized = XmlBindingTools.getInstance().unMarshall(xml);
            assertInstanceOf(NormalWindowConfigurationStorage.class, deserialized);

            NormalWindowConfigurationStorage result = (NormalWindowConfigurationStorage) deserialized;
            assertEquals(100, result.getX());
            assertEquals(200, result.getY());
            assertEquals(800, result.getWidth());
            assertEquals(600, result.getHeight());
        }
    }

    @Nested
    class XmlFormat {

        @Test
        void marshalled_xml_containsExpectedElements() {
            BoldNodeAction action = new BoldNodeAction();
            action.setNode("ID_123");
            action.setBold(true);

            String xml = Tools.marshall(action);
            assertTrue(xml.contains("<?xml"), "Should have XML declaration");
            assertTrue(xml.contains("bold_node_action"), "Should have element name");
            assertTrue(xml.contains("ID_123"), "Should contain node ID");
        }

        @Test
        void marshalled_xml_isWellFormed() {
            EditNodeAction action = new EditNodeAction();
            action.setNode("ID_WF");
            action.setText("test");

            String xml = Tools.marshall(action);
            // If unmarshalling succeeds, the XML is well-formed
            assertNotNull(Tools.unMarshall(xml));
        }
    }
}
