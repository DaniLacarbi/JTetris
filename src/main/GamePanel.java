// Gestione della finestra
package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // Dimensioni della finestra
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;

    // FPS di Gioco
    final int FPS = 60;

    // Processi del gioco
    Thread gameThread;
    PlayManager playManager;

    // Gestione effetti musicali
    public static Sound music = new Sound();
    public static Sound se = new Sound();

    public GamePanel(){
        // Dimensione e colore della finestra
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(PlayManager.background); // Default: nero, possibile: bianco
        this.setLayout(null);

        // Gestione della tastiera
        this.addKeyListener (new KeyHandler());
        this.setFocusable (true);

        // Creazione del gestore del gioco
        playManager = new PlayManager();
    }

    public void launchGame(){
        // Creazione e avvio del thread
        gameThread = new Thread(this);
        gameThread.start();

        // Musica di sottofondo
        music.play(0, true);
        music.loop();
    }

    @Override
    // 'Game Loop'. Questo l'ho copiato pari pari
    public void run() {
        double delta = 0;
        double drawInterval = 1000000000/FPS;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update(){
        // Se non sono in pausa né ho perso, continuo ad aggiornare
        if (!KeyHandler.pause && !PlayManager.gameOver){
            playManager.update();
        }

        // In caso cambi lo sfondo, lo aggiorno
        setBackground(PlayManager.background);
    }

    public void paint(Graphics g){
        // Disegno sulla finestra gli elementi del gioco
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Richiamo al gestore del gioco
        playManager.draw(g2d);
    }
}
