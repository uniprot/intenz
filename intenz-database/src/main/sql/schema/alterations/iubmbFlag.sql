-- Create the flag in the intenz tables:
alter table REACTIONS_MAP add (IUBMB varchar2(1) default 'N' not null);
comment on column reactions_map.iubmb is
	'Flag for reactions assigned to enzymes by IUBMB';

-- Set the flag for one-to-one relationships:
update reactions_map
set iubmb = 'Y' where enzyme_id in (
  select enzyme_id from (
    select count(reaction_id) c, enzyme_id
    from reactions_map
    group by enzyme_id
  ) where c = 1
);
-- Where there is more than one reaction assigned to the same enzyme,
-- the flag will be set manually in the curator tool (or via SQL script).

-- Deprecate iubmb column in Rhea:
comment on column intenz_reactions.iubmb is
	'DEPRECATED COLUMN. Please use reactions_map.iubmb instead.';
