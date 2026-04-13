#!/bin/bash
set -e

echo "=== Spring E-Commerce API - Starting ==="
echo "Java Version:"
java -version

echo ""
echo "=== Setting Environment Variables ==="
export DB_URL=${DB_URL:-jdbc:mysql://avnadmin:REMOVED_SECRET@mysql-ecommerce-vhzzlk.c.aivencloud.com:16320/defaultdb?ssl-mode=REQUIRED}
export DB_USERNAME=${DB_USERNAME:-avnadmin}
export DB_PASSWORD=${DB_PASSWORD}
export JWT_SECRET=${JWT_SECRET}
export JWT_EXPIRATION=${JWT_EXPIRATION:-3600000}
export SERVER_PORT=${PORT:-8080}

echo "Database URL: $DB_URL"
echo "Database User: $DB_USERNAME"
echo "Server Port: $SERVER_PORT"
echo ""

echo "=== Starting Application ==="
java -Dserver.port=$SERVER_PORT \
     -Dspring.datasource.url=$DB_URL \
     -Dspring.datasource.username=$DB_USERNAME \
     -Dspring.datasource.password=$DB_PASSWORD \
     -Dsecurity.jwt.secret-key=$JWT_SECRET \
     -Dsecurity.jwt.expiration-time=$JWT_EXPIRATION \
     -jar commerce-0.0.1-SNAPSHOT.jar

