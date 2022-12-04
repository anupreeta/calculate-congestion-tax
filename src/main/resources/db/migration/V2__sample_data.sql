-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: tax_calculator
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `city` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qsstlki7ni5ovaariyy9u8y79` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'Gothenburg');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city_vehicle_join_table`
--

DROP TABLE IF EXISTS `city_vehicle_join_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `city_vehicle_join_table` (
  `city_id` bigint NOT NULL,
  `vehicle_id` bigint NOT NULL,
  PRIMARY KEY (`city_id`,`vehicle_id`),
  KEY `FK6gxcrb31rg88jb5trenouy0kw` (`vehicle_id`),
  CONSTRAINT `FK6gxcrb31rg88jb5trenouy0kw` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKlbpov6lv39gx94t0rbnyh6ke3` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city_vehicle_join_table`
--

LOCK TABLES `city_vehicle_join_table` WRITE;
/*!40000 ALTER TABLE `city_vehicle_join_table` DISABLE KEYS */;
INSERT INTO `city_vehicle_join_table` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6);
/*!40000 ALTER TABLE `city_vehicle_join_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `holiday_calendar`
--

DROP TABLE IF EXISTS `holiday_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `holiday_calendar` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime(6) NOT NULL,
  `city_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn29qpqp4dr0k2s51vygvgle2o` (`city_id`),
  CONSTRAINT `FKn29qpqp4dr0k2s51vygvgle2o` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday_calendar`
--

LOCK TABLES `holiday_calendar` WRITE;
/*!40000 ALTER TABLE `holiday_calendar` DISABLE KEYS */;
INSERT INTO `holiday_calendar` VALUES (1,'2013-12-30 00:00:00.000000',1);
/*!40000 ALTER TABLE `holiday_calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `holiday_month_calendar`
--

DROP TABLE IF EXISTS `holiday_month_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday_month_calendar`
--

LOCK TABLES `holiday_month_calendar` WRITE;
/*!40000 ALTER TABLE `holiday_month_calendar` DISABLE KEYS */;
INSERT INTO `holiday_month_calendar` VALUES (1,0,0,0,0,0,1,0,0,0,0,0,0);
/*!40000 ALTER TABLE `holiday_month_calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preference` (
  `max_tax_per_day` int NOT NULL DEFAULT '60',
  `number_of_tax_free_days_after_holiday` int NOT NULL DEFAULT '0',
  `number_of_tax_free_days_before_holiday` int NOT NULL DEFAULT '0',
  `single_charge_interval_in_min` int NOT NULL DEFAULT '0',
  `city_entity_id` bigint NOT NULL,
  PRIMARY KEY (`city_entity_id`),
  CONSTRAINT `FKj2jmfemmo58h8sx771lbxavd9` FOREIGN KEY (`city_entity_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` VALUES (60,0,1,60,1);
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tariff`
--

DROP TABLE IF EXISTS `tariff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tariff` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `charge` decimal(19,2) NOT NULL,
  `from_time` time NOT NULL,
  `to_time` time NOT NULL,
  `city_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfw2gmot2dl3snaf8icecfecau` (`city_id`),
  CONSTRAINT `FKfw2gmot2dl3snaf8icecfecau` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tariff`
--

LOCK TABLES `tariff` WRITE;
/*!40000 ALTER TABLE `tariff` DISABLE KEYS */;
INSERT INTO `tariff` VALUES (1,13.00,'15:00:00','15:29:59',1),(2,13.00,'06:30:00','06:59:59',1),(3,13.00,'08:00:00','08:29:59',1),(4,13.00,'17:00:00','17:59:59',1),(5,8.00,'18:00:00','18:29:59',1),(6,0.00,'00:00:00','05:59:59',1),(7,8.00,'06:00:00','06:29:59',1),(8,8.00,'08:30:00','14:59:59',1),(9,18.00,'07:00:00','07:59:59',1),(10,0.00,'18:30:00','23:59:59',1),(11,18.00,'15:30:00','16:59:59',1);
/*!40000 ALTER TABLE `tariff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k7ifbpeh918n2gxjtk7vutv40` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (2,'Bus'),(7,'Car'),(3,'Diplomat'),(1,'Emergency'),(6,'Foreign'),(5,'Military'),(8,'Motorbike'),(4,'Motorcycle');
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_calendar`
--

DROP TABLE IF EXISTS `working_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_calendar`
--

LOCK TABLES `working_calendar` WRITE;
/*!40000 ALTER TABLE `working_calendar` DISABLE KEYS */;
INSERT INTO `working_calendar` VALUES (1,1,1,0,0,1,1,1);
/*!40000 ALTER TABLE `working_calendar` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-04  10:44:49