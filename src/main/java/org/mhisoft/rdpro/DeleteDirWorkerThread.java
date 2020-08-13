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
import java.util.ArrayList;
import java.util.List;

import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description: REMOVE EVERYTHING UNDER a directory
 *
 * @author Tony Xue
 * @since Sept 2014
 */
public class DeleteDirWorkerThread implements Runnable {

	static final int TRIGGER_MULTI_THREAD_THRESHHOLD = 20;

	private String dir;
	private RdProRunTimeProperties props;
	private FileRemoveStatistics frs;
	private RdProUI rdProUI;
	int depth = 0;
	FileWalker fileWalker;


	public DeleteDirWorkerThread(FileWalker fileWalker, RdProUI rdProUI, String _dir, int depth
			, RdProRunTimeProperties props, FileRemoveStatistics frs) {
		this.dir = _dir;
		this.props = props;
		this.frs = frs;
		this.depth = depth;
		this.rdProUI = rdProUI;
		this.fileWalker = fileWalker;
	}

	@Override
	public void run() {
		if (RdPro.debug)
			rdProUI.println(Thread.currentThread().getName() + " Starts");
		long t1 = System.currentTimeMillis();
		purgeDirectory(new File(this.dir), depth);
		//rdProUI.println("Removed Dir:" + this.dir);
		if (RdPro.debug)
			rdProUI.println("\t" + Thread.currentThread().getName() + " End. took " + (System.currentTimeMillis() - t1) + "ms");

	}




	void purgeDirectory(File dir, int depth) {
		if (dir==null)
			 return;

		if (RdPro.isStopThreads()) {
			rdProUI.println("Cancelled by user. Stop thread " + Thread.currentThread().getName());
			return;
		}

		if (RdPro.debug)
			rdProUI.println("purgeDirectory()- [" + Thread.currentThread().getName() + "] depth=" + depth + ", " + dir);

		List<File> childDirList = new ArrayList<File>();
		File[] childFIles = dir.listFiles();

		int deletedFiles=0;

		if ( childFIles!=null) {
			for (File file : childFIles) {
				if (file.isDirectory()) {

					if (UnlinkDirHelper.unLinkDir(rdProUI, props, file)) {
						continue; //remove the link only, exlcude from purge.
					}

					childDirList.add(file);
					//purgeDirectory(file);   --to be peruged in parallel later
				} else {

				    /*it is file. delete these files under the dir*/
					deletedFiles = fileWalker.tryDeleteFile(file);

				}
			}

		/*
		//dive deep into the child directories
		//process the dirs in parallel
		*/
			parallelRemoveDirs(childDirList);
		}


		String sDir = dir.getAbsolutePath();

		//now purge this dir
		showProgress();

		//if the getTargetFilePatterns is specified, the dir may not be empty, don't delete if it is not empty
		boolean hasFilePattern = props.getTargetFilePatterns()!=null &&  props.getTargetFilePatterns().length>0 ;

		if (hasFilePattern) {
			if (deletedFiles > 0 && FileUtils.isDirectoryEmpty(rdProUI, sDir)) {
				FileUtils.removeDir(dir, rdProUI, frs, props);
			}
		}
		else  {
			if (FileUtils.isDirectoryEmpty(rdProUI, sDir))
				FileUtils.removeDir(dir, rdProUI, frs, props);
		}



	}

	public void parallelRemoveDirs(List<File> childDirList) {
		depth++;
		if (childDirList.size() > TRIGGER_MULTI_THREAD_THRESHHOLD) {
			Workers workerpool = new Workers(5, rdProUI);
			for (File childDir : childDirList) {

				if (RdPro.isStopThreads()) {
					rdProUI.println("Cancelled by user. Stop thread " + Thread.currentThread().getName());
					return;
				}
				DeleteDirWorkerThread task = new DeleteDirWorkerThread(this.fileWalker, rdProUI, childDir.getAbsolutePath(), depth, props, frs);
				workerpool.addTask(task);
			}

			workerpool.shutDownandWaitForAllThreadsToComplete();

		} else {
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
		rdProUI.printf("%s", spinner[i % spinner.length]);
	}

}
