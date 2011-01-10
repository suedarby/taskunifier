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
package com.leclercb.taskunifier.gui.searchers;

import java.io.Serializable;
import java.util.UUID;

import com.leclercb.taskunifier.api.event.propertychange.AbstractPropertyChangeModel;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.utils.EqualsBuilder;
import com.leclercb.taskunifier.api.utils.HashCodeBuilder;

public class TaskSearcher extends AbstractPropertyChangeModel implements Serializable, Cloneable {
	
	public static final String PROP_TITLE = "SEARCHER_TITLE";
	public static final String PROP_ICON = "SEARCHER_ICON";
	public static final String PROP_FILTER = "SEARCHER_FILTER";
	public static final String PROP_SORTER = "SEARCHER_SORTER";
	
	private String id;
	private String title;
	private String icon;
	private TaskFilter filter;
	private TaskSorter sorter;
	
	public TaskSearcher(String title, TaskFilter filter, TaskSorter sorter) {
		this(title, null, filter, sorter);
	}
	
	public TaskSearcher(
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter) {
		this.setId(UUID.randomUUID().toString());
		
		this.setTitle(title);
		this.setIcon(icon);
		this.setFilter(filter);
		this.setSorter(sorter);
	}
	
	@Override
	public TaskSearcher clone() {
		return new TaskSearcher(
				this.title,
				this.icon,
				this.filter.clone(),
				this.sorter.clone());
	}
	
	public String getId() {
		return this.id;
	}
	
	private void setId(String id) {
		CheckUtils.isNotNull(id, "ID cannot be null");
		this.id = id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		CheckUtils.isNotNull(title, "Title cannot be null");
		String oldTitle = this.title;
		this.title = title;
		this.firePropertyChange(PROP_TITLE, oldTitle, title);
	}
	
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String icon) {
		String oldIcon = this.icon;
		this.icon = icon;
		this.firePropertyChange(PROP_ICON, oldIcon, icon);
	}
	
	public TaskFilter getFilter() {
		return this.filter;
	}
	
	public void setFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		TaskFilter oldFilter = this.filter;
		this.filter = filter;
		this.firePropertyChange(PROP_FILTER, oldFilter, filter);
	}
	
	public TaskSorter getSorter() {
		return this.sorter;
	}
	
	public void setSorter(TaskSorter sorter) {
		CheckUtils.isNotNull(sorter, "Sorter cannot be null");
		TaskSorter oldSorter = this.sorter;
		this.sorter = sorter;
		this.firePropertyChange(PROP_SORTER, oldSorter, sorter);
	}
	
	@Override
	public String toString() {
		return this.title;
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof TaskSearcher) {
			TaskSearcher searcher = (TaskSearcher) o;
			
			return new EqualsBuilder().append(this.id, searcher.id).isEqual();
		}
		
		return false;
	}
	
	@Override
	public final int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.id);
		
		return hashCode.toHashCode();
	}
	
}
