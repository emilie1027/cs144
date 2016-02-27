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
    	int numResultsToSkip = Integer.valueOf(request.getParameter("numResultsToSkip"));
    	int numResultsToReturn = Integer.valueOf(request.getParameter("numResultsToReturn"));
    	
    	if (numResultsToSkip < 0)
    		numResultsToSkip = 0;
    	if (numResultsToReturn <= 0)
    		numResultsToReturn = 10;
    	
    	SearchResult[] result = auctionSearchClient.basicSearch(request.getParameter("q"), numResultsToSkip, numResultsToReturn);
    	
    	request.setAttribute("result", result);
    	request.setAttribute("q", request.getParameter("q").replace(" ", "%20"));
    	request.setAttribute("numResults", (int)result.length);
    	request.setAttribute("numResultsToSkip", numResultsToSkip);
    	request.setAttribute("numResultsToReturn", numResultsToReturn);
    	
    	request.getRequestDispatcher("/searchResult.jsp").forward(request, response);
    }
}
