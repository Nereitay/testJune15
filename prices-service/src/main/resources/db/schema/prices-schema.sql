DROP TABLE IF EXISTS prices;
CREATE TABLE prices (
    id BIGINT auto_increment primary key,
    price_list BIGINT not null,
    brand_id BIGINT not null,
    product_id BIGINT not null,
    priority INTEGER default 0,
    start_date DATETIME,
    end_date DATETIME,
    price DECIMAL(18, 2),
    curr CHAR(3),
    last_update DATETIME,
    last_update_by VARCHAR(50)
);
ALTER TABLE prices ADD INDEX brand_product_idx (brand_id, product_id);