package output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static utils.Constants.*;

public class CSVMatrix implements FormatterInterface {

    private Formatter formatter;

    public CSVMatrix() {
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public void drawOutput() {
        String csvFile = "/Users/Justin/Desktop/killMatrix.csv";
        try {
            FileWriter writer = new FileWriter(csvFile);
            List<String> header = new ArrayList<>();
            header.add("");
            for (int id : formatter.getMutantIds()) {
                header.add(Integer.toString(id));
            }
            CSVUtils.writeLine(writer, header);
            for (String test : formatter.getTestNames()) {
                List<String> line = new ArrayList<>();
                line.add(test);
                for (Integer result : formatter.getMutantsForTest(test)) {
                    if (result == NOT_COVERED) {
                        line.add("");
                    } else {
                        line.add(result.toString());
                    }
                }
                CSVUtils.writeLine(writer, line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Mutation Score: " + formatter.getMutationScore());
    }
}
