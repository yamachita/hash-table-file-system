package Trabalho;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Tabela {

    private String nomeTabela;
    private Scanner teclado;
    private Catalogo catalogo;
    private EstruturaTabela estruturaTabela;

    public Tabela(String nomeTabela) throws IOException {
        this.nomeTabela = nomeTabela;
        catalogo = new Catalogo();
        teclado = new Scanner(System.in);
        this.estruturaTabela = new EstruturaTabela(nomeTabela);
        if (catalogo.tabelaExistente(nomeTabela)) {
            System.out.println("Tabela já existe.");
        } else {
            criarTabela();
        }

    }

    public void criarTabela() throws IOException {

        System.out.println("Digite a quantidade de atributos: ");
        int quantidade = teclado.nextInt();
        EstruturaAtributo atributo;
        ArrayList<String> nomeAtributo = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            boolean atributoValido;
            String resp;
            do {
                System.out.println("Digite o nome do " + i + "º" + " atributo: ");
                resp = teclado.next();
                if (!verificaUnicidade(nomeAtributo, resp)) {
                    System.out.println("Já existe um atributo com esse nome, tente novamente.");
                    atributoValido = false;
                } else {
                    atributoValido = true;
                }
            } while (!atributoValido);
            nomeAtributo.add(resp);

            boolean valido;
            atributo = null;
            do {
                System.out.println("Digite o tipo do atributo " + nomeAtributo.get(i - 1) + ": ");
                String tipo = teclado.next();
                switch (tipo.toUpperCase()) {
                    case "STRING":
                        valido = true;
                        System.out.println("Digite o tamanho máximo da String: ");
                        int tamanho = teclado.nextInt();
                        atributo = new EstruturaAtributo(nomeAtributo.get(i - 1), tipo, tamanho);
                        break;
                    case "INTEIRO":
                    case "DOUBLE":
                        valido = true;
                        atributo = new EstruturaAtributo(nomeAtributo.get(i - 1), tipo);
                        break;
                    default:
                        System.out.println("Tipo inválido, tente novamente!");
                        valido = false;
                }
            } while (!valido);
            estruturaTabela.add(atributo);
        }
        boolean chaveValida;
        String chave;
        do {
            System.out.println("Digite a chave da tabela: ");
            chave = teclado.next();
            if (verificaUnicidade(nomeAtributo, chave)) {
                System.out.println("A chave digitada não coincide com nenhum dos atributos criados.");
                chaveValida = false;
//                teclado.nextLine(); +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            } else {
                chaveValida = true;
            }
        } while (!chaveValida);
        estruturaTabela.setChave(chave);
        estruturaTabela.setTamanhoRegistroTabela(calculaTamanhoRegistroNoCatalogo());
        catalogo.salvarTabelaCatalogo(estruturaTabela);
    }

    private int calculaTamanhoRegistroNoCatalogo() throws IOException {
        Registro r = new Registro();
        for (int i = 0; i < estruturaTabela.getAtributos().size(); i++) {
            String tipo = estruturaTabela.getAtributo(i).getTipo().toUpperCase();
            switch (tipo) {
                case "INTEIRO":
                    r.addAtributo(new Atributo<Integer>(Integer.MAX_VALUE));
                    break;
                case "DOUBLE":
                    r.addAtributo(new Atributo<Double>(Double.MAX_VALUE));
                    break;
                case "STRING":
                    String s = new String();
                    for (int j = 0; j < estruturaTabela.getAtributo(i).getTamanhoMaximo(); j++) {
                        s += " ";
                    }
                    r.addAtributo(new Atributo<String>(s));
                    break;
            }
        }
        return calculaTamanhoRegistro(r);
    }

    private int calculaTamanhoRegistro(Registro r) throws IOException {
        ByteArrayOutputStream objetoBytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(objetoBytes);
        out.writeObject(r);
        out.close();
        int i = objetoBytes.toByteArray().length;
        return i;
    }
    
    // Verifica se já existe um atributo com esse nome
    private boolean verificaUnicidade(ArrayList<String> nomeAtributo, String s) {
        for (int i = 0; i < nomeAtributo.size(); i++) {
            if (s.equalsIgnoreCase(nomeAtributo.get(i))) {
                return false;
            }
        }
        return true;
    }
}
