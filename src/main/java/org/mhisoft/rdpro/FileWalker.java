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


	RdPro.RdProRunTimeProperties props;
	//Integer threads;
	boolean lastAnsweredDeleteAll = false;
	boolean initialConfirmation = false;
	Workers workerPool;
	RdProUI rdProUI;
	FileRemoveStatistics frs;

	public FileWalker( RdProUI rdProUI,
			Workers workerPool,
			RdPro.RdProRunTimeProperties props
			,	FileRemoveStatistics frs
	) {
		this.workerPool = workerPool;
		this.props = props;
		this.rdProUI = rdProUI;
		this.frs = frs;
	}


	public void walk(final String path) {

		File root = new File(path);
		File[] list = root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true; //todo
			}
		});

		if (list == null) return;

		boolean isRootMatchDirPattern = props.getTargetDir() == null || root.getAbsolutePath().endsWith(props.getTargetDir());

//		if (!initialConfirmation) {
//			String q ;
//			if (props.getTargetDir() != null)
//				q = "Confirm to remove the dir and everything under \"" + props.getTargetDir() +"\"?";
//			else
//				q = "Start to delete everything under \"" + props.getRootDir() + "\"";
//
//			RdProUI.Confirmation a = rdProUI.getConfirmation(q);
//			if (a!= RdProUI.Confirmation.YES) {
//				return;
//			}
//			initialConfirmation = true;
//		}


		for (File f : list) {


			if (RdPro.isStopThreads()) {
				rdProUI.println("[warn]Cancelled by user. stop walk. ");
				return;
			}

			if (f.isDirectory()) {
				if (props.getTargetDir() == null || f.getAbsolutePath().endsWith(props.getTargetDir())) {
					if (!props.isForceDelete()) {

						if (!lastAnsweredDeleteAll) {
							Confirmation a = rdProUI.getConfirmation(("\nConfirm to remove the dir and everything under it:\n" + f.getAbsoluteFile() + "(y/n/all)?")
									, Confirmation.YES, Confirmation.NO, Confirmation.YES_TO_ALL, Confirmation.QUIT);
							;
							if (a== Confirmation.YES_TO_ALL) {
								lastAnsweredDeleteAll = true;
							} else if (a!= Confirmation.YES) {
								if (props.isVerbose())
									rdProUI.println("skip dir " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}
						}
					}

					//recursively delete everything.
					//no need to walk down any more.
					Runnable task = new DeleteDirWorkerThread(rdProUI, f.getAbsolutePath(), 0, props.isVerbose(), frs);
					workerPool.addTask(task);
				} else {
					//keep walking down
					walk(f.getAbsolutePath());
				}

			}
			else {
				if (isRootMatchDirPattern) {

					if (!props.isForceDelete()) {

						if (!lastAnsweredDeleteAll) {
							Confirmation a = rdProUI.getConfirmation("\nConfirm to delete file:" + f.getAbsoluteFile() + "(y/n/all)?"
									, Confirmation.YES, Confirmation.NO, Confirmation.YES_TO_ALL,  Confirmation.QUIT);
							if (a== Confirmation.YES_TO_ALL) {
								lastAnsweredDeleteAll = true;
							}
							else if (a!= Confirmation.YES) {
								if (props.isVerbose())
									rdProUI.println("skip file " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}
						}
					}

					/*delete the files*/
					if (f.delete()) {
						if (props.isVerbose())
							rdProUI.println("\tRemoved file:" + f.getAbsolutePath());
						frs.filesRemoved++;
					} else
						rdProUI.println("\t[warn]Can't remove file:" + f.getAbsolutePath() + ". Is it being locked?");
				}
			}
		}   //loop all the files and dires under root

	}

}
