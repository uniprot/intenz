-- More space for volumes, as they are sometimes 'Supplement'

alter table publications_audit
modify volume varchar2(15);

alter table publications
modify volume varchar2(15);
