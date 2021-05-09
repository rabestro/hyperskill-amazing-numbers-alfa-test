package numbers;

// A very simple solution using only the most basic Java constructs.

import java.util.Scanner;

public final class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] PROPERTIES = new String[]{
            "even", "odd", "buzz", "duck", "harshad", "palindromic"
    };
    private final long number;

    Main(long number) {
        this.number = number;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        printHelp();
        run();
        System.out.println("Goodbye!");
    }

    private static void run() {
        while (true) {
            System.out.println();
            System.out.println("Enter a request:");
            final var number = scanner.nextLong();
            if (number == 0) {
                return;
            }
            if (number < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }
            var naturalNumber = new Main(number);
            naturalNumber.printCard();
        }
    }

    private static void printHelp() {
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be processed;");
        System.out.println("- enter 0 to exit.");
    }

    private void printCard() {
        System.out.printf("Properties of %,d%n", number);
        for (var property : PROPERTIES) {
            final var hasProperty = test(property);
            System.out.printf("%12s: %s%n", property, hasProperty);
        }
    }

    private boolean test(final String property) {
        switch (property) {
            case "even":
                return number % 2 == 0;
            case "odd":
                return number % 2 != 0;
            case "buzz":
                return number % 7 == 0 || number % 10 == 7;
            case "duck":
                return String.valueOf(number).indexOf('0') != -1;
            case "palindromic":
                final var digits = String.valueOf(number);
                return new StringBuilder(digits).reverse().toString().equals(digits);
            case "harshad":
                return number % digitsSum() == 0;
        }
        return false;
    }

    private long digitsSum() {
        long sum = 0;
        for (long i = number; i > 0; i /= 10) {
            sum += i % 10;
        }
        return sum;
    }
}
