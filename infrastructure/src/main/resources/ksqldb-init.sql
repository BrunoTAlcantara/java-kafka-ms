-- Create the source stream (only if not exists - erros serão ignorados)

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



-- Create the table from the stream
-- Nota: CREATE TABLE AS SELECT não suporta IF NOT EXISTS
-- O script vai ignorar erros se a tabela já existir

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



-- Create a queryable table from the orders_table

CREATE TABLE queryable_orders_table AS

SELECT * FROM orders_table;



-- Create the final table for queries

CREATE TABLE oms_table AS

SELECT * FROM queryable_orders_table;
