#!/bin/sh

# Wait for the selected database to be ready
if [ "$SELECTED_DB" = "postgres" ]; then
  echo "Waiting for PostgreSQL..."
  while ! nc -z db_postgres 5432; do
    sleep 1
  done
  echo "PostgreSQL started"
elif [ "$SELECTED_DB" = "mongo" ]; then
  echo "Waiting for MongoDB..."
  while ! nc -z db_mongo 27017; do
    sleep 1
  done
  echo "MongoDB started"
fi

# Start the main application
exec "$@"
