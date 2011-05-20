--------------------------------------------------------
--  File created - Wednesday-March-25-2009   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure P_CHANGE_STATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_CHANGE_STATUS" (
  the_enzyme_id IN enzymes.enzyme_id%type,
  the_status    IN enzymes.status%type
)
AS
  CURSOR lockcursor IS
  SELECT 1
    FROM enzymes e1,
         names n1,
         comments c1,
         xrefs x1,
         links l1
   WHERE e1.enzyme_id = the_enzyme_id
     AND e1.enzyme_id = n1.enzyme_id (+)
     AND e1.enzyme_id = c1.enzyme_id (+)
     AND e1.enzyme_id = x1.enzyme_id (+)
     AND e1.enzyme_id = l1.enzyme_id (+)
         FOR UPDATE;
  lockrec lockcursor%rowtype;
BEGIN
  /* check the_enzyme_id is not null */
  IF the_enzyme_id IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_enzyme_id is null');
  END IF;
  /* check the_status is not null */
  IF the_status IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_status is null');
  END IF;
  /* lock affected rows */
  OPEN lockcursor;
  /* check if the enzyme exists */
  DECLARE
    tmp_id enzymes.enzyme_id%type;
  BEGIN
    SELECT enzyme_id INTO tmp_id
      FROM enzymes
     WHERE enzyme_id = the_enzyme_id;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_enzyme_id not found');
  END;
  /* do table enzymes */
  UPDATE enzymes
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table names */
  UPDATE names
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table comments */
  UPDATE comments
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table xrefs */
  UPDATE xrefs
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  /* do table links */
  UPDATE links
     SET status = the_status
   WHERE enzyme_id = the_enzyme_id
     AND status != 'NO'
     AND status != the_status;
  CLOSE lockcursor;
END;

/

--------------------------------------------------------
--  DDL for Procedure P_CLONE_ENZYME
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_CLONE_ENZYME" (
  old_enzyme_id IN  enzymes.enzyme_id%type,
  new_enzyme_id OUT enzymes.enzyme_id%type,
  new_status    IN  enzymes.status%type DEFAULT 'SC',
  new_active    IN  enzymes.active%type DEFAULT 'N'
)
AS
  enz_a enzymes%rowtype;
BEGIN
  /* lock affected tables to be transaction save */
  LOCK TABLE enzymes   		IN EXCLUSIVE MODE;
  LOCK TABLE names     		IN EXCLUSIVE MODE;
  LOCK TABLE reactions_map	IN EXCLUSIVE MODE;
  LOCK TABLE comments  		IN EXCLUSIVE MODE;
  LOCK TABLE xrefs     		IN EXCLUSIVE MODE;
  LOCK TABLE links     		IN EXCLUSIVE MODE;

  /* get source enzyme */
  BEGIN
    SELECT *
      INTO enz_a
      FROM enzymes
     WHERE enzyme_id = old_enzyme_id;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20000, 'source enzyme not found');
  END;

  /* get new enzyme_id */
  SELECT s_enzyme_id.nextval
    INTO new_enzyme_id
    FROM DUAL;
  --DBMS_OUTPUT.PUT_LINE('got new enzyme_id='||new_enzyme_id);

  /*  clone enzyme */
  INSERT INTO enzymes
         (enzyme_id, status, active,
          ec1, ec2, ec3, ec4,
          history, note, source)
  VALUES (new_enzyme_id, new_status, new_active,
          enz_a.ec1, enz_a.ec2, enz_a.ec3, enz_a.ec4,
          enz_a.history, enz_a.note, enz_a.source);
  --DBMS_OUTPUT.PUT_LINE('Done enzymes');

  /* clone names*/
  INSERT INTO names (
         enzyme_id, status,
         name, name_class, warning, source, note, order_in, web_view)
  SELECT new_enzyme_id, new_status,
         name, name_class, warning, source, note, order_in, web_view
    FROM names
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done names');

  /* clone reactions mapping */
  INSERT INTO reactions_map (enzyme_id, reaction_id, web_view, order_in)
  SELECT new_enzyme_id, reaction_id, web_view, order_in
    FROM reactions_map
   WHERE enzyme_id = old_enzyme_id;

  /* clone abstract (free text) reactions */
  INSERT INTO reactions (enzyme_id, equation, order_in, status, source, web_view)
  SELECT new_enzyme_id, equation, order_in, status, source, web_view
    FROM reactions
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done reactions');

  /* clone comments */
  INSERT INTO comments (
         enzyme_id, status,
         comment_text, order_in, source, web_view)
  SELECT new_enzyme_id, new_status,
         comment_text, order_in, source, web_view
    FROM comments
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done comments');

  /* clone xrefs */
  INSERT INTO xrefs (
         enzyme_id, status,
         database_code, database_ac, name, source, data_comment, web_view)
  SELECT new_enzyme_id, new_status,
         database_code, database_ac, name, source, data_comment, web_view
    FROM xrefs
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done xrefs');

  /* clone links */
  INSERT INTO links (
         enzyme_id, status,
         url, display_name, source, data_comment, web_view)
  SELECT new_enzyme_id, new_status,
         url, display_name, source, data_comment, web_view
    FROM links
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done links');

  /* clone citations */
  INSERT INTO citations (
         enzyme_id, status,
         pub_id, order_in, source)
  SELECT new_enzyme_id, new_status,
         pub_id, order_in, source
    FROM citations
   WHERE enzyme_id = old_enzyme_id;
  --DBMS_OUTPUT.PUT_LINE('Done citations');

