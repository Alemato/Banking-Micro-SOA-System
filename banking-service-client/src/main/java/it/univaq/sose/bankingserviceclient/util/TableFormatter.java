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

    public static Table formatObjectDetails(Object obj) {
        if (obj instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                throw new IllegalArgumentException("Collection is empty");
            }
            return formatCollectionDetails(collection);
        } else {
            return formatSingleObjectDetails(obj);
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
                values[i] = fields[i].get(obj).toString();
            } catch (IllegalAccessException e) {
                values[i] = "N/A";
            }
        }

        String[][] data = combineArrays(headers, values);

        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);

        return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
    }

    private static Table formatCollectionDetails(Collection<?> collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Collection is empty");
        }

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
                    values[j] = fields[j].get(obj).toString();
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

    private static String[][] combineArrays(String[] headers, String[] values) {
        String[][] data = new String[2][headers.length];
        data[0] = headers;
        data[1] = values;
        return data;
    }
}
