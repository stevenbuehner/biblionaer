package window;

import interfaces.BackendWindow;
import interfaces.FrontendWindow;
import interfaces.QuizFenster;

import java.awt.image.VolatileImage;
import java.util.Iterator;
import java.util.LinkedList;

import main.Biblionaer;
import quiz.Quizfrage;

public class WindowController implements QuizFenster, BackendWindow, FrontendWindow {

	protected LinkedList<QuizFenster>	meineQuizFenster;
	protected QuizFenster				frontendFenster	= null;
	protected QuizFenster				backendFenster	= null;

	/**
	 * Konstruktor
	 */
	public WindowController() {
		meineQuizFenster = new LinkedList<QuizFenster>();
	}

	/**
	 * Neues Fenster zum Controller hinzufägen.
	 * 
	 * @param neuesFenster
	 */
	public void addQuizFenster(QuizFenster neuesFenster) {
		if ( this.meineQuizFenster.contains( neuesFenster ) ) {
			Biblionaer.meineKonsole
					.println(
							"Es wurde versucht ein Fenster das zweite Mal zum Controller hinzuzufägen!!",
							2 );
		}
		else {
			meineQuizFenster.addLast( neuesFenster );
			Biblionaer.meineSteuerung.windowSituationHasChanged();
		}
	}

	/**
	 * Neues Frontendfenster zum Controller hinzufägen.
	 * 
	 * @param neuesFenster
	 */
	public void addFrontendFenster(QuizFenster neuesFenster) {
		boolean isFrontend = false;

		if ( neuesFenster != null ) {
			Class<?>[] interfaces = neuesFenster.getClass().getInterfaces();
			for (int i = 0; i < interfaces.length; ++i) {
				if ( interfaces[i].getName().equals( "interfaces.FrontendWindow" ) ) {
					isFrontend = true;
					break;
				}
			}

			if ( isFrontend ) {
				this.addQuizFenster( neuesFenster );
				this.frontendFenster = neuesFenster;
			}
			else {
				Biblionaer.meineKonsole
						.println(
								"Es wurde versucht ein Fenster das als FrontendFenster zum Controller hinzuzufägen - doch es enthält nicht die nätigen Interfaces!!",
								2 );
			}
		}
	}

	/**
	 * Neues Backend (Administrationsfenster) zum Controller hinzufägen.
	 * 
	 * @param neuesFenster
	 */
	public void addBackendFenster(QuizFenster neuesFenster) {
		boolean isBackend = false;

		if ( neuesFenster != null ) {
			Class<?>[] interfaces = neuesFenster.getClass().getInterfaces();
			for (int i = 0; i < interfaces.length; ++i) {
				if ( interfaces[i].getName().equals( "interfaces.BackendWindow" ) ) {
					isBackend = true;
					break;
				}
			}

			if ( isBackend ) {
				this.addQuizFenster( neuesFenster );
				this.backendFenster = neuesFenster;
			}
			else {
				Biblionaer.meineKonsole
						.println(
								"Es wurde versucht ein Fenster das als FrontendFenster zum Controller hinzuzufägen - doch es enthält nicht die nätigen Interfaces!!",
								2 );
			}
		}
	}

	/**
	 * Fenster aus dem Controller läschen Gibt true zuräck, wenn es erfolgreich
	 * entfernt wurde.
	 * 
	 * @param zuLoeschendeFensterID
	 * @return true, wenn Fenster erfolgreich entfernt
	 */
	public boolean removeQuizFenster(QuizFenster zuLoeschendeFensterID) {
		return meineQuizFenster.remove( zuLoeschendeFensterID );
	}

