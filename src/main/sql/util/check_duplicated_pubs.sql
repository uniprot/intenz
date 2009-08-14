SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find publications that are identical
              AND cited by the same enzyme.

  Usage:      SQL*Plus> @check_duplicated_pubs
              or, from unix:
              sqlplus /@iprod @check_duplicated_pubs > report.lst
  
  Example output:
              EC             ENZYME_ID   O1 PUB_ID   O2 PUB_ID
              -------------- --------- ---- ------ ---- ------
              EC 1.1.1.188        1186    3    351    4    352
              EC 1.1.1.188        1186    3    351    5    353
              EC 1.1.1.188        1186    4    352    5    353
              
              EC 1.1.3.10         1282    2    494    3    495

              Legend: Enzyme EC 1.1.1.188 [id=1186] has three
              publications that are identical, pub_id=351,352
              and 353. Theses are cited as 3rd, 4th and 5th 
              citation.
  
  Required:   role enzyme_select
    
  $Date: 2008/01/17 12:19:14 $
  $Revision: 1.3 $

 *************************************************************/


SET SERVEROUT ON SIZE 1000000
SET LINE       500;
SET PAGESIZE   100;
SET TRIMSPOOL   ON;
SET TRIMOUT     ON;
SET TAB        OFF;
SET ECHO       OFF;
SET FEEDBACK   OFF;

COLUMN EC     format A14;
COLUMN o1     format 999;
COLUMN o2     format 999;
COLUMN pub_id format 9999;

BREAK ON enzyme_id DUP SKIP 1

PROMPT List of identical publications that are cited by the
PROMPT same enzyme.

SELECT /*+ use_hash (p1,c1) use_hash (p2,c2) */
       'EC '||enzyme.f_quad2string(ec1,ec2,ec3,ec4) as EC,
       c1.enzyme_id, 
       c1.order_in as o1, p1.pub_id, 
       c2.order_in as o2, p2.pub_id
  FROM enzyme.publications p1,
       enzyme.publications p2,
       enzyme.citations c1,
       enzyme.citations c2,
       enzyme.enzymes e1
 WHERE p1.pub_id < p2.pub_id
   AND p1.pub_id = c1.pub_id
   AND p2.pub_id = c2.pub_id
   AND c1.enzyme_id = c2.enzyme_id
   AND c1.enzyme_id = e1.enzyme_id
   AND (p1.pub_type     = p2.pub_type)
   AND (p1.author       = p2.author) 
   AND (p1.pub_year     = p2.pub_year)    
   AND (p1.journal_book = p2.journal_book)    
   AND (p1.title        = p2.title       OR (p1.title       IS NULL AND p2.title       IS NULL))    
   AND (p1.medline_id   = p2.medline_id  OR (p1.medline_id  IS NULL AND p2.medline_id  IS NULL))
   AND (p1.pubmed_id    = p2.pubmed_id   OR (p1.pubmed_id   IS NULL AND p2.pubmed_id   IS NULL))
   AND (p1.volume       = p2.volume      OR (p1.volume      IS NULL AND p2.volume      IS NULL))
   AND (p1.first_page   = p2.first_page  OR (p1.first_page  IS NULL AND p2.first_page  IS NULL))
   AND (p1.last_page    = p2.last_page   OR (p1.last_page   IS NULL AND p2.last_page   IS NULL))
   AND (p1.edition      = p2.edition     OR (p1.edition     IS NULL AND p2.edition     IS NULL))
   AND (p1.editor       = p2.editor      OR (p1.editor      IS NULL AND p2.editor      IS NULL))
   AND (p1.pub_company  = p2.pub_company OR (p1.pub_company IS NULL AND p2.pub_company IS NULL))
   AND (p1.pub_place    = p2.pub_place   OR (p1.pub_place   IS NULL AND p2.pub_place   IS NULL))
   AND (p1.url          = p2.url         OR (p1.url         IS NULL AND p2.url         IS NULL))
 ORDER BY c1.enzyme_id, c1.order_in, c2.order_in
   ;


SELECT user,name,sysdate FROM v$database;
EXIT;
