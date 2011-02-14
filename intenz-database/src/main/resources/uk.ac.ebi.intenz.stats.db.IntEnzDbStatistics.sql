--constraint.non.transient:\
and e.enzyme_id not in (select before_id from history_events where event_class = 'MOD')

--classes:\
select count(*) c from classes where active = 'Y'

--subclasses:\
select count(*) c from subclasses where active = 'Y'

--subsubclasses:\
select count(*) c from subsubclasses where active = 'Y'

--enzymes.by.status:\
select count(*) c, cv_status.name status, active \
	from enzymes e, cv_status \
	where e.status = cv_status.code {0} \
	group by cv_status.name, active \
	order by status, active desc

--synonyms:\
select count(distinct n.name) c from names n, enzymes e \ 
	where n.status = 'OK' and n.name_class = 'OTH' \
	and n.enzyme_id  = e.enzyme_id {0}

--xrefs:\
select cv_databases.name dbname, \
	count(distinct xrefs.database_ac) cd, \
	count(xrefs.database_ac) c \
	from xrefs, enzymes e, cv_databases \
	where e.enzyme_id = xrefs.enzyme_id \
	and e.status in ('OK','PM') and e.active = 'Y' \
	and cv_databases.code = xrefs.database_code \
	group by cv_databases.name order by dbname asc

--links:\
select display_name dbname, count(distinct links.url) cd, count(links.url) c \
	from links, enzymes \
	where enzymes.enzyme_id = links.enzyme_id \
	and enzymes.status in ('OK', 'PM') \
	and enzymes.active = 'Y' \
	group by display_name order by dbname asc

--release:\
select rel_no, rel_date from releases where db = 'INTENZ' \
	and rel_no = (select max(rel_no) from releases where db = 'INTENZ')

