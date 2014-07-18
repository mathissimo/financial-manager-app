-- ============== Trigger-Function: Stop delete of non-deletable rows
CREATE OR REPLACE FUNCTION stop_delete_row () RETURNS trigger 

AS $$
DECLARE
BEGIN
    if (old.deletable=false) then 
        raise exception 'This row cannot be deleted';
    end if;
return old;
END; $$
language 'plpgsql';

-- ============== Trigger: Stop delete of nobudget-row
create trigger trigger_stop_delete_nobudget_row 
before 
    delete
on 
    budget
for each row execute procedure
    stop_delete_row();

-- ============== Trigger: Stop delete of sonstiges-ausgabengruppe
create trigger trigger_stop_delete_sonstiges_row 
before 
    delete
on 
    ausgabengruppe
for each row execute procedure
    stop_delete_row();
