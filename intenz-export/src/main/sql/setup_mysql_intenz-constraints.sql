-- CHECK constraints are ignored silently by MySQL.

-- -----------------------------------------------------
--  Constraints for Table CLASSES
-- -----------------------------------------------------

ALTER TABLE classes ADD CONSTRAINT PK_CLASSES PRIMARY KEY (ec1);
ALTER TABLE classes ADD CONSTRAINT CK_CLASSES$NAME_WS
	CHECK (name = TRIM(TRIM(CHAR(9) FROM TRIM(CHAR(10) FROM TRIM(CHAR(13) FROM name)))));
ALTER TABLE classes ADD CONSTRAINT CK_CLASSES$ACTIVE CHECK (active IN ('Y','N'));
ALTER TABLE classes ADD CONSTRAINT CK_CLASSES$EC1_ZERO CHECK (ec1 > 0);

-- -----------------------------------------------------
--  Constraints for Table COMMENTS
-- -----------------------------------------------------

-- ALTER TABLE comments ADD CONSTRAINT PK_COMMENTS PRIMARY KEY (enzyme_id, comment_text);
ALTER TABLE comments ADD KEY K_COMMENTS (enzyme_id, comment_text);
ALTER TABLE comments ADD CONSTRAINT UQ_COMMENTS$ID$ORDER_IN UNIQUE (enzyme_id, order_in);
ALTER TABLE comments ADD CONSTRAINT CK_COMMENTS$TEXT_DOTS CHECK (comment_text LIKE '%.');

-- -----------------------------------------------------
--  Constraints for Table CITATIONS
-- -----------------------------------------------------

ALTER TABLE citations ADD CONSTRAINT PK_CITATIONS PRIMARY KEY (enzyme_id, pub_id);
ALTER TABLE citations ADD CONSTRAINT UQ_CITATIONS$ENZID$ORDER_IN UNIQUE (enzyme_id, order_in);
ALTER TABLE citations ADD CONSTRAINT CK_CITATIONS$ORDER_IN$ZERO CHECK (order_in > 0);

-- -----------------------------------------------------
--  Constraints for Table SUBSUBCLASSES
-- -----------------------------------------------------

ALTER TABLE subsubclasses ADD CONSTRAINT PK_SUBSUBCLASSES PRIMARY KEY (ec1, ec2, ec3);
ALTER TABLE subsubclasses ADD CONSTRAINT CK_SUBSUBCLASSES$ACTIVE CHECK (active IN ('Y','N'));
ALTER TABLE subsubclasses ADD CONSTRAINT CK_SUBSUBCLASSES$EC3_ZERO CHECK (ec3 > 0);
ALTER TABLE subsubclasses ADD CONSTRAINT CK_SUBSUBCLASSES$NAME_WS
	CHECK (name = TRIM(TRIM(CHAR(9) FROM TRIM(CHAR(10) FROM TRIM(CHAR(13) FROM name)))));

-- -----------------------------------------------------
--  Constraints for Table CV_COEFF_TYPES
-- -----------------------------------------------------

ALTER TABLE cv_coeff_types ADD PRIMARY KEY (code);

-- -----------------------------------------------------
--  Constraints for Table XREFS
-- -----------------------------------------------------

-- ALTER TABLE xrefs ADD CONSTRAINT PK_XREFS PRIMARY KEY (enzyme_id, database_ac, database_code);
ALTER TABLE xrefs ADD KEY K_XREFS (enzyme_id);

-- -----------------------------------------------------
--  Constraints for Table SUBCLASSES
-- -----------------------------------------------------

ALTER TABLE subclasses ADD CONSTRAINT PK_SUBCLASSES PRIMARY KEY (ec1, ec2);
ALTER TABLE subclasses ADD CONSTRAINT CK_SUBCLASSES$ACTIVE CHECK (active IN ('Y','N'));
ALTER TABLE subclasses ADD CONSTRAINT CK_SUBCLASSES$EC2_ZERO CHECK (ec2 > 0);
ALTER TABLE subclasses ADD CONSTRAINT CK_SUBCLASSES$NAME_WS
	CHECK (name = TRIM(TRIM(CHAR(9) FROM TRIM(CHAR(10) FROM TRIM(CHAR(13) FROM name)))));

