
INSERT INTO catalog.catalog_products
(
    id,
    sku,
    name,
    price_amount,
    price_currency,
    status,
    created_at,
    updated_at
)
VALUES

    (
        '11111111-1111-1111-1111-111111111111',
        'NOTEBOOK-PRO-15',
        'Notebook Pro 15',
        5999.90,
        'BRL',
        'ACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),

    (
        '22222222-2222-2222-2222-222222222222',
        'MOUSE-WL-01',
        'Wireless Mouse',
        199.90,
        'BRL',
        'ACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),

    (
        '33333333-3333-3333-3333-333333333333',
        'KEYBOARD-MECH-01',
        'Mechanical Keyboard',
        499.90,
        'BRL',
        'ACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),

    (
        '44444444-4444-4444-4444-444444444444',
        'LEGACY-PRODUCT',
        'Legacy Product',
        99.90,
        'BRL',
        'INACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );






