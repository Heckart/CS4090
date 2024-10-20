#include <cpprest/http_listener.h>
#include <cpprest/json.h>

#include "api_backend.h"

int main() {
  web::http::experimental::listener::http_listener listener(
      U("http://0.0.0.0:0000/api")); // TODO: get the right address

  listener.support(web::http::methods::POST, handle_post);

  try {
    listener.open()
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
