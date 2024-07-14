package it.univaq.sose.bankingserviceclient.util;

import java.util.Scanner;

public class InputReader {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
