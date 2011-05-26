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
package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXColorSelectionButton;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.gui.api.models.GuiGoal;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.commons.converters.ColorConverter;
import com.leclercb.taskunifier.gui.commons.models.GoalContributeModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueGoalLevel;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class GoalConfigurationPanel extends JSplitPane implements IModelList {
	
	private ModelList modelList;
	
	public GoalConfigurationPanel() {
		this.initialize();
	}
	
	@Override
	public Model getSelectedModel() {
		return this.modelList.getSelectedModel();
	}
	
	@Override
	public void setSelectedModel(Model model) {
		this.modelList.setSelectedModel(model);
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField goalTitle = new JTextField(30);
		final JComboBox goalLevel = new JComboBox();
		final JComboBox goalContributes = ComponentFactory.createModelComboBox(null);
		final JXColorSelectionButton goalColor = new JXColorSelectionButton();
		final JButton removeColor = new JButton();
		
		// Initialize Model List
		this.modelList = new ModelList(new GoalModel(false), goalTitle) {
			
			private BeanAdapter<Goal> adapter;
			
			{
				this.adapter = new BeanAdapter<Goal>((Goal) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Model.PROP_TITLE);
				Bindings.bind(goalTitle, titleModel);
				
				ValueModel levelModel = this.adapter.getValueModel(Goal.PROP_LEVEL);
				goalLevel.setModel(new ComboBoxAdapter<GoalLevel>(
						GoalLevel.values(),
						levelModel));
				
				ValueModel contributesModel = this.adapter.getValueModel(Goal.PROP_CONTRIBUTES);
				goalContributes.setModel(new ComboBoxAdapter<Goal>(
						new GoalContributeModel(true),
						contributesModel));
				
				ValueModel colorModel = this.adapter.getValueModel(GuiModel.PROP_COLOR);
				Bindings.bind(goalColor, "background", new ColorConverter(
						colorModel));
			}
			
			@Override
			public Model addModel() {
				return GoalFactory.getInstance().create(
						Translations.getString("goal.default.title"));
			}
			
			@Override
			public void removeModel(Model model) {
				GoalFactory.getInstance().markToDelete(model);
			}
			
			@Override
			public void modelSelected(Model model) {
				this.adapter.setBean(model != null ? (Goal) model : null);
				goalTitle.setEnabled(model != null);
				goalLevel.setEnabled(model != null);
				goalContributes.setEnabled(model != null);
				goalColor.setEnabled(model != null);
				removeColor.setEnabled(model != null);
				
				if (model != null)
					goalContributes.setEnabled(!((Goal) model).getLevel().equals(
							GoalLevel.LIFE_TIME));
			}
			
		};
		
		this.setLeftComponent(this.modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(rightPanel);
		
		JPanel info = new JPanel();
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Goal Title
		label = new JLabel(
				Translations.getString("general.goal.title") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		goalTitle.setEnabled(false);
		info.add(goalTitle);
		
		// Goal Level
		label = new JLabel(
				Translations.getString("general.goal.level") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		goalLevel.setEnabled(false);
		goalLevel.setRenderer(new DefaultListRenderer(
				new StringValueGoalLevel()));
		goalLevel.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Goal goal = (Goal) GoalConfigurationPanel.this.modelList.getSelectedModel();
				goalContributes.setEnabled(!goal.getLevel().equals(
						GoalLevel.LIFE_TIME));
			}
			
		});
		info.add(goalLevel);
		
		// Goal Contributes
		label = new JLabel(Translations.getString("general.goal.contributes")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		goalContributes.setEnabled(false);
		info.add(goalContributes);
		
		// Goal Color
		label = new JLabel(
				Translations.getString("general.color") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		goalColor.setEnabled(false);
		goalColor.setPreferredSize(new Dimension(24, 24));
		goalColor.setBorder(BorderFactory.createEmptyBorder());
		
		removeColor.setEnabled(false);
		removeColor.setIcon(Images.getResourceImage("remove.png", 16, 16));
		removeColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((GuiGoal) GoalConfigurationPanel.this.modelList.getSelectedModel()).setColor(null);
			}
			
		});
		
		JPanel p = new JPanel(new BorderLayout(5, 0));
		p.add(goalColor, BorderLayout.WEST);
		p.add(removeColor, BorderLayout.EAST);
		
		info.add(p);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 4, 2, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		this.setDividerLocation(200);
	}
	
}
