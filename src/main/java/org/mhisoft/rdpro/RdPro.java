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
package org.mhisoft.rdpro;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description: Recursive Delete Pro
 *
 * @author Tony Xue
 * @since Sept 2014
 */
public class RdPro {
	public static final String version = "v0.9 ";
	public static final String build = "build 203";

	public static boolean debug = Boolean.getBoolean("debug");

	String startFromPath = null;
	String target = null;

	boolean verbose = false;
	boolean forceDelete = false;
	boolean interactive = true;
	Integer numberOfWorkers = 5;
	Workers workerPool;

	static Logger logger;

	public RdPro(Logger _logger) {
		logger = _logger;
	}

	public static void help() {
		logger.println("RdPro  - A Powerful Recursive Directory Purge Utility (" +
				version + build + " MHISoft, Oct 2014)");
		logger.println("Disclaimer:");
		logger.println("\tDeleted files does not go to recycle bean and can't be recovered.");
		logger.println("\tThe author is not responsible for any lost of files or damage incurred by running this utility.");
		logger.println("Usages:");
		logger.println("\t rdpro [option] path-to-search [target-dir] ");
		logger.println("\t  path-to-search  root path to search, default to the current dir.");
		logger.println("\t -d/-dir specify the target dir");
		logger.println("\t -f force delete");
		logger.println("\t -i interactive, default true");
		logger.println("\t -v verbose mode");
		/*logger.println("\t -w number of worker threads, default 5");*/
		logger.println("Examples:");
		logger.println("\tRemove everything under a dir (purge a direcotry and everthing under it): rdpro c:\\mytempfiles");
		logger.println("\tRemove all directories that matches a specified name recursively: ");
		logger.println("\t\trdpro -d target s:\\projects");
		logger.println("\t\trdpro s:\\projects target");
		/*logger.println("\tRemove files matches a pattern recursively: rdpro s:\\projects -d target *.war ");*/
	}


	static boolean isAnswerY(String question) {
		String a = getConfirmation(question, "y", "n", "h");
		if (a.equalsIgnoreCase("h")) {
			help();
			return false;
		} else if (!a.equalsIgnoreCase("y")) {
			return false;
		}
		return true;
	}


	static String getConfirmation(String question, String... options) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		logger.print(question);
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
					logger.print("\tresponse \"" + a + "\" not recognized. input again:");
					a=null; //keep asking
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;

	}

	boolean parseArguments(String[] args) {

//		for (String arg : args) {
//			logger.println("arg=[" +arg+"]" );
//		}


		List<String> noneHyfenArgs = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help")) {
				help();
				return false;
			} else if (arg.equalsIgnoreCase("-v")) {
				verbose = true;
			} else if (arg.equalsIgnoreCase("-w")) {

				try {
					numberOfWorkers = Integer.parseInt(args[i + 1]);
					i++; //skip the next arg, it is the target.
				} catch (NumberFormatException e) {
					numberOfWorkers = 5;
				}

			} else if (arg.equalsIgnoreCase("-f")) {
				forceDelete = true;
				interactive = false;
			} else if (arg.equalsIgnoreCase("-i")) {
				interactive = true;
				forceDelete = false;
			} else if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("-dir")) {
				target = args[i + 1];
				i++; //skip the next arg, it is the target.

			} else {
				if (arg.startsWith("-")) {
					System.err.println("The argument is not recognized:" + arg);
					return false;
				} else
					//not start with "-"
					if (arg!=null && arg.trim().length()>0)
						noneHyfenArgs.add(arg);
			}


		}


		if (noneHyfenArgs.size() == 0) {
			startFromPath = System.getProperty("user.dir");
		} else if (noneHyfenArgs.size() == 1) {
			//rdpro d:\temp -d classes
			if (target != null)
				startFromPath = noneHyfenArgs.get(0);

			//rdpro d:\temp
			else if (noneHyfenArgs.get(0).contains(":")  || noneHyfenArgs.get(0).startsWith("\\")
					|| noneHyfenArgs.get(0).startsWith("/") ) {

				startFromPath = noneHyfenArgs.get(0);
			}
			else {
				//rdpro classes
				startFromPath = System.getProperty("user.dir");
				target = noneHyfenArgs.get(0);
			}

		} else {
			startFromPath = noneHyfenArgs.get(0);
			if (noneHyfenArgs.size() >= 2)
				target = noneHyfenArgs.get(1);
		}

		if (startFromPath == null)
			startFromPath = System.getProperty("user.dir");


		logger.println("");


		if (target != null) {
			if (!isAnswerY("Start to delete all the directories named \"" + target + "\" under \""
					+ startFromPath + "\".\nThere is no way to undelete, please confirm? (y/n or h for help)"))
				return false;

		}
		else {
			boolean b = isAnswerY("Start to delete everything under \"" + startFromPath + "\" (y/n or h for help)?");
			if (b) {
				if (!isAnswerY(" *Warning* There is no way to undelete. Confirm again (y/n or h for help)?")) {
					return false;
				}
			}
			else {
				return false;
			}
		}


		return true;

	}


	public void run() {
		workerPool = new Workers(this.numberOfWorkers, logger);
		FileWalker fw = new FileWalker(logger, workerPool, target, verbose, interactive, forceDelete);
		long t1 = System.currentTimeMillis();
		logger.print("working.");
		fw.walk(startFromPath);

		workerPool.shutDownandWaitForAllThreadsToComplete();

		logger.println("\nDone in " + (System.currentTimeMillis() - t1) / 1000 + " seconds.");
		logger.println("Dir Removed:" + fw.frs.dirRemoved + ", Files removed:" + fw.frs.filesRemoved);

	}


	public static void main(String[] args) {
		RdPro rd = new RdPro(new ConsoleLoggerImpl());
		if (rd.parseArguments(args))
			rd.run();


	}


}


