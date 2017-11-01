package output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static utils.Constants.*;

public class CSVFormatter implements FormatterInterface {

    private Formatter formatter;

    public CSVFormatter(Formatter formatter) {
        this.formatter = formatter;
        buildCSVOutput();
        System.out.println("Mutation Score: " + getMutationScore());
    }

    public int[] getMutantIds() {
        return formatter.getMutantIds();
    }

    public Set<String> getTestNames() {
        return formatter.getTestNames();
    }

    public float getMutationScore() {
        return formatter.getMutationScore();
    }

    public Integer getKillResult(String test, int mutant) {
        return formatter.getKillResult(test, mutant);
    }

    public HashMap<String, Integer> getTestsForMutant(int mutant) {
        return formatter.getTestsForMutant(mutant);
    }

    public ArrayList<Integer> getMutantsForTest(String test) {
        return formatter.getMutantsForTest(test);
    }

    private void buildCSVOutput() {
        String csvFile = "/Users/Justin/Desktop/killMatrix.csv";
        try {
            FileWriter writer = new FileWriter(csvFile);
            List<String> header = new ArrayList<>();
            header.add("");
            for (int id : getMutantIds()) {
                header.add(Integer.toString(id));
            }
            CSVUtils.writeLine(writer, header);
            for (String test : getTestNames()) {
                List<String> line = new ArrayList<>();
                line.add(test);
                for (Integer result : getMutantsForTest(test)) {
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
    }
}
