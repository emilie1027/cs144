<%@ page import = "edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
	<title>eBay Data Keyword Search Result</title>
</head>

<body>
	<h1>eBay Data Keyword Search Result</h1>
	<form id="form1" method="get" action="/eBay/search">
		<b>keyword:</b><br/>
		<input type="text" id="q" name="q"/>
		<br/><br/>
		<b>numResultsToSkip:</b><br/>
		<input type="text" name="numResultsToSkip" value="0"/>
		<br/><br/>
		<b>numResultsToReturn:</b><br/>
		<input type="text" name="numResultsToReturn" value="10"/>
		<br/><br/>
		<button type="submit">Search</button>
	</form><br/>
	<!--<p>Your search keyword: </p>-->
	<%
	SearchResult[] list = (SearchResult[]) request.getAttribute("result");

	for(SearchResult item : list) {
		out.println("<p>ItemID: " + item.getItemId() + "<br/>");
	    out.println("ItemName: " + item.getName() + "<br/>");
		out.println("[<a href=\"/eBay/item?id=" + item.getItemId() + "\">Show Item</a>]</p>");
	}
	%>
	<br/>
	<a href="/eBay/keywordSearch.html">Back to Search</a> 
	<% if ((Integer)request.getAttribute("numResultsToSkip") > 0) { %>
	[<a href="/eBay/search?q=<%= request.getAttribute("q") %>&numResultsToSkip=<%= (Integer)request.getAttribute("numResultsToSkip") - (Integer)request.getAttribute("numResultsToReturn") %>&numResultsToReturn=<%= request.getAttribute("numResultsToReturn") %>">Prev Page</a>] <% } %>
	<% if ((Integer)request.getAttribute("numResults") == (Integer)request.getAttribute("numResultsToReturn")) { %>[<a href="/eBay/search?q=<%= request.getAttribute("q") %>&numResultsToSkip=<%= (Integer)request.getAttribute("numResultsToSkip") + (Integer)request.getAttribute("numResultsToReturn") %>&numResultsToReturn=<%= request.getAttribute("numResultsToReturn") %>">Next Page</a>] <% } %>
</body>
</html>