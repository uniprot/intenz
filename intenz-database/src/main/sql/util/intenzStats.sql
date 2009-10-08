-- Statistics for approved active IntEnz entries:
-- totalXrefs: number of cross-references to other databases
-- extEntries: number of different external entries which are
--      linked from IntEnz
-- The table in HTML format is dumped to the file passed as
-- parameter.

set termout off
set pages 999
set markup HTML on spool on entmap on -
	table 'cellpadding="5" cellspacing="2"' -
	head '<title>IntEnz statistics</title> <style type="text/css">th {background: #aaa} td {background: #eee}</style>'

spool &1

col c heading "Classes"
col sc heading "Subclasses"
col ssc heading "Subsubclasses"

select c , sc, ssc from
(select count(*) c from classes where classes.ACTIVE = 'Y'),
(select count(*) sc from subclasses where subclasses.ACTIVE = 'Y'),
(select count(*) ssc from subsubclasses where SUBSUBCLASSES.ACTIVE = 'Y');



col c heading "Enzyme entries (from ENZYMES)"
col status heading "Status"
col active format a6 heading "Active"

select count(*) c, cv_status.name status, active
from enzymes, cv_status
where enzymes.status = cv_status.code
group by rollup(cv_status.name, active)
order by status, active desc;



col c heading "Enzyme entries (from ID2EC)"
col status heading "Status"

select count(id2ec.ec) c, cv_status.name status
from id2ec, cv_status
where id2ec.status = cv_status.CODE
group by rollup(cv_status.name);



col c heading "ENZYME.dat totals"

select count(*) c
from (
    select * from enzymes where status='OK' and enzyme_id not in (
        select before_id from history_events where event_class='MOD'
    )
);



col synonyms heading "Synonyms"

select count(*) synonyms from names
where status = 'OK' and name_class = 'OTH';



col dbName heading "Db name"
col extEntries heading "External entries"
col totalXrefs heading "Total cross references"

select dbName, count(*) extEntries, sum(c) totalXrefs from
(
  select count(xrefs.enzyme_id) c, xrefs.database_ac, cv_databases.name dbName
  from xrefs, enzymes, cv_databases
  where enzymes.enzyme_id = xrefs.enzyme_id
    and enzymes.status = 'OK'
    and enzymes.active = 'Y'
    and cv_databases.code = xrefs.database_code
  group by xrefs.database_ac, cv_databases.name
  order by c desc
)
group by rollup(dbName)
order by totalXrefs desc;



col display_name heading "Db name"
col c heading "Total links (EC number-based)"

select display_name, count(*) c
from
(
-- There are entries with more than one link to the same db
-- (CAS, for example) and url's repeated for different enzymes
-- (usually, without the ec number in the query string)
  select distinct display_name, links.enzyme_id, links.url
  from links, enzymes
  where enzymes.enzyme_id = links.enzyme_id
    and enzymes.status = 'OK'
    and enzymes.active = 'Y'
)
group by rollup(display_name)
order by c desc;


spool off;

