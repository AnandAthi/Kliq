package com.org.kliq.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.org.kliq.request.Constants;

public class UtilFunctions {

	public static int random(int bound){
		int randNo=0;
		Double doubleStr=Math.random();
		String randomStr=new String(doubleStr.toString());
		for(int i=0;i<randomStr.length();i++){
			char c=randomStr.charAt(i);
			if(c!='.'){
				randNo+=c;
			}
		}
		randNo%=bound;
		return randNo;
	}
	
	/**
	 * @param String
	 * 
	 * This method ensures the User request
	 * matches the naming conventions of the
	 * Request Handlers
	 * @return String 
	 */
	public static String makeRequestSane(String value){
		StringBuffer buff = new StringBuffer(value.toLowerCase());
		String firstChar = buff.substring(0, 1);
		buff.replace(0, 1, firstChar.toUpperCase());
		return buff.toString();
	}
	
	public static String decode(String value){
		try {
			if(value != null)
			return URLDecoder.decode(value, Constants.codingUsed);
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getSMSLocation(String locationXML) {
		
		return XMLParser(locationXML, Constants.userLocationStart, Constants.userLocationend);
	}

	public static String getCity(String locationXML) {
		return XMLParser(locationXML, Constants.cityStart, Constants.cityEnd);
	}
	
	public static String getTemperature(String weatherXML) {
		
		return XMLParser(weatherXML,Constants.temperatureStart,Constants.temperatureEnd);
	}
	
	public static String getConditions(String weatherXML) {
		
		return XMLParser(weatherXML,Constants.weatherStart,Constants.weatherEnd);
	}
	
	public static String XMLParser(String XML,String startElement,String endElement) {
		
		String elementValue = "error";
		if(XML.contains(startElement) && XML.contains(endElement))  {
			//Get the Start and end occurence of the element
			int startIndex = XML.indexOf(startElement);
			int endIndex   = XML.indexOf(endElement);
			//The required String lies between them
			elementValue = XML.substring(startIndex, endIndex);
		}
		return elementValue;
	}

	
}
