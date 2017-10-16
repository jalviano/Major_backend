package output;

import utils.Constants;

public class KillCell {

    private int mutationId;
    private int killResult;

    public KillCell(int mutationId, int killResult) {
        this.mutationId = mutationId;
        this.killResult = killResult;
    }

    int getMutationId() {
        return mutationId;
    }

    String getKillResult() {
        switch (killResult) {
            case Constants.UNKILLED:
                return "Unkilled";
            case Constants.KILLED:
                return "Killed";
            case Constants.TIMEDOUT:
                return "Timed out";
            default:
                return "Unkilled";
        }
    }
}
