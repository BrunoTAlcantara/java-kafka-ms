CREATE STREAM IF NOT EXISTS orders (
    orderId VARCHAR KEY,
    orderValue DOUBLE,
    channel VARCHAR,
    paymentStatus VARCHAR,
    updateAt VARCHAR,
    createdAt VARCHAR
) WITH (
    kafka_topic='order-events',
    value_format='JSON',
    partitions=1,
    replicas=1
);


CREATE TABLE orders_table (
  orderId VARCHAR PRIMARY KEY,
  orderValue DOUBLE,
  channel VARCHAR,
  paymentStatus VARCHAR,
  updateAt VARCHAR,
  createdAt VARCHAR
) WITH (
  kafka_topic='order-events',
  value_format='JSON'
);

CREATE TABLE orders_table AS
SELECT
    orderId,
    LATEST_BY_OFFSET(orderValue) AS orderValue,
    LATEST_BY_OFFSET(channel) AS channel,
    LATEST_BY_OFFSET(paymentStatus) AS paymentStatus,
    LATEST_BY_OFFSET(updateAt) AS updateAt,
    LATEST_BY_OFFSET(createdAt) AS createdAt
FROM orders
GROUP BY orderId;


DROP STREAM IF EXISTS orders;

DROP TABLE IF EXISTS orders_table;
DROP TABLE IF EXISTS oms_table;

CREATE TABLE oms_table AS
SELECT * FROM orders_table;


