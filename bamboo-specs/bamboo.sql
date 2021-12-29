select * from plan;

select * from project;

truncate table plan;

truncate table project;

SET FOREIGN_KEY_CHECKS = 0; 
TRUNCATE table project; 
SET FOREIGN_KEY_CHECKS = 1;

drop table project;
drop table plan;



create table project (project_id integer not null, project_key varchar(255), project_name varchar(255), primary key (project_id)) ;
 alter table plan add constraint FKmcne7qnx1nd4tabjmdfci2bwk foreign key (project_id) references project (project_id);
 
 
 
 create table plan (plan_id integer not null, plan_description varchar(255), plan_key varchar(255), plan_name varchar(255), project_id integer, primary key (plan_id)) ;