-- -----------------------------------------------------
--  Constraints for Table CV_REACTION_DIRECTIONS
-- -----------------------------------------------------

ALTER TABLE cv_reaction_directions ADD PRIMARY KEY (code);
ALTER TABLE cv_reaction_directions ADD CONSTRAINT UQ_CV_REACTION_DIRECTIONS$D UNIQUE (direction);

-- -----------------------------------------------------
--  Constraints for Table CV_WARNINGS
-- -----------------------------------------------------

ALTER TABLE cv_warnings ADD CONSTRAINT PK_CV_WARNINGS PRIMARY KEY (code);
ALTER TABLE cv_warnings ADD CONSTRAINT UQ_CV_WARNINGS$DISPLAY_NAME UNIQUE (display_name);
ALTER TABLE cv_warnings ADD CONSTRAINT UQ_CV_WARNINGS$SORT_ORDER UNIQUE (sort_order);
ALTER TABLE cv_warnings ADD CONSTRAINT CK_CV_WARNINGS$CODE CHECK (code = UPPER(code));

-- -----------------------------------------------------
--  Constraints for Table LINKS
-- -----------------------------------------------------

ALTER TABLE links ADD CONSTRAINT PK_LINKS PRIMARY KEY (enzyme_id, url);

-- -----------------------------------------------------
--  Constraints for Table CV_OPERATORS
-- -----------------------------------------------------

ALTER TABLE cv_operators ADD PRIMARY KEY (code);

-- -----------------------------------------------------
--  Constraints for Table CV_REACTION_SIDES
-- -----------------------------------------------------

ALTER TABLE cv_reaction_sides ADD PRIMARY KEY (code);
ALTER TABLE cv_reaction_sides ADD CONSTRAINT CV_REACTION_SIDES$S UNIQUE (side);

-- -----------------------------------------------------
--  Constraints for Table FUTURE_EVENTS
-- -----------------------------------------------------

ALTER TABLE future_events ADD CONSTRAINT PK_FUTURE_EVENTS PRIMARY KEY (group_id, event_id);
ALTER TABLE future_events ADD CONSTRAINT UQ_FUTURE_EVENTS$BEF$AFT UNIQUE (before_id, after_id);
ALTER TABLE future_events ADD CONSTRAINT FUTURE_EVENTS$CYCLE0 CHECK (before_id != after_id);

-- -----------------------------------------------------
--  Constraints for Table NAMES
-- -----------------------------------------------------

-- ALTER TABLE names ADD CONSTRAINT PK_NAMES PRIMARY KEY (enzyme_id, name_class, name);
ALTER TABLE names ADD KEY K_NAMES (enzyme_id, name_class, name);
ALTER TABLE names ADD CONSTRAINT UQ_NAMES$ENZID$CLASS$ORDER_IN UNIQUE (enzyme_id, name_class, order_in);
ALTER TABLE names ADD CONSTRAINT CK_NAMES$NAME_TRIM CHECK (TRIM(BOTH FROM name) = name);
ALTER TABLE names ADD CONSTRAINT CK_NAMES$NAME_WS
	CHECK (name NOT LIKE '%.' AND name NOT LIKE '%?' AND name NOT LIKE '%!');
ALTER TABLE names ADD CONSTRAINT CK_NAMES$ORDER_IN$ZERO CHECK (order_in > 0);
ALTER TABLE names ADD CONSTRAINT CK_NAMES$NAME_CRLF
	CHECK (name = REPLACE(name, CONCAT(CHAR(9), CHAR(10), CHAR(13)), ''));

-- -----------------------------------------------------
--  Constraints for Table CV_VIEW
-- -----------------------------------------------------

ALTER TABLE cv_view ADD CONSTRAINT PK_CV_VIEW PRIMARY KEY (code);
ALTER TABLE cv_view ADD CONSTRAINT CK_CV_VIEW$CODE CHECK (code = UPPER(code));

