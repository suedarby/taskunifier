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
package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
abstract class AbstractExportDialog extends JDialog {
	
	private String fileExtention;
	private String fileExtentionDescription;
	private JFileChooser fileChooser;
	private JTextField exportFile;
	
	public AbstractExportDialog(
			String title,
			String fileExtention,
			String fileExtentionDescription) {
		super(MainFrame.getInstance().getFrame());
		
		CheckUtils.isNotNull(fileExtention, "File extention cannot be null");
		CheckUtils.isNotNull(
				fileExtentionDescription,
				"File extention description cannot be null");
		
		this.fileExtention = fileExtention;
		this.fileExtentionDescription = fileExtentionDescription;
		
		this.initialize(title);
	}
	
	private void initialize(String title) {
		this.setModal(true);
		this.setTitle(title);
		this.setSize(500, 120);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				AbstractExportDialog.this.exportFile.setText(null);
				AbstractExportDialog.this.setVisible(false);
			}
			
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.NORTH);
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		// Export file
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return AbstractExportDialog.this.fileExtentionDescription;
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				String extention = FileUtils.getExtention(f.getName());
				
				return AbstractExportDialog.this.fileExtention.equals(extention);
			}
			
		});
		
		this.exportFile = new JTextField();
		JButton openFile = new JButton(Translations.getString("general.select"));
		
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = AbstractExportDialog.this.fileChooser.showSaveDialog(AbstractExportDialog.this);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					AbstractExportDialog.this.exportFile.setText(AbstractExportDialog.this.fileChooser.getSelectedFile().getAbsolutePath());
					if (!AbstractExportDialog.this.exportFile.getText().endsWith(
							"." + AbstractExportDialog.this.fileExtention))
						AbstractExportDialog.this.exportFile.setText(AbstractExportDialog.this.exportFile.getText()
								+ "."
								+ AbstractExportDialog.this.fileExtention);
				}
			}
			
		});
		
		JPanel importFilePanel = new JPanel();
		importFilePanel.setLayout(new BorderLayout(5, 0));
		importFilePanel.add(this.exportFile, BorderLayout.CENTER);
		importFilePanel.add(openFile, BorderLayout.EAST);
		
		builder.appendI15d("export.export_to_file", true, importFilePanel);
		
		// Lay out the panel
		panel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "EXPORT") {
					try {
						AbstractExportDialog.this.exportToFile(AbstractExportDialog.this.exportFile.getText());
						AbstractExportDialog.this.exportFile.setText(null);
						AbstractExportDialog.this.setVisible(false);
					} catch (Exception e) {
						ErrorInfo info = new ErrorInfo(
								Translations.getString("general.error"),
								e.getMessage(),
								null,
								null,
								e,
								null,
								null);
						
						JXErrorPane.showDialog(
								MainFrame.getInstance().getFrame(),
								info);
					}
				}
				
				if (event.getActionCommand() == "CANCEL") {
					AbstractExportDialog.this.exportFile.setText(null);
					AbstractExportDialog.this.setVisible(false);
				}
			}
			
		};
		
		JButton exportButton = new JButton(
				Translations.getString("general.export"));
		exportButton.setActionCommand("EXPORT");
		exportButton.addActionListener(listener);
		
		JButton cancelButton = ComponentFactory.createButtonCancel(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				exportButton,
				cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(exportButton);
	}
	
	protected abstract void exportToFile(String file) throws Exception;
	
}
