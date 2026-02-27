package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IconContainedConditionTest {

    private Controller controller;
    private MindMapNode node;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        node = mock(MindMapNode.class);
        when(node.getStateIcons()).thenReturn(Collections.emptyMap());
    }

    @Test
    void checkNode_returnsTrue_whenNodeHasMatchingIcon() {
        List<MindIcon> icons = List.of(MindIcon.factory("button_ok"), MindIcon.factory("bookmark"));
        when(node.getIcons()).thenReturn(icons);

        IconContainedCondition condition = new IconContainedCondition("bookmark");
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenNodeDoesNotHaveIcon() {
        List<MindIcon> icons = List.of(MindIcon.factory("button_ok"));
        when(node.getIcons()).thenReturn(icons);

        IconContainedCondition condition = new IconContainedCondition("bookmark");
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenNodeHasNoIcons() {
        when(node.getIcons()).thenReturn(Collections.emptyList());

        IconContainedCondition condition = new IconContainedCondition("bookmark");
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_whenStateIconMatches() {
        when(node.getIcons()).thenReturn(Collections.emptyList());
        Map<String, ImageIcon> stateIcons = new HashMap<>();
        stateIcons.put("links", null);
        when(node.getStateIcons()).thenReturn(stateIcons);

        IconContainedCondition condition = new IconContainedCondition("links");
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenStateIconDoesNotMatch() {
        when(node.getIcons()).thenReturn(Collections.emptyList());
        Map<String, ImageIcon> stateIcons = new HashMap<>();
        stateIcons.put("other_icon", null);
        when(node.getStateIcons()).thenReturn(stateIcons);

        IconContainedCondition condition = new IconContainedCondition("links");
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void saveAndLoad_roundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        IconContainedCondition original = new IconContainedCondition("bookmark");
        original.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        assertEquals("icon_contained_condition", saved.getTagName());
        assertEquals("bookmark", saved.getAttribute("icon"));

        Condition loaded = IconContainedCondition.load(saved);
        assertNotNull(loaded);
        assertInstanceOf(IconContainedCondition.class, loaded);

        // Verify loaded condition works correctly
        List<MindIcon> icons = List.of(MindIcon.factory("bookmark"));
        when(node.getIcons()).thenReturn(icons);
        assertTrue(loaded.checkNode(controller, node));
    }
}
