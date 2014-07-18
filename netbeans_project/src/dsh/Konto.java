/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;

/**
 *
 * @author maak
 */
public class Konto {
    private int id=-1;
    private String kontoName;
    private Model myModel;
    private RSdata myRSd;
    private boolean eigentum;
    private boolean positiv;
    private boolean kontoSuccesfullyLoaded=false;
    
    public Konto (int kontoID) {
        myModel=DshApp.getModel();
        myRSd=myModel.getKontoEditable(kontoID);
        TableModelHelper.printTabelleToLog(myRSd.getTableModel());
        loadKontofromDB();
    }
    
    public Konto () {
        myModel=DshApp.getModel();
        id=-1;
        kontoName="";
        eigentum=true;
        positiv=true;
        myRSd=null;
    }
    
    public static boolean deleteKonto (int buchungID) {
        if (DshApp.getModel().deleteKonto(buchungID)>0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void disposeMe () {
        myRSd.closedSuccesfully();
    }

    public boolean deleteKonto () {
        if (myRSd.closedSuccesfully()) {
            return Konto.deleteKonto(id);
        } else {
            return false;
        }
    }
    
    
    private void loadKontofromDB () {
        kontoSuccesfullyLoaded=true;
        if (myRSd.getTableModel().getValueAt(0,0)!=null){
            setId(((java.lang.Integer) myRSd.getTableModel().getValueAt(0,0)).intValue());
        } else {
            setId(-1);
            kontoSuccesfullyLoaded = false;
        }
        if (myRSd.getTableModel().getValueAt(0,1)!=null) {
            setKontoName(myRSd.getTableModel().getValueAt(0,1).toString());   
        } else {
            setKontoName("");
            kontoSuccesfullyLoaded = false;
        }            
        if (myRSd.getTableModel().getValueAt(0,2)!=null) {
            eigentum=((boolean) (java.lang.Boolean) myRSd.getTableModel().getValueAt(0,2));
        } else {
            eigentum=true;
            kontoSuccesfullyLoaded = false;
        }            
        if (myRSd.getTableModel().getValueAt(0,3)!=null) {
            positiv=((boolean) (java.lang.Boolean) myRSd.getTableModel().getValueAt(0,3));
        } else {
            positiv=true;
            kontoSuccesfullyLoaded = false;
        }            
    }

    public void printKontotoLog () {
        System.out.println ("\nObject-Data:");        
        System.out.println ("ID:		"+getId());        
        System.out.println ("Name:		"+getKontoName());        
        System.out.println ("Eigentum:		"+isEigentum());        
        System.out.println ("positiv:		"+isPositiv());        
        System.out.println ("\nRecordSet:");
        if (myRSd==null) {
            System.out.println ("Recordset is empty!");
        } else {
            TableModelHelper.printTabelleToLog(myRSd.getTableModel());            
        }
    }
    
    public boolean successfullySavedToDB (){
        if (id==-1) { // neu Buchung
            myRSd = myModel.createKonto(this);
            loadKontofromDB();
            return kontoSuccesfullyLoaded;
        } else {  // bestehende Buchung
            myRSd.getTableModel().setValueAt(kontoName,0,1);
            myRSd.getTableModel().setValueAt(eigentum,0,2);
            myRSd.getTableModel().setValueAt(positiv,0,3);
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
    public String getKontoName() {
        return kontoName;
    }

    /**
     * @param atName the atName to set
     */
    public void setKontoName(String atName) {
        this.kontoName = atName;
    }

    public boolean isEigentum() {
        return eigentum;
    }

    public void setEigentum(boolean newEigentum) {
        this.eigentum = newEigentum;
    }

    public boolean isPositiv() {
        return positiv;
    }

    public void setPositiv(boolean newPositiv) {
        this.positiv = newPositiv;
    }

    /**
     * @return the kontoSuccesfullyLoaded
     */
    public boolean isKontoSuccesfullyLoaded() {
        return kontoSuccesfullyLoaded;
    }


}
