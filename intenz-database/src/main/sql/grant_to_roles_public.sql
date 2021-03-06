GRANT SELECT                      ON cv_databases          TO ENZYME_SELECT;
GRANT SELECT                      ON cv_name_classes       TO ENZYME_SELECT;
GRANT SELECT                      ON cv_status             TO ENZYME_SELECT;
GRANT SELECT                      ON cv_warnings           TO ENZYME_SELECT;
GRANT SELECT                      ON intenz_text           TO ENZYME_SELECT;
GRANT SELECT                      ON id2ec                 TO ENZYME_SELECT;
GRANT SELECT                      ON classes               TO ENZYME_SELECT;
GRANT SELECT                      ON subclasses            TO ENZYME_SELECT;
GRANT SELECT                      ON subsubclasses         TO ENZYME_SELECT;
GRANT SELECT                      ON enzymes               TO ENZYME_SELECT;
GRANT SELECT                      ON names                 TO ENZYME_SELECT;
GRANT SELECT                      ON reactions             TO ENZYME_SELECT;
GRANT SELECT                      ON comments              TO ENZYME_SELECT;
GRANT SELECT                      ON xrefs                 TO ENZYME_SELECT;
GRANT SELECT                      ON cofactors             TO ENZYME_SELECT;
GRANT SELECT                      ON links                 TO ENZYME_SELECT;
GRANT SELECT                      ON publications          TO ENZYME_SELECT;
GRANT SELECT                      ON citations             TO ENZYME_SELECT;
GRANT SELECT                      ON timeouts              TO ENZYME_SELECT;
GRANT SELECT                      ON history_events        TO ENZYME_SELECT;
GRANT SELECT                      ON future_events         TO ENZYME_SELECT;
GRANT SELECT                      ON v_ec_for_datalib      TO ENZYME_SELECT;
GRANT SELECT                      ON v_interpro            TO ENZYME_SELECT;
GRANT EXECUTE                     ON f_quad2string         TO ENZYME_SELECT;
GRANT EXECUTE                     ON p_string2quad         TO ENZYME_SELECT;
GRANT EXECUTE                     ON f_rhea_family_id      TO ENZYME_WEBSERVER;

GRANT SELECT ON reactions_map TO ENZYME_SELECT;
GRANT SELECT ON intenz_reactions TO ENZYME_SELECT;
GRANT SELECT ON intenz_reactions_audit TO ENZYME_SELECT;
GRANT SELECT ON compound_data TO ENZYME_SELECT;
GRANT SELECT ON compound_data_updates TO ENZYME_SELECT;
GRANT SELECT ON reaction_participants TO ENZYME_SELECT;
GRANT SELECT ON complex_reactions TO ENZYME_SELECT;
GRANT SELECT ON complex_reactions_audit TO ENZYME_SELECT;
GRANT SELECT ON reaction_citations TO ENZYME_SELECT;
GRANT SELECT ON reaction_citations_audit TO ENZYME_SELECT;
GRANT SELECT ON reaction_xrefs TO ENZYME_SELECT;
GRANT SELECT ON reaction_xrefs_audit TO ENZYME_SELECT;
GRANT SELECT ON reaction_mergings TO ENZYME_SELECT;
GRANT SELECT ON cv_coeff_types TO ENZYME_SELECT;
GRANT SELECT ON cv_comp_pub_avail TO ENZYME_SELECT;
GRANT SELECT ON cv_location TO ENZYME_SELECT;
GRANT SELECT ON cv_operators TO ENZYME_SELECT;
GRANT SELECT ON cv_reaction_directions TO ENZYME_SELECT;
GRANT SELECT ON cv_reaction_qualifiers TO ENZYME_SELECT;
GRANT SELECT ON cv_reaction_sides TO ENZYME_SELECT;
GRANT SELECT ON cv_view TO ENZYME_SELECT;

GRANT EXECUTE ON reaction_qualifiers TO ENZYME_SELECT;
GRANT SELECT ON releases TO ENZYME_SELECT;

-- Rhea tables added after the upgrade to handle generics and polymers:
grant select on chebi_alias to ENZYME_SELECT;
grant select on chebi_compound to ENZYME_SELECT;
grant select on chebi_compound_update to ENZYME_SELECT;
grant select on chebi_name to ENZYME_SELECT;
grant select on chebi_normalized_exception to ENZYME_SELECT;
grant select on chebi_notchemical to ENZYME_SELECT;
grant select on chebi_rhea_compound to ENZYME_SELECT;
grant select on generic to ENZYME_SELECT;
grant select on polymer to ENZYME_SELECT;
grant select on polymer_sru to ENZYME_SELECT;
grant select on residue to ENZYME_SELECT;
grant select on rhea_compound to ENZYME_SELECT;
grant select on small_molecule to ENZYME_SELECT;

-- Concrete users for the ENZYME_SELECT role:

GRANT ENZYME_SELECT TO enzyme_webserver;
GRANT ENZYME_SELECT TO pdbegroup;

