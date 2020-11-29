package symbols;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import lexer.*;

import java.util.Comparator;
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
            Id found = env.table.get(token);
            if (found != null) // se Token existir em uma das TS
                return found;
        }
        return null;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (prev != null) {
            sb.append(prev);
        }
        sb.append("\nTabela de símbolos nível ").append(level).append(" {").append('\n');
        table.entrySet().stream().sorted(Comparator.comparing(entry -> entry.getKey().toString()))
                .forEach(entry -> sb.append('\t').append(entry.getKey()).append(", ").append(entry.getValue()).append('\n'));
        sb.append("}\n");
        return sb.toString();
    }

    public Id getOrCreate(Token token) {
        Id id = get(token);
        if (id == null) {
            return put(token);
        }
        return id;
    }

    /* Retorna o Id do token deseja, mas limita a busca apenas ao ambiente atual */
    public Id getOnCurrent(Token token) {
        return this.table.get(token);
    }
}
