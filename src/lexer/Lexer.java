package lexer;

import syntatic.Core;

import java.io.*;
import java.util.*;

public class Lexer {

    private char ch = ' '; //caractere lido do arquivo
    private final FileReader file;

    /* Esta é a tabela de tokens conhecidos pelo analisador léxico, ela não é a tabela de símbolos,
     * mas é utilizada para a implementação dela */
    private final Hashtable<String, Word> wordTable = new Hashtable<>();

    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word word) {
        wordTable.put(word.getLexeme(), word); // o lexema é a chave para entrada na tabela de tokens conhecidos
        Core.currentEnviroment.put(word); // o token é a chave para entrada na TS
    }

    /* Método construtor */
    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        // Inicializa contagem de linhas
        Core.line = 1;
        //Insere palavras reservadas na HashTable
        reserve(new Word("program", Tag.PROGRAM));
        reserve(new Word("is", Tag.IS));
        reserve(new Word("declare", Tag.DECLARE));
        reserve(new Word("begin", Tag.BEGIN));
        reserve(new Word("end", Tag.END));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("char", Tag.CHAR));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("repeat", Tag.REPEAT));
        reserve(new Word("until", Tag.UNTIL));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("in", Tag.IN));
        reserve(new Word("out", Tag.OUT));
    }

    /*Lê o próximo caractere do arquivo*/
    private void readch() throws IOException {
        ch = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c*/
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) return false;
        ch = ' ';
        return true;
    }

    public Token scan() throws Exception {

        // Desconsidera delimitadores na entrada
        discardDelimiters();

        // Verifica se existe um comentário
        if (ch == '/') {
            if (readch('*')) {
                analyzeComment(); // ignora o conteúdo do comentário
            } else {
                return Token.DIVIDED; // não é comentário, retorna a barra simples
            }
        }

        // Caracteres com regras próprias
        switch (ch) {
            case '&':
                return analyzeAnd();
            case '|':
                return analyzeOr();
            case '!':
                return analyzeNot();
            case '=':
                return analyzeEq();
            case '>':
                return analyzeGt();
            case '<':
                return analyzeLt();
            case '"':
                return analyzeLiteral();
            case '\'':
                return analyzeChar();
        }

        // Caracteres simples
        switch (ch) {
            case '-':
                return resetAndReturn(Token.MINUS);
            case '+':
                return resetAndReturn(Token.PLUS);
            case '*':
                return resetAndReturn(Token.TIMES);
            case ';':
                return resetAndReturn(Token.SEMICOLON);
            case ':':
                return resetAndReturn(Token.COLON);
            case ',':
                return resetAndReturn(Token.COMMA);
            case '.':
                return resetAndReturn(Token.DOT);
            case '(':
                return resetAndReturn(Token.OPEN_PAR);
            case ')':
                return resetAndReturn(Token.CLOSE_PAR);
        }

        // Números (inteiros ou decimais)
        if (Character.isDigit(ch)) {
            return analyzeNumber();
        }

        // Palavras (identificadores ou palavras reservadas)
        if (Character.isLetter(ch)) {
            return analyzeWord();
        }

        // Fim de arquivo retorna null
        if (!Character.isDefined(ch)) {
            int EOF = -1;
            if (file.read() == EOF) {
                return null;
            }
        }

        // Caracteres que não são conhecidos pela gramática geram um erro
        throw unknownCharException();

    }

    private CharConst analyzeChar() throws Exception {
        readch();
        if (Character.isDefined(ch)) {
            char charValue = ch;
            if (ch == '\n') { // a quebra de linha é um caractere da tabela ASCII
                Core.line++;
            }
            if (readch('\'')) {
                return new CharConst(charValue);
            }
        }
        throw unknownCharException();
    }

    private Literal analyzeLiteral() throws Exception {
        StringBuilder sb = new StringBuilder();
        while (Character.isDefined(ch) && ch != '\n') { // Não permite quebra de linha em uma string literal
            readch();
            if (ch == '"') {
                return (Literal) resetAndReturn(new Literal(sb.toString()));
            }
            sb.append(ch);
        }
        throw unknownCharException();
    }

    private void discardDelimiters() throws IOException {
        for (; ; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') continue;
            if (ch == '\n') Core.line++; // conta linhas
            else break;
        }
    }

    private void analyzeComment() throws Exception {
        while (Character.isDefined(ch)) {
            if (ch == '\n') {
                Core.line++;
            }
            if (ch == '*') {
                if (readch('/')) {
                    discardDelimiters(); // Após desconsiderar o comentário, precisa desconsiderar também delimitadores
                    return;
                }
                continue;
            }
            readch();
        }
        throw unknownCharException();
    }

    private Token analyzeNumber() throws Exception {
        StringBuilder sb = new StringBuilder(); // A string é convertida para número no final do método
        do {
            sb.append(ch);
            readch();
        } while (Character.isDigit(ch));
        if (ch == '.') { // Caso de float
            sb.append(ch);
            readch();
            if (!Character.isDigit(ch)) { // É obrigatório ter ao menos um caractere após o ponto
                throw unknownCharException();
            }
            do {
                sb.append(ch);
                readch();
            } while (Character.isDigit(ch));
            return new FloatConst(Float.parseFloat(sb.toString()));
        }
        return new IntConst(Integer.parseInt(sb.toString())); // Se não tem ponto, é inteiro
    }

    /* limpa o buffer quando necessário */
    private Token resetAndReturn(Token token) {
        ch = ' ';
        return token;
    }

    private Token analyzeAnd() throws Exception {
        if (readch('&')) {
            return Token.AND_AND;
        }
        throw unknownCharException();
    }

    private Token analyzeOr() throws Exception {
        if (readch('|')) {
            return Token.OR_OR;
        }
        throw unknownCharException();
    }

    private Token analyzeEq() throws IOException {
        if (readch('=')) {
            return Token.EQ_EQ;
        }
        return Token.EQ;
    }

    private Token analyzeNot() throws IOException {
        if (readch('=')) {
            return Token.NOT_EQ;
        }
        return Token.NOT;
    }

    private Token analyzeGt() throws IOException {
        readch();
        if (ch == '>') {
            return resetAndReturn(Token.GT_GT);
        }
        if (ch == '=') {
            return resetAndReturn(Token.GT_EQ);
        }
        return Token.GT;
    }

    private Token analyzeLt() throws IOException {
        readch();
        if (ch == '<') {
            return resetAndReturn(Token.LT_LT);
        }
        if (ch == '=') {
            return resetAndReturn(Token.LT_EQ);
        }
        return Token.LT;
    }

    private Word analyzeWord() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(ch);
            readch();
        } while (Character.isLetterOrDigit(ch) || ch == '_');
        Word word = Word.getOrCreate(wordTable, sb.toString()); // Verifica se a palavra já está na lista de tokens conhecidos
        Core.currentEnviroment.getOrCreate(word); // Verifica se o token está na tabela de símbolos
        return word;
    }

    /* Erro lançado quando encontramos um caractere inesperado */
    private Exception unknownCharException() {
        return new Exception("Erro: caractere inesperado encontrado na linha " + Core.line + ": '" + ch + "'");
    }

}