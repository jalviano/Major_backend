package analysis.runners;

import prepass.TestMethod;

import static utils.TestFinder.getTestFullName;
import static utils.TestFinder.parseTestFullName;

public class WorkOrder {

    public TestMethod test;
    public Integer mutantId;
    public Long timeout;

    public WorkOrder(TestMethod t, Integer m, Long time) {
        test = t;
        mutantId = m;
        timeout = time;
    }

    @Override
    public boolean equals(Object otherObj) {
        WorkOrder other = (WorkOrder)otherObj;
        return test.equals(other.test) && mutantId.equals(other.mutantId) && timeout.equals(other.timeout);
    }

    @Override
    public int hashCode() {
        return test.hashCode() + mutantId.hashCode() + timeout.hashCode();
    }

    @Override
    public String toString() {
        return (getTestFullName(test, "#") + "," + mutantId + "," + timeout);
    }

    public static WorkOrder fromString(String s) throws ClassNotFoundException, IllegalArgumentException,
            NoSuchMethodException {
        String[] method_mutantId_timeout = s.split(",", 3);
        if (!(method_mutantId_timeout.length == 3)) {
            throw new IllegalArgumentException(s);
        }
        TestMethod method = parseTestFullName(method_mutantId_timeout[0], "#");
        Integer mutantId = Integer.parseInt(method_mutantId_timeout[1]);
        Long timeout = Long.parseLong(method_mutantId_timeout[2]);
        return new WorkOrder(method, mutantId, timeout);
    }
}