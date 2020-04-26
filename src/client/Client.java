package client;

import Observer.MyObservable;
import Observer.MyObserver;
import Observer.ObserverEnum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements MyObservable {

    private List<String> users;
    private List<MyObserver> observers;
    private String username;
    private Socket socket;
    private PrintWriter out;

    public Client() {
        observers = new ArrayList<>();
        username = new String();
        users = new ArrayList<>();
    }

    //proverava da li je neki korisnik sa istim imenom vec ulogovan
    //ako jeste menja ime
    private String checkName(String name){

        if(name.isBlank()){
            name = "guest";
        }
        int i = 1;
        if(users.contains(name)) {
            while (users.contains(name + i)) {
                i++;
            }
            name+=i;
        }
        return name;
    }


    public void connect(String name) throws Exception{

        int port = 1550;
        socket = new Socket("192.168.1.7", port);

        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

        receiveUsernames();
        username = checkName(name);

        ClientThread clientThread = new ClientThread(this, socket);
        Thread thread = new Thread(clientThread);
        thread.start();

        out.println("User:" + username); //salje serveru informaciju o svom korisnickom imenu
    }

    //sluzi da klijent prilikom konektovanja dobije informacije o ostalim konektovanim klijentima na serveru
    public void receiveUsernames() throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String serverMsg = in.readLine();
        while (!serverMsg.equals("END")){
            users.add(serverMsg);
            notifyObservers(ObserverEnum.ADD, serverMsg);
            serverMsg = in.readLine();
        }
    }

    //poziva se kada klijent treba da se diskonektuje sa servera
    //on prefiskom Disconnect: javlja serveru da ce da se diskonektuje
    //server javlja clientThreadu da treba da se ugasi i zatvori socket
    public void disconnect(){
        if(out != null) {
            out.println("IDisconnect:" + username);
        }
    }

    //poziva je kontroler za text field kada se unese tekst
    public void writeOutput(String msg){
        msg = "Message:" + username + " : " + msg;//koristi se zbog parsiranja u metodi readInput
        out.println(msg);
    }

    //koristi se za parsiranje unosa
    public void readInput(String msg){
        if(msg.startsWith("User:")){
            msg = msg.substring(msg.indexOf(':') + 1);
            users.add(msg);
            notifyObservers(ObserverEnum.ADD, msg);
        }else if(msg.startsWith("Message:")){
            msg = msg.substring(msg.indexOf(':') + 1);
            notifyObservers(ObserverEnum.MESSAGE, msg);
        }else if(msg.startsWith("Disconnect:")){
            msg = msg.substring(msg.indexOf(':') + 1);
            users.remove(msg);
            notifyObservers(ObserverEnum.REMOVE, msg);
        }else if(msg.startsWith("IDisconnect:")){
            users.clear();
            notifyObservers(ObserverEnum.DISCONNECTED, msg);
        }
    }

    @Override
    public void notifyObservers(ObserverEnum observerEnum, Object o) {
        for (MyObserver observer : observers) {
            observer.update(observerEnum, o);
        }
    }

    @Override
    public void addObserver(MyObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MyObserver observer) {
        observers.add(observer);
    }
}