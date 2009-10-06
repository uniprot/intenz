SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find comments that have errors
              and correct errors.

  Usage:      SQL*Plus> @check_comments
              or, from unix:
              sqlplus /@iprod @check_comments > report.lst
  
  Example output:

              EC              ENZYME_ID COMMENT_TEXT
              --------------- --------- ---------------------------------------------
              EC 1.1.1.1      [id=1000] A zinc protein. Acts on primary or secondary
                                        alcohols or hemi-acetals; the animal, but not
                                         the yeast, enzyme acts also on cyclic second
                                        ary alcohols.

  Required:   role enzyme_select
    
  $Date: 2008/01/17 12:19:14 $
  $Revision: 1.3 $

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
COLUMN comment_text FORMAT A45;

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT   'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
         '[id='||e1.enzyme_id||']' as enzyme_id,
         c1.comment_text
  FROM   enzyme.enzymes e1, 
         enzyme.comments c1
 WHERE   e1.enzyme_id = c1.enzyme_id
//AND    e1.enzyme_id BETWEEN 1630 AND 1633 //
ORDER BY enzyme_id;

SELECT user,name,sysdate FROM v$database;
EXIT;




