package semantic;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import symbols.Id;
import syntatic.Core;

public class Semantic {

    /* Nesta classe não reportamos o erro pois erros sintáticos são reportados pelo analisador sintático */
    private Expression syntaticError() {
        System.out.println("Erro sintático detectado pelo analisador semântico");
        return new Expression(Type.VOID);
    }

    /* Identificador não foi declarado */
    private Expression undeclaredIdentifier(String lexeme) throws Exception {
        throw new Exception("Erro: identificador não declarado encontrado na linha " + Core.line + ": " + lexeme);
    }

    public Expression constant(Tag tag) {
        switch (tag) {
            case INT_CONST:
                return new Expression(Type.INT);
            case FLOAT_CONST:
                return new Expression(Type.FLOAT);
            case CHAR_CONST:
                return new Expression(Type.CHAR);
        }
        return syntaticError();
    }

    public Expression mulop(Tag tag) {
        switch (tag) {
            case TIMES:
            case DIVIDED:
                return new Expression(Type.NUMBER_OP);
            case AND_AND:
                return new Expression(Type.BOOLEAN_OP);
        }
        return syntaticError();
    }

    public Expression addop(Tag tag) {
        switch (tag) {
            case PLUS:
            case MINUS:
                return new Expression(Type.NUMBER_OP);
            case OR_OR:
                return new Expression(Type.BOOLEAN_OP);
        }
        return syntaticError();
    }

    public Expression relop() {
        return new Expression(Type.BOOLEAN_OP);
    }

    public Expression factor(Token identifier) throws Exception {
        Id identifierInfo = Core.currentEnviroment.get(identifier);
        if (!(identifier instanceof Word)) {
            return syntaticError();
        }
        if (identifierInfo == null || identifierInfo.getType() == null) {
            return undeclaredIdentifier(((Word) identifier).getLexeme());
        }
        return new Expression(identifierInfo.getType());
    }

    public Expression factor(Expression expression) {
        return new Expression(expression.getType());
    }
}
