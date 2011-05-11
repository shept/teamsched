SET search_path = ts6, pg_catalog;

GRANT ALL ON 
	issue, 
	timesheet,
	ttmpdelete,
	token,
	user1, 
	userAgent, 
	userScreen, 
	userinvitation, 
	userworkgroup, 
	workgroup, 
	loginLog,
	version
TO postgres;

GRANT SELECT, INSERT, UPDATE, DELETE ON 
	issue, 
	timesheet, 
	ttmpdelete,
	token,
	user1, 
	userAgent, 
	userScreen, 
	userinvitation, 
	userworkgroup, 
	workgroup, 
	loginLog,
	version
TO teamsched_group;

GRANT SELECT, UPDATE ON 
	issue_id_seq, 
	timesheet_id_seq,
	token_id_seq,
	user1_id_seq,
	useragent_id_seq,
	userscreen_id_seq,
	workgroup_id_seq,
	loginlog_id_seq,
	version_id_seq
TO teamsched_group; 
