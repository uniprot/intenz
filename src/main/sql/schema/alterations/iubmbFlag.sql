-- Create the flag in the intenz tables:
alter table REACTIONS_MAP add (IUBMB varchar2(1) default 'N' not null);
comment on column reactions_map.iubmb is
	'Flag for reactions assigned to enzymes by IUBMB';

alter table REACTIONS add (IUBMB varchar2(1) default 'N' not null);
comment on column reactions.iubmb is
	'Flag for reactions assigned to enzymes by IUBMB';

-- Copy existing values from the rhea table:
update (select ir.iubmb rheaFlag, rm.iubmb intenzFlag
	from INTENZ_REACTIONS ir, REACTIONS_MAP rm
	where ir.reaction_id = rm.reaction_id)
set intenzFlag = rheaFlag;

update reactions set iubmb = 'Y'
	where web_view = 'INTENZ' or web_view like 'IUBMB%';

-- Deprecate iubmb column in Rhea:
comment on column intenz_reactions.iubmb is
	'DEPRECATED COLUMN. Please use reactions.iubmb and reactions_map.iubmb.';
