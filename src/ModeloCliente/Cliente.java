package ModeloCliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

    private String host;
    private int porta;
    private boolean status = false;
    private Socket cliente;
    private PrintStream saida;
    private String comandoEntrada = "";
    private boolean statusLogin = false;
  

    // Construtor ()
    public Cliente() throws UnknownHostException, IOException {

    }
    // Metodo conecta na host e porta e retorna o PrintStream (Saida padrao do socket)

    public boolean conecta() throws UnknownHostException, IOException {
        // Constroi e instancia Socket
        cliente = new Socket(this.host, this.porta);

        // Thread para receber mensagens do servidor (InputStream e Cliente)
        ThreadClienteEntrada ThreadCli = new ThreadClienteEntrada(cliente.getInputStream(), this);
        new Thread(ThreadCli).start();

        // l� msgs vinda do servidor
        this.saida = new PrintStream(cliente.getOutputStream());
        status = true;// true == conectado
       
        return status;
    }
    // Metodo desconecta do socket

    public void desconecta() throws IOException {
        cliente.close();  // Fecha socket 
        status = false;	// false == desconectado
        saida.close();	// Fecha o PrintStream (Saida padrao)
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getComandoEntrada() {
        return comandoEntrada;
    }

    public void setComandoEntrada(String comandoEntrada) {
        this.comandoEntrada = comandoEntrada;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PrintStream getSaida() {
        return saida;
    }

    public void setSaida(PrintStream saida) {
        this.saida = saida;
    }

    /**
     * @return the statusLogin
     */
    public boolean isStatusLogin() {
        return statusLogin;
    }

    /**
     * @param statusLogin the statusLogin to set
     */
    public void setStatusLogin(boolean statusLogin) {
        this.statusLogin = statusLogin;
    }

}