-- -----------------------------------------------------
--  Constraints for Table CV_DATABASES
-- -----------------------------------------------------

ALTER TABLE cv_databases ADD CONSTRAINT PK_CV_DATABASES PRIMARY KEY (code);
ALTER TABLE cv_databases ADD CONSTRAINT UQ_CV_DATABASES$DISPLAY_NAME UNIQUE (display_name);
ALTER TABLE cv_databases ADD CONSTRAINT UQ_CV_DATABASES$NAME UNIQUE (name);
ALTER TABLE cv_databases ADD CONSTRAINT UQ_CV_DATABASES$SORT_ORDER UNIQUE (sort_order);
ALTER TABLE cv_databases ADD CONSTRAINT CK_CV_DATABASES$CODE CHECK (code = UPPER(code));

-- -----------------------------------------------------
--  Constraints for Table REACTION_PARTICIPANTS
-- -----------------------------------------------------

ALTER TABLE reaction_participants ADD CONSTRAINT CK_REACTION_PARTICIPANTS$COEF CHECK (coefficient > 0);

-- -----------------------------------------------------
--  Constraints for Table CV_COMP_PUB_AVAIL
-- -----------------------------------------------------

ALTER TABLE cv_comp_pub_avail ADD PRIMARY KEY (code);

-- -----------------------------------------------------
--  Constraints for Table COFACTORS
-- -----------------------------------------------------

-- ALTER TABLE cofactors ADD CONSTRAINT PK_COFACTORS PRIMARY KEY (enzyme_id, cofactor_text);
ALTER TABLE cofactors ADD CONSTRAINT PK_COFACTORS PRIMARY KEY (compound_id, enzyme_id);

-- -----------------------------------------------------
--  Constraints for Table ENZYMES
-- -----------------------------------------------------

ALTER TABLE enzymes ADD CONSTRAINT PK_ENZYMES PRIMARY KEY (enzyme_id);
ALTER TABLE enzymes ADD CONSTRAINT UQ_ENZYMES$SOURCE$ID UNIQUE (source, enzyme_id);
ALTER TABLE enzymes ADD CONSTRAINT CK_ENZYMES$EC_QUAD_NULL
	CHECK ((ec1 IS NOT NULL AND ec2 IS NOT NULL and ec3 IS NOT NULL and ec4 IS NOT NULL)
	OR (ec1 IS NOT NULL AND ec2 IS NOT NULL and ec3 IS NOT NULL and ec4 IS NULL)
	OR (ec1 IS NOT NULL AND ec2 IS NOT NULL and ec3 IS NULL and ec4 IS NULL)
	OR (ec1 IS NOT NULL AND ec2 IS NULL and ec3 IS NULL and ec4 IS NULL)
	OR (ec1 IS NULL AND ec2 IS NULL and ec3 IS NULL and ec4 IS NULL));
ALTER TABLE enzymes ADD CONSTRAINT CK_ENZYMES$ACTIVE CHECK (active IN ('Y','N'));
ALTER TABLE enzymes ADD CONSTRAINT CK_ENZYMES$EC4_ZERO CHECK (ec4 > 0);

-- -----------------------------------------------------
--  Constraints for Table REACTION_CITATIONS
-- -----------------------------------------------------

ALTER TABLE reaction_citations ADD CONSTRAINT PK_REACTION_CITATIONS
	PRIMARY KEY (reaction_id, pub_id, source);

-- -----------------------------------------------------
--  Constraints for Table CV_STATUS
-- -----------------------------------------------------

ALTER TABLE cv_status ADD CONSTRAINT PK_CV_STATUS PRIMARY KEY (code);
ALTER TABLE cv_status ADD CONSTRAINT UQ_CV_STATUS$DISPLAY_NAME UNIQUE (display_name);
ALTER TABLE cv_status ADD CONSTRAINT UQ_CV_STATUS$NAME UNIQUE (name);
ALTER TABLE cv_status ADD CONSTRAINT UQ_CV_STATUS$SORT_ORDER UNIQUE (sort_order);
ALTER TABLE cv_status ADD CONSTRAINT CK_CV_STATUS$CODE CHECK (code = UPPER(code));

