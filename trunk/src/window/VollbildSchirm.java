package window;

import interfaces.BackendWindow;
import interfaces.FrontendWindow;
import interfaces.QuizFenster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

import main.Biblionaer;
import quiz.Quizfrage;
import windowElements.QuizPanelJoker;
import windowElements.QuizPolygonPanelAntwort;
import windowElements.QuizPolygonPanelFrage;
import windowElements.QuizRectPanelTopLeiste;
import Grafik.GrafikLib;

public class VollbildSchirm extends JFrame implements QuizFenster, Runnable, FrontendWindow {

	private static final long serialVersionUID = 5136010910135552829L;
	private GraphicsDevice device;
	private DisplayMode originalDM; // Zukunftsorientiert
	private boolean isFullScreen = false;

	private VolatileImage backbuffer;
	private GraphicsEnvironment ge;
	private GraphicsConfiguration gc;
	private BufferStrategy strategy;
	boolean once = false;
	boolean blackScreen = false;

	// Damit dass Spiel fluessig laueft
	private long delta = 0;
	private long last = 0;
	private long fps = 0;

	// Grafikverwaltung
	private GrafikLib lib;

	// Felder des Spieles
	private QuizPolygonPanelAntwort antwort1;
	private QuizPolygonPanelAntwort antwort2;
	private QuizPolygonPanelAntwort antwort3;
	private QuizPolygonPanelAntwort antwort4;

	private QuizPolygonPanelFrage frage;

	private QuizRectPanelTopLeiste topLeisteBG;
	private QuizPanelJoker fiftyJokerPanel;
	private QuizPanelJoker tippJokerPanel;
	private QuizPanelJoker publikumsJoker;

	public VollbildSchirm(String fenstername, GraphicsDevice device, boolean vollbild) {
		super(device.getDefaultConfiguration());
		this.isFullScreen = vollbild;

		this.setIgnoreRepaint(true);
		this.setResizable(false);
		this.setSize(new Dimension(1024, 768));
		this.setBackground(Color.BLACK);

		this.device = device;
		setTitle(fenstername);
		originalDM = device.getDisplayMode();

		this.addKeyListener(Biblionaer.meineSteuerung);

		doInitializations();

		// Wenn mäglich, dann Fullscreen setzen
		// isFullScreen = device.isFullScreenSupported();
		setUndecorated(isFullScreen);
		setResizable(!isFullScreen);
		if (isFullScreen) {
			// Full-screen mode
			device.setFullScreenWindow(this);
			validate();
		} else {
			this.setVisible(true);
			this.setResizable(false);
		}

		createBufferStrategy(2);
		strategy = getBufferStrategy();
		createBackbuffer();

		// Thread anstoäen
		if (!once) {
			once = true;
			Thread t = new Thread(this);
			t.start();
		}
	}

	private void doInitializations() {
		last = System.nanoTime(); // Ohne Initialisierung stimmt die Berechnung
		// von delta nicht!!!
		berechneDelta(); // delta wird unten bei den Images benätigt

		// Lib mit Bilder einbinden
		lib = GrafikLib.getInstance();

		// Antwortpositionierung
		// Zentrieren der Antwortfelder und berechnen der Grääen
		int screenWidth = this.device.getDisplayMode().getWidth();
		int screenHeight = this.device.getDisplayMode().getHeight();
		int screenUsedHeight = this.device.getDisplayMode().getHeight();
		int screenUsedWidth = this.device.getDisplayMode().getWidth();
		if (screenWidth * 1.3334 < screenHeight) {
			screenUsedHeight = (int) (screenWidth * 1.3334);
		}
		if (!isFullScreen) {
			screenWidth = this.getWidth();
			screenHeight = this.getHeight();
			screenUsedHeight = this.getHeight();
			screenUsedWidth = this.getWidth();
			if (screenWidth * 1.3334 < screenHeight) {
				screenUsedHeight = (int) (screenWidth * 1.3334);
			}
		}

		int abstandObenFuerJoker = 100;
		int abstandZuJoker = (int) (screenUsedHeight * 0.095);
		int ganzerFragekastenHeight = (int) (screenUsedHeight * 0.548);
		int ganzerFragekastenWidth = (int) (screenUsedWidth * 0.769);

		initFragekasten(ganzerFragekastenHeight, ganzerFragekastenWidth, (screenWidth - ganzerFragekastenWidth) / 2,
				abstandObenFuerJoker + abstandZuJoker);

		// Die Top-Leiste
		topLeisteBG = new QuizRectPanelTopLeiste(0, 0, screenWidth, abstandObenFuerJoker, -1);

		// Joker initialisieren
		int jokerBreite = 87;
		int jokerAbstandX = 10;
		int JokerPosX = (screenWidth - ganzerFragekastenWidth) / 2 + ganzerFragekastenWidth - jokerBreite * 3
				- jokerAbstandX * 2;
		int JokerPosY = 17;
		fiftyJokerPanel = new QuizPanelJoker(lib.getSprite("img/fiftyJoker.png", 3, 1), JokerPosX, JokerPosY);
		tippJokerPanel = new QuizPanelJoker(lib.getSprite("img/tippJoker.png", 3, 1), JokerPosX + jokerBreite
				+ jokerAbstandX, JokerPosY);
		publikumsJoker = new QuizPanelJoker(lib.getSprite("img/puplikumsJoker.png", 3, 1), JokerPosX + jokerBreite * 2
				+ jokerAbstandX * 2, JokerPosY);

	}

