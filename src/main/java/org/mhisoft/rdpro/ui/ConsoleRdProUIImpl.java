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

import org.mhisoft.rdpro.RdProRunTimeProperties;

/**
 * Description: Console UI
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ConsoleRdProUIImpl extends AbstractRdProUIImpl {

	@Override
	public void print(final String msg) {
		System.out.print(msg);

	}

	@Override
	public void println(final String msg) {
		System.out.println(msg);
	}

	@Override
	public void printf(final String msg, Object args) {
		System.out.printf(msg, args);
	}


	@Override
	public boolean isAnswerY(String question) {
		Confirmation a = getConfirmation(question, Confirmation.YES, Confirmation.NO, Confirmation.HELP, Confirmation.QUIT);
		if (a == Confirmation.HELP) {
			help();
			return false;
		} else if (Confirmation.YES != a) {
			return false;
		}
		return true;
	}


	@Override
	public Confirmation getConfirmation(String question, Confirmation... options) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		print(question);
		String a = null;

		List<String> optionsList = new ArrayList<String>();
		for (Confirmation option : options) {
			optionsList.add(option.toString().toLowerCase());
			optionsList.add(option.toString().toUpperCase());
		}

		try {
			while (a == null || a.trim().length() == 0) {
				a = br.readLine();
				if (a != null && !optionsList.contains(a)) {
					print("\tresponse \"" + a + "\" not recognized. input again:");
					a = null; //keep asking
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Confirmation ret = Confirmation.fromString(a);
		if (ret != null) {
			if (ret == Confirmation.QUIT)
				System.exit(-2);
			return ret;
		} else {
			throw new RuntimeException("can't parse it, should not happen. input: " + a);
		}

	}

	public void help() {
		printBuildAndDisclaimer();
		println("Usages (see https://github.com/mhisoft/rdpro/wiki):");
		println("\t rdpro [option] path-to-search -d [target-dir] ");
		println("\t path-to-search:  The root path to search, default to the current dir.");
		println("\t -d or -dir specify the target dir. only dir names matched this name will be deleted. " +
				"if target file pattern is also specified, only matched files under these matched dirs will be deleted.");
		println("\t -tf file match patterns. Use comma to delimit multiple file match patterns. ex: *.repositories,*.log");
		println("\t -f  force delete. Use it only when you are absolutely sure. Default:false ");
		println("\t -i  interactive, Default:true");
		println("\t -unlink  Unlink the hard linked directory first. Files in the linked directory won't be removed. Default:false.");
		println("\t -v  verbose mode. Default:false.");
		/*println("\t -w number of worker threads, default 5");*/
		println("Examples:");
		println("\tRemove everything under a dir (purge a directory and everything under it): ");
				println("\t\t>rdpro c:\\mytempfiles");
		println("\tRemove all directories that matches a specified name recursively: ");
		        println("\t\t>rdpro s:\\projects -d target ");

		println("\tRemove files matches a pattern recursively on Mac or Linux:");
		       println("\t\t$rdpro.sh /Users/home/projects -d target -tf *.zip");
	}


	public RdProRunTimeProperties parseCommandLineArguments(String[] args) {

		RdProRunTimeProperties props = new RdProRunTimeProperties();

		List<String> noneHyfenArgs = new ArrayList<String>();

		props.setRootDir(System.getProperty("user.dir")); //default
		props.setUnLinkDirFirst(false); //default false

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.trim().length() == 0 || arg.startsWith("org.mhisoft.rdpro"))  //launched from sh script, the jar is the first argument.
				continue;


			if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help")) {
				help();
				props.setSuccess(false);
				return props;
			} else if (arg.equalsIgnoreCase("-v")) {
				props.setVerbose(true);
			} else if (arg.equalsIgnoreCase("-yes")) {     //silent mode. !dangerous
				props.setAnswerYforAll(true);
			} else if (arg.equalsIgnoreCase("-debug")) {
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
				props.setInteractive(false);}
			else if (arg.equalsIgnoreCase("-unlink")) {
				props.setUnLinkDirFirst(true);
			}
			else if (arg.equalsIgnoreCase("-tf")) {
				props.setTargetFilePatterns(args[i + 1]);
				i++;
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
				} else
					//not start with "-"
					if (arg != null && arg.trim().length() > 0)
						noneHyfenArgs.add(arg);

			}
		}


		if (noneHyfenArgs.size() == 0) {
			props.setRootDir(System.getProperty("user.dir"));
		} else if (noneHyfenArgs.size() == 1) {
			//rdpro d:\temp -d classes
			if (props.getTargetDir() != null)
				props.setRootDir(noneHyfenArgs.get(0));

			//rdpro d:\temp    (this is WIN)   not applying for MAC
//			else if (noneHyfenArgs.get(0).contains(":") || noneHyfenArgs.get(0).startsWith("\\")
//					|| noneHyfenArgs.get(0).startsWith("/")) {
//
//				props.setRootDir(noneHyfenArgs.get(0));
//			}
//			else {
			props.setRootDir(noneHyfenArgs.get(0));
//			}

		} else if (noneHyfenArgs.size() >= 2) {
			props.setRootDir(noneHyfenArgs.get(0));
			props.setTargetDir(noneHyfenArgs.get(1));
		}

		if (props.getRootDir() == null) {
			println("The root directory is not set. Using the current dir.");
			props.setRootDir(System.getProperty("user.dir"));
		}


		println("");


		props.setSuccess(true);

		return props;

	}


}
