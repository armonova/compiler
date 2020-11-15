package syntatic;


import lexer.Lexer;
import lexer.Token;
import symbols.Env;

/* Esta classe é o núcleo do compilador, que estará no analisador sintático */
public class Core {

    /* Ambiente atual, que possui a tabela de símbolos */
    public static Env currentEnviroment;

    public static void main(String[] args) throws Exception {

        String fileName = args.length > 0 ? args[0] : "codigos_para_teste/teste0.txt";

        currentEnviroment = new Env(); // Define ambiente raiz
        Lexer lexer = new Lexer(fileName); // Define analisador léxico
        Syntatic syntatic = new Syntatic(lexer); // Define analisador sintático

        syntatic.analyze();

    }

}