	private void initFragekasten(int height, int width, int posX, int posY) {
		int antwortKastenHeight = (int) (height * 0.176);
		int frageKastenHeight = (int) (height - antwortKastenHeight * 15 / 4);
		int heightFrageUndSchwGrad = (int) (height * 0.5);

		frage = new QuizPolygonPanelFrage(posX, posY, width, heightFrageUndSchwGrad, antwortKastenHeight,
				frageKastenHeight, -1);

		int abstandZwischenAntwortenHeight = (int) (antwortKastenHeight / 3);
		int abstandZwischenAntwortenWidth = abstandZwischenAntwortenHeight;
		int antwortKastenWidth = (int) ((width - abstandZwischenAntwortenWidth) / 2);

		antwort1 = new QuizPolygonPanelAntwort(posX, posY + height - antwortKastenHeight * 2
				- abstandZwischenAntwortenHeight, antwortKastenWidth, antwortKastenHeight, "A: ", -1);

		antwort2 = new QuizPolygonPanelAntwort(posX + antwortKastenWidth + abstandZwischenAntwortenWidth, posY + height
				- antwortKastenHeight * 2 - abstandZwischenAntwortenHeight, antwortKastenWidth, antwortKastenHeight,
				"B: ", -1);

		antwort3 = new QuizPolygonPanelAntwort(posX, posY + height - antwortKastenHeight, antwortKastenWidth,
				antwortKastenHeight, "C: ", -1);

		antwort4 = new QuizPolygonPanelAntwort(posX + antwortKastenWidth + abstandZwischenAntwortenWidth, posY + height
				- antwortKastenHeight, antwortKastenWidth, antwortKastenHeight, "D: ", -1);

	}

	protected void createBackbuffer() {
		if (backbuffer != null) {
			backbuffer.flush();
			backbuffer = null;
		}
		// GraphicsConfiguration fär VolatileImage
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		backbuffer = gc.createCompatibleVolatileImage(getWidth(), getHeight());

	}

	protected void checkBackbuffer() {
		if (backbuffer == null) {
			createBackbuffer();
		}
		if (backbuffer.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
			createBackbuffer();
		}
	}

	// doPainting wird periodisch aus dem GameLoop aufgerufen
	protected void doPainting() {

		checkBackbuffer(); // Präf-Methode fär VolatileImage

		Graphics g = backbuffer.getGraphics(); // GraphicsObject vom
		// VolatileImage holen
		// g.clearRect( 0, 0, getWidth(), getHeight() );
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Nur dann die Bildchens zeichnen, wenn nicht von Auäen der
		// "Black-Screen"-Command gegeben wurde
		if (!this.blackScreen) {
			render(g); // alle Zeichenoperationen: Map, Player, etc.
		} else {

		}

		g.dispose(); // Graphics-Objekt verwerfen

		Graphics g2 = strategy.getDrawGraphics(); // Zeichenobjekt der
		// BufferStrategy holen
		g2.drawImage(backbuffer, 0, 0, this); // VolatileImage in den Buffer
		// zeichnen
		g2.dispose(); // GraphicsObject verwerfen

		strategy.show(); // Bufferanzeigen.
	}

