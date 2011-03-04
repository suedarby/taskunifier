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
package com.leclercb.taskunifier.gui.components.tasks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.leclercb.commons.api.event.ListenerList;
import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public enum TaskColumn {
	
	TITLE(String.class, Translations.getString("general.task.title"), true),
	TAGS(String.class, Translations.getString("general.task.tags"), true),
	FOLDER(Folder.class, Translations.getString("general.task.folder"), true),
	CONTEXT(Context.class, Translations.getString("general.task.context"), true),
	GOAL(Goal.class, Translations.getString("general.task.goal"), true),
	LOCATION(Location.class, Translations.getString("general.task.location"), true),
	PARENT(Task.class, Translations.getString("general.task.parent"), false),
	COMPLETED(Boolean.class, Translations.getString("general.task.completed"), true),
	COMPLETED_ON(Calendar.class, Translations.getString("general.task.completed_on"), false),
	DUE_DATE(Calendar.class, Translations.getString("general.task.due_date"), true),
	START_DATE(Calendar.class, Translations.getString("general.task.start_date"), true),
	REMINDER(Integer.class, Translations.getString("general.task.reminder"), true),
	REPEAT(String.class, Translations.getString("general.task.repeat"), true),
	REPEAT_FROM(TaskRepeatFrom.class, Translations.getString("general.task.repeat_from"), true),
	STATUS(TaskStatus.class, Translations.getString("general.task.status"), true),
	LENGTH(Integer.class, Translations.getString("general.task.length"), true),
	PRIORITY(TaskPriority.class, Translations.getString("general.task.priority"), true),
	STAR(Boolean.class, Translations.getString("general.task.star"), true),
	NOTE(String.class, Translations.getString("general.task.note"), false),
	IMPORTANCE(Integer.class, Translations.getString("general.task.importance"), false);
	
	private Class<?> type;
	private String label;
	private boolean editable;
	
	private TaskColumn(Class<?> type, String label, boolean editable) {
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().startsWith("taskcolumn")) {
					if (evt.getNewValue() == null)
						return;
					
					if (evt.getPropertyName().equals(
							"taskcolumn."
									+ TaskColumn.this.name().toLowerCase()
									+ ".order"))
						TaskColumn.this.setOrder(Integer.parseInt(evt.getNewValue().toString()));
					
					if (evt.getPropertyName().equals(
							"taskcolumn."
									+ TaskColumn.this.name().toLowerCase()
									+ ".width"))
						TaskColumn.this.setWidth(Integer.parseInt(evt.getNewValue().toString()));
					
					if (evt.getPropertyName().equals(
							"taskcolumn."
									+ TaskColumn.this.name().toLowerCase()
									+ ".visible"))
						TaskColumn.this.setVisible(Boolean.parseBoolean(evt.getNewValue().toString()));
				}
			}
			
		});
	}
	
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	public int getOrder() {
		Integer order = Main.SETTINGS.getIntegerProperty("taskcolumn."
				+ this.name().toLowerCase()
				+ ".order");
		
		if (order == null)
			return 0;
		
		return order;
	}
	
	public void setOrder(int order) {
		if (order == this.getOrder())
			return;
		
		int oldOrder = this.getOrder();
		Main.SETTINGS.setIntegerProperty("taskcolumn."
				+ this.name().toLowerCase()
				+ ".order", order);
		TaskColumn.firePropertyChange(this, PROP_ORDER, oldOrder, order);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	public int getWidth() {
		Integer width = Main.SETTINGS.getIntegerProperty("taskcolumn."
				+ this.name().toLowerCase()
				+ ".width");
		
		if (width == null)
			return 100;
		
		return width;
	}
	
	public void setWidth(int width) {
		if (width == this.getWidth())
			return;
		
		int oldWidth = this.getWidth();
		Main.SETTINGS.setIntegerProperty("taskcolumn."
				+ this.name().toLowerCase()
				+ ".width", width);
		TaskColumn.firePropertyChange(this, PROP_WIDTH, oldWidth, width);
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isVisible() {
		Boolean visible = Main.SETTINGS.getBooleanProperty("taskcolumn."
				+ this.name().toLowerCase()
				+ ".visible");
		
		if (visible == null)
			return true;
		
		return visible;
	}
	
	public void setVisible(boolean visible) {
		if (visible == this.isVisible())
			return;
		
		boolean oldVisible = this.isVisible();
		Main.SETTINGS.setBooleanProperty("taskcolumn."
				+ this.name().toLowerCase()
				+ ".visible", visible);
		TaskColumn.firePropertyChange(this, PROP_VISIBLE, oldVisible, visible);
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	public static TaskColumn[] getValues(boolean onlyVisible) {
		TaskColumn[] columns = TaskColumn.values();
		Arrays.sort(columns, new Comparator<TaskColumn>() {
			
			@Override
			public int compare(TaskColumn o1, TaskColumn o2) {
				return new Integer(o1.getOrder()).compareTo(o2.getOrder());
			}
			
		});
		
		if (!onlyVisible)
			return columns;
		
		int count = 0;
		for (int i = 0; i < columns.length; i++)
			if (columns[i].isVisible())
				count++;
		
		TaskColumn[] visibleColumns = new TaskColumn[count];
		for (int i = 0, j = 0; i < columns.length; i++)
			if (columns[i].isVisible())
				visibleColumns[j++] = columns[i];
		
		return visibleColumns;
	}
	
	public Object getValue(Task task) {
		switch (this) {
			case TITLE:
				return task.getTitle();
			case TAGS:
				return ArrayUtils.arrayToString(task.getTags(), ", ");
			case FOLDER:
				return task.getFolder();
			case CONTEXT:
				return task.getContext();
			case GOAL:
				return task.getGoal();
			case LOCATION:
				return task.getLocation();
			case PARENT:
				return task.getParent();
			case COMPLETED:
				return task.isCompleted();
			case COMPLETED_ON:
				return task.getCompletedOn();
			case DUE_DATE:
				return task.getDueDate();
			case START_DATE:
				return task.getStartDate();
			case REMINDER:
				return task.getReminder();
			case REPEAT:
				return task.getRepeat();
			case REPEAT_FROM:
				return task.getRepeatFrom();
			case STATUS:
				return task.getStatus();
			case LENGTH:
				return task.getLength();
			case PRIORITY:
				return task.getPriority();
			case STAR:
				return task.isStar();
			case NOTE:
				return task.getNote();
			case IMPORTANCE:
				return TaskUtils.getImportance(task);
			default:
				return null;
		}
	}
	
	public void setValue(Task task, Object value) {
		switch (this) {
			case TITLE:
				task.setTitle((String) value);
				break;
			case TAGS:
				task.setTags(((String) value).split(","));
				break;
			case FOLDER:
				task.setFolder((Folder) value);
				break;
			case CONTEXT:
				task.setContext((Context) value);
				break;
			case GOAL:
				task.setGoal((Goal) value);
				break;
			case LOCATION:
				task.setLocation((Location) value);
				break;
			case PARENT:
				task.setParent((Task) value);
				break;
			case COMPLETED:
				task.setCompleted((Boolean) value);
				break;
			case COMPLETED_ON:
				task.setCompletedOn((Calendar) value);
				break;
			case DUE_DATE:
				task.setDueDate((Calendar) value);
				break;
			case START_DATE:
				task.setStartDate((Calendar) value);
				break;
			case REMINDER:
				if (value == null)
					task.setReminder(0);
				else
					task.setReminder((Integer) value);
				break;
			case REPEAT:
				task.setRepeat((String) value);
				break;
			case REPEAT_FROM:
				task.setRepeatFrom((TaskRepeatFrom) value);
				break;
			case STATUS:
				task.setStatus((TaskStatus) value);
				break;
			case LENGTH:
				if (value == null)
					task.setLength(0);
				else
					task.setLength((Integer) value);
				break;
			case PRIORITY:
				task.setPriority((TaskPriority) value);
				break;
			case STAR:
				task.setStar((Boolean) value);
				break;
			case NOTE:
				task.setNote((String) value);
				break;
			case IMPORTANCE:
				break;
		}
	}
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_WIDTH = "width";
	public static final String PROP_VISIBLE = "visible";
	
	private static ListenerList<PropertyChangeListener> propertyChangeListenerList;
	
	static {
		propertyChangeListenerList = new ListenerList<PropertyChangeListener>();
	}
	
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
		TaskColumn.propertyChangeListenerList.addListener(listener);
	}
	
	public static void removePropertyChangeListener(
			PropertyChangeListener listener) {
		TaskColumn.propertyChangeListenerList.removeListener(listener);
	}
	
	protected static void firePropertyChange(PropertyChangeEvent event) {
		if (propertyChangeListenerList != null)
			for (PropertyChangeListener listener : TaskColumn.propertyChangeListenerList)
				listener.propertyChange(event);
	}
	
	protected static void firePropertyChange(
			Object source,
			String property,
			Object oldValue,
			Object newValue) {
		firePropertyChange(new PropertyChangeEvent(
				source,
				property,
				oldValue,
				newValue));
	}
	
}
