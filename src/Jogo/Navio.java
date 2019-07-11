package Jogo;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Navio {

    private String nome;
    private int[] posicao;
    private int vida;
    private boolean vivo;
    private int position;
  

    public Navio(String nome, int vida,int position) {

        this.nome = nome;
        this.vida = vida;
        this.vivo = true;
         this.position = position;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * @return the posicao
     */
    public int[] getPosicao() {
        return posicao;
    }

    /**
     * @param posicao the posicao to set
     */
    public void setPosicao(int[] posicao) {
        this.posicao = posicao;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    
    
}
