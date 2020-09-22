/**
 * 
 */
package com.org.kliq.request;


/**
 * @author Anand
 *
 */
public interface SubRequestHandler {
	
	public void init(KliqRequestHandler handler);
	
	public void perform();

	public String generateResponse();

}
