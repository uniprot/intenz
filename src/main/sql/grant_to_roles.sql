SET DOC OFF
/*************************************************************

  Project:    IntEnz - Oracle DB

  Purpose:    Grant privileges to roles.

  Usage:      SQL*Plus> @grant_to_roles
  
  $Date: 2008/05/27 15:47:24 $
  $Revision: 1.1 $

 *************************************************************/

SET ECHO ON;

/* 
  Controlled vocab 
  Only changable by ENZYME
*/
GRANT SELECT                      ON cv_databases          TO ENZYME_SELECT;
GRANT SELECT                      ON cv_databases          TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_databases          TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_databases          TO ENZYME_PRODUCTION;
GRANT SELECT                      ON cv_databases_audit    TO ENZYME_SELECT;
GRANT SELECT                      ON cv_databases_audit    TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_databases_audit    TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_databases_audit    TO ENZYME_PRODUCTION;

GRANT SELECT                      ON cv_name_classes       TO ENZYME_SELECT;
GRANT SELECT                      ON cv_name_classes       TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_name_classes       TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_name_classes       TO ENZYME_PRODUCTION;
GRANT SELECT                      ON cv_name_classes_audit TO ENZYME_SELECT;
GRANT SELECT                      ON cv_name_classes_audit TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_name_classes_audit TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_name_classes_audit TO ENZYME_PRODUCTION;

GRANT SELECT                      ON cv_status             TO ENZYME_SELECT;
GRANT SELECT                      ON cv_status             TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_status             TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_status             TO ENZYME_PRODUCTION;
GRANT SELECT                      ON cv_status_audit       TO ENZYME_SELECT;
GRANT SELECT                      ON cv_status_audit       TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_status_audit       TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_status_audit       TO ENZYME_PRODUCTION;

GRANT SELECT                      ON cv_warnings           TO ENZYME_SELECT;
GRANT SELECT                      ON cv_warnings           TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_warnings           TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_warnings           TO ENZYME_PRODUCTION;
GRANT SELECT                      ON cv_warnings_audit     TO ENZYME_SELECT;
GRANT SELECT                      ON cv_warnings_audit     TO ENZYME_CURATOR;
GRANT SELECT                      ON cv_warnings_audit     TO ENZYME_COMMITTEE;
GRANT SELECT                      ON cv_warnings_audit     TO ENZYME_PRODUCTION;


/* 
  Text Index
*/
GRANT SELECT                      ON intenz_text           TO ENZYME_SELECT;
GRANT SELECT                      ON intenz_text           TO ENZYME_CURATOR;
GRANT SELECT			                ON intenz_text           TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON intenz_text           TO ENZYME_PRODUCTION;

/*
  ID-EC mapping table
*/
GRANT SELECT                      ON id2ec                 TO ENZYME_SELECT;
GRANT SELECT                      ON id2ec                 TO ENZYME_CURATOR;
GRANT SELECT			                ON id2ec                 TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON id2ec                 TO ENZYME_PRODUCTION;




/* 
  Hierarchy
  Only changable by ENZYME_COMMITTEE
*/
GRANT SELECT                      ON classes               TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON classes               TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON classes               TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON classes               TO ENZYME_PRODUCTION;
GRANT SELECT                      ON classes_audit         TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON classes_audit         TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON classes_audit         TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON classes_audit         TO ENZYME_PRODUCTION;

GRANT SELECT                      ON subclasses            TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON subclasses            TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON subclasses            TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON subclasses            TO ENZYME_PRODUCTION;
GRANT SELECT                      ON subclasses_audit      TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON subclasses_audit      TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON subclasses_audit      TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON subclasses_audit      TO ENZYME_PRODUCTION;

GRANT SELECT                      ON subsubclasses         TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON subsubclasses         TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON subsubclasses         TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON subsubclasses         TO ENZYME_PRODUCTION;
GRANT SELECT                      ON subsubclasses_audit   TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON subsubclasses_audit   TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON subsubclasses_audit   TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON subsubclasses_audit   TO ENZYME_PRODUCTION;