-- -----------------------------------------------------
--  Constraints for Table INTENZ_REACTIONS
-- -----------------------------------------------------

ALTER TABLE intenz_reactions ADD PRIMARY KEY (reaction_id);
-- ALTER TABLE intenz_reactions ADD CONSTRAINT UQ_INTENZ_REACTIONS$FP UNIQUE (fingerprint);

-- -----------------------------------------------------
--  Constraints for Table REACTIONS
-- -----------------------------------------------------

-- ALTER TABLE reactions ADD CONSTRAINT PK_REACTIONS PRIMARY KEY (enzyme_id, equation);
ALTER TABLE reactions ADD KEY K_REACTIONS (enzyme_id, equation);
ALTER TABLE reactions ADD CONSTRAINT UQ_REACTIONS$ID$ORDER_IN UNIQUE (enzyme_id, order_in);

-- -----------------------------------------------------
--  Constraints for Table REACTIONS_MAP
-- -----------------------------------------------------

ALTER TABLE reactions_map ADD CONSTRAINT PK_REACTIONS_MAP PRIMARY KEY (reaction_id, enzyme_id);
ALTER TABLE reactions_map ADD CONSTRAINT UQ_REACTIONS_MAP$ID$VIEW$ORDER
	UNIQUE (enzyme_id, web_view, order_in);
ALTER TABLE reactions_map ADD CONSTRAINT CK_REACTIONS_MAP$ORDER CHECK (order_in > 0);

-- -----------------------------------------------------
--  Constraints for Table COMPLEX_REACTIONS
-- -----------------------------------------------------

ALTER TABLE complex_reactions ADD CONSTRAINT PK_COMPLEX_REACTIONS PRIMARY KEY (parent_id, child_id);
ALTER TABLE complex_reactions ADD CONSTRAINT CK_COMPLEX_REACTIONS$COEF CHECK (coefficient > 0);
ALTER TABLE complex_reactions ADD CONSTRAINT CK_COMPLEX_REACTIONS$ORD CHECK (order_in >= 0);
ALTER TABLE complex_reactions ADD CONSTRAINT CK_COMPLEX_REACTIONS$SAMEID CHECK (parent_id != child_id);

-- -----------------------------------------------------
--  Constraints for Table COMPOUND_DATA
-- -----------------------------------------------------

ALTER TABLE compound_data ADD PRIMARY KEY (compound_id);
-- ALTER TABLE compound_data ADD UNIQUE (name);
-- ALTER TABLE compound_data ADD CONSTRAINT UQ_COMPOUND_DATA$SA UNIQUE (source, accession);
ALTER TABLE compound_data ADD CONSTRAINT CK_COMPOUND_DATA$SRC
	CHECK ((source IS NULL AND accession IS NULL)
	OR (source IS NOT NULL AND accession IS NOT NULL));
ALTER TABLE compound_data ADD CONSTRAINT CK_COMPOUND_DATA$PUB
	CHECK ((accession IS NULL AND child_accession IS NULL AND published IS NULL)
	OR (accession IS NOT NULL AND child_accession IS NULL AND published IN ('N','P'))
	OR (accession IS NOT NULL AND child_accession IS NOT NULL AND published IN ('N','P','C')));

-- -----------------------------------------------------
--  Constraints for Table REACTION_XREFS
-- -----------------------------------------------------

-- ALTER TABLE reaction_xrefs ADD CONSTRAINT PK_REACTION_XREFS
-- 	PRIMARY KEY (db_code, db_accession, reaction_id);

-- -----------------------------------------------------
--  Constraints for Table TIMEOUTS
-- -----------------------------------------------------

ALTER TABLE timeouts ADD CONSTRAINT PK_TIMEOUTS PRIMARY KEY (timeout_id);

-- -----------------------------------------------------
--  Constraints for Table HISTORY_EVENTS
-- -----------------------------------------------------

