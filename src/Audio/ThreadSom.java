package Audio;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;

/**
 * Batalha Naval (Projeto GeracaoTec) - Versao 2.2 de Interface Grafica Classe:
 * Thread Monitora Telas Thread (Processo Filho) que monitora o objeto Jogador e
 * todas Sub-Telas da Tela Principal (JPanel), se orienta principalmente pelo o
 * numero de rodadas para alterar a condicao dos componentes fazendo a iteracao
 * entre eles, mas tambem utiliza outras FLAGs. Assim as Telas podem se
 * relacionar com mais facilidade sem interromper nenhuma funcionamento enquanto
 * aguardam por alguma condicao... Autor: Henrique Wilhelm
 */
public class ThreadSom extends Thread {

    Trilha trilha;
    int i;

    public ThreadSom(int valor) {
        this.i = valor;
        start();
    }
    // Metodo que executa start() a Thread (Processo Filho)

    public void run() {
        try {
            trilha = new Trilha(i);
        } catch (FileNotFoundException | JavaLayerException ex) {
            Logger.getLogger(ThreadSom.class.getName()).log(Level.SEVERE, null, ex);
        }
        int contador=0;
        try {
            while (contador<100) {
                contador++;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
