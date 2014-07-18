/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;

/**
 *
 * @author maak
 */
public class AusgabenTyp {
    private int id=-1;
    private String atName;
    private Model myModel;
    private RSdata myRSd;
    private boolean budgetNeutral;
    private boolean ausgabenTypSuccesfullyLoaded=false;
    
    public AusgabenTyp (int ausgabenTypID) {
        myModel=DshApp.getModel();
        myRSd=myModel.getAusgabenTypEditable(ausgabenTypID);
        TableModelHelper.printTabelleToLog(myRSd.getTableModel());
        loadATfromDB();
    }
    
    public AusgabenTyp () {
        myModel=DshApp.getModel();
        id=-1;
        atName="";
        budgetNeutral=false;
        myRSd=null;
    }

    public void disposeMe () {
        myRSd.closedSuccesfully();
    }
    
    public static boolean deleteAT (int buchungID) {
        if (DshApp.getModel().deleteAT(buchungID)>0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteAT () {
        if (myRSd.closedSuccesfully()) {
            return AusgabenTyp.deleteAT(id);
        } else {
            return false;
        }
    }
    
    
    private void loadATfromDB () {
        ausgabenTypSuccesfullyLoaded=true;
        if (myRSd.getTableModel().getValueAt(0,0)!=null){
            setId(((java.lang.Integer) myRSd.getTableModel().getValueAt(0,0)).intValue());
        } else {
            setId(-1);
            ausgabenTypSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,1)!=null) {
            setAtName(myRSd.getTableModel().getValueAt(0,1).toString());   
        } else {
            setAtName("");
            ausgabenTypSuccesfullyLoaded = false;
        }            
        if (myRSd.getTableModel().getValueAt(0,2)!=null) {
            setBudgetNeutral((boolean) (java.lang.Boolean) myRSd.getTableModel().getValueAt(0,2));
        } else {
            setBudgetNeutral(false);
            ausgabenTypSuccesfullyLoaded = false;
        }            
    }

    public void printATtoLog () {
        System.out.println ("\nObject-Data:");        
        System.out.println ("ID:		"+getId());        
        System.out.println ("Name:		"+getAtName());        
        System.out.println ("Budget-Relevant:	"+isBudgetNeutral());        
        System.out.println ("\nRecordSet:");
        if (myRSd==null) {
            System.out.println ("Recordset is empty!");
        } else {
            TableModelHelper.printTabelleToLog(myRSd.getTableModel());            
        }
    }
    
    public boolean successfullySavedToDB (){
        if (id==-1) { // neu Buchung
            myRSd = myModel.createAusgabenTyp(this);
            loadATfromDB();
            return ausgabenTypSuccesfullyLoaded;
        } else {  // bestehende Buchung
            myRSd.getTableModel().setValueAt(atName,0,1);
            myRSd.getTableModel().setValueAt(budgetNeutral,0,2);
            return myRSd.updateDBrowSuccessfully(1);            
        }
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
     * @return the atName
     */
    public String getAtName() {
        return atName;
    }

    /**
     * @param atName the atName to set
     */
    public void setAtName(String atName) {
        this.atName = atName;
    }

    /**
     * @return the budgetNeutral
     */
    public boolean isBudgetNeutral() {
        return budgetNeutral;
    }

    /**
     * @param budgetNeutral the budgetNeutral to set
     */
    public void setBudgetNeutral(boolean budgetNeutral) {
        this.budgetNeutral = budgetNeutral;
    }

    /**
     * @return the ausgabenTypSuccesfullyLoaded
     */
    public boolean isAusgabenTypSuccesfullyLoaded() {
        return ausgabenTypSuccesfullyLoaded;
    }


}
