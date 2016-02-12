CREATE TABLE RegionIndex (
	ItemId int(11) NOT NULL PRIMARY KEY,
	point POINT NOT NULL,
	SPATIAL INDEX (point)
) ENGINE=MyISAM;

INSERT INTO RegionIndex (ItemId, point)
	SELECT ItemID, POINT(Latitude, Longitude)
	FROM Item
	WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;
