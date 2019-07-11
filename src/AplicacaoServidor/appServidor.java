package AplicacaoServidor;

import Jogo.Jogador;
import ModeloServidor.TelaServidor;
import ModeloServidor.ThreadServidorEntrada;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class appServidor {

    private volatile static TelaServidor telaServidor;
    private int porta;
    private List<PrintStream> clientes;// Arraylist de PrintStream (Saida Padrao do Server/Socket)
    private PrintStream saida;			// Saida Padrao do Server/Socket
    private ServerSocket servidor;
    private int numeroConexao = 0;
    private Jogador player1;
    private Jogador player2;
    private List<Jogador> lista;

    // Construtor (Recebe porta)
    public appServidor(int porta) {
        this.porta = porta;
        this.clientes = new ArrayList<PrintStream>();
        this.lista = new ArrayList<Jogador>();
    }
    // Metodo conecta na host e porta e retorna o PrintStream (Saida padrao do socket)

    public void conecta() throws IOException {
        try {
            // Cria e instancia ServerSocket (Passando a Porta)
            servidor = new ServerSocket(this.porta);
            // Escreve no Console
            System.out.println("* Servidor BATALHA (Batalha Naval) - Porta " + this.porta + " aberta...");
            this.setPlayer1(new Jogador());
            this.setPlayer2(new Jogador());
            // Esperando clientes	
            while (true) {
                if (numeroConexao < 6) {
                    // Aceita um cliente
                    Socket cliente = servidor.accept();
                    System.out.println("- Handshake realizado - Conexao numero: " + numeroConexao + " Endereco do Cliente: " + cliente.getInetAddress().getHostAddress());
                    // Le msgs vindas do cliente e adiconando ao ArrayList de Saida Padrao do Server/Socket
                    saida = new PrintStream(cliente.getOutputStream());
                    this.clientes.add(numeroConexao, saida);

                    // Thread para receber mensagens do cliente (InputStream e Servidor)
                    ThreadServidorEntrada ThreadServ = new ThreadServidorEntrada(cliente.getInputStream(), this, numeroConexao, this.player1, this.player2);
                    new Thread(ThreadServ).start();

                }
                numeroConexao++;

            }
        } catch (IOException e) {
            servidor.close();
        }

    }

    public void desconecta() throws IOException {
        broadCast("$QUIT");
        servidor.close();
    }
    // Envia mensagem a todos clientes conectados;

    public void broadCast(String msg) {
        // Envia mensagem para todos
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg);
        }
    }
    public void broadCastJogadores() {

        for (Jogador j : lista) {
            for (PrintStream cliente : this.clientes) {
                cliente.println("."+j.getNome());

            }
        }
    }
    // Envia mensagem para cliente especifico
    public void uniCast(int index, String msg) {
        // Envia mensagem para cliente especifico
        clientes.get(index).println(msg);
    }
    // Getters and Setters

    public int getNumeroConexao() {
        return numeroConexao;
    }

    public void setNumeroConexao(int numeroConexao) {
        this.numeroConexao = numeroConexao;
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        // inicia o servidor

        telaServidor = new TelaServidor();

        telaServidor.getBotaoIniciar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int porta = Integer.parseInt(telaServidor.getTxtServidor().getText());
                    new appServidor(porta).conecta();

                } catch (IOException ex) {
                    Logger.getLogger(appServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    /**
     * @return the player1
     */
    public Jogador getPlayer1() {
        return player1;
    }

    /**
     * @param player1 the player1 to set
     */
    public void setPlayer1(Jogador player1) {
        this.player1 = player1;
    }

    /**
     * @return the player2
     */
    public Jogador getPlayer2() {
        return player2;
    }

    /**
     * @param player2 the player2 to set
     */
    public void setPlayer2(Jogador player2) {
        this.player2 = player2;
    }

    /**
     * @return the lista
     */
    public List<Jogador> getLista() {
        return lista;
    }

}
