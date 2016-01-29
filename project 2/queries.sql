SELECT COUNT(userID) FROM (SELECT userID FROM Bidder UNION SELECT userID FROM Seller) AS tmp;
SELECT COUNT(*) FROM Item WHERE Location = 'New York';
SELECT COUNT(*) FROM Item WHERE (SELECT COUNT(*) FROM Category WHERE Category.ItemID = Item.ItemID) = 4;
SELECT (SELECT MAX(amount) FROM Bid WHERE Bid.ItemID = Item.ItemId) AS max FROM Item WHERE Item.Ends > '2001-12-20 00:00:01';
SELECT COUNT(*) FROM Seller WHERE rating > 1000;
SELECT userID FROM Bidder WHERE EXISTS (SELECT * FROM Seller WHERE Seller.userID = Bidder.userID);
SELECT COUNT(DISTINCT category) AS count FROM Category LEFT JOIN Bid ON Bid.ItemID = Category.ItemID WHERE Bid.amount > 100;