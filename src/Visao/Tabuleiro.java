package Visao;

import Jogo.Jogador;
import Jogo.Navio;
import Jogo.Posicao;
import ModeloCliente.Cliente;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tabuleiro extends JFrame implements ActionListener {

    private JPanel tabuleiro1;
    private JPanel tabuleiro2;
    private JPanel cards;
    private Navios navios;
    private PainelJogador pJogador;
    private TelaChat telaChat;
    private JPanel telaPrincipal;
    private JPanel tab;
    private Linha lEsquerda1, lDireita1, lEsquerda2, lDireita2;
    private Coluna cCima1, cBaixo1, cCima2, cBaixo2;
    private JPanel p1, p2;
    private VidaUm vidaum;
    private vidaDois vidadois;

    private Botao buttonsTab1[] = new Botao[225];
    private Botao buttonsTab2[] = new Botao[225];

    private Jogador jogador;
    private Cliente cliente;
    private boolean MarcarAlvo = false;
    private boolean conexao;
    private int submarino = 0;
    private int encouraçado = 0;
    private int portaAvioes = 0;
    private int hidroAvioes = 0;
    private int cruzador = 0;
    private int HidroProibidos[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
        30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180, 195, 210, 211, 212, 213, 214, 215, 216,
        217, 218, 219, 220, 221, 222, 223, 224, 225};

    // Contrutor da Tela
    public Tabuleiro(Cliente cliente, Jogador jogador) {
        this.jogador = jogador;

        this.cliente = cliente;
        this.navios = new Navios();
        this.pJogador = new PainelJogador();
        this.telaPrincipal = new JPanel(new BorderLayout());
        this.cards = new JPanel(new CardLayout());
        this.tab = new JPanel(new GridLayout(1, 3, 0, 0));
        this.p1 = new JPanel(new BorderLayout());
        this.p2 = new JPanel(new BorderLayout());
        this.vidaum = new VidaUm();
        this.vidadois= new vidaDois();

        this.lDireita1 = new Linha();
        this.lEsquerda1 = new Linha();
        this.cCima1 = new Coluna();
        this.cBaixo1 = new Coluna();
        this.lDireita2 = new Linha();
        this.lEsquerda2 = new Linha();
        this.cCima2 = new Coluna();
        this.cBaixo2 = new Coluna();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1024, 400);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);

        this.setTabuleiro1(new JPanel(new GridLayout(15, 1, 0, 0)));
        this.setTabuleiro2(new JPanel(new GridLayout(15, 1, 0, 0)));

        // Adiciona os botoes (JButton) no vetor de Botoes
        for (int contador = 0; contador < 225; contador++) {
            // Criando Tabuleiro1 (Player 1)
            this.getButtonsTab1()[contador] = new Botao();
            // Criando Tabuleiro2 (Player 2)			
            this.getButtonsTab2()[contador] = new Botao();
            // Adicionando ao Manipulador de Eventos
            this.getButtonsTab1()[contador].addActionListener(this);
            this.getButtonsTab2()[contador].addActionListener(this);
            // Adiciona objetos (Botoes) na Tela (JPanel)
            this.getTabuleiro1().add(getButtonsTab1()[contador]);
            this.getTabuleiro2().add(getButtonsTab2()[contador]);
        }

        try {

            p1.add(cCima1, BorderLayout.PAGE_START);
            p1.add(tabuleiro1, BorderLayout.CENTER);
            p1.add(lEsquerda1, BorderLayout.WEST);
            p1.add(lDireita1, BorderLayout.EAST);
            p1.add(cBaixo1, BorderLayout.SOUTH);

            p2.add(cCima2, BorderLayout.PAGE_START);
            p2.add(tabuleiro2, BorderLayout.CENTER);
            p2.add(lEsquerda2, BorderLayout.WEST);
            p2.add(lDireita2, BorderLayout.EAST);
            p2.add(cBaixo2, BorderLayout.SOUTH);

            // Cria Sub-Telas 
            telaChat = new TelaChat(this.cliente);
            tab.add(p1);
            tab.add(navios);

            tab.add(p2);

            telaPrincipal.add(pJogador, BorderLayout.PAGE_START);
            telaPrincipal.add(tab, BorderLayout.CENTER);
            p2.setVisible(false);
            telaPrincipal.add(telaChat.getPanelChat(), BorderLayout.SOUTH);
            this.add(telaPrincipal);

        } catch (Exception exception) {
            System.out.println(exception);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            for (int contador = 0; contador < 225; contador++) { // Se "apertar" no Tabuleiro (Player 1)
                if (e.getSource() == getButtonsTab1()[contador]) {

                    if (navios.getRadioSubmarine().isSelected()) {

                        int posicao = contador;

                        if (submarino < 4) {

                            Navio n = new Navio("submarino", 10, 10);
                            int[] p = {posicao};
                            n.setPosicao(p);
                            n.setVivo(true);

                            jogador.getNavios().add(n);
                            jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                            submarino++;
                        }

                    }
                    if (navios.getRadioCruzador().isSelected()) {

                        int posicao = contador;

                        if (cruzador <= 2) {
                            if (verificarposicaoCruzador(posicao)) {

                                Navio n = new Navio("cruzador", 10, 10);
                                int[] p = {posicao, posicao + 15};
                                n.setPosicao(p);
                                n.setVivo(true);

                                jogador.getNavios().add(n);
                                jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                                cruzador++;
                            } else {
                                JOptionPane.showMessageDialog(null, "posicao invalida!");

                            }
                        }

                    }
                    if (navios.getRadioEncouracado().isSelected()) {

                        int posicao = contador;

                        if (encouraçado <= 1) {
                            if (verificarposicaoEncouracado(posicao)) {
                                Navio n = new Navio("encouraçado", 10, 10);
                                int[] p = {posicao, posicao + 15, posicao + 30, posicao + 45};
                                n.setPosicao(p);
                                n.setVivo(true);

                                jogador.getNavios().add(n);
                                jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                                encouraçado++;
                            } else {
                                JOptionPane.showMessageDialog(null, "posicao invalida!");

                            }
                        }

                    }
                    if (navios.getRadioPorta().isSelected()) {

                        int posicao = contador;

                        if (portaAvioes <= 0) {
                            if (verificarposicaoPorta(posicao)) {
                                Navio n = new Navio("porta avião", 10, 10);
                                int[] p = {posicao, posicao + 15, posicao + 30, posicao + 45, posicao + 60};
                                n.setPosicao(p);
                                n.setVivo(true);

                                jogador.getNavios().add(n);
                                jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                                portaAvioes++;
                            } else {
                                JOptionPane.showMessageDialog(null, "posicao invalida!");

                            }
                        }

                    }
                    if (navios.getRadioHidro().isSelected()) {

                        int posicao = contador;

                        if (hidroAvioes <= 4) {
                            if (verificarposicaoHidro(posicao)) {

                                Navio n = new Navio("hidro avião", 10, posicao);
                                int[] p = {posicao, posicao - 16, posicao + 14};
                                n.setPosicao(p);
                                n.setVivo(true);
                                //editarVida(posicao, posicao - 16, posicao + 14);

                                jogador.getNavios().add(n);
                                jogador.setNumEscolhas(jogador.getNumEscolhas() + 1);
                                hidroAvioes++;
                            } else {
                                JOptionPane.showMessageDialog(null, "posicao invalida!");

                            }
                        }

                    }
                    if (isConexao() == false) {
                        if (jogador.getNumEscolhas() == 10) {  // Mensagem de orientacao

                            MarcarAlvo = true;

                            JOptionPane.showMessageDialog(null, "Espere sua vez para poder marcar um alvo  "
                            );

                        }
                    }
                    if (isConexao() == true) {
                        if (jogador.getNumEscolhas() == 5) {  // Mensagem de orientacao

                            MarcarAlvo = true;

                        }
                    }

                }

            }
            for (int i = 0; i < 225; i++) { // Se "apertar" no Tabuleiro (Player 2)
                if (e.getSource() == getButtonsTab2()[i]) {

                    if (jogador.isVez()) {

                        Posicao p = new Posicao(i, "hidro-Aviao");

                        jogador.getPosicaoAtaque().add(p);

                    } else {
                        JOptionPane.showMessageDialog(null, "Aguarde, sua vez...");

                    }

                }
            }
        } catch (HeadlessException exception) {
            JOptionPane.showMessageDialog(null, "ERRO - Uso incorreto do Tabuleiro");
        }
    }

    public void exibirEscolhaNavios(int cone) {

        if (cone == 0) {
            setConexao(true);
            navios.exibirPlayer2(false);
            navios.getRadioHidro().setSelected(true);

        } else {
            if (cone % 2 == 0) {

                setConexao(true);
                navios.exibirPlayer2(false);
                navios.getRadioHidro().setSelected(true);

            } else {
                setConexao(false);
                navios.exibirPlayer2(true);
                navios.getRadioPorta().setSelected(true);
                pJogador.getVidaUm().setVisible(false);
                pJogador.getVidaDois().setVisible(true);

            }
        }
    }

    public boolean verificarposicaoHidro(int posicao) {
        boolean sim = true;

        for (int i = 0; i < HidroProibidos.length; i++) {

            if (HidroProibidos[i] == posicao) {
                sim = false;

            }
        }
        return sim;
    }

    public boolean verificarposicaoCruzador(int posicao) {
        boolean sim = true;

        if (posicao > 209) {
            sim = false;

        }
        return sim;
    }

    public boolean verificarposicaoEncouracado(int posicao) {
        boolean sim = true;

        if (posicao > 179) {
            sim = false;

        }
        return sim;
    }

    public boolean verificarposicaoPorta(int posicao) {
        boolean sim = true;

        if (posicao > 164) {
            sim = false;

        }
        return sim;
    }

    // Getters and Setters
    public JPanel getTabuleiro1() {
        return tabuleiro1;
    }

    public void setTabuleiro1(JPanel tabuleiro1) {
        this.tabuleiro1 = tabuleiro1;
    }

    public JPanel getTabuleiro2() {
        return tabuleiro2;
    }

    public void setTabuleiro2(JPanel tabuleiro2) {
        this.tabuleiro2 = tabuleiro2;
    }

    public Botao[] getButtonsTab1() {
        return buttonsTab1;
    }

    public void setButtonsTab1(Botao[] buttonsTab1) {
        this.buttonsTab1 = buttonsTab1;
    }

    public Botao[] getButtonsTab2() {
        return buttonsTab2;
    }

    public void setButtonsTab2(Botao[] buttonsTab2) {
        this.buttonsTab2 = buttonsTab2;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the navios
     */
    public Navios getNavios() {
        return navios;
    }

    /**
     * @return the telaChat
     */
    public TelaChat getTelaChat() {
        return telaChat;
    }

    /**
     * @return the MarcarAlvo
     */
    public boolean isMarcarAlvo() {
        return MarcarAlvo;
    }

    /**
     * @param MarcarAlvo the MarcarAlvo to set
     */
    public void setMarcarAlvo(boolean MarcarAlvo) {
        this.MarcarAlvo = MarcarAlvo;
    }

    /**
     * @return the cards
     */
    public JPanel getCards() {
        return cards;
    }

    /**
     * @param cards the cards to set
     */
    public void setCards(JPanel cards) {
        this.cards = cards;
    }

    /**
     * @return the pJogador
     */
    public PainelJogador getpJogador() {
        return pJogador;
    }

    /**
     * @return the p2
     */
    public JPanel getP2() {
        return p2;
    }

    /**
     * @param navios the navios to set
     */
    public void setNavios(Navios navios) {
        this.navios = navios;
    }


    /**
     * @return the tab
     */
    public JPanel getTab() {
        return tab;
    }

   

    /**
     * @return the conexao
     */
    public boolean isConexao() {
        return conexao;
    }

    /**
     * @param conexao the conexao to set
     */
    public void setConexao(boolean conexao) {
        this.conexao = conexao;
    }

    /**
     * @return the vidaum
     */
    public VidaUm getVidaum() {
        return vidaum;
    }

    /**
     * @return the vidadois
     */
    public vidaDois getVidadois() {
        return vidadois;
    }

}
