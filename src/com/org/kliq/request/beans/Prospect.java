/**
 * 
 */
package com.org.kliq.request.beans;

import javax.persistence.Id;

/**
 * @author Anand
 *
 */
public class Prospect {
  @Id Long id;
  String inviter;
  String invitee;
  String groupName;
  
  public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getInviter() {
	return inviter;
}
public void setInviter(String inviter) {
	this.inviter = inviter;
}
public String getInvitee() {
	return invitee;
}
public void setInvitee(String invitee) {
	this.invitee = invitee;
}
public String getGroupName() {
	return groupName;
}
public void setGroupName(String groupName) {
	this.groupName = groupName;
}

}
