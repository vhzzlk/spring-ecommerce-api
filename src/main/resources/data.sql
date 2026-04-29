-- Mockaroo-style mock data (SQL) for spring-ecommerce-api
-- Save as mock_data/mockaroo_mock_data.sql and import into your MySQL database
-- Assumes tables use binary UUIDs (BINARY(16)) and MySQL 8+ (UUID_TO_BIN function available)
-- Replace database/schema name if needed and run:
--   mysql -u root -p avncommerce < mock_data/mockaroo_mock_data.sql

-- -----------------------------
-- Users (tb_user)
-- -----------------------------
INSERT INTO tb_user (id, name, email, password, role, phone) VALUES
  (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 'Alice Silva', 'alice@example.com', '$2a$10$7q6Fh1EwqQ9WgZp0X1Y6euVq9f1r8Zk9m1YkQ6b7hN2p3s4t5uV2', 'USER', '+5511999000111'),
  (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 'Bruno Costa', 'bruno@example.com', '$2a$10$7q6Fh1EwqQ9WgZp0X1Y6euVq9f1r8Zk9m1YkQ6b7hN2p3s4t5uV2', 'ADMIN', '+5511999000222');

-- NOTE: the password values above are example bcrypt hashes for the plain password "password".
-- If you prefer to create users via the API (so Spring will encode passwords automatically), remove tb_user inserts and use the /auth/register endpoint.

-- -----------------------------
-- Categories
-- -----------------------------
INSERT INTO category (id, name) VALUES
  (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 'Electronics'),
  (UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), 'Books'),
  (UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), 'Home');

-- -----------------------------
-- Products
-- -----------------------------
INSERT INTO product (id, name, description, price, image_url) VALUES
  (UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), 'Smartphone X', 'Smartphone X with 6.5" display and 128GB', 1299.90, 'https://example.com/images/smartphone.jpg'),
  (UUID_TO_BIN('77777777-7777-7777-7777-777777777777'), 'Wireless Headphones', 'Noise-cancelling over-ear headphones', 399.50, 'https://example.com/images/headphones.jpg'),
  (UUID_TO_BIN('88888888-8888-8888-8888-888888888888'), 'Eloquent JavaScript', 'A Modern Introduction to Programming', 49.90, 'https://example.com/images/book_ejs.jpg'),
  (UUID_TO_BIN('99999999-9999-9999-9999-999999999999'), 'Vacuum Cleaner', 'Bagless vacuum cleaner 1200W', 249.00, 'https://example.com/images/vacuum.jpg'),
  (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), 'Coffee Maker', '12-cup programmable coffee maker', 89.99, 'https://example.com/images/coffeemaker.jpg'),
  (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), 'Smartwatch', 'Fitness smartwatch with heart rate monitor', 199.00, 'https://example.com/images/smartwatch.jpg');

-- -----------------------------
-- Product <-> Category mapping (tb_product_category)
-- -----------------------------
INSERT INTO tb_product_category (product_id, category_id) VALUES
  (UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), UUID_TO_BIN('33333333-3333-3333-3333-333333333333')),
  (UUID_TO_BIN('77777777-7777-7777-7777-777777777777'), UUID_TO_BIN('33333333-3333-3333-3333-333333333333')),
  (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), UUID_TO_BIN('33333333-3333-3333-3333-333333333333')),
  (UUID_TO_BIN('88888888-8888-8888-8888-888888888888'), UUID_TO_BIN('44444444-4444-4444-4444-444444444444')),
  (UUID_TO_BIN('99999999-9999-9999-9999-999999999999'), UUID_TO_BIN('55555555-5555-5555-5555-555555555555')),
  (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), UUID_TO_BIN('55555555-5555-5555-5555-555555555555'));

-- -----------------------------
-- Orders (tb_order)
-- status stored as ORDINAL (0=AGUARDANDO_PAGAMENTO, 1=PAGO, 2=ENVIADO, 3=ENTREGUE, 4=CANCELADO)
-- -----------------------------
INSERT INTO tb_order (id, moment, status, user_id) VALUES
  (UUID_TO_BIN('12121212-1212-1212-1212-121212121212'), '2026-04-01', 1, UUID_TO_BIN('11111111-1111-1111-1111-111111111111')),
  (UUID_TO_BIN('13131313-1313-1313-1313-131313131313'), '2026-04-10', 0, UUID_TO_BIN('22222222-2222-2222-2222-222222222222'));

-- -----------------------------
-- Order items
-- -----------------------------
INSERT INTO order_item (id, quantity, price, order_id, product_id) VALUES
  (UUID_TO_BIN('14141414-1414-1414-1414-141414141414'), 1, 1299.90, UUID_TO_BIN('12121212-1212-1212-1212-121212121212'), UUID_TO_BIN('66666666-6666-6666-6666-666666666666')),
  (UUID_TO_BIN('15151515-1515-1515-1515-151515151515'), 2, 49.90, UUID_TO_BIN('12121212-1212-1212-1212-121212121212'), UUID_TO_BIN('88888888-8888-8888-8888-888888888888')),
  (UUID_TO_BIN('16161616-1616-1616-1616-161616161616'), 1, 199.00, UUID_TO_BIN('13131313-1313-1313-1313-131313131313'), UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'));

-- -----------------------------
-- Payments (MapsId to tb_order) — Payment.id = Order.id
-- -----------------------------
INSERT INTO payment (order_id, moment) VALUES
  (UUID_TO_BIN('12121212-1212-1212-1212-121212121212'), '2026-04-02');

-- -----------------------------
-- Quick notes
-- -----------------------------
-- 1) If your MySQL server does not support UUID_TO_BIN, replace UUID_TO_BIN('...') with UNHEX(REPLACE('...', '-', '')) or with plain UUID strings if your schema stores UUIDs as CHAR(36).
-- 2) If you need user passwords that work with Spring Security BCrypt, either seed users via the /auth/register endpoint (recommended) or copy a valid bcrypt hash into the password column.
-- 3) To import: make sure the database (schema) exists (e.g. `avncommerce`) and the ddl schema from your application has been created/migrated prior to running these inserts.

-- End of mock data

