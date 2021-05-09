package numbers;

// A very simple solution using only the most basic Java constructs.

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringJoiner;

public final class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        printHelp();
        run();
        System.out.println("Goodbye!");
    }

    private static void run() {
        askRequest:
        while (true) {
            final var request = readRequest();

            if ("0".equals(request[0])) {
                return;
            }
            if (NaturalNumber.notNatural(request[0])) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }

            final var naturalNumber = new NaturalNumber(request[0]);

            if (request.length == 1) {
                naturalNumber.printCard();
                continue;
            }
            if (NaturalNumber.notNatural(request[1])) {
                System.out.println("The second parameter should be a natural number.");
                continue;
            }
            int count = Integer.parseInt(request[1]);
            var query = request.length == 3 ? request[2].split(" ") : new String[0];
            for (var property : query) {
                if (NaturalNumber.isWrong(property)) {
                    System.out.println("The property \"" + property + "\" is wrong.");
                    System.out.println("Available properties: ");
                    System.out.println(Arrays.toString(NaturalNumber.PROPERTIES));
                    continue askRequest;
                }
            }
            while (count > 0) {
                if (naturalNumber.hasProperties(query)) {
                    naturalNumber.printLine();
                    count--;
                }
                naturalNumber.increment();
            }
        }
    }

    private static String[] readRequest() {
        System.out.println();
        System.out.print("Enter a request: ");
        final var input = scanner.nextLine().toLowerCase();
        System.out.println();
        return input.split(" ", 3);
    }

    private static void printHelp() {
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be processed;");
        System.out.println("- two natural numbers and a property to search for;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
    }

}

class NaturalNumber {
    static final String[] PROPERTIES = new String[]{
            "even", "odd", "buzz", "duck", "palindromic", "gapful", "spy"
    };

    static {
        Arrays.sort(PROPERTIES);
    }

    private String digits;
    private long number;

    NaturalNumber(String value) {
        digits = value;
        number = Long.parseLong(value);
    }

    static boolean notNatural(String value) {
        for (var symbol : value.toCharArray()) {
            if (!Character.isDigit(symbol)) {
                return true;
            }
        }
        return "0".equals(value);
    }

    static boolean isWrong(String property) {
        return Arrays.binarySearch(PROPERTIES, property) < 0;
    }

    void printCard() {
        System.out.printf("Properties of %,d%n", number);
        for (var property : PROPERTIES) {
            final var hasProperty = test(property);
            System.out.printf("%12s: %s%n", property, hasProperty);
        }
    }

    void printLine() {
        final var properties = new StringJoiner(", ");
        for (var property : PROPERTIES) {
            if (test(property)) {
                properties.add(property);
            }
        }
        System.out.printf("%,12d is %s%n", number, properties);
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
                return digits.indexOf('0') != -1;
            case "palindromic":
                return new StringBuilder(digits).reverse().toString().equals(digits);
            case "gapful":
                final var divider = (digits.charAt(0) - '0') * 10 + number % 10;
                return number > 100 && number % divider == 0;
            case "spy":
                return digitsSum() == digitsProduct();
        }
        return false;
    }

    void increment() {
        number++;
        digits = String.valueOf(number);
    }

    boolean hasProperties(String[] query) {
        for (var property : query) {
            if (!test(property)) {
                return false;
            }
        }
        return true;
    }

    private long digitsSum() {
        long sum = 0;
        for (long i = number; i > 0; i /= 10) {
            sum += i % 10;
        }
        return sum;
    }

    private long digitsProduct() {
        long product = 1;
        for (long i = number; i > 0; i /= 10) {
            product *= i % 10;
        }
        return product;
    }
}