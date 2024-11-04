#include "api_backend.h"

#include <cpprest/http_listener.h>
#include <cpprest/json.h>

#include <iostream>

void handle_get(web::http::http_request request) {
  // creating a dummy object for now
  // TODO: update this once we know whats needed
  web::json::value userInfo;
  userInfo[U("name")] = web::json::value::string(U("2% Milk"));
  userInfo[U("price")] = web::json::value::number(3);

  request.reply(web::http::status_codes::OK, userInfo);
}

void handle_post(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          // Extract data from the request body
          std::string orderID = jsonObject[U("orderID")].as_string();
          std::string userID = jsonObject[U("userID")].as_string();
          // deliveryAddress: AddressInformation class
          std::string addressPrimary =
              jsonObject[U("addressPrimary")].as_string();
          std::string addressSecondary =
              jsonObject[U("addressSecondary")].as_string();
          std::string city = jsonObject[U("city")].as_string();
          std::string state =
              jsonObject[U("state")].as_string(); // I believe this can just be
                                                  // a 2 letter string here
          std::string zipCode = jsonObject[U("zipCode")].as_string();
          // businessInfo: businessInformation class
          std::string businessName =
              jsonObject[U("businessInformation")].as_string();
          std::string businessAddress =
              jsonObject[U("businessAddress")].as_string();
          // status: OrderStatus class (enum of statuses)
          std::string status =
              jsonObject[U("status")]
                  .as_string(); // ORDERING, CONFIMRED, DISPATCHED, DELIVERED,
                                // CANCELLED
          // items: Hash map of <UPC (int), OrderItem>
          // TODO: how to hande??

          // log it...probably can delete
          std::cout << L"found orderID: " << orderID << std::endl;

          // Respond to the client
          request.reply(web::http::status_codes::OK, U("User info received."));

          // TODO: here the new data needs to be added to the database

        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Something went wrong, perhaps invalid data?"));
        }
      })
      .wait();
}
