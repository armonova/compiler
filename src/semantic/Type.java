package semantic;

public enum Type {

    INT,
    FLOAT,
    BOOLEAN,
    NUMBER_OP,
    BOOLEAN_OP,
    CHAR,
    STRING,
    ARRAY,
    VOID;

    @Override
    public String toString() {
        return this.name();
    }

}
