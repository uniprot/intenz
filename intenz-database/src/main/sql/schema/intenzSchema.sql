--------------------------------------------------------
--  File created - Thursday-June-21-2012   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Type REACTION_QUALIFIERS
--------------------------------------------------------

  CREATE OR REPLACE TYPE "ENZYME"."REACTION_QUALIFIERS" AS VARRAY(10) OF VARCHAR2(2);

/
--------------------------------------------------------
--  DDL for Sequence S_AUDIT_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_AUDIT_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 10633988 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_COMPOUND_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_COMPOUND_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 7302 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_ENZYME_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_ENZYME_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 19144 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_FUTURE_EVENT_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_FUTURE_EVENT_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 2100 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_FUTURE_GROUP_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_FUTURE_GROUP_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 2097 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_HISTORY_EVENT_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_HISTORY_EVENT_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 10897 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_HISTORY_GROUP_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_HISTORY_GROUP_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 10898 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_PUB_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_PUB_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 30971 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_REACTION_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_REACTION_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 33563 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_TIMEOUT_ID
--------------------------------------------------------

   CREATE SEQUENCE  "ENZYME"."S_TIMEOUT_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 2098 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table CITATIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CITATIONS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"PUB_ID" NUMBER(7,0), 
	"ORDER_IN" NUMBER(3,0), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CITATIONS"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS"."PUB_ID" IS 'Pub_id is used as an internal unique identifier for publications. Example is ''1'', ''253'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS"."ORDER_IN" IS 'Order_in indicates the order in which the publications are listed.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON TABLE "ENZYME"."CITATIONS"  IS 'Citations, table storing the publications referenced in enzymes.';
--------------------------------------------------------
--  DDL for Table CITATIONS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CITATIONS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"PUB_ID" NUMBER(7,0), 
	"ORDER_IN" NUMBER(3,0), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."PUB_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."ORDER_IN" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CITATIONS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CITATIONS_AUDIT"  IS 'Audit table storing the history of table citations.';
