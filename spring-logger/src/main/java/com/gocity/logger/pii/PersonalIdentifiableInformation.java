package com.gocity.logger.pii;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PersonalIdentifiableInformation {

    public static String obscure(Object object) {
        final var clazz = object.getClass();
        final var values = getFieldVales(object, field -> {
            field.setAccessible(!field.isAnnotationPresent(Obscure.class));

            try {
                return Map.entry(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                return Map.entry(field.getName(), "****");
            } catch (NullPointerException e) {
                return Map.entry(field.getName(), "null");
            }
        });

        return String.format("%s%s", clazz.getSimpleName(), values);
    }

    private static <K, V> HashMap<K, V> getFieldVales(Object object, Function<Field, Map.Entry<K, V>> handler) {
        final var clazz = object.getClass();

        return new HashMap<>() {{
            Arrays.stream(clazz.getDeclaredFields())
                .map(handler)
                .forEach(v -> put(v.getKey(), v.getValue()));
        }};
    }
}


