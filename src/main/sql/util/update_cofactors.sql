SET SERVEROUTPUT ON;

DECLARE
  remCofactors number := 0;
  theChebiId number;
  newName varchar2(4000);
  newChemData varchar2(512);
BEGIN
  /* Remove any compounds which are not used as cofactors: */
  for reg in (SELECT compound_id FROM compound_data cd
      where not exists (
      select 1 from cofactors where cofactors.compound_id = cd.compound_id))
  loop
    --DBMS_OUTPUT.PUT_LINE(reg.compound_id || ' should be removed from COMPOUND_DATA table');
    remCofactors := remCofactors + 1;
  end loop;
  DBMS_OUTPUT.PUT_LINE('Compounds to remove: ' || remCofactors);
  
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
      DBMS_OUTPUT.PUT_LINE(reg.c_id || ' *MERGED* to ' || reg.parent_id);
      theChebiId := reg.parent_id;
      -- TODO: update the ChEBI ID
    end if;
    
    -- Compare names:
    begin
      select cn.name into newName
        from chebi.names@chezpro cn, chebi.compounds@chezpro cc
        where (cc.id in (reg.c_id, theChebiId) or cc.parent_id = theChebiId)
        and cc.id = cn.compound_id
        and cn.source in ('UniProt','ChEBI')
        and cn.type = 'NAME'
        and cn.status in ('C','S')
        order by cn.source desc, cn.status asc;
      if (newName != reg.i_name) then
        -- TODO: update name
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' *name CHANGED* from ' || reg.i_name
          || ' to ' || newName);
      else
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' name unchanged: ' || reg.i_name);
      end if;
    exception
      when no_data_found then
        if (reg.i_name != reg.c_name) then
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' *name ERROR* - was: ' || reg.i_name
            || '; found in ChEBI: ' || reg.c_name );
        else
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' name (ChEBI) unchanged: '
            || reg.i_name);
        end if;
      when others then
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' name *ERROR* - ' || SQLERRM);
    end;
    
    -- Formula:
    begin
      select cd.chemical_data into newChemData
        from chebi.chemical_data@chezpro cd, chebi.compounds@chezpro cc
        where (cc.id in (reg.c_id, theChebiId) or cc.parent_id = theChebiId)
        and cc.id = cd.compound_id
        and cd.type = 'FORMULA'
        and cd.status in ('C','S');
      if (reg.i_formula != newChemData) then
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' *formula CHANGED* from '
            || reg.i_formula || ' to ' || newChemData );
          -- TODO: update formula
      else
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' formula unchanged: ' || reg.i_formula);
      end if;
    exception
      when no_data_found then
        if (reg.i_formula is not null) then
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' *formula VANISHED*: ' || reg.i_formula);
        else
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' formula unchanged: ' || reg.i_formula);
        end if;
      when others then
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' *formula ERROR* - ' || SQLERRM);
    end;
    
    -- Charge:
    begin
      select cd.chemical_data into newChemData
        from chebi.chemical_data@chezpro cd, chebi.compounds@chezpro cc
        where (cc.id in (reg.c_id, theChebiId) or cc.parent_id = theChebiId)
        and cc.id = cd.compound_id
        and cd.type = 'CHARGE'
        and cd.status in ('C','S');
      if (reg.i_charge != newChemData) then
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' *charge CHANGED* from '
            || reg.i_charge || ' to ' || newChemData );
          -- TODO: update charge
      else
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' charge unchanged: ' || reg.i_charge);
      end if;
    exception
      when no_data_found then
        if (reg.i_charge != 0) then
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' *charge VANISHED*: ' || reg.i_charge);
        else
          DBMS_OUTPUT.PUT_LINE(theChebiId || ' charge unchanged: ' || reg.i_charge);
        end if;
      when others then
        DBMS_OUTPUT.PUT_LINE(theChebiId || ' *charge ERROR* - ' || SQLERRM);
    end;
  end loop;
END;
/
