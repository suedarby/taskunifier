/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.commons.renderers.ModelListCellRenderer;

public final class ComponentFactory {
	
	private ComponentFactory() {

	}
	
	public static void createRepeatComboBox(JComboBox repeatComboBox) {
		repeatComboBox.setEditable(true);
		
		final JTextField repeatTextField = (JTextField) repeatComboBox.getEditor().getEditorComponent();
		repeatTextField.getDocument().addDocumentListener(
				new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent arg0) {
						this.update();
					}
					
					@Override
					public void insertUpdate(DocumentEvent arg0) {
						this.update();
					}
					
					@Override
					public void changedUpdate(DocumentEvent arg0) {
						this.update();
					}
					
					private void update() {
						if (SynchronizerUtils.getPlugin().getSynchronizerApi().isValidRepeatValue(
								repeatTextField.getText()))
							repeatTextField.setForeground(Color.BLACK);
						else
							repeatTextField.setForeground(Color.RED);
					}
					
				});
	}
	
	public static JComboBox createModelComboBox(ComboBoxModel model) {
		JComboBox comboBox = new JComboBox();
		
		if (model != null)
			comboBox.setModel(model);
		
		comboBox.setRenderer(new ModelListCellRenderer());
		
		return comboBox;
	}
	
	public static JPanel createSearchField(JTextField textField) {
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			textField.putClientProperty("JTextField.variant", "search");
			
			JPanel panel = new JPanel(new BorderLayout());
			panel.setOpaque(false);
			panel.add(textField, BorderLayout.CENTER);
			
			return panel;
		} else {
			JPanel panel = new JPanel(new BorderLayout(5, 0));
			panel.setOpaque(true);
			panel.setBackground(new Color(0xd6dde5));
			panel.add(
					new JLabel(Images.getResourceImage("search.png", 16, 16)),
					BorderLayout.WEST);
			panel.add(textField, BorderLayout.CENTER);
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			return panel;
		}
	}
	
	public static JScrollPane createJScrollPane(
			JComponent component,
			boolean border) {
		JScrollPane scrollPane = new JScrollPane(component);
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())
			IAppWidgetFactory.makeIAppScrollPane(scrollPane);
		
		if (border)
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		else
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		return scrollPane;
	}
	
	public static JSplitPane createThinJScrollPane(int orientation) {
		JSplitPane splitPane = new JSplitPane(orientation);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerSize(1);
		((BasicSplitPaneUI) splitPane.getUI()).getDivider().setBorder(
				BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0xa5a5a5)));
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		return splitPane;
	}
	
}
