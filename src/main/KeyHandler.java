// Gestione dell'input da tastiera
package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public static boolean up, down, left, right;
    // Pause gestisce la pausa, music la scelta dell'utente di (non) riprodurre audio
    public static boolean pause = false, music = true;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Se premo FRECCIA SU o W
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = true;

        // Se premo FRECCIA GIÙ o S
        else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = true;

        // Se premo FRECCIA SX o A
        else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = true;

        // Se premo FRECCIA DX o D
        else if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = true;

        // Se premo SPAZIO o ESC, metto in pausa
        else if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE) {
            pause = !pause;

            // Gestione della musica in caso di messa in pausa
            if (pause) GamePanel.music.stop();
            else if (music){
                GamePanel.music.play(0, true);
                GamePanel.music.loop();
            }
            if (music) GamePanel.se.play(5, false);
        }

        // Se premo C, scelgo tra il tema scuro o chiaro
        else if (code == KeyEvent.VK_C) PlayManager.swapColors();

        // Se premo M, scelgo se riprodurre la musica e gli effetti sonori
        else if (code == KeyEvent.VK_M){
            music = !music;
            if (!music) GamePanel.music.stop();
            else{
                GamePanel.music.play(0, true);
                GamePanel.music.loop();
            }
        }
    }

    // Le funzioni qui sotto non servono per il gioco, quindi non sono implementate
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
