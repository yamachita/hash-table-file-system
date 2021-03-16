package Trabalho;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


// Classe que realiza as operações de exclusão.
public class Exclusao {

    private String nomeTabela;
    private Scanner teclado;
    private EstruturaTabela estruturaTabela;
    private Catalogo catalogo;

    public Exclusao(String nomeTabela) throws IOException {
        catalogo = new Catalogo();
        teclado = new Scanner(System.in);
        this.nomeTabela = nomeTabela;
        estruturaTabela = catalogo.lerTabelaCatalogo(nomeTabela.trim());
    }

    public void menuExclusao() throws IOException, FileNotFoundException, ClassNotFoundException {

        boolean continuar = true;
        if (estruturaTabela != null) {

            System.out.printf("%s\n%s\n%s\n", "1 - Excluir tabela",
                    "2 - Selecionar registros a serem excluidos", "3 - Excluir todos os registros");
            do {
                int resposta = teclado.nextInt();
                if (resposta == 1) {
                    excluirTabela();
                } else if (resposta == 2) {
                    menuExclusao2();
                } else if (resposta == 3) {
                    excluirTodosRegistros();
                } else {
                    continuar = false;
                    System.out.println("Resposta invalida, tente novamente.");
                }

            } while (!continuar);
        }
    }

    private void excluirTodosRegistros() {
        File ponteiros = new File("ponteiro" + nomeTabela + ".dat");
        File registros = new File(nomeTabela + ".dat");
        ponteiros.delete();
        registros.delete();
    }

    private void menuExclusao2() throws IOException, FileNotFoundException, ClassNotFoundException {

        boolean continuar = true;
        System.out.printf("%s\n%s\n", "1 - Excluir registros selecionados por chave",
                "2 - Excluir registros selecionados por outros atributos");
        do {
            int resposta = teclado.nextInt();
            teclado.nextLine();
            if (resposta == 1) {
                excluirPorChave();
            } else if (resposta == 2) {
                excluirPorAtributos();
            } else {
                continuar = false;

                System.out.println("Resposta inválida, tente novamente.");
            }
        } while (!continuar);
    }

    public void excluirRegistros(ArrayList<Registro> registros) throws IOException, ClassNotFoundException {

        Selecao selecionar = new Selecao(nomeTabela);
        TabelaHash TH = selecionar.selecionarTipoTabelaHash();
        for (int i = 0; i < registros.size(); i++) {
            TH.excluirRegistro(registros.get(i).getAtributo(estruturaTabela.getPosicaoChaveVetor()).getValor(), estruturaTabela.getPosicaoChaveVetor());
        }
        TH.fecharArquivos();
    }
    // Método que exclui os registros selecionados por chave.
    public void excluirPorChave() throws IOException, FileNotFoundException, ClassNotFoundException {

        String tipo = estruturaTabela.getTipoChave().toUpperCase();
        boolean continuar = false;
        Selecao selecionar = new Selecao(nomeTabela);
        TabelaHash TH = selecionar.selecionarTipoTabelaHash();

        do {

            continuar = false;
            System.out.println("Diga a chave do registro: (" + tipo + ")");
            try {
                switch (tipo) { // Exclui o registro de acordo com o tipo de sua chave
                    case "INTEIRO":
                        int i = teclado.nextInt();
                        TH.excluirRegistro(i, estruturaTabela.getPosicaoChaveVetor());
                        break;
                    case "DOUBLE":
                        double d = teclado.nextDouble();
                        TH.excluirRegistro(d, estruturaTabela.getPosicaoChaveVetor());
                        break;
                    case "STRING":
                        String s = teclado.nextLine();
                        int tamanho = estruturaTabela.getAtributo(estruturaTabela.getPosicaoChaveVetor()).getTamanhoMaximo();
                        while (s.length() < tamanho) {
                            s += " ";
                        }
                        s = s.substring(0, tamanho);
                        TH.excluirRegistro(s, estruturaTabela.getPosicaoChaveVetor());
                        break;
                }
            } catch (InputMismatchException e) {
                continuar = true;
                System.out.println("Valor com tipo errado, tente novamente.");
                teclado.nextLine();
            }

            if (!continuar) {
                System.out.println("Deseja excluir outro registro ? (S|N)");
                String resp = teclado.next();
                teclado.nextLine();
                if (resp.equalsIgnoreCase("s")) {
                    continuar = true;
                }
            }

        } while (continuar);

        TH.fecharArquivos();
    }
    // Método que realiza a exclusão de registros selecionados por outros atributos.
    private void excluirPorAtributos() throws IOException, ClassNotFoundException {

        Selecao selecionar = new Selecao(nomeTabela);
        ArrayList<Registro> regs = selecionar.selecionarRegistros(selecionar.pegarTodosOsRegistros());
        if (!regs.isEmpty()) {
            excluirRegistros(regs);
        } else {
            System.out.println("Registro não existe.");
        }
    }

    private void excluirTabela() throws IOException {
        excluirTodosRegistros();
        Catalogo cat = new Catalogo();
        cat.excluirTabela(nomeTabela);
    }

}
