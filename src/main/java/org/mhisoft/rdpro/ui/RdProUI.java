package org.mhisoft.rdpro.ui;

import org.mhisoft.rdpro.RdPro;

/**
 * Description: The RdPro User Interface
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public interface RdProUI {

	public static final String version = "v0.9 ";
	public static final String build = "build 203";

	public  enum Confirmation {
		YES, NO, YES_TO_ALL, HELP
	}


	/**
	 * log th emessage
	 * @param msg
	 */
	void print(String msg);

	/**
	 * log the msg
	 * @param msg
	 */
	void println(String msg);

	/**
	 * Present a confirmation and return true if confirmed.
	 * @param question
	 * @return
	 */
	public  boolean isAnswerY(String question);

	/**
	 * Display help
	 */
	public  void help();


	/**
	 * Parse the arguments passed to the program
	 * @param args
	 * @return
	 */
	public RdPro.RdProRunTimeProperties parseCommandLineArguments(String[] args);


	/**
	 * Get a confirmation to the question.
	 * @param question
	 * @param options
	 * @return
	 */
	public Confirmation getConfirmation(String question, String... options);
}
