package me.alexflipnote.kawaiibot.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Helpers {

    private static final ClassLoader classLoader = Helpers.class.getClassLoader();
    private static final Random random = new Random();

    public static String chooseRandom(String[] array) {
        return array[random.nextInt(array.length)];
    }

    public static JSONObject chooseRandom(JSONArray array) {
        return array.getJSONObject(random.nextInt(array.length()));
    }

    public static String chooseRandom(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static String parseTime(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        long days = (milliseconds / (1000 * 60 * 60 * 24));

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    public static String pad(String content, String padWith, int length) {
        if (content.length() >= length)
            return content;

        StringBuilder sb = new StringBuilder(content);

        while (sb.length() < length)
            sb.append(padWith);

        return sb.toString();
    }

    public static InputStream getImageStream(String filename) {
        return classLoader.getResourceAsStream(filename);
    }

    public static boolean keyExists(JSONObject json, String key) {
        return json.has(key) && !json.isNull(key);
    }

    public static String[] splitText(String content, int limit) {
        ArrayList<String> pages = new ArrayList<>();

        String[] lines = content.trim().split("\n");
        StringBuilder chunk = new StringBuilder();

        for (String line : lines) {
            if (chunk.length() > 0 && chunk.length() + line.length() > limit) {
                pages.add(chunk.toString());
                chunk = new StringBuilder();
            }

            if (line.length() > limit) {
                int lineChunks = line.length() / limit;

                for (int i = 0; i < lineChunks; i++) {
                    int start = limit * i;
                    int end = start + limit;
                    pages.add(line.substring(start, end));
                }
            } else {
                chunk.append(line).append("\n");
            }
        }

        if (chunk.length() > 0)
            pages.add(chunk.toString());

        return pages.toArray(new String[pages.size()]);

    }

}
