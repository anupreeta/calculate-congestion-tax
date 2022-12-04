CREATE TABLE `city` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qsstlki7ni5ovaariyy9u8y79` (`name`)
);

CREATE TABLE `holiday_calendar` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime(6) NOT NULL,
  `city_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn29qpqp4dr0k2s51vygvgle2o` (`city_id`),
  CONSTRAINT `FKn29qpqp4dr0k2s51vygvgle2o` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);
CREATE TABLE `holiday_month_calendar` (
  `city_id` bigint NOT NULL,
  `is_april` tinyint(1) NOT NULL DEFAULT '0',
  `is_august` tinyint(1) NOT NULL DEFAULT '0',
  `is_december` tinyint(1) NOT NULL DEFAULT '0',
  `is_february` tinyint(1) NOT NULL DEFAULT '0',
  `is_january` tinyint(1) NOT NULL DEFAULT '0',
  `is_july` tinyint(1) NOT NULL DEFAULT '0',
  `is_june` tinyint(1) NOT NULL DEFAULT '0',
  `is_march` tinyint(1) NOT NULL DEFAULT '0',
  `is_may` tinyint(1) NOT NULL DEFAULT '0',
  `is_november` tinyint(1) NOT NULL DEFAULT '0',
  `is_october` tinyint(1) NOT NULL DEFAULT '0',
  `is_september` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`city_id`),
  CONSTRAINT `FK9c7go6rk65tywa1qerv86c8lj` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);

CREATE TABLE `preference` (
  `max_tax_per_day` int NOT NULL DEFAULT '60',
  `number_of_tax_free_days_after_holiday` int NOT NULL DEFAULT '0',
  `number_of_tax_free_days_before_holiday` int NOT NULL DEFAULT '0',
  `single_charge_interval_in_min` int NOT NULL DEFAULT '0',
  `city_entity_id` bigint NOT NULL,
  PRIMARY KEY (`city_entity_id`),
  CONSTRAINT `FKj2jmfemmo58h8sx771lbxavd9` FOREIGN KEY (`city_entity_id`) REFERENCES `city` (`id`)
);

CREATE TABLE `tariff` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `charge` decimal(19,2) NOT NULL,
  `from_time` time NOT NULL,
  `to_time` time NOT NULL,
  `city_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfw2gmot2dl3snaf8icecfecau` (`city_id`),
  CONSTRAINT `FKfw2gmot2dl3snaf8icecfecau` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);

CREATE TABLE `vehicle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k7ifbpeh918n2gxjtk7vutv40` (`name`)
);

CREATE TABLE `working_calendar` (
  `city_id` bigint NOT NULL,
  `is_friday` tinyint(1) NOT NULL DEFAULT '1',
  `is_monday` tinyint(1) NOT NULL DEFAULT '1',
  `is_saturday` tinyint(1) NOT NULL DEFAULT '0',
  `is_sunday` tinyint(1) NOT NULL DEFAULT '0',
  `is_thursday` tinyint(1) NOT NULL DEFAULT '1',
  `is_tuesday` tinyint(1) NOT NULL DEFAULT '1',
  `is_wednesday` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`city_id`),
  CONSTRAINT `FKnog0tbl7081enk9ce33tsdlpg` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);

CREATE TABLE `city_vehicle_join_table` (
  `city_id` bigint NOT NULL,
  `vehicle_id` bigint NOT NULL,
  PRIMARY KEY (`city_id`,`vehicle_id`),
  KEY `FK6gxcrb31rg88jb5trenouy0kw` (`vehicle_id`),
  CONSTRAINT `FK6gxcrb31rg88jb5trenouy0kw` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKlbpov6lv39gx94t0rbnyh6ke3` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);
