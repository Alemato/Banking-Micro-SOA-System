package it.univaq.sose.bankingserviceclient.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.table.*;

import java.lang.reflect.Field;
import java.util.Collection;

@Slf4j
public class TableFormatter {

    private TableFormatter() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatObjectDetails(Object obj, String title) {
        Table table;
        if (obj instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                throw new IllegalArgumentException("Collection is empty");
            }
            table = formatCollectionDetails(collection, title);
        } else {
            table = formatSingleObjectDetails(obj, title);
        }
        return table.render(80);
    }

    private static Table formatSingleObjectDetails(Object obj, String title) {
        Field[] fields = obj.getClass().getDeclaredFields();
        String[] headers = new String[fields.length];
        String[] values = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headers[i] = fields[i].getName();
            try {
                Object value = fields[i].get(obj);
                values[i] = value != null ? value.toString() : "N/A";
            } catch (IllegalAccessException e) {
                values[i] = "N/A";
            }
        }

        return buildTableWithTitle(title, headers, values);
    }

    private static Table formatCollectionDetails(Collection<?> collection, String title) {
        Object firstObj = collection.iterator().next();
        Field[] fields = firstObj.getClass().getDeclaredFields();
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headers[i] = fields[i].getName();
        }

        String[][] data = new String[collection.size() + 2][fields.length];
        data[0][0] = title; // Title row
        for (int i = 1; i < fields.length; i++) {
            data[0][i] = "";
        }
        data[1] = headers; // Headers row

        int rowIndex = 2;
        for (Object obj : collection) {
            String[] values = new String[fields.length];
            for (int j = 0; j < fields.length; j++) {
                try {
                    Object value = fields[j].get(obj);
                    values[j] = value != null ? value.toString() : "N/A";
                } catch (IllegalAccessException e) {
                    values[j] = "N/A";
                }
            }
            data[rowIndex++] = values;
        }

        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);

        tableBuilder.on(CellMatchers.row(0)).addAligner(SimpleHorizontalAligner.center); // Center the title
        tableBuilder.addFullBorder(BorderStyle.fancy_light);

        return tableBuilder.build();
    }

    private static Table buildTableWithTitle(String title, String[] headers, String[] values) {
        String[][] data = new String[3][headers.length];
        data[0][0] = title; // Title row
        for (int i = 1; i < headers.length; i++) {
            data[0][i] = "";
        }
        data[1] = headers; // Headers row
        data[2] = values; // Values row

        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);

        tableBuilder.on(CellMatchers.row(0)).addAligner(SimpleHorizontalAligner.center); // Center the title
        tableBuilder.addFullBorder(BorderStyle.fancy_light);

        return tableBuilder.build();
    }
}
