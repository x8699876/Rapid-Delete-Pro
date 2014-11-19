package org.mhisoft.rdpro;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: a pool of workers
 *
 * @author Tony Xue
 * @since Oct, 2014
 */
public class Workers {

	private  ThreadPoolExecutor executor;
	static int QUESIZE = 5000;
	private Logger logger;

	//creating the ThreadPoolExecutor
	public Workers(final int corePoolSize, final Logger _logger) {
		//executor = Executors.newFixedThreadPool(corePoolSize);
		this.logger= _logger;

		ThreadFactory threadFactory = Executors.defaultThreadFactory();


		executor = new ThreadPoolExecutor(corePoolSize, corePoolSize
				 , 10, TimeUnit.SECONDS
				//LinkedBlockingQueue is an Unbounded queues.
				// Thus, no more than corePoolSize threads will ever be created.
				// (And the value of the maximumPoolSize therefore doesn't have any effect.)
				, new LinkedBlockingQueue<Runnable>()
				, threadFactory
				, new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				logger.println("[warn]rejected thread:" + r.toString());
			}
		});

	}

	public  ExecutorService getExecutor() {
		return this.executor;
	}

//	public  void shutDown() {
//		executor.shutdown();
//	}

	public  void addTask(Runnable task) {
		this.executor.execute(task);
	}

	public void shutDownandWaitForAllThreadsToComplete() {
		executor.shutdown();
		while (!this.executor.isTerminated()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
