package lexer;

public class Word extends Token {
    private String lexeme = "";

    // escrever aqui as palavras

    public Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return "" + lexeme;
    }

    public static Word getOrCreate( String lexeme){
        Word word = (Word) words.get(lexeme);
        if (word != null){
            return word; //palavra já existe na HashTable
        }
        word = new Word(lexeme, Tag.ID);
        words.put(lexeme, word);
        return word;
    }

}
