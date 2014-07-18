/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maak
 */

// TODO: Konstanten anlegen für getBuchungEditable / Spalten-Nr. "Datum", "From",..
public class Model {
    private ConnectionDB myDB;
    
    public Model () {
        initConnection ();
    }
    
    private void initConnection () {
        myDB=getFreshConnection(false);
    }
    
    private ConnectionDB getFreshConnection (boolean blockDBwhileEditing) {
        try {
            return new ConnectionDB (blockDBwhileEditing);
        } catch (Exception e) {
            System.out.println("Model initConnection: "+e);
            DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
            e.printStackTrace();
            return null;
        }
    }
    
    private void closeConnection () {
        try {
            myDB.closeConnection();
        } catch (Exception e) {
            System.out.println("Model closeConnection: "+e);
            DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
            e.printStackTrace();
        }
    }
    
    private DefaultTableModel getDTMwithSQL (String sql) {
        // initConnection();
        DefaultTableModel returnValue = TableModelHelper.resultSetToTableModel(myDB.getResultSet(sql),false);
        // closeConnection ();
        return returnValue;
    }
    
    private int updateWithReturnValue (String sql) {
        // initConnection();
        int returnValue = myDB.updateQueryWithIntReturnValue(sql);
        // closeConnection ();
        return returnValue;
    }
    
    public DefaultTableModel getOverviewSalden () {
        String sql =
                "select "
                +   "konto_id as \"ID\", "
                +   "konto_name as \"Konto\", "
                +   "saldo as \"Saldo\" "
                + "from "
                +   "get_salden (current_date) "
                + "order by konto_id";
        return getDTMwithSQL (sql);
    }

    public DefaultTableModel getTableAusgabenTyp () {
        String sql =
                "select "
                +   "ausgabegrp_id as \"ID\", "
                +   "ausgabegrp_name as \"Konto\", "
                +   "budget_neutral as \"Budget Neutral\" "
                + "from "
                +   "ausgabengruppe "
                + "order by ausgabegrp_id";
        
        initConnection();
        return getDTMwithSQL (sql);
    }

    public DefaultTableModel getTableKonto () {
        String sql =
                "select "
                +   "konto_id as \"ID\", "
                +   "konto_name as \"Konto\", "
                +   "eigentum as \"Eigentum\", "
                +   "positiv as \"Positiv buchen\" "
                + "from "
                +   "konto "
                + "order by konto_id";
        return getDTMwithSQL (sql);
    }

    public DefaultTableModel getTableBudgetTyp () {
        String sql =
                "select "
                +   "budget_typ_id as \"ID\", "
                +   "budget_typ_name as \"Budget-Typ\" "
                + "from "
                +   "budget_typ "
                + "order by budget_typ_id";
        return getDTMwithSQL (sql);
    }

    public DefaultTableModel getAllBudgetsEver () {
        String sql =
                "select "
                +   "budget_id as \"ID\", "
                +   "budget_betrag as \"Budget Betrag\", "
                +   "start_date as \"Start\", "
                +   "end_date as \"End\", "
                +   "budget_name as \"Budget-Typ\" "
                + "from "
                +   "all_budgets_ever ";
        return getDTMwithSQL (sql);
    }

    public DefaultTableModel getOverviewKonten () {
        String sql =
                "select "
                +   "konto_id as \"ID\", "
                +   "konto_name as \"Konto\" "
                + "from "
                +   "get_salden (current_date) "
                + "order by konto_id";
        return getDTMwithSQL (sql);
    }

    public DefaultTableModel getOverviewActiveBudgets () {
        String sql =
                "select "
                +   "budget_typ_id as \"ID\", "
                +   "budget_name as \"Budget\", "
                +   "budget_rest as \"Stand.\", "
                +   "budget_urspr as \"Urspr.\" "
                + "from "
                +   "get_all_budgets (current_date) ";
        return getDTMwithSQL (sql);
    }
    
