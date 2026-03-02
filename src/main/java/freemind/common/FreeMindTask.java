package freemind.common;

import freemind.main.Resources;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

/**
 * Long running tasks inside FreeMind should derive from this class.
 */
@Slf4j
public abstract class FreeMindTask extends Thread {

    private static final long TIME_TO_DISPLAY_PROGRESS_BAR_IN_MILLIS = 1000;
    private boolean mInterrupted = false;
    private boolean mFinished = false;
    private int mAmountOfSteps;
    private FreeMindProgressMonitor mProgressMonitor = null;
    private int mRounds;
    protected ProgressDescription mProgressDescription;
    private final RootPaneContainer mFrame;
    private final JPanel mGlass;
    private final Component mOldGlassPane;

    protected static class ProgressDescription {
        public ProgressDescription(String pProgressString,Object[] pProgressParameters) {
            super();
            mProgressString = pProgressString;
            mProgressParameters = pProgressParameters;
        }

        public final String mProgressString;
        /**
         * To be inserted into mProgressString;
         */
        public final Object[] mProgressParameters;
    }

    public FreeMindTask(RootPaneContainer pRootPaneContainer, int pAmountOfSteps, String pName, Resources resources) {
        super(pName);
        mFrame = pRootPaneContainer;
        mAmountOfSteps = pAmountOfSteps;
        mProgressMonitor = new FreeMindProgressMonitor(getName(), resources);
        mGlass = new JPanel(new GridLayout(0, 1));
        JLabel padding = new JLabel();
        mGlass.setOpaque(false);
        mGlass.add(padding);

        // trap both mouse and key events. Could provide a smarter
        // key handler if you wanted to allow things like a keystroke
        // that would cancel the long-running operation.
        mGlass.addMouseListener(new MouseAdapter() {});
        mGlass.addMouseMotionListener(new MouseMotionAdapter() {});
        mGlass.addKeyListener(new KeyAdapter() {});

        // make sure the focus won't leave the glass pane
        mGlass.setFocusCycleRoot(true); // 1.4
        mOldGlassPane = pRootPaneContainer.getGlassPane();
        pRootPaneContainer.setGlassPane(mGlass);
        mGlass.setVisible(true);
        padding.requestFocus();  // required to trap key events
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        mRounds = 0;
        boolean again = true;
        while (again) {
            try {
                again = processAction();
                mRounds++;
                if (!again) {
                    // already ready!!
                    mRounds = mAmountOfSteps;
                }
                if (mRounds == mAmountOfSteps) {
                    again = false;
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                again = false;
            }
            if (isInterrupted()) {
                again = false;
            }
            if (System.currentTimeMillis() - startTime > TIME_TO_DISPLAY_PROGRESS_BAR_IN_MILLIS) {
                // mProgressMonitor.setModal(true);
                EventQueue.invokeLater(() -> mProgressMonitor.setVisible(true));
            }
            if (mProgressMonitor.isVisible()) {
                ProgressDescription progressDescription = mProgressDescription;
                if (mProgressDescription == null) {
                    progressDescription = new ProgressDescription(
                            "FreeMindTask.Default", new Object[]{Integer.valueOf(mRounds)});
                }
                boolean canceled = mProgressMonitor.showProgress(mRounds,
                        mAmountOfSteps, progressDescription.mProgressString,
                        progressDescription.mProgressParameters);
                if (canceled) {
                    mInterrupted = true;
                    again = false;
                }
            }
        }
        setFinished(true);
        EventQueue.invokeLater(() -> {
            mGlass.setVisible(false);
            mFrame.setGlassPane(mOldGlassPane);
            mProgressMonitor.dismiss();
        });
    }

    /**
     * Subclasses should process one single action out of the set of its actions
     * and then return. The method is directly called again by the task
     * controller until it returns false.
     *
     * @return true, if further actions follow. False, if done.
     */
    protected abstract boolean processAction();

    public boolean isInterrupted() {
        return mInterrupted;
    }

    public void setInterrupted(boolean pInterrupted) {
        mInterrupted = pInterrupted;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean pFinished) {
        mFinished = pFinished;
    }

    public int getAmountOfSteps() {
        return mAmountOfSteps;
    }

    public void setAmountOfSteps(int pAmountOfSteps) {
        mAmountOfSteps = pAmountOfSteps;
    }

    public int getRounds() {
        return mRounds;
    }

}
