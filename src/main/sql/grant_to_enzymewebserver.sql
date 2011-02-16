GRANT SELECT                      ON cv_databases          TO ENZYME_WEBSERVER;
GRANT SELECT                      ON cv_name_classes       TO ENZYME_WEBSERVER;
GRANT SELECT                      ON cv_status             TO ENZYME_WEBSERVER;
GRANT SELECT                      ON cv_warnings           TO ENZYME_WEBSERVER;
GRANT SELECT                      ON intenz_text           TO ENZYME_WEBSERVER;
GRANT SELECT                      ON id2ec                 TO ENZYME_WEBSERVER;
GRANT SELECT                      ON classes               TO ENZYME_WEBSERVER;
GRANT SELECT                      ON subclasses            TO ENZYME_WEBSERVER;
GRANT SELECT                      ON subsubclasses         TO ENZYME_WEBSERVER;
GRANT SELECT                      ON enzymes               TO ENZYME_WEBSERVER;
GRANT SELECT                      ON names                 TO ENZYME_WEBSERVER;
GRANT SELECT                      ON reactions             TO ENZYME_WEBSERVER;
GRANT SELECT                      ON comments              TO ENZYME_WEBSERVER;
GRANT SELECT                      ON xrefs                 TO ENZYME_WEBSERVER;
GRANT SELECT                      ON cofactors             TO ENZYME_WEBSERVER;
GRANT SELECT                      ON links                 TO ENZYME_WEBSERVER;
GRANT SELECT                      ON publications          TO ENZYME_WEBSERVER;
GRANT SELECT                      ON citations             TO ENZYME_WEBSERVER;
GRANT SELECT                      ON timeouts              TO ENZYME_WEBSERVER;
GRANT SELECT                      ON history_events        TO ENZYME_WEBSERVER;
GRANT SELECT                      ON future_events         TO ENZYME_WEBSERVER;
GRANT SELECT                      ON v_ec_for_datalib      TO ENZYME_WEBSERVER;
GRANT SELECT                      ON v_interpro            TO ENZYME_WEBSERVER;

GRANT EXECUTE                     ON f_quad2string         TO ENZYME_WEBSERVER;
GRANT EXECUTE                     ON p_string2quad         TO ENZYME_WEBSERVER;
GRANT EXECUTE                     ON f_rhea_family_id      TO ENZYME_WEBSERVER;

GRANT SELECT ON reactions_map TO ENZYME_WEBSERVER;
GRANT SELECT ON intenz_reactions TO ENZYME_WEBSERVER;
GRANT SELECT ON intenz_reactions_audit TO ENZYME_WEBSERVER;
GRANT SELECT ON compound_data TO ENZYME_WEBSERVER;
GRANT SELECT ON compound_data_updates TO ENZYME_WEBSERVER;
GRANT SELECT ON reaction_participants TO ENZYME_WEBSERVER;
GRANT SELECT ON complex_reactions TO ENZYME_WEBSERVER;
GRANT SELECT ON complex_reactions_audit TO ENZYME_WEBSERVER;
GRANT SELECT ON reaction_citations TO ENZYME_WEBSERVER;
GRANT SELECT ON reaction_citations_audit TO ENZYME_WEBSERVER;
GRANT SELECT ON reaction_xrefs TO ENZYME_WEBSERVER;
GRANT SELECT ON reaction_xrefs_audit TO ENZYME_WEBSERVER;
GRANT SELECT ON reaction_mergings TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_coeff_types TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_comp_pub_avail TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_location TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_operators TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_reaction_directions TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_reaction_qualifiers TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_reaction_sides TO ENZYME_WEBSERVER;
GRANT SELECT ON cv_view TO ENZYME_WEBSERVER;

GRANT SELECT ON releases TO ENZYME_WEBSERVER;

GRANT EXECUTE ON reaction_qualifiers TO ENZYME_WEBSERVER;

