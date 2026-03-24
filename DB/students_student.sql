CREATE DATABASE  IF NOT EXISTS `students` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `students`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: students
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_group` bigint NOT NULL,
  `student_phone_number` varchar(15) DEFAULT NULL,
  `student_second_phone_number` varchar(15) DEFAULT NULL,
  `student_middle_name` varchar(35) NOT NULL,
  `student_name` varchar(35) NOT NULL,
  `student_surname` varchar(35) NOT NULL,
  `student_email` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8riymitqbtr74n9owuod6gc9y` (`id_group`),
  CONSTRAINT `FK8riymitqbtr74n9owuod6gc9y` FOREIGN KEY (`id_group`) REFERENCES `student_group` (`id_group`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (20,1,NULL,NULL,'Русланівна','Маргарита','АЛАФІНА','marharyta.alafina@cs.khpi.edu.ua'),(21,1,'+380636449541','+380995062924','Віталіївна','Катерина','АНТИПЕНКО','antipenkokaterina84@gmail.com'),(22,1,'+380683956358',NULL,'Русланович','Артемій','БАГРЯНЦЕВ','bagryan444@gmail.com'),(23,1,NULL,NULL,'Олександрович','Віталій','БОВДУР','vitalii.bovdur@cs.khpi.edu.ua'),(24,1,'+380507802834',NULL,'Юрійович','Ярослав','БОНДАРЕНКО','bondy2526@gmail.com'),(25,1,'+380951634836',NULL,'Олександрович','Віктор','БУЛГАКОВ','viktor.bulhakov@cs.khpi.edu.ua'),(26,1,'+380507206190',NULL,'Олександрович','Михайло','ВАСИЛЬЄВ','mykhailo.vasyliev@cs.khpi.edu.ua'),(27,1,'+380966316062',NULL,'Андрійович','Володимир','ВОЇНОВ','vladimirvoinov04@gmail.com'),(28,1,NULL,NULL,'Станіславович','Артем','ГОРБАСЬ','artem.horbas@cs.khpi.edu.ua'),(29,1,NULL,NULL,'Андріївна','Поліна','ГРИГОРАЩЕНКО','polina.hryhorashchenko@cs.khpi.edu.ua'),(30,1,NULL,NULL,'Євгенович','Михайло','ЄЗІК','mykhailo.yezik@cs.khpi.edu.ua'),(31,1,NULL,NULL,'Дмитрович','Ярослав','ІВАШКІВ','yaroslav.ivashkiv@cs.khpi.edu.ua'),(32,1,NULL,NULL,'Ігорович','Тимур','ІЛЮХІН','tymur.iliukhin@cs.khpi.edu.ua'),(33,1,NULL,NULL,'Тамазович','Ніка','КАТАМАДЗЕ','nika.katamadze@cs.khpi.edu.ua'),(34,1,NULL,NULL,'Володимирович','Денис','КОВТУШЕНКО','denys.kovtushenko@cs.khpi.edu.ua'),(35,1,'+380957289233',NULL,'Олександрович','Єгор','КОНЮХОВ','egor598000@gmail.com'),(36,1,'+380682595042',NULL,'Віталійович','Олексій','КРИЖЕНКО','oleksii.kryzhenko@cs.khpi.edu.ua'),(37,1,'+380665040005',NULL,'Олегович','Дмитро','ЛЕВЧЕНКО','dmytro.levchenko@cs.khpi.edu.ua'),(38,1,'+380985238425','+380965310660','Сергійович','Максим','ЛОЗОВСЬКИЙ','lozovskiyzte@gmail.com'),(39,1,NULL,NULL,'Валерійович','Артем','ЛЮШУКОВ','artem.liushukov@cs.khpi.edu.ua'),(40,1,'+380505578648',NULL,'Михайлівна','Ольга','НЕДОСТУП','nedostupolha@gmail.com'),(41,1,NULL,NULL,'Вікторівна','Ганна','НЕЖИД','hanna.nezhyd@cs.khpi.edu.ua'),(42,1,'+380685388416','+4915154936472','Богданович','Дмитро','НІКІТЧЕНКО','nikitchenkodm@yahoo.com'),(43,1,'+380957740747',NULL,'Євгенович','Данило','ОСИПЧУК','osipchukd2019@gmail.com'),(44,1,NULL,NULL,'Сергійович','Олександр','ПАШНЬОВ','oleksandr.pashnov@cs.khpi.edu.ua'),(45,1,'+380967810802','+380975105340','Дмитрович','Ярослав','ПОСМАШНИЙ','1aver.bmx1@gmail.com'),(46,1,NULL,NULL,'Олегович','Олексій','РАКУЛЬЦЕВ','oleksii.rakultsev@cs.khpi.edu.ua'),(47,1,'+380503230070',NULL,'Олегович','Ярослав','РИБЧЕНКО','yaroslav.rybchenko@cs.khpi.edu.ua'),(48,1,'+380974652748',NULL,'Андрійович','Михайло','РОЖКОВ','mykhailo.rozhkov@cs.khpi.edu.ua'),(49,1,'+380732593909','+380994900837','Олександрівна','Єлизавета','СИЗОНЕНКО','isiklihira@gmail.com'),(50,1,NULL,NULL,'Олександрович','Денис','СЛЕСЬ','denys.sles@cs.khpi.edu.ua'),(51,1,'+380959232464',NULL,'Дмитрович','Андрій','ТАНЬКО','tankoandre13@gmail.com'),(52,1,NULL,NULL,'Олександрівна','Вікторія','ТИМОШЕНКО','viktoriia.tymoshenko@cs.khpi.edu.ua'),(53,1,'+380630733207','+48698209550','Максимович','Валерій','ХОХЛОВ','kasimovich85@gmail.com'),(54,1,NULL,NULL,'Володимирович','Ярослав','ШКАЛЕНКО','yaroslav.shkalenko@cs.khpi.edu.ua'),(55,2,'+380500382362','+380955266102','Вікторович','Юрій','БАБАРИЦЬКИЙ','yura@gmail.com'),(56,2,'+380500136169',NULL,'Дмитрович','Максим','БАРАНОВ','maksym.baranov2015@gmail.com'),(57,2,'+4915150151105','+380975013637','Олександрович','Артем','БАРЧАН','ase3.barchan@gmail.com'),(58,2,'+380666659049',NULL,'В`ячеславович','Сергій','ВИБОРНИЙ','serhii.vybornyi@cs.khpi.edu.ua'),(59,2,'+380660346917','+4915172689370','Віталійович','Богдан','ГРАБАР','austrocilindr@gmail.com'),(60,2,'+380934300181','+380665581275','Русланович','Максим','ГРЕЧКА','maksgre4ka@gmail.com'),(61,2,'+380676239511',NULL,'Ігорівна','Катерина','ГРИГОР`ЄВА','kategrigoreva2003@gmail.com'),(62,2,'+380664210975',NULL,'Миколайович','Владислав','ДОЛЖЕНКО','dolzenkovlad@gmail.com'),(63,2,'+380969207274','+34617442697','Сергійович','Єгор','ЄСИПЕНКО','egoresipenka@gmail.com'),(64,2,NULL,NULL,'Володимирівна','Васіліса','ЗОЗУЛЯ','vasilisa.zozulia@cs.khpi.edu.ua'),(65,2,'+380631168497','+380683430551','Олегович','Дмитро','ІВАНІВ','dmytro.ivaniv1@gmail.com'),(66,2,'+380952142543',NULL,'Миколайович','Артем','КОЗЛОВ','artem.kozlov@cs.khpi.edu.ua'),(67,2,'+380686374066','+380680444741','Миколайович','Давид','КОНСТАНТІНОВ','david.konstantinov13@gmail.com'),(68,2,'+380635734873','+380661155115','Геннадійович','Тимофій','КОПАЧОВ','timoshka.kopachov@gmail.com'),(69,2,'+380996504173','+380663772527','Валерійович','Богдан','КУЧМА','bogdan.kuchma2@gmail.com'),(70,2,'+380975322909',NULL,'Андрійович','Макар','МАНЬКОВ','mankovmakar@gmail.com'),(71,2,'+380502894672','+380661418020','Олександрович','Сергій','МИХАЙЛЕНКО','muhailenko.sergiy@gmail.com'),(72,2,'+380987463970',NULL,'Сергійович','Олексій','ПАВЛІЧЕНКО','aleksey99@ukr.net'),(73,2,'+380996334401',NULL,'Дмитрівна','Дар`я','ПАНАСЕНКО','dariapanasenko2024@gmail.com'),(74,2,'+380958217810','+380509729140','Олександрович','Богдан','ПОРИВАЙ','fanat991@ukr.net'),(75,2,NULL,NULL,'Дмитрович','Михайло','РИЖОВ','mikhalo.ryzkov@gmail.com'),(76,2,'+380683432312','+380974602518','Юрійович','Денис','РУДИЙ','thedarkgrundemperor@gmail.com'),(77,2,'+380631710865','+380953485038','Ігорович','Олександр','ЦВІЛЬ','alexandertsvil@gmail.com'),(78,2,'+380502184773','+380502184773','Олександрівна','Софія','ЧЕРЕУТА','sochereuta2004@gmail.com'),(79,2,'+380662740323','+380993606491','Сергіївна','Марія','ШАТАЛОВА','mashatalova2005@gmail.com'),(80,2,'+380978232570','+380978232570','Станіславович','Матвій','ШУЛЯК','brawldanibrawl@gmail.com'),(81,2,NULL,NULL,'Олександрович','Гліб','ЩИТ','hlib.shchyt@cs.khpi.edu.ua'),(82,2,'+380990978314',NULL,'Володимирович','Олександр','ЩУКІН','shchukin2020@ukr.net'),(83,3,'+380636449541','+380508595722','Петрівна','Тетяна','ВЛАДІМІРОВА','vladimirovatanya1107@gmail.com'),(84,3,'+380506526556',NULL,'Володимирович','Володимир','ГОЛОВАЩЕНКО','Volodymyr.Holovashchenko@gmail.com'),(85,3,'+380996672378','+380668768621','Михайлович','Андрій','ГОНЧАРЕНКО','andreusgoncharenko@gmail.com'),(86,3,'+380682204775','+420606591858','Миколаївна','Тетяна','ГОРЯНА','cellwydd@gmail.com'),(87,3,'+380954337595','+380505151082','Борисович','Федір','ГРЕБЕНКІН','fred.grebenkin@gmail.com'),(88,3,'+380685472605',NULL,'Олександрович','Антон','КОЛІСНИК','gwenfag@gmail.com'),(89,3,'+380996837606',NULL,'Олександрович','Андрій','КОТЕЛЕВЕЦЬ','andrii.kotelevets@cs.khpi.edu.ua'),(90,3,'+380989621678','+380975192149','Ігорівна','Ксенія','КРУТЬ','krutbkseniia@gmail.com'),(91,3,NULL,NULL,'Миколайович','Владислав','КУПРІЯНОВ','vladyslav.kupriianov@cs.khpi.edu.ua'),(92,3,'+380963160709',NULL,'Анатолійович','Богдан','ЛИТВИНОВИЧ','lolollb770@gmail.com'),(93,3,'+380663005091','+380990369602','Ярославович','Артем','МАНУХІН','artemuniacc22@gmail.com'),(94,3,'+380992291064','+380505888931','Андрійович','Максим','МУНТЬЯНОВ','tboimaksimka@gmail.com'),(95,3,NULL,NULL,'Русланович','Олег','ОМЕЛЬЧЕНКО','oleh.omelchenko@cs.khpi.edu.ua'),(96,3,'+4915157932241','+380676291800','Юріївна','Ксенія','ПЕТІНОВА','petinovaksenia316@gmail.com'),(97,3,'+380934161594','+380636655698','Васильович','Олександр','ПРОТОПОПОВ','protopopovsaha123@gmail.com'),(98,3,'+48796234074','+48577067839','Олегович','Денис','ПУДЛО','crisehop@gmail.com'),(99,3,'+380972061756','+380983455413','Вячеславович','Владислав','РЕВУЦЬКИЙ','vld6381@gmail.com'),(100,3,'+380668407559','+380669656725','Миколайович','Олександр','САВЧЕНКО','alexandr.savchenko04@gmail.com'),(101,3,NULL,NULL,'Євгенійович','Дмитро','СВІТЛИЧНИЙ','dmytro.svitlychnyi@cs.khpi.edu.ua'),(102,3,NULL,NULL,'Андрійович','Денис','СЕЛЮКОВ','denys.seliukov@cs.khpi.edu.ua'),(103,3,NULL,NULL,'Ігорович','Максим','СІВКОВСЬКИЙ','maksym.sivkovskyi@cs.khpi.edu.ua'),(104,3,'+380996590970','+380951735714','Григорович','Андрій','СКИБА','andrew.skiba@aol.com'),(105,3,NULL,NULL,'Сергійович','Даніл','СКРИПНИК','danil.skrypnyk@cs.khpi.edu.ua'),(106,3,'+380979639422','+380982374490','Сергійович','Ілля','СТЕПАНЮК','elioslukos@gmail.com'),(107,3,'+380506272503',NULL,'Анатолійович','Владислав','СТРАТІВСЬКИЙ','vladyslav.strativskyi@cs.khpi.edu.ua'),(108,3,'+380974668896',NULL,'Олегович','Глєб','ХРАМОВ','glebahah2@gmail.com'),(109,3,'+380991957635','+380987192029','Юрійович','Євген','ШОВКОПЛЯС','urza9507@gmail.com'),(110,3,'+380957258555',NULL,'Романович','Нікіта','ЮР`ЄВ','Nikita.Yuriev@cs.khpi.edu.ua'),(111,3,NULL,NULL,'Сергійович','Максим','ЮШКОВ','maksym.yushkov@cs.khpi.edu.ua'),(123,4,NULL,NULL,'Віталійович','Павло','ВАСЮТЕНКО','pavlo.vasiutenko@cs.khpi.edu.ua'),(124,4,'+380677803021','+380989497789','Олександрович','Владислав','ГУРО','vladchik2005@gmail.com'),(125,4,'+380503641684',NULL,'Сергійович','Дмитро','ІГНАТЬЄВ','dignatkovich@gmail.com'),(126,4,NULL,NULL,'Олександрівна','Кристина','ОСНАДЧУК','krystyna.osnadchuk@cs.khpi.edu.ua'),(127,4,'+380679976410','+380979563513','Вікторович','Вадим','ПЛЯКА','ggto.2021@gmail.com'),(128,4,'+380993402324','+380507671422','Ігорович','Ілля','ПОГУЛЯЄВ','bigtapok@ukr.net'),(129,4,NULL,NULL,'Михайлович','Олександр','СІДЄЛЬНІК','oleksandr.sidielnik@cs.khpi.edu.ua'),(130,4,'+380669627961','+380951711287','Миколайович','Ілля','ТОРСЬКИЙ','illyatorskiy@gmail.com'),(131,4,'+380967345707','+380979749441','Юрійович','Олексій','ФОМЕНКО','fomenko06aleksey@gmail.com'),(132,4,'+380507542482','+380509078159','Павлович','Євгеній','ШЕРЕПА','evgenijserepa@gmail.com');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-14 17:49:58
