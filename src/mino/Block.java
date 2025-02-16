// Singolo blocco alla base di ogni Tetramino
package mino;

import java.awt.*;

public class Block extends Rectangle {
    // Proprietà del singolo blocco
    public int x, y;
    public static int SIZE = 30;
    public Color c;

    // Assegnamento del colore
    public Block(Color c) {this.c = c;}

    // Disegno sullo schermo del blocco
    public void draw(Graphics g){
        g.setColor(c);
        g.fillRect(x, y, SIZE, SIZE);
    }
}
