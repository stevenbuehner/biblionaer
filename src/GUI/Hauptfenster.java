package GUI;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

import quiz.Quizfrage;
import quiz.Steuerung;
import Grafik.GrafikLib;

public abstract class Hauptfenster extends Canvas {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	protected Steuerung				meineSteuerung;
	protected GrafikLib				lib;

	protected VolatileImage			backbuffer;
	protected GraphicsEnvironment	ge;
	protected GraphicsConfiguration	gc;
	protected BufferStrategy		strategy;

	// Damit dass Spiel fluessig laueft
	protected long					delta				= 0;
	protected long					last				= 0;
	protected long					fps					= 0;

	Frame							frame;
	boolean							once				= false;

	protected BufferedImage			backgroundQuizScreen;			// Hintergrundbild
	protected BufferedImage			backgroundQuizRahmen;			// Rahmen am
	protected Image					programmIcon;
	// Rand

	QuizRoundLogo					ecLogoPanel;

	public quizPanelFrage			quizQuestionPanel;
	public quizPanelAntwort			quizAnswerPanel1;
	public quizPanelAntwort			quizAnswerPanel2;
	public quizPanelAntwort			quizAnswerPanel3;
	public quizPanelAntwort			quizAnswerPanel4;
	public QuizTippPanel			quizTipp;
	protected QuizStatusTextPanel	quizStatusTextPanel;

	public quizPanelJoker			quizFiftyJokerPanel;
	public quizPanelJoker			quizTippJokerPanel;
	public quizPanelJoker			quizStatistikJokerPanel;
	public quizPanelJoker			quizPubplikumsJoker;

	public Hauptfenster() {
		// TODO Auto-generated constructor stub
		this( "Hauptfenster ", 678, 549, null );
	}

	public Hauptfenster(String pFenstername, int width, int height, Steuerung pSteuerung) {

		meineSteuerung = pSteuerung;
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

		this.setPreferredSize( new Dimension( width, height ) );

		frame = new JFrame( pFenstername ); // geaendert wegen while
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

		// if ( !once ) {
		// once = true;
		// Thread t = new Thread( this );
		// t.start();
		// }

	}

	protected void createBackbuffer() {
		if ( backbuffer != null ) {
			backbuffer.flush();
			backbuffer = null;
		}
		// GraphicsConfiguration fŸr VolatileImage
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

	protected abstract void doInitializations();

	protected abstract MenuBar createMenue();

	public void setStatusText(String pStatusText) {
		this.quizStatusTextPanel.setText( pStatusText );
	}

	public String getStatusText() {
		return this.quizStatusTextPanel.getText();
	}

	protected abstract void doLogic();

	protected abstract void moveObjects();

	public abstract void render(Graphics g);

	
	public abstract void frageAnzeigen(Quizfrage pFrage, boolean mitAnimation);

	// doPainting wird periodisch aus dem GameLoop aufgerufen
	protected void doPainting() {

		checkBackbuffer(); // Prüf-Methode für VolatileImage

		Graphics g = backbuffer.getGraphics(); // GraphicsObject vom
		// VolatileImage holen
		g.clearRect( 0, 0, getWidth(), getHeight() );
		render( g ); // alle Zeichenoperationen: Map, Player, etc.
		g.dispose(); // Graphics-Objekt verwerfen

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

}
