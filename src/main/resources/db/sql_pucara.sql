/* Create database. */
DROP DATABASE IF EXISTS pucaratest;

CREATE DATABASE pucaratest;

USE pucaratest;

/* Grant all privileges to the root user. */
GRANT ALL ON pucaratest.* TO 'root'@'localhost';

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
	`cost` DOUBLE NOT NULL,
	`percentage` SMALLINT NOT NULL,
	`date` DATETIME NOT NULL,
	`stock` SMALLINT DEFAULT '0' NOT NULL,
	`minstock` SMALLINT DEFAULT '0' NOT NULL,
	`categoryid` SMALLINT NOT NULL,

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

/* Populate 'product' table. */
INSERT INTO product (`barcode`, `description`, `cost`, `percentage`, `date`, `stock`, `minstock`, `categoryid`) VALUES ('7790040994904', 'chocolate blanco arcor 30gr', '9.25', 30, NOW(), 1000, 5, 101);
INSERT INTO product (`barcode`, `description`, `cost`, `percentage`, `date`, `stock`, `minstock`, `categoryid`) VALUES ('7792216994357', 'sal dos anclas 200gr', '7.50', 20, '2014-01-22 12:15:11', 1000, 5, 100);
INSERT INTO product (`barcode`, `description`, `cost`, `percentage`, `date`, `stock`, `minstock`, `categoryid`) VALUES ('7792218734238', 'alcohol en gel moscu 50ml', '4.00', 35, '2014-01-12 10:10:08', 1000, 5, 100);
INSERT INTO product (`barcode`, `description`, `cost`, `percentage`, `date`, `stock`, `minstock`, `categoryid`) VALUES ('7798152010109', 'block a5 80 hojas 210x148', '3.00', 35, '2014-01-12 10:10:08', 1000, 5, 103);
INSERT INTO product (`barcode`, `description`, `cost`, `percentage`, `date`, `stock`, `minstock`, `categoryid`) VALUES ('7798010921646', 'cuaderno maraton 42 hojas rayadas', '5.5', 35, '2014-01-12 10:10:08', 1000, 5, 103);

/* Populate 'supplier' table. */
INSERT INTO supplier (`description`, `address`, `phone`) VALUES ('distribuidora los gordos', 'puan 2342', '2281422343');
INSERT INTO supplier (`description`, `address`, `phone`) VALUES ('distribuidora barderrama', 'av. colon 234', '2281412312');
INSERT INTO supplier (`description`, `address`, `phone`) VALUES ('distribuidora quilmes', 'av. piazza 1337', '2281429300');

/* Populate 'provides' table. */
INSERT INTO provides (`barcode`, `supplier_id`) VALUES ('7790040994904', 1001);
INSERT INTO provides (`barcode`, `supplier_id`) VALUES ('7792216994357', 1001);
INSERT INTO provides (`barcode`, `supplier_id`) VALUES ('7792218734238', 1002);
INSERT INTO provides (`barcode`, `supplier_id`) VALUES ('7792218734238', 1000);


/* Populate 'sale' table. */
INSERT INTO sale (`date`,`gain`) VALUES (NOW(), '23.70');
INSERT INTO sale (`date`,`gain`) VALUES (NOW(), '13.00');
INSERT INTO sale (`date`,`gain`) VALUES (NOW(), '31.50');

/* Populate 'sale_detail' table. */
INSERT INTO sale_detail (`number_of_products`) VALUES (2);
INSERT INTO sale_detail (`number_of_products`) VALUES (8);
INSERT INTO sale_detail (`number_of_products`) VALUES (5);

/* Populate 'x_sale_sale_detail' table. */
INSERT INTO x_sale_sale_detail (`sale_id`, `sale_detail_id`) VALUES (1000, 100);
INSERT INTO x_sale_sale_detail (`sale_id`, `sale_detail_id`) VALUES (1001, 101);
INSERT INTO x_sale_sale_detail (`sale_id`, `sale_detail_id`) VALUES (1002, 102);