/* 
  Enzymes 
*/
GRANT SELECT                      ON enzymes               TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON enzymes               TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON enzymes               TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON enzymes               TO ENZYME_PRODUCTION;
GRANT SELECT                      ON enzymes_audit         TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON enzymes_audit         TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON enzymes_audit         TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON enzymes_audit         TO ENZYME_PRODUCTION;

GRANT SELECT                      ON names                 TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON names                 TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON names                 TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON names                 TO ENZYME_PRODUCTION;
GRANT SELECT                      ON names_audit           TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON names_audit           TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON names_audit           TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON names_audit           TO ENZYME_PRODUCTION;

GRANT SELECT                      ON reactions             TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON reactions             TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reactions             TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON reactions             TO ENZYME_PRODUCTION;
GRANT SELECT                      ON reactions_audit       TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON reactions_audit       TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON reactions_audit       TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON reactions_audit       TO ENZYME_PRODUCTION;

GRANT SELECT                      ON comments              TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON comments              TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON comments              TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON comments              TO ENZYME_PRODUCTION;
GRANT SELECT                      ON comments_audit        TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON comments_audit        TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON comments_audit        TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON comments_audit        TO ENZYME_PRODUCTION;

GRANT SELECT                      ON xrefs                 TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON xrefs                 TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON xrefs                 TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON xrefs                 TO ENZYME_PRODUCTION;
GRANT SELECT                      ON xrefs_audit           TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON xrefs_audit           TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON xrefs_audit           TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON xrefs_audit           TO ENZYME_PRODUCTION;

GRANT SELECT                      ON cofactors             TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON cofactors             TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON cofactors             TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON cofactors             TO ENZYME_PRODUCTION;
GRANT SELECT                      ON cofactors_audit       TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON cofactors_audit       TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON cofactors_audit       TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON cofactors_audit       TO ENZYME_PRODUCTION;

GRANT SELECT                      ON links                 TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON links                 TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON links                 TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON links                 TO ENZYME_PRODUCTION;
GRANT SELECT                      ON links_audit           TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON links_audit           TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON links_audit           TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON links_audit           TO ENZYME_PRODUCTION;

GRANT SELECT                      ON publications          TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON publications          TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON publications          TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON publications          TO ENZYME_PRODUCTION;
GRANT SELECT                      ON publications_audit    TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON publications_audit    TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON publications_audit    TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON publications_audit    TO ENZYME_PRODUCTION;

GRANT SELECT                      ON citations             TO ENZYME_SELECT;
GRANT SELECT,INSERT,UPDATE,DELETE ON citations             TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON citations             TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT,UPDATE,DELETE ON citations             TO ENZYME_PRODUCTION;
GRANT SELECT                      ON citations_audit       TO ENZYME_SELECT;
GRANT SELECT,INSERT               ON citations_audit       TO ENZYME_CURATOR;
GRANT SELECT,INSERT               ON citations_audit       TO ENZYME_COMMITTEE;
GRANT SELECT,INSERT               ON citations_audit       TO ENZYME_PRODUCTION;

GRANT SELECT ON releases TO ENZYME_SELECT;

/*
 * Rhea-related
 */
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

GRANT SELECT,INSERT,UPDATE,DELETE ON reactions_map TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON intenz_reactions TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON intenz_reactions_audit TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON compound_data TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON compound_data_updates TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reaction_participants TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON complex_reactions TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON complex_reactions_audit TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reaction_citations TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reaction_citations_audit TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reaction_xrefs TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reaction_xrefs_audit TO ENZYME_CURATOR;
GRANT SELECT,INSERT,UPDATE,DELETE ON reaction_mergings TO ENZYME_CURATOR;