	protected void berechneDelta() {

		delta = System.nanoTime() - last;
		last = System.nanoTime();

		if (delta != 0)
			fps = ((long) 1e9) / delta;
	}

	protected void doLogic() {
		topLeisteBG.doLogic(delta);
		fiftyJokerPanel.doLogic(delta);
		publikumsJoker.doLogic(delta);
		tippJokerPanel.doLogic(delta);

		frage.doLogic(delta);
		antwort1.doLogic(delta);
		antwort2.doLogic(delta);
		antwort3.doLogic(delta);
		antwort4.doLogic(delta);
	}

	protected void moveObjects() {
		topLeisteBG.move(delta);
		fiftyJokerPanel.move(delta);
		publikumsJoker.move(delta);
		tippJokerPanel.move(delta);

		frage.move(delta);
		antwort1.move(delta);
		antwort2.move(delta);
		antwort3.move(delta);
		antwort4.move(delta);
	}

	public void render(Graphics g) {

		topLeisteBG.drawObjects(g);
		fiftyJokerPanel.drawObjects(g);
		publikumsJoker.drawObjects(g);
		tippJokerPanel.drawObjects(g);

		frage.drawObjects(g);
		antwort1.drawObjects(g);
		antwort2.drawObjects(g);
		antwort3.drawObjects(g);
		antwort4.drawObjects(g);

		if (Biblionaer.meineEinstellungen.getPingAnzeigen())
			g.drawString(Long.toString(fps), 20, 20);

	}

	public void run() {

		while (this.isVisible()) {

			berechneDelta();

			if (Biblionaer.meineSteuerung.getStatus() > 0) {
				doLogic();
				moveObjects();
			}

			doPainting();

			try {
				// Auch dem AdministratorSchirm ein aktuelles Bild senden
				if (Biblionaer.meinWindowController.getBackendFenster() != null
						&& Biblionaer.meinWindowController.getFrontendFenster() == this) {
					((BackendWindow) Biblionaer.meinWindowController.getBackendFenster())
							.setFrontendScreenImage(this.backbuffer);
				}
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}

		}
		this.dispose();
	}

	public void setBildschirmSchwarz(boolean schwarzerBildschirm) {
		blackScreen = schwarzerBildschirm;
	}

	public void setAnimationAktiviert(boolean aktiviert) {
		// TODO Auto-generated method stub

	}

	// Ab hier die Mehtoden fär das QuizFenster
	public void playFrageFalsch() {
		// TODO Auto-generated method stub
	}

	public void playFrageRichtig() {
		// TODO Auto-generated method stub
	}

	public void playSpielGewonnen() {
		this.setAntwortFeld1Markiert();
		this.setAntwortFeld2Markiert();
		this.setAntwortFeld3Markiert();
		this.setAntwortFeld4Markiert();
	}

	public void resetAlleJoker() {
		this.fiftyJokerPanel.setEnabled(true);
		this.publikumsJoker.setEnabled(true);
		this.tippJokerPanel.setEnabled(true);

		this.fiftyJokerPanel.setVisible(true);
		this.publikumsJoker.setVisible(true);
		this.tippJokerPanel.setVisible(true);
	}

	public void setAntwortFeld1Falsch() {
		this.antwort1.setStatusFalsch();
	}

	public void setAntwortFeld1Markiert() {
		this.antwort1.setStatusMarkiert();
	}

	public void setAntwortFeld1Normal() {
		this.antwort1.setStatusNormal();
	}

	public void setAntwortFeld1Richtig() {
		this.antwort1.setStatusRichtig();
	}

	public void setAntwortFeld1Sichtbar(boolean sichtbar) {
		this.antwort1.setVisible(sichtbar);
	}

	public void setAntwortFeld2Falsch() {
		this.antwort2.setStatusFalsch();
	}

	public void setAntwortFeld2Markiert() {
		this.antwort2.setStatusMarkiert();
	}

	public void setAntwortFeld2Normal() {
		this.antwort2.setStatusNormal();
	}

	public void setAntwortFeld2Richtig() {
		this.antwort2.setStatusRichtig();
	}

	public void setAntwortFeld2Sichtbar(boolean sichtbar) {
		this.antwort2.setVisible(sichtbar);
	}

