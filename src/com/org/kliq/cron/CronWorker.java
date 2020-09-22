/**
 * 
 */
package com.org.kliq.cron;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.org.kliq.request.Constants;
import com.org.kliq.request.service.MessageService;

/**
 * @author Anand
 * This class sends the Group Invite notifications
 * when run.
 *
 */
public class CronWorker implements Runnable {

	//This List holds the notifications to be sent. Use COWAL to 
	//mitigate concurrency problems.
	private static final List<String> inviteList = new CopyOnWriteArrayList<String>();
	
	//Add more notifications to the list
	public static void addtoCronQueue(List<String> items) {
		if(items != null) {
			inviteList.addAll(items);
		}
	}

	
	@Override
	public void run() {
		String [] inviteArray = null; 
		String message = "";
		String userHash = "";
		String response = "";
		for(String invite : inviteList) {
			inviteArray = invite.split(Constants.separator);
			message = Constants.user + " " + inviteArray[0] + " " + Constants.inviteMessage + inviteArray[1];
			userHash = inviteArray[2];
			response = MessageService.pushtoMobile(userHash,message);
			//Invite pushed successfully
			if(MessageService.checkforsuccess(response)){
				//remove this invite from queue 
				inviteList.remove(invite);
			}
			//we need the array back
			inviteArray = null;
		}

	}

}
