package Trabalho;

import java.io.Serializable;
import java.util.ArrayList;

// Classe que representa o registro a ser salvo.

public class Registro implements Serializable {
    
    private ArrayList<Atributo> atributos;
    private int ponteiro = -1;
    private boolean ocupado = true;
    
    public Registro(){
        atributos = new ArrayList<>();
    }
     
    public void addAtributo (Atributo a){
        atributos.add(a);
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public int getPonteiro() {
        return ponteiro;
    }

    public void setPonteiro(int ponteiro) {
        this.ponteiro = ponteiro;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
    
    public Atributo getAtributo(int i){
        return atributos.get(i);
    }
}

