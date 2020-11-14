package syntatic;

import jdk.javadoc.doclet.Taglet;
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
        } else {
            error();
        }
    }

    /* Verifica fim de arquivo */
    void eatEOF() throws Exception {
        if (token != null) { // O método lexer.scan retorna null para EOF e apenas para EOF
            error();
        }
    }

    /* Método que não faz nada, para representar o lambda e melhorar a legibilidade do código */
    void lambda() {
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

    private void declList() throws Exception {
        if (token.is(Tag.ID)) {
            decl();
            declTail();
        } else {
            error();
        }
    }

    private void declTail() throws Exception {
        if (token.is(Tag.SEMICOLON)) {
            eat(Tag.SEMICOLON);
            decl();
            declTail();
        } else if (token.is(Tag.BEGIN)) {
            lambda();
        } else {
            error();
        }
    }

    private void decl() throws Exception {
        if (token.is(Tag.ID)) {
            identLit();
            eat(Tag.COLON);
            type();
        } else {
            error();
        }
    }

    private void identLit() throws Exception {
        if (token.is(Tag.ID)) {
            eat(Tag.ID);
            identTail();
        } else {
            error();
        }
    }

    private void identTail() throws Exception {
        if (token.is(Tag.COMMA)) {
            eat(Tag.COMMA);
            eat(Tag.ID);
            identTail();
        } else if (token.is(Tag.COLON)) {
            lambda();
        } else {
            error();
        }
    }

    private void type() throws Exception {
        switch (token.tag) {
            case INT:
                eat(Tag.INT);
                break;
            case FLOAT:
                eat(Tag.FLOAT);
                break;
            case CHAR:
                eat(Tag.CHAR);
                break;
            default:
                error();
                break;
        }
    }

    private void stmtList() throws Exception {
        // TODO: implementar
        error();
    }

    private void stmtTail() throws Exception {
        // TODO: implementar
        error();
    }

    private void stmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void assignStmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void ifStmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void ifStmtEnd() throws Exception {
        // TODO: implementar
        error();
    }

    private void condition() throws Exception {
        // TODO: implementar
        error();
    }

    private void repeatStmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void stmtSuffix() throws Exception {
        // TODO: implementar
        error();
    }

    private void whileStmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void stmtPrefix() throws Exception {
        // TODO: implementar
        error();
    }

    private void readStmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void writeStmt() throws Exception {
        // TODO: implementar
        error();
    }

    private void writable() throws Exception {
        // TODO: implementar
        error();
    }

    private void expression() throws Exception {
        // TODO: implementar
        error();
    }

    private void expressionEnd() throws Exception {
        // TODO: implementar
        error();
    }

    private void simpleExpr() throws Exception {
        // TODO: implementar
        error();
    }

    private void simpleExprTail() throws Exception {
        // TODO: implementar
        error();
    }

    private void term() throws Exception {
        // TODO: implementar
        error();
    }

    private void termTail() throws Exception {
        // TODO: implementar
        error();
    }

    private void factorA() throws Exception {
        // TODO: implementar
        error();
    }

    private void factor() throws Exception {
        // TODO: implementar
        error();
    }

    private void relop() throws Exception {
        // TODO: implementar
        error();
    }

    private void addop() throws Exception {
        // TODO: implementar
        error();
    }

    private void mulop() throws Exception {
        // TODO: implementar
        error();
    }

    private void constant() throws Exception {
        // TODO: implementar
        error();
    }

}
