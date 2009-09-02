SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find publications that are cited
              but don't exist.

  Usage:      SQL*Plus> @check_missing_pubs
              or, from unix:
              sqlplus /@iprod @check_missing_pubs > report.lst
  
  Example output:
  
              EC              ENZYME_ID   ORDER_IN     PUB_ID
              --------------- --------- ---------- ----------
              EC 1.1.1.1      [id=1000]          1      10000
              
              EC 3.4.23.27    [id=4043]          1       4876
              EC 3.4.23.27                       2       4877
              EC 3.4.23.27                       3       4878

  Required:   role enzyme_select
    
  $Date: 2008/01/17 12:19:14 $
  $Revision: 1.4 $

 *************************************************************/


SET SERVEROUT ON SIZE 1000000
SET LINE       500;
SET TRIMSPOOL  ON;
SET TRIMOUT    ON;
SET TAB        OFF;
SET ECHO       OFF;
SET FEEDBACK   OFF;
SET PAGESIZE   600;

COLUMN ec  FORMAT A15;
COLUMN enzyme_id FORMAT A9;

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT 'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
       '[id='||e1.enzyme_id||']' as enzyme_id,
       c1.order_in,
       c1.pub_id
  FROM enzyme.enzymes e1,
       enzyme.citations c1,
       enzyme.publications p1
 WHERE e1.enzyme_id = c1.enzyme_id
   AND c1.pub_id = p1.pub_id (+)
   AND p1.pub_id IS NULL
 ORDER BY enzyme_id, order_in;
   
SELECT user,name,sysdate FROM v$database;
EXIT;
