package Trabalho;

import java.io.Serializable;

// Classe que armazena o valor de um atributo.
public class Atributo <Tipo> implements Serializable
{
    
    private Tipo valor;

    public Atributo(Tipo valor) {
        this.valor = valor;
    }

    public Tipo getValor() {
        return valor;
    }

    public void setValor(Tipo valor) {
        this.valor = valor;
    }
    
}
