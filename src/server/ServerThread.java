package server;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{

    private Socket socket;
    private ServerMain serverMain;
    private BufferedReader in;
    private PrintWriter out;
    private String msg;

    public ServerThread(ServerMain serverMain, Socket socket) {
        this.socket = socket;
        this.serverMain = serverMain;
        this.msg = new String();
    }

    @Override
    public void run() {
        try {
            System.out.println( "Otvorena konekcija sa klijentom sa IP adresom: " +
                    socket.getInetAddress().getHostAddress() + " na portu: " + socket.getLocalPort());

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            sendUsernamesToClient();//sluzi da klijent inicijalizuje svoju listu korisnickih imena
            recieveUsernameFromClient();

            while(true) {
                if(!socket.isConnected()){
                    closeConnection();
                    break;
                }
                msg = in.readLine();

                if(msg.startsWith("IDisconnect:")){
                    closeConnection();
                    break;
                }
                sendMsgToAllThreads(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //salje klijentu imena konektovanih korisnika
    private void sendUsernamesToClient() {
        int i = 0;
        while(serverMain.getUsers().size() > i){
            String name = serverMain.getUsers().get(i++);
            out.println(name);
        }
        out.println("END");
    }

    //prima korisnicko ime od klijenta i javlja ostalim klijentima da se konektovao novi korisnik
    private void recieveUsernameFromClient() throws Exception{
        String name = in.readLine();
        serverMain.getUsers().add(name.substring(name.indexOf(':') + 1));
        sendMsgToAllThreads(name);
    }

    private void sendMsgToAllThreads(String msg){
        for (ServerThread serverThread : serverMain.getServerThreadList()) {
            serverThread.printMsg(msg);
        }
    }

    private void printMsg(String msg){
        out.println(msg);
    }

    private void closeThisThread(){
        serverMain.getServerThreadList().remove(this);
        out.println(msg);
        msg = msg.substring(msg.indexOf(':') + 1);
        serverMain.getUsers().remove(msg);
    }

    private void closeConnection(){
        try {
            closeThisThread();

            msg = "Disconnect:" + msg;
            sendMsgToAllThreads(msg);
            socket.close();
            System.out.println( "Zatvorena konekcija sa klijentom sa IP adresom: " +
                    socket.getInetAddress().getHostAddress() + " na portu: " + socket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
