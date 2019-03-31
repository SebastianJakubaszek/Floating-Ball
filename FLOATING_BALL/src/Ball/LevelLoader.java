package Ball;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class LevelLoader {
    Handler handler;

    Configurator configurator;
    Scanner odczyt;
    String nazwa_obiektu;
    private Random generator = new Random();


    public LevelLoader (Handler handler,Configurator configurator) {
        this.handler=handler;
        this.configurator=configurator;
    }



    public void LoadLevel (){
        File plik = new File("res/level"+Game.LEVEL+".txt");
        try {
            odczyt = new Scanner(plik);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        while (odczyt.hasNextLine()){
            nazwa_obiektu=odczyt.nextLine();

            if(nazwa_obiektu.equals("Goal")){
                handler.addObject(new Goal(Integer.valueOf(odczyt.nextLine())*(Game.WIDTH / 30 - 1), Integer.valueOf(odczyt.nextLine())* (Game.HEIGHT / 22 - 1),0, ID.Goal, Game.WIDTH / 15, Game.WIDTH / 15, handler));
            }
            if(nazwa_obiektu.equals("Obstacle")){
                int roll = generator.nextInt(3);
                handler.addObject(new Obstacle(Integer.valueOf(odczyt.nextLine())*(Game.WIDTH / 30 - 1), Integer.valueOf(odczyt.nextLine())* (Game.HEIGHT / 22 - 1),roll, ID.Obstacle, Game.WIDTH / 30, (Game.WIDTH / 30)));
            }
            if(nazwa_obiektu.equals("Bonus_life")){
                handler.addObject(new LifeBonus(Integer.valueOf(odczyt.nextLine())*(Game.WIDTH / 30 - 1),Integer.valueOf(odczyt.nextLine())* (Game.HEIGHT / 22 - 1),0, ID.LifeBonus, Game.WIDTH / 30, Game.WIDTH / 30));
            }
            if(nazwa_obiektu.equals("Player")){
                handler.addObject(new Player(Integer.valueOf(odczyt.nextLine()) * (Game.WIDTH / 30 - 1), Integer.valueOf(odczyt.nextLine())* (Game.HEIGHT / 22 - 1), configurator.getLivesAmount(), configurator.getBallType(), ID.Player, Game.WIDTH / 15, Game.WIDTH / 15, handler, configurator));
           System.out.println("player");
            }
        }
    }
}
