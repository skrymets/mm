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
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter;

import freemind.common.NamedObject;
import freemind.controller.Controller;
import freemind.controller.filter.condition.Condition;
import freemind.controller.filter.condition.ConditionNotSatisfiedDecorator;
import freemind.controller.filter.condition.ConjunctConditions;
import freemind.controller.filter.condition.DisjunctConditions;
import freemind.controller.filter.util.ExtendedComboBoxModel;
import freemind.controller.filter.util.SortedComboBoxModel;
import freemind.controller.filter.util.SortedListModel;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.MindIcon;
import freemind.modes.attributes.Attribute;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Iterator;

/**
 * @author dimitri
 * 
 */
@SuppressWarnings("serial")
public class FilterComposerDialog extends JDialog {
	private static final Dimension maxButtonDimension = new Dimension(1000,
			1000);

	/**
	 * @author dimitri 06.05.2005
	 */
	private class AddConditionAction extends AbstractAction {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		AddConditionAction() {
			super();
			Tools.setLabelAndMnemonic(this, Resources.getInstance()
					.getResourceString("filter_add"));
		}

		public void actionPerformed(ActionEvent e) {
			Condition newCond = null;
			String value;
			try {
				value = getAttributeValue();
			} catch (NullPointerException ex) {
				return;
			}
			NamedObject simpleCond = (NamedObject) simpleCondition
					.getSelectedItem();
			boolean ignoreCase = caseInsensitive.isSelected();

			Object selectedItem = attributes.getSelectedItem();
			if (selectedItem instanceof NamedObject) {
				NamedObject attribute = (NamedObject) selectedItem;
				newCond = FilterController.getConditionFactory().createCondition(attribute,
						simpleCond, value, ignoreCase);
			} else {
				String attribute = selectedItem.toString();
				newCond = FilterController.getConditionFactory().createAttributeCondition(
						attribute, simpleCond, value, ignoreCase);
			}
			DefaultComboBoxModel<Condition> model = (DefaultComboBoxModel<Condition>) conditionList.getModel();
			if (newCond != null)
				model.addElement(newCond);
			if (values.isEditable()) {
				Object item = values.getSelectedItem();
				if (item != null && !item.equals("")) {
					values.removeItem(item);
					values.insertItemAt(item, 0);
					values.setSelectedIndex(0);
					if (values.getItemCount() >= 10)
						values.removeItemAt(9);
				}
			}
			validate();
		}
	}

	private class DeleteConditionAction extends AbstractAction {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		DeleteConditionAction() {
			super();
			Tools.setLabelAndMnemonic(this, Resources.getInstance()
					.getResourceString("filter_delete"));
		}

		public void actionPerformed(ActionEvent e) {
			DefaultComboBoxModel<Condition> model = (DefaultComboBoxModel<Condition>) conditionList
					.getModel();
			final int minSelectionIndex = conditionList.getMinSelectionIndex();
			int selectedIndex;
			while (0 <= (selectedIndex = conditionList.getSelectedIndex())) {
				model.removeElementAt(selectedIndex);
			}
			final int size = conditionList.getModel().getSize();
			if (size > 0) {
				conditionList
						.setSelectedIndex(minSelectionIndex < size ? minSelectionIndex
								: size - 1);
			}
			validate();
		}
	}

	private class CreateNotSatisfiedConditionAction extends AbstractAction {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		CreateNotSatisfiedConditionAction() {
			super();
			Tools.setLabelAndMnemonic(this, Resources.getInstance()
					.getResourceString("filter_not"));
		}

		public void actionPerformed(ActionEvent e) {
			int min = conditionList.getMinSelectionIndex();
			if (min >= 0) {
				int max = conditionList.getMinSelectionIndex();
				if (min == max) {
					Condition oldCond = (Condition) conditionList
							.getSelectedValue();
					Condition newCond = new ConditionNotSatisfiedDecorator(
							oldCond);
					DefaultComboBoxModel<Condition> model = (DefaultComboBoxModel<Condition>) conditionList.getModel();
					model.addElement(newCond);
					validate();

				}
			}
		}
	}

