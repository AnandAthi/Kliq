/**
 * 
 */
package com.org.kliq.request;

import java.util.HashMap;
import java.util.Map;

import com.org.kliq.datastore.KliqDataStore;

/**
 * @author Anand
 * This class checks the user's invitations
 *
 */
public class CheckHandler extends KliqRequestHandler implements
		SubRequestHandler {

	/* (non-Javadoc)
	 * @see com.org.quip.subrequest.SubRequestHandler#perform()
	 */
	String userName;
	Map<String,String> inviteMap;
	@Override
	
	
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	public void perform() {
		//We don't need any of the parameters for this functionality
		//processUserRequest(this.completeMessage);
		
		if(isRegisteredUser()) {
			userName = KliqDataStore.getUserNameforMobileHash(mobileNumber);
			//get all the groups for which he has been invited for and by whom
			inviteMap = getAllInvites();
		}
	}

	private Map<String,String> getAllInvites(){
		// TODO Auto-generated method stub
		return KliqDataStore.getAllinvitesforUser(userName);
	}

	private boolean isRegisteredUser() {
		return KliqDataStore.isUserExists(mobileNumber, null);
	}

	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		
	}

	@Override
	public String generateResponse() {
		StringBuffer buff = null;
		if(inviteMap.size() > 0) {
			buff = new StringBuffer("Sup ? You are wanted in "+Constants.htmlLineBreak);
			for(Map.Entry<String , String> items : inviteMap.entrySet()) {
				buff.append(items.getKey() + " by " + items.getValue()+ Constants.htmlLineBreak);
			}
		}
		else {
			buff = new StringBuffer("Looks like you don't have any invites.");
			buff.append("Why don ya create your own group and invite your buds ?");
			buff.append("To create a new group sms @kliq create &lt;yourgroupname&gt;");
		}
		return buff.toString();
	}

}
