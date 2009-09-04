package timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class Countdown {

	Timer	countdownTimer;
	int		remainingTime;

	public Countdown(boolean starteSofort) {
		this( starteSofort, 30 ); // Starte mit Standarzeit, 30 Sekunden
	}

	public Countdown(boolean starteSofort, int zeitInSekunden) {
		this.remainingTime = zeitInSekunden;

		countdownTimer = new Timer( 1000, new CountdownTimerListener() );
		countdownTimer.setRepeats( true ); // Standard

		if ( starteSofort ) {
			countdownTimer.start();
		}
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	/**
	 * Startet den Countdown
	 */
	public void starteCountdown() {
		if ( !countdownTimer.isRunning() ) {
			countdownTimer.start();
		}
	}

	/**
	 * Stoppt den Countdown
	 */
	public void stoppeCountdown() {
		if ( countdownTimer.isRunning() ) {
			countdownTimer.stop();
		}
	}

	/**
	 * Wenn der Countdown lŠuft und bei 0 angelang ist, wird diese Methode
	 * aufgerufen.
	 */
	protected abstract void doWhenCountdownFinished();

	/**
	 * Diese Methode wird nur aufgerufen, wenn der Countdown lŠuft und noch
	 * nicht bei 0 angelant ist. Diese Methode wird beim erreichen der 0 nicht
	 * mehr aufgerufen. Siehe stattdessen doWhenCountdownFinished().
	 */
	protected abstract void doEverySecondTimerRuns();

	class CountdownTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if ( --remainingTime > 0 ) {
				doEverySecondTimerRuns();
				// label.setText(String.valueOf(remainingTime));
			}
			else {
				doWhenCountdownFinished();
				// label.setText("Time's up!");
				countdownTimer.stop();
			}
		}
	}
}
