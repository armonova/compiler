package syntatic;


import lexer.Lexer;
import semantic.Semantic;
import symbols.Env;

/* Esta classe é o núcleo do compilador, que estará no analisador sintático */
public class Core {

    /* Ambiente atual, que possui a tabela de símbolos */
    public static Env currentEnviroment;

    /* Linha atual do programa */
    public static int line;

    public static void main(String[] args) throws Exception {

        String fileName = args.length > 0 ? args[0] : "codigos_para_teste/teste0.txt";

        currentEnviroment = new Env(); // Define ambiente raiz
        Lexer lexer = new Lexer(fileName); // Define analisador léxico
        Semantic semantic = new Semantic(); // Define analisador semântico
        Syntatic syntatic = new Syntatic(lexer, semantic); // Define analisador sintático

        syntatic.analyze();

    }

}
