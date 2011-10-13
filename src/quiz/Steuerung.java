package quiz;

import importer.XmlToSpiel;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import main.Biblionaer;
import timer.PuplikumsJokerCountdown;
import timer.TippJokerCountdown;
import window.Einstellungen;

public class Steuerung implements KeyListener {

	protected Spiel meinSpiel;

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	protected boolean game_running = true;
	protected boolean started = false;

	// Timer für den Tipp-Joker
	private TippJokerCountdown tippJokerTimer;
	// Timer für den Puplikums-Joker
	private PuplikumsJokerCountdown puplikumsJokerTimer;

	public Steuerung() {
		meinSpiel = null;
	}

	public void erstAufrufDerSteuerung() {
		// Startdialog
		/*
		 * int returnOptionDialog = JOptionPane .showOptionDialog( (Component)
		 * Biblionaer.meinWindowController.getFrontendFenster(),
		 * "Herzlich Willkommen zu \"Wer wird Biblionär\". Zu Beginn wird ein Standardspiel geladen. \nWeitere Spiele kännen äber das Menä geladen werden. Hierzu ist allerdings eine Verbindung zum Internet nätig. \nMit der Verwendung dieses Programmes stimmen Sie zu, nichts davon kommerziell zu verwenden.\n\nViel Spaä beim Spielen, \nIhr Biblionär-Team."
		 * , "Herzlich Willkommen", JOptionPane.OK_CANCEL_OPTION,
		 * JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.OK_OPTION);
		 * 
		 * if (returnOptionDialog == JOptionPane.CANCEL_OPTION ||
		 * returnOptionDialog == JOptionPane.CLOSED_OPTION) { System.exit(0); }
		 */
	}

	public void starteNeuesSpiel(File quizLocation) {
		XmlToSpiel dasFile = new XmlToSpiel(quizLocation);
		meinSpiel = dasFile.getSpiel();

		if (dasFile.getAnzahlFragen() > 0) {
			meinSpiel = dasFile.getSpiel();

			this.initialisiereNeuesSpiel();
		}
	}

	public void starteNeuesSpiel(URL quizLocation) {
		XmlToSpiel dasFile = new XmlToSpiel(quizLocation);
		if (dasFile.getAnzahlFragen() > 0) {
			meinSpiel = dasFile.getSpiel();

			this.initialisiereNeuesSpiel();
		}
	}

