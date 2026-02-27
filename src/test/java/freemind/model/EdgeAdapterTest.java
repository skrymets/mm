package freemind.model;

import freemind.main.FreeMind;
import freemind.modes.MapFeedback;
import freemind.modes.mindmapmode.MindMapEdgeModel;
import freemind.modes.mindmapmode.MindMapNodeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EdgeAdapter} via its concrete subclass {@link MindMapEdgeModel}.
 */
class EdgeAdapterTest {

    private MindMap mockMap;
    private MapFeedback mockFeedback;
    private MindMapNodeModel rootNode;
    private MindMapEdgeModel edge;

    @BeforeEach
    void setUp() {
        mockMap = mock(MindMap.class);
        mockFeedback = mock(MapFeedback.class);
        when(mockMap.getMapFeedback()).thenReturn(mockFeedback);
        when(mockFeedback.getProperty(anyString())).thenReturn("#000000");
        when(mockFeedback.getProperty(FreeMind.RESOURCES_EDGE_STYLE)).thenReturn(EdgeAdapter.EDGESTYLE_LINEAR);
        when(mockFeedback.getDefaultFont()).thenReturn(new Font("Dialog", Font.PLAIN, 12));

        rootNode = new MindMapNodeModel("Root", mockMap);
        edge = new MindMapEdgeModel(rootNode, mockFeedback);
    }

    @Nested
    class ColorTests {

        @Test
        void color_initiallyNull_setAndGet() {
            assertNull(edge.getRealColor());

            edge.setColor(Color.RED);
            assertEquals(Color.RED, edge.getRealColor());
            assertEquals(Color.RED, edge.getColor());
        }

        @Test
        void getColor_rootFallsBackToStandard_whenNoExplicitColor() {
            Color color = edge.getColor();
            assertNotNull(color);
        }

        @Test
        void getColor_inheritsFromParent_whenNotRoot() {
            MindMapNodeModel child = new MindMapNodeModel("Child", mockMap);
            rootNode.insert(child, 0);

            MindMapEdgeModel rootEdge = new MindMapEdgeModel(rootNode, mockFeedback);
            rootEdge.setColor(Color.GREEN);
            rootNode.setEdge(rootEdge);

            MindMapEdgeModel childEdge = new MindMapEdgeModel(child, mockFeedback);
            child.setEdge(childEdge);

            assertEquals(Color.GREEN, childEdge.getColor());
        }
    }

    @Nested
    class WidthTests {

        @Test
        void width_defaultAndExplicit() {
            assertEquals(EdgeAdapter.WIDTH_PARENT, edge.getRealWidth());
            // Root with WIDTH_PARENT returns WIDTH_THIN
            assertEquals(EdgeAdapter.WIDTH_THIN, edge.getWidth());

            edge.setWidth(5);
            assertEquals(5, edge.getRealWidth());
            assertEquals(5, edge.getWidth());
        }

        @Test
        void getWidth_inheritsFromParent_whenNotRoot() {
            MindMapNodeModel child = new MindMapNodeModel("Child", mockMap);
            rootNode.insert(child, 0);

            MindMapEdgeModel rootEdge = new MindMapEdgeModel(rootNode, mockFeedback);
            rootEdge.setWidth(4);
            rootNode.setEdge(rootEdge);

            MindMapEdgeModel childEdge = new MindMapEdgeModel(child, mockFeedback);
            child.setEdge(childEdge);

            assertEquals(4, childEdge.getWidth());
        }
    }

    @Nested
    class StyleTests {

        @Test
        void style_initiallyNone_setAndGet() {
            assertFalse(edge.hasStyle());

            edge.setStyle(EdgeAdapter.EDGESTYLE_BEZIER);
            assertTrue(edge.hasStyle());
            assertEquals(EdgeAdapter.EDGESTYLE_BEZIER, edge.getStyle());
        }

        @Test
        void getStyle_root_fallsBackToProperty() {
            assertEquals(EdgeAdapter.EDGESTYLE_LINEAR, edge.getStyle());
        }

        @Test
        void getStyle_inheritsFromParent_whenNotRoot() {
            MindMapNodeModel child = new MindMapNodeModel("Child", mockMap);
            rootNode.insert(child, 0);

            MindMapEdgeModel rootEdge = new MindMapEdgeModel(rootNode, mockFeedback);
            rootEdge.setStyle(EdgeAdapter.EDGESTYLE_SHARP_BEZIER);
            rootNode.setEdge(rootEdge);

            MindMapEdgeModel childEdge = new MindMapEdgeModel(child, mockFeedback);
            child.setEdge(childEdge);

            assertEquals(EdgeAdapter.EDGESTYLE_SHARP_BEZIER, childEdge.getStyle());
        }
    }

    @Nested
    class StyleAsInt {

        @Test
        void allStylesMapToCorrectInts() {
            edge.setStyle(EdgeAdapter.EDGESTYLE_LINEAR);
            assertEquals(EdgeAdapter.INT_EDGESTYLE_LINEAR, edge.getStyleAsInt());

            edge.setStyle(EdgeAdapter.EDGESTYLE_BEZIER);
            assertEquals(EdgeAdapter.INT_EDGESTYLE_BEZIER, edge.getStyleAsInt());

            edge.setStyle(EdgeAdapter.EDGESTYLE_SHARP_LINEAR);
            assertEquals(EdgeAdapter.INT_EDGESTYLE_SHARP_LINEAR, edge.getStyleAsInt());

            edge.setStyle(EdgeAdapter.EDGESTYLE_SHARP_BEZIER);
            assertEquals(EdgeAdapter.INT_EDGESTYLE_SHARP_BEZIER, edge.getStyleAsInt());
        }
    }

    @Nested
    class Constants {

        @Test
        void edgeStyleConstants() {
            assertEquals(4, EdgeAdapter.EDGESTYLES.length);
            assertEquals(-1, EdgeAdapter.WIDTH_PARENT);
            assertEquals(0, EdgeAdapter.WIDTH_THIN);
            assertEquals("thin", EdgeAdapter.EDGE_WIDTH_THIN_STRING);
        }
    }

    @Nested
    class TargetNode {

        @Test
        void target_getAndSet() {
            assertSame(rootNode, edge.getTarget());

            MindMapNodeModel other = new MindMapNodeModel("Other", mockMap);
            edge.setTarget(other);
            assertSame(other, edge.getTarget());
        }
    }
}
