/**
 * 
 */
package com.org.kliq.request;

import java.util.List;

import com.org.kliq.datastore.KliqDataStore;

/**
 * @author Anand
 *
 */
public class ViewHandler extends KliqRequestHandler implements
		SubRequestHandler {

	
	private List<String> groupMembers;
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
		String groupName = this.completeMessage[1];
		if(isUsergroupMember(groupName)) {
			groupMembers = KliqDataStore.getGroupMembers(groupName);
		}
		

	}

	private boolean isUsergroupMember(String groupName) {
		String userName = KliqDataStore.getUserNameforMobileHash(mobileNumber);
		return KliqDataStore.isUserInvited(userName, groupName);
	}
	
	/* (non-Javadoc)
	 * @see com.org.kliq.request.SubRequestHandler#generateResponse()
	 */
	@Override
	public String generateResponse() {
		StringBuffer buff = new StringBuffer();
		if(groupMembers != null && groupMembers.size() > 0) {
			buff.append(" Here are the members of your group").append(Constants.htmlLineBreak);
			for(String member : groupMembers) {
				buff.append(member).append(Constants.htmlLineBreak);
			}
		}
		else {
			buff.append("Either the group does not exits or you are not a member of the group");
		}
		return buff.toString();
	}

}
