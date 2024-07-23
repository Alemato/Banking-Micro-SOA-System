package it.univaq.sose.bankingserviceclient.util;

import org.jline.terminal.Terminal;

/**
 * Utility class for printing messages to the terminal.
 */
public class TerminalUtil {

    private TerminalUtil() {
        throw new IllegalStateException("Utility class for Terminal");
    }

    /**
     * Prints a message to the terminal with a new line, formatted to the terminal width.
     *
     * @param terminal the terminal to print the message
     * @param message  the message to print
     */
    public static void printlnOnTerminal(Terminal terminal, String message) {
        int width = terminal.getWidth();
        String msg = String.format("%-" + width + "s", message);
        terminal.writer().write(msg + System.lineSeparator());
        terminal.writer().flush();
    }

    /**
     * Prints a message to the terminal without a new line.
     *
     * @param terminal the terminal to print the message
     * @param message  the message to print
     */
    public static void printOnTerminal(Terminal terminal, String message) {
        System.out.print(message);
    }
}
