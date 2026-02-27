package freemind.model;

import freemind.extensions.PermanentNodeHook;
import freemind.modes.MapFeedback;
import freemind.modes.MindIcon;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.MindMapNodeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link NodeAdapter} via its concrete subclass {@link MindMapNodeModel}.
 */
class NodeAdapterTest {

    private MindMap mockMap;
    private MapFeedback mockFeedback;
    private MindMapNodeModel node;

    @BeforeEach
    void setUp() {
        mockMap = mock(MindMap.class);
        mockFeedback = mock(MapFeedback.class);
        when(mockMap.getMapFeedback()).thenReturn(mockFeedback);
        when(mockFeedback.getProperty(anyString())).thenReturn("#000000");
        when(mockFeedback.getDefaultFont()).thenReturn(new Font("Dialog", Font.PLAIN, 12));
        when(mockFeedback.getFontThroughMap(any(Font.class))).thenAnswer(inv -> inv.getArgument(0));
        node = new MindMapNodeModel("Test Node", mockMap);
    }

    @Nested
    class TextHandling {

        @Test
        void getText_and_setText() {
            assertEquals("Test Node", node.getText());
            assertEquals("Test Node", node.toString());

            node.setText("Updated");
            assertEquals("Updated", node.getText());
        }

        @Test
        void setText_null_clearsText() {
            node.setText(null);
            assertEquals("", node.getText());
        }

        @Test
        void setUserObject_updatesText() {
            node.setUserObject("Via UserObject");
            assertEquals("Via UserObject", node.getText());
        }

        @Test
        void setText_and_getText_consistent() {
            node.setText("Hello World");
            assertEquals("Hello World", node.getText());
        }
    }

    @Nested
    class NoteHandling {

        @Test
        void noteText_initiallyNull_and_setGet() {
            assertNull(node.getNoteText());
            node.setNoteText("A note");
            assertNotNull(node.getNoteText());
        }

        @Test
        void setNoteText_null_clearsNote() {
            node.setNoteText("Something");
            node.setNoteText(null);
            assertNull(node.getNoteText());
            assertNull(node.getXmlNoteText());
        }
    }

    @Nested
    class TreeStructure {

        @Test
        void newNode_hasNoChildren() {
            assertFalse(node.hasChildren());
            assertEquals(0, node.getChildCount());
            assertTrue(node.isLeaf());
            assertTrue(node.isRoot());
        }

        @Test
        void insert_addsChild_and_setsParent() {
            MindMapNodeModel child = new MindMapNodeModel("Child", mockMap);
            node.insert(child, 0);

            assertTrue(node.hasChildren());
            assertEquals(1, node.getChildCount());
            assertSame(child, node.getChildAt(0));
            assertSame(node, child.getParent());
            assertFalse(child.isRoot());
        }

        @Test
        void insert_negativeIndex_appendsToEnd() {
            MindMapNodeModel child1 = new MindMapNodeModel("C1", mockMap);
            MindMapNodeModel child2 = new MindMapNodeModel("C2", mockMap);
            node.insert(child1, -1);
            node.insert(child2, -1);

            assertEquals(2, node.getChildCount());
            assertSame(child1, node.getChildAt(0));
            assertSame(child2, node.getChildAt(1));
        }

        @Test
        void remove_byIndex_and_byNode() {
            MindMapNodeModel c1 = new MindMapNodeModel("C1", mockMap);
            MindMapNodeModel c2 = new MindMapNodeModel("C2", mockMap);
            node.insert(c1, 0);
            node.insert(c2, 1);

            node.remove(0);
            assertEquals(1, node.getChildCount());
            assertNull(c1.getParent());

            node.remove(c2);
            assertEquals(0, node.getChildCount());
        }

        @Test
        void getChildPosition_and_getChildren() {
            MindMapNodeModel c1 = new MindMapNodeModel("C1", mockMap);
            MindMapNodeModel c2 = new MindMapNodeModel("C2", mockMap);
            node.insert(c1, 0);
            node.insert(c2, 1);

            assertEquals(0, node.getChildPosition(c1));
            assertEquals(1, node.getChildPosition(c2));
            assertEquals(-1, node.getChildPosition(new MindMapNodeModel("X", mockMap)));

            List<MindMapNode> children = node.getChildren();
            assertEquals(2, children.size());
            assertThrows(UnsupportedOperationException.class, () -> children.add(c1));
        }

        @Test
        void isDescendantOf_variousDepths() {
            MindMapNodeModel child = new MindMapNodeModel("Child", mockMap);
            MindMapNodeModel grandchild = new MindMapNodeModel("Grandchild", mockMap);
            node.insert(child, 0);
            child.insert(grandchild, 0);

            assertTrue(child.isDescendantOf(node));
            assertTrue(grandchild.isDescendantOf(node));
            assertFalse(node.isDescendantOf(node));
            assertTrue(node.isDescendantOfOrEqual(node));
        }
    }

