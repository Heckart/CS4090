




CREATE TABLE IF NOT EXISTS Users(
    UserID TEXT UNIQUE NOT NULL,
    password CHAR(80) NOT NULL,
    address VARCHAR(255) NOT NULL,
    Median_rating(int) NOT NULL,
    PRIMARY KEY(USERID)
);
UPDATE USER SET Median_rating  = (SELECT AVG(rating) FROM review where user= review.person_reviewed)


CREATE TABLE IF NOT EXISTS Orders (
    OrderID TEXT UNIQUE NOT NULL,
    UserID TEXT NOT NULL REFERENCES Users(UserID) ON DELETE RESTRICT,
    ShopperID TEXT NOT NULL REFERENCES Users(UserID) ON DELETE RESTRICT,
    Status TEXT,
    PRIMARY KEY(OrderID)
);



CREATE TABLE IF NOT EXISTS location(

    id SERIAL PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50 NOT NULL),
    address VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20) NOT NULL


);


CREATE TABLE IF NOT EXISTS Reviews(
    OrderID TEXT UNIQUE NOT NULL REFERENCES Order(OrderID) ON DELETE RESTRICT,
    reviewerID TEXT NOT NULL REFERENCES Users(UserID) ON DELETE RESTRICT,
    Rating INTEGER NOT NULL CHECK(Rating>=1 AND Rating <=5),
    Comments TEXT,
    PRIMARY KEY (OrderId, ReviewerId)
);
