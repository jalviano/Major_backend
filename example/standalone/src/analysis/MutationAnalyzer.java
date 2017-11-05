package analysis;

import output.DefaultKillMatrix;

public interface MutationAnalyzer {

    DefaultKillMatrix runCompleteAnalysis();
    DefaultKillMatrix runSparseAnalysis();
}
