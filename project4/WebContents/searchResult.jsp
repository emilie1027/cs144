<%@ page import = "edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
	<title>eBay Data Keyword Search Result</title>
</head>

<body>
	<h1>eBay Data Keyword Search Result</h1>
	<!--<p>Your search keyword: </p>-->
	<%
	SearchResult[] list = (SearchResult[]) request.getAttribute("result");

	for(SearchResult item : list) {
		out.println("<p>ItemID: " + item.getItemId() + "<br/>");
	    out.println("ItemName: " + item.getName() + "<br/>");
		out.println("[<a href=\"/eBay/item?id=" + item.getItemId() + "\">Show Item</a>]</p>");
	}
	%>
</body>
</html>