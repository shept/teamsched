-- Function: ts6.pradmdeletehist(timestamp without time zone)

-- DROP FUNCTION ts6.pradmdeletehist(timestamp without time zone);

CREATE OR REPLACE FUNCTION ts6.pradmdeletehist(datparm timestamp without time zone)
  RETURNS integer AS
$BODY$
BEGIN

set search_path=ts6;

-- remove timesheets --
delete from timesheet where datecreated < datparm;

-- remove deleted issues --
insert into ttmpdelete Select id from issue where fdel != 0;
-- remove all issues (including undeleted) that belong to deleted users if they are no logner referenced --
insert into ttmpdelete 
	select id from issue where workgroup_id in (Select id from workgroup where owner_id in (Select id from user1 where fdel != 0));
-- remove all issues (including undeleted) that belong to deleted workgroups if they are no logner referenced --
insert into ttmpdelete 
	select id from issue where workgroup_id in (Select id from workgroup where fdel != 0);
-- exclude existing references --
delete from ttmpdelete where id in (Select issue_id from timesheet);
delete from ttmpdelete where id in (Select issue_id from issue);  -- remove issues with children from deletion list --
delete from issue where id in (Select id from ttmpdelete);
delete from ttmpdelete;


-- remove deleted workgroups that are no longer referenced --
insert into ttmpdelete Select id from workgroup where fdel != 0;
-- collect workgroups (including undeleted) of deleted users --
insert into ttmpdelete
	select id from workgroup where owner_id in (Select id from user1 where fdel != 0);
-- remove those whch are still referenced --
delete from ttmpdelete where id in (Select workgroup_id from issue); 
delete from userworkgroup where workgroup_id in (Select id from ttmpdelete);
delete from workgroup where id in (select id from ttmpdelete);
delete from ttmpdelete;


-- remove deleted users --
insert into ttmpdelete Select id from user1 where fdel != 0;
delete from ttmpdelete where id in (Select owner_id from workgroup);

delete from userinvitation where userhost_id in (Select id from ttmpdelete);
delete from userinvitation where userinvited_id in (Select id from ttmpdelete);
delete from userworkgroup where user_id in (Select id from ttmpdelete);
update loginlog set user_id = null where user_id in (Select id from ttmpdelete);
update user1 set datterms = null, confirming_user_id = null where confirming_user_id in (Select id from ttmpdelete);
update user1 set parent_user_id = null where parent_user_id in (Select id from ttmpdelete);
delete from token where user_id in (Select id from ttmpdelete);
delete from user1 where id in (Select id from ttmpdelete);
delete from ttmpdelete;


return 0 ;
END 
  $BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION ts6.pradmdeletehist(timestamp without time zone) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION ts6.pradmdeletehist(timestamp without time zone) TO postgres;
GRANT EXECUTE ON FUNCTION ts6.pradmdeletehist(timestamp without time zone) TO public;
GRANT EXECUTE ON FUNCTION ts6.pradmdeletehist(timestamp without time zone) TO teamsched_group;
