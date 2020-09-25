package lexer;

public class Tag {
    // Palavras reservadas
    public final static int
        PROGRAM = 256,
        IS = 257,
        DECLARE = 258,
        INIT = 259,
        END = 260,
        INT = 261,
        FLOAT = 262,
        CHAR = 263,
        IF = 264,
        THEN = 265,
        ELSE = 266,
        REPEAT = 267,
        UNTIL = 268,
        WHILE = 269,
        DO = 270,
        IN = 271,
        OUT = 272,
        AND_AND = 271,
        OR_OR = 273,

    // outros tokens
        NUM = 274,
        ID = 275;
}