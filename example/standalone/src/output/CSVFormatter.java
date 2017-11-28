package output;

import utils.CSVUtils;
import utils.Outcome;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static utils.Outcome.*;

public class CSVFormatter implements Formatter {

    public CSVFormatter() {

    }

    /**
     * Writes new CSV file containing kill matrix data. Test outcomes are represented in CSV by integer id values.
     * @param matrix kill matrix data to be written to CSV file
     */
    public void drawOutput(DefaultKillMatrix matrix) {
        String csvFile = "./killMatrix.csv";
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
        System.out.println("Mutation Score: " + (matrix.getMutationScore() * 100) + "%");
    }
}
