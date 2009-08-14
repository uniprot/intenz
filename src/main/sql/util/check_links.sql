SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Sanity check - find links that have errors
              and correct errors.

  Usage:      SQL*Plus> @check_links
              or, from unix:
              sqlplus /@iprod @check_links > report.lst
  
  Example output:

              EC              ENZYME_ID URL                            DISPLAY_NAME STATUS
              --------------- --------- ------------------------------ ------------ ------
              EC 1.1.1.1      [id=1000] 9031-72-5                      CAS          NO
              EC 1.1.1.1                http://brenda.bc.uni-koeln.de/ BRENDA       NO
                                        php/result_flat.php3?ecno=1.1.
                                        1.1

              EC 1.1.1.1                http://umbbd.ahc.umn.edu/servl UM-BBD       NO
                                        ets/pageservlet?ptype=e&ECcode
                                        =1.1.1.1

              EC 1.1.1.1                http://wit.mcs.anl.gov/WIT/CGI WIT          NO
                                        /fr.cgi?org=&user=&fr=1.1.1.1

              EC 1.1.1.1                http://www.expasy.ch/cgi-bin/n EXPASY       NO
                                        icezyme.pl?1.1.1.1

              EC 1.1.1.1                http://www.genome.ad.jp/dbget- KEGG         NO
                                        bin/www_bget?ec:1.1.1.1

              EC 1.1.1.1                http://wwwbmcd.nist.gov:8080/e GTD          NO
                                        nzyme/ename.html

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
COLUMN url FORMAT A30;
COLUMN display_name FORMAT A12;
COLUMN status FORMAT A6; 

BREAK ON ENZYME_ID NODUP SKIP 1

SELECT   'EC '||enzyme.f_quad2string(e1.ec1,e1.ec2,e1.ec3,e1.ec4) as EC,
         '[id='||e1.enzyme_id||']' as enzyme_id,
         l1.url,
         l1.display_name,
         l1.status
  FROM   enzyme.enzymes e1, 
         enzyme.links l1
 WHERE   e1.enzyme_id = l1.enzyme_id
//AND    e1.enzyme_id BETWEEN 4792 AND 4793 //
ORDER BY enzyme_id, url;

SELECT user,name,sysdate FROM v$database;
EXIT;




???????????????????????????????????
SQL> select enzyme_id from links
  2  where status = 'OK';

 ENZYME_ID
----------
      4793
      4421
      4425
      4434
      4437
      4438

6 rows selected.
??????????????????????????????????