package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
	private AuctionSearchClient auctionSearchClient;
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	SearchResult[] result = auctionSearchClient.basicSearch(request.getParameter("q"), Integer.valueOf(request.getParameter("numResultsToSkip")), Integer.valueOf(request.getParameter("numResultsToReturn")));
    	
    	request.setAttribute("result", result);
    	
    	request.getRequestDispatcher("/searchResult.jsp").forward(request, response);
    }
}
