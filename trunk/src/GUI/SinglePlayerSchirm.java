package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import quiz.Quizfrage;
import quiz.Steuerung;

import main.Biblionaer;

import Grafik.GrafikLib;

public class SinglePlayerSchirm extends Hauptfenster implements Runnable, MouseListener {

	private static final long	serialVersionUID	= 8869415625961638325L;

	public SinglePlayerSchirm() {
		this( "Single-Player-Schirm", 678, 549, Biblionaer.meineSteuerung );
	}

	public SinglePlayerSchirm(String fenstername, int width, int height, Steuerung pSteuerung) {
		super( fenstername, width, height, pSteuerung );

		// Thread ansto§en
		if ( !once ) {
			once = true;
			Thread t = new Thread( this );
			t.start();
		}
	}

	@Override
	protected void doInitializations() {

		// I
		frame.setMenuBar( createMenue() );
		this.addMouseListener( this );

		last = System.nanoTime(); // Ohne Initialisierung stimmt die Berechnung
		// von delta nicht!!!
		berechneDelta(); // delta wird unten bei den Images benštigt

		// Programm-Icon setzen
		/*
		 * try { programmIcon = ImageIO.read( new URL( "img/quizLogo.png" ) );
		 * // this.programmIcon = ImageIO.read( //
		 * FramesIconImage.class.getResource( "discovery.gif" ) ); } catch
		 * (MalformedURLException e) { e.printStackTrace(); } catch (IOException
		 * e) { e.printStackTrace(); } finally { this.frame.setIconImage(
		 * this.programmIcon ); }
		 */
		lib = GrafikLib.getInstance();
		/*
		 * car = new Car( lib.getSprite( "pics/car.gif", 12, 1 ), 400, 300, 200,
		 * this ); map = new MapDisplay( "level/level.txt", "pics/tiles.gif",
		 * "pics/shadow.gif", 3, 4, this ); map.setVisibleRectangle( new
		 * Rectangle2D.Double( 50, map.getHeight() - getHeight(), getWidth(),
		 * getHeight() ) );
		 */
		backgroundQuizScreen = lib.getSprite( "img/quizBackground.jpg" );
		backgroundQuizRahmen = lib.getSprite( "img/quizRahmen.png" );
		// BufferedImage quizLogo = lib.getSprite( "img/logo.png" );

		// Das EC-Logo
		BufferedImage ecLogo = lib.getSprite( "img/ECLogoKleiner.png" );
		this.ecLogoPanel = new QuizRoundLogo( ecLogo, 15, 15, -1, this );

		BufferedImage[] quizImgQuestionBlau = lib.getSprite( "img/fragePanelBlau.png", 3, 1 );
		quizQuestionPanel = new QuizPanelFrage( quizImgQuestionBlau, -1, this, null );
		quizQuestionPanel.setLoop( QuizPanel.BLAU, QuizPanel.BLAU );

		int antPosY = 300; // Antwortpositionierung

		BufferedImage[] quizAnswerPanelBlau = lib.getSprite( "img/antwortPanelBlau.png", 3, 1 );
		quizAnswerPanel1 = new QuizPanelAntwort( quizAnswerPanelBlau, -quizAnswerPanelBlau[0]
				.getWidth(), antPosY, 10, antPosY, -1, this, null );
		quizAnswerPanel3 = new QuizPanelAntwort( quizAnswerPanelBlau, -quizAnswerPanelBlau[0]
				.getWidth(), (antPosY + 50), 10, (antPosY + 50), -1, this, null );
		quizAnswerPanel2 = new QuizPanelAntwort( quizAnswerPanelBlau, this.frame.getWidth(),
				antPosY, quizAnswerPanelBlau[0].getWidth(), antPosY, -1, this, null );
		quizAnswerPanel4 = new QuizPanelAntwort( quizAnswerPanelBlau, this.frame.getWidth(),
				(antPosY + 50), quizAnswerPanelBlau[0].getWidth(), (antPosY + 50), -1, this, null );

		quizAnswerPanel1.setLoop( QuizPanel.BLAU, QuizPanel.BLAU );
		quizAnswerPanel2.setLoop( QuizPanel.BLAU, QuizPanel.BLAU );
		quizAnswerPanel3.setLoop( QuizPanel.BLAU, QuizPanel.BLAU );
		quizAnswerPanel4.setLoop( QuizPanel.BLAU, QuizPanel.BLAU );

		// Joker initialisieren
		int JokerPosX = 400;
		int JokerPosY = 17;
		quizFiftyJokerPanel = new QuizPanelJoker( lib.getSprite( "img/fiftyJoker.png", 3, 1 ),
				JokerPosX, JokerPosY, this );
		quizTippJokerPanel = new QuizPanelJoker( lib.getSprite( "img/tippJoker.png", 3, 1 ),
				JokerPosX + 90, JokerPosY, this );
		// quizStatistikJokerPanel = new quizPanelJoker( lib.getSprite(
		// "img/statistikJoker.png", 3, 1 ), JokerPosX + 180, JokerPosY, this );
		quizPubplikumsJoker = new QuizPanelJoker( lib.getSprite( "img/puplikumsJoker.png", 3, 1 ),
				JokerPosX + 180, JokerPosY, this );

		// Bibelstelle
		quizTipp = new QuizTippPanel( null );

		// Statustext
		quizStatusTextPanel = new QuizStatusTextPanel( 50, 475, 400 );
		quizStatusTextPanel.setText( null );

	}

