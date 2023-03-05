-- masking extention
drop extension if exists postmask;
create extension postmask;

-- data tables
create table if not exists patient_table (
    patient_id serial primary key,
    patient_name text not null,
    patient_birthday date not null,
    patient_phone text not null,
    patient_address text not null
);

create table if not exists diagnose_table (
    diagnose_id serial primary key,
    diagnose_date date not null,
    diagnose_content text not null,
    patient_id integer not null
);

-- masking views
create view patient as 
select 
patient_id, 
mask_str_with_replace('patient_name', patient_name, 'ACCESS DENIED') as patient_name, 
mask_date_with_noise('patient_birthday', patient_birthday, 0.005) as patient_birthday,
mask_str_with_partial('patient_phone', patient_phone, 3, 'XXXXXXX', 1) as patient_phone,
mask_str_with_fake_province('patient_address', patient_address) as patient_address
from patient_table;

create or replace rule patient_insert as on insert to patient do instead 
insert into patient_table (patient_name, patient_birthday, patient_phone, patient_address)
values (new.patient_name, new.patient_birthday, new.patient_phone, new.patient_address);

create or replace rule patient_update as on update to patient do instead
update patient_table 
set patient_name = new.patient_name,
patient_birthday = new.patient_birthday,
patient_phone = new.patient_phone,
patient_address = new.patient_address
where patient_id = old.patient_id;

create or replace rule patient_delete as on delete to patient do instead
delete from patient_table
where patient_id = old.patient_id;

create view diagnose as 
select 
diagnose_id, 
mask_date_with_noise('diagnose_date', diagnose_date, 0.2) as diagnose_date,
mask_str_with_replace('diagnose_content', diagnose_content, 'ACCESS DENIED') as diagnose_content,
patient_id
from diagnose_table;

create or replace rule diagnose_insert as on insert to diagnose do instead 
insert into diagnose_table (diagnose_content, diagnose_date, patient_id)
values (new.diagnose_content, new.diagnose_date, new.patient_id);

create or replace rule diagnose_update as on update to diagnose do instead
update diagnose_table 
set diagnose_content = new.diagnose_content,
diagnose_date = new.diagnose_date,
patient_id = new.patient_id
where diagnose_id = old.diagnose_id;

create or replace rule diagnose_delete as on delete to diagnose do instead
delete from diagnose_table
where diagnose_id = old.diagnose_id;

-- all permission assign for admin;
select assign_object_to_group('patient_name', 'admin');
select assign_object_to_group('patient_birthday', 'admin');
select assign_object_to_group('patient_phone', 'admin');
select assign_object_to_group('diagnose_date', 'admin');
select assign_object_to_group('diagnose_content', 'admin');

-- permission assign for doctor;
select assign_object_to_group('patient_name', 'doctor');
select assign_object_to_group('diagnose_date', 'doctor');
select assign_object_to_group('diagnose_content', 'doctor');

-- permission assign for assistant;
select assign_object_to_group('patient_name', 'assistant');
select assign_object_to_group('patient_address', 'doctor');
select assign_object_to_group('patient_phone', 'assistant');
select assign_object_to_group('patient_date', 'assistant');

-- permission assign for analyzer;
select assign_object_to_group('patient_address', 'doctor');
select assign_object_to_group('diagnose_date', 'analyzer');
select assign_object_to_group('diagnose_content', 'analyzer');
