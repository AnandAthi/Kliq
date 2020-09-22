/**
 * 
 */
package com.org.kliq.request.beans;

import javax.persistence.Id;

/**
 * @author Anand
 * After much deliberations, we are sticking with the 
 * Google Low Level API for DataStore,However all
 * the calls are wrapped using the Objectify API.
 *
 */
public class User {
	
	private String userName;
	@Id private String mobileHash;
	//private String cronStatus;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileHash() {
		return mobileHash;
	}
	public void setMobileHash(String mobileHash) {
		this.mobileHash = mobileHash;
	}
	
	
	/*public String getCronStatus() {
		return cronStatus;
	}
	public void setCronStatus(String cronStatus) {
		this.cronStatus = cronStatus;
	}*/

}
