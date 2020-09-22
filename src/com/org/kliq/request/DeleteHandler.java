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
public class DeleteHandler extends KliqRequestHandler implements
		SubRequestHandler {

	
	boolean deleteStatus = false;
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
		//Check if the user is group owner, only owners can delete members
		if(isOwner()) {
			KliqDataStore.unSubscribeUserfromGroup((String)params.get("usertodelete"), (String)params.get("groupName"));
			deleteStatus = true;
		}

	}

	private boolean isOwner() {
		String groupName = (String)params.get("groupName");
		return KliqDataStore.isUserGroupOwner(mobileNumber, groupName);
	}

	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		params.put("usertodelete",completeMessage[1]);
		params.put("groupName",completeMessage[2]);
	}

	/* (non-Javadoc)
	 * @see com.org.kliq.request.SubRequestHandler#generateResponse()
	 */
	@Override
	public String generateResponse() {
		StringBuffer buff = new StringBuffer();
		if(this.deleteStatus) {
			buff.append("User "+ (String)params.get("usertodelete") + " removed from your group "+ (String)params.get("groupName"));
		}
		else {
			buff.append("Either the group you specified does not exist or you are not the owner of it");
		}
		return buff.toString();
	}

}
