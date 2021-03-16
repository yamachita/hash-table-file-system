package Trabalho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


// Classe que manipula o catálogo.
public class Catalogo {
    
    
    //Método que lê do catalálogo e retorna uma estruturaTabela.
    public EstruturaTabela lerTabelaCatalogo(String nome) throws IOException {

        EstruturaTabela novaTabela = new EstruturaTabela();
        String[] estrutura = getTabela(nome);

        if (estrutura == null) {
            return null;
        }
        EstruturaAtributo atribuuto;
        novaTabela.setNome(estrutura[0]);

        for (int i = 1; i < estrutura.length - 3; i = i + 2) {
            atribuuto = new EstruturaAtributo();
            atribuuto.setNome(estrutura[i]);
            atribuuto.setTipo(estrutura[i + 1]);
            if (atribuuto.getTipo().equalsIgnoreCase("string")) {
                atribuuto.setTamanhoMaximo(Integer.parseInt(estrutura[i + 2]));
                i++;
            }
            novaTabela.add(atribuuto);
        }

        novaTabela.setTamanhoRegistroTabela(Integer.parseInt(estrutura[estrutura.length - 2]));
        novaTabela.setChave(estrutura[estrutura.length - 1]);

        return novaTabela;

    }
    
    // Método que retorna uma linha do catálogo represetando uma tabela.
    public String[] getTabela(String nome) throws IOException {
        FileReader f = null;
        BufferedReader catalogo = null;
        try {
            f = new FileReader("catalogo.txt");
            catalogo = new BufferedReader(f);
            boolean achou = false;
            String tabela = "";
            String[] estrutura = null;

            while (!achou) {
                tabela = catalogo.readLine();
                estrutura = tabela.split("@");
                String nomeTabela = estrutura[0];

                if (nomeTabela.equalsIgnoreCase(nome)) {
                    achou = true;
                } else {
                    achou = false;
                }
            }

            return estrutura;

        } catch (Exception e) {
            System.out.println("Tabela não existe");
            return null;
        } finally {
            if (f != null) {
                f.close();
            }
            if (catalogo != null) {
                catalogo.close();
            }
        }

    }

    public void salvarTabelaCatalogo(EstruturaTabela tabela) throws IOException {

        FileWriter f = new FileWriter("catalogo.txt", true);
        BufferedWriter b = new BufferedWriter(f);
        b.write(tabela.getNome() + "@");
        for (EstruturaAtributo a : tabela.getAtributos()) {
            b.write(a.getNome() + "@");
            b.write(a.getTipo() + "@");
            if (a.getTipo().equalsIgnoreCase("string")) {
                b.write(a.getTamanhoMaximo() + "@");
            }
        }

        String tamanho = String.valueOf(tabela.getTamanhoRegistroTabela());
        b.write(tamanho + "@");
        b.write(tabela.getChave());
        b.newLine();
        b.close();
        f.close();
    }

    public void imprimeEstruturaTabela(String[] estrutura) {
        System.out.println("\nNome: " + estrutura[0]);
        String atributos = "";
        for (int i = 1; i < estrutura.length - 3; i = i + 2) {
            atributos = atributos + estrutura[i] + " ("
                    + estrutura[i + 1];
            if (estrutura[i + 1].equalsIgnoreCase("string")) {
                atributos += " tam. máx. " + estrutura[i + 2];
                i++;
            }
            atributos += ") ";
        }
        System.out.println("Atributos: " + atributos);
        System.out.printf("%s: %s\n", "Chave da tabela",
                estrutura[estrutura.length - 1]);
    }

    public void exibirNomeTabelas() throws IOException {
        FileReader f = null;
        BufferedReader catalogo = null;
        boolean EOF = false;
        try {
            f = new FileReader("catalogo.txt");
            catalogo = new BufferedReader(f);
            String tabela = "";
            String[] estrutura = null;

            while (!EOF) {
                tabela = catalogo.readLine();
                estrutura = tabela.split("@");
                String nomeTabela = estrutura[0];
                System.out.println(nomeTabela);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Não existem tabelas cadastradas.");
        } catch (Exception e) {
            EOF = true;
        } finally {
            if (f != null) {
                f.close();
            }
            if (catalogo != null) {
                catalogo.close();
            }
        }
    }

    public boolean tabelaExistente(String nome) {

        boolean continuar = true;
        try {
            FileReader f = new FileReader("catalogo.txt");
            BufferedReader catalogo = new BufferedReader(f);
            String tabela = "";
            String[] estrutura = null;

            while (continuar) {
                tabela = catalogo.readLine();
                estrutura = tabela.split("@");
                String nomeTabela = estrutura[0];

                if (nomeTabela.equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        } catch (Exception e) {
            continuar = false;
        }

        return false;
    }

    public void excluirTabela(String nome) throws IOException {
        boolean achou = false;
        ArrayList<String> nomeTabs = new ArrayList<>();
        FileReader f = null;
        BufferedReader catalogo = null;
        try {
            f = new FileReader("catalogo.txt");
            catalogo = new BufferedReader(f);
            String linha;
            while (true) {
                linha = catalogo.readLine();
                String nomeTab = linha.substring(0, linha.indexOf("@"));
                if (!nomeTab.equalsIgnoreCase(nome)) {
                    nomeTabs.add(nomeTab);
                } else {
                    achou = true;
                }
            }
        } catch (Exception e) {
            //EOF
        } finally {
            if (f != null) {
                f.close();
            }
            if (catalogo != null) {
                catalogo.close();
            }
        }

        if (achou) {
            ArrayList<EstruturaTabela> tabelas = new ArrayList<>();
            for (int i = 0; i < nomeTabs.size(); i++) {
                tabelas.add(lerTabelaCatalogo(nomeTabs.get(i)));
            }
            File cat = new File("catalogo.txt");
            cat.delete();
            for (int i = 0; i < tabelas.size(); i++) {
                salvarTabelaCatalogo(tabelas.get(i));
            }
        } else {
            System.out.println("A tabela não existe.");
        }
    }
}
