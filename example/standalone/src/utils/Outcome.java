package utils;

public enum Outcome {

    /**
     * Enum of possible outcomes when running JUnit tests with mutants enabled. If test passes, outcome is UNKILLED. If
     * test fails because of JUnit assertion error, outcome is ASSERTION_ERROR. If test fails because of other exception
     * when run, outcome is GENERAL_EXCEPTION. If test times out when run, outcome is TIMEOUT. For full kill matrices,
     * if test is not covered by mutant, outcome is NOT_COVERED. For sparse kill matrices, if mutant has already been
     * killed and disabled but would have been covered by test, outcome is NOT_TESTED.
     */

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
