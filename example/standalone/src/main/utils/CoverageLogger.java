package utils;

import prepass.TestMethod;

import java.util.ArrayList;
import java.util.HashMap;

public class CoverageLogger {

    public static void printCoverage(HashMap<TestMethod, ArrayList<Integer>> coverage) {
        System.out.println("Covered mutants: ");
        for (TestMethod key : coverage.keySet()) {
            System.out.println(key.getLongName() + ": " + coverage.get(key).toString());
        }
    }
}
