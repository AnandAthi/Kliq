package com.org.kliq.request;

public interface Constants {
	
	public static final String codingUsed="UTF-8";
	public static final String userMessageParam="txtweb-message";
	public static final String userMobileParam="txtweb-mobile";
	public static final String messageTokenizer=" ";
	public static final String appPubKey = "txtweb-pubkey";
	
	public static final String goundamaniService="goundertalk";
	
	public static final String chennai="Chennai";
	public static final String bengaluru="Bengaluru";
	
	public static final String traffic="Traffic";
	public static final String whether="Whether";

	public static final String locationDefaultText="Please send the kind of update you need eg. traffic, whether.";
	public static final String applicationDefaultText="Thank you for your patronage, to use our service SMS @QUIP GounderTalk to 9243342000";
	
	//These constants are for the Traffic Updates from Twitter
	public static final String trafficUpdatesUser = "@cctpolice";
	public static final String trafficContent = "CTP:";
	//End Twitter Constants
	
	//These are used for Parsing the XML from txtWeb Location API
	public static final String userLocationStart = "<userlocationtext>";
	public static final String userLocationend = "</userlocationtext>";
	public static final String cityStart = "<city>";
	public static final String cityEnd = "</city>";
	
	//Used for the Weather API - wunderground.com
	public static final String WeatherAPIKey = "3573faa0cedccc35";
	public static final String country = "India";
	public static final String format = ".xml";
	public static final String temperatureStart = "<temperature_string>";
	public static final String temperatureEnd = "</temperature_string>";
	public static final String weatherStart = "<weather>";
	public static final String weatherEnd = "</weather>";
	
	//Request Instantiation
	public static final String Request = "com.org.kliq.request.";
	public static final String handler = "Handler";
	
	//User Messages
	public static final String unavailable = "Ah Snap ! Somethin's wrong. Please try again later";
	public static final String unregistered = "You need to be registered to use our services. SMS @kliq signup &lt;urmobilenumber&gt; to register";
	
	//Group Members related
	public static final int membercap = 10;
	
	//Cron
	public static final long period = 15 * 60 * 1000;
	
	//Group chat related
	public static final String serviceKeyword = "@";
	public static final String htmlLineBreak = "<br/>";
	
	//PUSH api 
	public static final String pushAPI = "http://api.txtweb.com/v1/push";
	public static final String tempPubkey = "b5eb4828-9776-40d6-a360-e607589f4959";
	public static final String tempmessageHeader = "<html><head><meta name='txtweb-appkey' content='4a7fc205-b10a-4175-8fb0-f32734f92e97'/></head><body>";
	public static final String tail = "</body></html>";
	public static final String success = "success";
	public static final String separator = ",";
	public static final String user = "User ";
	public static final String inviteMessage = "  has invited you to be part of the group -   ";
	public static final String unsubMessage = " To unsubscribe from the group sms @kliq unsub groupname";
}
