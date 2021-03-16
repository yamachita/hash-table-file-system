package Trabalho;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


// Classe que seleciona os registros usados em operações.
public class Selecao {

    private String nomeTabela;
    private Scanner teclado;
    private EstruturaTabela estruturaTabela;
    private Catalogo catalogo;

    public Selecao(String nomeTabela) throws IOException {

        catalogo = new Catalogo();
        teclado = new Scanner(System.in);
        this.nomeTabela = nomeTabela;
        estruturaTabela = catalogo.lerTabelaCatalogo(nomeTabela);

    }

    public void menuSelecao() throws IOException, ClassNotFoundException {

        if (estruturaTabela != null) {
            boolean continuar = true;
            System.out.printf("%s\n%s\n%s\n", "1 - Consultar estrutura da tabela",
                    "2 - Exibir todos os registros da tabela",
                    "3 - Selecionar registros");
            do {
                int resposta = teclado.nextInt();
                switch (resposta) {
                    case 1:
                        exibirEstrutura();
                        break;
                    case 2:
                        exibirRegistros();
                        break;
                    case 3:
                        menuSelecao2();
                        break;
                    default:
                        continuar = false;
                        System.out.println("Entrada inválida, tente novamente.");
                }
            } while (!continuar);
        }
    }

    private void exibirEstrutura() throws IOException {
        String[] tabela = catalogo.getTabela(nomeTabela);
        if (tabela != null) {
            catalogo.imprimeEstruturaTabela(tabela);
        }
    }

    private void exibirRegistros() throws IOException, ClassNotFoundException {
        ArrayList<Registro> regs = pegarTodosOsRegistros();
        if (!regs.isEmpty()) {
            imprimirRegistro(regs);
        } else {
            System.out.println("Tabela vazia.");
        }
    }

    public ArrayList<Registro> pegarTodosOsRegistros() throws IOException, ClassNotFoundException {
        TabelaHash TH = new TabelaHash(nomeTabela, estruturaTabela.getTamanhoRegistroTabela());
        ArrayList<Registro> regs = TH.leTodosOsRegistros();
        TH.fecharArquivos();
        return regs;
    }

    private void menuSelecao2() throws IOException, FileNotFoundException, ClassNotFoundException {

        ArrayList<Registro> reg;
        boolean continuar = true;
        System.out.printf("%s\n%s\n", "1 - Consultar registros por chave",
                "2 - Consultar registros por outros atributos");
        do {
            int resposta = teclado.nextInt();
            teclado.nextLine();
            if (resposta == 1) {
                reg = selecionarPorChave();
                imprimirRegistro(reg);
            } else if (resposta == 2) {
                reg = selecionarRegistros(pegarTodosOsRegistros());
                imprimirRegistro(reg);

            } else {
                continuar = false;
                System.out.println("Resposta inválida, tente novamente.");
            }
        } while (!continuar);
    }
    
    // Método que seleciona os registros escolhidos por chave.
    public ArrayList<Registro> selecionarPorChave() throws IOException, FileNotFoundException, ClassNotFoundException {

        Registro reg = null;
        ArrayList<Registro> registros = new ArrayList<Registro>();
        String tipo = estruturaTabela.getTipoChave().toUpperCase();
        boolean continuar = false;
        TabelaHash TH = selecionarTipoTabelaHash();

        do {
            continuar = false;
            System.out.println("Diga a chave do registro." + "( " + tipo + " )");

            try {
                switch (tipo) { // Recupera o registro de acordo com tipo de sua chave
                    case "INTEIRO":
                        int i = teclado.nextInt();
                        reg = TH.recuperaRegistro(i, estruturaTabela);
                        break;
                    case "DOUBLE":
                        double d = teclado.nextDouble();
                        reg = TH.recuperaRegistro(d, estruturaTabela);
                        break;
                    case "STRING":
                        String s = teclado.nextLine();
                        int tamanho = estruturaTabela.getAtributo(estruturaTabela.getPosicaoChaveVetor()).getTamanhoMaximo();
                        while (s.length() < tamanho) {
                            s += " ";
                        }
                        s = s.substring(0, tamanho);
                        reg = TH.recuperaRegistro(s, estruturaTabela);
                        break;
                }
            } catch (InputMismatchException e) {
                continuar = true;
                System.out.println("Valor com tipo errado, tente novamente.");
                teclado.nextLine();
            }

            if (!continuar) {
                if (reg != null) {
                    registros.add(reg);
                }

                System.out.println("Deseja selecionar outro registro ? (S|N)");
                String resp = teclado.next();
                teclado.nextLine();
                continuar = false;
                if (resp.equalsIgnoreCase("s")) {
                    continuar = true;
                }

            }

        } while (continuar);

        TH.fecharArquivos();
        
        if(registros.size() > 1){
        System.out.println("Deseja selecionar esse grupo de registros por outros atributos? (S|N)");
        String resposta = teclado.nextLine();
        if(resposta.equalsIgnoreCase("s")){
            registros = selecionarRegistros(registros);
        }
    }

        return registros;
    }
    
