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
package freemind.modes.mindmapmode.services;

import freemind.common.XmlBindingTools;
import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.controller.actions.Base;
import freemind.controller.actions.MenuActionBase;
import freemind.controller.actions.MenuCategoryBase;
import freemind.controller.actions.MenuCheckedAction;
import freemind.controller.actions.MenuRadioAction;
import freemind.controller.actions.MenuSeparator;
import freemind.controller.actions.MenuStructure;
import freemind.controller.actions.MenuSubmenu;
import freemind.modes.mindmapmode.actions.HookAction;
import freemind.main.MindMapUtils;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Service for menu configuration and construction.
 * Extracted from MindMapController to reduce class size.
 */
@Slf4j
public class MenuConfigService {

    private final MindMapController controller;

    public MenuConfigService(MindMapController controller) {
        this.controller = controller;
    }

    /**
     * Populates the structured menu holder with all menu items, hooks, popup, toolbar,
     * patterns, and icons.
     */
    public void updateMenus(StructuredMenuHolder holder) {
        List<Object> objects = controller.getMenuStructure().getMenuCategoryList()
                .stream().map(mcb -> (Object) mcb).collect(Collectors.toList());

        processMenuCategory(holder, objects, "");

        MindMapHookFactory hookFactory = (MindMapHookFactory) controller.getHookFactory();
        for (HookAction action : controller.getHookActions()) {
            AbstractAction hookAction = (AbstractAction) action;
            String hookName = ((HookAction) hookAction).getHookName();
            hookFactory.decorateAction(hookName, hookAction);
            List<String> hookMenuPositions = hookFactory.getHookMenuPositions(hookName);
            for (String pos : hookMenuPositions) {
                holder.addMenuItem(hookFactory.getMenuItem(hookName, hookAction), pos);
            }
        }

        controller.getPopupMenuInternal().update(holder);
        controller.getToolBar().update(holder);

        String formatMenuString = MenuBar.FORMAT_MENU;
        createPatternSubMenu(holder, formatMenuString);

        addIconsToMenu(holder, MenuBar.INSERT_MENU + "icons");
    }

    /**
     * Adds icon actions and remove-icon actions to the given menu path.
     */
    public void addIconsToMenu(StructuredMenuHolder holder, String iconMenuString) {
        JMenu iconMenu = holder.addMenu(new JMenu(controller.getText("icon_menu")), iconMenuString + "/.");
        holder.addAction(controller.getActions().removeLastIconAction, iconMenuString + "/removeLastIcon");
        holder.addAction(controller.getActions().removeAllIconsAction, iconMenuString + "/removeAllIcons");
        holder.addSeparator(iconMenuString);
        for (int i = 0; i < controller.getActions().iconActions.size(); ++i) {
            holder.addAction(controller.getActions().iconActions.get(i), iconMenuString + "/" + i);
        }
    }

    /**
     * Creates the pattern sub-menu under the given format menu path.
     */
    public void createPatternSubMenu(StructuredMenuHolder holder, String formatMenuString) {
        for (int i = 0; i < controller.getActions().patterns.length; ++i) {
            JMenuItem item = holder.addAction(controller.getActions().patterns[i], formatMenuString + "patterns/patterns/" + i);
            item.setAccelerator(KeyStroke.getKeyStroke(
                    controller.getFrame().getAdjustableProperty("keystroke_apply_pattern_" + (i + 1))));
        }
    }

    /**
     * Parses menu structure XML from the given input stream.
     */
    public MenuStructure updateMenusFromXml(InputStream in) {
        MenuStructure menus = (MenuStructure) XmlBindingTools.getInstance().unMarshall(in);
        if (menus == null) {
            throw new IllegalArgumentException("Menu structure could not be read.");
        }
        return menus;
    }

    /**
     * Recursively processes menu categories, actions, and separators from the menu structure XML.
     */
    public void processMenuCategory(StructuredMenuHolder holder, List<Object> list, String category) {
        ButtonGroup buttonGroup = null;
        for (Object obj : list) {
            Object unwrapped = unwrapBaseObject(obj);

            if (unwrapped instanceof MenuCategoryBase) {
                MenuCategoryBase cat = (MenuCategoryBase) unwrapped;
                String newCategory = category + "/" + cat.getName();
                holder.addCategory(newCategory);
                if (cat instanceof MenuSubmenu) {
                    MenuSubmenu submenu = (MenuSubmenu) cat;
                    holder.addMenu(new JMenu(controller.getText(submenu.getNameRef())), newCategory + "/.");
                }

                List<?> baseList = cat.getBaseList();
                if (CollectionUtils.isNotEmpty(baseList)) {
                    List<Object> objects = baseList.stream().map(mcb -> (Object) mcb).collect(toList());
                    processMenuCategory(holder, objects, newCategory);
                }
            } else if (unwrapped instanceof MenuActionBase) {
                MenuActionBase action = (MenuActionBase) unwrapped;
                String field = action.getField();
                String name = action.getName();
                if (name == null) {
                    name = field;
                }
                String keystroke = action.getKeyRef();
                try {
                    Action theAction = (Action) MindMapUtils.getField(
                            new Object[]{controller, controller.getController()}, field);
                    if (theAction == null) {
                        log.warn("Menu action field '{}' resolved to null", field);
                        continue;
                    }
                    String theCategory = category + "/" + name;
                    if (unwrapped instanceof MenuCheckedAction) {
                        controller.addCheckBoxMenuItem(holder, theCategory, theAction, keystroke);
                    } else if (unwrapped instanceof MenuRadioAction) {
                        final JRadioButtonMenuItem item = (JRadioButtonMenuItem) controller.addRadioMenuItem(
                                holder, theCategory, theAction, keystroke,
                                ((MenuRadioAction) unwrapped).getSelected());
                        if (buttonGroup == null) {
                            buttonGroup = new ButtonGroup();
                        }
                        buttonGroup.add(item);
                    } else {
                        controller.addMenuItem(holder, theCategory, theAction, keystroke);
                    }
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            } else if (unwrapped instanceof MenuSeparator) {
                holder.addSeparator(category);
            }
        }
    }

    /**
     * Unwrap JiBX Base wrapper object to get the actual menu element.
     * The Base class is a choice wrapper generated by JiBX for xs:choice elements.
     */
    private Object unwrapBaseObject(Object obj) {
        if (obj instanceof Base) {
            Base base = (Base) obj;
            if (base.ifMenuCategory()) {
                return base.getMenuCategory();
            } else if (base.ifMenuSubmenu()) {
                return base.getMenuSubmenu();
            } else if (base.ifMenuAction()) {
                return base.getMenuAction();
            } else if (base.ifMenuCheckedAction()) {
                return base.getMenuCheckedAction();
            } else if (base.ifMenuRadioAction()) {
                return base.getMenuRadioAction();
            } else if (base.ifMenuSeparator()) {
                return base.getMenuSeparator();
            }
        }
        return obj;
    }
}
