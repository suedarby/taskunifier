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
package com.leclercb.taskunifier.gui.commons.models;

import java.util.List;

import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelStatus;

public class LocationModel extends AbstractModelSortedModel {
	
	public LocationModel(boolean firstNull) {
		this.initialize(firstNull);
	}
	
	private void initialize(boolean firstNull) {
		if (firstNull)
			this.addElement(null);
		
		List<Location> locations = LocationFactory.getInstance().getList();
		for (Location location : locations)
			if (location.getModelStatus().equals(ModelStatus.LOADED)
					|| location.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.addElement(location);
		
		LocationFactory.getInstance().addListChangeListener(this);
		LocationFactory.getInstance().addPropertyChangeListener(this);
	}
	
}