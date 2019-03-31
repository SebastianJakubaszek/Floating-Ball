package KulkaServer1;



import java.io.*;
import java.util.*;

/**
        * Klasa zawierajaca swicha polecen od klienta oraz funkcje im odpowiadajace

        */

public final class ServerCommands {


    private static int firstLevel=1;

    private static int lastLevel=2;

    private static int clientNumber =0;

    private static  String[] levelList;

    private static boolean acceptingClients= true;

    static{
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigFiles\\levels.txt"));
            firstLevel=Integer.parseInt(br.readLine());
            lastLevel=Integer.parseInt(br.readLine());
            System.out.println(firstLevel);
            System.out.println(lastLevel);
        }
        catch (IOException e){
            System.out.println("Plik 'ConfigFiles\\IPCONFIG.txt' nie m�g zostac otwarty");
            System.err.println(e);
        }
        levelList=new String[lastLevel];
        for(int i=0;i<lastLevel;i++) {
            levelList[i] = "res/level" + (i + 1);
            System.out.println(levelList[i]);
        }

    }


    public static String serverAction(String command){
        String serverCommand = command;
        String originalCommand= command;
        System.out.println(command);
        if(command.contains("GET_LEVEL:")){
            originalCommand=command;
            serverCommand=("GET_LEVEL");
        }
        if(command.contains("ADD_SCORE:")){
            originalCommand=command;
            serverCommand="ADD_SCORE";
        }


        String serverMessage;
        switch (serverCommand){
            case "LOGIN":
                serverMessage=login();
                break;

            case "GET_HIGH_SCORES":
                serverMessage=getHighScores();
                break;
            case "GET_AVAILBLE_LEVELS":
                serverMessage=getAvailableLevels();
                break;
            case "GET_LEVEL":
                String str[] = originalCommand.split(":");
                serverMessage=getLevel(Integer.parseInt(str[1]));
                break;
            case "ADD_SCORE":
                int i, x = 0, tempIndex = 0, y = 0;
                i = originalCommand.length();
                char arr[] = originalCommand.toCharArray();


                tempIndex=originalCommand.indexOf(';');
                char tempNick[] = new char[tempIndex-10];
                char tempScore[] = new char[i-tempIndex-1];
                System.out.println(tempIndex);
                System.out.println(i);


                for(int z=10; z<tempIndex ; z++){
                    tempNick[x] = arr[z];
                    x++;
                }
                for(int z=tempIndex+1; z<i; z++){
                    tempScore[y] = arr[z];
                    y++;
                }
                String score2 = String.valueOf(tempScore);
                int score = Integer.valueOf(score2);
                String nickname = String.valueOf(tempNick);
                System.out.println(nickname);
                System.out.println(score2);
                writeScores(score, nickname);
                serverMessage = "hejka";
             //   serverMessage=updateHighScore(str2[0],Integer.parseInt(str2[1]));
                break;
            case "LOGOUT":
                serverMessage=logout();
                break;
            case "CONNECTION_CLOSED":
                serverMessage=connectionClosed();
                break;
            default:
                serverMessage="INVALID_COMMAND";
        }
        return serverMessage;
    }


    private static String login(){
        String serverMessage;
        if(acceptingClients) {
            serverMessage="LOGGED_IN "+clientNumber+"\n";
            clientNumber++;
        }
        else{
            serverMessage="CONNECTION_REJECTED";
        }
        return serverMessage;
    }





    private static String getHighScores(){
        StringBuilder sb = new StringBuilder();
        File plik = new File("res/scores.txt");
        Scanner odczyt = null;
        try {
            odczyt = new Scanner(plik);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sb.append(odczyt.nextLine());
        return sb.toString();
    }




    private static String getAvailableLevels(){
        String message = String.valueOf(lastLevel);
        return message;
    }



    private static String getLevel(int levelNumber){
        StringBuilder sb = new StringBuilder();
        String numer=Integer.toString(levelNumber);
        File plik = new File("res/level"+numer+".txt");
        Scanner odczyt = null;
        try {
            odczyt = new Scanner(plik);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (odczyt.hasNextLine()){
            sb.append(odczyt.nextLine()+"\n");
        }
        System.out.println(sb.toString());
        return sb.toString();

    }






    private static String logout(){
        return "LOGGEDOUT";
    }


    private static String connectionClosed(){
        return "CLOSE_CONNECTION_NOW";
    }

    private static int []indexes = new int[5];
    private static int []scores = new int[5];
    private static String []nicknames = new String[5];
    private static char[] tempArray = new char[100];
    private static String beforeString ="";
    private static String centerString ="";
    private static String []afterString = new String[4];
    private static String afterString2= "";
    private static int []ii =new int[5];
    private static int []iii =new int[5];
    private static int []y =new int[5];

    public static void writeScores(int score, String nickname){
        try {
            readScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("res/scores.txt"));
            int temp = 5;
            for(int i = 0; i < 5; i++){
                if(scores[i]<score){
                    temp=i;
                    break;
                }
            }
            if(temp<4 && temp!=0) {
                beforeString = new String(tempArray, 0, y[temp] - 1);
                centerString = indexes[temp]+";"+score+";"+nickname+";";

                for(int killMe = 0; killMe < (4-temp); killMe++) {
                    afterString[killMe] = indexes[temp + 1 + killMe] + ";" + scores[temp+killMe] + ";" + nicknames[temp+killMe] + ";";
                }
                for(int kk = 0; kk < 4; kk++){
                    if(afterString[kk]!=null) {afterString2 = afterString2+afterString[kk];} else break;
                }
                out.write(beforeString+centerString+afterString2);
            }else if(temp==0) {
                centerString = indexes[temp]+";"+score+";"+nickname+";";

                for(int killMe = 0; killMe < (4-temp); killMe++) {
                    afterString[killMe] = indexes[temp + 1 + killMe] + ";" + scores[temp+killMe] + ";" + nicknames[temp+killMe] + ";";
                }
                for(int kk = 0; kk < 4; kk++){
                    if(afterString[kk]!=null) {afterString2 = afterString2+afterString[kk];} else break;
                }
                out.write(centerString+afterString2);
            }else if(temp==4) {
                beforeString = new String(tempArray, 0, y[temp] - 1);
                centerString = indexes[temp]+";"+score+";"+nickname+";";
                out.write(beforeString+centerString);
            }else if(temp==5) {
                out.write(new String(tempArray));
            }
            out.close();
        } catch (IOException e) {}
        score = 0;
        nickname = "";
        indexes = new int[5];
        scores = new int[5];
        nicknames = new String[5];
        tempArray = new char[100];
        beforeString ="";
        centerString ="";
        afterString = new String[4];
        afterString2= "";
        ii =new int[5];
        iii =new int[5];
        y =new int[5];
    }

    public static void readScores() throws IOException {
        InputStream in = null;

        try {
            in = new FileInputStream("res/scores.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Reader r = new InputStreamReader(in);

        int c = -1;
        int i = 0;
        int j = 0;
        int counter = 0;

        while ((c = r.read())> -1) {
            tempArray[i] = (char)c;

            if((char)c==';' && counter == 2){
                iii[j]=i;
                counter = 0;
                j++;
            }else if((char)c==';' && counter == 1){
                ii[j]=i;
                counter++;
            }else if((char)c==';' && counter == 0){
                counter++;
                y[j]=i;
            }
            i++;
        }

        for(int m=0; m<5; m++){
            indexes[m]=Integer.parseInt(new String(tempArray, y[m]-1, 1));
            scores[m]=Integer.parseInt(new String(tempArray, y[m]+1, ii[m]-y[m]-1));
            nicknames[m] = new String(tempArray, ii[m]+1, iii[m]-ii[m]-1);
        }
    }



}

