package freemind.controller;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

// import ublic class MindMapNodesSelection implements Transferable,
// ClipboardOwner {
// public static DataFlavor fileListFlavor = null;

public class NodeDropListener implements DropTargetListener {

    private DropTargetListener mListener;

    public NodeDropListener(Controller controller) {
    }

    public void register(DropTargetListener listener) {
        this.mListener = listener;
    }

    public void deregister() {
        mListener = null;
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        if (mListener != null)
            mListener.dragEnter(dtde);
    }

    public void dragExit(DropTargetEvent dte) {
        if (mListener != null)
            mListener.dragExit(dte);
    }

    public void dragOver(DropTargetDragEvent dtde) {
        if (mListener != null)
            mListener.dragOver(dtde);
    }

    public void drop(DropTargetDropEvent dtde) {
        if (mListener != null)
            mListener.drop(dtde);
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        if (mListener != null)
            mListener.dropActionChanged(dtde);
    }

}
