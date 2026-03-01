package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NodeInformationTimerAction implements ActionListener {

    private final MindMapController controller;
    private boolean mIsInterrupted = false;
    private boolean mIsDone = true;

    public NodeInformationTimerAction(MindMapController controller) {
        this.controller = controller;
    }

    public boolean isRunning() {
        return !mIsDone;
    }

    public boolean interrupt() {
        mIsInterrupted = true;
        int i = 1000;
        try {
            while (i > 0 && !mIsDone) {
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        mIsInterrupted = false;
        return i > 0;
    }

    @Override
    public void actionPerformed(ActionEvent pE) {
        mIsDone = false;
        actionPerformedInternally(pE);
        mIsDone = true;
    }

    public void actionPerformedInternally(ActionEvent pE) {
        String nodeStatusLine;
        List<MindMapNode> selecteds = controller.getSelecteds();
        int amountOfSelected = selecteds.size();
        if (amountOfSelected == 0) {
            return;
        }
        double sum = 0d;
        Pattern p = Pattern.compile(MindMapController.REGEXP_FOR_NUMBERS_IN_STRINGS);
        for (MindMapNode selectedNode : selecteds) {
            if (mIsInterrupted) {
                return;
            }
            Matcher m = p.matcher(selectedNode.getText());
            while (m.find()) {
                String number = m.group();
                try {
                    sum += Double.parseDouble(number);
                } catch (NumberFormatException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
        if (amountOfSelected > 1) {
            nodeStatusLine = controller.getResources().format("node_status_line_several_selected_nodes", new Object[]{amountOfSelected, sum});
        } else {
            MindMapNode sel = selecteds.get(0);
            long amountOfChildren = 0;
            List<MindMapNode> allDescendants = new ArrayList<>(Collections.singletonList(sel));
            while (!allDescendants.isEmpty()) {
                if (mIsInterrupted) {
                    return;
                }
                MindMapNode child = allDescendants.get(0);
                amountOfChildren += child.getChildCount();
                allDescendants.addAll(child.getChildren());
                allDescendants.remove(0);
            }
            nodeStatusLine = controller.getResources().format("node_status_line", new Object[]{sel.getShortText(controller), Integer.valueOf(sel.getChildCount()), amountOfChildren});
        }
        controller.getFrame().setStatusText(nodeStatusLine);
    }
}
