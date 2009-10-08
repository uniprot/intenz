SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find duplicated publications.

  Usage:      SQL*Plus> @check_identical_pubs
              or, from unix:
              sqlplus /@iprod @check_identical_pubs > report.lst
  
  Example output:
  
              Biochem. Z. 334 (1961),  Jaenicke, L. and Brode, E.
              -  pub_id=6720  -  used as #1 in EC 6.3.4.3 [id=5132]
              -  pub_id=6721  -  used as #2 in EC 6.3.4.3 [id=5132]
              J. Biol. Chem. 217 (1955),  Weimberg, R. and Doudoroff, M.
              -  pub_id=126   -  used as #1 in EC 1.1.1.46 [id=1045]
              -  pub_id=3883  -  used as #1 in EC 3.1.1.15 [id=3190]
              -  pub_id=5984  -  used as #1 in EC 4.2.1.25 [id=4686]
  
  Required:   role enzyme_select
    
  $Date: 2008/01/17 12:19:14 $
  $Revision: 1.10 $

 *************************************************************/


SET SERVEROUT ON SIZE 1000000
SET LINE       500;
SET TRIMSPOOL  ON;
SET TRIMOUT    ON;
SET TAB        OFF;
SET ECHO       OFF;
SET FEEDBACK   OFF;

DECLARE
  /* cursor to find groups of duplicated publications */
  CURSOR c1 IS
  SELECT min(pub_id) as pub_id, count(pub_id) as dupcount
    FROM enzyme.publications   
   GROUP BY pub_type, author, pub_year, title, 
            journal_book, medline_id, pubmed_id, volume,
            first_page, last_page, edition, editor, 
            pub_company, pub_place, url
  HAVING count(pub_id) > 1
  ORDER BY 2 DESC, 1;
  r1 c1%rowtype;
  
  /* cursor to find publications which are identical to a give pub_id */
  CURSOR c2 (the_pub_id IN enzyme.publications.pub_id%type) IS
  SELECT p2.pub_id, p2.journal_book, p2.volume, p2.pub_year,p2.author
    FROM enzyme.publications p1,
         enzyme.publications p2
   WHERE p1.pub_id = the_pub_id
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
   ORDER BY p2.pub_id;
  p2 c2%rowtype;
  /* cursor to find enzyme usage of a given pub_id */
  CURSOR c3 (the_pub_id IN enzyme.publications.pub_id%type) IS
  SELECT enzymes.enzyme_id, citations.order_in, ec1,ec2,ec3,ec4
    FROM enzyme.enzymes,
         enzyme.citations
   WHERE citations.pub_id = the_pub_id
     AND citations.enzyme_id = enzymes.enzyme_id;
  e3 c3%rowtype;
  total_groups number := 0;
  count2 number;
  line VARCHAR2(2000);
BEGIN
  FOR r1 IN c1 LOOP
    total_groups := total_groups + 1;
    count2 := 0;
    --EXIT WHEN count1 > 10;
    --DBMS_OUTPUT.PUT('pub_id='||r1.pub_id||', #duplicates='||r1.dupcount);
    FOR p2 IN c2 (r1.pub_id) LOOP
      count2 := count2 + 1;
      IF count2 = 1 THEN
        line := p2.journal_book||' '||p2.volume||' ('||p2.pub_year||'),  '|| p2.author;
        IF length(line) > 250 THEN
          line := substr(line,1,250) || '...';
        END IF;
        DBMS_OUTPUT.PUT_LINE(line);
      END IF;
      DBMS_OUTPUT.PUT ('-  pub_id='||rpad(p2.pub_id,4));
      FOR e3 IN c3 (p2.pub_id) LOOP
        DBMS_OUTPUT.PUT_LINE(
          '  -  used as #'||e3.order_in||' in EC '||enzyme.f_quad2string(e3.ec1,e3.ec2,e3.ec3,e3.ec4)||
          ' [id='||e3.enzyme_id||']');
      END LOOP;
    END LOOP;
  END LOOP;
END;
/


SELECT user,name,sysdate FROM v$database;
EXIT;
