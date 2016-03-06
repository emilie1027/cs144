package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	String query = request.getParameter("q");
    	if (query == null)
    		query = "";
    	query = query.replace(" ", "%20");
    	
    	URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + query);
    	HttpURLConnection requester = (HttpURLConnection) url.openConnection();
    	
    	requester.setRequestMethod("GET");
    	
    	BufferedReader in = new BufferedReader(new InputStreamReader(requester.getInputStream()));
    	PrintWriter out = response.getWriter();
    	
    	String str;
    	while ((str = in.readLine()) != null) {
    		out.println(str);
    	}
    	
    	in.close();
    	out.close();
    }
}