    // Método que seleciona os registros escolhidos por atributos não chave 
    // e seleciona os que atendem as restrições.
    public ArrayList<Registro> selecionarRegistros(ArrayList<Registro> registros) throws IOException, ClassNotFoundException {

        boolean existe = false;
        boolean continuar = true;
        int posicao = 0;
        String atributo = "";
        ArrayList<Registro> regs = registros;
        do {
            existe = false;
            while (!existe) {
                System.out.println("Digite o nome do atributo:");
                atributo = teclado.next();
                for (int i = 0; i < estruturaTabela.getAtributos().size(); i++) {
                    if (estruturaTabela.getAtributo(i).getNome().equalsIgnoreCase(atributo)) {
                        existe = true;
                        posicao = i;
                    }
                }
                if (!existe) {
                    System.out.println("O atributo não existe nessa tabela!");
                }
            }
            int resp;
            do {

                System.out.println("\nSelecione a condição de comparação:");
                System.out.printf("%s\n%s\n%s\n%s\n%s\n", "1 - >", "2 - <", "3 - >=", "4 - <=", "5 - =");
                resp = teclado.nextInt();
                teclado.nextLine();
                if (resp < 1 || resp > 5) {
                    System.out.println("Opção errada! Tente novamente");
                } else {
                    do {
                        if (regs == null) {
                            regs = registros;
                        }
                        System.out.println("Digite o valor a ser comparado: ");
                        String valor = teclado.nextLine();
                        String tipo = estruturaTabela.getAtributo(posicao).getTipo();

                        if (tipo.equalsIgnoreCase("string")) {
                            while (valor.length() < estruturaTabela.getAtributo(posicao).getTamanhoMaximo()) {
                                valor += " ";
                            }
                        }
                        regs = comparaRegistro(regs, posicao, tipo, resp, valor);
                    } while (regs == null);
                }
            } while (resp < 1 || resp > 5);
            System.out.println("Deseja selecionar mais um atributo? (S/N)");
            String resposta = teclado.nextLine();
            if (resposta.equalsIgnoreCase("n")) {
                continuar = false;
            }
        } while (continuar);

        return regs;

    }
    
    // Compara os registros e retorna os que atendem as restrições.
    private ArrayList<Registro> comparaRegistro(ArrayList<Registro> regs, int posicao, String tipo, int resp, String valor) {

        ArrayList<Registro> selecionados = new ArrayList<>();
        if (tipo.equalsIgnoreCase("string")) {
            for (int i = 0; i < regs.size(); i++) {
                int comparacao = regs.get(i).getAtributo(posicao).getValor().toString().compareToIgnoreCase(valor);
                switch (resp) {
                    case 1:
                        if (comparacao > 0) {
                            selecionados.add(regs.get(i));
                        }
                        break;
                    case 2:
                        if (comparacao < 0) {
                            selecionados.add(regs.get(i));
                        }
                        break;
                    case 3:
                        if (comparacao >= 0) {
                            selecionados.add(regs.get(i));
                        }
                        break;
                    case 4:
                        if (comparacao <= 0) {
                            selecionados.add(regs.get(i));
                        }
                        break;
                    case 5:
                        if (comparacao == 0) {
                            selecionados.add(regs.get(i));
                        }
                        break;
                }
            }
        } else {
            try {
                for (int i = 0; i < regs.size(); i++) {
                    switch (resp) {
                        case 1:
                            if (Double.parseDouble(regs.get(i).getAtributo(posicao).getValor().toString()) > Double.parseDouble(valor)) {
                                selecionados.add(regs.get(i));
                            }
                            break;
                        case 2:
                            if (Double.parseDouble(regs.get(i).getAtributo(posicao).getValor().toString()) < Double.parseDouble(valor)) {
                                selecionados.add(regs.get(i));
                            }
                            break;
                        case 3:
                            if (Double.parseDouble(regs.get(i).getAtributo(posicao).getValor().toString()) >= Double.parseDouble(valor)) {
                                selecionados.add(regs.get(i));
                            }
                            break;
                        case 4:
                            if (Double.parseDouble(regs.get(i).getAtributo(posicao).getValor().toString()) <= Double.parseDouble(valor)) {
                                selecionados.add(regs.get(i));
                            }
                            break;
                        case 5:
                            if (Double.parseDouble(regs.get(i).getAtributo(posicao).getValor().toString()) == Double.parseDouble(valor)) {
                                selecionados.add(regs.get(i));
                            }
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Tipos incompatíveis, tente novamente.");
                selecionados = null;
            }
        }
        return selecionados;

    }

    public void imprimirRegistro(ArrayList<Registro> reg) {

        if (!reg.isEmpty()) {
            for (int j = 0; j < reg.size(); j++) {
                for (int i = 0; i < estruturaTabela.getAtributos().size(); i++) {

                    System.out.print(estruturaTabela.getAtributo(i).getNome() + " = " + reg.get(j).getAtributo(i).getValor() + "\t");

                }
                System.out.println();
            }
        }
    }
    
    // Retorna uma instância de tabela hash de acordo com o tipo de chave da tabela. 
    public TabelaHash selecionarTipoTabelaHash() throws IOException, FileNotFoundException, ClassNotFoundException {

        String tipoChave = estruturaTabela.getTipoChave().toUpperCase();
        switch (tipoChave) {
            case "INTEIRO":
                TabelaHash<Integer> TH1 = new TabelaHash<Integer>(nomeTabela, estruturaTabela.getTamanhoRegistroTabela());
                return TH1;
            case "DOUBLE":
                TabelaHash<Double> TH2 = new TabelaHash<Double>(nomeTabela, estruturaTabela.getTamanhoRegistroTabela());
                return TH2;
            case "STRING":
                TabelaHash<String> TH3 = new TabelaHash<String>(nomeTabela, estruturaTabela.getTamanhoRegistroTabela());
                return TH3;
        }

        return null;
    }
}