--------------------------------------------------------
--  DDL for Table CLASSES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CLASSES" 
   (	"EC1" NUMBER(1,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE) DEFAULT 'Y'
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CLASSES"."EC1" IS 'The first number, ec1, is the number of the class, example ''3''.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES"."NAME" IS 'Name of the class, example ''Hydrolases''.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES"."DESCRIPTION" IS 'Description is a short paragraph explaining the contents of the class.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES"."ACTIVE" IS 'IUBMB uses ''deleted'' for different meanings. We say ''active'' for enzymes that are currently recommended by IUBMB. Opposit of inactive.';
 
   COMMENT ON TABLE "ENZYME"."CLASSES"  IS 'Classes, highest level of the enzyme hierachy. There are currently six different classes, 1 to 6. It is unlikely that there will be more classes in future.';
--------------------------------------------------------
--  DDL for Table CLASSES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CLASSES_AUDIT" 
   (	"EC1" NUMBER(1,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."EC1" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."DESCRIPTION" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."ACTIVE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CLASSES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CLASSES_AUDIT"  IS 'Audit table storing the history of table classes.';
--------------------------------------------------------
--  DDL for Table COFACTORS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COFACTORS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"ORDER_IN" NUMBER(3,0) DEFAULT 1, 
	"COFACTOR_TEXT" VARCHAR2(1000 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"COMPOUND_ID" NUMBER(15,0), 
	"OPERATOR" VARCHAR2(5 BYTE), 
	"OP_GRP" VARCHAR2(10 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."COFACTORS"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."STATUS" IS 'Status code, for example ''OK'', ''PR''.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."ORDER_IN" IS 'Number to be used to order cofactors.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."COFACTOR_TEXT" IS 'Cofactor_text stores the cofactors on enzymes as free text field. Later on the cofactor might get a detailed structure.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."WEB_VIEW" IS '(not used: cofactors are shown in INTENZ and ENZYME views)';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."COMPOUND_ID" IS 'Internal ID of the compound acting as cofactor.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."OPERATOR" IS 'Operator (AND, OR) applied to cofactors, if more than one per enzyme.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS"."OP_GRP" IS 'Operator group to insert the compound into (in complex cofactor setups).';
 
   COMMENT ON TABLE "ENZYME"."COFACTORS"  IS 'Cofactors, table storing cofactors on enzymes. Cofactors are free text notes on an enzyme, and may be used to convey any useful information.';
--------------------------------------------------------
--  DDL for Table COFACTORS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COFACTORS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"ORDER_IN" NUMBER(3,0), 
	"COFACTOR_TEXT" VARCHAR2(1000 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"COMPOUND_ID" NUMBER(15,0), 
	"OPERATOR" VARCHAR2(5 BYTE), 
	"OP_GRP" VARCHAR2(10 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."ORDER_IN" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."COFACTOR_TEXT" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."COFACTORS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."COFACTORS_AUDIT"  IS 'Audit table storing the history of table cofactors.';
--------------------------------------------------------
--  DDL for Table COMMENTS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMMENTS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"COMMENT_TEXT" VARCHAR2(2000 BYTE), 
	"ORDER_IN" NUMBER(3,0) DEFAULT 1, 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."COMMENTS"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS"."COMMENT_TEXT" IS 'Comment_text stores the comments on enzymes as free text field. Later the comment will get a detailed structure.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS"."ORDER_IN" IS 'The column order_in is of no use at the moment as a comment will be stored as a single text field.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS"."WEB_VIEW" IS 'Web view  in which to show the comment.';
 
   COMMENT ON TABLE "ENZYME"."COMMENTS"  IS 'Comments, table storing comments on enzymes. Comments are free text notes on an enzyme, and may be used to convey any useful information.';
--------------------------------------------------------
--  DDL for Table COMMENTS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMMENTS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"COMMENT_TEXT" VARCHAR2(2000 BYTE), 
	"ORDER_IN" NUMBER(3,0), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."COMMENT_TEXT" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."ORDER_IN" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."COMMENTS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for deletc.';
 
   COMMENT ON TABLE "ENZYME"."COMMENTS_AUDIT"  IS 'Audit table storing the history of table comments.';
--------------------------------------------------------
--  DDL for Table COMPLEX_REACTIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMPLEX_REACTIONS" 
   (	"PARENT_ID" NUMBER(7,0), 
	"CHILD_ID" NUMBER(7,0), 
	"ORDER_IN" NUMBER(3,0) DEFAULT 1, 
	"COEFFICIENT" NUMBER(1,0) DEFAULT 1
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."COMPLEX_REACTIONS"."PARENT_ID" IS 'Rhea ID of the overall (stepwise/coupling) reaction.';
 
   COMMENT ON COLUMN "ENZYME"."COMPLEX_REACTIONS"."CHILD_ID" IS 'Rhea ID of the step/coupled reaction.';
 
   COMMENT ON COLUMN "ENZYME"."COMPLEX_REACTIONS"."ORDER_IN" IS 'Order of the steps (only for multi-step reactions, not for coupled ones).';
 
   COMMENT ON COLUMN "ENZYME"."COMPLEX_REACTIONS"."COEFFICIENT" IS 'Stoichiometric coefficient applied to the whole step/coupled reaction.';
 
   COMMENT ON TABLE "ENZYME"."COMPLEX_REACTIONS"  IS 'Table connecting stepwise/coupling reactons with their steps/coupled reactions.';
--------------------------------------------------------
--  DDL for Table COMPLEX_REACTIONS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMPLEX_REACTIONS_AUDIT" 
   (	"PARENT_ID" NUMBER(7,0), 
	"CHILD_ID" NUMBER(7,0), 
	"ORDER_IN" NUMBER(3,0), 
	"COEFFICIENT" NUMBER(1,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(500 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."COMPLEX_REACTIONS_AUDIT"  IS 'Audit table for complex_reactions.';
--------------------------------------------------------
--  DDL for Table COMPOUND_DATA
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMPOUND_DATA" 
   (	"COMPOUND_ID" NUMBER(15,0), 
	"NAME" VARCHAR2(4000 BYTE), 
	"FORMULA" VARCHAR2(512 BYTE), 
	"CHARGE" NUMBER(2,0) DEFAULT 0, 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ACCESSION" VARCHAR2(256 BYTE), 
	"CHILD_ACCESSION" VARCHAR2(256 BYTE), 
	"PUBLISHED" CHAR(1 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."COMPOUND_ID" IS 'Internal compound ID (does not change with ChEBI updates).';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."NAME" IS 'Accepted Rhea name for the compound.';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."FORMULA" IS 'Chemical formula.';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."CHARGE" IS 'Electrical charge.';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."SOURCE" IS 'Source database of compound data.';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."ACCESSION" IS 'Accession number of the compound in the source database.';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."CHILD_ACCESSION" IS 'Accession of the child compound, in case it was merged in the source';
 
   COMMENT ON COLUMN "ENZYME"."COMPOUND_DATA"."PUBLISHED" IS 'State of the compound accession: publicly available or not';
 
   COMMENT ON TABLE "ENZYME"."COMPOUND_DATA"  IS 'Table storing essential data about compounds.';
--------------------------------------------------------
--  DDL for Table COMPOUND_DATA_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMPOUND_DATA_AUDIT" 
   (	"COMPOUND_ID" NUMBER(15,0), 
	"NAME" VARCHAR2(4000 BYTE), 
	"FORMULA" VARCHAR2(512 BYTE), 
	"CHARGE" NUMBER(2,0), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ACCESSION" VARCHAR2(256 BYTE), 
	"CHILD_ACCESSION" VARCHAR2(256 BYTE), 
	"PUBLISHED" CHAR(1 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."COMPOUND_DATA_AUDIT"  IS 'Audit table for compound_data.';
--------------------------------------------------------
--  DDL for Table COMPOUND_DATA_UPDATES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."COMPOUND_DATA_UPDATES" 
   (	"COMPOUND_ID" NUMBER(15,0), 
	"NAME" VARCHAR2(4000 BYTE), 
	"FORMULA" VARCHAR2(512 BYTE), 
	"CHARGE" NUMBER(2,0), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ACCESSION" VARCHAR2(256 BYTE), 
	"CHILD_ACCESSION" VARCHAR2(256 BYTE), 
	"PUBLISHED" CHAR(1 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."COMPOUND_DATA_UPDATES"  IS 'Table with updated data for existing compounds in compound_data table.';
--------------------------------------------------------
--  DDL for Table CV_COEFF_TYPES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_COEFF_TYPES" 
   (	"CODE" VARCHAR2(1 BYTE), 
	"EXPLANATION" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_COEFF_TYPES"."CODE" IS 'Internal database code.';
 
   COMMENT ON COLUMN "ENZYME"."CV_COEFF_TYPES"."EXPLANATION" IS 'Explanation of the coefficient type.';
 
   COMMENT ON TABLE "ENZYME"."CV_COEFF_TYPES"  IS 'Types of stoichiometric coefficients.';
--------------------------------------------------------
--  DDL for Table CV_COMP_PUB_AVAIL
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_COMP_PUB_AVAIL" 
   (	"CODE" CHAR(1 BYTE), 
	"EXPLANATION" VARCHAR2(110 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_COMP_PUB_AVAIL"."CODE" IS 'Code for the compound availability.';
 
   COMMENT ON COLUMN "ENZYME"."CV_COMP_PUB_AVAIL"."EXPLANATION" IS 'Description of the availability.';
 
   COMMENT ON TABLE "ENZYME"."CV_COMP_PUB_AVAIL"  IS 'Allowed values for compound availability.';
--------------------------------------------------------
--  DDL for Table CV_DATABASES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_DATABASES" 
   (	"CODE" VARCHAR2(6 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_DATABASES"."CODE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''S'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES"."NAME" IS 'Name of the database, for example ''INTENZ'', ''IUBMB'', ''SWISSPROT'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES"."DISPLAY_NAME" IS 'Displayed name of the database, for example ''IntEnz'', ''IUBMB enzyme list'', ''SWISS-PROT'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES"."SORT_ORDER" IS 'Display order to get stable output.';
 
   COMMENT ON TABLE "ENZYME"."CV_DATABASES"  IS 'Controlled list of external databases, for example IntEnz, IUBMB, SWISS-PROT, etc.';
--------------------------------------------------------
--  DDL for Table CV_DATABASES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_DATABASES_AUDIT" 
   (	"CODE" VARCHAR2(6 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."CODE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."DISPLAY_NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."SORT_ORDER" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CV_DATABASES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CV_DATABASES_AUDIT"  IS 'Audit table storing the history of table cv_databases.';
--------------------------------------------------------
--  DDL for Table CV_LOCATION
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_LOCATION" 
   (	"CODE" VARCHAR2(1 BYTE), 
	"TEXT" VARCHAR2(10 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_LOCATION"."CODE" IS 'Internal code for the location.';
 
   COMMENT ON COLUMN "ENZYME"."CV_LOCATION"."TEXT" IS 'Human readable location.';
 
   COMMENT ON TABLE "ENZYME"."CV_LOCATION"  IS 'Controlled vocabulary for relative locations of reaction participants.';
--------------------------------------------------------
--  DDL for Table CV_NAME_CLASSES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_NAME_CLASSES" 
   (	"CODE" VARCHAR2(3 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES"."CODE" IS 'Internal code of name_class, e.g. ''SYS'' for systematic name.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES"."NAME" IS 'Name of name_class, e.g. ''SYSTEMATIC'' for systematic name.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES"."DISPLAY_NAME" IS 'Displayed name of name_class, e.g. ''systematic''.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES"."SORT_ORDER" IS 'Display order to get stable output.';
 
   COMMENT ON TABLE "ENZYME"."CV_NAME_CLASSES"  IS 'Controlled list of values for name classes in table names. There are three types of names: common name, systematic name, and other name(s).';
--------------------------------------------------------
--  DDL for Table CV_NAME_CLASSES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_NAME_CLASSES_AUDIT" 
   (	"CODE" VARCHAR2(3 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."CODE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."DISPLAY_NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."SORT_ORDER" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CV_NAME_CLASSES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CV_NAME_CLASSES_AUDIT"  IS 'Audit table storing the history of table cv_name_classes.';
--------------------------------------------------------
--  DDL for Table CV_OPERATORS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_OPERATORS" 
   (	"CODE" VARCHAR2(5 BYTE), 
	"EXPLANATION" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_OPERATORS"."CODE" IS 'Internal code for the operator.';
 
   COMMENT ON COLUMN "ENZYME"."CV_OPERATORS"."EXPLANATION" IS 'Description of the operator.';
 
   COMMENT ON TABLE "ENZYME"."CV_OPERATORS"  IS 'CV for operators applied to combinations of cofactors.';
--------------------------------------------------------
--  DDL for Table CV_REACTION_DIRECTIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_REACTION_DIRECTIONS" 
   (	"DIRECTION" VARCHAR2(20 BYTE), 
	"CODE" VARCHAR2(2 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_REACTION_DIRECTIONS"."DIRECTION" IS 'Textual value of the directionality.';
 
   COMMENT ON COLUMN "ENZYME"."CV_REACTION_DIRECTIONS"."CODE" IS 'Internal code for the directionality.';
 
   COMMENT ON TABLE "ENZYME"."CV_REACTION_DIRECTIONS"  IS 'CV for reaction directions.';
--------------------------------------------------------
--  DDL for Table CV_REACTION_QUALIFIERS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_REACTION_QUALIFIERS" 
   (	"QUALIFIER" VARCHAR2(20 BYTE), 
	"CODE" VARCHAR2(2 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_REACTION_QUALIFIERS"."QUALIFIER" IS 'Textual value of the qualifier.';
 
   COMMENT ON COLUMN "ENZYME"."CV_REACTION_QUALIFIERS"."CODE" IS 'Internal code for the qualifier.';
 
   COMMENT ON TABLE "ENZYME"."CV_REACTION_QUALIFIERS"  IS 'CV for qualifiers applied to reactions.';
--------------------------------------------------------
--  DDL for Table CV_REACTION_SIDES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_REACTION_SIDES" 
   (	"CODE" VARCHAR2(1 BYTE), 
	"SIDE" VARCHAR2(5 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_REACTION_SIDES"."CODE" IS 'Internal code for the reaction side.';
 
   COMMENT ON COLUMN "ENZYME"."CV_REACTION_SIDES"."SIDE" IS 'Textual value of the reaction side.';
 
   COMMENT ON TABLE "ENZYME"."CV_REACTION_SIDES"  IS 'CV for reaction sides.';
--------------------------------------------------------
--  DDL for Table CV_STATUS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_STATUS" 
   (	"CODE" VARCHAR2(2 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_STATUS"."CODE" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS"."NAME" IS 'Name of status, for example ''NONE'', ''UNKNOWN'', ''SUGGESTED NEW'', ''APPROVED'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS"."DISPLAY_NAME" IS 'Displayed name of status, for example ''no status'', ''unknown'', ''suggested'', ''approved'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS"."SORT_ORDER" IS 'Display order to get stable output.';
 
   COMMENT ON TABLE "ENZYME"."CV_STATUS"  IS 'Controlled list of values for ''status'' columns, e.g. approved, proposed, suggested, etc.';
--------------------------------------------------------
--  DDL for Table CV_STATUS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_STATUS_AUDIT" 
   (	"CODE" VARCHAR2(2 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."CODE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."DISPLAY_NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."SORT_ORDER" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CV_STATUS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CV_STATUS_AUDIT"  IS 'Audit table storing the history of table cv_status.';
--------------------------------------------------------
--  DDL for Table CV_VIEW
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_VIEW" 
   (	"CODE" VARCHAR2(20 BYTE), 
	"DESCRIPTION" VARCHAR2(1000 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_VIEW"."CODE" IS 'The code defining in which view the information will be displayed.';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW"."DESCRIPTION" IS 'A short description about the code.';
 
   COMMENT ON TABLE "ENZYME"."CV_VIEW"  IS 'Stores all possible view codes used by the web application.';
--------------------------------------------------------
--  DDL for Table CV_VIEW_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_VIEW_AUDIT" 
   (	"CODE" VARCHAR2(20 BYTE), 
	"DESCRIPTION" VARCHAR2(1000 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."CODE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."DESCRIPTION" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CV_VIEW_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CV_VIEW_AUDIT"  IS 'Audit table storing the history of table cv_view.';
--------------------------------------------------------
--  DDL for Table CV_WARNINGS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_WARNINGS" 
   (	"CODE" VARCHAR2(3 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS"."CODE" IS 'Internal code of warning, e.g. ''OBS'' for obsolete.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS"."NAME" IS 'Name of warning, e.g. ''OBSOLETE'' for obsolete.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS"."DISPLAY_NAME" IS 'Displayed name of warning, e.g. ''obsolete''.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS"."SORT_ORDER" IS 'Display order to get stable output.';
 
   COMMENT ON TABLE "ENZYME"."CV_WARNINGS"  IS 'Controlled list of values for warning column in table names, e.g. ''ambiguous'', ''misleading'', ''obsolete''.';
--------------------------------------------------------
--  DDL for Table CV_WARNINGS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."CV_WARNINGS_AUDIT" 
   (	"CODE" VARCHAR2(3 BYTE), 
	"NAME" VARCHAR2(30 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"SORT_ORDER" NUMBER(2,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."CODE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."DISPLAY_NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."SORT_ORDER" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."CV_WARNINGS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."CV_WARNINGS_AUDIT"  IS 'Audit table storing the history of table cv_warnings.';
--------------------------------------------------------
--  DDL for Table DR$intenz_text_idx$text$I
--------------------------------------------------------

  CREATE TABLE "ENZYME"."DR$intenz_text_idx$text$I" 
   (	"TOKEN_TEXT" VARCHAR2(64 BYTE), 
	"TOKEN_TYPE" NUMBER(3,0), 
	"TOKEN_FIRST" NUMBER(10,0), 
	"TOKEN_LAST" NUMBER(10,0), 
	"TOKEN_COUNT" NUMBER(10,0), 
	"TOKEN_INFO" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table DR$intenz_text_idx$text$K
--------------------------------------------------------

  CREATE TABLE "ENZYME"."DR$intenz_text_idx$text$K" 
   (	"DOCID" NUMBER(38,0), 
	"TEXTKEY" ROWID, 
	 PRIMARY KEY ("TEXTKEY") ENABLE
   ) ORGANIZATION INDEX NOCOMPRESS ;
--------------------------------------------------------
--  DDL for Table DR$intenz_text_idx$text$N
--------------------------------------------------------

  CREATE TABLE "ENZYME"."DR$intenz_text_idx$text$N" 
   (	"NLT_DOCID" NUMBER(38,0), 
	"NLT_MARK" CHAR(1 BYTE), 
	 PRIMARY KEY ("NLT_DOCID") ENABLE
   ) ORGANIZATION INDEX NOCOMPRESS ;
--------------------------------------------------------
--  DDL for Table DR$intenz_text_idx$text$R
--------------------------------------------------------

  CREATE TABLE "ENZYME"."DR$intenz_text_idx$text$R" 
   (	"ROW_NO" NUMBER(3,0), 
	"DATA" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table ENZYMES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."ENZYMES" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"EC1" NUMBER(1,0), 
	"EC2" NUMBER(2,0), 
	"EC3" NUMBER(3,0), 
	"EC4" NUMBER(4,0), 
	"HISTORY" VARCHAR2(400 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"NOTE" VARCHAR2(2000 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE) DEFAULT 'Y'
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."ENZYMES"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."EC1" IS 'The first number, ec1, is the number of the class, example ''3''.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."EC2" IS 'The second number, ec2, defines the subclass inside the class, example ''3.1''.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."EC3" IS 'The third number, ec3, defines the subsubclass inside the subclass, example ''3.1.1''.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."EC4" IS 'The fourth number, ec4, is a serial number of the enzyme inside the subsubclass, example ''3.1.1.1''.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."HISTORY" IS 'Short form of history of an enzyme, especially date of creation, e.g. ''EC 1.1.1.1 created 1961'', ''EC 1.1.3.8 created 1965, modified 2001''.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."NOTE" IS 'Comment or note of an enzyme annotator which wont be viewable for the public.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES"."ACTIVE" IS 'IUBMB uses ''deleted'' for different meanings. We say ''active'' for enzymes that are currently recommended by IUBMB. Opposit of inactive';
 
   COMMENT ON TABLE "ENZYME"."ENZYMES"  IS 'Enzymes, main table storing enzymes.';
--------------------------------------------------------
--  DDL for Table ENZYMES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."ENZYMES_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"EC1" NUMBER(1,0), 
	"EC2" NUMBER(2,0), 
	"EC3" NUMBER(3,0), 
	"EC4" NUMBER(4,0), 
	"HISTORY" VARCHAR2(400 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"NOTE" VARCHAR2(2000 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."EC1" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."EC2" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."EC3" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."EC4" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."HISTORY" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."NOTE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."ACTIVE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."ENZYMES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."ENZYMES_AUDIT"  IS 'Audit table storing the history of table enzymes.';
--------------------------------------------------------
--  DDL for Table FUTURE_EVENTS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."FUTURE_EVENTS" 
   (	"GROUP_ID" NUMBER(7,0), 
	"EVENT_ID" NUMBER(7,0), 
	"BEFORE_ID" NUMBER(7,0), 
	"AFTER_ID" NUMBER(7,0), 
	"EVENT_YEAR" DATE DEFAULT SYSDATE, 
	"EVENT_NOTE" VARCHAR2(2000 BYTE), 
	"EVENT_CLASS" VARCHAR2(20 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"TIMEOUT_ID" NUMBER(7,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."FUTURE_EVENTS"."BEFORE_ID" IS 'Internal enzyme entry ID object of the event.';
 
   COMMENT ON COLUMN "ENZYME"."FUTURE_EVENTS"."AFTER_ID" IS 'Final ID (if it changes) of the enzyme entry after the event.';
 
   COMMENT ON COLUMN "ENZYME"."FUTURE_EVENTS"."EVENT_CLASS" IS 'Type of event (modification, transfer, deletion).';
 
   COMMENT ON COLUMN "ENZYME"."FUTURE_EVENTS"."TIMEOUT_ID" IS 'ID of the timeout for the schedulled event.';
 
   COMMENT ON TABLE "ENZYME"."FUTURE_EVENTS"  IS 'Schedulled events (modifications, transfers, deletions of EC numbers).';
--------------------------------------------------------
--  DDL for Table FUTURE_EVENTS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."FUTURE_EVENTS_AUDIT" 
   (	"GROUP_ID" NUMBER(7,0), 
	"EVENT_ID" NUMBER(7,0), 
	"BEFORE_ID" NUMBER(7,0), 
	"AFTER_ID" NUMBER(7,0), 
	"EVENT_YEAR" DATE, 
	"EVENT_NOTE" VARCHAR2(2000 BYTE), 
	"EVENT_CLASS" VARCHAR2(20 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"TIMEOUT_ID" NUMBER(7,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."FUTURE_EVENTS_AUDIT"  IS 'Audit table for the FUTURE_EVENTS table.';
--------------------------------------------------------
--  DDL for Table HISTORY_EVENTS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."HISTORY_EVENTS" 
   (	"GROUP_ID" NUMBER(7,0), 
	"EVENT_ID" NUMBER(7,0), 
	"BEFORE_ID" NUMBER(7,0), 
	"AFTER_ID" NUMBER(7,0), 
	"EVENT_YEAR" DATE DEFAULT SYSDATE, 
	"EVENT_NOTE" VARCHAR2(2000 BYTE), 
	"EVENT_CLASS" VARCHAR2(20 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."HISTORY_EVENTS"  IS 'Past events (modifications, transfers, deletions of EC numbers).';
--------------------------------------------------------
--  DDL for Table HISTORY_EVENTS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."HISTORY_EVENTS_AUDIT" 
   (	"GROUP_ID" NUMBER(7,0), 
	"EVENT_ID" NUMBER(7,0), 
	"BEFORE_ID" NUMBER(7,0), 
	"AFTER_ID" NUMBER(7,0), 
	"EVENT_YEAR" DATE, 
	"EVENT_NOTE" VARCHAR2(2000 BYTE), 
	"EVENT_CLASS" VARCHAR2(20 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."HISTORY_EVENTS_AUDIT"  IS 'Audit table for HISTORY_EVENTS table.';
--------------------------------------------------------
--  DDL for Table ID2EC
--------------------------------------------------------

  CREATE TABLE "ENZYME"."ID2EC" 
   (	"ENZYME_ID" VARCHAR2(10 BYTE), 
	"EC" VARCHAR2(20 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table INTENZ_REACTIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."INTENZ_REACTIONS" 
   (	"REACTION_ID" NUMBER(7,0), 
	"INTENZ_ACCESSION" VARCHAR2(10 BYTE), 
	"EQUATION" VARCHAR2(3000 BYTE), 
	"FINGERPRINT" VARCHAR2(3000 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"DIRECTION" VARCHAR2(2 BYTE) DEFAULT 'UN', 
	"UN_REACTION" NUMBER(7,0), 
	"QUALIFIERS" "ENZYME"."REACTION_QUALIFIERS" , 
	"DATA_COMMENT" VARCHAR2(3000 BYTE), 
	"REACTION_COMMENT" VARCHAR2(3000 BYTE), 
	"IUBMB" VARCHAR2(1 BYTE) DEFAULT 'N', 
	"PUBLIC_IN_CHEBI" VARCHAR2(1 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."REACTION_ID" IS 'Rhea reaction ID (stable).';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."EQUATION" IS 'Textual representation of the reaction.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."FINGERPRINT" IS 'Fingerprint used to identify unique reactions in the database.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."STATUS" IS 'Reaction status.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."SOURCE" IS 'Source of the reaction data.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."DIRECTION" IS 'Reaction directionality.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."UN_REACTION" IS 'This is the unspecified-direction reaction ID related to left-to-right, right-to-left and bidirectional reactions. UN_REACTION is NULL for unspecified-direction reactions.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."QUALIFIERS" IS 'Any qualifiers applied to the reaction.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."DATA_COMMENT" IS 'Comment on the data, for internal use only.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."REACTION_COMMENT" IS 'Public comment.';
 
   COMMENT ON COLUMN "ENZYME"."INTENZ_REACTIONS"."PUBLIC_IN_CHEBI" IS 'Visibility of the reaction according to the public status of its participants in ChEBI: P (public) | N (not public).';
 
   COMMENT ON TABLE "ENZYME"."INTENZ_REACTIONS"  IS 'Main Rhea table, initially populated with reactions parsed from IntEnz textual equations.';
--------------------------------------------------------
--  DDL for Table INTENZ_REACTIONS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."INTENZ_REACTIONS_AUDIT" 
   (	"REACTION_ID" NUMBER(7,0), 
	"INTENZ_ACCESSION" VARCHAR2(10 BYTE), 
	"EQUATION" VARCHAR2(3000 BYTE), 
	"FINGERPRINT" VARCHAR2(3000 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"DIRECTION" VARCHAR2(2 BYTE), 
	"UN_REACTION" NUMBER(7,0), 
	"QUALIFIERS" "ENZYME"."REACTION_QUALIFIERS" , 
	"DATA_COMMENT" VARCHAR2(3000 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(4000 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"REACTION_COMMENT" VARCHAR2(3000 BYTE), 
	"IUBMB" VARCHAR2(1 BYTE), 
	"PUBLIC_IN_CHEBI" VARCHAR2(1 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."INTENZ_REACTIONS_AUDIT"  IS 'Audit table for INTENZ_REACTIONS.';
--------------------------------------------------------
--  DDL for Table INTENZ_TEXT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."INTENZ_TEXT" 
   (	"ENZYME_ID" VARCHAR2(10 BYTE), 
	"EC" VARCHAR2(20 BYTE), 
	"COMMON_NAME" VARCHAR2(1000 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"TEXT_ORDER" NUMBER(4,0), 
	"TEXT" VARCHAR2(4000 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table LINKS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."LINKS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"URL" VARCHAR2(100 BYTE), 
	"DISPLAY_NAME" VARCHAR2(100 BYTE), 
	"STATUS" VARCHAR2(2 BYTE) DEFAULT 'NO', 
	"SOURCE" VARCHAR2(6 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"DATA_COMMENT" VARCHAR2(1000 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."LINKS"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS"."URL" IS 'Url stores the links from an enzyme number to another database which is stored on Gerry Moss'' webpages as ''Links to other databases'', e.g. a link to BRENDA from 1.1.1.1 ''http://brenda.bc.uni-koeln.de/php/result_flat.php3?ecno=1.1.1.1''. ';
 
   COMMENT ON COLUMN "ENZYME"."LINKS"."DISPLAY_NAME" IS 'Displayed name of links, e.g. ''BRENDA'', ''CAS'', ''WIT''.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS"."WEB_VIEW" IS 'View to display the link in.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS"."DATA_COMMENT" IS 'Any comment on the link.';
 
   COMMENT ON TABLE "ENZYME"."LINKS"  IS 'Links, table storing internet documents linked from an enzyme';
--------------------------------------------------------
--  DDL for Table LINKS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."LINKS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"URL" VARCHAR2(200 BYTE), 
	"DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"DATA_COMMENT" VARCHAR2(1000 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."URL" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."DISPLAY_NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."TIMESTAMP" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."LINKS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."LINKS_AUDIT"  IS 'Audit table storing the history of table links.';
--------------------------------------------------------
--  DDL for Table NAMES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."NAMES" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"NAME" VARCHAR2(1000 BYTE), 
	"NAME_CLASS" VARCHAR2(3 BYTE), 
	"WARNING" VARCHAR2(3 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"NOTE" VARCHAR2(200 BYTE), 
	"ORDER_IN" NUMBER(4,0), 
	"WEB_VIEW" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."NAMES"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."NAME" IS 'Name stores the actual name of an enzyme which could be a systematic name, a common name, or an other/alternate name, e.g. ''alcohol:NAD oxidoreductase'', ''alcohol dehydrogenase'', ''aldehyde reductase'' (taken from 1.1.1.1 - IUBMB).';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."NAME_CLASS" IS 'There are three types of names or name_classes: common name, systematic name, and other name(s).';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."WARNING" IS 'Some names of an enzyme are ambiguous, misleading, obsolete, which is stored as ''AMB'', ''MIS'', ''OBS''.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."NOTE" IS 'Ambiguous, misleading, obsolete names have sometimes a comment attached to them which can be stored in this column.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."ORDER_IN" IS 'Order applied to names for the same enzyme.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES"."WEB_VIEW" IS 'View to display the name in.';
 
   COMMENT ON TABLE "ENZYME"."NAMES"  IS 'Names of enzymes, including common name, systematic name, and other names.';
--------------------------------------------------------
--  DDL for Table NAMES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."NAMES_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"NAME" VARCHAR2(1000 BYTE), 
	"NAME_CLASS" VARCHAR2(3 BYTE), 
	"WARNING" VARCHAR2(3 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"NOTE" VARCHAR2(200 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"ORDER_IN" NUMBER(4,0), 
	"WEB_VIEW" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."NAME_CLASS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."WARNING" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."NOTE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."NAMES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."NAMES_AUDIT"  IS 'Audit table storing the history of table names.';
--------------------------------------------------------
--  DDL for Table PUBLICATIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."PUBLICATIONS" 
   (	"PUB_ID" NUMBER(7,0), 
	"MEDLINE_ID" NUMBER(9,0), 
	"PUBMED_ID" NUMBER(8,0), 
	"PUB_TYPE" CHAR(1 BYTE), 
	"AUTHOR" VARCHAR2(2000 BYTE), 
	"PUB_YEAR" NUMBER(4,0), 
	"TITLE" VARCHAR2(1000 BYTE), 
	"JOURNAL_BOOK" VARCHAR2(1000 BYTE), 
	"VOLUME" VARCHAR2(7 BYTE), 
	"FIRST_PAGE" VARCHAR2(12 BYTE), 
	"LAST_PAGE" VARCHAR2(12 BYTE), 
	"EDITION" NUMBER(3,0), 
	"EDITOR" VARCHAR2(2000 BYTE), 
	"PUB_COMPANY" VARCHAR2(50 BYTE), 
	"PUB_PLACE" VARCHAR2(50 BYTE), 
	"URL" VARCHAR2(1000 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"SOURCE" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."PUB_ID" IS 'Pub_id is used as an internal unique identifier for publications. Example is ''1'', ''253'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."MEDLINE_ID" IS 'Medline_id is the unique identifier used in Medline. Example is ''73143748'', ''68240400''.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."PUBMED_ID" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."PUB_TYPE" IS 'Pub_type stores the information whether the publication is a book or a journal';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."AUTHOR" IS 'Author stores all the authors listed in one publication.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."PUB_YEAR" IS 'Year indicates the year a publication was published.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."TITLE" IS 'Title gives the title of an publication.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."JOURNAL_BOOK" IS 'Journal_book lists the title of the book or journal of the publication.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."VOLUME" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."FIRST_PAGE" IS 'First_page lists the first page of a publication.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."LAST_PAGE" IS 'Last_page lists the last page of a publication.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."EDITION" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."EDITOR" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."PUB_COMPANY" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."PUB_PLACE" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."URL" IS 'Self explaining.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."WEB_VIEW" IS 'View to display the publication in.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS"."SOURCE" IS 'Source of the publication data.';
 
   COMMENT ON TABLE "ENZYME"."PUBLICATIONS"  IS 'Publications, table storing articles in books and journals where at least one EC number is mentioned.';
--------------------------------------------------------
--  DDL for Table PUBLICATIONS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."PUBLICATIONS_AUDIT" 
   (	"PUB_ID" NUMBER(7,0), 
	"MEDLINE_ID" NUMBER(9,0), 
	"PUBMED_ID" NUMBER(8,0), 
	"PUB_TYPE" CHAR(1 BYTE), 
	"AUTHOR" VARCHAR2(2000 BYTE), 
	"PUB_YEAR" NUMBER(4,0), 
	"TITLE" VARCHAR2(1000 BYTE), 
	"JOURNAL_BOOK" VARCHAR2(1000 BYTE), 
	"VOLUME" VARCHAR2(7 BYTE), 
	"FIRST_PAGE" VARCHAR2(12 BYTE), 
	"LAST_PAGE" VARCHAR2(12 BYTE), 
	"EDITION" NUMBER(3,0), 
	"EDITOR" VARCHAR2(2000 BYTE), 
	"PUB_COMPANY" VARCHAR2(50 BYTE), 
	"PUB_PLACE" VARCHAR2(50 BYTE), 
	"URL" VARCHAR2(1000 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"SOURCE" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."PUB_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."MEDLINE_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."PUBMED_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."PUB_TYPE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."AUTHOR" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."PUB_YEAR" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."TITLE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."JOURNAL_BOOK" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."VOLUME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."FIRST_PAGE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."LAST_PAGE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."EDITION" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."EDITOR" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."PUB_COMPANY" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."PUB_PLACE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."URL" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."PUBLICATIONS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."PUBLICATIONS_AUDIT"  IS 'Audit table storing the history of table publications.';
--------------------------------------------------------
--  DDL for Table REACTIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTIONS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"EQUATION" VARCHAR2(3000 BYTE), 
	"ORDER_IN" NUMBER(2,0) DEFAULT 1, 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTIONS"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS"."EQUATION" IS 'Equation stores the reaction/catalytic activity of an enzyme, e.g. ''an alcohol + NAD = an aldehyde or ketone + NADH2'' (taken from 1.1.1.1 - IUBMB). In some cases free text is used to describe a reaction, e.g. ';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS"."ORDER_IN" IS 'Order_in stores the order of reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS"."WEB_VIEW" IS 'View to display the reaction in.';
 
   COMMENT ON TABLE "ENZYME"."REACTIONS"  IS 'Plain text reactions of enzymes. An enzyme can have more than one reaction.';
--------------------------------------------------------
--  DDL for Table REACTIONS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTIONS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"EQUATION" VARCHAR2(3000 BYTE), 
	"ORDER_IN" NUMBER(2,0), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."EQUATION" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."ORDER_IN" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."REACTIONS_AUDIT"  IS 'Audit table storing the history of table reactions.';
--------------------------------------------------------
--  DDL for Table REACTIONS_MAP
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTIONS_MAP" 
   (	"REACTION_ID" NUMBER(7,0), 
	"ENZYME_ID" NUMBER(7,0), 
	"WEB_VIEW" VARCHAR2(12 BYTE), 
	"ORDER_IN" NUMBER(2,0) DEFAULT 1
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTIONS_MAP"."REACTION_ID" IS 'Rhea ID of the reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_MAP"."ENZYME_ID" IS 'Internal enzyme ID.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_MAP"."WEB_VIEW" IS 'View in which to show the mapping.';
 
   COMMENT ON COLUMN "ENZYME"."REACTIONS_MAP"."ORDER_IN" IS 'Order of the reactions, if more than one per enzyme.';
 
   COMMENT ON TABLE "ENZYME"."REACTIONS_MAP"  IS 'Connection table between enzymes and intenz_reactions.';
--------------------------------------------------------
--  DDL for Table REACTIONS_MAP_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTIONS_MAP_AUDIT" 
   (	"REACTION_ID" NUMBER(7,0), 
	"ENZYME_ID" NUMBER(7,0), 
	"WEB_VIEW" VARCHAR2(12 BYTE), 
	"ORDER_IN" NUMBER(2,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."REACTIONS_MAP_AUDIT"  IS 'Audit table for reactions_map.';
--------------------------------------------------------
--  DDL for Table REACTION_CITATIONS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_CITATIONS" 
   (	"REACTION_ID" NUMBER(7,0), 
	"PUB_ID" VARCHAR2(10 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ORDER_IN" NUMBER(3,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTION_CITATIONS"."REACTION_ID" IS 'Rhea ID of the cited reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_CITATIONS"."PUB_ID" IS 'ID of the publication citing the reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_CITATIONS"."SOURCE" IS 'Source database of the citation.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_CITATIONS"."ORDER_IN" IS 'Order applied to citations for the same reaction. May or may not be used.';
 
   COMMENT ON TABLE "ENZYME"."REACTION_CITATIONS"  IS 'Bibliographic citations for reactions.';
--------------------------------------------------------
--  DDL for Table REACTION_CITATIONS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_CITATIONS_AUDIT" 
   (	"REACTION_ID" NUMBER(7,0), 
	"PUB_ID" VARCHAR2(10 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"ORDER_IN" NUMBER(3,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(500 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."REACTION_CITATIONS_AUDIT"  IS 'Audit table for reaction citations.';
--------------------------------------------------------
--  DDL for Table REACTION_MERGINGS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_MERGINGS" 
   (	"FROM_ID" NUMBER(7,0), 
	"TO_ID" NUMBER(7,0), 
	"MERGING_COMMENT" VARCHAR2(3000 BYTE), 
	"MERGING_WHO" VARCHAR2(30 BYTE), 
	"MERGING_WHEN" DATE
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTION_MERGINGS"."FROM_ID" IS 'ID of the superseded reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_MERGINGS"."TO_ID" IS 'ID of the prevailing reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_MERGINGS"."MERGING_COMMENT" IS 'Explanation for the merging.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_MERGINGS"."MERGING_WHO" IS 'User merging reactions.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_MERGINGS"."MERGING_WHEN" IS 'Date and time of merging.';
 
   COMMENT ON TABLE "ENZYME"."REACTION_MERGINGS"  IS 'Table keeping track of merged reactions.';
--------------------------------------------------------
--  DDL for Table REACTION_PARTICIPANTS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_PARTICIPANTS" 
   (	"REACTION_ID" NUMBER(7,0), 
	"COMPOUND_ID" NUMBER(15,0), 
	"SIDE" VARCHAR2(1 BYTE), 
	"COEFFICIENT" NUMBER(2,0) DEFAULT 1, 
	"COEFF_TYPE" VARCHAR2(1 BYTE) DEFAULT 'F', 
	"LOCATION" VARCHAR2(1 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTION_PARTICIPANTS"."REACTION_ID" IS 'Rhea ID of the reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_PARTICIPANTS"."COMPOUND_ID" IS 'Internal compound ID of the reaction participant.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_PARTICIPANTS"."SIDE" IS 'Side of the reaction.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_PARTICIPANTS"."COEFFICIENT" IS 'Stoichiometric coefficient applied to the reaction participant.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_PARTICIPANTS"."COEFF_TYPE" IS 'Type of stoichiometric coefficient.';
 
   COMMENT ON TABLE "ENZYME"."REACTION_PARTICIPANTS"  IS 'Connection table between compound_data and intenz_reactions.';
--------------------------------------------------------
--  DDL for Table REACTION_PARTICIPANTS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_PARTICIPANTS_AUDIT" 
   (	"REACTION_ID" NUMBER(7,0), 
	"COMPOUND_ID" NUMBER(15,0), 
	"SIDE" VARCHAR2(1 BYTE), 
	"COEFFICIENT" NUMBER(2,0), 
	"COEFF_TYPE" VARCHAR2(1 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"LOCATION" VARCHAR2(1 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."REACTION_PARTICIPANTS_AUDIT"  IS 'Audit table for reaction_participants.';
--------------------------------------------------------
--  DDL for Table REACTION_XREFS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_XREFS" 
   (	"REACTION_ID" NUMBER(7,0), 
	"DB_CODE" VARCHAR2(6 BYTE), 
	"DB_ACCESSION" VARCHAR2(500 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."REACTION_XREFS"."REACTION_ID" IS 'Rhea ID';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_XREFS"."DB_CODE" IS 'Code of the referenced database.';
 
   COMMENT ON COLUMN "ENZYME"."REACTION_XREFS"."DB_ACCESSION" IS 'Accession number of the cross-reference.';
 
   COMMENT ON TABLE "ENZYME"."REACTION_XREFS"  IS 'Reaction cross-references to other databases.';
--------------------------------------------------------
--  DDL for Table REACTION_XREFS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."REACTION_XREFS_AUDIT" 
   (	"REACTION_ID" NUMBER(7,0), 
	"DB_CODE" VARCHAR2(9 BYTE), 
	"DB_ACCESSION" VARCHAR2(500 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(500 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."REACTION_XREFS_AUDIT"  IS 'Audit table for reaction_xrefs.';
--------------------------------------------------------
--  DDL for Table RELEASES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."RELEASES" 
   (	"DB" VARCHAR2(6 BYTE), 
	"REL_NO" NUMBER(3,0), 
	"REL_DATE" DATE
   ) ;
 

   COMMENT ON TABLE "ENZYME"."RELEASES"  IS 'Release history for the databases in this schema. The higher figure reflects the current public release.';
--------------------------------------------------------
--  DDL for Table ROLES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."ROLES" 
   (	"USERNAME" VARCHAR2(20 BYTE), 
	"ROLE" VARCHAR2(20 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table STATS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."STATS" 
   (	"STATID" VARCHAR2(30 BYTE), 
	"TYPE" CHAR(1 BYTE), 
	"VERSION" NUMBER, 
	"FLAGS" NUMBER, 
	"C1" VARCHAR2(30 BYTE), 
	"C2" VARCHAR2(30 BYTE), 
	"C3" VARCHAR2(30 BYTE), 
	"C4" VARCHAR2(30 BYTE), 
	"C5" VARCHAR2(30 BYTE), 
	"N1" NUMBER, 
	"N2" NUMBER, 
	"N3" NUMBER, 
	"N4" NUMBER, 
	"N5" NUMBER, 
	"N6" NUMBER, 
	"N7" NUMBER, 
	"N8" NUMBER, 
	"N9" NUMBER, 
	"N10" NUMBER, 
	"N11" NUMBER, 
	"N12" NUMBER, 
	"D1" DATE, 
	"R1" RAW(32), 
	"R2" RAW(32), 
	"CH1" VARCHAR2(1000 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table SUBCLASSES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."SUBCLASSES" 
   (	"EC1" NUMBER(1,0), 
	"EC2" NUMBER(2,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE) DEFAULT 'Y'
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."SUBCLASSES"."EC1" IS 'The first number, ec1, is the number of the class, example ''3''.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES"."EC2" IS 'The second number, ec2, defines the subclass inside the class, example ''3.1''.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES"."NAME" IS 'Name of the subclass, example ''Acting on ester bonds''. This name makes only sense inside it''s class.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES"."DESCRIPTION" IS 'Description is a short paragraph explaining the contents of the subclass.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES"."ACTIVE" IS 'IUBMB uses ''deleted'' for different meanings. We say ''active'' for enzymes that are currently recommended by IUBMB. Opposit of inactive';
 
   COMMENT ON TABLE "ENZYME"."SUBCLASSES"  IS 'Subclasses, second level of the enzyme hierachy. A subclass is always part of a class. For example, the subclass EC 3.1 ''Acting on ester bonds'' is a child of the class EC 3 ''Hydrolases''. ';
--------------------------------------------------------
--  DDL for Table SUBCLASSES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."SUBCLASSES_AUDIT" 
   (	"EC1" NUMBER(1,0), 
	"EC2" NUMBER(2,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."EC1" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."EC2" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."DESCRIPTION" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."ACTIVE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."SUBCLASSES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."SUBCLASSES_AUDIT"  IS 'Audit table storing the history of table subclasses.';
--------------------------------------------------------
--  DDL for Table SUBSUBCLASSES
--------------------------------------------------------

  CREATE TABLE "ENZYME"."SUBSUBCLASSES" 
   (	"EC1" NUMBER(1,0), 
	"EC2" NUMBER(2,0), 
	"EC3" NUMBER(3,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE) DEFAULT 'Y'
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES"."EC1" IS 'The first number is the number of the class, example ''3''.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES"."EC2" IS 'The second number, ec2, defines the subclass inside the class, example ''3.1''.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES"."EC3" IS 'The third number, ec3, defines the subsubclass inside the subclass, example ''3.1.1''.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES"."NAME" IS 'Name, example ''Carboxylic ester hydrolases''. This name makes only sense inside it''s subclass.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES"."DESCRIPTION" IS 'Description is a short paragraph explaining the contents of the subsubclass.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES"."ACTIVE" IS 'IUBMB uses ''deleted'' for different meanings. We say ''active'' for enzymes that are currently recommended by IUBMB. Opposit of inactive';
 
   COMMENT ON TABLE "ENZYME"."SUBSUBCLASSES"  IS 'Subsubclasses, third level of the enzyme hierachy. A subsubclass is always part of a subclass. For example, the subsubclass EC 3.1.1 ''Carboxylic ester hydrolases'' is a child of subclass EC 3.1 ''Acting on ester bonds''.';
--------------------------------------------------------
--  DDL for Table SUBSUBCLASSES_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."SUBSUBCLASSES_AUDIT" 
   (	"EC1" NUMBER(1,0), 
	"EC2" NUMBER(2,0), 
	"EC3" NUMBER(3,0), 
	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."EC1" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."EC2" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."EC3" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."DESCRIPTION" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."ACTIVE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."SUBSUBCLASSES_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."SUBSUBCLASSES_AUDIT"  IS 'Audit table storing the history of table subsubclasses.';
--------------------------------------------------------
--  DDL for Table TIMEOUTS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."TIMEOUTS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"START_DATE" DATE, 
	"DUE_DATE" DATE, 
	"TIMEOUT_ID" NUMBER(7,0)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."TIMEOUTS"."ENZYME_ID" IS 'Suggested or proposed enzyme.';
 
   COMMENT ON COLUMN "ENZYME"."TIMEOUTS"."START_DATE" IS 'Date and time when enzyme was suggested or proposed.';
 
   COMMENT ON COLUMN "ENZYME"."TIMEOUTS"."DUE_DATE" IS 'Date and time when this suggestion or proposal will be automatically accepted. Should be two month after start_date in normal cases.';
 
   COMMENT ON TABLE "ENZYME"."TIMEOUTS"  IS 'Table of pending timeouts for suggested and proposed enzymes. If suggestions or proposals are not approved or rejected by IUBMB with two month, they automatically are accepted. This table holds the dates.';
--------------------------------------------------------
--  DDL for Table TIMEOUTS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."TIMEOUTS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"START_DATE" DATE, 
	"DUE_DATE" DATE, 
	"TIMEOUT_ID" NUMBER(7,0), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE)
   ) ;
 

   COMMENT ON TABLE "ENZYME"."TIMEOUTS_AUDIT"  IS 'Audit table for TIMEOUTS.';
--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."USERS" 
   (	"USERNAME" VARCHAR2(20 BYTE), 
	"PASSWORD" VARCHAR2(32 BYTE), 
	"ORGANISATION" VARCHAR2(20 BYTE), 
	"EMAIL" VARCHAR2(40 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."USERS"."USERNAME" IS 'Uppercase username';
 
   COMMENT ON COLUMN "ENZYME"."USERS"."PASSWORD" IS 'MD5 digest of username+password as hexadecimal string';
--------------------------------------------------------
--  DDL for Table XREFS
--------------------------------------------------------

  CREATE TABLE "ENZYME"."XREFS" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"DATABASE_CODE" VARCHAR2(9 BYTE), 
	"DATABASE_AC" VARCHAR2(500 BYTE), 
	"NAME" VARCHAR2(200 BYTE), 
	"STATUS" VARCHAR2(2 BYTE) DEFAULT 'NO', 
	"SOURCE" VARCHAR2(6 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"DATA_COMMENT" VARCHAR2(1000 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."XREFS"."ENZYME_ID" IS 'Enzyme_id is used as an internal unique identifier throughout the database as some entries don''t have an EC number, e.g. suggested entries. Example is ''70001'', ''9945'', etc';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."DATABASE_CODE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''S'', etc. In the beginning there are only Xrefs to the databases SWISS-PROT and TrEMBL.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."DATABASE_AC" IS 'Database accession.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."NAME" IS 'Name of the cross-reference, if any.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."STATUS" IS 'Internal code of status, for example ''NO'', ''UN'', ''SN'', ''OK'', etc.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."SOURCE" IS 'Database internal code, for example ''INTENZ'', ''IUBMB'', ''BRENDA'', ''SIB'' etc. Each enzyme belongs to one of them. In the beginning there are only Xrefs to the databases SWISS-PROT and TrEMBL.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."WEB_VIEW" IS 'View to display the cross-reference in.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS"."DATA_COMMENT" IS 'Comment on the cross-reference.';
 
   COMMENT ON TABLE "ENZYME"."XREFS"  IS 'Xrefs, table storing links to other databases like SWISS-PROT (''S''), TrEMBL (''T''), and later also PROSITE, MIM database, and so on.';
--------------------------------------------------------
--  DDL for Table XREFS_AUDIT
--------------------------------------------------------

  CREATE TABLE "ENZYME"."XREFS_AUDIT" 
   (	"ENZYME_ID" NUMBER(7,0), 
	"DATABASE_CODE" VARCHAR2(9 BYTE), 
	"DATABASE_AC" VARCHAR2(500 BYTE), 
	"NAME" VARCHAR2(200 BYTE), 
	"STATUS" VARCHAR2(2 BYTE), 
	"SOURCE" VARCHAR2(6 BYTE), 
	"TIMESTAMP" DATE, 
	"AUDIT_ID" NUMBER(15,0), 
	"DBUSER" VARCHAR2(30 BYTE), 
	"OSUSER" VARCHAR2(30 BYTE), 
	"REMARK" VARCHAR2(50 BYTE), 
	"ACTION" VARCHAR2(3 BYTE), 
	"WEB_VIEW" VARCHAR2(50 BYTE), 
	"DATA_COMMENT" VARCHAR2(1000 BYTE)
   ) ;
 

   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."ENZYME_ID" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."DATABASE_CODE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."DATABASE_AC" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."NAME" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."STATUS" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."SOURCE" IS 'Audit column';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."TIMESTAMP" IS 'Date and time of the action.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."AUDIT_ID" IS 'Serial number of audit action.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."DBUSER" IS 'Oracle account name of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."OSUSER" IS 'Unix account of the user doing the action.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."REMARK" IS 'Optional remark, set by calling enzyme.auditpackage.setremark.';
 
   COMMENT ON COLUMN "ENZYME"."XREFS_AUDIT"."ACTION" IS 'A short code for the action, ''I'' for insert, ''U'' for update, ''D'' for delete.';
 
   COMMENT ON TABLE "ENZYME"."XREFS_AUDIT"  IS 'Audit table storing the history of table xrefs.';
--------------------------------------------------------
--  DDL for View CHEBI_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "ENZYME"."CHEBI_REACTIONS" ("CHEBI_ID", "REACTION_ID", "EQUATION") AS 
  SELECT UNIQUE cd.accession chebi_id, ir.reaction_id, ir.equation
FROM compound_data cd, reaction_participants rp, intenz_reactions ir
WHERE cd.accession IS NOT NULL
AND cd.compound_id = rp.compound_id
AND (rp.reaction_id = ir.reaction_id OR rp.reaction_id = ir.un_reaction)
AND ir.status IN ('OK','PM','OB');
--------------------------------------------------------
--  DDL for View CHEBI_XREFS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "ENZYME"."CHEBI_XREFS" ("CHEBI_ID", "ENZYME_ID", "EC", "ENZYME_NAME") AS 
  select unique cd.accession chebi_id, ie.enzyme_id, ie.ec, n.name enzyme_name
from compound_data cd, id2ec ie, cofactors cf, names n
where
  cd.accession is not null
  and cd.compound_id = cf.compound_id
  and cf.enzyme_id = ie.enzyme_id
  and ie.enzyme_id = n.enzyme_id
  and n.name_class = 'COM'
  and n.web_view like '%INTENZ'
UNION
-- reaction participants
select unique cd.accession chebi_id, ie.enzyme_id, ie.ec, n.name enzyme_name
from compound_data cd, id2ec ie, reaction_participants rp, intenz_reactions ir, reactions_map rm, names n
where
  cd.accession is not null
  and cd.compound_id = rp.compound_id
  and (rp.reaction_id = ir.reaction_id or rp.reaction_id = ir.un_reaction)
  and ir.status in ('OK','PM')
  and ir.reaction_id = rm.reaction_id
  and rm.enzyme_id = ie.enzyme_id
  and ie.enzyme_id = n.enzyme_id
  and n.name_class = 'COM'
  and n.web_view like '%INTENZ';
--------------------------------------------------------
--  DDL for View VW_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "ENZYME"."VW_CLASSES" ("EC1", "EC2", "EC3", "DESCRIPTION") AS 
  select decode(ec1,0,null,ec1) ec1
       ,decode(ec2,0,null,ec2) ec2
       ,decode(ec3,0,null,ec3) ec3
       ,description
 from
 (
  select ec1  ec1
       , 0    ec2
       , 0    ec3
       , f_classes(ec1,null,null) description
  from  classes
  union
  select ec1
       , ec2
       , 0
       , f_classes(ec1,ec2,null)
  from  subclasses
  union
  select ec1
       , ec2
       , ec3
       , f_classes(ec1,ec2,ec3)  descr1
  from  subsubclasses
  order by 1,2,3
 );
--------------------------------------------------------
--  DDL for View V_EC_FOR_DATALIB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "ENZYME"."V_EC_FOR_DATALIB" ("EC_NUMBER") AS 
  SELECT distinct f_quad2string(ec1,ec2,ec3,ec4)
  FROM enzymes E1
 WHERE E1.active = 'Y'
   AND E1.status = 'OK'
   AND E1.ec1 IS NOT NULL
  WITH READ ONLY
;
--------------------------------------------------------
--  DDL for View V_INTERPRO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "ENZYME"."V_INTERPRO" ("EC", "NAME", "UNIPROT_ACCESSION", "UNIPROT_NAME") AS 
  SELECT
        ie.ec,
        f_xml_display_sp_code(n.name) name,
        x.database_ac UniProt_accession,
        x.name UniProt_name
FROM
        id2ec ie, names n, xrefs x
WHERE
        ie.enzyme_id = n.enzyme_id
        AND n.enzyme_id = x.enzyme_id
        AND x.database_code = 'S'
        AND n.name_class = 'COM'
ORDER BY ec ASC;
--------------------------------------------------------
--  DDL for Index PK_LINKS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_LINKS" ON "ENZYME"."LINKS" ("ENZYME_ID", "URL") 
  ;
--------------------------------------------------------
--  DDL for Index PK_REACTION_CITATIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_REACTION_CITATIONS" ON "ENZYME"."REACTION_CITATIONS" ("REACTION_ID", "PUB_ID", "SOURCE") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_STATUS$SORT_ORDER
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_STATUS$SORT_ORDER" ON "ENZYME"."CV_STATUS" ("SORT_ORDER") 
  ;
--------------------------------------------------------
--  DDL for Index PK_CV_VIEW
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CV_VIEW" ON "ENZYME"."CV_VIEW" ("CODE") 
  ;
--------------------------------------------------------
--  DDL for Index PK_FUTURE_EVENTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_FUTURE_EVENTS" ON "ENZYME"."FUTURE_EVENTS" ("GROUP_ID", "EVENT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_COMMENTS$ID$ORDER_IN
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_COMMENTS$ID$ORDER_IN" ON "ENZYME"."COMMENTS" ("ENZYME_ID", "ORDER_IN") 
  ;
--------------------------------------------------------
--  DDL for Index PK_CV_NAME_CLASSES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CV_NAME_CLASSES" ON "ENZYME"."CV_NAME_CLASSES" ("CODE") 
  ;
--------------------------------------------------------
--  DDL for Index PK_CV_STATUS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CV_STATUS" ON "ENZYME"."CV_STATUS" ("CODE") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_DATABASES$SORT_ORDER
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_DATABASES$SORT_ORDER" ON "ENZYME"."CV_DATABASES" ("SORT_ORDER") 
  ;
--------------------------------------------------------
--  DDL for Index PK_ENZYMES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_ENZYMES" ON "ENZYME"."ENZYMES" ("ENZYME_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_STATUS$NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_STATUS$NAME" ON "ENZYME"."CV_STATUS" ("NAME") 
  ;
--------------------------------------------------------
--  DDL for Index REACTION_MERGINGS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."REACTION_MERGINGS_PK" ON "ENZYME"."REACTION_MERGINGS" ("FROM_ID", "TO_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TIMEOUTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_TIMEOUTS" ON "ENZYME"."TIMEOUTS" ("TIMEOUT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_REACTIONS_MAP$ID$VIEW$ORDER
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_REACTIONS_MAP$ID$VIEW$ORDER" ON "ENZYME"."REACTIONS_MAP" ("ENZYME_ID", "WEB_VIEW", "ORDER_IN") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_WARNINGS$NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_WARNINGS$NAME" ON "ENZYME"."CV_WARNINGS" ("NAME") 
  ;
--------------------------------------------------------
--  DDL for Index PK_ID2EC
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_ID2EC" ON "ENZYME"."ID2EC" ("ENZYME_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_HISTORY_EVENTS$BEF$AFT
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_HISTORY_EVENTS$BEF$AFT" ON "ENZYME"."HISTORY_EVENTS" ("BEFORE_ID", "AFTER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_CV_WARNINGS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CV_WARNINGS" ON "ENZYME"."CV_WARNINGS" ("CODE") 
  ;
--------------------------------------------------------
--  DDL for Index PK_PUBLICATIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_PUBLICATIONS" ON "ENZYME"."PUBLICATIONS" ("PUB_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_STATUS$DISPLAY_NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_STATUS$DISPLAY_NAME" ON "ENZYME"."CV_STATUS" ("DISPLAY_NAME") 
  ;
--------------------------------------------------------
--  DDL for Index PK_NAMES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_NAMES" ON "ENZYME"."NAMES" ("ENZYME_ID", "NAME_CLASS", "NAME") 
  ;
--------------------------------------------------------
--  DDL for Index PK_REACTIONS_MAP
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_REACTIONS_MAP" ON "ENZYME"."REACTIONS_MAP" ("REACTION_ID", "ENZYME_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_COFACTORS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_COFACTORS" ON "ENZYME"."COFACTORS" ("ENZYME_ID", "COFACTOR_TEXT") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CITATIONS$ENZID$ORDER_IN
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CITATIONS$ENZID$ORDER_IN" ON "ENZYME"."CITATIONS" ("ENZYME_ID", "ORDER_IN") 
  ;
--------------------------------------------------------
--  DDL for Index PK_HISTORY_EVENTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_HISTORY_EVENTS" ON "ENZYME"."HISTORY_EVENTS" ("GROUP_ID", "EVENT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_ENZYMES$SOURCE$ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_ENZYMES$SOURCE$ID" ON "ENZYME"."ENZYMES" ("SOURCE", "ENZYME_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_REACTIONS$ID$ORDER_IN
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_REACTIONS$ID$ORDER_IN" ON "ENZYME"."REACTIONS" ("ENZYME_ID", "ORDER_IN") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_ROLES$UR
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_ROLES$UR" ON "ENZYME"."ROLES" ("USERNAME", "ROLE") 
  ;
--------------------------------------------------------
--  DDL for Index PK_COMPLEX_REACTIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_COMPLEX_REACTIONS" ON "ENZYME"."COMPLEX_REACTIONS" ("PARENT_ID", "CHILD_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_REACTION_DIRECTIONS$D
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_REACTION_DIRECTIONS$D" ON "ENZYME"."CV_REACTION_DIRECTIONS" ("DIRECTION") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_DATABASES$DISPLAY_NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_DATABASES$DISPLAY_NAME" ON "ENZYME"."CV_DATABASES" ("DISPLAY_NAME") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_NAME_CLASSES$SORT_ORDER
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_NAME_CLASSES$SORT_ORDER" ON "ENZYME"."CV_NAME_CLASSES" ("SORT_ORDER") 
  ;
--------------------------------------------------------
--  DDL for Index PK_SUBCLASSES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_SUBCLASSES" ON "ENZYME"."SUBCLASSES" ("EC1", "EC2") 
  ;
--------------------------------------------------------
--  DDL for Index PK_SUBSUBCLASSES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_SUBSUBCLASSES" ON "ENZYME"."SUBSUBCLASSES" ("EC1", "EC2", "EC3") 
  ;
--------------------------------------------------------
--  DDL for Index intenz_text_idx$ec
--------------------------------------------------------

  CREATE INDEX "ENZYME"."intenz_text_idx$ec" ON "ENZYME"."INTENZ_TEXT" ("EC") 
  ;
--------------------------------------------------------
--  DDL for Index PK_CITATIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CITATIONS" ON "ENZYME"."CITATIONS" ("ENZYME_ID", "PUB_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_NAME_CLASSES$NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_NAME_CLASSES$NAME" ON "ENZYME"."CV_NAME_CLASSES" ("NAME") 
  ;
--------------------------------------------------------
--  DDL for Index CV_REACTION_SIDES$S
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."CV_REACTION_SIDES$S" ON "ENZYME"."CV_REACTION_SIDES" ("SIDE") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_FUTURE_EVENTS$BEF$AFT
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_FUTURE_EVENTS$BEF$AFT" ON "ENZYME"."FUTURE_EVENTS" ("BEFORE_ID", "AFTER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_COMPOUND_DATA$SA
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_COMPOUND_DATA$SA" ON "ENZYME"."COMPOUND_DATA" ("SOURCE", "ACCESSION") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_WARNINGS$DISPLAY_NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_WARNINGS$DISPLAY_NAME" ON "ENZYME"."CV_WARNINGS" ("DISPLAY_NAME") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_INTENZ_REACTIONS$FP
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_INTENZ_REACTIONS$FP" ON "ENZYME"."INTENZ_REACTIONS" ("FINGERPRINT") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_WARNINGS$SORT_ORDER
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_WARNINGS$SORT_ORDER" ON "ENZYME"."CV_WARNINGS" ("SORT_ORDER") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_NAMES$ENZID$CLASS$ORDER_IN
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_NAMES$ENZID$CLASS$ORDER_IN" ON "ENZYME"."NAMES" ("ENZYME_ID", "NAME_CLASS", "ORDER_IN") 
  ;
--------------------------------------------------------
--  DDL for Index PK_INTENZ_TEXT
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_INTENZ_TEXT" ON "ENZYME"."INTENZ_TEXT" ("ENZYME_ID", "TEXT_ORDER") 
  ;
--------------------------------------------------------
--  DDL for Index PK_REACTION_XREFS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_REACTION_XREFS" ON "ENZYME"."REACTION_XREFS" ("DB_CODE", "DB_ACCESSION", "REACTION_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_REACTIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_REACTIONS" ON "ENZYME"."REACTIONS" ("ENZYME_ID", "EQUATION") 
  ;
--------------------------------------------------------
--  DDL for Index intenz_text_idx$text
--------------------------------------------------------

  CREATE INDEX "ENZYME"."intenz_text_idx$text" ON "ENZYME"."INTENZ_TEXT" ("TEXT") 
   INDEXTYPE IS "CTXSYS"."CONTEXT"  PARAMETERS ('section group intenz_text_sg storage enzyme_storage lexer enzyme_lexer stoplist enzyme_stoplist memory 20M');
--------------------------------------------------------
--  DDL for Index PK_CLASSES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CLASSES" ON "ENZYME"."CLASSES" ("EC1") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_DATABASES$NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_DATABASES$NAME" ON "ENZYME"."CV_DATABASES" ("NAME") 
  ;
--------------------------------------------------------
--  DDL for Index COMPOUND_NAME_IDX
--------------------------------------------------------

  CREATE INDEX "ENZYME"."COMPOUND_NAME_IDX" ON "ENZYME"."COMPOUND_DATA" (LOWER("ENZYME"."F_XML_DISPLAY_SP_CODE"("NAME"))) 
  ;
--------------------------------------------------------
--  DDL for Index PK_CV_DATABASES
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_CV_DATABASES" ON "ENZYME"."CV_DATABASES" ("CODE") 
  ;
--------------------------------------------------------
--  DDL for Index PK_COMMENTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_COMMENTS" ON "ENZYME"."COMMENTS" ("ENZYME_ID", "COMMENT_TEXT") 
  ;
--------------------------------------------------------
--  DDL for Index STATS
--------------------------------------------------------

  CREATE INDEX "ENZYME"."STATS" ON "ENZYME"."STATS" ("STATID", "TYPE", "C5", "C1", "C2", "C3", "C4", "VERSION") 
  ;
--------------------------------------------------------
--  DDL for Index PK_XREFS
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."PK_XREFS" ON "ENZYME"."XREFS" ("ENZYME_ID", "DATABASE_AC", "DATABASE_CODE") 
  ;
--------------------------------------------------------
--  DDL for Index UQ_CV_REACTION_QUALIFIERS$Q
--------------------------------------------------------

  CREATE UNIQUE INDEX "ENZYME"."UQ_CV_REACTION_QUALIFIERS$Q" ON "ENZYME"."CV_REACTION_QUALIFIERS" ("QUALIFIER") 
  ;
--------------------------------------------------------
--  DDL for Index DR$intenz_text_idx$text$X
--------------------------------------------------------

  CREATE INDEX "ENZYME"."DR$intenz_text_idx$text$X" ON "ENZYME"."DR$intenz_text_idx$text$I" ("TOKEN_TEXT", "TOKEN_TYPE", "TOKEN_FIRST", "TOKEN_LAST", "TOKEN_COUNT") 
  ;
--------------------------------------------------------
--  Constraints for Table REACTION_MERGINGS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_MERGINGS" ADD CONSTRAINT "REACTINO_MERGINGS_CHKIDS" CHECK (from_id != to_id) ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_MERGINGS" ADD CONSTRAINT "REACTION_MERGINGS_PK" PRIMARY KEY ("FROM_ID", "TO_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_MERGINGS" MODIFY ("MERGING_COMMENT" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_MERGINGS" MODIFY ("MERGING_WHO" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_MERGINGS" MODIFY ("MERGING_WHEN" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TIMEOUTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."TIMEOUTS" ADD CONSTRAINT "PK_TIMEOUTS" PRIMARY KEY ("TIMEOUT_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."TIMEOUTS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."TIMEOUTS" MODIFY ("START_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."TIMEOUTS" MODIFY ("DUE_DATE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."TIMEOUTS" MODIFY ("TIMEOUT_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table CV_WARNINGS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_WARNINGS" ADD CONSTRAINT "CK_CV_WARNINGS$CODE" CHECK (code = UPPER(code)) ENABLE;
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" ADD CONSTRAINT "PK_CV_WARNINGS" PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" MODIFY ("CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" MODIFY ("SORT_ORDER" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" ADD CONSTRAINT "UQ_CV_WARNINGS$DISPLAY_NAME" UNIQUE ("DISPLAY_NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" ADD CONSTRAINT "UQ_CV_WARNINGS$NAME" UNIQUE ("NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_WARNINGS" ADD CONSTRAINT "UQ_CV_WARNINGS$SORT_ORDER" UNIQUE ("SORT_ORDER") ENABLE;
--------------------------------------------------------
--  Constraints for Table USERS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."USERS" MODIFY ("PASSWORD" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."USERS" ADD PRIMARY KEY ("USERNAME") ENABLE;

--------------------------------------------------------
--  Constraints for Table CV_REACTION_DIRECTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_REACTION_DIRECTIONS" MODIFY ("DIRECTION" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_REACTION_DIRECTIONS" ADD PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_REACTION_DIRECTIONS" ADD CONSTRAINT "UQ_CV_REACTION_DIRECTIONS$D" UNIQUE ("DIRECTION") ENABLE;
--------------------------------------------------------
--  Constraints for Table COMMENTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMMENTS" ADD CONSTRAINT "CK_COMMENTS$TEXT_DOTS" CHECK (comment_text LIKE '%.') ENABLE;
 
  ALTER TABLE "ENZYME"."COMMENTS" ADD CONSTRAINT "PK_COMMENTS" PRIMARY KEY ("ENZYME_ID", "COMMENT_TEXT") ENABLE;
 
  ALTER TABLE "ENZYME"."COMMENTS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMMENTS" MODIFY ("COMMENT_TEXT" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMMENTS" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMMENTS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMMENTS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMMENTS" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMMENTS" ADD CONSTRAINT "UQ_COMMENTS$ID$ORDER_IN" UNIQUE ("ENZYME_ID", "ORDER_IN") ENABLE;


--------------------------------------------------------
--  Constraints for Table NAMES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "CK_NAMES$NAME_CRLF" CHECK (name = TRANSLATE(name, 'X'||chr(9)||chr(10)||chr(13), 'X')) ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "CK_NAMES$NAME_TRIM" CHECK (TRIM(BOTH FROM name) = name) ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "CK_NAMES$NAME_WS" CHECK (name NOT LIKE '%.'AND name NOT LIKE '%?'AND name NOT LIKE '%!') ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "CK_NAMES$ORDER_IN$ZERO" CHECK (order_in > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "PK_NAMES" PRIMARY KEY ("ENZYME_ID", "NAME_CLASS", "NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("NAME_CLASS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "UQ_NAMES$ENZID$CLASS$ORDER_IN" UNIQUE ("ENZYME_ID", "NAME_CLASS", "ORDER_IN") ENABLE;

--------------------------------------------------------
--  Constraints for Table CV_REACTION_SIDES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_REACTION_SIDES" ADD CONSTRAINT "CV_REACTION_SIDES$S" UNIQUE ("SIDE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_REACTION_SIDES" MODIFY ("SIDE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_REACTION_SIDES" ADD PRIMARY KEY ("CODE") ENABLE;



--------------------------------------------------------
--  Constraints for Table REACTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTIONS" ADD CONSTRAINT "PK_REACTIONS" PRIMARY KEY ("ENZYME_ID", "EQUATION") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS" MODIFY ("EQUATION" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS" ADD CONSTRAINT "UQ_REACTIONS$ID$ORDER_IN" UNIQUE ("ENZYME_ID", "ORDER_IN") ENABLE;
--------------------------------------------------------
--  Constraints for Table INTENZ_REACTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD CONSTRAINT "CK_INTENZ_REACTIONS$UN" CHECK ((un_reaction is null AND direction = 'UN')
		OR (un_reaction is not null AND direction != 'UN')) DISABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" MODIFY ("EQUATION" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" MODIFY ("DIRECTION" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" MODIFY ("IUBMB" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD PRIMARY KEY ("REACTION_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD UNIQUE ("INTENZ_ACCESSION") ENABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD CONSTRAINT "UQ_INTENZ_REACTIONS$FP" UNIQUE ("FINGERPRINT") ENABLE;



--------------------------------------------------------
--  Constraints for Table LINKS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."LINKS" ADD CONSTRAINT "PK_LINKS" PRIMARY KEY ("ENZYME_ID", "URL") ENABLE;
 
  ALTER TABLE "ENZYME"."LINKS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."LINKS" MODIFY ("URL" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."LINKS" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."LINKS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."LINKS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."LINKS" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CV_LOCATION
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_LOCATION" ADD PRIMARY KEY ("CODE") ENABLE;

--------------------------------------------------------
--  Constraints for Table CV_VIEW
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_VIEW" ADD CONSTRAINT "CK_CV_VIEW$CODE" CHECK (code = UPPER(code)) ENABLE;
 
  ALTER TABLE "ENZYME"."CV_VIEW" ADD CONSTRAINT "PK_CV_VIEW" PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_VIEW" MODIFY ("CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_VIEW" MODIFY ("DESCRIPTION" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table CV_REACTION_QUALIFIERS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_REACTION_QUALIFIERS" MODIFY ("QUALIFIER" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_REACTION_QUALIFIERS" ADD PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_REACTION_QUALIFIERS" ADD CONSTRAINT "UQ_CV_REACTION_QUALIFIERS$Q" UNIQUE ("QUALIFIER") ENABLE;
--------------------------------------------------------
--  Constraints for Table CITATIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CITATIONS" ADD CONSTRAINT "CK_CITATIONS$ORDER_IN$ZERO" CHECK (order_in > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."CITATIONS" ADD CONSTRAINT "PK_CITATIONS" PRIMARY KEY ("ENZYME_ID", "PUB_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."CITATIONS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CITATIONS" MODIFY ("PUB_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CITATIONS" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CITATIONS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CITATIONS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CITATIONS" ADD CONSTRAINT "UQ_CITATIONS$ENZID$ORDER_IN" UNIQUE ("ENZYME_ID", "ORDER_IN") ENABLE;

--------------------------------------------------------
--  Constraints for Table DR$intenz_text_idx$text$I
--------------------------------------------------------

  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$I" MODIFY ("TOKEN_TEXT" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$I" MODIFY ("TOKEN_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$I" MODIFY ("TOKEN_FIRST" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$I" MODIFY ("TOKEN_LAST" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$I" MODIFY ("TOKEN_COUNT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ROLES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."ROLES" MODIFY ("ROLE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ROLES" ADD CONSTRAINT "UQ_ROLES$UR" UNIQUE ("USERNAME", "ROLE") ENABLE;
--------------------------------------------------------
--  Constraints for Table DR$intenz_text_idx$text$K
--------------------------------------------------------

  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$K" ADD PRIMARY KEY ("TEXTKEY") ENABLE;
--------------------------------------------------------
--  Constraints for Table REACTION_PARTICIPANTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD CONSTRAINT "CK_REACTION_PARTICIPANTS$COEF" CHECK (coefficient > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD CONSTRAINT "PK_REACTION_PARTICIPANTS" PRIMARY KEY ("REACTION_ID", "COMPOUND_ID", "SIDE") DISABLE;
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" MODIFY ("REACTION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" MODIFY ("COMPOUND_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" MODIFY ("SIDE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" MODIFY ("COEFFICIENT" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" MODIFY ("COEFF_TYPE" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table REACTIONS_MAP
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTIONS_MAP" ADD CONSTRAINT "CK_REACTIONS_MAP$ORDER" CHECK (order_in > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" ADD CONSTRAINT "PK_REACTIONS_MAP" PRIMARY KEY ("REACTION_ID", "ENZYME_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" MODIFY ("REACTION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" ADD CONSTRAINT "UQ_REACTIONS_MAP$ID$VIEW$ORDER" UNIQUE ("ENZYME_ID", "WEB_VIEW", "ORDER_IN") ENABLE;

--------------------------------------------------------
--  Constraints for Table PUBLICATIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."PUBLICATIONS" ADD CONSTRAINT "CK_PUBLICATIONS$PUB_TYPE" CHECK (pub_type IN ('J', 'B', 'P')) ENABLE;
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" ADD CONSTRAINT "PK_PUBLICATIONS" PRIMARY KEY ("PUB_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" MODIFY ("PUB_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" MODIFY ("PUB_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" MODIFY ("PUB_YEAR" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" MODIFY ("JOURNAL_BOOK" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" ADD CONSTRAINT "UQ_PUBLICATIONS$MEDLINE_ID" UNIQUE ("MEDLINE_ID") DISABLE;
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" ADD CONSTRAINT "UQ_PUBLICATIONS$PUBMED_ID" UNIQUE ("PUBMED_ID") DISABLE;
--------------------------------------------------------
--  Constraints for Table CV_DATABASES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_DATABASES" ADD CONSTRAINT "CK_CV_DATABASES$CODE" CHECK (code = UPPER(code)) ENABLE;
 
  ALTER TABLE "ENZYME"."CV_DATABASES" ADD CONSTRAINT "PK_CV_DATABASES" PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_DATABASES" MODIFY ("CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_DATABASES" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_DATABASES" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_DATABASES" MODIFY ("SORT_ORDER" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_DATABASES" ADD CONSTRAINT "UQ_CV_DATABASES$DISPLAY_NAME" UNIQUE ("DISPLAY_NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_DATABASES" ADD CONSTRAINT "UQ_CV_DATABASES$NAME" UNIQUE ("NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_DATABASES" ADD CONSTRAINT "UQ_CV_DATABASES$SORT_ORDER" UNIQUE ("SORT_ORDER") ENABLE;
--------------------------------------------------------
--  Constraints for Table SUBCLASSES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."SUBCLASSES" ADD CONSTRAINT "CK_SUBCLASSES$ACTIVE" CHECK (active IN ('Y','N')) ENABLE;
 
  ALTER TABLE "ENZYME"."SUBCLASSES" ADD CONSTRAINT "CK_SUBCLASSES$EC2_ZERO" CHECK (ec2 > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."SUBCLASSES" ADD CONSTRAINT "CK_SUBCLASSES$NAME_WS" CHECK (name = TRIM(TRIM(chr(9)  FROM TRIM(chr(10) FROM TRIM(chr(13) FROM name))))) ENABLE;
 
  ALTER TABLE "ENZYME"."SUBCLASSES" ADD CONSTRAINT "PK_SUBCLASSES" PRIMARY KEY ("EC1", "EC2") ENABLE;
 
  ALTER TABLE "ENZYME"."SUBCLASSES" MODIFY ("EC1" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."SUBCLASSES" MODIFY ("EC2" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."SUBCLASSES" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."SUBCLASSES" MODIFY ("ACTIVE" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table COFACTORS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COFACTORS" ADD CONSTRAINT "PK_COFACTORS" PRIMARY KEY ("ENZYME_ID", "COFACTOR_TEXT") ENABLE;
 
  ALTER TABLE "ENZYME"."COFACTORS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COFACTORS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COFACTORS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COFACTORS" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COFACTORS" MODIFY ("COFACTOR_TEXT" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COFACTORS" MODIFY ("WEB_VIEW" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CV_COEFF_TYPES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_COEFF_TYPES" ADD PRIMARY KEY ("CODE") ENABLE;
--------------------------------------------------------
--  Constraints for Table FUTURE_EVENTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."FUTURE_EVENTS" ADD CONSTRAINT "FUTURE_EVENTS$CYCLE0" CHECK (before_id != after_id) ENABLE;
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" ADD CONSTRAINT "PK_FUTURE_EVENTS" PRIMARY KEY ("GROUP_ID", "EVENT_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" MODIFY ("GROUP_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" MODIFY ("EVENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" MODIFY ("EVENT_CLASS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" MODIFY ("TIMEOUT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" ADD CONSTRAINT "UQ_FUTURE_EVENTS$BEF$AFT" UNIQUE ("BEFORE_ID", "AFTER_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table REACTION_CITATIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_CITATIONS" ADD CONSTRAINT "CK_REACTION_CITATIONS$ORD" CHECK (order_in > 0) DISABLE;
 
  ALTER TABLE "ENZYME"."REACTION_CITATIONS" ADD CONSTRAINT "PK_REACTION_CITATIONS" PRIMARY KEY ("REACTION_ID", "PUB_ID", "SOURCE") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_CITATIONS" MODIFY ("REACTION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_CITATIONS" MODIFY ("PUB_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_CITATIONS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_CITATIONS" ADD CONSTRAINT "UQ_REACTION_CITATIONS$RO" UNIQUE ("REACTION_ID", "ORDER_IN") DISABLE;
--------------------------------------------------------
--  Constraints for Table CV_NAME_CLASSES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" ADD CONSTRAINT "CK_CV_NAME_CLASSES$CODE" CHECK (code = UPPER(code)) ENABLE;
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" ADD CONSTRAINT "PK_CV_NAME_CLASSES" PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" MODIFY ("CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" MODIFY ("SORT_ORDER" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" ADD CONSTRAINT "UQ_CV_NAME_CLASSES$NAME" UNIQUE ("NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_NAME_CLASSES" ADD CONSTRAINT "UQ_CV_NAME_CLASSES$SORT_ORDER" UNIQUE ("SORT_ORDER") ENABLE;
--------------------------------------------------------
--  Constraints for Table ENZYMES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "CK_ENZYMES$ACTIVE" CHECK (active IN ('Y','N')) ENABLE;
 
  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "CK_ENZYMES$EC4_ZERO" CHECK (ec4 > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "CK_ENZYMES$EC_QUAD_NULL" CHECK ((ec1 IS NOT NULL AND ec2 IS NOT NULL and ec3 IS NOT NULL and ec4 IS NOT NULL)
OR (ec1 IS NOT NULL AND ec2 IS NOT NULL and ec3 IS NOT NULL and ec4 IS NULL) OR
(ec1 IS NOT NULL AND ec2 IS NOT NULL and ec3 IS NULL and ec4 IS NULL) OR
(ec1 IS NOT NULL AND ec2 IS NULL and ec3 IS NULL and ec4 IS NULL) OR
(ec1 IS NULL AND ec2 IS NULL and ec3 IS NULL and ec4 IS NULL)) ENABLE;
 
  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "PK_ENZYMES" PRIMARY KEY ("ENZYME_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."ENZYMES" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ENZYMES" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ENZYMES" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ENZYMES" MODIFY ("ACTIVE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "UQ_ENZYMES$SOURCE$ID" UNIQUE ("SOURCE", "ENZYME_ID") ENABLE;

--------------------------------------------------------
--  Constraints for Table CLASSES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CLASSES" ADD CONSTRAINT "CK_CLASSES$ACTIVE" CHECK (active IN ('Y','N')) ENABLE;
 
  ALTER TABLE "ENZYME"."CLASSES" ADD CONSTRAINT "CK_CLASSES$EC1_ZERO" CHECK (ec1 > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."CLASSES" ADD CONSTRAINT "CK_CLASSES$NAME_WS" CHECK (name = TRIM(TRIM(chr(9)  FROM TRIM(chr(10) FROM TRIM(chr(13) FROM name))))) ENABLE;
 
  ALTER TABLE "ENZYME"."CLASSES" ADD CONSTRAINT "PK_CLASSES" PRIMARY KEY ("EC1") ENABLE;
 
  ALTER TABLE "ENZYME"."CLASSES" MODIFY ("EC1" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CLASSES" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CLASSES" MODIFY ("ACTIVE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CV_COMP_PUB_AVAIL
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_COMP_PUB_AVAIL" ADD PRIMARY KEY ("CODE") ENABLE;

--------------------------------------------------------
--  Constraints for Table REACTION_XREFS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_XREFS" ADD CONSTRAINT "PK_REACTION_XREFS" PRIMARY KEY ("DB_CODE", "DB_ACCESSION", "REACTION_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_XREFS" MODIFY ("REACTION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_XREFS" MODIFY ("DB_CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."REACTION_XREFS" MODIFY ("DB_ACCESSION" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table COMPOUND_DATA
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD CONSTRAINT "CK_COMPOUND_DATA$PUB" CHECK (
			(accession IS NULL AND child_accession IS NULL AND published IS NULL)
			OR
			(accession IS NOT NULL AND child_accession IS NULL AND published IN ('N','P'))
			OR
			(accession IS NOT NULL AND child_accession IS NOT NULL AND published IN ('N','P','C'))
		) ENABLE;
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD CONSTRAINT "CK_COMPOUND_DATA$SRC" CHECK (
			(source IS NULL AND accession IS NULL)
			OR
			(source IS NOT NULL AND accession IS NOT NULL)
		) ENABLE;
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD PRIMARY KEY ("COMPOUND_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD UNIQUE ("NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD CONSTRAINT "UQ_COMPOUND_DATA$SA" UNIQUE ("SOURCE", "ACCESSION") ENABLE;



--------------------------------------------------------
--  Constraints for Table CV_OPERATORS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_OPERATORS" ADD PRIMARY KEY ("CODE") ENABLE;
--------------------------------------------------------
--  Constraints for Table XREFS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."XREFS" ADD CONSTRAINT "PK_XREFS" PRIMARY KEY ("ENZYME_ID", "DATABASE_AC", "DATABASE_CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("DATABASE_CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("DATABASE_AC" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."XREFS" MODIFY ("WEB_VIEW" NOT NULL ENABLE);


--------------------------------------------------------
--  Constraints for Table CV_STATUS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CV_STATUS" ADD CONSTRAINT "CK_CV_STATUS$CODE" CHECK (code = UPPER(code)) ENABLE;
 
  ALTER TABLE "ENZYME"."CV_STATUS" ADD CONSTRAINT "PK_CV_STATUS" PRIMARY KEY ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_STATUS" MODIFY ("CODE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_STATUS" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_STATUS" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_STATUS" MODIFY ("SORT_ORDER" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."CV_STATUS" ADD CONSTRAINT "UQ_CV_STATUS$DISPLAY_NAME" UNIQUE ("DISPLAY_NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_STATUS" ADD CONSTRAINT "UQ_CV_STATUS$NAME" UNIQUE ("NAME") ENABLE;
 
  ALTER TABLE "ENZYME"."CV_STATUS" ADD CONSTRAINT "UQ_CV_STATUS$SORT_ORDER" UNIQUE ("SORT_ORDER") ENABLE;

--------------------------------------------------------
--  Constraints for Table HISTORY_EVENTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."HISTORY_EVENTS" ADD CONSTRAINT "CK_HISTORY_EVENTS$CYCLE0" CHECK (before_id != after_id) ENABLE;
 
  ALTER TABLE "ENZYME"."HISTORY_EVENTS" ADD CONSTRAINT "PK_HISTORY_EVENTS" PRIMARY KEY ("GROUP_ID", "EVENT_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."HISTORY_EVENTS" MODIFY ("GROUP_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."HISTORY_EVENTS" MODIFY ("EVENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."HISTORY_EVENTS" MODIFY ("EVENT_CLASS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."HISTORY_EVENTS" ADD CONSTRAINT "UQ_HISTORY_EVENTS$BEF$AFT" UNIQUE ("BEFORE_ID", "AFTER_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table COMPLEX_REACTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "CK_COMPLEX_REACTIONS$COEF" CHECK (coefficient > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "CK_COMPLEX_REACTIONS$ORD" CHECK (order_in >= 0) ENABLE;
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "CK_COMPLEX_REACTIONS$SAMEID" CHECK (parent_id != child_id) ENABLE;
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "PK_COMPLEX_REACTIONS" PRIMARY KEY ("PARENT_ID", "CHILD_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" MODIFY ("PARENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" MODIFY ("CHILD_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" MODIFY ("ORDER_IN" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" MODIFY ("COEFFICIENT" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "UQ_COMPLEX_REACTIONS$PO" UNIQUE ("PARENT_ID", "ORDER_IN") DISABLE;

--------------------------------------------------------
--  Constraints for Table DR$intenz_text_idx$text$N
--------------------------------------------------------

  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$N" MODIFY ("NLT_MARK" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."DR$intenz_text_idx$text$N" ADD PRIMARY KEY ("NLT_DOCID") ENABLE;
--------------------------------------------------------
--  Constraints for Table ID2EC
--------------------------------------------------------

  ALTER TABLE "ENZYME"."ID2EC" ADD CONSTRAINT "PK_ID2EC" PRIMARY KEY ("ENZYME_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."ID2EC" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ID2EC" MODIFY ("EC" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ID2EC" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."ID2EC" MODIFY ("SOURCE" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table INTENZ_TEXT
--------------------------------------------------------

  ALTER TABLE "ENZYME"."INTENZ_TEXT" ADD CONSTRAINT "PK_INTENZ_TEXT" PRIMARY KEY ("ENZYME_ID", "TEXT_ORDER") ENABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_TEXT" MODIFY ("ENZYME_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_TEXT" MODIFY ("EC" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_TEXT" MODIFY ("STATUS" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_TEXT" MODIFY ("SOURCE" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_TEXT" MODIFY ("TEXT_ORDER" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."INTENZ_TEXT" MODIFY ("TEXT" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table COMPOUND_DATA_UPDATES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMPOUND_DATA_UPDATES" MODIFY ("NAME" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA_UPDATES" ADD PRIMARY KEY ("COMPOUND_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table SUBSUBCLASSES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."SUBSUBCLASSES" ADD CONSTRAINT "CK_SUBSUBCLASSES$ACTIVE" CHECK (active IN ('Y','N')) ENABLE;
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" ADD CONSTRAINT "CK_SUBSUBCLASSES$EC3_ZERO" CHECK (ec3 > 0) ENABLE;
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" ADD CONSTRAINT "CK_SUBSUBCLASSES$NAME_WS" CHECK (name = TRIM(TRIM(chr(9)  FROM TRIM(chr(10) FROM TRIM(chr(13) FROM name))))) ENABLE;
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" ADD CONSTRAINT "PK_SUBSUBCLASSES" PRIMARY KEY ("EC1", "EC2", "EC3") ENABLE;
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" MODIFY ("EC1" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" MODIFY ("EC2" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" MODIFY ("EC3" NOT NULL ENABLE);
 
  ALTER TABLE "ENZYME"."SUBSUBCLASSES" MODIFY ("ACTIVE" NOT NULL ENABLE);

--------------------------------------------------------
--  Ref Constraints for Table CITATIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."CITATIONS" ADD CONSTRAINT "FK_CITATIONS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;



--------------------------------------------------------
--  Ref Constraints for Table COFACTORS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COFACTORS" ADD CONSTRAINT "FK_COFACTORS$COMPOUND_ID" FOREIGN KEY ("COMPOUND_ID")
	  REFERENCES "ENZYME"."COMPOUND_DATA" ("COMPOUND_ID") ON DELETE SET NULL ENABLE;
 
  ALTER TABLE "ENZYME"."COFACTORS" ADD CONSTRAINT "FK_COFACTORS$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."COFACTORS" ADD CONSTRAINT "FK_COFACTORS$OPERATOR" FOREIGN KEY ("OPERATOR")
	  REFERENCES "ENZYME"."CV_OPERATORS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."COFACTORS" ADD CONSTRAINT "FK_COFACTORS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."COFACTORS" ADD CONSTRAINT "FK_COFACTORS$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table COMMENTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMMENTS" ADD CONSTRAINT "FK_COMMENTS$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."COMMENTS" ADD CONSTRAINT "FK_COMMENTS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."COMMENTS" ADD CONSTRAINT "FK_COMMENTS$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table COMPLEX_REACTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "FK_COMPLEX_REACTIONS$CHILD" FOREIGN KEY ("CHILD_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."COMPLEX_REACTIONS" ADD CONSTRAINT "FK_COMPLEX_REACTIONS$PARENT" FOREIGN KEY ("PARENT_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table COMPOUND_DATA
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD CONSTRAINT "FK_COMPOUND_DATA$PUB" FOREIGN KEY ("PUBLISHED")
	  REFERENCES "ENZYME"."CV_COMP_PUB_AVAIL" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA" ADD CONSTRAINT "FK_COMPOUND_DATA$SOURCE" FOREIGN KEY ("SOURCE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table COMPOUND_DATA_UPDATES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."COMPOUND_DATA_UPDATES" ADD CONSTRAINT "FK_COMPOUND_DATA_UPDATES$CID" FOREIGN KEY ("COMPOUND_ID")
	  REFERENCES "ENZYME"."COMPOUND_DATA" ("COMPOUND_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."COMPOUND_DATA_UPDATES" ADD CONSTRAINT "FK_COMPOUND_DATA_UPDATES$PUB" FOREIGN KEY ("PUBLISHED")
	  REFERENCES "ENZYME"."CV_COMP_PUB_AVAIL" ("CODE") ENABLE;





















--------------------------------------------------------
--  Ref Constraints for Table ENZYMES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "FK_ENZYMES$EC1$EC2$EC3" FOREIGN KEY ("EC1", "EC2", "EC3")
	  REFERENCES "ENZYME"."SUBSUBCLASSES" ("EC1", "EC2", "EC3") ENABLE;
 
  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "FK_ENZYMES$SOURCE" FOREIGN KEY ("SOURCE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."ENZYMES" ADD CONSTRAINT "FK_ENZYMES$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table FUTURE_EVENTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."FUTURE_EVENTS" ADD CONSTRAINT "FK_FUTURE_EVENTS$AFTER_ID" FOREIGN KEY ("AFTER_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" ADD CONSTRAINT "FK_FUTURE_EVENTS$BEFORE_ID" FOREIGN KEY ("BEFORE_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."FUTURE_EVENTS" ADD CONSTRAINT "FK_FUTURE_EVENTS$TIMEOUT_ID" FOREIGN KEY ("TIMEOUT_ID")
	  REFERENCES "ENZYME"."TIMEOUTS" ("TIMEOUT_ID") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table HISTORY_EVENTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."HISTORY_EVENTS" ADD CONSTRAINT "FK_HISTORY_EVENTS$AFTER_ID" FOREIGN KEY ("AFTER_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."HISTORY_EVENTS" ADD CONSTRAINT "FK_HISTORY_EVENTS$BEFORE_ID" FOREIGN KEY ("BEFORE_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ENABLE;


--------------------------------------------------------
--  Ref Constraints for Table INTENZ_REACTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD CONSTRAINT "FK_INTENZ_REACTIONS$DIRECTION" FOREIGN KEY ("DIRECTION")
	  REFERENCES "ENZYME"."CV_REACTION_DIRECTIONS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD CONSTRAINT "FK_INTENZ_REACTIONS$SOURCE" FOREIGN KEY ("SOURCE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD CONSTRAINT "FK_INTENZ_REACTIONS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."INTENZ_REACTIONS" ADD CONSTRAINT "FK_INTENZ_REACTIONS$UN" FOREIGN KEY ("UN_REACTION")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ENABLE;


--------------------------------------------------------
--  Ref Constraints for Table LINKS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."LINKS" ADD CONSTRAINT "FK_LINKS$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."LINKS" ADD CONSTRAINT "FK_LINKS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."LINKS" ADD CONSTRAINT "FK_LINKS$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table NAMES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "FK_NAMES$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "FK_NAMES$NAME_CLASS" FOREIGN KEY ("NAME_CLASS")
	  REFERENCES "ENZYME"."CV_NAME_CLASSES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "FK_NAMES$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "FK_NAMES$WARNING" FOREIGN KEY ("WARNING")
	  REFERENCES "ENZYME"."CV_WARNINGS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."NAMES" ADD CONSTRAINT "FK_NAMES$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PUBLICATIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."PUBLICATIONS" ADD CONSTRAINT "FK_PUBLICATIONS$SOURCE" FOREIGN KEY ("SOURCE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."PUBLICATIONS" ADD CONSTRAINT "FK_PUBLICATIONS$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table REACTIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTIONS" ADD CONSTRAINT "FK_REACTIONS$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS" ADD CONSTRAINT "FK_REACTIONS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS" ADD CONSTRAINT "FK_REACTIONS$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table REACTIONS_MAP
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTIONS_MAP" ADD CONSTRAINT "FK_REACTIONS_MAP$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" ADD CONSTRAINT "FK_REACTIONS_MAP$REACTION_ID" FOREIGN KEY ("REACTION_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."REACTIONS_MAP" ADD CONSTRAINT "FK_REACTIONS_MAP$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table REACTION_CITATIONS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_CITATIONS" ADD CONSTRAINT "FK_REACTION_CITATIONS$REAC" FOREIGN KEY ("REACTION_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_CITATIONS" ADD CONSTRAINT "FK_REACTION_CITATIONS$SO" FOREIGN KEY ("SOURCE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table REACTION_MERGINGS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_MERGINGS" ADD CONSTRAINT "REACTION_MERGINGS_FK_FROM_ID" FOREIGN KEY ("FROM_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE DISABLE;
 
  ALTER TABLE "ENZYME"."REACTION_MERGINGS" ADD CONSTRAINT "REACTION_MERGINGS_FK_TO_ID" FOREIGN KEY ("TO_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REACTION_PARTICIPANTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD CONSTRAINT "FK_REACTION_PARTICIPANTS$COMP" FOREIGN KEY ("COMPOUND_ID")
	  REFERENCES "ENZYME"."COMPOUND_DATA" ("COMPOUND_ID") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD CONSTRAINT "FK_REACTION_PARTICIPANTS$CT" FOREIGN KEY ("COEFF_TYPE")
	  REFERENCES "ENZYME"."CV_COEFF_TYPES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD CONSTRAINT "FK_REACTION_PARTICIPANTS$REAC" FOREIGN KEY ("REACTION_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD CONSTRAINT "FK_REACTION_PARTICIPANTS$SIDE" FOREIGN KEY ("SIDE")
	  REFERENCES "ENZYME"."CV_REACTION_SIDES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS" ADD FOREIGN KEY ("LOCATION")
	  REFERENCES "ENZYME"."CV_LOCATION" ("CODE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REACTION_PARTICIPANTS_AUDIT
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_PARTICIPANTS_AUDIT" ADD FOREIGN KEY ("LOCATION")
	  REFERENCES "ENZYME"."CV_LOCATION" ("CODE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REACTION_XREFS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."REACTION_XREFS" ADD CONSTRAINT "FK_REACTION_XREFS$DBC" FOREIGN KEY ("DB_CODE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."REACTION_XREFS" ADD CONSTRAINT "FK_REACTION_XREFS$RID" FOREIGN KEY ("REACTION_ID")
	  REFERENCES "ENZYME"."INTENZ_REACTIONS" ("REACTION_ID") ON DELETE CASCADE ENABLE;


--------------------------------------------------------
--  Ref Constraints for Table ROLES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."ROLES" ADD CONSTRAINT "FK_ROLES$UN" FOREIGN KEY ("USERNAME")
	  REFERENCES "ENZYME"."USERS" ("USERNAME") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table SUBCLASSES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."SUBCLASSES" ADD CONSTRAINT "FK_SUBCLASSES$EC1" FOREIGN KEY ("EC1")
	  REFERENCES "ENZYME"."CLASSES" ("EC1") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table SUBSUBCLASSES
--------------------------------------------------------

  ALTER TABLE "ENZYME"."SUBSUBCLASSES" ADD CONSTRAINT "FK_SUBSUBCLASSES$EC1$EC2" FOREIGN KEY ("EC1", "EC2")
	  REFERENCES "ENZYME"."SUBCLASSES" ("EC1", "EC2") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table TIMEOUTS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."TIMEOUTS" ADD CONSTRAINT "FK_TIMEOUTS$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;


--------------------------------------------------------
--  Ref Constraints for Table XREFS
--------------------------------------------------------

  ALTER TABLE "ENZYME"."XREFS" ADD CONSTRAINT "FK_XREFS$DATABASE_CODE" FOREIGN KEY ("DATABASE_CODE")
	  REFERENCES "ENZYME"."CV_DATABASES" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."XREFS" ADD CONSTRAINT "FK_XREFS$ENZYME_ID" FOREIGN KEY ("ENZYME_ID")
	  REFERENCES "ENZYME"."ENZYMES" ("ENZYME_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "ENZYME"."XREFS" ADD CONSTRAINT "FK_XREFS$STATUS" FOREIGN KEY ("STATUS")
	  REFERENCES "ENZYME"."CV_STATUS" ("CODE") ENABLE;
 
  ALTER TABLE "ENZYME"."XREFS" ADD CONSTRAINT "FK_XREFS$WEB_VIEW" FOREIGN KEY ("WEB_VIEW")
	  REFERENCES "ENZYME"."CV_VIEW" ("CODE") ENABLE;

--------------------------------------------------------
--  DDL for Trigger TD_CITATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CITATIONS" 
  AFTER DELETE ON citations FOR EACH ROW



BEGIN
  INSERT INTO citations_audit (
    enzyme_id,pub_id,order_in,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.enzyme_id,:old.pub_id,:old.order_in,:old.status,:old.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_CITATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CLASSES" 
  AFTER DELETE ON classes FOR EACH ROW



BEGIN
  INSERT INTO classes_audit (
    ec1,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.ec1,:old.name,:old.description,:old.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_CLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_COFACTORS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_COFACTORS" 
  AFTER DELETE ON cofactors FOR EACH ROW
BEGIN
  INSERT INTO cofactors_audit (
    enzyme_id,source,status,order_in,cofactor_text,
    timestamp,audit_id,dbuser,osuser,remark,action,
    compound_id,operator,op_grp)
  VALUES (
    :old.enzyme_id,:old.source,:old.status,:new.order_in,:old.cofactor_text,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D',
    :new.compound_id,:new.operator,:new.op_grp);
END;
/
ALTER TRIGGER "ENZYME"."TD_COFACTORS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_COMMENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_COMMENTS" 
  AFTER DELETE ON comments FOR EACH ROW
BEGIN
  INSERT INTO comments_audit (
    enzyme_id,comment_text,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.enzyme_id,:old.comment_text,:old.order_in,:old.status,:old.source,:old.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_COMMENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_COMPLEX_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_COMPLEX_REACTIONS" 
  AFTER DELETE ON complex_reactions FOR EACH ROW
BEGIN
  INSERT INTO complex_reactions_audit (
    parent_id,child_id,order_in,coefficient,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.parent_id,:old.child_id,:old.order_in,:old.coefficient,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_COMPLEX_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_COMPOUND_DATA
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_COMPOUND_DATA" 
  AFTER DELETE ON compound_data FOR EACH ROW
BEGIN
  INSERT INTO compound_data_audit (
    compound_id,name,formula,charge,source,accession,child_accession,published,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.compound_id,:old.name,:old.formula,:old.charge,:old.source,:old.accession,:old.child_accession,:old.published,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_COMPOUND_DATA" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_CV_DATABASES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CV_DATABASES" 
  AFTER DELETE ON cv_databases FOR EACH ROW



BEGIN
  INSERT INTO cv_databases_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.code,:old.name,:old.display_name,:old.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_CV_DATABASES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_CV_NAME_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CV_NAME_CLASSES" 
  AFTER DELETE ON cv_name_classes FOR EACH ROW



BEGIN
  INSERT INTO cv_name_classes_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.code,:old.name,:old.display_name,:old.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_CV_NAME_CLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_CV_STATUS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CV_STATUS" 
  AFTER DELETE ON cv_status FOR EACH ROW



BEGIN
  INSERT INTO cv_status_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.code,:old.name,:old.display_name,:old.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_CV_STATUS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_CV_VIEW
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CV_VIEW" 
  AFTER DELETE ON cv_view FOR EACH ROW
BEGIN
  INSERT INTO cv_view_audit (
    code,description,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.code,:old.description,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_CV_VIEW" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_CV_WARNINGS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_CV_WARNINGS" 
  AFTER DELETE ON cv_warnings FOR EACH ROW



BEGIN
  INSERT INTO cv_warnings_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.code,:old.name,:old.display_name,:old.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_CV_WARNINGS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_ENZYMES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_ENZYMES" 
  AFTER DELETE ON enzymes FOR EACH ROW



BEGIN
  INSERT INTO enzymes_audit (
    enzyme_id,ec1,ec2,ec3,ec4,history,status,note,source,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.enzyme_id,:old.ec1,:old.ec2,:old.ec3,:old.ec4,:old.history,:old.status,:old.note,:old.source,:old.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_ENZYMES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_FUTURE_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_FUTURE_EVENTS" 
  AFTER DELETE ON future_events FOR EACH ROW


BEGIN
  INSERT INTO future_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    :new.group_id,:new.event_id,:new.before_id,:new.after_id,:new.event_year,:new.event_note,:new.event_class,:new.status,:new.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_FUTURE_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_HISTORY_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_HISTORY_EVENTS" 
  AFTER DELETE ON history_events FOR EACH ROW


BEGIN
  INSERT INTO history_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    :new.group_id,:new.event_id,:new.before_id,:new.after_id,:new.event_year,:new.event_note,:new.event_class,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_HISTORY_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_INTENZ_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_INTENZ_REACTIONS" 
  AFTER DELETE ON intenz_reactions FOR EACH ROW
BEGIN
  INSERT INTO intenz_reactions_audit (
    reaction_id,intenz_accession,equation,fingerprint
    ,status,source,iubmb,public_in_chebi,direction,
    un_reaction,qualifiers,data_comment,reaction_comment,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.reaction_id,:old.intenz_accession,:old.equation,:old.fingerprint,
    :old.status,:old.source,:old.iubmb,:old.public_in_chebi,:old.direction,
    :old.un_reaction,:old.qualifiers,:old.data_comment,:old.reaction_comment,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,
    'D');
end;
/
ALTER TRIGGER "ENZYME"."TD_INTENZ_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_LINKS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_LINKS" 
  AFTER DELETE ON links FOR EACH ROW
BEGIN
  INSERT INTO links_audit (
    enzyme_id,url,display_name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    :old.enzyme_id,:old.url,:old.display_name,:old.status,:old.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D',
    :new.data_comment);
END;
/
ALTER TRIGGER "ENZYME"."TD_LINKS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_NAMES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_NAMES" 
  AFTER DELETE ON names FOR EACH ROW
BEGIN
  INSERT INTO names_audit (
    enzyme_id,name,name_class,warning,status,source,note,order_in,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.enzyme_id,:old.name,:old.name_class,:old.warning,:old.status,:old.source,:old.note,:old.order_in,:old.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_NAMES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_PUBLICATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_PUBLICATIONS" 
  AFTER DELETE ON publications FOR EACH ROW
BEGIN
  INSERT INTO publications_audit (
    pub_id,medline_id,pubmed_id,pub_type,author,pub_year,title,journal_book,volume,first_page,last_page,edition,editor,pub_company,pub_place,url,web_view,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.pub_id,:old.medline_id,:old.pubmed_id,:old.pub_type,:old.author,:old.pub_year,:old.title,:old.journal_book,:old.volume,:old.first_page,:old.last_page,:old.edition,:old.editor,:old.pub_company,:old.pub_place,:old.url,:old.web_view,:old.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_PUBLICATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_REACTIONS" 
  AFTER DELETE ON reactions FOR EACH ROW
BEGIN
  INSERT INTO reactions_audit (
    enzyme_id,equation,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.enzyme_id,:old.equation,:old.order_in,:old.status,:old.source,:old.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_REACTIONS_MAP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_REACTIONS_MAP" 
  AFTER DELETE ON reactions_map FOR EACH ROW
BEGIN
  INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.reaction_id,:old.enzyme_id,:old.web_view,:old.order_in,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_REACTIONS_MAP" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_REACTION_CITATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_REACTION_CITATIONS" 
  AFTER DELETE ON reaction_citations FOR EACH ROW
BEGIN
  INSERT INTO reaction_citations_audit (
    reaction_id,pub_id,order_in,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.reaction_id,:old.pub_id,:old.order_in,:old.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_REACTION_CITATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_REACTION_PARTICIPANTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_REACTION_PARTICIPANTS" 
  AFTER DELETE ON reaction_participants FOR EACH ROW
BEGIN
  insert into reaction_participants_audit (
    reaction_id,compound_id,side,coefficient,coeff_type,location,
    timestamp,audit_id,dbuser,osuser,remark,action)
  values (
    :old.reaction_id,:old.compound_id,:old.side,:old.coefficient,:old.coeff_type,:old.location,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
end;
/
ALTER TRIGGER "ENZYME"."TD_REACTION_PARTICIPANTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_REACTION_XREFS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_REACTION_XREFS" 
  AFTER DELETE ON reaction_xrefs FOR EACH ROW
BEGIN
  INSERT INTO reaction_xrefs_audit (
    reaction_id,db_code,db_accession,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.reaction_id,:old.db_code,:old.db_accession,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "ENZYME"."TD_REACTION_XREFS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_SUBCLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_SUBCLASSES" 
  AFTER DELETE ON subclasses FOR EACH ROW



BEGIN
  INSERT INTO subclasses_audit (
    ec1,ec2,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.ec1,:old.ec2,:old.name,:old.description,:old.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_SUBCLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_SUBSUBCLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_SUBSUBCLASSES" 
  AFTER DELETE ON subsubclasses FOR EACH ROW



BEGIN
  INSERT INTO subsubclasses_audit (
    ec1,ec2,ec3,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.ec1,:old.ec2,:old.ec3,:old.name,:old.description,:old.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_SUBSUBCLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_TIMEOUTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_TIMEOUTS" 
  AFTER DELETE ON timeouts FOR EACH ROW


BEGIN
  INSERT INTO timeouts_audit (
    enzyme_id,start_date,due_date,timeout_id,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :old.enzyme_id,:old.start_date,:old.due_date,:new.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;

/
ALTER TRIGGER "ENZYME"."TD_TIMEOUTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TD_XREFS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TD_XREFS" 
  AFTER DELETE ON xrefs FOR EACH ROW
   WHEN (old.database_code != 'S' and old.database_code != 'GO') BEGIN
  INSERT INTO xrefs_audit (
    enzyme_id,database_code,database_ac,name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    :old.enzyme_id,:old.database_code,:old.database_ac,:old.name,:old.status,:old.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D',
    :new.data_comment);
END;
/
ALTER TRIGGER "ENZYME"."TD_XREFS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CITATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CITATIONS" 
  AFTER INSERT ON citations FOR EACH ROW



BEGIN
  INSERT INTO citations_audit (
    enzyme_id,pub_id,order_in,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.pub_id,:new.order_in,:new.status,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_CITATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CLASSES" 
  AFTER INSERT ON classes FOR EACH ROW



BEGIN
  INSERT INTO classes_audit (
    ec1,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.ec1,:new.name,:new.description,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_CLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_COFACTORS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_COFACTORS" 
  AFTER INSERT ON cofactors FOR EACH ROW
BEGIN
  INSERT INTO cofactors_audit (
    enzyme_id,source,status,order_in,cofactor_text,
    timestamp,audit_id,dbuser,osuser,remark,action,
    compound_id,operator,op_grp)
  VALUES (
    :new.enzyme_id,:new.source,:new.status,:new.order_in,:new.cofactor_text,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I',
    :new.compound_id,:new.operator,:new.op_grp);
END;
/
ALTER TRIGGER "ENZYME"."TI_COFACTORS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_COMMENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_COMMENTS" 
  AFTER INSERT ON comments FOR EACH ROW
BEGIN
  INSERT INTO comments_audit (
    enzyme_id,comment_text,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.comment_text,:new.order_in,:new.status,:new.source,:new.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_COMMENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_COMPLEX_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_COMPLEX_REACTIONS" 
  AFTER INSERT ON complex_reactions FOR EACH ROW
BEGIN
  INSERT INTO complex_reactions_audit (
    parent_id,child_id,order_in,coefficient,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.parent_id,:new.child_id,:new.order_in,:new.coefficient,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_COMPLEX_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_COMPOUND_DATA
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_COMPOUND_DATA" 
  AFTER INSERT ON compound_data FOR EACH ROW
BEGIN
  INSERT INTO compound_data_audit (
    compound_id,name,formula,charge,source,accession,child_accession,published,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.compound_id,:new.name,:new.formula,:new.charge,:new.source,:new.accession,:new.child_accession,:new.published,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_COMPOUND_DATA" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CV_DATABASES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CV_DATABASES" 
  AFTER INSERT ON cv_databases FOR EACH ROW



BEGIN
  INSERT INTO cv_databases_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_CV_DATABASES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CV_NAME_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CV_NAME_CLASSES" 
  AFTER INSERT ON cv_name_classes FOR EACH ROW



BEGIN
  INSERT INTO cv_name_classes_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_CV_NAME_CLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CV_STATUS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CV_STATUS" 
  AFTER INSERT ON cv_status FOR EACH ROW



BEGIN
  INSERT INTO cv_status_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_CV_STATUS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CV_VIEW
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CV_VIEW" 
  AFTER INSERT ON cv_view FOR EACH ROW
BEGIN
  INSERT INTO cv_view_audit (
    code,description,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.description,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_CV_VIEW" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_CV_WARNINGS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_CV_WARNINGS" 
  AFTER INSERT ON cv_warnings FOR EACH ROW



BEGIN
  INSERT INTO cv_warnings_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_CV_WARNINGS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_ENZYMES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_ENZYMES" 
  AFTER INSERT ON enzymes FOR EACH ROW



BEGIN
  INSERT INTO enzymes_audit (
    enzyme_id,ec1,ec2,ec3,ec4,history,status,note,source,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.ec1,:new.ec2,:new.ec3,:new.ec4,:new.history,:new.status,:new.note,:new.source,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_ENZYMES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_FUTURE_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_FUTURE_EVENTS" 
  AFTER INSERT ON future_events FOR EACH ROW


BEGIN
  INSERT INTO future_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    :new.group_id,:new.event_id,:new.before_id,:new.after_id,:new.event_year,:new.event_note,:new.event_class,:new.status,:new.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_FUTURE_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_HISTORY_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_HISTORY_EVENTS" 
  AFTER INSERT ON history_events FOR EACH ROW


BEGIN
  INSERT INTO history_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    :new.group_id,:new.event_id,:new.before_id,:new.after_id,:new.event_year,:new.event_note,:new.event_class,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_HISTORY_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_INTENZ_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_INTENZ_REACTIONS" 
  AFTER INSERT ON intenz_reactions FOR EACH ROW
BEGIN
  insert into intenz_reactions_audit (
    reaction_id,intenz_accession,equation,fingerprint,
    status,source,iubmb,public_in_chebi,direction,un_reaction,
    qualifiers,data_comment,reaction_comment,
    timestamp,audit_id,dbuser,osuser,remark,action)
  values (
    :new.reaction_id,:new.intenz_accession,:new.equation,:new.fingerprint,
    :new.status,:new.source,:new.iubmb,:new.public_in_chebi,:new.direction,
    :new.un_reaction,:new.qualifiers,:new.data_comment,:new.reaction_comment,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,
    'I');
end;
/
ALTER TRIGGER "ENZYME"."TI_INTENZ_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_LINKS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_LINKS" 
  AFTER INSERT ON links FOR EACH ROW
BEGIN
  INSERT INTO links_audit (
    enzyme_id,url,display_name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    :new.enzyme_id,:new.url,:new.display_name,:new.status,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I',
    :new.data_comment);
END;
/
ALTER TRIGGER "ENZYME"."TI_LINKS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_NAMES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_NAMES" 
  AFTER INSERT ON names FOR EACH ROW
BEGIN
  INSERT INTO names_audit (
    enzyme_id,name,name_class,warning,status,source,note,order_in,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.name,:new.name_class,:new.warning,:new.status,:new.source,:new.note,:new.order_in,:new.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_NAMES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_PUBLICATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_PUBLICATIONS" 
  AFTER INSERT ON publications FOR EACH ROW
BEGIN
  INSERT INTO publications_audit (
    pub_id,medline_id,pubmed_id,pub_type,author,pub_year,title,journal_book,volume,first_page,last_page,edition,editor,pub_company,pub_place,url,web_view,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.pub_id,:new.medline_id,:new.pubmed_id,:new.pub_type,:new.author,:new.pub_year,:new.title,:new.journal_book,:new.volume,:new.first_page,:new.last_page,:new.edition,:new.editor,:new.pub_company,:new.pub_place,:new.url,:new.web_view,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_PUBLICATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_REACTIONS" 
  AFTER INSERT ON reactions FOR EACH ROW
BEGIN
  INSERT INTO reactions_audit (
    enzyme_id,equation,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.equation,:new.order_in,:new.status,:new.source,:new.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTIONS_MAP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_REACTIONS_MAP" 
  AFTER INSERT ON reactions_map FOR EACH ROW
BEGIN
  INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.enzyme_id,:new.web_view,:new.order_in,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_REACTIONS_MAP" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTION_CITATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_REACTION_CITATIONS" 
  AFTER INSERT ON reaction_citations FOR EACH ROW
BEGIN
  INSERT INTO reaction_citations_audit (
    reaction_id,pub_id,order_in,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.pub_id,:new.order_in,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_REACTION_CITATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTION_PARTICIPANTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_REACTION_PARTICIPANTS" 
  AFTER INSERT ON reaction_participants FOR EACH ROW
BEGIN
  insert into reaction_participants_audit (
    reaction_id,compound_id,side,coefficient,coeff_type,location,
    timestamp,audit_id,dbuser,osuser,remark,action)
  values (
    :new.reaction_id,:new.compound_id,:new.side,:new.coefficient,:new.coeff_type,:new.location,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
end;
/
ALTER TRIGGER "ENZYME"."TI_REACTION_PARTICIPANTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTION_QUALIFIERS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_REACTION_QUALIFIERS" 
BEFORE INSERT ON intenz_reactions FOR EACH ROW
BEGIN
	IF check_reaction_qualifiers(:NEW.qualifiers) > 0
	THEN
		RAISE_APPLICATION_ERROR(-20000, 'Bad reaction qualifiers');
	END IF;
END;
/
ALTER TRIGGER "ENZYME"."TI_REACTION_QUALIFIERS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTION_XREFS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_REACTION_XREFS" 
  AFTER INSERT ON reaction_xrefs FOR EACH ROW
BEGIN
  INSERT INTO reaction_xrefs_audit (
    reaction_id,db_code,db_accession,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.db_code,:new.db_accession,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "ENZYME"."TI_REACTION_XREFS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_SUBCLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_SUBCLASSES" 
  AFTER INSERT ON subclasses FOR EACH ROW



BEGIN
  INSERT INTO subclasses_audit (
    ec1,ec2,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.ec1,:new.ec2,:new.name,:new.description,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_SUBCLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_SUBSUBCLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_SUBSUBCLASSES" 
  AFTER INSERT ON subsubclasses FOR EACH ROW



BEGIN
  INSERT INTO subsubclasses_audit (
    ec1,ec2,ec3,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.ec1,:new.ec2,:new.ec3,:new.name,:new.description,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_SUBSUBCLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_TIMEOUTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_TIMEOUTS" 
  AFTER INSERT ON timeouts FOR EACH ROW


BEGIN
  INSERT INTO timeouts_audit (
    enzyme_id,start_date,due_date,timeout_id,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.start_date,:new.due_date,:new.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;

/
ALTER TRIGGER "ENZYME"."TI_TIMEOUTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TI_XREFS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TI_XREFS" 
  AFTER INSERT ON xrefs FOR EACH ROW
   WHEN (new.database_code != 'S' and new.database_code != 'GO') BEGIN
  INSERT INTO xrefs_audit (
    enzyme_id,database_code,database_ac,name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    :new.enzyme_id,:new.database_code,:new.database_ac,:new.name,:new.status,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I',
    :new.data_comment);
END;
/
ALTER TRIGGER "ENZYME"."TI_XREFS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CITATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CITATIONS" 
  AFTER UPDATE ON citations FOR EACH ROW



BEGIN
  INSERT INTO citations_audit (
    enzyme_id,pub_id,order_in,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.pub_id,:new.order_in,:new.status,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_CITATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CLASSES" 
  AFTER UPDATE ON classes FOR EACH ROW



BEGIN
  INSERT INTO classes_audit (
    ec1,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.ec1,:new.name,:new.description,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_CLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_COFACTORS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_COFACTORS" 
  AFTER UPDATE ON cofactors FOR EACH ROW
BEGIN
  INSERT INTO cofactors_audit (
    enzyme_id,source,status,order_in,cofactor_text,
    timestamp,audit_id,dbuser,osuser,remark,action,
    compound_id,operator,op_grp)
  VALUES (
    :new.enzyme_id,:new.source,:new.status,:new.order_in,:new.cofactor_text,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U',
    :new.compound_id,:new.operator,:new.op_grp);
END;
/
ALTER TRIGGER "ENZYME"."TU_COFACTORS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_COMMENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_COMMENTS" 
  AFTER UPDATE ON comments FOR EACH ROW
BEGIN
  INSERT INTO comments_audit (
    enzyme_id,comment_text,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.comment_text,:new.order_in,:new.status,:new.source,:new.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_COMMENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_COMPLEX_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_COMPLEX_REACTIONS" 
  AFTER UPDATE ON complex_reactions FOR EACH ROW
BEGIN
  INSERT INTO complex_reactions_audit (
    parent_id,child_id,order_in,coefficient,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.parent_id,:new.child_id,:new.order_in,:new.coefficient,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_COMPLEX_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_COMPOUND_DATA
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_COMPOUND_DATA" 
  AFTER UPDATE ON compound_data FOR EACH ROW
BEGIN
  INSERT INTO compound_data_audit (
    compound_id,name,formula,charge,source,accession,child_accession,published,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.compound_id,:new.name,:new.formula,:new.charge,:new.source,:new.accession,:new.child_accession,:new.published,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_COMPOUND_DATA" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CV_DATABASES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CV_DATABASES" 
  AFTER UPDATE ON cv_databases FOR EACH ROW



BEGIN
  INSERT INTO cv_databases_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_CV_DATABASES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CV_NAME_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CV_NAME_CLASSES" 
  AFTER UPDATE ON cv_name_classes FOR EACH ROW



BEGIN
  INSERT INTO cv_name_classes_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_CV_NAME_CLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CV_STATUS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CV_STATUS" 
  AFTER UPDATE ON cv_status FOR EACH ROW



BEGIN
  INSERT INTO cv_status_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_CV_STATUS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CV_VIEW
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CV_VIEW" 
  AFTER UPDATE ON cv_view FOR EACH ROW
BEGIN
  INSERT INTO cv_view_audit (
    code,description,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.description,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_CV_VIEW" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_CV_WARNINGS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_CV_WARNINGS" 
  AFTER UPDATE ON cv_warnings FOR EACH ROW



BEGIN
  INSERT INTO cv_warnings_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.code,:new.name,:new.display_name,:new.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_CV_WARNINGS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_ENZYMES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_ENZYMES" 
  AFTER UPDATE ON enzymes FOR EACH ROW



BEGIN
  INSERT INTO enzymes_audit (
    enzyme_id,ec1,ec2,ec3,ec4,history,status,note,source,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.ec1,:new.ec2,:new.ec3,:new.ec4,:new.history,:new.status,:new.note,:new.source,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_ENZYMES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_FUTURE_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_FUTURE_EVENTS" 
  AFTER UPDATE ON future_events FOR EACH ROW


BEGIN
  INSERT INTO future_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    :new.group_id,:new.event_id,:new.before_id,:new.after_id,:new.event_year,:new.event_note,:new.event_class,:new.status,:new.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_FUTURE_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_HISTORY_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_HISTORY_EVENTS" 
  AFTER UPDATE ON history_events FOR EACH ROW


BEGIN
  INSERT INTO history_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    :new.group_id,:new.event_id,:new.before_id,:new.after_id,:new.event_year,:new.event_note,:new.event_class,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_HISTORY_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_INTENZ_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_INTENZ_REACTIONS" 
  AFTER UPDATE ON intenz_reactions FOR EACH ROW
BEGIN
  insert into intenz_reactions_audit (
    reaction_id,intenz_accession,equation,fingerprint,
    status,source,iubmb,public_in_chebi,direction,un_reaction,
    qualifiers,data_comment,reaction_comment,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.intenz_accession,:new.equation,:new.fingerprint,
    :new.status,:new.source,:new.iubmb,:new.public_in_chebi,:new.direction,
    :new.un_reaction,:new.qualifiers,:new.data_comment,:new.reaction_comment,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,
    'U');
end;
/
ALTER TRIGGER "ENZYME"."TU_INTENZ_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_LINKS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_LINKS" 
  AFTER UPDATE ON links FOR EACH ROW
BEGIN
  INSERT INTO links_audit (
    enzyme_id,url,display_name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    :new.enzyme_id,:new.url,:new.display_name,:new.status,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U',
    :new.data_comment);
END;
/
ALTER TRIGGER "ENZYME"."TU_LINKS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_NAMES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_NAMES" 
  AFTER UPDATE ON names FOR EACH ROW
BEGIN
  INSERT INTO names_audit (
    enzyme_id,name,name_class,warning,status,source,note,order_in,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.name,:new.name_class,:new.warning,:new.status,:new.source,:new.note,:new.order_in,:new.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_NAMES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_PUBLICATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_PUBLICATIONS" 
  AFTER UPDATE ON publications FOR EACH ROW
BEGIN
  INSERT INTO publications_audit (
    pub_id,medline_id,pubmed_id,pub_type,author,pub_year,title,journal_book,volume,first_page,last_page,edition,editor,pub_company,pub_place,url,web_view,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.pub_id,:new.medline_id,:new.pubmed_id,:new.pub_type,:new.author,:new.pub_year,:new.title,:new.journal_book,:new.volume,:new.first_page,:new.last_page,:new.edition,:new.editor,:new.pub_company,:new.pub_place,:new.url,:new.web_view,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_PUBLICATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_REACTIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_REACTIONS" 
  AFTER UPDATE ON reactions FOR EACH ROW
BEGIN
  INSERT INTO reactions_audit (
    enzyme_id,equation,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.equation,:new.order_in,:new.status,:new.source,:new.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_REACTIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_REACTIONS_MAP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_REACTIONS_MAP" 
  AFTER UPDATE ON reactions_map FOR EACH ROW
BEGIN
  INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.enzyme_id,:new.web_view,:new.order_in,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_REACTIONS_MAP" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_REACTION_CITATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_REACTION_CITATIONS" 
  AFTER UPDATE ON reaction_citations FOR EACH ROW
BEGIN
  INSERT INTO reaction_citations_audit (
    reaction_id,pub_id,order_in,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.pub_id,:new.order_in,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_REACTION_CITATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_REACTION_PARTICIPANTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_REACTION_PARTICIPANTS" 
  AFTER UPDATE ON reaction_participants FOR EACH ROW
BEGIN
  insert into reaction_participants_audit (
    reaction_id,compound_id,side,coefficient,coeff_type,location,
    timestamp,audit_id,dbuser,osuser,remark,action)
  values (
    :new.reaction_id,:new.compound_id,:new.side,:new.coefficient,:new.coeff_type,:new.location,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
end;
/
ALTER TRIGGER "ENZYME"."TU_REACTION_PARTICIPANTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_REACTION_QUALIFIERS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_REACTION_QUALIFIERS" 
BEFORE UPDATE ON intenz_reactions FOR EACH ROW
BEGIN
	IF check_reaction_qualifiers(:NEW.qualifiers) > 0
	THEN
		RAISE_APPLICATION_ERROR(-20000, 'Bad reaction qualifiers');
	END IF;
END;
/
ALTER TRIGGER "ENZYME"."TU_REACTION_QUALIFIERS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_REACTION_XREFS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_REACTION_XREFS" 
  AFTER UPDATE ON reaction_xrefs FOR EACH ROW
BEGIN
  INSERT INTO reaction_xrefs_audit (
    reaction_id,db_code,db_accession,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.reaction_id,:new.db_code,:new.db_accession,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/
ALTER TRIGGER "ENZYME"."TU_REACTION_XREFS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_SUBCLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_SUBCLASSES" 
  AFTER UPDATE ON subclasses FOR EACH ROW



BEGIN
  INSERT INTO subclasses_audit (
    ec1,ec2,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.ec1,:new.ec2,:new.name,:new.description,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_SUBCLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_SUBSUBCLASSES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_SUBSUBCLASSES" 
  AFTER UPDATE ON subsubclasses FOR EACH ROW



BEGIN
  INSERT INTO subsubclasses_audit (
    ec1,ec2,ec3,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.ec1,:new.ec2,:new.ec3,:new.name,:new.description,:new.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_SUBSUBCLASSES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_TIMEOUTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_TIMEOUTS" 
  AFTER UPDATE ON timeouts FOR EACH ROW


BEGIN
  INSERT INTO timeouts_audit (
    enzyme_id,start_date,due_date,timeout_id,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    :new.enzyme_id,:new.start_date,:new.due_date,:new.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;

/
ALTER TRIGGER "ENZYME"."TU_TIMEOUTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TU_XREFS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."TU_XREFS" 
  AFTER UPDATE ON xrefs FOR EACH ROW
BEGIN
  INSERT INTO xrefs_audit (
    enzyme_id,database_code,database_ac,name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    :new.enzyme_id,:new.database_code,:new.database_ac,:new.name,:new.status,:new.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U',
    :new.data_comment);
END;
/
ALTER TRIGGER "ENZYME"."TU_XREFS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger T_COMPOUND_DATA$AUTOINC
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."T_COMPOUND_DATA$AUTOINC" 
BEFORE INSERT ON compound_data
FOR EACH ROW
 WHEN (new.compound_id IS NULL) BEGIN
    SELECT s_compound_id.nextval INTO :new.compound_id FROM DUAL;
END;
/
ALTER TRIGGER "ENZYME"."T_COMPOUND_DATA$AUTOINC" ENABLE;
--------------------------------------------------------
--  DDL for Trigger T_ENZYMES$SOURCE_CHANGE
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."T_ENZYMES$SOURCE_CHANGE" 
  BEFORE UPDATE OF source ON enzymes
  FOR EACH ROW
   WHEN (new.source != old.source) BEGIN
  RAISE_APPLICATION_ERROR(-20000, 'enzyme.source must not be changed', TRUE);
END;
/
ALTER TRIGGER "ENZYME"."T_ENZYMES$SOURCE_CHANGE" ENABLE;
--------------------------------------------------------
--  DDL for Trigger T_INTENZ_REACTIONS$AUTOINC
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "ENZYME"."T_INTENZ_REACTIONS$AUTOINC" 
BEFORE INSERT ON intenz_reactions
FOR EACH ROW
 WHEN (new.reaction_id IS NULL) BEGIN
    SELECT s_reaction_id.nextval INTO :new.reaction_id FROM DUAL;
END;
/
ALTER TRIGGER "ENZYME"."T_INTENZ_REACTIONS$AUTOINC" ENABLE;
--------------------------------------------------------
--  DDL for Function CHECK_REACTION_QUALIFIERS
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."CHECK_REACTION_QUALIFIERS" (qq IN reaction_qualifiers)
RETURN INTEGER AS
  tmp CV_REACTION_QUALIFIERS.CODE%type;
BEGIN
	IF qq IS NOT NULL
	THEN
		FOR i IN qq.FIRST .. qq.LAST
		LOOP
			SELECT code INTO tmp FROM cv_reaction_qualifiers WHERE qq(i) = code;
	    END LOOP;
    END IF;
	RETURN 0;

	EXCEPTION
		WHEN NO_DATA_FOUND THEN RETURN 1;
END;

/
--------------------------------------------------------
--  DDL for Function F_CLASSES
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_CLASSES" (p_ec1 subsubclasses.ec1%type, p_ec2 subsubclasses.ec2%type, p_ec3 subsubclasses.ec3%type)
return varchar2
as
  separator char(1) := ',';
  output    varchar2(32000) :='';
begin

  if p_ec2 is null and p_ec3 is null then
    for r_classes in (select ec1, name, description from classes where ec1 = p_ec1) loop
       if r_classes.NAME        is not null then output := output || r_classes.NAME          ||separator ; end if;
       if r_classes.DESCRIPTION is not null then output := output || r_classes.DESCRIPTION   ||separator; end if;
    end loop;
    if length(output) > 4000 then
       return ('ERROR !' );
    else
       output := substr(output, 1, 4000);
       return output;
    end if;
  elsif p_ec3 is null then
    for r_subclasses in (select ec1, ec2, name, description from subclasses where ec1 = p_ec1 and ec2 = p_ec2) loop
       if r_subclasses.NAME        is not null then output := substr(output || r_subclasses.NAME          ||separator,1,4000); end if;
       if r_subclasses.DESCRIPTION is not null then output := substr(output || r_subclasses.DESCRIPTION   ||separator,1,4000); end if;
    end loop;
    if length(output) > 4000 then
       return ('ERROR !' );
    else
       output := substr(output, 1, 4000);
       return output;
    end if;
  else
    for r_subsubclasses in (select ec1, ec2, ec3, name, description from subsubclasses where ec1 = p_ec1 and ec2 = p_ec2 and ec3 = p_ec3) loop
       if r_subsubclasses.NAME        is not null then output := substr(output || r_subsubclasses.NAME          ||separator,1,4000); end if;
       if r_subsubclasses.DESCRIPTION is not null then output := substr(output || r_subsubclasses.DESCRIPTION   ||separator,1,4000); end if;
       if length(output) = 4000 then exit; end if;
    end loop;
    if length(output) > 4000 then
       return ('ERROR !' );
    else
       output := substr(output, 1, 4000);
       return output;
    end if;
  end if;
end;

/
--------------------------------------------------------
--  DDL for Function F_LINKS
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_LINKS" (p_enzyme_id enzymes.enzyme_id%type)
return varchar2
as
  separator char(1) := ',';
  output    varchar2(4000) :='';
begin
  for r_lx in (select url from links where enzyme_id = p_enzyme_id and display_name = 'CAS' ) loop
     output := output||r_lx.url||separator;
  end loop;
  output := substr (output,1,(length(output)-1)) ; -- get rid of last comma
  return output;
end;

/
--------------------------------------------------------
--  DDL for Function F_OTHER_NAMES
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_OTHER_NAMES" (p_enzyme_id enzymes.enzyme_id%type)
return varchar2
as
  separator char(1) := ',';
  output    varchar2(4000) :='';
begin
  for r_com_names in (select name from names where enzyme_id = p_enzyme_id and name_class='OTH') loop
     output := output||r_com_names.name||separator;
  end loop;

  output := substr (output,1,(length(output)-1)) ; -- get rid of last comma

  return output;
end;

/
--------------------------------------------------------
--  DDL for Function F_PUBDATA
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_PUBDATA" (p_enzyme_id enzymes.enzyme_id%type)
return varchar2
as
  separator char(1) := ';';
  output    varchar2(4000) :='';
begin
  for r_pub in (select pub.* from publications pub, citations cit where cit.enzyme_id = p_enzyme_id and cit.pub_id = pub.pub_id) loop
     if r_pub.PUB_ID       is not null then output := substr(output || ' PUB_ID= '      || r_pub.PUB_ID       ||separator,1,4000); end if;
     if r_pub.MEDLINE_ID   is not null then output := substr(output || ' MEDLINE_ID= '  || r_pub.MEDLINE_ID   ||separator,1,4000); end if;
     if r_pub.PUBMED_ID    is not null then output := substr(output || ' PUBMED_ID= '   || r_pub.PUBMED_ID    ||separator,1,4000); end if;
     if r_pub.PUB_TYPE     is not null then output := substr(output || ' PUB_TYPE= '    || r_pub.PUB_TYPE     ||separator,1,4000); end if;
     if r_pub.AUTHOR       is not null then output := substr(output || ' AUTHOR= '      || r_pub.AUTHOR       ||separator,1,4000); end if;
     if r_pub.PUB_YEAR     is not null then output := substr(output || ' PUB_YEAR= '    || r_pub.PUB_YEAR     ||separator,1,4000); end if;
     if r_pub.TITLE        is not null then output := substr(output || ' TITLE= '       || r_pub.TITLE        ||separator,1,4000); end if;
     if r_pub.JOURNAL_BOOK is not null then output := substr(output || ' JOURNAL_BOOK= '|| r_pub.JOURNAL_BOOK ||separator,1,4000); end if;
     if r_pub.VOLUME       is not null then output := substr(output || ' VOLUME= '      || r_pub.VOLUME       ||separator,1,4000); end if;
     if r_pub.FIRST_PAGE   is not null then output := substr(output || ' FIRST_PAGE= '  || r_pub.FIRST_PAGE   ||separator,1,4000); end if;
     if r_pub.LAST_PAGE    is not null then output := substr(output || ' LAST_PAGE= '   || r_pub.LAST_PAGE    ||separator,1,4000); end if;
     if r_pub.EDITION      is not null then output := substr(output || ' EDITION= '     || r_pub.EDITION      ||separator,1,4000); end if;
     if r_pub.EDITOR       is not null then output := substr(output || ' EDITOR= '      || r_pub.EDITOR       ||separator,1,4000); end if;
     if r_pub.PUB_COMPANY  is not null then output := substr(output || ' PUB_COMPANY= ' || r_pub.PUB_COMPANY  ||separator,1,4000); end if;
     if r_pub.PUB_PLACE    is not null then output := substr(output || ' PUB_PLACE= '   || r_pub.PUB_PLACE    ||separator,1,4000); end if;
     if r_pub.URL          is not null then output := substr(output || ' URL = '        || r_pub.URL                     ,1,4000);  end if;
     if length(output) = 4000 then exit; end if;
  end loop;
  return output;
end;

/
--------------------------------------------------------
--  DDL for Function F_QUAD2STRING
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_QUAD2STRING" (
  ec1 IN NUMBER DEFAULT NULL,
  ec2 IN NUMBER DEFAULT NULL,
  ec3 IN NUMBER DEFAULT NULL,
  ec4 IN NUMBER DEFAULT NULL)
RETURN CHAR
IS
  result VARCHAR2(255) := '';
BEGIN
  IF ec1 IS NOT NULL THEN
    result := TO_CHAR(ec1);
    IF ec2 IS NOT NULL THEN
      result := result || '.' || TO_CHAR(ec2);
      IF ec3 IS NOT NULL THEN
        result := result || '.' || TO_CHAR(ec3);
        IF ec4 IS NOT NULL THEN
          result := result || '.' || TO_CHAR(ec4);
        END IF;
      END IF;
    END IF;
  END IF;
  RETURN result;
END f_quad2string;

/
--------------------------------------------------------
--  DDL for Function F_REACTION
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_REACTION" (p_enzyme_id enzymes.enzyme_id%type)
return varchar2
as
  separator char(1) := ',';
  output    varchar2(4000) :='';
begin
  for r_reac in (select equation from reactions where enzyme_id = p_enzyme_id ) loop
     output := output||r_reac.equation||separator;
  end loop;
  output := substr (output,1,(length(output)-1)) ; -- get rid of last comma
  return output;
end;

/
--------------------------------------------------------
--  DDL for Function F_REACTION_DIR_ID
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_REACTION_DIR_ID" (
    anId in intenz_reactions.reaction_id%type,
    aDir in intenz_reactions.direction%type
    ) return NUMBER as
    dirId intenz_reactions.reaction_id%type;
begin
    select reaction_id
        into dirId
        from intenz_reactions
        where un_reaction = f_rhea_family_id(anId)
        and direction = adir;
    return dirId;
end f_reaction_dir_id;

/
--------------------------------------------------------
--  DDL for Function F_RHEA_FAMILY_ID
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_RHEA_FAMILY_ID" 
( r_id IN intenz_reactions.reaction_id%type
) RETURN NUMBER AS
  family_id intenz_reactions.reaction_id%type;
BEGIN
  select case
      when un_reaction is null then reaction_id
      else un_reaction
      end
  into family_id
  from intenz_reactions
  where reaction_id = r_id;
  RETURN family_id;
END F_RHEA_FAMILY_ID;

/
--------------------------------------------------------
--  DDL for Function F_XML_DISPLAY_SP_CODE
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ENZYME"."F_XML_DISPLAY_SP_CODE" (p_name IN names.name%type)
RETURN VARCHAR2
DETERMINISTIC
AS
BEGIN
    RETURN CHEBI_DEV10.xml_display_sp_code ('/transport/downloads/special_characters/', 'specialCharacters.xml', p_name);
END;

/
--------------------------------------------------------
--  DDL for Package AUDITPACKAGE
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "ENZYME"."AUDITPACKAGE" AS
  osuser    VARCHAR2(30);
  remark    VARCHAR2(4000);

  /* set session wide remark for all audit actions */
  PROCEDURE setremark (s IN VARCHAR2);

  /* shortcut for procedure setremark */
  PROCEDURE put       (s IN VARCHAR2);

  /* reset any remark to null */
  PROCEDURE clrremark;

  /* shortcut for clrremark */
  PROCEDURE clr;

END auditpackage;

/
--------------------------------------------------------
--  DDL for Package EVENT
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "ENZYME"."EVENT" 
AS
  /* transit_class value for created enzymes */
  const_create      CONSTANT future_events.event_class%type := 'NEW';

  /* transit_class value for modified enzymes */
  const_modify      CONSTANT future_events.event_class%type := 'MOD';

  /* event_class value for enzymes to be transferred */
  const_transfer    CONSTANT future_events.event_class%type := 'TRA';

  /* event_class value for enzymes to be deleted */
  const_delete      CONSTANT future_events.event_class%type := 'DEL';



  /*------------------------------------ INTERFACE ------------------------------------*/

  /* TIMER PROCEDURE INTERFACES */

  /*
    Start a timeout for a certain enzyme.

    Usage:
      exec enzyme.event.p_start_timeout (<timeout_id>, <enzyme_id>);
  */
  PROCEDURE p_start_timeout (
    the_timeout_id IN timeouts.timeout_id%type,
    the_enzyme_id  IN enzymes.enzyme_id%type
  );

  /*
    Restart a timeout for a certain enzyme.

    Usage:
      exec enzyme.event.p_restart_timeout (<timeout_id>);
  */
  PROCEDURE p_restart_timeout (
    the_timeout_id IN timeouts.timeout_id%type
  );

  /*
    Stop a timeout for a certain enzyme.

    Usage:
      exec enzyme.event.p_stop_timeout (<timeout_id>);
  */
  PROCEDURE p_stop_timeout (
    the_timeout_id IN timeouts.timeout_id%type
  );


  /* FUTURE_GRAPH PROCEDURES INTERFACES */

  /*
    Notify the future_history table that a new enzyme has been suggested.
    Usage:
      exec enzyme.event.p_insert_future_creation (<enzyme_id>);
    Optional parameters:
      the_event_year: date of creation
  */
  PROCEDURE p_insert_future_creation (
    the_enzyme_id    IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE
  );

  /*
    Notify the future_history table that a new enzyme's status has been updated.
    Usage:
      exec enzyme.event.p_update_future_creation (<enzyme_id>);
  */
  PROCEDURE p_update_future_creation (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type
  );

  /*
    Notify the future_history table that an enzyme has been modified.
    Usage:
      exec enzyme.event.p_insert_future_modification (<from_enzyme_id>, <to_enzyme_id>);
    Optional parameters:
      the_event_year: date of transition
      the_status    : necessary for intial update only
  */
  PROCEDURE p_insert_future_modification (
    the_before_id    IN enzymes.enzyme_id%type,
    the_after_id     IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE,
    the_status       IN future_events.status%type DEFAULT 'SU'
  );

  /*
    Notify the future_history table that a modified enzyme's status has been updated.
    Usage:
      exec enzyme.event.p_update_future_modification (<group_id>, <event_id>, <new_status>);
  */
  PROCEDURE p_update_future_modification (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type
  );

  /*
    Notify the future_history table that an enzyme is inofficially transferred.
    Usage:
      exec enzyme.event.p_insert_future_transfer (<from_enzyme_id>, <to_enzyme_id>);
    Optional parameters:
      the_event_year: date of transition
      the_event_note: optional note about the transition
      the_status    : necessary for intial update only
  */
  PROCEDURE p_insert_future_transfer (
    the_before_id    IN enzymes.enzyme_id%type,
    the_after_id     IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE,
    the_event_note   IN future_events.event_note%type DEFAULT NULL,
    the_status       IN future_events.status%type DEFAULT 'SU'
  );

  /*
    Updates a transfer event. In case of an approval all necessary steps of updating the history_events table will be performed.
    Usage:
      exec enzyme.event.p_update_future_transfer(<group_id>, <event_id>, <new_status>, <new_note>);
  */
  PROCEDURE p_update_future_transfer (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type,
    the_new_note       IN future_events.event_note%type,
    the_history_line   IN enzymes.history%type
  );


  /*
    Notify the future_history table that an enzyme is inofficially deleted.
    Usage:
      exec enzyme.event.p_insert_future_deletion (<enzyme_id>, <to_enzyme_id>);
    Optional parameters:
      the_event_year: date of deletion
      the_event_note: optional note about the deletion
      the_status    : necessary for intial update only
  */
  PROCEDURE p_insert_future_deletion (
    the_before_id    IN enzymes.enzyme_id%type,
    the_after_id     IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE,
    the_event_note   IN future_events.event_note%type DEFAULT NULL,
    the_status       IN future_events.status%type DEFAULT 'SU'
  );

  /*
    Updates a deletion event. In case of an approval all necessary steps of updating the history_events table will be performed.
    Usage:
      exec enzyme.event.p_update_future_deletion(<group_id>, <event_id>, <new_status>, <new_note>);
  */
  PROCEDURE p_update_future_deletion (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type,
    the_new_note       IN future_events.event_note%type
  );

END event;

/
--------------------------------------------------------
--  DDL for Package Body AUDITPACKAGE
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "ENZYME"."AUDITPACKAGE" AS

  PROCEDURE setremark (s IN VARCHAR2) IS
  BEGIN
    remark := s;
    DBMS_APPLICATION_INFO.SET_ACTION(remark);
  END setremark;

  PROCEDURE put (s IN VARCHAR2) IS
  BEGIN
    setremark(s);
  END put;

  PROCEDURE clrremark IS
  BEGIN
    setremark(NULL);
  END clrremark;

  PROCEDURE clr IS
  BEGIN
    clrremark;
  END clr;

BEGIN
  SELECT MAX(osuser) INTO osuser
    FROM V$SESSION
   WHERE audsid = userenv('sessionid');
END auditpackage;

/
--------------------------------------------------------
--  DDL for Package Body EVENT
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "ENZYME"."EVENT" 
AS

  /* TIMER PROCEDURES BODIES */

  PROCEDURE p_start_timeout (
    the_timeout_id IN timeouts.timeout_id%type,
    the_enzyme_id  IN enzymes.enzyme_id%type
    )
  IS
  BEGIN
    INSERT INTO timeouts (timeout_id, enzyme_id, start_date, due_date)
      VALUES (the_timeout_id, the_enzyme_id, sysdate, add_months(sysdate, 2));
  END;

  /* Restart the timeout for a certain enzyme */
  PROCEDURE p_restart_timeout (
    the_timeout_id IN timeouts.timeout_id%type
    )
  IS
  BEGIN
    UPDATE timeouts
       SET start_date = sysdate,
           due_date   = add_months(sysdate,2)
     WHERE timeout_id = the_timeout_id;
  END;

  /* Stop a timeout for a certain enzyme */
  PROCEDURE p_stop_timeout (
    the_timeout_id IN timeouts.timeout_id%type
    )
  IS
  BEGIN
    DELETE
      FROM timeouts
     WHERE timeout_id = the_timeout_id;
  END;


  /* FUTURE_GRAPH PROCEDURES BODIES */

  PROCEDURE p_insert_future_creation (
    the_enzyme_id    IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE
    )
  IS
    future_event_group_id   NUMBER(7);
    future_event_id         NUMBER(7);
    future_event_timeout_id NUMBER(7);
  BEGIN
    SELECT enzyme.s_future_group_id.nextval INTO future_event_group_id FROM DUAL;
    SELECT enzyme.s_future_event_id.nextval INTO future_event_id FROM DUAL;
    SELECT enzyme.s_timeout_id.nextval INTO future_event_timeout_id FROM DUAL;

    p_start_timeout(future_event_timeout_id, the_enzyme_id);

    INSERT INTO future_events(group_id, event_id, after_id, event_year, event_class, status, timeout_id)
      VALUES (future_event_group_id, future_event_id, the_enzyme_id, the_event_year, const_create, 'SU', future_event_timeout_id);
  END p_insert_future_creation;

  PROCEDURE p_insert_future_modification (
    the_before_id    IN enzymes.enzyme_id%type,
    the_after_id     IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE,
    the_status       IN future_events.status%type DEFAULT 'SU'
    )
  IS
    future_event_group_id   NUMBER(7);
    future_event_id         NUMBER(7);
    future_event_timeout_id NUMBER(7);
  BEGIN
    SELECT enzyme.s_future_group_id.nextval INTO future_event_group_id FROM DUAL;
    SELECT enzyme.s_future_event_id.nextval INTO future_event_id FROM DUAL;
    SELECT enzyme.s_timeout_id.nextval INTO future_event_timeout_id FROM DUAL;

    p_start_timeout(future_event_timeout_id, the_after_id);

    INSERT INTO future_events(group_id, event_id, before_id, after_id, event_year, event_class, status, timeout_id)
      VALUES (future_event_group_id, future_event_id, the_before_id, the_after_id, the_event_year, const_modify, 'SU', future_event_timeout_id);
  END p_insert_future_modification;

  PROCEDURE p_insert_future_transfer (
    the_before_id    IN enzymes.enzyme_id%type,
    the_after_id     IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE,
    the_event_note   IN future_events.event_note%type DEFAULT NULL,
    the_status       IN future_events.status%type DEFAULT 'SU'
    )
  IS
    future_event_group_id   NUMBER(7);
    future_event_id         NUMBER(7);
    future_event_timeout_id NUMBER(7);
  BEGIN
    SELECT enzyme.s_future_group_id.nextval INTO future_event_group_id FROM DUAL;
    SELECT enzyme.s_future_event_id.nextval INTO future_event_id FROM DUAL;
    SELECT enzyme.s_timeout_id.nextval INTO future_event_timeout_id FROM DUAL;

    p_start_timeout(future_event_timeout_id, the_after_id);

    INSERT INTO future_events(group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id)
      VALUES (future_event_group_id, future_event_id, the_before_id, the_after_id, the_event_year, the_event_note, const_transfer, the_status, future_event_timeout_id);
  END p_insert_future_transfer;

  PROCEDURE p_insert_future_deletion (
    the_before_id    IN enzymes.enzyme_id%type,
    the_after_id     IN enzymes.enzyme_id%type,
    the_event_year   IN date DEFAULT SYSDATE,
    the_event_note   IN future_events.event_note%type DEFAULT NULL,
    the_status       IN future_events.status%type DEFAULT 'SU'
    )
  IS
    future_event_group_id   NUMBER(7);
    future_event_id         NUMBER(7);
    future_event_timeout_id NUMBER(7);
  BEGIN
    SELECT enzyme.s_future_group_id.nextval INTO future_event_group_id FROM DUAL;
    SELECT enzyme.s_future_event_id.nextval INTO future_event_id FROM DUAL;
    SELECT enzyme.s_timeout_id.nextval INTO future_event_timeout_id FROM DUAL;

    p_start_timeout(future_event_timeout_id, the_after_id);

    INSERT INTO future_events(group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id)
      VALUES (future_event_group_id, future_event_id, the_before_id, the_after_id, the_event_year, the_event_note, const_delete, the_status, future_event_timeout_id);
  END p_insert_future_deletion;

  PROCEDURE p_update_future_creation (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type
    )
  IS
    history_event_group_id    NUMBER(7);
    history_event_id          NUMBER(7);
    history_event_before_id   NUMBER(7);
    history_event_after_id    NUMBER(7);
    history_event_year        DATE;
    history_event_note        VARCHAR2(2000);
    history_event_class       VARCHAR2(20);
    future_event_timeout_id   NUMBER(7);
  BEGIN

    IF the_new_status = 'OK' THEN

      /* Get ids for the history event. */
      SELECT enzyme.s_history_group_id.nextval INTO history_event_group_id FROM DUAL;
      SELECT enzyme.s_history_event_id.nextval INTO history_event_id FROM DUAL;

      SELECT before_id, after_id, event_year, event_note, event_class, timeout_id
        INTO history_event_before_id, history_event_after_id, history_event_year, history_event_note, history_event_class, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      INSERT INTO enzyme.history_events(group_id, event_id, after_id, event_year, event_note, event_class)
        VALUES(history_event_group_id, history_event_id, history_event_after_id, history_event_year, history_event_note, history_event_class);

      UPDATE enzyme.enzymes SET status = the_new_status, active = 'Y' WHERE enzyme_id = history_event_after_id;

      DELETE FROM enzyme.future_events WHERE group_id = the_group_id AND event_id = the_event_id;

      p_stop_timeout(future_event_timeout_id);

    ELSE

      UPDATE enzyme.future_events SET status = the_new_status
        WHERE group_id = the_group_id AND event_id = the_event_id;

      SELECT after_id, timeout_id
        INTO history_event_after_id, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      UPDATE enzyme.enzymes SET status = the_new_status
        WHERE enzyme_id = history_event_after_id;

      IF the_new_status = 'PR' THEN
        p_restart_timeout(future_event_timeout_id);
      END IF;

    END IF;

  END p_update_future_creation;

  PROCEDURE p_update_future_modification (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type
    )
  IS
    history_event_group_id    NUMBER(7);
    history_event_id          NUMBER(7);
    history_event_before_id   NUMBER(7);
    history_event_after_id    NUMBER(7);
    history_event_year        DATE;
    history_event_note        VARCHAR2(2000);
    history_event_class       VARCHAR2(20);
    future_event_timeout_id   NUMBER(7);
  BEGIN

    IF the_new_status = 'OK' THEN

      SELECT before_id, after_id, event_year, event_note, event_class, timeout_id
        INTO history_event_before_id, history_event_after_id, history_event_year, history_event_note, history_event_class, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      /* Get ids for the history event. */
      SELECT enzyme.s_history_group_id.nextval INTO history_event_group_id FROM DUAL;
      SELECT enzyme.s_history_event_id.nextval INTO history_event_id FROM DUAL;

      INSERT INTO enzyme.history_events(group_id, event_id, before_id, after_id, event_year, event_note, event_class)
        VALUES(history_event_group_id, history_event_id, history_event_before_id, history_event_after_id, history_event_year, history_event_note, history_event_class);

      /* Get ids for the history event. */
      SELECT enzyme.s_history_group_id.nextval INTO history_event_group_id FROM DUAL;
      SELECT enzyme.s_history_event_id.nextval INTO history_event_id FROM DUAL;

      INSERT INTO enzyme.history_events(group_id, event_id, after_id, event_year, event_class)
        VALUES(history_event_group_id, history_event_id, history_event_after_id, history_event_year, 'NEW');

      UPDATE enzyme.enzymes SET status = the_new_status, active = 'Y' WHERE enzyme_id = history_event_after_id;

      UPDATE enzyme.enzymes SET active = 'N' WHERE enzyme_id = history_event_before_id;

      DELETE FROM enzyme.future_events WHERE group_id = the_group_id AND event_id = the_event_id;

      p_stop_timeout(future_event_timeout_id);

    ELSE

      UPDATE enzyme.future_events SET status = the_new_status
        WHERE group_id = the_group_id AND event_id = the_event_id;

      SELECT after_id, timeout_id
        INTO history_event_after_id, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      UPDATE enzyme.enzymes SET status = the_new_status
        WHERE enzyme_id = history_event_after_id;

      IF the_new_status = 'PR' THEN
        p_restart_timeout(future_event_timeout_id);
      END IF;

    END IF;

  END p_update_future_modification;

  PROCEDURE p_update_future_transfer (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type,
    the_new_note       IN future_events.event_note%type,
    the_history_line   IN enzymes.history%type
    )
  IS
    history_event_group_id    NUMBER(7);
    history_event_id          NUMBER(7);
    history_event_before_id   NUMBER(7);
    history_event_after_id    NUMBER(7);
    history_event_year        DATE;
    history_event_note        VARCHAR2(2000);
    history_event_class       VARCHAR2(20);
    future_event_timeout_id   NUMBER(7);
  BEGIN

    IF the_new_status = 'OK' THEN

      SELECT before_id, after_id, event_year, event_note, event_class, timeout_id
        INTO history_event_before_id, history_event_after_id, history_event_year, history_event_note, history_event_class, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      /* Get ids for the history event. */
      SELECT enzyme.s_history_group_id.nextval INTO history_event_group_id FROM DUAL;
      SELECT enzyme.s_history_event_id.nextval INTO history_event_id FROM DUAL;

      /* Add event to old enzyme. */
      INSERT INTO enzyme.history_events(group_id, event_id, before_id, after_id, event_year, event_note, event_class)
        VALUES(history_event_group_id, history_event_id, history_event_before_id, history_event_after_id, history_event_year, the_new_note, history_event_class);

      /* Get ids for the history event. */
      SELECT enzyme.s_history_group_id.nextval INTO history_event_group_id FROM DUAL;
      SELECT enzyme.s_history_event_id.nextval INTO history_event_id FROM DUAL;

      /* Create new event for new enzymes. */
      INSERT INTO enzyme.history_events(group_id, event_id, after_id, event_year, event_class)
        VALUES(history_event_group_id, history_event_id, history_event_after_id, history_event_year, 'NEW');

      UPDATE enzyme.enzymes SET history = the_history_line, active = 'N' WHERE enzyme_id = history_event_before_id;

      UPDATE enzyme.enzymes SET status = the_new_status, active = 'Y' WHERE enzyme_id = history_event_after_id;

      DELETE FROM enzyme.future_events WHERE group_id = the_group_id AND event_id = the_event_id;

      p_stop_timeout(future_event_timeout_id);

    ELSE

      UPDATE enzyme.future_events SET status = the_new_status, event_note = the_new_note
        WHERE group_id = the_group_id AND event_id = the_event_id;

      SELECT after_id, timeout_id
        INTO history_event_after_id, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      UPDATE enzyme.enzymes SET status = the_new_status
        WHERE enzyme_id = history_event_after_id;

      IF the_new_status = 'PR' THEN
        p_restart_timeout(future_event_timeout_id);
      END IF;

    END IF;

  END p_update_future_transfer;

  PROCEDURE p_update_future_deletion (
    the_group_id       IN future_events.group_id%type,
    the_event_id       IN future_events.event_id%type,
    the_new_status     IN future_events.status%type,
    the_new_note       IN future_events.event_note%type
    )
  IS
    history_event_group_id    NUMBER(7);
    history_event_id          NUMBER(7);
    history_event_before_id   NUMBER(7);
    history_event_after_id    NUMBER(7);
    history_event_year        DATE;
    history_event_note        VARCHAR2(2000);
    history_event_class       VARCHAR2(20);
    enzyme_history_line       VARCHAR2(1000);
    future_event_timeout_id   NUMBER(7);
  BEGIN

    IF the_new_status = 'OK' THEN

      /* Get ids for the history event. */
      SELECT enzyme.s_history_group_id.nextval INTO history_event_group_id FROM DUAL;
      SELECT enzyme.s_history_event_id.nextval INTO history_event_id FROM DUAL;

      SELECT before_id, after_id, event_year, event_note, event_class, timeout_id
        INTO history_event_before_id, history_event_after_id, history_event_year, history_event_note, history_event_class, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      SELECT history INTO enzyme_history_line
        FROM enzyme.enzymes
        WHERE enzyme_id = history_event_after_id;

      INSERT INTO enzyme.history_events(group_id, event_id, before_id, event_year, event_note, event_class)
        VALUES(history_event_group_id, history_event_id, history_event_before_id, history_event_year, the_new_note, history_event_class);

      UPDATE enzyme.enzymes SET history = enzyme_history_line, active = 'N' WHERE enzyme_id = history_event_before_id;

      DELETE FROM enzyme.future_events WHERE group_id = the_group_id AND event_id = the_event_id;

      DELETE FROM enzyme.enzymes WHERE enzyme_id = history_event_after_id;

      p_stop_timeout(future_event_timeout_id);

    ELSE

      UPDATE enzyme.future_events SET status = the_new_status, event_note = the_new_note
        WHERE group_id = the_group_id AND event_id = the_event_id;

      SELECT after_id, timeout_id
        INTO history_event_after_id, future_event_timeout_id
        FROM enzyme.future_events
        WHERE group_id = the_group_id AND event_id = the_event_id;

      UPDATE enzyme.enzymes SET status = the_new_status
        WHERE enzyme_id = history_event_after_id;

      IF the_new_status = 'PR' THEN
        p_restart_timeout(future_event_timeout_id);
      END IF;

    END IF;

  END p_update_future_deletion;

END event;

/
--------------------------------------------------------
--  DDL for Procedure P_CHANGE_STATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_CHANGE_STATUS" (
  the_enzyme_id IN enzymes.enzyme_id%type,
  the_status    IN enzymes.status%type
)
AS
  CURSOR lockcursor IS
  SELECT 1
    FROM enzymes e1,
         names n1,
         comments c1,
         xrefs x1,
         links l1
   WHERE e1.enzyme_id = the_enzyme_id
     AND e1.enzyme_id = n1.enzyme_id (+)
     AND e1.enzyme_id = c1.enzyme_id (+)
     AND e1.enzyme_id = x1.enzyme_id (+)
     AND e1.enzyme_id = l1.enzyme_id (+)
         FOR UPDATE;
  lockrec lockcursor%rowtype;
BEGIN
  /* check the_enzyme_id is not null */
  IF the_enzyme_id IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_enzyme_id is null');
  END IF;
  /* check the_status is not null */
  IF the_status IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_status is null');
  END IF;
  /* lock affected rows */
  OPEN lockcursor;
  /* check if the enzyme exists */
  DECLARE
    tmp_id enzymes.enzyme_id%type;
  BEGIN
    SELECT enzyme_id INTO tmp_id
      FROM enzymes
     WHERE enzyme_id = the_enzyme_id;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_enzyme_id not found');
  END;
  /* do table enzymes */
  UPDATE enzymes
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table names */
  UPDATE names
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table comments */
  UPDATE comments
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table xrefs */
  UPDATE xrefs
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table links */
  UPDATE links
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  CLOSE lockcursor;
END;

/
--------------------------------------------------------
--  DDL for Procedure P_CLONE_ENZYME
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_CLONE_ENZYME" (
  old_enzyme_id IN  enzymes.enzyme_id%type,
  new_enzyme_id OUT enzymes.enzyme_id%type,
  new_status    IN  enzymes.status%type DEFAULT 'SC',
  new_active    IN  enzymes.active%type DEFAULT 'N'
)
AS
  enz_a enzymes%rowtype;
BEGIN
  /* lock affected tables to be transaction save */
  LOCK TABLE enzymes   		IN EXCLUSIVE MODE;
  LOCK TABLE names     		IN EXCLUSIVE MODE;
  LOCK TABLE reactions_map	IN EXCLUSIVE MODE;
  LOCK TABLE comments  		IN EXCLUSIVE MODE;
  LOCK TABLE xrefs     		IN EXCLUSIVE MODE;
  LOCK TABLE links     		IN EXCLUSIVE MODE;

  /* get source enzyme */
  BEGIN
    SELECT *
      INTO enz_a
      FROM enzymes
     WHERE enzyme_id = old_enzyme_id;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20000, 'source enzyme not found');
  END;

  /* get new enzyme_id */
  SELECT s_enzyme_id.nextval
    INTO new_enzyme_id
    FROM DUAL;
  --DBMS_OUTPUT.PUT_LINE('got new enzyme_id='||new_enzyme_id);

  /*  clone enzyme */
  INSERT INTO enzymes
         (enzyme_id, status, active,
          ec1, ec2, ec3, ec4,
          history, note, source)
  VALUES (new_enzyme_id, new_status, new_active,
          enz_a.ec1, enz_a.ec2, enz_a.ec3, enz_a.ec4,
          enz_a.history, enz_a.note, enz_a.source);
  --DBMS_OUTPUT.PUT_LINE('Done enzymes');

  /* clone names*/
  INSERT INTO names (
         enzyme_id, status,
         name, name_class, warning, source, note, order_in, web_view)
  SELECT new_enzyme_id, new_status,
         name, name_class, warning, source, note, order_in, web_view
    FROM names
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done names');

  /* clone reactions mapping */
  INSERT INTO reactions_map (enzyme_id, reaction_id, web_view, order_in)
  SELECT new_enzyme_id, reaction_id, web_view, order_in
    FROM reactions_map
   WHERE enzyme_id = old_enzyme_id;

  /* clone abstract (free text) reactions */
  INSERT INTO reactions (enzyme_id, equation, order_in, status, source, web_view)
  SELECT new_enzyme_id, equation, order_in, status, source, web_view
    FROM reactions
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done reactions');

  /* clone comments */
  INSERT INTO comments (
         enzyme_id, status,
         comment_text, order_in, source, web_view)
  SELECT new_enzyme_id, new_status,
         comment_text, order_in, source, web_view
    FROM comments
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done comments');

  /* clone xrefs */
  INSERT INTO xrefs (
         enzyme_id, status,
         database_code, database_ac, name, source, data_comment, web_view)
  SELECT new_enzyme_id, new_status,
         database_code, database_ac, name, source, data_comment, web_view
    FROM xrefs
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done xrefs');

  /* clone links */
  INSERT INTO links (
         enzyme_id, status,
         url, display_name, source, data_comment, web_view)
  SELECT new_enzyme_id, new_status,
         url, display_name, source, data_comment, web_view
    FROM links
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done links');

  /* clone citations */
  INSERT INTO citations (
         enzyme_id, status,
         pub_id, order_in, source)
  SELECT new_enzyme_id, new_status,
         pub_id, order_in, source
    FROM citations
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done citations');

END;

/
--------------------------------------------------------
--  DDL for Procedure P_INSERT_COMPOUND_DATA
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_INSERT_COMPOUND_DATA" (
	new_id				IN OUT	compound_data.compound_id%type,
	new_name			IN		compound_data.name%type,
	new_formula			IN		compound_data.formula%type,
	new_charge			IN		compound_data.charge%type,
	new_source			IN		compound_data.source%type,
	new_accession		IN		compound_data.accession%type,
	new_child_accession	IN		compound_data.child_accession%type,
	new_published		IN		compound_data.published%type
)
AS
	existing_id		compound_data.compound_id%type;
BEGIN
	SELECT compound_id INTO existing_id
		FROM compound_data
		WHERE name = new_name OR (source = new_source AND accession = new_accession);
	IF existing_id IS NOT NULL THEN
		new_id := -existing_id;
	END IF;
	EXCEPTION
		WHEN NO_DATA_FOUND
			THEN
				IF new_id IS NULL THEN
				    SELECT s_compound_id.nextval INTO new_id FROM DUAL;
				END IF;
				INSERT INTO compound_data (compound_id, name, formula, charge, source, accession, child_accession, published)
				VALUES (new_id, new_name, new_formula, new_charge, new_source, new_accession, new_child_accession, new_published);
END;

/
--------------------------------------------------------
--  DDL for Procedure P_INSERT_REACTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_INSERT_REACTION" 
  (
    new_id               IN OUT intenz_reactions.reaction_id%type,
    new_accession        IN intenz_reactions.intenz_accession%type,
    new_equation         IN intenz_reactions.equation%type,
    new_fp               IN intenz_reactions.fingerprint%type,
    new_status           IN intenz_reactions.status%type,
    new_source           IN intenz_reactions.source%type,
    new_qualifiers       IN intenz_reactions.qualifiers%type,
    new_data_comment     IN intenz_reactions.data_comment%type,
    new_reaction_comment IN intenz_reactions.reaction_comment%type,
    new_direction        IN intenz_reactions.direction%type,
    new_un_reaction      in intenz_reactions.un_reaction%type,
    new_iubmb            IN intenz_reactions.iubmb%type)
AS
  existing_id intenz_reactions.reaction_id%type;
BEGIN
   SELECT reaction_id
     INTO existing_id
     FROM intenz_reactions
    WHERE equation = new_equation
  AND fingerprint  = new_fp;
  IF existing_id  IS NOT NULL THEN
    new_id        := -existing_id;
  END IF;
EXCEPTION
WHEN NO_DATA_FOUND THEN
  IF new_id IS NULL THEN
     SELECT s_reaction_id.nextval INTO new_id FROM DUAL;
  END IF;
   INSERT
     INTO intenz_reactions
    (
      reaction_id     ,
      intenz_accession,
      equation        ,
      fingerprint     ,
      status          ,
      source          ,
      qualifiers      ,
      data_comment    ,
      reaction_comment,
      direction       ,
      un_reaction,
      iubmb
    )
    VALUES
    (
      new_id              ,
      new_accession       ,
      new_equation        ,
      new_fp              ,
      new_status          ,
      new_source          ,
      new_qualifiers      ,
      new_data_comment    ,
      new_reaction_comment,
      new_direction       ,
      new_un_reaction,
      new_iubmb
    );
END;

/
--------------------------------------------------------
--  DDL for Procedure P_MERGE_PUBLICATIONS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_MERGE_PUBLICATIONS" (
  the_pub_id_from IN publications.pub_id%type,
  the_pub_id_to   IN publications.pub_id%type
)
AS
  CURSOR cit_cur IS
  SELECT *
    FROM citations
   WHERE pub_id = the_pub_id_from
     FOR UPDATE OF pub_id;
  CURSOR lock_work IS
  SELECT 1
    FROM citations c,
         publications p
   WHERE c.pub_id = p.pub_id
     AND (p.pub_id = the_pub_id_from OR p.pub_id = the_pub_id_to)
    FOR UPDATE;
  cit_a cit_cur%ROWTYPE;      -- cursor record for old citations
  pub_a publications%ROWTYPE; -- local var for old publications
  pub_b publications%ROWTYPE; -- local var for new publications
  old_audit_remark VARCHAR2(200);
BEGIN
  /* check a != b */
  IF the_pub_id_from = the_pub_id_to THEN
    RAISE_APPLICATION_ERROR(-20000, 'Cant merge publication to itself');
  END IF;
  /* check a not null */
  IF the_pub_id_from IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_from is null');
  END IF;
  /* check b not null */
  IF the_pub_id_to IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_to is null');
  END IF;
  /* lock rows for a and b in citations and publications */
  OPEN lock_work;
  /* fetch pub a and b and check they exists*/
  BEGIN
    SELECT * INTO pub_a
      FROM publications
     WHERE pub_id = the_pub_id_from;
  EXCEPTION WHEN NO_DATA_FOUND THEN
    CLOSE lock_work;
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_from doesnt exist');
  END;
  BEGIN
    SELECT * INTO pub_b
      FROM publications
     WHERE pub_id = the_pub_id_to;
  EXCEPTION WHEN NO_DATA_FOUND THEN
    CLOSE lock_work;
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_to doesnt exist');
  END;
  /* set audit remark (and save old remark for restoring it later) */
  old_audit_remark := auditpackage.remark;
  auditpackage.setremark('merge publication ' || the_pub_id_from ||
    ' to ' || the_pub_id_to);
  /* change citations with the_pub_id_from to the_pub_id_to, if any */
  FOR cit_a IN cit_cur LOOP
    DBMS_OUTPUT.PUT_LINE('Changing citatation of enzyme_id='||cit_a.enzyme_id||
      ' from pub_id='||cit_a.pub_id||' to '||the_pub_id_to);
    UPDATE citations
       SET pub_id = the_pub_id_to
     WHERE CURRENT OF cit_cur;
  END LOOP;
  /* delete publication the_pub_id_from */
  DELETE
    FROM publications
   WHERE pub_id = the_pub_id_from;
  /* restore auditremark as it was before calling this procedure */
  auditpackage.setremark(old_audit_remark);
  /* close locking cursor, note: rows will be unlocked by next commit */
  CLOSE lock_work;
  /* final debug remark */
  DBMS_OUTPUT.PUT_LINE('publication '|| the_pub_id_from ||
    ' merged to ' || the_pub_id_to);
END;

/
--------------------------------------------------------
--  DDL for Procedure P_PRINT_PUBLICATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_PRINT_PUBLICATION" (
  the_pub_id IN publications.pub_id%type
)
AS
  r publications%rowtype;
BEGIN
  -- fetch record
  SELECT * INTO r
    FROM publications
   WHERE pub_id = the_pub_id;
  -- RN line with pub_id
  DBMS_OUTPUT.PUT_LINE('RN   ['||r.pub_id||']');
  -- RX line with medline_id and pubmed_id
  IF (r.medline_id IS NOT NULL OR r.pubmed_id IS NOT NULL) THEN
    DBMS_OUTPUT.PUT( 'RX   ');
    IF r.medline_id IS NOT NULL THEN
      DBMS_OUTPUT.PUT('MEDLINE=' || r.medline_id || ';');
    END IF;
    IF r.pubmed_id IS NOT NULL THEN
      DBMS_OUTPUT.PUT('PubMed=' || r.pubmed_id || ';');
    END IF;
    DBMS_OUTPUT.PUT_LINE('');
  END IF;
  -- RA line with author
  DBMS_OUTPUT.PUT_LINE('RA   ' || r.author || ';');
  -- RT line with title
  DBMS_OUTPUT.PUT_LINE('RT   "' || r.title || '";');
  -- RL line
  DBMS_OUTPUT.PUT('RL   ');
  IF r.pub_type = 'J' THEN
    DBMS_OUTPUT.PUT(r.journal_book||' '||NVL(r.volume,'???')||':');
    DBMS_OUTPUT.PUT(NVL(r.first_page,'???')||'-'||NVL(r.last_page,'???'));
    DBMS_OUTPUT.PUT('('||r.pub_year||').');
  ELSE
    DBMS_OUTPUT.PUT('*** NYI pub_type '||r.pub_type);
  END IF;
  DBMS_OUTPUT.PUT_LINE('');
END p_print_publication;

/
--------------------------------------------------------
--  DDL for Procedure P_RESTART_TIMEOUT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_RESTART_TIMEOUT" (
  the_enzyme_id IN enzymes.enzyme_id%type
  )
AS
BEGIN
  UPDATE timeouts
     SET start_date = sysdate,
         due_date   = add_months(sysdate,2)
   WHERE enzyme_id = the_enzyme_id;
END;

/
--------------------------------------------------------
--  DDL for Procedure P_START_TIMEOUT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_START_TIMEOUT" (
  the_enzyme_id IN enzymes.enzyme_id%type
  )
AS
BEGIN
  INSERT INTO timeouts (enzyme_id, start_date, due_date)
  VALUES (the_enzyme_id, sysdate, add_months(sysdate, 2));
END;

/
--------------------------------------------------------
--  DDL for Procedure P_STOP_TIMEOUT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_STOP_TIMEOUT" (
  the_enzyme_id IN enzymes.enzyme_id%type
  )
AS
BEGIN
  DELETE
    FROM timeouts
   WHERE enzyme_id = the_enzyme_id;
END;

/
--------------------------------------------------------
--  DDL for Procedure P_STRING2QUAD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "ENZYME"."P_STRING2QUAD" (
  ec IN CHAR,
  ec1 OUT NUMBER,
  ec2 OUT NUMBER,
  ec3 OUT NUMBER,
  ec4 OUT NUMBER)
IS
  pos INTEGER;
  s VARCHAR2(100) := ec; -- local working copy of ec string
  p VARCHAR2(10);
BEGIN
  s := trim(s);

  -- just to be graceful, ignore any 'EC ' prefix
  IF (SUBSTR(s, 1, 2) = 'EC') THEN
    s := SUBSTR(s, 3);
    s := TRIM(s);
    --DBMS_OUTPUT.PUT_LINE('s='||s);
  END IF;

  -- ec1 vulgo class
  pos := INSTR(s, '.', 1);
  IF pos=0 THEN
    IF s != '-' THEN ec1 := TO_NUMBER(s); END IF;
    RETURN;
  ELSE
    p := SUBSTR(s, 1, pos-1);  s := SUBSTR(s, pos+1);
    --DBMS_OUTPUT.PUT_LINE('s1='||p||',s='||s);
    IF p != '-' THEN ec1 := TO_NUMBER(p); END IF;
  END IF;

  -- ec2 vulgo subclass
  pos := INSTR(s, '.', 1);
  IF pos=0 THEN
    IF s != '-' THEN ec2 := TO_NUMBER(s); END IF;
    RETURN;
  ELSE
    p := SUBSTR(s, 1, pos-1);  s := SUBSTR(s, pos+1);
    --DBMS_OUTPUT.PUT_LINE('s2='||p||',s='||s);
    IF p != '-' THEN ec2 := TO_NUMBER(p); END IF;
  END IF;

  -- ec3 vulgo subsubclass
  pos := INSTR(s, '.', 1);
  IF pos=0 THEN
    IF s != '-' THEN ec3 := TO_NUMBER(s); END IF;
    RETURN;
  ELSE
    p := SUBSTR(s, 1, pos-1);  s := SUBSTR(s, pos+1);
    --DBMS_OUTPUT.PUT_LINE('s3='||p||',s='||s);
    IF p != '-' THEN ec3 := TO_NUMBER(p); END IF;
  END IF;

  -- ec4 vulgo forth number
  IF s != '-' THEN ec4 := TO_NUMBER(s); END IF;
END;

/
