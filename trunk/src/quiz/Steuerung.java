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
import window.Einstellungen;
import window.Konsole;
import window.WindowController;
import windowElements.QuizPanel;

public class Steuerung implements ActionListener, KeyListener {

	protected Einstellungen			meineEinstellungen;
	protected WindowController	meinWindowController;
	protected Konsole				meineKonsole;
	protected Spiel					meinSpiel;

	private static final long		serialVersionUID	= 1L;
	protected boolean				game_running		= true;
	protected boolean				started				= false;

	// Timer für den Tipp-Joker
	private TippJokerCountdown		tippJokerTimer;
	// Timer für den Puplikums-Joker
	private PuplikumsJokerCountdown	puplikumsJokerTimer;

	public Einstellungen getEinstellungen() {
		return meineEinstellungen;
	}

	public void setEinstellungen(Einstellungen pEinstellungen) {
		this.meineEinstellungen = pEinstellungen;
	}

	public WindowController getWindowController() {
		return meinWindowController;
	}

	public void setWindowController(WindowController pHauptfenster) {
		this.meinWindowController = pHauptfenster;
	}

	public Konsole getKonsole() {
		return meineKonsole;
	}

	public void setKonsole(Konsole pKonsole) {
		this.meineKonsole = pKonsole;
	}

	public Steuerung() {
		// TODO Auto-generated constructor stub
		meineEinstellungen = null;
		meinWindowController = null;
		meineKonsole = null;
		meinSpiel = null;

	}

	public void erstAufrufDerSteuerung() {
		// Startdialog
		JOptionPane startDialog = new JOptionPane();
		int returnOptionDialog = startDialog
				.showOptionDialog(
						(Component) meinWindowController.getFrontendFenster(),
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
			meineEinstellungen.setVisible( true );
		}
		else if ( e.getActionCommand().equals( "Neues Spiel aus dem Internet" ) ) {

			// meinSpiel = new Spiel( 15 );
			try {
				XmlToSpiel dasFile = new XmlToSpiel( new URL( meineEinstellungen.getXMLquelle() ) );
				this.meinSpiel = dasFile.getSpiel();

			}
			catch (MalformedURLException e1) {
				meineKonsole.println( "Die URL zum XML-File ist falsch!" );
				e1.printStackTrace();
			}
			finally {
				this.starteNeuesSpiel();
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

			if ( derFC.showOpenDialog( (Component) meinWindowController.getFrontendFenster() ) == JFileChooser.APPROVE_OPTION ) {

				XmlToSpiel dasFile = new XmlToSpiel( derFC.getSelectedFile() );
				meinSpiel = dasFile.getSpiel();

				this.starteNeuesSpiel();

			}

		}
		else if ( e.getActionCommand().equals( "Neues Standard-Spiel" ) ) {
			// Das Spiel direkt aus der SRC-Quelltext-Datei laden
			XmlToSpiel dasFile = new XmlToSpiel( getClass().getClassLoader().getResource(
					"lokaleSpiele/spiel1.txt" ) );
			meinSpiel = dasFile.getSpiel();

			this.starteNeuesSpiel();
		}
		else if ( e.getActionCommand().equals( "URLtest" ) ) {
			try {
				URL test = new URL( "http://schwann-evangelisch.torres.webcontact.de/" );
				URLConnection con = test.openConnection();
				System.out.println( con );
			}
			catch (MalformedURLException e1) {
				meineKonsole.println( "MalformedURLException:", 3 );
				e1.printStackTrace();
			}
			catch (IOException e1) {
				meineKonsole.println( "IOException:", 3 );
				e1.printStackTrace();
			}
			finally {
				meineKonsole.println( "Es besteht eine Verbindung zum Internet", 3 );
			}

		}
		else {
			System.out.println( "Steuerung: Kein Absender den ich kenne, sagt mir: "
					+ e.getActionCommand() );
			System.out.print( "Absender: " );
			System.out.println( e );
		}

	}

	private void starteNeuesSpiel() {
		if ( meinSpiel != null ) {
			meinSpiel.starteSpiel();

			// Falls ein Timer noch läuft, beende ihn
			this.loescheAlleTimer();

			meinWindowController.setStatusText( null );

			meinWindowController.resetAlleJoker();
			meinWindowController.setFrageFeldSichtbar( true );
			meinWindowController.setAntwortenSichtbar( true );
			meinWindowController.setAntwortfelderNormal();

			meinWindowController.setFrage( meinSpiel.getAktuelleFrageAnzuzeigen(), true );
		}
		else {
			Biblionaer.meineKonsole
					.println(
							"Es konnte kein neues Spiel aus der angegebenen Datei heraus gestartet werden.",
							2 );
		}

	}

	public int getStatus() {
		/*
		 * 0 = Initialisierung und Co 1 = Intro 2 = Menu anzeigen 3 = Spiel
		 * gestartet
		 */

		return 3;

	}

	public void keyPressed(KeyEvent e) {

		// Zum Testen
		// System.setProperty( "proxyHost", meineEinstellungen.getProxyHost() );
		// System.setProperty( "proxyPort", meineEinstellungen.getProxyPort() );
		// System.setProperty( "proxySet", "false" ); // Proxy aktivieren

		if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
			// Quit
			System.exit( 0 );
		}

		else if ( e.getKeyCode() == KeyEvent.VK_SPACE && meineEinstellungen.darfGechetetWerden() ) {
			// neue Fragen laden

			if ( meinSpiel.setNaechsteFrage() ) {
				meinWindowController.setFrage( meinSpiel.getAktuelleFrageAnzuzeigen(), true );
			}
		}

		if ( e.getKeyCode() == KeyEvent.VK_P ) {
			System.setProperty( "proxySet", "false" ); // Proxy aktivieren
		}

		if ( e.getKeyCode() == KeyEvent.VK_T ) {
			// Nur zum testen
			meinSpiel = new XmlToSpiel().getSpiel();
			this.starteNeuesSpiel();
		}
	}

