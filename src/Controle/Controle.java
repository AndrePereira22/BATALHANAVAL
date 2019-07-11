package Controle;

import Audio.ThreadSom;
import Jogo.Jogador;
import Jogo.Navio;
import ModeloCliente.Cliente;
import ModeloCliente.TelaCliente;
import Visao.DiaConfirm;
import Visao.Tabuleiro;
import Visao.TelaChat;
import Audio.Trilha;
import Visao.DiaLooser;
import Visao.DiaWinner;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author Andre-Coude
 */
public class Controle implements Runnable {

    private Jogador jogador;
    private Cliente cliente;
    private TelaChat telaChat;
    private TelaCliente tela;
    private Tabuleiro tabuleiro;
    private Trilha trilha;
    private final DiaConfirm mens;
    private int numeroConexao;
    private boolean enviado = false;
    private boolean cadastro = false;
    private boolean over = false;
    private int contadorAtaque = 0;
    private List<Jogador> lista;
    private DiaWinner venceu;
    private DiaLooser perdeu;

    public Controle(Jogador jogador, Cliente cliente, TelaCliente tela) throws FileNotFoundException {

        this.jogador = jogador;
        this.cliente = cliente;
        this.tela = tela;
        this.tabuleiro = tela.getTabuleiro();
        this.mens = new DiaConfirm(tela.getTabuleiro(), true);
        this.telaChat = tabuleiro.getTelaChat();
        this.lista = new ArrayList<Jogador>();
        venceu = new DiaWinner(tabuleiro, true);
        perdeu = new DiaLooser(tabuleiro, true);

        try {
            this.trilha = new Trilha(2);
            trilha.stop();
        } catch (JavaLayerException ex) {
            Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        String aux = "";

        while (true) {
            System.out.flush();
            try {
                verificarSubmarinos();

                if (cliente.isStatus()) {
                    String comandoEntrada = cliente.getComandoEntrada();

                    if (!comandoEntrada.equals(aux)) {
                        char[] protocolo;
                        protocolo = comandoEntrada.toCharArray();

                        switch (protocolo[0]) {
                            case '$':                //protocolo de login
                                if (!cliente.isStatusLogin()) {
                                    cliente.getSaida().println("$" + tela.getTxtLogin().getText());
                                    Thread.sleep(250);
                                    if (cliente.getComandoEntrada().equals("$OK1")) {
                                        jogador.setNome(tela.getTxtLogin().getText());
                                        tabuleiro.getpJogador().getNOME().setText(jogador.getNome());
                                    }
                                    cliente.getSaida().println("$" + tela.getTxtSenha().getText());
                                    Thread.sleep(250);
                                    if (cliente.getComandoEntrada().equals("$OK2")) {
                                        tela.getBtnEntrar().setEnabled(false);
                                        tela.getBotaoDesconecta().setEnabled(true);
                                        jogador.setNome(tela.getTxtLogin().getText());
                                        tabuleiro.getpJogador().getNOME().setText(jogador.getNome());
                                        tela.getBuffer().setVisible(true);
                                        Thread.sleep(1000);
                                        cliente.setStatusLogin(true);
                                        tabuleiro.getpJogador().getNOME().setText(jogador.getNome());
                                        tela.getTabuleiro().setVisible(true);

                                        cliente.getSaida().println("<");

                                    }
                                    if (cliente.getComandoEntrada().equals("$OK3")) {
                                        JOptionPane.showMessageDialog(null, "ERRO NO LOGIN");
                                    }
                                }
                                break;

                            case ':':  //protocolo de cadastro

                                if (!cadastro) {
                                    cliente.getSaida().println(":" + tela.getTxtLogin().getText());
                                    Thread.sleep(500);
                                    if (cliente.getComandoEntrada().equals(":OK1")) {
                                        cliente.getSaida().println(":" + tela.getTxtSenha().getText());
                                    }
                                    Thread.sleep(500);
                                    if (cliente.getComandoEntrada().equals(":OK2")) {
                                        JOptionPane.showMessageDialog(null, "CADASTRO REALIZADO");
                                        Thread.sleep(2000);
                                        cadastro = true;
                                    }
                                    if (cliente.getComandoEntrada().equals("$OK3")) {
                                        JOptionPane.showMessageDialog(null, "ERRO NO CADASTRO");

                                    }
                                }

                                break;

                            case '<': //protocolo receber o numero da conexao
                                numeroConexao = leIntProtocolo(protocolo);
                                tabuleiro.exibirEscolhaNavios(numeroConexao);
                                cliente.getSaida().println(";");

                                break;

                            case '.':  //

                                String nome = leStringProtocolo(protocolo);
                                if (a(nome)) {
                                    lista.add(new Jogador(nome, ""));
                                    listarUsers(nome);
                                }

                                break;

                            case '!':

                                contadorAtaque++;
                                int posicao = pegarPosicao(comandoEntrada);
                                tabuleiro.getButtonsTab1()[posicao].setEnabled(true);
                                tabuleiro.getButtonsTab1()[posicao].setFundo(2);
                                tabuleiro.getButtonsTab1()[posicao].setEnabled(false);
                                new ThreadSom(4);
                                retirarVida(numeroConexao);
                                if (contadorAtaque == 3) {
                                    tabuleiro.getJogador().setVez(true);
                                    jogador.setVez(true);
                                    contadorAtaque = 0;
                                }
                                break;

                            case '/':    //protocolo mensagens par iniciar
                                if (leStringProtocolo(protocolo).equals("ini")) {
                                    tabuleiro.getJogador().setVez(true);
                                    jogador.setVez(true);
                                    mens.setVisible(true);

                                }
                                break;

                            case '+': //protocolo ataque sofrido, acerto. 

                                int p = pegarPosicao(comandoEntrada);

                                tabuleiro.getButtonsTab2()[p].setEnabled(true);
                                tabuleiro.getButtonsTab2()[p].setFundo(2);
                                tabuleiro.getButtonsTab2()[p].setEnabled(false);

                                break;

                            case '-': //protocolo ataque .muda imagem botao
                                int q = pegarPosicao(comandoEntrada);

                                tabuleiro.getButtonsTab2()[q].setEnabled(true);
                                tabuleiro.getButtonsTab2()[q].setFundo(3);
                                tabuleiro.getButtonsTab2()[q].setEnabled(false);

                                break;

                            case '&': //protocolo ataque sofrido/ muda imagem botao
                                int po = pegarPosicao(comandoEntrada);
                                contadorAtaque++;
                                tabuleiro.getButtonsTab1()[po].setEnabled(true);
                                tabuleiro.getButtonsTab1()[po].setFundo(3);
                                tabuleiro.getButtonsTab1()[po].setEnabled(false);
                                new ThreadSom(3);
                                if (contadorAtaque == 3) {
                                    tabuleiro.getJogador().setVez(true);
                                    jogador.setVez(true);
                                    contadorAtaque = 0;
                                }
                                break;
                            case '0': //protocolo perdi a partida
                                perdeu.setVisible(true);
                                tabuleiro.setVisible(false);
                                 {
                                    try {
                                        cliente.desconecta();
                                    } catch (IOException ex) {
                                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                                break;
                            case '6': //protocolo ganhei a partida
                                venceu.setVisible(true);
                                tabuleiro.setVisible(false);
                                try {
                                    cliente.desconecta();
                                } catch (IOException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;

                            default:  //protocolo mensagens do chat 

                                telaChat.setTextAreaConversas(comandoEntrada + "\n" + telaChat.getTextAreaConversas().getText());
                                break;
                        }
                        aux = comandoEntrada;

                    }

                }
                // enviar posicoes dos barcos 
                if (jogador.getNumEscolhas() == 5 && enviado == false) {

                    if (tabuleiro.isConexao()) {
                        for (int i = 0; i < jogador.getNavios().size(); i++) {
                            for (int j = 0; j < jogador.getNavios().get(i).getPosicao().length; j++) {
                                int p = jogador.getNavios().get(i).getPosicao()[j];
                                cliente.getSaida().println("#" + p);
                            }
                        }
                        jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                        tabuleiro.getP2().setVisible(true);
                        tabuleiro.getTab().remove(1);
                        tabuleiro.getTab().add(tabuleiro.getVidaum(), 1);
                        this.trilha.stop();
                        this.trilha = new Trilha(2);
                        if (tela.isSom()) {
                            tela.getTrilha().stop();
                            tela.setSom(false);
                        }
                        tabuleiro.getpJogador().getLblAtaque().setVisible(true);
                        telaChat.setTextAreaConversas("ESTOU PRONTO!");
                        enviado = true;
                        Thread.sleep(1000);
                        cliente.getSaida().println("=");
                    }

                } else if (jogador.getNumEscolhas() == 10 && enviado == false) {
                    if (tabuleiro.isConexao() == false) {
                        for (int i = 0; i < jogador.getNavios().size(); i++) {
                            for (int j = 0; j < jogador.getNavios().get(i).getPosicao().length; j++) {
                                int p = jogador.getNavios().get(i).getPosicao()[j];
                                cliente.getSaida().println("#" + p);
                            }
                        }
                        jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                        tabuleiro.getP2().setVisible(true);
                        tabuleiro.getTab().remove(1);
                        tabuleiro.getTab().add(tabuleiro.getVidadois(), 1);

                        this.trilha.stop();
                        this.trilha = new Trilha(2);
                        if (tela.isSom()) {
                            tela.getTrilha().stop();
                            tela.setSom(false);
                        }
                        telaChat.setTextAreaConversas("ESTOU PRONTO!");
                        tabuleiro.getpJogador().getLblAtaque().setVisible(true);
                        enviado = true;
                        Thread.sleep(1000);
                        cliente.getSaida().println("=");
                    }
                }

                if (jogador.isVez()) { //envia um ataque 
                    try {
                        if (jogador.getPosicaoAtaque().size() == 3) {
                            for (int j = 0; j < jogador.getPosicaoAtaque().size(); j++) {
                                int posicao = jogador.getPosicaoAtaque().get(j).getPosicao();
                                cliente.getSaida().println("@" + posicao);
                            }
                            tabuleiro.getJogador().getPosicaoAtaque().clear();
                            jogador.setVez(false);

                        }
                    } catch (java.lang.NullPointerException e) {

                    }
                }

            } catch (HeadlessException | FileNotFoundException | InterruptedException | JavaLayerException e) {
            }

            if (!over) {
                verGameOver();
            }

        }

    }

    public void verificarSubmarinos() {    //imagens dos botoes 

        if (tabuleiro.isMarcarAlvo() == false) {
            try {
                for (int i = 0; i < jogador.getNavios().size(); i++) {

                    Navio n = jogador.getNavios().get(i);

                    for (int j = 0; j < n.getPosicao().length; j++) {
                        int posicao = n.getPosicao()[j];
                        if (tabuleiro.getNavios().isStatus()) {
                            tabuleiro.getButtonsTab1()[posicao].setEnabled(false);
                            tabuleiro.getButtonsTab1()[posicao].setFundo(1);
                        }
                    }
                }
            } catch (java.lang.NullPointerException e) {
            }
        }

        for (int i = 0; i < jogador.getPosicaoAtaque().size(); i++) {
            try {
                int posicao = jogador.getPosicaoAtaque().get(i).getPosicao();

                tabuleiro.getButtonsTab2()[posicao].setEnabled(false);
                tabuleiro.getButtonsTab2()[posicao].setFundo(4);
            } catch (java.lang.NullPointerException e) {
            }

        }

    }

    public int leIntProtocolo(char[] protocolo) {
        String palavra;
        if (protocolo.length == 3) {
            palavra = String.valueOf(protocolo[1]) + String.valueOf(protocolo[2]);
        } else {
            palavra = String.valueOf(protocolo[1]);
        }

        return Integer.parseInt(palavra);
    }

    public String leStringProtocolo(char[] protocolo) {
        char[] palavra = new char[protocolo.length - 1];
        for (int i = 0; i < palavra.length; i++) {
            palavra[i] = protocolo[i + 1];
        }
        return String.valueOf(palavra);
    }

    public int pegarPosicao(String posicao) {

        if (!posicao.isEmpty()) {
            posicao = posicao.substring(1);
        }

        int p = Integer.parseInt(posicao);
        return p;

    }

    public void listarUsers(String nome) {

        int i = 0;
        try {
            String[] colunas = new String[]{"NOME", "NUMERO DA CONEXÃO"};
            Object[][] dados = new Object[lista.size()][2];
            for (Jogador a : lista) {
                dados[i][0] = a.getNome();
                dados[i][1] = i;
                i++;
            }
            DefaultTableModel dataModel = new DefaultTableModel(dados, colunas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tela.getTabela().setModel(dataModel);
        } catch (Exception ex) {

        }
    }

    public void retirarVida(int conexao) {

        if (conexao % 2 == 0) {
            tabuleiro.getpJogador().getVidaUm().setBounds(tabuleiro.getpJogador().getVidaUm().getX(), tabuleiro.getpJogador().getVidaUm().getY(),
                    tabuleiro.getpJogador().getVidaUm().getWidth() - 10, tabuleiro.getpJogador().getVidaUm().getHeight());
        } else {
            tabuleiro.getpJogador().getVidaDois().setBounds(tabuleiro.getpJogador().getVidaDois().getX(), tabuleiro.getpJogador().getVidaDois().getY(),
                    tabuleiro.getpJogador().getVidaDois().getWidth() - 10, tabuleiro.getpJogador().getVidaDois().getHeight());

        }
    }

    public void verGameOver() {

        if (numeroConexao % 2 == 0) {
            if (tabuleiro.getpJogador().getVidaUm().getWidth() < 1) {
                over = true;
                jogador.setVez(false);
                cliente.getSaida().println("0");
            }
        } else {
            if (tabuleiro.getpJogador().getVidaDois().getWidth() < 1) {
                over = true;
                jogador.setVez(false);
                cliente.getSaida().println("0");
            }
        }

    }

    public String pegarString(String nome) {

        String aux = nome.substring(1, nome.length() - 1);

        return aux;
    }

    public boolean a(String nome) {
        boolean aux = true;
        for (Jogador j : lista) {
            if (j.getNome().equals(nome)) {
                aux = false;
            }
        }
        return aux;
    }

}