END;

/

--------------------------------------------------------
--  DDL for Procedure P_INSERT_COMPOUND_DATA
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_INSERT_COMPOUND_DATA" (
	new_id				IN OUT	compound_data.compound_id%type,
	new_name			IN		compound_data.name%type,
	new_formula			IN		compound_data.formula%type,
	new_charge			IN		compound_data.charge%type,
	new_source			IN		compound_data.source%type,
	new_accession		IN		compound_data.accession%type,
	new_child_accession	IN		compound_data.child_accession%type,
	new_published		IN		compound_data.published%type
)
AS
	existing_id		compound_data.compound_id%type;
BEGIN
	SELECT compound_id INTO existing_id
		FROM compound_data
		WHERE name = new_name OR (source = new_source AND accession = new_accession);
	IF existing_id IS NOT NULL THEN
		new_id := -existing_id;
	END IF;
	EXCEPTION
		WHEN NO_DATA_FOUND
			THEN
				IF new_id IS NULL THEN
				    SELECT s_compound_id.nextval INTO new_id FROM DUAL;
				END IF;
				INSERT INTO compound_data (compound_id, name, formula, charge, source, accession, child_accession, published)
				VALUES (new_id, new_name, new_formula, new_charge, new_source, new_accession, new_child_accession, new_published);
END;

/

--------------------------------------------------------
--  DDL for Procedure P_INSERT_REACTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_INSERT_REACTION" 
  (
    new_id               IN OUT intenz_reactions.reaction_id%type,
    new_accession        IN intenz_reactions.intenz_accession%type,
    new_equation         IN intenz_reactions.equation%type,
    new_fp               IN intenz_reactions.fingerprint%type,
    new_status           IN intenz_reactions.status%type,
    new_source           IN intenz_reactions.source%type,
    new_qualifiers       IN intenz_reactions.qualifiers%type,
    new_data_comment     IN intenz_reactions.data_comment%type,
    new_reaction_comment IN intenz_reactions.reaction_comment%type,
    new_direction        IN intenz_reactions.direction%type,
    new_un_reaction      IN intenz_reactions.un_reaction%type )
AS
  existing_id intenz_reactions.reaction_id%type;
BEGIN
   SELECT reaction_id
     INTO existing_id
     FROM intenz_reactions
    WHERE equation = new_equation
  AND fingerprint  = new_fp;
  IF existing_id  IS NOT NULL THEN
    new_id        := -existing_id;
  END IF;
EXCEPTION
WHEN NO_DATA_FOUND THEN
  IF new_id IS NULL THEN
     SELECT s_reaction_id.nextval INTO new_id FROM DUAL;
  END IF;
   INSERT
     INTO intenz_reactions
    (
      reaction_id     ,
      intenz_accession,
      equation        ,
      fingerprint     ,
      status          ,
      source          ,
      qualifiers      ,
      data_comment    ,
      reaction_comment,
      direction       ,
      un_reaction
    )
    VALUES
    (
      new_id              ,
      new_accession       ,
      new_equation        ,
      new_fp              ,
      new_status          ,
      new_source          ,
      new_qualifiers      ,
      new_data_comment    ,
      new_reaction_comment,
      new_direction       ,
      new_un_reaction
    );