	public void klickAufAntwortFeld(int klickFeld) {
		meineKonsole.println( "Klick auf Antwortfeld " + klickFeld, 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() ) {

			this.loescheAlleTimer();
			meinWindowController.setStatusText( "" );

			meinSpiel.aktuelleFrageBeantwortet();

			if ( meinSpiel.istAktuelleAntwort( klickFeld ) ) {
				// Frage richtig beantwortet
				klickAufRichtigeAntwort();
				meinWindowController.playFrageRichtig();
			}

			else {
				// Frage falsch beantwortet
				klickAufFalscheAntwort();
				meinWindowController.playFrageFalsch();
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
			meinWindowController.setStatusText( "" );
		}
	}

	private void zeigeRichtigeAntwortGelb() {
		if ( meinSpiel != null ) {
			switch (meinSpiel.getAktuelleRichtigeAntwort()) {
				case 1:
					meinWindowController.setAntwortFeld1Markiert();
					break;
				case 2:
					meinWindowController.setAntwortFeld2Markiert();
					break;
				case 3:
					meinWindowController.setAntwortFeld3Markiert();
					break;
				case 4:
					meinWindowController.setAntwortFeld4Markiert();
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
					meinWindowController.setAntwortFeld1Richtig();
					break;
				case 2:
					meinWindowController.setAntwortFeld2Richtig();
					break;
				case 3:
					meinWindowController.setAntwortFeld3Richtig();
					break;
				case 4:
					meinWindowController.setAntwortFeld4Richtig();
					break;
				default:
					break;
			}
		}
	}

	private void klickAufRichtigeAntwort() {
		if ( meinSpiel.istGeradeLetzteFrage() ) {
			// gewonnen
			meinWindowController.setStatusText( "GEWONNEN - Gratuliere" );
			meinWindowController.playSpielGewonnen();
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
			meinWindowController.setAntwortfelderNormal();
			meinWindowController.setFrage( meinSpiel.getAktuelleFrageAnzuzeigen(), true );
		}
	}

	private void klickAufFalscheAntwort() {
		meinSpiel.setEnde();
		meinWindowController.setStatusText( "Falsche Antwort - Spiel beendet" );

		zeigeRichtigeAntwortGelb();
	}

	public void tippJokerZeitAbgelaufen() {
		meineKonsole.println( "tippJokerZeitAbgelaufen() wurde auferufen", 4 );

		if ( meinSpiel != null ) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			meinWindowController
					.setStatusText( "Zeit für den Tippjoker abgelaufen - Spiel beendet! ..." );
		}
	}

	public void puplikumsJokerZeitAbgelaufen() {
		meineKonsole.println( "puplikumsJokerZeitAbgelaufen() wurde auferufen", 4 );

		if ( meinSpiel != null ) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			meinWindowController
					.setStatusText( "Zeit für den Puplikumsjoker abgelaufen - Spiel beendet! ..." );
		}
	}

	public void klickAufFrageFeld() {
		meineKonsole.println( "Klick auf Fragefeld", 4 );
	}

	public void klickAufTippJoker() {
		meineKonsole.println( "Klick auf Tipp-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.tippJokerSchonVerwendet() ) {
			meinWindowController.setTippJokerBenutzt( true );
			meinSpiel.setTippJokerSchonVerwendet( true );
			tippJokerTimer = new TippJokerCountdown( true );

			meinWindowController.setFrage( meinSpiel.getAktuelleFrageAnzuzeigen(), false );
		}
		else {
			meineKonsole.println( "Tipp-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufFiftyJoker() {
		meineKonsole.println( "Klick auf Fifty-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.fiftyJokerSchonVerwendet() ) {
			meinWindowController.setFiftyJokerBenutzt( true );
			meinSpiel.setFiftyJokerVerwendet( true );

			meinWindowController.setFrage( meinSpiel.getAktuelleFrageAnzuzeigen(), false );
		}
		else {
			meineKonsole.println( "Fifty-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufPuplikumsJoker() {
		meineKonsole.println( "Klick auf Puplikums-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.puplikumsJokerSchonVerwendet() ) {
			meinWindowController.setPublikumsJokerBenutzt( true );
			puplikumsJokerTimer = new PuplikumsJokerCountdown( true );
			meinSpiel.setPuplikumsJokerSchonVerwendet( true );
		}
		else {
			meineKonsole.println( "Puplikums-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufECLogo() {
		meineKonsole.println( "Klick auf EC-Logo", 4 );

		// Geht nur ab Java Version 6
		/*
		 * try { Desktop.getDesktop().browse( new URI( "http://www.sv-ec.de/" )
		 * ); } catch (Exception e) { // e.printStackTrace(); meineKonsole
		 * .println(
		 * "Der Link zur EC-Site kann nicht geöffnet werden. Vermutlich wird eine Java-Version < 6 verwendet."
		 * , 3 ); }
		 */
	}

	public void klickAufQuizLogo() {
		meineKonsole.println( "Klick auf Quizlogo", 4 );

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

}
