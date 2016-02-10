package edu.ucla.cs.cs144;

import java.util.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    private IndexWriter indexWriter = null;
    
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
        //Retrieve data
        
        //Erase existing index
        getIndexWriter(true);
        // Index all Accommodation entries
        //task: retrive content from database: indexItem();
        List<Item> itemList = retrieveItemFromDatabase(conn);
        Map<String, List<String>> categoryMap = retrieveCategoryFromDatabase(conn);
        for (Item item : itemList) {
            indexItemUnion(item, categoryMap.get(item.getItemID()));
        }
        //close index writer
        closeIndexWriter();


        // close the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    public Map<String, List<String>> retrieveCategoryFromDatabase(Connection c) {
        Map<String, List<String>> categoryMap = new HashMap<String, List<String>>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Category");
            
            while(rs.next()) {
                String itemID = rs.getString("ItemID");
                String category = rs.getString("category");
                if (categoryMap.containsKey(itemID))
                    categoryMap.get(itemID).add(category);
                else{
                    List<String> categoryList = new ArrayList<String>();
                    categoryList.add(category);
                    categoryMap.put(itemID, categoryList);
                }
            }
        //} catch (ClassNotFoundException ex){
        //    System.out.println(ex);
        } catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        } finally {
            return categoryMap;
        }
    }
    
    /*
    public List<Category> retrieveCategoryFromDatabase(Connection c) {
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM Category");
        List<Category> categoryList = new ArrayList<Category>();
        while(rs.next()) {
            categoryList.add(new Category(rs.getString("ItemID"), rs.getString("category")));
        }
        retrn categoryList;
    }
     */
    
    public List<Item> retrieveItemFromDatabase(Connection c) {
        List<Item> itemList = new ArrayList<Item>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Item");
            
            while (rs.next()) {
                String[] attributes = new String[14];
                for (int i = 1 ; i <  attributes.length ; i++) {
                    attributes[i-1] = rs.getString(i);
                }
                itemList.add(new Item(attributes));
            }
        //} catch (ClassNotFoundException ex){
        //    System.out.println(ex);
        } catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        } finally {
            return itemList;
        }
    }
    
    public IndexWriter getIndexWriter(boolean create) {
        try {
            if (indexWriter == null) {
                Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1"));
                //Directory indexDir = FSDirectory.open(new File("index-directory"));
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
                indexWriter = new IndexWriter(indexDir, config);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            return indexWriter;
        }
    }
    
    public void closeIndexWriter() {
        try {
            if (indexWriter != null) {
                indexWriter.close();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void indexItemUnion(Item item, List<String> categoryList) {
        try {
            IndexWriter writer = getIndexWriter(false);
            Document doc = new Document();
            doc.add(new StringField("ItemID", item.getItemID(), Field.Store.YES));
            doc.add(new StringField("Name", item.getName(), Field.Store.YES));
            String fullSearchableText = item.getName() + " " + item.getDescription();
            for (String category: categoryList)
                fullSearchableText = fullSearchableText + " " + category;
            doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
            writer.addDocument(doc);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
        /*
        try {
            System.out.println("performSearch");
            SearchEngine se = new SearchEngine();
            //TopDocs topDocs = se.performSearch("Notre Dame museum", 100);
            //TopDocs topDocs = se.performSearch("superman", Integer.MAX_VALUE);
            //System.out.println("Results found: " + topDocs.totalHits);
            
            TopDocs topDocs = se.performSearch("star trek", Integer.MAX_VALUE);
            System.out.println("Results found: " + topDocs.totalHits);
            //TopDocs topDocs = se.performSearch("", Integer.MAX_VALUE);
            
            ScoreDoc[] hits = topDocs.scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                Document doc = se.getDocument(hits[i].doc);
                System.out.println(doc.get("ItemID")
                                   + " " + doc.get("Name")
                                   + " (" + hits[i].score + ")");
            }
            System.out.println("performSearch done");
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }
         */
    }
}
