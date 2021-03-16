package Trabalho;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class TabelaHash<TipoChave> {
    
    private RandomAccessFile arqPonteiros;
    private RandomAccessFile arqRegistros;
    private Registro registro;
    private int tamanhoRegistro;
    private int ponteiroArqPonteiros; // ponteiro para uma posição no arquivo de ponteiros calculada pela função hash.
    private int ponteiroArqRegistros; // ponteiro para um endereço no aqruivo de registros.
    private int endereçoUltimoDoBloco; // endereço do ultimo registro do bloco de registros com mesmo endereços calculados pela função hash .
    private int endereçoProximoMeio = -1; // endereço no arquivo de registros de algum registro que foi removido (boolean ocupado = false).
    private int endereçoArqPonteiros; // ponteiro para o aquivo de registros no aquivo de ponteiros.
    private boolean registroInserido = true;
    
    // Construtor abre os aruivos de ponteiros e registros e inicializa o arquivo de ponteiros caso necessário
    public TabelaHash(String nomeTabela, int tamanhoRegistro) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.arqPonteiros = new RandomAccessFile("ponteiro" + nomeTabela + ".dat", "rw");
        this.arqRegistros = new RandomAccessFile(nomeTabela + ".dat", "rw");
        this.tamanhoRegistro = tamanhoRegistro;
        if (arqPonteiros.length() == 0) {
            inicializaArqPonteiros();
        }
    }
   
    // Busca um registro na tabela, modificando os ponteiros.
    private boolean buscaRegistro(TipoChave chave, int posicaoChave) throws IOException, ClassNotFoundException {
        
        // inicializa os ponteiros
        this.endereçoUltimoDoBloco = -1;
        this.endereçoProximoMeio = -1;
        
        // converte a chave pra inteiro
        int ch;
        if (chave.getClass().getName().equals("java.lang.String")) {
            ch = converteString(chave);
        } else {
            Double d = Double.parseDouble(chave.toString());
            ch = d.intValue();
        }

        Atributo atributo;
        ponteiroArqPonteiros = (hash(ch) * 4); // calcula a posição no arquivo de ponteiros
        this.arqPonteiros.seek(ponteiroArqPonteiros); // avança até essa posição
        endereçoArqPonteiros = this.arqPonteiros.readInt(); // Lê o endereço do arquivo de registro
        ponteiroArqRegistros = endereçoArqPonteiros;
        
            while ((ponteiroArqRegistros != -1)) {

                registro = leRegistro(ponteiroArqRegistros); // Lê o registro no endereço

                if (registro.isOcupado()) { // Se estiver com flag ocupado, verifica se achave é igual a chave procurada
                    atributo = registro.getAtributo(posicaoChave);
                    TipoChave chave1 = (TipoChave) atributo.getValor();
                    if (chave.toString().equalsIgnoreCase(chave1.toString())) { // Se a chave for igual retorna true
                        return true;
                    }
                } else {
                    this.endereçoProximoMeio = ponteiroArqRegistros; // guarda o endereço do registro com flag livre
                }
                this.endereçoUltimoDoBloco = ponteiroArqRegistros; // guarda o endereço do último registro lido
                ponteiroArqRegistros = registro.getPonteiro(); // pega o ponteiro pro próximo registro
            }

            return false; // retorna false se não achou
    }

    private Registro leRegistro(int endereçoRegistro) throws IOException, ClassNotFoundException {

        Registro reg = null;
        this.arqRegistros.seek(endereçoRegistro); // avança para o endereço do registro

        byte[] vetorBytesObjeto = new byte[tamanhoRegistro]; //cria umm vetor de bytes pra armazenar o registro
        arqRegistros.read(vetorBytesObjeto); // le o registro
        ByteArrayInputStream bis = new ByteArrayInputStream(vetorBytesObjeto);
        ObjectInputStream in = new ObjectInputStream(bis);

        reg = (Registro) in.readObject();
        in.close();

        return reg;

    }

    public void salvaRegistro(Registro reg, TipoChave chave, int posicaoChave) throws IOException, ClassNotFoundException {

        if (buscaRegistro(chave, posicaoChave)) {
            
            this.registroInserido = false;
            System.out.println("Registro de chave " + chave + " não inserido. Chave já existente.");

        } else {
            this.registroInserido = true;
            if (endereçoProximoMeio != -1) { // Verifica se tem um registro com flag livre entre os registros do bloco
                registro = leRegistro(endereçoProximoMeio);
                reg.setPonteiro(registro.getPonteiro());// atualiza o ponteiro do registro que vai ser inserido, botando o ponteiro do registro que estava no endereço
                arqRegistros.seek(endereçoProximoMeio);
            } else {
                if (endereçoArqPonteiros == -1) { // Verifica se não existe nenhum registro com mesmo valor da função hash já armazenado
                    arqPonteiros.seek(ponteiroArqPonteiros);
                    this.arqPonteiros.writeInt((int) arqRegistros.length()); //Atualiza o ponteiro no arquivo de ponteiros
                    this.arqRegistros.seek((int) arqRegistros.length());
                } else {
                    registro = leRegistro(endereçoUltimoDoBloco); // insere no fim do arquivo de registros e atualiza os ponteiros
                    registro.setPonteiro((int) arqRegistros.length());
                    arqRegistros.seek(endereçoUltimoDoBloco);
                    armazenaArquivo(registro);
                    arqRegistros.seek(arqRegistros.length());
                }
            }

            armazenaArquivo(reg);

        }
    }

    private int hash(int chave) {

        int endereço = chave % 16;

        return endereço;
    }

    public void fecharArquivos() throws IOException {
        arqPonteiros.close();
        arqRegistros.close();
    }
    
    // Grava o registro no arquivo
    private void armazenaArquivo(Registro reg) throws IOException {

        ByteArrayOutputStream objetoBytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(objetoBytes);
        out.writeObject(reg);
        out.close();
        arqRegistros.write(objetoBytes.toByteArray());

    }
    
    // inicializa o arquivo de ponteiros com -1
    private void inicializaArqPonteiros() throws IOException {
        for (int i = 0; i < 16; i++) {
            arqPonteiros.writeInt(-1);
        }
    }

    public Registro recuperaRegistro(TipoChave chave, EstruturaTabela estruturaTabela) throws IOException, ClassNotFoundException {
        if (!buscaRegistro(chave, estruturaTabela.getPosicaoChaveVetor())) { 
            System.out.println("Registro não existe.");
            return null;
        } else {
            return registro;
        }
    }

    private int converteString(TipoChave chave) {
        String s = chave.toString();
        int valor = 0;
        for (int i = 0; i < s.length(); i++) {
            valor += (int) s.charAt(i);
        }
        return valor;
    }

    public void excluirRegistro(TipoChave chave, int posicaoVetorChave) throws IOException, ClassNotFoundException {

        if (!buscaRegistro(chave, posicaoVetorChave)) {
            System.out.println("Registro não existe");
        } else {
            registro.setOcupado(false); // marca como ocupado o registro excluido
            arqRegistros.seek(ponteiroArqRegistros);
            armazenaArquivo(registro);
            
        }
    }
    
    public void atualizaRegistro(Registro reg, TipoChave chave, int posicaoVetorChave) throws IOException, ClassNotFoundException{
            if (!buscaRegistro(chave, posicaoVetorChave)) {
            System.out.println("Registro não existe");
        } else {
            registro = reg; // salva o registro atualizado
            arqRegistros.seek(ponteiroArqRegistros);
            armazenaArquivo(registro);
            
        }
    }
    
    public void setTamanhoRegistro(int tamanhoRegistro) {
        this.tamanhoRegistro = tamanhoRegistro;
    }
    
    public ArrayList<Registro> leTodosOsRegistros() throws IOException, ClassNotFoundException {
        ArrayList<Registro> registros = new ArrayList<Registro>();
        Registro r = null;
        int j = 0;
        boolean EOR = false;
        do {
            try {
                r = leRegistro(j * tamanhoRegistro);
                if (r.isOcupado()) {
                    registros.add(r);
                }
                j++;
            } catch (IOException e) {
                EOR = true;
            }
        } while (!EOR);
        return registros;
    }

    public boolean isRegistroInserido() {
        return registroInserido;
    }
    
    
}

