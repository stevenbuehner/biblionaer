package quiz;

import importer.XmlToSpiel;

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
import javax.swing.filechooser.FileFilter;

import main.Biblionaer;
import timer.PuplikumsJokerCountdown;
import timer.TippJokerCountdown;
import GUI.Einstellungen;
import GUI.Hauptfenster;
import GUI.Konsole;
import GUI.QuizPanel;

public class Steuerung implements ActionListener, KeyListener {

	protected Einstellungen			meineEinstellungen;
	protected Hauptfenster			meinHauptfenster;
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

	public Hauptfenster getHauptfenster() {
		return meinHauptfenster;
	}

	public void setHauptfenster(Hauptfenster pHauptfenster) {
		this.meinHauptfenster = pHauptfenster;
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
		meinHauptfenster = null;
		meineKonsole = null;
		meinSpiel = null;

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

			if ( derFC.showOpenDialog( meinHauptfenster ) == JFileChooser.APPROVE_OPTION ) {

				XmlToSpiel dasFile = new XmlToSpiel( derFC.getSelectedFile() );
				meinSpiel = dasFile.getSpiel();

				this.starteNeuesSpiel();

			}

		}
		else if ( e.getActionCommand().equals( "Neues Standard-Spiel" ) ) {
			// Das Spiel direkt aus der SRC-Quelltext-Datei laden
			XmlToSpiel dasFile = new XmlToSpiel( getClass().getClassLoader().getResource(
					"importer/quiz.bqxml" ) );
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
			meinHauptfenster.setStatusText( "" );

			meinHauptfenster.setStatusText( null );

			if ( meinHauptfenster.quizFiftyJokerPanel != null )
				meinHauptfenster.quizFiftyJokerPanel.resetJoker();

			if ( meinHauptfenster.quizStatistikJokerPanel != null )
				meinHauptfenster.quizStatistikJokerPanel.resetJoker();

			if ( meinHauptfenster.quizTippJokerPanel != null )
				meinHauptfenster.quizTippJokerPanel.resetJoker();

			if ( meinHauptfenster.quizPubplikumsJoker != null )
				meinHauptfenster.quizPubplikumsJoker.resetJoker();

			meinHauptfenster.quizAnswerPanel1.zeigeBlau();
			meinHauptfenster.quizAnswerPanel2.zeigeBlau();
			meinHauptfenster.quizAnswerPanel3.zeigeBlau();
			meinHauptfenster.quizAnswerPanel4.zeigeBlau();

			meinHauptfenster.frageAnzeigen( meinSpiel.getAktuelleFrageAnzuzeigen(), true );
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
				meinHauptfenster.frageAnzeigen( meinSpiel.getAktuelleFrageAnzuzeigen(), true );
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
			meinHauptfenster.setStatusText( "" );

			meinSpiel.aktuelleFrageBeantwortet();

			if ( meinSpiel.istAktuelleAntwort( klickFeld ) ) {
				// Frage richtig beantwortet
				klickAufRichtigeAntwort();
			}

			else {
				// Frage falsch beantwortet
				klickAufFalscheAntwort();
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
			meinHauptfenster.setStatusText( "" );
		}
	}

	private void zeigeRichtigeAntwortGelb() {
		if ( meinSpiel != null ) {
			switch (meinSpiel.getAktuelleRichtigeAntwort()) {
				case 1:
					meinHauptfenster.quizAnswerPanel1.zeigeGelb();
					break;
				case 2:
					meinHauptfenster.quizAnswerPanel2.zeigeGelb();
					break;
				case 3:
					meinHauptfenster.quizAnswerPanel3.zeigeGelb();
					break;
				case 4:
					meinHauptfenster.quizAnswerPanel4.zeigeGelb();
					break;
				default:
					break;
			}
		}
	}

	private void zeigeRichtigeAntwortGelbFuer(int pMillesekungen) {
		if ( meinSpiel != null ) {
			switch (meinSpiel.getAktuelleRichtigeAntwort()) {
				case 1:
					meinHauptfenster.quizAnswerPanel1.zeigeGelbFuer( pMillesekungen );
					break;
				case 2:
					meinHauptfenster.quizAnswerPanel2.zeigeGelbFuer( pMillesekungen );
					break;
				case 3:
					meinHauptfenster.quizAnswerPanel3.zeigeGelbFuer( pMillesekungen );
					break;
				case 4:
					meinHauptfenster.quizAnswerPanel4.zeigeGelbFuer( pMillesekungen );
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
					meinHauptfenster.quizAnswerPanel1.zeigeGruen();
					break;
				case 2:
					meinHauptfenster.quizAnswerPanel2.zeigeGruen();
					break;
				case 3:
					meinHauptfenster.quizAnswerPanel3.zeigeGruen();
					break;
				case 4:
					meinHauptfenster.quizAnswerPanel4.zeigeGruen();
					break;
				default:
					break;
			}
		}
	}

