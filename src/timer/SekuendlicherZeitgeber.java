package timer;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public class SekuendlicherZeitgeber {

	public Timer timer;

	public SekuendlicherZeitgeber(ActionListener al) {
		this.timer = new Timer(1000, al);
		timer.setRepeats(true); // Standard
	}

	public void starteZeitgeber() {
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	public void stoppeZeitgeber() {
		if (timer.isRunning()) {
			timer.stop();
		}
	}

}
