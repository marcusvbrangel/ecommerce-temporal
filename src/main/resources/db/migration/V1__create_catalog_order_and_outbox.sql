
CREATE SCHEMA IF NOT EXISTS catalog;

CREATE SCHEMA IF NOT EXISTS orders;

CREATE SCHEMA IF NOT EXISTS kafka;

CREATE TABLE catalog.catalog_products
(
    id UUID PRIMARY KEY,

    sku VARCHAR(40) NOT NULL,

    name VARCHAR(200) NOT NULL,

    price_amount NUMERIC(19, 2) NOT NULL,

    price_currency CHAR(3) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,


    CONSTRAINT uk_catalog_products_sku
        UNIQUE (sku),


    CONSTRAINT ck_catalog_products_price
        CHECK (price_amount > 0),


    CONSTRAINT ck_catalog_products_status
        CHECK
            (
            status IN
            (
             'ACTIVE',
             'INACTIVE'
                )
            )
);


CREATE TABLE orders.orders
(
    id UUID PRIMARY KEY,

    customer_id UUID NOT NULL,

    customer_email VARCHAR(320) NOT NULL,

    status VARCHAR(30) NOT NULL,

    total_amount NUMERIC(19, 2) NOT NULL,

    total_currency CHAR(3) NOT NULL,

    placed_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,


    CONSTRAINT ck_orders_total
        CHECK (total_amount > 0),


    CONSTRAINT ck_orders_status
        CHECK
            (
            status IN
            (
             'PENDING',
             'PROCESSING',
             'COMPLETED',
             'CANCELLED',
             'FAILED'
                )
            )
);


CREATE TABLE orders.order_items
(
    id UUID PRIMARY KEY,

    order_id UUID NOT NULL,

    product_id UUID NOT NULL,

    product_name VARCHAR(200) NOT NULL,

    unit_price_amount NUMERIC(19, 2) NOT NULL,

    unit_price_currency CHAR(3) NOT NULL,

    quantity INTEGER NOT NULL,


    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
            REFERENCES orders.orders(id)
            ON DELETE CASCADE,


    CONSTRAINT ck_order_items_unit_price
        CHECK (unit_price_amount > 0),


    CONSTRAINT ck_order_items_quantity
        CHECK (quantity > 0),


    CONSTRAINT uk_order_items_order_product
        UNIQUE
            (
             order_id,
             product_id
                )
);


CREATE INDEX idx_order_items_order
    ON orders.order_items(order_id);


CREATE TABLE kafka.domain_event_outbox
(
    event_id UUID PRIMARY KEY,

    aggregate_type VARCHAR(100) NOT NULL,

    aggregate_id UUID NOT NULL,

    event_type VARCHAR(150) NOT NULL,

    payload JSONB NOT NULL,

    occurred_at TIMESTAMPTZ NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    attempt_count INTEGER NOT NULL DEFAULT 0,

    published_at TIMESTAMPTZ NULL,


    CONSTRAINT ck_domain_event_outbox_status
        CHECK
            (
            status IN
            (
             'PENDING',
             'PUBLISHED',
             'FAILED'
                )
            )
);







