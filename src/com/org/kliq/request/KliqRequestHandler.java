/**
 * 
 */
package com.org.kliq.request;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.org.kliq.datastore.KliqDataStore;
import com.org.kliq.utils.UtilFunctions;

/**
 * @author Anand
 * This is the main entry point for 
 * the Kliq Application
 *
 */
public class KliqRequestHandler extends RequestHandler{

	/**
	 * pointers for completeMessage 
	 * 0 = Main Request 
	 * 1 = Sub Request
	 * 2 = User Parameters
	 * 3 = User Parameters
	 */
	
	public String[] completeMessage;
	protected Map params;
	
	@Override
	public void init(HttpServletRequest request) {
		super.init(request);
		completeMessage = RequestHandler.getCompleteMessage(request);
	}
	
	@Override
	public void doProcess() {
		//This method wil instantiate the correct sub request handler
		//Based on the user SubRequest
		String subRequest = UtilFunctions.makeRequestSane(completeMessage[0]);
		SubRequestHandler handler = null;
		try {
			handler = (SubRequestHandler) Class.forName(Constants.Request+subRequest+Constants.handler).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(handler==null){
			handler=new SubRequestHandler(){

				@Override
				public void perform() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public String generateResponse() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void init(KliqRequestHandler handler) {
					// TODO Auto-generated method stub
					
				}};
		}
		
		//init method copies the superclass object values to the new instance od sub class
		//Yeah this sucks :(
		handler.init(this);
		try{
		//Except for the initial signup, users must be registered to proceed
		if(subRequest.equalsIgnoreCase("signup"))	{
			handler.perform();
		}
		else {
			//Check if the user is registered
			if(KliqDataStore.isUserExists(this.mobileNumber, null)) {
				handler.perform();
			}
			else{
				setResponseText(Constants.unregistered);
				return;
			}
		}
		setResponseText(handler.generateResponse());
		}catch(Exception e){
			e.printStackTrace();
			setResponseText(Constants.unavailable);
		}
	}
}
