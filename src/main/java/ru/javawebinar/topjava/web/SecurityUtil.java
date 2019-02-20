package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int authUserId = 0;

    public static void setAuthUserId(int authUserId) {
        SecurityUtil.authUserId = authUserId;
    }

    public static int getAuthUserId() {
        return SecurityUtil.authUserId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}