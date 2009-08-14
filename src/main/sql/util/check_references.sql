SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find publications that have errors
              and correct errors.

  Usage:      SQL*Plus> @check_publications
              or, from unix:
              sqlplus /@iprod @check_publications > report.lst
  
  Example output:

              EC              ENZYME_ID EQUATION                         ORDER_IN
              --------------- --------- ------------------------------ ----------
              EC 1.1.1.1      [id=1000] an alcohol + NAD = an aldehyde          1
                                        or ketone + NADH<sub><small>2
                                        </small></sub>

  Required:   role enzyme_select
    
  $Date: 2008/01/17 12:19:14 $
  $Revision: 1.2 $

 *************************************************************/


SET LINE       2000;
SET TRIMSPOOL  ON;
SET TRIMOUT    ON;
SET TAB        OFF;
SET ECHO       OFF;
SET FEEDBACK   OFF;
SET PAGESIZE   600;

COLUMN ec  FORMAT A15;
COLUMN enzyme_id FORMAT A9;

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT   'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
         '[id='||e1.enzyme_id||']' as enzyme_id,
         p1.pub_id,
         p1.medline_id,
         p1.pub_type,
         p1.author,
         p1.pub_year
         p1.title
         p1.journal_book
         p1.volume
         p1.first_page
         p1.last_page
         p1.edition
         p1.editor
         p1.pub_company
         p1.pub_place
         p1.url
         c1.order_in
  FROM   enzyme.enzymes e1, 
         enzyme.publications p1,
         enzyme.citations c1
 WHERE   e1.enzyme_id = c1.enzyme_id
   AND   p1.pub_id = c1.pub_id
//AND    e1.enzyme_id = 1000 //
ORDER BY e.enzyme_id, c.order_in;

SELECT user,name,sysdate FROM v$database;
EXIT;

