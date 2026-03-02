package freemind.controller;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class MainToolBar extends FreeMindToolBar {
    final Controller controller;

    public MainToolBar(final Controller controller) {
        super();
        this.controller = controller;
        setRollover(true);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
    }

    public void activate(boolean visible) {
    }

    public void setAllActions(boolean enabled) {
    }

}
