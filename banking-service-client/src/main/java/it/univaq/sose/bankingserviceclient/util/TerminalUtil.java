package it.univaq.sose.bankingserviceclient.util;

import org.jline.terminal.Terminal;

public class TerminalUtil {

    private TerminalUtil() {
        throw new IllegalStateException("Utility class for Terminal");
    }

    public static void printlnOnTerminal(Terminal terminal, String message) {
        int width = terminal.getWidth();
        String msg = String.format("%-" + width + "s", message);
        terminal.writer().write(msg + System.lineSeparator());
        terminal.writer().flush();
    }

    public static void printOnTerminal(Terminal terminal, String message) {
        System.out.print(message);
    }
}
