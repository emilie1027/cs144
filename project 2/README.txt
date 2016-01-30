1
Item(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Country, Started, Ends, SellerID, Description, Latitude, Longitude)
key:ItemID

Category(ItemID, category)
key:ItemID, category

Bid(ItemID, BidderID, time, amount)
key:ItemID, BidderID, time(a user can bid on one item multiple times but should not at the same time)
dependency: Bid.ItemID depends on Item.ItemID, Bid.BidderID depends on Bidder.BidderID

Bidder(BidderID, rating, location, country)
key:BidderID

Seller(SellerID, rating)
key:SellerID

2.
{ItemID} -> Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Country, Started, Ends, SellerID, Description, Latitude, Longitude}
{ItemID, category}
{ItemID, BidderID, time} -> amount
{BidderID} -> {rating, location, country}
{SellerID} -> {rating}

3.It is in BCNF. Every left side of functional dependency holds the key. 

4.It is in 4NF. It is in BCNF and there is no MVD in each relation.



