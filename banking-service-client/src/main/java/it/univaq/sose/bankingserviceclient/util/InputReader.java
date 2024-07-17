package it.univaq.sose.bankingserviceclient.util;

import org.jline.terminal.Terminal;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Scanner;

public class InputReader {

    private static final Scanner scanner = new Scanner(System.in);

    public static String singleReadInput(Terminal terminal, String prompt) {
        TerminalUtil.printOnTerminal(terminal, "Enter the " + prompt);
        return scanner.nextLine();
    }

    public static String singleReadInputCustom(Terminal terminal, String prompt) {
        TerminalUtil.printOnTerminal(terminal, prompt);
        return scanner.nextLine();
    }

    public static <T> T multipleReadInputs(Terminal terminal, Class<T> clazz) {
        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of class", e);
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            TerminalUtil.printOnTerminal(terminal, "Enter the " + field.getName() + ": ");
            String input = scanner.nextLine();
            try {
                if (field.getType().equals(String.class)) {
                    field.set(instance, input);
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.set(instance, Integer.parseInt(input));
                } else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                    field.set(instance, Long.parseLong(input));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(instance, BigDecimal.valueOf(Long.parseLong(input)));
                } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.set(instance, Double.parseDouble(input));
                } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    field.set(instance, Boolean.parseBoolean(input));
                }
                // Aggiungi altre conversioni di tipi se necessario
            } catch (IllegalAccessException e) {
                TerminalUtil.printOnTerminal(terminal, "Failed to set field: " + field.getName());
            }
        }

        return instance;
    }
}
