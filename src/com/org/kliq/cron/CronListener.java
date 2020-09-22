package com.org.kliq.cron;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Anand
 * This is the Context Listener which uses Executors
 * in java to send group invites
 * It is stopped when the app is taken out
 *
 */
public class CronListener implements ServletContextListener {

	private final ScheduledExecutorService scheduler =
		       Executors.newScheduledThreadPool(1);
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		scheduler.shutdownNow();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		System.out.println("context initialized");
		final CronWorker  worker = new CronWorker();
		try {
		//Set the worker to run every 15 mins
		scheduler.scheduleAtFixedRate(worker, 0, 15, MINUTES);
		}catch(Exception e) {
			//not sure what will go wrong :)
			//But don ya worry, the Executor will run again
			e.printStackTrace();
			//send SMS to the admin - TBD
		}
	}

}
