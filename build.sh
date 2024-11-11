# Build script for the backend.
# Note: This requires cpprestsdk and mysql libraries to be installed
# on debian-based systems: use "sudo apt install libcpprest-dev libcpprest2.10 libmysql++-dev libmysql++3t64 libmysqlcppconn-dev libmysqlcppconn7t64"

g++ backend/src/*.cpp -o api_backend -lcpprest -lboost_system -lssl -lcrypto -lpthread -lmysqlcppconn
