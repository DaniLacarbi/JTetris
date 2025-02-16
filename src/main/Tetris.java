// Nonostante il nome, è il file main del programma
// Guida che ho seguito (ma non copiato al 100%):
// https://www.youtube.com/watch?v=N1ktYfszqnM

package main;

import javax.swing.JFrame;

public class Tetris {
    public static void main(String[] args) {
        // Titolo della finestra
        JFrame window = new JFrame("Tetris ma copiato da un video su YT perché cercarlo su Internet era troppo difficile");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Creazione della finestra con la classe GamePanel
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Inizio del gioco
        gamePanel.launchGame();
    }
}