package window;

import interfaces.BackendWindow;
import interfaces.FrontendWindow;
import interfaces.QuizFenster;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

import main.Biblionaer;
import quiz.Quizfrage;
import quiz.Steuerung;
import windowElements.QuizImagePanel;
import windowElements.QuizPanelAntwort;
import windowElements.QuizPanelFrage;
import windowElements.QuizPanelJoker;
import windowElements.QuizRoundLogo;
import windowElements.QuizStatusTextPanel;
import windowElements.QuizTippPanel;
import Grafik.GrafikLib;

public class SinglePlayerSchirm extends Canvas implements Runnable, MouseListener, QuizFenster,
		FrontendWindow {

	private static final long		serialVersionUID	= 8869415625961638325L;

	private Steuerung				meineSteuerung;
	private GrafikLib				lib;

	private VolatileImage			backbuffer;
	private GraphicsEnvironment		ge;
	private GraphicsConfiguration	gc;
	private BufferStrategy			strategy;

	// Damit dass Spiel fluessig laueft
	private long					delta				= 0;
	private long					last				= 0;
	private long					fps					= 0;

	protected Frame					frame;
	protected boolean				once				= false;
	protected boolean				blackScreen			= false;

	private BufferedImage			backgroundQuizScreen;						// Hintergrundbild
	private BufferedImage			backgroundQuizRahmen;						// Rahmen
	// am
	private Image					programmIcon;
	// Rand

	QuizRoundLogo					ecLogoPanel;

	private QuizPanelFrage			quizQuestionPanel;
	private QuizPanelAntwort		quizAnswerPanel1;
	private QuizPanelAntwort		quizAnswerPanel2;
	private QuizPanelAntwort		quizAnswerPanel3;
	private QuizPanelAntwort		quizAnswerPanel4;
	private QuizTippPanel			quizTipp;
	private QuizStatusTextPanel		quizStatusTextPanel;

	private QuizPanelJoker			quizFiftyJokerPanel;
	private QuizPanelJoker			quizTippJokerPanel;
	private QuizPanelJoker			quizPublikumsJoker;

	public SinglePlayerSchirm() {
		this( "Single-Player-Schirm", 678, 549, Biblionaer.meineSteuerung );
	}

	public SinglePlayerSchirm(String fenstername, int width, int height, Steuerung pSteuerung) {
		meineSteuerung = pSteuerung;
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

		this.setPreferredSize( new Dimension( width, height ) );

		frame = new JFrame( fenstername ); // geaendert wegen while
		// frame.setLocationRelativeTo( null );
		frame.setLocation( 200, 100 );
		frame.addKeyListener( meineSteuerung );
		// this.addMouseListener( this );
		frame.add( this );
		frame.pack();
		frame.setResizable( false );
		frame.setIgnoreRepaint( true );

		// frame.setMenuBar( createMenue() );

		frame.setVisible( true );

		createBufferStrategy( 2 );
		strategy = getBufferStrategy();
		createBackbuffer();

		doInitializations();

		// Thread anstoßen
		if ( !once ) {
			once = true;
			Thread t = new Thread( this );
			t.start();
		}
	}

	protected void createBackbuffer() {
		if ( backbuffer != null ) {
			backbuffer.flush();
			backbuffer = null;
		}
		// GraphicsConfiguration für VolatileImage
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		backbuffer = gc.createCompatibleVolatileImage( getWidth(), getHeight() );

	}

	protected void checkBackbuffer() {
		if ( backbuffer == null ) {
			createBackbuffer();
		}
		if ( backbuffer.validate( gc ) == VolatileImage.IMAGE_INCOMPATIBLE ) {
			createBackbuffer();
		}
	}

	// doPainting wird periodisch aus dem GameLoop aufgerufen
	protected void doPainting() {

		checkBackbuffer(); // Pr¸f-Methode f¸r VolatileImage

		Graphics g = backbuffer.getGraphics(); // GraphicsObject vom
		// VolatileImage holen
		g.clearRect( 0, 0, getWidth(), getHeight() );
		render( g ); // alle Zeichenoperationen: Map, Player, etc.
		g.dispose(); // Graphics-Objekt verwerfen

		// Nur dann die Bildchens zeichnen, wenn nicht von Außen der
		// "Black-Screen"-Command gegeben wurde
		if ( !this.blackScreen ) {
			render( g ); // alle Zeichenoperationen: Map, Player, etc.
		}
		else {
			g.setColor( Color.BLACK );
			g.fillRect( 0, 0, getWidth(), getHeight() );
		}

		Graphics g2 = strategy.getDrawGraphics(); // Zeichenobjekt der
		// BufferStrategy holen
		g2.drawImage( backbuffer, 0, 0, this ); // VolatileImage in den Buffer
		// zeichnen
		g2.dispose(); // GraphicsObject verwerfen

		strategy.show(); // Bufferanzeigen.
	}

	protected void berechneDelta() {

		delta = System.nanoTime() - last;
		last = System.nanoTime();

		fps = ((long) 1e9) / delta;
	}

	protected void doInitializations() {

		// I
		frame.setMenuBar( createMenue() );
		this.addMouseListener( this );

		last = System.nanoTime(); // Ohne Initialisierung stimmt die Berechnung
		// von delta nicht!!!
		berechneDelta(); // delta wird unten bei den Images benötigt

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
		this.ecLogoPanel = new QuizRoundLogo( ecLogo, 15, 15, -1 );

		BufferedImage[] quizImgQuestionBlau = lib.getSprite( "img/fragePanelBlau.png", 3, 1 );
		quizQuestionPanel = new QuizPanelFrage( quizImgQuestionBlau, -1, null );
		quizQuestionPanel.setLoop( QuizImagePanel.BLAU, QuizImagePanel.BLAU );

		int antPosY = 300; // Antwortpositionierung

		BufferedImage[] quizAnswerPanelBlau = lib.getSprite( "img/antwortPanelBlau.png", 3, 1 );
		quizAnswerPanel1 = new QuizPanelAntwort( quizAnswerPanelBlau, -quizAnswerPanelBlau[0]
				.getWidth(), antPosY, 10, antPosY, -1, null );
		quizAnswerPanel3 = new QuizPanelAntwort( quizAnswerPanelBlau, -quizAnswerPanelBlau[0]
				.getWidth(), (antPosY + 50), 10, (antPosY + 50), -1, null );
		quizAnswerPanel2 = new QuizPanelAntwort( quizAnswerPanelBlau, this.frame.getWidth(),
				antPosY, quizAnswerPanelBlau[0].getWidth(), antPosY, -1, null );
		quizAnswerPanel4 = new QuizPanelAntwort( quizAnswerPanelBlau, this.frame.getWidth(),
				(antPosY + 50), quizAnswerPanelBlau[0].getWidth(), (antPosY + 50), -1, null );

		quizAnswerPanel1.setLoop( QuizImagePanel.BLAU, QuizImagePanel.BLAU );
		quizAnswerPanel2.setLoop( QuizImagePanel.BLAU, QuizImagePanel.BLAU );
		quizAnswerPanel3.setLoop( QuizImagePanel.BLAU, QuizImagePanel.BLAU );
		quizAnswerPanel4.setLoop( QuizImagePanel.BLAU, QuizImagePanel.BLAU );

		// Joker initialisieren
		int JokerPosX = 400;
		int JokerPosY = 17;
		quizFiftyJokerPanel = new QuizPanelJoker( lib.getSprite( "img/fiftyJoker.png", 3, 1 ),
				JokerPosX, JokerPosY );
		quizTippJokerPanel = new QuizPanelJoker( lib.getSprite( "img/tippJoker.png", 3, 1 ),
				JokerPosX + 90, JokerPosY );
		// quizStatistikJokerPanel = new quizPanelJoker( lib.getSprite(
		// "img/statistikJoker.png", 3, 1 ), JokerPosX + 180, JokerPosY, this );
		quizPublikumsJoker = new QuizPanelJoker( lib.getSprite( "img/puplikumsJoker.png", 3, 1 ),
				JokerPosX + 180, JokerPosY );

		// Bibelstelle
		quizTipp = new QuizTippPanel( null );

		// Statustext
		quizStatusTextPanel = new QuizStatusTextPanel( 50, 475, backgroundQuizRahmen.getWidth() );
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

		if ( quizPublikumsJoker != null )
			quizPublikumsJoker.doLogic( delta );
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

		if ( quizPublikumsJoker != null )
			quizPublikumsJoker.drawObjects( g );

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

			// Auch dem AdministratorSchirm ein aktuelles Bild senden
			if ( Biblionaer.meinWindowController.getBackendFenster() != null
					&& Biblionaer.meinWindowController.getFrontendFenster() == this ) {
				((BackendWindow) Biblionaer.meinWindowController.getBackendFenster())
						.setFrontendScreenImage( this.backbuffer );
			}

			try {
				Thread.sleep( 10 );
			}
			catch (InterruptedException e) {}

		}
		System.exit( 0 );

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

		if ( quizPublikumsJoker != null ) {
			if ( quizPublikumsJoker.feldAngeklickt( e.getX(), e.getY() ) ) {
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

		if ( quizPublikumsJoker != null ) {
			if ( quizPublikumsJoker.feldAngeklickt( e.getX(), e.getY() )
					&& meineSteuerung.statistikJokerNochFrei() ) {
				quizPublikumsJoker.setPressedIfPossible();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if ( quizFiftyJokerPanel != null )
			quizFiftyJokerPanel.setReleasedIfPossible();

		if ( quizTippJokerPanel != null )
			quizTippJokerPanel.setReleasedIfPossible();

		if ( quizPublikumsJoker != null )
			quizPublikumsJoker.setReleasedIfPossible();
	}

	public void resetAlleJoker() {
		this.quizFiftyJokerPanel.setEnabled( true );
		this.quizPublikumsJoker.setEnabled( true );
		this.quizTippJokerPanel.setEnabled( true );

		this.quizFiftyJokerPanel.setVisible( true );
		this.quizPublikumsJoker.setVisible( true );
		this.quizTippJokerPanel.setVisible( true );
	}

	public void setAnimationAktiviert(boolean aktiviert) {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Falsch() {
		this.quizAnswerPanel1.zeigeBlau();
	}

	public void setAntwortFeld1Markiert() {
		this.quizAnswerPanel1.zeigeGelb();

	}

	public void setAntwortFeld1Normal() {
		this.quizAnswerPanel1.zeigeBlau();

	}

	public void setAntwortFeld1Richtig() {
		this.quizAnswerPanel1.zeigeGruen();

	}

	public void setAntwortFeld1Sichtbar(boolean sichtbar) {
		this.quizAnswerPanel1.setVisible( sichtbar );

	}

	public void setAntwortFeld2Falsch() {
		this.quizAnswerPanel2.zeigeBlau();
	}

	public void setAntwortFeld2Markiert() {
		this.quizAnswerPanel2.zeigeGelb();
	}

	public void setAntwortFeld2Normal() {
		this.quizAnswerPanel2.zeigeBlau();
	}

	public void setAntwortFeld2Richtig() {
		this.quizAnswerPanel2.zeigeGruen();
	}

	public void setAntwortFeld2Sichtbar(boolean sichtbar) {
		this.quizAnswerPanel2.setVisible( sichtbar );

	}

	public void setAntwortFeld3Falsch() {
		this.quizAnswerPanel3.zeigeBlau();
	}

	public void setAntwortFeld3Markiert() {
		this.quizAnswerPanel3.zeigeGelb();

	}

	public void setAntwortFeld3Normal() {
		this.quizAnswerPanel3.zeigeBlau();
	}

	public void setAntwortFeld3Richtig() {
		this.quizAnswerPanel3.zeigeGruen();
	}

	public void setAntwortFeld3Sichtbar(boolean sichtbar) {
		this.quizAnswerPanel3.setVisible( sichtbar );
	}

	public void setAntwortFeld4Falsch() {
		this.quizAnswerPanel4.zeigeBlau();
	}

	public void setAntwortFeld4Markiert() {
		this.quizAnswerPanel4.zeigeGelb();
	}

	public void setAntwortFeld4Normal() {
		this.quizAnswerPanel4.zeigeBlau();

	}

	public void setAntwortFeld4Richtig() {
		this.quizAnswerPanel4.zeigeGruen();

	}

	public void setAntwortFeld4Sichtbar(boolean sichtbar) {
		this.quizAnswerPanel4.setVisible( sichtbar );
	}

	public void setAntwortFelderSichtbar(boolean sichtbar) {
		this.quizAnswerPanel1.setVisible( sichtbar );
		this.quizAnswerPanel2.setVisible( sichtbar );
		this.quizAnswerPanel3.setVisible( sichtbar );
		this.quizAnswerPanel4.setVisible( sichtbar );

	}

	public void setFiftyJokerBenutzt(boolean benutzt) {
		this.quizFiftyJokerPanel.setEnabled( !benutzt );
	}

	public void setFiftyJokerSichtbar(boolean sichtbar) {
		this.quizFiftyJokerPanel.setVisible( sichtbar );
	}

	public void setFrage(Quizfrage frage, boolean mitAnimation) {
		if ( mitAnimation ) {
			quizQuestionPanel.resetAnimation();
			quizAnswerPanel1.resetAnimation();
			quizAnswerPanel2.resetAnimation();
			quizAnswerPanel3.resetAnimation();
			quizAnswerPanel4.resetAnimation();
		}

		if ( frage == null ) {
			quizQuestionPanel.setFrage( frage );
			quizTipp.setFrage( frage );
			quizAnswerPanel1.setAusgabeString( null );
			quizAnswerPanel2.setAusgabeString( null );
			quizAnswerPanel3.setAusgabeString( null );
			quizAnswerPanel4.setAusgabeString( null );
		}
		else {
			quizQuestionPanel.setFrage( frage );
			quizTipp.setFrage( frage );
			quizAnswerPanel1.setAusgabeString( frage.getAntwort1() );
			quizAnswerPanel2.setAusgabeString( frage.getAntwort2() );
			quizAnswerPanel3.setAusgabeString( frage.getAntwort3() );
			quizAnswerPanel4.setAusgabeString( frage.getAntwort4() );
		}
	}

	public void setFrageFeldSichtbar(boolean sichtbar) {
		this.quizQuestionPanel.setVisible( sichtbar );
	}

	public void setPublikumsJokerBenutzt(boolean benutzt) {
		this.quizPublikumsJoker.setEnabled( !benutzt );
	}

	public void setPublikumsJokerSichtbar(boolean sichtbar) {
		this.quizPublikumsJoker.setVisible( sichtbar );
	}

	public void setTippJokerBenutzt(boolean benutzt) {
		this.quizTippJokerPanel.setEnabled( !benutzt );
	}

	public void setTippJokerSichtbar(boolean sichtbar) {
		this.quizTippJokerPanel.setVisible( sichtbar );
	}

	public void setCountdownText(String text) {
		this.setStatusText( text );
	}

	public void setStatusText(String text) {
		this.quizStatusTextPanel.setText( text );
	}

	public void setAntwortfelderFalsch() {
		this.quizAnswerPanel1.zeigeBlau();
		this.quizAnswerPanel2.zeigeBlau();
		this.quizAnswerPanel3.zeigeBlau();
		this.quizAnswerPanel4.zeigeBlau();
	}

	public void setAntwortfelderMariert() {
		this.quizAnswerPanel1.zeigeGelb();
		this.quizAnswerPanel2.zeigeGelb();
		this.quizAnswerPanel3.zeigeGelb();
		this.quizAnswerPanel4.zeigeGelb();
	}

	public void setAntwortfelderNormal() {
		this.quizAnswerPanel1.zeigeBlau();
		this.quizAnswerPanel2.zeigeBlau();
		this.quizAnswerPanel3.zeigeBlau();
		this.quizAnswerPanel4.zeigeBlau();
	}

	public void setAntwortfelderRichtig() {
		this.quizAnswerPanel1.zeigeGruen();
		this.quizAnswerPanel2.zeigeGruen();
		this.quizAnswerPanel3.zeigeGruen();
		this.quizAnswerPanel4.zeigeGruen();
	}

	public void playFrageFalsch() {
	// Do nothing
	}

	public void playFrageRichtig() {
	// Do nothing
	}

	public void playSpielGewonnen() {
		// setze alle Spielfeldfarben auf gelb
		this.setAntwortFeld1Markiert();
		this.setAntwortFeld2Markiert();
		this.setAntwortFeld3Markiert();
		this.setAntwortFeld4Markiert();

		this.quizQuestionPanel.setLoop( 2, 2 );

		// Eventuell hier noch einen Sound abspielen
	}

	// * Ab hier die Interfacemethoden für FrontendWindow
	public void setBildschirmSchwarz(boolean schwarzerBildschirm) {
		this.blackScreen = schwarzerBildschirm;
	}

}
