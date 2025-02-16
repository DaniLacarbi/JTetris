// I 7 pezzi con cui si gioca a Tetris
// NB: I file dei singoli Tetramini non sono commentati, in particolare le 4 direzioni. Prima o poi rimedierò
package mino;

import main.*;
import java.awt.*;

public class Mino {
    // Un Tetramino è un array di 4 'Blocks'
    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];

    // Proprietà per calcolare quanto veloce deve scendere e se può muoversi/ruotare
    int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, downCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivationCounter = 0;

    // Creazione dei 4 blocchi
    public void create(Color c){
        for (int i = 0; i < 4; i++) {
            b[i] = new Block(c);
            tempB[i] = new Block(c);
        }
    }

    // Impostazione dei blocchi nello spazio. Definita separatamente per ognuno dei 7 Tetramini
    public void setXY(int x, int y){}

    // Aggiornamento della posizione solo se non è in collisione
    public void updateXY(int direction){
        checkRotationCollision();
        if (!leftCollision && !rightCollision && !downCollision) {
            this.direction = direction;
            for (int i = 0; i < 4; i++) {
                b[i].x = tempB[i].x;
                b[i].y = tempB[i].y;
            }
        }
    }

    // Funzioni per ruotare i Tetramini. Definite separatamente per ognuno dei 7 Tetramini
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}

    // Controllo se il Tetramino è in collisione
    public void checkMovementCollision(){
        leftCollision = false;
        rightCollision = false;
        downCollision = false;

        // Controllo collisione con blocchi fermi
        checkStaticBlocksCollision();

        for (int i = 0; i < 4; i++) {
            // Controllo collisione a sinistra...
            if (b[i].x == PlayManager.left_x){
                leftCollision = true;
                KeyHandler.left = false;
            }

            // ... a destra...
            if (b[i].x + Block.SIZE == PlayManager.right_x){
                rightCollision = true;
                KeyHandler.right = false;
            }

            // ... e in basso
            if (b[i].y + Block.SIZE == PlayManager.bottom_y){
                downCollision = true;
                KeyHandler.down = false;
            }
        }
    }

    // Controllo della collisione prima di eseguire la rotazione
    // Uguale a prima, solo che al posto di controllare con 'b', si controlla con 'tempB'
    public void checkRotationCollision(){
        leftCollision = false;
        rightCollision = false;
        downCollision = false;
        checkStaticBlocksCollision();
        for (int i = 0; i < 4; i++) {
            if (tempB[i].x < PlayManager.left_x){
                leftCollision = true;
                KeyHandler.left = false;
            }
            if (tempB[i].x + Block.SIZE > PlayManager.right_x){
                rightCollision = true;
                KeyHandler.right = false;
            }
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y){
                downCollision = true;
                KeyHandler.down = false;
            }
        }
    }

    // Controllo collisione con i blocchi fermi
    private void checkStaticBlocksCollision(){
        // Ciclo tra tutti i blocchi fermi
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // Controllo per ogni singolo blocco del Tetramino
            for (int j = 0; j < b.length; j++) {
                if (b[j].x == targetX && b[j].y + Block.SIZE == targetY) downCollision = true;
                if (b[j].x - Block.SIZE == targetX && b[j].y == targetY) leftCollision = true;
                if (b[j].x + Block.SIZE == targetX && b[j].y == targetY) rightCollision = true;
            }
        }
    }

    // Aggiorno lo stato del Tetramino
    public void update() {
        // Se è in disattivazione, chiamo la funzione apposita
        if (deactivating) deactivating();

        // Se ho premuto su, ruoto con le 4 funzioni apposite e riproduco un suono
        if (KeyHandler.up){
            switch (direction) {
                case 1: getDirection2(); break;
                case 2: getDirection3(); break;
                case 3: getDirection4(); break;
                case 4: getDirection1(); break;
                default: break;
            }
            KeyHandler.up = false;
            if (KeyHandler.music) GamePanel.se.play(3, false);
        }

        // Controllo la collisione
        checkMovementCollision();

        // Se premo giù e non sono in fondo, scendo di un blocco
        if (KeyHandler.down && !downCollision){
            for (int i = 0; i < 4; i++) b[i].y += Block.SIZE;
            autoDropCounter = 0;
            KeyHandler.down = false;
        }

        // Se premo sx o dx, mi sposto
        if (KeyHandler.left && !leftCollision){
            for (int i = 0; i < 4; i++) b[i].x -= Block.SIZE;
            KeyHandler.left = false;
        }

        if (KeyHandler.right && !rightCollision){
            for (int i = 0; i < 4; i++) b[i].x += Block.SIZE;
            KeyHandler.right = false;
        }

        // Se tocco giù, il blocco è in disattivazione
        if (downCollision) deactivating = true;

        // Altrimenti, scendo costantemente
        else {
            autoDropCounter++;
            if (autoDropCounter == PlayManager.dropInterval){
                for (int i = 0; i < 4; i++) b[i].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }

    // Procedura di disattivazione
    public void deactivating(){
        deactivationCounter++;

        // Se il counter di disattivazione raggiunge la metà del 'dropInterval', il blocco si ferma
        if (deactivationCounter == PlayManager.dropInterval/2){
            deactivationCounter = 0;

            // Prima di disattivarlo lascio che possa muoversi ancora un attimo
            checkMovementCollision();
            if (downCollision){
                active = false;
                if (KeyHandler.music) GamePanel.se.play(4, false);
            }
        }
    }

    // Disegno le modifiche effettuate in 'update'
    public void draw(Graphics2D g2){
        g2.setColor(b[0].c);
        int margin = 0;
        for (int i = 0; i < 4; i++) {
            g2.fillRect(b[i].x+margin, b[i].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        }
    }

}
