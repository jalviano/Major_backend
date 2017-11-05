package utils;

public enum Outcome {

    UNKILLED(0),
    ASSERTION_ERROR(1),
    GENERAL_EXCEPTION(2),
    TIMEOUT(3),
    NOT_COVERED(4),
    NOT_TESTED(5);

    private int id;

    Outcome(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
