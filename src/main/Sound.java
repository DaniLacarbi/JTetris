// Serve per il suono
package main;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {
    Clip musicClip;
    URL[] url = new URL [10];

    public Sound() {
        // Ogni file audio nella cartella 'res' è associata a un elemento dell'array 'url'
        url[0] = getClass().getResource("/white-labyrinth-active.wav");
        url[1] = getClass().getResource("/delete line.wav");
        url[2] = getClass().getResource("/gameover.wav");
        url[3] = getClass().getResource("/rotation.wav");
        url[4] = getClass().getResource("/touch floor.wav");
        url[5] = getClass().getResource("/pause.wav");
    }

    public void play(int i, boolean music) {
        try{
            // Ottengo dal nome del file lo stream audio e lo assegno a 'clip'
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            // Se ho appena passato un file di background, lo assegno a 'musicClip'
            if (music) musicClip = clip;

            // Avvio lo stream. Funzione sotto copiata così com'è
            clip.open(ais);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });

            // Chiudo lo stream e parte l'audio
            ais.close();
            clip.start();
        } catch (Exception _) {}
    }

    // Riproduce la canzone in loop, solo se è di background
    public void loop() {musicClip.loop(Clip.LOOP_CONTINUOUSLY);}

    // Ferma la riproduzione
    public void stop(){
        musicClip.stop();
        musicClip.close();
    }
}
