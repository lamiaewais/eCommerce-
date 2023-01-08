package com.example.demo;

import java.lang.reflect.Field;

public class TestUtil {
    public static void injectObjects(Object target, String field, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field field1 = target.getClass().getDeclaredField(field);
            if (!field1.isAccessible()) {
                field1.setAccessible(true);
                wasPrivate = true;
            }

            field1.set(target, toInject);
            if (wasPrivate) {
                field1.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

