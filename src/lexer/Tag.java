package lexer;

public enum Tag {
    // Palavras reservadas
    PROGRAM, IS, DECLARE, BEGIN, END,
    INT, FLOAT, CHAR,
    IF, THEN, ELSE,
    REPEAT, UNTIL, WHILE, DO,
    IN, OUT,

    // Operadores
    AND_AND, OR_OR, NOT,
    EQ,
    EQ_EQ, GT, GT_EQ, LT, LT_EQ, NOT_EQ,
    LT_LT, GT_GT,
    MINUS, PLUS, TIMES, DIVIDED,

    // Pontuação
    SEMICOLON, COLON, COMMA, DOT,
    OPEN_PAR, CLOSE_PAR,

    // outros tokens
    INT_CONST, FLOAT_CONST,
    CHAR_CONST,
    LITERAL,
    ID;

    @Override
    public String toString() {
        return this.name();
    }

}