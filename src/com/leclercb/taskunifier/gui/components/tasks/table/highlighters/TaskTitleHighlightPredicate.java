package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskTitleHighlightPredicate implements HighlightPredicate {

	@Override
	public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
		if (adapter.getColumnIdentifierAt(adapter.convertColumnIndexToModel(adapter.column)) != TaskColumn.TITLE)
			return false;

		Object value = adapter.getValue(adapter.convertColumnIndexToModel(adapter.column));

		if (value == null || !(value instanceof String))
			return false;

		return ((String) value).length() == 0;
	}

}
