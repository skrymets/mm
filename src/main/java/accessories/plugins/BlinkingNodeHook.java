package accessories.plugins;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@code @file} BlinkingNodeHook.java
 * {@code @package} freemind.modes.mindmapmode
 */
public class BlinkingNodeHook extends PermanentMindMapNodeHookAdapter {

    private Timer timer = null;

    public BlinkingNodeHook() {
        super();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerColorChanger(), 500, 500);
            nodeChanged(getNode());
        }
    }

    // add a new node:
    // MindMapNode newNode=((ControllerAdapter)getController()).newNode();
    // ((MapAdapter) getMap()).insertNodeInto(newNode, getNode(), 0);

    static final List<Color> colors = new ArrayList<>();

    protected class TimerColorChanger extends TimerTask {
        TimerColorChanger() {
            colors.clear();
            colors.add(Color.BLUE);
            colors.add(Color.RED);
            colors.add(Color.MAGENTA);
            colors.add(Color.CYAN);

        }

        /**
         * TimerTask method to enable the selection after a given time.
         */
        public void run() {
            SwingUtilities.invokeLater(() -> {
                if (getNode() == null || getController().isBlocked())
                    return;
                getController().getView().getViewerRegistryService().acceptViewVisitor(getNode(), view -> {
                    if (!view.isVisible()) {
                        return;
                    }
                    Color col = view.getMainView().getForeground();
                    int index = -1;
                    if (col != null && colors.contains(col)) {
                        index = colors.indexOf(col);
                    }
                    index++;
                    if (index >= colors.size())
                        index = 0;
                    view.getMainView().setForeground(
                            colors.get(index));
                });
            });
        }
    }

    public void shutdownMapHook() {
        timer.cancel();
        nodeChanged(getNode());
        timer = null;
        super.shutdownMapHook();
    }

}
