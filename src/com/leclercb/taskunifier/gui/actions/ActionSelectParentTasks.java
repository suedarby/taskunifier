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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionSelectParentTasks extends AbstractViewAction {
	
	public ActionSelectParentTasks() {
		this(32, 32);
	}
	
	public ActionSelectParentTasks(int width, int height) {
		super(
				Translations.getString("action.select_parent_tasks"),
				ImageUtils.getResourceImage("task.png", width, height),
				ViewType.TASKS);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.select_parent_tasks"));
		
		this.viewLoaded();
		
		ViewType.TASKS.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ActionSelectParentTasks.this.viewLoaded();
			}
			
		});
		
		this.setEnabled(false);
	}
	
	private void viewLoaded() {
		if (ViewType.TASKS.isLoaded()) {
			ViewType.getTaskView().getTaskTableView().addModelSelectionChangeListener(
					new ModelSelectionListener() {
						
						@Override
						public void modelSelectionChange(
								ModelSelectionChangeEvent event) {
							ActionSelectParentTasks.this.setEnabled(ActionSelectParentTasks.this.shouldBeEnabled());
						}
						
					});
			
			this.setEnabled(this.shouldBeEnabled());
		}
	}
	
	@Override
	protected boolean shouldBeEnabled() {
		if (!super.shouldBeEnabled())
			return false;
		
		Task[] tasks = ViewType.getSelectedTasks();
		
		if (tasks == null)
			return false;
		
		return tasks.length != 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionSelectParentTasks.showParentTasks();
	}
	
	public static void showParentTasks() {
		Task[] tasks = ViewType.getSelectedTasks();
		
		List<Task> parents = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.getParent() != null)
				if (!parents.contains(task.getParent()))
					parents.add(task.getParent());
		}
		
		ViewType.setSelectedTasks(parents.toArray(new Task[0]));
	}
	
}
