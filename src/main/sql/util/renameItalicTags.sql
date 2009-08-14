PROMPT Alterations to remove html tags from database
/**************************************************************************
**                                                                       **
** Addition of the reference table for the uniprot identifiers           **
**                                                                       **
** Author: pmatos@ebi.ac.uk                                              **
** Creation date: 13 October 2004                                        **
** Status:  Development database only (chebi_test)                       **
**                                                                       **
**************************************************************************/

set serverout on;

CREATE TABLE temp_debug (id NUMBER(15), old_name VARCHAR2(4000), new_name VARCHAR2(4000));

DECLARE
  l_search_string VARCHAR2(60);
  iii integer;
  l_name VARCHAR2(4000);

BEGIN
   --CLASSES (name)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT ec1, name FROM classes WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1, r_italic.name, l_name);

          UPDATE classes
              SET name=l_name
              WHERE ec1=r_italic.ec1;
      END LOOP;

   --CLASSES (description)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT ec1, description FROM classes WHERE description LIKE l_search_string) LOOP
          l_name:=r_italic.description;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1, r_italic.description, l_name);

          UPDATE classes
              SET description=l_name
              WHERE ec1=r_italic.ec1;
      END LOOP;

   --SUBCLASSES (name)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT ec1, ec2, name FROM subclasses WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2, r_italic.name, l_name);

          UPDATE subclasses
              SET name=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2;
      END LOOP;

   --SUBCLASSES (description)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT ec1, ec2, description FROM subclasses WHERE description LIKE l_search_string) LOOP
          l_name:=r_italic.description;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2, r_italic.description, l_name);

          UPDATE subclasses
              SET description=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2;
      END LOOP;

   --SUB-SUBCLASSES (name)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT ec1, ec2, ec3, name FROM subsubclasses WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2||r_italic.ec3, r_italic.name, l_name);

          UPDATE subsubclasses
              SET name=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2 AND ec3=r_italic.ec3;
      END LOOP;

   --SUB-SUBCLASSES (description)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT ec1, ec2, ec3, description FROM subsubclasses WHERE description LIKE l_search_string) LOOP
          l_name:=r_italic.description;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2||r_italic.ec3, r_italic.description, l_name);

          UPDATE subsubclasses
              SET description=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2 AND ec3=r_italic.ec3;
      END LOOP;

   --COMMENTS

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT enzyme_id, comment_text FROM comments WHERE comment_text LIKE l_search_string) LOOP
          l_name:=r_italic.comment_text;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.enzyme_id, r_italic.comment_text, l_name);

          UPDATE comments
              SET comment_text=l_name
              WHERE enzyme_id=r_italic.enzyme_id;
      END LOOP;

   --FUTURE_EVENTS

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT group_id, event_id, event_note FROM future_events WHERE event_note LIKE l_search_string) LOOP
          l_name:=r_italic.event_note;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.group_id||r_italic.event_id, r_italic.event_note, l_name);

          UPDATE future_events
              SET event_note=l_name
              WHERE group_id=r_italic.group_id AND event_id=r_italic.event_id;
      END LOOP;

   --HISTORY_EVENTS

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT group_id, event_id, event_note FROM history_events WHERE event_note LIKE l_search_string) LOOP
          l_name:=r_italic.event_note;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.group_id||r_italic.event_id, r_italic.event_note, l_name);

          UPDATE history_events
              SET event_note=l_name
              WHERE group_id=r_italic.group_id AND event_id=r_italic.event_id;
      END LOOP;

   --REACTIONS

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT enzyme_id, equation FROM reactions WHERE equation LIKE l_search_string) LOOP
          l_name:=r_italic.equation;
          --dbms_output.put_line('ID: '||r_italic.enzyme_id);
          --dbms_output.put_line('Before '||l_name);
          l_name := replace (l_name, '-</i>' , '</i>-');
          --dbms_output.put_line('After '||l_name);


          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.enzyme_id, r_italic.equation, l_name);

          UPDATE reactions
              SET equation=l_name
              WHERE enzyme_id=r_italic.enzyme_id AND equation=r_italic.equation;
      END LOOP;

   --PUBLICATIONS (author)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT pub_id, author FROM publications WHERE author LIKE l_search_string) LOOP
          l_name:=r_italic.author;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.author, l_name);

          UPDATE publications
              SET author=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;

   --PUBLICATIONS (title)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT pub_id, title FROM publications WHERE title LIKE l_search_string) LOOP
          l_name:=r_italic.title;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.title, l_name);

          UPDATE publications
              SET title=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;

   --PUBLICATIONS (journal_book)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT pub_id, journal_book FROM publications WHERE journal_book LIKE l_search_string) LOOP
          l_name:=r_italic.journal_book;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.journal_book, l_name);

          UPDATE publications
              SET journal_book=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;

   --PUBLICATIONS (editor)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT pub_id, editor FROM publications WHERE editor LIKE l_search_string) LOOP
          l_name:=r_italic.editor;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.editor, l_name);

          UPDATE publications
              SET editor=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;

   --PUBLICATIONS (pub_company)

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT pub_id, pub_company FROM publications WHERE pub_company LIKE l_search_string) LOOP
          l_name:=r_italic.pub_company;
          l_name := replace (l_name, '-</i>' , '</i>-');

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.pub_company, l_name);

          UPDATE publications
              SET pub_company=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;

   --NAMES

      l_search_string := '%<i>%-</i>%';
      FOR r_italic IN (SELECT enzyme_id, name, name_class FROM names WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          --dbms_output.put_line('Before '||l_name);
          l_name := replace (l_name, '-</i>' , '</i>-');
          --dbms_output.put_line('After '||l_name);

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.enzyme_id, r_italic.name, l_name);

          UPDATE names
              SET name=l_name
              WHERE enzyme_id=r_italic.enzyme_id AND name=r_italic.name AND name_class=r_italic.name_class;
      END LOOP;
END;
/



