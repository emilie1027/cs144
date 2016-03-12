<!DOCTYPE html>
<html>
<head>
<title>Transaction: Input Credit Card Information</title>
<%@ page import = "edu.ucla.cs.cs144.BidResult" %>
<%@ page import = "java.util.ArrayList" %>
</head>

<body>
<h1>Transaction: Input Credit Card Information</h1>
<form id="form" method="post" action="https://<%=request.getServerName()+ ":8443" + request.getContextPath()%>/confirm">
<p>ItemID: <%= request.getSession().getAttribute("ItemID")%></p>
<p>Name: <%= request.getSession().getAttribute("Name")%></p>
<p>Buy_Price: <%= request.getSession().getAttribute("Buy_Price")%></p></br><br/>
<b>Credit Card:</b></br><input type="text" name="Credit_Card"/><br/>
<button type="submit">Submit</button>
</form><br/>
</body>
</html>