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
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.commons.gui.swing.lookandfeel.exc.LookAndFeelException;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ThemeConfigurationPanel extends DefaultConfigurationPanel {
	
	private Window[] windows;
	
	public ThemeConfigurationPanel(Window[] windows) {
		this.windows = windows;
		this.initialize();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		// Look And Feel & Theme
		LookAndFeelDescriptor laf = (LookAndFeelDescriptor) this.getValue("LOOK_AND_FEEL");
		Main.SETTINGS.setStringProperty(
				"theme.lookandfeel",
				laf.getIdentifier());
		
		// Colors
		Main.SETTINGS.setBooleanProperty(
				"theme.color.enabled",
				(Boolean) this.getValue("COLORS_ENABLED"));
		Main.SETTINGS.setColorProperty(
				"theme.color.even",
				(Color) this.getValue("COLOR_EVEN"));
		Main.SETTINGS.setColorProperty(
				"theme.color.odd",
				(Color) this.getValue("COLOR_ODD"));
	}
	
	private void applyTheme() {
		LookAndFeelDescriptor laf = (LookAndFeelDescriptor) this.getValue("LOOK_AND_FEEL");
		
		try {
			if (this.windows != null)
				for (int i = 0; i < this.windows.length; i++)
					laf.setLookAndFeel(this.windows[i]);
		} catch (LookAndFeelException e) {
			ErrorDialog errorDialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					null,
					e,
					true);
			errorDialog.setVisible(true);
			
			return;
		}
	}
	
	private void initialize() {
		LookAndFeelDescriptor themeLookAndFeelValue = null;
		Boolean themeColorEnabledValue = false;
		Color themeColorEvenValue = Color.WHITE;
		Color themeColorOddValue = Color.WHITE;
		
		if (Main.SETTINGS.getStringProperty("theme.lookandfeel") != null)
			themeLookAndFeelValue = LookAndFeelUtils.getLookAndFeel(Main.SETTINGS.getStringProperty("theme.lookandfeel"));
		
		if (Main.SETTINGS.getBooleanProperty("theme.color.enabled") != null)
			themeColorEnabledValue = Main.SETTINGS.getBooleanProperty("theme.color.enabled");
		
		if (Main.SETTINGS.getColorProperty("theme.color.even") != null)
			themeColorEvenValue = Main.SETTINGS.getColorProperty("theme.color.even");
		
		if (Main.SETTINGS.getColorProperty("theme.color.odd") != null)
			themeColorOddValue = Main.SETTINGS.getColorProperty("theme.color.odd");
		
		// Sort look and feels by name
		List<LookAndFeelDescriptor> lookAndFeels = new ArrayList<LookAndFeelDescriptor>(
				LookAndFeelUtils.getLookAndFeels());
		Collections.sort(lookAndFeels, new Comparator<LookAndFeelDescriptor>() {
			
			@Override
			public int compare(
					LookAndFeelDescriptor laf1,
					LookAndFeelDescriptor laf2) {
				return laf1.getName().compareTo(laf2.getName());
			}
			
		});
		
		this.addField(new ConfigurationField(
				"LOOK_AND_FEEL",
				Translations.getString("configuration.theme.look_and_feel"),
				new ConfigurationFieldType.ComboBox(
						lookAndFeels.toArray(),
						themeLookAndFeelValue)));
		
		this.addField(new ConfigurationField(
				"APPLY_THEME",
				null,
				new ConfigurationFieldType.Button(
						Translations.getString("general.apply"),
						new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								ThemeConfigurationPanel.this.applyTheme();
							}
							
						})));
		
		this.addField(new ConfigurationField(
				"LOOK_AND_FEEL_PREVIEW",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.theme.look_and_feel_preview"))));
		
		this.addField(new ConfigurationField(
				"COLOR_CHANGED_NEXT_STARTUP",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.theme.colors_changed_after_restart"))));
		
		this.addField(new ConfigurationField(
				"COLORS_ENABLED",
				Translations.getString("configuration.theme.colors_enabled"),
				new ConfigurationFieldType.CheckBox(themeColorEnabledValue)));
		
		this.addField(new ConfigurationField(
				"COLOR_EVEN",
				Translations.getString("configuration.theme.color_even"),
				new ConfigurationFieldType.ColorChooser(themeColorEvenValue)));
		
		this.addField(new ConfigurationField(
				"COLOR_ODD",
				Translations.getString("configuration.theme.color_odd"),
				new ConfigurationFieldType.ColorChooser(themeColorOddValue)));
	}
	
}
