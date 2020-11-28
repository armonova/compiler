package semantic;

public enum Type {

    INT,
    FLOAT,
    BOOLEAN,
    CHAR,
    NUMBER_OP,
    BOOLEAN_OP,
    LITERAL,
    VOID;

    @Override
    public String toString() {
        return this.name();
    }

    public boolean isNumber() {
        return this == INT || this == FLOAT;
    }

    public boolean isBoolean() {
        return this == BOOLEAN;
    }

    public boolean isBooleanOp() {
        return this == BOOLEAN_OP;
    }

    public boolean isNumberOp() {
        return this == NUMBER_OP;
    }

    public boolean isVoid() {
        return this == VOID;
    }

    public boolean isInt() {
        return this == INT;
    }

    public boolean isFloat() {
        return this == FLOAT;
    }

    public boolean isChar() {
        return this == CHAR;
    }
}
