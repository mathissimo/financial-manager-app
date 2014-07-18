/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;
import java.sql.Date;
import java.math.BigDecimal;

/**
 *
 * @author maak
 */
public class Buchung {
    private Model myModel;
    private RSdata myRSd;
    private String betreff;
    private int id;
    private int ziel;
    private int quelle;
    private int budget;
    private int ausgabenTyp;
    private BigDecimal betrag;
    private Date datum;
    private boolean buchungExistsInDB;
    
    public Buchung () {
        myModel= DshApp.getModel();
        betreff= "";
        id = -1;
        ziel=0;
        quelle=0;
        datum= new java.sql.Date(new java.util.Date().getTime());
        budget=0;
        ausgabenTyp=0;
        betrag = new BigDecimal(0.0);
        buchungExistsInDB = false;
        myRSd=null;
    }
    public Buchung (int buchungsID) {
        // bestehende Werte für ausgewählten Datensatz auslesen (DB-Ids)
        // ACHTUNG: GEKOPPELT AN SQL-Anfrage im Model (Reihenfolge Spalten)
        myModel= DshApp.getModel();
        myRSd=myModel.getBuchungEditable (buchungsID);
        TableModelHelper.printTabelleToLog(myRSd.getTableModel());
        loadBuchung();
    }
       
    public static boolean deleteBuchung (int buchungID) {
        if (DshApp.getModel().deleteBuchung(buchungID)>0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteBuchung () {
        if (myRSd.closedSuccesfully()) {
            return Buchung.deleteBuchung(id);
        } else {
            return false;
        }
    }
    
    private boolean loadBuchung () {
        boolean buchungSuccesfullyLoaded=true;
        if (myRSd!=null) {
            if (myRSd.getTableModel().getValueAt(0,0)!=null){
                id = ((java.lang.Integer) myRSd.getTableModel().getValueAt(0,0)).intValue();
            } else {
                id = -1;
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,4)!=null){
                ziel = ((java.lang.Integer) myRSd.getTableModel().getValueAt(0,4)).intValue();
            } else {
                ziel = 0;
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,5)!=null) {
                quelle = ((java.lang.Integer) myRSd.getTableModel().getValueAt(0,5)).intValue();                
            } else {
                quelle = 0;
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,6)!=null) {
                budget = ((java.lang.Integer) myRSd.getTableModel().getValueAt(0,6)).intValue();
            } else {
                budget = 0;
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,7)!=null) {
                ausgabenTyp = ((java.lang.Integer) myRSd.getTableModel().getValueAt(0,7)).intValue();
            } else {
                ausgabenTyp = 0;
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,2)!=null) {
                betrag = (java.math.BigDecimal) myRSd.getTableModel().getValueAt(0,2);
            } else {
                betrag = new BigDecimal(0.0);
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,1)!=null) {
                datum = (java.sql.Date) myRSd.getTableModel().getValueAt(0,1);
            } else {
                datum= new java.sql.Date(new java.util.Date().getTime());
                buchungSuccesfullyLoaded = false;
            }
            if (myRSd.getTableModel().getValueAt(0,3)!=null) {
             betreff = myRSd.getTableModel().getValueAt(0,3).toString();   
            } else {
                betreff = "";
                buchungSuccesfullyLoaded = false;
            }
        } else {
            buchungSuccesfullyLoaded = false;
        }
        return buchungSuccesfullyLoaded;
    }
    
    public void printBuchungToLog () {
        System.out.println ("\nObject-Data:");        
        System.out.println ("ID:		"+id);        
        System.out.println ("Betreff:	"+betreff);        
        System.out.println ("Betrag:		"+betrag.toPlainString());        
        System.out.println ("Quelle:		"+quelle);        
        System.out.println ("Ziel:		"+ziel);        
        System.out.println ("Datum:		"+datum);        
        System.out.println ("Budget:		"+budget);        
        System.out.println ("Ausgaben-Typ:	"+ausgabenTyp);        
        System.out.println ("\nRecordSet:");
        if (myRSd==null) {
            System.out.println ("Recordset is empty!");
        } else {
            TableModelHelper.printTabelleToLog(myRSd.getTableModel());            
        }
    }
    
    public boolean successfullySavedToDB (){
        if (id==-1) { // neu Buchung
            myRSd = myModel.createBuchung(this);
            return loadBuchung();
        } else {  // bestehende Buchung
            myRSd.getTableModel().setValueAt(datum,0,1);
            myRSd.getTableModel().setValueAt(ziel,0,4);
            myRSd.getTableModel().setValueAt(quelle,0,5);    
            myRSd.getTableModel().setValueAt(budget,0,6);
            myRSd.getTableModel().setValueAt(ausgabenTyp,0,7);
            myRSd.getTableModel().setValueAt(betrag,0,2);
            myRSd.getTableModel().setValueAt(betreff,0,3);
            return myRSd.updateDBrowSuccessfully(1);            
        }
    }
    
    public void disposeMe () {
        myRSd.closedSuccesfully();
    }

    /**
     * @return the betreff
     */
    public String getBetreff() {
        return betreff;
    }

    /**
     * @param betreff the betreff to set
     */
    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the ziel
     */
    public int getZiel() {
        return ziel;
    }

    /**
     * @param ziel the ziel to set
     */
    public void setZiel(int ziel) {
        this.ziel = ziel;
    }

    /**
     * @return the quelle
     */
    public int getQuelle() {
        return quelle;
    }

    /**
     * @param quelle the quelle to set
     */
    public void setQuelle(int quelle) {
        this.quelle = quelle;
    }

    /**
     * @return the budget
     */
    public int getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(int budget) {
        this.budget = budget;
    }

    /**
     * @return the ausgabenTyp
     */
    public int getAusgabenTyp() {
        return ausgabenTyp;
    }

    /**
     * @param ausgabenTyp the ausgabenTyp to set
     */
    public void setAusgabenTyp(int ausgabenTyp) {
        this.ausgabenTyp = ausgabenTyp;
    }

    /**
     * @return the betrag
     */
    public BigDecimal getBetrag() {
        return betrag;
    }

    /**
     * @param betrag the betrag to set
     */
    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    /**
     * @return the datum
     */
    public Date getDatum() {
        return datum;
    }

    /**
     * @return the datum
     */
    public String getDatumAsString() {
        return Model.dateToString(datum);
    }

    /**
     * @param datum the datum to set
     */
    public void setDatum(Date datum) {
        this.datum = datum;
    }
    
}
