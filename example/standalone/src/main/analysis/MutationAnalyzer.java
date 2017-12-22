package analysis;

import output.DefaultKillMatrix;

public interface MutationAnalyzer {

    DefaultKillMatrix runFullAnalysis();
    DefaultKillMatrix runSparseAnalysis();
}