ALTER TABLE history_events ADD CONSTRAINT PK_HISTORY_EVENTS PRIMARY KEY (group_id, event_id);
ALTER TABLE history_events ADD CONSTRAINT UQ_HISTORY_EVENTS$BEF$AFT UNIQUE (before_id, after_id);
ALTER TABLE history_events ADD CONSTRAINT CK_HISTORY_EVENTS$CYCLE0 CHECK (before_id != after_id);

-- -----------------------------------------------------
--  Constraints for Table CV_REACTION_QUALIFIERS
-- -----------------------------------------------------

ALTER TABLE cv_reaction_qualifiers ADD PRIMARY KEY (code);
ALTER TABLE cv_reaction_qualifiers ADD CONSTRAINT UQ_CV_REACTION_QUALIFIERS$Q UNIQUE (qualifier);

-- -----------------------------------------------------
--  Constraints for Table CV_NAME_CLASSES
-- -----------------------------------------------------

ALTER TABLE cv_name_classes ADD CONSTRAINT PK_CV_NAME_CLASSES PRIMARY KEY (code);
ALTER TABLE cv_name_classes ADD CONSTRAINT UQ_CV_NAME_CLASSES$NAME UNIQUE (name);
ALTER TABLE cv_name_classes ADD CONSTRAINT UQ_CV_NAME_CLASSES$SORT_ORDER UNIQUE (sort_order);
ALTER TABLE cv_name_classes ADD CONSTRAINT CK_CV_NAME_CLASSES$CODE CHECK (code = UPPER(code));

-- -----------------------------------------------------
--  Constraints for Table PUBLICATIONS
-- -----------------------------------------------------

ALTER TABLE publications ADD CONSTRAINT PK_PUBLICATIONS PRIMARY KEY (pub_id);
ALTER TABLE publications ADD CONSTRAINT CK_PUBLICATIONS$PUB_TYPE CHECK (pub_type IN ('J', 'B', 'P'));

-- -----------------------------------------------------
--  Ref Constraints for Table CITATIONS
-- -----------------------------------------------------

ALTER TABLE citations ADD CONSTRAINT FK_CITATIONS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);

-- -----------------------------------------------------
--  Ref Constraints for Table COFACTORS
-- -----------------------------------------------------

ALTER TABLE cofactors ADD CONSTRAINT FK_COFACTORS$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE cofactors ADD CONSTRAINT FK_COFACTORS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE cofactors ADD CONSTRAINT FK_COFACTORS$OPERATOR
	FOREIGN KEY (operator) REFERENCES cv_operators (code);
ALTER TABLE cofactors ADD CONSTRAINT FK_COFACTORS$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;
ALTER TABLE cofactors ADD CONSTRAINT FK_COFACTORS$COMPOUND_ID
	FOREIGN KEY (compound_id) REFERENCES compound_data (compound_id);

-- -----------------------------------------------------
--  Ref Constraints for Table COMMENTS
-- -----------------------------------------------------

ALTER TABLE comments ADD CONSTRAINT FK_COMMENTS$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE comments ADD CONSTRAINT FK_COMMENTS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE comments ADD CONSTRAINT FK_COMMENTS$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table COMPLEX_REACTIONS
-- -----------------------------------------------------

ALTER TABLE complex_reactions ADD CONSTRAINT FK_COMPLEX_REACTIONS$PARENT
	FOREIGN KEY (parent_id) REFERENCES intenz_reactions (reaction_id) ON DELETE CASCADE;
ALTER TABLE complex_reactions ADD CONSTRAINT FK_COMPLEX_REACTIONS$CHILD
	FOREIGN KEY (child_id) REFERENCES intenz_reactions (reaction_id);

-- -----------------------------------------------------
--  Ref Constraints for Table COMPOUND_DATA
-- -----------------------------------------------------

ALTER TABLE compound_data ADD CONSTRAINT FK_COMPOUND_DATA$SOURCE
	FOREIGN KEY (source) REFERENCES cv_databases (code);
ALTER TABLE compound_data ADD CONSTRAINT FK_COMPOUND_DATA$PUB
	FOREIGN KEY (published) REFERENCES cv_comp_pub_avail (code);

-- -----------------------------------------------------
--  Ref Constraints for Table ENZYMES
-- -----------------------------------------------------

