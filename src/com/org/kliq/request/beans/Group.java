/**
 * 
 */
package com.org.kliq.request.beans;

import java.util.List;

import javax.persistence.Id;

/**
 * @author Anand
 * After much deliberations, we are sticking with the 
 * Google Low Level API for DataStore,However all
 * the calls are wrapped using the Objectify API.
 *
 */
public class Group {
	
	@Id private String groupName;
	private String groupOwner;
	private int memberCount;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupOwner() {
		return groupOwner;
	}
	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}
	
	public int getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
	
	
}