    public DefaultTableModel getOverviewBudgetsByDate (String date) {
        if (date.equals("")) date="current_date";
        String sql =
                "select "
                +   "budget_typ_id as \"ID\", "
                +   "budget_name as \"Budget\" "
                + "from "
                +   "get_all_budgets ('"+date+"') ";
        System.out.println("Model.getTableBudgetAuswahl(): date: "+date);
        return getDTMwithSQL (sql);
    }
    
    public DefaultTableModel getOverviewAusgaben () {
        String sql =
                "select "
                +   "ausgabegrp_id as \"ID\", "
                +   "ausgabegrp_name as \"Gruppe\", "
                +   "ausgaben_sum as \"Stand.\" "
                + "from "
                +   "get_all_ausgaben ('1900-01-01',current_date) ";
        return getDTMwithSQL (sql);
    }
    
    public DefaultTableModel gerOverviewAusgabenByToday () {
        String sql =
                "select "
                +   "ausgabegrp_id as \"ID\", "
                +   "ausgabegrp_name as \"Gruppe\" "
                + "from "
                +   "get_all_ausgaben ('1900-01-01',current_date) ";
        return getDTMwithSQL (sql);
    }
    
    public DefaultTableModel getOverviewBuchungen () {
        String sql =
                "select "
                +   "buchung_id as \"ID\", "
                +   "datum as \"Datum\", "
                +   "betrag as \"Betrag\", "
                +   "betreff as \"Betreff\", "
                +   "ziel_name as \"von\", "
                +   "quelle_name as \"auf\", "
                +   "budget_typ_name as \"Budget\", "
                +   "ausgabengrp_name as \"Ausgabengrp.\" "
                + "from "
                +   "get_plain_buchungen ('1900-01-01',current_date) ";
        return getDTMwithSQL (sql);
    }
    
    public String getSuperSaldo () {
        String sql =
                "select "
                +   "* "
                + "from "
                +   "get_supersaldo (current_date) ";
        // initConnection ();
        String returnValue = "Gesamt: "+TableModelHelper.resultSetToTableModel(myDB.getResultSet(sql),false).getValueAt(0,0).toString()+" €";
        // closeConnection();
        return returnValue;
    }
    
    public int deleteBuchung (int id) {
        String sql =
                "delete "
                + "from buchung "
                + "where buchung_id='"+id+"'";
        return updateWithReturnValue (sql);
    }
    
    public int deleteAT (int id) {
        String sql =
                "delete "
                + "from ausgabengruppe "
                + "where ausgabegrp_id='"+id+"'";
        return updateWithReturnValue (sql);
    }
    
    public int deleteKonto (int id) {
        String sql =
                "delete "
                + "from konto "
                + "where konto_id='"+id+"'";
        return updateWithReturnValue (sql);
    }
    
    public int deleteBT (int id) {
        String sql =
                "delete "
                + "from budget_typ "
                + "where budget_typ_id='"+id+"'";
        return updateWithReturnValue (sql);
    }
    
    public int deleteBudget (int id) {
        String sql =
                "delete "
                + "from budget "
                + "where budget_id='"+id+"'";
        return updateWithReturnValue (sql);
    }
    
    public RSdata getBuchungEditable (int id) {
        
        // ACHTUNG REIHENFOLGE NICHT ÄNDERN (relvant im Buchungseditor: Selected Row besimmen
        String sql =
                "select "
                +   "buchung_id, "
                +   "datum, "
                +   "betrag, "
                +   "betreff, "
                +   "ziel, "
                +   "quelle, "
                +   "budget_relevant, "
                +   "ausgaben_typ "
                + "from buchung where buchung_id='"+id+"'";
        
        // eigenständige Datenverbindung öffnen
        ConnectionDB buchungDB = getFreshConnection (true); 
        RSdata result= new RSdata (buchungDB.getConnection(), buchungDB.getResultSet(sql));
        return result;
    }
    
