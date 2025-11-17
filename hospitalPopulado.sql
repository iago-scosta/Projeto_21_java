CREATE DATABASE  IF NOT EXISTS `hospital` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hospital`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hospital
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `atendimento`
--

DROP TABLE IF EXISTS `atendimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `atendimento` (
  `idAtendimento` int NOT NULL AUTO_INCREMENT,
  `dataAtendimento` datetime DEFAULT NULL,
  `tipo` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_idPaciente` int DEFAULT NULL,
  `fk_idEnfermeiro` int DEFAULT NULL,
  PRIMARY KEY (`idAtendimento`),
  KEY `fk_atend_paciente` (`fk_idPaciente`),
  KEY `fk_atend_enfer` (`fk_idEnfermeiro`),
  CONSTRAINT `fk_atend_enfer` FOREIGN KEY (`fk_idEnfermeiro`) REFERENCES `enfermeiro` (`fk_idFuncionarioEnfermeiro`),
  CONSTRAINT `fk_atend_paciente` FOREIGN KEY (`fk_idPaciente`) REFERENCES `paciente` (`idPaciente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atendimento`
--

LOCK TABLES `atendimento` WRITE;
/*!40000 ALTER TABLE `atendimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `atendimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consulta`
--

DROP TABLE IF EXISTS `consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consulta` (
  `idConsulta` int NOT NULL AUTO_INCREMENT,
  `dataConsulta` date DEFAULT NULL,
  `fk_idPaciente` int DEFAULT NULL,
  `fk_idMedico` int DEFAULT NULL,
  `descricao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idConsulta`),
  KEY `fk_consult_paciente` (`fk_idPaciente`),
  KEY `fk_consult_medico` (`fk_idMedico`),
  CONSTRAINT `fk_consult_medico` FOREIGN KEY (`fk_idMedico`) REFERENCES `medico` (`fk_idFuncionarioMedico`),
  CONSTRAINT `fk_consult_paciente` FOREIGN KEY (`fk_idPaciente`) REFERENCES `paciente` (`idPaciente`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consulta`
--

LOCK TABLES `consulta` WRITE;
/*!40000 ALTER TABLE `consulta` DISABLE KEYS */;
INSERT INTO `consulta` VALUES (1,'2025-10-20',1,1,'Consulta de rotina');
/*!40000 ALTER TABLE `consulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamento` (
  `idDepartamento` int NOT NULL AUTO_INCREMENT,
  `nomeDep` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `depEspecialidade` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idDepartamento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
INSERT INTO `departamento` VALUES (1,'Cardiologia','Coração'),(2,'Pediatria','Crianças');
/*!40000 ALTER TABLE `departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enfermeiro`
--

DROP TABLE IF EXISTS `enfermeiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enfermeiro` (
  `numeroCoren` int NOT NULL,
  `fk_idFuncionarioEnfermeiro` int NOT NULL,
  PRIMARY KEY (`fk_idFuncionarioEnfermeiro`),
  UNIQUE KEY `numeroCoren` (`numeroCoren`),
  CONSTRAINT `fk_enfer_func` FOREIGN KEY (`fk_idFuncionarioEnfermeiro`) REFERENCES `funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enfermeiro`
--

LOCK TABLES `enfermeiro` WRITE;
/*!40000 ALTER TABLE `enfermeiro` DISABLE KEYS */;
INSERT INTO `enfermeiro` VALUES (987654,2);
/*!40000 ALTER TABLE `enfermeiro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `idFuncionario` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastName` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `funcionarioCPF` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idFuncionario`),
  UNIQUE KEY `idFuncionario_UNIQUE` (`idFuncionario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionario`
--

LOCK TABLES `funcionario` WRITE;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` VALUES (1,'João','Silva','11111111111'),(2,'Maria','Souza','22222222222'),(3,'Paulo','Oliveira','33333333333');
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `internacao`
--

DROP TABLE IF EXISTS `internacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `internacao` (
  `idInternacao` int NOT NULL AUTO_INCREMENT,
  `dataEntrada` date DEFAULT NULL,
  `dataSaida` date DEFAULT NULL,
  `fk_idPaciente` int DEFAULT NULL,
  `fk_idMedico` int DEFAULT NULL,
  `fk_idSala` int DEFAULT NULL,
  PRIMARY KEY (`idInternacao`),
  KEY `fk_intern_paciente` (`fk_idPaciente`),
  KEY `fk_intern_medico` (`fk_idMedico`),
  KEY `fk_intern_sala` (`fk_idSala`),
  CONSTRAINT `fk_intern_medico` FOREIGN KEY (`fk_idMedico`) REFERENCES `medico` (`fk_idFuncionarioMedico`),
  CONSTRAINT `fk_intern_paciente` FOREIGN KEY (`fk_idPaciente`) REFERENCES `paciente` (`idPaciente`),
  CONSTRAINT `fk_intern_sala` FOREIGN KEY (`fk_idSala`) REFERENCES `sala` (`idSala`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `internacao`
--

LOCK TABLES `internacao` WRITE;
/*!40000 ALTER TABLE `internacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `internacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medico`
--

DROP TABLE IF EXISTS `medico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medico` (
  `medico_CRM` int NOT NULL,
  `medico_especialidade` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_idFuncionarioMedico` int NOT NULL,
  `fk_idDepartamento` int DEFAULT NULL,
  PRIMARY KEY (`fk_idFuncionarioMedico`),
  UNIQUE KEY `medico_CRM` (`medico_CRM`),
  KEY `fk_medico_departamento` (`fk_idDepartamento`),
  CONSTRAINT `fk_medico_departamento` FOREIGN KEY (`fk_idDepartamento`) REFERENCES `departamento` (`idDepartamento`),
  CONSTRAINT `fk_medico_funcionario` FOREIGN KEY (`fk_idFuncionarioMedico`) REFERENCES `funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medico`
--

LOCK TABLES `medico` WRITE;
/*!40000 ALTER TABLE `medico` DISABLE KEYS */;
INSERT INTO `medico` VALUES (123456,'Cardiologista',1,1);
/*!40000 ALTER TABLE `medico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `idPaciente` int NOT NULL AUTO_INCREMENT,
  `nomePaciente` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pacienteCPF` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dataNascimento` date DEFAULT NULL,
  PRIMARY KEY (`idPaciente`),
  UNIQUE KEY `pacienteCPF` (`pacienteCPF`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
INSERT INTO `paciente` VALUES (1,'Carlos Santos','55555555555','1990-05-10'),(2,'Ana Paula','66666666666','2001-01-20');
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sala`
--

DROP TABLE IF EXISTS `sala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sala` (
  `idSala` int NOT NULL AUTO_INCREMENT,
  `numeroSala` int DEFAULT NULL,
  `fk_idDepartamento` int DEFAULT NULL,
  PRIMARY KEY (`idSala`),
  KEY `fk_sala_depart` (`fk_idDepartamento`),
  CONSTRAINT `fk_sala_depart` FOREIGN KEY (`fk_idDepartamento`) REFERENCES `departamento` (`idDepartamento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sala`
--

LOCK TABLES `sala` WRITE;
/*!40000 ALTER TABLE `sala` DISABLE KEYS */;
INSERT INTO `sala` VALUES (1,101,1),(2,202,2);
/*!40000 ALTER TABLE `sala` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-16 22:23:56
