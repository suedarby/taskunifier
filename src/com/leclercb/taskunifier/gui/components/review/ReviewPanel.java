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
package com.leclercb.taskunifier.gui.components.review;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class ReviewPanel extends JPanel {
	
	public ReviewPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = null;
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel icon = new JLabel(Constants.TITLE, Images.getResourceImage(
				"logo.png",
				48,
				48), SwingConstants.CENTER);
		
		panel.add(icon, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JEditorPane pane = new JEditorPane();
		pane.setContentType("text/html");
		pane.setEditable(false);
		pane.setText(Translations.getString("review.message"));
		pane.setCaretPosition(0);
		
		JButton reviewButton = new JButton(
				Translations.getString("review.link"));
		reviewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					BrowserUtils.openDefaultBrowser(Constants.REVIEW_URL);
				} catch (Exception e) {
					ErrorDialog dialog = new ErrorDialog(
							MainFrame.getInstance().getFrame(),
							null,
							e,
							false);
					dialog.setVisible(true);
				}
			}
			
		});
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		buttonsPanel.add(reviewButton);
		
		panel.add(
				ComponentFactory.createJScrollPane(pane, true),
				BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
}
