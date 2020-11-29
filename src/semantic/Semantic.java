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
        return new Expression();
    }

    /* Identificador não foi declarado */
    private void undeclaredIdentifier(String lexeme) throws Exception {
        throw new Exception("Erro: identificador não declarado encontrado na linha " + Core.line + ": " + lexeme);
    }

    /* Identificador já foi declarado */
    private void declaredIdentifier(String lexeme) throws Exception {
        throw new Exception("Erro: identificador que ja foi declarado encontrado na linha " + Core.line + ": " + lexeme);
    }

    /* Tipo inesperado */
    private Expression incorrectVariableTypeError(Type foundType, Type... expectedType) throws Exception {
        throw new Exception("Erro: tipo inesperado identificado na linha " + Core.line + ": " + foundType
                + ". Tipo(s) esperado(s): " + Arrays.toString(expectedType));
    }

    /* Classe inesperada */
    private void incorrectVariableTypeError(IdClass foundClass) throws Exception {
        throw new Exception("Erro: classe inesperada identificada na linha " + Core.line + ": " + foundClass
                + ". Classe esperada: " + IdClass.VARIABLE);
    }

    /* Verifica se uma variável já foi declarada corretamente */
    private Id assertDeclaredVariable(Token token) throws Exception {
        Id identifierInfo = Core.currentEnviroment.get(token);
        if (identifierInfo == null || !identifierInfo.declared()) {
            undeclaredIdentifier(((Word) token).getLexeme());
        } else if (!identifierInfo.getIdClass().isVariable()) {
            incorrectVariableTypeError(identifierInfo.getIdClass());
        }
        return identifierInfo;
    }

    /* Verifica que um identificador não foi previamente declarado */
    private Id assertUndeclaredVariable(Token token) throws Exception {
        Id identifierInfo = Core.currentEnviroment.get(token);
        if (identifierInfo == null) {
            undeclaredIdentifier(((Word) token).getLexeme());
        } else if (identifierInfo.declared()) {
            declaredIdentifier(((Word) token).getLexeme());
        }
        return identifierInfo;
    }

    /* declara um identificador como variável */
    private void declareVariable(Token token) throws Exception {
        Id id = assertUndeclaredVariable(token);
        id.setIdClass(IdClass.VARIABLE);
    }

    // Procedimentos semânticos para cada produção da linguagem

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
        Id identifierInfo = assertDeclaredVariable(identifier);
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
                if (factor.isBoolean() || factor.isInt()) {
                    return new Expression(Type.BOOLEAN);
                }
                return incorrectVariableTypeError(factor.getType(), Type.BOOLEAN, Type.INT);
            }
            case MINUS: {
                if (factor.getType().isNumber()) {
                    return new Expression(factor.getType());
                }
                return incorrectVariableTypeError(factor.getType(), Type.INT, Type.FLOAT);
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
                return incorrectVariableTypeError(termTail.getType(), Type.VOID, Type.BOOLEAN);
            }
            return incorrectVariableTypeError(factorA.getType(), Type.INT, Type.BOOLEAN);
        }

        if (mulop.isNumberOp()) {

            if (factorA.isFloat()) {
                if (termTail.isVoid() || termTail.isInt() || termTail.isFloat()) {
                    return new Expression(Type.FLOAT);
                }
                return incorrectVariableTypeError(termTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
            }

            if (factorA.isInt()) {
                if (termTail.isVoid() || termTail.isInt()) {
                    return new Expression(Type.INT);
                }
                if (termTail.isFloat()) {
                    return new Expression(Type.FLOAT);
                }
                return incorrectVariableTypeError(termTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
            }

            return incorrectVariableTypeError(factorA.getType(), Type.INT, Type.FLOAT);

        }

        return incorrectVariableTypeError(mulop.getType(), Type.BOOLEAN_OP, Type.NUMBER_OP);

    }

    public Expression lambda() {
        return new Expression();
    }

    public Expression term(Expression factorA, Expression termTail) throws Exception {

        if (factorA.isBoolean()) {
            if (termTail.isBoolean() || termTail.isVoid()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectVariableTypeError(termTail.getType(), Type.BOOLEAN, Type.VOID);
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
            return incorrectVariableTypeError(termTail.getType(), Type.INT, Type.BOOLEAN, Type.FLOAT);
        }

        if (factorA.isFloat()) {
            if (termTail.isVoid() || termTail.isInt() || termTail.isFloat()) {
                return new Expression(Type.FLOAT);
            }
            return incorrectVariableTypeError(termTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
        }

        if (factorA.isChar()) {
            if (termTail.isVoid()) {
                return new Expression(Type.CHAR);
            }
            return incorrectVariableTypeError(termTail.getType(), Type.VOID);
        }

        return incorrectVariableTypeError(factorA.getType(), Type.BOOLEAN, Type.INT, Type.FLOAT, Type.CHAR);

    }

    public Expression simpleExprTail(Expression addop, Expression term, Expression simpleExprTail) throws Exception {

        if (addop.isBooleanOp()) {
            if (term.isInt() || term.isBoolean()) {
                if (simpleExprTail.isVoid() || simpleExprTail.isBoolean()) {
                    return new Expression(Type.BOOLEAN);
                }
                return incorrectVariableTypeError(simpleExprTail.getType(), Type.VOID, Type.BOOLEAN);
            }
            return incorrectVariableTypeError(term.getType(), Type.INT, Type.BOOLEAN);
        }

        if (addop.isNumberOp()) {

            if (term.isFloat()) {
                if (simpleExprTail.isVoid() || simpleExprTail.isInt() || simpleExprTail.isFloat()) {
                    return new Expression(Type.FLOAT);
                }
                return incorrectVariableTypeError(simpleExprTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
            }

            if (term.isInt()) {
                if (simpleExprTail.isVoid() || simpleExprTail.isInt()) {
                    return new Expression(Type.INT);
                }
                if (simpleExprTail.isFloat()) {
                    return new Expression(Type.FLOAT);
                }
                return incorrectVariableTypeError(simpleExprTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
            }

            return incorrectVariableTypeError(term.getType(), Type.INT, Type.FLOAT);

        }

        return incorrectVariableTypeError(addop.getType(), Type.BOOLEAN_OP, Type.NUMBER_OP);

    }

    public Expression simpleExpr(Expression term, Expression simpleExprTail) throws Exception {

        if (term.isBoolean()) {
            if (simpleExprTail.isBoolean() || simpleExprTail.isVoid()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectVariableTypeError(simpleExprTail.getType(), Type.BOOLEAN, Type.VOID);
        }

        if (term.isInt()) {
            if (simpleExprTail.isVoid() || simpleExprTail.isInt()) {
                return new Expression(Type.INT);
            }
            if (simpleExprTail.isBoolean()) {
                return new Expression(Type.BOOLEAN);
            }
            if (simpleExprTail.isFloat()) {
                return new Expression(Type.FLOAT);
            }
            return incorrectVariableTypeError(simpleExprTail.getType(), Type.INT, Type.BOOLEAN, Type.FLOAT);
        }

        if (term.isFloat()) {
            if (simpleExprTail.isVoid() || simpleExprTail.isInt() || simpleExprTail.isFloat()) {
                return new Expression(Type.FLOAT);
            }
            return incorrectVariableTypeError(simpleExprTail.getType(), Type.VOID, Type.INT, Type.FLOAT);
        }

        if (term.isChar()) {
            if (simpleExprTail.isVoid()) {
                return new Expression(Type.CHAR);
            }
            return incorrectVariableTypeError(simpleExprTail.getType(), Type.VOID);
        }

        return incorrectVariableTypeError(term.getType(), Type.BOOLEAN, Type.INT, Type.FLOAT, Type.CHAR);

    }

    public Expression expressionEnd(Tag tag, Expression simpleExpr) throws Exception {
        switch (tag) {

            case EQ_EQ:
            case NOT_EQ: {
                return new Expression(simpleExpr.getType());
            }

            case GT:
            case GT_EQ:
            case LT:
            case LT_EQ: {
                if (simpleExpr.isNumber()) {
                    return new Expression(simpleExpr.getType());
                }
                return incorrectVariableTypeError(simpleExpr.getType(), Type.INT, Type.FLOAT);
            }

            default:
                return syntaticError();

        }
    }

    public Expression expression(Expression simpleExpr, Expression expressionEnd) throws Exception {

        if (simpleExpr.isInt()) {
            if (expressionEnd.isVoid()) {
                return new Expression(Type.INT);
            }
            if (expressionEnd.isInt() || expressionEnd.isFloat() || expressionEnd.isBoolean()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectVariableTypeError(expressionEnd.getType(), Type.VOID, Type.INT, Type.FLOAT, Type.BOOLEAN);
        }

        if (simpleExpr.isFloat()) {
            if (expressionEnd.isVoid()) {
                return new Expression(Type.FLOAT);
            }
            if (expressionEnd.isInt() || expressionEnd.isFloat()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectVariableTypeError(expressionEnd.getType(), Type.VOID, Type.INT, Type.FLOAT);
        }

        if (simpleExpr.isBoolean()) {
            if (expressionEnd.isVoid() || expressionEnd.isBoolean()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectVariableTypeError(expressionEnd.getType(), Type.VOID, Type.BOOLEAN);
        }

        if (simpleExpr.isChar()) {
            if (expressionEnd.isVoid()) {
                return new Expression(Type.CHAR);
            }
            if (expressionEnd.isChar()) {
                return new Expression(Type.BOOLEAN);
            }
            return incorrectVariableTypeError(expressionEnd.getType(), Type.VOID, Type.CHAR);
        }

        return incorrectVariableTypeError(simpleExpr.getType(), Type.INT, Type.FLOAT, Type.BOOLEAN, Type.CHAR);

    }

    public Expression writable(Expression simpleExpr) {
        return new Expression(simpleExpr.getType());
    }

    public Expression writable() {
        return new Expression(Type.LITERAL);
    }

    public Expression writeStmt() {
        return new Expression();
    }

    public Expression readStmt(Token identifier) throws Exception {
        assertDeclaredVariable(identifier);
        return new Expression();
    }

    public Expression stmtPrefix() {
        return new Expression();
    }

    public Expression whileStmt() {
        return new Expression();
    }

    public Expression stmtSuffix() {
        return new Expression();
    }

    public Expression repeatStmt() {
        return new Expression();
    }

    public Expression condition(Expression expression) throws Exception {
        if (expression.isBoolean() || expression.isInt()) {
            return new Expression(Type.BOOLEAN);
        }
        return incorrectVariableTypeError(expression.getType(), Type.BOOLEAN, Type.INT);
    }

    public Expression ifStmtEnd() {
        return new Expression();
    }

    public Expression ifStmt() {
        return new Expression();
    }

    public Expression assignStmt(Token identifier, Expression simpleExpr) throws Exception {
        Id identifierInfo = assertDeclaredVariable(identifier);
        if (identifierInfo.getType().isFloat()) {
            if (simpleExpr.isInt() || simpleExpr.isFloat()) {
                return new Expression();
            }
            return incorrectVariableTypeError(simpleExpr.getType(), Type.FLOAT, Type.INT);
        }
        if (identifierInfo.getType().equals(simpleExpr.getType())) {
            return new Expression();
        }
        return incorrectVariableTypeError(simpleExpr.getType(), identifierInfo.getType());
    }

    public Expression stmt() {
        return new Expression();
    }

    public Expression stmtTail() {
        return new Expression();
    }

    public Expression stmtList() {
        return new Expression();
    }

    public Expression type(Tag tag) {
        switch (tag) {
            case INT:
                return new Expression(Type.INT);
            case FLOAT:
                return new Expression(Type.FLOAT);
            case CHAR:
                return new Expression(Type.CHAR);
            default:
                return syntaticError();
        }
    }

    public Expression identTail(Token identifier, Expression identTail) throws Exception {
        declareVariable(identifier);
        return new Expression(identTail.getVariableList(), identifier);
    }
}
