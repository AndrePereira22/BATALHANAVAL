package ModeloCliente;

import java.io.InputStream;
import java.util.Scanner;

public class ThreadClienteEntrada implements Runnable {

    private InputStream inputCliente;
    private Cliente cliente;

    public ThreadClienteEntrada(InputStream inputCliente, Cliente cliente) {
        this.inputCliente = inputCliente;
        this.cliente = cliente;
        
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        Scanner s = new Scanner(this.inputCliente);
        try {
            while (s.hasNextLine()) {
                cliente.setComandoEntrada(s.nextLine());

            }
        } catch (Exception e) {
            s.close();
        }
    }
}
