#ifndef APIBACKEND
#define APIBACKEND

#include <cpprest/http_listener.h>
#include <cpprest/json.h>

/**
 * @brief Handles POST requests to receive user information in JSON format.
 *
 * @param The HTTP request object containing the JSON payload.
 *
 * @return Nothing.
 */
void handle_post(web::http::http_request request);

#endif
