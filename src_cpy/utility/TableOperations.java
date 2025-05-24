package utility;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;

public class TableOperations {
    public static void createStandardRowSorter(JTable table) {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(1);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        table.getRowSorter().setSortKeys(sortKeys);

        table.getTableHeader().setReorderingAllowed(false);
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
