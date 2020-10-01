package lexer;

public class Literal extends Token {

    public final String value;

    public Literal(String value) {
        super(Tag.LITERAL);
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + tag + ", \"" + value + "\">";
    }
}
