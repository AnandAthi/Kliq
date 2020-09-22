package com.org.kliq.request;

import java.util.ArrayList;
import java.util.List;

import com.org.kliq.datastore.KliqDataStore;
import com.org.kliq.request.beans.UserGroup;

public class MygroupsHandler extends KliqRequestHandler implements
		SubRequestHandler {

	List<String> groups =  new ArrayList<String>();
	
	@Override
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}

	@Override
	public void perform() {
		String userName = KliqDataStore.getUserNameforMobileHash(mobileNumber);
		this.processGroups(KliqDataStore.getSubscribedGroupsList(userName));

	}

	private void processGroups(List<UserGroup> subscribedGroupsList) {
		for(UserGroup ug : subscribedGroupsList) {
			groups.add(ug.getGroupName());
		}
	}

	@Override
	public String generateResponse() {
		StringBuffer buff = new StringBuffer();
		if(this.groups.size() > 0) {
			buff.append("You are part of the following groups :").append(Constants.htmlLineBreak);
			for(String groupname : this.groups) {
				buff.append(groupname).append(Constants.htmlLineBreak);
			}
		}
		else {
			buff.append("You are not part of any groups. To create a new group, reply @kliq create groupname");
		}
		return buff.toString();
	}

}