	public void setAntwortFeld3Falsch() {
		this.antwort3.setStatusFalsch();
	}

	public void setAntwortFeld3Markiert() {
		this.antwort3.setStatusMarkiert();

	}

	public void setAntwortFeld3Normal() {
		this.antwort3.setStatusNormal();
	}

	public void setAntwortFeld3Richtig() {
		this.antwort3.setStatusRichtig();
	}

	public void setAntwortFeld3Sichtbar(boolean sichtbar) {
		this.antwort3.setVisible(sichtbar);
	}

	public void setAntwortFeld4Falsch() {
		this.antwort4.setStatusFalsch();
	}

	public void setAntwortFeld4Markiert() {
		this.antwort4.setStatusMarkiert();
	}

	public void setAntwortFeld4Normal() {
		this.antwort4.setStatusNormal();
	}

	public void setAntwortFeld4Richtig() {
		this.antwort4.setStatusRichtig();
	}

	public void setAntwortFeld4Sichtbar(boolean sichtbar) {
		this.antwort4.setVisible(sichtbar);
	}

	public void setAntwortFelderSichtbar(boolean sichtbar) {
		this.antwort1.setVisible(sichtbar);
		this.antwort2.setVisible(sichtbar);
		this.antwort3.setVisible(sichtbar);
		this.antwort4.setVisible(sichtbar);
	}

	public void setAntwortfelderFalsch() {
		this.antwort1.setStatusFalsch();
		this.antwort2.setStatusFalsch();
		this.antwort3.setStatusFalsch();
		this.antwort4.setStatusFalsch();
	}

	public void setAntwortfelderMariert() {
		this.antwort1.setStatusMarkiert();
		this.antwort2.setStatusMarkiert();
		this.antwort3.setStatusMarkiert();
		this.antwort4.setStatusMarkiert();

	}

	public void setAntwortfelderNormal() {
		this.antwort1.setStatusNormal();
		this.antwort2.setStatusNormal();
		this.antwort3.setStatusNormal();
		this.antwort4.setStatusNormal();
	}

	public void setAntwortfelderRichtig() {
		this.antwort1.setStatusRichtig();
		this.antwort2.setStatusRichtig();
		this.antwort3.setStatusRichtig();
		this.antwort4.setStatusRichtig();
	}

	public void setCountdownText(String text) {
		this.setStatusText(text);
	}

	public void setFiftyJokerBenutzt(boolean benutzt) {
		this.fiftyJokerPanel.setEnabled(!benutzt);
	}

	public void setFiftyJokerSichtbar(boolean sichtbar) {
		this.fiftyJokerPanel.setVisible(sichtbar);
	}

	public void setFrageAnzuzeigen(Quizfrage frage, boolean mitAnimation) {
		if (frage != null) {
			this.frage.setFrage(frage);

			this.antwort1.setAusgabeString(frage.getAntwort1());
			this.antwort2.setAusgabeString(frage.getAntwort2());
			this.antwort3.setAusgabeString(frage.getAntwort3());
			this.antwort4.setAusgabeString(frage.getAntwort4());
		} else {
			this.frage.setFrage(null);

			this.antwort1.setAusgabeString(null);
			this.antwort2.setAusgabeString(null);
			this.antwort3.setAusgabeString(null);
			this.antwort4.setAusgabeString(null);
		}
	}

	public void setFrageFeldSichtbar(boolean sichtbar) {
		this.frage.setVisible(sichtbar);
	}

	public void setPublikumsJokerBenutzt(boolean benutzt) {
		this.publikumsJoker.setEnabled(!benutzt);
	}

	public void setPublikumsJokerSichtbar(boolean sichtbar) {
		this.publikumsJoker.setVisible(sichtbar);
	}

	public void setStatusText(String text) {
	}

	public void setTippJokerBenutzt(boolean benutzt) {
		this.tippJokerPanel.setEnabled(!benutzt);
	}

	public void setTippJokerSichtbar(boolean sichtbar) {
		this.tippJokerPanel.setVisible(sichtbar);
	}

	public void killYourSelf() {
		this.setVisible(false);
	}

	public void playStarteSpiel() {
	}

	@Override
	public void spielBeendet() {
	}

	@Override
	public void spielGestartet() {
	}

}
