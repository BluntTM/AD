package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class StreamUtils {

    private StreamUtils() {}

    public static InputStream getResourceAsStream(String name) {
        return StreamUtils.class.getResourceAsStream(name);
    }

    public static String read(String name) {
        return read(getResourceAsStream(name));
    }

    public static String read(InputStream in) {
        return new BufferedReader(new InputStreamReader(in))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
