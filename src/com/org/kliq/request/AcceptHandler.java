/**
 * 
 */
package com.org.kliq.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.org.kliq.datastore.KliqDataStore;

/**
 * @author Anand
 *
 */
public class AcceptHandler extends KliqRequestHandler implements
		SubRequestHandler {

	
	String userName;
	List<String> registeredGroups = new ArrayList<String>();
	List<String> unregisteredGroups = new ArrayList<String>();
	/* (non-Javadoc)
	 * @see com.org.quip.subrequest.SubRequestHandler#perform()
	 */
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	@Override
	public void perform() {
		this.processUserRequest(this.completeMessage);
		//Check if the user is registered 
		if(isRegisteredUser()) {
			//Check if the user is invited to be part of the group
			for(String group : (List<String>)this.params.get("groupsList")) {
				 if(isInvited(group)) {
					 //Good to go 
					 addUsertoGroup(group);
					 registeredGroups.add(group);
				 }
				 //This is needed to ensure the user doesn't try to 
				 //add himself to a group, he is not invited to
				 else {
					 unregisteredGroups.add(group);
				 }
			}
		}

	}
	@Override
	public String generateResponse() {
		String response = Constants.unavailable;
		StringBuffer buff = new StringBuffer();
		if(registeredGroups.size() > 0) {
				buff.append("Welcome to the Kliq "+Constants.htmlLineBreak);
			buff.append("You are now part of the following groups. You can share and will recieve messages."+Constants.htmlLineBreak);
			for(String group : registeredGroups) {
				buff.append(group + Constants.htmlLineBreak);
			}
		}
		if(unregisteredGroups.size() > 0) {
			buff.append("You have not been invited to the following groups and cannot be part of them unless invited by the group owner" +Constants.htmlLineBreak);
			for(String group : unregisteredGroups) {
				buff.append(group + Constants.htmlLineBreak);
			}
		}
		return (buff != null) ? buff.toString() : response;
	}
	
	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		List<String> listOfGroups = new ArrayList<String>();
		int length = completeMessage.length;
		for(int i = 1;i < length ; i++) {
			listOfGroups.add(completeMessage[i]);
		}
		params.put("groupsList", listOfGroups);
	}
	
	private void addUsertoGroup(String group) {
		KliqDataStore.addUsertoGroup(userName, group);
	}

	private boolean isInvited(String group) {
		 userName = KliqDataStore.getUserNameforMobileHash(this.mobileNumber);
		return KliqDataStore.isUserInvited(userName, group);
	}

	private boolean isRegisteredUser() {
		//Mobile number here is really the mobile hash
		return KliqDataStore.isUserExists(this.mobileNumber, null);
	}
}
