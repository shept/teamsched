-- Search timesheet for entries which do not have an owner ( =orphaned)
-- for delete change from clause to:
-- delete * from timesheet as ts using issue as iss, workgroup wg where ...
set search_path to ts5;
select * from timesheet as ts, issue as iss, workgroup wg where
	ts.issue_id = iss.id and
	iss.workgroup_id = wg.id and
	wg.owner_id is null
