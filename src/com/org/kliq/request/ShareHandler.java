/**
 * 
 */
package com.org.kliq.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.org.kliq.datastore.KliqDataStore;
import com.org.kliq.request.service.MessageService;

/**
 * @author Anand
 * Post Messages to your group
 * Messages can be posted to only one group at a time
 *
 */
public class ShareHandler extends KliqRequestHandler implements SubRequestHandler {

	/* (non-Javadoc)
	 * @see com.org.quip.subrequest.SubRequestHandler#perform()
	 */
	
	String userName;
	String message;
	String groupName;
	List<String> groupMembersHash = new ArrayList<String>();
	List<String> failedNotifications;
	boolean messagePostStatus = false;
	
	
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	@Override
	public void perform() {
		processUserRequest(this.completeMessage);
		if(isValidUser() && isGroupMember()) {
			//valid user and belongs to the group
			//Analyze the message to see if the poster wants to use our service
			//Change message accordingly and append the poster's username
			//This has not been rolled out for now
			//this.message = analyzeUserMessage(this.message);
			
			this.message = buildMessage();
			//Post the message and get the status
			messagePostStatus = postMessagetoGroup();
		}

	}

	
	private String buildMessage() {
		StringBuffer buff = new StringBuffer(this.userName);
		buff.append("says - ").append(this.message).append(Constants.htmlLineBreak).append(" ----End of Message----");
		buff.append(Constants.htmlLineBreak).append(Constants.unsubMessage);
		return buff.toString();
		
	}

	private boolean postMessagetoGroup() {
		boolean status = false;
		//Get all the members of the group and their mobile Hash
		List<String> groupMembers = KliqDataStore.getGroupMembers(this.groupName);
		for(String member : groupMembers) {
			this.groupMembersHash.add(KliqDataStore.getMobileHashforUser(member));
		}
		//Now that we have all the hash of the members and the message, PUSH them
		try{
			this.failedNotifications = MessageService.posttoGroup(groupMembersHash, message);
			status = true;
		}catch(Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}


	private boolean isGroupMember() {
		this.userName = KliqDataStore.getUserNameforMobileHash(this.mobileNumber);
		return KliqDataStore.isUserInvited(this.userName, this.groupName);
	}

	private boolean isValidUser() {
		return KliqDataStore.isUserExists(this.mobileNumber, null);
	}

	private void processUserRequest(String[] completeMessage) {
		//In the completeMessage array, the parameters 2 is the groupName
		//Rest are the messages
		StringBuffer buff = new StringBuffer();
		this.params = new HashMap();
		int length = completeMessage.length;
		params.put("groupName", completeMessage[1]);
		this.groupName = completeMessage[1];
		for(int i=2 ; i < length ; i++) {
			buff.append(completeMessage[i]).append(" ");
		}
		params.put("message", buff.toString());
		this.message = buff.toString();
	}
	
	private String analyzeUserMessage(String message) {
		StringBuffer temp = new StringBuffer(message);
		//the user is requesting something of our service
		if(message.startsWith(Constants.serviceKeyword)) {
			
		}
		return message;
	}

	@Override
	public String generateResponse() {
		String response = "Your message shared with the group "+Constants.htmlLineBreak;
		response = response + "Please note : Recipients opted for DND will not recieve your message";
		if(this.failedNotifications.size() > 0) {
			StringBuffer buff = new StringBuffer();
			buff.append("But the message couldn't reach the following recipients");
			for(String recipient : this.failedNotifications) {
				buff.append(KliqDataStore.getUserNameforMobileHash(recipient)+ Constants.htmlLineBreak);
			}
			
			response = response + buff.toString();
		}
		return response;
	}

}
