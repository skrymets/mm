package freemind.view.mindmapview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZoomAnimatorTest {

    @Test
    void interpolatesZoomSteps() {
        float[] steps = ZoomAnimator.computeSteps(1.0f, 2.0f, 5);
        assertEquals(5, steps.length);
        assertEquals(1.0f, steps[0], 0.01f);
        assertEquals(2.0f, steps[4], 0.01f);
        for (int i = 1; i < steps.length; i++) {
            assertTrue(steps[i] > steps[i - 1]);
        }
    }

    @Test
    void singleStepReturnsTarget() {
        float[] steps = ZoomAnimator.computeSteps(1.0f, 1.5f, 1);
        assertEquals(1, steps.length);
        assertEquals(1.5f, steps[0], 0.01f);
    }

    @Test
    void zoomOutStepsDecrease() {
        float[] steps = ZoomAnimator.computeSteps(2.0f, 0.5f, 5);
        assertEquals(5, steps.length);
        assertEquals(2.0f, steps[0], 0.01f);
        assertEquals(0.5f, steps[4], 0.01f);
        for (int i = 1; i < steps.length; i++) {
            assertTrue(steps[i] < steps[i - 1]);
        }
    }

    @Test
    void sameZoomReturnsEmpty() {
        float[] steps = ZoomAnimator.computeSteps(1.0f, 1.0f, 5);
        assertEquals(0, steps.length);
    }
}
