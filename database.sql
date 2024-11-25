CREATE TABLE IF NOT EXISTS Users (
    userID VARCHAR(36) NOT NULL,
    password CHAR(80) NOT NULL,
    firstName CHAR(80) NOT NULL,
    lastName CHAR(80) NOT NULL,
    PRIMARY KEY (userID)
);

CREATE TABLE IF NOT EXISTS BusinessInfo (
    businessID VARCHAR(36) NOT NULL,
    addressPrimary VARCHAR(255) NOT NULL, 
    addressSecondary VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    stateRegion VARCHAR(50) NOT NULL,
    zipCode VARCHAR(20) NOT NULL,
    PRIMARY KEY (businessID)
);
CREATE TABLE IF NOT EXISTS JobBoard (
    shopperID VARCHAR(36) NOT NULL,
    fulfillmentTime VARCHAR(20) NOT NULL,
    PRIMARY KEY (shopperID, fulfillmentTime),
    FOREIGN KEY (shopperID) REFERENCES Users(userID)
);
CREATE TABLE IF NOT EXISTS Orders (
    orderID VARCHAR(36) NOT NULL,
    userID VARCHAR(36) NOT NULL,
    businessID VARCHAR(36) NOT NULL,
    shopperID VARCHAR(36),
    status VARCHAR(50),
    fulfillmentTime INT,
    addressPrimary VARCHAR(255) NOT NULL, 
    addressSecondary VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    stateRegion VARCHAR(50) NOT NULL,
    zipCode VARCHAR(20) NOT NULL,
    PRIMARY KEY (orderID),
    FOREIGN KEY (userID) REFERENCES Users(userID),
    FOREIGN KEY (businessID) REFERENCES BusinessInfo(businessID),
    FOREIGN KEY (shopperID) REFERENCES JobBoard(shopperID)
);



CREATE TABLE IF NOT EXISTS Items (
    upc VARCHAR(12) NOT NULL,
    businessID VARCHAR(36) NOT NULL,
    name VARCHAR(512) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (upc, businessID),
    UNIQUE (name),
    FOREIGN KEY (businessID) REFERENCES BusinessInfo(businessID)
);

CREATE TABLE IF NOT EXISTS OrderItems (
    orderID VARCHAR(36) NOT NULL,
    upc VARCHAR(12) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (orderID, upc),
    FOREIGN KEY (orderID) REFERENCES Orders(orderID),
    FOREIGN KEY (upc) REFERENCES Items(upc)
);
