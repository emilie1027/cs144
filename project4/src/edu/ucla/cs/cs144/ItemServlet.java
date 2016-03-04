package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
            out.println("<!DOCTYPE html><html><body>Item Not Found<br><a href='/eBay/keywordSearch.html'>[Back to Search]</a></body><html>");
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
	    	request.setAttribute("Started", doc.getElementsByTagName("Started").item(0).getTextContent());
	    	request.setAttribute("Ends", doc.getElementsByTagName("Ends").item(0).getTextContent());
	    	request.setAttribute("Description", doc.getElementsByTagName("Description").item(0).getTextContent());
	    	
	    	try {
	    		request.setAttribute("Buy_Price", doc.getElementsByTagName("Buy_Price").item(0).getTextContent());
	    	} catch (Exception e) {
	    		request.setAttribute("Buy_Price", "");
	    	}
	    	
	    	Element sellerElement = (Element)doc.getElementsByTagName("Seller").item(0);
	    	request.setAttribute("SellerUserID", sellerElement.getAttribute("UserID"));
	    	request.setAttribute("SellerRating", sellerElement.getAttribute("Rating"));
	    	
	    	int count = doc.getElementsByTagName("Bid").getLength();
	    	//BidResult[] bidList = new BidResult[count];
	    	List<BidResult> bidList = new ArrayList<BidResult>();
	    	for (int i = 0; i < count; i++)
	    		bidList.add(new BidResult());
	    	
	    	NodeList list = doc.getElementsByTagName("Bidder");
	    	for (int i = 0; i < count; i++) {
	    		Element element = (Element)list.item(i);
	    		bidList.get(i).userID = element.getAttribute("UserID");
	    		bidList.get(i).rating = element.getAttribute("Rating");
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
	    		bidList.get(i).time = list.item(i).getTextContent();
	    	}
	    	
	    	list = doc.getElementsByTagName("Amount");
	    	for (int i = 0; i < count; i++) {
	    		bidList.get(i).amount = list.item(i).getTextContent();
	    	}
	    	
	    	list = doc.getElementsByTagName("Country");
	    	for (int i = 0; i < count; i++) {
	    		bidList.get(i).country = list.item(i).getTextContent();
	    	}
	    	
	    	request.setAttribute("Country", doc.getElementsByTagName("Country").item(count).getTextContent());
	    	
	    	list = doc.getElementsByTagName("Location");
	    	for (int i = 0; i < count; i++) {
	    		bidList.get(i).location = list.item(i).getTextContent();
	    	}
	    	
	    	Collections.sort(bidList, new BidResultComparator());
	    	
	    	request.setAttribute("BidList", bidList);
	    	
	    	if (list.getLength() > count) {
	    		request.setAttribute("Location", doc.getElementsByTagName("Location").item(count).getTextContent());
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
    
    static class BidResultComparator implements Comparator<BidResult> {
        public int compare(BidResult o1, BidResult o2) {
        	try {
	        	SimpleDateFormat parser = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	        	Date d1 = parser.parse(o1.time);
	        	Date d2 = parser.parse(o2.time);
	        	return d2.compareTo(d1);
        	} catch (Exception e) {
        		return 0;
        	}
       }
    }
}