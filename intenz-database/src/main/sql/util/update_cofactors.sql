/*
Updates IntEnz cofactors (name, ChEBI ID, formula, charge) with any changes in
ChEBI. Sends the report to stdout.
*/

SET SERVEROUTPUT ON;
SET FEEDBACK OFF;
SET ECHO OFF;

DECLARE
  remCofactors number := 0;
  updatedChebiIds number := 0;
  updatedNames number := 0;
  updatedFormulae number := 0;
  updatedCharges number := 0;
  theChebiId number;
  newName varchar2(4000);
  newNameSrc varchar2(512);
  newChemData varchar2(512);
BEGIN
  /* Remove any compounds which are not used as cofactors: */
  for reg in (SELECT compound_id FROM compound_data cd
      where not exists (
      select 1 from cofactors where cofactors.compound_id = cd.compound_id))
  loop
    delete from compound_data where compound_id = reg.compound_id;
    remCofactors := remCofactors + 1;
  end loop;
  
  /* Update remaining compounds (cofactors): */
  for reg in (SELECT
    cd.compound_id, cd.accession,
    cd.name i_name, cd.formula i_formula, cd.charge i_charge,
    cd.published i_published,
    cc.id c_id, cc.parent_id, cc.name c_name, cc.status c_status
    FROM compound_data cd, chebi.compounds@chezpro cc
    where cd.accession = cc.chebi_accession
    and exists (select 1 from cofactors where compound_id = cd.compound_id))
  loop
    if (reg.parent_id is null) then
      theChebiId := reg.c_id;
    else
      DBMS_OUTPUT.PUT_LINE('CHEBI:' || reg.c_id
        || ' *MERGED* to ' || 'CHEBI:' || reg.parent_id);
      theChebiId := reg.parent_id;
      update compound_data set accession = theChebiId
        where accession = reg.c_id;
      updatedChebiIds := updatedChebiIds + 1;
    end if;
    
    -- Compare names:
    begin
      select cn.name, cn.source into newName, newNameSrc
        from chebi.names@chezpro cn, chebi.compounds@chezpro cc
        where (cc.id in (reg.c_id, theChebiId) or cc.parent_id = theChebiId)
        and cc.id = cn.compound_id
        and cn.type = 'NAME'
        and cn.source in ('UniProt','ChEBI')
        and cn.status in ('C','S')
        order by cn.source desc, cn.status asc;
      if (newName != reg.i_name) then
        DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
          || ' *name CHANGED* from ' || reg.i_name
          || ' to ' || newName || ' (source: ' || newNameSrc || ')');
        update compound_data set name = newName
          where accession = 'CHEBI:'||theChebiId;
        updatedNames := updatedNames + 1;
      end if;
    exception
      when no_data_found then
        if (reg.i_name != reg.c_name) then
          DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
            || ' *name not found* - was: ' || reg.i_name
            || '; found in CHEBI.COMPOUND: ' || reg.c_name );
        else
          DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
            || ' name (CHEBI.COMPOUND) unchanged: ' || reg.i_name);
        end if;
      when others then
        DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
          || ' name *ERROR* - ' || SQLERRM);
    end;
    
    -- Formula:
    begin
      select cd.chemical_data into newChemData
        from chebi.chemical_data@chezpro cd, chebi.compounds@chezpro cc
        where (cc.id in (reg.c_id, theChebiId) or cc.parent_id = theChebiId)
        and cc.id = cd.compound_id
        and cd.type = 'FORMULA'
        and cd.status in ('C','S') order by cd.status asc;
      if (reg.i_formula != newChemData) then
          DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
            || ' *formula CHANGED* from ' || reg.i_formula
            || ' to ' || newChemData );
          update compound_data set formula = newChemData
            where accession = 'CHEBI:'||theChebiId;
          updatedFormulae := updatedFormulae + 1;
      end if;
    exception
      when no_data_found then
        if (reg.i_formula is not null) then
          DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
            || ' *formula VANISHED*: ' || reg.i_formula);
        end if;
      when others then
        DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
          || ' *formula ERROR* - ' || SQLERRM);
    end;
    
    -- Charge:
    begin
      select cd.chemical_data into newChemData
        from chebi.chemical_data@chezpro cd, chebi.compounds@chezpro cc
        where (cc.id in (reg.c_id, theChebiId) or cc.parent_id = theChebiId)
        and cc.id = cd.compound_id
        and cd.type = 'CHARGE'
        and cd.status in ('C','S') order by cd.status asc;
      if (reg.i_charge != newChemData) then
          DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
            || ' *charge CHANGED* from ' || reg.i_charge
            || ' to ' || newChemData );
          update compound_data set charge = to_number(newChemData)
            where accession = 'CHEBI:'||theChebiId;
          updatedCharges := updatedCharges + 1;
      end if;
    exception
      when no_data_found then
        if (reg.i_charge != 0) then
          DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
            || ' *charge VANISHED*: ' || reg.i_charge);
        end if;
      when others then
        DBMS_OUTPUT.PUT_LINE('CHEBI:' || theChebiId
          || ' *charge ERROR* - ' || SQLERRM);
    end;
  end loop;
  
  DBMS_OUTPUT.PUT_LINE(CHR(10));
  DBMS_OUTPUT.PUT_LINE('Unused cofactors removed: ' || remCofactors);
  DBMS_OUTPUT.PUT_LINE('Updated ChEBI IDs: ' || updatedChebiIds);
  DBMS_OUTPUT.PUT_LINE('Updated names: ' || updatedNames);
  DBMS_OUTPUT.PUT_LINE('Updated formulae: ' || updatedFormulae);
  DBMS_OUTPUT.PUT_LINE('Updated charges: ' || updatedCharges);

END;
/
