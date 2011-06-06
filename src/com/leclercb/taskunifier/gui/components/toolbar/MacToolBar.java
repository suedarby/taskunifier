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
package com.leclercb.taskunifier.gui.components.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXSearchField;

import com.explodingpixels.macwidgets.UnifiedToolBar;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionScheduledSync;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TemplateUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class MacToolBar extends UnifiedToolBar {
	
	public MacToolBar(JXSearchField searchField) {
		CheckUtils.isNotNull(searchField, "Search field cannot be null");
		
		this.initialize();
		this.addComponentToRight(searchField);
	}
	
	private void initialize() {
		this.addComponentToLeft(this.createButton(new ActionAddNote(24, 24)));
		this.addComponentToLeft(this.createButton(new ActionAddTask(24, 24)));
		this.initializeTemplates();
		this.addComponentToLeft(this.createButton(new ActionDelete(24, 24)));
		this.addComponentToLeft(new JSeparator());
		this.addComponentToLeft(this.createButton(new ActionSynchronize(
				false,
				24,
				24)));
		this.addComponentToLeft(this.createButton(new ActionScheduledSync(
				24,
				24)));
		this.addComponentToLeft(new JSeparator());
		this.addComponentToLeft(this.createButton(new ActionConfiguration(
				24,
				24)));
	}
	
	private void initializeTemplates() {
		final JPopupMenu popupMenu = new JPopupMenu(
				Translations.getString("action.name.add_template_task"));
		
		TemplateUtils.updateTemplateList(null, popupMenu);
		
		TemplateFactory.getInstance().addListChangeListener(
				new ListChangeListener() {
					
					@Override
					public void listChange(ListChangeEvent event) {
						TemplateUtils.updateTemplateList(null, popupMenu);
					}
					
				});
		
		final JButton addTemplateTaskButton = this.createButton(null);
		
		Action actionAddTemplateTask = new AbstractAction() {
			
			{
				this.putValue(
						NAME,
						Translations.getString("action.name.add_template_task"));
				
				this.putValue(
						SMALL_ICON,
						Images.getResourceImage("duplicate.png", 24, 24));
				
				this.putValue(
						SHORT_DESCRIPTION,
						Translations.getString("action.description.add_template_task"));
			}
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				popupMenu.show(
						addTemplateTaskButton,
						addTemplateTaskButton.getX(),
						addTemplateTaskButton.getY());
			}
			
		};
		
		addTemplateTaskButton.setAction(actionAddTemplateTask);
		
		this.addComponentToLeft(addTemplateTaskButton);
	}
	
	private JButton createButton(Action action) {
		JButton button = new JButton(action);
		
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		
		String text = button.getText() == null ? "" : button.getText();
		text = text.length() > 30 ? text.substring(0, 30 - 2) + "..." : text;
		
		button.setText(text);
		
		return button;
	}
	
}