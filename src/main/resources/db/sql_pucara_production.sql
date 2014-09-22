/* Create database. */
DROP DATABASE IF EXISTS pucaradb;

CREATE DATABASE pucaradb;

USE pucaradb;

/* Grant all privileges to the root user. */
GRANT ALL ON pucaradb.* TO 'root'@'localhost';

/* Create 'category' table. */
DROP TABLE IF EXISTS category;

CREATE TABLE category (
	`id` SMALLINT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(20) NOT NULL UNIQUE, 
	`description` VARCHAR(50) NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE category AUTO_INCREMENT = 100;

/* Create 'product' table. */

DROP TABLE IF EXISTS product;

CREATE TABLE product (
	`barcode` VARCHAR(15) NOT NULL,
	`description` VARCHAR(50) NOT NULL UNIQUE,
	`initialcost` DOUBLE NOT NULL,
	`finalcost` DOUBLE NOT NULL,
	`percentage` DOUBLE NOT NULL,
	`date` DATETIME NOT NULL,
	`stock` SMALLINT DEFAULT '0' NOT NULL,
	`minstock` SMALLINT DEFAULT '0' NOT NULL,
	`categoryid` SMALLINT NOT NULL,
	`bypercentage` TINYINT(1) NOT NULL,

	PRIMARY KEY (`barcode`)
);

ALTER TABLE `product`
  ADD CONSTRAINT `product_category_id` FOREIGN KEY (`categoryid`) REFERENCES `category` (`id`);

CREATE INDEX product_barcode_index ON product (barcode);
CREATE INDEX product_description_index ON product (description);

/* Create 'supplier' table. */

DROP TABLE IF EXISTS supplier;

CREATE TABLE supplier (
	`id` SMALLINT NOT NULL AUTO_INCREMENT,
	`description` VARCHAR(50) NOT NULL UNIQUE,
	`address` VARCHAR(30) NULL,
	`phone` VARCHAR(20) NULL,

	PRIMARY KEY (`id`)
);

ALTER TABLE supplier AUTO_INCREMENT = 1000;

/* Create 'provides' table. */

DROP TABLE IF EXISTS provides;

CREATE TABLE provides (
	`barcode` VARCHAR(15) NOT NULL,
	`supplier_id` SMALLINT NOT NULL,

	PRIMARY KEY (`barcode`, `supplier_id`)
);

ALTER TABLE `provides`
  ADD CONSTRAINT `barcode_fk` FOREIGN KEY (`barcode`) REFERENCES `product` (`barcode`);

ALTER TABLE `provides`
  ADD CONSTRAINT `supplier_id_fk` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`);

/* Create 'sale' table. */

DROP TABLE IF EXISTS sale;

CREATE TABLE sale (
	`id` INT NOT NULL AUTO_INCREMENT,
	`date` DATETIME NOT NULL,
	`gain` DOUBLE NOT NULL,

	PRIMARY KEY (`id`)
);

ALTER TABLE sale AUTO_INCREMENT = 1000;

CREATE INDEX sale_data_index ON sale (date);

/* Create 'sale_detail' table. */

DROP TABLE IF EXISTS sale_detail;

CREATE TABLE sale_detail (
	`id` INT NOT NULL AUTO_INCREMENT,
	`number_of_products` SMALLINT NOT NULL,

	PRIMARY KEY (`id`)
);

ALTER TABLE sale_detail AUTO_INCREMENT = 100;

/* Create 'x_sale_sale_detail' table. */

DROP TABLE IF EXISTS x_sale_sale_detail;

CREATE TABLE x_sale_sale_detail (
	`sale_id` INT NOT NULL,
	`sale_detail_id` INT NOT NULL UNIQUE,

	PRIMARY KEY (`sale_id`, `sale_detail_id`)
);

ALTER TABLE `x_sale_sale_detail`
  ADD CONSTRAINT `x_sale_id` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`id`);

ALTER TABLE `x_sale_sale_detail`
  ADD CONSTRAINT `x_sale_detail_id` FOREIGN KEY (`sale_detail_id`) REFERENCES `sale_detail` (`id`);

/* Create 'x_sale_sale_detail_product' table. */

DROP TABLE IF EXISTS x_sale_sale_detail_product;

CREATE TABLE x_sale_sale_detail_product (
	`sale_id` INT NOT NULL,
	`sale_detail_id` INT NOT NULL,
	`barcode` VARCHAR(15) NOT NULL,
	`count` SMALLINT NOT NULL,

	PRIMARY KEY (`sale_id`, `sale_detail_id`, `barcode`)
);

ALTER TABLE `x_sale_sale_detail_product`
  ADD CONSTRAINT `x_sale_sale_detail_fk_si` FOREIGN KEY (`sale_id`) REFERENCES `x_sale_sale_detail` (`sale_id`);

ALTER TABLE `x_sale_sale_detail_product`
  ADD CONSTRAINT `x_sale_sale_detail_fk2_ssd` FOREIGN KEY (`sale_detail_id`) REFERENCES `x_sale_sale_detail` (`sale_detail_id`);

ALTER TABLE `x_sale_sale_detail_product`
  ADD CONSTRAINT `product_sale_fk` FOREIGN KEY (`barcode`) REFERENCES `product` (`barcode`);

/* Create 'purchase' table. */

DROP TABLE IF EXISTS purchase;

CREATE TABLE purchase (
	`id` INT NOT NULL AUTO_INCREMENT,
	`description` VARCHAR(50) NOT NULL,
	`date` DATETIME NOT NULL,
	`expense` DOUBLE NOT NULL,

	PRIMARY KEY (`id`)
);

ALTER TABLE purchase AUTO_INCREMENT = 1000;

CREATE INDEX purchase_date_index ON purchase (date);

/* Create 'purchase_detail' table. */

DROP TABLE IF EXISTS purchase_detail;

CREATE TABLE purchase_detail (
	`id` INT NOT NULL AUTO_INCREMENT,
	`number_of_products` SMALLINT NOT NULL,

	PRIMARY KEY (`id`)
);

ALTER TABLE purchase_detail AUTO_INCREMENT = 100;

/* Create 'x_purchase_purchase_detail' table. */

DROP TABLE IF EXISTS x_purchase_purchase_detail;

CREATE TABLE x_purchase_purchase_detail (
	`purchase_id` INT NOT NULL,
	`purchase_detail_id` INT NOT NULL UNIQUE,

	PRIMARY KEY (`purchase_id`, `purchase_detail_id`)
);

ALTER TABLE `x_purchase_purchase_detail`
  ADD CONSTRAINT `x_purchase_id` FOREIGN KEY (`purchase_id`) REFERENCES `purchase` (`id`);

ALTER TABLE `x_purchase_purchase_detail`
  ADD CONSTRAINT `x_purchase_detail_id` FOREIGN KEY (`purchase_detail_id`) REFERENCES `purchase_detail` (`id`);

/* Create 'x_purchase_purchase_detail_product' table. */

DROP TABLE IF EXISTS x_purchase_purchase_detail_product;

CREATE TABLE x_purchase_purchase_detail_product (
	`purchase_id` INT NOT NULL,
	`purchase_detail_id` INT NOT NULL,
	`barcode` VARCHAR(15) NOT NULL,
	`count` SMALLINT NOT NULL,

	PRIMARY KEY (`purchase_id`, `purchase_detail_id`, `barcode`)
);

ALTER TABLE `x_purchase_purchase_detail_product` ADD CONSTRAINT `x_purchase_purchase_detail_fk1` FOREIGN KEY (`purchase_id`) 
REFERENCES `x_purchase_purchase_detail` (`purchase_id`);

ALTER TABLE `x_purchase_purchase_detail_product` ADD CONSTRAINT `x_purchase_purchase_detail_fk2` FOREIGN KEY (`purchase_detail_id`) 
REFERENCES `x_purchase_purchase_detail` (`purchase_detail_id`);

ALTER TABLE `x_purchase_purchase_detail_product`
  ADD CONSTRAINT `product_purchase_fk` FOREIGN KEY (`barcode`) REFERENCES `product` (`barcode`);


/* Populate 'category' table. */
INSERT INTO category (`name`, `description`) VALUES ('almacén', 'descripción de categoría');
INSERT INTO category (`name`, `description`) VALUES ('kiosco', 'descripción de categoría');
INSERT INTO category (`name`, `description`) VALUES ('juguetería', 'descripción de categoría');
INSERT INTO category (`name`, `description`) VALUES ('librería', 'descripción de categoría');


/* Create views */
CREATE VIEW product_view AS
SELECT barcode, description, stock, minstock, finalcost FROM product;

CREATE VIEW daily_report_view AS
select SUM(s.gain) AS gain, SUM(sd.number_of_products) AS count from x_sale_sale_detail xssd inner join sale s on (xssd.sale_id = s.id) inner join sale_detail sd on (sd.id = xssd.sale_detail_id) where DATE(s.date) = DATE(NOW());

CREATE VIEW daily_purchase_report_view AS
select p.description as description, p.expense as expense, p.date as date from purchase p where date(p.date) = curdate();
