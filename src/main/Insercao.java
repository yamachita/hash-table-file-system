package Trabalho;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


// Classe que realiza as operações de inserção de registros.
public class Insercao {

    private String nomeTabela;
    private Scanner teclado;
    private EstruturaTabela estruturaTabela;
    private Catalogo catalogo;
    private boolean insercaoConcluida; // Indica se a operação de inserção foi realizada com sucesso.

    public Insercao(String nomeTabela) throws IOException {
        catalogo = new Catalogo();
        teclado = new Scanner(System.in);
        this.nomeTabela = nomeTabela;
        estruturaTabela = catalogo.lerTabelaCatalogo(nomeTabela.trim());
    }
    
    // Método de cria um registro pra ser salvo na tabela.
    private ArrayList<Registro> criarRegistro() {

        ArrayList<Registro> registros = null;
        boolean continuar = false;
        boolean continuar2 = false;

        if (estruturaTabela != null) {

            registros = new ArrayList<Registro>();
            do { // Loop que pergunta se quer criar outro registro
                continuar = false;
                Registro reg = new Registro();

                for (int i = 0; i < estruturaTabela.getAtributos().size(); i++) {
                    do { // Loop pra garantir o tipo certo do atributo.
                        continuar2 = false;
                        String tipo = estruturaTabela.getAtributo(i).getTipo().toUpperCase();
                        System.out.println("Digite um valor para " + estruturaTabela.getAtributo(i).getNome() + ": (" + tipo + ")");

                        try {
                            switch (tipo) { // Adiciona os atributos em um registro de acordo com seu tipo
                                case "INTEIRO":
                                    int valorInt = teclado.nextInt();
                                    teclado.nextLine();
                                    Atributo<Integer> at1 = new Atributo(valorInt);
                                    reg.addAtributo(at1);
                                    break;
                                case "DOUBLE":
                                    double valorDouble = teclado.nextDouble();
                                    teclado.nextLine();
                                    Atributo<Double> at2 = new Atributo(valorDouble);
                                    reg.addAtributo(at2);
                                    break;
                                case "STRING":
                                    String valorString = teclado.nextLine();
                                    if (!verificaValidadeString(valorString)) {
                                        throw new IllegalArgumentException();
                                    }
                                    int tam = estruturaTabela.getAtributo(i).getTamanhoMaximo();
                                    if (valorString.length() <= tam) {
                                        while (valorString.length() < tam) {
                                            valorString += " ";
                                        }
                                    } else {
                                        throw new IndexOutOfBoundsException();
                                    }
                                    valorString = valorString.substring(0, tam);
                                    Atributo<String> at3 = new Atributo(valorString);
                                    reg.addAtributo(at3);
                                    break;
                            }

                        } catch (InputMismatchException e) { // Tipo errado
                            continuar2 = true;
                            System.out.println("Valor com tipo errado.");
                            teclado.nextLine();
                        } catch (IllegalArgumentException e) { // String com caracteres especiais
                            continuar2 = true;
                            System.out.println("String com caracteres não suportados, tente novamente.");

                        } catch (IndexOutOfBoundsException e) { // String maior que o tamanho máximo
                            continuar2 = true;
                            System.out.println("String maior que o tamanho máximo definido (" + estruturaTabela.getAtributo(i).getTamanhoMaximo() + "), tente novamente.");
                        }

                    } while (continuar2);
                }

                if (!continuar) {
                    registros.add(reg);
                    System.out.println("Deseja inserir outro registro ? (S|N)");
                    String resp = teclado.next();
                    teclado.nextLine();
                    if (resp.equalsIgnoreCase("s")) {
                        continuar = true;
                    }
                }
            } while (continuar);
        }

        return registros;
    }

    public void inserirRegistro(ArrayList<Registro> registros) throws IOException, ClassNotFoundException {

        Selecao selecionar = new Selecao(nomeTabela);
        TabelaHash TH = selecionar.selecionarTipoTabelaHash(); // Seleciona tabela Hash de acordo com o tipo de chave

        for (int i = 0; i < registros.size(); i++) {
            TH.salvaRegistro(registros.get(i), registros.get(i).getAtributo(estruturaTabela.getPosicaoChaveVetor()).getValor(), estruturaTabela.getPosicaoChaveVetor());
        }
        TH.fecharArquivos();
        this.insercaoConcluida = TH.isRegistroInserido();
    }
    
    public void salvarNovoRegistro() throws IOException, FileNotFoundException, ClassNotFoundException {

        ArrayList<Registro> registro = criarRegistro();
        if (registro != null) {
            inserirRegistro(registro);
        }
    }

    private boolean verificaValidadeString(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!(s.charAt(i) > 0 && s.charAt(i) < 126)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isInsercaoConcluida() {
        return insercaoConcluida;
    }
}