ALTER TABLE enzymes ADD CONSTRAINT FK_ENZYMES$EC1$EC2$EC3
	FOREIGN KEY (ec1, ec2, ec3) REFERENCES subsubclasses (ec1, ec2, ec3);
ALTER TABLE enzymes ADD CONSTRAINT FK_ENZYMES$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE enzymes ADD CONSTRAINT FK_ENZYMES$SOURCE
	FOREIGN KEY (source) REFERENCES cv_databases (code);

-- -----------------------------------------------------
--  Ref Constraints for Table FUTURE_EVENTS
-- -----------------------------------------------------

ALTER TABLE future_events ADD CONSTRAINT FK_FUTURE_EVENTS$TIMEOUT_ID
	FOREIGN KEY (timeout_id) REFERENCES timeouts (timeout_id);
ALTER TABLE future_events ADD CONSTRAINT FK_FUTURE_EVENTS$BEFORE_ID
	FOREIGN KEY (before_id) REFERENCES enzymes (enzyme_id);
ALTER TABLE future_events ADD CONSTRAINT FK_FUTURE_EVENTS$AFTER_ID
	FOREIGN KEY (after_id) REFERENCES enzymes (enzyme_id);

-- -----------------------------------------------------
--  Ref Constraints for Table HISTORY_EVENTS
-- -----------------------------------------------------

ALTER TABLE history_events ADD CONSTRAINT FK_HISTORY_EVENTS$BEFORE_ID
	FOREIGN KEY (before_id) REFERENCES enzymes (enzyme_id);
ALTER TABLE history_events ADD CONSTRAINT FK_HISTORY_EVENTS$AFTER_ID
	FOREIGN KEY (after_id) REFERENCES enzymes (enzyme_id);

-- -----------------------------------------------------
--  Ref Constraints for Table INTENZ_REACTIONS
-- -----------------------------------------------------

ALTER TABLE intenz_reactions ADD CONSTRAINT FK_INTENZ_REACTIONS$UN
	FOREIGN KEY (un_reaction) REFERENCES intenz_reactions (reaction_id);
ALTER TABLE intenz_reactions ADD CONSTRAINT FK_INTENZ_REACTIONS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE intenz_reactions ADD CONSTRAINT FK_INTENZ_REACTIONS$SOURCE
	FOREIGN KEY (source) REFERENCES cv_databases (code);
ALTER TABLE intenz_reactions ADD CONSTRAINT FK_INTENZ_REACTIONS$DIRECTION
	FOREIGN KEY (direction) REFERENCES cv_reaction_directions (code);

-- -----------------------------------------------------
--  Ref Constraints for Table LINKS
-- -----------------------------------------------------

ALTER TABLE links ADD CONSTRAINT FK_LINKS$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE links ADD CONSTRAINT FK_LINKS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE links ADD CONSTRAINT FK_LINKS$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table NAMES
-- -----------------------------------------------------

ALTER TABLE names ADD CONSTRAINT FK_NAMES$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE names ADD CONSTRAINT FK_NAMES$WARNING
	FOREIGN KEY (warning) REFERENCES cv_warnings (code);
ALTER TABLE names ADD CONSTRAINT FK_NAMES$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE names ADD CONSTRAINT FK_NAMES$NAME_CLASS
	FOREIGN KEY (name_class) REFERENCES cv_name_classes (code);
ALTER TABLE names ADD CONSTRAINT FK_NAMES$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table PUBLICATIONS
-- -----------------------------------------------------

ALTER TABLE publications ADD CONSTRAINT FK_PUBLICATIONS$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE publications ADD CONSTRAINT FK_PUBLICATIONS$SOURCE
	FOREIGN KEY (source) REFERENCES cv_databases (code);

-- -----------------------------------------------------
--  Ref Constraints for Table REACTIONS
-- -----------------------------------------------------

ALTER TABLE reactions ADD CONSTRAINT FK_REACTIONS$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE reactions ADD CONSTRAINT FK_REACTIONS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE reactions ADD CONSTRAINT FK_REACTIONS$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table REACTIONS_MAP
-- -----------------------------------------------------