	private void zeigeRichtigeAntwortGruenFuer(int pMillesekungen) {
		if ( meinSpiel != null ) {
			switch (meinSpiel.getAktuelleRichtigeAntwort()) {
				case 1:
					meinHauptfenster.quizAnswerPanel1.zeigeGruenFuer( pMillesekungen );
					break;
				case 2:
					meinHauptfenster.quizAnswerPanel2.zeigeGruenFuer( pMillesekungen );
					break;
				case 3:
					meinHauptfenster.quizAnswerPanel3.zeigeGruenFuer( pMillesekungen );
					break;
				case 4:
					meinHauptfenster.quizAnswerPanel4.zeigeGruenFuer( pMillesekungen );
					break;
				default:
					break;
			}
		}
	}

	private void zeigeAlleAntwortenBlau() {
		meinHauptfenster.quizAnswerPanel1.zeigeBlau();
		meinHauptfenster.quizAnswerPanel2.zeigeBlau();
		meinHauptfenster.quizAnswerPanel3.zeigeBlau();
		meinHauptfenster.quizAnswerPanel4.zeigeBlau();
	}

	private void klickAufRichtigeAntwort() {
		if ( meinSpiel.istGeradeLetzteFrage() ) {
			// gewonnen
			meinHauptfenster.setStatusText( "GEWONNEN - Gratuliere" );
			meinHauptfenster.quizQuestionPanel.setLoop( QuizPanel.GELB, QuizPanel.GELB );
			meinHauptfenster.quizAnswerPanel1.setLoop( QuizPanel.GELB, QuizPanel.GELB );
			meinHauptfenster.quizAnswerPanel2.setLoop( QuizPanel.GELB, QuizPanel.GELB );
			meinHauptfenster.quizAnswerPanel3.setLoop( QuizPanel.GELB, QuizPanel.GELB );
			meinHauptfenster.quizAnswerPanel4.setLoop( QuizPanel.GELB, QuizPanel.GELB );

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
			this.zeigeAlleAntwortenBlau();
			meinHauptfenster.frageAnzeigen( meinSpiel.getAktuelleFrageAnzuzeigen(), true );
		}
	}

	private void klickAufFalscheAntwort() {
		meinSpiel.setEnde();
		meinHauptfenster.setStatusText( "Falsche Antwort - Spiel beendet" );

		zeigeRichtigeAntwortGelb();
	}

	public void tippJokerZeitAbgelaufen() {
		meineKonsole.println( "tippJokerZeitAbgelaufen() wurde auferufen", 4 );

		if ( meinSpiel != null ) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			meinHauptfenster
					.setStatusText( "Zeit für den Tippjoker abgelaufen - Spiel beendet! ..." );
		}
	}

	public void puplikumsJokerZeitAbgelaufen() {
		meineKonsole.println( "puplikumsJokerZeitAbgelaufen() wurde auferufen", 4 );

		if ( meinSpiel != null ) {
			meinSpiel.setEnde();
			this.zeigeRichtigeAntwortGelb();
			meinHauptfenster
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
			meinHauptfenster.quizTippJokerPanel.setLoop( 2, 2 );

			tippJokerTimer = new TippJokerCountdown( true );
			meinSpiel.setTippJokerSchonVerwendet( true );
			meinHauptfenster.frageAnzeigen( meinSpiel.getAktuelleFrageAnzuzeigen(), false );
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
			meinHauptfenster.quizFiftyJokerPanel.setLoop( 2, 2 );
			meinSpiel.setFiftyJokerVerwendet( true );

			meinHauptfenster.frageAnzeigen( meinSpiel.getAktuelleFrageAnzuzeigen(), false );

		}
		else {
			meineKonsole.println( "Fifty-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufStatistikJoker() {
		meineKonsole.println( "Klick auf Statistik-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.puplikumsJokerSchonVerwendet() ) {
			meinHauptfenster.quizStatistikJokerPanel.setLoop( 2, 2 );
			meinSpiel.setPuplikumsJokerSchonVerwendet( true );

			// Aktion
		}
		else {
			meineKonsole.println( "Statistik-Joker schon verwendet oder Spiel beendet.", 3 );
		}
	}

	public void klickAufPuplikumsJoker() {
		meineKonsole.println( "Klick auf Puplikums-Joker", 4 );

		if ( meinSpiel == null )
			return;

		if ( meinSpiel.laeufDasSpiel() && !meinSpiel.puplikumsJokerSchonVerwendet() ) {
			meinHauptfenster.quizPubplikumsJoker.setLoop( 2, 2 );

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
