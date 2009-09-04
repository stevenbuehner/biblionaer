package timer;

import java.util.TimerTask;

import main.Biblionaer;

public class TippJokerTimer extends TimerTask{

	@Override
	public void run() {
		Biblionaer.meineSteuerung.tippJokerZeitAbgelaufen();
	}

}
