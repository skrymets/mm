/**
 * Capability mixins for cross-cutting features that can attach to any
 * topology. Each capability is a small interface a concrete diagram type
 * can opt into (e.g., {@link HasAuxiliaryLinks} for mind-map ArrowLinks,
 * future {@code HasCategorizedFirstLevel} for fishbone categories).
 *
 * <p>Capabilities are added on demand — do not preemptively define every
 * possible facet (rule of three).
 */
package freemind.diagram.capabilities;
