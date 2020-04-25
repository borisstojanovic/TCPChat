package client;

import client.gui.ClientView;

public class ClientMain {
    public static void main(String[] args) {
        try {
            new ClientView(new Client());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