	public void removeAllQuizFensters() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			QuizFenster dasFenster = iter.next();
			dasFenster.killYourSelf();
			iter.remove();
		}

		this.backendFenster = null;
		this.frontendFenster = null;
	}

	public QuizFenster getFrontendFenster() {
		return frontendFenster;
	}

	public void setFrontendFenster(QuizFenster frontendFenster) {
		this.frontendFenster = frontendFenster;
	}

	public QuizFenster getBackendFenster() {
		return backendFenster;
	}

	public void setBackendFenster(QuizFenster backendFenster) {
		this.backendFenster = backendFenster;
	}

	// * Ab hier die Mehtoden die an die Fensterobjekte weitergegeben werden
	public void playFrageFalsch() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().playFrageFalsch();
		}
	}

	public void playFrageRichtig() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().playFrageRichtig();
		}
	}

	public void playSpielGewonnen() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().playSpielGewonnen();
		}
	}

	public void resetAlleJoker() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().resetAlleJoker();
		}
	}

	public void setAnimationAktiviert(boolean aktiviert) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAnimationAktiviert( aktiviert );
		}
	}

	public void setAntwortFeld1Falsch() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld1Falsch();
		}
	}

	public void setAntwortFeld1Markiert() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld1Markiert();
		}
	}

	public void setAntwortFeld1Normal() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld1Normal();
		}
	}

	public void setAntwortFeld1Richtig() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld1Richtig();
		}
	}

	public void setAntwortFeld1Sichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld1Sichtbar( sichtbar );
		}
	}

	public void setAntwortFeld2Falsch() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld2Falsch();
		}
	}

	public void setAntwortFeld2Markiert() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld2Markiert();
		}
	}

	public void setAntwortFeld2Normal() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld2Normal();
		}
	}

	public void setAntwortFeld2Richtig() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld2Richtig();
		}
	}

	public void setAntwortFeld2Sichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld2Sichtbar( sichtbar );
		}
	}

	public void setAntwortFeld3Falsch() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld3Falsch();
		}
	}

	public void setAntwortFeld3Markiert() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld3Markiert();
		}
	}

	public void setAntwortFeld3Normal() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld3Normal();
		}
	}

	public void setAntwortFeld3Richtig() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld3Richtig();
		}
	}

	public void setAntwortFeld3Sichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld3Sichtbar( sichtbar );
		}
	}

	public void setAntwortFeld4Falsch() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld4Falsch();
		}
	}

	public void setAntwortFeld4Markiert() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld4Markiert();
		}
	}

	public void setAntwortFeld4Normal() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld4Normal();
		}
	}

	public void setAntwortFeld4Richtig() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld4Richtig();
		}
	}

	public void setAntwortFeld4Sichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFeld4Sichtbar( sichtbar );
		}
	}

	public void setAntwortFelderSichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortFelderSichtbar( sichtbar );
		}
	}

	public void setAntwortfelderFalsch() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortfelderFalsch();
		}
	}

	public void setAntwortfelderMariert() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortfelderMariert();
		}
	}

	public void setAntwortfelderNormal() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortfelderNormal();
		}
	}

	public void setAntwortfelderRichtig() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setAntwortfelderRichtig();
		}
	}

	public void setCountdownText(String text) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setCountdownText( text );
		}
	}

	public void setFiftyJokerBenutzt(boolean benutzt) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setFiftyJokerBenutzt( benutzt );
		}
	}

	public void setFiftyJokerSichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setFiftyJokerSichtbar( sichtbar );
		}
	}

	public void setFrageAnzuzeigen(Quizfrage frage, boolean mitAnimation) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setFrageAnzuzeigen( frage, mitAnimation );
		}
	}

	public void setFrageFeldSichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setFrageFeldSichtbar( sichtbar );
		}
	}

	public void setPublikumsJokerBenutzt(boolean benutzt) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setPublikumsJokerBenutzt( benutzt );
		}
	}

	public void setPublikumsJokerSichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setPublikumsJokerSichtbar( sichtbar );
		}
	}

	public void setStatusText(String text) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setStatusText( text );
		}
	}

	public void setTippJokerBenutzt(boolean benutzt) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setTippJokerBenutzt( benutzt );
		}
	}

	public void setTippJokerSichtbar(boolean sichtbar) {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().setTippJokerSichtbar( sichtbar );
		}
	}

	public void killYourSelf() {
		// Dieses Teil hier killt sich nicht :-) - Keine Chance
	}

	// Funktionen der FrontendFenster
	public void setFrontendScreenImage(VolatileImage screen) {
		// Brauchen wir hier nicht
	}

	public void setBildschirmSchwarz(boolean schwarzerBildschirm) {
		// Nur an die Frontendfenster senden
		if ( this.frontendFenster != null ) {
			((FrontendWindow) this.frontendFenster).setBildschirmSchwarz( schwarzerBildschirm );
		}
	}

	// * Ab hier Funktionen der BackendFenster
	public void setFrageKomplett(Quizfrage frage) {
		if ( this.backendFenster != null ) {
			((BackendWindow) this.backendFenster).setFrageKomplett( frage );
		}
	}

	public void playStarteSpiel() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().playStarteSpiel();
		}
	}

	public void spielBeendet() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().spielBeendet();
		}
	}

	public void spielGestartet() {
		Iterator<QuizFenster> iter = meineQuizFenster.iterator();
		while (iter.hasNext()) {
			iter.next().spielGestartet();
		}
	}

}
