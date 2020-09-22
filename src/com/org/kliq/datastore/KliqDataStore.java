/**
 * 
 */
package com.org.kliq.datastore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.org.kliq.request.Constants;
import com.org.kliq.request.beans.Group;
import com.org.kliq.request.beans.Prospect;
import com.org.kliq.request.beans.User;
import com.org.kliq.request.beans.UserGroup;

/**
 * @author Anand
 * 
 */
public class KliqDataStore {
	
	static {
		ObjectifyService.register(User.class);
		ObjectifyService.register(Group.class);
		ObjectifyService.register(UserGroup.class);
		ObjectifyService.register(Prospect.class);
		
	}
	
	public static boolean signUp(User user){
		boolean status = false;
		Objectify ofy = ObjectifyService.begin();
		try{
			ofy.put(user);
			status = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		//If he was a prospect and registered now, delete his prospect records
		//But what will happen to his other invites !? - Hold on !
		return status;
	}
	
	public static boolean isUserExists(String mobileHash,String userName){
		boolean status = false;
		User u = null;
		Objectify ofy = ObjectifyService.begin();
		if(mobileHash != null) {
			 u = ofy.find(User.class, mobileHash);
			 if(u != null) {
				//User already exists
				status = true;
			}
		}
		if(userName != null) {
			u = ofy.query(User.class).filter("userName", userName).get();
			if(u != null) {
				status = true;
			}
		}
		
		return status;
	}
	
	public static boolean createGroup(Group group){
		boolean status = false;
		Objectify ofy = ObjectifyService.begin();
		try{
			ofy.put(group);
			status = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return status;
	}

	public static boolean isGroupAlreadyExists(String groupName){
		boolean status = false;
		Objectify ofy = ObjectifyService.begin();
		Group g = ofy.query(Group.class).filter("groupName", groupName).get();
		if(g != null) {
			//Group already exists 
			status = true;
		}
		return status;
	}
	
	public static boolean isUserGroupOwner(String mobileHash,String groupName){
		boolean status = false;
		Objectify ofy = ObjectifyService.begin();
		Group g = ofy.query(Group.class).filter("groupName", groupName).get();
		if(g != null){
			status = mobileHash.equals(g.getGroupOwner());
		}
		return status;
	}
	
	public static String getMobileHashforUser(String userName) {
		String mobileHash = null;
		User u = getUser(userName,false);
		if(u != null) {
			mobileHash = u.getMobileHash();
		}
		return mobileHash;
	}
	
	public static String getUserNameforMobileHash(String mobileHash) {
		
		String userName = null;
		User u = getUser(mobileHash,true);
		if(u != null) {
			userName = u.getUserName();
		}
		return userName;
	}
	
	
	/**
	 * This method returns the user object
	 * for the given identifier.
	 * Identifier can be MobileHash or UserName
	 * if identifier is MobileHash, isKey should be passed true
	 * if identifier is User Name , isKey should be passed false 
	 * @param identifier
	 * @param isKey
	 * @return
	 */
	public static User getUser(String identifier,boolean isKey) {
		User u = null;
		Objectify ofy = ObjectifyService.begin();
		if(isKey) {
			 u = ofy.find(User.class, identifier);
		}
		else {
			 u = ofy.query(User.class).filter("userName", identifier).get();
		}
		return u; 
	}

	public static boolean createUserGroup(UserGroup ug) {
		boolean status = false;
		
		Objectify ofy = ObjectifyService.begin();
		try{
			ofy.put(ug);
			status = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return status;

	}
	
	public static boolean createProspect(Prospect p) {
		boolean status = false;
		Objectify ofy = ObjectifyService.begin();
		try {
			ofy.put(p);
			status = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	public static int getNumberofGroupMembers(String groupName) {
		//This is the maximum number of members allowed per group;
		int number = Constants.membercap;
		Group g = null;
		Objectify ofy = ObjectifyService.begin();
		g = ofy.query(Group.class).filter("groupName", groupName).get();
		if(g != null) {
			number = g.getMemberCount();
		}
		return number;
	}
	
	//This method checks if the user is invited for the group 
	//for which he wishes to be part of
	//Can also be used to check if he is part of the group he posts messages to
	public static boolean isUserInvited(String userName,String groupName) {
		boolean status = false;
		UserGroup ug = null;
		Objectify ofy = ObjectifyService.begin();
		ug = ofy.query(UserGroup.class).filter("groupName", groupName).filter("userName", userName).get();
		if(ug != null) {
			status = true;
		}
		return status;
	}
	
	public static void addUsertoGroup(String userName,String groupName){
		UserGroup ug = null;
		Date now = Calendar.getInstance().getTime();
		Objectify ofy = ObjectifyService.begin();
		ug = ofy.query(UserGroup.class).filter("groupName", groupName).filter("userName", userName).get();
		if(ug != null) {
			ug.setSubscribed(true);
			ug.setResponseDate(now);
			ofy.put(ug);
		}
		//Once added, increase the member count of the group
		KliqDataStore.changeMemberCount(groupName, true);
	}
	
	//This method gives the list of invites for the user.
	//It also gives the inviter
	public static Map<String,String> getAllinvitesforUser(String userName) {
		Map<String,String> invitemap = new HashMap<String, String>();
		String groupOwner;
		Objectify ofy = ObjectifyService.begin();
		Query<Prospect> q = ofy.query(Prospect.class).filter("invitee", userName);
		for(Prospect invite : q) {
			invitemap.put(invite.getGroupName(), invite.getInviter());
		}
		//If the user is not a proepect but a registered, check the user group entries
		Query<UserGroup> ug = ofy.query(UserGroup.class).filter("userName", userName).filter("responseDate", null).filter("isSubscribed", false);
		for(UserGroup invite : ug) {
			groupOwner = getUserNameforMobileHash(KliqDataStore.getGroupOwner(invite.getGroupName()));
			invitemap.put(invite.getGroupName() , groupOwner);
		}
		return invitemap;
	}
	
	public static void ignoreInvite(String userName, String groupName) {
		UserGroup ug = null;
		Date now = Calendar.getInstance().getTime();
		Objectify ofy = ObjectifyService.begin();
		ug = ofy.query(UserGroup.class).filter("groupName", groupName).filter("userName", userName).get();
		if(ug != null) {
			ug.setSubscribed(false);
			ug.setResponseDate(now);
			ofy.put(ug);
		}
		KliqDataStore.changeMemberCount(groupName, false);
	}
	
	public static void changeMemberCount(String groupName, boolean addMember) {
		Group g = null;
		int count = 0;
		Objectify ofy = ObjectifyService.begin();
		g = ofy.query(Group.class).filter("groupName", groupName).get();
		count = g.getMemberCount();
		if(addMember) {
			count++;
		}
		else {
			count--;
		}
		//Update the member count and persist
		g.setMemberCount(count);
		ofy.put(g);
	}
	
	public static List<String> getGroupMembers(String groupName) {
		List<String> groupMembers = new ArrayList<String>();
		Objectify ofy = ObjectifyService.begin();
		Query<UserGroup> ugList = ofy.query(UserGroup.class).filter("groupName", groupName).filter("isSubscribed", true);
		for(UserGroup ug : ugList) {
			groupMembers.add(ug.getUserName());
		}
		return groupMembers;
	}
	
	public static String getGroupOwner(String groupName) {
		String owner = "";
		Group g = null;
		Objectify ofy = ObjectifyService.begin();
		g = ofy.find(Group.class,groupName);
		if(g != null) {
			owner = g.getGroupOwner();
		}
		return owner;
	}

	public static void unSubscribeUserfromGroup(String userName, String groupName) {
		UserGroup ug = null;
		Objectify ofy = ObjectifyService.begin();
		ug = ofy.query(UserGroup.class).filter("groupName", groupName).filter("userName", userName).get();
		if(ug != null) {
			ofy.delete(ug);
		}
		KliqDataStore.changeMemberCount(groupName, false);
	}

	public static List<UserGroup> getSubscribedGroupsList(String userName) {
		List<UserGroup> groupList = new ArrayList<UserGroup>();
		Objectify ofy = ObjectifyService.begin();
		Query<UserGroup> q = ofy.query(UserGroup.class).filter("userName",userName).filter("isSubscribed", true);
			for(UserGroup ug : q) {
				groupList.add(ug);
			}
		return  groupList;
	}

}
