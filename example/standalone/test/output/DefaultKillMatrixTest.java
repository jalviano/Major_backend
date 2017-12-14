package output;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Outcome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import static utils.Outcome.*;

public class DefaultKillMatrixTest {

    private static DefaultKillMatrix matrix;

    @BeforeClass
    public static void setup() {
        matrix = new DefaultKillMatrix(4);
        matrix.addKillResult("test1", UNKILLED);
        matrix.addKillResult("test1", TIMEOUT);
        matrix.addKillResult("test1", NOT_COVERED);
        matrix.addKillResult("test1", NOT_COVERED);
        matrix.addKillResult("test2", GENERAL_EXCEPTION);
        matrix.addKillResult("test2", ASSERTION_ERROR);
        matrix.addKillResult("test2", NOT_COVERED);
        matrix.addKillResult("test2", ASSERTION_ERROR);
        matrix.addKillResult("test3", NOT_COVERED);
        matrix.addKillResult("test3", UNKILLED);
        matrix.addKillResult("test3", NOT_COVERED);
        matrix.addKillResult("test3", NOT_TESTED);
    }

    @Test
    public void mutationScoreTest() {
        float mutationScore = 0.75f;
        matrix.setMutationScore(mutationScore);
        Assert.assertEquals(mutationScore, matrix.getMutationScore(), 0);
    }

    @Test
    public void killResultTest() {
        Assert.assertEquals(UNKILLED, matrix.getKillResult("test1", 1));
        Assert.assertEquals(TIMEOUT, matrix.getKillResult("test1", 2));
        Assert.assertEquals(NOT_COVERED, matrix.getKillResult("test1", 3));
        Assert.assertEquals(NOT_COVERED, matrix.getKillResult("test1", 4));
        Assert.assertEquals(GENERAL_EXCEPTION, matrix.getKillResult("test2", 1));
        Assert.assertEquals(ASSERTION_ERROR, matrix.getKillResult("test2", 2));
        Assert.assertEquals(NOT_COVERED, matrix.getKillResult("test2", 3));
        Assert.assertEquals(ASSERTION_ERROR, matrix.getKillResult("test2", 4));
        Assert.assertEquals(NOT_COVERED, matrix.getKillResult("test3", 1));
        Assert.assertEquals(UNKILLED, matrix.getKillResult("test3", 2));
        Assert.assertEquals(NOT_COVERED, matrix.getKillResult("test3", 3));
        Assert.assertEquals(NOT_TESTED, matrix.getKillResult("test3", 4));
    }

    @Test
    public void getMutantIdsTest() {
        int[] mutants = {1, 2, 3, 4};
        Assert.assertArrayEquals(mutants, matrix.getMutantIds());
    }

    @Test
    public void getTestNamesTest() {
        Set<String> tests = matrix.getTestNames();
        Assert.assertTrue(tests.size() == 3);
        Assert.assertTrue(tests.contains("test1"));
        Assert.assertTrue(tests.contains("test2"));
        Assert.assertTrue(tests.contains("test3"));
    }

    @Test
    public void getMutantNumberTest() {
        Assert.assertEquals(4, matrix.getMutantNumber());
    }

    @Test
    public void getTestsForMutantTest1() {
        HashMap<String, Outcome> mutant1Tests = new HashMap<>();
        mutant1Tests.put("test1", UNKILLED);
        mutant1Tests.put("test2", GENERAL_EXCEPTION);
        Assert.assertEquals(mutant1Tests, matrix.getTestsForMutant(1));
    }

    @Test
    public void getTestsForMutantTest2() {
        Assert.assertTrue(matrix.getTestsForMutant(3).size() == 0);
    }

    @Test
    public void getMutantsForTestTest() {
        ArrayList<Outcome> test1 = new ArrayList<>(Arrays.asList(UNKILLED, TIMEOUT, NOT_COVERED, NOT_COVERED));
        Assert.assertEquals(test1, matrix.getMutantsForTest("test1"));
    }

    @Test
    public void getMutantsForTestSparseTest() {
        HashMap<Integer, Outcome> test1 = matrix.getMutantsForTestSparse("test1");
        Assert.assertTrue(test1.size() == 2);
        Assert.assertEquals(UNKILLED, test1.get(1));
        Assert.assertEquals(TIMEOUT, test1.get(2));
    }
}
