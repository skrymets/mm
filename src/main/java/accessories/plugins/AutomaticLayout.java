/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
 */
/*
 * Created on 16.03.2004
 *
 */
package accessories.plugins;

import accessories.plugins.dialogs.ChooseFormatPopupDialog;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import freemind.common.*;
import freemind.controller.Controller;
import freemind.controller.actions.generated.instance.Pattern;
import freemind.controller.actions.generated.instance.Patterns;
import freemind.extensions.HookRegistration;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.StylePatternFactory;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;
import freemind.preferences.FreemindPropertyContributor;
import freemind.preferences.FreemindPropertyListener;
import freemind.preferences.layout.OptionPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author foltin
 * 
 */
public class AutomaticLayout extends PermanentMindMapNodeHookAdapter {

	private static final String AUTOMATIC_FORMAT_LEVEL = "automaticFormat_level";

	/**
	 * Registers the property pages.
	 * 
	 * @author foltin
	 * 
	 */
	public static class Registration implements HookRegistration {
		private AutomaticLayoutPropertyContributor mAutomaticLayoutPropertyContributor;

		private final MindMapController modeController;

		private static FreemindPropertyListener listener = null;

		public Registration(ModeController controller, MindMap map) {
			modeController = (MindMapController) controller;
		}

		static class MyFreemindPropertyListener implements
				FreemindPropertyListener {
			public void propertyChanged(String propertyName, String newValue,
					String oldValue) {
				if (propertyName.startsWith(AUTOMATIC_FORMAT_LEVEL)) {
					patterns = null;
				}
			}
		};

		public void register() {
			// add listener:
			if (listener == null) {
				listener = new MyFreemindPropertyListener();
			}
			Controller.addPropertyChangeListener(listener);

			mAutomaticLayoutPropertyContributor = new AutomaticLayoutPropertyContributor(
					modeController);
			OptionPanel.addContributor(mAutomaticLayoutPropertyContributor);
		}

		public void deRegister() {
			OptionPanel.removeContributor(mAutomaticLayoutPropertyContributor);
			Controller.removePropertyChangeListener(listener);
		}

	}

	/**
	 * Translates style pattern properties into strings.
	 * */
	static class StylePropertyTranslator implements TextTranslator {
		private final MindMapController controller;

		StylePropertyTranslator(MindMapController controller) {
			super();
			this.controller = controller;
		}

		public String getText(String pKey) {
			return controller.getText(pKey);
		}
	}

	/**
	 * Currently not used. Is useful if you want to make single patterns
	 * changeable.
	 * */
	public static class StylePatternProperty extends PropertyBean implements
			PropertyControl, ActionListener {

		String description;

		String label;

		String pattern;

		JButton mButton;

		private final MindMapController mindMapController;

		public StylePatternProperty(String description, String label,
				TextTranslator pTranslator, MindMapController pController) {
			super();
			this.description = description;
			this.label = label;
			mindMapController = pController;
			mButton = new JButton();
			mButton.addActionListener(this);
			pattern = null;
		}

		public String getDescription() {
			return description;
		}

		public String getLabel() {
			return label;
		}

		public void setValue(String value) {
			pattern = value;
			Pattern resultPattern = getPatternFromString();
			String patternString = StylePatternFactory.toString(resultPattern,
					new StylePropertyTranslator(mindMapController));
			mButton.setText(patternString);
			mButton.setToolTipText(patternString);
		}

		public String getValue() {
			return pattern;
		}

		public void layout(DefaultFormBuilder builder,
				TextTranslator pTranslator) {
			JLabel label = builder.append(pTranslator.getText(getLabel()),
					mButton);
			label.setToolTipText(pTranslator.getText(getDescription()));
			// add "reset to standard" popup:

		}

		public void actionPerformed(ActionEvent arg0) {
			// construct pattern:
			Pattern pat = getPatternFromString();
			ChooseFormatPopupDialog formatDialog = new ChooseFormatPopupDialog(
					mindMapController.getFrame().getJFrame(),
					mindMapController,
					"accessories/plugins/AutomaticLayout.properties_StyleDialogTitle",
					pat, null);
			formatDialog.setModal(true);
			formatDialog.setVisible(true);
			// process result:
			if (formatDialog.getResult() == ChooseFormatPopupDialog.OK) {
				Pattern resultPattern = formatDialog.getPattern();
				resultPattern.setName("dummy");
				pattern = XmlBindingTools.getInstance().marshall(resultPattern);
				setValue(pattern);
				firePropertyChangeEvent();
			}
		}

		private Pattern getPatternFromString() {
			return StylePatternFactory.getPatternFromString(pattern);
		}

		public void setEnabled(boolean pEnabled) {
			mButton.setEnabled(pEnabled);
		}

	}

