/****************************************************************************

  IntEnz EXPORT GENERIC DUMP

  This file exports your data in generic SQL table dumps.
  @param &1 This is the directory you would like the data exported to.

  @see FUNCTION dateformatter

  Software Requirements:
  Oracle 9 and higher

  Please contact: rafalcan@ebi.ac.uk if you have any problems.
  Date Created: November 2005
 ***************************************************************************/

/* Converts empty values (null numbers) into the NULL keyword */
create or replace function is_null_int (x number) return varchar2 is
begin
    if (x is null)
    then
        return 'NULL';
    end if;
    return x;
end;
/

/* Converts empty values (null strings) into the NULL keyword */
create or replace function is_null_str (x varchar2) return varchar2 is
begin
    if (x is null)
    then
        return 'NULL';
    end if;
    return ''''||TRIM(x)||'''';
end;
/

/* Retrieves qualifiers for a given reaction ID, as comma-delimited string. */
create or replace function qual_list
(ri IN number)
return VARCHAR2 as
  qq reaction_qualifiers;
  str VARCHAR2(30);
begin
  select qualifiers INTO qq from intenz_reactions where reaction_id = ri;
  if (qq is null)
  then
  	return 'NULL';
  end if;
  str := '(''';
  FOR i in qq.first .. qq.last LOOP
    if i > 1 then str := str||','; end IF;
    str := str||qq(i);
  END LOOP;
  str := str||''')';
  return str;
end;
/

/* Retrieves comments on a table. */
create or replace function f_table_comment
(t_name IN varchar2) return VARCHAR2
as
  all_comments VARCHAR2(4000);
begin
  SELECT ''''||replace(comments,'''','''''')||''''||CHR(10)
    into all_comments
    from all_tab_comments
    where table_name = upper(t_name) and owner = 'ENZYME'
    ;
  return all_comments;  
end;
/

/* Retrieves comments on a column. */
create or replace function f_column_comment
(t_name IN varchar2, c_name IN VARCHAR2) return VARCHAR2
as
  all_comments VARCHAR2(4000);
begin
  SELECT ''''||replace(comments,'''','''''')||''''||CHR(10)
    into all_comments
    FROM All_Col_Comments
    WHERE TABLE_NAME = UPPER(t_name) and column_name = upper(c_name)
    ;
  return all_comments;  
end;
/

show errors;


set lines 15000
set pages 40000
set linesize 1500
set heading off
set verify off
set feedback off
set serveroutput on
set long 50000
col structure format a15000

spool &1/citations.sql.raw
select 'alter table citations comment = '||f_table_comment('citations')||';'||CHR(10) from dual;
select   'insert into citations (ENZYME_ID,PUB_ID,ORDER_IN,STATUS,SOURCE) values ('||CHR(10)
    ||ENZYME_ID      ||','||CHR(10)
    ||PUB_ID         ||','||CHR(10)
    ||ORDER_IN       ||','||CHR(10)
    ||''''||STATUS   ||''','||CHR(10)
    ||''''||SOURCE   ||''');'
from citations
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/citations.sql.raw > &1/citations.sql ; rm &1/citations.sql.raw

spool &1/citations_audit.sql.raw
select 'alter table citations_audit comment = '||f_table_comment('citations_audit')||';'||CHR(10) from dual;
select   'insert into citations_audit (ENZYME_ID,PUB_ID,ORDER_IN,STATUS,SOURCE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||ENZYME_ID                                      ||','||CHR(10)
    ||PUB_ID                                         ||','||CHR(10)
    ||ORDER_IN                                       ||','||CHR(10)
    ||is_null_str(STATUS)||','||CHR(10)
    ||is_null_str(SOURCE)||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||is_null_str(TRIM(DBUSER                             ))||','||CHR(10)
    ||is_null_str(TRIM(OSUSER                             ))||','||CHR(10)
    ||is_null_str(TRIM(REMARK                             ))||','||CHR(10)
    ||is_null_str(TRIM(ACTION                             ))||');'
from citations_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/citations_audit.sql.raw > &1/citations_audit.sql ; rm &1/citations_audit.sql.raw

