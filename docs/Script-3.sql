
select * from catalog.catalog_products

select * from orders.orders

select * from orders.order_items

select * from kafka.domain_event_outbox

-- /home/wolf/snap/dbeaver-ce/549/.local/share/DBeaverData/workspace6/General/Scripts/Script-3.sql

select
    id,
    sku,
    name,
    price_amount,
    price_currency,
    status,
    created_at,
    updated_at
from catalog.catalog_products
where status = 'ACTIVE'
order by name



