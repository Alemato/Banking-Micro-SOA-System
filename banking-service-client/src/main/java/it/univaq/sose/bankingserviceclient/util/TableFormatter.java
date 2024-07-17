package it.univaq.sose.bankingserviceclient.util;

import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.shell.table.*;

import java.lang.reflect.Field;
import java.util.Collection;

@Slf4j
public class TableFormatter {

    private TableFormatter() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatObjectDetails(Terminal terminal, Object obj, String title) {
        TerminalUtil.printOnTerminal(terminal, "\n" + title);

        if (obj instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                throw new IllegalArgumentException("Collection is empty");
            }
            return formatCollectionDetails(collection).render(100);
        } else {
            return formatSingleObjectDetails(obj).render(100);
        }
    }

    private static Table formatSingleObjectDetails(Object obj) {
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

        return buildTable(headers, values);
    }

    private static Table formatCollectionDetails(Collection<?> collection) {
        Object firstObj = collection.iterator().next();
        Field[] fields = firstObj.getClass().getDeclaredFields();
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headers[i] = fields[i].getName();
        }

        String[][] data = new String[collection.size() + 1][fields.length];
        data[0] = headers;

        int rowIndex = 1;
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

        return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
    }

    private static Table buildTable(String[] headers, String[] values) {
        String[][] data = new String[2][headers.length];
        data[0] = headers;
        data[1] = values;

        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);

        return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
    }
}
