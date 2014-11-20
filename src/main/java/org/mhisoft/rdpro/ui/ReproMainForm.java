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
		outputTextAreaScrollPane.setVisible(false);
		outputTextArea.setVisible(false);

		frame.pack();
		frame.setLocationRelativeTo(null);  // *** this will center your app ***
		frame.setVisible(true);




	}

	public static void main(String[] args) {

		String rootDir = "S:\\projects\\mhisoft\\RdPro\\target\\classes";
		if (args.length==0) {
			args = new String[] {rootDir};
		}


		ReproMainForm form = new ReproMainForm();
		form.init();

		GraphicsRdProUIImpl rdProUI = new GraphicsRdProUIImpl();
		rdProUI.setOutputTextArea(form.outputTextArea);

		form.rdpro = new RdPro(rdProUI);
		RdPro.RdProRunTimeProperties props =rdProUI.parseCommandLineArguments(args);
		props.setRootDir(rootDir);

		if (props.isSuccess()) {
			props.setForceDelete(form.chkForceDelete.isSelected());
			props.setInteractive(!form.chkForceDelete.isSelected());

			String targetDir = form.fldTargetDir.getText() == null || form.fldTargetDir.getText().trim().length() == 0 ? null : form.fldTargetDir.getText().trim();
			props.setTargetDir(targetDir);
			props.setVerbose(form.chkShowInfo.isSelected());
			form.rdpro.run();
		}



	}

}
