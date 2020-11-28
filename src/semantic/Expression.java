package semantic;

public class Expression {

    private final Type type;

    public Expression(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isBoolean() {
        return type != null && type.isBoolean();
    }

    public boolean isBooleanOp() {
        return type != null && type.isBooleanOp();
    }

    public boolean isNumberOp() {
        return type != null && type.isNumberOp();
    }

    public boolean isVoid() {
        return type != null && type.isVoid();
    }

    public boolean isInt() {
        return type != null && type.isInt();
    }

    public boolean isFloat() {
        return type != null && type.isFloat();
    }

    public boolean isChar() {
        return type != null && type.isChar();
    }

    public boolean isNumber() {
        return type != null && type.isNumber();
    }
}