ALTER TABLE reactions_map ADD CONSTRAINT FK_REACTIONS_MAP$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE reactions_map ADD CONSTRAINT FK_REACTIONS_MAP$REACTION_ID
	FOREIGN KEY (reaction_id) REFERENCES intenz_reactions (reaction_id) ON DELETE CASCADE;
ALTER TABLE reactions_map ADD CONSTRAINT FK_REACTIONS_MAP$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table REACTION_CITATIONS
-- -----------------------------------------------------

ALTER TABLE reaction_citations ADD CONSTRAINT FK_REACTION_CITATIONS$SO
	FOREIGN KEY (source) REFERENCES cv_databases (code);
ALTER TABLE reaction_citations ADD CONSTRAINT FK_REACTION_CITATIONS$REAC
	FOREIGN KEY (reaction_id) REFERENCES intenz_reactions (reaction_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table REACTION_PARTICIPANTS
-- -----------------------------------------------------

ALTER TABLE reaction_participants ADD CONSTRAINT FK_REACTION_PARTICIPANTS$SIDE
	FOREIGN KEY (side) REFERENCES cv_reaction_sides (code);
ALTER TABLE reaction_participants ADD CONSTRAINT FK_REACTION_PARTICIPANTS$REAC
	FOREIGN KEY (reaction_id) REFERENCES intenz_reactions (reaction_id) ON DELETE CASCADE;
ALTER TABLE reaction_participants ADD CONSTRAINT FK_REACTION_PARTICIPANTS$CT
	FOREIGN KEY (coeff_type) REFERENCES cv_coeff_types (code);
ALTER TABLE reaction_participants ADD CONSTRAINT FK_REACTION_PARTICIPANTS$COMP
	FOREIGN KEY (compound_id) REFERENCES compound_data (compound_id);

-- -----------------------------------------------------
--  Ref Constraints for Table REACTION_XREFS
-- -----------------------------------------------------

ALTER TABLE reaction_xrefs ADD CONSTRAINT FK_REACTION_XREFS$RID
	FOREIGN KEY (reaction_id) REFERENCES intenz_reactions (reaction_id) ON DELETE CASCADE;
ALTER TABLE reaction_xrefs ADD CONSTRAINT FK_REACTION_XREFS$DBC
	FOREIGN KEY (db_code) REFERENCES cv_databases (code);

-- -----------------------------------------------------
--  Ref Constraints for Table SUBCLASSES
-- -----------------------------------------------------

ALTER TABLE subclasses ADD CONSTRAINT FK_SUBCLASSES$EC1
	FOREIGN KEY (ec1) REFERENCES classes (ec1);

-- -----------------------------------------------------
--  Ref Constraints for Table SUBSUBCLASSES
-- -----------------------------------------------------

ALTER TABLE subsubclasses ADD CONSTRAINT FK_SUBSUBCLASSES$EC1$EC2
	FOREIGN KEY (ec1, ec2) REFERENCES subclasses (ec1, ec2);

-- -----------------------------------------------------
--  Ref Constraints for Table TIMEOUTS
-- -----------------------------------------------------

ALTER TABLE timeouts ADD CONSTRAINT FK_TIMEOUTS$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;

-- -----------------------------------------------------
--  Ref Constraints for Table XREFS
-- -----------------------------------------------------

ALTER TABLE xrefs ADD CONSTRAINT FK_XREFS$WEB_VIEW
	FOREIGN KEY (web_view) REFERENCES cv_view (code);
ALTER TABLE xrefs ADD CONSTRAINT FK_XREFS$STATUS
	FOREIGN KEY (status) REFERENCES cv_status (code);
ALTER TABLE xrefs ADD CONSTRAINT FK_XREFS$ENZYME_ID
	FOREIGN KEY (enzyme_id) REFERENCES enzymes (enzyme_id) ON DELETE CASCADE;
ALTER TABLE xrefs ADD CONSTRAINT FK_XREFS$DATABASE_CODE
	FOREIGN KEY (database_code) REFERENCES cv_databases (code);
