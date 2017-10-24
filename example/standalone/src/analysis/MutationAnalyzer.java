package analysis;

import major.mutation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import output.KillCell;
import output.KillMatrix;
import prepass.TestMethod;
import utils.Constants;

public class MutationAnalyzer {

    private HashMap<TestMethod, ArrayList<Integer>> coverage;
    private Class<?> testClass;

    public MutationAnalyzer(List<Class<?>> testClasses, HashMap<TestMethod, ArrayList<Integer>> coverage) {
        this.coverage = coverage;
        testClass = testClasses.get(1);
    }

    public int runAnalysis() {
        int killedCount = 0;
        KillMatrix matrix = new KillMatrix();
        for (TestMethod test : coverage.keySet()) {
            matrix.newTestRow(test.getName());
            for (Integer mutation : coverage.get(test)) {
                Config.__M_NO = mutation;
                Request request = Request.method(testClass, test.getName());
                Result result = new JUnitCore().run(request);
                if (result.getFailureCount() != 0) {
                    matrix.addKillCell(new KillCell(mutation, Constants.KILLED));
                    killedCount++;
                } else {
                    matrix.addKillCell(new KillCell(mutation, Constants.UNKILLED));
                }
            }
            matrix.addTestRow();
        }
        matrix.printOutput();
        return killedCount;
    }
}
