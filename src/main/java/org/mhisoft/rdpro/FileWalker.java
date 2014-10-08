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

	public FileWalker( Workers workerPool,
			final String targetDeleteDir, final boolean verbose,
			final boolean interactive, final boolean forceDelete) {
		this.workerPool = workerPool;
		this.targetDeleteDir = targetDeleteDir;
		this.verbose = verbose;
		this.forceDelete = forceDelete;
		this.interactive = interactive;
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
									System.out.println("skip dir " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}
						}
					}

					//recursively delete everything.
					//no need to walk down any more.
					Runnable task = new DeleteDirWorkerThread(f.getAbsolutePath(), 0, verbose, frs);
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
									System.out.println("skip file " + f.getAbsoluteFile() + ", not deleted.");
								continue;
							}
						}
					}

					/*delete the files*/
					if (f.delete()) {
						if (verbose)
							System.out.println("\tRemoved file:" + f.getAbsolutePath());
						frs.filesRemoved++;
					} else
						System.out.println("\t[warn]Can't remove file:" + f.getAbsolutePath() + ". Is it being locked?");
				}
			}
		}   //loop all the files and dires under root
	}

}
