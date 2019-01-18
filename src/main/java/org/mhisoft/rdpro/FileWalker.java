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
import java.io.FilenameFilter;

import org.mhisoft.rdpro.ui.Confirmation;
import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description: walk the directory and schedule works to remove the target files and directories.
 *
 * @author Tony Xue
 * @since Oct, 2014
 */
public class FileWalker {


	RdProRunTimeProperties props;
	//Integer threads;
	boolean lastAnsweredDeleteAll = false;
	boolean initialConfirmation = false;
	Workers workerPool;
	RdProUI rdProUI;
	FileRemoveStatistics frs;
	boolean quit = false;

	public FileWalker(RdProUI rdProUI,
			Workers workerPool,
			RdProRunTimeProperties props
			, FileRemoveStatistics frs
	) {
		this.workerPool = workerPool;
		this.props = props;
		this.rdProUI = rdProUI;
		this.frs = frs;
	}

	public boolean walk(final String[] sourceFileDirs) {

		for (String source : sourceFileDirs) {

			if (RdPro.isStopThreads()) {
				rdProUI.println("[warn]Cancelled by user. stop walk. ");
				return false;
			}

			if (quit)
				return false;

			File fSource = new File(source);
			if (fSource.isFile()) {
				//delete the file
				 tryDeleteFile(fSource);
			} else if (fSource.isDirectory()) {
				rdProUI.println(String.format("Remove target \"%s\" under dir \"%s\".", props.getTargetDir() == null ? "*" : props.getTargetDir(), source));
       			walkSubDir(source);
			}
		}

		return false ;

	}


	/**
	 *
	 * @param dir
	 * @return  false when use quites.
	 */
	protected boolean walkSubDir(final String dir) {

		if (quit)
			return false;

		File root = new File(dir);
		File[] list = root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true; //todo
			}
		});

		if (list == null) return true;

		boolean isRootMatchDirPattern = props.getTargetDir() == null || root.getAbsolutePath().endsWith(props.getTargetDir());

		if (root.isDirectory() && isRootMatchDirPattern) {
			//root is the dir to be unlinked?
			if (UnlinkDirHelper.unLinkDir(rdProUI, props, root)) {
				return true;
			}
		}



		for (File f : list) {


			if (RdPro.isStopThreads()) {
				rdProUI.println("[warn]Cancelled by user. stop walk. ");
				return false;
			}

			if (f.isDirectory()) {

				//this dir matches the target dir
				// or there is no target specified.
				//If target file patttern is specified, we will only delete files
				if ((props.getTargetDir() == null || f.getAbsolutePath().endsWith(props.getTargetDir())) //
						//&& props.getTargetFilePatterns() == null //comment out, we still need to walk down the directory and find matched files only
				)
				{

					if (!props.isForceDelete() && !lastAnsweredDeleteAll //need to ask confirmation again
							&& props.getTargetFilePatterns()==null ) { //no file patterns to match, remove whole sub dir

							String msg = "\nConfirm to remove the directory and everything under it:\n"
										+ f.getAbsoluteFile() + "(y/n/all)?";
 							Confirmation a = rdProUI.getConfirmation(msg
									, Confirmation.YES, Confirmation.NO, Confirmation.YES_TO_ALL, Confirmation.QUIT);
							;
							if (a == Confirmation.YES_TO_ALL) {
								lastAnsweredDeleteAll = true;
							} else if (a == Confirmation.QUIT) {
								if (props.isVerbose())
									rdProUI.println("User abort.");
								this.quit = true;
								return false;
							} else if (a != Confirmation.YES) {
								if (props.isVerbose())
									rdProUI.println("skip dir " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}

					}

					if (UnlinkDirHelper.unLinkDir(rdProUI, props, f)) {
						rdProUI.println("\t*Unlinked dir:" + f);
					} else {
						//recursively delete everything.
						//no need to walk down any more.
						Runnable task = new DeleteDirWorkerThread(this, rdProUI, f.getAbsolutePath(), 0, props, frs);
						workerPool.addTask(task);
					}
				} else {
					//keep walking down
					walkSubDir(f.getAbsolutePath());
				}

			} else {
				/*it is a file*/
				if (isRootMatchDirPattern) {

					int ret =  tryDeleteFile(f);
					if (ret==-1) {
						return false; //quit
					}
				}
			}
		}   //loop all the files and dires under root


		/* remove the root dir */
		if (root.isDirectory() && isRootMatchDirPattern) {
			Runnable task = new DeleteDirWorkerThread(this,rdProUI, root.getAbsolutePath(), 0, props, frs);
			workerPool.addTask(task);
		}

		return true;

	}

	protected int tryDeleteFile(File f) {

		boolean filePatternMatch = FileUtils.isFileMatchTargetFilePatterns(f, props.getTargetFilePatterns());
		if (filePatternMatch) {

			if (!props.isForceDelete()) {

				if (!lastAnsweredDeleteAll) {
					Confirmation a = rdProUI.getConfirmation("\nConfirm to delete file:" + f.getAbsoluteFile() + "(y/n/all)?"
							, Confirmation.YES, Confirmation.NO, Confirmation.YES_TO_ALL, Confirmation.QUIT);

					if (a == Confirmation.YES_TO_ALL) {
						lastAnsweredDeleteAll = true;
					} else if (a == Confirmation.QUIT) {
						if (props.isVerbose())
							rdProUI.println("User abort.");
						this.quit = true;
						return -1; //-------> quit
					} else if (a != Confirmation.YES) {
						if (props.isVerbose())
							rdProUI.println("skip file " + f.getAbsoluteFile() + ", not deleted.");
						return -2;   //---------> skip this file
					}
				}
			}

			/*delete the files*/
			if (f.delete()) {
				if (props.isVerbose())
					rdProUI.println("\tRemoved file:" + f.getAbsolutePath());
				frs.filesRemoved++;
				rdProUI.reportStatus(frs);
			} else {
				if (f.exists())
				rdProUI.println("\t[warn]Can't remove file:" + f.getAbsolutePath() + ". Is it being locked?");
			}
		}
		return 0;  //---------> continue to next file
	}

}
