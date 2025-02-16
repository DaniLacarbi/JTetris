// Gestione delle dinamiche principali di gioco
package main;

import mino.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    // Area di gioco
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Gestione Tetramini
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>() ;

    // Controllo discesa Tetramino
    public static int dropInterval = 60;

    // Effetto eliminazione riga
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    // Variabile Game Over
    public static boolean gameOver = false;

    // Gestione tema chiaro/scuro
    public static Color background = Color.BLACK;
    public static Color accent = Color.WHITE;

    // Variabili statistiche
    int level = 1;
    int lines = 0;
    int score = 0;

    public PlayManager(){
        // Limiti schermo di gioco
        left_x = 50;
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y +HEIGHT;

        // Punto di patenza del Tetramino
        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        // Punto dove viene mostrato il prossimo Tetramino
        NEXT_MINO_X = right_x+140;
        NEXT_MINO_Y = top_y + 500;

        // Come vengono 'scelti' i Tetramini
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    // Funzione per passare dal tema chiaro a quello scuro
    // Di fatto inverte 'background' e 'accent'
    public static void swapColors(){
        Color c = background;
        background = accent;
        accent = c;
    }

    // Scelta pseudocasuale del prossimo Tetramino
    private Mino pickMino(){
       Mino mino = null;
       int i = new Random().nextInt(7);
       mino = switch (i) {
            case 0 -> new Mino_L1();
            case 1 -> new Mino_L2();
            case 2 -> new Mino_Z1();
            case 3 -> new Mino_Z2();
            case 4 -> new Mino_Bar();
            case 5 -> new Mino_Square();
            case 6 -> new Mino_T();
            default -> null;
        };
       return mino;
    }

    public void update() {
        // Aggiungi i Tetramino inattivi sul fondo
        if (!currentMino.active) {
            for (int i = 0; i < 4; i++) staticBlocks.add(currentMino.b[i]);

            // Se il Tetramino attuale comincia dove ce ne è già un altro, è game over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                gameOver = true;
                GamePanel.music.stop();
                if (KeyHandler.music) GamePanel.se.play(2, false);
            }

            // Il prossimo è quello attuale e ne viene scelto uno nuovo come prossimo e messi in posizione di partenza
            currentMino.deactivating = false;
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

            // Controlla se qualche riga va eliminata
            checkDelete();
        } else {
            // Se il Tetramino è in gioco, aggiorna la sua posizione
            currentMino.update();
        }
    }

    // Funzione per eliminare linee piene, e calcolo punteggio
    private void checkDelete(){
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        // Finché non abbiamo finito di esaminare l'area di gioco
        while (x < right_x && y < bottom_y){

            // Leggo l'array di blocchi statici. Se almeno uno corrisponde al blocco 'cursore', aumento il contatore
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) blockCount++;
            }

            // Mi sposto a destra
            x += Block.SIZE;

            // Se sono arrivato a fine riga, controllo alcune cose prima di procedere
            if (x == right_x){

                // Se il numero di blocchi individuati corrisponde a quello massimo possibile, cancello la riga
                if (blockCount == 12){

                    // Attivazione effetto cancellazione riga
                    effectCounterOn = true;
                    effectY.add(y);

                    // Rimuovo i blocchi
                    for (int i = staticBlocks.size()-1; i > -1; i--) {
                        if (staticBlocks. get(i).y == y) staticBlocks.remove(i);
                    }

                    // Aggiungo ai contatori le linee eliminate
                    lineCount++;
                    lines++;

                    // Calcolo punteggio in base al livello
                    score += 10 * level * lineCount;

                    // Aumento velocità di caduta dopo 10 linee eliminate. 1 è la più veloce, 60 la più lenta
                    if (lines%10 == 0 && dropInterval > 1){
                        level++;
                        if (dropInterval > 10) dropInterval -= 5;
                        else dropInterval -= 1;
                    }

                    // Per eventuali linee SOPRA quelle eliminate, le sposto verso il basso
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).y < y) staticBlocks.get(i).y += Block.SIZE;
                    }

                    // Riproduco eliminazione lines
                    if (KeyHandler.music) GamePanel.se.play(1, false);
                }

                // Reset variabili e spostamento in giù di una riga il blocco 'cursore'
                blockCount = 0;
                lineCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
    }

    public void draw(Graphics2D g2){
        int x, y;

        // Rettangolo di gioco
        g2.setColor(accent);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        // Attuale Tetramino
        if (currentMino != null) currentMino.draw(g2);

        // Rettangolo prossimo Tetramino
        g2.setColor(accent);
        x = right_x + 60;
        y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font ("Arial", Font. PLAIN, 30)) ;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON) ;
        g2.drawString("Prossimo", x+40, y+50);
        nextMino.draw(g2);

        // Rettangolo statistiche
        g2.setColor(accent);
        g2.drawRect(x, top_y, 200, 200);
        x += 30;
        y = top_y + 45;
        g2.drawString("Statistiche", x, y);
        x -= 15;
        y = top_y + 120;
        g2.drawString("Livello", x, y);
        g2.drawString("" + level, x+100, y); y += 30;
        g2.drawString("Linee", x, y);
        g2.drawString("" + lines, x+100, y); y += 30;
        g2.drawString("Punti", x, y);
        g2.drawString("" + score, x+100, y);

        // Rappresentazione tetramini già giocati
        for (int i = 0; i < staticBlocks.size(); i++) staticBlocks.get(i).draw(g2);

        // Effetto cancellazione riga
        if (effectCounterOn) {
            effectCounter++;
            g2.setColor(accent);
            for (Integer integer : effectY) {
                g2.fillRect(left_x, integer, WIDTH, Block.SIZE);
            }
            if (effectCounter == 20){
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // In caso di Game Over
        if (gameOver){
            g2.setColor(accent);
            g2.setFont(g2.getFont().deriveFont(50f));
            x = right_x + 30;
            y = top_y + 315;
            g2.drawString("Game over.", x, y);
        }

        // In caso di pausa...
        if (KeyHandler.pause && !gameOver){
            //... mostro la scritta 'In pausa...'
            g2.setColor(accent);
            g2.setFont(g2.getFont().deriveFont(50f));
            x = right_x + 50;
            y = top_y + 315;
            g2.drawString("In pausa...", x, y);

            //... e i comandi di gioco sullo schermo
            g2.setFont(g2.getFont().deriveFont(30f));
            x = left_x + 15;
            y = top_y + 45;
            g2.drawString("Comandi", x, y); y += 30;
            g2.drawString("SU/W: ruota", x, y); y += 30;
            g2.drawString("GIÙ/S: scende", x, y); y += 30;
            g2.drawString("SX e DX/A e D: sposta", x, y); y += 30;
            g2.drawString("ESC/SPAZIO: pausa", x, y); y += 30;
            g2.drawString("C: tema chiaro/scuro", x, y); y += 30;
            g2.drawString("M: modalità muto/sonoro", x, y);
        }

        // Se non sono in pausa e non sono in game over, mostro come mettere in pausa il gioco
        if (!KeyHandler.pause && !gameOver){
            g2.setColor(accent);
            g2.setFont(g2.getFont().deriveFont(30f));
            x = right_x + 75;
            y = top_y + 300;
            g2.drawString("ESC/SPAZIO:", x, y);
            x -= 20; y += 30;
            g2.drawString("pausa e comandi", x, y);
        }
    }
}
