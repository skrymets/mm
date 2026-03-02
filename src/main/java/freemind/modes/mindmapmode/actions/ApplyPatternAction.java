package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.Pattern;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.StylePatternFactory;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin;
import freemind.modes.mindmapmode.MindMapMapModel;
import freemind.modes.mindmapmode.MindMapNodeModel;

@SuppressWarnings("serial")
public class ApplyPatternAction extends NodeGeneralAction implements
        SingleNodeOperation {
    public interface ExternalPatternAction extends MindMapControllerPlugin {
        void act(MindMapNode node, Pattern pattern);
    }

    private final Pattern mpattern;

    public ApplyPatternAction(MindMapController controller, Pattern pattern) {
        super(controller, null /* no text */, null /* = no icon */);
        setName(pattern.getName());
        this.mpattern = pattern;
        setSingleNodeOperation(this);
    }

    public void apply(MindMapMapModel map, MindMapNodeModel node) {
        StylePatternFactory.applyPattern(node, mpattern, getMindMapController().getPatternsList(), getMindMapController().getPlugins(), getMindMapController());
    }

    public static String edgeWidthIntToString(int value) {
        if (value == EdgeAdapter.DEFAULT_WIDTH) {
            return null;
        }
        if (value == EdgeAdapter.WIDTH_THIN) {
            return EdgeAdapter.EDGE_WIDTH_THIN_STRING;
        }
        return Integer.toString(value);
    }

    /**
     * @return Returns the pattern.
     */
    public Pattern getPattern() {
        return mpattern;
    }

}
