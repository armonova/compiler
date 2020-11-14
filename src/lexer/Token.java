package lexer;

public class Token {

    public final Tag tag; //constante que representa o token

    public Token(Tag t) {
        tag = t;
    }

    public String toString() {
        return "<" + tag + ">";
    }

    // Tokens simples
    public final static Token AND_AND = new Token(Tag.AND_AND);
    public final static Token OR_OR = new Token(Tag.OR_OR);
    public final static Token NOT = new Token(Tag.NOT);
    public final static Token EQ = new Token(Tag.EQ);
    public final static Token EQ_EQ = new Token(Tag.EQ_EQ);
    public final static Token GT = new Token(Tag.GT);
    public final static Token GT_EQ = new Token(Tag.GT_EQ);
    public final static Token LT = new Token(Tag.LT);
    public final static Token LT_EQ = new Token(Tag.LT_EQ);
    public final static Token NOT_EQ = new Token(Tag.NOT_EQ);
    public final static Token LT_LT = new Token(Tag.LT_LT);
    public final static Token GT_GT = new Token(Tag.GT_GT);
    public final static Token MINUS = new Token(Tag.MINUS);
    public final static Token PLUS = new Token(Tag.PLUS);
    public final static Token TIMES = new Token(Tag.TIMES);
    public final static Token DIVIDED = new Token(Tag.DIVIDED);
    public final static Token SEMICOLON = new Token(Tag.SEMICOLON);
    public final static Token COLON = new Token(Tag.COLON);
    public final static Token COMMA = new Token(Tag.COMMA);
    public final static Token DOT = new Token(Tag.DOT);
    public final static Token OPEN_PAR = new Token(Tag.OPEN_PAR);
    public final static Token CLOSE_PAR = new Token(Tag.CLOSE_PAR);

    public boolean is(Tag tag) {
        return this.tag == tag;
    }
}
