package edu.ucla.cs.cs144;

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;


import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XMLReconstructor {
    
    private String[] itemAttrName = new String[]{"ItemID", "Name", "Currently", "Buy_Price", "First_Bid", "Number_of_Bids", "Location", "Country", "Started", "Ends", "Description", "Seller", "Latitude", "Longitude"};
    private String[] categoryAttrName = new String[]{"ItemID", "Category"};
    private String[] bidAttrName = new String[]{"ItemID", "UserID", "Time", "Amount"};
    private String[] bidderAttrName = new String[]{"UserID", "Rating", "Location", "Country"};
    private String[] sellerAttrName = new String[]{"UserID", "Rating"};
    
    private String itemQuery;
    private String categoryQuery;
    private String bidQuery;
    private String bidderQuery;
    private String sellerQuery;
    
    private List<String[]> itemList;
    private List<String[]> categoryList;
    private List<String[]> bidList;
    private List<String[]> bidderList;
    private List<String[]> sellerList;
    
    XMLReconstructor(String itemId) {
        itemQuery = "SELECT * FROM Item WHERE ItemID = " + itemId;
        categoryQuery = "SELECT * FROM Category WHERE ItemID = " + itemId;
        bidQuery = "SELECT * FROM Bid WHERE ItemID = " + itemId;
        sellerQuery = "SELECT * FROM Seller WHERE userID = ";
        bidderQuery = "SELECT * FROM Bidder WHERE userID = ";
    }
	
    public void retrieveData() {
        Connection conn = null;
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(itemQuery);
            itemList = retrieveDataListFromRSet(rs, itemAttrName.length);
            System.out.println(itemList.size());
            if (itemList.size() == 1) {
                String[] item = itemList.get(0);
                categoryList = retrieveDataListFromRSet(s.executeQuery(categoryQuery), categoryAttrName.length);
                sellerList = retrieveDataListFromRSet(s.executeQuery(sellerQuery + "\'"+item[11] + "\'"), sellerAttrName.length);
                System.out.println(sellerList.size());
                bidList = retrieveDataListFromRSet(s.executeQuery(bidQuery), bidAttrName.length);
                bidderList = new ArrayList<String[]>();
                for (int i = 0 ; i < bidList.size() ; i++) {
                    String[] bid = bidList.get(i);
                    bidderList.add(retrieveDataListFromRSet(s.executeQuery(bidderQuery+"\'" + bid[1] +"\'"), bidderAttrName.length).get(0));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    

    private List<String[]> retrieveDataListFromRSet(ResultSet rs, int dataLength) {
        List<String[]> dataList = new ArrayList<String[]>();
        try {
            while (rs.next()) {
                String[] data = new String[dataLength];
                for (int i = 0; i < dataLength ; i++) {
                    data[i] = rs.getString(i+1);
                }
                dataList.add(data);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        }
        return dataList;
    }

    
    public String reconstructXML() {
        /*
         <Item ItemID="1043374545">
         <Name>christopher radko | fritz n_ frosty sledding</Name>
         <Category>Collectibles</Category>
         <Category>Decorative &amp; Holiday</Category>
         <Category>Decorative by Brand</Category>
         <Category>Christopher Radko</Category>
         <Currently>$30.00</Currently>
         <First_Bid>$30.00</First_Bid>
         <Number_of_Bids>0</Number_of_Bids>
         <Bids />
         <Location>its a dry heat</Location>
         <Country>USA</Country>
         <Started>Dec-03-01 18:10:40</Started>
         <Ends>Dec-13-01 18:10:40</Ends>
         <Seller Rating="1035" UserID="rulabula" />
         <Description>
         */
        retrieveData();
        
        String result = "";
        if (itemList.size() == 0)
            return result;
        try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        
        //add item
        String[] itemAttr = itemList.get(0);
        Element itemEle = doc.createElement("Item");
        itemEle.setAttribute(itemAttrName[0], itemAttr[0]);
        doc.appendChild(itemEle);
        
        for (int i = 1 ; i < itemAttrName.length - 2; i++) {
            Element eleTemp = doc.createElement(itemAttrName[i]);
            eleTemp.appendChild(doc.createTextNode(itemAttr[i]));
            itemEle.appendChild(eleTemp);
        }
        //add category
        for (int i = 0 ; i < categoryList.size() ; i++) {
            String[] category = categoryList.get(i);
            Element categoryEle = doc.createElement("Category");
            categoryEle.appendChild(doc.createTextNode(category[1]));
            itemEle.appendChild(categoryEle);
        }
        //add seller
        String[] seller = sellerList.get(0);
        Element sellerEle = (Element)itemEle.getElementsByTagName("Seller").item(0);
        for (int i = 0; i < sellerAttrName.length ; i++) {
            sellerEle.setAttribute(sellerAttrName[i], seller[i]);
        }
        //add latitue, longitude
        Element locationEle = (Element)itemEle.getElementsByTagName("Location").item(0);
        for (int i = 12; i < itemAttrName.length ; i++) {
            locationEle.setAttribute(itemAttrName[i], itemAttr[i]);
        }
        //add bid and bidder
        if (bidList.size() > 0) {
            Element bidsEle = (Element)itemEle.getElementsByTagName("Bids").item(0);
            for (int i = 0 ; i < bidList.size() ; i++) {
                String[] bid = bidList.get(i);
                String[] bidder = bidderList.get(i);
                Element bidEle = doc.createElement("Bid");
                Element bidderEle = doc.createElement("Bidder");
                //add bidder
                for (int j = 0 ; j < 2 ; j++) {
                    bidderEle.setAttribute(bidderAttrName[j], bidder[j]);
                }
                for (int j = 2; j < bidderAttrName.length ; j++) {
                    bidderEle.appendChild(doc.createElement(bidderAttrName[j]).appendChild(doc.createTextNode(bidder[j])));
                }
                bidEle.appendChild(bidderEle);
                for (int j = 2; j < bidAttrName.length ; j++) {
                    Element eleTemp = doc.createElement(bidAttrName[j]);
                    eleTemp.appendChild(doc.createTextNode(bid[j]));
                    bidEle.appendChild(eleTemp);
                }
                bidsEle.appendChild(bidEle);
            }
        }
            result = getStringFromDoc(doc);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        }
        return result;
    }
    
    public String getStringFromDoc(org.w3c.dom.Document doc)    {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch(TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
   /*
    public static void main(String[] args) {
        XMLReconstructor xmlReconstructor = new XMLReconstructor("1043374545");
        String result = xmlReconstructor.reconstructXML();
        System.out.println(result);
    }
    */
}
