package Audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Som {
	private String arquivo;
	private Player player;

	public Som(String arquivo) {
		setArquivo(arquivo);
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void close() {
		if (this.player != null)
		getPlayer().close();

	}

	public void play() throws FileNotFoundException, JavaLayerException {
		FileInputStream fileInputStream = new FileInputStream(arquivo);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				fileInputStream);
		player = new Player(bufferedInputStream);

		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}.start();
	}
}
