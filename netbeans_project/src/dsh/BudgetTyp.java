/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;

/**
 *
 * @author maak
 */
public class BudgetTyp {
    private int bTid=-1;
    private String bTname;
    private Model myModel;
    private RSdata myRSd;
    private boolean budgetTypSuccesfullyLoaded=false;
    
    public BudgetTyp (int budgetTypID) {
        myModel=DshApp.getModel();
        myRSd=myModel.getBudgetTypEditable(budgetTypID);
        TableModelHelper.printTabelleToLog(myRSd.getTableModel());
        loadBTfromDB();
    }
    
    public BudgetTyp () {
        myModel=DshApp.getModel();
        bTid=-1;
        bTname="";
        myRSd=null;
    }
    
    public static boolean deleteBT (int buchungID) {
        if (DshApp.getModel().deleteBT(buchungID)>0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void disposeMe () {
        myRSd.closedSuccesfully();
    }

    public boolean deleteBT () {
        if (myRSd.closedSuccesfully()) {
            return BudgetTyp.deleteBT(bTid);
        } else {
            return false;
        }
    }
    
    
    private void loadBTfromDB () {
        budgetTypSuccesfullyLoaded=true;
        if (myRSd.getTableModel().getValueAt(0,0)!=null){
            bTid=((java.lang.Integer) myRSd.getTableModel().getValueAt(0,0)).intValue();
        } else {
            bTid=(-1);
            budgetTypSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,1)!=null) {
            bTname=(myRSd.getTableModel().getValueAt(0,1).toString());   
        } else {
            bTname="";
            budgetTypSuccesfullyLoaded = false;
        }            
    }

    public void printBTtoLog () {
        System.out.println ("\nObject-Data:");        
        System.out.println ("ID:		"+bTid);        
        System.out.println ("Name:		"+bTname);        
        System.out.println ("\nRecordSet:");
        if (myRSd==null) {
            System.out.println ("Recordset is empty!");
        } else {
            TableModelHelper.printTabelleToLog(myRSd.getTableModel());            
        }
    }
    
    public boolean successfullySavedToDB (){
        if (bTid==-1) { // neu Buchung
            myRSd = myModel.createBudgetTyp(this);
            loadBTfromDB();
            return budgetTypSuccesfullyLoaded;
        } else {  // bestehende Buchung
            myRSd.getTableModel().setValueAt(bTname,0,1);
            return myRSd.updateDBrowSuccessfully(1);            
        }
    }    

    /**
     * @return the id
     */
    public int getId() {
        return bTid;
    }

    /**
     * @return the BTname
     */
    public String getBTname() {
        return bTname;
    }

    /**
     * @param BTname the BTname to set
     */
    public void setBTname(String BTname) {
        this.bTname = BTname;
    }

    /**
     * @return the budgetTypSuccesfullyLoaded
     */
    public boolean isBudgetTypSuccesfullyLoaded() {
        return budgetTypSuccesfullyLoaded;
    }
}
