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

	;
	Workers workerPool;

	public  RdProUI rdProUI;

	public RdPro(RdProUI rdProUI) {
		this.rdProUI = rdProUI;
	}

	public RdProUI getRdProUI() {
		return rdProUI;
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

		boolean success=true;

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
	}


	public void run(RdProRunTimeProperties props) {
		workerPool = new Workers(props.numberOfWorkers, rdProUI);
		FileWalker fw = new FileWalker(rdProUI, workerPool, props);
		long t1 = System.currentTimeMillis();

		fw.walk(props.rootDir);
		workerPool.shutDownandWaitForAllThreadsToComplete();

		//now try to remove the root
		File root = new File(props.rootDir);
		FileUtils.removeDir(root, rdProUI );

		rdProUI.println("\nDone in " + (System.currentTimeMillis() - t1) / 1000 + " seconds.");
		rdProUI.println("Dir Removed:" + fw.frs.dirRemoved + ", Files removed:" + fw.frs.filesRemoved);
	}


	public static void main(String[] args) {
		RdPro rdpro = new RdPro(new ConsoleRdProUIImpl());
		RdPro.RdProRunTimeProperties props = rdpro.getRdProUI().parseCommandLineArguments(args);
		if (props.isSuccess()) {
			rdpro.getRdProUI().print("working.");
			rdpro.run(props);
		}
	}


}