GRANT SELECT ON cv_coeff_types TO ENZYME_SELECT;
GRANT SELECT ON cv_comp_pub_avail TO ENZYME_SELECT;
GRANT SELECT ON cv_location TO ENZYME_SELECT;
GRANT SELECT ON cv_operators TO ENZYME_SELECT;
GRANT SELECT ON cv_reaction_directions TO ENZYME_SELECT;
GRANT SELECT ON cv_reaction_qualifiers TO ENZYME_SELECT;
GRANT SELECT ON cv_reaction_sides TO ENZYME_SELECT;
GRANT SELECT ON cv_view TO ENZYME_SELECT;

/* 
   Entry history and status
   No write privs, access only via procedures
*/
GRANT SELECT                      ON timeouts              TO ENZYME_SELECT;
GRANT SELECT                      ON timeouts              TO ENZYME_CURATOR;
GRANT SELECT                      ON timeouts              TO ENZYME_COMMITTEE;
GRANT SELECT                      ON timeouts              TO ENZYME_PRODUCTION;
GRANT SELECT                      ON timeouts_audit        TO ENZYME_SELECT;
GRANT SELECT                      ON timeouts_audit        TO ENZYME_CURATOR;
GRANT SELECT                      ON timeouts_audit        TO ENZYME_COMMITTEE;
GRANT SELECT                      ON timeouts_audit        TO ENZYME_PRODUCTION;

GRANT SELECT                      ON history_events        TO ENZYME_SELECT;
GRANT SELECT                      ON history_events        TO ENZYME_CURATOR;
GRANT SELECT                      ON history_events        TO ENZYME_COMMITTEE;
GRANT SELECT                      ON history_events        TO ENZYME_PRODUCTION;
GRANT SELECT                      ON history_events_audit  TO ENZYME_SELECT;
GRANT SELECT                      ON history_events_audit  TO ENZYME_CURATOR;
GRANT SELECT                      ON history_events_audit  TO ENZYME_COMMITTEE;
GRANT SELECT                      ON history_events_audit  TO ENZYME_PRODUCTION;

GRANT SELECT                      ON future_events         TO ENZYME_SELECT;
GRANT SELECT                      ON future_events         TO ENZYME_CURATOR;
GRANT SELECT                      ON future_events         TO ENZYME_COMMITTEE;
GRANT SELECT                      ON future_events         TO ENZYME_PRODUCTION;
GRANT SELECT                      ON future_events_audit   TO ENZYME_SELECT;
GRANT SELECT                      ON future_events_audit   TO ENZYME_CURATOR;
GRANT SELECT                      ON future_events_audit   TO ENZYME_COMMITTEE;
GRANT SELECT                      ON future_events_audit   TO ENZYME_PRODUCTION;



/*
GRANT SELECT                      ON transitions           TO ENZYME_SELECT;
GRANT SELECT                      ON transitions           TO ENZYME_CURATOR;
GRANT SELECT                      ON transitions           TO ENZYME_COMMITTEE;
GRANT SELECT                      ON transitions           TO ENZYME_PRODUCTION;
GRANT SELECT                      ON transitions_audit     TO ENZYME_SELECT;
GRANT SELECT                      ON transitions_audit     TO ENZYME_CURATOR;
GRANT SELECT                      ON transitions_audit     TO ENZYME_COMMITTEE;
GRANT SELECT                      ON transitions_audit     TO ENZYME_PRODUCTION;
*/

/* 
   Views
*/
GRANT SELECT                      ON v_ec_for_datalib      TO ENZYME_SELECT;
GRANT SELECT                      ON v_ec_for_datalib      TO ENZYME_CURATOR;
GRANT SELECT                      ON v_ec_for_datalib      TO ENZYME_COMMITTEE;
GRANT SELECT                      ON v_ec_for_datalib      TO ENZYME_PRODUCTION;

GRANT SELECT                      ON v_interpro            TO ENZYME_SELECT;
GRANT SELECT                      ON v_interpro            TO ENZYME_CURATOR;
GRANT SELECT                      ON v_interpro            TO ENZYME_COMMITTEE;
GRANT SELECT                      ON v_interpro            TO ENZYME_PRODUCTION;


/* 
  Sequences and other stuff
 */
