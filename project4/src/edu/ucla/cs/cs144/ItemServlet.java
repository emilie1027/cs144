package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
	private AuctionSearchClient auctionSearchClient;
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	String result = auctionSearchClient.getXMLDataForItemId(request.getParameter("id"));
    	
    	PrintWriter out = response.getWriter();
    	out.println(result);
    	out.close();
    	
    	/*request.setAttribute("result", result);
    	
    	request.getRequestDispatcher("/itemResult.jsp").forward(request, response);*/
    }
}
