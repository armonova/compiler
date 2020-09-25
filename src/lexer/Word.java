package lexer;

public class Word {
    private String lexeme = "";

    // escrever aqui as palavras

    public Word (String s, int tag){
//        super (tag);
        lexeme = s;
    }
    public String toString(){
        return "" + lexeme;
    }
}
