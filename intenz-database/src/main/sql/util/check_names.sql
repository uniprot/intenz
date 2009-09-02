SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find names that have errors
              and correct errors.

  Usage:      SQL*Plus> @check_names
              or, from unix:
              sqlplus /@iprod @check_names > report.lst
  
  Example output:

              EC              ENZYME_ID NAME                                          NAME_CLASS
              --------------- --------- --------------------------------------------- ----------
              EC 1.1.1.1      [id=1000] alcohol dehydrogenase                         COM
              EC 1.1.1.1                ADH                                           OTH
              EC 1.1.1.1                NAD-dependent alcohol dehydrogenase           OTH
              EC 1.1.1.1                NAD-specific aromatic alcohol dehydrogenase   OTH
              EC 1.1.1.1                NADH-alcohol dehydrogenase                    OTH
              EC 1.1.1.1                NADH-aldehyde dehydrogenase                   OTH
              EC 1.1.1.1                alcohol dehydrogenase (NAD)                   OTH
              EC 1.1.1.1                aldehyde reductase                            OTH
              EC 1.1.1.1                aliphatic alcohol dehydrogenase               OTH
              EC 1.1.1.1                ethanol dehydrogenase                         OTH
              EC 1.1.1.1                primary alcohol dehydrogenase                 OTH
              EC 1.1.1.1                yeast alcohol dehydrogenase                   OTH
              EC 1.1.1.1                alcohol:NAD oxidoreductase                    SYS

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
COLUMN name FORMAT A45;
COLUMN name_class FORMAT A10;

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT   'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
         '[id='||e1.enzyme_id||']' as enzyme_id,
         n1.name,
         n1.name_class
  FROM   enzyme.enzymes e1, 
         enzyme.names n1
 WHERE   e1.enzyme_id = n1.enzyme_id
//AND    e1.enzyme_id BETWEEN 1630 AND 1633 //
ORDER BY enzyme_id, name_class;

SELECT user,name,sysdate FROM v$database;
EXIT;

