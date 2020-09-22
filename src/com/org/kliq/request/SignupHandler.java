/**
 * 
 */
package com.org.kliq.request;

import java.util.HashMap;

import com.org.kliq.datastore.KliqDataStore;
import com.org.kliq.request.beans.User;
import com.org.kliq.utils.UtilFunctions;

/**
 * @author Anand
 * This class handles the Sign Up Process
 *
 */
public class SignupHandler extends KliqRequestHandler implements
		SubRequestHandler {

	boolean registrationStatus = false;
	
	/* (non-Javadoc)
	 * @see com.org.quip.subrequest.SubRequestHandler#perform()
	 */
	
	
	public void init(KliqRequestHandler handler) {
		this.completeMessage = handler.completeMessage;
		this.mobileNumber = handler.mobileNumber;
		this.userMessage = handler.userMessage;
	}
	
	@Override
	public void perform() {
		
		processUserRequest(this.completeMessage);
		//Now that we have the userName, check if this guy is already signed up 
		if( ! isRegistered(this.mobileNumber)) {
			registrationStatus = handleSignup();
		}
		else {
			//Smart Ass trying to register again, don't allow
			
		}
	}

	private boolean handleSignup() {
		User user = new User();
		user.setUserName((String)params.get("UserName"));
		user.setMobileHash((String)params.get("MobileHash"));
		return KliqDataStore.signUp(user);
	}


	private boolean isRegistered(String mobileHash) {
		return KliqDataStore.isUserExists(mobileHash,null);
	}

	private void processUserRequest(String[] completeMessage) {
		 
		   this.params = new HashMap();
			if(completeMessage != null) {
				//We need the User's mobile number which is also the user name
				params.put("UserName", UtilFunctions.decode(completeMessage[1]));
				params.put("MobileHash", this.mobileNumber);
			}
			
	}

	@Override
	public String generateResponse() {
		String response = Constants.unavailable;
		
		if(registrationStatus) {
			StringBuffer buff = new StringBuffer((String)this.params.get("UserName"));
			buff.append(" successfully created. Create a new group or check if you have any invites from your friends!");
			buff.append("To create a new group SMS @kliq create \"groupname\" ");
			buff.append(" To check for invites, SMS @kliq check");
			response = buff.toString();
		}
		else {
			response = "Oops. Looks like you are already registered. We are sorry, only one user per mobile";
		}
		return response;
	}

}
