package numbers.domain;

import java.util.function.LongPredicate;

public enum NumberProperty implements LongPredicate {
    EVEN(number -> number % 2 == 0),
    ODD(number -> number % 2 != 0),
    BUZZ(number -> number % 7 == 0 || number % 10 == 7),
    DUCK(number -> String.valueOf(number).indexOf('0') != -1);

    private final LongPredicate hasProperty;

    NumberProperty(LongPredicate hasProperty) {
        this.hasProperty = hasProperty;
    }

    @Override
    public boolean test(long number) {
        return hasProperty.test(number);
    }

}