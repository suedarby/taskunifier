/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SettingsUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class SettingsVersion {
	
	private SettingsVersion() {

	}
	
	public static void updateSettings() {
		String version = Main.SETTINGS.getStringProperty("general.version");
		
		if (version == null)
			version = "0.5.2";
		
		if (version.equals("0.5.2"))
			version = updateSettings_0_5_2_to_0_6();
		
		if (version.equals("0.6"))
			version = "0.6.1";
		
		if (version.equals("0.6.1"))
			version = updateSettings_0_6_1_to_0_6_2();
		
		if (version.equals("0.6.2"))
			version = updateSettings_0_6_2_to_0_6_3();
		
		if (version.equals("0.6.3"))
			version = "0.6.4";
		
		if (version.equals("0.6.4"))
			version = updateSettings_0_6_4_to_0_7_0();
		
		if (version.equals("0.7.0"))
			version = updateSettings_0_7_0_to_0_7_1();
		
		if (version.equals("0.7.1"))
			version = updateSettings_0_7_1_to_0_7_2();
		
		if (version.equals("0.7.2"))
			version = updateSettings_0_7_2_to_0_7_3();
		
		if (version.equals("0.7.3"))
			version = updateSettings_0_7_3_to_0_7_4();
		
		if (version.equals("0.7.4"))
			version = updateSettings_0_7_4_to_0_8_0();
		
		if (version.equals("0.8.0"))
			version = "0.8.1";
		
		if (version.equals("0.8.1"))
			version = updateSettings_0_8_1_to_0_8_2();
		
		if (version.equals("0.8.2"))
			version = updateSettings_0_8_2_to_0_8_3();
		
		if (version.equals("0.8.3"))
			version = updateSettings_0_8_3_to_0_8_4();
		
		if (version.equals("0.8.4"))
			version = updateSettings_0_8_4_to_0_8_5();
		
		if (version.equals("0.8.5"))
			version = updateSettings_0_8_5_to_0_8_6();
		
		if (version.equals("0.8.6"))
			version = updateSettings_0_8_6_to_0_8_7();
		
		if (version.equals("0.8.7"))
			version = updateSettings_0_8_7_to_0_9_0();
		
		if (version.equals("0.9.0"))
			version = updateSettings_0_9_0_to_0_9_1();
		
		if (version.equals("0.9.1"))
			version = updateSettings_0_9_1_to_0_9_2();
		
		if (version.equals("0.9.2"))
			version = updateSettings_0_9_2_to_0_9_3();
		
		if (version.equals("0.9.3"))
			version = updateSettings_0_9_3_to_0_9_4();
		
		if (version.equals("0.9.4"))
			version = updateSettings_0_9_4_to_0_9_5();
		
		if (version.equals("0.9.5"))
			version = updateSettings_0_9_5_to_0_9_6();
		
		if (version.equals("0.9.6"))
			version = updateSettings_0_9_6_to_0_9_7();
		
		Main.SETTINGS.setStringProperty("general.version", Constants.VERSION);
	}
	
	private static String updateSettings_0_5_2_to_0_6() {
		GuiLogger.getLogger().info("Update settings from version 0.5.2 to 0.6");
		
		Main.SETTINGS.setStringProperty("date.date_format", "dd/MM/yyyy");
		Main.SETTINGS.setStringProperty("date.time_format", "HH:mm");
		
		Main.SETTINGS.setStringProperty(
				"theme.lookandfeel",
				"com.jtattoo.plaf.luna.LunaLookAndFeel$Default");
		
		Main.SETTINGS.remove("date.simple_time_format");
		Main.SETTINGS.remove("date.date_time_format");
		
		return "0.6";
	}
	
	private static String updateSettings_0_6_1_to_0_6_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.1 to 0.6.2");
		
		Main.SETTINGS.setStringProperty("theme.color.searcher_list", "-3090718");
		
		return "0.6.2";
	}
	
	private static String updateSettings_0_6_2_to_0_6_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.2 to 0.6.3");
		
		Main.SETTINGS.setStringProperty(
				"synchronizer.scheduler_enabled",
				"false");
		Main.SETTINGS.setStringProperty(
				"synchronizer.scheduler_sleep_time",
				"600000");
		
		return "0.6.3";
	}
	
	private static String updateSettings_0_6_4_to_0_7_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.4 to 0.7.0");
		
		Main.SETTINGS.remove("task.default.completed");
		Main.SETTINGS.remove("task.default.context");
		Main.SETTINGS.remove("task.default.due_date");
		Main.SETTINGS.remove("task.default.folder");
		Main.SETTINGS.remove("task.default.goal");
		Main.SETTINGS.remove("task.default.location");
		Main.SETTINGS.remove("task.default.length");
		Main.SETTINGS.remove("task.default.note");
		Main.SETTINGS.remove("task.default.priority");
		Main.SETTINGS.remove("task.default.reminder");
		Main.SETTINGS.remove("task.default.repeat");
		Main.SETTINGS.remove("task.default.repeat_from");
		Main.SETTINGS.remove("task.default.star");
		Main.SETTINGS.remove("task.default.start_date");
		Main.SETTINGS.remove("task.default.status");
		Main.SETTINGS.remove("task.default.tags");
		Main.SETTINGS.remove("task.default.title");
		
		Main.SETTINGS.remove("synchronizer.last_context_edit");
		Main.SETTINGS.remove("synchronizer.last_folder_edit");
		Main.SETTINGS.remove("synchronizer.last_goal_edit");
		Main.SETTINGS.remove("synchronizer.last_location_edit");
		Main.SETTINGS.remove("synchronizer.last_task_edit");
		Main.SETTINGS.remove("synchronizer.last_task_delete");
		
		Main.SETTINGS.remove("toodledo.token");
		Main.SETTINGS.remove("toodledo.token_creation_date");
		Main.SETTINGS.remove("toodledo.userid");
		
		if ("KEEP_TOODLEDO".equals(Main.SETTINGS.getStringProperty("synchronizer.choice")))
			Main.SETTINGS.setStringProperty("synchronizer.choice", "KEEP_API");
		
		Main.SETTINGS.setStringProperty("api.id", "1");
		Main.SETTINGS.setStringProperty("api.version", "1.0");
		
		Main.SETTINGS.setStringProperty("review.showed", "false");
		
		Main.SETTINGS.setStringProperty("searcher.show_completed_tasks", "true");
		Main.SETTINGS.setStringProperty(
				"searcher.show_completed_tasks_at_the_end",
				"false");
		
		Main.SETTINGS.setStringProperty("proxy.use_system_proxy", "false");
		
		return "0.7.0";
	}
	
	private static String updateSettings_0_7_0_to_0_7_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.0 to 0.7.1");
		
		if (SystemUtils.IS_OS_MAC)
			Main.SETTINGS.setStringProperty(
					"theme.lookandfeel",
					UIManager.getSystemLookAndFeelClassName());
		
		Main.SETTINGS.remove("theme.color.searcher_list");
		
		return "0.7.1";
	}
	
	private static String updateSettings_0_7_1_to_0_7_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.1 to 0.7.2");
		
		Main.SETTINGS.setStringProperty("new_version.showed", "0.7.2");
		
		Main.SETTINGS.remove("proxy.use_system_proxy");
		Main.SETTINGS.remove("proxy.type");
		
		return "0.7.2";
	}
	
	private static String updateSettings_0_7_2_to_0_7_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.2 to 0.7.3");
		
		if ("MMM dd, yyyy".equals(Main.SETTINGS.getStringProperty("date.date_format")))
			Main.SETTINGS.setStringProperty("date.date_format", "MM/dd/yyyy");
		
		return "0.7.3";
	}
	
	private static String updateSettings_0_7_3_to_0_7_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.3 to 0.7.4");
		
		Main.SETTINGS.setStringProperty("synchronizer.sync_start", "false");
		Main.SETTINGS.setStringProperty("synchronizer.sync_exit", "false");
		
		return "0.7.4";
	}
	
	private static String updateSettings_0_7_4_to_0_8_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.4 to 0.8.0");
		
		Main.SETTINGS.setStringProperty("searcher.show_badges", "false");
		Main.SETTINGS.setStringProperty("task.show_edit_window_on_add", "false");
		
		return "0.8.0";
	}
	
	private static String updateSettings_0_8_1_to_0_8_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.1 to 0.8.2");
		
		Main.SETTINGS.setStringProperty("api.id", "0");
		Main.SETTINGS.setStringProperty("api.version", "1.0");
		// Main.SETTINGS.remove("api.version");
		
		Main.SETTINGS.remove("toodledo.toodledo.token_creation_date");
		Main.SETTINGS.remove("toodledo.toodledo.token");
		Main.SETTINGS.remove("toodledo.toodledo.userid");
		
		return "0.8.2";
	}
	
	private static String updateSettings_0_8_2_to_0_8_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.2 to 0.8.3");
		
		return "0.8.3";
	}
	
	private static String updateSettings_0_8_3_to_0_8_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.3 to 0.8.4");
		
		try {
			String oldPluginsDir = Main.RESOURCES_FOLDER
					+ File.separator
					+ "plugins";
			
			FileUtils.copyDirectory(new File(oldPluginsDir), new File(
					Main.PLUGINS_FOLDER));
		} catch (Throwable t) {}
		
		return "0.8.4";
	}
	
	private static String updateSettings_0_8_4_to_0_8_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.4 to 0.8.5");
		
		return "0.8.5";
	}
	
	private static String updateSettings_0_8_5_to_0_8_6() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.5 to 0.8.6");
		
		return "0.8.6";
	}
	
	private static String updateSettings_0_8_6_to_0_8_7() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.6 to 0.8.7");
		
		return "0.8.7";
	}
	
	private static String updateSettings_0_8_7_to_0_9_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.7 to 0.9.0");
		
		ActionResetGeneralSearchers.resetGeneralSearchers();
		
		Main.SETTINGS.setStringProperty("notecolumn.folder.order", "1");
		Main.SETTINGS.setStringProperty("notecolumn.folder.visible", "true");
		Main.SETTINGS.setStringProperty("notecolumn.folder.width", "150");
		Main.SETTINGS.setStringProperty("notecolumn.model.order", "4");
		Main.SETTINGS.setStringProperty("notecolumn.model.visible", "false");
		Main.SETTINGS.setStringProperty("notecolumn.model.width", "150");
		Main.SETTINGS.setStringProperty("notecolumn.note.order", "3");
		Main.SETTINGS.setStringProperty("notecolumn.note.visible", "false");
		Main.SETTINGS.setStringProperty("notecolumn.note.width", "300");
		Main.SETTINGS.setStringProperty("notecolumn.title.order", "2");
		Main.SETTINGS.setStringProperty("notecolumn.title.visible", "true");
		Main.SETTINGS.setStringProperty("notecolumn.title.width", "300");
		Main.SETTINGS.setStringProperty("taskcolumn.model.order", "20");
		Main.SETTINGS.setStringProperty("taskcolumn.model.visible", "false");
		Main.SETTINGS.setStringProperty("taskcolumn.model.width", "150");
		
		Main.SETTINGS.setStringProperty(
				"theme.color.importance.enabled",
				"true");
		
		return "0.9.0";
	}
	
	private static String updateSettings_0_9_0_to_0_9_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.0 to 0.9.1");
		
		return "0.9.1";
	}
	
	private static String updateSettings_0_9_1_to_0_9_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.1 to 0.9.2");
		
		return "0.9.2";
	}
	
	private static String updateSettings_0_9_2_to_0_9_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.2 to 0.9.3");
		
		return "0.9.3";
	}
	
	private static String updateSettings_0_9_3_to_0_9_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.3 to 0.9.4");
		
		Main.SETTINGS.setStringProperty("taskcolumn.show_children.order", "3");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.show_children.visible",
				"true");
		Main.SETTINGS.setStringProperty("taskcolumn.show_children.width", "40");
		
		return "0.9.4";
	}
	
	private static String updateSettings_0_9_4_to_0_9_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.4 to 0.9.5");
		
		SettingsUtils.resetImportanceColors();
		SettingsUtils.resetPriorityColors();
		
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsUtils.removeCompletedCondition();
			}
			
		});
		
		Main.SETTINGS.setStringProperty("taskcolumn.progress.order", "2");
		Main.SETTINGS.setStringProperty("taskcolumn.progress.visible", "true");
		Main.SETTINGS.setStringProperty("taskcolumn.progress.width", "80");
		
		Main.SETTINGS.setStringProperty("theme.color.progress", "-3355393");
		
		Main.SETTINGS.setStringProperty(
				"window.minimize_to_system_tray",
				"false");
		
		return "0.9.5";
	}
	
	private static String updateSettings_0_9_5_to_0_9_6() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.5 to 0.9.6");
		
		return "0.9.6";
	}
	
	private static String updateSettings_0_9_6_to_0_9_7() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.6 to 0.9.7");
		
		return "0.9.7";
	}
	
}
