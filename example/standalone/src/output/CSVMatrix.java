package output;

import utils.Outcome;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static utils.Outcome.*;

public class CSVMatrix implements Formatter {

    public CSVMatrix() {
    }

    public void drawOutput(DefaultKillMatrix matrix) {
        String csvFile = "/Users/Justin/Desktop/killMatrix.csv";
        try {
            FileWriter writer = new FileWriter(csvFile);
            List<String> header = new ArrayList<>();
            header.add("");
            for (int id : matrix.getMutantIds()) {
                header.add(Integer.toString(id));
            }
            CSVUtils.writeLine(writer, header);
            for (String test : matrix.getTestNames()) {
                List<String> line = new ArrayList<>();
                line.add(test);
                for (Outcome result : matrix.getMutantsForTest(test)) {
                    if (result == NOT_COVERED) {
                        line.add("");
                    } else {
                        line.add(Integer.toString(result.getId()));
                    }
                }
                CSVUtils.writeLine(writer, line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Mutation Score: " + matrix.getMutationScore());
    }
}
