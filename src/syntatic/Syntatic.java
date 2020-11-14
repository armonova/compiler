package syntatic;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

public class Syntatic {

    private Token token;

    private final Lexer lexer;

    public Syntatic(Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.token = lexer.scan(); // Lê o primeiro token
    }

    /*  Lê próximo token */
    void advance() throws Exception {
        token = lexer.scan();
    }

    /* Consome token */
    void eat(Tag tag) throws Exception {
        if (token.is(tag)) {
            advance();
            return;
        }
        throw unknownTokenException();
    }

    /* Verifica fim de arquivo */
    void eatEOF() throws Exception {
        if (token == null) {
            return;
        }
        throw unknownTokenException();
    }

    /* Realiza a análise sintática */
    public void analyze() throws Exception {
        programInitial();
        System.out.println("\nAnálise léxica e sintática concluída com sucesso.\n");
    }

    /* Erro lançado quando encontramos um token inesperado */
    private Exception unknownTokenException() {
        return new Exception("Erro: token inesperado encontrado na linha " + lexer.getLine() + ": " + token.toString());
    }

    private void error() throws Exception {
        throw unknownTokenException();
    }

    // Procedimentos para os símbolos não terminais:

    /* Símbolo inicial da gramática */
    private void programInitial() throws Exception {
        if (token.is(Tag.PROGRAM)) {
            programNode();
            eatEOF();
        } else {
            error();
        }
    }

    private void programNode() throws Exception {
        if (token.is(Tag.PROGRAM)) {
            eat(Tag.PROGRAM);
            eat(Tag.ID);
            eat(Tag.IS);
            body();
        } else {
            error();
        }
    }

    private void body() throws Exception {
        if (token.is(Tag.DECLARE)) {
            eat(Tag.DECLARE);
            declList();
            eat(Tag.BEGIN);
            stmtList();
            eat(Tag.END);
            eat(Tag.DOT);
        } else if (token.is(Tag.BEGIN)) {
            eat(Tag.BEGIN);
            stmtList();
            eat(Tag.END);
            eat(Tag.DOT);
        } else {
            error();
        }
    }


}
