package Trabalho;

// Classe que contém apenas o modelo do atributo para ser
// salvo no catálogo.
public class EstruturaAtributo {

    private String nome;
    private String tipo;
    private int tamanhoMaximo;

    public EstruturaAtributo() {
    }   
    
    public EstruturaAtributo(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    // Construtor para atributo do tipo String.
    public EstruturaAtributo(String nome, String tipo, int tamanhoMaximo) {
        this.nome = nome;
        this.tipo = tipo;
        this.tamanhoMaximo = tamanhoMaximo;
    }
    
    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTamanhoMaximo() {
        return tamanhoMaximo;
    }

    public void setTamanhoMaximo(int tamanhoMaximo) {
        this.tamanhoMaximo = tamanhoMaximo;
    }

}
