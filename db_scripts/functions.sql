

-- ============== Ausgabe detailierter Buchungen
create or replace function get_plain_buchungen (date,date)
returns table (
    buchung_id          int,
    datum               date,
    betrag              numeric (8,2),
    betreff             varchar,
    ziel_name           varchar,
    quelle_name         varchar,
    budget_typ_name     varchar,
    ausgabengrp_name    varchar
    )
as $$  
begin
return query
select
    buchung.buchung_id as buchung_id,
    buchung.datum as datum,
    buchung.betrag as betrag,
    buchung.betreff as betreff,
    zielkonto.konto_name as ziel_name,
    quellekonto.konto_name as quelle_name,
    bud.budget_typ_name as budget_typ_name,
    ausg.ausgabegrp_name as ausgabengrp_name
from 
    buchung
left join
    (select konto_id, konto_name from konto) as zielkonto
    on (zielkonto.konto_id=buchung.ziel)
left join
    (select konto_id, konto_name from konto) as quellekonto
    on (quellekonto.konto_id=buchung.quelle)
left join
    (select budget_typ.budget_typ_id, budget_typ.budget_typ_name from budget_typ) as bud
    on (bud.budget_typ_id=buchung.budget_relevant)
left join
    (select ausgabengruppe.ausgabegrp_id, ausgabengruppe.ausgabegrp_name from ausgabengruppe) as ausg
    on (ausg.ausgabegrp_id=buchung.ausgaben_typ)
where
    buchung.datum >=$1   -- Buchung im Zeitrahmen?
    and buchung.datum <=$2   -- Buchung im Zeitrahmen?
order by
    buchung.datum;
end;	
$$
LANGUAGE 'plpgsql';

-- ============== Ausgabe der Ausgaben als Liste
create or replace function get_all_ausgaben (date,date)
returns table (
    ausgabegrp_id       int,
    ausgabegrp_name     varchar,
    ausgaben_sum        numeric (8,2) -- money-werte geben Fehler im jdbc
    )
as $$  
begin
return query

    select
        ausgabengruppe.ausgabegrp_id as ausgabegrp_id,
        ausgabengruppe.ausgabegrp_name as ausgabegrp_name,
        sum (get_ausgaben(ausgabengruppe.ausgabegrp_id,$1,$2)) as ausgaben_sum
    from 
        ausgabengruppe
    group by
        ausgabengruppe.ausgabegrp_id,
        ausgabengruppe.ausgabegrp_name
    order by
        ausgabengruppe.ausgabegrp_id;
end;	
$$
LANGUAGE 'plpgsql';

-- ============== Ausgabe der Ausgaben zu einem Typ
create or replace function get_ausgaben (int,date,date)
returns numeric (8,2)
as $$  
declare
ausgaben numeric (8,2):='0.0';
begin

select -- absolute Signing zum Saldieren, Vorzeichen drehen, damit positiver Wert
    COALESCE('0'-sum(absolutesigning(buchung.ziel,'true',buchung.betrag)),'0')
into
    ausgaben 
from 
    buchung
where 
    buchung.ausgaben_typ =$1   -- nur gesuchter Ausgaben-Typ
    and buchung.datum >=$2   -- Buchung im Zeitrahmen?
    and buchung.datum <=$3;   -- Buchung im Zeitrahmen?

return ausgaben;
end;	
$$
LANGUAGE 'plpgsql';

-- ============== Tabelle Budgets

create or replace view all_budgets_ever
as
select   
    budget_typ.budget_typ_id as budget_typ_id,
    budget_typ.budget_typ_name as budget_name,
    budget.budget_id as budget_id,
    budget.budget_betrag as budget_betrag,
    budget.start_date as start_date,
    budget.end_date as end_date
from 
    budget,
    budget_typ
where
    budget.typ=budget_typ.budget_typ_id -- joint budget-tabellen
order by
    budget_id;

-- ============== Budget- Übersicht

create or replace function get_all_budgets (date)
returns table (
    budget_typ_id   int,
    budget_name     varchar,
    budget_urspr    numeric (8,2), -- Ursprünglicher Betrag -- money-werte geben Fehler im jdbc
    budget_rest     numeric (8,2) -- absouluter Restbetrag
    )
as $$  
begin
return query
select   
    budget_typ.budget_typ_id as budget_typ_id,
    budget_typ.budget_typ_name as budget_name,
    budget.budget_betrag as budget_urspr,
    get_budget (budget_typ.budget_typ_id,'2013-01-16') as budget_rest
from 
    budget,
    budget_typ
where
    budget.typ=budget_typ.budget_typ_id -- joint budget-tabellen
    and budget.start_date <= '2013-01-16'-- Budget im Zeitrahmen?
    and budget.end_date >='2013-01-16' -- Budget im Zeitrahmen?
order by
    budget_typ_id;


end;	
$$
LANGUAGE 'plpgsql';

-- ============== Einzelner Budgetstand
create or replace function get_budget (int,date)
    returns numeric (8,2)
