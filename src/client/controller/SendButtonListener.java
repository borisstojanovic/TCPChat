package client.controller;

import client.Client;
import client.gui.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendButtonListener implements ActionListener {

    private Client model;
    private ClientView view;

    public SendButtonListener(Client model, ClientView view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.writeOutput(view.getTextFieldMessage().getText());
        view.getTextFieldMessage().setText("");
    }
}
