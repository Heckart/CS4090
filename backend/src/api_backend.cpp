#include "api_backend.h"
#include "db_backend.h"

#include <cppconn/prepared_statement.h>
#include <cpprest/http_listener.h>
#include <cpprest/json.h>

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

void handle_request(web::http::http_request request) {
  auto path = web::uri::decode(request.relative_uri().path());

  if (path == U("/startOrder") &&
      request.method() == web::http::methods::POST) {
    handle_start_order(request);
  } else if (path == U("/selectBusiness") &&
             request.method() == web::http::methods::POST) {
    handle_select_business(request);
  } else if (path == U("/selectDriver") &&
             request.method() == web::http::methods::POST) {
    handle_select_shopper(request);
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
          std::string orderID = insert_order_to_db(userID, addressInfo);

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

void handle_select_shopper(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          std::string orderID = utility::conversions::to_utf8string(
              jsonObject[U("orderID")].as_string());

          // TODO: validate orderID exists?

          // Fetch available shoppers from the database
          web::json::value shoppersArray = fetch_shoppers_from_db();

          // Construct response JSON
          web::json::value response;
          response[U("shoppers")] = shoppersArray;

          // Send response to the client
          request.reply(web::http::status_codes::OK, response);

        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid data."));
        }
      })
      .wait();
}
