package freemind.main;

import javax.swing.*;

public interface IFreeMindSplash {

    FeedBack getFeedBack();

    void close();

    void setVisible(boolean pB);

    ImageIcon getWindowIcon();

}
// private static org.slf4j.Logger logger =
// freemind.main.Resources.getInstance().getLogger(IFreeMindSplash.class.getName());
