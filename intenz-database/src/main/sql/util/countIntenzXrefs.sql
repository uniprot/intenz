-- IntEnz xrefs by database (total and unique):
select database_code, count(database_ac) c, count(distinct database_ac) d
from enzyme.xrefs
group by database_code;