CREATE DATABASE  IF NOT EXISTS `history_contest` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `history_contest`;
-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: history_contest
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hc_choice_question`
--

DROP TABLE IF EXISTS `hc_choice_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hc_choice_question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question` varchar(512) NOT NULL,
  `choice_a` varchar(512) DEFAULT NULL,
  `choice_b` varchar(512) NOT NULL,
  `choice_c` varchar(512) NOT NULL,
  `choice_d` varchar(512) DEFAULT NULL,
  `answer` tinyint NOT NULL COMMENT '从0开始',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hc_choice_question`
--

LOCK TABLES `hc_choice_question` WRITE;
/*!40000 ALTER TABLE `hc_choice_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `hc_choice_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hc_department`
--

DROP TABLE IF EXISTS `hc_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hc_department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `prefix` varchar(16) NOT NULL,
  `name` varchar(64) NOT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hc_department`
--

LOCK TABLES `hc_department` WRITE;
/*!40000 ALTER TABLE `hc_department` DISABLE KEYS */;
INSERT INTO `hc_department` VALUES (1,'01','建筑学类','2020-09-18 17:01:16','2020-09-18 17:01:16'),(2,'G1','机械能源材料类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(3,'GS','工商管理类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(4,'G3','环境化工生物类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(5,'D1','电子信息类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(6,'D2','电子信息类(无锡)','2020-09-18 17:01:18','2020-09-18 17:01:18'),(7,'TJ','土木交通类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(8,'G2','自动化电气测控类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(9,'JS','计算机类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(10,'LK','理科实验班','2020-09-18 17:01:18','2020-09-18 17:01:18'),(11,'11','生物医学类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(12,'13','人文科学试验班','2020-09-18 17:01:18','2020-09-18 17:01:18'),(13,'14','经济学类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(14,'17','外国语类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(15,'24','艺术类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(16,'42','预防医学类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(17,'43','临床医学类','2020-09-18 17:01:18','2020-09-18 17:01:18'),(18,'61','吴健雄学院','2020-09-18 17:01:18','2020-09-18 17:01:18');
/*!40000 ALTER TABLE `hc_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hc_judge_question`
--

DROP TABLE IF EXISTS `hc_judge_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hc_judge_question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question` varchar(512) NOT NULL,
  `answer` tinyint NOT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hc_judge_question`
--

LOCK TABLES `hc_judge_question` WRITE;
/*!40000 ALTER TABLE `hc_judge_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `hc_judge_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hc_paper`
--

DROP TABLE IF EXISTS `hc_paper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hc_paper` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `choice_question` varchar(512) DEFAULT NULL,
  `choice_answer` varchar(512) DEFAULT NULL COMMENT '以","分割',
  `judge_question` varchar(512) DEFAULT NULL,
  `judge_answer` varchar(512) DEFAULT NULL,
  `score` int DEFAULT '-1' COMMENT '"-1"代表未评分',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hc_paper_uid_uindex` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hc_paper`
--

LOCK TABLES `hc_paper` WRITE;
/*!40000 ALTER TABLE `hc_paper` DISABLE KEYS */;
/*!40000 ALTER TABLE `hc_paper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hc_user`
--

DROP TABLE IF EXISTS `hc_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hc_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `sid` varchar(200) NOT NULL COMMENT '学号',
  `card_id` varchar(200) NOT NULL,
  `password` varchar(200) DEFAULT NULL COMMENT '密码/一卡通号',
  `name` varchar(200) DEFAULT NULL,
  `role` varchar(32) NOT NULL COMMENT '角色',
  `status` varchar(32) DEFAULT 'STATUS_NOT_START',
  `department` int DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hc_user`
--

LOCK TABLES `hc_user` WRITE;
/*!40000 ALTER TABLE `hc_user` DISABLE KEYS */;
INSERT INTO `hc_user` VALUES (1,'admin','admin',NULL,'admin','ROLE_ADMIN','STATUS_ALL',NULL,'2020-09-18 17:01:49','2020-09-18 17:01:56');
/*!40000 ALTER TABLE `hc_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-18 17:52:10
