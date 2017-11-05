package prepass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface PrepassAnalyzer {

    HashMap<TestMethod, ArrayList<Integer>> runPrepass();
}
