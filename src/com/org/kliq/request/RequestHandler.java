package com.org.kliq.request;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.org.kliq.utils.UtilFunctions;

public abstract class RequestHandler {
	public String responseText;
	public String mobileNumber;
	public String userMessage;

	public static final String orgPackage = "com.org.quip.request.";
	public static final String requestHandler = "RequestHandler";
	
	protected void init(HttpServletRequest request){
		userMessage=getEntireMessage(request);
		mobileNumber=getMobileNumber(request);
		setResponseText(Constants.applicationDefaultText);
	}
	
	public void doProcess(){};
	
	public static RequestHandler getRequestHandler(HttpServletRequest request){
		
		// Make the user request Match Request Handler Convention
		//String requestedService=UtilFunctions.makeRequestSane(getRequestedService(request));
		
		RequestHandler handler= new KliqRequestHandler();
		// This make sure that init() method is even called for the RequestHandler class 
		// though an exception was thrown above.
		handler.init(request);
		return handler;
	}
	
	public static String getRequestedService(HttpServletRequest request){
		String entireMessage=getEntireMessage(request);
		String requestedService=null;
		if(entireMessage!=null){
			String message[]=entireMessage.split(Constants.messageTokenizer);
			if(message!=null){
				requestedService=message[0];
			}
		}
		return requestedService;
	}
	
	public static String[] getCompleteMessage(HttpServletRequest request){
		String message[] = getEntireMessage(request).split(Constants.messageTokenizer);
		return message;
		
	}
	
	public static String getEntireMessage(HttpServletRequest request){
		return UtilFunctions.decode(request.getParameter(Constants.userMessageParam));
	}
	
	public static String getMobileNumber(HttpServletRequest request){
		return request.getParameter(Constants.userMobileParam);
	}

	protected void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getResponseText(){
		return responseText;
	}
}