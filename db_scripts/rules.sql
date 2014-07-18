
-- ============== Regeln um Start<End Date bei Budgets zu prüfen

create rule update_start_before_end_budget as on
    update to budget where new.start_date>new.end_date
    do instead select raise_exception_over_plpgsql (1);

-- Konflikt bei insert - > kein return-statement möglich!
-- create rule insert_start_before_end_budget as on
--     insert to budget where new.start_date>new.end_date
--     do instead select raise_exception_over_plpgsql (1);


-- ============== Ausgabe-Function für Exceptions
CREATE OR REPLACE FUNCTION raise_exception_over_plpgsql (int) RETURNS void 

AS $$
DECLARE
BEGIN
if ($1=1) then
    raise exception 'Ending date before starting date';
    end if;
END; $$
language 'plpgsql';
