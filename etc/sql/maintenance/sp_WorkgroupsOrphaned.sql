-- search for workgroups which do not have an owner
-- (where onwer is null)
set search_path to ts5;
select * from workgroup wg where wg.owner_id is null -- order by datecreated