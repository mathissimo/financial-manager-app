/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author maak
 */
public class Budget {
    private BigDecimal betrag;
    private Date startDate;
    private Date endDate;
    private int id=-1;
    private int budgetTyp=-1;
    private Model myModel;
    private RSdata myRSd;
    private boolean budgetSuccesfullyLoaded=false;
    
    public Budget (int budgetID) {
        myModel=DshApp.getModel();
        myRSd=myModel.getBudgetEditable(budgetID);
        TableModelHelper.printTabelleToLog(myRSd.getTableModel());
        loadBudgetfromDB();
    }
    
    public Budget () {
        myModel=DshApp.getModel();
        id=-1;
        myRSd=null;
        startDate= new java.sql.Date(new java.util.Date().getTime());
        endDate= new java.sql.Date(new java.util.Date().getTime());
        budgetTyp=-1;
        betrag = new BigDecimal(0.0);
    }
    
    
    public void disposeMe () {
        myRSd.closedSuccesfully();
    }

    public static boolean deleteBudget (int buchungID) {
        if (DshApp.getModel().deleteBudget(buchungID)>0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteBudget () {
        if (myRSd.closedSuccesfully()) {
            return Budget.deleteBudget(id);
        } else {
            return false;
        }
    }
    
    
    private void loadBudgetfromDB () {
        budgetSuccesfullyLoaded=true;
        if (myRSd.getTableModel().getValueAt(0,0)!=null){
            id=((java.lang.Integer) myRSd.getTableModel().getValueAt(0,0)).intValue();
        } else {
            id=(-1);
            budgetSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,1)!=null) {
            betrag = (java.math.BigDecimal) myRSd.getTableModel().getValueAt(0,1);
        } else {
            betrag = new BigDecimal(0.0);
            budgetSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,2)!=null) {
            startDate = (java.sql.Date) myRSd.getTableModel().getValueAt(0,2);
        } else {
            startDate= new java.sql.Date(new java.util.Date().getTime());
            budgetSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,3)!=null) {
            endDate = (java.sql.Date) myRSd.getTableModel().getValueAt(0,3);
        } else {
            endDate= new java.sql.Date(new java.util.Date().getTime());
            budgetSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,4)!=null){
            budgetTyp=((java.lang.Integer) myRSd.getTableModel().getValueAt(0,4)).intValue();
        } else {
            budgetTyp=(-1);
            budgetSuccesfullyLoaded = false;
        }
    }

    public void printBudgettoLog () {
        System.out.println ("\nObject-Data:");        
        System.out.println ("ID:		"+id);        
        System.out.println ("Betrag:		"+betrag);        
        System.out.println ("StartDate:		"+getStartDateAsString());        
        System.out.println ("EndDate:		"+getEndDateAsString());        
        System.out.println ("Budget-Typ:	"+budgetTyp);        
        System.out.println ("\nRecordSet:");
        if (myRSd==null) {
            System.out.println ("Recordset is empty!");
        } else {
            TableModelHelper.printTabelleToLog(myRSd.getTableModel());            
        }
    }
    
    public boolean successfullySavedToDB (){
        if (id==-1) { // neu Buchung
            myRSd = myModel.createBudget(this);
            loadBudgetfromDB();
            return isBudgetSuccesfullyLoaded();
        } else {  // bestehende Buchung
            myRSd.getTableModel().setValueAt(betrag,0,1);
            myRSd.getTableModel().setValueAt(startDate,0,2);
            myRSd.getTableModel().setValueAt(endDate,0,3);
            myRSd.getTableModel().setValueAt(budgetTyp,0,4);
            return myRSd.updateDBrowSuccessfully(1);            
        }
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
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the bTid
     */
    public int getId() {
        return id;
    }

    /**
     * @return the budgetTyp
     */
    public int getBudgetTyp() {
        return budgetTyp;
    }

    /**
     * @param budgetTyp the budgetTyp to set
     */
    public void setBudgetTyp(int budgetTyp) {
        this.budgetTyp = budgetTyp;
    }

    /**
     * @return the budgetSuccesfullyLoaded
     */
    public boolean isBudgetSuccesfullyLoaded() {
        return budgetSuccesfullyLoaded;
    }
    /**
     * @return the datum
     */
    public String getStartDateAsString() {
        return Model.dateToString(startDate);
    }
    
    public String getEndDateAsString() {
        return Model.dateToString(endDate);
    }
}
