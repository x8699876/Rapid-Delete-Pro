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

import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.mhisoft.rdpro.FileRemoveStatistics;
import org.mhisoft.rdpro.RdProRunTimeProperties;

/**
 * Description:  Swing UI implementation.
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class GraphicsRdProUIImpl extends AbstractRdProUIImpl {

	JTextArea outputTextArea;
	JLabel labelStatus;
	JFrame frame;
	public static int bufferLineThreshold = 9999;
	private int lineNumber = 0;

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

	public JLabel getLabelStatus() {
		return labelStatus;
	}

	public void setLabelStatus(JLabel labelStatus) {
		this.labelStatus = labelStatus;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}


	long lastreportTime = -1;
	static DecimalFormat df = new DecimalFormat("###,###");

	public void reportStatus(FileRemoveStatistics frs) {
		if (lastreportTime == -1 || System.currentTimeMillis() - lastreportTime > 1000) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					labelStatus.setText("Dir Removed:" + df.format(frs.getDirRemoved())
							+ ", Files removed:" + df.format(frs.getFilesRemoved()));
					lastreportTime = System.currentTimeMillis();
				}
			});
		}
	}


	@Override
	public void print(final String msg) {
		//invokeLater()
		//This method allows us to post a "job" to Swing, which it will then run
		// on the event dispatch thread at its next convenience.

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Here, we can safely update the GUI
				// because we'll be called from the
				// event dispatch thread
				if (lineNumber >= bufferLineThreshold) {
					outputTextArea.setText("");
					lineNumber = 0;
				}
				outputTextArea.append(msg);
				//outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());
				lineNumber++;
			}
		});

	}

	@Override
	public void println(final String msg) {
		print(msg + "\n");
	}

	@Override
	public void printf(final String msg, Object args) {
		//
	}

	@Override
	public boolean isAnswerY(String question) {
		int dialogResult = JOptionPane.showConfirmDialog(frame, question, "Please confirm", JOptionPane.YES_NO_OPTION);
		return dialogResult == JOptionPane.YES_OPTION;
	}

	@Override
	public Confirmation getConfirmation(String question, Confirmation... options) {
		int dialogResult = JOptionPane.showConfirmDialog(frame, question, "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION);
		if (JOptionPane.YES_OPTION == dialogResult) {
			return Confirmation.YES;
		} else if (JOptionPane.CANCEL_OPTION == dialogResult) {
			return Confirmation.QUIT;
		} else
			return Confirmation.NO;

		//todo support presend a check box to check Yes for all future confirmations
		//return  Confirmation.YES_TO_ALL
	}

	@Override
	public void help() {
		printBuildAndDisclaimer();
	}


	@Override
	public RdProRunTimeProperties parseCommandLineArguments(String[] args) {

		RdProRunTimeProperties props = new RdProRunTimeProperties();


		if (args.length < 1 || args[0] == null || args[0].trim().length() == 0) {
			//JOptionPane.showMessageDialog(null, "The root dir to start with can't be determined from args[].", "Error"
			//		, JOptionPane.ERROR_MESSAGE);
			//props.setSuccess(false);
			props.setRootDir(System.getProperty("user.dir"));
		} else {

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < args.length; i++) {

				String arg = args[i];

				if (arg.trim().length() == 0 || arg.startsWith("org.mhisoft.rdpro"))  //launched from sh script, the jar is the first argument.
					continue;
				if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help")) {
					help();
				} else if (arg.equalsIgnoreCase("-v")) {
					props.setVerbose(true);
				} else if (arg.equalsIgnoreCase("-yes")) {     //silent mode. !dangerous
					props.setAnswerYforAll(true);
				} else if (arg.equalsIgnoreCase("-debug")) {     //silent mode. !dangerous
					props.setDebug(true);
				} else if (arg.equalsIgnoreCase("-w")) {

					try {
						props.setNumberOfWorkers(Integer.parseInt(args[i + 1]));
						i++; //skip the next arg, it is the target.
					} catch (NumberFormatException e) {
						props.setNumberOfWorkers(5);
					}

				} else if (arg.equalsIgnoreCase("-f")) {
					props.setForceDelete(true);
					props.setInteractive(false);
				} else if (arg.equalsIgnoreCase("-dry")) {
					props.setDryRun(true);
					props.setForceDelete(false);
				} else if (arg.equalsIgnoreCase("-i")) {
					props.setInteractive(true);
					props.setForceDelete(false);
				} else if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("-dir")) {
					if (i + 1 < args.length)
						props.setTargetDir(args[i + 1]);
					else
						props.setTargetDir(null);
					i++; //skip the next arg, it is the target.

				} else {
					if (arg.startsWith("-")) {
						System.err.println("The argument is not recognized:" + arg);
						props.setSuccess(false);
						return props;
					}
					/* none - prefixed arguments */
					else {
						//collect into the root dir
						if (i > 0)
							sb.append(" ");
						sb.append(arg);


					}
				}

			}

			props.setRootDir(sb.toString());
		}
		return props;
	}
}
