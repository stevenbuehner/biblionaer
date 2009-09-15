package timer;

import main.Biblionaer;

public class TippJokerCountdown extends Countdown {

	public TippJokerCountdown(boolean starteSofort) {
		super( starteSofort, Biblionaer.meineEinstellungen.getTippJokerZeitInSekunden() );
		this.doEverySecondTimerRuns();
	}

	@Override
	protected void doEverySecondTimerRuns() {
		String ausgabe;

		switch (this.remainingTime) {
			case 3:
				ausgabe = "DREI";
				break;
			case 2:
				ausgabe = "ZWEI";
				break;
			case 1:
				ausgabe = "EINS";
				break;
			case 0:
				ausgabe = "NULL";
				break;
			default:
				ausgabe = "Sie haben noch " + Integer.toString( this.remainingTime )
						+ " Sekunden Zeit, um obige Bibelstelle nachzuschlagen.";
				break;
		}

		Biblionaer.meinWindowController.setStatusText( ausgabe );

	}

	@Override
	protected void doWhenCountdownFinished() {

		Biblionaer.meineSteuerung.tippJokerZeitAbgelaufen();
		try {
			this.finalize();
		}
		catch (Throwable e) {
			e.printStackTrace();
			Biblionaer.meineKonsole.println(
					"TippJokerCountdown konnte sich nicht selbst lšschen!", 2 );
		}
	}
}
