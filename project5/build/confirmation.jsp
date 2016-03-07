<!DOCTYPE html>
<html>
<head>
<title>Transaction: Confirmation</title>
<%@ page import = "edu.ucla.cs.cs144.BidResult" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "java.util.List" %>
</head>

<body>
<h1>Transaction: Confirmation</h1>
<p>ItemID: <%= request.getSession().getAttribute("ItemID")%></p>
<p>Name: <%= request.getSession().getAttribute("Name")%></p>
<p>Buy_Price: <%= request.getSession().getAttribute("Buy_Price")%></p>
<p>Credit Card: <%= request.getSession().getAttribute("Credit_Card")%></p>
<p>Time: <%= request.getSession().getAttribute("Transaction_Time")%></p>
</body>
</html>