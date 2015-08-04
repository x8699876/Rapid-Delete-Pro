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
package org.mhisoft.rdpro;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mhisoft.rdpro.ui.Confirmation;
import org.mhisoft.rdpro.ui.ConsoleRdProUIImpl;
import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description: Recursive Delete Pro
 *
 * @author Tony Xue
 * @since Sept 2014
 */
public class RdPro {
	public static boolean debug = Boolean.getBoolean("debug");

	FileRemoveStatistics frs = new FileRemoveStatistics();
	Workers workerPool;

	public RdProUI rdProUI;

	public RdPro(RdProUI rdProUI) {
		this.rdProUI = rdProUI;
	}

	public RdProUI getRdProUI() {
		return rdProUI;
	}

	public FileRemoveStatistics getStatistics() {
		return frs;
	}


	/**
	 * Run time properties
	 */
	public static class RdProRunTimeProperties {
		String rootDir = null;
		String targetDir = null;

		boolean verbose = false;
		boolean forceDelete = false;
		boolean interactive = true;
		Integer numberOfWorkers = 5;

		boolean success = true;
		boolean answerYforAll = false;
		boolean debug = false;

		public String getRootDir() {
			return rootDir;
		}

		public void setRootDir(String rootDir) {
			this.rootDir = rootDir;
		}

		public String getTargetDir() {
			return targetDir;
		}

		public void setTargetDir(String targetDir) {
			this.targetDir = targetDir;
		}

		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}

		public boolean isForceDelete() {
			return forceDelete;
		}

		public void setForceDelete(boolean forceDelete) {
			this.forceDelete = forceDelete;
		}

		public boolean isInteractive() {
			return interactive;
		}

		public void setInteractive(boolean interactive) {
			this.interactive = interactive;
		}

		public Integer getNumberOfWorkers() {
			return numberOfWorkers;
		}

		public void setNumberOfWorkers(Integer numberOfWorkers) {
			this.numberOfWorkers = numberOfWorkers;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public boolean isAnswerYforAll() {
			return answerYforAll;
		}

		public void setAnswerYforAll(boolean answerYforAll) {
			this.answerYforAll = answerYforAll;
		}

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("RdProRunTimeProperties{");
			sb.append("rootDir='").append(rootDir).append('\'');
			sb.append(", targetDir='").append(targetDir).append('\'');
			sb.append(", verbose=").append(verbose);
			sb.append(", forceDelete=").append(forceDelete);
			sb.append(", interactive=").append(interactive);
			sb.append(", numberOfWorkers=").append(numberOfWorkers);
			sb.append(", success=").append(success);
			sb.append(", answerYforAll=").append(answerYforAll);
			sb.append(", debug=").append(debug);
			sb.append('}');
			return sb.toString();
		}
	}


	public void run(RdProRunTimeProperties props) {
		rdProUI.println(String.format("Removed target \"%s\" under dir \"%s\".", props.getTargetDir() == null ? "*" : props.getTargetDir(), props.rootDir));
		workerPool = new Workers(props.numberOfWorkers, rdProUI);
		FileWalker fw = new FileWalker(rdProUI, workerPool, props, frs);
		long t1 = System.currentTimeMillis();

		fw.walk(props.rootDir);
		workerPool.shutDownandWaitForAllThreadsToComplete();

		//now try to remove the root
		File root = new File(props.rootDir);
		FileUtils.removeDir(root, rdProUI, frs, props.isVerbose());

		rdProUI.println("\nDone in " + (System.currentTimeMillis() - t1) / 1000 + " seconds.");
		rdProUI.println("Dir Removed:" + frs.dirRemoved + ", Files removed:" + frs.filesRemoved);
	}


	public static void main(String[] args) {
		RdPro rdpro = new RdPro(new ConsoleRdProUIImpl());
		RdPro.RdProRunTimeProperties props = rdpro.getRdProUI().parseCommandLineArguments(args);
		if (props.isDebug())
			rdpro.getRdProUI().dumpArguments(args, props);


		Path path = Paths.get(props.getRootDir()) ;

		if (Files.notExists(path)) {
			rdpro.getRdProUI().print("root dir does not exist:" +  props.getRootDir()) ;
			System.exit(-2);
		}

		rdpro.getRdProUI().printBuildAndDisclaimer();

		if (props.getTargetDir() != null) {

			Confirmation confirmation  = rdpro.getRdProUI().getConfirmation("Start to delete all the directories named \"" + props.getTargetDir() + "\" under \""
					+ props.getRootDir() + "\".\nThere is no way to undelete, please confirm? (y/n/q or h for help)",
					Confirmation.HELP, Confirmation.YES, Confirmation.NO, Confirmation.QUIT )  ;

			if (confirmation!=Confirmation.YES) {
				return;
			}
		} else {
			boolean b = rdpro.getRdProUI().isAnswerY("Start to delete everything under \"" + props.getRootDir() + "\" (y/n or h for help)?");
			if (b) {
				if (!rdpro.getRdProUI().isAnswerY(" *Warning* There is no way to undelete. Confirm again (y/n/q or h for help)?")) {
					return;
				}
			}


			rdpro.getRdProUI().print("working.");
			rdpro.run(props);
		}
	}

}


