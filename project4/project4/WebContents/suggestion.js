function StateSuggestions() {};

StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
														  bTypeAhead /*:boolean*/) {
	var aSuggestions = [];
	var sTextboxValue = oAutoSuggestControl.textbox.value;
	
	if (sTextboxValue.length > 0){

		var requester = new XMLHttpRequest();
		requester.onreadystatechange = function() {
			if (requester.readyState == XMLHttpRequest.DONE) {
				if (requester.status == 200) {
					var doc = requester.responseXML.getElementsByTagName("CompleteSuggestion");
					for (var i = 0; i < doc.length; i++) {
						aSuggestions.push(doc[i].childNodes[0].getAttribute("data"));
					}
				} else {
					console.log("Ajax request failed");
				}

				oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
			}
		}
		
		requester.open("GET", "/eBay/suggest?q=" + sTextboxValue, true);
		requester.send();
	}
};
