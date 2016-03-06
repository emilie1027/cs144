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

public class PayServlet extends HttpServlet implements Servlet {
	private AuctionSearchClient auctionSearchClient;
       
    public PayServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        request.getRequestDispatcher("/creditCardInput.jsp").forward(request, response);
    }
}