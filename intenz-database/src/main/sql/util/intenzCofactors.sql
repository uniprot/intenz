-- Retrieves data about cofactors (initially requested by JO)
select cf.enzyme_id,
  x.database_ac uniprot_acc,
  f_quad2string(e.ec1,e.ec2,e.ec3,e.ec4) ec,
  cd.name cofactor_name,
  cd.accession cofactor_chebi_id
from cofactors cf, xrefs x, enzymes e, compound_data cd
where cf.enzyme_id = e.enzyme_id
  and e.active = 'Y'
  and e.status in ('OK','PM')
  and cf.enzyme_id = x.enzyme_id
  and x.database_code = 'S'
  and cf.compound_id = cd.compound_id
order by enzyme_id, uniprot_acc
;