/* Populate 'x_sale_sale_detail_product' table. */
INSERT INTO x_sale_sale_detail_product (`sale_id`, `sale_detail_id`, `barcode`, `count`) VALUES (1000, 100, '7790040994904', 1);
INSERT INTO x_sale_sale_detail_product (`sale_id`, `sale_detail_id`, `barcode`, `count`) VALUES (1000, 100, '7792216994357', 1);
INSERT INTO x_sale_sale_detail_product (`sale_id`, `sale_detail_id`, `barcode`, `count`) VALUES (1001, 101, '7790040994904', 1);
INSERT INTO x_sale_sale_detail_product (`sale_id`, `sale_detail_id`, `barcode`, `count`) VALUES (1001, 101, '7792216994357', 4);
INSERT INTO x_sale_sale_detail_product (`sale_id`, `sale_detail_id`, `barcode`, `count`) VALUES (1001, 101, '7792218734238', 3);
INSERT INTO x_sale_sale_detail_product (`sale_id`, `sale_detail_id`, `barcode`, `count`) VALUES (1002, 102, '7792218734238', 5);


/* Populate 'purchase' table. */
INSERT INTO purchase (`description`, `date`, `expense`) VALUES ('compra los chinos', NOW(), '273.70');
INSERT INTO purchase (`description`, `date`, `expense`) VALUES ('pago haberes xx', NOW(), '130.00');
INSERT INTO purchase (`description`, `date`, `expense`) VALUES ('boleta de luz mar 2014', NOW(), '431.50');

/* Populate 'purchase_detail' table. */
-- INSERT INTO purchase_detail (`number_of_products`) VALUES (14);
-- INSERT INTO purchase_detail (`number_of_products`) VALUES (41);
-- INSERT INTO purchase_detail (`number_of_products`) VALUES (15);

/* Populate 'x_purchase_purchase_detail' table. */
-- INSERT INTO x_purchase_purchase_detail (`purchase_id`, `purchase_detail_id`) VALUES (1000, 100);
-- INSERT INTO x_purchase_purchase_detail (`purchase_id`, `purchase_detail_id`) VALUES (1001, 101);
-- INSERT INTO x_purchase_purchase_detail (`purchase_id`, `purchase_detail_id`) VALUES (1002, 102);

/* Populate 'x_purchase_purchase_detail_product' table. */
-- INSERT INTO x_purchase_purchase_detail_product (`purchase_id`, `purchase_detail_id`, `barcode`, `count`) VALUES (1000, 100, '7790040994904', 4);
-- INSERT INTO x_purchase_purchase_detail_product (`purchase_id`, `purchase_detail_id`, `barcode`, `count`) VALUES (1000, 100, '7792216994357', 10);
-- INSERT INTO x_purchase_purchase_detail_product (`purchase_id`, `purchase_detail_id`, `barcode`, `count`) VALUES (1001, 101, '7790040994904', 5);
-- INSERT INTO x_purchase_purchase_detail_product (`purchase_id`, `purchase_detail_id`, `barcode`, `count`) VALUES (1001, 101, '7792216994357', 22);
-- INSERT INTO x_purchase_purchase_detail_product (`purchase_id`, `purchase_detail_id`, `barcode`, `count`) VALUES (1001, 101, '7792218734238', 14);
-- INSERT INTO x_purchase_purchase_detail_product (`purchase_id`, `purchase_detail_id`, `barcode`, `count`) VALUES (1002, 102, '7792218734238', 15);

/* Create views */
CREATE VIEW product_view AS
SELECT barcode, description, stock, minstock, (cost + cost * percentage/100) AS final_cost FROM product;

CREATE VIEW daily_report_view AS
select SUM(s.gain) AS gain, SUM(sd.number_of_products) AS count from x_sale_sale_detail xssd inner join sale s on (xssd.sale_id = s.id) inner join sale_detail sd on (sd.id = xssd.sale_detail_id) where DATE(s.date) = DATE(NOW());

CREATE VIEW daily_purchase_report_view AS
select p.description as description, p.expense as expense, p.date as date from purchase p where date(p.date) = curdate();

/* Export Table to CSV */
-- SELECT * INTO OUTFILE '/tmp/products.csv'
-- FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
-- LINES TERMINATED BY '\n'
-- FROM product;

/* Import From CSV */
-- LOAD DATA INFILE '/tmp/products.csv' INTO TABLE product
-- FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
-- LINES TERMINATED BY '\n';
