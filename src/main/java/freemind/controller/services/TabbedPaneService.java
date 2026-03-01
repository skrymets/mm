/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
 */

package freemind.controller.services;

import freemind.controller.Controller;
import freemind.controller.MapModuleChangeObserver;
import freemind.model.MindMap;
import freemind.modes.Mode;
import freemind.view.MapModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.swap;
import static java.util.stream.IntStream.range;

/**
 * Manages the tabbed pane that displays open map modules as tabs, including
 * tab selection, tab movement, and synchronization with the MapModuleManager.
 */
@Slf4j
public class TabbedPaneService {

    private final Controller controller;

    private List<MapModule> tabbedPaneMapModules;

    @Getter
    private JTabbedPane tabbedPane;

    private boolean tabbedPaneSelectionUpdate = true;

    public TabbedPaneService(Controller controller) {
        this.controller = controller;
    }

    /**
     * Initializes the tabbed pane, registers change listeners and map module observers
     * to keep tabs synchronized with open maps.
     */
    public void addTabbedPane(JTabbedPane pTabbedPane) {
        tabbedPane = pTabbedPane;
        tabbedPaneMapModules = new ArrayList<>();
        tabbedPane.addChangeListener(new ChangeListener() {

            public synchronized void stateChanged(ChangeEvent pE) {
                tabSelectionChanged();
            }

        });
        controller.getMapModuleManager().addListener(new MapModuleChangeObserver() {

            public void afterMapModuleChange(MapModule pOldMapModule, Mode pOldMode, MapModule pNewMapModule, Mode pNewMode) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (pNewMapModule == null) {
                    return;
                }
                // search, if already present:
                for (int i = 0; i < tabbedPaneMapModules.size(); ++i) {
                    if (tabbedPaneMapModules.get(i) == pNewMapModule) {
                        if (selectedIndex != i) {
                            tabbedPane.setSelectedIndex(i);
                        }
                        return;
                    }
                }
                // create new tab:
                tabbedPaneMapModules.add(pNewMapModule);
                tabbedPane.addTab(pNewMapModule.toString(), new JPanel());
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }

            public void beforeMapModuleChange(MapModule pOldMapModule,
                                              Mode pOldMode, MapModule pNewMapModule, Mode pNewMode) {
            }

            public boolean isMapModuleChangeAllowed(MapModule pOldMapModule,
                                                    Mode pOldMode, MapModule pNewMapModule, Mode pNewMode) {
                return true;
            }

            public void numberOfOpenMapInformation(int pNumber, int pIndex) {
            }

            public void afterMapClose(MapModule pOldMapModule, Mode pOldMode) {
                for (int i = 0; i < tabbedPaneMapModules.size(); ++i) {
                    if (tabbedPaneMapModules.get(i) == pOldMapModule) {
                        log.trace("Remove tab:{} with title:{}", i, tabbedPane.getTitleAt(i));
                        tabbedPaneSelectionUpdate = false;
                        tabbedPane.removeTabAt(i);
                        tabbedPaneMapModules.remove(i);
                        tabbedPaneSelectionUpdate = true;
                        tabSelectionChanged();
                        return;
                    }
                }
            }
        });

        controller.registerMapTitleChangeListener((pNewMapTitle, pMapModule, pModel) ->
                range(0, tabbedPaneMapModules.size())
                        .filter(i -> tabbedPaneMapModules.get(i) == pMapModule)
                        .forEachOrdered(i -> tabbedPane.setTitleAt(i, pNewMapTitle + ((pModel.isSaved()) ? "" : "*"))));

    }

    private void tabSelectionChanged() {
        if (!tabbedPaneSelectionUpdate)
            return;

        int selectedIndex = tabbedPane.getSelectedIndex();
        // display nothing on the other tabs:
        range(0, tabbedPane.getTabCount())
                .filter(j -> j != selectedIndex)
                .forEachOrdered(j -> tabbedPane.setComponentAt(j, new JPanel()));
        if (selectedIndex < 0) {
            // nothing selected. probably, the last map was closed
            return;
        }
        MapModule module = tabbedPaneMapModules.get(selectedIndex);
        log.trace("Selected index of tab is now: {} with title:{}", selectedIndex, module.toString());
        if (module != controller.getMapModule()) {
            // we have to change the active map actively:
            controller.getMapModuleManager().changeToMapModule(module.toString());
        }
        // mScrollPane could be set invisible by JTabbedPane
        controller.getFrame().getScrollPane().setVisible(true);
        tabbedPane.setComponentAt(selectedIndex, controller.getFrame().getContentComponent());
        // double call, due to mac strangeness.
        controller.obtainFocusForSelected();
    }

    /**
     * Moves a tab from one position to another, preserving all tab properties.
     */
    public void moveTab(int src, int dst) {
        // Get all the properties
        Component comp = tabbedPane.getComponentAt(src);
        String label = tabbedPane.getTitleAt(src);
        Icon icon = tabbedPane.getIconAt(src);
        Icon iconDis = tabbedPane.getDisabledIconAt(src);
        String tooltip = tabbedPane.getToolTipTextAt(src);
        boolean enabled = tabbedPane.isEnabledAt(src);
        int keycode = tabbedPane.getMnemonicAt(src);
        int mnemonicLoc = tabbedPane.getDisplayedMnemonicIndexAt(src);
        Color fg = tabbedPane.getForegroundAt(src);
        Color bg = tabbedPane.getBackgroundAt(src);

        tabbedPaneSelectionUpdate = false;
        // Remove the tab
        tabbedPane.remove(src);
        // Add a new tab
        tabbedPane.insertTab(label, icon, comp, tooltip, dst);
        swap(tabbedPaneMapModules, src, dst);
        controller.getMapModuleManager().swapModules(src, dst);
        tabbedPane.setSelectedIndex(dst);
        tabbedPaneSelectionUpdate = true;

        // Restore all properties
        tabbedPane.setDisabledIconAt(dst, iconDis);
        tabbedPane.setEnabledAt(dst, enabled);
        tabbedPane.setMnemonicAt(dst, keycode);
        tabbedPane.setDisplayedMnemonicIndexAt(dst, mnemonicLoc);
        tabbedPane.setForegroundAt(dst, fg);
        tabbedPane.setBackgroundAt(dst, bg);
    }
}
