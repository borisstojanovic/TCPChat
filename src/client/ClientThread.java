package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable{

    private BufferedReader in;
    private Socket socket;
    private String msg;
    private Client client;

    public  ClientThread(Client client, Socket socket) throws Exception{
        this.socket = socket;
        this.client = client;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try{
            while ((msg = in.readLine()) != null) {
                client.readInput(msg);
                if(msg.startsWith("IDisconnect:")){
                    break;
                }
            }
            socket.close();
            System.out.println("Disconnected");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
