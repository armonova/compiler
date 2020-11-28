package semantic;

import lexer.Tag;
import lexer.Token;
import lexer.Word;
import symbols.Id;
import syntatic.Core;

import java.util.Arrays;

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

    /* Tipo inesperado */
    private Expression incorrectTypeError(Type foundType, Type... expectedType) throws Exception {
        throw new Exception("Erro: tipo inesperado identificado na linha " + Core.line + ": " + foundType
                + ". Tipo(s) esperado(s): " + Arrays.toString(expectedType));
    }

    public Expression constant(Tag tag) {
        switch (tag) {
            case INT_CONST:
                return new Expression(Type.INT);
            case FLOAT_CONST:
                return new Expression(Type.FLOAT);
            case CHAR_CONST:
                return new Expression(Type.CHAR);
            default:
                return syntaticError();
        }
    }

    public Expression mulop(Tag tag) {
        switch (tag) {
            case TIMES:
            case DIVIDED:
                return new Expression(Type.NUMBER_OP);
            case AND_AND:
                return new Expression(Type.BOOLEAN_OP);
            default:
                return syntaticError();
        }
    }

    public Expression addop(Tag tag) {
        switch (tag) {
            case PLUS:
            case MINUS:
                return new Expression(Type.NUMBER_OP);
            case OR_OR:
                return new Expression(Type.BOOLEAN_OP);
            default:
                return syntaticError();
        }
    }

    public Expression relop() {
        return new Expression(Type.BOOLEAN_OP);
    }

    public Expression factor(Token identifier) throws Exception {
        Id identifierInfo = Core.currentEnviroment.get(identifier);
        if (!(identifier instanceof Word)) {
            return syntaticError();
        }
        if (identifierInfo == null || identifierInfo.typeIsNull()) {
            return undeclaredIdentifier(((Word) identifier).getLexeme());
        }
        return new Expression(identifierInfo.getType());
    }

    public Expression factor(Expression expression) {
        return new Expression(expression.getType());
    }

    public Expression factorA(Expression factor) {
        return new Expression(factor.getType());
    }

    public Expression factorA(Expression factor, Tag tag) throws Exception {
        switch (tag) {
            case NOT: {
                if (factor.isBoolean()) {
                    return new Expression(Type.BOOLEAN);
                }
                return incorrectTypeError(factor.getType(), Type.BOOLEAN);
            }
            case MINUS: {
                if (factor.getType().isNumber()) {
                    return new Expression(factor.getType());
                }
                return incorrectTypeError(factor.getType(), Type.INT, Type.FLOAT);
            }
            default:
                return syntaticError();
        }
    }

    public Expression termTail(Expression mulop, Expression factorA, Expression termTail) throws Exception {

        if (mulop.isBooleanOp()) {
            if (factorA.isInt() || factorA.isBoolean()) {
                if (termTail.isVoid() || termTail.isBoolean()) {
                    return new Expression(Type.BOOLEAN);
                }
                return incorrectTypeError(termTail.getType(), Type.VOID, Type.BOOLEAN);
            }
            return incorrectTypeError(factorA.getType(), Type.INT, Type.BOOLEAN);
        }

        if (mulop.isNumberOp()) {

            if (factorA.isFloat()) {
                if (termTail.isVoid() || termTail.isInt() || termTail.isFloat()) {
                    return new Expression(Type.FLOAT);
                }
                return incorrectTypeError(termTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
            }

            if (factorA.isInt()) {
                if (termTail.isVoid() || termTail.isInt()) {
                    return new Expression(Type.INT);
                }
                if (termTail.isFloat()) {
                    return new Expression(Type.FLOAT);
                }
                return incorrectTypeError(termTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
            }

            return incorrectTypeError(factorA.getType(), Type.INT, Type.FLOAT);

        }

        return incorrectTypeError(mulop.getType(), Type.BOOLEAN_OP, Type.NUMBER_OP);

    }

    public Expression lambda() {
        return new Expression(Type.VOID);
    }

    public Expression term(Expression factorA, Expression termTail) throws Exception {

        if (factorA.isBoolean()) {
            if (termTail.isBoolean() || termTail.isVoid()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectTypeError(termTail.getType(), Type.BOOLEAN, Type.VOID);
        }

        if (factorA.isInt()) {
            if (termTail.isVoid() || termTail.isInt()) {
                return new Expression(Type.INT);
            }
            if (termTail.isBoolean()) {
                return new Expression(Type.BOOLEAN);
            }
            if (termTail.isFloat()) {
                return new Expression(Type.FLOAT);
            }
            return incorrectTypeError(termTail.getType(), Type.INT, Type.BOOLEAN, Type.FLOAT);
        }

        if (factorA.isFloat()) {
            if (termTail.isVoid() || termTail.isInt() || termTail.isFloat()) {
                return new Expression(Type.FLOAT);
            }
            return incorrectTypeError(termTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
        }

        if (factorA.isChar()) {
            if (termTail.isVoid()) {
                return new Expression(Type.CHAR);
            }
            return incorrectTypeError(termTail.getType(), Type.VOID);
        }

        return incorrectTypeError(factorA.getType(), Type.BOOLEAN, Type.INT, Type.FLOAT, Type.CHAR);

    }

}
