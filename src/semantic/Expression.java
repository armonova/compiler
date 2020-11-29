package semantic;

import lexer.Token;

import java.util.ArrayList;

public class Expression {

    private final Type type;
    private final ArrayList<Token> variableList;

    public Expression() {
        this.type = Type.VOID;
        this.variableList = new ArrayList<>();
    }

    public Expression(Type type) {
        this.type = type;
        this.variableList = new ArrayList<>();
    }

    public Expression(ArrayList<Token> tokenList, Token newToken) {
        this.type = Type.VOID;
        this.variableList = new ArrayList<>();
        this.variableList.addAll(tokenList);
        this.variableList.add(newToken);
    }

    public Type getType() {
        return type;
    }

    public ArrayList<Token> getVariableList() {
        return variableList;
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
