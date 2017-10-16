package output;

import java.util.LinkedList;

class TestRow<E> {

    private String testName;
    private LinkedList<E> list;

    TestRow(String testName) {
        this.testName = testName;
        list = new LinkedList<>();
    }

    void addMutation(E mutation) {
        list.addLast(mutation);
    }

    E getNextMutation() {
        return list.poll();
    }

    boolean hasItems() {
        return !list.isEmpty();
    }

    int size() {
        return list.size();
    }

    String getTestName() {
        return testName;
    }
}
