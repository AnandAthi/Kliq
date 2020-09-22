/**
 * 
 */
package com.org.kliq.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.org.kliq.cron.CronWorker;
import com.org.kliq.datastore.KliqDataStore;
import com.org.kliq.request.beans.Prospect;
import com.org.kliq.request.beans.UserGroup;

/**
 * @author Anand
 *
 */
public class AddHandler extends KliqRequestHandler implements SubRequestHandler {

	/* (non-Javadoc)
	 * @see com.org.quip.subrequest.SubRequestHandler#perform()
	 */
	boolean addUserstoGroup = false;
	boolean isGroupReachedMax = false;
	//This list will hold the registered invitees
	List<String> registeredUsers = new ArrayList<String>();
	//This list will hold the unregistered invitees, considered as prospects
	List<String> unregisteredUsers = new ArrayList<String>();
	//Have a reference to the Inviter
	String inviter;
	
	
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	@Override
	public void perform() {
		processUserRequest(this.completeMessage);
		//Check if the group exists first and 
		//Only group Owner can add members to the group
		if(isGroupExists() && isUserOwner()) {
			//Verify if the group has less than 10 members
			if( ! hasGroupMax()) {
				//Good to go till now but are the invitees registered Users?
				checkForMembership();
				//Invite the registered Users
				handleInvitations();
				//Consider the unregistered as Prospects
				handleProspects();
				addUserstoGroup = true;
			}
			else {
				isGroupReachedMax = true;
			}
		}
		

	}

	private boolean hasGroupMax() {
		int memberCount = KliqDataStore.getNumberofGroupMembers((String) this.params.get("groupName"));
		return (memberCount < Constants.membercap) ? false : true;
	}

	private void handleProspects() {
		//Iterate the prospects data and persist it
		Prospect p = null;
		for(String unregUser : this.unregisteredUsers) {
			p = new Prospect();
			p.setInvitee(unregUser);
			//mobileNumber is actually the mobileHash,so get the userName of the group owner
			p.setInviter(KliqDataStore.getUserNameforMobileHash(this.mobileNumber));
			p.setGroupName((String)this.params.get("groupName"));
			//create prospect
			KliqDataStore.createProspect(p);
			this.inviter = p.getInviter();
			
		}
		
	}

	private void handleInvitations() {
		List<String> mobileHashList = new ArrayList<String>();
		String mobileHash = null;
		//Iterate the registered members list and send invite
		for(String regUser : this.registeredUsers) {
			addUsertoProvisionalGroup(regUser);
			mobileHash = KliqDataStore.getMobileHashforUser(regUser);
			if(mobileHash != null) {
				//We are adding this to a list instead of a Map, because the invitee may be invited for more
				//than one group. So you cannot have the mobileHash as they key for the map 
				mobileHashList.add(this.inviter+ "," + (String)this.params.get("groupName")+ ","+mobileHash);
			}
			//Add the list to the cronQueue which takes care of sending 
			//Notifications asynchronously
			CronWorker.addtoCronQueue(mobileHashList);
		}
		
	}

	
	//Adds the user to the group, but its only provisional
	//The user becomes member of the group only when he accepts the invitation
	//This provisional restriction doesn't apply to owner, he becomes member as soon
	//as he created the group
	private void addUsertoProvisionalGroup(String regUser) {
	
		UserGroup ug = new UserGroup();
		ug.setGroupName((String)this.params.get("groupName"));
		ug.setUserName(regUser);
		ug.setSubscribed(false);
		KliqDataStore.createUserGroup(ug);
	}

	private void checkForMembership() {
		//This method will separate out the registered and 
		//Non registered users from the invite
		List<String> invitees = (List<String>) this.params.get("groupMembers");
		for(String invitee : invitees) {
			if(isUserExists(invitee)){
				registeredUsers.add(invitee);
			}
			else{
				unregisteredUsers.add(invitee);
			}
		}
		
	}

	private boolean isUserOwner() {
		return KliqDataStore.isUserGroupOwner(mobileNumber, (String)params.get("groupName"));
	}

	private void processUserRequest(String[] completeMessage) {
		this.params = new HashMap();
		List<String> groupMembers = new ArrayList();
		int length = completeMessage.length;
		if(completeMessage != null) {
			params.put("groupName", completeMessage[1]);
			for(int i = 2 ; i < length ; i++){
				groupMembers.add(completeMessage[i]);
			}
			params.put("groupMembers", groupMembers);
		}
	}
	
	private boolean isGroupExists(){
		return KliqDataStore.isGroupAlreadyExists((String)params.get("groupName"));
	}
	
	private boolean isUserExists(String userName){
		return KliqDataStore.isUserExists(null, userName);
	}

	@Override
	public String generateResponse() {
		String response = "";
		if(addUserstoGroup) {
			StringBuffer buff = new StringBuffer("Here's what we have for you -");
			if(this.registeredUsers.size() > 0) {
				buff.append("The following are registered users and have been invited to your group "+Constants.htmlLineBreak);
				for(String user : this.registeredUsers) {
					buff.append(user + Constants.htmlLineBreak);
				}
			}	
			 if(this.unregisteredUsers.size() > 0) {
				 buff.append(Constants.htmlLineBreak + "The following haven't registered yet. Please ask them to register first. Your invite will be sent then");
				 for(String user : this.unregisteredUsers) {
					 buff.append(Constants.htmlLineBreak + user);
				 }
			  }
			response = buff.toString();
		}
		else if(isGroupReachedMax) {
			response = "Your group already has 10 members. You cannot add more.Delete any member if you wish";
		}
		else {
			response = "Either the group does not exists or you are not the owner of it";
		}
		return response;
	}

}
