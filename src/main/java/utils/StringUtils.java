package utils;

import java.util.Collection;

public class StringUtils {

    public static String listToString(Collection<?> collection, int indentation) {
        if (collection == null || collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        String prefix = "\n" + " ".repeat(indentation);
        for (Object o : collection) {
            sb.append(prefix).append(o).append(',');
        }
        return sb.append("\n").append(" ".repeat(indentation - 2)).append(']').toString();
    }
}