spool &1/classes.sql.raw
select 'alter table classes comment = '||f_table_comment('classes')||';'||CHR(10) from dual;
select   'insert into classes (EC1,NAME,DESCRIPTION,ACTIVE) values ('||CHR(10)
    ||EC1                                         ||','||CHR(10)
    ||''''||TRIM(NAME                            )||''','||CHR(10)
    ||is_null_str(replace(replace(DESCRIPTION,chr(10)),'''',''''''))||','||CHR(10)
    ||''''||ACTIVE                                ||''');'
from classes
order by EC1 asc
;
spool off;
host sed 's/ \+$//' &1/classes.sql.raw > &1/classes.sql ; rm &1/classes.sql.raw

spool &1/classes_audit.sql.raw
select 'alter table classes_audit comment = '||f_table_comment('classes_audit')||';'||CHR(10) from dual;
select   'insert into classes_audit (EC1,NAME,DESCRIPTION,ACTIVE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(EC1)                             ||','||CHR(10)
    ||is_null_str(NAME                            )||','||CHR(10)
    ||is_null_str(replace(replace(DESCRIPTION,chr(10)),'''',''''''))||','||CHR(10)
    ||is_null_str(ACTIVE                                )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from classes_audit
order by EC1 asc
;
spool off;
host sed 's/ \+$//' &1/classes_audit.sql.raw > &1/classes_audit.sql ; rm &1/classes_audit.sql.raw

spool &1/cofactors.sql.raw
select 'alter table cofactors comment = '||f_table_comment('cofactors')||';'||CHR(10) from dual;
select   'insert into cofactors (ENZYME_ID,SOURCE,STATUS,ORDER_IN,COFACTOR_TEXT,WEB_VIEW,COMPOUND_ID,OPERATOR,OP_GRP) values ('||CHR(10)
    ||ENZYME_ID                                 ||','||CHR(10)
    ||''''||SOURCE                              ||''','||CHR(10)
    ||''''||STATUS                              ||''','||CHR(10)
    ||ORDER_IN                                  ||','||CHR(10)
    ||''''||TRIM(replace(COFACTOR_TEXT,'''','''''')  )||''','||CHR(10)
    ||''''||TRIM(WEB_VIEW)                      ||''','||CHR(10)
    ||is_null_int(COMPOUND_ID)                      ||','||CHR(10)
    ||is_null_str(OPERATOR                            )||','||CHR(10)
    ||is_null_str(OP_GRP                              )||');'
from cofactors
where compound_id is not null
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/cofactors.sql.raw > &1/cofactors.sql ; rm &1/cofactors.sql.raw

spool &1/cofactors_audit.sql.raw
select 'alter table cofactors_audit comment = '||f_table_comment('cofactors_audit')||';'||CHR(10) from dual;
select   'insert into cofactors_audit (ENZYME_ID,SOURCE,STATUS,ORDER_IN,COFACTOR_TEXT,WEB_VIEW,COMPOUND_ID,OPERATOR,OP_GRP,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)                           ||','||CHR(10)
    ||is_null_str(SOURCE                              )||','||CHR(10)
    ||is_null_str(STATUS                              )||','||CHR(10)
    ||is_null_int(ORDER_IN)                     ||','||CHR(10)
    ||is_null_str(replace(COFACTOR_TEXT,'''',''''''))||','||CHR(10)
    ||is_null_str(WEB_VIEW)                      ||','||CHR(10)
    ||is_null_int(COMPOUND_ID)                  ||','||CHR(10)
    ||is_null_str(OPERATOR                            )||','||CHR(10)
    ||is_null_str(OP_GRP                              )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS'))||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from cofactors_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/cofactors_audit.sql.raw > &1/cofactors_audit.sql ; rm &1/cofactors_audit.sql.raw

spool &1/comments.sql.raw
select 'alter table comments comment = '||f_table_comment('comments')||';'||CHR(10) from dual;
select   'insert into comments (ENZYME_ID,COMMENT_TEXT,ORDER_IN,STATUS,SOURCE,WEB_VIEW) values ('||CHR(10)
    ||ENZYME_ID                                       ||','||CHR(10)
    ||''''||TRIM(replace(COMMENT_TEXT,'''','''''')   )||''','||CHR(10)
    ||ORDER_IN                                        ||','||CHR(10)
    ||''''||TRIM(STATUS                              )||''','||CHR(10)
    ||''''||TRIM(SOURCE                              )||''','||CHR(10)
    ||''''||TRIM(WEB_VIEW                            )||''');'
from comments
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/comments.sql.raw > &1/comments.sql ; rm &1/comments.sql.raw

spool &1/comments_audit.sql.raw
select 'alter table comments_audit comment = '||f_table_comment('comments_audit')||';'||CHR(10) from dual;
select   'insert into comments_audit (ENZYME_ID,COMMENT_TEXT,ORDER_IN,STATUS,SOURCE,WEB_VIEW,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)                           ||','||CHR(10)
    ||is_null_str(replace(COMMENT_TEXT,'''','''''')   )||','||CHR(10)
    ||is_null_int(ORDER_IN)                            ||','||CHR(10)
    ||is_null_str(STATUS                              )||','||CHR(10)
    ||is_null_str(SOURCE                              )||','||CHR(10)
    ||is_null_str(WEB_VIEW                            )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS'))||','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from comments_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/comments_audit.sql.raw > &1/comments_audit.sql ; rm &1/comments_audit.sql.raw

spool &1/enzymes.sql.raw
select 'alter table enzymes comment = '||f_table_comment('enzymes')||';'||CHR(10) from dual;
select   'insert into enzymes (ENZYME_ID,EC1,EC2,EC3,EC4,HISTORY,STATUS,NOTE,SOURCE,ACTIVE) values ('||CHR(10)
    ||ENZYME_ID                   ||','||CHR(10)
    ||EC1                         ||','||CHR(10)
    ||EC2                         ||','||CHR(10)
    ||EC3                         ||','||CHR(10)
    ||EC4                         ||','||CHR(10)
    ||is_null_str(TRIM(replace(HISTORY,'''','''''')))||','||CHR(10)
    ||''''||TRIM(STATUS                      )||''','||CHR(10)
    ||is_null_str(replace(NOTE,'''','''''')  )||','||CHR(10)
    ||''''||TRIM(SOURCE                      )||''','||CHR(10)
    ||''''||TRIM(ACTIVE                      )||''');'
from enzymes
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/enzymes.sql.raw > &1/enzymes.sql ; rm &1/enzymes.sql.raw

spool &1/enzymes_audit.sql.raw
select 'alter table enzymes_audit comment = '||f_table_comment('enzymes_audit')||';'||CHR(10) from dual;
select   'insert into enzymes_audit (ENZYME_ID,EC1,EC2,EC3,EC4,HISTORY,STATUS,NOTE,SOURCE,ACTIVE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)                   ||','||CHR(10)
    ||is_null_int(EC1)                         ||','||CHR(10)
    ||is_null_int(EC2)                         ||','||CHR(10)
    ||is_null_int(EC3)                         ||','||CHR(10)
    ||is_null_int(EC4)                         ||','||CHR(10)
    ||is_null_str(replace(HISTORY,'''',''''''))||','||CHR(10)
    ||is_null_str(STATUS                      )||','||CHR(10)
    ||is_null_str(replace(NOTE,'''','''''')   )||','||CHR(10)
    ||is_null_str(SOURCE                      )||','||CHR(10)
    ||is_null_str(ACTIVE                      )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                           ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from enzymes_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/enzymes_audit.sql.raw > &1/enzymes_audit.sql ; rm &1/enzymes_audit.sql.raw

spool &1/links.sql.raw
select 'alter table links comment = '||f_table_comment('links')||';'||CHR(10) from dual;
select   'insert into links (ENZYME_ID,URL,DISPLAY_NAME,STATUS,SOURCE,WEB_VIEW,DATA_COMMENT) values ('||CHR(10)
    ||ENZYME_ID               ||','||CHR(10)
    ||''''||TRIM(URL         )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME)||''','||CHR(10)
    ||''''||TRIM(STATUS      )||''','||CHR(10)
    ||''''||TRIM(SOURCE      )||''','||CHR(10)
    ||''''||TRIM(WEB_VIEW    )||''','||CHR(10)
    ||is_null_str(replace(DATA_COMMENT,'''',''''''))||');'
from links
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/links.sql.raw > &1/links.sql ; rm &1/links.sql.raw

spool &1/links_audit.sql.raw
select 'alter table links_audit comment = '||f_table_comment('links_audit')||';'||CHR(10) from dual;
select   'insert into links_audit (ENZYME_ID,URL,DISPLAY_NAME,STATUS,SOURCE,WEB_VIEW,DATA_COMMENT,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)   ||','||CHR(10)
    ||is_null_str(URL         )||','||CHR(10)
    ||is_null_str(DISPLAY_NAME)||','||CHR(10)
    ||is_null_str(STATUS      )||','||CHR(10)
    ||is_null_str(SOURCE      )||','||CHR(10)
    ||is_null_str(WEB_VIEW    )||','||CHR(10)
    ||is_null_str(replace(DATA_COMMENT,'''',''''''))||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS'))||','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from links_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/links_audit.sql.raw > &1/links_audit.sql ; rm &1/links_audit.sql.raw

spool &1/names.sql.raw
select 'alter table names comment = '||f_table_comment('names')||';'||CHR(10) from dual;
select  'insert into names (ENZYME_ID,NAME,NAME_CLASS,WARNING,STATUS,SOURCE,NOTE,ORDER_IN,WEB_VIEW) values ('||CHR(10)
    ||ENZYME_ID                               ||','||CHR(10)
    ||''''||TRIM(replace(NAME,'''','''''')   )||''','||CHR(10)
    ||''''||TRIM(NAME_CLASS                  )||''','||CHR(10)
    ||is_null_str(WARNING                     )||','||CHR(10)
    ||''''||TRIM(STATUS                      )||''','||CHR(10)
    ||''''||TRIM(SOURCE                      )||''','||CHR(10)
    ||is_null_str(NOTE                        )||','||CHR(10)
    ||ORDER_IN                                ||','||CHR(10)
    ||''''||TRIM(WEB_VIEW                    )||''');'
from names
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/names.sql.raw > &1/names.sql ; rm &1/names.sql.raw

spool &1/names_audit.sql.raw
select 'alter table names_audit comment = '||f_table_comment('names_audit')||';'||CHR(10) from dual;
select  'insert into names_audit (ENZYME_ID,NAME,NAME_CLASS,WARNING,STATUS,SOURCE,NOTE,ORDER_IN,WEB_VIEW,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)                   ||','||CHR(10)
    ||is_null_str(replace(NAME,'''','''''')   )||','||CHR(10)
    ||is_null_str(NAME_CLASS                  )||','||CHR(10)
    ||is_null_str(WARNING                     )||','||CHR(10)
    ||is_null_str(STATUS                      )||','||CHR(10)
    ||is_null_str(SOURCE                      )||','||CHR(10)
    ||is_null_str(NOTE                        )||','||CHR(10)
    ||is_null_int(ORDER_IN)                    ||','||CHR(10)
    ||is_null_str(WEB_VIEW                    )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS'))||','||CHR(10)
    ||is_null_int(AUDIT_ID)                           ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from names_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/names_audit.sql.raw > &1/names_audit.sql ; rm &1/names_audit.sql.raw

spool &1/publications.sql.raw
select 'alter table publications comment = '||f_table_comment('publications')||';'||CHR(10) from dual;
select  'insert into publications (PUB_ID,MEDLINE_ID,PUBMED_ID,PUB_TYPE,AUTHOR,PUB_YEAR,TITLE,JOURNAL_BOOK,VOLUME,FIRST_PAGE,LAST_PAGE,EDITION,EDITOR,PUB_COMPANY,PUB_PLACE,URL,WEB_VIEW,SOURCE) values ('||CHR(10)
    ||PUB_ID                                  ||','||CHR(10)
    ||is_null_int(MEDLINE_ID)                     ||','||CHR(10)
    ||is_null_int(PUBMED_ID)                      ||','||CHR(10)
    ||''''||TRIM(PUB_TYPE                    )||''','||CHR(10)
    ||is_null_str(replace(AUTHOR,'''','''''') )||','||CHR(10)
    ||PUB_YEAR                                ||','||CHR(10)
    ||is_null_str(replace(TITLE,'''','''''')  )||','||CHR(10)
    ||''''||TRIM(replace(JOURNAL_BOOK,'''',''''''))||''','||CHR(10)
    ||is_null_str(VOLUME                      )||','||CHR(10)
    ||is_null_str(FIRST_PAGE                  )||','||CHR(10)
    ||is_null_str(LAST_PAGE                   )||','||CHR(10)
    ||is_null_int(EDITION)                        ||','||CHR(10)
    ||is_null_str(replace(EDITOR,'''','''''') )||','||CHR(10)
    ||is_null_str(PUB_COMPANY                 )||','||CHR(10)
    ||is_null_str(PUB_PLACE                   )||','||CHR(10)
    ||is_null_str(URL                         )||','||CHR(10)
    ||''''||TRIM(WEB_VIEW                    )||''','||CHR(10)
    ||''''||TRIM(SOURCE                      )||''');'
from publications
order by PUB_ID asc
;
spool off;
host sed 's/ \+$//' &1/publications.sql.raw > &1/publications.sql ; rm &1/publications.sql.raw

spool &1/publications_audit.sql.raw
select 'alter table publications_audit comment = '||f_table_comment('publications_audit')||';'||CHR(10) from dual;
select  'insert into publications_audit (PUB_ID,MEDLINE_ID,PUBMED_ID,PUB_TYPE,AUTHOR,PUB_YEAR,TITLE,JOURNAL_BOOK,VOLUME,FIRST_PAGE,LAST_PAGE,EDITION,EDITOR,PUB_COMPANY,PUB_PLACE,URL,WEB_VIEW,SOURCE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(PUB_ID)                                  ||','||CHR(10)
    ||is_null_int(MEDLINE_ID)                     ||','||CHR(10)
    ||is_null_int(PUBMED_ID)                      ||','||CHR(10)
    ||is_null_str(TRIM(PUB_TYPE                    ))||','||CHR(10)
    ||is_null_str(replace(AUTHOR,'''','''''') )||','||CHR(10)
    ||is_null_int(PUB_YEAR)                                ||','||CHR(10)
    ||is_null_str(replace(TITLE,'''','''''')  )||','||CHR(10)
    ||is_null_str(replace(JOURNAL_BOOK,'''',''''''))||','||CHR(10)
    ||is_null_str(VOLUME                      )||','||CHR(10)
    ||is_null_str(FIRST_PAGE                  )||','||CHR(10)
    ||is_null_str(LAST_PAGE                   )||','||CHR(10)
    ||is_null_int(EDITION)                        ||','||CHR(10)
    ||is_null_str(replace(EDITOR,'''','''''') )||','||CHR(10)
    ||is_null_str(PUB_COMPANY                 )||','||CHR(10)
    ||is_null_str(PUB_PLACE                   )||','||CHR(10)
    ||is_null_str(URL                         )||','||CHR(10)
    ||is_null_str(WEB_VIEW                    )||','||CHR(10)
    ||is_null_str(SOURCE                      )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from publications_audit
order by PUB_ID asc
;
spool off;
host sed 's/ \+$//' &1/publications_audit.sql.raw > &1/publications_audit.sql ; rm &1/publications_audit.sql.raw

spool &1/reactions.sql.raw
select 'alter table reactions comment = '||f_table_comment('reactions')||';'||CHR(10) from dual;
select  'insert into reactions (ENZYME_ID,EQUATION,ORDER_IN,STATUS,SOURCE,WEB_VIEW) values ('||CHR(10)
    ||ENZYME_ID                                   ||','||CHR(10)
    ||''''||TRIM(replace(EQUATION,'''','''''')   )||''','||CHR(10)
    ||ORDER_IN                                    ||','||CHR(10)
    ||''''||TRIM(STATUS                          )||''','||CHR(10)
    ||''''||TRIM(SOURCE                          )||''','||CHR(10)
    ||''''||TRIM(WEB_VIEW                        )||''');'
from reactions
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/reactions.sql.raw > &1/reactions.sql ; rm &1/reactions.sql.raw

spool &1/reactions_audit.sql.raw
select 'alter table reactions_audit comment = '||f_table_comment('reactions_audit')||';'||CHR(10) from dual;
select  'insert into reactions_audit (ENZYME_ID,EQUATION,ORDER_IN,STATUS,SOURCE,WEB_VIEW,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)                       ||','||CHR(10)
    ||is_null_str(replace(EQUATION,'''','''''')   )||','||CHR(10)
    ||is_null_int(ORDER_IN)                        ||','||CHR(10)
    ||is_null_str(STATUS                          )||','||CHR(10)
    ||is_null_str(SOURCE                          )||','||CHR(10)
    ||is_null_str(WEB_VIEW                        )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from reactions_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/reactions_audit.sql.raw > &1/reactions_audit.sql ; rm &1/reactions_audit.sql.raw

spool &1/subclasses.sql.raw
select 'alter table subclasses comment = '||f_table_comment('subclasses')||';'||CHR(10) from dual;
select  'insert into subclasses (EC1,EC2,NAME,DESCRIPTION,ACTIVE) values ('||CHR(10)
    ||EC1                                         ||','||CHR(10)
    ||EC2                                         ||','||CHR(10)
    ||''''||TRIM(replace(NAME,'''','''''')       )||''','||CHR(10)
    ||is_null_str(replace(DESCRIPTION,'''',''''''))||','||CHR(10)
    ||''''||TRIM(ACTIVE                          )||''');'
from subclasses
order by EC1 asc
;
spool off;
host sed 's/ \+$//' &1/subclasses.sql.raw > &1/subclasses.sql ; rm &1/subclasses.sql.raw

spool &1/subclasses_audit.sql.raw
select 'alter table subclasses_audit comment = '||f_table_comment('subclasses_audit')||';'||CHR(10) from dual;
select  'insert into subclasses_audit (EC1,EC2,NAME,DESCRIPTION,ACTIVE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(EC1)                                         ||','||CHR(10)
    ||is_null_int(EC2)                                         ||','||CHR(10)
    ||is_null_str(replace(NAME,'''','''''')       )||','||CHR(10)
    ||is_null_str(replace(DESCRIPTION,'''',''''''))||','||CHR(10)
    ||is_null_str(ACTIVE                          )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from subclasses_audit
order by EC1 asc
;
spool off;
host sed 's/ \+$//' &1/subclasses_audit.sql.raw > &1/subclasses_audit.sql ; rm &1/subclasses_audit.sql.raw

spool &1/subsubclasses.sql.raw
select 'alter table subsubclasses comment = '||f_table_comment('subsubclasses')||';'||CHR(10) from dual;
select  'insert into subsubclasses (EC1,EC2,EC3,NAME,DESCRIPTION,ACTIVE) values ('||CHR(10)
    ||EC1                                         ||','||CHR(10)
    ||EC2                                         ||','||CHR(10)
    ||EC3                                         ||','||CHR(10)
    ||is_null_str(replace(NAME,'''','''''')       )||','||CHR(10)
    ||is_null_str(replace(DESCRIPTION,'''',''''''))||','||CHR(10)
    ||''''||TRIM(ACTIVE                          )||''');'
from subsubclasses
order by EC1 asc
;
spool off;
host sed 's/ \+$//' &1/subsubclasses.sql.raw > &1/subsubclasses.sql ; rm &1/subsubclasses.sql.raw

spool &1/subsubclasses_audit.sql.raw
select 'alter table subsubclasses_audit comment = '||f_table_comment('subsubclasses_audit')||';'||CHR(10) from dual;
select  'insert into subsubclasses_audit (EC1,EC2,EC3,NAME,DESCRIPTION,ACTIVE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(EC1)                                         ||','||CHR(10)
    ||is_null_int(EC2)                                         ||','||CHR(10)
    ||is_null_int(EC3)                                         ||','||CHR(10)
    ||is_null_str(TRIM(replace(NAME,'''','''''')       ))||','||CHR(10)
    ||is_null_str(TRIM(replace(DESCRIPTION,'''','''''')))||','||CHR(10)
    ||is_null_str(TRIM(ACTIVE                          ))||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from subsubclasses_audit
order by EC1 asc
;
spool off;
host sed 's/ \+$//' &1/subsubclasses_audit.sql.raw > &1/subsubclasses_audit.sql ; rm &1/subsubclasses_audit.sql.raw

spool &1/xrefs.sql.raw
select 'alter table xrefs comment = '||f_table_comment('xrefs')||';'||CHR(10) from dual;
select  'insert into xrefs (ENZYME_ID,DATABASE_CODE,DATABASE_AC,NAME,STATUS,SOURCE,WEB_VIEW,DATA_COMMENT) values ('||CHR(10)
    ||ENZYME_ID                   ||','||CHR(10)
    ||''''||TRIM(DATABASE_CODE   )||''','||CHR(10)
    ||''''||TRIM(DATABASE_AC     )||''','||CHR(10)
    ||''''||TRIM(replace(NAME,'''',''''''))||''','||CHR(10)
    ||''''||TRIM(STATUS          )||''','||CHR(10)
    ||''''||TRIM(SOURCE          )||''','||CHR(10)
    ||''''||TRIM(WEB_VIEW        )||''','||CHR(10)
    ||is_null_str(replace(DATA_COMMENT,'''',''''''))||');'
from xrefs
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/xrefs.sql.raw > &1/xrefs.sql ; rm &1/xrefs.sql.raw

spool &1/xrefs_audit.sql.raw
select 'alter table xrefs_audit comment = '||f_table_comment('xrefs_audit')||';'||CHR(10) from dual;
select  'insert into xrefs_audit (ENZYME_ID,DATABASE_CODE,DATABASE_AC,NAME,STATUS,SOURCE,WEB_VIEW,DATA_COMMENT,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(ENZYME_ID)                   ||','||CHR(10)
    ||is_null_str(TRIM(DATABASE_CODE   ))||','||CHR(10)
    ||is_null_str(TRIM(DATABASE_AC     ))||','||CHR(10)
    ||is_null_str(TRIM(replace(NAME,'''','''''')))||','||CHR(10)
    ||is_null_str(TRIM(STATUS          ))||','||CHR(10)
    ||is_null_str(TRIM(SOURCE          ))||','||CHR(10)
    ||is_null_str(TRIM(WEB_VIEW        ))||','||CHR(10)
    ||is_null_str(TRIM(replace(DATA_COMMENT,'''','''''')    ))||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from xrefs_audit
order by ENZYME_ID asc
;
spool off;
host sed 's/ \+$//' &1/xrefs_audit.sql.raw > &1/xrefs_audit.sql ; rm &1/xrefs_audit.sql.raw


/** Workflow-related tables **/


spool &1/history_events.sql.raw
select 'alter table history_events comment = '||f_table_comment('history_events')||';'||CHR(10) from dual;
select 'insert into history_events (GROUP_ID,EVENT_ID,BEFORE_ID,AFTER_ID,EVENT_YEAR,EVENT_NOTE,EVENT_CLASS) values ('||CHR(10)
    ||GROUP_ID                                        ||','||CHR(10)
    ||EVENT_ID                                        ||','||CHR(10)
    ||is_null_int(BEFORE_ID)                              ||','||CHR(10)
    ||is_null_int(AFTER_ID)                               ||','||CHR(10)
    ||is_null_str(to_char(EVENT_YEAR,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_str(EVENT_NOTE                          )||','||CHR(10)
    ||''''||TRIM(EVENT_CLASS                         )||''');'
from history_events
order by event_id
;
spool off;
host sed 's/ \+$//' &1/history_events.sql.raw > &1/history_events.sql ; rm &1/history_events.sql.raw

spool &1/history_events_audit.sql.raw
select 'alter table history_events_audit comment = '||f_table_comment('history_events_audit')||';'||CHR(10) from dual;
select 'insert into history_events_audit (GROUP_ID,EVENT_ID,BEFORE_ID,AFTER_ID,EVENT_YEAR,EVENT_NOTE,EVENT_CLASS,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(GROUP_ID)                                        ||','||CHR(10)
    ||is_null_int(EVENT_ID)                                        ||','||CHR(10)
    ||is_null_int(BEFORE_ID)                              ||','||CHR(10)
    ||is_null_int(AFTER_ID)                               ||','||CHR(10)
    ||is_null_str(to_char(EVENT_YEAR,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_str(TRIM(EVENT_NOTE                          ))||','||CHR(10)
    ||is_null_str(TRIM(EVENT_CLASS                         ))||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from history_events_audit
order by event_id
;
spool off;
host sed 's/ \+$//' &1/history_events_audit.sql.raw > &1/history_events_audit.sql ; rm &1/history_events_audit.sql.raw

spool &1/future_events.sql.raw
select 'alter table future_events comment = '||f_table_comment('future_events')||';'||CHR(10) from dual;
select 'insert into future_events (GROUP_ID,EVENT_ID,BEFORE_ID,AFTER_ID,EVENT_YEAR,EVENT_NOTE,EVENT_CLASS,STATUS,TIMEOUT_ID) values ('||CHR(10)
    ||GROUP_ID                                        ||','||CHR(10)
    ||EVENT_ID                                        ||','||CHR(10)
    ||is_null_int(BEFORE_ID)                              ||','||CHR(10)
    ||is_null_int(AFTER_ID)                               ||','||CHR(10)
    ||is_null_str(to_char(EVENT_YEAR,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_str(EVENT_NOTE                          )||','||CHR(10)
    ||''''||TRIM(EVENT_CLASS                         )||''','||CHR(10)
    ||''''||TRIM(STATUS                              )||''','||CHR(10)
    ||TIMEOUT_ID                                      ||');'
from future_events
order by event_id
;
spool off;
host sed 's/ \+$//' &1/future_events.sql.raw > &1/future_events.sql ; rm &1/future_events.sql.raw

spool &1/future_events_audit.sql.raw
select 'alter table future_events_audit comment = '||f_table_comment('future_events_audit')||';'||CHR(10) from dual;
select 'insert into future_events_audit (GROUP_ID,EVENT_ID,BEFORE_ID,AFTER_ID,EVENT_YEAR,EVENT_NOTE,EVENT_CLASS,STATUS,TIMEOUT_ID,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(GROUP_ID)                                        ||','||CHR(10)
    ||is_null_int(EVENT_ID)                                        ||','||CHR(10)
    ||is_null_int(BEFORE_ID)                              ||','||CHR(10)
    ||is_null_int(AFTER_ID)                               ||','||CHR(10)
    ||is_null_str(to_char(EVENT_YEAR,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_str(EVENT_NOTE                          )||','||CHR(10)
    ||is_null_str(EVENT_CLASS                         )||','||CHR(10)
    ||is_null_str(STATUS                              )||','||CHR(10)
    ||is_null_int(TIMEOUT_ID)                          ||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from future_events_audit
order by event_id
;
spool off;
host sed 's/ \+$//' &1/future_events_audit.sql.raw > &1/future_events_audit.sql ; rm &1/future_events_audit.sql.raw

spool &1/timeouts.sql.raw
select 'alter table timeouts comment = '||f_table_comment('timeouts')||';'||CHR(10) from dual;
select 'insert into timeouts (TIMEOUT_ID,ENZYME_ID,START_DATE,DUE_DATE) values ('||CHR(10)
    ||TIMEOUT_ID ||','||CHR(10)
    ||ENZYME_ID ||','||CHR(10)
    ||''''||to_char(START_DATE,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||''''||to_char(DUE_DATE,'YYYY-MM-DD HH:MI:SS') ||''');'
from timeouts
order by due_date
;
spool off;
host sed 's/ \+$//' &1/timeouts.sql.raw > &1/timeouts.sql ; rm &1/timeouts.sql.raw

spool &1/timeouts_audit.sql.raw
select 'alter table timeouts_audit comment = '||f_table_comment('timeouts_audit')||';'||CHR(10) from dual;
select 'insert into timeouts_audit (TIMEOUT_ID,ENZYME_ID,START_DATE,DUE_DATE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(TIMEOUT_ID)||','||CHR(10)
    ||is_null_int(ENZYME_ID)||','||CHR(10)
    ||is_null_str(to_char(START_DATE,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_str(to_char(DUE_DATE,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from timeouts_audit
order by due_date
;
spool off;
host sed 's/ \+$//' &1/timeouts_audit.sql.raw > &1/timeouts_audit.sql ; rm &1/timeouts_audit.sql.raw


/** Rhea-related tables **/


spool &1/reactions_map.sql.raw
select 'alter table reactions_map comment = '||f_table_comment('reactions_map')||';'||CHR(10) from dual;
select 'insert into reactions_map (REACTION_ID,ENZYME_ID,WEB_VIEW,ORDER_IN) values ('||CHR(10)
    ||REACTION_ID                                 ||','||CHR(10)
    ||ENZYME_ID                                   ||','||CHR(10)
    ||''''||TRIM(WEB_VIEW)                        ||''','||CHR(10)
    ||ORDER_IN                                    ||');'
from reactions_map
order by enzyme_id asc
;
spool off;
host sed 's/ \+$//' &1/reactions_map.sql.raw > &1/reactions_map.sql ; rm &1/reactions_map.sql.raw

spool &1/reactions_map_audit.sql.raw
select 'alter table reactions_map_audit comment = '||f_table_comment('reactions_map_audit')||';'||CHR(10) from dual;
select 'insert into reactions_map_audit (REACTION_ID,ENZYME_ID,WEB_VIEW,ORDER_IN,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||is_null_int(REACTION_ID)                              ||','||CHR(10)
    ||is_null_int(ENZYME_ID)                                ||','||CHR(10)
    ||is_null_str(TRIM(WEB_VIEW)                        )||','||CHR(10)
    ||is_null_int(ORDER_IN)                                 ||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from reactions_map_audit
order by enzyme_id asc
;
spool off;
host sed 's/ \+$//' &1/reactions_map_audit.sql.raw > &1/reactions_map_audit.sql ; rm &1/reactions_map_audit.sql.raw

spool &1/intenz_reactions.sql.raw
select 'alter table intenz_reactions comment = '||f_table_comment('intenz_reactions')||';'||CHR(10) from dual;
select 'insert into intenz_reactions (REACTION_ID,EQUATION,FINGERPRINT,STATUS,SOURCE,DIRECTION,UN_REACTION,QUALIFIERS,DATA_COMMENT,REACTION_COMMENT) values ('||CHR(10)
	||REACTION_ID                                     ||','||CHR(10)
	||''''||TRIM(replace(EQUATION,'''','''''')       )||''','||CHR(10)
	||is_null_str(FINGERPRINT                         )||','||CHR(10)
    ||''''||TRIM(STATUS                              )||''','||CHR(10)
    ||''''||TRIM(SOURCE                              )||''','||CHR(10)
    ||''''||TRIM(DIRECTION                           )||''','||CHR(10)
	||is_null_int(UN_REACTION)                            ||','||CHR(10)
	||qual_list(REACTION_ID)                   ||','||CHR(10)
	||is_null_str(replace(DATA_COMMENT,'''','''''')   )||','||CHR(10)
	||is_null_str(replace(REACTION_COMMENT,'''',''''''))||');'
from intenz_reactions
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/intenz_reactions.sql.raw > &1/intenz_reactions.sql ; rm &1/intenz_reactions.sql.raw

spool &1/intenz_reactions_audit.sql.raw
select 'alter table intenz_reactions_audit comment = '||f_table_comment('intenz_reactions_audit')||';'||CHR(10) from dual;
select 'insert into intenz_reactions_audit (REACTION_ID,EQUATION,FINGERPRINT,STATUS,SOURCE,DIRECTION,UN_REACTION,QUALIFIERS,DATA_COMMENT,REACTION_COMMENT,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
	||is_null_int(REACTION_ID)                                     ||','||CHR(10)
	||is_null_str(TRIM(replace(EQUATION,'''','''''')       ))||','||CHR(10)
	||is_null_str(FINGERPRINT                         )||','||CHR(10)
    ||is_null_str(TRIM(STATUS                              ))||','||CHR(10)
    ||is_null_str(TRIM(SOURCE                              ))||','||CHR(10)
    ||is_null_str(TRIM(DIRECTION                           ))||','||CHR(10)
	||is_null_int(UN_REACTION)                            ||','||CHR(10)
	||qual_list(REACTION_ID)                   ||','||CHR(10)
	||is_null_str(replace(DATA_COMMENT,'''','''''')   )||','||CHR(10)
	, is_null_str(replace(REACTION_COMMENT,'''',''''''))||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(replace(REMARK,'''','''''')        )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from intenz_reactions_audit
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/intenz_reactions_audit.sql.raw > &1/intenz_reactions_audit.sql ; rm &1/intenz_reactions_audit.sql.raw

spool &1/reaction_participants.sql.raw
select 'alter table reaction_participants comment = '||f_table_comment('reaction_participants')||';'||CHR(10) from dual;
select 'insert into reaction_participants (REACTION_ID,COMPOUND_ID,SIDE,COEFFICIENT,COEFF_TYPE) values ('||CHR(10)
	||REACTION_ID                                     ||','||CHR(10)
	||COMPOUND_ID                                     ||','||CHR(10)
    ||''''||TRIM(SIDE                                )||''','||CHR(10)
	||COEFFICIENT                                     ||','||CHR(10)
    ||''''||TRIM(COEFF_TYPE                          )||''');'
from reaction_participants
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/reaction_participants.sql.raw > &1/reaction_participants.sql ; rm &1/reaction_participants.sql.raw

spool &1/reaction_participants_audit.sql.raw
select 'alter table reaction_participants_audit comment = '||f_table_comment('reaction_participants_audit')||';'||CHR(10) from dual;
select 'insert into reaction_participants_audit (REACTION_ID,COMPOUND_ID,SIDE,COEFFICIENT,COEFF_TYPE,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
	||is_null_int(REACTION_ID)                                     ||','||CHR(10)
	||is_null_int(COMPOUND_ID)                                     ||','||CHR(10)
    ||is_null_str(TRIM(SIDE                                ))||','||CHR(10)
	||is_null_int(COEFFICIENT)                                     ||','||CHR(10)
    ||is_null_str(COEFF_TYPE                          )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from reaction_participants_audit
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/reaction_participants_audit.sql.raw > &1/reaction_participants_audit.sql ; rm &1/reaction_participants_audit.sql.raw

spool &1/compound_data.sql.raw
select 'alter table compound_data comment = '||f_table_comment('compound_data')||';'||CHR(10) from dual;
select 'insert into compound_data (COMPOUND_ID,NAME,FORMULA,CHARGE,SOURCE,ACCESSION,CHILD_ACCESSION,PUBLISHED) values ('||CHR(10)
	||COMPOUND_ID                                     ||','||CHR(10)
    ||''''||TRIM(replace(NAME,'''','''''')           )||''','||CHR(10)
    ||is_null_str(FORMULA                             )||','||CHR(10)
	||is_null_int(CHARGE)                                 ||','||CHR(10)
    ||is_null_str(SOURCE                              )||','||CHR(10)
    ||is_null_str(ACCESSION                           )||','||CHR(10)
    ||is_null_str(CHILD_ACCESSION                     )||','||CHR(10)
    ||is_null_str(PUBLISHED                           )||');'
from compound_data
order by compound_id asc
;
spool off;
host sed 's/ \+$//' &1/compound_data.sql.raw > &1/compound_data.sql ; rm &1/compound_data.sql.raw

spool &1/compound_data_audit.sql.raw
select 'alter table compound_data_audit comment = '||f_table_comment('compound_data_audit')||';'||CHR(10) from dual;
select 'insert into compound_data_audit (COMPOUND_ID,NAME,FORMULA,CHARGE,SOURCE,ACCESSION,CHILD_ACCESSION,PUBLISHED,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
	||is_null_int(COMPOUND_ID)                                     ||','||CHR(10)
    ||is_null_str(TRIM(replace(NAME,'''','''''')           ))||','||CHR(10)
    ||is_null_str(FORMULA                             )||','||CHR(10)
	||is_null_int(CHARGE)                                 ||','||CHR(10)
    ||is_null_str(SOURCE                              )||','||CHR(10)
    ||is_null_str(ACCESSION                           )||','||CHR(10)
    ||is_null_str(CHILD_ACCESSION                     )||','||CHR(10)
    ||is_null_str(PUBLISHED                           )||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from compound_data_audit
order by compound_id asc
;
spool off;
host sed 's/ \+$//' &1/compound_data_audit.sql.raw > &1/compound_data_audit.sql ; rm &1/compound_data_audit.sql.raw

spool &1/complex_reactions.sql.raw
select 'alter table complex_reactions comment = '||f_table_comment('complex_reactions')||';'||CHR(10) from dual;
select 'insert into complex_reactions (PARENT_ID,CHILD_ID,ORDER_IN,COEFFICIENT) values ('||CHR(10)
	||PARENT_ID                                      ||','||CHR(10)
	||CHILD_ID                                       ||','||CHR(10)
    ||ORDER_IN                                       ||','||CHR(10)
    ||COEFFICIENT                                    ||');'
from complex_reactions
order by parent_id, order_in asc
;
spool off;
host sed 's/ \+$//' &1/complex_reactions.sql.raw > &1/complex_reactions.sql ; rm &1/complex_reactions.sql.raw

spool &1/complex_reactions_audit.sql.raw
select 'alter table complex_reactions_audit comment = '||f_table_comment('complex_reactions_audit')||';'||CHR(10) from dual;
select 'insert into complex_reactions_audit (PARENT_ID,CHILD_ID,ORDER_IN,COEFFICIENT,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
	||is_null_int(PARENT_ID)                                      ||','||CHR(10)
	||is_null_int(CHILD_ID)                                       ||','||CHR(10)
    ||is_null_int(ORDER_IN)                                       ||','||CHR(10)
    ||is_null_int(COEFFICIENT)                                    ||','||CHR(10)
    ||is_null_str(to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') )||','||CHR(10)
    ||is_null_int(AUDIT_ID)                          ||','||CHR(10)
    ||is_null_str(DBUSER                             )||','||CHR(10)
    ||is_null_str(OSUSER                             )||','||CHR(10)
    ||is_null_str(REMARK                             )||','||CHR(10)
    ||is_null_str(ACTION                             )||');'
from complex_reactions_audit
order by parent_id, order_in asc
;
spool off;
host sed 's/ \+$//' &1/complex_reactions_audit.sql.raw > &1/complex_reactions_audit.sql ; rm &1/complex_reactions_audit.sql.raw


spool &1/reaction_citations.sql.raw
select 'alter table reaction_citations comment = '||f_table_comment('reaction_citations')||';'||CHR(10) from dual;
select 'insert into reaction_citations (REACTION_ID,PUB_ID,SOURCE,ORDER_IN) values ('||CHR(10)
	||REACTION_ID                                     ||','||CHR(10)
    ||''''||TRIM(PUB_ID                              )||''','||CHR(10)
    ||''''||TRIM(SOURCE                              )||''','||CHR(10)
    ||is_null_int(ORDER_IN)                               ||');'
from reaction_citations
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/reaction_citations.sql.raw > &1/reaction_citations.sql ; rm &1/reaction_citations.sql.raw

spool &1/reaction_citations_audit.sql.raw
select 'alter table reaction_citations_audit comment = '||f_table_comment('reaction_citations_audit')||';'||CHR(10) from dual;
select 'insert into reaction_citations_audit (REACTION_ID,PUB_ID,SOURCE,ORDER_IN,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
	||REACTION_ID                                     ||','||CHR(10)
    ||''''||TRIM(PUB_ID                              )||''','||CHR(10)
    ||''''||TRIM(SOURCE                              )||''','||CHR(10)
    ||is_null_int(ORDER_IN)                               ||','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from reaction_citations_audit
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/reaction_citations_audit.sql.raw > &1/reaction_citations_audit.sql ; rm &1/reaction_citations_audit.sql.raw

spool &1/reaction_xrefs.sql.raw
select 'alter table reaction_xrefs comment = '||f_table_comment('reaction_xrefs')||';'||CHR(10) from dual;
select 'insert into reaction_xrefs (REACTION_ID,DB_CODE,DB_ACCESSION) values ('||CHR(10)
	||REACTION_ID                                     ||','||CHR(10)
    ||''''||TRIM(DB_CODE                             )||''','||CHR(10)
    ||''''||TRIM(DB_ACCESSION                        )||''');'
from reaction_xrefs
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/reaction_xrefs.sql.raw > &1/reaction_xrefs.sql ; rm &1/reaction_xrefs.sql.raw

spool &1/reaction_xrefs_audit.sql.raw
select 'alter table reaction_xrefs_audit comment = '||f_table_comment('reaction_xrefs_audit')||';'||CHR(10) from dual;
select 'insert into reaction_xrefs_audit (REACTION_ID,DB_CODE,DB_ACCESSION,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
	||REACTION_ID                                     ||','||CHR(10)
    ||''''||TRIM(DB_CODE                             )||''','||CHR(10)
    ||''''||TRIM(DB_ACCESSION                        )||''','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from reaction_xrefs_audit
order by reaction_id asc
;
spool off;
host sed 's/ \+$//' &1/reaction_xrefs_audit.sql.raw > &1/reaction_xrefs_audit.sql ; rm &1/reaction_xrefs_audit.sql.raw


/** Controlled vocabularies tables **/


spool &1/cv_coeff_types.sql.raw
select 'alter table cv_coeff_types comment = '||f_table_comment('cv_coeff_types')||';'||CHR(10) from dual;
select 'insert into cv_coeff_types (CODE,EXPLANATION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(EXPLANATION                      )||''');'
from cv_coeff_types
;
spool off;
host sed 's/ \+$//' &1/cv_coeff_types.sql.raw > &1/cv_coeff_types.sql ; rm &1/cv_coeff_types.sql.raw

spool &1/cv_comp_pub_avail.sql.raw
select 'alter table cv_comp_pub_avail comment = '||f_table_comment('cv_comp_pub_avail')||';'||CHR(10) from dual;
select 'insert into cv_comp_pub_avail (CODE,EXPLANATION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(EXPLANATION                      )||''');'
from cv_comp_pub_avail
;
spool off;
host sed 's/ \+$//' &1/cv_comp_pub_avail.sql.raw > &1/cv_comp_pub_avail.sql ; rm &1/cv_comp_pub_avail.sql.raw

spool &1/cv_databases.sql.raw
select 'alter table cv_databases comment = '||f_table_comment('cv_databases')||';'||CHR(10) from dual;
select 'insert into cv_databases (CODE,NAME,DISPLAY_NAME,SORT_ORDER) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||');'
from cv_databases
;
spool off;
host sed 's/ \+$//' &1/cv_databases.sql.raw > &1/cv_databases.sql ; rm &1/cv_databases.sql.raw

spool &1/cv_databases_audit.sql.raw
select 'alter table cv_databases_audit comment = '||f_table_comment('cv_databases_audit')||';'||CHR(10) from dual;
select 'insert into cv_databases_audit (CODE,NAME,DISPLAY_NAME,SORT_ORDER,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from cv_databases_audit
;
spool off;
host sed 's/ \+$//' &1/cv_databases_audit.sql.raw > &1/cv_databases_audit.sql ; rm &1/cv_databases_audit.sql.raw

spool &1/cv_name_classes.sql.raw
select 'alter table cv_name_classes comment = '||f_table_comment('cv_name_classes')||';'||CHR(10) from dual;
select 'insert into cv_name_classes (CODE,NAME,DISPLAY_NAME,SORT_ORDER) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||');'
from cv_name_classes
;
spool off;
host sed 's/ \+$//' &1/cv_name_classes.sql.raw > &1/cv_name_classes.sql ; rm &1/cv_name_classes.sql.raw

spool &1/cv_name_classes_audit.sql.raw
select 'alter table cv_name_classes_audit comment = '||f_table_comment('cv_name_classes_audit')||';'||CHR(10) from dual;
select 'insert into cv_name_classes_audit (CODE,NAME,DISPLAY_NAME,SORT_ORDER,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from cv_name_classes_audit
;
spool off;
host sed 's/ \+$//' &1/cv_name_classes_audit.sql.raw > &1/cv_name_classes_audit.sql ; rm &1/cv_name_classes_audit.sql.raw

spool &1/cv_operators.sql.raw
select 'alter table cv_operators comment = '||f_table_comment('cv_operators')||';'||CHR(10) from dual;
select 'insert into cv_operators (CODE,EXPLANATION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(EXPLANATION                      )||''');'
from cv_operators
;
spool off;
host sed 's/ \+$//' &1/cv_operators.sql.raw > &1/cv_operators.sql ; rm &1/cv_operators.sql.raw

spool &1/cv_reaction_directions.sql.raw
select 'alter table cv_reaction_directions comment = '||f_table_comment('cv_reaction_directions')||';'||CHR(10) from dual;
select 'insert into cv_reaction_directions (CODE,DIRECTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(DIRECTION                        )||''');'
from cv_reaction_directions
;
spool off;
host sed 's/ \+$//' &1/cv_reaction_directions.sql.raw > &1/cv_reaction_directions.sql ; rm &1/cv_reaction_directions.sql.raw

spool &1/cv_reaction_qualifiers.sql.raw
select 'alter table cv_reaction_qualifiers comment = '||f_table_comment('cv_reaction_qualifiers')||';'||CHR(10) from dual;
select 'insert into cv_reaction_qualifiers (CODE,QUALIFIER) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(QUALIFIER                        )||''');'
from cv_reaction_qualifiers
;
spool off;
host sed 's/ \+$//' &1/cv_reaction_qualifiers.sql.raw > &1/cv_reaction_qualifiers.sql ; rm &1/cv_reaction_qualifiers.sql.raw

spool &1/cv_reaction_sides.sql.raw
select 'alter table cv_reaction_sides comment = '||f_table_comment('cv_reaction_sides')||';'||CHR(10) from dual;
select 'insert into cv_reaction_sides (CODE,SIDE) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(SIDE                             )||''');'
from cv_reaction_sides
;
spool off;
host sed 's/ \+$//' &1/cv_reaction_sides.sql.raw > &1/cv_reaction_sides.sql ; rm &1/cv_reaction_sides.sql.raw

spool &1/cv_status.sql.raw
select 'alter table cv_status comment = '||f_table_comment('cv_status')||';'||CHR(10) from dual;
select 'insert into cv_status (CODE,NAME,DISPLAY_NAME,SORT_ORDER) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||');'
from cv_status
;
spool off;
host sed 's/ \+$//' &1/cv_status.sql.raw > &1/cv_status.sql ; rm &1/cv_status.sql.raw

spool &1/cv_status_audit.sql.raw
select 'alter table cv_status_audit comment = '||f_table_comment('cv_status_audit')||';'||CHR(10) from dual;
select 'insert into cv_status_audit (CODE,NAME,DISPLAY_NAME,SORT_ORDER,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from cv_status_audit
;
spool off;
host sed 's/ \+$//' &1/cv_status_audit.sql.raw > &1/cv_status_audit.sql ; rm &1/cv_status_audit.sql.raw

spool &1/cv_view.sql.raw
select 'alter table cv_view comment = '||f_table_comment('cv_view')||';'||CHR(10) from dual;
select 'insert into cv_view (CODE,DESCRIPTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(DESCRIPTION                      )||''');'
from cv_view
;
spool off;
host sed 's/ \+$//' &1/cv_view.sql.raw > &1/cv_view.sql ; rm &1/cv_view.sql.raw

spool &1/cv_view_audit.sql.raw
select 'alter table cv_view_audit comment = '||f_table_comment('cv_view_audit')||';'||CHR(10) from dual;
select 'insert into cv_view_audit (CODE,DESCRIPTION,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(DESCRIPTION                      )||''','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from cv_view_audit
;
spool off;
host sed 's/ \+$//' &1/cv_view_audit.sql.raw > &1/cv_view_audit.sql ; rm &1/cv_view_audit.sql.raw

spool &1/cv_warnings.sql.raw
select 'alter table cv_warnings comment = '||f_table_comment('cv_warnings')||';'||CHR(10) from dual;
select 'insert into cv_warnings (CODE,NAME,DISPLAY_NAME,SORT_ORDER) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||');'
from cv_warnings
;
spool off;
host sed 's/ \+$//' &1/cv_warnings.sql.raw > &1/cv_warnings.sql ; rm &1/cv_warnings.sql.raw

spool &1/cv_warnings_audit.sql.raw
select 'alter table cv_warnings_audit comment = '||f_table_comment('cv_warnings_audit')||';'||CHR(10) from dual;
select 'insert into cv_warnings_audit (CODE,NAME,DISPLAY_NAME,SORT_ORDER,TIMESTAMP,AUDIT_ID,DBUSER,OSUSER,REMARK,ACTION) values ('||CHR(10)
    ||''''||TRIM(CODE                             )||''','||CHR(10)
    ||''''||TRIM(NAME                             )||''','||CHR(10)
    ||''''||TRIM(DISPLAY_NAME                     )||''','||CHR(10)
    ||SORT_ORDER                                   ||','||CHR(10)
    ||''''||to_char(TIMESTAMP,'YYYY-MM-DD HH:MI:SS') ||''','||CHR(10)
    ||is_null_int(AUDIT_ID)                              ||','||CHR(10)
    ||''''||TRIM(DBUSER                             )||''','||CHR(10)
    ||''''||TRIM(OSUSER                             )||''','||CHR(10)
    ||''''||TRIM(REMARK                             )||''','||CHR(10)
    ||''''||TRIM(ACTION                             )||''');'
from cv_warnings_audit
;
spool off;
host sed 's/ \+$//' &1/cv_warnings_audit.sql.raw > &1/cv_warnings_audit.sql ; rm &1/cv_warnings_audit.sql.raw


drop function is_null_int;
drop function is_null_str;
drop function qual_list;
drop function f_table_comment;
drop function f_column_comment;

exit;
