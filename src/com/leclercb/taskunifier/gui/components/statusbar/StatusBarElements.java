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
package com.leclercb.taskunifier.gui.components.statusbar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;

import com.leclercb.taskunifier.gui.components.synchronize.ProgressMessageListener;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.threads.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
final class StatusBarElements {
	
	private StatusBarElements() {

	}
	
	public static final JLabel createSynchronizerStatus() {
		final JLabel element = new JLabel();
		
		Constants.PROGRESS_MONITOR.addListChangeListener(new ProgressMessageListener() {
			
			@Override
			public void showMessage(String message) {
				element.setText(Translations.getString("synchronizer.status")
						+ ": "
						+ message);
			}
			
		});
		
		element.setText(Translations.getString("synchronizer.status") + ": ");
		
		return element;
	}
	
	public static final JLabel createLastSynchronizationDate() {
		final JLabel element = new JLabel();
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				Main.SETTINGS.getStringProperty("date.date_format")
						+ " "
						+ Main.SETTINGS.getStringProperty("date.time_format"));
		
		String date = Translations.getString("statusbar.never");
		
		if (Main.SETTINGS.getCalendarProperty("synchronizer.last_synchronization_date") != null)
			date = dateFormat.format(Main.SETTINGS.getCalendarProperty(
					"synchronizer.last_synchronization_date").getTime());
		
		element.setText(Translations.getString("statusbar.last_synchronization_date")
				+ ": "
				+ date);
		
		Main.SETTINGS.addPropertyChangeListener(
				"synchronizer.last_synchronization_date",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						String date = Translations.getString("statusbar.never");
						
						if (Main.SETTINGS.getCalendarProperty("synchronizer.last_synchronization_date") != null)
							date = dateFormat.format(Main.SETTINGS.getCalendarProperty(
									"synchronizer.last_synchronization_date").getTime());
						
						element.setText(Translations.getString("statusbar.last_synchronization_date")
								+ ": "
								+ date);
					}
					
				});
		
		return element;
	}
	
	public static final JLabel createScheduledSyncStatus(
			final ScheduledSyncThread thread) {
		final JLabel element = new JLabel();
		
		updateScheduledSyncStatus(element, thread);
		
		Main.SETTINGS.addPropertyChangeListener(
				"synchronizer.scheduler_enabled",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						updateScheduledSyncStatus(element, thread);
					}
					
				});
		
		thread.addPropertyChangeListener(
				ScheduledSyncThread.PROP_REMAINING_SLEEP_TIME,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						updateScheduledSyncStatus(element, thread);
					}
					
				});
		
		return element;
	}
	
	private static final void updateScheduledSyncStatus(
			JLabel element,
			ScheduledSyncThread thread) {
		String text = null;
		
		if (Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled")) {
			long sleep = thread.getRemainingSleepTime();
			sleep = sleep / 1000;
			
			String time = "";
			time += (sleep / 3600) + "h ";
			time += ((sleep % 3600) / 60) + "m ";
			time += (sleep % 60) + "s";
			
			text = Translations.getString("statusbar.next_scheduled_sync", time);
		} else {
			text = Translations.getString(
					"statusbar.next_scheduled_sync",
					Translations.getString("statusbar.never"));
		}
		
		element.setText(text);
	}
	
}