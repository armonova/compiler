package symbols;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import lexer.*;

import java.util.Hashtable;

/**
 * @author arthur
 */
public class Env {
    private final Hashtable<Token, Id> table; // tabela de símbolos do ambiente
    protected Env prev; // ambiente imediatamente superior
    private final int level; // profundidade do ambiente, para a etapa 1 do trabalho, é sempre 0

    /* criar um novo ambiente a partir de um existente */
    public Env(Env previous) {
        table = new Hashtable<>();
        prev = previous;
        level = previous.getLevel() + 1;
    }

    /* criar o ambiente raiz */
    public Env() {
        table = new Hashtable<>();
        prev = null;
        level = 0;
    }

    /* Este método insere uma entrada na TS do ambiente
     * A chave da entrada é o Token devolvido pelo analisador léxico
     * Id é uma classe que representa os dados a serem armazenados na TS para
     * identificadores */

    public Id put(Token token) {
        Id id = new Id(level);
        table.put(token, id);
        return id;
    }

    /* Este método retorna as informações (Id) referentes a determinado Token
    /* O Token é pesquisado do ambiente atual para os anteriores */
    public Id get(Token token) {
        for (Env env = this; env != null; env = env.prev) {
            Id found = (Id) env.table.get(token);
            if (found != null) // se Token existir em uma das TS
                return found;
        }
        return null;
    }

    public int getLevel() {
        return level;
    }

    public void print() { // Método para facilitar a visualização dos ambientes
        if (prev != null) {
            prev.print();
        }
        System.out.println("\nTabela de símbolos nível " + level + " {");
        table.forEach((token, id) -> System.out.println("\t" + token + ", " + id));
        System.out.println("}\n");
    }

    public Id getOrCreate(Token token) {  
        Id id = get(token);
        if (id == null) {
            return put(token);
        }
        return id;
    }
}
