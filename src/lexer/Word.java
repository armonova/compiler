package lexer;

import symbols.Env;
import syntatic.Core;

import java.util.Hashtable;

public class Word extends Token {

    private final String lexeme;

    public Word(String s, Tag tag) {
        super(tag);
        lexeme = s;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        if (tag.equals(Tag.ID)) {
            return "<" + tag + ", " + lexeme + ">";
        }
        return "<" + tag + ">";
    }

    public static Word getOrCreate(Hashtable<String, Word> wordTable, String lexeme) {
        Word word = wordTable.get(lexeme);
        if (word != null) {
            return word; // palavra já possui um token conhecido
        }
        word = new Word(lexeme, Tag.ID);
        wordTable.put(lexeme, word); // adicionada na lista de tokens conhecidos
        return word;
    }

}
