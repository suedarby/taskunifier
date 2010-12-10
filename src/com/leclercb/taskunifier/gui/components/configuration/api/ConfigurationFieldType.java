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
package com.leclercb.taskunifier.gui.components.configuration.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.images.Images;

public interface ConfigurationFieldType<ComponentType extends Component, ValueType> {

	public static class Separator extends JSeparator implements ConfigurationFieldType<JSeparator, Void> {

		public Separator() {

		}

		@Override
		public JSeparator getFieldComponent() {
			return this;
		}

		@Override
		public Void getFieldValue() {
			return null;
		}

	}

	public static class Label extends JLabel implements ConfigurationFieldType<JLabel, Void> {

		public Label(String label) {
			super(label);
			this.setEnabled(false);
		}

		@Override
		public JLabel getFieldComponent() {
			return this;
		}

		@Override
		public Void getFieldValue() {
			return null;
		}

	}

	public static class Button extends JButton implements ConfigurationFieldType<JButton, Void> {

		public Button(Action action) {
			super(action);
		}

		public Button(String label, ActionListener listener) {
			super(label);
			this.addActionListener(listener);
		}

		@Override
		public JButton getFieldComponent() {
			return this;
		}

		@Override
		public Void getFieldValue() {
			return null;
		}

	}

	public static class CheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {

		public CheckBox(Boolean selected) {
			this.setSelected(selected);
		}

		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}

		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}

	}

	public static class StarCheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {

		public StarCheckBox(Boolean selected) {
			this.setIcon(Images.getResourceImage("checkbox_star.gif"));
			this.setSelectedIcon(Images.getResourceImage("checkbox_star_selected.gif"));

			this.setSelected(selected);
		}

		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}

		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}

	}

	public static class ComboBox extends JComboBox implements ConfigurationFieldType<JComboBox, Object> {

		public ComboBox(ComboBoxModel model, Object selectedItem) {
			super(model);
			this.setSelectedItem(selectedItem);
		}

		public ComboBox(Object[] items, Object selectedItem) {
			super(items);
			this.setSelectedItem(selectedItem);
		}

		@Override
		public JComboBox getFieldComponent() {
			return this;
		}

		@Override
		public Object getFieldValue() {
			return this.getSelectedItem();
		}

	}

	public static class TextArea extends JTextArea implements ConfigurationFieldType<JTextArea, String> {

		public TextArea(String text) {
			super(text, 5, 20);
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}

		@Override
		public JTextArea getFieldComponent() {
			return this;
		}

		@Override
		public String getFieldValue() {
			return this.getText();
		}

	}

	public static class TextField extends JTextField implements ConfigurationFieldType<JTextField, String> {

		public TextField(String text) {
			super(text);
		}

		@Override
		public JTextField getFieldComponent() {
			return this;
		}

		@Override
		public String getFieldValue() {
			return this.getText();
		}

	}

	public static class FormattedTextField extends JFormattedTextField implements ConfigurationFieldType<JFormattedTextField, String> {

		public FormattedTextField(AbstractFormatter formatter, String text) {
			super(formatter);
			this.setValue(text);
		}

		@Override
		public JFormattedTextField getFieldComponent() {
			return this;
		}

		@Override
		public String getFieldValue() {
			return this.getText();
		}

	}

	public static class PasswordField extends JPasswordField implements ConfigurationFieldType<JPasswordField, String> {

		public PasswordField(String password) {
			super(password);
		}

		@Override
		public JPasswordField getFieldComponent() {
			return this;
		}

		@Override
		public String getFieldValue() {
			return new String(this.getPassword());
		}

	}

	public static class ColorChooser extends JButton implements ConfigurationFieldType<JButton, Color> {

		JColorChooser colorChooser;

		public ColorChooser(Color color) {
			this.setBackground(color);
			this.setBorderPainted(true);

			colorChooser = new JColorChooser();
			colorChooser.setColor(color);

			final JDialog colorDialog = JColorChooser.createDialog(
					this, 
					"Color", 
					true, 
					colorChooser, 
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							ColorChooser.this.setBackground(colorChooser.getColor());
						}

					}, 
					null);

			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					colorDialog.setVisible(true);
				}

			});
		}

		@Override
		public JButton getFieldComponent() {
			return this;
		}

		@Override
		public Color getFieldValue() {
			return this.colorChooser.getColor();
		}

	}

	public abstract ComponentType getFieldComponent();
	public abstract ValueType getFieldValue();

}