	protected MenuBar createMenue() {
		MenuBar mb = new MenuBar();
		Menu men;
		MenuItem mi;

		// Biblionaer
		men = new Menu( "Biblionaer" );

		// Neues Standard-Spiel
		mi = new MenuItem( "Neues Standard-Spiel" );
		mi.addActionListener( meineSteuerung );
		mi.setShortcut( new MenuShortcut( KeyEvent.VK_S ) );
		men.add( mi );

		// Neues Spiel aus dem Internet
		mi = new MenuItem( "Neues Spiel aus dem Internet" );
		mi.addActionListener( meineSteuerung );
		mi.setShortcut( new MenuShortcut( KeyEvent.VK_N ) );
		men.add( mi );

		// Neues Spiel von Datei
		mi = new MenuItem( "Neues Spiel von Datei" );
		mi.addActionListener( meineSteuerung );
		mi.setShortcut( new MenuShortcut( KeyEvent.VK_L ) );
		men.add( mi );

		// Trennstrich
		men.addSeparator();

		// Einstellungen
		mi = new MenuItem( "Einstellungen" );
		mi.addActionListener( meineSteuerung );
		mi.setShortcut( new MenuShortcut( KeyEvent.VK_COMMA ) );
		men.add( mi );

		mb.add( men );

		// Admin-Tests
		men = new Menu( "Admin-Tests " );

		// URLtest
		mi = new MenuItem( "URLtest" );
		mi.addActionListener( meineSteuerung );
		men.add( mi );

		// Spiel nach ID laden
		mi = new MenuItem( "Lade Frage mit der ID" );
		mi.addActionListener( meineSteuerung );
		men.add( mi );

		mb.add( men );

		return mb;
	}

	protected void doLogic() {
		quizQuestionPanel.doLogic( delta );
		quizAnswerPanel1.doLogic( delta );
		quizAnswerPanel2.doLogic( delta );
		quizAnswerPanel3.doLogic( delta );
		quizAnswerPanel4.doLogic( delta );

		if ( quizFiftyJokerPanel != null )
			quizFiftyJokerPanel.doLogic( delta );

		if ( quizTippJokerPanel != null )
			quizTippJokerPanel.doLogic( delta );

		if ( quizStatistikJokerPanel != null )
			quizStatistikJokerPanel.doLogic( delta );

		if ( quizPubplikumsJoker != null )
			quizPubplikumsJoker.doLogic( delta );
	}

	protected void moveObjects() {
		quizQuestionPanel.move( delta );
		quizAnswerPanel1.move( delta );
		quizAnswerPanel2.move( delta );
		quizAnswerPanel3.move( delta );
		quizAnswerPanel4.move( delta );

		// quizFiftyJokerPanel.move( delta );
		// quizTippJokerPanel.move( delta );
		// quizStatistikJokerPanel.move( delta );
		// quizPubplikumsJoker.move( delta );
	}

	public void render(Graphics g) {

		g.drawImage( backgroundQuizScreen, 0, 0, this );
		quizQuestionPanel.drawObjects( g );
		quizAnswerPanel1.drawObjects( g );
		quizAnswerPanel2.drawObjects( g );
		quizAnswerPanel3.drawObjects( g );
		quizAnswerPanel4.drawObjects( g );
		g.drawImage( backgroundQuizRahmen, 0, 0, this );

		if ( quizFiftyJokerPanel != null )
			quizFiftyJokerPanel.drawObjects( g );

		if ( quizTippJokerPanel != null )
			quizTippJokerPanel.drawObjects( g );

		if ( quizStatistikJokerPanel != null )
			quizStatistikJokerPanel.drawObjects( g );

		if ( quizPubplikumsJoker != null )
			quizPubplikumsJoker.drawObjects( g );

		quizTipp.drawObjects( g );

		ecLogoPanel.drawObjects( g );
		quizStatusTextPanel.drawObjects( g );
		g.setColor( Color.red );

		if ( Biblionaer.meineEinstellungen.getPingAnzeigen() )
			g.drawString( Long.toString( fps ), 20, 20 );

	}

	public void run() {

		while (frame.isVisible()) {

			berechneDelta();

			if ( meineSteuerung.getStatus() > 0 ) {
				// checkKeys();
				doLogic();
				moveObjects();
			}

			doPainting();

			try {
				Thread.sleep( 10 );
			}
			catch (InterruptedException e) {}

		}
		System.exit( 0 );

	}

