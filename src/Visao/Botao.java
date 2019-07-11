package Visao;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Botao extends javax.swing.JButton {  

    private static final long serialVersionUID = 1L;
    // Declara COmponentes
    private JLabel label;
    private ImageIcon agua;
    private ImageIcon aux;
    private ImageIcon navio;
    private ImageIcon mar;
    private ImageIcon bomba;
    private ImageIcon explosao;

    // Construtor de Botao com nome
    public Botao(String text) {
        this();
        label.setText(text);
    }

    // Construtor de Botao sem nome  
    public Botao() {
        label = new JLabel();
        this.add(label);

        bomba = new ImageIcon("src/Resource/mar4.jpg");
        navio = new ImageIcon("src/Resource/navio1.png");
        mar = new ImageIcon("src/Resource/mar1.jpg");
        explosao = new ImageIcon("src/Resource/mar2.jpg");
        agua = new ImageIcon("src/Resource/mar3.jpg");
        aux = mar;
       
    }

    // Set nome (String) no botao
    public void setText(String nome) {
        label.setText(nome);
    }

    @Override
    public void paintComponent(Graphics g) {

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final Image backgroundImage = getAux().getImage();
        // Pega tamanho da imagem  (Obs: A imagem determina o tamanho do botao)
        double scaleX = getWidth() / (double) backgroundImage.getWidth(null);
        double scaleY = getHeight() / (double) backgroundImage.getHeight(null);
        AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);
        // Desenha Imagem
        ((Graphics2D) g).drawImage(backgroundImage, xform, this);
       
    }

    public void setFundo(int i) {
        // Conforme Tipo e Posicao a variavel da imagem (fundo do botao) muda
        if (i == 1) {
            this.aux = navio;
        } else if (i == 2) {
            this.aux = explosao;
        } else if (i == 3) {
            this.aux = agua;
        }else if (i == 4) {
            this.aux = bomba;
        }  else {
            this.aux = mar;
        }

        repaint();

    }

    /**
     * @return the agua
     */
    public ImageIcon getAgua() {
        return agua;
    }

    /**
     * @return the aux
     */
    public ImageIcon getAux() {
        return aux;
    }

    /**
     * @return the navio
     */
    public ImageIcon getNavio() {
        return navio;
    }

    /**
     * @return the mar
     */
    public ImageIcon getMar() {
        return mar;
    }

    /**
     * @return the bomba
     */
    public ImageIcon getBomba() {
        return bomba;
    }

    /**
     * @return the explosao
     */
    public ImageIcon getExplosao() {
        return explosao;
    }

}
