/**
 * 
 */
package com.org.kliq.request;

import java.util.HashMap;

import com.org.kliq.datastore.KliqDataStore;

/**
 * @author Anand
 *
 */
public class UnsubHandler extends KliqRequestHandler implements SubRequestHandler {

	
	boolean unSubscriptionStatus = false;
	String userName = "";
	/* (non-Javadoc)
	 * @see com.org.kliq.request.SubRequestHandler#init(com.org.kliq.request.KliqRequestHandler)
	 */
	@Override
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
		
	}

	/* (non-Javadoc)
	 * @see com.org.kliq.request.SubRequestHandler#perform()
	 */
	@Override
	public void perform() {
		processUserRequest(this.completeMessage);
		//Check if the user is part of the group first
		if(isUserSubscribed()) {
			KliqDataStore.unSubscribeUserfromGroup(this.userName,(String)params.get("groupName"));
			unSubscriptionStatus = true;
		}

	}

	private boolean isUserSubscribed() {
		return KliqDataStore.isUserInvited(this.userName, (String)params.get("groupName"));
	}

	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		params.put("groupName",this.completeMessage[1]);
		this.userName = KliqDataStore.getUserNameforMobileHash(mobileNumber);
		
	}

	/* (non-Javadoc)
	 * @see com.org.kliq.request.SubRequestHandler#generateResponse()
	 */
	@Override
	public String generateResponse() {
		String response = "";
		if(unSubscriptionStatus) {
			response = "You have been unsubscribed from "+(String) params.get("groupName");
		}
		else {
			response = "You need to be part of " +  (String) params.get("groupName") + " to unsubscribe";
		}
		return response;
	}

}
