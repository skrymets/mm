/**
 * Plugin layer: bundles model, controller, UI contributions, hooks, and codecs
 * for a specific diagram type. The host application registers plugins through
 * {@link DiagramPluginRegistry} and looks them up by
 * {@link freemind.diagram.DiagramTypeId}.
 *
 * <p>This package and its contracts must not depend on {@code javax.swing.*}
 * or {@code java.awt.*}. UI is described via {@link freemind.diagram.ui}
 * descriptors, not concrete Swing widgets.
 */
package freemind.diagram.plugin;