	private void initialisiereNeuesSpiel() {
		if (meinSpiel != null) {
			meinSpiel.starteSpiel();

			// Falls ein Timer noch läuft, beende ihn
			this.loescheAlleTimer();

			Biblionaer.meinWindowController.setStatusText(null);

			Biblionaer.meinWindowController.resetAlleJoker();
			Biblionaer.meinWindowController.setFrageFeldSichtbar(true);
			Biblionaer.meinWindowController.setAntwortFelderSichtbar(true);
			Biblionaer.meinWindowController.setAntwortfelderNormal();
			Biblionaer.meinWindowController.playStarteSpiel();

			Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), true);
			Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());

			Biblionaer.meinWindowController.spielGestartet();

		} else {
			Biblionaer.meineKonsole.println(
					"Es konnte kein neues Spiel aus der angegebenen Datei heraus gestartet werden.", 2);
		}

	}

	public int getStatus() {
		// Diese Funktion wurde noch nicht richtig in das Spiel integriert

		/*
		 * 0 = Initialisierung und Co 1 = Intro 2 = Menu anzeigen 3 = Spiel
		 * gestartet
		 */

		return 3;

	}

	public void keyPressed(KeyEvent e) {

		// Zum Testen
		// System.setProperty( "proxyHost",
		// Biblionaer.meineEinstellungen.getProxyHost() );
		// System.setProperty( "proxyPort",
		// Biblionaer.meineEinstellungen.getProxyPort() );
		// System.setProperty( "proxySet", "false" ); // Proxy aktivieren

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			// Quit
			System.exit(0);
		}

		else if (e.getKeyCode() == KeyEvent.VK_1 && Biblionaer.meineEinstellungen.getKonsolenModus() >= 4) {
			Biblionaer.meineEinstellungen.setQuizScreenModus(Einstellungen.quizScreenModusMultiWindow1FullScreen);
		} else if (e.getKeyCode() == KeyEvent.VK_2 && Biblionaer.meineEinstellungen.getKonsolenModus() >= 4) {
			// Wechsle in den Betriebsmodus Windowed Player - Das ist zum
			// Debuggen ganz nätzlich
			Biblionaer.meineEinstellungen.setQuizScreenModus(Einstellungen.quizScreenModusMultiWindow1Windowed);
		} else if (e.getKeyCode() == KeyEvent.VK_3 && Biblionaer.meineEinstellungen.getKonsolenModus() >= 4) {
			Biblionaer.meineEinstellungen.setQuizScreenModus(Einstellungen.quizScreenModusMultiWindow2FullScreen);
		} else if (e.getKeyCode() == KeyEvent.VK_4 && Biblionaer.meineEinstellungen.getKonsolenModus() >= 4) {
			Biblionaer.meineEinstellungen.setQuizScreenModus(Einstellungen.quizScreenModusSingleWindow);
		}

		else if (e.getKeyCode() == KeyEvent.VK_SPACE && Biblionaer.meineEinstellungen.darfGechetetWerden()) {
			// neue Fragen laden

			if (meinSpiel.setNaechsteFrage()) {
				Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), true);
				Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_P) {
			System.setProperty("proxySet", "false"); // Proxy aktivieren
		}

		if (e.getKeyCode() == KeyEvent.VK_T) {
			// Nur zum testen
			meinSpiel = new XmlToSpiel().getSpiel();
			this.initialisiereNeuesSpiel();
		}
	}

	public void klickAufAntwortFeld(int klickFeld) {
		Biblionaer.meineKonsole.println("Klick auf Antwortfeld " + klickFeld, 4);

		if (meinSpiel == null)
			return;

		if (meinSpiel.laeufDasSpiel()) {

			this.loescheAlleTimer();
			Biblionaer.meinWindowController.setStatusText("");

			meinSpiel.aktuelleFrageBeantwortet();

			if (meinSpiel.istAktuelleAntwort(klickFeld)) {
				// Frage richtig beantwortet
				klickAufRichtigeAntwort();
				Biblionaer.meinWindowController.playFrageRichtig();
			}

			else {
				// Frage falsch beantwortet
				klickAufFalscheAntwort(klickFeld);
				Biblionaer.meinWindowController.playFrageFalsch();
			}
		} else {
			// Trotzdem die richtige Antwort anzeigen ;-)
			zeigeRichtigeAntwortGelb();
		}

	}

	private void loescheAlleTimer() {

		if (tippJokerTimer != null) {
			tippJokerTimer.stoppeCountdown();
			tippJokerTimer = null;
		}

		if (puplikumsJokerTimer != null) {
			puplikumsJokerTimer.stoppeCountdown();
			puplikumsJokerTimer = null;
			Biblionaer.meinWindowController.setStatusText("");
		}
	}

	private void zeigeRichtigeAntwortGelb() {
		if (meinSpiel != null) {
			switch (meinSpiel.getAktuelleRichtigeAntwort()) {
			case 1:
				Biblionaer.meinWindowController.setAntwortFeld1Markiert();
				break;
			case 2:
				Biblionaer.meinWindowController.setAntwortFeld2Markiert();
				break;
			case 3:
				Biblionaer.meinWindowController.setAntwortFeld3Markiert();
				break;
			case 4:
				Biblionaer.meinWindowController.setAntwortFeld4Markiert();
				break;
			default:
				break;
			}
		}
	}

	private void zeigeRichtigeAntwortGruen() {
		if (meinSpiel != null) {
			switch (meinSpiel.getAktuelleRichtigeAntwort()) {
			case 1:
				Biblionaer.meinWindowController.setAntwortFeld1Richtig();
				break;
			case 2:
				Biblionaer.meinWindowController.setAntwortFeld2Richtig();
				break;
			case 3:
				Biblionaer.meinWindowController.setAntwortFeld3Richtig();
				break;
			case 4:
				Biblionaer.meinWindowController.setAntwortFeld4Richtig();
				break;
			default:
				break;
			}
		}
	}

	private void klickAufRichtigeAntwort() {
		if (meinSpiel.istGeradeLetzteFrage()) {
			// gewonnen
			Biblionaer.meinWindowController.setStatusText("GEWONNEN - Gratuliere");
			Biblionaer.meinWindowController.playSpielGewonnen();
			this.spielBeenden();
		} else {

			this.zeigeRichtigeAntwortGruen();
			try {
				Thread.sleep(2010);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			meinSpiel.setNaechsteFrage();
			// Play: RICHTIIIGGG ...
			Biblionaer.meinWindowController.setAntwortfelderNormal();
			Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), true);
			Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());

		}
	}

	private void klickAufFalscheAntwort(int klickFeldFalscheAntwort) {
		this.spielBeenden();
		Biblionaer.meinWindowController.setStatusText("Falsche Antwort - Spiel beendet");

		// Zeige im Frontend die Frage mit Bibelstelle an
		Quizfrage mitBibelstelle = this.meinSpiel.getAktuelleFrageAnzuzeigen();
		mitBibelstelle.setLoesungshinweis(this.meinSpiel.getAktuelleFrage().getLoesungshinweis());

		Biblionaer.meinWindowController.setFrageAnzuzeigen(mitBibelstelle, false);
		Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());
		Biblionaer.meinWindowController.playFrageFalsch();

		zeigeRichtigeAntwortGelb();

		switch (klickFeldFalscheAntwort) {
		case 1:
			Biblionaer.meinWindowController.setAntwortFeld1Falsch();
			break;
		case 2:
			Biblionaer.meinWindowController.setAntwortFeld2Falsch();
			break;
		case 3:
			Biblionaer.meinWindowController.setAntwortFeld3Falsch();
			break;
		case 4:
			Biblionaer.meinWindowController.setAntwortFeld4Falsch();
			break;
		default:
			Biblionaer.meineKonsole
					.println("Dieses Antwortfeld gibt es nicht (Steuerung - klickFeldFalscheAntwort)", 2);
			break;

		}
	}

	/**
	 * Zum Aufruf äber ein AdministratorPanel ...
	 */
	public void spielBeenden() {
		if (meinSpiel != null) {
			if (meinSpiel.laeufDasSpiel()) {
				meinSpiel.setEnde();
				Biblionaer.meinWindowController.spielBeendet();
			}
		}
	}

	public void tippJokerZeitAbgelaufen() {
		Biblionaer.meineKonsole.println("tippJokerZeitAbgelaufen() wurde auferufen", 4);

		if (meinSpiel != null) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			Biblionaer.meinWindowController.setStatusText("Zeit fär den Tippjoker abgelaufen - Spiel beendet! ...");
		}
	}

	public void puplikumsJokerZeitAbgelaufen() {
		Biblionaer.meineKonsole.println("puplikumsJokerZeitAbgelaufen() wurde auferufen", 4);

		if (meinSpiel != null) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			Biblionaer.meinWindowController
					.setStatusText("Zeit fär den Puplikumsjoker abgelaufen - Spiel beendet! ...");
		}
	}

	public void klickAufFrageFeld() {
		Biblionaer.meineKonsole.println("Klick auf Fragefeld", 4);
	}

	public void klickAufTippJoker() {
		Biblionaer.meineKonsole.println("Klick auf Tipp-Joker", 4);

		if (meinSpiel == null)
			return;

		if (meinSpiel.laeufDasSpiel() && !meinSpiel.tippJokerSchonVerwendet()) {
			Biblionaer.meinWindowController.setTippJokerBenutzt(true);
			meinSpiel.setTippJokerSchonVerwendet(true);
			tippJokerTimer = new TippJokerCountdown(true);

			Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), false);
			Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());

		} else {
			Biblionaer.meineKonsole.println("Tipp-Joker schon verwendet oder Spiel beendet.", 3);
		}
	}

	/**
	 * Setzt den Tipp-Joker zurück, egal ob er schon verwendet wurde oder nicht
	 * Anwendung in erster Linie aus dem Admin-Schirm
	 */
	public void resetTippJoker() {
		Biblionaer.meineKonsole.println("Klick auf Reset-Tipp-Joker", 4);

		if (meinSpiel == null) {
			return;
		}

		Biblionaer.meinWindowController.setTippJokerBenutzt(false);
		meinSpiel.setTippJokerSchonVerwendet(false);
		tippJokerTimer.stoppeCountdown();
		tippJokerTimer = null;

		Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), false);
		Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());
	}

	public void klickAufFiftyJoker() {
		Biblionaer.meineKonsole.println("Klick auf Fifty-Joker", 4);

		if (meinSpiel == null)
			return;

		if (meinSpiel.laeufDasSpiel() && !meinSpiel.fiftyJokerSchonVerwendet()) {
			Biblionaer.meinWindowController.setFiftyJokerBenutzt(true);
			meinSpiel.setFiftyJokerVerwendet(true);

			Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), false);
			Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());

		} else {
			Biblionaer.meineKonsole.println("Fifty-Joker schon verwendet oder Spiel beendet.", 3);
		}
	}

	/**
	 * Setzt den Fifty-Joker zurück, egal ob er schon verwendet wurde oder nicht
	 * Anwendung in erster Linie aus dem Admin-Schirm
	 */
	public void resetFiftyJoker() {
		Biblionaer.meineKonsole.println("Klick auf Reset-Fifty-Joker", 4);

		if (meinSpiel == null) {
			return;
		}

		Biblionaer.meinWindowController.setFiftyJokerBenutzt(false);
		meinSpiel.setFiftyJokerVerwendet(false);

		Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), false);
		Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());
	}

	public void klickAufPuplikumsJoker() {
		Biblionaer.meineKonsole.println("Klick auf Puplikums-Joker", 4);

		if (meinSpiel == null)
			return;

		if (meinSpiel.laeufDasSpiel() && !meinSpiel.puplikumsJokerSchonVerwendet()) {
			Biblionaer.meinWindowController.setPublikumsJokerBenutzt(true);
			puplikumsJokerTimer = new PuplikumsJokerCountdown(true);
			meinSpiel.setPuplikumsJokerSchonVerwendet(true);
		} else {
			Biblionaer.meineKonsole.println("Puplikums-Joker schon verwendet oder Spiel beendet.", 3);
		}
	}

	/**
	 * Setzt den Publikums-Joker zurück, egal ob er schon verwendet wurde oder
	 * nicht Anwendung in erster Linie aus dem Admin-Schirm
	 */
	public void resetPublikumsJoker() {
		Biblionaer.meineKonsole.println("Klick auf Reset-Publikums-Joker", 4);

		if (this.meinSpiel == null) {
			return;
		}

		Biblionaer.meinWindowController.setPublikumsJokerBenutzt(false);
		this.meinSpiel.setPuplikumsJokerSchonVerwendet(false);
		this.puplikumsJokerTimer.stoppeCountdown();
		this.puplikumsJokerTimer = null;

		Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), false);
		Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());
	}

	public void klickAufECLogo() {
		Biblionaer.meineKonsole.println("Klick auf EC-Logo", 4);

		// Geht nur ab Java Version 6
		/*
		 * try { Desktop.getDesktop().browse( new URI( "http://www.sv-ec.de/" )
		 * ); } catch (Exception e) { // e.printStackTrace();
		 * Biblionaer.meineKonsole .println(
		 * "Der Link zur EC-Site kann nicht geäffnet werden. Vermutlich wird eine Java-Version < 6 verwendet."
		 * , 3 ); }
		 */
	}

	public void klickAufQuizLogo() {
		Biblionaer.meineKonsole.println("Klick auf Quizlogo", 4);

	}

	public boolean statistikJokerNochFrei() {
		if (meinSpiel == null)
			return false;

		return !(meinSpiel.puplikumsJokerSchonVerwendet());
	}

	public boolean FiftyJokerAnzeigenNochFrei() {
		if (meinSpiel == null)
			return false;

		return !(meinSpiel.fiftyJokerSchonVerwendet());
	}

	public boolean TippJokerAnzeigenNochFrei() {
		if (meinSpiel == null)
			return false;

		return !(meinSpiel.tippJokerSchonVerwendet());
	}

	public long frageDauerBisJetztInSekunden() {
		if (this.meinSpiel != null) {
			return this.meinSpiel.frageDauerBisJetztInSekunden();
		} else
			return 0;
	}

	public long spielDauerBisJetztInSekunden() {
		if (this.meinSpiel != null) {
			return this.meinSpiel.spielDauerBisJetztInSekunden();
		} else
			return 0;
	}

	public void programmBeendenRequest() {
		if (this.meinSpiel != null && this.meinSpiel.laeufDasSpiel()) {
			int returnOptionDialog = JOptionPane.showOptionDialog(
					(Component) Biblionaer.meinWindowController.getBackendFenster(),
					"Bist Du dir sicher, dass Du dieses Spiel beenden möchtest?", "Warnung", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.NO_OPTION);

			if (returnOptionDialog == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}else{
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {
		// Do nothing
	}

	public void keyTyped(KeyEvent e) {
		// Do nothing
	}

	/**
	 * Wird vom WindowController aufgerufen, wenn sich etwas an der aktuellen
	 * Fensterkonstellation geändert hat. So kännen anzeigeOperationen erneut an
	 * alle Fenster gesendet werden.
	 */
	public void windowSituationHasChanged() {
		if (meinSpiel != null) {
			if (meinSpiel.laeufDasSpiel()) {
				Biblionaer.meinWindowController.setFrageAnzuzeigen(meinSpiel.getAktuelleFrageAnzuzeigen(), false);
				Biblionaer.meinWindowController.setFrageKomplett(this.meinSpiel.getAktuelleFrage());

				Biblionaer.meinWindowController.setFiftyJokerBenutzt(meinSpiel.fiftyJokerSchonVerwendet());
				Biblionaer.meinWindowController.setTippJokerBenutzt(meinSpiel.tippJokerSchonVerwendet());
				Biblionaer.meinWindowController.setPublikumsJokerBenutzt(meinSpiel.puplikumsJokerSchonVerwendet());
			} else {
			}
		}

	}
}
