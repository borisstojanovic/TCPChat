package client.controller;

import client.Client;
import client.gui.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisconnectButtonListener implements ActionListener {

    private Client model;

    public DisconnectButtonListener(Client model){
        this.model = model;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        model.disconnect();
    }
}
