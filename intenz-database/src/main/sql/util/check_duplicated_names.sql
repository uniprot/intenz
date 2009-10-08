SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find names that appear more than
              once per enzyme.

  Usage:      SQL*Plus> @check_duplicated_names
  
  Example output:

              EC              ENZYME_ID COMMENT_TEXT
              --------------- --------- ---------------------------------------------
              EC 1.1.1.1      [id=1000] A zinc protein. Acts on primary or secondary
                                        alcohols or hemi-acetals; the animal, but not
                                         the yeast, enzyme acts also on cyclic second
                                        ary alcohols.

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

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT 'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
       '[id='||e1.enzyme_id||']' as enzyme_id,
       n1.name_class, n1.name
  FROM enzyme.names n1, 
       enzyme.enzymes e1
 WHERE EXISTS 
       (
        SELECT n2.enzyme_id, n2.name
          FROM enzyme.names n2
         WHERE n1.enzyme_id = n2.enzyme_id
           AND n1.name = n2.name
         GROUP BY n2.enzyme_id, n2.name
        HAVING count(*) > 1
       )
   AND n1.enzyme_id = e1.enzyme_id
ORDER BY 1, n1.name, n1.name_class;

SELECT user,name,sysdate FROM v$database;
EXIT SUCCESS;

