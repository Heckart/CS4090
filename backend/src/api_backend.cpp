#include "api_backend.h"

#include <cppconn/prepared_statement.h>
#include <cpprest/http_listener.h>
#include <cpprest/json.h>
#include <mysql_connection.h>
#include <mysql_driver.h>
#include <uuid/uuid.h>

#include <iostream>

/*
  enum class OrderStatus {
    ORDERING,
    CONFIMRED,
    DISPATCHED,
    DELIVERED,
    CANCELLED
  }
   */

std::string generate_uuid() {
  uuid_t uuid;
  uuid_generate(uuid);
  char uuid_str[37];
  uuid_unparse(uuid, uuid_str);
  return std::string(uuid_str);
}

void handle_request(web::http::http_request request) {
  auto path = web::uri::decode(request.relative_uri().path());

  if (path == U("/startOrder") &&
      request.method() == web::http::methods::POST) {
    handle_start_order(request);
  } else if (path == U("/selectBusiness") &&
             request.method() == web::http::methods::POST) {
    handle_select_business(request);
  } else {
    request.reply(web::http::status_codes::NotFound, U("Endpoint not found."));
  }
}

void handle_start_order(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          web::json::value addressInfo = jsonObject[U("addressInfo")];

          // Insert the order into the database and retrieve the generated
          // orderID
          std::string orderID = insert_order_to_db(userID);

          // Fetch available businesses from the database
          web::json::value businessInfo = fetch_businesses_from_db();

          // Construct response JSON
          web::json::value response;
          response[U("orderID")] = web::json::value::string(
              utility::conversions::to_string_t(orderID));
          response[U("businessInfo")] = businessInfo;

          // Send response to the client
          request.reply(web::http::status_codes::OK, response);
        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid data."));
        }
      })
      .wait();
}

void handle_select_business(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          // Extract userID, orderID, and businessID from the request JSON
          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          std::string orderID = utility::conversions::to_utf8string(
              jsonObject[U("orderID")].as_string());
          std::string businessID = utility::conversions::to_utf8string(
              jsonObject[U("businessID")].as_string());

          // TODO: validate orderID exists?

          // Fetch available items for the selected business from the database
          web::json::value itemsArray = fetch_items_from_db(businessID);

          // Construct response JSON
          web::json::value response;
          response[U("businessID")] = web::json::value::string(
              utility::conversions::to_string_t(businessID));
          response[U("items")] = itemsArray;

          // Send response to the client
          request.reply(web::http::status_codes::OK, response);
        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid data."));
        }
      })
      .wait();
}

std::string insert_order_to_db(const std::string &userID) {
  std::string orderID = generate_uuid();

  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;
    driver = sql::mysql::get_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username",
                           "password"); // TODO: get correct credentials
    conn->setSchema("api_database");    // TODO: what is this actually called?

    std::unique_ptr<sql::PreparedStatement> pstmt(conn->prepareStatement(
        "INSERT INTO Orders (orderID, userID, status) VALUES (?, ?, "
        "?)")); // the unused columns should be confgured to default to
                // NULL values

    pstmt->setString(1, orderID);
    pstmt->setString(2, userID);
    pstmt->setString(3, "ORDERING");

    pstmt->executeUpdate();

    delete conn;
  } catch (sql::SQLException &e) {
    std::cerr << "SQL Exception: " << e.what() << std::endl;
  }
  return orderID;
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
        "SELECT upc, name, price FROM items WHERE businessID = "
        "?")); // TODO: I'm totally making this up because this
               // table isnt in our drawio schema yet
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
    return itemsArray;

  } catch (sql::SQLException &e) {
    std::cerr << "SQL Eception: " << e.what() << std::endl;
  }
  return itemsArray;
}
