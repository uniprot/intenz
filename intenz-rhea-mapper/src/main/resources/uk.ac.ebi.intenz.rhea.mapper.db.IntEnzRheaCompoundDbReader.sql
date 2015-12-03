-------------------------------------------------------------------------------------------
-- warning: *.structure.*, *.image.* and *.polymer.* queries rewritten to access
-- rhea from intenz after db split - see UES-14 -> UES-12
-- this is a fork of rhea-mapper uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader.sql
--
-- most queries below remain unchanged for UES-12, UES-14 - see uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader.sql
---------------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------------
--                                 rhea_compound, chebi_rhea_compound                                        --
---------------------------------------------------------------------------------------------------------------

--find.chebi.rhea.compounds.inreactions.by.names:\
SELECT DISTINCT r.compound_id,m.ascii_name,c.chebi_normalized as chebi_id, r.accession, m.compound_type, m.xml_name, c.status_compound, sm.has_uniprot_name, sm.uniprot_name_status \
	FROM chebi_name n, chebi_rhea_compound m, chebi_compound c, small_molecule sm, rhea_compound r, reaction_participants rp \
	WHERE r.compound_id = m.compound_id \
	AND r.compound_id = rp.compound_id \
	AND r.compound_id = sm.compound_id \
	AND n.chebi_id = c.chebi_id AND c.chebi_normalized = m.chebi_id AND (lower(n.ascii_name) LIKE {0}

--find.type.by.id:\
SELECT compound_type FROM rhea_compound WHERE compound_id = ?

--find.rhea.polymers.by.names:\
SELECT DISTINCT p.compound_id, p.ascii_name, p.chebi_id, p.accession, ''polymer'' \
	FROM polymer p WHERE lower(p.ascii_name) LIKE {0}

--find.rhea.polymers.inreactions.by.names:\
SELECT DISTINCT p.compound_id, p.ascii_name, p.chebi_id, p.accession, ''polymer'' \
	FROM polymer p \
	INNER JOIN reaction_participants rp ON p.compound_id=rp.compound_id \
	WHERE lower(p.ascii_name) LIKE {0}

--find.chebi.by.exact.name:\
SELECT chebi_id FROM chebi_rhea_compound WHERE ascii_name = ?
 
--find.compound.id.and.type.by.chebi.id:\
SELECT compound_id, compound_type \
	FROM chebi_rhea_compound WHERE chebi_id = ? ORDER BY compound_type DESC

--find.compound.id.by.accession:\
SELECT compound_id FROM rhea_compound WHERE accession = ?


---------------------------------------------------------------------------------------------------------------
--                                                small_molecule                                             --
---------------------------------------------------------------------------------------------------------------

--find.all:\
SELECT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule

--find.by.id:\
SELECT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule WHERE compound_id = ?

--find.small.molecules.by.ids:\
SELECT chebi_id, ascii_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
        FROM small_molecule WHERE chebi_id IN ({0})

--find.by.name:\
SELECT DISTINCT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule WHERE lower(f_xml_display_sp_code(xml_name)) LIKE ?

--find.by.exact.name:\
SELECT DISTINCT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule WHERE xml_name = ?

--find.by.accession:\
SELECT DISTINCT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule WHERE accession = ?

--find.no.formula:\
SELECT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule WHERE formula IS NULL AND accession IS NOT NULL

--find.no.accession:\
SELECT compound_id, chebi_id, ascii_name, xml_name as name, formula, charge, accession, status_compound, has_uniprot_name, uniprot_name_status \
	FROM small_molecule WHERE accession IS NULL

--find.pending.chebi.check:\
SELECT crc.compound_id, cc.chebi_id, cc.rhea_ascii_name as ascii_name, cc.rhea_xml_name as name, cc.formula, cc.charge, cc.accession, cc.status_compound, cc.has_uniprot_name, cc.uniprot_name_status \
    FROM chebi_rhea_compound crc \
    INNER JOIN chebi_compound cc ON cc.chebi_id=crc.chebi_id \
    WHERE cc.published='N'

---------------------------------------------------------------------------------------------------------------
--                                                chebi_compound                                             --
---------------------------------------------------------------------------------------------------------------

--find.chebi.compounds.by.names:\
SELECT DISTINCT chebi_id, rhea_ascii_name as name, rhea_xml_name as xml_name, formula, charge, accession, published, is_normalized as isnormalized, chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
FROM chebi_compound WHERE lower(rhea_ascii_name) LIKE {0}

--find.normalized.id.by.name.old:\
SELECT DISTINCT c.chebi_normalized as chebi_id,m.compound_id \
	FROM chebi_name n, chebi_compound c \
	left outer join chebi_rhea_compound m on (c.chebi_normalized = m.chebi_id) \
	where n.chebi_id = c.chebi_id  and c.chebi_normalized IS NOT NULL and m.compound_id IS NOT NULL \
	and lower(n.ascii_name) like {0}

--find.normalized.id.by.name:\
SELECT DISTINCT c.chebi_normalized as chebi_id,m.compound_id \
	FROM chebi_name n, chebi_compound c \
	left outer join chebi_rhea_compound m on (c.chebi_normalized = m.chebi_id) \
	where n.chebi_id = c.chebi_id and c.chebi_normalized IS NOT NULL \
	and lower(n.ascii_name) like {0}

--find.chebi.id.by.name:\
select distinct c.chebi_id,m.compound_id \
	from chebi_name n, chebi_compound c \
	left outer join chebi_rhea_compound m on (c.chebi_id = m.chebi_id) \
	where n.chebi_id = c.chebi_id and lower(n.ascii_name) like {0}

--find.chebi.by.accession:\
SELECT DISTINCT chebi_id, rhea_ascii_name as name, rhea_xml_name as xml_name, formula, charge, accession, published, is_normalized as isnormalized, chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
	FROM chebi_compound WHERE accession = ?

--find.chebi.by.inchi:\
SELECT DISTINCT chebi_id, rhea_ascii_name as name, rhea_xml_name as xml_name, formula, charge, accession, published, is_normalized as isnormalized, chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
	FROM chebi_compound WHERE to_char(computed_inchi) = ? ORDER by chebi_id ASC

--find.chebi.by.smiles:\
SELECT DISTINCT chebi_id, rhea_ascii_name as name, rhea_xml_name as xml_name, formula, charge, accession, published, is_normalized as isnormalized, chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
	FROM chebi_compound WHERE to_char(computed_smiles) = ? ORDER by chebi_id ASC

--find.normalized.chebi.id:\
SELECT c.chebi_normalized as chebi_id,m.compound_id \
	FROM chebi_alias a, chebi_rhea_compound m, chebi_compound c \
	WHERE a.chebi_id = c.chebi_id AND c.chebi_normalized = m.chebi_id AND  a.alias = ?

--find.normalized3.chebi.id:\
SELECT c.chebi_normalized as chebi_id, m.compound_id \
	FROM chebi_alias a, chebi_compound c \
		LEFT OUTER JOIN chebi_rhea_compound m ON (c.chebi_normalized = m.chebi_id) \
	WHERE a.chebi_id = c.chebi_id AND a.alias = ?

--delete.updates:\
SELECT c.chebi_id, c.rhea_ascii_name as name, c.rhea_xml_name as xml_name, \
	c.formula, c.charge, c.accession, c.published, c.status_compound, c.has_uniprot_name, c.uniprot_name_status, c.is_normalized as isnormalized \
	FROM chebi_compound c, chebi_compound_delete cd WHERE c.chebi_id=cd.chebi_id

--find.structure.by.chebi.accession:\
select * from table(rhea_data.find_rhea_structure_by_acc(?));

--find.structure.by.chebi.update.accession:\
select * from table(rhea_data.find_rhea_structure_by_update(?));

--find.chebi.compounds.by.ids:\
SELECT c.chebi_id, c.rhea_ascii_name as name, c.rhea_xml_name as xml_name, c.formula, c.charge, c.accession, c.published, c.is_normalized as isnormalized, c.chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
        FROM chebi_compound c \
        WHERE c.chebi_id IN ({0})

--find.image.by.chebi.id:\
select * from table(rhea_data.find_rhea_image_by_chebi_id(?));



---------------------------------------------------------------------------------------------------------------
--                                         polymers, generics                                             --
---------------------------------------------------------------------------------------------------------------


--find.all.generics:\
SELECT compound_id, accession, ascii_name, xml_name, global_formula, global_charge, fingerprint, is_root, root_accession, generic_type \
	FROM generic

--find.all.polymers:\
select * from table(rhea_data.find_all_rhea_polymers);

--find.polymer.by.fingerprint:\
select * from table(rhea_data.find_rhea_polymer_fingerprint(?));

--find.generic.by.fingerprint:\
SELECT compound_id, accession, ascii_name, xml_name, global_formula, global_charge, fingerprint, is_root, root_accession, generic_type \
	FROM generic WHERE fingerprint = ?

--find.polymer.by.name:\
select * from table(rhea_data.find_rhea_polymer_by_name(?))

--find.generic.by.name:\
SELECT compound_id, accession, ascii_name, xml_name, global_formula, global_charge, fingerprint, is_root, root_accession, generic_type \
	FROM generic WHERE xml_name = ?
 
--find.polymer.by.id:\
select * from table(rhea_data.find_rhea_polymer_by_id(?))

--find.polymers.by.ids:\
select * from table(rhea_data.find_rhea_polymer_by_ids(id_arr({0})));

--find.generic.by.id:\
SELECT compound_id, accession, is_root, root_accession, fingerprint, ascii_name, xml_name, global_formula, global_charge, generic_type \
        FROM generic WHERE compound_id = ?

--find.generic.by.accession:\
SELECT g.compound_id, g.accession, g.ascii_name, g.xml_name, g.global_formula, g.global_charge, g.fingerprint, g.is_root, g.root_accession, g.generic_type \
	FROM generic g WHERE g.accession = ?

--find.generic.onlyroot.by.accession:\
SELECT g.compound_id, g.accession, g.ascii_name, g.xml_name, g.global_formula, g.global_charge, g.fingerprint, g.is_root, g.root_accession, g.generic_type \
	FROM generic g \
	WHERE g.is_root = 'Y' AND accession = ?

--find.generic.onlynonroot.by.accession:\
SELECT g.compound_id, g.accession, g.ascii_name, g.xml_name, g.global_formula, g.global_charge, g.fingerprint, g.is_root, g.root_accession, g.generic_type \
	FROM generic g \
	WHERE g.is_root = 'N' AND accession = ?

--find.generic.inreactions.by.accession:\
SELECT g.compound_id, g.accession, g.ascii_name, g.xml_name, g.global_formula, g.global_charge, g.fingerprint, g.is_root, g.root_accession, g.generic_type \
	FROM generic g \
	INNER JOIN reaction_participants rp ON g.compound_id=rp.compound_id \
	WHERE g.accession = ?
	
--find.polymer.by.accession:\
select * from table(rhea_data.find_rhea_polymer_by_accession( ? ))

--find.polymer.inreactions.by.accession:\
select * from table(rhea_data.find_rhea_polymer_inreactions(?))

--find.chebi.polymer.ids.by.names:\
SELECT distinct c.chebi_id \
	FROM chebi_name n, chebi_compound c \
	WHERE n.chebi_id = c.chebi_id \
	AND c.is_normalized = ''Y'' \
	AND c.is_polymer = ''Y'' AND lower(n.ascii_name) LIKE {0}

--find.polymer.id.by.name:\
SELECT p.compound_id FROM polymer p WHERE lower(p.ascii_name) LIKE {0}

--find.polymer.id.inreactions.by.name:\
SELECT p.compound_id \
	FROM polymer p
	INNER JOIN reaction_participants rp ON p.compound_id=rp.compound_id \
	WHERE lower(p.ascii_name) LIKE {0}

--find.rhea.generics.by.names:\
SELECT DISTINCT g.compound_id, g.ascii_name, g.accession, g.generic_type \
	FROM generic g WHERE lower(g.ascii_name) LIKE {0}

--find.rhea.generics.inreactions.by.names:\
SELECT DISTINCT g.compound_id, g.ascii_name, g.accession, g.generic_type \
	FROM generic g \
	INNER JOIN reaction_participants rp ON g.compound_id=rp.compound_id \
	WHERE lower(g.ascii_name) LIKE {0}

--find.generics.by.name.NOTUSED:\
SELECT compound_id, accession, is_root, root_accession, fingerprint, ascii_name, xml_name as name, global_formula, global_charge \
	FROM generic WHERE lower(f_xml_display_sp_code(xml_name)) LIKE ?

--find.sru.by.compound.id:\
SELECT sru_id, charge, formula, polymer_index \
	FROM polymer_sru WHERE compound_id = ? 

--find.residues.by.chebi.id:\
SELECT compound_id, chebi_id, ascii_name, xml_name, charge, formula, position \
	FROM residue  WHERE chebi_id = ?

--find.residues.by.id:\
SELECT compound_id, chebi_id, ascii_name, xml_name, charge, formula, position, status_compound, has_uniprot_name, uniprot_name_status \
	FROM residue WHERE compound_id = ?

--find.related.polymers.by.chebi.id:\
select * from table(rhea_data.find_rhea_polymer_by_chebiid(?))

--find.related.generics.by.rootaccession:\
SELECT compound_id, accession, is_root, root_accession, fingerprint, ascii_name, xml_name as name, global_formula, global_charge \
	FROM generic WHERE root_accession = ?

--find.related.generics.by.residue.chebi.id:\
SELECT gen.compound_id, gen.accession, gen.ascii_name, gen.xml_name, gen.global_formula, gen.global_charge, gen.fingerprint, gen.is_root, gen.root_accession, gen.generic_type \
	FROM generic gen \
	INNER JOIN residue res ON gen.compound_id=res.compound_id \
	WHERE res.chebi_id = ?
	ORDER BY gen.accession ASC

--find.structure.by.polymerid:\
SELECT * FROM table(rhea_data.find_rhea_structure_by_polymer(?);

--find.generic.ids.by.names:\
select g.compound_id from generic g where lower(g.ascii_name) like {0}

--find.generic.ids.onlyroot.by.names:\
SELECT g.compound_id FROM generic g WHERE g.is_root = ''Y'' AND lower(g.ascii_name) like {0}

--find.generic.ids.onlynonroot.by.names:\
SELECT g.compound_id FROM generic g WHERE g.is_root = ''N'' AND lower(g.ascii_name) like {0}

--find.generic.ids.inreactions.by.names:\
SELECT g.compound_id \
	FROM generic g \
	INNER JOIN reaction_participants rp ON g.compound_id=rp.compound_id \
	WHERE lower(g.ascii_name) LIKE {0}

--find.participated.generics.by.accession:\
select compound_id from generic where is_root = 'N' and root_accession = ?

---------------------------------------------------------------------------------------------------------------
--                            chebi_compound_delete, chebi_compound_update                                   --
---------------------------------------------------------------------------------------------------------------

--delete.message:\
SELECT message FROM chebi_compound_delete WHERE chebi_id = ?

--updates:\
SELECT cdu.* \
	FROM compound_data_updates cdu, rhea_compound rc \
	WHERE cdu.compound_id = rc.compound_id \
	AND EXISTS (SELECT compound_id FROM compound_data_updates)

--full.updates:\
SELECT cu.chebi_id, cu.rhea_ascii_name as name, cu.rhea_xml_name as xml_name, cu.formula, cu.charge, cu.accession, cu.published, cu.status_compound, cu.has_uniprot_name, cu.uniprot_name_status, cu.is_normalized as isnormalized \
	FROM chebi_compound c,chebi_compound_update cu \
	WHERE c.chebi_id=cu.chebi_id \
	AND (c.formula!=cu.formula \
	OR c.charge!=cu.charge OR c.computed_charge!=cu.computed_charge OR c.computed_formula!=cu.computed_formula)

--update.by.chebi.id:\
SELECT chebi_id, rhea_ascii_name as name, rhea_xml_name as xml_name, formula, charge, accession, published, status_compound, has_uniprot_name, uniprot_name_status, is_normalized as isnormalized \
	FROM chebi_compound_update WHERE chebi_id = ?

--update.by.chebi.id.getfcupdated:\
SELECT chebi_id, fc_updated \
	FROM chebi_compound_update WHERE chebi_id = ?

--name.updates:\
SELECT cu.chebi_id, cu.rhea_ascii_name as name, \
	cu.rhea_xml_name as xml_name, \
	cu.formula, \
	cu.charge, \
	cu.accession, cu.published, cu.status_compound, cu.has_uniprot_name, cu.uniprot_name_status, cu.is_normalized as isnormalized \
	FROM chebi_compound c,chebi_compound_update cu \
	WHERE c.chebi_id=cu.chebi_id AND (c.rhea_ascii_name != cu.rhea_ascii_name \
	OR c.rhea_xml_name != cu.rhea_xml_name) \
	AND ((cu.formula is null OR c.formula = cu.formula) \
	AND (cu.charge is null OR c.charge = cu.charge)) \
	AND ((cu.computed_formula is null OR c.computed_formula = cu.computed_formula) \
	AND (cu.computed_charge is null OR c.computed_charge = cu.computed_charge)) \
	
    
--has.chebi.pending.update:\
SELECT chebi_id FROM chebi_compound_update WHERE chebi_id = ?
    
--generic.has.chebi.pending.update:\
SELECT cu.chebi_id FROM chebi_compound_update cu \
INNER JOIN residue r ON r.chebi_id=cu.chebi_id \
INNER JOIN generic g ON g.compound_id=r.compound_id \
WHERE g.compound_id = ?


---------------------------------------------------------------------------------------------------------------
--                                                   cofactors                                               --
---------------------------------------------------------------------------------------------------------------
--find.where.cofactor:\
SELECT DISTINCT enzyme_id FROM cofactors WHERE compound_id = ?

--find.generics.by.ids:\
SELECT compound_id, ascii_name, xml_name, fingerprint, accession, global_formula, global_charge, is_root, root_accession, generic_type \
	FROM generic WHERE compound_id IN ({0})

---------------------------------------------------------------------------------------------------------------
--                                 access to chebi_normalized_exception table - added for MET-57
---------------------------------------------------------------------------------------------------------------

--chebi.by.normalized.exceptions:\
SELECT cc.chebi_id, cc.rhea_ascii_name as name, cc.rhea_xml_name as xml_name, cc.formula, cc.charge, cc.accession, cc.published, cc.is_normalized as isnormalized, cc.chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
	FROM chebi_compound cc \
	INNER JOIN chebi_normalized_exception cn ON cn.chebi_id=cc.chebi_id \
	ORDER BY chebi_id

--chebi.by.normalized.exceptions.find.by.id \
SELECT cc.chebi_id, cc.rhea_ascii_name as name, cc.rhea_xml_name as xml_name, cc.formula, cc.charge, cc.accession, cc.published, cc.is_normalized as isnormalized, cc.chebi_normalized as chebinormalized, status_compound, has_uniprot_name, uniprot_name_status \
	FROM chebi_compound cc \
	INNER JOIN chebi_normalized_exception cn ON cn.chebi_id=cc.chebi_id \
	WHERE cc.chebi_id= ?


--reaction.core:\
SELECT equation, status, source, iubmb, qualifiers, data_comment, \
    reaction_comment, public_in_chebi, direction, un_reaction \
	FROM intenz_reactions WHERE reaction_id = ?

--citations:\
SELECT pub_id, source FROM reaction_citations \
	WHERE reaction_id = (SELECT CASE WHEN UN_REACTION IS NULL THEN REACTION_ID ELSE UN_REACTION END FROM INTENZ_REACTIONS WHERE REACTION_ID = ?)

--xrefs:\
SELECT db_code, db_accession FROM reaction_xrefs \
	WHERE reaction_id = ? ORDER BY db_code, db_accession


--enzymes.related:\
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status \
	FROM enzymes e, reactions_map rm \
	WHERE rm.reaction_id = ? \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
UNION \
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status \
	FROM enzymes e, reactions_map rm, reaction_mergings rme \
	WHERE (rme.to_id = ? and rm.reaction_id = rme.from_id) \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
	ORDER BY ec

--family.enzymes.related:\
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status, ir.direction \
	FROM enzymes e, reactions_map rm, intenz_reactions ir \
	WHERE rm.reaction_id IN \
        (SELECT reaction_id FROM intenz_reactions \
        WHERE reaction_id = ? OR un_reaction = ?) \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
    AND rm.reaction_id = ir.reaction_id \
UNION \
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status, ir.direction \
	FROM enzymes e, reactions_map rm, reaction_mergings rme, intenz_reactions ir \
	WHERE (rme.to_id IN \
        (SELECT reaction_id FROM intenz_reactions \
        WHERE reaction_id = ? OR un_reaction = ?) \
    AND rm.reaction_id = rme.from_id) \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
    AND rm.reaction_id = ir.reaction_id \
	ORDER BY ec


------------------------------
-- importing everything from rhea module
------------------------------

--constraint.compound.name:\
AND cd.name LIKE ?

--constraint.compound.name.ignore.case:\
AND LOWER(cd.ascii_name) LIKE LOWER(?)

--constraint.directional:\
AND ir.direction IN ('LR', 'RL')

--constraint.direction:\
AND ir.direction = ?

--constraint.status:\
AND ir.status = ?

--constraint.merged:\
AND EXISTS \
	(SELECT mg.from_id FROM reaction_mergings mg \
	WHERE mg.from_id = ir.reaction_id)

--constraint.not.merged:\
AND NOT EXISTS \
	(SELECT mg.from_id FROM reaction_mergings mg \
	WHERE mg.from_id = ir.reaction_id)

--constraint.complex.stepwise:\
AND cr.order_in > 0

--constraint.complex.coupled:\
AND cr.order_in = 0
	
	
--reaction.directional.id:\
SELECT reaction_id \
	FROM intenz_reactions \
	WHERE un_reaction = ? AND direction = ?

--reaction.participants:\
SELECT rp.side, rp.coefficient, rp.coeff_type, rp.compound_id, rp.location \
	FROM intenz_reactions ir, reaction_participants rp \
	WHERE ir.reaction_id = ? \
	AND (ir.reaction_id = rp.reaction_id OR ir.un_reaction = rp.reaction_id) \
	ORDER BY rp.side ASC

--enzymes.related:\
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status \
	FROM enzymes e, reactions_map rm \
	WHERE rm.reaction_id = ? \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
UNION \
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status \
	FROM enzymes e, reactions_map rm, reaction_mergings rme \
	WHERE (rme.to_id = ? and rm.reaction_id = rme.from_id) \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
	ORDER BY ec

--family.enzymes.related:\
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status, ir.direction \
	FROM enzymes e, reactions_map rm, intenz_reactions ir \
	WHERE rm.reaction_id IN \
        (SELECT reaction_id FROM intenz_reactions \
        WHERE reaction_id = ? OR un_reaction = ?) \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
    AND rm.reaction_id = ir.reaction_id \
UNION \
SELECT f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec, e.enzyme_id, e.status, ir.direction \
	FROM enzymes e, reactions_map rm, reaction_mergings rme, intenz_reactions ir \
	WHERE (rme.to_id IN \
        (SELECT reaction_id FROM intenz_reactions \
        WHERE reaction_id = ? OR un_reaction = ?) \
    AND rm.reaction_id = rme.from_id) \
	AND rm.enzyme_id = e.enzyme_id \
	AND e.status IN ('OK','PM') AND e.active = 'Y' \
	AND e.enzyme_id NOT IN \
		(SELECT before_id FROM history_events WHERE event_class = 'MOD') \
    AND rm.reaction_id = ir.reaction_id \
	ORDER BY ec


--xrefs:\
SELECT db_code, db_accession FROM reaction_xrefs \
	WHERE reaction_id = ? ORDER BY db_code, db_accession

--xrefs.uniprot:\
select distinct x.database_ac, x.name \
	from reactions_map rm, xrefs x, enzymes e \
	where rm.reaction_id = ?  \
	and rm.enzyme_id = e.enzyme_id and e.status in ('OK','PM') \
	and e.active = 'Y' \
	and rm.enzyme_id = x.enzyme_id \
	and x.database_code = 'S' \
	order by x.name

--family.xrefs:\
SELECT ir.direction, rx.db_code, rx.db_accession \
    FROM intenz_reactions ir, reaction_xrefs rx \
    WHERE (ir.reaction_id = ? OR ir.un_reaction = ?) /* A FAMILY ID! */ \
    AND ir.reaction_id = rx.reaction_id \
    ORDER BY ir.reaction_id

--family.xrefs.uniprot:\
select distinct ir.direction, x.database_ac, x.name \
	from intenz_reactions ir, reactions_map rm, xrefs x, enzymes e \
	where (ir.reaction_id = ? or ir.un_reaction = ?) \
	and ir.reaction_id = rm.reaction_id \
	and rm.enzyme_id = x.enzyme_id \
	and x.database_code = 'S' \
	and rm.enzyme_id = e.enzyme_id and e.status in ('OK','PM') \
	and e.active = 'Y' \
	order by x.name

--simple.by.compound.name:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, chebi_rhea_compound cd, reaction_participants rp \
	WHERE (ir.un_reaction = rp.reaction_id or ir.reaction_id = rp.reaction_id) \
	AND rp.compound_id = cd.compound_id \
	{0} {1} {2} {3} \
	ORDER BY reaction_id ASC
	
--complex.by.compound.name:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, complex_reactions cr \
	WHERE ir.reaction_id = cr.parent_id \
	AND cr.child_id IN (\
		SELECT DISTINCT intenz_reactions.reaction_id \
		FROM intenz_reactions, reaction_participants, chebi_rhea_compound cd \
		WHERE intenz_reactions.un_reaction = reaction_participants.reaction_id \
		AND reaction_participants.compound_id = cd.compound_id \
		{0}) \
	{1} {2} {3} {4} \
	ORDER BY reaction_id ASC

--reaction.identical.by.fp:\
SELECT reaction_id FROM intenz_reactions WHERE fingerprint = ?

--reaction.by.fp:\
SELECT reaction_id FROM intenz_reactions \
	WHERE fingerprint = ? OR fingerprint LIKE ?

--simple.by.compound.accession.old:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, rhea_compound r, chebi_rhea_compound cd, reaction_participants rp \
	WHERE (ir.un_reaction = rp.reaction_id or ir.reaction_id = rp.reaction_id) \
	AND rp.compound_id = cd.compound_id \
	AND r.compound_id = rp.compound_id \
	AND r.accession = ? {0} {1} {2} \
	ORDER BY reaction_id ASC
	
--simple.by.compound.accession:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, rhea_compound r, reaction_participants rp \
	WHERE (ir.un_reaction = rp.reaction_id or ir.reaction_id = rp.reaction_id) \
	AND r.compound_id = rp.compound_id \
	AND r.accession = ? {0} {1} {2} \
	ORDER BY reaction_id ASC

	
--complex.by.compound.accession:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, complex_reactions cr \
	WHERE ir.reaction_id = cr.parent_id \
	AND cr.child_id in (\
		SELECT DISTINCT intenz_reactions.reaction_id \
		FROM intenz_reactions, reaction_participants, rhea_compound \
		WHERE intenz_reactions.un_reaction = reaction_participants.reaction_id \
		AND reaction_participants.compound_id = rhea_compound.compound_id \
		AND rhea_compound.accession = ?) \
	{0} {1} {2} {3} \
	ORDER BY reaction_id ASC

--reaction.by.ec:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, reactions_map rm, enzymes e \
	WHERE e.ec1||''.''||e.ec2||''.''||e.ec3||''.''||(case \
	  WHEN e.status = ''PM'' THEN ''n'' ELSE '''' END)||e.ec4 LIKE ? \
	AND e.enzyme_id = rm.enzyme_id \
	AND rm.reaction_id = ir.reaction_id \
	AND e.active = ''Y'' \
	{0} {1} {2} \
	ORDER BY reaction_id ASC

--reaction.by.compound.id:\
SELECT ir.equation, ir.source, ir.iubmb, ir.reaction_id, ir.qualifiers, ir.status \
	FROM intenz_reactions ir, reaction_participants rp \
	WHERE rp.reaction_id = ir.reaction_id \
	AND rp.compound_id = ?
	
--parents:\
SELECT parent_id FROM complex_reactions WHERE child_id = ?
	
--children:\
SELECT child_id, order_in, coefficient FROM complex_reactions \
	WHERE parent_id = ? ORDER BY order_in

--decompositions:\
SELECT reaction_id FROM intenz_reactions WHERE fingerprint LIKE ?||'|%'

--related:\
SELECT DISTINCT direction, reaction_id FROM intenz_reactions \
	WHERE reaction_id != ? \
	AND (\
		un_reaction = ? \
		OR reaction_id = (SELECT un_reaction FROM intenz_reactions \
			WHERE reaction_id = ? AND un_reaction IS NOT NULL) \
		OR un_reaction = (SELECT un_reaction FROM intenz_reactions \
			WHERE reaction_id = ? AND un_reaction IS NOT NULL)\
	) ORDER BY reaction_id

--related.all:\
SELECT DISTINCT direction, reaction_id FROM intenz_reactions \
	WHERE reaction_id = ? \
	OR un_reaction = ? \
	OR reaction_id = (SELECT un_reaction FROM intenz_reactions \
		WHERE reaction_id = ? AND un_reaction IS NOT NULL) \
	OR un_reaction = (SELECT un_reaction FROM intenz_reactions \
		WHERE reaction_id = ? AND un_reaction IS NOT NULL) \
	ORDER BY reaction_id
	
--compound.participated.reactions:\
SELECT DISTINCT reaction_id FROM reaction_participants rp \
	WHERE rp.compound_id = ? \
UNION \
SELECT parent_id FROM complex_reactions WHERE child_id IN (\
	SELECT DISTINCT reaction_id FROM intenz_reactions WHERE un_reaction IN (\
		SELECT DISTINCT reaction_id  FROM reaction_participants rp \
			WHERE rp.compound_id = ? \
	)\
)
	
--compound.participated.reactions.number:\
SELECT COUNT(DISTINCT reaction_id) FROM reaction_participants \
	WHERE compound_id = ?

--participated.reactions.by.chebi.id:\
SELECT distinct r.reaction_id \
	FROM chebi_rhea_compound c, reaction_participants r \
	WHERE c.compound_id = r.compound_id AND c.chebi_id = ?
	
--public.all:\
SELECT reaction_id FROM intenz_reactions WHERE status IN ('OK','PM','OB') \
	AND public_in_chebi = 'P'

--mergings:\
SELECT from_id, to_id, merging_when, merging_who, merging_comment \
	FROM reaction_mergings \
	WHERE from_id = ?

--find.reactions.affected.by.chebi.formula.or.charge.update:\
SELECT DISTINCT l.reaction_id, crc1.chebi_id, l.status, count_left, (CASE WHEN count_right IS NULL THEN 0 ELSE count_right END) AS count_right \
FROM reaction_participants rp, chebi_rhea_compound crc1, v_ccu_left l \
LEFT OUTER JOIN v_ccu_right r ON (l.reaction_id = r.reaction_id) \
WHERE rp.reaction_id = l.reaction_id \
AND rp.compound_id = crc1.compound_id \
AND crc1.chebi_id = ? \
UNION \
SELECT DISTINCT r.reaction_id, crc2.chebi_id, r.status, (CASE WHEN count_left IS NULL THEN 0 ELSE count_left END) AS count_left, count_right \
FROM reaction_participants rp, chebi_rhea_compound crc2, v_ccu_right r \
LEFT OUTER JOIN v_ccu_left l ON (l.reaction_id = r.reaction_id) \
WHERE rp.reaction_id = r.reaction_id \
AND rp.compound_id = crc2.compound_id \
AND crc2.chebi_id = ? \
ORDER BY count_left,count_right