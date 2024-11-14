




CREATE TABLE IF NOT EXISTS Users(
    UserID TEXT UNIQUE NOT NULL,
    password CHAR(80) NOT NULL,
    firstName CHAR(80) NOT NULL,
    lastName CHAR(80) NOT NULL,
    PRIMARY KEY(USERID)
    
);


CREATE TABLE IF NOT EXISTS Orders (
    OrderID TEXT UNIQUE NOT NULL,
    UserID TEXT NOT NULL,
    BusinessID TEXT NOT NULL,
    ShopperID TEXT NOT NULL,
    Status TEXT,
    fulfillmentTime INT NOT NULL,
    addressprimary VARCHAR(255) NOT NULL, 
    addresssecondary VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50 NOT NULL),
    zip_code VARCHAR(20) NOT NULL
    PRIMARY KEY(OrderID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BusinessID) REFERENCES BusinessInfo(BusinessID),
    FOREIGN KEY (ShopperID) REFERENCES JobBoard(ShopperID),
    FOREIGN KEY (fulfillmenttime) REFERENCES JobBoard(fulfillmenttime)


);



CREATE TABLE IF NOT EXISTS BusinessInfo(
    BusinessID TEXT UNIQUE NOT NULL,
    addressprimary VARCHAR(255) NOT NULL, 
    addresssecondary VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50 NOT NULL),
    zip_code VARCHAR(20) NOT NULL,
    PRIMARY KEY (BusinessID)


);


CREATE TABLE IF NOT EXISTS OrderItems(
    OrderID TEXT UNIQUE NOT NULL,
    UPC TEXT UNIQUE NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (OrderID, UPC),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (UPC) REFERENCES Items(UPC)


);

CREATE TABLE IF NOT EXISTS JobBoard(
    ShopperID TEXT UNIQUE NOT NULL,
    fulfillmenttime TEXT UNIQUE NOT NULL,
 
    PRIMARY KEY (ShopperID, fulfillmenttime),
    FOREIGN KEY (ShopperID) REFERENCES Users(UserID)

);

CREATE TABLE IF NOT EXISTS Items(
    UPC TEXT UNIQUE NOT NULL,
    BusinessID TEXT UNIQUE NOT NULL,
    name TEXT UNIQUE NOT NULL,
    price REAL NOT NULL,
    PRIMARY KEY (UPC, BusinessID),
    FOREIGN KEY (BusinessID) REFERENCES BusinessInfo(BusinessID)

);



INSERT INTO Users (User ID, password, firstName, lastName) 
VALUES ('101', 'password', 'John', 'Doe');

INSERT INTO BusinessInfo (BusinessID, addressprimary, addresssecondary, city, state, zip_code) 
VALUES ('B001', '123 Main St', 'Suite 100', 'Anytown', 'CA', '90210');

INSERT INTO Items (UPC, BusinessID, name, price) 
VALUES ('UPC001', 'B001', 'Apple', 19.99);

INSERT INTO JobBoard (ShopperID, fulfillmenttime) 
VALUES ('101', '2023-10-01 12:00:00');

INSERT INTO Orders (OrderID, UserID, BusinessID, ShopperID, Status, fulfillmentTime, addressprimary, addresssecondary, city, state, zip_code) 
VALUES ('001', '101', 'B001', '101', 'Pending', 30, '123 Main St', 'Suite 100', 'Anytown', 'CA', '90210');

INSERT INTO OrderItems (OrderID, UPC, quantity) 
VALUES ('001', 'UPC001', 2);
