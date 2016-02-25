<%@ page import = "edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
	<title>eBay Data Keyword Item Detail</title>
</head>

<body>
	<h1>eBay Data Item Detail</h1>
	<!--<p>Your search keyword: </p>-->
	<%
	String result = (String) request.getAttribute("result");

	out.println(result);
	%>
</body>
</html>