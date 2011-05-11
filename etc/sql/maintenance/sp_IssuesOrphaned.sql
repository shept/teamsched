-- search for issue which belong to workgroups that do not have an owner
-- (where onwer is null)
set search_path to ts5;
select * from issue, workgroup 
where issue.workgroup_id = workgroup.id
and workgroup.owner_id is null
