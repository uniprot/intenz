CREATE TABLE releases (
  db varchar2(6),
  rel_no NUMBER(3),
  rel_date DATE
  );

comment on table releases is 'Release history for the databases in this schema';

grant select on releases to enzyme_webserver;

/* To auto-increase given a db name:
INSERT INTO releases(db, rel_no, rel_date)
VALUES (
  UPPER($db),
  (SELECT MAX(rel_no)+1 FROM releases WHERE db = UPPER($db)),
  sysdate);
*/
