package com.org.kliq;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.org.kliq.request.RequestHandler;

@SuppressWarnings("serial")
public class KliqServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(" Is this the requested query param  " + request.getQueryString());
		StringBuffer buffer = new StringBuffer();
		try{
		RequestHandler handler=RequestHandler.getRequestHandler(request);
		handler.doProcess();
		buffer.append("<html>")
		.append("<head>")
		.append("<title>QUIP</title>")
		.append("<meta http-equiv='Content-Type' content='text/html'; charset='UTF-8'/>")
		.append("<meta name='txtweb-appkey' content='3a7c32e9-6d16-41bf-8c03-387cd983c46f'/>")
		.append("</head>")
		.append("<body>")
		.append(handler.getResponseText())
		.append("</body>")
		.append("</html>");
		}catch(Exception e) {
			buffer.append("<html>").append("<head>");
			buffer.append("<meta name='txtweb-appkey' content='3a7c32e9-6d16-41bf-8c03-387cd983c46f'/>");
			buffer.append("</head>").append("</html>");
		}
		response.getWriter().print(buffer);
	}

}
