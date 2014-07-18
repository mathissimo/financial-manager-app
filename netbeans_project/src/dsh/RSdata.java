/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maak
 */

// Das RSdata beinhaltet 1. Einen RecordSet und 2. ein TableModel wobei das TableModel
// zuerst die Änderungen bekommt und dann an das RS zur speicherung übergibt.
public class RSdata {
    private ResultSet myRS;
   
    private int rowCount=0;       // fängt mit 1 an! 0=> no data!
    private int columnCount=0;    // fängt mit 1 an! 0=> no data!
    private DefaultTableModel myTmod;
    private Connection myCon;
    
    public RSdata (ResultSet rs) {
        initRSdata (null,rs);
    }
    
    public RSdata (Connection newCon, ResultSet rs) {
        initRSdata (newCon,rs);
    }
    
    private void initRSdata (Connection con,ResultSet rs) {
        this.myRS = rs;
        this.myTmod = TableModelHelper.resultSetToTableModel(myRS,false);
        this.rowCount=myTmod.getRowCount();
        this.columnCount=myTmod.getColumnCount();
        this.myCon=con;
    }
    
    public boolean closedSuccesfully () {
        if (myRS!=null) {
            try {
                myRS.close();
                if (myCon!=null) {
                    myCon.close();
                }
                return true;
            } catch (Exception e) {
                DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
                System.out.println("RSdata:closedSuccesfully: "+e);
                e.printStackTrace();
                return false;
            }
        }
        return true;        
    }
    
    public DefaultTableModel getTableModel (){
        return myTmod;
    }
    
    public ResultSet getResultSet (){
        return myRS;
    }
    
    public void getTableModel (DefaultTableModel newTM){
        this.myTmod=newTM;
    }
    
    public void getResultSet (ResultSet newTM){
        this.myRS=newTM;
    }
    
    private void copyTmodToRS (int row){
        try {
            this.myRS.first();   // ResultSet updaten
            for (int i=1; i<= this.rowCount; i++) {
                for (int j=1; j<= this.columnCount; j++) {
                    this.myRS.updateObject(j, this.myTmod.getValueAt(i-1, j-1));
                }
                this.myRS.next();
            }
            this.myRS.updateRow(); // DB updaten
        } catch (Exception e) {
            System.out.println("RSdata copyTmodToRS: "+e);
        }   
    }
    
    public boolean updateDBrowSuccessfully (int rowIndex) {
        try {
            this.myRS.absolute(rowIndex);   
            // ResultSet Row updaten
            for (int j=1; j<= this.columnCount; j++) {
                this.myRS.updateObject(j, this.myTmod.getValueAt(rowIndex-1, j-1));
            }
            // DB updaten
            this.myRS.updateRow();
            // end transaction: comit + close
            if (this.myCon!=null) {
                this.myCon.commit();
            }
        } catch (Exception e) {
            System.out.println("RSdata updateDBrow: "+e);
            return false;
        }
        return true;
    }
        
    public void updateDBrow (int rowIndex, int moneyBugCol) {
        // Um das JDBC-Money Problem zu umgehen werden die Zeilen als String mit "$" voran ausgegeben.
        int jBuffer=0;
        Object doubleBuffer=null;
        try {
            this.myRS.absolute(rowIndex);   
            // ResultSet Row updaten
            for (int j=1; j<= this.columnCount; j++) {
                if (j==moneyBugCol) {
                    // Money-String anlegen und mit double-Object tauschen
                    jBuffer=j;
                    doubleBuffer = this.myTmod.getValueAt(rowIndex-1, j-1);
                    String doubleAsString = "$"+ doubleBuffer.toString();
                    this.myTmod.setValueAt(doubleAsString,rowIndex-1, j-1);
                }
                this.myRS.updateObject(j, this.myTmod.getValueAt(rowIndex-1, j-1));
            }
            this.myRS.updateRow(); // DB updaten
        } catch (Exception e) {
            System.out.println("RSdata updateDBrow: "+e);
        }   
    }
}
