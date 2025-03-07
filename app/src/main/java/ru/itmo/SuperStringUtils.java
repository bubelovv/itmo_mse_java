package ru.itmo;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SuperStringUtils {
    public static String concat(String... args) {
        return String.join(":", args);
    }
}