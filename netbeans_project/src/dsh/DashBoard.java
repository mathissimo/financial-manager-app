/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DashBoard.java
 *
 * Created on Jun 25, 2012, 5:19:56 PM
 */
package dsh;

/**
 *
 * @author maak
 */
public class DashBoard extends javax.swing.JFrame {
    private Model myModel;
   

    /** Creates new form DashBoard */
    public DashBoard() {
        initComponents();
    }
    
    public void initDashboard () {
        myModel=DshApp.getModel();
        initGauges ();
    }
    
    public void initGauges () {
        // Kontostände
        tableSalden.setModel(myModel.getOverviewSalden());
        TableModelHelper.packColumns(tableSalden);
        tableSalden.setRowSelectionAllowed(false);
        labelSuperSaldo.setText(myModel.getSuperSaldo());        
        
        // Budgets
        tableBudgets.setModel(myModel.getOverviewActiveBudgets());
        //TableModelHelper.packColumns(tableBudgets);
        tableSalden.setRowSelectionAllowed(false);
        
        // Ausgaben
        tableAusgaben.setModel(myModel.getOverviewAusgaben());
        TableModelHelper.packColumns(tableAusgaben);
        tableSalden.setRowSelectionAllowed(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSalden = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableBudgets = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableAusgaben = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        buttonEnde = new javax.swing.JButton();
        buttonBuchungen = new javax.swing.JButton();
        buttonNeueBuchung = new javax.swing.JButton();
        buttonBudgets = new javax.swing.JButton();
        buttonKontoManager = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        labelSuperSaldo = new javax.swing.JLabel();
        buttonAktualisieren = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        buttonAusgabenTypen = new javax.swing.JButton();
        buttonBudgetTypManager = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableSalden.setModel(new javax.swing.table.DefaultTableModel(
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
        tableSalden.setName("tableSalden"); // NOI18N
        jScrollPane1.setViewportView(tableSalden);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 220, 156));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableBudgets.setModel(new javax.swing.table.DefaultTableModel(
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
        tableBudgets.setName("tableBudgets"); // NOI18N
        jScrollPane2.setViewportView(tableBudgets);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 240, 216));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

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
        jScrollPane3.setViewportView(tableAusgaben);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 260, 300));

        jLabel1.setName("jLabel1"); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 134, -1));

        jLabel3.setName("jLabel3"); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, -1, -1));

        jLabel4.setName("jLabel4"); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(dsh.DshApp.class).getContext().getResourceMap(DashBoard.class);
        buttonEnde.setText(resourceMap.getString("buttonEnde.text")); // NOI18N
        buttonEnde.setName("buttonEnde"); // NOI18N
        buttonEnde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEndeActionPerformed(evt);
            }
        });
        jPanel1.add(buttonEnde, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, 70, -1));

        buttonBuchungen.setText(resourceMap.getString("buttonBuchungen.text")); // NOI18N
        buttonBuchungen.setName("buttonBuchungen"); // NOI18N
        buttonBuchungen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBuchungenActionPerformed(evt);
            }
        });
        jPanel1.add(buttonBuchungen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 389, 230, 40));

        buttonNeueBuchung.setText(resourceMap.getString("buttonNeueBuchung.text")); // NOI18N
        buttonNeueBuchung.setName("buttonNeueBuchung"); // NOI18N
        buttonNeueBuchung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNeueBuchungActionPerformed(evt);
            }
        });
        jPanel1.add(buttonNeueBuchung, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 490, 60));

        buttonBudgets.setText(resourceMap.getString("buttonBudgets.text")); // NOI18N
        buttonBudgets.setName("buttonBudgets"); // NOI18N
        buttonBudgets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBudgetsActionPerformed(evt);
            }
        });
        jPanel1.add(buttonBudgets, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 389, 240, 40));

        buttonKontoManager.setText(resourceMap.getString("buttonKontoManager.text")); // NOI18N
        buttonKontoManager.setName("buttonKontoManager"); // NOI18N
        buttonKontoManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonKontoManagerActionPerformed(evt);
            }
        });
        jPanel1.add(buttonKontoManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 90, -1));

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 310, 30));

        labelSuperSaldo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelSuperSaldo.setName("labelSuperSaldo"); // NOI18N
        jPanel1.add(labelSuperSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 210, 20));

        buttonAktualisieren.setText(resourceMap.getString("buttonAktualisieren.text")); // NOI18N
        buttonAktualisieren.setName("buttonAktualisieren"); // NOI18N
        buttonAktualisieren.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAktualisierenActionPerformed(evt);
            }
        });
        jPanel1.add(buttonAktualisieren, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 400, 120, -1));

        jLabel6.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, -1, -1));

        jLabel7.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel8.setFont(resourceMap.getFont("jLabel7.font")); // NOI18N
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, -1, -1));

        jLabel9.setFont(resourceMap.getFont("jLabel9.font")); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        buttonAusgabenTypen.setText(resourceMap.getString("buttonAusgabenTypen.text")); // NOI18N
        buttonAusgabenTypen.setName("buttonAusgabenTypen"); // NOI18N
        buttonAusgabenTypen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAusgabenTypenActionPerformed(evt);
            }
        });
        jPanel1.add(buttonAusgabenTypen, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, 90, -1));

        buttonBudgetTypManager.setText(resourceMap.getString("buttonBudgetTypManager.text")); // NOI18N
        buttonBudgetTypManager.setName("buttonBudgetTypManager"); // NOI18N
        buttonBudgetTypManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBudgetTypManagerActionPerformed(evt);
            }
        });
        jPanel1.add(buttonBudgetTypManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 90, -1));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 824, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAktualisierenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAktualisierenActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            initGauges ();
        }
    }//GEN-LAST:event_buttonAktualisierenActionPerformed

    private void buttonBuchungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBuchungenActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().listBuchungen();            
        }
    }//GEN-LAST:event_buttonBuchungenActionPerformed

    private void buttonEndeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEndeActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().killme ();            
        }
    }//GEN-LAST:event_buttonEndeActionPerformed

    private void buttonNeueBuchungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNeueBuchungActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().createBuchung();            
        }
    }//GEN-LAST:event_buttonNeueBuchungActionPerformed

    private void buttonKontoManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonKontoManagerActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().startKontoEditor();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonKontoManagerActionPerformed

    private void buttonAusgabenTypenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAusgabenTypenActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().startAusgabenTypEditor();            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonAusgabenTypenActionPerformed

    private void buttonBudgetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBudgetsActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().startBudgetEditor();            
        }
    }//GEN-LAST:event_buttonBudgetsActionPerformed

    private void buttonBudgetTypManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBudgetTypManagerActionPerformed
        if (DshApp.getApplication().isTheTopWindow(this)) {
            DshApp.getApplication().startBudgetTypEditor();            
        }
    }//GEN-LAST:event_buttonBudgetTypManagerActionPerformed

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
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DashBoard().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAktualisieren;
    private javax.swing.JButton buttonAusgabenTypen;
    private javax.swing.JButton buttonBuchungen;
    private javax.swing.JButton buttonBudgetTypManager;
    private javax.swing.JButton buttonBudgets;
    private javax.swing.JButton buttonEnde;
    private javax.swing.JButton buttonKontoManager;
    private javax.swing.JButton buttonNeueBuchung;
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
    private javax.swing.JLabel labelSuperSaldo;
    private javax.swing.JTable tableAusgaben;
    private javax.swing.JTable tableBudgets;
    private javax.swing.JTable tableSalden;
    // End of variables declaration//GEN-END:variables
}
