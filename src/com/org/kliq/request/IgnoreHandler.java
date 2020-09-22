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
 * This class handles the ignore requests
 * Ignore does not delete the user's profile
 * It just changes the subscription status to false
 * with the Date, the ignore request was sent.
 *
 */
public class IgnoreHandler extends KliqRequestHandler implements
		SubRequestHandler {

	/* (non-Javadoc)
	 * @see com.org.quip.subrequest.SubRequestHandler#perform()
	 */
	String userName;
	List<String> ignoreGroupList = new ArrayList<String>();
	
	
	
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	
	@Override
	public void perform() {
		processUserRequest(this.completeMessage);
		//Is this a registered user ?
		if(isValidUser()) {
			userName = KliqDataStore.getMobileHashforUser(mobileNumber);
			//Was he invited first ?
			for(String group : (List<String>) this.params.get("groupList")) {
				if(isInvited(group)) {
					//good to go
					ignoreInvite(group);
				}
			}
		}
	}

	private void ignoreInvite(String group) {
		KliqDataStore.ignoreInvite(userName, group);
		
	}

	private boolean isInvited(String group) {
		return KliqDataStore.isUserInvited(userName, group);
	}

	private boolean isValidUser() {
		return KliqDataStore.isUserExists(mobileNumber, null);
	}

	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		//get all the groups user wishes to ignore
		int length = completeMessage.length;
		for(int i=1 ; i < length ; i++) {
			ignoreGroupList.add(completeMessage[i]);
		}
		params.put("groupList",ignoreGroupList);
		
	}

	@Override
	public String generateResponse() {
		StringBuffer buff = null;
		if(ignoreGroupList.size() > 0) {
			buff = new StringBuffer("You have ignored the following groups : ");
			for(String group : ignoreGroupList) {
				buff.append(group + Constants.htmlLineBreak);
			}
			buff.append("You cannot post to or will recieve messages from these groups.");
		}
		else {
			buff = new StringBuffer("You are not invited to be part of these groups.");
		}
		return buff.toString();
	}

}
