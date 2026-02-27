package freemind.view;

import org.junit.jupiter.api.Test;
import javax.swing.Icon;
import static org.junit.jupiter.api.Assertions.*;

class SvgIconFactoryTest {

    @Test
    void loadsSvgIconFromClasspath() {
        Icon icon = SvgIconFactory.loadSvgIcon("images/svg/filenew.svg", 24, 24);
        assertNotNull(icon);
        assertEquals(24, icon.getIconWidth());
        assertEquals(24, icon.getIconHeight());
    }

    @Test
    void returnsNullForMissingSvg() {
        Icon icon = SvgIconFactory.loadSvgIcon("images/svg/nonexistent.svg", 24, 24);
        assertNull(icon);
    }

    @Test
    void loadsSvgWithCustomSize() {
        Icon icon = SvgIconFactory.loadSvgIcon("images/svg/filenew.svg", 32, 32);
        assertNotNull(icon);
        assertEquals(32, icon.getIconWidth());
        assertEquals(32, icon.getIconHeight());
    }

    @Test
    void defaultSizeIs24() {
        Icon icon = SvgIconFactory.loadSvgIcon("images/svg/filenew.svg");
        assertNotNull(icon);
        assertEquals(24, icon.getIconWidth());
    }
}