as $$  
declare
    budget_rest numeric (8,2):='0.0';
begin

select 
    (aktiv_budget.bd_amount+sum(absoluteSigning(buchung.ziel,'true',buchung.betrag)))as sum
into
    budget_rest 
from 
    buchung
join -- Budget-Tabellen zusammenführen + zeitlich relevantes Budget ausgesucht
    ( 
        select   
            budget_typ.budget_typ_id as bd_typ_id,
            budget_typ.budget_typ_name as bd_typ_name,
            budget.budget_betrag as bd_amount,
            budget.start_date as start,
            budget.end_date as end
        from 
            budget,
            budget_typ
        where
            budget.typ=budget_typ.budget_typ_id -- joint budget-tabellen
            and budget.start_date <=$2 -- Budget im Zeitrahmen?
            and budget.end_date >=$2 -- Budget im Zeitrahmen?
            and budget_typ.budget_typ_id=$1 -- nur gesuchtes Budget
     ) as aktiv_budget
    on (buchung.budget_relevant=aktiv_budget.bd_typ_id)  -- joint: nur aktive Budgets
where 
    buchung.datum >=aktiv_budget.start   -- Buchung im Zeitrahmen?
    and buchung.datum <=aktiv_budget.end   -- Buchung im Zeitrahmen?
Group by
    aktiv_budget.bd_typ_id,
    aktiv_budget.bd_typ_name,
    aktiv_budget.bd_amount;

return budget_rest;
end;	
$$
LANGUAGE 'plpgsql';


-- ============== Ermitteln ob absolut gesehen positiver oder negativer Buchungswert
create or replace function absoluteSigning (integer, boolean, numeric (8,2))
-- Parameter: Konto_id, Konto war Ziel? (Ziel=true, Quelle=false), Betrag
returns numeric (8,2)
as $$  
declare
newSignedMoney numeric (8,2):=$3;
positivKonto boolean:='false';
begin
Select konto.positiv into positivKonto from Konto where konto.konto_id=$1;
if (not positivKonto) then 
    newSignedMoney:='0'-newSignedMoney; -- Wenn nicht positiv Konto Vorzeichen umdrehen
end if;
if (not $2) then 
    newSignedMoney:='0'-newSignedMoney; -- Wenn nicht Ziel dann Vorzeichen umdrehen
end if;

return newSignedMoney;
end;	
$$
LANGUAGE 'plpgsql';


-- ============== Super Kontostand 
create or replace function get_supersaldo (date)
-- Parameter: Stichdatum
returns numeric
as $$  
declare
supersaldo numeric:=0.0;
begin
select  
    sum (saldo)
into
    supersaldo
from    
    konto,
    get_salden($1) as salden
where 
    konto.eigentum='1'
    and konto.konto_id=salden.konto_id;
return supersaldo;
end;	
$$
LANGUAGE 'plpgsql';



-- ============== Einzelner Kontostand
create or replace function get_saldo (int,date)
returns table (
    saldo       numeric (8,2)
    )
as $$  
begin
return query
select  
    (COALESCE(positiv.sum,'0')-COALESCE(negativ.sum,'0')) as saldo
from    
    (
        select 
            sum(buchung.betrag) as sum
        from 
            buchung
        where 
            buchung.ziel=$1
            and buchung.datum <=$2
    ) as positiv
cross join
    (
        select 
            sum(buchung.betrag) as sum
        from  
            buchung
        where 
            buchung.quelle=$1
            and buchung.datum <=$2
    ) as negativ;
end;	
$$
LANGUAGE 'plpgsql';


-- ============== Kontostand- Übersicht
create or replace function get_salden (date)
returns table (
    konto_name  varchar,
    konto_id    int,
    saldo       numeric (8,2) -- money-werte geben Fehler im jdbc
    )
as $$  
begin
return query
select
    konto.konto_name as konto_name,
    salden.konto_id as konto_id,
    salden.saldo as saldo
from
    konto,
    (
        select  
            (COALESCE(positiv.sum,'0')-COALESCE(negativ.sum,'0')) as saldo,
            COALESCE(positiv.kto_id, negativ.kto_id) as konto_id 
        from    
            (
                select 
                    sum(buchung.betrag) as sum,
                    konto.konto_id as kto_id 
                from 
                    buchung,
                    konto 
                where 
                    buchung.ziel=konto.konto_id
                    and buchung.datum <=$1
                group by konto.konto_id
            ) as positiv
        full outer join
            (
                select 
                    sum(buchung.betrag) as sum,
                    konto.konto_id as kto_id 
                from  
                    buchung,
                    konto 
                where 
                    buchung.quelle=konto.konto_id
                    and buchung.datum <=$1
                group by konto.konto_id
            ) as negativ
        on (positiv.kto_id=negativ.kto_id)
    ) as salden
where
    (salden.konto_id=konto.konto_id); 
end;	
$$
LANGUAGE 'plpgsql';



