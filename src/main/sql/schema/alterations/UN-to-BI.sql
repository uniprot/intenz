declare
  bi_id intenz_reactions.reaction_id%type;
BEGIN

for un in (select distinct rm.reaction_id
  from reactions_map rm, intenz_reactions ir
  where rm.reaction_id = ir.reaction_id
  and ir.direction = 'UN')
loop
  select ir.reaction_id
    into bi_id
    from intenz_reactions ir
    where ir.un_reaction = un.reaction_id
    and ir.direction = 'BI';
  update reactions_map
    set reaction_id = bi_id
    where reaction_id = un.reaction_id;
end loop;
end;
/

commit;