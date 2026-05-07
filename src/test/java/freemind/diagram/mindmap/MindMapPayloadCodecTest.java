package freemind.diagram.mindmap;

import freemind.diagram.AttributeBag;
import freemind.diagram.ContentFormat;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StyleId;
import freemind.diagram.StylePalette;
import freemind.diagram.StyleRef;
import freemind.diagram.StyleReferences;
import freemind.diagram.capabilities.AuxiliaryLink;
import freemind.diagram.persistence.PayloadReadContext;
import freemind.diagram.persistence.PayloadWriteContext;
import freemind.diagram.persistence.UnsupportedPayloadVersionException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class MindMapPayloadCodecTest {

    private final MindMapPayloadCodec codec = new MindMapPayloadCodec();

    @Test
    void roundTripPreservesTreeStructure() {
        var d = buildSample();   // root → a → a1, root → b
        var roundTripped = roundTrip(d);

        var allIds = StreamSupport.stream(roundTripped.allNodes().spliterator(), false)
            .map(n -> n.nodeId().value()).collect(Collectors.toSet());
        assertEquals(Set.of("root", "a", "a1", "b"), allIds);
        var rootR = roundTripped.rootNode();
        var rootChildIds = roundTripped.getChildren(rootR).stream()
            .map(n -> n.nodeId().value()).collect(Collectors.toList());
        assertEquals(List.of("a", "b"), rootChildIds);
    }

    @Test
    void roundTripPreservesNodeContent() {
        var root = node("root", NodeContent.html("<b>Bold root</b>"));
        var d = MindMapDiagramImpl.createEmpty(root);
        var a = node("a", NodeContent.markdown("# Header"));
        d.addChild(root, a);

        var rt = roundTrip(d);
        assertEquals(ContentFormat.HTML, rt.rootNode().content().format());
        assertEquals("<b>Bold root</b>", rt.rootNode().content().text());
        var aR = rt.getChildren(rt.rootNode()).get(0);
        assertEquals(ContentFormat.MARKDOWN, aR.content().format());
        assertEquals("# Header", aR.content().text());
    }

    @Test
    void roundTripPreservesStyleReferences() {
        var root = node("root", NodeContent.plain("r"));
        var d = MindMapDiagramImpl.createEmpty(root);
        root.setStyleReferences(new StyleReferences(
            Optional.of(new StyleRef(new StyleId("primary"))),
            Optional.of(new StyleRef(new StyleId("title"))),
            Optional.empty()));

        var rt = roundTrip(d);
        var refs = rt.rootNode().styleReferences();
        assertEquals(Optional.of(new StyleRef(new StyleId("primary"))), refs.color());
        assertEquals(Optional.of(new StyleRef(new StyleId("title"))), refs.font());
        assertTrue(refs.stroke().isEmpty());
    }

    @Test
    void roundTripPreservesAttributes() {
        var root = node("root", NodeContent.plain("r"));
        var d = MindMapDiagramImpl.createEmpty(root);
        root.setAttributes(AttributeBag.empty().with("priority", "high").with("status", "open"));

        var rt = roundTrip(d);
        var attrs = rt.rootNode().attributes();
        assertEquals("high", attrs.get("priority").orElseThrow());
        assertEquals("open", attrs.get("status").orElseThrow());
    }

    @Test
    void roundTripPreservesAuxiliaryLinks() {
        var root = node("root", NodeContent.plain("r"));
        var a = node("a", NodeContent.plain("a"));
        var b = node("b", NodeContent.plain("b"));
        var d = MindMapDiagramImpl.createEmpty(root);
        d.addChild(root, a);
        d.addChild(root, b);
        d.addAuxiliaryLink(new AuxiliaryLink<>(a, b, Optional.of("relates-to"), StyleReferences.none()));

        var rt = roundTrip(d);
        var aR = rt.getChildren(rt.rootNode()).get(0);
        var outLinks = rt.outgoingAuxiliaryLinks(aR);
        assertEquals(1, outLinks.size());
        assertEquals(Optional.of("relates-to"), outLinks.get(0).label());
    }

    @Test
    void unsupportedPayloadVersionThrows() {
        var d = buildSample();
        var doc = newDocument();
        var holder = new Element[1];
        var writeCtx = new PayloadWriteContext(doc, e -> holder[0] = e);
        codec.writePayload(d, writeCtx);
        var readCtx = new PayloadReadContext(d.documentId(), d.metadata(),
            d.stylePalette(), d.resources(), holder[0]);

        assertThrows(UnsupportedPayloadVersionException.class,
            () -> codec.readPayload(99, readCtx));
    }

    // --- helpers ---

    private MindMapDiagramImpl buildSample() {
        var root = node("root", NodeContent.plain("root"));
        var a = node("a", NodeContent.plain("a"));
        var a1 = node("a1", NodeContent.plain("a1"));
        var b = node("b", NodeContent.plain("b"));
        var d = MindMapDiagramImpl.createEmpty(root);
        d.addChild(root, a);
        d.addChild(a, a1);
        d.addChild(root, b);
        return d;
    }

    private MindMapNodeImpl node(String id, NodeContent content) {
        return new MindMapNodeImpl(new NodeId(id), content);
    }

    private MindMapDiagram roundTrip(MindMapDiagram d) {
        var doc = newDocument();
        var holder = new Element[1];
        var writeCtx = new PayloadWriteContext(doc, e -> holder[0] = e);
        codec.writePayload(d, writeCtx);
        var readCtx = new PayloadReadContext(d.documentId(), d.metadata(),
            d.stylePalette(), d.resources(), holder[0]);
        return codec.readPayload(codec.currentPayloadVersion(), readCtx);
    }

    private static org.w3c.dom.Document newDocument() {
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            return dbf.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}
