<!DOCTYPE html>
<html>
<head>
	<title>eBay Data Keyword Item Detail</title>
	<%@ page import = "edu.ucla.cs.cs144.BidResult" %>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?"></script>
	<%
	if (!request.getAttribute("Longitude").equals("")) {
	%>
	<script type="text/javascript">
			var defaultLatlng = new google.maps.LatLng(<%=request.getAttribute("Latitude")%>,<%=request.getAttribute("Longitude")%>);
			
			function initialize() {
				var geocoder = new google.maps.Geocoder();
				
				var myOptions = {
					zoom: 14, // default is 8
					center: defaultLatlng,
					mapTypeId: google.maps.MapTypeId.ROADMAP
				};
				var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
			}
	</script>
	<style type="text/css">
		html { height: 100% }
		body { height: 100%; margin: 0px; padding: 0px }
		#map_canvas { height: 400px }
		#location-panel {  position: absolute;top: 10px;left: 25%;z-index: 5;background-color: #fff;padding: 5px;border: 1px solid #999;text-align: center;font-family: 'Roboto','sans-serif';line-height: 30px;padding-left: 10px; }
	</style>
	<% } %>
</head>

<body<%
	if (!request.getAttribute("Longitude").equals("")) { out.print(" onload=\"initialize()\""); }
	%>>
	<h1>eBay Data Item Detail</h1>
	<p>ItemID: <%= request.getAttribute("ItemID") %></p>
	<p>Name: <%= request.getAttribute("Name") %></p>
	<p>Seller: <%= request.getAttribute("SellerUserID") %> (Rating: <%= request.getAttribute("SellerRating") %>)</p>
	<p>Currently: <%= request.getAttribute("Currently") %></p>
	<p>First_Bid: <%= request.getAttribute("First_Bid") %></p>
	<p>Number of Bids: <%= request.getAttribute("Number_of_Bids") %></p>
	<p>Location: <%= request.getAttribute("Location") %></p>
	<p>Latitude: <%= request.getAttribute("Latitude") %> | Longitude: <%= request.getAttribute("Longitude") %></p>
	<p>Category: <%= request.getAttribute("Category") %></p>
	<p>Started: <%= request.getAttribute("Started") %></p>
	<p>Ends: <%= request.getAttribute("Ends") %></p>
	<p>Description: <br/><%= request.getAttribute("Description") %><br/></p>
	<p>Bids: </p>
	<%
		BidResult[] list = (BidResult[]) request.getAttribute("BidList");

		for(BidResult item : list) {
			out.println("<hr>");
			out.println("UserID: " + item.userID + " (rating: " + item.rating + ")<br/>");
			out.println("Time: " + item.time + "<br/>");
			out.println("Amount: " + item.amount + "<br/>");
			out.println("Location: " + item.location + "<br/>");
			out.println("Country: " + item.country + "<br/>");
		}

		out.println("<hr>");
	%>

	<div id="map_canvas" style="width:600px; height:400px"></div> 
</body>
</html>