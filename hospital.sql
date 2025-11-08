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
-- Table structure for table `Atendimento`
--

DROP TABLE IF EXISTS `Atendimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Atendimento` (
  `idAtendimento` int NOT NULL AUTO_INCREMENT,
  `dataAtendimento` datetime DEFAULT NULL,
  `tipo` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_idPaciente` int DEFAULT NULL,
  `fk_idEnfermeiro` int DEFAULT NULL,
  PRIMARY KEY (`idAtendimento`),
  KEY `fk_atend_paciente` (`fk_idPaciente`),
  KEY `fk_atend_enfer` (`fk_idEnfermeiro`),
  CONSTRAINT `fk_atend_enfer` FOREIGN KEY (`fk_idEnfermeiro`) REFERENCES `Enfermeiro` (`fk_idFuncionarioEnfermeiro`),
  CONSTRAINT `fk_atend_paciente` FOREIGN KEY (`fk_idPaciente`) REFERENCES `Paciente` (`idPaciente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Atendimento`
--

LOCK TABLES `Atendimento` WRITE;
/*!40000 ALTER TABLE `Atendimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `Atendimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Consulta`
--

DROP TABLE IF EXISTS `Consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Consulta` (
  `idConsulta` int NOT NULL AUTO_INCREMENT,
  `dataConsulta` date DEFAULT NULL,
  `fk_idPaciente` int DEFAULT NULL,
  `fk_idMedico` int DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idConsulta`),
  KEY `fk_consult_paciente` (`fk_idPaciente`),
  KEY `fk_consult_medico` (`fk_idMedico`),
  CONSTRAINT `fk_consult_medico` FOREIGN KEY (`fk_idMedico`) REFERENCES `Medico` (`fk_idFuncionarioMedico`),
  CONSTRAINT `fk_consult_paciente` FOREIGN KEY (`fk_idPaciente`) REFERENCES `Paciente` (`idPaciente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Consulta`
--

LOCK TABLES `Consulta` WRITE;
/*!40000 ALTER TABLE `Consulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `Consulta` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`idDepartamento`)
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
-- Table structure for table `Enfermeiro`
--

DROP TABLE IF EXISTS `Enfermeiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Enfermeiro` (
  `numeroCoren` int NOT NULL,
  `fk_idFuncionarioEnfermeiro` int NOT NULL,
  PRIMARY KEY (`fk_idFuncionarioEnfermeiro`),
  UNIQUE KEY `numeroCoren` (`numeroCoren`),
  CONSTRAINT `fk_enfer_func` FOREIGN KEY (`fk_idFuncionarioEnfermeiro`) REFERENCES `Funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Enfermeiro`
--

LOCK TABLES `Enfermeiro` WRITE;
/*!40000 ALTER TABLE `Enfermeiro` DISABLE KEYS */;
/*!40000 ALTER TABLE `Enfermeiro` ENABLE KEYS */;
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
-- Table structure for table `Internacao`
--

DROP TABLE IF EXISTS `Internacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Internacao` (
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
  CONSTRAINT `fk_intern_medico` FOREIGN KEY (`fk_idMedico`) REFERENCES `Medico` (`fk_idFuncionarioMedico`),
  CONSTRAINT `fk_intern_paciente` FOREIGN KEY (`fk_idPaciente`) REFERENCES `Paciente` (`idPaciente`),
  CONSTRAINT `fk_intern_sala` FOREIGN KEY (`fk_idSala`) REFERENCES `Sala` (`idSala`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Internacao`
--

LOCK TABLES `Internacao` WRITE;
/*!40000 ALTER TABLE `Internacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `Internacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Medico`
--

DROP TABLE IF EXISTS `Medico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Medico` (
  `medico_CRM` int NOT NULL,
  `medico_especialidade` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_idFuncionarioMedico` int NOT NULL,
  `fk_idDepartamento` int DEFAULT NULL,
  PRIMARY KEY (`fk_idFuncionarioMedico`),
  UNIQUE KEY `medico_CRM` (`medico_CRM`),
  KEY `fk_medico_departamento` (`fk_idDepartamento`),
  CONSTRAINT `fk_medico_departamento` FOREIGN KEY (`fk_idDepartamento`) REFERENCES `Departamento` (`idDepartamento`),
  CONSTRAINT `fk_medico_funcionario` FOREIGN KEY (`fk_idFuncionarioMedico`) REFERENCES `Funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Medico`
--

LOCK TABLES `Medico` WRITE;
/*!40000 ALTER TABLE `Medico` DISABLE KEYS */;
/*!40000 ALTER TABLE `Medico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Paciente`
--

DROP TABLE IF EXISTS `Paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Paciente` (
  `idPaciente` int NOT NULL AUTO_INCREMENT,
  `nomePaciente` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pacienteCPF` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dataNascimento` date DEFAULT NULL,
  PRIMARY KEY (`idPaciente`),
  UNIQUE KEY `pacienteCPF` (`pacienteCPF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Paciente`
--

LOCK TABLES `Paciente` WRITE;
/*!40000 ALTER TABLE `Paciente` DISABLE KEYS */;
/*!40000 ALTER TABLE `Paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Sala`
--

DROP TABLE IF EXISTS `Sala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Sala` (
  `idSala` int NOT NULL AUTO_INCREMENT,
  `numeroSala` int DEFAULT NULL,
  `fk_idDepartamento` int DEFAULT NULL,
  PRIMARY KEY (`idSala`),
  KEY `fk_sala_depart` (`fk_idDepartamento`),
  CONSTRAINT `fk_sala_depart` FOREIGN KEY (`fk_idDepartamento`) REFERENCES `Departamento` (`idDepartamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Sala`
--

LOCK TABLES `Sala` WRITE;
/*!40000 ALTER TABLE `Sala` DISABLE KEYS */;
/*!40000 ALTER TABLE `Sala` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-08 18:49:52
