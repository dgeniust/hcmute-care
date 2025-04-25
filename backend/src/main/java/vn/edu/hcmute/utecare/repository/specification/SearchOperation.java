package vn.edu.hcmute.utecare.repository.specification;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN;

    public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<" };

    public static SearchOperation getSimpleOperation(final char input) {
        return switch (input) {
            case ':' -> EQUALITY;
            case '!' -> NEGATION;
            case '>' -> GREATER_THAN;
            case '<' -> LESS_THAN;
            default -> null;
        };
    }
}