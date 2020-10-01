package lexer;

public class CharConst extends Token {

    public final char value;

    public CharConst(char value) {
        super(Tag.CHAR_CONST);
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + tag + ", '" + value + "'>";
    }

}
