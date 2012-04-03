--------------------------------------------------------
--  DDL for Trigger TD_CITATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CITATIONS;
CREATE TRIGGER TD_CITATIONS AFTER DELETE ON citations FOR EACH ROW
BEGIN
  INSERT INTO citations_audit (
    enzyme_id,pub_id,order_in,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.enzyme_id,OLD.pub_id,OLD.order_in,OLD.status,OLD.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_CLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CLASSES;
CREATE TRIGGER TD_CLASSES  AFTER DELETE ON classes FOR EACH ROW
BEGIN
  INSERT INTO classes_audit (
    ec1,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.ec1,OLD.name,OLD.description,OLD.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_COFACTORS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_COFACTORS;
CREATE TRIGGER TD_COFACTORS  AFTER DELETE ON cofactors FOR EACH ROW
BEGIN
  INSERT INTO cofactors_audit (
    enzyme_id,source,status,order_in,cofactor_text,
    timestamp,audit_id,dbuser,osuser,remark,action,
    compound_id,operator,op_grp)
  VALUES (
    OLD.enzyme_id,OLD.source,OLD.status,NEW.order_in,OLD.cofactor_text,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D',
    NEW.compound_id,NEW.operator,NEW.op_grp);
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_COMMENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_COMMENTS;
CREATE TRIGGER TD_COMMENTS  AFTER DELETE ON comments FOR EACH ROW
BEGIN
  INSERT INTO comments_audit (
    enzyme_id,comment_text,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.enzyme_id,OLD.comment_text,OLD.order_in,OLD.status,OLD.source,OLD.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_COMPLEX_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_COMPLEX_REACTIONS;
CREATE TRIGGER TD_COMPLEX_REACTIONS  AFTER DELETE ON complex_reactions FOR EACH ROW
BEGIN
  INSERT INTO complex_reactions_audit (
    parent_id,child_id,order_in,coefficient,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.parent_id,OLD.child_id,OLD.order_in,OLD.coefficient,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "TD_COMPLEX_REACTIONS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TD_COMPOUND_DATA
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_COMPOUND_DATA;
CREATE TRIGGER TD_COMPOUND_DATA  AFTER DELETE ON compound_data FOR EACH ROW
BEGIN
  INSERT INTO compound_data_audit (
    compound_id,name,formula,charge,source,accession,child_accession,published,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.compound_id,OLD.name,OLD.formula,OLD.charge,OLD.source,OLD.accession,OLD.child_accession,OLD.published,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_CV_DATABASES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CV_DATABASES;
CREATE TRIGGER TD_CV_DATABASES  AFTER DELETE ON cv_databases FOR EACH ROW



BEGIN
  INSERT INTO cv_databases_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.code,OLD.name,OLD.display_name,OLD.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_CV_NAME_CLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CV_NAME_CLASSES;
CREATE TRIGGER TD_CV_NAME_CLASSES  AFTER DELETE ON cv_name_classes FOR EACH ROW



BEGIN
  INSERT INTO cv_name_classes_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.code,OLD.name,OLD.display_name,OLD.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_CV_STATUS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CV_STATUS;
CREATE TRIGGER TD_CV_STATUS  AFTER DELETE ON cv_status FOR EACH ROW



BEGIN
  INSERT INTO cv_status_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.code,OLD.name,OLD.display_name,OLD.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_CV_VIEW
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CV_VIEW;
CREATE TRIGGER TD_CV_VIEW  AFTER DELETE ON cv_view FOR EACH ROW
BEGIN
  INSERT INTO cv_view_audit (
    code,description,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.code,OLD.description,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_CV_WARNINGS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_CV_WARNINGS;
CREATE TRIGGER TD_CV_WARNINGS  AFTER DELETE ON cv_warnings FOR EACH ROW



BEGIN
  INSERT INTO cv_warnings_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.code,OLD.name,OLD.display_name,OLD.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_ENZYMES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_ENZYMES;
CREATE TRIGGER TD_ENZYMES  AFTER DELETE ON enzymes FOR EACH ROW



BEGIN
  INSERT INTO enzymes_audit (
    enzyme_id,ec1,ec2,ec3,ec4,history,status,note,source,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.enzyme_id,OLD.ec1,OLD.ec2,OLD.ec3,OLD.ec4,OLD.history,OLD.status,OLD.note,OLD.source,OLD.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_FUTURE_EVENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_FUTURE_EVENTS;
CREATE TRIGGER TD_FUTURE_EVENTS  AFTER DELETE ON future_events FOR EACH ROW


BEGIN
  INSERT INTO future_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    NEW.group_id,NEW.event_id,NEW.before_id,NEW.after_id,NEW.event_year,NEW.event_note,NEW.event_class,NEW.status,NEW.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_HISTORY_EVENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_HISTORY_EVENTS;
CREATE TRIGGER TD_HISTORY_EVENTS  AFTER DELETE ON history_events FOR EACH ROW


BEGIN
  INSERT INTO history_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    NEW.group_id,NEW.event_id,NEW.before_id,NEW.after_id,NEW.event_year,NEW.event_note,NEW.event_class,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_INTENZ_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_INTENZ_REACTIONS;
CREATE TRIGGER TD_INTENZ_REACTIONS  AFTER DELETE ON intenz_reactions FOR EACH ROW
BEGIN
  INSERT INTO intenz_reactions_audit (
    reaction_id,intenz_accession,equation,fingerprint,status,source,direction,un_reaction,qualifiers,data_comment,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.reaction_id,OLD.intenz_accession,OLD.equation,OLD.fingerprint,OLD.status,OLD.source,OLD.direction,OLD.un_reaction,OLD.qualifiers,OLD.data_comment,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "TD_INTENZ_REACTIONS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TD_LINKS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_LINKS;
CREATE TRIGGER TD_LINKS  AFTER DELETE ON links FOR EACH ROW
BEGIN
  INSERT INTO links_audit (
    enzyme_id,url,display_name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    OLD.enzyme_id,OLD.url,OLD.display_name,OLD.status,OLD.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D',
    NEW.data_comment);
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_NAMES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_NAMES;
CREATE TRIGGER TD_NAMES  AFTER DELETE ON names FOR EACH ROW
BEGIN
  INSERT INTO names_audit (
    enzyme_id,name,name_class,warning,status,source,note,order_in,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.enzyme_id,OLD.name,OLD.name_class,OLD.warning,OLD.status,OLD.source,OLD.note,OLD.order_in,OLD.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_PUBLICATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_PUBLICATIONS;
CREATE TRIGGER TD_PUBLICATIONS  AFTER DELETE ON publications FOR EACH ROW
BEGIN
  INSERT INTO publications_audit (
    pub_id,medline_id,pubmed_id,pub_type,author,pub_year,title,journal_book,volume,first_page,last_page,edition,editor,pub_company,pub_place,url,web_view,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.pub_id,OLD.medline_id,OLD.pubmed_id,OLD.pub_type,OLD.author,OLD.pub_year,OLD.title,OLD.journal_book,OLD.volume,OLD.first_page,OLD.last_page,OLD.edition,OLD.editor,OLD.pub_company,OLD.pub_place,OLD.url,OLD.web_view,OLD.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_REACTIONS;
CREATE TRIGGER TD_REACTIONS  AFTER DELETE ON reactions FOR EACH ROW
BEGIN
  INSERT INTO reactions_audit (
    enzyme_id,equation,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.enzyme_id,OLD.equation,OLD.order_in,OLD.status,OLD.source,OLD.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_REACTIONS_MAP
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_REACTIONS_MAP;
CREATE TRIGGER TD_REACTIONS_MAP  AFTER DELETE ON reactions_map FOR EACH ROW
BEGIN
  INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.reaction_id,OLD.enzyme_id,OLD.web_view,OLD.order_in,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_REACTION_CITATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_REACTION_CITATIONS;
CREATE TRIGGER TD_REACTION_CITATIONS  AFTER DELETE ON reaction_citations FOR EACH ROW
BEGIN
  INSERT INTO reaction_citations_audit (
    reaction_id,pub_id,order_in,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.reaction_id,OLD.pub_id,OLD.order_in,OLD.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "TD_REACTION_CITATIONS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TD_REACTION_PARTICIPANTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_REACTION_PARTICIPANTS;
CREATE TRIGGER TD_REACTION_PARTICIPANTS  AFTER DELETE ON reaction_participants FOR EACH ROW
BEGIN
  INSERT INTO reaction_participants_audit (
    reaction_id,compound_id,side,coefficient,coeff_type,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.reaction_id,OLD.compound_id,OLD.side,OLD.coefficient,OLD.coeff_type,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/

--------------------------------------------------------
--  DDL for Trigger TD_REACTION_XREFS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_REACTION_XREFS;
CREATE TRIGGER TD_REACTION_XREFS  AFTER DELETE ON reaction_xrefs FOR EACH ROW
BEGIN
  INSERT INTO reaction_xrefs_audit (
    reaction_id,db_code,db_accession,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.reaction_id,OLD.db_code,OLD.db_accession,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;
/
ALTER TRIGGER "TD_REACTION_XREFS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TD_SUBCLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_SUBCLASSES;
CREATE TRIGGER TD_SUBCLASSES  AFTER DELETE ON subclasses FOR EACH ROW



BEGIN
  INSERT INTO subclasses_audit (
    ec1,ec2,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.ec1,OLD.ec2,OLD.name,OLD.description,OLD.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_SUBSUBCLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_SUBSUBCLASSES;
CREATE TRIGGER TD_SUBSUBCLASSES  AFTER DELETE ON subsubclasses FOR EACH ROW



BEGIN
  INSERT INTO subsubclasses_audit (
    ec1,ec2,ec3,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.ec1,OLD.ec2,OLD.ec3,OLD.name,OLD.description,OLD.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_TIMEOUTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_TIMEOUTS;
CREATE TRIGGER TD_TIMEOUTS  AFTER DELETE ON timeouts FOR EACH ROW


BEGIN
  INSERT INTO timeouts_audit (
    enzyme_id,start_date,due_date,timeout_id,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    OLD.enzyme_id,OLD.start_date,OLD.due_date,NEW.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D');
END;


--------------------------------------------------------
--  DDL for Trigger TD_XREFS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TD_XREFS;
CREATE TRIGGER TD_XREFS  AFTER DELETE ON xrefs FOR EACH ROW
BEGIN
  INSERT INTO xrefs_audit (
    enzyme_id,database_code,database_ac,name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    OLD.enzyme_id,OLD.database_code,OLD.database_ac,OLD.name,OLD.status,OLD.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'D',
    NEW.data_comment);
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_CITATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CITATIONS;
CREATE TRIGGER TI_CITATIONS  AFTER INSERT ON citations FOR EACH ROW



BEGIN
  INSERT INTO citations_audit (
    enzyme_id,pub_id,order_in,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.pub_id,NEW.order_in,NEW.status,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_CLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CLASSES;
CREATE TRIGGER TI_CLASSES  AFTER INSERT ON classes FOR EACH ROW



BEGIN
  INSERT INTO classes_audit (
    ec1,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.ec1,NEW.name,NEW.description,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_COFACTORS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_COFACTORS;
CREATE TRIGGER TI_COFACTORS  AFTER INSERT ON cofactors FOR EACH ROW
BEGIN
  INSERT INTO cofactors_audit (
    enzyme_id,source,status,order_in,cofactor_text,
    timestamp,audit_id,dbuser,osuser,remark,action,
    compound_id,operator,op_grp)
  VALUES (
    NEW.enzyme_id,NEW.source,NEW.status,NEW.order_in,NEW.cofactor_text,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I',
    NEW.compound_id,NEW.operator,NEW.op_grp);
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_COMMENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_COMMENTS;
CREATE TRIGGER TI_COMMENTS  AFTER INSERT ON comments FOR EACH ROW
BEGIN
  INSERT INTO comments_audit (
    enzyme_id,comment_text,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.comment_text,NEW.order_in,NEW.status,NEW.source,NEW.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_COMPLEX_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_COMPLEX_REACTIONS;
CREATE TRIGGER TI_COMPLEX_REACTIONS  AFTER INSERT ON complex_reactions FOR EACH ROW
BEGIN
  INSERT INTO complex_reactions_audit (
    parent_id,child_id,order_in,coefficient,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.parent_id,NEW.child_id,NEW.order_in,NEW.coefficient,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "TI_COMPLEX_REACTIONS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TI_COMPOUND_DATA
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_COMPOUND_DATA;
CREATE TRIGGER TI_COMPOUND_DATA  AFTER INSERT ON compound_data FOR EACH ROW
BEGIN
  INSERT INTO compound_data_audit (
    compound_id,name,formula,charge,source,accession,child_accession,published,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.compound_id,NEW.name,NEW.formula,NEW.charge,NEW.source,NEW.accession,NEW.child_accession,NEW.published,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_CV_DATABASES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CV_DATABASES;
CREATE TRIGGER TI_CV_DATABASES  AFTER INSERT ON cv_databases FOR EACH ROW



BEGIN
  INSERT INTO cv_databases_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_CV_NAME_CLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CV_NAME_CLASSES;
CREATE TRIGGER TI_CV_NAME_CLASSES  AFTER INSERT ON cv_name_classes FOR EACH ROW



BEGIN
  INSERT INTO cv_name_classes_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_CV_STATUS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CV_STATUS;
CREATE TRIGGER TI_CV_STATUS  AFTER INSERT ON cv_status FOR EACH ROW



BEGIN
  INSERT INTO cv_status_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_CV_VIEW
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CV_VIEW;
CREATE TRIGGER TI_CV_VIEW  AFTER INSERT ON cv_view FOR EACH ROW
BEGIN
  INSERT INTO cv_view_audit (
    code,description,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.description,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_CV_WARNINGS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_CV_WARNINGS;
CREATE TRIGGER TI_CV_WARNINGS  AFTER INSERT ON cv_warnings FOR EACH ROW



BEGIN
  INSERT INTO cv_warnings_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_ENZYMES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_ENZYMES;
CREATE TRIGGER TI_ENZYMES  AFTER INSERT ON enzymes FOR EACH ROW



BEGIN
  INSERT INTO enzymes_audit (
    enzyme_id,ec1,ec2,ec3,ec4,history,status,note,source,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.ec1,NEW.ec2,NEW.ec3,NEW.ec4,NEW.history,NEW.status,NEW.note,NEW.source,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_FUTURE_EVENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_FUTURE_EVENTS;
CREATE TRIGGER TI_FUTURE_EVENTS  AFTER INSERT ON future_events FOR EACH ROW


BEGIN
  INSERT INTO future_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    NEW.group_id,NEW.event_id,NEW.before_id,NEW.after_id,NEW.event_year,NEW.event_note,NEW.event_class,NEW.status,NEW.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_HISTORY_EVENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_HISTORY_EVENTS;
CREATE TRIGGER TI_HISTORY_EVENTS  AFTER INSERT ON history_events FOR EACH ROW


BEGIN
  INSERT INTO history_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    NEW.group_id,NEW.event_id,NEW.before_id,NEW.after_id,NEW.event_year,NEW.event_note,NEW.event_class,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_INTENZ_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_INTENZ_REACTIONS;
CREATE TRIGGER TI_INTENZ_REACTIONS  AFTER INSERT ON intenz_reactions FOR EACH ROW
BEGIN
  INSERT INTO intenz_reactions_audit (
    reaction_id,intenz_accession,equation,fingerprint,status,source,direction,un_reaction,qualifiers,data_comment,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.intenz_accession,NEW.equation,NEW.fingerprint,NEW.status,NEW.source,NEW.direction,NEW.un_reaction,NEW.qualifiers,NEW.data_comment,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "TI_INTENZ_REACTIONS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TI_LINKS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_LINKS;
CREATE TRIGGER TI_LINKS  AFTER INSERT ON links FOR EACH ROW
BEGIN
  INSERT INTO links_audit (
    enzyme_id,url,display_name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    NEW.enzyme_id,NEW.url,NEW.display_name,NEW.status,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I',
    NEW.data_comment);
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_NAMES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_NAMES;
CREATE TRIGGER TI_NAMES  AFTER INSERT ON names FOR EACH ROW
BEGIN
  INSERT INTO names_audit (
    enzyme_id,name,name_class,warning,status,source,note,order_in,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.name,NEW.name_class,NEW.warning,NEW.status,NEW.source,NEW.note,NEW.order_in,NEW.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_PUBLICATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_PUBLICATIONS;
CREATE TRIGGER TI_PUBLICATIONS  AFTER INSERT ON publications FOR EACH ROW
BEGIN
  INSERT INTO publications_audit (
    pub_id,medline_id,pubmed_id,pub_type,author,pub_year,title,journal_book,volume,first_page,last_page,edition,editor,pub_company,pub_place,url,web_view,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.pub_id,NEW.medline_id,NEW.pubmed_id,NEW.pub_type,NEW.author,NEW.pub_year,NEW.title,NEW.journal_book,NEW.volume,NEW.first_page,NEW.last_page,NEW.edition,NEW.editor,NEW.pub_company,NEW.pub_place,NEW.url,NEW.web_view,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_REACTIONS;
CREATE TRIGGER TI_REACTIONS  AFTER INSERT ON reactions FOR EACH ROW
BEGIN
  INSERT INTO reactions_audit (
    enzyme_id,equation,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.equation,NEW.order_in,NEW.status,NEW.source,NEW.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_REACTIONS_MAP
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_REACTIONS_MAP;
CREATE TRIGGER TI_REACTIONS_MAP  AFTER INSERT ON reactions_map FOR EACH ROW
BEGIN
  INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.enzyme_id,NEW.web_view,NEW.order_in,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_REACTION_CITATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_REACTION_CITATIONS;
CREATE TRIGGER TI_REACTION_CITATIONS  AFTER INSERT ON reaction_citations FOR EACH ROW
BEGIN
  INSERT INTO reaction_citations_audit (
    reaction_id,pub_id,order_in,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.pub_id,NEW.order_in,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "TI_REACTION_CITATIONS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TI_REACTION_PARTICIPANTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_REACTION_PARTICIPANTS;
CREATE TRIGGER TI_REACTION_PARTICIPANTS  AFTER INSERT ON reaction_participants FOR EACH ROW
BEGIN
  INSERT INTO reaction_participants_audit (
    reaction_id,compound_id,side,coefficient,coeff_type,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.compound_id,NEW.side,NEW.coefficient,NEW.coeff_type,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_REACTION_QUALIFIERS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_REACTION_QUALIFIERS;
CREATE TRIGGER TI_REACTION_QUALIFIERSBEFORE INSERT ON intenz_reactions FOR EACH ROW
BEGIN
	IF check_reaction_qualifiers(NEW.qualifiers) > 0
	THEN
		RAISE_APPLICATION_ERROR(-20000, 'Bad reaction qualifiers');
	END IF;
END;
/

--------------------------------------------------------
--  DDL for Trigger TI_REACTION_XREFS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_REACTION_XREFS;
CREATE TRIGGER TI_REACTION_XREFS  AFTER INSERT ON reaction_xrefs FOR EACH ROW
BEGIN
  INSERT INTO reaction_xrefs_audit (
    reaction_id,db_code,db_accession,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.db_code,NEW.db_accession,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;
/
ALTER TRIGGER "TI_REACTION_XREFS" DISABLE;
--------------------------------------------------------
--  DDL for Trigger TI_SUBCLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_SUBCLASSES;
CREATE TRIGGER TI_SUBCLASSES  AFTER INSERT ON subclasses FOR EACH ROW



BEGIN
  INSERT INTO subclasses_audit (
    ec1,ec2,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.ec1,NEW.ec2,NEW.name,NEW.description,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_SUBSUBCLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_SUBSUBCLASSES;
CREATE TRIGGER TI_SUBSUBCLASSES  AFTER INSERT ON subsubclasses FOR EACH ROW



BEGIN
  INSERT INTO subsubclasses_audit (
    ec1,ec2,ec3,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.ec1,NEW.ec2,NEW.ec3,NEW.name,NEW.description,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_TIMEOUTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_TIMEOUTS;
CREATE TRIGGER TI_TIMEOUTS  AFTER INSERT ON timeouts FOR EACH ROW


BEGIN
  INSERT INTO timeouts_audit (
    enzyme_id,start_date,due_date,timeout_id,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.start_date,NEW.due_date,NEW.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I');
END;


--------------------------------------------------------
--  DDL for Trigger TI_XREFS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TI_XREFS;
CREATE TRIGGER TI_XREFS  AFTER INSERT ON xrefs FOR EACH ROW
BEGIN
  INSERT INTO xrefs_audit (
    enzyme_id,database_code,database_ac,name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    NEW.enzyme_id,NEW.database_code,NEW.database_ac,NEW.name,NEW.status,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'I',
    NEW.data_comment);
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_CITATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CITATIONS;
CREATE TRIGGER TU_CITATIONS  AFTER UPDATE ON citations FOR EACH ROW



BEGIN
  INSERT INTO citations_audit (
    enzyme_id,pub_id,order_in,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.pub_id,NEW.order_in,NEW.status,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_CLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CLASSES;
CREATE TRIGGER TU_CLASSES  AFTER UPDATE ON classes FOR EACH ROW



BEGIN
  INSERT INTO classes_audit (
    ec1,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.ec1,NEW.name,NEW.description,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_COFACTORS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_COFACTORS;
CREATE TRIGGER TU_COFACTORS  AFTER UPDATE ON cofactors FOR EACH ROW
BEGIN
  INSERT INTO cofactors_audit (
    enzyme_id,source,status,order_in,cofactor_text,
    timestamp,audit_id,dbuser,osuser,remark,action,
    compound_id,operator,op_grp)
  VALUES (
    NEW.enzyme_id,NEW.source,NEW.status,NEW.order_in,NEW.cofactor_text,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U',
    NEW.compound_id,NEW.operator,NEW.op_grp);
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_COMMENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_COMMENTS;
CREATE TRIGGER TU_COMMENTS  AFTER UPDATE ON comments FOR EACH ROW
BEGIN
  INSERT INTO comments_audit (
    enzyme_id,comment_text,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.comment_text,NEW.order_in,NEW.status,NEW.source,NEW.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_COMPLEX_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_COMPLEX_REACTIONS;
CREATE TRIGGER TU_COMPLEX_REACTIONS  AFTER UPDATE ON complex_reactions FOR EACH ROW
BEGIN
  INSERT INTO complex_reactions_audit (
    parent_id,child_id,order_in,coefficient,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.parent_id,NEW.child_id,NEW.order_in,NEW.coefficient,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_COMPOUND_DATA
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_COMPOUND_DATA;
CREATE TRIGGER TU_COMPOUND_DATA  AFTER UPDATE ON compound_data FOR EACH ROW
BEGIN
  INSERT INTO compound_data_audit (
    compound_id,name,formula,charge,source,accession,child_accession,published,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.compound_id,NEW.name,NEW.formula,NEW.charge,NEW.source,NEW.accession,NEW.child_accession,NEW.published,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_CV_DATABASES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CV_DATABASES;
CREATE TRIGGER TU_CV_DATABASES  AFTER UPDATE ON cv_databases FOR EACH ROW



BEGIN
  INSERT INTO cv_databases_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_CV_NAME_CLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CV_NAME_CLASSES;
CREATE TRIGGER TU_CV_NAME_CLASSES  AFTER UPDATE ON cv_name_classes FOR EACH ROW



BEGIN
  INSERT INTO cv_name_classes_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_CV_STATUS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CV_STATUS;
CREATE TRIGGER TU_CV_STATUS  AFTER UPDATE ON cv_status FOR EACH ROW



BEGIN
  INSERT INTO cv_status_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_CV_VIEW
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CV_VIEW;
CREATE TRIGGER TU_CV_VIEW  AFTER UPDATE ON cv_view FOR EACH ROW
BEGIN
  INSERT INTO cv_view_audit (
    code,description,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.description,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_CV_WARNINGS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_CV_WARNINGS;
CREATE TRIGGER TU_CV_WARNINGS  AFTER UPDATE ON cv_warnings FOR EACH ROW



BEGIN
  INSERT INTO cv_warnings_audit (
    code,name,display_name,sort_order,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.code,NEW.name,NEW.display_name,NEW.sort_order,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_ENZYMES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_ENZYMES;
CREATE TRIGGER TU_ENZYMES  AFTER UPDATE ON enzymes FOR EACH ROW



BEGIN
  INSERT INTO enzymes_audit (
    enzyme_id,ec1,ec2,ec3,ec4,history,status,note,source,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.ec1,NEW.ec2,NEW.ec3,NEW.ec4,NEW.history,NEW.status,NEW.note,NEW.source,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_FUTURE_EVENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_FUTURE_EVENTS;
CREATE TRIGGER TU_FUTURE_EVENTS  AFTER UPDATE ON future_events FOR EACH ROW


BEGIN
  INSERT INTO future_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class, status, timeout_id,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    NEW.group_id,NEW.event_id,NEW.before_id,NEW.after_id,NEW.event_year,NEW.event_note,NEW.event_class,NEW.status,NEW.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_HISTORY_EVENTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_HISTORY_EVENTS;
CREATE TRIGGER TU_HISTORY_EVENTS  AFTER UPDATE ON history_events FOR EACH ROW


BEGIN
  INSERT INTO history_events_audit (
    group_id, event_id, before_id, after_id, event_year, event_note, event_class,
    timestamp, audit_id, dbuser, osuser, remark, action)
  VALUES (
    NEW.group_id,NEW.event_id,NEW.before_id,NEW.after_id,NEW.event_year,NEW.event_note,NEW.event_class,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_INTENZ_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_INTENZ_REACTIONS;
CREATE TRIGGER TU_INTENZ_REACTIONS  AFTER UPDATE ON intenz_reactions FOR EACH ROW
BEGIN
  INSERT INTO intenz_reactions_audit (
    reaction_id,intenz_accession,equation,fingerprint,status,source,direction,un_reaction,qualifiers,data_comment,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.intenz_accession,NEW.equation,NEW.fingerprint,NEW.status,NEW.source,NEW.direction,NEW.un_reaction,NEW.qualifiers,NEW.data_comment,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_LINKS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_LINKS;
CREATE TRIGGER TU_LINKS  AFTER UPDATE ON links FOR EACH ROW
BEGIN
  INSERT INTO links_audit (
    enzyme_id,url,display_name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    NEW.enzyme_id,NEW.url,NEW.display_name,NEW.status,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U',
    NEW.data_comment);
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_NAMES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_NAMES;
CREATE TRIGGER TU_NAMES  AFTER UPDATE ON names FOR EACH ROW
BEGIN
  INSERT INTO names_audit (
    enzyme_id,name,name_class,warning,status,source,note,order_in,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.name,NEW.name_class,NEW.warning,NEW.status,NEW.source,NEW.note,NEW.order_in,NEW.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_PUBLICATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_PUBLICATIONS;
CREATE TRIGGER TU_PUBLICATIONS  AFTER UPDATE ON publications FOR EACH ROW
BEGIN
  INSERT INTO publications_audit (
    pub_id,medline_id,pubmed_id,pub_type,author,pub_year,title,journal_book,volume,first_page,last_page,edition,editor,pub_company,pub_place,url,web_view,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.pub_id,NEW.medline_id,NEW.pubmed_id,NEW.pub_type,NEW.author,NEW.pub_year,NEW.title,NEW.journal_book,NEW.volume,NEW.first_page,NEW.last_page,NEW.edition,NEW.editor,NEW.pub_company,NEW.pub_place,NEW.url,NEW.web_view,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_REACTIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_REACTIONS;
CREATE TRIGGER TU_REACTIONS  AFTER UPDATE ON reactions FOR EACH ROW
BEGIN
  INSERT INTO reactions_audit (
    enzyme_id,equation,order_in,status,source,web_view,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.equation,NEW.order_in,NEW.status,NEW.source,NEW.web_view,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_REACTIONS_MAP
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_REACTIONS_MAP;
CREATE TRIGGER TU_REACTIONS_MAP  AFTER UPDATE ON reactions_map FOR EACH ROW
BEGIN
  INSERT INTO reactions_map_audit (
    reaction_id,enzyme_id,web_view,order_in,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.enzyme_id,NEW.web_view,NEW.order_in,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_REACTION_CITATIONS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_REACTION_CITATIONS;
CREATE TRIGGER TU_REACTION_CITATIONS  AFTER UPDATE ON reaction_citations FOR EACH ROW
BEGIN
  INSERT INTO reaction_citations_audit (
    reaction_id,pub_id,order_in,source,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.pub_id,NEW.order_in,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_REACTION_PARTICIPANTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_REACTION_PARTICIPANTS;
CREATE TRIGGER TU_REACTION_PARTICIPANTS  AFTER UPDATE ON reaction_participants FOR EACH ROW
BEGIN
  INSERT INTO reaction_participants_audit (
    reaction_id,compound_id,side,coefficient,coeff_type,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.compound_id,NEW.side,NEW.coefficient,NEW.coeff_type,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_REACTION_QUALIFIERS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_REACTION_QUALIFIERS;
CREATE TRIGGER TU_REACTION_QUALIFIERSBEFORE UPDATE ON intenz_reactions FOR EACH ROW
BEGIN
	IF check_reaction_qualifiers(NEW.qualifiers) > 0
	THEN
		RAISE_APPLICATION_ERROR(-20000, 'Bad reaction qualifiers');
	END IF;
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_REACTION_XREFS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_REACTION_XREFS;
CREATE TRIGGER TU_REACTION_XREFS  AFTER UPDATE ON reaction_xrefs FOR EACH ROW
BEGIN
  INSERT INTO reaction_xrefs_audit (
    reaction_id,db_code,db_accession,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.reaction_id,NEW.db_code,NEW.db_accession,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;
/

--------------------------------------------------------
--  DDL for Trigger TU_SUBCLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_SUBCLASSES;
CREATE TRIGGER TU_SUBCLASSES  AFTER UPDATE ON subclasses FOR EACH ROW



BEGIN
  INSERT INTO subclasses_audit (
    ec1,ec2,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.ec1,NEW.ec2,NEW.name,NEW.description,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_SUBSUBCLASSES
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_SUBSUBCLASSES;
CREATE TRIGGER TU_SUBSUBCLASSES  AFTER UPDATE ON subsubclasses FOR EACH ROW



BEGIN
  INSERT INTO subsubclasses_audit (
    ec1,ec2,ec3,name,description,active,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.ec1,NEW.ec2,NEW.ec3,NEW.name,NEW.description,NEW.active,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_TIMEOUTS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_TIMEOUTS;
CREATE TRIGGER TU_TIMEOUTS  AFTER UPDATE ON timeouts FOR EACH ROW


BEGIN
  INSERT INTO timeouts_audit (
    enzyme_id,start_date,due_date,timeout_id,
    timestamp,audit_id,dbuser,osuser,remark,action)
  VALUES (
    NEW.enzyme_id,NEW.start_date,NEW.due_date,NEW.timeout_id,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U');
END;


--------------------------------------------------------
--  DDL for Trigger TU_XREFS
--------------------------------------------------------

DROP TRIGGER IF EXISTS TU_XREFS;
CREATE TRIGGER TU_XREFS  AFTER UPDATE ON xrefs FOR EACH ROW
BEGIN
  INSERT INTO xrefs_audit (
    enzyme_id,database_code,database_ac,name,status,source,
    timestamp,audit_id,dbuser,osuser,remark,action,
    data_comment)
  VALUES (
    NEW.enzyme_id,NEW.database_code,NEW.database_ac,NEW.name,NEW.status,NEW.source,
    sysdate,s_audit_id.nextval,user,auditpackage.osuser,auditpackage.remark,'U',
    NEW.data_comment);
END;


--------------------------------------------------------
--  DDL for Trigger T_COMPOUND_DATA$AUTOINC
--------------------------------------------------------

DROP TRIGGER IF EXISTS T_COMPOUND_DATA$AUTOINC;
CREATE TRIGGER T_COMPOUND_DATA$AUTOINCBEFORE INSERT ON compound_data
FOR EACH ROW
 WHEN (new.compound_id IS NULL) BEGIN
    SELECT s_compound_id.nextval INTO NEW.compound_id FROM DUAL;
END;


--------------------------------------------------------
--  DDL for Trigger T_ENZYMES$SOURCE_CHANGE
--------------------------------------------------------

DROP TRIGGER IF EXISTS T_ENZYMES$SOURCE_CHANGE;
CREATE TRIGGER T_ENZYMES$SOURCE_CHANGE  BEFORE UPDATE OF source ON enzymes
  FOR EACH ROW
   WHEN (new.source != old.source) BEGIN
  RAISE_APPLICATION_ERROR(-20000, 'enzyme.source must not be changed', TRUE);
END;


--------------------------------------------------------
--  DDL for Trigger T_INTENZ_REACTIONS$AUTOINC
--------------------------------------------------------

DROP TRIGGER IF EXISTS T_INTENZ_REACTIONS$AUTOINC;
CREATE TRIGGER T_INTENZ_REACTIONS$AUTOINC BEFORE INSERT ON intenz_reactions
FOR EACH ROW
 WHEN (new.reaction_id IS NULL) BEGIN
    SELECT s_reaction_id.nextval INTO NEW.reaction_id FROM DUAL;
END;


