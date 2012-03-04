package com.leclercb.taskunifier.gui.utils;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.taskunifier.gui.main.Main;

public final class TaskStatusUtils {
	
	private TaskStatusUtils() {
		
	}
	
	public static String[] getTaskStatuses() {
		boolean custom = SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().isAllowCustomStatusValue();
		
		if (!custom)
			return SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getDefaultStatusValues();
		
		String value = Main.getSettings().getStringProperty("taskstatuses");
		return value.split(";");
	}
	
	public static void setTaskStatuses(String[] statuses) {
		for (int i = 0; i < statuses.length; i++) {
			statuses[i] = statuses[i].trim();
		}
		
		String value = StringUtils.join(statuses, ";");
		Main.getSettings().setStringProperty("taskstatuses", value);
	}
	
}
