package utility;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TableOperations {
    public static void createStandardRowSorter(JTable table) {
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>(1);
//        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
//        table.getRowSorter().setSortKeys(sortKeys);
//
//        table.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());

        // Set a custom comparator for Integer columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getModel().getColumnClass(i) == Integer.class) {
                sorter.setComparator(i, Comparator.comparingInt(o -> (Integer) o));
            }
        }

        table.setRowSorter(sorter);
    }

    public static void centerColumnClass(JTable table, Class<?> columnClass) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(columnClass, centerRenderer);
    }

    public static void createInlineTableComboBox(JTable table, int columnIndex, List collection) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        JComboBox inlineSubjectComboBox = new JComboBox();
        for (var item : collection) {
            inlineSubjectComboBox.addItem(item);
        }

        column.setCellEditor(new DefaultCellEditor(inlineSubjectComboBox));
    }
}
