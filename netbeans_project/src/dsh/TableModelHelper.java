/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
/**
 *
 * Original from: http://technojeeves.com/joomla/index.php/free/59-resultset-to-tablemodel
 * Adaption: Set editable
 */
public class TableModelHelper {
    public static DefaultTableModel resultSetToTableModel(ResultSet rs,boolean editable) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();

            // Get the column names
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();

            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }
            if (editable) {
                return new DefaultTableModel(rows, columnNames);
            }
            else {
                DefaultTableModel buffer=TableModelHelper.createEmptyTableModel(true);
                buffer.setDataVector (rows, columnNames);
                return buffer;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static DefaultTableModel createEmptyTableModel (boolean editable) {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }
    public static void packColumns(JTable table) {
        packColumns (table, 3);
    }

    public static void packColumns(JTable table, int margin) {  
        //Fremdcode: http://www.exampledepot.com/egs/javax.swing.table/PackCol.html
        for (int c=0; c<table.getColumnCount(); c++) {
            packColumn(table, c, margin);
        }
    }    
    public static void packColumn(JTable table, int vColIndex, int margin) { 
        //Fremdcode: http://www.exampledepot.com/egs/javax.swing.table/PackCol.html
        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        // Add margin
        width += 2*margin;

        // Set the width
        col.setPreferredWidth(width);
    }    
    public static void selectRowByIDvalue (JTable table,int id) {
        selectRowByIDvalue (table,id,0);
    }
        
    public static void selectRowByIDvalue (JTable table,int id,int col) {
        for (int r=0; r<table.getRowCount(); r++) {
            if (((java.lang.Integer)table.getValueAt(r, col)).intValue()==id) {
                table.setRowSelectionInterval(r,r);
            }
        }
    }
    
    private static int getColumnSize (DefaultTableModel tMod,int col) {
        int buffer =tMod.getColumnName(col).length(); // min: Size of Col-Name
           for (int j = 0; j < tMod.getRowCount(); j++) {
                    if ((tMod.getValueAt(j,col)!=null)&&(tMod.getValueAt(j,col).toString().length() > buffer)) {
                        buffer=tMod.getValueAt(j,col).toString().length();
                    }
                }
        return buffer;
    }
    
    public static void printTabelleToLog (DefaultTableModel tMod) {
        // Vertical Blocks:
        // Zeilenname
        int colSize=0;
        int tableItemBuffer =0;
        System.out.print ("   ");
        for (int colLooper=0; colLooper< tMod.getColumnCount(); colLooper++) {
            System.out.print (tMod.getColumnName(colLooper));
            colSize = getColumnSize(tMod,colLooper);
            for (int charLooper=1; charLooper<= colSize-tMod.getColumnName(colLooper).length(); charLooper++) {
                System.out.print (" ");
            }
            System.out.print (" | ");
        }
        System.out.println ();
        // Tabellenrahmen
        System.out.print ("   ");
        for (int colLooper=0; colLooper< tMod.getColumnCount(); colLooper++) {
            colSize = getColumnSize(tMod,colLooper);
            for (int charLooper=1; charLooper<= (colSize+3); charLooper++) {
                System.out.print ("=");
            }
        }
        // Zeilen mit Daten
        System.out.println ();
        for (int rowLooper=0; rowLooper< tMod.getRowCount(); rowLooper++) {
            System.out.print ("   "); // nicht markierte Zeile
            for (int colLooper=0; colLooper< tMod.getColumnCount(); colLooper++) {
                colSize = getColumnSize(tMod,colLooper);
                if (tMod.getValueAt(rowLooper,colLooper)!=null) {
                    tableItemBuffer=tMod.getValueAt(rowLooper,colLooper).toString().length();
                    System.out.print (tMod.getValueAt(rowLooper,colLooper).toString());
                } else {
                    tableItemBuffer =0;
                }
                // Leerzeichen bis Spalten-Ende
                for (int charLooper=1; charLooper<=colSize-tableItemBuffer; charLooper++) {
                    System.out.print (" ");
                }
                System.out.print (" | ");
            }
            System.out.println ();
        }
    }
}
