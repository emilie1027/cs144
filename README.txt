Item(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Country, Started, Ends, SellerID, Description, Latitude, Longitude)
key:ItemID

Category(ItemID, category)
key:ItemID, category
explanation: It's one to many relationship.

Bid(ItemID, BidderID, time, amount)
key:ItemID, BidderID, time(a user can bid on one item multiple times but should not at the same time)
constraint: time > started && time < ends if it ended

Bidder(BidderID, rating, location, country)
key:userID


Seller(SellerID, rating)
key:userID


Duplication exists in Bidder and seller.

Questions:
1. need another user table? or modify user and seller? or use union



