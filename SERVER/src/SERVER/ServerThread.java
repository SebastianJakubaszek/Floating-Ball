package KulkaServer1;


import java.io.*;
import java.net.Socket;

/**
       *klasa opisujaca watek serwera
        */
public class ServerThread implements Runnable {

    private Socket socket;


    public ServerThread(Socket socket){
        this.socket=socket;
    }


    public void run() {
        try {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String fromClient = br.readLine();
                if(fromClient!=null) {
                    System.out.println("FROM_CLIENT: " + fromClient);
                    String serverMessage = ServerCommands.serverAction(fromClient);
                    if(serverMessage=="CONNECTION_REJECTED")
                        socket.close();
                    else {
                        pw.println(serverMessage);
                        pw.flush();
                        System.out.println("TO_CLIENT: " + serverMessage);
                        if (serverMessage == "LOGGEDOUT") {
                            socket.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
