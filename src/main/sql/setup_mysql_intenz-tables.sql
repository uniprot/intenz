--
-- Table structure for table `citations`
--
DROP TABLE IF EXISTS `citations`;
CREATE TABLE `citations` (
  `enzyme_id` int(7) NOT NULL,
  `pub_id` int(7) NOT NULL,
  `order_in` int(3) NOT NULL,
  `status` varchar(2) NOT NULL,
  `source` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `citations`
--
LOCK TABLES `citations` WRITE;
source citations.sql
UNLOCK TABLES;

--
-- Table structure for table `classes`
--
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `ec1` int(1) NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` varchar(2000),
  `active` enum('Y','N') NOT NULL default 'Y'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `classes`
--
LOCK TABLES `classes` WRITE;
source classes.sql
UNLOCK TABLES;

--
-- Table structure for table `cofactors`
--
DROP TABLE IF EXISTS `cofactors`;
CREATE TABLE `cofactors` (
  `enzyme_id` int(7) NOT NULL,
  `source` varchar(6) NOT NULL,
  `status` varchar(2) NOT NULL,
  `order_in` int(3) NOT NULL default '1',
  `cofactor_text` varchar(1000) NOT NULL,
  `web_view` varchar(50) NOT NULL,
  `compound_id` int(15),
  `operator` varchar(5),
  `op_grp` varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cofactors`
--
LOCK TABLES `cofactors` WRITE;
source cofactors.sql
UNLOCK TABLES;

--
-- Table structure for table `comments`
--
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `enzyme_id` int(7) NOT NULL,
  `comment_text` varchar(2000) NOT NULL,
  `order_in` int(3) NOT NULL default '1',
  `status` varchar(2) NOT NULL,
  `source` varchar(6) NOT NULL,
  `web_view` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `comments`
--
LOCK TABLES `comments` WRITE;
source comments.sql
UNLOCK TABLES;

--
-- Table structure for table `enzymes`
--
DROP TABLE IF EXISTS `enzymes`;
CREATE TABLE `enzymes` (
  `enzyme_id` int(7) NOT NULL,
  `ec1` int(1) default NULL,
  `ec2` int(2) default NULL,
  `ec3` int(3) default NULL,
  `ec4` int(4) default NULL,
  `history` varchar(400) default NULL,
  `status` varchar(2) NOT NULL,
  `note` varchar(2000) default NULL,
  `source` varchar(6) NOT NULL,
  `active` enum('Y','N') NOT NULL default 'Y'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `enzymes`
--
LOCK TABLES `enzymes` WRITE;
source enzymes.sql
UNLOCK TABLES;

--
-- Table structure for table `links`
--
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links` (
  `enzyme_id` int(7) NOT NULL,
  `url` varchar(100) NOT NULL,
  `display_name` varchar(100) NOT NULL,
  `status` varchar(2) NOT NULL default 'NO',
  `source` varchar(6) NOT NULL,
  `web_view` varchar(50) NOT NULL,
  `data_comment` varchar(1000) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `links`
--
LOCK TABLES `links` WRITE;
source links.sql
UNLOCK TABLES;

--
-- Table structure for table `names`
--
DROP TABLE IF EXISTS `names`;
CREATE TABLE `names` (
  `enzyme_id` int(7) NOT NULL,
  `name` varchar(1000) NOT NULL,
  `name_class` varchar(3) NOT NULL,
  `warning` varchar(3) default NULL,
  `status` varchar(2) NOT NULL,
  `source` varchar(6) NOT NULL,
  `note` varchar(200) default NULL,
  `order_in` int(4) NOT NULL,
  `web_view` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `names`
--
LOCK TABLES `names` WRITE;
source names.sql
UNLOCK TABLES;

--
-- Table structure for table `publications`
--
DROP TABLE IF EXISTS `publications`;
CREATE TABLE `publications` (
  `pub_id` int(7) NOT NULL,
  `medline_id` int(9) default NULL,
  `pubmed_id` int(8) default NULL,
  `pub_type` char(1) NOT NULL,
  `author` varchar(2000) default NULL,
  `pub_year` int(4) NOT NULL,
  `title` varchar(1000) default NULL,
  `journal_book` varchar(1000) NOT NULL,
  `volume` varchar(7) default NULL,
  `first_page` varchar(12) default NULL,
  `last_page` varchar(12) default NULL,
  `edition` int(3) default NULL,
  `editor` varchar(2000) default NULL,
  `pub_company` varchar(50) default NULL,
  `pub_place` varchar(50) default NULL,
  `url` varchar(1000) default NULL,
  `web_view` varchar(50) NOT NULL,
  `source` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `publications`
--
LOCK TABLES `publications` WRITE;
source publications.sql
UNLOCK TABLES;

--
-- Table structure for table `reactions`
--
DROP TABLE IF EXISTS `reactions`;
CREATE TABLE `reactions` (
  `enzyme_id` int(7) NOT NULL,
  `equation` varchar(3000) NOT NULL,
  `order_in` int(2) NOT NULL default '1',
  `status` varchar(2) NOT NULL,
  `source` varchar(6) NOT NULL,
  `web_view` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reactions`
--
LOCK TABLES `reactions` WRITE;
source reactions.sql
UNLOCK TABLES;

--
-- Table structure for table `subclasses`
--
DROP TABLE IF EXISTS `subclasses`;
CREATE TABLE `subclasses` (
  `ec1` int(1) NOT NULL,
  `ec2` int(2) NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` varchar(2000) default NULL,
  `active` enum('Y','N') NOT NULL default 'Y'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `subclasses`
--
LOCK TABLES `subclasses` WRITE;
source subclasses.sql
UNLOCK TABLES;

--
-- Table structure for table `subsubclasses`
--
DROP TABLE IF EXISTS `subsubclasses`;
CREATE TABLE `subsubclasses` (
  `ec1` int(1) NOT NULL,
  `ec2` int(2) NOT NULL,
  `ec3` int(3) NOT NULL,
  `name` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `active` enum('Y','N') NOT NULL default 'Y'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `subsubclasses`
--
LOCK TABLES `subsubclasses` WRITE;
source subsubclasses.sql
UNLOCK TABLES;

--
-- Table structure for table `xrefs`
--
DROP TABLE IF EXISTS `xrefs`;
CREATE TABLE `xrefs` (
  `enzyme_id` int(7) NOT NULL,
  `database_code` varchar(9) NOT NULL,
  `database_ac` varchar(500) NOT NULL,
  `name` varchar(200) NOT NULL,
  `status` varchar(2) NOT NULL default 'NO',
  `source` varchar(6) NOT NULL,
  `web_view` varchar(50) NOT NULL,
  `data_comment` varchar(1000) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `xrefs`
--
LOCK TABLES `xrefs` WRITE;
source xrefs.sql
UNLOCK TABLES;




--
-- Workflow-related tables
--


--
-- Table structure for table `history_events`
--
DROP TABLE IF EXISTS `history_events`;
CREATE TABLE `history_events` (
  `group_id` int(7) NOT NULL,
  `event_id` int(7) NOT NULL,
  `before_id` int(7),
  `after_id` int(7),
  `event_year` timestamp DEFAULT current_timestamp,
  `event_note` varchar(2000),
  `event_class` varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `history_events`
--
LOCK TABLES `history_events` WRITE;
source history_events.sql
UNLOCK TABLES;

--
-- Table structure for table `future_events`
--
DROP TABLE IF EXISTS `future_events`;
CREATE TABLE `future_events` (
  `group_id` int(7) NOT NULL,
  `event_id` int(7) NOT NULL,
  `before_id` int(7),
  `after_id` int(7),
  `event_year` timestamp DEFAULT current_timestamp,
  `event_note` varchar(2000),
  `event_class` varchar(20),
  `status` varchar(2) NOT NULL,
  `timeout_id` int(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `future_events`
--
LOCK TABLES `future_events` WRITE;
source future_events.sql
UNLOCK TABLES;

--
-- Table structure for table `timeouts`
--
DROP TABLE IF EXISTS `timeouts`;
CREATE TABLE `timeouts` (
  `timeout_id` int(7) NOT NULL,
  `enzyme_id` int(7) NOT NULL,
  `start_date` datetime NOT NULL,
  `due_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `timeouts`
--
LOCK TABLES `timeouts` WRITE;
source timeouts.sql
UNLOCK TABLES;


--
-- Rhea-related tables
--

--  TODO: REACTION_MERGINGS

--
-- Table structure for table `reactions_map`
--
DROP TABLE IF EXISTS `reactions_map`;
CREATE TABLE `reactions_map` (
  `reaction_id` int(7) NOT NULL,
  `enzyme_id` int(7) NOT NULL,
  `web_view` varchar(50) NOT NULL,
  `order_in` int(2) NOT NULL default '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reactions_map`
--
LOCK TABLES `reactions_map` WRITE;
source reactions_map.sql
UNLOCK TABLES;

--
-- Table structure for table `intenz_reactions`
--
DROP TABLE IF EXISTS `intenz_reactions`;
CREATE TABLE `intenz_reactions` (
  `reaction_id` int(7) NOT NULL,
  `equation` varchar(3000) NOT NULL,
  `fingerprint` varchar(3000) NOT NULL,
  `status` varchar(2) NOT NULL,
  `source` varchar(6) NOT NULL,
  `direction` varchar(2) NOT NULL,
  `un_reaction` int(7),
  `qualifiers` set('CB', 'PO', 'TR', 'CR'),
  `data_comment` varchar(3000),
  `reaction_comment` varchar(3000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `intenz_reactions`
--
LOCK TABLES `intenz_reactions` WRITE;
source intenz_reactions.sql
UNLOCK TABLES;

--
-- Table structure for table `reaction_participants`
--
DROP TABLE IF EXISTS `reaction_participants`;
CREATE TABLE `reaction_participants` (
  `reaction_id` int(7) NOT NULL,
  `compound_id` int(15) NOT NULL,
  `side` varchar(1) NOT NULL,
  `coefficient` int(2) NOT NULL DEFAULT '1',
  `coeff_type` varchar(1) NOT NULL DEFAULT 'F',
  `location` varchar(1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reaction_participants`
--
LOCK TABLES `reaction_participants` WRITE;
source reaction_participants.sql
UNLOCK TABLES;

--
-- Table structure for table `compound_data`
--
DROP TABLE IF EXISTS `compound_data`;
CREATE TABLE `compound_data` (
  `compound_id` int(15) NOT NULL,
  `name` varchar(4000) NOT NULL,
  `formula` varchar(512),
  `charge` int(2),
  `source` varchar(6),
  `accession` varchar(256),
  `child_accession` varchar(256),
  `published` char(1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `compound_data`
--
LOCK TABLES `compound_data` WRITE;
source compound_data.sql
UNLOCK TABLES;

--
-- Table structure for table `complex_reactions`
--
DROP TABLE IF EXISTS `complex_reactions`;
CREATE TABLE `complex_reactions` (
  `parent_id` int(15) NOT NULL,
  `child_id` int(15) NOT NULL,
  `order_in` int(2) NOT NULL DEFAULT '0',
  `coefficient` int(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `complex_reactions`
--
LOCK TABLES `complex_reactions` WRITE;
source complex_reactions.sql
UNLOCK TABLES;

--
-- Table structure for table `reaction_citations`
--
DROP TABLE IF EXISTS `reaction_citations`;
CREATE TABLE `reaction_citations` (
  `reaction_id` int(7) NOT NULL,
  `pub_id` varchar(10) NOT NULL,
  `source` varchar(6) NOT NULL,
  `order_in` int(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reaction_citations`
--
LOCK TABLES `reaction_citations` WRITE;
source reaction_citations.sql
UNLOCK TABLES;

--
-- Table structure for table `reaction_xrefs`
--
DROP TABLE IF EXISTS `reaction_xrefs`;
CREATE TABLE `reaction_xrefs` (
  `reaction_id` int(7) NOT NULL,
  `db_code` varchar(6) NOT NULL,
  `db_accession` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `reaction_xrefs`
--
LOCK TABLES `reaction_xrefs` WRITE;
source reaction_xrefs.sql
UNLOCK TABLES;



--
-- Controlled Vocabularies tables
--

--
-- Table structure for table `cv_coeff_types`
--
DROP TABLE IF EXISTS `cv_coeff_types`;
CREATE TABLE `cv_coeff_types` (
  `code` varchar(1) NOT NULL,
  `explanation` varchar(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_coeff_types`
--
LOCK TABLES `cv_coeff_types` WRITE;
source cv_coeff_types.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_comp_pub_avail`
--
DROP TABLE IF EXISTS `cv_comp_pub_avail`;
CREATE TABLE `cv_comp_pub_avail` (
  `code` varchar(1) NOT NULL,
  `explanation` varchar(110)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_comp_pub_avail`
--
LOCK TABLES `cv_comp_pub_avail` WRITE;
source cv_comp_pub_avail.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_databases`
--
DROP TABLE IF EXISTS `cv_databases`;
CREATE TABLE `cv_databases` (
  `code` varchar(6) NOT NULL,
  `name` varchar(30) NOT NULL,
  `display_name` varchar(200) NOT NULL,
  `sort_order` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_databases`
--
LOCK TABLES `cv_databases` WRITE;
source cv_databases.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_name_classes`
--
DROP TABLE IF EXISTS `cv_name_classes`;
CREATE TABLE `cv_name_classes` (
  `code` varchar(3) NOT NULL,
  `name` varchar(30) NOT NULL,
  `display_name` varchar(200) NOT NULL,
  `sort_order` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_name_classes`
--
LOCK TABLES `cv_name_classes` WRITE;
source cv_name_classes.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_operators`
--
DROP TABLE IF EXISTS `cv_operators`;
CREATE TABLE `cv_operators` (
  `code` varchar(5) NOT NULL,
  `explanation` varchar(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_operators`
--
LOCK TABLES `cv_operators` WRITE;
source cv_operators.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_reaction_directions`
--
DROP TABLE IF EXISTS `cv_reaction_directions`;
CREATE TABLE `cv_reaction_directions` (
  `code` varchar(2) NOT NULL,
  `direction` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_reaction_directions`
--
LOCK TABLES `cv_reaction_directions` WRITE;
source cv_reaction_directions.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_reaction_qualifiers`
--
DROP TABLE IF EXISTS `cv_reaction_qualifiers`;
CREATE TABLE `cv_reaction_qualifiers` (
  `code` varchar(2) NOT NULL,
  `qualifier` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_reaction_qualifiers`
--
LOCK TABLES `cv_reaction_qualifiers` WRITE;
source cv_reaction_qualifiers.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_reaction_sides`
--
DROP TABLE IF EXISTS `cv_reaction_sides`;
CREATE TABLE `cv_reaction_sides` (
  `code` varchar(1) NOT NULL,
  `side` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_reaction_sides`
--
LOCK TABLES `cv_reaction_sides` WRITE;
source cv_reaction_sides.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_status`
--
DROP TABLE IF EXISTS `cv_status`;
CREATE TABLE `cv_status` (
  `code` varchar(2) NOT NULL,
  `name` varchar(30) NOT NULL,
  `display_name` varchar(200) NOT NULL,
  `sort_order` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_status`
--
LOCK TABLES `cv_status` WRITE;
source cv_status.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_operators`
--
DROP TABLE IF EXISTS `cv_view`;
CREATE TABLE `cv_view` (
  `code` varchar(20) NOT NULL,
  `description` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_view`
--
LOCK TABLES `cv_view` WRITE;
source cv_view.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_warnings`
--
DROP TABLE IF EXISTS `cv_warnings`;
CREATE TABLE `cv_warnings` (
  `code` varchar(3) NOT NULL,
  `name` varchar(30) NOT NULL,
  `display_name` varchar(200) NOT NULL,
  `sort_order` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_warnings`
--
LOCK TABLES `cv_warnings` WRITE;
source cv_warnings.sql
UNLOCK TABLES;

--
-- Table structure for table `cv_location`
--
DROP TABLE IF EXISTS `cv_location`;
CREATE TABLE `cv_location` (
  `code` varchar(1) NOT NULL,
  `text` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `cv_location`
--
LOCK TABLES `cv_location` WRITE;
source cv_location.sql
UNLOCK TABLES;
