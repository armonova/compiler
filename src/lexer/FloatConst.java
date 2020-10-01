package lexer;

public class FloatConst extends Token {

    public final float value;

    public FloatConst(float value) {
        super(Tag.FLOAT_CONST);
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + tag + ", " + value + ">";
    }

}
