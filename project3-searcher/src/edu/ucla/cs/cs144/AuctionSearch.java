package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
        SearchResult[] searchResult = new SearchResult[numResultsToReturn];
        try {
             System.out.println("performSearch");
             SearchEngine se = new SearchEngine();
             TopDocs topDocs = se.performSearch(query, numResultsToSkip + numResultsToReturn);
             
             ScoreDoc[] hits = topDocs.scoreDocs;
             for (int i = 0 ; i < numResultsToReturn; i++) {
                 Document doc = se.getDocument(hits[i + numResultsToSkip].doc);
                 searchResult[i] =  new SearchResult(doc.get("ItemID"), doc.get("Name"));
             }
             System.out.println("performSearch done");
         } catch (Exception e) {
             System.out.println("Exception caught.\n");
         }
        finally {
            return searchResult;
        }
		//return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] searchResult = new SearchResult[numResultsToReturn];
		SearchResult[] finalResult = null;
		
		try {
			SearchResult[] basicResult = basicSearch(query, 0, Integer.MAX_VALUE);
			HashSet<String> inRegionList = new HashSet<String>();
			Connection db = DbManager.getConnection(true);
			Statement stmt = db.createStatement();
			
			String geoCondition = "POLYGON((" + region.getLx() + " " + region.getLy() + ", "
					+ region.getLx() + " " + region.getRy() + ", "
					+ region.getRx() + " " + region.getRy() + ", "
					+ region.getRx() + " " + region.getLy() + ", "
					+ region.getLx() + " " + region.getLy() + "))";
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM RegionIndex WHERE Contains(GeomFromText('"+ geoCondition +"'), point);");
			while (rs.next()) {
				inRegionList.add(rs.getString("ItemID"));
			}
			
			int mixedResultCounter = 0;
			ArrayList<SearchResult> mixedResult = new ArrayList<SearchResult>();
			for (int i = 0; i < searchResult.length; i++)
				if (inRegionList.contains(searchResult[i].getItemId())) {
					mixedResultCounter++;
					if (mixedResultCounter > numResultsToSkip && mixedResultCounter <= numResultsToSkip + numResultsToReturn) {
						mixedResult.add(searchResult[i]);
					}
				}
			
			finalResult = new SearchResult[mixedResult.size()];
			for (int i = 0; i < mixedResult.size(); i++)
				finalResult[i] = mixedResult.get(i);
			
			rs.close();
			stmt.close();
			db.close();
		} catch (Exception e) {
			System.out.println("Exception caught.\n");
		} finally {
			return searchResult;
		}
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
