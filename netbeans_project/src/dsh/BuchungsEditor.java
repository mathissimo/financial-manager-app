    // TODO: Das Speichern eines zu editierenden Datensatzes funktioniert nicht



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BuchungsEditor.java
 *
 * Created on Jun 26, 2012, 4:21:06 PM
 */
package dsh;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 *
 * @author maak
 */

// modX: Tabellen für GUI
// xOld: Puffer für Werte beim Laden (zum späteren Abgleich)
// selBuchung: Recordset for saving
public class BuchungsEditor extends javax.swing.JFrame {
    private Model myModel;
    private DefaultTableModel modZiel,modQuelle,modBudget,modAusgabe;
    private Buchung myBuchung;

    /** Creates new form BuchungsEditor */
    public BuchungsEditor() {
        initComponents();
        datepickerDatum.setFormats(new String[] {"dd.MM.yyyy"});
    }
    public void initBuchungsEditor (Buchung buchung) {
        myBuchung=buchung;
        myModel=DshApp.getModel();
                
        genTables ();
        initGauges ();
    }
    
    private void genTables (){
        modZiel = new DefaultTableModel();
        modQuelle = new DefaultTableModel();
        modBudget = new DefaultTableModel();
        modAusgabe = new DefaultTableModel();        
    }

     private void initGauges () {
        myBuchung.printBuchungToLog();
        // Wahlmöglichkeiten für Tabellen laden
        // Konten
        modZiel = myModel.getOverviewKonten();
        modQuelle = myModel.getOverviewKonten();
        tableZiel.setModel(modZiel);
        tableZiel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableQuelle.setModel(modQuelle);
        tableQuelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Budgets
        modBudget=myModel.getOverviewBudgetsByDate(myBuchung.getDatumAsString()); // Budgets zum Buchungszeitraum!
        tableBudget.setModel(modBudget);
        tableBudget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ausgaben
        modAusgabe=myModel.gerOverviewAusgabenByToday();
        tableAusgaben.setModel(modAusgabe);
        tableAusgaben.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Column-Resize
        TableModelHelper.packColumns(tableZiel);
        TableModelHelper.packColumns(tableQuelle);
        TableModelHelper.packColumns(tableBudget);
        TableModelHelper.packColumns(tableAusgaben);

        
        // Werte der Buchung einsetzen               
        // Daten aus bestehender Datensatz eintragen (->Zeilen makieren)
        TableModelHelper.selectRowByIDvalue(tableZiel, myBuchung.getZiel());
        TableModelHelper.selectRowByIDvalue(tableQuelle, myBuchung.getQuelle());
        TableModelHelper.selectRowByIDvalue(tableBudget, myBuchung.getBudget());
        TableModelHelper.selectRowByIDvalue(tableAusgaben, myBuchung.getAusgabenTyp());
        fieldBetrag.setText(myBuchung.getBetrag().toPlainString());
        fieldBetreff.setText(myBuchung.getBetreff());
        datepickerDatum.setDate(myBuchung.getDatum());
    }
    
    private String validatingFieldsErrorCode (){
        String fieldCheckErrorCode="";
        if (tableZiel.getSelectedRow()==-1) fieldCheckErrorCode = "Kein Ziel gewählt";
        if (tableQuelle.getSelectedRow()==-1) fieldCheckErrorCode =  "Keine Quelle gewählt";
        if (tableBudget.getSelectedRow()==-1) fieldCheckErrorCode =  "Kein Budget gewählt";
        if (tableAusgaben.getSelectedRow()==-1) fieldCheckErrorCode =  "Kein Ausgaben-Typ gewählt";
        if (fieldBetreff.getText().equals("")) fieldCheckErrorCode =  "Betreff-Text fehlt";
        try {
            BigDecimal checkNumber = new BigDecimal (fieldBetrag.getText().replace(",", "."));
            // check decimal digits (using the "." position in the string)
            if ((checkNumber.toPlainString().length()-checkNumber.toPlainString().indexOf(".")-1)>2) fieldCheckErrorCode = "Betrag hat zuviel Nachkommastellen";
        } catch (Exception e) {
            System.out.println ("BuchungsEditor:allFiledsHaveValues: Checking Betrag: ");
            e.printStackTrace();
            fieldCheckErrorCode = "Betrag nicht lesbar";
        }
        try {
            Model.utilDateToSqlDate(datepickerDatum.getDate());
        } catch (Exception e) {
            System.out.println ("BuchungsEditor:allFiledsHaveValues: Checking Datum: ");
            e.printStackTrace();
            fieldCheckErrorCode = "Datum nicht lesbar";
        }
        return fieldCheckErrorCode;
    }
    
