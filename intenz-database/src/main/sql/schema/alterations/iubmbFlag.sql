-- Create the flag in the intenz tables:
alter table REACTIONS_MAP add (IUBMB varchar2(1) default 'N' not null);
comment on column reactions_map.iubmb is
	'Flag for reactions assigned to enzymes by IUBMB';
alter table REACTIONS_MAP_AUDIT add (IUBMB varchar2(1));

create or replace
TRIGGER td_reactions_map
AFTER DELETE ON reactions_map FOR EACH ROW
BEGIN
    INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,iubmb,
    timestamp,audit_id,dbuser,osuser,remark,action)
    VALUES (
    :old.reaction_id,:old.enzyme_id,:old.web_view,:old.order_in,:old.iubmb,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D'
    );
END;
/

create or replace
TRIGGER ti_reactions_map
AFTER INSERT ON reactions_map FOR EACH ROW
BEGIN
    INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,iubmb,
    timestamp,audit_id,dbuser,osuser,remark,action)
    VALUES (
    :new.reaction_id,:new.enzyme_id,:new.web_view,:new.order_in,:new.iubmb,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I'
    );
END;
/

create or replace
TRIGGER tu_reactions_map
AFTER UPDATE ON reactions_map FOR EACH ROW
BEGIN
    INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,iubmb,
    timestamp,audit_id,dbuser,osuser,remark,action)
    VALUES (
    :new.reaction_id,:new.enzyme_id,:new.web_view,:new.order_in,:new.iubmb,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U'
    );
END;
/

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