    public RSdata getAusgabenTypEditable (int id) {
        
        // ACHTUNG REIHENFOLGE NICHT ÄNDERN (relvant im AusgabenTypEditor: Selected Row besimmen
        String sql =
                "select "
                +   "ausgabegrp_id, "
                +   "ausgabegrp_name, "
                +   "budget_neutral "
                + "from ausgabengruppe where ausgabegrp_id='"+id+"'";
        
        System.out.println ("Model:ausgtyp: sql: "+sql);
        // eigenständige Datenverbindung öffnen
        ConnectionDB ausgabenTypDB = getFreshConnection (true);
        RSdata result= new RSdata (ausgabenTypDB.getConnection(), ausgabenTypDB.getResultSet(sql));
        return result;
    }
    
    public RSdata getKontoEditable (int id) {
        
        // ACHTUNG REIHENFOLGE NICHT ÄNDERN (relvant im AusgabenTypEditor: Selected Row besimmen
        String sql =
                "select "
                +   "konto_id, "
                +   "konto_name, "
                +   "eigentum, "
                +   "positiv "
                + "from konto where konto_id='"+id+"'";
        
        System.out.println ("Model:getKontoEditable: sql: "+sql);
        // eigenständige Datenverbindung öffnen
        ConnectionDB kontoDB = getFreshConnection (true);
        RSdata result= new RSdata (kontoDB.getConnection(), kontoDB.getResultSet(sql));
        return result;
    }
    
    public RSdata getBudgetTypEditable (int id) {
        
        // ACHTUNG REIHENFOLGE NICHT ÄNDERN (relvant im AusgabenTypEditor: Selected Row besimmen
        String sql =
                "select "
                +   "budget_typ_id, "
                +   "budget_typ_name "
                + "from budget_typ where budget_typ_id='"+id+"'";
        
        System.out.println ("Model:budgetTypEditable: sql: "+sql);
        // eigenständige Datenverbindung öffnen
        ConnectionDB budgetTypDB = getFreshConnection (true);
        RSdata result= new RSdata (budgetTypDB.getConnection(), budgetTypDB.getResultSet(sql));
        return result;
    }
    
    public RSdata getBudgetEditable (int id) {
        
        // ACHTUNG REIHENFOLGE NICHT ÄNDERN (relvant im AusgabenTypEditor: Selected Row besimmen
        String sql =
                "select "
                +   "budget_id, "
                +   "budget_betrag, "
                +   "start_date, "
                +   "end_date, "
                +   "typ "
                + "from budget where budget_id='"+id+"'";
        
        System.out.println ("Model:budgetEditable: sql: "+sql);
        // eigenständige Datenverbindung öffnen
        ConnectionDB budgetTypDB = getFreshConnection (true);
        RSdata result= new RSdata (budgetTypDB.getConnection(), budgetTypDB.getResultSet(sql));
        return result;
    }
    
    public RSdata createBuchung (Buchung newBuchung) {
        String sql =
                "insert into buchung (datum,betrag,betreff,ziel,quelle,budget_relevant,ausgaben_typ) "
                 +  "values ("
                 +  "'" +newBuchung.getDatumAsString()+ "', "
                 +  "'" +newBuchung.getBetrag().toPlainString()+ "', "
                 +  "'" +newBuchung.getBetreff()+ "', "
                 +  "'" +newBuchung.getZiel()+ "', "
                 +  "'" +newBuchung.getQuelle()+ "', "
                 +  "'" +newBuchung.getBudget()+ "', "
                 +  "'" +newBuchung.getAusgabenTyp()+ "'"
                 +  ")";
        System.out.println ("Model:creatingBuchung: sql-string: "+sql);
        ResultSet rsBuffer= myDB.updateQueryWithSpecificReturnValues(sql,new String [] {"buchung_id"});
        int newID=extractIDfromResultSet(rsBuffer);
        if (newID!=-1) {
            return this.getBuchungEditable(newID);
        } else {
            return null;
        }
        
    }
    
