--------------------------------------------------------
--  DDL for Function F_CLASSES
--------------------------------------------------------

DROP FUNCTION IF EXISTS F_CLASSES;
CREATE FUNCTION F_CLASSES (p_ec1 int(1), p_ec2 int(2), p_ec3 int(3))
RETURNS varchar2
begin
	DECLARE separator char(1) DEFAULT ',';
	DECLARE output    varchar(32000) DEFAULT '';

  if p_ec2 is null and p_ec3 is null then
    for r_classes (select ec1, name, description from classes where ec1 = p_ec1) loop
       if r_classes.NAME        is not null then output := output || r_classes.NAME          ||separator ; end if;
       if r_classes.DESCRIPTION is not null then output := output || r_classes.DESCRIPTION   ||separator; end if;
    end loop;
    if length(output) > 4000 then
       return ('ERROR !' );
    else
       output := substr(output, 1, 4000);
       return output;
    end if;
  else if p_ec3 is null then
    for r_subclasses (select ec1, ec2, name, description from subclasses where ec1 = p_ec1 and ec2 = p_ec2) loop
       if r_subclasses.NAME        is not null then output := substr(output || r_subclasses.NAME          ||separator,1,4000); end if;
       if r_subclasses.DESCRIPTION is not null then output := substr(output || r_subclasses.DESCRIPTION   ||separator,1,4000); end if;
    end loop;
    if length(output) > 4000 then
       return ('ERROR !' );
    else
       output := substr(output, 1, 4000);
       return output;
    end if;
  else
    for r_subsubclasses (select ec1, ec2, ec3, name, description from subsubclasses where ec1 = p_ec1 and ec2 = p_ec2 and ec3 = p_ec3) loop
       if r_subsubclasses.NAME        is not null then output := substr(output || r_subsubclasses.NAME          ||separator,1,4000); end if;
       if r_subsubclasses.DESCRIPTION is not null then output := substr(output || r_subsubclasses.DESCRIPTION   ||separator,1,4000); end if;
       if length(output) = 4000 then exit; end if;
    end loop;
    if length(output) > 4000 then
       return ('ERROR !' );
    else
       output := substr(output, 1, 4000);
       return output;
    end if;
  end if;
end;

/


--------------------------------------------------------
--  DDL for Function F_QUAD2STRING
--------------------------------------------------------

DROP FUNCTION IF EXISTS F_QUAD2STRING;
CREATE FUNCTION F_QUAD2STRING (
  ec1 NUMBER DEFAULT NULL,
  ec2 NUMBER DEFAULT NULL,
  ec3 NUMBER DEFAULT NULL,
  ec4 NUMBER DEFAULT NULL)
RETURNS CHAR
IS
  result VARCHAR2(255) := '';
BEGIN
  IF ec1 IS NOT NULL THEN
    result := TO_CHAR(ec1);
    IF ec2 IS NOT NULL THEN
      result := result || '.' || TO_CHAR(ec2);
      IF ec3 IS NOT NULL THEN
        result := result || '.' || TO_CHAR(ec3);
        IF ec4 IS NOT NULL THEN
          result := result || '.' || TO_CHAR(ec4);
        END IF;
      END IF;
    END IF;
  END IF;
  RETURN result;
END f_quad2string;

/
