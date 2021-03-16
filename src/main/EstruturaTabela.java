package Trabalho;

import java.util.ArrayList;


// Classe que contém informações de uma tabela salva no catálogo.
public class EstruturaTabela {

    private String nome;
    private ArrayList<EstruturaAtributo> atributos = new ArrayList<EstruturaAtributo>();
    private int tamanhoRegistroTabela;
    private String chave;

    public EstruturaTabela() {
    }

    public EstruturaTabela(String nome) {
        this.nome = nome;
    }

    public void add(EstruturaAtributo atributo) {
        atributos.add(atributo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<EstruturaAtributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<EstruturaAtributo> atributos) {
        this.atributos = atributos;
    }

    public int getTamanhoRegistroTabela() {
        return tamanhoRegistroTabela;
    }

    public void setTamanhoRegistroTabela(int tamanhoRegistroTabela) {
        this.tamanhoRegistroTabela = tamanhoRegistroTabela;
    }

    public int getQdtAtributos() {
        return atributos.size();
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public EstruturaAtributo getAtributo(int i) {
        return atributos.get(i);
    }

    public String getTipoChave() {

        String tipo = "";

        for (EstruturaAtributo a : atributos) {
            if (this.chave.equalsIgnoreCase(a.getNome())) {
                tipo = a.getTipo();
            }
        }

        return tipo;
    }
    
    //Método que retorna a posição da chave no vetor de atributos.
    public int getPosicaoChaveVetor() {
        int p = 0;

        for (int i = 0; i < atributos.size(); i++) {
            if (this.chave.equalsIgnoreCase(atributos.get(i).getNome())) {
                p = i;
            }
        }

        return p;
    }
   // Método que retorna a posição de um atributo no vetor de atributos.
    public int getPosicaoAtributoVetor(String nome) {

        int p = -1;

        for (int i = 0; i < atributos.size(); i++) {
            if (nome.equalsIgnoreCase(atributos.get(i).getNome())) {
                p = i;
            }
        }
        if(p == -1){
            System.out.println("Atributo não existe.");
        }
        return p;
    }
}
