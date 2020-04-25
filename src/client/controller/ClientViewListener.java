package client.controller;

import client.Client;
import client.gui.ClientView;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ClientViewListener implements WindowListener {

    Client model;
    ClientView view;

    public ClientViewListener(Client model, ClientView view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        model.disconnect();
        view.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
