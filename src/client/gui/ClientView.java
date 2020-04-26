package client.gui;

import Observer.MyObserver;
import Observer.ObserverEnum;
import client.Client;
import client.controller.ClientViewListener;
import client.controller.ConnectButtonListener;
import client.controller.DisconnectButtonListener;
import client.controller.SendButtonListener;

import javax.swing.*;
import java.awt.*;

public class ClientView extends JFrame implements MyObserver {

    private Client client;
    private JButton discButton;
    private JButton conButton;
    private JButton sendButton;
    private JLabel messageLabel;
    private JLabel conversationLabel;
    private JLabel onlineLabel;
    private JLabel nameLabel;
    private JTextField textFieldName;
    private JTextField textFieldMessage;
    private JTextArea textAreaConversation;
    private JList<String> userList;
    private DefaultListModel listModel;
    private JScrollPane scrollPaneOnline;
    private JScrollPane scrollPaneConversation;


    public ClientView(Client client){
        this.client = client;
        client.addObserver(this);

        initialise();
    }

    private void initialise(){
        initialiseJFrame();
        initialiseView();
        initialiseControllers();
        nameView();
    }

    private void reinitialise(){
       // this.getContentPane().removeAll();
        //initialiseView();
        //initialiseControllers();
        textAreaConversation.setText("");
        listModel.removeAllElements();
        nameView();
    }

    private void initialiseJFrame(){
        this.setBackground(new Color(255,255,255));
        this.setSize(500, 320);
        this.setLocation(220,180);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
    }

    //inicijalizuje glavni view i stavlja sve komponente na njega
    private void initialiseView(){

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(0, 0, 255));
        sendButton.setForeground(new Color(0, 255, 255));
        this.getContentPane().add(sendButton);
        sendButton.setBounds(250,40,80,25);

        discButton = new JButton("Disconnect");
        discButton.setBackground(new Color(0, 0, 255));
        discButton.setForeground(new Color(0, 255, 255));
        this.getContentPane().add(discButton);
        discButton.setBounds(10, 40, 110, 25);

        conButton = new JButton("Connect");
        conButton.setBackground(new Color(0, 0, 255));
        conButton.setForeground(new Color(0, 255, 255));
        this.getContentPane().add(conButton);
        conButton.setBounds(130, 40, 110, 25);

        messageLabel = new JLabel("Message");
        this.getContentPane().add(messageLabel);
        messageLabel.setBounds(10, 10, 60, 20);

        textFieldMessage = new JTextField();
        textFieldMessage.requestFocus();
        this.getContentPane().add(textFieldMessage);
        textFieldMessage.setBounds(70, 4, 260, 30);


        nameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBounds(360, 15, 120, 16);
        this.getContentPane().add(nameLabel);

        textFieldName = new JTextField();
        this.getContentPane().add(textFieldName);
        textFieldName.setBounds(350, 30, 120, 30);

        conversationLabel = new JLabel("Conversation");
        conversationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.getContentPane().add(conversationLabel);
        conversationLabel.setBounds(100, 70, 140, 16);

        textAreaConversation = new JTextArea();
        textAreaConversation.setColumns(20);
        textAreaConversation.setFont(new Font("Arial", 0, 12));
        textAreaConversation.setForeground(new Color(0, 0, 255));
        textAreaConversation.setLineWrap(true);
        textAreaConversation.setRows(5);
        textAreaConversation.setEditable(false);


        onlineLabel = new JLabel("Online");
        onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        onlineLabel.setBounds(350, 73, 130, 16);
        this.getContentPane().add(onlineLabel);

        listModel = new DefaultListModel();

        userList = new JList<>(listModel);
        userList.setForeground(new Color(0, 0, 255));

        scrollPaneOnline = new JScrollPane();
        scrollPaneOnline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneOnline.setViewportView(userList);
        this.getContentPane().add(scrollPaneOnline);
        scrollPaneOnline.setBounds(350, 90, 130, 180);

        scrollPaneConversation = new JScrollPane();
        scrollPaneConversation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneConversation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneConversation.setAutoscrolls(true);
        scrollPaneConversation.setViewportView(textAreaConversation);
        this.getContentPane().add(scrollPaneConversation);
        scrollPaneConversation.setBounds(10, 90, 260, 180);

        this.addWindowListener(new ClientViewListener(client, this));
        this.setVisible(true);

    }

    private void initialiseControllers() {
        conButton.addActionListener(new ConnectButtonListener(client, this));
        textFieldName.addActionListener(new ConnectButtonListener(client, this));
        discButton.addActionListener(new DisconnectButtonListener(client));
        sendButton.addActionListener(new SendButtonListener(client, this));
        textFieldMessage.addActionListener(new SendButtonListener(client, this));
    }

    public void nameView(){
        discButton.setEnabled(false);
        sendButton.setEnabled(false);
        textFieldMessage.setEnabled(false);
        conButton.setEnabled(true);
        textFieldName.setEnabled(true);
        textFieldName.requestFocus();
    }

    public void connectView() {
        discButton.setEnabled(true);
        sendButton.setEnabled(true);
        textFieldMessage.setEnabled(true);
        conButton.setEnabled(false);
        textFieldName.setEnabled(false);
        textFieldMessage.requestFocus();
    }

    public JTextField getTextFieldName() {
        return textFieldName;
    }

    public JTextField getTextFieldMessage() {
        return textFieldMessage;
    }

    @Override
    public void update(ObserverEnum observerEnum, Object o) {
        if(o instanceof String) {
            String text = (String) o;
            if(observerEnum == ObserverEnum.ADD) {
                listModel.addElement(text);
            }else if(observerEnum == ObserverEnum.REMOVE) {
                listModel.removeElement(text);
            }else if(observerEnum == ObserverEnum.MESSAGE){
                textAreaConversation.append("\n" + text);
                textAreaConversation.setCaretPosition(textAreaConversation.getDocument().getLength());
                textFieldMessage.requestFocus();
            }else if(observerEnum == ObserverEnum.DISCONNECTED){
                reinitialise();
            }
        }else{
            //ako je konektovan trenutno
        }
    }

}
