package kevin.utils;

import java.util.Random;

public final class RandomUtils {

    public static final Random random = new Random();

    public static int nextInt(int startInclusive, int endExclusive) {
        return endExclusive - startInclusive <= 0 ?
                startInclusive :
                startInclusive + random.nextInt(endExclusive - startInclusive);
    }

    public static double nextDouble(double startInclusive, double endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0.0 ?
                startInclusive :
                startInclusive + (endInclusive - startInclusive) * Math.random();
    }

    public static float nextFloat(float startInclusive, float endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0f ?
                startInclusive :
                (float) (startInclusive + (endInclusive - startInclusive) * Math.random());
    }

    public static String randomNumber(int length) {
        return nextString(length, "123456789");
    }

    public static String randomString(int length) {
        return nextString(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    private static String nextString(int length, String chars) {
        char[] charArray = chars.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charArray.length);
            stringBuilder.append(charArray[index]);
        }
        return stringBuilder.toString();
    }
}
