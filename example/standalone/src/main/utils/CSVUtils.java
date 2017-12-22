package utils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    /**
     * Formats list of strings for CSV and writes to file.
     * @param w CSV file writer
     * @param values list of strings to be written to CSV file
     */
    public static void writeLine(Writer w, List<String> values) throws IOException {
        newLine(w, values, DEFAULT_SEPARATOR);
    }

    /**
     * Formats provided string for CSV file.
     */
    private static String formatForCSV(String value) {
        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;
    }

    /**
     * Writes new line to CSV file.
     * @param w CSV file writer
     * @param values list of strings to be written to CSV file
     * @param separators separators in strings of values to be formatted for CSV file
     */
    private static void newLine(Writer w, List<String> values, char separators) throws IOException {
        boolean first = true;
        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            sb.append(formatForCSV(value));
            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }
}
