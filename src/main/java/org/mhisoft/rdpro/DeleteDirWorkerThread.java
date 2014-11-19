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
import java.io.File;

/**
 * Description: REMOVE EVERTHING UNDER a directory
 *
 * @author Tony Xue
 * @since Sept 2014
 */
public class DeleteDirWorkerThread implements Runnable {

	static final int TRIGGER_MULTI_THREAD_THRESHHOLD=20;

	private String dir;
	private boolean verbose;
	private FileRemoveStatistics frs;
	private Logger logger;
	int depth = 0;


	public DeleteDirWorkerThread(Logger logger, String _dir, int depth,  boolean verbose, FileRemoveStatistics frs) {
		this.dir = _dir;
		this.verbose = verbose;
		this.frs = frs;
		this.depth=depth;
		this.logger=logger;
	}

	@Override
	public void run() {
		if (RdPro.debug)
			System.out.println(Thread.currentThread().getName() + " Starts");
		long t1 = System.currentTimeMillis();
		purgeDirectory(new File(this.dir), depth);
		//System.out.println("Removed Dir:" + this.dir);
		if (RdPro.debug)
			System.out.println("\t" + Thread.currentThread().getName() + " End. took " + (System.currentTimeMillis() - t1) + "ms");

	}

	void purgeDirectory(File dir, int depth) {
		if (RdPro.debug)
			System.out.println("purgeDirectory()- ["+Thread.currentThread().getName()+"] depth=" + depth + ", " + dir);

		List<File> childDirList = new ArrayList<File>();

		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				childDirList.add(file);
				//purgeDirectory(file);   --moved
			} else {
				/*it is file. delete these files under the dir*/
				if (file.delete()) {
					frs.filesRemoved++;
					if (verbose)
						System.out.println("\tRemoved file:" + file.getAbsolutePath());
				} else {
					System.out.println("\t[warn]Can't remove file:" + dir.getAbsolutePath() + ". Is it being locked?");
				}
			}
		}

		/*
		//dive deep into the child directories
		//process the dirs in parallel
		*/
		parallelRemoveDirs(childDirList);


		String s = dir.getAbsolutePath();

		//now purge this dir
		showProgress();
		if (!dir.delete()) {
			if (RdPro.debug)
				System.err.println("\t[warn]Can't remove:" + dir.getAbsolutePath() + ".");
		} else {
			frs.dirRemoved++;
			if (verbose)
				System.out.println("\tRemoved dir:" + s);
		}

	}

	public void parallelRemoveDirs(List<File> childDirList) {
		depth++;
		if (childDirList.size()>TRIGGER_MULTI_THREAD_THRESHHOLD) {
		    Workers workerpool = new Workers(5, logger);
			for (File childDir : childDirList) {
				DeleteDirWorkerThread task = new DeleteDirWorkerThread(logger, childDir.getAbsolutePath(), depth, verbose, frs);
				workerpool.addTask(task);
			}

			workerpool.shutDownandWaitForAllThreadsToComplete();

		}
		else {
			for (File childDir : childDirList) {
				purgeDirectory(childDir, depth);
			}
		}
	}


	@Override
	public String toString() {
		return this.dir;
	}

	String[] spinner = new String[]{"\u0008/", "\u0008-", "\u0008\\", "\u0008|"};
	int i = 0;

	public void showProgress() {
		i++;
		if (i >= Integer.MAX_VALUE)
			i = 0;
		System.out.printf("%s", spinner[i % spinner.length]);
	}

}