    public RSdata createAusgabenTyp (AusgabenTyp newAT) {
        String sql =
                "insert into ausgabengruppe (ausgabegrp_name,budget_neutral) "
                 +  "values ("
                 +  "'" +newAT.getAtName()+ "', "
                 +  "'" +newAT.isBudgetNeutral()+ "' "
                 +  ")";
        System.out.println ("Model:creatingAT: sql-string: "+sql);
        ResultSet rsBuffer= myDB.updateQueryWithSpecificReturnValues(sql,new String [] {"ausgabegrp_id"});
        int newID=extractIDfromResultSet(rsBuffer);
        if (newID!=-1) {
            return this.getAusgabenTypEditable(newID);
        } else {
            return null;
        }
        
    }
    
    public RSdata createKonto (Konto newKonto) {
        String sql =
                "insert into konto (konto_name,eigentum,positiv) "
                 +  "values ("
                 +  "'" +newKonto.getKontoName()+ "', "
                 +  "'" +newKonto.isEigentum()+ "', "
                 +  "'" +newKonto.isPositiv()+ "' "
                 +  ")";
        System.out.println ("Model:creatingKonto: sql-string: "+sql);
        ResultSet rsBuffer= myDB.updateQueryWithSpecificReturnValues(sql,new String [] {"konto_id"});
        int newID=extractIDfromResultSet(rsBuffer);
        if (newID!=-1) {
            return this.getKontoEditable(newID);
        } else {
            return null;
        }
    }
    
    public RSdata createBudgetTyp (BudgetTyp newBT) {
        String sql =
                "insert into budget_typ (budget_typ_name) "
                 +  "values ("
                 +  "'" +newBT.getBTname()+ "' "
                 +  ")";
        System.out.println ("Model:creatingBT: sql-string: "+sql);
        ResultSet rsBuffer= myDB.updateQueryWithSpecificReturnValues(sql,new String [] {"budget_typ_id"});
        int newID=extractIDfromResultSet(rsBuffer);
        if (newID!=-1) {
            return this.getBudgetTypEditable(newID);
        } else {
            return null;
        }
    }
    
    public RSdata createBudget (Budget newBudget) {
        String sql =
                "insert into budget (budget_betrag,start_date,end_date,typ) "
                 +  "values ("
                 +  "'" +newBudget.getBetrag().toPlainString()+ "', "
                 +  "'" +newBudget.getStartDateAsString()+ "', "
                 +  "'" +newBudget.getEndDateAsString()+ "', "
                 +  "'" +newBudget.getBudgetTyp()+ "' "
                 +  ")";
        System.out.println ("Model:creatingBudget: sql-string: "+sql);
        ResultSet rsBuffer= myDB.updateQueryWithSpecificReturnValues(sql,new String [] {"budget_id"});
        int newID=extractIDfromResultSet(rsBuffer);
        if (newID!=-1) {
            return this.getBudgetEditable(newID);
        } else {
            return null;
        }
    }
    
    public static int extractIDfromResultSet (ResultSet rs) {
        try {
            if (rs.next() ) {
                return rs.getInt(1);
            } 
        } catch (Exception e) {
            System.out.println ("Model:extractIDfromResultSet:");
            e.printStackTrace();
        }
        return -1;
    }
    
    public static String dateToString(Date input) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd",Locale.GERMAN);
        return simpleDate.format(input);
    }
    
    public static java.sql.Date utilDateToSqlDate (java.util.Date input) {
        java.sql.Date sqlDate = new java.sql.Date(input.getTime());
        return sqlDate;
    }
    
    public static Date stringToDate (String input) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd",Locale.GERMAN);
        try {
                System.out.println("Model:stringToDate: input converted: >"+simpleDate.parse(input)+"<");
                return simpleDate.parse(input);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }
    
    public static boolean stringHasMoneyValue (String input) {
        try {
            input.replace(",", ".");
            if (input.indexOf(".")==-1) input=input+".00";
            if ((input.length()-input.indexOf(".")-1)>2) return false;
            BigDecimal checkNumber = new BigDecimal (input);            
        } catch (Exception e) {
            System.out.println ("Model:stringHasMoneyValue: Checking Betrag: ");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
