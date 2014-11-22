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

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.mhisoft.rdpro.RdPro;

/**
 * Description:  Swing UI implementation.
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
	public  void printf(final String msg, Object args) {
		//
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
			//JOptionPane.showMessageDialog(null, "The root dir to start with can't be determined from args[].", "Error"
			//		, JOptionPane.ERROR_MESSAGE);
			//props.setSuccess(false);
			props.setRootDir(null);
		}
		else
			props.setRootDir(args[0]);
		return props;
	}
}
