CREATE OR REPLACE FUNCTION check_buchung_for_doubles () RETURNS void 

AS $$
DECLARE
first_pick record;
doubles_found record;
doubles_counter integer;
 
BEGIN
create temp table doubles_removed (id integer); 
for first_pick in (
    select
        *
    from
        buchung
    )
Loop
    -- raise notice 'first_pick: %', first_pick;
    for doubles_found in (
        select
            *
        from
            buchung
        where
            datum=first_pick.datum and
            betrag=first_pick.betrag and
            betreff=first_pick.betreff and
            ziel=first_pick.ziel and
            quelle=first_pick.quelle and
            budget_relevant=first_pick.budget_relevant and
            ausgaben_typ=first_pick.ausgaben_typ and
            COALESCE(repeat,'0')=COALESCE(first_pick.repeat,'0') and
            buchung_id!=first_pick.buchung_id
        )
    Loop
        select count (id) from doubles_removed where doubles_removed.id=first_pick.buchung_id into doubles_counter;
        if (doubles_counter=0) then
            raise notice 'doubles_found: %', doubles_found;
            insert into backup_doubles_buchung (buchung_id, datum, betrag, betreff, ziel, quelle, budget_relevant, ausgaben_typ, repeat)
                values (
                    doubles_found.buchung_id, 
                    doubles_found.datum, 
                    doubles_found.betrag, 
                    doubles_found.betreff, 
                    doubles_found.ziel, 
                    doubles_found.quelle, 
                    doubles_found.budget_relevant, 
                    doubles_found.ausgaben_typ, 
                    doubles_found.repeat
                );
            delete from buchung where buchung_id=doubles_found.buchung_id;
            insert into doubles_removed (id) values (doubles_found.buchung_id);
        end if;
    end loop;
end loop;    
drop table doubles_removed;
END; $$
language 'plpgsql';

select check_buchung_for_doubles ();
select * from backup_doubles_buchung;
drop function check_buchung_for_doubles ();