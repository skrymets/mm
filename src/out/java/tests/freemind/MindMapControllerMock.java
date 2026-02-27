/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Dimitri Polivaev, Christian Foltin and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Created on 10.10.2006
 */
/*$Id: MindMapControllerMock.java,v 1.1.2.12 2008/12/09 21:09:43 christianfoltin Exp $*/
package tests.freemind;

import freemind.frok.patches.FreeMindMainMock;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import freemind.controller.Controller;
import freemind.controller.MapModuleManager;
import freemind.controller.StructuredMenuHolder;
import freemind.extensions.HookFactory;
import freemind.main.FreeMindMain;
import freemind.main.XMLParseException;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.MapAdapter;
import freemind.modes.MapFeedbackAdapter;
import freemind.modes.MindMap;
import freemind.modes.MindMapNode;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.modes.NodeAdapter;
import freemind.view.MapModule;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

/**
 * @author foltin
 */
public class MindMapControllerMock extends MapFeedbackAdapter implements ModeController {

    private final FreeMindMainMock freeMindMain;
    private final MindMapMock mindMapMock;

    public MindMapControllerMock(FreeMindMainMock freeMindMain, String pMapXmlString) {
        this.freeMindMain = freeMindMain;
        mindMapMock = new MindMapMock(pMapXmlString);
    }

    @Override
    public FreeMindMain getFrame() {
        return freeMindMain;
    }

    @Override
    public MindMap getMap() {
        return mindMapMock;
    }

    @Override
    public ModeController load(URL pFile) throws FileNotFoundException, IOException, XMLParseException, URISyntaxException {
        return null;
    }

    @Override
    public ModeController load(File pFile) throws FileNotFoundException, IOException {
        return null;
    }

    @Override
    public void loadURL(String pRelative) {
    }

    @Override
    public boolean save(File pFile) {
        return false;
    }

    @Override
    public ModeController newMap() {
        return null;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public boolean saveAs() {
        return false;
    }

    @Override
    public void open() {
    }

    @Override
    public boolean close(boolean pForce, MapModuleManager pMapModuleManager) {
        return false;
    }

    @Override
    public void startupController() {
    }

    @Override
    public void shutdownController() {
    }

    @Override
    public void doubleClick(MouseEvent pE) {
    }

    @Override
    public void plainClick(MouseEvent pE) {
    }

    @Override
    public void setVisible(boolean pVisible) {
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public NodeAdapter getNodeFromID(String pNodeID) {
        return null;
    }

    @Override
    public String getNodeID(MindMapNode pSelected) {
        return null;
    }

    @Override
    public void select(NodeView pNode) {
    }

    @Override
    public void select(MindMapNode pFocused, List<MindMapNode> pSelecteds) {
    }

    @Override
    public void selectBranch(NodeView pSelected, boolean pExtend) {
    }

    @Override
    public MindMapNode getSelected() {
        return null;
    }

    @Override
    public NodeView getSelectedView() {
        return null;
    }

    @Override
    public List<MindMapNode> getSelecteds() {
        return null;
    }

    @Override
    public List<MindMapNode> getSelectedsByDepth() {
        return null;
    }

    @Override
    public void sortNodesByDepth(List<MindMapNode> pInPlaceList) {
    }

    @Override
    public boolean extendSelection(MouseEvent pE) {
        return false;
    }

    @Override
    public void setSaved(boolean pIsClean) {
    }

    @Override
    public void registerNodeSelectionListener(NodeSelectionListener pListener, boolean pCallWithCurrentSelection) {
    }

    @Override
    public void deregisterNodeSelectionListener(NodeSelectionListener pListener) {
    }

    @Override
    public void registerNodeLifetimeListener(NodeLifetimeListener pListener, boolean pFireCreateEvent) {
    }

    @Override
    public void deregisterNodeLifetimeListener(NodeLifetimeListener pListener) {
    }

    @Override
    public void displayNode(MindMapNode pNode) {
    }

    @Override
    public void centerNode(MindMapNode pNode) {
    }

    @Override
    public String getLinkShortText(MindMapNode pNode) {
        return null;
    }

    @Override
    public JToolBar getModeToolBar() {
        return null;
    }

    @Override
    public void updateMenus(StructuredMenuHolder pHolder) {
    }

    @Override
    public void updatePopupMenu(StructuredMenuHolder pHolder) {
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return null;
    }

    @Override
    public void showPopupMenu(MouseEvent pE) {
    }

    @Override
    public JPopupMenu getPopupForModel(Object pObj) {
        return null;
    }

    @Override
    public MapView getView() {
        return null;
    }

    @Override
    public void setModel(MapAdapter pModel) {
    }

    @Override
    public Mode getMode() {
        return null;
    }

    @Override
    public MapModule getMapModule() {
        return null;
    }

    @Override
    public Controller getController() {
        return null;
    }

    @Override
    public HookFactory getHookFactory() {
        return null;
    }

    @Override
    public Color getSelectionColor() {
        return null;
    }

    @Override
    public String getText(String pTextId) {
        return null;
    }

    @Override
    public URL getResource(String pPath) {
        return null;
    }

    @Override
    public NodeView getNodeView(MindMapNode pNode) {
        return null;
    }

    @Override
    public void refreshMap() {
    }

    @Override
    public Transferable copy(MindMapNode pNode, boolean pSaveInvisible) {
        return null;
    }

    @Override
    public Transferable copy() {
        return null;
    }

    @Override
    public Transferable copySingle() {
        return null;
    }

    @Override
    public Transferable copy(List<MindMapNode> pSelectedNodes, boolean pCopyInvisible) {
        return null;
    }

    @Override
    public FreeMindFileDialog getFileChooser(FileFilter pFilter) {
        return null;
    }

    @Override
    public void setView(MapView pView) {
    }

    @Override
    public void setToolTip(MindMapNode pNode, String pKey, String pValue) {
    }

}
