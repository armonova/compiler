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
        }
    }

    private void stmtList() throws Exception {
        if (token.is(Tag.ID)
                || token.is(Tag.IF)
                || token.is(Tag.WHILE)
                || token.is(Tag.REPEAT)
                || token.is(Tag.IN)
                || token.is(Tag.OUT)) {
            stmt();
            stmtTail();
        } else {
            error();
        }
    }

    private void stmtTail() throws Exception {
        if (token.is(Tag.SEMICOLON)) {
            eat(Tag.SEMICOLON);
            stmt();
            stmtTail();
        } else if (token.is(Tag.END)
                || token.is(Tag.ELSE)
                || token.is(Tag.UNTIL)) {
            lambda();
        } else {
            error();
        }
    }

    private void stmt() throws Exception {
        switch (token.tag) {
            case ID:
                assignStmt();
                break;
            case IF:
                ifStmt();
                break;
            case WHILE:
                whileStmt();
                break;
            case REPEAT:
                repeatStmt();
                break;
            case IN:
                readStmt();
                break;
            case OUT:
                writeStmt();
                break;
            default:
                error();
        }
    }

    private void assignStmt() throws Exception {
        if (token.is(Tag.ID)) {
            eat(Tag.ID);
            eat(Tag.EQ);
            simpleExpr();
        } else {
            error();
        }
    }

    private void ifStmt() throws Exception {
        if (token.is(Tag.IF)) {
            eat(Tag.IF);
            condition();
            eat(Tag.THEN);
            stmtList();
            ifStmtEnd();
        } else {
            error();
        }
    }

    private void ifStmtEnd() throws Exception {
        if (token.is(Tag.END)) {
            eat(Tag.END);
        } else if (token.is(Tag.ELSE)) {
            eat(Tag.ELSE);
            stmtList();
            eat(Tag.END);
        } else {
            error();
        }
    }

    private void condition() throws Exception {
        if (token.is(Tag.ID)
                || token.is(Tag.INT_CONST)
                || token.is(Tag.FLOAT_CONST)
                || token.is(Tag.CHAR_CONST)
                || token.is(Tag.OPEN_PAR)
                || token.is(Tag.NOT)
                || token.is(Tag.MINUS)) {
            expression();
        } else {
            error();
        }
    }

    private void repeatStmt() throws Exception {
        if (token.is(Tag.REPEAT)) {
            eat(Tag.REPEAT);
            stmtList();
            stmtSuffix();
        } else {
            error();
        }
    }

    private void stmtSuffix() throws Exception {
        if (token.is(Tag.UNTIL)) {
            eat(Tag.UNTIL);
            condition();
        } else {
            error();
        }
    }

    private void whileStmt() throws Exception {
        if (token.is(Tag.WHILE)) {
            stmtPrefix();
            stmtList();
            eat(Tag.END);
        } else {
            error();
        }
    }

    private void stmtPrefix() throws Exception {
        if (token.is(Tag.WHILE)) {
            eat(Tag.WHILE);
            condition();
            eat(Tag.DO);
        } else {
            error();
        }
    }

    private void readStmt() throws Exception {
        if (token.is(Tag.IS)) {
            eat(Tag.IN);
            eat(Tag.LT_LT);
            eat(Tag.ID);
        } else {
            error();
        }
    }

    private void writeStmt() throws Exception {
        if (token.is(Tag.OUT)) {
            eat(Tag.OUT);
            eat(Tag.GT_GT);
            writable();
        } else {
            error();
        }
    }

    private void writable() throws Exception {
        if (token.is(Tag.ID)
                || token.is(Tag.INT_CONST)
                || token.is(Tag.FLOAT_CONST)
                || token.is(Tag.CHAR_CONST)
                || token.is(Tag.OPEN_PAR)
                || token.is(Tag.NOT)
                || token.is(Tag.MINUS)) {
            simpleExpr();
        } else if (token.is(Tag.LITERAL)) {
            eat(Tag.LITERAL);
        } else {
            error();
        }
    }

    private void expression() throws Exception {
        if (token.is(Tag.ID)
                || token.is(Tag.INT_CONST)
                || token.is(Tag.FLOAT_CONST)
                || token.is(Tag.CHAR_CONST)
                || token.is(Tag.OPEN_PAR)
                || token.is(Tag.NOT)
                || token.is(Tag.MINUS)) {
            simpleExpr();
            expressionEnd();
        } else {
            error();
        }
    }

    private void expressionEnd() throws Exception {
        if (token.is(Tag.EQ_EQ)
                || token.is(Tag.GT)
                || token.is(Tag.GT_EQ)
                || token.is(Tag.LT)
                || token.is(Tag.LT_EQ)
                || token.is(Tag.NOT_EQ)) {
            relop();
            simpleExpr();
        } else if (token.is(Tag.SEMICOLON)
                || token.is(Tag.END)
                || token.is(Tag.ELSE)
                || token.is(Tag.UNTIL)
                || token.is(Tag.THEN)
                || token.is(Tag.DO)
                || token.is(Tag.CLOSE_PAR)) {
            lambda();
        } else {
            error();
        }
    }

    private void simpleExpr() throws Exception {
        if (token.is(Tag.ID)
                || token.is(Tag.INT_CONST)
                || token.is(Tag.FLOAT_CONST)
                || token.is(Tag.CHAR_CONST)
                || token.is(Tag.OPEN_PAR)
                || token.is(Tag.NOT)
                || token.is(Tag.MINUS)) {
            term();
            simpleExprTail();
        } else {
            error();
        }
    }

    private void simpleExprTail() throws Exception {
        if (token.is(Tag.MINUS)
                || token.is(Tag.PLUS)
                || token.is(Tag.OR_OR)) {
            addop();
            term();
            simpleExprTail();
        } else if (token.is(Tag.SEMICOLON)
                || token.is(Tag.END)
                || token.is(Tag.ELSE)
                || token.is(Tag.UNTIL)
                || token.is(Tag.EQ_EQ)
                || token.is(Tag.GT)
                || token.is(Tag.GT_EQ)
                || token.is(Tag.LT)
                || token.is(Tag.LT_EQ)
                || token.is(Tag.NOT_EQ)
                || token.is(Tag.THEN)
                || token.is(Tag.DO)) {
            lambda();
        } else {
            error();
        }
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
