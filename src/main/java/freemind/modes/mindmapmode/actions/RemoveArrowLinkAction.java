package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapArrowLinkModel;
import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class RemoveArrowLinkAction extends MindmapAction {

    private MindMapArrowLinkModel mArrowLink;

    private final MindMapController controller;

    /**
     * can be null can be null.
     */
    public RemoveArrowLinkAction(MindMapController controller,
                                 MindMapArrowLinkModel arrowLink) {
        super("remove_arrow_link", "images/edittrash.png", controller);
        this.controller = controller;
        setArrowLink(arrowLink);
    }

    /**
     * @return Returns the arrowLink.
     */
    public MindMapArrowLinkModel getArrowLink() {
        return mArrowLink;
    }

    /**
     * The arrowLink to set.
     */
    public void setArrowLink(MindMapArrowLinkModel arrowLink) {
        this.mArrowLink = arrowLink;
    }

    public void actionPerformed(ActionEvent e) {
        controller.removeReference(mArrowLink);
    }

}
