package Ball;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

/**
 * Glowna klasa gry. Zawiera:
 * konstruktor inicjalizujacy okno głowne i pierwsze grafiki,
 * funkcje startująca i zatrzymująca watek,
 * petle dzialania gry,
 * renderer grafki.
 */
public class Game extends Canvas implements Runnable {

    public static int WIDTH=960, HEIGHT = WIDTH / 12 * 9;
    public static int LEVEL = 1;
    public int frames = 0;
    public static int FROZENFPS = 0;
    private int frozenScore = 0;
    private int score = 0;

    public static BufferedImage background;

    private Thread thread;
    private boolean running = false;
    private static Handler handler;
    static Texture tex;
    private Configurator configurator;
    private Menu menu;
    private static Scores scores;
    private static KeyInput keyInput;
    private Server server = new Server();
    public int avaliablelevel;
    Socket serversocket;

    /**
     * Enum zawierajacy mozliwe stany gry
     */
    public enum STATE{
        Menu,
        Game,
        Help,
        Chooser,
        Hiscores,
        DifficultyChooser
    }

    public static STATE gameState = STATE.Menu;

    /**
     * Konstruktor klasy Game
     */
    public Game() {
        serversocket=server.connectToServer() ;
        if(serversocket!=null){
        avaliablelevel=server.avaliablelevels(serversocket);
        System.out.println("Dostepne: "+ avaliablelevel + " levele");
        server.getLevel();
        server.getHighlist(serversocket);
        server.logout();}

        handler = new Handler();
        tex = new Texture();
        configurator = new Configurator();
        scores = new Scores();
        menu = new Menu(this, handler);
        keyInput = new KeyInput(handler, configurator);
        BufferedImageLoader loader = new BufferedImageLoader();
        background = loader.loadImage("/bg4.jpg"); //tło
        this.addKeyListener(keyInput);
        this.addMouseListener(menu);
        new Window(WIDTH, HEIGHT, "Floating ball", this);

    }

    /**
     * Start watku
     */
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Zatrzymanie watku
     */
    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Game loop + zegar + zliczanie klatek na sekunde
     */
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
                if(handler.isPaused()==false) {
                    score = configurator.getScoreAmount();
                    if (score > 0) {
                        score--;
                    }
                      configurator.setScoreAmount(score);
                }
            }
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                FROZENFPS = frames;
                frames = 0;
            }
        }
        stop();
    }

    /**
     * wykonuje instrukcje z kazdym tyknieciem zegara. Aktualizuje parametry klas handler i menu.
     */
    private void tick(){
        if (gameState == STATE.Game) {
            handler.tick();
        } else if (gameState == STATE.Menu){
            menu.tick();
        }
        if (configurator.getLivesAmount() == 0 || LEVEL == 7) {
            handler.clearLevel();
            String playerName = JOptionPane.showInputDialog("Enter your nickname");
            frozenScore = (int)(score*configurator.getGameDifficulty());
            scores.setScoreAndNickname(frozenScore, playerName);
            serversocket =server.connectToServer();
            if(serversocket!=null){
                server.add_score(frozenScore,playerName,serversocket);
                server.getHighlist(serversocket);
                server.logout();
            }
            gameState = STATE.Hiscores;
            LEVEL = 1;
            configurator.setLivesAmount(20);
            configurator.setScoreAmount(0);
        }
    }

    /**
     * wykonuje instrukcje z kazdym tyknieciem zegara. Renderuje na biezaco wyswietlanie grafiki
     */
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(background, 0, 0, this);

        if (gameState == STATE.Game) {
            handler.render(g);
        } else if (gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.Hiscores || gameState == STATE.Chooser || gameState ==STATE.DifficultyChooser){
           menu.render(g);
        }


        g.dispose();
        bs.show();
    }

    /**
     * gettery zwracajace instancje obiektow w klasie Game
     * @return
     */
    public static Texture getInstance(){ return tex; }
    public static Handler getHandlerInstance() {return handler;}
    public static Scores getScoresInstance() {return scores;}
    public static KeyInput getKeyInputInstance() {return keyInput;}

    /**
     * funkcja main programu
     * @param args
     */
    public static void main(String[] args){
        new Game();
    }



}
