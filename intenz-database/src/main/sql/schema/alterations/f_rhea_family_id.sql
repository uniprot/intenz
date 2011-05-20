create or replace FUNCTION "F_RHEA_FAMILY_ID" 
( r_id IN intenz_reactions.reaction_id%type
) RETURN NUMBER AS
  family_id intenz_reactions.reaction_id%type;
BEGIN
    select
        case
            when un_reaction is null then reaction_id
            else un_reaction
        end
        into family_id
        from intenz_reactions
        where reaction_id = r_id;
    RETURN family_id;
END F_RHEA_FAMILY_ID;
