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
        // //
        System.out.println("\nSequência de tokens:\n");
        Token token = lexer.scan();
        while (token != null) { // Obtem e exibe todos os tokens do analisador léxico
            System.out.println(token.toString());
            try {
                token = lexer.scan();
            } catch (Exception e) {
                System.out.println(currentEnviroment); // Nesta etapa, exibe TS mesmo se der algum erro
                throw e;
            }
        }

        System.out.println(currentEnviroment); // Exibe TS final

    }

}