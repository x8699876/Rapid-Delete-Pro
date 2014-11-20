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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ReproMainForm {

	JFrame frame;
	RdPro rdpro;
	RdPro.RdProRunTimeProperties props;


	JCheckBox chkForceDelete;
	JCheckBox chkShowInfo;

	JPanel layoutPanel1;
	JLabel labelDirName;
	JTextArea outputTextArea;
	JScrollPane outputTextAreaScrollPane;
	JLabel labelDirValue;
	private JButton btnOk;
	private JButton btnCancel;
	private JButton btnHelp;
	private JTextField fldTargetDir;
	private JLabel labelRootDir;

	JList list1;

	public ReproMainForm() {
		chkForceDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(null
//						, "Value of the checkbox:" + chkForceDelete.isSelected()
//						, "Title", JOptionPane.INFORMATION_MESSAGE);
//
//				;

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
				frame.dispose();
			}
		});
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doit();
			}
		});
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText("");
				showHideInfo(true);
				rdpro.getRdProUI().help();
				scrollToTop();

			}
		});
	}


	public void showHideInfo(boolean visible) {
		outputTextArea.setVisible(visible);
		outputTextAreaScrollPane.setVisible(visible);

		if (visible && frame.getSize().getWidth() < 500) {
			frame.setPreferredSize(new Dimension(500, 500));
			frame.pack();
		}

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
		frame = new JFrame("RdproForm");
		frame.setContentPane(layoutPanel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		outputTextAreaScrollPane.setVisible(false);
//		outputTextArea.setVisible(false);

		frame.pack();
		frame.setLocationRelativeTo(null);  // *** this will center your app ***
		frame.setVisible(true);


	}



	public void doit() {
		if (props.isSuccess()) {
			props.setForceDelete(chkForceDelete.isSelected());
			props.setInteractive(!chkForceDelete.isSelected());

			String targetDir = fldTargetDir.getText() == null || fldTargetDir.getText().trim().length() == 0 ? null : fldTargetDir.getText().trim();
			props.setTargetDir(targetDir);
			props.setVerbose(chkShowInfo.isSelected());

			rdpro.getRdProUI().println("working.");

			rdpro.run(props);
		}
	}

	public static void main(String[] args) {

		String rootDir = "S:\\projects\\mhisoft\\RdPro\\target\\classes";
		if (args.length == 0) {
			args = new String[]{rootDir};
		}


		ReproMainForm rdProMain = new ReproMainForm();
		rdProMain.init();

		GraphicsRdProUIImpl rdProUI = new GraphicsRdProUIImpl();
		rdProUI.setOutputTextArea(rdProMain.outputTextArea);

		rdProMain.rdpro = new RdPro(rdProUI);
		rdProMain.props= rdProUI.parseCommandLineArguments(args);
		rdProMain.props.setRootDir(rootDir);

		rdProMain.labelRootDir.setText(rootDir);


	}

}
