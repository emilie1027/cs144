CREATE TABLE Bidder (
	userID int(11) NOT NULL PRIMARY KEY,
	rating int(11) NOT NULL,
	location varchar(100) NOT NULL,
	country varchar(100) NOT NULL
) ENGINE=INNODB;

CREATE TABLE Seller (
	userID int(11) NOT NULL PRIMARY KEY,
	rating int(11) NOT NULL
) ENGINE=INNODB;

CREATE TABLE Item (
	ItemID int(11) NOT NULL PRIMARY KEY,
	Name varchar(100) NOT NULL,
	Currently float NOT NULL,
	Buy_Price float NOT NULL,
	NumberofBids int(11) NOT NULL,
	Location varchar(100) NOT NULL,
	Country varchar(50) NOT NULL,
	Started timestamp NOT NULL,
	Ends timestamp NOT NULL,
	Seller int NOT NULL,
	Latitude float NOT NULL,
	Longitude float NOT NULL,
	FOREIGN KEY (Seller) REFERENCES Seller(userID)
) ENGINE=INNODB;

CREATE TABLE Category (
	ItemID int(11) NOT NULL,
	category varchar(100) NOT NULL,
	PRIMARY KEY (ItemID, category),
	FOREIGN KEY (ItemID) REFERENCES Item(ItemID)
) ENGINE=INNODB;

CREATE TABLE Bid (
	ItemID int(11) NOT NULL,
	userID int(11) NOT NULL,
	time timestamp,
	PRIMARY KEY (ItemID, userID),
	FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
	FOREIGN KEY (userID) REFERENCES Bidder(userID)
) ENGINE=INNODB;

CREATE INDEX Category_ItemID ON Category (ItemID);
CREATE INDEX Bid_ItemID ON Bid (ItemID);
CREATE INDEX Bid_userID ON Bid (userID);