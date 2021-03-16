package Trabalho;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Sistema {

    private Scanner teclado;

    public void iniciarSistema() throws IOException, FileNotFoundException, ClassNotFoundException {

        teclado = new Scanner(System.in);
        int resposta = 0;

        do {

            System.out.printf("        *%s*\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
                    "MENU", "1 - Criar tabela", "2 - Exibir tabelas",
                    "3 - Inserir registro", "4 - Consultar tabela",
                    "5 - Excluir registros", "6 - Atualizar registros", "7 - Sair");

            resposta = teclado.nextInt();

            switch (resposta) {
                case 1:
                    criarTabela();
                    break;
                case 2:
                    exibirTabelas();
                    break;
                case 3:
                    inserirRegistro();
                    break;
                case 4:
                    consultarTabela();
                    break;
                case 5:
                    excluirRegistro();
                    break;
                case 6:
                    atualizarRegistros();
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Entrada inválida. Tente novamente.");
            }

        } while (resposta != 7);
    }

    private void criarTabela() throws IOException {

        System.out.println("Digite o nome da tabela: ");
        String nomeTabela = teclado.next();
        Tabela tabela = new Tabela(nomeTabela);

    }

    private void exibirTabelas() throws IOException {

        Catalogo catalogo = new Catalogo();
        catalogo.exibirNomeTabelas();
    }

    private void inserirRegistro() throws IOException, FileNotFoundException, ClassNotFoundException {

        System.out.println("Diga o nome da tabela");
        String nomeTabela = teclado.next();
        Insercao inserir = new Insercao(nomeTabela);
        inserir.salvarNovoRegistro();
    }

    private void consultarTabela() throws IOException, ClassNotFoundException {

        System.out.println("Diga o nome da tabela");
        String nomeTabela = teclado.next();
        Selecao selecionar = new Selecao(nomeTabela);
        selecionar.menuSelecao();
    }

    private void excluirRegistro() throws IOException, FileNotFoundException, ClassNotFoundException {

        System.out.println("Diga o nome da tabela");
        String nomeTabela = teclado.next();
        Exclusao excluir = new Exclusao(nomeTabela);
        excluir.menuExclusao();
    }

    private void atualizarRegistros() throws IOException, FileNotFoundException, ClassNotFoundException {

        System.out.println("Diga o nome da tabela");
        String nomeTabela = teclado.next();
        Atualizacao atualizar = new Atualizacao(nomeTabela);
        atualizar.menuAtualizacao();
    }

}
