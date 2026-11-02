package GUI.Components;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Table extends javax.swing.JTable{
    
    public Table(){
        getModel().addTableModelListener(this);
        setShowHorizontalLines(true);
        setGridColor(new Color(230,230,230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);
        setAutoCreateRowSorter(true);
        

        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int column) {
                TableHeader header = new TableHeader(o + "");
                
                return header;
            }
        });
    }

    
    /** Sorts these columns as numbers instead of text ("21" after "6"). */
    public void setNumericColumns(int... columns) {
        if (getRowSorter() instanceof TableRowSorter) {
            TableRowSorter<?> sorter = (TableRowSorter<?>) getRowSorter();
            for (int column : columns) {
                sorter.setComparator(column, (a, b) -> Double.compare(toNumber(a), toNumber(b)));
            }
        }
    }

    private static double toNumber(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(o));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public DefaultTableModel getTableModel(){
        DefaultTableModel model = (DefaultTableModel) super.getModel();
        return model;
    }

    @Override
    public int getSelectedRow() {
        return super.getSelectedRow(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
}
