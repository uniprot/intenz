-- Delete legacy links (currently automated, based on EC numbers as identifiers)
delete from links where display_name in ('BRENDA','ERGO','EXPASY','KEGG');