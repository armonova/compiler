package syntatic;


import lexer.Lexer;
import lexer.Token;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Syntatic {

    public static void main(String[] args) throws IOException {
        String path = "../../../codigos_para_teste/";
        String fileName = args.length > 0 ? args[0] : "teste0.txt";
        Lexer lexer = new Lexer(path + fileName);

        Token token = lexer.scan();
        while (token != null) {
            System.out.println(token.toString());
            token = lexer.scan();
        }

    }

}
