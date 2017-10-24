package output;

import static utils.Constants.*;

public class KillCell {

    private String testName;
    private int mutationId;
    private int killResult;

    public KillCell(int mutationId, int killResult) {
        this.mutationId = mutationId;
        this.killResult = killResult;
    }

    public KillCell(String testName, int killResult) {
        this.testName = testName;
        this.killResult = killResult;
    }

    int getMutationId() {
        return mutationId;
    }

    String getTestName() {
        return  testName;
    }

    String getKillResult() {
        switch (killResult) {
            case UNKILLED:
                return "Unkilled";
            case KILLED:
                return "Killed";
            case TIMEDOUT:
                return "Timed out";
            default:
                return "Unkilled";
        }
    }
}
