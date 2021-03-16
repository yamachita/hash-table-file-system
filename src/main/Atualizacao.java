package Trabalho;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

//Classe que realiza as operações de atualização.

public class Atualizacao {

    private String nomeTabela;
    private Scanner teclado;
    private EstruturaTabela estruturaTabela;
    private Catalogo catalogo;
    private boolean modificouChave = false;
    private String chaveAntiga;

    public Atualizacao(String nomeTabela) throws IOException {

        catalogo = new Catalogo();
        teclado = new Scanner(System.in);
        this.nomeTabela = nomeTabela;
        estruturaTabela = catalogo.lerTabelaCatalogo(nomeTabela.trim());

    }

    public void menuAtualizacao() throws IOException, FileNotFoundException, ClassNotFoundException {

        if (estruturaTabela != null) {

            System.out.printf("%s\n%s\n", "1 - Atualizar registros selecionados por chave",
                    "2 - Atualizar registros selecionados por outros atributos");
            boolean cnt = true;
            do {
                int resposta = teclado.nextInt();
                teclado.nextLine();
                if (resposta == 1) {
                    atualizarRegistro(1);
                } else if (resposta == 2) {
                    atualizarRegistro(2);
                } else {
                    cnt = false;
                    System.out.println("Resposta inválida, tente novamente.");
                }
            } while (!cnt);
        }
    }

