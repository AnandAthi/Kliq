/**
 * 
 */
package com.org.kliq.request;

import java.util.Calendar;
import java.util.HashMap;

import com.org.kliq.datastore.KliqDataStore;
import com.org.kliq.request.beans.Group;
import com.org.kliq.request.beans.UserGroup;
import com.org.kliq.utils.UtilFunctions;

/**
 * @author Anand
 *
 */
public class CreateHandler extends KliqRequestHandler implements SubRequestHandler{
	
	boolean groupCreationStatus = false;
	boolean addOwnertoGroupStatus = false;
	
	
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	public void perform() {
		
		processUserRequest(this.completeMessage);
		//Now that we have the userName, check if the user is valid and Some one else has this groupName
		if(isUserExists() && ! isGroupNameExists((String)params.get("GroupName"))) {
			//Good to go 
			groupCreationStatus = createGroup();
			//Now that group has been created, automatically enroll the owner as the 
			//group member
			if(groupCreationStatus) {
			//UserGroup Mapping goes here.
				addOwnertoGroupStatus = addOwnertoGroup();
			}
		}
		
		//Dammit, tough luck buddy;try a different name
	}

	private boolean isUserExists() {
		//mobile number here is actually the mobileHash
		return KliqDataStore.isUserExists(this.mobileNumber, null);
	}

	private boolean addOwnertoGroup() {
		UserGroup ug = new UserGroup();
		String userName = KliqDataStore.getUserNameforMobileHash(this.mobileNumber);
		ug.setGroupName((String)this.params.get("GroupName"));
		ug.setUserName(userName);
		ug.setSubscribed(true);
		ug.setResponseDate(Calendar.getInstance().getTime());
		return KliqDataStore.createUserGroup(ug);
	}

	private boolean createGroup() {
		Group group = new Group();
		String groupName = (String)params.get("GroupName");
		group.setGroupName(groupName);
		group.setGroupOwner(mobileNumber);
		group.setMemberCount(1);
		return KliqDataStore.createGroup(group);
	}

	private boolean isGroupNameExists(String groupName) {
		return KliqDataStore.isGroupAlreadyExists(groupName);
	}

	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		if(completeMessage != null) {
			//We need the User's mobile Hash and the Group Name
			params.put("GroupName", UtilFunctions.decode(completeMessage[1]));
			params.put("MobileHash", this.mobileNumber);
		}
		
	}

	@Override
	public String generateResponse() {
		String response = Constants.unavailable;
		
		
		if(groupCreationStatus) {
			response = this.params.get("GroupName") + " successfully created. Invite your buddies ! Each group can have maximum of 10 members including you.";
			if(addOwnertoGroupStatus) {
				response =  response + " You are the owner and also a member of your group - "+ this.params.get("GroupName");
				response = response + "To invite your friends sms @kliq add yourfriend'smobile number.If more than one, separate each friend's number with spaces";
			}
		}
		else {
			response = "Oops. Someone already has that groupname.Please try a different one. Thanks !";
		}
		return response;
	}

}
