package quiz;

import importer.XmlToSpiel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import main.Biblionaer;
import timer.PuplikumsJokerCountdown;
import timer.TippJokerCountdown;

public class Steuerung implements ActionListener, KeyListener {

	protected Spiel					meinSpiel;

	private static final long		serialVersionUID	= 1L;
	protected boolean				game_running		= true;
	protected boolean				started				= false;

	// Timer für den Tipp-Joker
	private TippJokerCountdown		tippJokerTimer;
	// Timer für den Puplikums-Joker
	private PuplikumsJokerCountdown	puplikumsJokerTimer;

	public Steuerung() {
		meinSpiel = null;
	}

	public void erstAufrufDerSteuerung() {
		// Startdialog
		int returnOptionDialog = JOptionPane
				.showOptionDialog(
						(Component) Biblionaer.meinWindowController.getFrontendFenster(),
						"Herzlich Willkommen zu \"Wer wird Biblionär\". Zu Beginn wird ein Standardspiel geladen. \nWeitere Spiele können über das Menü geladen werden. Hierzu ist allerdings eine Verbindung zum Internet nötig. \nMit der Verwendung dieses Programmes stimmen Sie zu, nichts davon kommerziell zu verwenden.\n\nViel Spaß beim Spielen, \nIhr Biblionär-Team.",
						"Herzlich Willkommen", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.OK_OPTION );

		if ( returnOptionDialog == JOptionPane.CANCEL_OPTION
				|| returnOptionDialog == JOptionPane.CLOSED_OPTION ) {
			System.exit( 0 );
		}

		this.actionPerformed( new ActionEvent( this, 0, "Neues Standard-Spiel" ) );

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		// *** Klicks aus dem Hauptfenster ***//
		// Fenster Einstellungen öffnen
		if ( e.getActionCommand().equals( "Einstellungen" ) ) {
			Biblionaer.meineEinstellungen.setVisible( true );
		}
		else if ( e.getActionCommand().equals( "Neues Spiel aus dem Internet" ) ) {

			// meinSpiel = new Spiel( 15 );
			try {
				XmlToSpiel dasFile = new XmlToSpiel( new URL( Biblionaer.meineEinstellungen
						.getXMLquelle() ) );
				this.meinSpiel = dasFile.getSpiel();

			}
			catch (MalformedURLException e1) {
				Biblionaer.meineKonsole.println( "Die URL zum XML-File ist falsch!" );
				e1.printStackTrace();
			}
			finally {
				this.initialisiereNeuesSpiel();
			}
		}

		else if ( e.getActionCommand().equals( "Neues Spiel von Datei" ) ) {
			// Spiel aus einer Datei laden
			JFileChooser derFC = new JFileChooser();
			derFC.setFileSelectionMode( JFileChooser.FILES_ONLY );
			derFC.setDialogTitle( "Gespeichertes Spiel auswählen" );
			derFC.setAcceptAllFileFilterUsed( false );

			File currentDir = new File( System.getProperty( "user.home" ), "Desktop" );
			derFC.setCurrentDirectory( currentDir );

			derFC.setFileFilter( new FileFilter() {

				@Override
				public String getDescription() {
					return "Bibel-Quiz-Dateien (*.bqxml)";
				}

				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith( ".bqxml" ) || f.isDirectory();
				}
			} );

			if ( derFC.showOpenDialog( (Component) Biblionaer.meinWindowController
					.getFrontendFenster() ) == JFileChooser.APPROVE_OPTION ) {

				XmlToSpiel dasFile = new XmlToSpiel( derFC.getSelectedFile() );
				meinSpiel = dasFile.getSpiel();

				this.initialisiereNeuesSpiel();

			}

		}
		else if ( e.getActionCommand().equals( "Neues Standard-Spiel" ) ) {
			// Das Spiel direkt aus der SRC-Quelltext-Datei laden
			this.starteNeuesSpiel( getClass().getClassLoader().getResource(
					"lokaleSpiele/spielTest.txt" ) );
		}
		else if ( e.getActionCommand().equals( "URLtest" ) ) {
			try {
				URL test = new URL( "http://schwann-evangelisch.torres.webcontact.de/" );
				URLConnection con = test.openConnection();
				System.out.println( con );
			}
			catch (MalformedURLException e1) {
				Biblionaer.meineKonsole.println( "MalformedURLException:", 3 );
				e1.printStackTrace();
			}
			catch (IOException e1) {
				Biblionaer.meineKonsole.println( "IOException:", 3 );
				e1.printStackTrace();
			}
			finally {
				Biblionaer.meineKonsole.println( "Es besteht eine Verbindung zum Internet", 3 );
			}

		}
		else {
			System.out.println( "Steuerung: Kein Absender den ich kenne, sagt mir: "
					+ e.getActionCommand() );
			System.out.print( "Absender: " );
			System.out.println( e );
		}

	}

	public void starteNeuesSpiel(File quizLocation) {
		XmlToSpiel dasFile = new XmlToSpiel( quizLocation );
		meinSpiel = dasFile.getSpiel();

		if ( dasFile != null ) {
			if ( dasFile.getAnzahlFragen() > 0 ) {
				meinSpiel = dasFile.getSpiel();

				this.initialisiereNeuesSpiel();
			}
		}
		else {
			Biblionaer.meineKonsole
					.println(
							"Steuerung meldet: Neues Spiel konnte nicht initialisiert werden. Fehlerhafter Pfad.",
							2 );
		}
	}

	public void starteNeuesSpiel(URL quizLocation) {
		XmlToSpiel dasFile = new XmlToSpiel( quizLocation );

		if ( dasFile != null ) {
			if ( dasFile.getAnzahlFragen() > 0 ) {
				meinSpiel = dasFile.getSpiel();

				this.initialisiereNeuesSpiel();
			}
		}
		else {
			Biblionaer.meineKonsole
					.println(
							"Steuerung meldet: Neues Spiel konnte nicht initialisiert werden. Fehlerhafter Pfad.",
							2 );
		}
	}

	private void initialisiereNeuesSpiel() {
		if ( meinSpiel != null ) {
			meinSpiel.starteSpiel();

			// Falls ein Timer noch läuft, beende ihn
			this.loescheAlleTimer();

			Biblionaer.meinWindowController.setStatusText( null );

			Biblionaer.meinWindowController.resetAlleJoker();
			Biblionaer.meinWindowController.setFrageFeldSichtbar( true );
			Biblionaer.meinWindowController.setAntwortFelderSichtbar( true );
			Biblionaer.meinWindowController.setAntwortfelderNormal();
			Biblionaer.meinWindowController.playStarteSpiel();

			Biblionaer.meinWindowController.setFrageAnzuzeigen( meinSpiel
					.getAktuelleFrageAnzuzeigen(), true );
			Biblionaer.meinWindowController.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );

		}
		else {
			Biblionaer.meineKonsole
					.println(
							"Es konnte kein neues Spiel aus der angegebenen Datei heraus gestartet werden.",
							2 );
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

		if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
			// Quit
			System.exit( 0 );
		}

		else if ( e.getKeyCode() == KeyEvent.VK_1
				&& Biblionaer.meineEinstellungen.getKonsolenModus() >= 4 ) {
			Biblionaer.meineEinstellungen
					.setQuizScreenModus( Biblionaer.meineEinstellungen.quizScreenModusMultiWindow1FullScreen );
		}
		else if ( e.getKeyCode() == KeyEvent.VK_2
				&& Biblionaer.meineEinstellungen.getKonsolenModus() >= 4 ) {
			// Wechsle in den Betriebsmodus Windowed Player - Das ist zum
			// Debuggen ganz nützlich
			Biblionaer.meineEinstellungen
					.setQuizScreenModus( Biblionaer.meineEinstellungen.quizScreenModusMultiWindow1Windowed );
		}
		else if ( e.getKeyCode() == KeyEvent.VK_3
				&& Biblionaer.meineEinstellungen.getKonsolenModus() >= 4 ) {
			Biblionaer.meineEinstellungen
					.setQuizScreenModus( Biblionaer.meineEinstellungen.quizScreenModusMultiWindow2FullScreen );
		}
		else if ( e.getKeyCode() == KeyEvent.VK_4
				&& Biblionaer.meineEinstellungen.getKonsolenModus() >= 4 ) {
			Biblionaer.meineEinstellungen
					.setQuizScreenModus( Biblionaer.meineEinstellungen.quizScreenModusSingleWindow );
		}

		else if ( e.getKeyCode() == KeyEvent.VK_SPACE
				&& Biblionaer.meineEinstellungen.darfGechetetWerden() ) {
			// neue Fragen laden

			if ( meinSpiel.setNaechsteFrage() ) {
				Biblionaer.meinWindowController.setFrageAnzuzeigen( meinSpiel
						.getAktuelleFrageAnzuzeigen(), true );
				Biblionaer.meinWindowController
						.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );

			}
		}

		if ( e.getKeyCode() == KeyEvent.VK_P ) {
			System.setProperty( "proxySet", "false" ); // Proxy aktivieren
		}

		if ( e.getKeyCode() == KeyEvent.VK_T ) {
			// Nur zum testen
			meinSpiel = new XmlToSpiel().getSpiel();
			this.initialisiereNeuesSpiel();
		}
	}

	public void klickAufAntwortFeld(int klickFeld) {
		Biblionaer.meineKonsole.println( "Klick auf Antwortfeld " + klickFeld, 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() ) {

			this.loescheAlleTimer();
			Biblionaer.meinWindowController.setStatusText( "" );

			meinSpiel.aktuelleFrageBeantwortet();

			if ( meinSpiel.istAktuelleAntwort( klickFeld ) ) {
				// Frage richtig beantwortet
				klickAufRichtigeAntwort();
				Biblionaer.meinWindowController.playFrageRichtig();
			}

			else {
				// Frage falsch beantwortet
				klickAufFalscheAntwort( klickFeld );
				Biblionaer.meinWindowController.playFrageFalsch();
			}
		}
		else {
			// Trotzdem die richtige Antwort anzeigen ;-)
			zeigeRichtigeAntwortGelb();
		}

	}

	private void loescheAlleTimer() {

		if ( tippJokerTimer != null ) {
			tippJokerTimer.stoppeCountdown();
			tippJokerTimer = null;
		}

		if ( puplikumsJokerTimer != null ) {
			puplikumsJokerTimer.stoppeCountdown();
			puplikumsJokerTimer = null;
			Biblionaer.meinWindowController.setStatusText( "" );
		}
	}

	private void zeigeRichtigeAntwortGelb() {
		if ( meinSpiel != null ) {
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
		if ( meinSpiel != null ) {
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
		if ( meinSpiel.istGeradeLetzteFrage() ) {
			// gewonnen
			Biblionaer.meinWindowController.setStatusText( "GEWONNEN - Gratuliere" );
			Biblionaer.meinWindowController.playSpielGewonnen();
		}
		else {

			this.zeigeRichtigeAntwortGruen();
			try {
				Thread.sleep( 2010 );
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			meinSpiel.setNaechsteFrage();
			// Play: RICHTIIIGGG ...
			Biblionaer.meinWindowController.setAntwortfelderNormal();
			Biblionaer.meinWindowController.setFrageAnzuzeigen( meinSpiel
					.getAktuelleFrageAnzuzeigen(), true );
			Biblionaer.meinWindowController.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );

		}
	}

	private void klickAufFalscheAntwort(int klickFeldFalscheAntwort) {
		meinSpiel.setEnde();
		Biblionaer.meinWindowController.setStatusText( "Falsche Antwort - Spiel beendet" );

		// Zeige im Frontend die Frage mit Bibelstelle an
		Quizfrage mitBibelstelle = this.meinSpiel.getAktuelleFrageAnzuzeigen();
		mitBibelstelle.setLoesungshinweis( this.meinSpiel.getAktuelleFrage().getLoesungshinweis() );

		Biblionaer.meinWindowController.setFrageAnzuzeigen( mitBibelstelle, false );
		Biblionaer.meinWindowController.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );
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
						.println(
								"Dieses Antwortfeld gibt es nicht (Steuerung - klickFeldFalscheAntwort)",
								2 );
				break;

		}
	}

	/**
	 * Zum Aufruf über ein AdministratorPanel ...
	 */
	public void spielBeenden() {
		if ( meinSpiel != null ) {
			if ( meinSpiel.laeufDasSpiel() ) {
				meinSpiel.setEnde();
			}
		}
	}

	public void tippJokerZeitAbgelaufen() {
		Biblionaer.meineKonsole.println( "tippJokerZeitAbgelaufen() wurde auferufen", 4 );

		if ( meinSpiel != null ) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			Biblionaer.meinWindowController
					.setStatusText( "Zeit für den Tippjoker abgelaufen - Spiel beendet! ..." );
		}
	}

	public void puplikumsJokerZeitAbgelaufen() {
		Biblionaer.meineKonsole.println( "puplikumsJokerZeitAbgelaufen() wurde auferufen", 4 );

		if ( meinSpiel != null ) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			Biblionaer.meinWindowController
					.setStatusText( "Zeit für den Puplikumsjoker abgelaufen - Spiel beendet! ..." );
		}
	}

	public void klickAufFrageFeld() {
		Biblionaer.meineKonsole.println( "Klick auf Fragefeld", 4 );
	}

	public void klickAufTippJoker() {
		Biblionaer.meineKonsole.println( "Klick auf Tipp-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.tippJokerSchonVerwendet() ) {
			Biblionaer.meinWindowController.setTippJokerBenutzt( true );
			meinSpiel.setTippJokerSchonVerwendet( true );
			tippJokerTimer = new TippJokerCountdown( true );

			Biblionaer.meinWindowController.setFrageAnzuzeigen( meinSpiel
					.getAktuelleFrageAnzuzeigen(), false );
			Biblionaer.meinWindowController.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );

		}
		else {
			Biblionaer.meineKonsole.println( "Tipp-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufFiftyJoker() {
		Biblionaer.meineKonsole.println( "Klick auf Fifty-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.fiftyJokerSchonVerwendet() ) {
			Biblionaer.meinWindowController.setFiftyJokerBenutzt( true );
			meinSpiel.setFiftyJokerVerwendet( true );

			Biblionaer.meinWindowController.setFrageAnzuzeigen( meinSpiel
					.getAktuelleFrageAnzuzeigen(), false );
			Biblionaer.meinWindowController.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );

		}
		else {
			Biblionaer.meineKonsole.println( "Fifty-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufPuplikumsJoker() {
		Biblionaer.meineKonsole.println( "Klick auf Puplikums-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.puplikumsJokerSchonVerwendet() ) {
			Biblionaer.meinWindowController.setPublikumsJokerBenutzt( true );
			puplikumsJokerTimer = new PuplikumsJokerCountdown( true );
			meinSpiel.setPuplikumsJokerSchonVerwendet( true );
		}
		else {
			Biblionaer.meineKonsole.println( "Puplikums-Joker schon verwendet oder Spiel beendet.",
					3 );
		}
	}

	public void klickAufECLogo() {
		Biblionaer.meineKonsole.println( "Klick auf EC-Logo", 4 );

		// Geht nur ab Java Version 6
		/*
		 * try { Desktop.getDesktop().browse( new URI( "http://www.sv-ec.de/" )
		 * ); } catch (Exception e) { // e.printStackTrace();
		 * Biblionaer.meineKonsole .println(
		 * "Der Link zur EC-Site kann nicht geöffnet werden. Vermutlich wird eine Java-Version < 6 verwendet."
		 * , 3 ); }
		 */
	}

	public void klickAufQuizLogo() {
		Biblionaer.meineKonsole.println( "Klick auf Quizlogo", 4 );

	}

	public boolean statistikJokerNochFrei() {
		if ( meinSpiel == null )
			return false;

		return !(meinSpiel.puplikumsJokerSchonVerwendet());
	}

	public boolean FiftyJokerAnzeigenNochFrei() {
		if ( meinSpiel == null )
			return false;

		return !(meinSpiel.fiftyJokerSchonVerwendet());
	}

	public boolean TippJokerAnzeigenNochFrei() {
		if ( meinSpiel == null )
			return false;

		return !(meinSpiel.tippJokerSchonVerwendet());
	}

	public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub

	}

	/**
	 * Wird vom WindowController aufgerufen, wenn sich etwas an der aktuellen
	 * Fensterkonstellation geändert hat. So können anzeigeOperationen erneut an
	 * alle Fenster gesendet werden.
	 */
	public void windowSituationHasChanged() {
		if ( meinSpiel != null ) {
			if ( meinSpiel.laeufDasSpiel() ) {
				Biblionaer.meinWindowController.setFrageAnzuzeigen( meinSpiel
						.getAktuelleFrageAnzuzeigen(), false );
				Biblionaer.meinWindowController
						.setFrageKomplett( this.meinSpiel.getAktuelleFrage() );

				Biblionaer.meinWindowController.setFiftyJokerBenutzt( meinSpiel
						.fiftyJokerSchonVerwendet() );
				Biblionaer.meinWindowController.setTippJokerBenutzt( meinSpiel
						.tippJokerSchonVerwendet() );
				Biblionaer.meinWindowController.setPublikumsJokerBenutzt( meinSpiel
						.puplikumsJokerSchonVerwendet() );
			}
			else {}
		}

	}
}