END;

/

--------------------------------------------------------
--  DDL for Procedure P_MERGE_PUBLICATIONS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_MERGE_PUBLICATIONS" (
  the_pub_id_from IN publications.pub_id%type,
  the_pub_id_to   IN publications.pub_id%type
)
AS
  CURSOR cit_cur IS
  SELECT *
    FROM citations
   WHERE pub_id = the_pub_id_from
     FOR UPDATE OF pub_id;
  CURSOR lock_work IS
  SELECT 1
    FROM citations c,
         publications p
   WHERE c.pub_id = p.pub_id
     AND (p.pub_id = the_pub_id_from OR p.pub_id = the_pub_id_to)
    FOR UPDATE;
  cit_a cit_cur%ROWTYPE;      -- cursor record for old citations
  pub_a publications%ROWTYPE; -- local var for old publications
  pub_b publications%ROWTYPE; -- local var for new publications
  old_audit_remark VARCHAR2(200);
BEGIN
  /* check a != b */
  IF the_pub_id_from = the_pub_id_to THEN
    RAISE_APPLICATION_ERROR(-20000, 'Cant merge publication to itself');
  END IF;
  /* check a not null */
  IF the_pub_id_from IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_from is null');
  END IF;
  /* check b not null */
  IF the_pub_id_to IS NULL THEN
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_to is null');
  END IF;
  /* lock rows for a and b in citations and publications */
  OPEN lock_work;
  /* fetch pub a and b and check they exists*/
  BEGIN
    SELECT * INTO pub_a
      FROM publications
     WHERE pub_id = the_pub_id_from;
  EXCEPTION WHEN NO_DATA_FOUND THEN
    CLOSE lock_work;
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_from doesnt exist');
  END;
  BEGIN
    SELECT * INTO pub_b
      FROM publications
     WHERE pub_id = the_pub_id_to;
  EXCEPTION WHEN NO_DATA_FOUND THEN
    CLOSE lock_work;
    RAISE_APPLICATION_ERROR(-20000, 'the_pub_id_to doesnt exist');
  END;
  /* set audit remark (and save old remark for restoring it later) */
  old_audit_remark := auditpackage.remark;
  auditpackage.setremark('merge publication ' || the_pub_id_from ||
    ' to ' || the_pub_id_to);
  /* change citations with the_pub_id_from to the_pub_id_to, if any */
  FOR cit_a IN cit_cur LOOP
    DBMS_OUTPUT.PUT_LINE('Changing citatation of enzyme_id='||cit_a.enzyme_id||
      ' from pub_id='||cit_a.pub_id||' to '||the_pub_id_to);
    UPDATE citations
       SET pub_id = the_pub_id_to
     WHERE CURRENT OF cit_cur;
  END LOOP;
  /* delete publication the_pub_id_from */
  DELETE
    FROM publications
   WHERE pub_id = the_pub_id_from;
  /* restore auditremark as it was before calling this procedure */
  auditpackage.setremark(old_audit_remark);
  /* close locking cursor, note: rows will be unlocked by next commit */
  CLOSE lock_work;
  /* final debug remark */
  DBMS_OUTPUT.PUT_LINE('publication '|| the_pub_id_from ||
    ' merged to ' || the_pub_id_to);
END;

/

--------------------------------------------------------
--  DDL for Procedure P_PRINT_PUBLICATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_PRINT_PUBLICATION" (
  the_pub_id IN publications.pub_id%type
)
AS
  r publications%rowtype;
BEGIN
  -- fetch record
  SELECT * INTO r
    FROM publications
   WHERE pub_id = the_pub_id;
  -- RN line with pub_id
  DBMS_OUTPUT.PUT_LINE('RN   ['||r.pub_id||']');
  -- RX line with medline_id and pubmed_id
  IF (r.medline_id IS NOT NULL OR r.pubmed_id IS NOT NULL) THEN
    DBMS_OUTPUT.PUT( 'RX   ');
    IF r.medline_id IS NOT NULL THEN
      DBMS_OUTPUT.PUT('MEDLINE=' || r.medline_id || ';');
    END IF;
    IF r.pubmed_id IS NOT NULL THEN
      DBMS_OUTPUT.PUT('PubMed=' || r.pubmed_id || ';');
    END IF;
    DBMS_OUTPUT.PUT_LINE('');
  END IF;
  -- RA line with author
  DBMS_OUTPUT.PUT_LINE('RA   ' || r.author || ';');
  -- RT line with title
  DBMS_OUTPUT.PUT_LINE('RT   "' || r.title || '";');
  -- RL line
  DBMS_OUTPUT.PUT('RL   ');
  IF r.pub_type = 'J' THEN
    DBMS_OUTPUT.PUT(r.journal_book||' '||NVL(r.volume,'???')||':');
    DBMS_OUTPUT.PUT(NVL(r.first_page,'???')||'-'||NVL(r.last_page,'???'));
    DBMS_OUTPUT.PUT('('||r.pub_year||').');
  ELSE
    DBMS_OUTPUT.PUT('*** NYI pub_type '||r.pub_type);
  END IF;
  DBMS_OUTPUT.PUT_LINE('');
