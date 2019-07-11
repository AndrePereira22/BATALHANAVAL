package Jogo;

public class Posicao {

    private int posicao;
    private String nome;

    public Posicao(int valor,String name) {
        this.posicao = valor;
        this.nome = name;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

}
