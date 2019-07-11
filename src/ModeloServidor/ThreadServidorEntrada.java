package ModeloServidor;

import AplicacaoServidor.appServidor;
import java.util.Scanner;
import java.io.InputStream;

import Jogo.Jogador;
import BancoDeDados.JDBCConexao;
import BancoDeDados.JogadorDAO;
import Jogo.Navio;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ThreadServidorEntrada implements Runnable {

    private InputStream cliente;
    private appServidor servidor;
    List<Jogador> jogadores;
    private int numeroConexao;
    private boolean login;
    private boolean nome, salvo;
    private boolean cadastro;
    private boolean enviaDados;
    private Jogador player1, player2, jogador;
    private int opcao1, opcao2;
    private final String[] LETRAS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};

    public ThreadServidorEntrada(InputStream cliente, appServidor servidor, int numeroConexao, Jogador player1, Jogador player2) {
        this.cliente = cliente;
        this.servidor = servidor;
        this.numeroConexao = numeroConexao;
        this.player1 = player1;
        this.player2 = player2;
        this.jogador = new Jogador();
        player1.setConexao(numeroConexao);
        player2.setConexao(numeroConexao);

        nome = true;
        cadastro = false;
        if (numeroConexao < 2) {
            opcao1 = 0;
            opcao2 = 1;
        } else if (numeroConexao > 1 && numeroConexao < 4) {
            opcao1 = 2;
            opcao2 = 3;
        } else if (numeroConexao > 3 && numeroConexao < 6) {
            opcao1 = 4;
            opcao2 = 5;
        }

    }
    @Override
    public void run() {
        Scanner s = new Scanner(this.cliente);
        char[] protocolo;
        String aux = "";
        while (s.hasNextLine()) {
            String msg = s.nextLine();
            protocolo = msg.toString().toCharArray();
            switch (protocolo[0]) {
                case '$':
                    if (numeroConexao == opcao1) {
                        if (!login) {
                            player1.setNome(leStringProtocolo(protocolo));
                            login = validaNome(player1.getNome(), 1);
                            player1.setId(verificaId(player1.getNome()));
                            servidor.uniCast(numeroConexao, "$OK1");
                        } else if (login) { //CONSULTA BD
                            if (!enviaDados) {
                                enviaDados = validaSenha(leStringProtocolo(protocolo), player1.getId(), 1);
                                if (enviaDados) {
                                    servidor.uniCast(numeroConexao, "$OK2");
                                    enviaDados = false;
                                    login = false;
                                    servidor.getLista().add(player1);
                                    
                                }
                            }
                        }
                    }
                    if (numeroConexao == opcao2) {
                        if (!login) {
                            player2.setNome(leStringProtocolo(protocolo));
                            login = validaNome(player2.getNome(), 1);
                            player2.setId(verificaId(player2.getNome()));
                            servidor.uniCast(numeroConexao, "$OK1");
                        } else if (login) { //CONSULTA BD
                            if (!enviaDados) {
                                enviaDados = validaSenha(leStringProtocolo(protocolo), player2.getId(), 1);
                                if (enviaDados) {
                                    servidor.uniCast(numeroConexao, "$OK2");
                                    enviaDados = false;
                                    login = false;
                                    servidor.getLista().add(player2);
                                }
                            }
                        }
                    }
                    break;
                    
                case ':':
                    
                    if (!cadastro) {
                        if (nome) { //CONSULTA BD
                            player1.setNome(leStringProtocolo(protocolo));
                            nome = validaNome(player1.getNome(),numeroConexao);
                            servidor.uniCast(numeroConexao, ":OK1");
                        }
                        if (!nome) {//CONSULTA BD
                            jogador.setSenha(leStringProtocolo(protocolo));
                            
                            salvo = salvar(jogador.getNome(), jogador.getSenha());
                            if (salvo) {
                                servidor.uniCast(numeroConexao, ":OK2");
                                cadastro = true;
                            }
                        }
                    }
                    
                    break;
                    
                case '#':
                    int pos = pegarPosicao(msg);
                    
                    if (this.numeroConexao == opcao1) {
                        
                        player1.getHerois().add(new Navio("Hidro", 10, pos));
                        player1.setVez(true);
                    }
                    if (this.numeroConexao == opcao2) {
                        
                        player2.getHerois().add(new Navio("Hidro", 10, pos));
                        player2.setVez(true);
                    }
                    if (player1.isVez() && player2.isVez()) {
                        servidor.uniCast(opcao1, "/ini");
                    }
                    break;
                case '=':
                    
                    if (player1.isVez() && player2.isVez()) {
                        servidor.uniCast(opcao1, "/ini");
                    }
                    
                    break;
                    
                case '@':
                    if (!msg.equals(aux)) {
                        
                        int posicao = pegarPosicao(msg);
                        String pVisual = criarPosicaoVisual(posicao);
                        
                        if (this.numeroConexao == opcao1) {
                            
                            if (player2.verificaPosicaoHerois(posicao, player2.getHerois())) {
                                servidor.uniCast(opcao2, "!" + posicao);
                                
                                servidor.uniCast(opcao1, "+" + posicao);
                                //Thread.sleep(100);
                                servidor.uniCast(opcao2, "SERVIDOR: " + player1.getNome() + " lhe atacou na (" + pVisual + ") e acertou.  ");
                                
                                servidor.uniCast(opcao1, "SERVIDOR :  seu ataque na posiçao (" + pVisual + ")  acertou!");
                                
                            } else { // errou
                                
                                servidor.uniCast(opcao2, "&" + posicao);
                                servidor.uniCast(opcao1, "-" + posicao);
                                
                                //Thread.sleep(100);
                                
                                servidor.uniCast(opcao2, "SERVIDOR: " + player1.getNome() + " lhe atacou na (" + pVisual + ") e errou. ");
                                
                                servidor.uniCast(opcao1, "SERVIDOR :  seu ataque na posiçao (" + pVisual + ") falhou!");
                            }
                        }
                        
                        if (this.numeroConexao == opcao2) {
                            
                            if (player1.verificaPosicaoHerois(posicao, player1.getHerois())) {
                                
                                servidor.uniCast(opcao1, "!" + posicao);
                                
                                servidor.uniCast(opcao2, "+" + posicao);
                                //Thread.sleep(100);
                                servidor.uniCast(opcao2, "SERVIDOR : seu ataque na posiçao (" + pVisual + ")  acertou!");
                                servidor.uniCast(opcao1, "SERVIDOR: " + player2.getNome() + " lhe atacou na (" + pVisual + ") e acertou.  ");
                                
                            } else {
                                
                                servidor.uniCast(opcao1, "&" + posicao);
                                servidor.uniCast(opcao2, "-" + posicao);
                                
                                //Thread.sleep(100);
                                
                                servidor.uniCast(opcao2, " SERVIDOR :  seu ataque na posiçao (" + pVisual + ") falhou!");
                                servidor.uniCast(opcao1, "SERVIDOR: " + player2.getNome() + " lhe atacou na (" + pVisual + ") e errou.");
                                
                            }
                        }
                        aux = msg;
                    }
                    break;
                case '<':
                    servidor.uniCast(numeroConexao, "<" + numeroConexao);
                    
                    break;
                    
                case ';':
                    servidor.broadCastJogadores();
                    break;
                    
                case '0':
                    if (this.numeroConexao == opcao1) {
                        servidor.uniCast(opcao1, "0");
                        servidor.uniCast(opcao2, "6");
                    }
                    if (this.numeroConexao == opcao2) {
                        servidor.uniCast(opcao2, "0");
                        servidor.uniCast(opcao1, "6");
                    }
                    
                    break;
                    
                default:
                    if (this.numeroConexao == opcao1) {
                        servidor.broadCast(player1.getNome() + ">>" + msg);
                    }
                    if (this.numeroConexao == opcao2) {
                        servidor.broadCast(player2.getNome() + ">>" + msg);
                    }
                    break;
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

    public int pegarPosicao(String posicao) {

        if (!posicao.isEmpty()) {
            posicao = posicao.substring(1);
        }

        int p = Integer.parseInt(posicao);
        return p;

    }

    public String criarPosicaoVisual(int posicao) {
        String p = "";
        int aux = 0;

        if (posicao < 15) {         //A
            aux = posicao + 1;
            p = new String(LETRAS[0] + "" + aux);

        } else if (posicao < 30 && posicao > 14) {  ///B
            aux = posicao - 14;
            p = new String(LETRAS[1] + "" + aux);

        } else if (posicao < 45 && posicao > 29) {        ///C
            aux = posicao - 29;
            p = new String(LETRAS[2] + "" + aux);

        } else if (posicao < 60 && posicao > 44) {             ///D       
            aux = posicao - 44;
            p = new String(LETRAS[3] + "" + aux);

        } else if (posicao < 75 && posicao > 59) {             ///E      
            aux = posicao - 59;
            p = new String(LETRAS[4] + "" + aux);

        } else if (posicao < 90 && posicao > 74) {             ///F      
            aux = posicao - 74;
            p = new String(LETRAS[5] + "" + aux);

        } else if (posicao < 105 && posicao > 89) {             ///G    
            aux = posicao - 89;
            p = new String(LETRAS[6] + "" + aux);

        } else if (posicao < 120 && posicao > 104) {             ///h   
            aux = posicao - 104;
            p = new String(LETRAS[7] + "" + aux);

        } else if (posicao < 135 && posicao > 119) {             ///i   
            aux = posicao - 119;
            p = new String(LETRAS[8] + "" + aux);

        } else if (posicao < 150 && posicao > 134) {             ///J
            aux = posicao - 134;
            p = new String(LETRAS[9] + "" + aux);

        } else if (posicao < 165 && posicao > 149) {             ///k
            aux = posicao - 149;
            p = new String(LETRAS[10] + "" + aux);

        } else if (posicao < 180 && posicao > 164) {             ///l
            aux = posicao - 164;
            p = new String(LETRAS[11] + "" + aux);

        } else if (posicao < 195 && posicao > 179) {             ///m
            aux = posicao - 179;
            p = new String(LETRAS[12] + "" + aux);

        } else if (posicao < 210 && posicao > 194) {             ///n
            aux = posicao - 194;
            p = new String(LETRAS[13] + "" + aux);

        } else if (posicao > 209) {                             ///0
            aux = posicao - 209;
            p = new String(LETRAS[14] + "" + aux);
        }

        return p;
    }

    public String leStringProtocolo(char[] protocolo) {

        char[] palavra = new char[protocolo.length - 1];
        for (int i = 0; i < palavra.length; i++) {
            palavra[i] = protocolo[i + 1];
        }
        return String.valueOf(palavra);
    }

    public InputStream getCliente() {
        return cliente;
    }

    public void setCliente(InputStream cliente) {
        this.cliente = cliente;
    }

    public appServidor getServidor() {
        return servidor;
    }

    public void setServidor(appServidor servidor) {
        this.servidor = servidor;
    }

    public int getNumeroConexao() {
        return numeroConexao;
    }

    public void setNumeroConexao(int numeroConexao) {
        this.numeroConexao = numeroConexao;
    }

    public boolean validaSenha(String senha, int id, int conection) {
        try {
            JDBCConexao conexao = new JDBCConexao();
            java.sql.Connection con = conexao.criarConexao();
            JogadorDAO logando = new JogadorDAO();

            if (logando.verificaSenha(con, senha, id)) {
                return true;

            } else {
                servidor.uniCast(numeroConexao, "$OK3");

                con.close();
                return false;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean validaNome(String nome, int conection) {
        try {
            JDBCConexao conexao = new JDBCConexao();
            java.sql.Connection con = conexao.criarConexao();
            JogadorDAO logando = new JogadorDAO();
            if (logando.verificaNome(con, nome)) {
                return true;
            } else {
                servidor.uniCast(numeroConexao, "$OK3");

                con.close();
                return false;
            }

        } catch (SQLException e) {
        }
        return false;
    }

    public boolean salvar(String nome, String senha) {
        try {
            JDBCConexao conexao = new JDBCConexao();
            Connection con = conexao.criarConexao();
            Jogador jogador = new Jogador(nome, senha);
            JogadorDAO logando = new JogadorDAO();

            if (logando.addLogin(con, jogador.getNome(), jogador.getSenha())) {
                return true;

            } else {
                servidor.uniCast(numeroConexao, "$OK3");
                con.close();
                return false;
            }

        } catch (SQLException ex) {
        } catch (Exception ex) {

        }
        return false;
    }

    public int verificaId(String nome) {
        try {
            JDBCConexao conexao = new JDBCConexao();
            java.sql.Connection con = conexao.criarConexao();
            JogadorDAO logando = new JogadorDAO();
            return logando.buscaId(con, nome);

        } catch (SQLException e) {
        }
        return 0;
    }

}
