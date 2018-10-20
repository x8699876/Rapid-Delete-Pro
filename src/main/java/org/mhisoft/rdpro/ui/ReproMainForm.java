/*
 * Copyright (c) 2014- MHISoft LLC and/or its affiliates. All rights reserved.
 * Licensed to MHISoft LLC under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. MHISoft LLC licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mhisoft.rdpro.ui;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.mhisoft.rdpro.RdPro;
import org.mhisoft.rdpro.RdProRunTimeProperties;

import com.googlecode.vfsjfilechooser2.VFSJFileChooser;
import com.googlecode.vfsjfilechooser2.accessories.DefaultAccessoriesPanel;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ReproMainForm {

	JFrame frame;
	RdPro rdpro;
	RdProRunTimeProperties props;


	JCheckBox chkForceDelete;
	JCheckBox chkShowInfo;

	JPanel layoutPanel1;
	JLabel labelDirName;
	JTextArea outputTextArea;
	JScrollPane outputTextAreaScrollPane;
	private JButton btnOk;
	private JButton btnCancel;
	private JButton btnHelp;
	private JTextField fldTargetDir;
	private JLabel labelStatus;
	private JTextField fldRootDir;
	private JButton btnEditRootDir;
	private JButton btnBrowseRootDir;
	private JTextField fldFilePatterns;
	private JCheckBox chkUnlinkDir;

	JList list1;
	private DoItJobThread doItJobThread;


	String lastSourceFileLocation =null;

	public ReproMainForm() {
		chkForceDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//outputTextArea.append("Value of the checkbox:" + chkForceDelete.isSelected());
			}
		});
		chkShowInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHideInfo(chkShowInfo.isSelected());
			}
		});



		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdpro.isRunning()) {
					stopIt();

				} else {
					frame.dispose();
					System.exit(0);
				}
			}
		});


		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Don't block the EDT
				//probably using the Swing thread which is waiting
				// for your code to execute before it can update the UI. Try using a separate thread for that loop.
				//just do invokeLater() as below does not work.


//				SwingUtilities.invokeLater(new Runnable() {
//					@Override
//					public void run() {
						//doit();
//					}
//				});

				doItJobThread= new DoItJobThread();
				doItJobThread.start();

			}
		});
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText("");
				showHideInfo(true);
				rdpro.getRdProUI().help();
				//scrollToTop();

			}
		});
		btnEditRootDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fldRootDir.setEditable(!fldRootDir.isEditable());
			}
		});


		btnBrowseRootDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*out of box OS provided file chooser
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setCurrentDirectory( new File(props.getRootDir()));
				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
//						uiImpl.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
//						uiImpl.println("getSelectedFile() : " + chooser.getSelectedFile());
					props.setRootDir(chooser.getSelectedFile().getAbsolutePath());  ;
					fldRootDir.setText(props.getRootDir());

				}   */



				if (lastSourceFileLocation ==null)
					lastSourceFileLocation = props.getRootDir();


				File[] files = chooseFiles(new File(lastSourceFileLocation)
						, VFSJFileChooser.SELECTION_MODE.FILES_AND_DIRECTORIES);

				if (files != null && files.length > 0) {
					StringBuilder builder = new StringBuilder();

					//append to existing
//					if (fldSourceDir.getText() != null && fldSourceDir.getText().length() > 0) {
//						builder.append(fldSourceDir.getText()) ;
//					}

					//now append the new directories.  //todo support multiple DIRs later
//					for (File file : files) {
//						if (builder.length() > 0)
//							builder.append(";");
//						builder.append(file.getAbsolutePath());
//						lastSourceFileLocation =   file;
//
//					}


					lastSourceFileLocation =files[0].getAbsolutePath();
					props.setRootDir(files[0].getAbsolutePath());  ;
					fldRootDir.setText(props.getRootDir());
				}



			}





		});

	}


	protected void resize() {
		if (frame.getSize().getWidth() < 500) {
			frame.setPreferredSize(new Dimension(500, 400));
		}
		frame.pack();
	}

	public void showHideInfo(boolean visible) {
		outputTextArea.setVisible(visible);
		outputTextAreaScrollPane.setVisible(visible);
		frame.pack();
		chkShowInfo.setSelected(visible);
	}

	public void scrollToBottom() {
		outputTextArea.validate();
		JScrollBar vertical = outputTextAreaScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	public void scrollToTop() {
		outputTextArea.validate();
		JScrollBar vertical = outputTextAreaScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMinimum());
	}




	public void init() {
		frame = new JFrame("Recursive Directory Removal Pro "+RdProUI.version);
		frame.setContentPane(layoutPanel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();

		/*position it*/
		//frame.setLocationRelativeTo(null);  // *** this will center your app ***
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int x = (int) b.getX();
		int y = (int) b.getY();
		frame.setLocation(x + 100, y);

		btnEditRootDir.setBorder(null);
		btnCancel.setText("Close");
		//resize();

		frame.setVisible(true);

	}


	class DoItJobThread extends Thread {
		@Override
		public void run() {
			doit();
		}
	}


	public void doit() {
		if (props.isSuccess()) {
			props.setForceDelete(chkForceDelete.isSelected());
			props.setInteractive(!chkForceDelete.isSelected());
			props.setUnLinkDirFirst(chkUnlinkDir.isSelected());

			String targetDir = fldTargetDir.getText() == null || fldTargetDir.getText().trim().length() == 0 ? null : fldTargetDir.getText().trim();
			props.setTargetDir(targetDir);
			props.setVerbose(chkShowInfo.isSelected());
			props.setRootDir( fldRootDir.getText() );

			props.setTargetFilePatterns(fldFilePatterns.getText());

			RdPro.setStopThreads(false);
			btnCancel.setText("Cancel");
			rdpro.getRdProUI().println("working.");

			labelStatus.setText("Working...");
			labelStatus.setText("");
			rdpro.setRunning(true);

			rdpro.run(props);

			rdpro.setRunning(false);
			btnCancel.setText("Close");

			labelStatus.setText("Done. Dir Removed:" + rdpro.getStatistics().getDirRemoved()
					+ ", Files removed:" + rdpro.getStatistics().getFilesRemoved());
		}
	}



	File[] chooseFiles(final File currentDir, VFSJFileChooser.SELECTION_MODE selectionMode) {
		// create a file chooser
		final VFSJFileChooser fileChooser = new VFSJFileChooser();

		// configure the file dialog
		fileChooser.setAccessory(new DefaultAccessoriesPanel(fileChooser));
		fileChooser.setFileHidingEnabled(false);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(selectionMode);
		fileChooser.setCurrentDirectory(currentDir);
		fileChooser.setFileHidingEnabled(true);  //show hidden files
		fileChooser.setPreferredSize( new Dimension(800, 500));


		// show the file dialog
		VFSJFileChooser.RETURN_TYPE answer = fileChooser.showOpenDialog(null);

		// check if a file was selected
		if (answer == VFSJFileChooser.RETURN_TYPE.APPROVE) {
			final File[] files = fileChooser.getSelectedFiles();

//			// remove authentication credentials from the file path
//			final String safeName = VFSUtils.getFriendlyName(aFileObject.toString());
//
//			System.out.printf("%s %s", "You selected:", safeName);
			return files;
		}
		return null;
	}



	public void stopIt() {
		RdPro.setStopThreads(true);
		//progressPanel.setVisible(false);

		rdpro.stopWorkers();

		//main thread
		doItJobThread.interrupt();


		//set running false only afer all threads are shutdown.
		rdpro.setRunning(false);
		btnCancel.setText("Close");

	}

	public static void main(String[] args) {
		ReproMainForm rdProMain = new ReproMainForm();
		rdProMain.init();
		GraphicsRdProUIImpl rdProUI = new GraphicsRdProUIImpl();
		rdProUI.setFrame(rdProMain.frame);
		rdProUI.setOutputTextArea(rdProMain.outputTextArea);
		rdProUI.setLabelStatus(rdProMain.labelStatus);


		//default it to current dir
		String defaultRootDir = System.getProperty("user.dir");
		rdProMain.rdpro = new RdPro(rdProUI);
		rdProMain.props = rdProUI.parseCommandLineArguments(args);

		if (RdPro.debug || rdProMain.props.isDebug() ) {
			int i = 0;
			for (String arg : args) {
				rdProUI.println("arg[" + i + "]=" + arg);
				i++;
			}
		}

		if (rdProMain.props.getRootDir() == null)
			rdProMain.props.setRootDir(defaultRootDir);

		if (RdPro.debug || rdProMain.props.isDebug())  {
			rdProUI.println("set root dir=" + rdProMain.props.getRootDir());
		}

		//display it
		rdProMain.fldRootDir.setText(rdProMain.props.getRootDir());
		rdProMain.fldTargetDir.setText(rdProMain.props.getTargetDir());

		rdProUI.help();


	}
}
