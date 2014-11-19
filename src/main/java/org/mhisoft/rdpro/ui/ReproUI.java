/*
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

import org.mhisoft.rdpro.GuiLoggerImpl;
import org.mhisoft.rdpro.RdPro;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ReproUI {

	JFrame frame;
	static RdPro rdpro;


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
	private JTextField textField1;

	JList list1;

	public ReproUI() {
		chkForceDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(null
//						, "Value of the checkbox:" + chkForceDelete.isSelected()
//						, "Title", JOptionPane.INFORMATION_MESSAGE);
//
//				;

				outputTextArea.append("Value of the checkbox:" + chkForceDelete.isSelected());
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
				frame.dispose();
			}
		});
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText("");
				showHideInfo(true);
				RdPro.help();
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
		outputTextAreaScrollPane.setVisible(false);
		outputTextArea.setVisible(false);

		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) {


		ReproUI form = new ReproUI();
		form.init();
		GuiLoggerImpl logger = new GuiLoggerImpl(form.outputTextArea);
		rdpro = new RdPro(logger);

//
//		form.outputTextArea.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//				"<form xmlns=\"http://www.intellij.com/uidesigner/form/\" version=\"1\" bind-to-class=\"org.mhisoft.rdpro.ui.ReproForm\">\n" +
//				"  <grid id=\"27dc6\" binding=\"layoutPanel1\" layout-manager=\"GridLayoutManager\" row-count=\"9\" column-count=\"3\" same-size-horizontally=\"false\" same-size-vertically=\"false\" hgap=\"-1\" vgap=\"-1\">\n" +
//				"    <margin top=\"10\" left=\"10\" bottom=\"10\" right=\"0\"/>\n" +
//				"    <constraints>\n" +
//				"      <xy x=\"20\" y=\"20\" width=\"1124\" height=\"526\"/>\n" +
//				"    </constraints>\n" +
//				"    <properties/>\n" +
//				"    <border type=\"none\"/>\n" +
//				"    <children>\n" +
//				"      <vspacer id=\"e4655\">\n" +
//				"        <constraints>\n" +
//				"          <grid row=\"0\" column=\"2\" row-span=\"1\" col-span=\"1\" vsize-policy=\"0\" hsize-policy=\"1\" anchor=\"0\" fill=\"2\" indent=\"1\" use-parent-layout=\"false\">\n" +
//				"            <minimum-size width=\"-1\" height=\"20\"/>\n");

		//form.outputTextArea.setCaretPosition(form.outputTextArea.getLineCount());




		//form.outputTextAreaScrollPane.scrollRectToVisible();

	}

}
