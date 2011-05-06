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
package com.leclercb.taskunifier.gui.components.batchaddtask;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.commons.models.TemplateModel;
import com.leclercb.taskunifier.gui.commons.renderers.TemplateListCellRenderer;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class BatchAddTaskDialog extends JDialog {
	
	private static BatchAddTaskDialog INSTANCE;
	
	public static BatchAddTaskDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BatchAddTaskDialog();
		
		return INSTANCE;
	}
	
	private JTextArea answerTextArea;
	private JComboBox templateComboBox;
	
	private BatchAddTaskDialog() {
		super(MainFrame.getInstance().getFrame());
		this.initialize();
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.batch_add_tasks"));
		this.setSize(500, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = null;
		
		panel = new JPanel(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(panel, BorderLayout.NORTH);
		
		JLabel icon = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
		panel.add(icon, BorderLayout.WEST);
		
		JLabel question = new JLabel(
				Translations.getString("batch_add_tasks.insert_task_titles"));
		panel.add(question, BorderLayout.CENTER);
		
		panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		
		this.answerTextArea = new JTextArea();
		this.answerTextArea.setEditable(true);
		
		JPanel templatePanel = new JPanel();
		templatePanel.setLayout(new BorderLayout());
		
		this.templateComboBox = new JComboBox();
		this.templateComboBox.setModel(new TemplateModel(true));
		this.templateComboBox.setRenderer(new TemplateListCellRenderer());
		
		templatePanel.add(new JLabel(Translations.getString("general.template")
				+ ": "), BorderLayout.WEST);
		templatePanel.add(this.templateComboBox, BorderLayout.CENTER);
		
		panel.add(
				ComponentFactory.createJScrollPane(this.answerTextArea, true),
				BorderLayout.CENTER);
		panel.add(templatePanel, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					String answer = BatchAddTaskDialog.this.answerTextArea.getText();
					Template template = (Template) BatchAddTaskDialog.this.templateComboBox.getSelectedItem();
					
					if (answer == null)
						return;
					
					MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
					
					String[] titles = answer.split("\n");
					
					Synchronizing.setSynchronizing(true);
					
					List<Task> tasks = new ArrayList<Task>();
					for (String title : titles) {
						title = title.trim();
						
						if (title.length() == 0)
							continue;
						
						Task task = TaskFactory.getInstance().create("");
						
						if (template != null)
							template.applyToTask(task);
						
						task.setTitle(title);
						
						tasks.add(task);
					}
					
					Synchronizing.setSynchronizing(false);
					
					MainFrame.getInstance().getTaskView().setSelectedTasks(
							tasks.toArray(new Task[0]));
					
					BatchAddTaskDialog.this.answerTextArea.setText(null);
					BatchAddTaskDialog.this.templateComboBox.setSelectedItem(null);
					
					BatchAddTaskDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					BatchAddTaskDialog.this.dispose();
				}
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
