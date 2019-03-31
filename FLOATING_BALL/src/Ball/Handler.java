package Ball;



import java.awt.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

/**
 * Klasa na biezaco obslugujaca obiekty interaktywne(ich wyswietlanie oraz w przyszlosci: zachowanie)
 * HUD
 * oraz tworzenie i wyswietlanie poziomow.
 *
 */
public class Handler {

    public LinkedList<GameObject> object = new LinkedList<>();
    private LinkedList<HUD> hud = new LinkedList<>();
    public static Configurator configurator;
    private Random generator;
    private boolean paused;
    public static int tempHeight = Game.HEIGHT;
    public static int tempWidth = Game.WIDTH;
    private Texture tex = Game.getInstance();
    private int SIZE = Game.HEIGHT;
    private boolean blocked = true;
    private Socket serverSocket;
    public LevelLoader levelLoader;


    public Handler() {
        configurator = new Configurator(0, "0", 0, 0, 0, 0, 0);
        configurator.loadProperties();
        levelLoader = new LevelLoader(this, configurator);
        generator = new Random();
        paused = false;
    }


    public static Configurator getConfiguratorInstance() {
        return configurator;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPauseStatus(boolean p) {
        paused = p;
    }

    public void tick() {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);

            if ((tempHeight != Game.HEIGHT || tempWidth != Game.WIDTH) && (!blocked)) {
                if (Game.WIDTH > Game.HEIGHT) {
                    SIZE = Game.HEIGHT;
                } else {
                    SIZE = Game.WIDTH;
                }
                for (int j = 0; j < object.size(); j++) {
                    GameObject tempObject2 = object.get(j);
                    tempObject2.setX(tempObject2.getX() * Game.WIDTH / tempWidth);
                    tempObject2.setY(tempObject2.getY() * Game.HEIGHT / tempHeight);
                    if (tempObject2.getId().equals(ID.Player) || tempObject2.getId().equals(ID.Goal)) {
                        tempObject2.setWidth(SIZE / 11);
                        tempObject2.setHeight(SIZE / 11);
                    } else if (tempObject2.getId().equals(ID.Obstacle) || tempObject2.getId().equals(ID.LifeBonus)) {
                        tempObject2.setWidth(SIZE / 22);
                        tempObject2.setHeight(SIZE / 22);
                    }
                }
            }
            tempHeight = Game.HEIGHT;
            tempWidth = Game.WIDTH;
            tempObject.tick();
            blocked = false;
        }
    }


    public void render(Graphics g) {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }
        for (int i = 0; i < hud.size(); i++) {
            HUD tempHud = hud.get(i);
            tempHud.render(g);
        }
    }

    public void LoadHUD() {
        addHudElement(new LivesBelt(0, 0, ID.LivesBelt, configurator));
        addHudElement(new ScoreBelt(0, 0, ID.ScoreBelt, configurator));
        addHudElement(new LevelBelt(0, 0, ID.LevelBelt));
        addHudElement(new FpsBelt(0, 0, ID.FpsBelt));
        addHudElement(new PauseSign(0, 0, ID.PauseSign, this));
    }



    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void addHudElement(HUD hud) {
        this.hud.add(hud);
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }

    public void switchLevel() {
        clearLevel();
        switch (Game.LEVEL) {
            case 1:
                Game.LEVEL++;
                levelLoader.LoadLevel();
                Game.LEVEL--;
                break;
            case 2:
                Game.LEVEL++;
                levelLoader.LoadLevel();
                Game.LEVEL--;
                break;
            case 3:
                Game.LEVEL++;
                levelLoader.LoadLevel();
                Game.LEVEL--;
                break;
            case 4:
                Game.LEVEL++;
                levelLoader.LoadLevel();
                Game.LEVEL--;
                break;
            case 5:
                Game.LEVEL++;
                levelLoader.LoadLevel();
                Game.LEVEL--;
                break;
            case 6:
                Game.LEVEL++;
                levelLoader.LoadLevel();
                Game.LEVEL--;
                break;
        }
        Game.LEVEL++;
    }

    public void clearLevel() {
        object.clear();
    }


}
