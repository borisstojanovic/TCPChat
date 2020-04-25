package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {

    private List<ServerThread> serverThreadList;
    private List<String> users;

    public ServerMain() throws Exception{

        int port = 1550;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Otvoren je port " + port);

        serverThreadList = new ArrayList<>();
        users = new ArrayList<>();

        while(true) {
            Socket socket = serverSocket.accept();

            ServerThread serverThread = new ServerThread(this, socket);
            serverThreadList.add(serverThread);

            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }

    public static void main(String[] args) {
        try {
            new ServerMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ServerThread> getServerThreadList() {
        return serverThreadList;
    }

    public List<String> getUsers() {
        return users;
    }
}