package AplicacaoCliente;

import Controle.Controle;
import Jogo.Jogador;
import ModeloCliente.Cliente;
import ModeloCliente.TelaCliente;
import java.io.IOException;
import javax.swing.WindowConstants;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class appCliente {

    private static Cliente cliente;
    private static Jogador jogador;

    public static void main(String args[]) throws UnknownHostException, IOException {

        // Cria e instancia Classe de Comunicacao de Dados 
        setCliente(new Cliente());
        // Cria Jogador
        setJogador(new Jogador());

        // Cria Janela
        TelaCliente tela = new TelaCliente();
        tela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Aponta o processador para thread.
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(
        new Controle(getJogador(),getCliente(),tela));

    }

    /**
     * @return the cliente
     */
    public static Cliente getCliente() {
        return cliente;
    }

    /**
     * @param aCliente the cliente to set
     */
    public static void setCliente(Cliente aCliente) {
        cliente = aCliente;
    }

    /**
     * @return the jogador
     */
    public static Jogador getJogador() {
        return jogador;
    }

    /**
     * @param aJogador the jogador to set
     */
    public static void setJogador(Jogador aJogador) {
        jogador = aJogador;
    }

}
