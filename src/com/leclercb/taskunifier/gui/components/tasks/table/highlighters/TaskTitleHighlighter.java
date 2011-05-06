package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.ToolTipHighlighter;

import com.leclercb.taskunifier.gui.components.tasks.table.renderers.StringValueTitle;

public class TaskTitleHighlighter extends ToolTipHighlighter {
	
	public TaskTitleHighlighter(HighlightPredicate predicate) {
		super(predicate, new StringValueTitle());
	}

}
