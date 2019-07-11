package Audio;

import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;

public class Trilha {

    final String arquivo1 = "src/Resource/skyrim.mp3";
    final String arquivo2 = "src/Resource/mar.mp3";
    final String arquivo3 = "src/Resource/missil.mp3";
    final String arquivo4 = "src/Resource/explosion.mp3";
    private Som mp3;

    public Trilha(int valor) throws FileNotFoundException, JavaLayerException {
        switch (valor) {
            case 1:
                this.mp3 = new Som(arquivo1);
                this.mp3.play();
                break;
            case 2:
                this.mp3 = new Som(arquivo2);
                this.mp3.play();
                break;
            case 3:
                this.mp3 = new Som(arquivo3);
                this.mp3.play();
                break;
            default:
                this.mp3 = new Som(arquivo4);
                this.mp3.play();
                break;
        }
    }

    public void play() throws FileNotFoundException, JavaLayerException {
        this.getMp3().play();
    }

    public void stop() {
        this.getMp3().close();
    }

    /**
     * @return the mp3
     */
    public Som getMp3() {
        return mp3;
    }

}
