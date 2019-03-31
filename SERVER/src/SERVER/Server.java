package KulkaServer1;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
        * Klasa w ktorej tworzony jest watek serwera

        */


public class Server {


    private int numberofport;



    public void runServer() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigFiles/IPCONFIG.txt"));
            numberofport = Integer.parseInt(br.readLine());
            ServerSocket serverSocket = new ServerSocket(numberofport);
            System.out.println("Serwer uruchomiony");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerThread(socket)).start();
            }
        }
        catch (IOException e){
            System.out.println("Serwer nie mógł zostać uruchomiony");
            System.err.println(e);
        }
    }

}
