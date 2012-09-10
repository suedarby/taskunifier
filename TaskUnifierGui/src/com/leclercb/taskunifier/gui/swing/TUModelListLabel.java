package com.leclercb.taskunifier.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;

public class TUModelListLabel extends JPanel {
	
	private ModelList<?> modelList;
	
	public TUModelListLabel() {
		this(null);
	}
	
	public TUModelListLabel(ModelList<?> modelList) {
		this.initialize();
		this.setModelList(modelList);
	}
	
	private void initialize() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.setOpaque(false);
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		
		for (Component component : this.getComponents())
			component.setFont(font);
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		
		for (Component component : this.getComponents())
			component.setForeground(fg);
	}
	
	public ModelList<?> getModelList() {
		return this.modelList;
	}
	
	public void setModelList(ModelList<?> modelList) {
		this.modelList = modelList;
		
		this.removeAll();
		
		if (modelList != null) {
			for (Model model : modelList) {
				JLabel label = new JLabel(
						StringValueModel.INSTANCE.getString(model));
				
				if (model instanceof GuiModel)
					label.setIcon(IconValueModel.INSTANCE.getIcon(model));
				
				this.add(label);
				this.add(Box.createHorizontalStrut(3));
			}
		}
		
		this.revalidate();
		this.repaint();
	}
	
}