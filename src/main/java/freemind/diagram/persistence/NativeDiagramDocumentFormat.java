package freemind.diagram.persistence;

import freemind.diagram.Diagram;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.DocumentId;
import freemind.diagram.ResourceEntry;
import freemind.diagram.ResourceId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StyleId;
import freemind.diagram.StylePalette;
import freemind.diagram.persistence.envelope.ColorEntryXml;
import freemind.diagram.persistence.envelope.DiagramSlotXml;
import freemind.diagram.persistence.envelope.EnvelopeXml;
import freemind.diagram.persistence.envelope.FontEntryXml;
import freemind.diagram.persistence.envelope.MetadataXml;
import freemind.diagram.persistence.envelope.PayloadXml;
import freemind.diagram.persistence.envelope.ResourceEntryXml;
import freemind.diagram.persistence.envelope.ResourcesXml;
import freemind.diagram.persistence.envelope.StrokeEntryXml;
import freemind.diagram.persistence.envelope.StylesXml;
import freemind.diagram.plugin.DiagramPlugin;
import freemind.diagram.plugin.DiagramPluginRegistry;
import freemind.diagram.style.ColorEntry;
import freemind.diagram.style.FontEntry;
import freemind.diagram.style.FontWeight;
import freemind.diagram.style.StrokeEntry;
import freemind.diagram.style.StrokeStyle;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Reads and writes the {@code <diagram-document>} envelope, delegating
 * payload marshalling/unmarshalling to the {@link DiagramPayloadCodec} of
 * the plugin registered for the document's {@link DiagramTypeId}.
 */
@Slf4j
public final class NativeDiagramDocumentFormat {

    public static final int CURRENT_FORMAT_VERSION = 1;

    private final DiagramPluginRegistry registry;
    private final String applicationVersion;
    private final JAXBContext envelopeContext;

    public NativeDiagramDocumentFormat(DiagramPluginRegistry registry, String applicationVersion) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.applicationVersion = Objects.requireNonNull(applicationVersion, "applicationVersion");
        try {
            this.envelopeContext = JAXBContext.newInstance(EnvelopeXml.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to build envelope JAXB context", e);
        }
    }

    /** Reads an envelope from the given stream, then asks the registered plugin to decode the payload. */
    public Diagram read(InputStream in) {
        Objects.requireNonNull(in, "in");
        var envelope = unmarshalEnvelope(in);
        if (envelope.formatVersion != CURRENT_FORMAT_VERSION) {
            throw new UnsupportedFormatVersionException(envelope.formatVersion);
        }
        var typeId = new DiagramTypeId(envelope.diagram.type);
        var plugin = registry.findByTypeId(typeId)
            .orElseThrow(() -> new UnsupportedDiagramTypeException(typeId));
        var stylePalette = stylePaletteFromXml(envelope.styles);
        var resources = resourcesFromXml(envelope.resources);
        var documentId = new DocumentId(UUID.fromString(envelope.metadata.documentId));
        var ctx = new PayloadReadContext(documentId, stylePalette, resources,
            envelope.diagram.payload.root);
        return readPayload(plugin, envelope.diagram.payloadVersion, ctx);
    }

    /** Asks the plugin's codec to write the payload, then marshals the full envelope. */
    public void write(Diagram diagram, OutputStream out) {
        Objects.requireNonNull(diagram, "diagram");
        Objects.requireNonNull(out, "out");
        var plugin = registry.findByTypeId(diagram.typeId())
            .orElseThrow(() -> new UnsupportedDiagramTypeException(diagram.typeId()));
        var doc = newW3cDocument();
        var payloadRootHolder = new Element[1];
        var ctx = new PayloadWriteContext(doc, e -> payloadRootHolder[0] = e);
        writePayload(plugin, diagram, ctx);
        if (payloadRootHolder[0] == null) {
            throw new IllegalStateException(
                "Plugin '" + plugin.typeId().value() + "' did not call setPayloadRoot");
        }
        var envelope = buildEnvelope(diagram, plugin, payloadRootHolder[0]);
        marshalEnvelope(envelope, out);
    }

    @SuppressWarnings("unchecked")
    private <D extends Diagram> Diagram readPayload(DiagramPlugin<?> plugin, int payloadVersion, PayloadReadContext ctx) {
        return ((DiagramPlugin<D>) plugin).nativePayloadCodec().readPayload(payloadVersion, ctx);
    }

    @SuppressWarnings("unchecked")
    private <D extends Diagram> void writePayload(DiagramPlugin<?> plugin, Diagram diagram, PayloadWriteContext ctx) {
        ((DiagramPlugin<D>) plugin).nativePayloadCodec().writePayload((D) diagram, ctx);
    }

