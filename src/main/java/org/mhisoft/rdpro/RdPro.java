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

import java.util.Arrays;
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
	//-Ddebug=true
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

	public static boolean stopThreads=false;
	private boolean running;

	public static boolean isStopThreads() {
		return stopThreads;
	}


	public static void setStopThreads(boolean stopThreads) {
		RdPro.stopThreads = stopThreads;
	}

	public void stopWorkers() {
		workerPool.shutDown();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}


	public void run(RdProRunTimeProperties props) {
		rdProUI.println(String.format("Remove target \"%s\" under dir \"%s\".", props.getTargetDir() == null ? "*" : props.getTargetDir(), props.rootDir));
		rdProUI.println("\tFile pattern to match:" + (props.getTargetFilePatterns()==null?"None.": Arrays.toString( props.getTargetFilePatterns() )) );
		workerPool = new Workers(props.numberOfWorkers, rdProUI);
		frs.reset();
		FileWalker fw = new FileWalker(rdProUI, workerPool, props, frs);
		long t1 = System.currentTimeMillis();

		String[] files = FileUtils.split(props.rootDir, ';', ',');
		boolean didNotAbortWalkDirs = fw.walk(files);
		workerPool.shutDownandWaitForAllThreadsToComplete();

		//now try to remove the root
		if (didNotAbortWalkDirs && props.getTargetFilePatterns()==null) {
			File root = new File(props.rootDir);
			FileUtils.removeDir(root, rdProUI, frs, props.isVerbose(), props.unLinkDirFirst);
		}

		rdProUI.println("\nDone in " + (System.currentTimeMillis() - t1) / 1000 + " seconds.");
	}


	public static void main(String[] args) {
		RdPro rdpro = new RdPro(new ConsoleRdProUIImpl());
		RdProRunTimeProperties props = rdpro.getRdProUI().parseCommandLineArguments(args);
		if (props.isDebug())
			rdpro.getRdProUI().dumpArguments(args, props);

		if (!props.isSuccess())  {
			System.exit(-1);
		}


		Path path = Paths.get(props.getRootDir()) ;

		if (Files.notExists(path)) {
			rdpro.getRdProUI().print("root dir does not exist:" +  props.getRootDir()) ;
			System.exit(-2);
		}

		rdpro.getRdProUI().printBuildAndDisclaimer();


		String msg ;

		if (props.getTargetDir() != null) {
			if (props.getTargetFilePatterns() != null) {
				msg = "Start to delete files that match pattern " + props.getTargetFilePatternString()  //
						+" for all the target directories named \""  + props.getTargetDir()
						+ "\" under root \""+ props.getRootDir() +"\".";
			}
			else {
				msg = "Start to delete all the target directories named \""  + props.getTargetDir()
						+ "\" under root \""+ props.getRootDir() +"\".";
			}

		}
		else {
			if (props.getTargetFilePatterns() == null)
				msg = "Start to delete everything under root \"" + props.getRootDir();
			else {
				msg = "Start to delete files that match pattern " + props.getTargetFilePatternString()  //
						+" under root \"" + props.getRootDir() + "\".";
			}
		}


		rdpro.getRdProUI().println("\n"+msg);;

		if (props.isForceDelete()) {
			rdpro.getRdProUI().println("\nYou have specified to force delete without further confirmation on each directory removal!");;
		}

		String confirmMsg = "There is no way to un-delete, please confirm? (y/n/q or h for help)";

		if (! (props.forceDelete && props.isAnswerYforAll())) {

				//ask
				Confirmation confirmation = rdpro.getRdProUI().getConfirmation(confirmMsg
						, Confirmation.HELP, Confirmation.YES, Confirmation.NO, Confirmation.QUIT);

				if (confirmation == Confirmation.HELP) {
					rdpro.getRdProUI().help();
					return;
				}
				else if (confirmation != Confirmation.YES)
					return;


			//ask again
				if (!rdpro.getRdProUI().isAnswerY(" *Warning* There is no way to undelete. Confirm again (y/n/q or h for help)?"))
						return;

		}
		else {
			rdpro.getRdProUI().println("Bypassing user interactions.")  ;
		}


		rdpro.getRdProUI().print("working.");
		rdpro.run(props);
	}

}


