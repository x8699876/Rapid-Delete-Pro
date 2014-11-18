package org.mhisoft.rdpro.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ReproUI {
	 JCheckBox checkBox1;
	 JPanel layoutPanel1;
	 JLabel labelDirName;
	 JTextArea outputTextArea;
	 JScrollPane outputTextAreaScrollPane;
	 JLabel labelDirValue;
	 JList list1;

	public ReproUI() {
		checkBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(null
//						, "Value of the checkbox:" + checkBox1.isSelected()
//						, "Title", JOptionPane.INFORMATION_MESSAGE);
//
//				;

				outputTextArea.append("Value of the checkbox:" + checkBox1.isSelected());
			}
		});
	}



	public void init() {
		JFrame frame = new JFrame("RdproForm");
		frame.setContentPane(layoutPanel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		ReproUI form = new ReproUI();
		form.init();



		form.labelDirValue.setText("test dir");


		form.outputTextArea.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<form xmlns=\"http://www.intellij.com/uidesigner/form/\" version=\"1\" bind-to-class=\"org.mhisoft.rdpro.ui.ReproForm\">\n" +
				"  <grid id=\"27dc6\" binding=\"layoutPanel1\" layout-manager=\"GridLayoutManager\" row-count=\"9\" column-count=\"3\" same-size-horizontally=\"false\" same-size-vertically=\"false\" hgap=\"-1\" vgap=\"-1\">\n" +
				"    <margin top=\"10\" left=\"10\" bottom=\"10\" right=\"0\"/>\n" +
				"    <constraints>\n" +
				"      <xy x=\"20\" y=\"20\" width=\"1124\" height=\"526\"/>\n" +
				"    </constraints>\n" +
				"    <properties/>\n" +
				"    <border type=\"none\"/>\n" +
				"    <children>\n" +
				"      <vspacer id=\"e4655\">\n" +
				"        <constraints>\n" +
				"          <grid row=\"0\" column=\"2\" row-span=\"1\" col-span=\"1\" vsize-policy=\"0\" hsize-policy=\"1\" anchor=\"0\" fill=\"2\" indent=\"1\" use-parent-layout=\"false\">\n" +
				"            <minimum-size width=\"-1\" height=\"20\"/>\n");

		//form.outputTextArea.setCaretPosition(form.outputTextArea.getLineCount());


		form.outputTextArea.validate();
		JScrollBar vertical = form.outputTextAreaScrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		//form.outputTextAreaScrollPane.scrollRectToVisible();

	}

}
