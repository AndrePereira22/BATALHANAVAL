package Visao;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ModeloCliente.Cliente;

public class TelaChat {
    // Declara Tela (JPanel)

    private JPanel panelChat;
    // Declara Componentes
    private JTextArea textAreaConversas;
    private JScrollPane scrollPane;
    private JTextField textFieldChat;
    private JButton buttonChat; // Enviar

    // Declara Classe de Comunicacao de Dados
    private Cliente cliente;
    // Construtor da Tela

    public TelaChat(Cliente cliente) {
        this.cliente = cliente;
        // Cria nova Tela (JPanel)
        setPanelChat(new JPanel(new GridBagLayout()));
        // Cria manipulador de eventos
        ButtonHandler handler = new ButtonHandler();
        // Cria Campo de texto
        setTextFieldChat(new JTextField(40));
        getTextFieldChat().addActionListener(handler);
        // Cria Campo de texto com um ScrollPane (Barra de Rolagem)
        setTextAreaConversas(new JTextArea(5, 20));
        getTextAreaConversas().setEditable(false);
        scrollPane = new JScrollPane(textAreaConversas);
        // Cria botao Enviar
        setButtonChat(new JButton("Enviar"));

        getTextFieldChat().addActionListener(handler);
        getButtonChat().addActionListener(handler);

        // Adicionando Componentes a Tela (JPanel).
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        getPanelChat().add(scrollPane, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        getPanelChat().add(textFieldChat, c);
        getPanelChat().add(buttonChat);
    }
    // Manipulador de Acoes - Botoes (BottonsChat)

    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                // Se botao for apertado
                if (e.getSource() == getButtonChat()) {
                    // Envia para o servidor

                    String n = getTextFieldChat().getText();

                    if (!n.equals("")) {
                        cliente.getSaida().println(n);
                        getTextFieldChat().setText("");
                    }

                }
                if (e.getSource() == getTextFieldChat()) {
                    String n = getTextFieldChat().getText();

                    if (!n.equals("")) {
                        cliente.getSaida().println(n);
                        getTextFieldChat().setText("");
                    }

                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "ERRO - Uso incorreto");
            }
        }
    }

    public JTextArea getTextAreaConversas() {
        return textAreaConversas;
    }

    public void setTextAreaConversas(JTextArea textAreaConversas) {
        this.textAreaConversas = textAreaConversas;
    }

    public void setTextAreaConversas(String texto) {
        this.textAreaConversas.setText(texto);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JTextField getTextFieldChat() {
        return textFieldChat;
    }

    public void setTextFieldChat(JTextField textFieldChat) {
        this.textFieldChat = textFieldChat;
    }

    public JButton getButtonChat() {
        return buttonChat;
    }

    public void setButtonChat(JButton buttonChat) {
        this.buttonChat = buttonChat;
    }

    public JPanel getPanelChat() {
        return panelChat;
    }

    public void setPanelChat(JPanel panelChat) {
        this.panelChat = panelChat;
    }
}