	private class CreateConjunctConditionAction extends AbstractAction {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		CreateConjunctConditionAction() {
			super();
			Tools.setLabelAndMnemonic(this, Resources.getInstance()
					.getResourceString("filter_and"));
		}

		public void actionPerformed(ActionEvent e) {
			Object[] selectedValues = conditionList.getSelectedValues();
			if (selectedValues.length < 2)
				return;
			Condition newCond = new ConjunctConditions(selectedValues);
			DefaultComboBoxModel<Condition> model = (DefaultComboBoxModel<Condition>) conditionList.getModel();
			model.addElement(newCond);
			validate();

		}
	}

	private class CreateDisjunctConditionAction extends AbstractAction {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		CreateDisjunctConditionAction() {
			super();
			Tools.setLabelAndMnemonic(this, Resources.getInstance()
					.getResourceString("filter_or"));
		}

		public void actionPerformed(ActionEvent e) {
			Object[] selectedValues = conditionList.getSelectedValues();
			if (selectedValues.length < 2)
				return;
			Condition newCond = new DisjunctConditions(selectedValues);
			DefaultComboBoxModel<Condition> model = (DefaultComboBoxModel<Condition>) conditionList.getModel();
			model.addElement(newCond);
			validate();

		}
	}

	private class ConditionListSelectionListener implements
			ListSelectionListener, ListDataListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.
		 * event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent e) {

			if (conditionList.getMinSelectionIndex() == -1) {
				btnNot.setEnabled(false);
				btnAnd.setEnabled(false);
				btnOr.setEnabled(false);
				btnDelete.setEnabled(false);
				return;
			} else if (conditionList.getMinSelectionIndex() == conditionList
					.getMaxSelectionIndex()) {
				btnNot.setEnabled(true);
				btnAnd.setEnabled(false);
				btnOr.setEnabled(false);
				btnDelete.setEnabled(true);
				return;
			} else {
				btnNot.setEnabled(false);
				btnAnd.setEnabled(true);
				btnOr.setEnabled(true);
				btnDelete.setEnabled(true);
			}
		}

		public void intervalAdded(ListDataEvent e) {
			conditionList.setSelectedIndex(e.getIndex0());
		}

		public void intervalRemoved(ListDataEvent e) {
		}

		public void contentsChanged(ListDataEvent e) {
		}

	}

	private class ConditionListMouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						if (selectCondition()) {
							dispose();
						}
					}
				});
			}
		}
	}

	private class CloseAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == btnOK || source == btnApply)
				applyChanges();
			if (source == btnOK || source == btnCancel)
				dispose();
			else
				initInternalConditionModel();
		}
	}

	static private class MindMapFilterFileFilter extends FileFilter {
		static FileFilter filter = new MindMapFilterFileFilter();

		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			String extension = Tools.getExtension(f.getName());
			if (extension != null) {
				if (extension
						.equals(FilterController.FREEMIND_FILTER_EXTENSION_WITHOUT_DOT)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		public String getDescription() {
			return Resources.getInstance().getResourceString(
					"mindmaps_filter_desc");
		}
	}

	protected FreeMindFileDialog getFileChooser() {
		FreeMindFileDialog chooser = Resources.getInstance().getStandardFileChooser(MindMapFilterFileFilter.filter);
		return chooser;
	}

	private class SaveAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FreeMindFileDialog chooser = getFileChooser();
			chooser.setDialogTitle(Resources.getInstance().getResourceString(
					"save_as"));
			int returnVal = chooser.showSaveDialog(FilterComposerDialog.this);
			if (returnVal != JFileChooser.APPROVE_OPTION) {// not ok pressed
				return;
			}

			// |= Pressed O.K.
			try {
				File f = chooser.getSelectedFile();
				String canonicalPath = f.getCanonicalPath();
				final String suffix = '.' + FilterController.FREEMIND_FILTER_EXTENSION_WITHOUT_DOT;
				if (!canonicalPath.endsWith(suffix)) {
					canonicalPath = canonicalPath + suffix;
				}
				mFilterController.saveConditions(internalConditionsModel, canonicalPath);
			} catch (Exception ex) {
				handleSavingException(ex);
			}
		}

		private void handleSavingException(Exception ex) {
			// TODO Auto-generated method stub

		}

	}

	private class LoadAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FreeMindFileDialog chooser = getFileChooser();
			int returnVal = chooser.showOpenDialog(FilterComposerDialog.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					File theFile = chooser.getSelectedFile();
					mFilterController.loadConditions(internalConditionsModel,
							theFile.getCanonicalPath());
				} catch (Exception ex) {
					handleLoadingException(ex);
				}
			}
		}

		private void handleLoadingException(Exception ex) {
			// TODO Auto-generated method stub

		}

	}

	private static final int NODE_POSITION = 0;
	private static final int ICON_POSITION = 1;

	private class SimpleConditionChangeListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				boolean considerValue = true;
				caseInsensitive.setEnabled(considerValue);
				values.setEnabled(considerValue);
			}
		}
	}

	private class SelectedAttributeChangeListener implements ItemListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.
		 * event.ListSelectionEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (attributes.getSelectedIndex() == NODE_POSITION) {
					simpleCondition.setModel(simpleNodeConditionComboBoxModel);
					simpleCondition.setEnabled(true);
					
					values.setEditable(true);
					values.setEnabled(true);
					nodes.setExtensionList(null);
					values.setModel(nodes);
					
					caseInsensitive.setEnabled(true);
					return;
				}
				if (attributes.getSelectedIndex() == ICON_POSITION) {
					simpleCondition.setModel(simpleIconConditionComboBoxModel);
                    simpleCondition.setEnabled(true);
					
                    values.setEditable(false);
					values.setEnabled(true);
					values.setModel(icons);
					if (icons.getSize() >= 1) {
						values.setSelectedIndex(0);
					}
					
					caseInsensitive.setEnabled(false);
					return;
				}
				if (attributes.getSelectedIndex() > NODE_POSITION) {
					final String attributeName = attributes.getSelectedItem()
							.toString();
					SortedComboBoxModel attributesInMap = new SortedComboBoxModel();
					addAttributeValuesRecursively(attributeName, mController.getMap().getRootNode(), attributesInMap);
					nodes.setExtensionList(attributesInMap);
					values.setModel(nodes);
					if (values.getSelectedItem() != null) {
						if (nodes.getSize() >= 1) {
							values.setSelectedIndex(0);
						} else {
							values.setSelectedItem(null);
						}
					}
					if (simpleCondition.getModel() != simpleAttributeConditionComboBoxModel) {
						simpleCondition
								.setModel(simpleAttributeConditionComboBoxModel);
						simpleCondition.setSelectedIndex(0);
					}
					if (simpleCondition.getSelectedIndex() == 0) {
						caseInsensitive.setEnabled(false);
						values.setEnabled(false);
					}
					values.setEditable(true);
					simpleCondition.setEnabled(true);
					return;
				}
			}
		}
	}

	private FilterController mFilterController;
	private JList<Condition> conditionList;
	private JComboBox<Condition> simpleCondition;
	private JComboBox values;
	private JComboBox attributes;
	private JButton btnAdd;
	private JButton btnNot;
	private JButton btnAnd;
	private JButton btnOr;
	private JButton btnDelete;
	private JCheckBox caseInsensitive;
	private ExtendedComboBoxModel icons;
	private ExtendedComboBoxModel nodes;
	private DefaultComboBoxModel<Condition> simpleNodeConditionComboBoxModel;
	private DefaultComboBoxModel<Condition> simpleIconConditionComboBoxModel;
	private ExtendedComboBoxModel filteredAttributeComboBoxModel;
	private DefaultComboBoxModel internalConditionsModel;
	private ComboBoxModel externalConditionsModel;
	private JButton btnOK;
	private JButton btnApply;
	private JButton btnCancel;
	private JButton btnSave;
	private JButton btnLoad;
	private ConditionListSelectionListener conditionListListener;
	private Controller mController;
	private DefaultComboBoxModel simpleAttributeConditionComboBoxModel;

	public FilterComposerDialog(Controller controller, final FilterToolbar pFilterToolbar) {
		super(controller.getJFrame(), controller.getResourceString("filter_dialog"));
		mController = controller;

		this.mFilterController = controller.getFilterController();

		final Box simpleConditionBox = Box.createHorizontalBox();
		simpleConditionBox.setBorder(new EmptyBorder(5, 0, 5, 0));
		getContentPane().add(simpleConditionBox, BorderLayout.NORTH);

		attributes = new JComboBox<>();
		filteredAttributeComboBoxModel = new ExtendedComboBoxModel(FilterController.getConditionFactory().getAttributeConditionNames());
		getAttributesFromMap(controller.getMap());
		attributes.setModel(filteredAttributeComboBoxModel);
		attributes.addItemListener(new SelectedAttributeChangeListener());
		simpleConditionBox.add(Box.createHorizontalGlue());
		simpleConditionBox.add(attributes);
		attributes.setRenderer(mFilterController.getConditionRenderer());

		simpleNodeConditionComboBoxModel = new DefaultComboBoxModel(FilterController.getConditionFactory().getNodeConditionNames());
		simpleIconConditionComboBoxModel = new DefaultComboBoxModel(FilterController.getConditionFactory().getIconConditionNames());

		simpleCondition = new JComboBox<>();
		simpleCondition.setModel(simpleNodeConditionComboBoxModel);
		simpleCondition.addItemListener(new SimpleConditionChangeListener());
		simpleConditionBox.add(Box.createHorizontalGlue());
		simpleConditionBox.add(simpleCondition);
		simpleCondition.setRenderer(mFilterController.getConditionRenderer());
		
		simpleAttributeConditionComboBoxModel = new DefaultComboBoxModel(FilterController
				.getConditionFactory().getAttributeConditionNames());

		nodes = new ExtendedComboBoxModel();
		values.setModel(nodes);
		simpleConditionBox.add(Box.createHorizontalGlue());
		simpleConditionBox.add(values);
		values.setRenderer(mFilterController.getConditionRenderer());
		values.setEditable(true);

		icons = new ExtendedComboBoxModel();
		icons.setExtensionList(controller.getMap().getIcons());

		caseInsensitive = new JCheckBox();
		simpleConditionBox.add(Box.createHorizontalGlue());
		simpleConditionBox.add(caseInsensitive);
		caseInsensitive.setText(Resources.getInstance().getResourceString(
				"filter_ignore_case"));

		final Box conditionButtonBox = Box.createVerticalBox();
		conditionButtonBox.setBorder(new EmptyBorder(0, 10, 0, 10));
		getContentPane().add(conditionButtonBox, BorderLayout.EAST);

		btnAdd = new JButton(new AddConditionAction());
		btnAdd.setMaximumSize(maxButtonDimension);
		conditionButtonBox.add(Box.createVerticalGlue());
		conditionButtonBox.add(btnAdd);

		btnNot = new JButton(new CreateNotSatisfiedConditionAction());
		conditionButtonBox.add(Box.createVerticalGlue());
		btnNot.setMaximumSize(maxButtonDimension);
		conditionButtonBox.add(btnNot);
		btnNot.setEnabled(false);

		btnAnd = new JButton(new CreateConjunctConditionAction());
		conditionButtonBox.add(Box.createVerticalGlue());
		btnAnd.setMaximumSize(maxButtonDimension);
		conditionButtonBox.add(btnAnd);
		btnAnd.setEnabled(false);

		btnOr = new JButton(new CreateDisjunctConditionAction());
		conditionButtonBox.add(Box.createVerticalGlue());
		btnOr.setMaximumSize(maxButtonDimension);
		conditionButtonBox.add(btnOr);
		btnOr.setEnabled(false);

		btnDelete = new JButton(new DeleteConditionAction());
		btnDelete.setEnabled(false);
		conditionButtonBox.add(Box.createVerticalGlue());
		btnDelete.setMaximumSize(maxButtonDimension);
		conditionButtonBox.add(btnDelete);
		conditionButtonBox.add(Box.createVerticalGlue());

		final Box controllerBox = Box.createHorizontalBox();
		controllerBox.setBorder(new EmptyBorder(5, 0, 5, 0));
		getContentPane().add(controllerBox, BorderLayout.SOUTH);

		CloseAction closeAction = new CloseAction();

		btnOK = new JButton();
		Tools.setLabelAndMnemonic(btnOK, Resources.getInstance()
				.getResourceString("ok"));
		btnOK.addActionListener(closeAction);
		btnOK.setMaximumSize(maxButtonDimension);

		btnApply = new JButton();
		Tools.setLabelAndMnemonic(btnApply, Resources.getInstance()
				.getResourceString("apply"));
		btnApply.addActionListener(closeAction);
		btnApply.setMaximumSize(maxButtonDimension);

		btnCancel = new JButton();
		Tools.setLabelAndMnemonic(btnCancel, Resources.getInstance()
				.getResourceString("cancel"));
		btnCancel.addActionListener(closeAction);
		btnCancel.setMaximumSize(maxButtonDimension);

		controllerBox.add(Box.createHorizontalGlue());
		controllerBox.add(btnOK);
		controllerBox.add(Box.createHorizontalGlue());
		controllerBox.add(btnApply);
		controllerBox.add(Box.createHorizontalGlue());
		controllerBox.add(btnCancel);
		controllerBox.add(Box.createHorizontalGlue());

		if (!controller.getFrame().isApplet()) {
			ActionListener saveAction = new SaveAction();
			btnSave = new JButton();
			Tools.setLabelAndMnemonic(btnSave, Resources.getInstance()
					.getResourceString("save"));
			btnSave.addActionListener(saveAction);
			btnSave.setMaximumSize(maxButtonDimension);

			ActionListener loadAction = new LoadAction();
			btnLoad = new JButton();
			Tools.setLabelAndMnemonic(btnLoad, Resources.getInstance()
					.getResourceString("load"));
			btnLoad.addActionListener(loadAction);
			btnLoad.setMaximumSize(maxButtonDimension);

			controllerBox.add(btnSave);
			controllerBox.add(Box.createHorizontalGlue());
			controllerBox.add(btnLoad);
			controllerBox.add(Box.createHorizontalGlue());
		}
		conditionList = new JList<>();
		conditionList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		conditionList.setCellRenderer(mFilterController.getConditionRenderer());
		conditionList.setLayoutOrientation(JList.VERTICAL);
		conditionList.setAlignmentX(Component.LEFT_ALIGNMENT);
		conditionListListener = new ConditionListSelectionListener();
		conditionList.addListSelectionListener(conditionListListener);

		conditionList.addMouseListener(new ConditionListMouseListener());

		final JScrollPane conditionScrollPane = new JScrollPane(conditionList);
		JLabel conditionColumnHeader = new JLabel(Resources.getInstance()
				.getResourceString("filter_conditions"));
		conditionColumnHeader.setHorizontalAlignment(JLabel.CENTER);
		conditionScrollPane.setColumnHeaderView(conditionColumnHeader);
		conditionScrollPane.setPreferredSize(new Dimension(500, 200));
		getContentPane().add(conditionScrollPane, BorderLayout.CENTER);

		Tools.addEscapeActionToDialog(this);
		pack();
	}

	private void getAttributesFromMap(MindMap map) {
		if(map != null) {
			// gather attributes in the map:
			SortedListModel attributesInMap = new SortedComboBoxModel();
			addAttributeKeysRecursively(map.getRootNode(), attributesInMap);
			filteredAttributeComboBoxModel.setExtensionList(attributesInMap);
		} else {
			filteredAttributeComboBoxModel.setExtensionList(null);
		}
	}

	private void addAttributeKeysRecursively(MindMapNode pNode, SortedListModel pAttributesInMap) {
		for (String key : pNode.getAttributeKeyList()) {
			pAttributesInMap.add(key);
		}
		for (Iterator it = pNode.getChildren().iterator(); it.hasNext();) {
			MindMapNode child = (MindMapNode) it.next();
			addAttributeKeysRecursively(child, pAttributesInMap);
		}
	}

	private void addAttributeValuesRecursively(String pKey, MindMapNode pNode, SortedListModel pAttributesInMap) {
		for (Attribute attr : pNode.getAttributes()) {
			if(Tools.safeEquals(attr.getName(), pKey)){
				pAttributesInMap.add(attr.getValue());
			}
		}
		for (Iterator it = pNode.getChildren().iterator(); it.hasNext();) {
			MindMapNode child = (MindMapNode) it.next();
			addAttributeValuesRecursively(pKey, child, pAttributesInMap);
		}
	}
	
	private String getAttributeValue() {
		if (attributes.getSelectedIndex() == ICON_POSITION) {
			MindIcon mi = (MindIcon) values.getSelectedItem();
			return mi.getName();
		}
		Object item = values.getSelectedItem();
		return item != null ? item.toString() : "";
	}

	/**
     */
	void mapChanged(MindMap newMap) {
		if (newMap != null) {
			icons.setExtensionList(newMap.getIcons());
			if (icons.getSize() >= 1 && values.getModel() == icons) {
				values.setSelectedIndex(0);
			} else {
				values.setSelectedIndex(-1);
				if (values.getModel() == icons) {
					values.setSelectedItem(null);
				}
			}
			if (attributes.getSelectedIndex() > 1)
				attributes.setSelectedIndex(0);
			getAttributesFromMap(newMap);
		} else {
			icons.setExtensionList(null);
			values.setSelectedIndex(-1);
			attributes.setSelectedIndex(0);
			filteredAttributeComboBoxModel.setExtensionList(null);
		}
	}

	private boolean selectCondition() {
		int min = conditionList.getMinSelectionIndex();
		if (min >= 0) {
			int max = conditionList.getMinSelectionIndex();
			if (min == max) {
				applyChanges();
				return true;
			}
		}
		return false;
	}

	/**
     */
	public void setSelectedItem(Object selectedItem) {
		conditionList.setSelectedValue(selectedItem, true);

	}

	public void show() {
		initInternalConditionModel();
		super.show();
	}

	private void initInternalConditionModel() {
		externalConditionsModel = mFilterController.getFilterConditionModel();
		if (internalConditionsModel == null) {
			internalConditionsModel = new DefaultComboBoxModel<>();
			internalConditionsModel.addListDataListener(conditionListListener);
			conditionList.setModel(internalConditionsModel);
		} else {
			internalConditionsModel.removeAllElements();
		}
		int index = -1;
		for (int i = 2; i < externalConditionsModel.getSize(); i++) {
			final Object element = externalConditionsModel.getElementAt(i);
			internalConditionsModel.addElement(element);
			if (element == externalConditionsModel.getSelectedItem()) {
				index = i - 2;
			}
		}
		if (index >= 0) {
			conditionList.setSelectedIndex(index);
		} else {
			conditionList.clearSelection();
		}
	}

	private void applyChanges() {
		internalConditionsModel.setSelectedItem(conditionList
				.getSelectedValue());
		internalConditionsModel.removeListDataListener(conditionListListener);
		mFilterController.setFilterConditionModel(internalConditionsModel);
		internalConditionsModel = null;
	}
}
