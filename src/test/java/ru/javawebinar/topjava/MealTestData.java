package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL01_ID = START_SEQ + 2;
    public static final int MEAL02_ID = START_SEQ + 3;
    public static final int MEAL03_ID = START_SEQ + 4;
    public static final int MEAL04_ID = START_SEQ + 5;
    public static final int MEAL05_ID = START_SEQ + 6;
    public static final int MEAL06_ID = START_SEQ + 7;

    public static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2019, 2, 23, 11,  1, 35);
    public static final LocalDateTime END_DATE_TIME = LocalDateTime.of(2019, 2, 23, 17,  1, 35);

    //USER
    public static final Meal MEAL01 = new Meal(MEAL01_ID, LocalDateTime.of(2019, 2, 23, 10,  1, 35), "Завтрак", 500);
    public static final Meal MEAL02 = new Meal(MEAL02_ID, LocalDateTime.of(2019, 2, 23, 13,  1, 35), "Обед", 1000);
    public static final Meal MEAL03 = new Meal(MEAL03_ID, LocalDateTime.of(2019, 2, 23, 18,  1, 35), "Ужин", 500);

    //ADMIN
    public static final Meal MEAL04 = new Meal(MEAL04_ID, LocalDateTime.of(2019, 2, 24, 9,  15, 35), "Завтрак", 1000);
    public static final Meal MEAL05 = new Meal(MEAL05_ID, LocalDateTime.of(2019, 2, 24, 14,  1, 35), "Обед", 500);
    public static final Meal MEAL06 = new Meal(MEAL06_ID, LocalDateTime.of(2019, 2, 24, 19,  1, 35), "Ужин", 510);

    public static void assertMealMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMealMatch(Iterable<Meal> actual, Meal... expected) {
        assertMealMatch(actual, Arrays.asList(expected));
    }

    private static void assertMealMatch(Iterable<Meal> actual, List<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }

}
