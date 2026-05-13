CREATE KEYSPACE IF NOT EXISTS test_container_keyspace
            WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE test_container_keyspace;

CREATE TABLE IF NOT EXISTS users
(
    id   uuid PRIMARY KEY,
    name text
);