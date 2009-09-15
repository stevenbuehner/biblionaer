package timer;

import main.Biblionaer;

public class PuplikumsJokerCountdown extends Countdown {

	public PuplikumsJokerCountdown(boolean starteSofort) {
		super( starteSofort, Biblionaer.meineEinstellungen.getPuplikumsJokerZeitInSekunden() );
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
						+ " Sekunden, EINE Person um Hilfe zu bitten.";
				break;
		}

		Biblionaer.meinWindowController.setStatusText( ausgabe );
	}

	@Override
	protected void doWhenCountdownFinished() {

		Biblionaer.meineSteuerung.puplikumsJokerZeitAbgelaufen();
		try {
			this.finalize();
		}
		catch (Throwable e) {
			e.printStackTrace();
			Biblionaer.meineKonsole.println(
					"PuplikumsKokerCountdown konnte sich nicht selbst lšschen!", 2 );
		}
	}
}
