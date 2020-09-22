/**
 * 
 */
package com.org.kliq.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;


import com.org.kliq.request.Constants;

/**
 * @author Anand
 * This class has Utility methods
 * for Http requests and responses
 * Marked for refactoring
 *
 */
public class HttpUtils {
	
	public static final String userLocationURL = "http://api.txtweb.com/v1/location/get";
	//This URL gives weather conditions only. Visit http://www.wunderground.com/weather/api/d/docs?d=data/index for more details
	public static final String weatherAPI = "http://api.wunderground.com/api/"+Constants.WeatherAPIKey+"/conditions/q/";
	
	//public static final URL locationAPI = constructURL();
	
	
	private static URL constructURL() {
		
		URL userLocation = null;
		try {
			userLocation = new URL(userLocationURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userLocation;
	}

	public static String getLocationXML(String param) {
			//Spoiler Alert - GAE and Apache Http client don't mix well
			//Fall back to Vanilla URL Connection :(
		
			InputStream is = null;
			URLConnection conn = null;
			String queryParam = "";
			try {
				queryParam = Constants.userMobileParam+"="+URLEncoder.encode(param, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String responseText = "";
			try {
				 	conn = new URL(userLocationURL + "?" + queryParam).openConnection();
				 	conn.setConnectTimeout(10000);
				 	is = conn.getInputStream();
				 	responseText = IOUtils.toString(is, "UTF-8");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finally {
					try {
							if(is != null)
								is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			}
			
			return responseText;
		
		}

	//You can specify JSON or XML as response format. Defaulting to XML. JSON reserved for future 
	public static String getWeatherUpdate(String city) {
		
		InputStream is = null;
		URLConnection conn = null;
		String responseText = "";
		StringBuffer buff = new StringBuffer(weatherAPI);
		buff.append(Constants.country).append("/").append(city).append(Constants.format);
		try {
			conn = new URL(buff.toString()).openConnection();
			is = conn.getInputStream();
			conn.setConnectTimeout(10000);
			responseText = IOUtils.toString(is);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
				 try {
				  if(is != null)
					is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	 }
		return responseText;
	}
	
	public static String pushMessage(String urltoCall, int count) throws MalformedURLException, IOException {
		
		InputStream is = null;
		HttpURLConnection conn = null;
		String responseText = null;
		if(count <= 3) {	
			try{
				conn = (HttpURLConnection) new URL(urltoCall).openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.connect();
				conn.setConnectTimeout(10000);
				is = conn.getInputStream();
				responseText = IOUtils.toString(is);
			}
			finally {
				if(is != null)
					is.close();
				conn.disconnect();
			}
		}
		//Txt web api may have some network glitch, so try again couple more times
		if(serverError(responseText)){
			//try again 
			pushMessage(urltoCall, ++count);
		}
		return responseText;
	}
	private static boolean serverError(String response) {
		return response.contains("-1") ? true : false;
	}
}
