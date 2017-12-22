package output;

import utils.Outcome;

public interface KillMatrixBuilder {

    void setMutationScore(float mutationScore);
    void addKillResult(String test, Outcome killResult);
}
