package analysis.runners;

import org.junit.runner.Result;

import java.util.Collection;
import java.util.ArrayList;

public class Outcome {

    public enum Type {
        PASS, FAIL, TIMEOUT, CRASH;
    }

    public Type type;
    public String digest;
    public Long runTime;
    public String stackTrace;
    public Collection<Integer> coveredMutants;

    private static String normalizeStackTrace(String stackTrace) {
        return stackTrace.replaceAll(" *\r?\n[ \t]*", " ")
                .replaceAll("^[ \t\r\n]*|[ \t\r\n]*$", "");
    }

    Outcome(Result result, String digest_) {
        digest = digest_;
        coveredMutants = new ArrayList<>();
        if (result.wasSuccessful()) {
            type = Type.PASS;
            runTime = result.getRunTime();
            stackTrace = "";
        } else {
            type = Type.FAIL;
            runTime = result.getRunTime();
            stackTrace = normalizeStackTrace(result.getFailures().get(0).getTrace());
        }
    }

    private Outcome(Type type_) {
        type = type_;
        digest = "";
        runTime = (long)-1;
        stackTrace = "";
        coveredMutants = new ArrayList<>();
    }

    static Outcome createTimeout(WorkOrder workOrder) {
        Outcome result = new Outcome(Type.TIMEOUT);
        result.runTime = workOrder.timeout;
        return result;
    }

    public static Outcome createCrash() {
        return new Outcome(Type.CRASH);
    }

    @Override
    public boolean equals(Object otherObj) {
        Outcome other = (Outcome)otherObj;
        return type == other.type && digest.equals(other.digest) && runTime.equals(other.runTime) && stackTrace.equals(
                other.stackTrace) && coveredMutants.equals(other.coveredMutants);
    }

    @Override
    public int hashCode() {
        return type.hashCode() + digest.hashCode() + runTime.hashCode() + stackTrace.hashCode() + coveredMutants.hashCode();
    }

    @Override
    public String toString() {
        String mutants = "";
        for (Integer m : coveredMutants) {
            mutants = mutants + m + " ";
        }
        if (mutants.length() > 0) mutants = mutants.substring(0, mutants.length()-1);
        return type.toString() + "," + runTime.toString() + "," + digest.toString() + "," + mutants + "," +
                stackTrace.toString();
    }

    static Outcome fromString(String s) throws IllegalArgumentException {
        String[] type_runTime_digest_mutants_stackTrace = s.split(",", 5);
        if (!(type_runTime_digest_mutants_stackTrace.length == 5)) {
            throw new IllegalArgumentException(s);
        }
        Type type = Type.valueOf(type_runTime_digest_mutants_stackTrace[0]);
        Long runTime = Long.parseLong(type_runTime_digest_mutants_stackTrace[1]);
        String digest = type_runTime_digest_mutants_stackTrace[2];
        String[] mutantStrs = type_runTime_digest_mutants_stackTrace[3].split(" ");
        String stackTrace = type_runTime_digest_mutants_stackTrace[4];
        ArrayList<Integer> coveredMutants = new ArrayList<>();
        for (String mutantStr : mutantStrs) {
            if (mutantStr.matches("[0-9]+")) {
                coveredMutants.add(Integer.parseInt(mutantStr));
            }
        }
        Outcome result = new Outcome(type);
        result.runTime = runTime;
        result.digest = digest;
        result.stackTrace = stackTrace;
        result.coveredMutants = coveredMutants;
        return result;
    }
}