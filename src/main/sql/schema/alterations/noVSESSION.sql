/*
 * The body of the auditpackage is changed just to remove the reference to
 * V$SESSION. The variable osuser will have to be set by an explicit call to the
 * existing method setosuser.
 */

create or replace
PACKAGE BODY          "AUDITPACKAGE"  AS

  PROCEDURE setremark (s IN VARCHAR2) IS
  BEGIN
    remark := s;
    DBMS_APPLICATION_INFO.SET_ACTION(remark);
  END setremark;

  PROCEDURE put (s IN VARCHAR2) IS
  BEGIN
    setremark(s);
  END put;

  PROCEDURE clrremark IS
  BEGIN
    setremark(NULL);
  END clrremark;

  PROCEDURE clr IS
  BEGIN
    clrremark;
  END clr;

  procedure setosuser(s IN varchar2) is
  begin
    osuser := s;
  END setosuser;

END auditpackage;