    @Nested
    class FoldUnfold {

        @Test
        void fold_unfold_behavior() {
            assertFalse(node.isFolded());

            MindMapNodeModel child = new MindMapNodeModel("C", mockMap);
            node.insert(child, 0);

            node.setFolded(true);
            assertTrue(node.isFolded());
            assertFalse(node.childrenFolded().hasNext());

            node.setFolded(false);
            assertTrue(node.childrenFolded().hasNext());
        }

        @Test
        void hasFoldedStrictDescendant() {
            MindMapNodeModel child = new MindMapNodeModel("C", mockMap);
            node.insert(child, 0);

            assertFalse(node.hasFoldedStrictDescendant());
            child.setFolded(true);
            assertTrue(node.hasFoldedStrictDescendant());
        }
    }

    @Nested
    class Positioning {

        @Test
        void setLeft_and_propagation() {
            MindMapNodeModel child = new MindMapNodeModel("C", mockMap);
            node.insert(child, 0);

            node.setLeft(true);
            assertTrue(node.isLeft());
            assertTrue(child.isLeft());

            node.setLeft(false);
            assertFalse(node.isLeft());
        }
    }

    @Nested
    class Appearance {

        @Test
        void color_and_backgroundColor() {
            node.setColor(Color.RED);
            assertEquals(Color.RED, node.getColor());

            node.setBackgroundColor(Color.BLUE);
            assertEquals(Color.BLUE, node.getBackgroundColor());
        }

        @Test
        void bold_and_italic() {
            assertFalse(node.isBold());
            assertFalse(node.isItalic());

            node.setBold(true);
            assertTrue(node.isBold());

            node.setItalic(true);
            assertTrue(node.isItalic());
        }

        @Test
        void icons_addAndRemove() {
            assertTrue(node.getIcons().isEmpty());

            MindIcon icon1 = MindIcon.factory("button_ok");
            MindIcon icon2 = MindIcon.factory("button_cancel");
            node.addIcon(icon1, MindIcon.LAST);
            node.addIcon(icon2, MindIcon.LAST);

            assertEquals(2, node.getIcons().size());
            int remaining = node.removeIcon(0);
            assertEquals(1, remaining);
        }
    }

    @Nested
    class Attributes {

        @Test
        void attribute_crud() {
            assertEquals(0, node.getAttributeTableLength());
            assertNull(node.getAttribute("anything"));
            assertEquals(-1, node.getAttributePosition("nonexistent"));

            node.addAttribute(new Attribute("key1", "value1"));
            assertEquals(1, node.getAttributeTableLength());
            assertEquals("value1", node.getAttribute("key1"));
            assertEquals(0, node.getAttributePosition("key1"));

            node.setAttribute(0, new Attribute("key1", "value2"));
            assertEquals("value2", node.getAttribute("key1"));

            node.removeAttribute(0);
            assertEquals(0, node.getAttributeTableLength());
        }

        @Test
        void checkAttributePosition_throwsOnInvalid() {
            assertThrows(IllegalArgumentException.class, () -> node.checkAttributePosition(0));
        }
    }

    @Nested
    class Layout {

        @Test
        void vGap_hGap_shiftY() {
            assertEquals(NodeAdapter.VGAP, node.getVGap());
            assertEquals(NodeAdapter.HGAP, node.getHGap());
            assertEquals(0, node.getShiftY());

            node.setVGap(-5);
            assertEquals(0, node.getVGap());
            node.setVGap(10);
            assertEquals(10, node.getVGap());

            node.setHGap(30);
            assertEquals(30, node.getHGap());

            node.setShiftY(15);
            assertEquals(15, node.getShiftY());
        }
    }

    @Nested
    class Hooks {

        @Test
        void addHook_removeHook_lifecycle() {
            assertTrue(node.getHooks().isEmpty());

            PermanentNodeHook hook = mock(PermanentNodeHook.class);
            when(hook.getName()).thenReturn("testHook");
            node.addHook(hook);
            assertEquals(1, node.getHooks().size());

            node.removeHook(hook);
            assertTrue(node.getHooks().isEmpty());
        }

        @Test
        void addHook_null_throws() {
            assertThrows(IllegalArgumentException.class, () -> node.addHook(null));
        }
    }

    @Nested
    class MiscProperties {

        @Test
        void initialDefaults() {
            assertNull(node.getLink());
            assertTrue(node.getAllowsChildren());
            assertNotNull(node.getEdge());
            assertSame(mockMap, node.getMap());
            assertTrue(node.getActivatedHooks().isEmpty());
            assertNotNull(node.getHistoryInformation());
            assertNotNull(node.getHistoryInformation().getCreatedAt());
        }

        @Test
        void toolTip_setAndRemove() {
            node.setToolTip("myKey", "myValue");
            assertEquals("myValue", node.getToolTip().get("myKey"));

            node.setToolTip("myKey", null);
            assertFalse(node.getToolTip().containsKey("myKey"));
        }
    }
}
