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

import java.io.File;
import java.io.FilenameFilter;

/**
* Description:
*
* @author Tony Xue
* @since Oct, 2014
*/
public class FileWalker {


	String targetDeleteDir;
	boolean verbose;
	boolean interactive;
	boolean forceDelete;
	//Integer threads;
	FileRemoveStatistics frs = new FileRemoveStatistics();
	boolean lastAnsweredDeleteAll = false;
	Workers workerPool;
	Logger logger;

	public FileWalker( Logger logger,
			Workers workerPool,
			final String targetDeleteDir, final boolean verbose,
			final boolean interactive, final boolean forceDelete) {
		this.workerPool = workerPool;
		this.targetDeleteDir = targetDeleteDir;
		this.verbose = verbose;
		this.forceDelete = forceDelete;
		this.interactive = interactive;
		this.logger = logger;
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

		boolean isRootMatchDirPattern = targetDeleteDir == null || root.getAbsolutePath().endsWith(targetDeleteDir);


		for (File f : list) {
			if (f.isDirectory()) {

				if (targetDeleteDir == null || f.getAbsolutePath().endsWith(targetDeleteDir)) {
					if (!forceDelete) {

						if (!lastAnsweredDeleteAll) {
							String a = RdPro.getConfirmation(("\nConfirm to remove the dir and everything under it:" + f.getAbsoluteFile() + "(y/n/all)?")
									, "y", "n", "all");
							if (a.equalsIgnoreCase("all")) {
								lastAnsweredDeleteAll = true;
							} else if (!a.equalsIgnoreCase("y")) {
								if (verbose)
									logger.println("skip dir " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}
						}
					}

					//recursively delete everything.
					//no need to walk down any more.
					Runnable task = new DeleteDirWorkerThread(logger, f.getAbsolutePath(), 0, verbose, frs);
					workerPool.addTask(task);
				} else {
					//keep walking down
					walk(f.getAbsolutePath());
				}

			}
			else {
				if (isRootMatchDirPattern) {

					if (!forceDelete) {

						if (!lastAnsweredDeleteAll) {
							String a = RdPro.getConfirmation("\nConfirm to delete file:" + f.getAbsoluteFile() + "(y/n/all)?", "y", "n", "all");
							if (a.equalsIgnoreCase("all")) {
								lastAnsweredDeleteAll = true;
							}
							else if (!a.equalsIgnoreCase("y")) {
								if (verbose)
									logger.println("skip file " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}
						}
					}

					/*delete the files*/
					if (f.delete()) {
						if (verbose)
							logger.println("\tRemoved file:" + f.getAbsolutePath());
						frs.filesRemoved++;
					} else
						logger.println("\t[warn]Can't remove file:" + f.getAbsolutePath() + ". Is it being locked?");
				}
			}
		}   //loop all the files and dires under root
	}

}
