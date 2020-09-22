/**
 * 
 */
package com.org.kliq.request.beans;

import java.util.Date;

import javax.persistence.Id;

/**
 * @author Anand
 * This class models the 
 * Relationship between users and the 
 * groups they belong to.
 *
 */
public class UserGroup {
	@Id Long id;
	private String groupName;
	private String userName;
	private boolean isSubscribed;
	private Date responseDate;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isSubscribed() {
		return isSubscribed;
	}
	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}
	public Date getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	
	

}