GRANT SELECT                      ON s_enzyme_id           TO ENZYME_CURATOR;
GRANT SELECT                      ON s_enzyme_id           TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_enzyme_id           TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_pub_id              TO ENZYME_CURATOR;
GRANT SELECT                      ON s_pub_id              TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_pub_id              TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_audit_id            TO ENZYME_CURATOR;
GRANT SELECT                      ON s_audit_id            TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_audit_id            TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_timeout_id          TO ENZYME_CURATOR;
GRANT SELECT                      ON s_timeout_id          TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_timeout_id          TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_future_group_id     TO ENZYME_CURATOR;
GRANT SELECT                      ON s_future_group_id     TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_future_group_id     TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_future_event_id     TO ENZYME_CURATOR;
GRANT SELECT                      ON s_future_event_id     TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_future_event_id     TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_history_group_id    TO ENZYME_CURATOR;
GRANT SELECT                      ON s_history_group_id    TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_history_group_id    TO ENZYME_PRODUCTION;

GRANT SELECT                      ON s_history_event_id    TO ENZYME_CURATOR;
GRANT SELECT                      ON s_history_event_id    TO ENZYME_COMMITTEE;
GRANT SELECT                      ON s_history_event_id    TO ENZYME_PRODUCTION;

 
/*
  Procedures, functions and packages 
*/
GRANT EXECUTE                     ON auditpackage          TO ENZYME_CURATOR;
GRANT EXECUTE                     ON auditpackage          TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON auditpackage          TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON event                 TO ENZYME_CURATOR;
GRANT EXECUTE                     ON event                 TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON event                 TO ENZYME_PRODUCTION;


GRANT EXECUTE                     ON f_quad2string         TO ENZYME_SELECT;
GRANT EXECUTE                     ON f_quad2string         TO ENZYME_CURATOR;
GRANT EXECUTE                     ON f_quad2string         TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON f_quad2string         TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON p_string2quad         TO ENZYME_SELECT;
GRANT EXECUTE                     ON p_string2quad         TO ENZYME_CURATOR;
GRANT EXECUTE                     ON p_string2quad         TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON p_string2quad         TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON f_rhea_family_id      TO ENZYME_SELECT;
GRANT EXECUTE                     ON f_rhea_family_id      TO ENZYME_CURATOR;
GRANT EXECUTE                     ON f_rhea_family_id      TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON f_rhea_family_id      TO ENZYME_PRODUCTION;
/*
GRANT EXECUTE                     ON transit               TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON p_start_timeout       TO ENZYME_CURATOR;
GRANT EXECUTE                     ON p_start_timeout       TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON p_start_timeout       TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON p_restart_timeout     TO ENZYME_CURATOR;
GRANT EXECUTE                     ON p_restart_timeout     TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON p_restart_timeout     TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON p_stop_timeout        TO ENZYME_CURATOR;
GRANT EXECUTE                     ON p_stop_timeout        TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON p_stop_timeout        TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON p_change_status       TO ENZYME_CURATOR;
GRANT EXECUTE                     ON p_change_status       TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON p_change_status       TO ENZYME_PRODUCTION;
*/

GRANT EXECUTE                     ON p_clone_enzyme        TO ENZYME_CURATOR;
GRANT EXECUTE                     ON p_clone_enzyme        TO ENZYME_COMMITTEE;
GRANT EXECUTE                     ON p_clone_enzyme        TO ENZYME_PRODUCTION;

GRANT EXECUTE                     ON p_print_publication   TO ENZYME_CURATOR,ENZYME_PRODUCTION;
GRANT EXECUTE                     ON p_merge_publications  TO ENZYME_CURATOR,ENZYME_PRODUCTION;
 
GRANT EXECUTE                     ON xml_display_sp_code    TO ENZYME_SELECT;
GRANT EXECUTE                     ON xml_display_chebi_code TO ENZYME_SELECT;
GRANT EXECUTE                     ON xml_display_unicode    TO ENZYME_SELECT;

GRANT EXECUTE ON reaction_qualifiers TO ENZYME_SELECT;
GRANT EXECUTE ON reaction_qualifiers TO ENZYME_CURATOR;