	public void frageAnzeigen(Quizfrage pFrage, boolean mitAnimation) {

		if ( mitAnimation ) {
			quizQuestionPanel.resetAnimation();
			quizAnswerPanel1.resetAnimation();
			quizAnswerPanel2.resetAnimation();
			quizAnswerPanel3.resetAnimation();
			quizAnswerPanel4.resetAnimation();
		}

		if ( pFrage == null ) {
			quizQuestionPanel.setFrage( pFrage );
			quizTipp.setFrage( pFrage );
			quizAnswerPanel1.setAusgabeString( null );
			quizAnswerPanel2.setAusgabeString( null );
			quizAnswerPanel3.setAusgabeString( null );
			quizAnswerPanel4.setAusgabeString( null );
		}
		else {
			quizQuestionPanel.setFrage( pFrage );
			quizTipp.setFrage( pFrage );
			quizAnswerPanel1.setAusgabeString( pFrage.getAntwort1() );
			quizAnswerPanel2.setAusgabeString( pFrage.getAntwort2() );
			quizAnswerPanel3.setAusgabeString( pFrage.getAntwort3() );
			quizAnswerPanel4.setAusgabeString( pFrage.getAntwort4() );
		}
	}

	public void mouseClicked(MouseEvent e) {

		if ( quizQuestionPanel.feldAngeklickt( e.getX(), e.getY() ) ) {
			meineSteuerung.klickAufFrageFeld();
		}

		if ( quizAnswerPanel1.feldAngeklickt( e.getX(), e.getY() ) ) {
			meineSteuerung.klickAufAntwortFeld( 1 );
		}

		if ( quizAnswerPanel2.feldAngeklickt( e.getX(), e.getY() ) ) {
			meineSteuerung.klickAufAntwortFeld( 2 );
		}

		if ( quizAnswerPanel3.feldAngeklickt( e.getX(), e.getY() ) ) {
			meineSteuerung.klickAufAntwortFeld( 3 );
		}

		if ( quizAnswerPanel4.feldAngeklickt( e.getX(), e.getY() ) ) {
			meineSteuerung.klickAufAntwortFeld( 4 );
		}

		if ( ecLogoPanel != null ) {
			if ( ecLogoPanel.feldAngeklickt( e.getX(), e.getY() ) ) {
				meineSteuerung.klickAufECLogo();
			}
		}

		if ( quizFiftyJokerPanel != null ) {
			if ( quizFiftyJokerPanel.feldAngeklickt( e.getX(), e.getY() ) ) {
				meineSteuerung.klickAufFiftyJoker();
			}
		}

		if ( quizTippJokerPanel != null ) {
			if ( quizTippJokerPanel.feldAngeklickt( e.getX(), e.getY() ) ) {
				meineSteuerung.klickAufTippJoker();
			}
		}

		if ( quizStatistikJokerPanel != null ) {
			if ( quizStatistikJokerPanel.feldAngeklickt( e.getX(), e.getY() ) ) {
				meineSteuerung.klickAufStatistikJoker();
			}
		}

		if ( quizPubplikumsJoker != null ) {
			if ( quizPubplikumsJoker.feldAngeklickt( e.getX(), e.getY() ) ) {
				meineSteuerung.klickAufPuplikumsJoker();
			}
		}

	}

	public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		if ( quizFiftyJokerPanel != null ) {
			if ( quizFiftyJokerPanel.feldAngeklickt( e.getX(), e.getY() )
					&& meineSteuerung.FiftyJokerAnzeigenNochFrei() ) {
				quizFiftyJokerPanel.setPressedIfPossible();
			}
		}

		if ( quizTippJokerPanel != null ) {
			if ( quizTippJokerPanel.feldAngeklickt( e.getX(), e.getY() )

			&& meineSteuerung.TippJokerAnzeigenNochFrei() ) {
				quizTippJokerPanel.setPressedIfPossible();
			}
		}

		if ( quizStatistikJokerPanel != null ) {
			if ( quizStatistikJokerPanel.feldAngeklickt( e.getX(), e.getY() )
					&& meineSteuerung.statistikJokerNochFrei() ) {
				quizStatistikJokerPanel.setPressedIfPossible();
			}
		}

		if ( quizPubplikumsJoker != null ) {
			if ( quizPubplikumsJoker.feldAngeklickt( e.getX(), e.getY() )
					&& meineSteuerung.statistikJokerNochFrei() ) {
				quizPubplikumsJoker.setPressedIfPossible();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if ( quizFiftyJokerPanel != null )
			quizFiftyJokerPanel.setReleasedIfPossible();

		if ( quizTippJokerPanel != null )
			quizTippJokerPanel.setReleasedIfPossible();

		if ( quizStatistikJokerPanel != null )
			quizStatistikJokerPanel.setReleasedIfPossible();

		if ( quizPubplikumsJoker != null )
			quizPubplikumsJoker.setReleasedIfPossible();
	}

}
