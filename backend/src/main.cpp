#include "api_backend.h"
#include <cpprest/http_listener.h>
#include <cpprest/json.h>
#include <iostream>

int main() {
  web::http::experimental::listener::http_listener listener(
      U("http://0.0.0.0:8080")); // on the android frontend, while being
                                 // emulated, 10.0.2.2 is an alias for the
                                 // host machine's localhost

  listener.support(web::http::methods::POST, handle_request);

  try {
    listener
        .open() // flawfinder: ignore. In a real implementation off of
                // localhost, mutexes should be used to prevent race conditions
        .then([&listener]() {
          std::cout << "Starting to listen on: " << listener.uri().to_string()
                    << std::endl;
        })
        .wait();

    while (true)
      ;
  } catch (const std::exception &e) {
    std::cerr << "Exception: " << e.what() << std::endl;
  }

  return 0;
}
