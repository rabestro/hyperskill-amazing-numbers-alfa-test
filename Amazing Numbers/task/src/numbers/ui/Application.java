package numbers.ui;

import numbers.domain.NumberProperty;

import java.util.Scanner;

public final class Application implements Runnable {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        System.out.println("Enter a natural number:");
        final var number = scanner.nextLong();

        if (number > 0) {
            System.out.printf("Properties of %,d%n", number);
            for (var property : NumberProperty.values()) {
                final var name = property.name().toLowerCase();
                final var hasProperty = property.test(number);
                System.out.printf("%12s: %s%n", name, hasProperty);
            }
        } else {
            System.out.println("This number is not natural!");
        }
    }
}
