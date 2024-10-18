#include "api_backend.h"

#include <cpprest/http_listener.h>
#include <cpprest/json.h>

#include <iostream>

void handle_post(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          // Extract data from the request body
          // TODO: find out what data needs to be rec'd and update this
          std::string name = jsonObject[U("name")].as_string();

          // log it...probably can delete
          std::cout << L"found Name: " << name << std::endl;

          // Respond to the client
          request.reply(web::http::status_codes::OK, U("User info received."));
        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Something went wrong, perhaps invalid data?"));
        }
      })
      .wait();
}
