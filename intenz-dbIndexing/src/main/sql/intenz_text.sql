/*
  Drop old indexes, settings and preferences.
*/
DROP INDEX "intenz_text_idx$ec";
DROP INDEX "intenz_text_idx$text";
EXECUTE ctx_ddl.drop_preference('enzyme_storage');
EXECUTE ctx_ddl.drop_preference('enzyme_lexer');
EXECUTE ctx_ddl.drop_stoplist('enzyme_stoplist');
EXECUTE ctx_ddl.drop_section_group('intenz_text_sg');

/*
  The benefit of an XML document is that the tags can be used for sectioning, that is allowing
  the user to search within particular fields using the WITHIN operator 
  (e.g. 'SELECT ... 'Degtyarenko' WITHIN 'authors').
  For this purpose a basic section group has been created, which parses the document w/o using a DTD.
  Also the sections have to be predefined, that is how should the tags be named in a query by the user.
  Usually the same name is used.
*/
EXECUTE ctx_ddl.create_section_group('intenz_text_sg','basic_section_group');

EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','ec','ec');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','name','common_name');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','description','description');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','reaction','reaction');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','cofactor','cofactor');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','synonyms','synonym');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','systematic','syst_name');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','comment','comments');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','links','link');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','references','reference');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','authors','authors');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','title','title');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','patentno','patent_no');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','editors','editor');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','publisher','pub_company');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','pubmed','pub_med');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','deleted','deleted_text');
EXECUTE ctx_ddl.add_zone_section('intenz_text_sg','active','active');


/*
  Lexer attributes.
*/
EXECUTE ctx_ddl.create_preference('enzyme_lexer', 'BASIC_LEXER');
EXECUTE ctx_ddl.set_attribute('enzyme_lexer', 'PRINTJOINS', '_''-/:()&#;,[]{}');
EXECUTE ctx_ddl.set_attribute('enzyme_lexer', 'STARTJOINS', '<');
EXECUTE ctx_ddl.set_attribute('enzyme_lexer', 'PUNCTUATIONS', ',');
EXECUTE ctx_ddl.set_attribute('enzyme_lexer', 'ENDJOINS', '>');

/*
  Stoplist population.
*/
EXECUTE ctx_ddl.create_stoplist( 'enzyme_stoplist' );

EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'Corp' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'Mr' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'Mrs' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'Ms' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'Mz' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'a' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'about' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'after' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'all' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'also' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'an' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'and' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'any' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'are' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'as' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'at' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'be' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'because' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'been' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'but' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'by' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'can' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'co' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'could' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'for' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'from' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'had' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'has' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'have' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'he' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'her' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'his' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'if' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'in' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'inc' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'into' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'is' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'it' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'its' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'last' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'more' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'most' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'no' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'not' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'of' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'on' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'one' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'only' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'or' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'other' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'out' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'over' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 's' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'says' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'she' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'so' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'some' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'such' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'than' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'that' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'the' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'their' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'there' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'they' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'this' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'to' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'up' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'was' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'we' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'were' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'when' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'which' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'who' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'will' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'with' );
EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'would' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'b' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'c' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'd' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'e' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'f' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'g' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'h' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'i' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'j' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'k' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'l' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'm' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'n' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'o' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'p' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'q' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'r' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 't' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'u' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'v' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'w' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'x' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'y' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', 'z' );
--EXECUTE ctx_ddl.add_stopword( 'enzyme_stoplist', '-' );

--EXECUTE ctx_ddl.add_stopclass( 'enzyme_stoplist', 'NUMBERS' );

/* 
  The storage has to be defined, otherwise the index will be written to the SYSTEM table space.
*/
EXECUTE ctx_ddl.create_preference('enzyme_storage', 'BASIC_STORAGE');
EXECUTE ctx_ddl.set_attribute('enzyme_storage', 'I_TABLE_CLAUSE', 'TABLESPACE enzym_ind');
EXECUTE ctx_ddl.set_attribute('enzyme_storage', 'K_TABLE_CLAUSE', 'TABLESPACE enzym_ind');
EXECUTE ctx_ddl.set_attribute('enzyme_storage', 'R_TABLE_CLAUSE', 'TABLESPACE enzym_ind LOB (DATA) STORE AS (CACHE)');
EXECUTE ctx_ddl.set_attribute('enzyme_storage', 'N_TABLE_CLAUSE', 'TABLESPACE enzym_ind');
EXECUTE ctx_ddl.set_attribute('enzyme_storage', 'I_INDEX_CLAUSE', 'TABLESPACE enzym_ind COMPRESS 2');
EXECUTE ctx_ddl.set_attribute('enzyme_storage', 'P_TABLE_CLAUSE', 'TABLESPACE enzym_ind');


/*
  The indexes can be created using the following statements.
*/
CREATE INDEX "intenz_text_idx$ec" ON intenz_text(ec);

CREATE INDEX "intenz_text_idx$text" ON enzyme.intenz_text(text)
INDEXTYPE IS ctxsys.context
PARAMETERS( 'section group intenz_text_sg storage enzyme_storage lexer enzyme_lexer stoplist enzyme_stoplist memory 20M');
