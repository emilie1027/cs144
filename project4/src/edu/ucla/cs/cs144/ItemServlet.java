package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class ItemServlet extends HttpServlet implements Servlet {
	private AuctionSearchClient auctionSearchClient;
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	String result = auctionSearchClient.getXMLDataForItemId(request.getParameter("id"));
    	
    	if (result.isEmpty()) {
    		PrintWriter out = response.getWriter();
    		out.println("Item Not Found");
        	out.close();
        	
    		return;
    	}
    	
    	try {
	    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    	Document doc = builder.parse(new InputSource(new StringReader(result)));
	    	
	    	request.setAttribute("ItemID", request.getParameter("id"));
	    	request.setAttribute("Name", doc.getElementsByTagName("Name").item(0).getTextContent());
	    	request.setAttribute("Currently", doc.getElementsByTagName("Currently").item(0).getTextContent());
	    	request.setAttribute("First_Bid", doc.getElementsByTagName("First_Bid").item(0).getTextContent());
	    	request.setAttribute("Number_of_Bids", doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent());
	    	request.setAttribute("Location", doc.getElementsByTagName("Location").item(0).getTextContent());
	    	request.setAttribute("Country", doc.getElementsByTagName("Country").item(0).getTextContent());
	    	request.setAttribute("Started", doc.getElementsByTagName("Started").item(0).getTextContent());
	    	request.setAttribute("Ends", doc.getElementsByTagName("Ends").item(0).getTextContent());
	    	request.setAttribute("Description", doc.getElementsByTagName("Description").item(0).getTextContent());
	    	
	    	Element sellerElement = (Element)doc.getElementsByTagName("Seller").item(0);
	    	request.setAttribute("SellerUserID", sellerElement.getAttribute("UserID"));
	    	request.setAttribute("SellerRating", sellerElement.getAttribute("Rating"));
	    	
	    	int count = doc.getElementsByTagName("Bid").getLength();
	    	BidResult[] bidList = new BidResult[count];
	    	for (int i = 0; i < count; i++)
	    		bidList[i] = new BidResult();
	    	
	    	NodeList list = doc.getElementsByTagName("Bidder");
	    	for (int i = 0; i < count; i++) {
	    		Element element = (Element)list.item(i);
	    		bidList[i].userID = element.getAttribute("UserID");
	    		bidList[i].rating = element.getAttribute("Rating");
	    	}
	    	
	    	list = doc.getElementsByTagName("Category");
	    	String category = "";
	    	for (int i = 0; i < list.getLength(); i++) {
	    		if (category.equals(""))
	    			category = list.item(i).getTextContent();
	    		else
	    			category = category + ", " + list.item(i).getTextContent();
	    	}
	    	if (category.equals(""))
	    		category = "none";
	    	
	    	request.setAttribute("Category", category);
	    	
	    	list = doc.getElementsByTagName("Time");
	    	for (int i = 0; i < count; i++) {
	    		bidList[i].time = list.item(i).getTextContent();
	    	}
	    	
	    	list = doc.getElementsByTagName("Amount");
	    	for (int i = 0; i < count; i++) {
	    		bidList[i].amount = list.item(i).getTextContent();
	    	}
	    	
	    	list = doc.getElementsByTagName("Country");
	    	for (int i = 0; i < count; i++) {
	    		bidList[i].country = list.item(i).getTextContent();
	    	}
	    	
	    	list = doc.getElementsByTagName("Location");
	    	for (int i = 0; i < count; i++) {
	    		bidList[i].location = list.item(i).getTextContent();
	    	}
	    	
	    	request.setAttribute("BidList", bidList);
	    	
	    	if (list.getLength() > count) {
	    		Element element = (Element)list.item(count);
	    		request.setAttribute("Latitude", element.getAttribute("Latitude"));
	    		request.setAttribute("Longitude", element.getAttribute("Longitude"));
	    	} else {
	    		request.setAttribute("Latitude", "");
	    		request.setAttribute("Longitude", "");
	    	}
    	} catch (Exception e) {
    		PrintWriter out = response.getWriter();
    		out.println("Item Not Found!" + e);
        	out.close();
        	
        	return;
    	}
    	
    	request.getRequestDispatcher("/itemResult.jsp").forward(request, response);
    }
}
