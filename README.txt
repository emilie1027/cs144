Item(ItemID, Name, Category, Currently, Buy_Price, First_Bid, Number of Bids, Location, Country, Started, Ends, Seller)
key:ItemID

Bids(ItemID, userID, time, amount)
key:ItemID, userID, time (a user can bid on one item multiple times but should not at the same time)
constraint: time > started && time < ends if it ended

Bidder(userID, rating, location, country)
key:userID

Seller(userID, rating)
key:userID

Location(location, attitude, longitude)
key:location, attitude, longitude