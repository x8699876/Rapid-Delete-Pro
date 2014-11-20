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

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.mhisoft.rdpro.RdPro;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ConsoleRdProUIImpl implements RdProUI {

	@Override
	public void print(final String msg) {
		System.out.print(msg);

	}

	@Override
	public  void println(final String msg) {
		System.out.println(msg);
	}

	@Override
	public  void printf(final String msg, Object args) {
		System.out.printf(msg, args);
	}


	@Override
	public  boolean isAnswerY(String question) {
		Confirmation a = getConfirmation(question, "y", "n", "h");
		if (a==Confirmation.HELP) {
			help();
			return false;
		} else if (Confirmation.YES!=a) {
			return false;
		}
		return true;
	}



	@Override
	public Confirmation getConfirmation(String question, String... options) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		print(question);
		String a = null;

		List<String> optionsList = new ArrayList<String>();
		for (String option : options) {
			optionsList.add(option.toLowerCase());
			optionsList.add(option.toUpperCase());
		}

		try {
			while (a == null || a.trim().length() == 0 ) {
				a = br.readLine();
				if ( a!=null && !optionsList.contains(a)) {
					print("\tresponse \"" + a + "\" not recognized. input again:");
					a=null; //keep asking
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (a.equalsIgnoreCase("h")) {
			return Confirmation.HELP;
		}
		if (a.equalsIgnoreCase("all")) {
			return Confirmation.YES_TO_ALL;
		}
		else if (!a.equalsIgnoreCase("y")) {
			return Confirmation.NO;
		}
		return Confirmation.YES;
	}

	public  void help() {
		println("RdPro  - A Powerful Recursive Directory Purge Utility (" +
			version + build + " MHISoft Oct 2014, Shareware, Tony Xue)");
		println("Disclaimer:");
		println("\tDeleted files does not go to recycle bean and can't be recovered.");
		println("\tThe author is not responsible for any lost of files or damage incurred by running this utility.");
		println("Usages:");
		println("\t rdpro [option] path-to-search [target-dir] ");
		println("\t  path-to-search  root path to search, default to the current dir.");
		println("\t -d/-dir specify the target dir");
		println("\t -f force delete");
		println("\t -i interactive, default true");
		println("\t -v verbose mode");
		/*println("\t -w number of worker threads, default 5");*/
		println("Examples:");
		println("\tRemove everything under a dir (purge a direcotry and everthing under it): rdpro c:\\mytempfiles");
		println("\tRemove all directories that matches a specified name recursively: ");
		println("\t\trdpro -d target s:\\projects");
		println("\t\trdpro s:\\projects target");
		/*println("\tRemove files matches a pattern recursively: rdpro s:\\projects -d target *.war ");*/
	}


	public RdPro.RdProRunTimeProperties parseCommandLineArguments(String[] args) {

		RdPro.RdProRunTimeProperties props = new RdPro.RdProRunTimeProperties();

		List<String> noneHyfenArgs = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help")) {
				help();
				props.setSuccess(false);
				return props;
			} else if (arg.equalsIgnoreCase("-v")) {
				props.setVerbose(true);
			} else if (arg.equalsIgnoreCase("-w")) {

				try {
					props.setNumberOfWorkers( Integer.parseInt(args[i + 1]));
					i++; //skip the next arg, it is the target.
				} catch (NumberFormatException e) {
					props.setNumberOfWorkers( 5);
				}

			} else if (arg.equalsIgnoreCase("-f")) {
				props.setForceDelete( true) ;
				props.setInteractive(false);
			} else if (arg.equalsIgnoreCase("-i")) {
				props.setInteractive(true);
				props.setForceDelete(false);
			} else if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("-dir")) {
				props.setTargetDir( args[i + 1] );
				i++; //skip the next arg, it is the target.

			} else {
				if (arg.startsWith("-")) {
					System.err.println("The argument is not recognized:" + arg);
					props.setSuccess(false);
					return props;
				} else
					//not start with "-"
					if (arg!=null && arg.trim().length()>0)
						noneHyfenArgs.add(arg);
			}
		}


		if (noneHyfenArgs.size() == 0) {
			props.setRootDir(System.getProperty("user.dir"));
		} else if (noneHyfenArgs.size() == 1) {
			//rdpro d:\temp -d classes
			if (props.getTargetDir() != null)
				props.setRootDir(noneHyfenArgs.get(0));

				//rdpro d:\temp
			else if (noneHyfenArgs.get(0).contains(":")  || noneHyfenArgs.get(0).startsWith("\\")
					|| noneHyfenArgs.get(0).startsWith("/") ) {

				props.setRootDir(noneHyfenArgs.get(0));
			}
			else {
				//rdpro classes
				props.setRootDir(System.getProperty("user.dir"));
				props.setTargetDir(noneHyfenArgs.get(0));
			}

		} else {
			props.setRootDir(System.getProperty("user.dir"));
			if (noneHyfenArgs.size() >= 2)
				props.setTargetDir(noneHyfenArgs.get(1));
		}

		if (props.getRootDir() == null)
			props.setRootDir(System.getProperty("user.dir"));


		println("");


		if (props.getTargetDir() != null) {
			if (!isAnswerY("Start to delete all the directories named \"" + props.getTargetDir() + "\" under \""
					+ props.getRootDir() + "\".\nThere is no way to undelete, please confirm? (y/n or h for help)"))
				props.setSuccess(false);
			return props;

		}
		else {
			boolean b = isAnswerY("Start to delete everything under \"" + props.getRootDir() + "\" (y/n or h for help)?");
			if (b) {
				if (!isAnswerY(" *Warning* There is no way to undelete. Confirm again (y/n or h for help)?")) {
					props.setSuccess(false);
					return props;
				}
			}
			else {
				props.setSuccess(false);
				return props;
			}
		}

		props.setSuccess(true);
		return props;

	}


}
