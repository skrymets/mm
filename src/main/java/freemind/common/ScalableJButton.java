package freemind.common;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serial;

/**
 * See <a href="http://stackoverflow.com/questions/8183949/swing-scale-a-text-font-of-component">...</a>
 */

@Slf4j
public class ScalableJButton extends JButton implements ComponentListener {

    @Serial
    private static final long serialVersionUID = 1L;

    public ScalableJButton(String pString) {
        super(pString);
        init();
    }

    public ScalableJButton() {
        super();
        init();
    }

    private void init() {
//		mInitialFont = getFont();
//		addComponentListener(this);
    }

    @Override
    public void componentResized(ComponentEvent pE) {
//		if (mInitialHeight == 0) {
//			mInitialHeight = getHeight();
//		}
//		int resizal = this.getHeight() * mInitialFont.getSize() / mInitialHeight;
//		if(resizal != mCurrentSize){
//			setFont(mInitialFont.deriveFont((float) resizal));
//			mCurrentSize = resizal;
//		}
    }

    @Override
    public void componentMoved(ComponentEvent pE) {
    }

    @Override
    public void componentShown(ComponentEvent pE) {
    }

    @Override
    public void componentHidden(ComponentEvent pE) {
    }
}
