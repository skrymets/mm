package freemind.modes.common.plugins;

import freemind.extensions.PermanentNodeHookAdapter;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public abstract class ReminderHookBase extends PermanentNodeHookAdapter {

    public static final String PLUGIN_LABEL = "plugins/TimeManagementReminder.xml";

    private static final int CLOCK_INVISIBLE = 0;

    private static final int CLOCK_VISIBLE = 1;

    private static final int REMOVE_CLOCK = -1;

    public static final String REMINDUSERAT = "REMINDUSERAT";

    private static final int BLINK_INTERVAL_IN_MILLIES = 3000;

    @Setter
    @Getter
    private long remindUserAt = 0;

    private Timer timer;

    private static ImageIcon clockIcon = null;

    private static ImageIcon bellIcon;

    private static ImageIcon flagIcon;

    // private Vector dateVector = new Vector();

    public ReminderHookBase() {
        super();
    }

    public void loadFrom(Element child) {
        super.loadFrom(child);
        HashMap<String, String> hash = loadNameValuePairs(child);
        if (hash.containsKey(REMINDUSERAT)) {
            String remindAt = hash.get(REMINDUSERAT);
            setRemindUserAt(Long.parseLong(remindAt));
        }

    }

    public void save(Document doc, Element xml) {
        super.save(doc, xml);
        HashMap<String, Object> nameValuePairs = new HashMap<>();
        nameValuePairs.put(REMINDUSERAT, remindUserAt);
        saveNameValuePairs(nameValuePairs, doc, xml);
    }

    public void shutdownMapHook() {
        setToolTip(getNode(), getName(), null);
        if (timer != null) {
            timer.stop();
        }
        displayState(REMOVE_CLOCK, getNode(), true);
        super.shutdownMapHook();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        if (remindUserAt == 0) {
            return;
        }
        if (timer == null) {
            scheduleTimer();
            Date date = new Date(remindUserAt);
            Object[] messageArguments = {date};
            MessageFormat formatter = new MessageFormat(
                    getResourceString("plugins/TimeManagement.xml_reminderNode_tooltip"));
            String message = formatter.format(messageArguments);
            setToolTip(node, getName(), message);
            displayState(CLOCK_VISIBLE, getNode(), false);
        }
        log.info("Invoke for node: {}", node.getObjectId(getController()));
    }

    private void scheduleTimer() {
        timer = new Timer(getRemindUserAtAsSecondsFromNow(),
                new TimerBlinkTask(false));
        timer.setDelay(BLINK_INTERVAL_IN_MILLIES);
        timer.start();
    }

    private ImageIcon getClockIcon() {
        // icon
        if (clockIcon == null) {
            clockIcon = MindIcon.factory("clock2").getIcon();
        }
        return clockIcon;
    }

    private ImageIcon getBellIcon() {
        // icon
        if (bellIcon == null) {
            bellIcon = MindIcon.factory("bell").getIcon();
        }
        return bellIcon;
    }

    private ImageIcon getFlagIcon() {
        // icon
        if (flagIcon == null) {
            flagIcon = MindIcon.factory("flag").getIcon();
        }
        return flagIcon;
    }

    public class TimerBlinkTask implements ActionListener {

        public TimerBlinkTask(boolean stateAdded) {
            super();
            this.stateAdded = stateAdded;
        }

        private boolean stateAdded = false;

        public void actionPerformed(ActionEvent pE) {
            // check for time over:
            int remindAt = getRemindUserAtAsSecondsFromNow();
            if (remindAt > BLINK_INTERVAL_IN_MILLIES) {
                // time not over (maybe due to integer-long conversion too big)
                timer.stop();
                scheduleTimer();
                return;

            }
            // time is over, we add the new icon until
            // the user removes the reminder.
            //
            stateAdded = !stateAdded;
            setRemindUserAt(System.currentTimeMillis()
                    + BLINK_INTERVAL_IN_MILLIES); // 3
            displayState((stateAdded) ? CLOCK_VISIBLE : CLOCK_INVISIBLE,
                    getNode(), true);

        }

    }

    public void displayState(int stateAdded, MindMapNode pNode, boolean recurse) {
        ImageIcon icon = null;
        if (stateAdded == CLOCK_VISIBLE) {
            icon = getClockIcon();
        } else if (stateAdded == CLOCK_INVISIBLE) {
            if (pNode == getNode()) {
                icon = getBellIcon();
            } else {
                icon = getFlagIcon();
            }
        }
        pNode.setStateIcon(getStateKey(), icon);
        nodeRefresh(pNode);
        if (recurse && !pNode.isRoot()) {
            displayState(stateAdded, pNode.getParentNode(), recurse);
        }
    }

    protected abstract void nodeRefresh(MindMapNode node);

    protected abstract void setToolTip(MindMapNode node, String key,
                                       String value);

    public int getRemindUserAtAsSecondsFromNow() {
        long timeDiff = remindUserAt - System.currentTimeMillis();
        if (timeDiff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (timeDiff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) timeDiff;
    }

    private final String STATE_TOOLTIP = TimerBlinkTask.class.getName()
            + "_STATE_";

    private String mStateTooltipName = null;

    public String getStateKey() {
        if (mStateTooltipName == null) {
            mStateTooltipName = STATE_TOOLTIP;
            // + getNode().getObjectId(getController());
        }
        return mStateTooltipName;
    }
}
