package it.polimi.ingsw.utils.text;

import java.util.Random;

// TODO: document
public final class RandomString {
    private static final String ALL_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456";
    private static Random random = new Random(System.currentTimeMillis());

    private RandomString() {
    }

    public static String create() {
        int bound1 = random.nextInt(128);
        int bound2 = random.nextInt(128);

        return create(Math.min(bound1, bound2), Math.max(bound1, bound2), ALL_CHARACTERS);
    }

    public static String create(String allowedCharacter) {
        int bound1 = random.nextInt(128);
        int bound2 = random.nextInt(128);

        return create(Math.min(bound1, bound2), Math.max(bound1, bound2), allowedCharacter);
    }

    public static String create(int length) {
        return create(length, length, ALL_CHARACTERS);
    }

    public static String create(int length, String allowedCharacter) {
        return create(length, length, allowedCharacter);
    }

    public static String create(int minLength, int maxLength, String allowedCharacter) {
        StringBuilder stringBuilder = new StringBuilder(minLength);
        int allowedCount = allowedCharacter.length();

        for (int i = 0; i < maxLength; i++) {
            if (i >= minLength && random.nextBoolean()) {
                break;
            }

            int nextCharIndex = random.nextInt(allowedCount);
            stringBuilder.append(allowedCharacter.charAt(nextCharIndex));
        }

        return stringBuilder.toString();
    }
}
