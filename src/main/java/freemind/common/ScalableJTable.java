package freemind.common;

import javax.swing.*;

public class ScalableJTable extends JTable {

    public ScalableJTable() {
        int scale = freemind.main.SwingUtils.getScalingFactorPlain();
        setRowHeight(getRowHeight() * scale / 100);
    }
}
