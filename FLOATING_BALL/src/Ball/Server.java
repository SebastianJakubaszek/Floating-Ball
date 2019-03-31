package Ball;

import java.io.*;
import java.net.Socket;

public class Server {
    private static int Port;
    private static String IPAddress;
    private Socket serverSocket;
    private  int avaliablelevel;


    public static Socket connectToServer() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigFiles//IPCONFIG.txt"));
            IPAddress=br.readLine();
            Port=Integer.parseInt(br.readLine());
            Socket socket = new Socket(IPAddress, Port);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("LOGIN");
            InputStream is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            if(br.readLine().contains("LOGGED_IN")){
                System.out.println("połaczone");
                return socket;

            }
            else{
                return null;
            }

        }
        catch (Exception e) {
            System.out.println("Connection could not be opened..");
            System.out.println("error: "+e);
        }
        return null;
    }

    public int avaliablelevels(Socket serverSocket){
    this.serverSocket=serverSocket;
        try{
            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("GET_AVAILBLE_LEVELS");
            InputStream is = serverSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            avaliablelevel=Integer.valueOf(br.readLine());
            return avaliablelevel;


        }catch (IOException e){
            System.out.println("Blad polaczenia z serwerem, plik nie mogl zostac pobrany");
            System.out.println(e);
        }
    return 0;
    }

    public void getLevel() {

        for (int i = 1; i < avaliablelevel + 1; i++) {
            try {

                OutputStream os = serverSocket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);

                pw.println("GET_LEVEL:" + i);
                InputStream is = serverSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                PrintWriter out = new PrintWriter("res/level" + i + ".txt");
                while (true) {
                    String level = br.readLine();
                    if (level.equals("x")) {
                        break;
                    }
                    out.println(level);
                }
                out.close();


            } catch (IOException e) {
                System.out.println("Blad polaczenia z serwerem, plik nie mogl zostac pobrany");
                System.out.println(e);
            }
        }
    }
    public void logout(){
        try {

            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("LOGOUT");

        } catch (IOException e) {
            System.out.println("Blad polaczenia z serwerem, plik nie mogl zostac pobrany");
            System.out.println(e);
        }
    }
    public void getHighlist(Socket serverSocket){

    try {

        OutputStream os = serverSocket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);

        pw.println("GET_HIGH_SCORES" );
        InputStream is = serverSocket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PrintWriter out = new PrintWriter("res/scores.txt");
        String list = br.readLine();
        out.println(list);

        out.close();


    } catch (IOException e) {
        System.out.println("Blad polaczenia z serwerem, plik nie mogl zostac pobrany");
        System.out.println(e);
    }
}
    public void add_score (int score,String nickname, Socket serverSocket){
        try {

            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("ADD_SCORE:"+nickname+";"+score);
            InputStream is = serverSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String list = br.readLine();
            System.out.println(list);

        } catch (IOException e) {
            System.out.println("Blad polaczenia z serwerem, plik nie mogl zostac pobrany");
            System.out.println(e);
        }
    }


}
