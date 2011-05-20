/*
Preliminary EC numbers in UniProt/ENZYME which were not created in IntEnz, and
turned out to have an official EC number by NC-IUMBM.
Had to create the inactive preliminary entries, then register their transfer
so that this appears in enzyme.dat.
*/

set serveroutput on

declare
  type ec is varray(4) of number;
  type ec_pair is varray(2) of ec;
  type ec_pairs is varray(32) of ec_pair;
  prelimEcsToTransfer ec_pairs;
begin
  prelimEcsToTransfer := ec_pairs(
    ec_pair(ec(1,1,1,2), ec(1,1,1,300)),
    ec_pair(ec(1,5,3,1), ec(1,5,3,16)),
    ec_pair(ec(1,5,3,2), ec(1,5,3,16)),
    ec_pair(ec(1,5,3,3), ec(1,5,3,13)),
    ec_pair(ec(1,5,3,4), ec(1,5,3,13)),
    ec_pair(ec(1,5,3,5), ec(1,5,3,16)),
    ec_pair(ec(1,5,3,6), ec(1,5,3,14)),
    ec_pair(ec(1,5,3,7), ec(1,5,3,14)),
    ec_pair(ec(1,5,3,8), ec(1,5,3,14)),
    ec_pair(ec(1,5,3,9), ec(1,5,3,14)),
    ec_pair(ec(1,5,3,10), ec(1,5,3,13)),
    ec_pair(ec(1,14,19,1), ec(1,14,19,4)),
    ec_pair(ec(1,14,19,2), ec(1,14,19,5)),
    ec_pair(ec(1,14,19,3), ec(1,14,19,6)),
    ec_pair(ec(2,4,1,1), ec(2,4,1,245)),
    ec_pair(ec(2,5,1,1), ec(2,2,1,9)),
    ec_pair(ec(2,5,1,3), ec(2,5,1,73)),
    ec_pair(ec(2,7,1,2), ec(2,7,1,161)),
    ec_pair(ec(2,7,1,3), ec(2,7,1,164)),
    ec_pair(ec(2,7,1,6), ec(2,7,1,163)),
    ec_pair(ec(2,7,7,2), ec(2,7,7,67)),
    ec_pair(ec(2,9,1,1), ec(2,9,1,2)),
    ec_pair(ec(3,1,26,1), ec(3,1,26,12)),
    ec_pair(ec(3,1,3,3), ec(3,1,3,78)),
    ec_pair(ec(3,1,4,1), ec(3,1,4,53)),
    ec_pair(ec(3,4,11,1), ec(3,4,11,24)),
    ec_pair(ec(3,5,1,2), ec(3,5,1,99)),
    ec_pair(ec(4,2,3,1), ec(4,2,3,38)),
    ec_pair(ec(4,2,3,9), ec(4,2,3,44)),
    ec_pair(ec(4,2,99,1), ec(4,2,99,20)),
    ec_pair(ec(6,1,1,1), ec(6,3,1,13)),
    ec_pair(ec(6,1,1,2), ec(6,1,1,27))
    );
  for i in prelimEcsToTransfer.first .. prelimEcsToTransfer.last
  loop
      dbms_output.put_line(prelimEcsToTransfer(i)(1)(1)
        ||'.'||prelimEcsToTransfer(i)(1)(2)
        ||'.'||prelimEcsToTransfer(i)(1)(3)
        ||'.n'||prelimEcsToTransfer(i)(1)(4)
        ||' -> '||prelimEcsToTransfer(i)(2)(1)
        ||'.'||prelimEcsToTransfer(i)(2)(2)
        ||'.'||prelimEcsToTransfer(i)(2)(3)
        ||'.'||prelimEcsToTransfer(i)(2)(4));
      
      insert into enzymes (enzyme_id,active,ec1,ec2,ec3,ec4,status,source)
      values (s_enzyme_id.nextval,'N',
        prelimEcsToTransfer(i)(1)(1),
        prelimEcsToTransfer(i)(1)(2),
        prelimEcsToTransfer(i)(1)(3),
        prelimEcsToTransfer(i)(1)(4),
        'PM','INTENZ');
      
      insert into enzyme.history_events
      (group_id, event_id, before_id, after_id, event_class, event_note)
      values
      (s_history_group_id.nextval, s_history_event_id.nextval,
      (select enzyme_id from enzymes
        where ec1=prelimEcsToTransfer(i)(1)(1)
        and ec2=prelimEcsToTransfer(i)(1)(2)
        and ec3=prelimEcsToTransfer(i)(1)(3)
        and ec4=prelimEcsToTransfer(i)(1)(4)
        and status='PM' and active='N'),
      (select enzyme_id from enzymes
        where ec1=prelimEcsToTransfer(i)(2)(1)
        and ec2=prelimEcsToTransfer(i)(2)(2)
        and ec3=prelimEcsToTransfer(i)(2)(3)
        and ec4=prelimEcsToTransfer(i)(2)(4)
        and status='OK' and active='Y'),
      'TRA', 'Now EC '||prelimEcsToTransfer(i)(2)(1)
        ||'.'||prelimEcsToTransfer(i)(2)(2)
        ||'.'||prelimEcsToTransfer(i)(2)(3)
        ||'.'||prelimEcsToTransfer(i)(2)(4));
      
  end loop;
end;
/
