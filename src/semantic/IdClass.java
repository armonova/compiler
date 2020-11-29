package semantic;

public enum IdClass {

    VARIABLE,
    PROGRAM;

    @Override
    public String toString() {
        return this.name();
    }

    public boolean isVariable() {
        return this == VARIABLE;
    }

}
