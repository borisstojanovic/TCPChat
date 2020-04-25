package client.controller;

import client.Client;
import client.gui.ClientView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectButtonListener implements ActionListener {

    private Client model;
    private ClientView view;

    public ConnectButtonListener(Client model, ClientView view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            model.connect(view.getTextFieldName().getText());
            view.connectView();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
