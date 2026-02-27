package freemind.view.mindmapview;

import javax.swing.Timer;
import java.util.function.Consumer;

/**
 * Provides smooth animated zoom transitions using eased interpolation.
 * Uses a Swing Timer to step through zoom levels at ~55fps.
 */
public class ZoomAnimator {

    private static final int DEFAULT_STEPS = 8;
    private static final int FRAME_DELAY_MS = 18; // ~55fps

    private Timer timer;

    /**
     * Computes interpolated zoom steps from {@code from} to {@code to}
     * using ease-out quadratic easing.
     *
     * @param from      starting zoom level
     * @param to        target zoom level
     * @param stepCount number of steps to generate
     * @return array of interpolated zoom values; empty if from == to
     */
    public static float[] computeSteps(float from, float to, int stepCount) {
        if (Float.compare(from, to) == 0) {
            return new float[0];
        }
        if (stepCount <= 1) {
            return new float[]{to};
        }
        float[] steps = new float[stepCount];
        for (int i = 0; i < stepCount; i++) {
            float t = (float) i / (stepCount - 1);
            // Ease-out quadratic: fast start, slow finish
            float eased = 1.0f - (1.0f - t) * (1.0f - t);
            steps[i] = from + (to - from) * eased;
        }
        // Ensure exact target value at the end
        steps[stepCount - 1] = to;
        return steps;
    }

    /**
     * Animates a zoom transition from {@code from} to {@code to},
     * calling {@code applyZoom} at each step. Any running animation
     * is stopped before starting a new one.
     *
     * @param from      starting zoom level
     * @param to        target zoom level
     * @param applyZoom callback invoked with each intermediate zoom value
     */
    public void animateZoom(float from, float to, Consumer<Float> applyZoom) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        float[] steps = computeSteps(from, to, DEFAULT_STEPS);
        if (steps.length == 0) {
            return;
        }
        final int[] index = {0};
        timer = new Timer(FRAME_DELAY_MS, e -> {
            if (index[0] < steps.length) {
                applyZoom.accept(steps[index[0]]);
                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Stops any currently running zoom animation.
     */
    public void stopAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
