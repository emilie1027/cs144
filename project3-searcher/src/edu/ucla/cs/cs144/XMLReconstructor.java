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
    
    public String getStringFromDoc(org.w3c.dom.Document doc)    {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch(TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private String formatTime(String time) {
        String result = "";
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsed = oldFormat.parse(time);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
            result = newFormat.format(parsed);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
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
            itemAttr[8] = formatTime(itemAttr[8]);
            itemAttr[9] = formatTime(itemAttr[9]);
            for (int i = 2 ; i < 5 ; i++ ) {
                if ( !"".equals(itemAttr[i])) itemAttr[i] = "$" + itemAttr[i];
            }
            Element itemEle = doc.createElement("Item");
            itemEle.setAttribute(itemAttrName[0], itemAttr[0]);
            doc.appendChild(itemEle);
            Element nameEle = doc.createElement(itemAttrName[1]);
            nameEle.appendChild(doc.createTextNode(itemAttr[1]));
            itemEle.appendChild(nameEle);
            //add category
            for (int i = 0 ; i < categoryList.size() ; i++) {
                String[] category = categoryList.get(i);
                Element categoryEle = doc.createElement("Category");
                categoryEle.appendChild(doc.createTextNode(category[1]));
                itemEle.appendChild(categoryEle);
            }
            for (int i = 2 ; i < 6 ; i++) {
                if (!"".equals(itemAttr[i])) {
                    Element eleTemp = doc.createElement(itemAttrName[i]);
                    eleTemp.appendChild(doc.createTextNode(itemAttr[i]));
                    itemEle.appendChild(eleTemp);
                }
            }
            //add bid and bidder
            if (bidList.size() > 0) {
                Element bidsEle = doc.createElement("Bids");
                itemEle.appendChild(bidsEle);
                for (int i = 0 ; i < bidList.size() ; i++) {
                    String[] bid = bidList.get(i);
                    bid[2] = formatTime(bid[2]);
                    bid[3] = "$" + bid[3];
                    String[] bidder = bidderList.get(i);
                    Element bidEle = doc.createElement("Bid");
                    Element bidderEle = doc.createElement("Bidder");
                    //add bidder
                    for (int j = 0 ; j < 2 ; j++) {
                        bidderEle.setAttribute(bidderAttrName[j], bidder[j]);
                    }
                    for (int j = 2; j < bidderAttrName.length ; j++) {
                        Element eleTemp = doc.createElement(bidderAttrName[j]);
                        eleTemp.appendChild(doc.createTextNode(bidder[j]));
                        bidderEle.appendChild(eleTemp);
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
            //add latitue, longitude
            Element locationEle = doc.createElement("Location");
            locationEle.appendChild(doc.createTextNode(itemAttr[6]));
            for (int i = 12; i < itemAttrName.length ; i++) {
                if (!("".equals(itemAttr[i])))
                    locationEle.setAttribute(itemAttrName[i], itemAttr[i]);
            }
            itemEle.appendChild(locationEle);
            
            for (int i = 7 ; i < 10; i++) {
                if (!"".equals(itemAttr[i])) {
                    Element eleTemp = doc.createElement(itemAttrName[i]);
                    eleTemp.appendChild(doc.createTextNode(itemAttr[i]));
                    itemEle.appendChild(eleTemp);
                }
            }
            //add seller
            String[] seller = sellerList.get(0);
            Element sellerEle = doc.createElement("Seller");
            for (int i = 0; i < sellerAttrName.length ; i++) {
                sellerEle.setAttribute(sellerAttrName[i], seller[i]);
            }
            itemEle.appendChild(sellerEle);
            //add description
            Element descriptionEle = doc.createElement(itemAttrName[10]);
            descriptionEle.appendChild(doc.createTextNode(itemAttr[10]));
            itemEle.appendChild(descriptionEle);
            result = getStringFromDoc(doc);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        }
        return result;
    }
    
    
}