    private boolean saveDataToDb () {
        // Einträge valide?
        String fieldsErrorCode = validatingFieldsErrorCode();
        if (fieldsErrorCode.equals("")) { // Einträge auf Änderung prüfen und speichern 
            // Bei Datumsänderung, nur Datum isoliert ändern
            if ((myBuchung.getId()!=-1)&&
                (!myBuchung.getDatumAsString().equals(Model.dateToString(datepickerDatum.getDate())))) { // Datepicker returns java.util.Date, but jdbc needs java.sql.Date 
                myBuchung.setDatum(Model.utilDateToSqlDate(datepickerDatum.getDate()));
            } else { // All changes can only be save if the date wasn't changed (semantical interdepencies in the db)
                if (myBuchung.getId()!=-1) myBuchung.setDatum(Model.utilDateToSqlDate(datepickerDatum.getDate()));
                myBuchung.setZiel ((java.lang.Integer)(tableZiel.getValueAt(tableZiel.getSelectedRow(),0)));
                myBuchung.setQuelle ((java.lang.Integer)(tableQuelle.getValueAt(tableQuelle.getSelectedRow(),0)));    
                myBuchung.setBudget ((java.lang.Integer)(tableBudget.getValueAt(tableBudget.getSelectedRow(),0)));
                myBuchung.setAusgabenTyp ((java.lang.Integer)(tableAusgaben.getValueAt(tableAusgaben.getSelectedRow(),0)));
                myBuchung.setBetrag (new BigDecimal (fieldBetrag.getText().replace(",", ".")));
                myBuchung.setBetreff (fieldBetreff.getText());
            }
            return myBuchung.successfullySavedToDB();
        } else {
           JOptionPane.showMessageDialog(this, fieldsErrorCode+": Bitte überprüfen Sie Ihre Eingabe.");
           System.out.println ("** Eingabefehler: "+fieldsErrorCode);
        }
        return false;
    } 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDatePickerUtil1 = new net.sourceforge.jdatepicker.util.JDatePickerUtil();
        jPanel1 = new javax.swing.JPanel();
        buttonSpeichern = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableZiel = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableQuelle = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        fieldBetreff = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        datepickerDatum = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableBudget = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableAusgaben = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        fieldBetrag = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        buttonVerwerfen = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        buttonLoeschen = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(dsh.DshApp.class).getContext().getResourceMap(BuchungsEditor.class);
        buttonSpeichern.setText(resourceMap.getString("buttonSpeichern.text")); // NOI18N
        buttonSpeichern.setName("buttonSpeichern"); // NOI18N
        buttonSpeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSpeichernActionPerformed(evt);
            }
        });
        jPanel1.add(buttonSpeichern, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 480, -1, -1));

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 287, 26));

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableZiel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableZiel.setName("tableZiel"); // NOI18N
        jScrollPane1.setViewportView(tableZiel);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 190, 229));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableQuelle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableQuelle.setName("tableQuelle"); // NOI18N
        jScrollPane2.setViewportView(tableQuelle);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 190, 229));

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, -1, 20));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        fieldBetreff.setColumns(20);
        fieldBetreff.setRows(5);
        fieldBetreff.setName("fieldBetreff"); // NOI18N
        jScrollPane3.setViewportView(fieldBetreff);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 210, 110));

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));

        datepickerDatum.setName("datepickerDatum"); // NOI18N
        jPanel1.add(datepickerDatum, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 280, 210, 30));

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        tableBudget.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableBudget.setName("tableBudget"); // NOI18N
        jScrollPane4.setViewportView(tableBudget);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 190, 150));

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, -1, -1));

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        tableAusgaben.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableAusgaben.setName("tableAusgaben"); // NOI18N
        jScrollPane5.setViewportView(tableAusgaben);
        tableAusgaben.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tableAusgaben.columnModel.title0")); // NOI18N
        tableAusgaben.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tableAusgaben.columnModel.title1")); // NOI18N
        tableAusgaben.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tableAusgaben.columnModel.title2")); // NOI18N
        tableAusgaben.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("tableAusgaben.columnModel.title3")); // NOI18N

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 360, 190, 150));

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        fieldBetrag.setText(resourceMap.getString("fieldBetrag.text")); // NOI18N
        fieldBetrag.setName("fieldBetrag"); // NOI18N
        jPanel1.add(fieldBetrag, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 210, -1));

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 60, -1, -1));

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 260, -1, -1));

        buttonVerwerfen.setText(resourceMap.getString("buttonVerwerfen.text")); // NOI18N
        buttonVerwerfen.setName("buttonVerwerfen"); // NOI18N
        buttonVerwerfen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVerwerfenActionPerformed(evt);
            }
        });
        jPanel1.add(buttonVerwerfen, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, -1));

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 630, -1));

        buttonLoeschen.setText(resourceMap.getString("buttonLoeschen.text")); // NOI18N
        buttonLoeschen.setName("buttonLoeschen"); // NOI18N
        buttonLoeschen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoeschenActionPerformed(evt);
            }
        });
        jPanel1.add(buttonLoeschen, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 480, -1, -1));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonVerwerfenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVerwerfenActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            if (myBuchung.getId()>0) myBuchung.disposeMe();
            DshApp.getApplication().EndEditBuchung ();        
        }
    
    }//GEN-LAST:event_buttonVerwerfenActionPerformed

    private void buttonSpeichernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSpeichernActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            if (saveDataToDb()) initGauges ();            
        }
    }//GEN-LAST:event_buttonSpeichernActionPerformed

    private void buttonLoeschenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoeschenActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this) && DshApp.getApplication().userAccordOnDeleteWarning(this)) {
            if (myBuchung.getId()==-1) {
                buttonVerwerfenActionPerformed(null);
            } else {
                myBuchung.deleteBuchung();
                buttonVerwerfenActionPerformed(null);
            }            
        }
    }//GEN-LAST:event_buttonLoeschenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BuchungsEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuchungsEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuchungsEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuchungsEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new BuchungsEditor().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLoeschen;
    private javax.swing.JButton buttonSpeichern;
    private javax.swing.JButton buttonVerwerfen;
    private org.jdesktop.swingx.JXDatePicker datepickerDatum;
    private javax.swing.JTextField fieldBetrag;
    private javax.swing.JTextArea fieldBetreff;
    private net.sourceforge.jdatepicker.util.JDatePickerUtil jDatePickerUtil1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable tableAusgaben;
    private javax.swing.JTable tableBudget;
    private javax.swing.JTable tableQuelle;
    private javax.swing.JTable tableZiel;
    // End of variables declaration//GEN-END:variables
}
