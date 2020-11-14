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
        if (token.is(Tag.ID) ||
            token.is(Tag.INT_CONST) ||
            token.is(Tag.FLOAT_CONST) ||
            token.is(Tag.CHAR_CONST) ||
            token.is(Tag.OPEN_PAR) ||
            token.is(Tag.NOT) ||
            token.is(Tag.MINUS)) {
            factorA();
            termTail();
        } else {
            error();
        }
    }

    private void term() throws Exception {
        if (token.is(Tag.TIMES) || token.is(Tag.DIVIDED) || token.is(Tag.AND_AND)) {
            mulop();
            factorA();
            termTail();
        } else if (token.is(Tag.SEMICOLON) ||
            token.is(Tag.END) ||
            token.is(Tag.ELSE) ||
            token.is(Tag.UNTIL) ||
            token.is(Tag.MINUS) ||
            token.is(Tag.EQ_EQ) ||
            token.is(Tag.GT) ||
            token.is(Tag.GT_EQ) ||
            token.is(Tag.LT) ||
            token.is(Tag.LT_EQ) ||
            token.is(Tag.NOT_EQ) ||
            token.is(Tag.THEN) ||
            token.is(Tag.DO) ||
            token.is(Tag.PLUS) ||
            token.is(Tag.OR_OR)) {
            lambda();
        } else {
            error();
        }
    }

    private void termTail() throws Exception {
        // TODO: implementar
        error();
    }

    private void factorA() throws Exception {
        if (token.is(Tag.ID) || token.is(Tag.INT_CONST) || token.is(Tag.FLOAT_CONST) || token.is(Tag.CHAR_CONST) || token.is(Tag.OPEN_PAR)) {
            factor();
        } else if (token.is(Tag.NOT)) {
            eat(Tag.NOT);
            factor();
        } else if (token.is(Tag.MINUS)) {
            eat(Tag.MINUS);
            factor();
        } else {
            error();
        }
    }

    private void factor() throws Exception {
        if (token.is(Tag.ID)) {
            eat(Tag.ID);
        } else if (token.is(Tag.INT_CONST) || token.is(Tag.FLOAT_CONST) || token.is(Tag.CHAR_CONST)) {
            constant();
        } else if (token.is(Tag.OPEN_PAR)) {
            eat(Tag.OPEN_PAR);
            expression();
            eat(Tag.CLOSE_PAR);
        } else {
            error();
        }
    }

    private void relop() throws Exception {
        switch (token.tag) {
            case EQ_EQ:
                eat(Tag.EQ_EQ);
                break;
            case GT:
                eat(Tag.GT);
                break;
            case GT_EQ:
                eat(Tag.GT_EQ);
                break;
            case LT:
                eat(Tag.LT);
                break;
            case LT_EQ:
                eat(Tag.LT_EQ);
                break;
            case NOT_EQ:
                eat(Tag.NOT_EQ);
                break;
            default:
                error();
                break;
        }
    }

    private void addop() throws Exception {
        if (token.is(Tag.PLUS)) {
            eat(Tag.PLUS);
        } else if (token.is(Tag.MINUS)) {
            eat(Tag.MINUS);
        } else if(token.is(Tag.OR_OR)) {
            eat(Tag.OR_OR);
        } else {
            error();
        }
    }

    private void mulop() throws Exception {
        if (token.is(Tag.TIMES)) {
            eat(Tag.TIMES);
        } else if (token.is(Tag.DIVIDED)){
            eat(Tag.DIVIDED);
        } else if (token.is(Tag.AND_AND)) {
            eat(Tag.AND_AND);
        } else {
            error();
        }
    }

    private void constant() throws Exception {
        if (token.is(Tag.INT_CONST)) {
            eat(Tag.INT_CONST);
        } else if (token.is(Tag.FLOAT_CONST)) {
            eat(Tag.FLOAT_CONST);
        } else if (token.is(Tag.CHAR_CONST)) {
            eat(Tag.CHAR_CONST);
        } else {
            error();
        }
    }

}
