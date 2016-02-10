/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import java.text.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    static String getAttributeTextByTagName(Element element, String str) {
        org.w3c.dom.NamedNodeMap nodeMap = element.getAttributes();
        if (nodeMap != null && nodeMap.getLength() > 0) {
            return nodeMap.getNamedItem(str).getNodeValue();
        }
        return "";
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    static String formatTime(String time) {
        String result = "";
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
            Date parsed = oldFormat.parse(time);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = newFormat.format(parsed);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        String[] itemAttrName = new String[]{"ItemID", "Name", "Currently", "Buy_Price", "First_Bid", "Number_of_Bids", "Location", "Country", "Started", "Ends", "Description", "Seller", "Latitude", "Longitude"};
        String[] categoryAttrName = new String[]{"ItemID", "Category"};
        String[] bidAttrName = new String[]{"ItemID", "UserID", "Time", "Amount"};
        String[] bidderAttrName = new String[]{"UserID", "Rating", "Location", "Country"};
        String[] locationAttrName = new String[]{"Latitude", "Longitude"};
        String[] sellerAttrName = new String[]{"UserID", "Rating"};
        
        LinkedList<String[]> itemList= new LinkedList<String[]>();
        LinkedList<String[]> categoryList = new LinkedList<String[]>();
        LinkedList<String[]> bidList = new LinkedList<String[]>();
        LinkedList<String[]> bidderList = new LinkedList<String[]>();
        LinkedList<String[]> sellerList = new LinkedList<String[]>();
        
        HashSet<String> bidderSet = new HashSet<String>();
        HashSet<String> sellerSet = new HashSet<String>();

        org.w3c.dom.NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element itemEle = (Element) node;
            String[] item = new String[itemAttrName.length];
            item[0] = getAttributeTextByTagName(itemEle, "ItemID");
            for (int j = 1; j < itemAttrName.length; ++j) {
                item[j] = '"' + getElementTextByTagNameNR(itemEle, itemAttrName[j]).replace("\"", "\"\"").replace("\\", "\\\\") + '"';
            }
            Element[] categoriesEle = getElementsByTagNameNR(itemEle, "Category");
            for (int j = 0; j < categoriesEle.length; ++j) {
                String[] category = new String[categoryAttrName.length];
                category[0] = item[0];
                category[1] = '"' + getElementText(categoriesEle[j]).replace("\"", "\"\"").replace("\\", "\\\\") + '"';
                categoryList.add(category);
            }
            Element[] bidsEle = getElementsByTagNameNR(getElementByTagNameNR(itemEle, "Bids"), "Bid");
            for (int j = 0; j < bidsEle.length; ++j) {
                Element bidEle = bidsEle[j];
                Element bidderEle = getElementByTagNameNR(bidEle, "Bidder");
                String[] bid = new String[bidAttrName.length];
                String[] bidder = new String[bidderAttrName.length];
                if (bid != null) {
                    for (int k = 0; k < 2; ++k) {
                        bidder[k] = '"' + getAttributeTextByTagName(bidderEle, bidderAttrName[k]).replace("\"", "\"\"").replace("\\", "\\\\") + '"';
                    }
                    for (int k = 2; k < bidderAttrName.length; ++k) {
                        bidder[k] = '"' + getElementTextByTagNameNR(bidderEle, bidderAttrName[k]).replace("\"", "\"\"").replace("\\", "\\\\") + '"';
                    }
                }
                bid[0] = item[0];
                bid[1] = bidder[0];
                bid[2] = formatTime(getElementTextByTagNameNR(bidEle, "Time"));
                bid[3] = strip(getElementTextByTagNameNR(bidEle, "Amount"));
                if (!bidderSet.contains(bidder[0])) {
                    bidderList.add(bidder);
                    bidderSet.add(bidder[0]);
                }
                bidList.add(bid);
            }
            String[] location = new String[locationAttrName.length];
            Element locationEle = getElementByTagNameNR(itemEle, "Location");
            for (int j = 0; j < locationAttrName.length; ++j) {
                location[j] = '"' + getAttributeTextByTagName(locationEle, locationAttrName[j]).replace("\"", "\"\"").replace("\\", "\\\\") + '"';
            }
            String[] seller = new String[sellerAttrName.length];
            Element sellerEle = getElementByTagNameNR(itemEle, "Seller");
            for (int j = 0; j < sellerAttrName.length; ++j) {
                seller[j] = '"' + getAttributeTextByTagName(sellerEle, sellerAttrName[j]).replace("\"", "\"\"").replace("\\", "\\\\") + '"';
            }
            if (!sellerSet.contains(seller[0])) {
                sellerList.add(seller);
                sellerSet.add(seller[0]);
            }
            item[8] = formatTime(item[8].replace("\"", "")).replace("\\", "");
            item[9] = formatTime(item[9].replace("\"", "")).replace("\\", "");
            item[itemAttrName.length - 3] = seller[0];
            item[itemAttrName.length - 2] = location[0];
            item[itemAttrName.length - 1] = location[1];
            for (int j = 2; j < 5; ++j) {
                item[j] = strip(item[j].replace("\"", ""));
            }
            itemList.add(item);
        }
        }
        
        writeToFile(itemList, "Item-0.txt");
        writeToFile(categoryList, "Category-0.txt");
        writeToFile(bidList, "Bid-0.txt");
        writeToFile(bidderList, "Bidder-0.txt");
        writeToFile(sellerList, "Seller-0.txt");
    }
    
    public static void writeToFile(List<String[]> list, String filepath) {
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String[] arrstring : list) {
                if (arrstring.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < arrstring.length - 1; ++i) {
                    stringBuilder.append(arrstring[i]).append(",");
                }
                stringBuilder.append(arrstring[arrstring.length - 1]);
                bufferedWriter.write(stringBuilder.toString());
                bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
        
        /**************************************************************/
    
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
