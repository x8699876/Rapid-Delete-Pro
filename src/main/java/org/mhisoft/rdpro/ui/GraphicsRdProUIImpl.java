package org.mhisoft.rdpro.ui;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.mhisoft.rdpro.RdPro;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class GraphicsRdProUIImpl implements RdProUI {

	JTextArea outputTextArea;

	public GraphicsRdProUIImpl(JTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	public GraphicsRdProUIImpl() {
	}

	public JTextArea getOutputTextArea() {
		return outputTextArea;
	}

	public void setOutputTextArea(JTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	@Override
	public void print(final String msg) {
		outputTextArea.append(msg);

	}

	@Override
	public  void println(final String msg) {
		outputTextArea.append(msg+"\n");
	}

	@Override
	public  boolean isAnswerY(String question) {
		int dialogResult = JOptionPane.showConfirmDialog(null, question, "Please confirm", JOptionPane.YES_NO_OPTION);
		return dialogResult == JOptionPane.YES_OPTION;
	}

	@Override
	public Confirmation getConfirmation(String question, String... options) {
		int dialogResult = JOptionPane.showConfirmDialog(null, question, "Please confirm", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION==dialogResult) {
			return  Confirmation.YES;
		}
		else
			return  Confirmation.NO;

		//todo support presend a check box to check Yes for all future confirmations
		//return  Confirmation.YES_TO_ALL
	}

	@Override
	public void help() {
		println("RdPro  - A Powerful Recursive Directory Purge Utility (" +
				version + build + " MHISoft Oct 2014, Shareware, Tony Xue)");
		println("Disclaimer:");
		println("\tDeleted files does not go to recycle bean and can't be recovered.");
		println("\tThe author is not responsible for any lost of files or damage incurred by running this utility.");
	}

	@Override
	public RdPro.RdProRunTimeProperties parseCommandLineArguments(String[] args) {
		RdPro.RdProRunTimeProperties props= new RdPro.RdProRunTimeProperties();

		if (args.length<1 || args[0]==null || args[0].trim().length()==0) {
			JOptionPane.showMessageDialog(null, "The root dir to start with can't be determined from args[].", "Error"
					, JOptionPane.ERROR_MESSAGE);
			props.setSuccess(false);
		}
		props.setRootDir(args[0]);
		return props;
	}
}
