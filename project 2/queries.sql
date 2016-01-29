
SELECT COUNT(*) FROM Item WHERE Location = 'New York';
SELECT COUNT(*) FROM Item WHERE (SELECT COUNT(*) FROM Category WHERE Category.ItemID = Item.ItemID) = 4;

SELECT COUNT(*) FROM Seller WHERE rating > 1000;

SELECT COUNT(DISTINCT category) FROM Category LEFT JOIN Bid ON Bid.ItemID = Category.ItemID WHERE Bid.amount > 100;