package output;

import utils.Outcome;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static utils.Outcome.*;

public class CSVKilledFormatter implements Formatter {

    public CSVKilledFormatter() {
    }

    public void drawOutput(DefaultKillMatrix matrix) {
        String csvFile = "./killed.csv";
        try {
            FileWriter writer = new FileWriter(csvFile);
            List<String> header = new ArrayList<>();
            header.add("MutantNo");
            header.add("[FAIL | TIME | EXC | LIVE]");
            CSVUtils.writeLine(writer, header);
            for (int mutant: matrix.getMutantIds()) {
                List<String> line = new ArrayList<>();
                HashMap<String, Outcome> tests = matrix.getTestsForMutant(mutant);
                if (tests.size() > 0) {
                    line.add(Integer.toString(mutant));
                    if (tests.containsValue(ASSERTION_ERROR)) {
                        line.add("FAIL");
                    } else if (tests.containsValue(TIMEOUT)) {
                        line.add("TIME");
                    } else if (tests.containsValue(GENERAL_EXCEPTION)) {
                        line.add("EXC");
                    } else {
                        line.add("LIVE");
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
