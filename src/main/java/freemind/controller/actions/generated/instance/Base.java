
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:group xmlns:xs="http://www.w3.org/2001/XMLSchema" name="base">
 *   &lt;xs:choice>
 *     &lt;xs:element ref="menu_category"/>
 *     &lt;xs:element ref="menu_submenu"/>
 *     &lt;xs:element ref="menu_action"/>
 *     &lt;xs:element ref="menu_checked_action"/>
 *     &lt;xs:element ref="menu_radio_action"/>
 *     &lt;xs:element ref="menu_separator"/>
 *   &lt;/xs:choice>
 * &lt;/xs:group>
 * </pre>
 */
public class Base
{
    private int choiceSelect = -1;
    /** 
     * ChoiceSelect value when menuCategory is set
     */
    public static final int MENU_CATEGORY_CHOICE = 0;
    /** 
     * ChoiceSelect value when menuSubmenu is set
     */
    public static final int MENU_SUBMENU_CHOICE = 1;
    /** 
     * ChoiceSelect value when menuAction is set
     */
    public static final int MENU_ACTION_CHOICE = 2;
    /** 
     * ChoiceSelect value when menuCheckedAction is set
     */
    public static final int MENU_CHECKED_ACTION_CHOICE = 3;
    /** 
     * ChoiceSelect value when menuRadioAction is set
     */
    public static final int MENU_RADIO_ACTION_CHOICE = 4;
    /** 
     * ChoiceSelect value when menuSeparator is set
     */
    public static final int MENU_SEPARATOR_CHOICE = 5;
    private MenuCategoryBase menuCategory;
    private MenuSubmenu menuSubmenu;
    private MenuAction menuAction;
    private MenuCheckedAction menuCheckedAction;
    private MenuRadioAction menuRadioAction;
    private MenuSeparator menuSeparator;

    private void setChoiceSelect(int choice) {
        choiceSelect = choice;
    }

    /** 
     * Clear the choice selection.
     */
    public void clearChoiceSelect() {
        choiceSelect = -1;
    }

    /** 
     * Get the current choice state.
     * @return state
     */
    public int stateChoiceSelect() {
        return choiceSelect;
    }

    /** 
     * Check if MenuCategory is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifMenuCategory() {
        return choiceSelect == MENU_CATEGORY_CHOICE;
    }

    /** 
     * Get the 'menu_category' element value.
     * 
     * @return value
     */
    public MenuCategoryBase getMenuCategory() {
        return menuCategory;
    }

    /** 
     * Set the 'menu_category' element value.
     * 
     * @param menuCategory
     */
    public void setMenuCategory(MenuCategoryBase menuCategory) {
        choiceSelect = MENU_CATEGORY_CHOICE;
        this.menuCategory = menuCategory;
    }

    /** 
     * Check if MenuSubmenu is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifMenuSubmenu() {
        return choiceSelect == MENU_SUBMENU_CHOICE;
    }

    /** 
     * Get the 'menu_submenu' element value.
     * 
     * @return value
     */
    public MenuSubmenu getMenuSubmenu() {
        return menuSubmenu;
    }

    /** 
     * Set the 'menu_submenu' element value.
     * 
     * @param menuSubmenu
     */
    public void setMenuSubmenu(MenuSubmenu menuSubmenu) {
        choiceSelect = MENU_SUBMENU_CHOICE;
        this.menuSubmenu = menuSubmenu;
    }

    /** 
     * Check if MenuAction is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifMenuAction() {
        return choiceSelect == MENU_ACTION_CHOICE;
    }

    /** 
     * Get the 'menu_action' element value.
     * 
     * @return value
     */
    public MenuAction getMenuAction() {
        return menuAction;
    }

    /** 
     * Set the 'menu_action' element value.
     * 
     * @param menuAction
     */
    public void setMenuAction(MenuAction menuAction) {
        choiceSelect = MENU_ACTION_CHOICE;
        this.menuAction = menuAction;
    }

    /** 
     * Check if MenuCheckedAction is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifMenuCheckedAction() {
        return choiceSelect == MENU_CHECKED_ACTION_CHOICE;
    }

    /** 
     * Get the 'menu_checked_action' element value.
     * 
     * @return value
     */
    public MenuCheckedAction getMenuCheckedAction() {
        return menuCheckedAction;
    }

    /** 
     * Set the 'menu_checked_action' element value.
     * 
     * @param menuCheckedAction
     */
    public void setMenuCheckedAction(MenuCheckedAction menuCheckedAction) {
        choiceSelect = MENU_CHECKED_ACTION_CHOICE;
        this.menuCheckedAction = menuCheckedAction;
    }

    /** 
     * Check if MenuRadioAction is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifMenuRadioAction() {
        return choiceSelect == MENU_RADIO_ACTION_CHOICE;
    }

    /** 
     * Get the 'menu_radio_action' element value.
     * 
     * @return value
     */
    public MenuRadioAction getMenuRadioAction() {
        return menuRadioAction;
    }

    /** 
     * Set the 'menu_radio_action' element value.
     * 
     * @param menuRadioAction
     */
    public void setMenuRadioAction(MenuRadioAction menuRadioAction) {
        choiceSelect = MENU_RADIO_ACTION_CHOICE;
        this.menuRadioAction = menuRadioAction;
    }

    /** 
     * Check if MenuSeparator is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifMenuSeparator() {
        return choiceSelect == MENU_SEPARATOR_CHOICE;
    }

    /** 
     * Get the 'menu_separator' element value.
     * 
     * @return value
     */
    public MenuSeparator getMenuSeparator() {
        return menuSeparator;
    }

    /** 
     * Set the 'menu_separator' element value.
     * 
     * @param menuSeparator
     */
    public void setMenuSeparator(MenuSeparator menuSeparator) {
        choiceSelect = MENU_SEPARATOR_CHOICE;
        this.menuSeparator = menuSeparator;
    }
}
