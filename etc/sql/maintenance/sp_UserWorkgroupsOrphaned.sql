-- search for workgroups in userworkgroup
-- where the referencing user_id is not the owner 
-- and the workgroup is not on the invited by somebody else
set search_path to ts5;
select * from userworkgroup 
left join workgroup on workgroup_id = id
where user_id not in (select userinvited_id from userinvitation) 
	and user_id <> owner_id;

