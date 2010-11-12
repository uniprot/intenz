--
-- Table structure for table `citations_audit`
--
DROP TABLE IF EXISTS `citations_audit`;
CREATE TABLE `citations_audit` (
  `enzyme_id` int(7),
  `pub_id` int(7),
  `order_in` int(3),
  `status` varchar(2),
  `source` varchar(6),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `citations_audit`
--
LOCK TABLES `citations_audit` WRITE;
source citations_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `classes_audit`
--
DROP TABLE IF EXISTS `classes_audit`;
CREATE TABLE `classes_audit` (
  `ec1` int(1),
  `name` varchar(200),
  `description` varchar(2000),
  `active` varchar(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `classes_audit`
--
LOCK TABLES `classes_audit` WRITE;
source classes_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `cofactors_audit`
--
DROP TABLE IF EXISTS `cofactors_audit`;
CREATE TABLE `cofactors_audit` (
  `enzyme_id` int(7),
  `source` varchar(6),
  `status` varchar(2),
  `order_in` int(3),
  `cofactor_text` varchar(1000),
  `web_view` varchar(50),
  `compound_id` int(15),
  `operator` varchar(5),
  `op_grp` varchar(10),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cofactors_audit`
--
LOCK TABLES `cofactors_audit` WRITE;
source cofactors_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `comments_audit`
--
DROP TABLE IF EXISTS `comments_audit`;
CREATE TABLE `comments_audit` (
  `enzyme_id` int(7),
  `comment_text` varchar(2000),
  `order_in` int(3),
  `status` varchar(2),
  `source` varchar(6),
  `web_view` varchar(50),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `comments_audit`
--
LOCK TABLES `comments_audit` WRITE;
source comments_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `enzymes_audit`
--
DROP TABLE IF EXISTS `enzymes_audit`;
CREATE TABLE `enzymes_audit` (
  `enzyme_id` int(7),
  `ec1` int(1),
  `ec2` int(2),
  `ec3` int(3),
  `ec4` int(4),
  `history` varchar(400),
  `status` varchar(2),
  `note` varchar(2000),
  `source` varchar(6),
  `active` varchar(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `enzymes_audit`
--
LOCK TABLES `enzymes_audit` WRITE;
source enzymes_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `links_audit`
--
DROP TABLE IF EXISTS `links_audit`;
CREATE TABLE `links_audit` (
  `enzyme_id` int(7),
  `url` varchar(100),
  `display_name` varchar(100),
  `status` varchar(2),
  `source` varchar(6),
  `web_view` varchar(50),
  `data_comment` varchar(1000),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `links_audit`
--
LOCK TABLES `links_audit` WRITE;
source links_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `names_audit`
--
DROP TABLE IF EXISTS `names_audit`;
CREATE TABLE `names_audit` (
  `enzyme_id` int(7),
  `name` varchar(1000),
  `name_class` varchar(3),
  `warning` varchar(3),
  `status` varchar(2),
  `source` varchar(6),
  `note` varchar(200),
  `order_in` int(4),
  `web_view` varchar(50),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `names_audit`
--
LOCK TABLES `names_audit` WRITE;
source names_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `publications_audit`
--
DROP TABLE IF EXISTS `publications_audit`;
CREATE TABLE `publications_audit` (
  `pub_id` int(7),
  `medline_id` int(9),
  `pubmed_id` int(8),
  `pub_type` char(1),
  `author` varchar(2000),
  `pub_year` int(4),
  `title` varchar(1000),
  `journal_book` varchar(1000),
  `volume` varchar(7),
  `first_page` varchar(12),
  `last_page` varchar(12),
  `edition` int(3),
  `editor` varchar(2000),
  `pub_company` varchar(50),
  `pub_place` varchar(50),
  `url` varchar(1000),
  `web_view` varchar(50),
  `source` varchar(50),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `publications_audit`
--
LOCK TABLES `publications_audit` WRITE;
source publications_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `reactions_audit`
--
DROP TABLE IF EXISTS `reactions_audit`;
CREATE TABLE `reactions_audit` (
  `enzyme_id` int(7),
  `equation` varchar(3000),
  `order_in` int(2),
  `status` varchar(2),
  `source` varchar(6),
  `web_view` varchar(50),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reactions_audit`
--
LOCK TABLES `reactions_audit` WRITE;
source reactions_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `subclasses_audit`
--
DROP TABLE IF EXISTS `subclasses_audit`;
CREATE TABLE `subclasses_audit` (
  `ec1` int(1),
  `ec2` int(2),
  `name` varchar(200),
  `description` varchar(2000),
  `active` varchar(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `subclasses_audit`
--
LOCK TABLES `subclasses_audit` WRITE;
source subclasses_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `subsubclasses_audit`
--
DROP TABLE IF EXISTS `subsubclasses_audit`;
CREATE TABLE `subsubclasses_audit` (
  `ec1` int(1),
  `ec2` int(2),
  `ec3` int(3),
  `name` varchar(200),
  `description` varchar(2000),
  `active` varchar(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `subsubclasses_audit`
--
LOCK TABLES `subsubclasses_audit` WRITE;
source subsubclasses_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `xrefs_audit`
--
DROP TABLE IF EXISTS `xrefs_audit`;
CREATE TABLE `xrefs_audit` (
  `enzyme_id` int(7),
  `database_code` varchar(9),
  `database_ac` varchar(500),
  `name` varchar(200),
  `status` varchar(2),
  `source` varchar(6),
  `web_view` varchar(50),
  `data_comment` varchar(1000),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `xrefs_audit`
--
LOCK TABLES `xrefs_audit` WRITE;
source xrefs_audit.sql
UNLOCK TABLES;




--
-- Workflow-related tables
--


--
-- Table structure for table `history_events_audit`
--
DROP TABLE IF EXISTS `history_events_audit`;
CREATE TABLE `history_events_audit` (
  `group_id` int(7),
  `event_id` int(7),
  `before_id` int(7),
  `after_id` int(7),
  `event_year` datetime,
  `event_note` varchar(2000),
  `event_class` varchar(20),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `history_events_audit`
--
LOCK TABLES `history_events_audit` WRITE;
source history_events_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `future_events_audit`
--
DROP TABLE IF EXISTS `future_events_audit`;
CREATE TABLE `future_events_audit` (
  `group_id` int(7),
  `event_id` int(7),
  `before_id` int(7),
  `after_id` int(7),
  `event_year` datetime,
  `event_note` varchar(2000),
  `event_class` varchar(20),
  `status` varchar(2),
  `timeout_id` int(7),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `future_events_audit`
--
LOCK TABLES `future_events_audit` WRITE;
source future_events_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `timeouts_audit`
--
DROP TABLE IF EXISTS `timeouts_audit`;
CREATE TABLE `timeouts_audit` (
  `timeout_id` int(7),
  `enzyme_id` int(7),
  `start_date` datetime,
  `due_date` datetime,
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `timeouts_audit`
--
LOCK TABLES `timeouts_audit` WRITE;
source timeouts_audit.sql
UNLOCK TABLES;



--
-- Rhea-related tables
--


--
-- Table structure for table `reactions_map_audit`
--
DROP TABLE IF EXISTS `reactions_map_audit`;
CREATE TABLE `reactions_map_audit` (
  `reaction_id` int(7),
  `enzyme_id` int(7),
  `web_view` varchar(50),
  `order_in` int(2),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reactions_map_audit`
--
LOCK TABLES `reactions_map_audit` WRITE;
source reactions_map_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `intenz_reactions_audit`
--
DROP TABLE IF EXISTS `intenz_reactions_audit`;
CREATE TABLE `intenz_reactions_audit` (
  `reaction_id` int(7),
  `equation` varchar(3000),
  `fingerprint` varchar(3000),
  `status` varchar(2),
  `source` varchar(6),
  `direction` varchar(2),
  `un_reaction` int(7),
  `qualifiers` set('CB', 'PO', 'TR', 'CR'),
  `data_comment` varchar(3000),
  `reaction_comment` varchar(3000),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(4000),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `intenz_reactions_audit`
--
LOCK TABLES `intenz_reactions_audit` WRITE;
source intenz_reactions_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `reaction_participants_audit`
--
DROP TABLE IF EXISTS `reaction_participants_audit`;
CREATE TABLE `reaction_participants_audit` (
  `reaction_id` int(7),
  `compound_id` int(15),
  `side` varchar(1),
  `coefficient` int(2),
  `coeff_type` varchar(1),
  `location` varchar(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reaction_participants_audit`
--
LOCK TABLES `reaction_participants_audit` WRITE;
source reaction_participants_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `compound_data_audit`
--
DROP TABLE IF EXISTS `compound_data_audit`;
CREATE TABLE `compound_data_audit` (
  `compound_id` int(15),
  `name` varchar(4000),
  `formula` varchar(512),
  `charge` int(2),
  `source` varchar(6),
  `accession` varchar(256),
  `child_accession` varchar(256),
  `published` char(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `compound_data_audit`
--
LOCK TABLES `compound_data_audit` WRITE;
source compound_data_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `complex_reactions_audit`
--
DROP TABLE IF EXISTS `complex_reactions_audit`;
CREATE TABLE `complex_reactions_audit` (
  `parent_id` int(15),
  `child_id` int(15),
  `order_in` int(2),
  `coefficient` int(1),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(500),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `complex_reactions_audit`
--
LOCK TABLES `complex_reactions_audit` WRITE;
source complex_reactions_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `reaction_citations_audit`
--
DROP TABLE IF EXISTS `reaction_citations_audit`;
CREATE TABLE `reaction_citations_audit` (
  `reaction_id` int(7),
  `pub_id` varchar(10),
  `source` varchar(6),
  `order_in` int(3),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(500),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reaction_citations_audit`
--
LOCK TABLES `reaction_citations_audit` WRITE;
source reaction_citations_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `reaction_xrefs_audit`
--
DROP TABLE IF EXISTS `reaction_xrefs_audit`;
CREATE TABLE `reaction_xrefs_audit` (
  `reaction_id` int(7),
  `db_code` varchar(6),
  `db_accession` varchar(500),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(500),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reaction_xrefs_audit`
--
LOCK TABLES `reaction_xrefs_audit` WRITE;
source reaction_xrefs_audit.sql
UNLOCK TABLES;



--
-- Controlled Vocabularies audit tables
--

--
-- Table structure for table `cv_databases_audit`
--
DROP TABLE IF EXISTS `cv_databases_audit`;
CREATE TABLE `cv_databases_audit` (
  `code` varchar(6),
  `name` varchar(30),
  `display_name` varchar(200),
  `sort_order` int(2),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_databases_audit`
--
LOCK TABLES `cv_databases_audit` WRITE;
source cv_databases_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_name_classes_audit`
--
DROP TABLE IF EXISTS `cv_name_classes_audit`;
CREATE TABLE `cv_name_classes_audit` (
  `code` varchar(3),
  `name` varchar(30),
  `display_name` varchar(200),
  `sort_order` int(2),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_name_classes_audit`
--
LOCK TABLES `cv_name_classes_audit` WRITE;
source cv_name_classes_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_status_audit`
--
DROP TABLE IF EXISTS `cv_status_audit`;
CREATE TABLE `cv_status_audit` (
  `code` varchar(2),
  `name` varchar(30),
  `display_name` varchar(200),
  `sort_order` int(2),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_status_audit`
--
LOCK TABLES `cv_status_audit` WRITE;
source cv_status_audit.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_view_audit`
--
DROP TABLE IF EXISTS `cv_view_audit`;
CREATE TABLE `cv_view_audit` (
  `code` varchar(20),
  `description` varchar(1000),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_view_audit`
--
LOCK TABLES `cv_view_audit` WRITE;
source cv_iew_audit.sql
UNLOCK TABLES;


--
-- Table structure for table `cv_warnings_audit`
--
DROP TABLE IF EXISTS `cv_warnings_audit`;
CREATE TABLE `cv_warnings_audit` (
  `code` varchar(3),
  `name` varchar(30),
  `display_name` varchar(200),
  `sort_order` int(2),
  `timestamp` datetime,
  `audit_id` int(15),
  `dbuser` varchar(30),
  `osuser` varchar(30),
  `remark` varchar(50),
  `action` varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_warnings_audit`
--
LOCK TABLES `cv_warnings_audit` WRITE;
source cv_warnings_audit.sql
UNLOCK TABLES;