    //Método que seleciona os registros a serem atualizados, se foi modificada a chave do registro
    //o novo registro é inserido e o antigo removido. Se não foi modificado a chave o registro é
    //atualizado na tabela no mesmo local.
    private void atualizarRegistro(int opcao) throws IOException, FileNotFoundException, ClassNotFoundException {

        Selecao selecionar = new Selecao(nomeTabela);
        ArrayList<Registro> registro = null;
        if (opcao == 1) {
            System.out.println("Selecione um ou mais registros pela sua chave.");
            registro = selecionar.selecionarPorChave();
        } else {
            registro = selecionar.selecionarRegistros(selecionar.pegarTodosOsRegistros());
            if (registro.isEmpty()) {
                System.out.println("Nenhum registro encontrado.");
            }
        }

        if (!registro.isEmpty()) {

            registro = modificarRegistro(registro);

            if (modificouChave) {

                Insercao inserir = new Insercao(nomeTabela);
                inserir.inserirRegistro(registro);

                if (inserir.isInsercaoConcluida()) { // só exclui o registro antigo se a chave nova for válida

                    TabelaHash th = selecionar.selecionarTipoTabelaHash();
                    String tipoChave = estruturaTabela.getTipoChave().toUpperCase();
                    switch (tipoChave) {
                        case "INTEIRO":
                            int chaveInt = Integer.valueOf(chaveAntiga);
                            th.excluirRegistro(chaveInt, estruturaTabela.getPosicaoChaveVetor());
                            break;
                        case "DOUBLE":
                            double chaveDouble = Double.valueOf(chaveAntiga);
                            th.excluirRegistro(chaveDouble, estruturaTabela.getPosicaoChaveVetor());
                            break;
                        case "String":
                            th.excluirRegistro(chaveAntiga, estruturaTabela.getPosicaoChaveVetor());
                            break;
                    }
                    th.fecharArquivos();
                }
            } else {
                inserirRegistroAtualizado(registro);
            }

        }

    }
    
    
    // Método que modifica os registros selecionados, garantindo a integridade da chave.
    private ArrayList<Registro> modificarRegistro(ArrayList<Registro> regs) throws IOException, FileNotFoundException, ClassNotFoundException {

        String chave = estruturaTabela.getChave();
        boolean continuar1 = true;
        boolean continuar2 = false;
        boolean atributoChave = false;
        String nomeAtributo = "";

        do { // Loop perguntando se deseja modificar mais um atributo

            do { // Loop pra garantir a integridade da chave
                atributoChave = false;
                System.out.println("Diga o nome do atributo a ser modificado: ");
                nomeAtributo = teclado.next();
                teclado.nextLine();

                if (regs.size() > 1 && nomeAtributo.equalsIgnoreCase(chave)) { // Se mais de 1 registro for escolhido não é possivel modificar a chave
                    System.out.println("Não é possivel modificar a chave de mais de um atributo.");
                    atributoChave = true;
                }
            } while (atributoChave);

            int p = estruturaTabela.getPosicaoAtributoVetor(nomeAtributo);
            if (p != -1) {

                if (chave.equalsIgnoreCase(nomeAtributo)) {
                    this.modificouChave = true;
                    this.chaveAntiga = regs.get(0).getAtributo(estruturaTabela.getPosicaoChaveVetor()).getValor().toString();
                    for (int i = 0; i < regs.size(); i++) { // Se a chave for modificada atualiza os ponteiros do registro 
                        regs.get(i).setPonteiro(-1);
                    }
                }

                do { // Loop pra garantir o tipo certo dos atributos

                    String tipo = estruturaTabela.getAtributo(p).getTipo().toUpperCase();
                    System.out.println("Diga o novo valor do atributo: (" + tipo + ")");

                    continuar2 = false;
                    try {
                        switch (tipo) { // Modifica os atributos de todos os registros selecionados
                            case "INTEIRO":
                                int valorInt = teclado.nextInt();
                                for (int k = 0; k < regs.size(); k++) {
                                    regs.get(k).getAtributo(p).setValor(valorInt);
                                }
                                break;
                            case "DOUBLE":
                                double valorDouble = teclado.nextDouble();
                                for (int k = 0; k < regs.size(); k++) {
                                    regs.get(k).getAtributo(p).setValor(valorDouble);
                                }
                                break;
                            case "STRING":
                                String valorString = teclado.nextLine();
                                if (!verificaValidadeString(valorString)) {
                                    throw new IllegalArgumentException();
                                }
                                int tam = estruturaTabela.getAtributo(p).getTamanhoMaximo();
                                if (valorString.length() <= tam) {
                                    while (valorString.length() < tam) {
                                        valorString += " ";
                                    }
                                } else {
                                    throw new IndexOutOfBoundsException();
                                }
                                valorString = valorString.substring(0, tam);
                                for (int k = 0; k < regs.size(); k++) {
                                    regs.get(k).getAtributo(p).setValor(valorString);
                                }
                                break;
                        }

                    } catch (InputMismatchException e) {
                        continuar2 = true;
                        System.out.println("Valor com tipo errado.");
                        teclado.nextLine();
                    } catch (IllegalArgumentException e) {
                        continuar2 = true;
                        System.out.println("String com caracteres não suportados, tente novamente.");
                    } catch (IndexOutOfBoundsException e) {
                        continuar2 = true;
                        System.out.println("String maior que o tamanho máximo definido (" + estruturaTabela.getAtributo(p).getTamanhoMaximo() + "), tente novamente.");
                    }

                } while (continuar2);

                System.out.println("Deseja modificar mais algum atributo ?(S|N)");
                String resposta = teclado.next();
                teclado.nextLine();
                if (resposta.equalsIgnoreCase("n")) {
                    continuar1 = false;
                }

            }

        } while (continuar1);

        return regs;
    }
    // Método que insere os registros modificados na tabela. 
    private void inserirRegistroAtualizado(ArrayList<Registro> registros) throws IOException, FileNotFoundException, ClassNotFoundException {

        Selecao selecionar = new Selecao(nomeTabela);
        TabelaHash tabelaHash = selecionar.selecionarTipoTabelaHash();
        for (int i = 0; i < registros.size(); i++) { // Percorre o array atualizando cada registro
            tabelaHash.atualizaRegistro(registros.get(i), registros.get(i).getAtributo(estruturaTabela.getPosicaoChaveVetor()).getValor(), estruturaTabela.getPosicaoChaveVetor());
        }

        tabelaHash.fecharArquivos();
    }
    // Verifica se tem caracteres especiais.
    private boolean verificaValidadeString(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!(s.charAt(i) > 0 && s.charAt(i) < 126)) {
                return false;
            }
        }
        return true;
    }
}
