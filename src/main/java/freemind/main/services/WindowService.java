/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
package freemind.main.services;

import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.main.Tools;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

/**
 * Service for managing the split pane layout within the FreeMind window.
 * Extracted from FreeMind to reduce class complexity.
 */
@Slf4j
public class WindowService {

    private static final String SPLIT_PANE_POSITION = "split_pane_position";

    private static final String SPLIT_PANE_LAST_POSITION = "split_pane_last_position";

    private final FreeMind frame;

    private final JScrollPane scrollPane;

    private final JTabbedPane tabbedPane;

    private JSplitPane splitPane;

    private JComponent contentComponent;

    public WindowService(FreeMind frame, JScrollPane scrollPane, JTabbedPane tabbedPane) {
        this.frame = frame;
        this.scrollPane = scrollPane;
        this.tabbedPane = tabbedPane;
        this.contentComponent = scrollPane;
    }

    public JComponent getContentComponent() {
        return contentComponent;
    }

    /**
     * Inserts a component into a split pane alongside the main scroll pane.
     * If the split pane already exists, returns the existing one.
     */
    public JSplitPane insertComponentIntoSplitPane(JComponent pMindMapComponent) {
        if (splitPane != null) {
            return splitPane;
        }
        removeContentComponent();
        int splitType = JSplitPane.VERTICAL_SPLIT;
        String splitProperty = frame.getProperty(FreeMind.J_SPLIT_PANE_SPLIT_TYPE);
        if (Objects.equals(splitProperty, FreeMind.HORIZONTAL_SPLIT_RIGHT)) {
            splitType = JSplitPane.HORIZONTAL_SPLIT;
        } else if (Objects.equals(splitProperty, FreeMind.VERTICAL_SPLIT_BELOW)) {
            // default
        } else {
            log.warn("Split type not known: {}", splitProperty);
        }
        splitPane = new JSplitPane(splitType, scrollPane, pMindMapComponent);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(false);

        // This means that the mind map area gets all the space that results from resizing the window.
        splitPane.setResizeWeight(1.0d);

        // split panes eat F8 and F6. This is corrected here.
        Tools.correctJSplitPaneKeyMap();
        contentComponent = splitPane;

        setContentComponent();
        setSplitLocation();
        // after making this window visible, the size is adjusted. To get the right split location, we postpone this.
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent pE) {
                setSplitLocation();
                frame.removeComponentListener(this);
            }
        });
        return splitPane;
    }

    /**
     * Removes the split pane, restoring the scroll pane as the sole content component.
     */
    public void removeSplitPane() {
        if (splitPane == null) {
            return;
        }

        frame.setProperty(SPLIT_PANE_POSITION, "" + splitPane.getDividerLocation());
        frame.setProperty(SPLIT_PANE_LAST_POSITION, "" + splitPane.getLastDividerLocation());
        removeContentComponent();
        contentComponent = scrollPane;
        setContentComponent();
        splitPane = null;
    }

    private void setSplitLocation() {
        int splitPanePosition = frame.getIntProperty(SPLIT_PANE_POSITION, -1);
        int lastSplitPanePosition = frame.getIntProperty(SPLIT_PANE_LAST_POSITION, -1);

        if (splitPane != null && splitPanePosition != -1 && lastSplitPanePosition != -1) {
            splitPane.setDividerLocation(splitPanePosition);
            splitPane.setLastDividerLocation(lastSplitPanePosition);
        }
    }

    private void removeContentComponent() {
        if (tabbedPane == null) {
            frame.getContentPane().remove(contentComponent);
            frame.getRootPane().revalidate();
        } else {
            if (tabbedPane.getSelectedIndex() >= 0) {
                tabbedPane.setComponentAt(tabbedPane.getSelectedIndex(), new JPanel());
            }
        }
    }

    private void setContentComponent() {
        if (tabbedPane == null) {
            frame.getContentPane().add(contentComponent, BorderLayout.CENTER);
            frame.getRootPane().revalidate();
        } else {
            if (tabbedPane.getSelectedIndex() >= 0) {
                tabbedPane.setComponentAt(tabbedPane.getSelectedIndex(), contentComponent);
            }
        }
    }
}
