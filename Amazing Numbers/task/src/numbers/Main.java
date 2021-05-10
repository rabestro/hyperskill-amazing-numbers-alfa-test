package numbers;

// A very simple solution using only the most basic Java constructs.

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringJoiner;

import static java.util.Arrays.binarySearch;

public final class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        printHelp();
        run();
        System.out.println("Goodbye!");
    }

    private static void run() {
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
            final var query = request.length == 3 ? request[2].split(" ") : new String[0];
            final var wrong = NaturalNumber.getWrongProperties(query);
            if (!wrong.isBlank()) {
                System.out.printf(wrong.contains(", ") ?
                        "The properties %s are wrong" : "The property %s is wrong", wrong);
                System.out.println("Available properties: ");
                System.out.println(Arrays.toString(NaturalNumber.PROPERTIES));
                continue;
            }

            final var mutuallyExclusivePairs = NaturalNumber.getMutuallyExclusive(query);

            if (!mutuallyExclusivePairs.isBlank()) {
                System.out.print("The request contains mutually exclusive properties: ");
                System.out.println(mutuallyExclusivePairs);
                System.out.println("There are no numbers with these properties.");
                continue;
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
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
    }

}

class NaturalNumber {
    static final String[] PROPERTIES = new String[]{
            "even", "odd", "buzz", "duck", "palindromic", "gapful",
            "spy", "square", "sunny", "jumping", "happy", "sad"
    };
    private static final String[][] EXCLUSIVE = new String[][]{
            {"even", "odd"}, {"spy", "duck"}, {"sunny", "square"}, {"happy", "sad"},
            {"-even", "-odd"}, {"-happy", "-sad"}
    };
    public static final String[][] MUTUALLY_EXCLUSIVE = new String[EXCLUSIVE.length + PROPERTIES.length][];

    static {
        Arrays.sort(PROPERTIES);
        int index = 0;
        for (var pair : EXCLUSIVE) {
            MUTUALLY_EXCLUSIVE[index++] = pair;
        }
        for (var property : PROPERTIES) {
            MUTUALLY_EXCLUSIVE[index++] = new String[]{property, "-" + property};
        }
    }

    private String digits;
    private long number;

    NaturalNumber(final String value) {
        digits = value;
        number = Long.parseLong(value);
    }

    NaturalNumber(final long value) {
        digits = String.valueOf(value);
        number = value;
    }

    static boolean notNatural(final String value) {
        for (var symbol : value.toCharArray()) {
            if (!Character.isDigit(symbol)) {
                return true;
            }
        }
        return "0".equals(value);
    }

    static boolean isWrong(final String property) {
        return binarySearch(PROPERTIES, property) < 0;
    }

    static String getWrongProperties(final String[] query) {
        var wrong = new StringJoiner(", ");
        for (var property : query) {
            var name = property.charAt(0) == '-' ? property.substring(1) : property;
            if (NaturalNumber.isWrong(name)) {
                wrong.add(property);
            }
        }
        return wrong.toString();
    }

    static String getMutuallyExclusive(final String[] query) {
        Arrays.sort(query);
        var me = new StringJoiner(", ");
        for (var pair : MUTUALLY_EXCLUSIVE) {
            var containsPair = binarySearch(query, pair[0]) >= 0 && binarySearch(query, pair[1]) >= 0;
            if (containsPair) {
                me.add(pair[0] + " and " + pair[1]);
            }
        }
        return me.toString();
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
            case "square":
                return (long) Math.pow((long) Math.sqrt(number), 2) == number;
            case "sunny":
                return new NaturalNumber(number + 1).test("square");
            case "jumping":
                for (long p = number % 10, rest = number / 10; rest > 0; rest /= 10) {
                    long c = rest % 10;
                    long d = p - c;
                    if (d != 1 && d != -1) {
                        return false;
                    }
                    p = c;
                }
                return true;
            case "happy":
                return isHappy();
            case "sad":
                return !isHappy();
        }
        return false;
    }

    void increment() {
        number++;
        digits = String.valueOf(number);
    }

    boolean hasProperties(String[] query) {
        for (var property : query) {
            var isNegative = property.charAt(0) == '-';
            if (isNegative ? test(property.substring(1)) : !test(property)) {
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

    private boolean isHappy() {
        final var sequence = new HashSet<Long>();
        for (long i = number; !sequence.contains(i); i = happyNext(i)) {
            if (i == 1) {
                return true;
            }
            sequence.add(i);
        }
        return false;
    }

    private long happyNext(long number) {
        long result = 0;
        for (long i = number; i > 0; i /= 10) {
            int digit = (int) (i % 10);
            result += digit * digit;
        }
        return result;
    }
}