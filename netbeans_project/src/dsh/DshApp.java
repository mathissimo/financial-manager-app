/*
 * DshApp.java
 */

package dsh;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class DshApp extends SingleFrameApplication {
    private Model myModel;
    private DashBoard myDashBoard;
    private TableViewer myTableViewer;
    private BuchungsEditor myBuchungsEditor;
    private AusgabenTypEditor myAusgabenTypEditor;
    private BudgetTypEditor myBudgetTypEditor;
    private BudgetEditor myBudgetEditor;
    private KontoEditor myKontoEditor;
    private WindowStack myWinStacker;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        myModel= new Model ();
        myDashBoard = new DashBoard ();
        myDashBoard.initDashboard();
        myWinStacker=new WindowStack ();
        myWinStacker.pushOnWindowStack(myDashBoard);
        
        show(myDashBoard);
    }
   

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of DshApp
     */
    public static DshApp getApplication() {
        return Application.getInstance(DshApp.class);
    }
    
    public static Model getModel() {
        return DshApp.getApplication().myModel;
    }
        
    public void startAusgabenTypEditor () {
        if (myWinStacker.getTopWindow()==myDashBoard) {
            myAusgabenTypEditor = new AusgabenTypEditor();
            myAusgabenTypEditor.setVisible(true);            
            myWinStacker.pushOnWindowStack(myAusgabenTypEditor);
        }   
    }
    
    public void ausgabenTypEditorEnd () {
        myAusgabenTypEditor.setVisible(false);
        myAusgabenTypEditor.dispose();
        myWinStacker.popWindowStack();
    }

    public void startKontoEditor () {
        System.out.println ("stacker: "+myWinStacker.getTopWindow());
        if (((DashBoard)myWinStacker.getTopWindow())==myDashBoard) {
            myKontoEditor = new KontoEditor();
            myKontoEditor.setVisible(true);
            myWinStacker.pushOnWindowStack(myKontoEditor);
        }
    }
    
    private void refreshWindows () {
        if (myWinStacker.getTopWindow()==myDashBoard) myDashBoard.initGauges();
        if (myWinStacker.getTopWindow()==myTableViewer) updateListBuchungen();
        if (myWinStacker.getTopWindow()==myBudgetEditor) {
            myBudgetEditor.refreshBTtable();
            myBudgetEditor.refreshBudgetTable();
        }
    }
        
    public void endKontoEditor () {
        myKontoEditor.setVisible(false);
        myKontoEditor.dispose();
        myWinStacker.popWindowStack();
        refreshWindows ();
    }

    public void startBudgetTypEditor () {
        if (myWinStacker.getTopWindow()==myDashBoard || myWinStacker.getTopWindow()==myBudgetEditor) {
            myBudgetTypEditor = new BudgetTypEditor();
            myBudgetTypEditor.setVisible(true);
            myWinStacker.pushOnWindowStack(myBudgetTypEditor);
        }
    }
    
    public void endBudgetTypEditor () {
        myBudgetTypEditor.setVisible(false);
        myBudgetTypEditor.dispose();
        myWinStacker.popWindowStack();
        refreshWindows ();
    }

    public void startBudgetEditor () {
        if (myWinStacker.getTopWindow()==myDashBoard) {            
            myBudgetEditor = new BudgetEditor();
            myBudgetEditor.setVisible(true);
            myWinStacker.pushOnWindowStack(myBudgetEditor);
        }
    }
    
    public void endBudgetEditor () {
        myBudgetEditor.setVisible(false);
        myBudgetEditor.dispose();
        myWinStacker.popWindowStack();
        refreshWindows ();
    }

    public void StartBuchungsEditor (int id) {
        if (myWinStacker.getTopWindow()==myTableViewer) {            
            Buchung aBuchung = new Buchung (id);
            myBuchungsEditor = new BuchungsEditor ();
            myBuchungsEditor.initBuchungsEditor(aBuchung);
            myBuchungsEditor.setVisible(true);
            myWinStacker.pushOnWindowStack(myBuchungsEditor);
        }
    }
    
    public void EndEditBuchung () {
        myBuchungsEditor.setVisible(false);
        myBuchungsEditor.dispose();
        myWinStacker.popWindowStack();
        refreshWindows ();
    }
    public void listBuchungen () {
        if (myWinStacker.getTopWindow()==myDashBoard) { 
            myTableViewer = new TableViewer ();
            myTableViewer.initTableViewer(myModel.getOverviewBuchungen(),0);
            myTableViewer.setVisible(true);
            myWinStacker.pushOnWindowStack(myTableViewer);
        }
    }
    public void EndListBuchungen () {
        myTableViewer.setVisible(false);
        myTableViewer.dispose();
        myWinStacker.popWindowStack();
        refreshWindows ();
    }
    
    public void updateListBuchungen () {
        myTableViewer.initTableViewer(myModel.getOverviewBuchungen(),0);
    }
    
    public void createBuchung () {
        if (myWinStacker.getTopWindow()==myDashBoard) {            
            Buchung aBuchung = new Buchung ();
            myBuchungsEditor = new BuchungsEditor ();
            myBuchungsEditor.initBuchungsEditor(aBuchung);
            myBuchungsEditor.setVisible(true);        
            myWinStacker.pushOnWindowStack(myBuchungsEditor);
        }
    }
    
    public void deleteBuchung (JFrame controller, int buchungID) {
        if (Buchung.deleteBuchung(buchungID)) {
            JOptionPane.showMessageDialog(controller, "Buchung erfolgreich gelöscht!");
        } else {
            JOptionPane.showMessageDialog(controller, "Buchung konnte NICHT gelöscht werden!");
        }            
    }
    
    public boolean userAccordOnDeleteWarning (JFrame controller) {
        int userReply = JOptionPane.showConfirmDialog(controller, "Das Löschen dieses Eintrages kann zum Löschen weiterer abhängiger Einträge führen.","Eintrag Löschen",JOptionPane.YES_NO_OPTION);
        if (userReply==JOptionPane.YES_OPTION) return true;
        return false;
    }
    
    public void showErrorMessage (String errorMessage) {
        JOptionPane.showMessageDialog(myWinStacker.getTopWindow(), errorMessage);
    }
    
    public boolean isTheTopWindow (JFrame myInstance) {
        if (myInstance==myWinStacker.getTopWindow()){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(DshApp.class, args);
    }
    public void killme (){
        myDashBoard.dispose();
        this.exit();
    }

    /**
     * @return the myModel
     */
    public Model getMyModel() {
        return myModel;
    }
}
