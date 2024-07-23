package it.univaq.sose.bankingserviceclient.util;

import org.jline.terminal.Terminal;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Utility class for reading user input from the terminal.
 */
public class InputReader {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a single input from the user with a specified prompt.
     *
     * @param terminal the terminal to print the prompt
     * @param prompt   the prompt message
     * @return the user's input as a String
     */
    public static String singleReadInput(Terminal terminal, String prompt) {
        TerminalUtil.printOnTerminal(terminal, "Enter the " + prompt);
        return scanner.nextLine();
    }


    /**
     * Reads a single custom input from the user with a specified prompt.
     *
     * @param terminal the terminal to print the prompt
     * @param prompt   the prompt message
     * @return the user's input as a String
     */
    public static String singleReadInputCustom(Terminal terminal, String prompt) {
        TerminalUtil.printOnTerminal(terminal, prompt);
        return scanner.nextLine();
    }

    /**
     * Reads multiple inputs from the user and populates an instance of the specified class with the inputs.
     *
     * @param terminal the terminal to print the prompts
     * @param clazz    the class to instantiate and populate
     * @param <T>      the type of the class
     * @return an instance of the specified class populated with user inputs
     */
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
                TerminalUtil.printlnOnTerminal(terminal, "Failed to set field: " + field.getName());
            }
        }

        return instance;
    }
}
