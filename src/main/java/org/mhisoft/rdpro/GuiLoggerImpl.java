package org.mhisoft.rdpro;

import javax.swing.JTextArea;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class GuiLoggerImpl implements Logger {

	JTextArea outputTextArea;

	public GuiLoggerImpl(JTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	public GuiLoggerImpl() {
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

}
