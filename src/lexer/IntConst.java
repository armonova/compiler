package lexer;

public class IntConst extends Token {

    public final int value;

    public IntConst(int value) {
        super(Tag.INT_CONST);
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + tag + ", " + value + ">";
    }

}