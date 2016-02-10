SELECT COUNT(userID) FROM (SELECT userID FROM Bidder UNION SELECT userID FROM Seller) AS tmp;
SELECT COUNT(*) FROM Item WHERE BINARY Location = 'New York';
SELECT COUNT(*) FROM Item WHERE (SELECT COUNT(*) FROM Category WHERE Category.ItemID = Item.ItemID) = 4;
SELECT DISTINCT(I.ItemId) FROM Item As I LEFT JOIN Bid AS B ON B.ItemId = I.ItemId WHERE I.Ends > '2001-12-20 00:00:01' AND B.amount = (SELECT MAX(amount) AS max FROM Item LEFT JOIN Bid ON Bid.ItemId = Item.ItemId WHERE Item.Ends > '2001-12-20 00:00:01');
SELECT COUNT(*) FROM Seller WHERE rating > 1000;
SELECT COUNT(userID) FROM Bidder WHERE EXISTS (SELECT * FROM Seller WHERE Seller.userID = Bidder.userID);
SELECT COUNT(DISTINCT category) AS count FROM Category LEFT JOIN Bid ON Bid.ItemID = Category.ItemID WHERE Bid.amount > 100;
