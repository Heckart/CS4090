#include "db_backend.h"

#include <cppconn/prepared_statement.h>
#include <cpprest/http_listener.h>
#include <cpprest/json.h>
#include <mysql_connection.h>
#include <mysql_driver.h>
#include <uuid/uuid.h>

std::string generate_uuid() {
  uuid_t uuid;
  uuid_generate(uuid);
  std::array<char, 37> uuid_str;
  uuid_unparse(uuid, uuid_str.data());
  return std::string(uuid_str.data());
}

std::string insert_order_to_db(const std::string &userID,
                               const web::json::value &addressInfo) {
  std::string orderID = generate_uuid();

  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;
    driver = sql::mysql::get_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username",
                           "password"); // TODO: get correct credentials
    conn->setSchema("api_database");    // TODO: what is this actually called?

    std::unique_ptr<sql::PreparedStatement> pstmt(conn->prepareStatement(
        "INSERT INTO Orders (orderID, userID, status, addressPrimary, "
        "addressSecondary, city, state, zipCode) VALUES (?, ?, ?, ?, ?, ?, ?, "
        "?)")); // the unused columns should be confgured to default to NULL
                // values

    pstmt->setString(1, orderID);
    pstmt->setString(2, userID);
    pstmt->setString(3, "ORDERING");
    pstmt->setString(4, utility::conversions::to_utf8string(
                            addressInfo.at(U("addressPrimary")).as_string()));
    pstmt->setString(5, utility::conversions::to_utf8string(
                            addressInfo.at(U("addressSecondary")).as_string()));
    pstmt->setString(6, utility::conversions::to_utf8string(
                            addressInfo.at(U("city")).as_string()));
    pstmt->setString(7, utility::conversions::to_utf8string(
                            addressInfo.at(U("state")).as_string()));
    pstmt->setString(8, utility::conversions::to_utf8string(
                            addressInfo.at(U("zipCode")).as_string()));

    pstmt->executeUpdate();

    delete conn;
  } catch (sql::SQLException &e) {
    std::cerr << "SQL Exception: " << e.what() << std::endl;
  }
  return orderID;
}

void insert_shopper_to_order(const std::string &orderID,
                             const std::string &shopperID) {
  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;

    driver = sql::mysql::get_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username",
                           "password"); // TODO: get correct credentials
    conn->setSchema("api_database");    // TODO: what is this actually called?

    std::unique_ptr<sql::PreparedStatement> pstmt(conn->prepareStatement(
        "UPDATE Orders SET shopperID = ? WHERE orderID = ?"));

    pstmt->setString(1, shopperID);
    pstmt->setString(2, orderID);

    delete conn;

  } catch (sql::SQLException &e) {
    std::cerr << "SQL Excpetion: " << e.what() << std::endl;
  }

  return;
}

web::json::value fetch_businesses_from_db() {
  web::json::value businessArray = web::json::value::array();
  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;
    driver = sql::mysql::get_mysql_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username",
                           "password"); // TODO: get correct credentials
    conn->setSchema("api_database");    // TODO: what is this actually called?

    std::unique_ptr<sql::PreparedStatement> pstmt(
        conn->prepareStatement("SELECT * FROM BusinessInfo"));
    std::unique_ptr<sql::ResultSet> res(pstmt->executeQuery());

    int index = 0;
    while (res->next()) {
      web::json::value business;
      business[U("businessID")] = web::json::value::string(
          utility::conversions::to_string_t(res->getString("businessID")));
      web::json::value addressInfo;
      addressInfo[U("addressPrimary")] = web::json::value::string(
          utility::conversions::to_string_t(res->getString("addressPrimary")));
      addressInfo[U("addressSecondary")] =
          web::json::value::string(utility::conversions::to_string_t(
              res->getString("addressSecondary")));
      addressInfo[U("city")] = web::json::value::string(
          utility::conversions::to_string_t(res->getString("city")));
      addressInfo[U("state")] = web::json::value::string(
          utility::conversions::to_string_t(res->getString("state")));
      addressInfo[U("zipCode")] = web::json::value::string(
          utility::conversions::to_string_t(res->getString("zipCode")));
      business[U("addressInfo")] = addressInfo;

      businessArray[index++] = business;
    }

    delete conn;

  } catch (sql::SQLException &e) {
    std::cerr << "SQL Exception: " << e.what() << std::endl;
  }
  return businessArray;
}

web::json::value fetch_items_from_db(const std::string &businessID) {
  web::json::value itemsArray = web::json::value::array();

  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;
    driver = sql::mysql::get_mysql_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username",
                           "password"); // TODO: get correct credentials
    conn->setSchema("api_database");    // TODO: what is this actually called?

    std::unique_ptr<sql::PreparedStatement> pstmt(conn->prepareStatement(
        "SELECT upc, name, price FROM Items WHERE businessID = "
        "?"));
    pstmt->setString(1, businessID);
    std::unique_ptr<sql::ResultSet> res(pstmt->executeQuery());

    int index = 0;
    while (res->next()) {
      web::json::value item;
      item[U("upc")] = web::json::value::number(res->getInt64("upc"));
      item[U("name")] = web::json::value::string(
          utility::conversions::to_string_t(res->getString("name")));
      item[U("price")] = web::json::value::number(
          static_cast<double>(res->getDouble("price")));

      itemsArray[index++] = item;
    }

    delete conn;

  } catch (sql::SQLException &e) {
    std::cerr << "SQL Exception: " << e.what() << std::endl;
  }
  return itemsArray;
}

web::json::value fetch_shoppers_from_db() {
  web::json::value shoppersArray = web::json::value::array();

  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;
    driver = sql::mysql::get_mysql_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username",
                           "password"); // TODO: get correct credentials
    conn->setSchema("api_database");    // TODO: what is this actually called?

    // jobBoard has shopperID, which is a userID. Reference the Users table with
    // the shopperID/userID to grab firstName and lastName
    std::unique_ptr<sql::PreparedStatement> pstmt(conn->prepareStatement(
        "SELECT shopperID, fulfillmentTime FROM JobBoard"));
    std::unique_ptr<sql::ResultSet> res(pstmt->executeQuery());

    int index = 0;
    while (res->next()) {
      std::string shopperID = res->getString("shopperID");
      std::string fulfillmentTime =
          res->getString("fullfillmentTime"); // TODO: find out if this is
                                              // actually stored as a string

      // Query the user table to get the shopper's first and last name
      std::unique_ptr<sql::PreparedStatement> userPstmt(conn->prepareStatement(
          "SELECT firstName, lastName FROM user WHERE userID = ?"));
      userPstmt->setString(1, shopperID);
      std::unique_ptr<sql::ResultSet> userRes(userPstmt->executeQuery());

      if (userRes->next()) {
        // Populate JSON with shopper details
        web::json::value shopper;
        shopper[U("shopperID")] = web::json::value::string(
            utility::conversions::to_string_t(shopperID));
        shopper[U("firstName")] = web::json::value::string(
            utility::conversions::to_string_t(userRes->getString("firstName")));
        shopper[U("lastName")] = web::json::value::string(
            utility::conversions::to_string_t(userRes->getString("lastName")));
        shopper[U("fulfillmentTime")] = web::json::value::string(
            utility::conversions::to_string_t(fulfillmentTime));

        shoppersArray[index++] = shopper;
      }
    }

    delete conn;

  } catch (sql::SQLException &e) {
    std::cerr << "SQL Exception: " << e.what() << std::endl;
  }
  return shoppersArray;
}
