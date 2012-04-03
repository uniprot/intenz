--
-- Table structure for table citations
--

DROP TABLE IF EXISTS citations;
CREATE TABLE citations (
  enzyme_id numeric(7) NOT NULL,
  pub_id numeric(7) NOT NULL,
  order_in numeric(3) NOT NULL,
  status varchar(2) NOT NULL,
  source varchar(6) NOT NULL
);

--
-- Dumping data for table citations
--

BEGIN WORK;
LOCK TABLE citations IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE citations DISABLE KEYS */;
\i citations.sql
/*!40000 ALTER TABLE citations ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table classes
--

DROP TABLE IF EXISTS classes;
CREATE TABLE classes (
  ec1 numeric(1) NOT NULL,
  name varchar(200) NOT NULL,
  description varchar(2000) default NULL,
  active varchar(1) NOT NULL default 'Y'
);

--
-- Dumping data for table classes
--

BEGIN WORK;
LOCK TABLE classes IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE classes DISABLE KEYS */;
\i classes.sql
/*!40000 ALTER TABLE classes ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table cofactors
--

DROP TABLE IF EXISTS cofactors;
CREATE TABLE cofactors (
  enzyme_id numeric(7) NOT NULL,
  source varchar(6) NOT NULL,
  status varchar(2) NOT NULL,
  order_in numeric(3) NOT NULL default '1',
  cofactor_text varchar(1000) NOT NULL,
  web_view varchar(50) NOT NULL
);

--
-- Dumping data for table cofactors
--

BEGIN WORK;
LOCK TABLE cofactors IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE cofactors DISABLE KEYS */;
\i cofactors.sql
/*!40000 ALTER TABLE cofactors ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table comments
--

DROP TABLE IF EXISTS comments;
CREATE TABLE comments (
  enzyme_id numeric(7) NOT NULL,
  comment_text varchar(2000) NOT NULL,
  order_in numeric(3) NOT NULL default '1',
  status varchar(2) NOT NULL,
  source varchar(6) NOT NULL,
  web_view varchar(50) NOT NULL
);

--
-- Dumping data for table comments
--

BEGIN WORK;
LOCK TABLE comments IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE comments DISABLE KEYS */;
\i comments.sql
/*!40000 ALTER TABLE comments ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table data_comments
--

DROP TABLE IF EXISTS data_comments;
CREATE TABLE data_comments (
  comment_id numeric(5) NOT NULL,
  comment_text varchar(1000) NOT NULL,
  enzyme_id numeric(7) NOT NULL
);

--
-- Dumping data for table data_comments
--

BEGIN WORK;
LOCK TABLE data_comments IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE data_comments DISABLE KEYS */;
\i data_comments.sql
/*!40000 ALTER TABLE data_comments ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table enzymes
--

DROP TABLE IF EXISTS enzymes;
CREATE TABLE enzymes (
  enzyme_id numeric(7) NOT NULL,
  ec1 numeric(1) default NULL,
  ec2 numeric(2) default NULL,
  ec3 numeric(3) default NULL,
  ec4 numeric(4) default NULL,
  history varchar(400) default NULL,
  status varchar(2) NOT NULL,
  note varchar(2000) default NULL,
  source varchar(6) NOT NULL,
  active varchar(1) NOT NULL default 'Y'
);

--
-- Dumping data for table enzymes
--

BEGIN WORK;
LOCK TABLE enzymes IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE enzymes DISABLE KEYS */;
\i enzymes.sql
/*!40000 ALTER TABLE enzymes ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table links
--

DROP TABLE IF EXISTS links;
CREATE TABLE links (
  enzyme_id numeric(7) NOT NULL,
  url varchar(100) NOT NULL,
  display_name varchar(100) NOT NULL,
  status varchar(2) NOT NULL default 'NO',
  source varchar(6) NOT NULL,
  web_view varchar(50) NOT NULL,
  comment_id numeric(5) default NULL
);

--
-- Dumping data for table links
--

BEGIN WORK;
LOCK TABLE links IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE links DISABLE KEYS */;
\i links.sql
/*!40000 ALTER TABLE links ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table names
--

DROP TABLE IF EXISTS names;
CREATE TABLE names (
  enzyme_id numeric(7) NOT NULL,
  name varchar(1000) NOT NULL,
  name_class varchar(3) NOT NULL,
  warning varchar(3) default NULL,
  status varchar(2) NOT NULL,
  source varchar(6) NOT NULL,
  note varchar(200) default NULL,
  order_in numeric(4) NOT NULL,
  web_view varchar(50) NOT NULL
);

--
-- Dumping data for table names
--

BEGIN WORK;
LOCK TABLE names IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE names DISABLE KEYS */;
\i names.sql
/*!40000 ALTER TABLE names ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table publications
--

DROP TABLE IF EXISTS publications;
CREATE TABLE publications (
  pub_id numeric(7) NOT NULL,
  medline_id numeric(9) default NULL,
  pubmed_id numeric(8) default NULL,
  pub_type char(1) NOT NULL,
  author varchar(2000) default NULL,
  pub_year numeric(4) NOT NULL,
  title varchar(1000) default NULL,
  journal_book varchar(1000) NOT NULL,
  volume varchar(7) default NULL,
  first_page varchar(12) default NULL,
  last_page varchar(12) default NULL,
  edition numeric(3) default NULL,
  editor varchar(2000) default NULL,
  pub_company varchar(50) default NULL,
  pub_place varchar(50) default NULL,
  url varchar(1000) default NULL,
  web_view varchar(50) NOT NULL,
  source varchar(50) NOT NULL
);

--
-- Dumping data for table publications
--

BEGIN WORK;
LOCK TABLE publications IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE publications DISABLE KEYS */;
\i publications.sql
/*!40000 ALTER TABLE publications ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table reactions
--

DROP TABLE IF EXISTS reactions;
CREATE TABLE reactions (
  enzyme_id numeric(7) NOT NULL,
  equation varchar(3000) NOT NULL,
  order_in numeric(2) NOT NULL default '1',
  status varchar(2) NOT NULL,
  source varchar(6) NOT NULL,
  web_view varchar(50) NOT NULL
);

--
-- Dumping data for table reactions
--

BEGIN WORK;
LOCK TABLE reactions IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE reactions DISABLE KEYS */;
\i reactions.sql
/*!40000 ALTER TABLE reactions ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table subclasses
--

DROP TABLE IF EXISTS subclasses;
CREATE TABLE subclasses (
  ec1 numeric(1) NOT NULL,
  ec2 numeric(2) NOT NULL,
  name varchar(200) NOT NULL,
  description varchar(2000) default NULL,
  active varchar(1) NOT NULL default 'Y'
);

--
-- Dumping data for table subclasses
--

BEGIN WORK;
LOCK TABLE subclasses IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE subclasses DISABLE KEYS */;
\i subclasses.sql
/*!40000 ALTER TABLE subclasses ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table subsubclasses
--

DROP TABLE IF EXISTS subsubclasses;
CREATE TABLE subsubclasses (
  ec1 numeric(1) NOT NULL,
  ec2 numeric(2) NOT NULL,
  ec3 numeric(3) NOT NULL,
  name varchar(200) default NULL,
  description varchar(2000) default NULL,
  active varchar(1) NOT NULL default 'Y'
);

--
-- Dumping data for table subsubclasses
--

BEGIN WORK;
LOCK TABLE subsubclasses IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE subsubclasses DISABLE KEYS */;
\i subsubclasses.sql
/*!40000 ALTER TABLE subsubclasses ENABLE KEYS */;
COMMIT WORK;

--
-- Table structure for table xrefs
--

DROP TABLE IF EXISTS xrefs;
CREATE TABLE xrefs (
  enzyme_id numeric(7) NOT NULL,
  database_code varchar(9) NOT NULL,
  database_ac varchar(500) NOT NULL,
  name varchar(200) NOT NULL,
  status varchar(2) NOT NULL default 'NO',
  source varchar(6) NOT NULL,
  web_view varchar(50) NOT NULL,
  comment_id numeric(5) default NULL
);

--
-- Dumping data for table xrefs
--

BEGIN WORK;
LOCK TABLE xrefs IN EXCLUSIVE MODE;
/*!40000 ALTER TABLE xrefs DISABLE KEYS */;
\i xrefs.sql
/*!40000 ALTER TABLE xrefs ENABLE KEYS */;
COMMIT WORK;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2007-02-08  9:53:59
