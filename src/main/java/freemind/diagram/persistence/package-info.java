/**
 * Native persistence: shared envelope ({@code <diagram-document>}) plus
 * plugin-owned payload codecs. The envelope carries metadata, style palette,
 * and resources; the payload is opaque to the envelope and delegated to
 * the registered {@link freemind.diagram.plugin.DiagramPlugin}'s
 * {@link DiagramPayloadCodec}.
 */
package freemind.diagram.persistence;
