package org.mhisoft.rdpro;

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

	RdProRunTimeProperties props;
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


	public void run() {
		workerPool = new Workers(props.numberOfWorkers, rdProUI);
		FileWalker fw = new FileWalker(rdProUI, workerPool, props);
		long t1 = System.currentTimeMillis();
		rdProUI.print("working.");
		fw.walk(props.rootDir);

		workerPool.shutDownandWaitForAllThreadsToComplete();

		rdProUI.println("\nDone in " + (System.currentTimeMillis() - t1) / 1000 + " seconds.");
		rdProUI.println("Dir Removed:" + fw.frs.dirRemoved + ", Files removed:" + fw.frs.filesRemoved);
	}


	public static void main(String[] args) {
		RdPro rdpro = new RdPro(new ConsoleRdProUIImpl());
		if (rdpro.getRdProUI().parseCommandLineArguments(args).isSuccess())
			rdpro.run();
	}


}


