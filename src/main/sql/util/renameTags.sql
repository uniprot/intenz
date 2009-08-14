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
  TYPE string_array IS VARRAY(48) OF VARCHAR2(1024);
  l_name VARCHAR2(4000);
  l_old_html_array string_array;
  l_new_xml_array string_array;

BEGIN
   --dbms_output.enable(10000000);
   EXECUTE IMMEDIATE ('TRUNCATE TABLE temp_debug');

   l_old_html_array := string_array('<small>I</small>', '<small>II</small>', '<small>III</small>', '<small>IV</small>',
                                    '<small>V</small>', '<small>VI</small>', '<small>VII</small>', '<small>VIII</small>',
                                    '<small>LL</small>', '<small>DL</small>', '<small>DD</small>', '<i>RS</i>',
                                    '<i>SR</i>', '<small>D</small>', '<small>L</small>', '<i>E</i>',
                                    '<i>R</i>', '<i>S</i>', '<i>Z</i>', '<i>all</i>-<i>cis</i>',
                                    '<i>all</i>-<i>trans</i>', '<i>cis</i>', '<i>trans</i>', '<i>allo</i>',
                                    '<i>altro</i>', '<i>arabino</i>', '<i>erythro</i>', '<i>galacto</i>',
                                    '<i>gluco</i>', '<i>glycero</i>', '<i>gulo</i>', '<i>ido</i>',
                                    '<i>lyxo</i>', '<i>manno</i>', '<i>ribo</i>', '<i>talo</i>',
                                    '<i>threo</i>', '<i>xylo</i>', '<i>C</i>', '<i>H</i>',
                                    '<i>N</i>', '<i>O</i>', '<i>P</i>', '<i>S</i>',
                                    '<i>f</i>','<i>p</i>', '<i>', '</i>');

   l_new_xml_array := string_array('<oxs>1</oxs>', '<oxs>2</oxs>', '<oxs>3</oxs>', '<oxs>4</oxs>',
                                   '<oxs>5</oxs>', '<oxs>6</oxs>', '<oxs>7</oxs>', '<oxs>8</oxs>',
                                   '<stereo>L</stereo><stereo>L</stereo>', '<stereo>D</stereo><stereo>L</stereo>',
                                   '<stereo>D</stereo><stereo>D</stereo>', '<stereo>R</stereo><stereo>S</stereo>',
                                   '<stereo>S</stereo><stereo>R</stereo>', '<stereo>D</stereo>',
                                   '<stereo>L</stereo>', '<stereo>E</stereo>', '<stereo>R</stereo>', '<stereo>S</stereo>',
                                   '<stereo>Z</stereo>', '<stereo>all-cis</stereo>',
                                   '<stereo>all-trans</stereo>','<stereo>cis</stereo>',
                                   '<stereo>trans</stereo>', '<stereo>allo</stereo>',
                                   '<stereo>altro</stereo>', '<stereo>arabino</stereo>',
                                   '<stereo>erythro</stereo>', '<stereo>galacto</stereo>',
                                   '<stereo>gluco</stereo>', '<stereo>glycero</stereo>',
                                   '<stereo>gulo</stereo>', '<stereo>ido</stereo>',
                                   '<stereo>lyxo</stereo>', '<stereo>manno</stereo>',
                                   '<stereo>ribo</stereo>', '<stereo>talo</stereo>',
                                   '<stereo>threo</stereo>', '<stereo>xylo</stereo>',
                                   '<element>C</element>', '<element>H</element>',
                                   '<element>N</element>', '<element>O</element>', 
                                   '<element>P</element>', '<element>S</element>',
                                   '<ring>f</ring>', '<ring>p</ring>', '<ital>', '</ital>' );

   --CLASSES (name)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT ec1, name FROM classes WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1, r_italic.name, l_name);

          UPDATE classes
              SET name=l_name
              WHERE ec1=r_italic.ec1;
      END LOOP;
   END LOOP;

   --CLASSES (description)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT ec1, description FROM classes WHERE description LIKE l_search_string) LOOP
          l_name:=r_italic.description;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1, r_italic.description, l_name);

          UPDATE classes
              SET description=l_name
              WHERE ec1=r_italic.ec1;
      END LOOP;
   END LOOP;

   --SUBCLASSES (name)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT ec1, ec2, name FROM subclasses WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2, r_italic.name, l_name);

          UPDATE subclasses
              SET name=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2;
      END LOOP;
   END LOOP;

   --SUBCLASSES (description)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT ec1, ec2, description FROM subclasses WHERE description LIKE l_search_string) LOOP
          l_name:=r_italic.description;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2, r_italic.description, l_name);

          UPDATE subclasses
              SET description=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2;
      END LOOP;
   END LOOP;

   --SUB-SUBCLASSES (name)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT ec1, ec2, ec3, name FROM subsubclasses WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2||r_italic.ec3, r_italic.name, l_name);

          UPDATE subsubclasses
              SET name=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2 AND ec3=r_italic.ec3;
      END LOOP;
   END LOOP;

   --SUB-SUBCLASSES (description)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT ec1, ec2, ec3, description FROM subsubclasses WHERE description LIKE l_search_string) LOOP
          l_name:=r_italic.description;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.ec1||r_italic.ec2||r_italic.ec3, r_italic.description, l_name);

          UPDATE subsubclasses
              SET description=l_name
              WHERE ec1=r_italic.ec1 AND ec2=r_italic.ec2 AND ec3=r_italic.ec3;
      END LOOP;
   END LOOP;

   --COMMENTS
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT enzyme_id, comment_text FROM comments WHERE comment_text LIKE l_search_string) LOOP
          l_name:=r_italic.comment_text;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.enzyme_id, r_italic.comment_text, l_name);

          UPDATE comments
              SET comment_text=l_name
              WHERE enzyme_id=r_italic.enzyme_id;
      END LOOP;
   END LOOP;

   --FUTURE_EVENTS
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT group_id, event_id, event_note FROM future_events WHERE event_note LIKE l_search_string) LOOP
          l_name:=r_italic.event_note;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.group_id||r_italic.event_id, r_italic.event_note, l_name);

          UPDATE future_events
              SET event_note=l_name
              WHERE group_id=r_italic.group_id AND event_id=r_italic.event_id;
      END LOOP;
   END LOOP;

   --HISTORY_EVENTS
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT group_id, event_id, event_note FROM history_events WHERE event_note LIKE l_search_string) LOOP
          l_name:=r_italic.event_note;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.group_id||r_italic.event_id, r_italic.event_note, l_name);

          UPDATE history_events
              SET event_note=l_name
              WHERE group_id=r_italic.group_id AND event_id=r_italic.event_id;
      END LOOP;
   END LOOP;

   --REACTIONS
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT enzyme_id, equation FROM reactions WHERE equation LIKE l_search_string) LOOP
          l_name:=r_italic.equation;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.enzyme_id, r_italic.equation, l_name);

          UPDATE reactions
              SET equation=l_name
              WHERE enzyme_id=r_italic.enzyme_id AND equation=r_italic.equation;
      END LOOP;
   END LOOP;

   --PUBLICATIONS (author)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT pub_id, author FROM publications WHERE author LIKE l_search_string) LOOP
          l_name:=r_italic.author;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.author, l_name);

          UPDATE publications
              SET author=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;
   END LOOP;

   --PUBLICATIONS (title)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT pub_id, title FROM publications WHERE title LIKE l_search_string) LOOP
          l_name:=r_italic.title;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.title, l_name);

          UPDATE publications
              SET title=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;
   END LOOP;

   --PUBLICATIONS (journal_book)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT pub_id, journal_book FROM publications WHERE journal_book LIKE l_search_string) LOOP
          l_name:=r_italic.journal_book;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.journal_book, l_name);

          UPDATE publications
              SET journal_book=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;
   END LOOP;

   --PUBLICATIONS (editor)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT pub_id, editor FROM publications WHERE editor LIKE l_search_string) LOOP
          l_name:=r_italic.editor;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.editor, l_name);

          UPDATE publications
              SET editor=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;
   END LOOP;

   --PUBLICATIONS (pub_company)
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT pub_id, pub_company FROM publications WHERE pub_company LIKE l_search_string) LOOP
          l_name:=r_italic.pub_company;
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.pub_id, r_italic.pub_company, l_name);

          UPDATE publications
              SET pub_company=l_name
              WHERE pub_id=r_italic.pub_id;
      END LOOP;
   END LOOP;

   --NAMES
   FOR iii in 1..l_old_html_array.COUNT LOOP
      l_search_string := '%'||l_old_html_array(iii)||'%';
      FOR r_italic IN (SELECT enzyme_id, name, name_class FROM names WHERE name LIKE l_search_string) LOOP
          l_name:=r_italic.name;
          --dbms_output.put_line('Before '||l_name);
          l_name := replace (l_name, l_old_html_array(iii) , l_new_xml_array(iii));
          --dbms_output.put_line('After '||l_name);

          INSERT INTO temp_debug (id, old_name, new_name)
              VALUES (r_italic.enzyme_id, r_italic.name, l_name);

          UPDATE names
              SET name=l_name
              WHERE enzyme_id=r_italic.enzyme_id AND name=r_italic.name AND name_class=r_italic.name_class;
      END LOOP;
   END LOOP;
END;
/



