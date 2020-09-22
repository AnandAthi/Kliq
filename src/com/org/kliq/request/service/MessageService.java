/**
 * 
 */
package com.org.kliq.request.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.org.kliq.request.Constants;
import com.org.kliq.utils.HttpUtils;

/**
 * @author Anand
 * This service can be used to PUSH messages
 * to users
 *
 */
public class MessageService {
	
	public static List<String> posttoGroup(List<String> group,String message) throws Exception{
		List<String> failedNotifications = new ArrayList<String>();
		String notification = "";
		//Iterate the list and push Message one at a time
		for(String user : group) {
			notification = pushtoMobile(user,message);
			//Add the mobile hash for which msg couldn't be sent
			if(! checkforsuccess(notification)) {
				failedNotifications.add(user);
			}
			
		}
		return failedNotifications;
		
	}

	public static boolean checkforsuccess(String notification) {
		return notification.contains(Constants.success) ? true : false;
	}

	public static String pushtoMobile(String user, String message) {
		
		String response = null;
		//construct the url
		StringBuffer url = new StringBuffer(Constants.pushAPI);
		String encodedMessage = encodeMessageforPushAPI(message);
		url.append("?").append(Constants.userMobileParam).append("=").append(user).append("&");
		url.append(Constants.appPubKey).append("=").append(Constants.tempPubkey).append("&");
		url.append(Constants.userMessageParam).append("=").append(encodedMessage);
		try {
				response = HttpUtils.pushMessage(url.toString(), 1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private static String encodeMessageforPushAPI(String message) {
		String encodedMessage = "";
		StringBuffer buff = new StringBuffer(Constants.tempmessageHeader);
		buff.append(message).append(Constants.tail);
		try {
			encodedMessage = URLEncoder.encode(buff.toString(), Constants.codingUsed);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodedMessage;
	}

}