END p_print_publication;

/

--------------------------------------------------------
--  DDL for Procedure P_RESTART_TIMEOUT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_RESTART_TIMEOUT" (
  the_enzyme_id IN enzymes.enzyme_id%type
  )
AS
BEGIN
  UPDATE timeouts
     SET start_date = sysdate,
         due_date   = add_months(sysdate,2)
   WHERE enzyme_id = the_enzyme_id;
END;

/

--------------------------------------------------------
--  DDL for Procedure P_START_TIMEOUT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_START_TIMEOUT" (
  the_enzyme_id IN enzymes.enzyme_id%type
  )
AS
BEGIN
  INSERT INTO timeouts (enzyme_id, start_date, due_date)
  VALUES (the_enzyme_id, sysdate, add_months(sysdate, 2));
END;

/

--------------------------------------------------------
--  DDL for Procedure P_STOP_TIMEOUT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_STOP_TIMEOUT" (
  the_enzyme_id IN enzymes.enzyme_id%type
  )
AS
BEGIN
  DELETE
    FROM timeouts
   WHERE enzyme_id = the_enzyme_id;
END;

/

--------------------------------------------------------
--  DDL for Procedure P_STRING2QUAD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "P_STRING2QUAD" (
  ec IN CHAR,
  ec1 OUT NUMBER,
  ec2 OUT NUMBER,
  ec3 OUT NUMBER,
  ec4 OUT NUMBER)
IS
  pos INTEGER;
  s VARCHAR2(100) := ec; -- local working copy of ec string
  p VARCHAR2(10);
BEGIN
  s := trim(s);

  -- just to be graceful, ignore any 'EC ' prefix
  IF (SUBSTR(s, 1, 2) = 'EC') THEN
    s := SUBSTR(s, 3);
    s := TRIM(s);
    --DBMS_OUTPUT.PUT_LINE('s='||s);
  END IF;

  -- ec1 vulgo class
  pos := INSTR(s, '.', 1);
  IF pos=0 THEN
    IF s != '-' THEN ec1 := TO_NUMBER(s); END IF;
    RETURN;
  ELSE
    p := SUBSTR(s, 1, pos-1);  s := SUBSTR(s, pos+1);
    --DBMS_OUTPUT.PUT_LINE('s1='||p||',s='||s);
    IF p != '-' THEN ec1 := TO_NUMBER(p); END IF;
  END IF;

  -- ec2 vulgo subclass
  pos := INSTR(s, '.', 1);
  IF pos=0 THEN
    IF s != '-' THEN ec2 := TO_NUMBER(s); END IF;
    RETURN;
  ELSE
    p := SUBSTR(s, 1, pos-1);  s := SUBSTR(s, pos+1);
    --DBMS_OUTPUT.PUT_LINE('s2='||p||',s='||s);
    IF p != '-' THEN ec2 := TO_NUMBER(p); END IF;
  END IF;

  -- ec3 vulgo subsubclass
  pos := INSTR(s, '.', 1);
  IF pos=0 THEN
    IF s != '-' THEN ec3 := TO_NUMBER(s); END IF;
    RETURN;
  ELSE
    p := SUBSTR(s, 1, pos-1);  s := SUBSTR(s, pos+1);
    --DBMS_OUTPUT.PUT_LINE('s3='||p||',s='||s);
    IF p != '-' THEN ec3 := TO_NUMBER(p); END IF;
  END IF;

  -- ec4 vulgo forth number
  IF s != '-' THEN ec4 := TO_NUMBER(s); END IF;
END;

/

