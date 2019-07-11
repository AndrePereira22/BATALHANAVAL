package Jogo;

import java.util.ArrayList;
import java.util.List;

public class Jogador {

    private int id = 0;
    private String nome;
    private String senha;
    private int numEscolhas = 0;
    private ArrayList<Navio> navios;
private int conexao;
    private int vida = 10;
    private int numeroNavios = 5;

    private boolean vez;
    private List<Posicao> posicaoAtaque;
    private ArrayList<Navio> herois;

    public Jogador() {
        this.nome = "";
        this.senha = "";
        vez = false;
        posicaoAtaque = new ArrayList<Posicao>();
        herois = new ArrayList<Navio>();
        navios = new ArrayList<Navio>();

    }

    public Jogador(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
        vez = false;
        posicaoAtaque = new ArrayList<Posicao>();
        herois = new ArrayList<Navio>();
        navios = new ArrayList<Navio>();

    }

    public Jogador(String nome, String senha,int  conexao) {
        this.nome = nome;
        this.senha = senha;
        this.conexao=conexao;
        vez = false;
        posicaoAtaque = new ArrayList<Posicao>();
        herois = new ArrayList<Navio>();
        navios = new ArrayList<Navio>();
    }
    
    public Navio verificaHeroi(int posicao, ArrayList<Navio> navios) {

        Navio aux = null;
        for (int i = 0; i < getNavios().size(); i++) {

            Navio n = getNavios().get(i);

            for (int j = 0; j < n.getPosicao()[j]; j++) {

                if (n.getPosicao()[j] == posicao) {
                    aux = n;

                }
            }
        }
        return aux;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isVez() {
        return vez;
    }

    public void setVez(boolean vez) {
        this.vez = vez;
    }

    public int getNumEscolhas() {
        return numEscolhas;
    }

    public void setNumEscolhas(int numRodadas) {
        this.numEscolhas = numRodadas;
    }

    public int getVida() {
        return this.vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getNumeroNavios() {
        return numeroNavios;
    }

    public void setNumeroNavios(int numeroNavios) {
        this.numeroNavios = numeroNavios;
    }

    /**
     * @return the submarinos
     */
    public ArrayList<Navio> getNavios() {
        return navios;
    }

    /**
     * @return the posicaoAtaque
     */
    public List<Posicao> getPosicaoAtaque() {
        return posicaoAtaque;
    }

    /**
     * @return the herois1
     */
    public ArrayList<Navio> getHerois() {
        return herois;
    }

    public boolean verificaPosicaoHerois(int posicao, ArrayList<Navio> navios) {
        boolean aux = false;

        for (int i = 0; i < navios.size(); i++) {

            if (navios.get(i).getPosition() == posicao) {
                aux = true;
            }

        }
        return aux;
    }

    public void setHerois(ArrayList<Navio> herois) {
        this.herois = herois;
    }

    /**
     * @return the conexao
     */
    public int getConexao() {
        return conexao;
    }

    /**
     * @param conexao the conexao to set
     */
    public void setConexao(int conexao) {
        this.conexao = conexao;
    }

}