	public static class StylePatternListProperty extends PropertyBean implements
			PropertyControl, ListSelectionListener {

		String description;

		String label;

		String patterns;

		JList<String> mList;

		boolean mDialogIsShown = false;

		private final TextTranslator mTranslator;

		private final MindMapController mindMapController;

		private DefaultListModel<String> mDefaultListModel;

		public StylePatternListProperty(String description, String label,
				TextTranslator pTranslator, MindMapController pController) {
			super();
			this.description = description;
			this.label = label;
			mTranslator = pTranslator;
			mindMapController = pController;
			mList = new JList<>();
			mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			mDefaultListModel = new DefaultListModel<>();
			mList.setModel(mDefaultListModel);
			mList.addListSelectionListener(this);
			patterns = null;
		}

		public String getDescription() {
			return description;
		}

		public String getLabel() {
			return label;
		}

		public void setValue(String value) {
			patterns = value;
			Patterns resultPatterns = getPatternsFromString();
			mDefaultListModel.clear();
			int j = 1;
			StylePropertyTranslator stylePropertyTranslator = new StylePropertyTranslator(
					mindMapController);
            for (Pattern pattern : resultPatterns.getPatternList()) {
                mDefaultListModel.addElement(mTranslator.getText("level" + j)
                        + ": "
                        + StylePatternFactory.toString(pattern, stylePropertyTranslator));
                j++;
            }
		}

		public String getValue() {
			return patterns;
		}

		public void layout(DefaultFormBuilder builder,
				TextTranslator pTranslator) {
			JLabel label = builder.append(pTranslator.getText(getLabel()));
			builder.append(new JLabel());
			label.setToolTipText(pTranslator.getText(getDescription()));
			builder.appendSeparator();
			builder.append(new JScrollPane(mList), 3);
			// builder.append(mList);
		}

		private Patterns getPatternsFromString() {
			return StylePatternFactory.getPatternsFromString(patterns);
		}

		public void setEnabled(boolean pEnabled) {
			mList.setEnabled(pEnabled);
		}

		public void valueChanged(ListSelectionEvent e) {
			// construct pattern:
			final Patterns pat = getPatternsFromString();
			JList<String> source = (JList<String>) e.getSource();
			if (source.getSelectedIndex() < 0)
				return;
			final Pattern choice = (Pattern) pat.getPattern(source
					.getSelectedIndex());
			final ChooseFormatPopupDialog formatDialog = new ChooseFormatPopupDialog(
					mindMapController.getFrame().getJFrame(),
					mindMapController,
					"accessories/plugins/AutomaticLayout.properties_StyleDialogTitle",
					choice, null);
			// FIXME: What's that? (fc, 8,4,2008).
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					if (mDialogIsShown)
						return;
					mDialogIsShown = true;
					try {
						formatDialog.setModal(true);
						formatDialog.setVisible(true);
						// process result:
						if (formatDialog.getResult() == ChooseFormatPopupDialog.OK) {
							formatDialog.getPattern(choice);
							patterns = XmlBindingTools.getInstance().marshall(
									pat);
							setValue(patterns);
							firePropertyChangeEvent();
						}
					} finally {
						mDialogIsShown = false;
					}
				}

			});
		}

	}

	private static final class AutomaticLayoutPropertyContributor implements
			FreemindPropertyContributor {

		private final MindMapController modeController;

		public AutomaticLayoutPropertyContributor(
				MindMapController modeController) {
			this.modeController = modeController;
		}

		public List<PropertyControl> getControls(TextTranslator pTextTranslator) {
			Vector<PropertyControl> controls = new Vector<>();
			controls.add(new OptionPanel.NewTabProperty(
					"accessories/plugins/AutomaticLayout.properties_PatternTabName"));
			controls.add(new SeparatorProperty(
					"accessories/plugins/AutomaticLayout.properties_PatternSeparatorName"));
			controls.add(new StylePatternListProperty("level",
					AUTOMATIC_FORMAT_LEVEL, pTextTranslator, modeController));
			return controls;
		}
	}

	private static Patterns patterns = null;

	/**
     * 
     */
	public AutomaticLayout() {
		super();

	}

	private void setStyle(MindMapNode node) {
		logger.trace("updating node id="
				+ node.getObjectId(getMindMapController()) + " and text:"
				+ node);
		int depth = depth(node);
		logger.trace("COLOR, depth=" + (depth));
		reloadPatterns();
		int myIndex = patterns.sizePatternList()- 1;
		if (depth < patterns.sizePatternList())
			myIndex = depth;
		Pattern p = (Pattern) patterns.getPattern(myIndex);
		getMindMapController().applyPattern(node, p);
	}

	private int depth(MindMapNode node) {
		if (node.isRoot())
			return 0;
		return depth(node.getParentNode()) + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.extensions.PermanentNodeHook#onAddChild(freemind.model.MindMapNode
	 * )
	 */
	public void onAddChildren(MindMapNode newChildNode) {
		logger.trace("onAddChildren " + newChildNode);
		super.onAddChild(newChildNode);
		setStyleRecursive(newChildNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.extensions.PermanentNodeHook#onUpdateChildrenHook(freemind.modes
	 * .MindMapNode)
	 */
	public void onUpdateChildrenHook(MindMapNode updatedNode) {
		super.onUpdateChildrenHook(updatedNode);
		setStyleRecursive(updatedNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.PermanentNodeHook#onUpdateNodeHook()
	 */
	public void onUpdateNodeHook() {
		super.onUpdateNodeHook();
		setStyle(getNode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.NodeHook#invoke(freemind.model.MindMapNode)
	 */
	public void invoke(MindMapNode node) {
		super.invoke(node);
		setStyleRecursive(node);
	}

	/** get styles from preferences: */
	private void reloadPatterns() {
		if (patterns == null) {
			String property = getMindMapController().getFrame().getProperty(
					AUTOMATIC_FORMAT_LEVEL);
			patterns = StylePatternFactory.getPatternsFromString(property);
		}
	}

	/**
     */
	private void setStyleRecursive(MindMapNode node) {
		logger.trace("setStyle " + node);
		setStyle(node);
		// recurse:
		for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext();) {
			MindMapNode child = i.next();
			invoke(child);
		}
	}

}