    private EnvelopeXml buildEnvelope(Diagram diagram, DiagramPlugin<?> plugin, Element payloadRoot) {
        var envelope = new EnvelopeXml();
        envelope.formatVersion = CURRENT_FORMAT_VERSION;
        envelope.applicationVersion = applicationVersion;
        envelope.metadata = metadataToXml(diagram.documentId(), diagram.metadata());
        envelope.styles = styleToXml(diagram.stylePalette());
        envelope.resources = resourcesToXml(diagram.resources());
        envelope.diagram = new DiagramSlotXml();
        envelope.diagram.type = diagram.typeId().value();
        envelope.diagram.payloadVersion = plugin.nativePayloadCodec().currentPayloadVersion();
        envelope.diagram.payload = new PayloadXml();
        envelope.diagram.payload.root = payloadRoot;
        return envelope;
    }

    private MetadataXml metadataToXml(DocumentId documentId, DiagramMetadata m) {
        var x = new MetadataXml();
        x.documentId = documentId.value().toString();
        x.title = m.title().orElse(null);
        x.author = m.author().orElse(null);
        x.createdAt = m.createdAt().toString();
        x.modifiedAt = m.modifiedAt().toString();
        return x;
    }

    private DiagramMetadata metadataFromXml(MetadataXml x) {
        return new DiagramMetadata(
            Optional.ofNullable(x.title),
            Optional.ofNullable(x.author),
            Instant.parse(x.createdAt),
            Instant.parse(x.modifiedAt));
    }

    private StylesXml styleToXml(StylePalette palette) {
        var x = new StylesXml();
        palette.colors().forEach((id, c) -> {
            var e = new ColorEntryXml();
            e.id = id.value();
            e.value = c.hex();
            x.colors.add(e);
        });
        palette.fonts().forEach((id, f) -> {
            var e = new FontEntryXml();
            e.id = id.value();
            e.family = f.family();
            e.size = f.size();
            e.weight = f.weight().name();
            x.fonts.add(e);
        });
        palette.strokes().forEach((id, s) -> {
            var e = new StrokeEntryXml();
            e.id = id.value();
            e.width = s.width();
            e.style = s.style().name();
            x.strokes.add(e);
        });
        return x;
    }

    private StylePalette stylePaletteFromXml(StylesXml x) {
        var palette = StylePalette.empty();
        if (x == null) return palette;
        for (var c : x.colors)   palette = palette.withColor(new StyleId(c.id), new ColorEntry(c.value));
        for (var f : x.fonts)    palette = palette.withFont(new StyleId(f.id),
            new FontEntry(f.family, f.size, FontWeight.valueOf(f.weight)));
        for (var s : x.strokes)  palette = palette.withStroke(new StyleId(s.id),
            new StrokeEntry(s.width, StrokeStyle.valueOf(s.style)));
        return palette;
    }

    private ResourcesXml resourcesToXml(ResourceTable t) {
        var x = new ResourcesXml();
        t.entries().forEach((id, entry) -> {
            var e = new ResourceEntryXml();
            e.id = id.value();
            e.mimeType = entry.mimeType();
            entry.externalUri().ifPresent(uri -> e.externalUri = uri.toString());
            entry.embeddedBlob().ifPresent(blob -> e.embeddedBase64 = Base64.getEncoder().encodeToString(blob));
            x.entries.add(e);
        });
        return x;
    }

    private ResourceTable resourcesFromXml(ResourcesXml x) {
        var table = ResourceTable.empty();
        if (x == null) return table;
        for (var e : x.entries) {
            ResourceEntry entry = e.externalUri != null
                ? ResourceEntry.external(e.mimeType, URI.create(e.externalUri))
                : ResourceEntry.embedded(e.mimeType,
                    Base64.getDecoder().decode(e.embeddedBase64 == null ? "" : e.embeddedBase64.trim()));
            table = table.withEntry(new ResourceId(e.id), entry);
        }
        return table;
    }

    private EnvelopeXml unmarshalEnvelope(InputStream in) {
        try {
            Unmarshaller u = envelopeContext.createUnmarshaller();
            return (EnvelopeXml) u.unmarshal(in);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to unmarshal envelope", e);
        }
    }

    private void marshalEnvelope(EnvelopeXml envelope, OutputStream out) {
        try {
            Marshaller m = envelopeContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            m.marshal(envelope, out);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to marshal envelope", e);
        }
    }

    private Document newW3cDocument() {
        try {
            var f = DocumentBuilderFactory.newInstance();
            f.setNamespaceAware(true);
            return f.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Failed to create W3C Document builder", e);
        }
    }
}
