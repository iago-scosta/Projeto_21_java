CREATE DATABASE  IF NOT EXISTS `hospital` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hospital`;
-- MySQL dump 10.13  Distrib 9.3.0, for Linux (x86_64)
--
-- Host: localhost    Database: hospital
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `Departamento`
--

DROP TABLE IF EXISTS `Departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Departamento` (
  `idDepartamento` int NOT NULL AUTO_INCREMENT,
  `nomeDep` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `depEspecialidade` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idDepartamento`),
  UNIQUE KEY `idDepartamento_UNIQUE` (`idDepartamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Departamento`
--

LOCK TABLES `Departamento` WRITE;
/*!40000 ALTER TABLE `Departamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `Departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Funcionario`
--

DROP TABLE IF EXISTS `Funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Funcionario` (
  `idFuncionario` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastName` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `funcionarioCPF` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idFuncionario`),
  UNIQUE KEY `idFuncionario_UNIQUE` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Funcionario`
--

LOCK TABLES `Funcionario` WRITE;
/*!40000 ALTER TABLE `Funcionario` DISABLE KEYS */;
/*!40000 ALTER TABLE `Funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Medico`
--

DROP TABLE IF EXISTS `Medico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Medico` (
  `medico_CRM` int NOT NULL,
  `medico_especialiadade` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_idFuncionarioMedico` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`fk_idFuncionarioMedico`),
  UNIQUE KEY `medico_CRM_UNIQUE` (`medico_CRM`),
  UNIQUE KEY `fk_idFuncionarioMedico_UNIQUE` (`fk_idFuncionarioMedico`),
  CONSTRAINT `fk_idFuncionarioMedio` FOREIGN KEY (`fk_idFuncionarioMedico`) REFERENCES `Funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Medico`
--

LOCK TABLES `Medico` WRITE;
/*!40000 ALTER TABLE `Medico` DISABLE KEYS */;
/*!40000 ALTER TABLE `Medico` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-26 22:40:32
