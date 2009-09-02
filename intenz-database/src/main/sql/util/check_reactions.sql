SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find reactions that have errors
              and correct errors.

  Usage:      SQL*Plus> @check_reactions
              or, from unix:
              sqlplus /@iprod @check_reactions > report.lst
  
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
COLUMN equation FORMAT A30;

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT   'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
         '[id='||e1.enzyme_id||']' as enzyme_id,
         r1.equation,
         r1.order_in
  FROM   enzyme.enzymes e1, 
         enzyme.reactions r1
 WHERE   e1.enzyme_id = r1.enzyme_id
//AND    e1.enzyme_id BETWEEN 1630 AND 1633 //
ORDER BY enzyme_id, order_in;

SELECT user,name,sysdate FROM v$database;
EXIT;

