package window;

import interfaces.BackendWindow;
import interfaces.FrontendWindow;
import interfaces.QuizFenster;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.VolatileImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Biblionaer;

import quiz.Quizfrage;
import Grafik.GrafikLib;

public class AdministratorSchirm extends JFrame implements QuizFenster, BackendWindow,
		ActionListener {

	private static final long		serialVersionUID		= 1L;

	protected JPanel				monitorPanel;																			// links
	// oben
	protected JPanel				steuerungPanel;																		// rechts
	// oben
	protected JPanel				dateiPanel;																			// links
	// unten
	protected JPanel				weiteresPanel;																			// rechts
	// unten

	private GraphicsDevice			device;
	private boolean					isFullScreen			= false;

	private GraphicsEnvironment		ge;
	private GraphicsConfiguration	gc;

	// Grafikverwaltung
	private GrafikLib				lib;

	// SteuerungPanel Material
	JButton							auswahlBestaetigenBtn	= new JButton( "Auswahl Bestätigen" );
	JButton							antwort1KlickenBtn		= new JButton( "Antwort A" );
	JButton							antwort2KlickenBtn		= new JButton( "Antwort B" );
	JButton							antwort3KlickenBtn		= new JButton( "Antwort C" );
	JButton							antwort4KlickenBtn		= new JButton( "Antwort D" );
	JLabel							bibelstelleLabel		= new JLabel( "Bibelstelle" );

	JButton							fiftyJoker				= new JButton( "50:50 Joker" );
	JButton							tippJokerBtn			= new JButton( "Tipp Joker" );
	JButton							puplikumsJokerBtn		= new JButton( "Puplikums Joker" );

	// DateiPanel Material
	JButton							neuesSpielStartenBtn	= new JButton( "Neues Spiel starten" );
	JButton							laufendsSpielBeendenBtn	= new JButton(
																	"laufendes Spiel beenden" );
	JButton							neuesSpielAusInternBtn	= new JButton(
																	"Neues Spiel aus dem Intert zur Liste hinzufügen" );

	// WeiteresPanel Material
	JButton							leisteEinblendenBtn		= new JButton( "Leiste einblenden" );
	JButton							schwarzerBildschirmBtn	= new JButton( "schwarzer Bildschirm" );

	public AdministratorSchirm(String fenstername, GraphicsDevice device, boolean vollbildModus) {
		super( fenstername );
		this.device = device;
		this.isFullScreen = vollbildModus;

		doInitialisations();

		this.setResizable( false );
		// Wenn möglich, dann Fullscreen setzen
		// isFullScreen = device.isFullScreenSupported();
		setUndecorated( isFullScreen );
		setResizable( !isFullScreen );
		if ( isFullScreen ) {
			// Full-screen mode
			device.setFullScreenWindow( this );
		}
		else {
			// Window mode
			this.setVisible( true );
			this.setSize( new Dimension( 1024, 768 ) );
			this.setResizable( false );
		}
	}

	private void doInitialisations() {
		Container cnt = this.getContentPane();

		// ******* GesamtPanel *******
		JPanel mainPanel = new JPanel( new GridLayout( 2, 2, 5, 5 ) );
		mainPanel.setBackground( Color.LIGHT_GRAY );

		// ******* MonitorPanel *******
		this.monitorPanel = new JPanel( new FlowLayout() );
		this.monitorPanel.setBackground( Color.BLACK );

		// ******* SteuerungPanel *******
		this.steuerungPanel = new JPanel( new GridLayout( 2, 1, 20, 20 ) );
		this.steuerungPanel.setBackground( Color.BLACK );

		// TopLeisten-Panel
		JPanel steuerungTopLeistePanel = new JPanel( new FlowLayout() );
		// Button
		auswahlBestaetigenBtn.addActionListener( this );
		fiftyJoker.addActionListener( this );
		tippJokerBtn.addActionListener( this );
		puplikumsJokerBtn.addActionListener( this );

		auswahlBestaetigenBtn.setEnabled( false );
		fiftyJoker.setEnabled( false );
		tippJokerBtn.setEnabled( false );
		puplikumsJokerBtn.setEnabled( false );

		steuerungTopLeistePanel.add( auswahlBestaetigenBtn );
		steuerungTopLeistePanel.add( fiftyJoker );
		steuerungTopLeistePanel.add( tippJokerBtn );
		steuerungTopLeistePanel.add( puplikumsJokerBtn );

		// Quiz-Panel
		JPanel steuerungQuizPanel = new JPanel( new GridLayout( 3, 2, 5, 5 ) );
		antwort1KlickenBtn.addActionListener( this );
		antwort2KlickenBtn.addActionListener( this );
		antwort3KlickenBtn.addActionListener( this );
		antwort4KlickenBtn.addActionListener( this );

		antwort1KlickenBtn.setEnabled( false );
		antwort2KlickenBtn.setEnabled( false );
		antwort3KlickenBtn.setEnabled( false );
		antwort4KlickenBtn.setEnabled( false );

		steuerungQuizPanel.add( this.antwort1KlickenBtn );
		steuerungQuizPanel.add( this.antwort2KlickenBtn );
		steuerungQuizPanel.add( this.antwort3KlickenBtn );
		steuerungQuizPanel.add( this.antwort4KlickenBtn );
		steuerungQuizPanel.add( new JLabel( "Bibelstelle: " ) );
		steuerungQuizPanel.add( this.bibelstelleLabel );

		steuerungPanel.add( steuerungTopLeistePanel );
		steuerungPanel.add( steuerungQuizPanel );

		// ******* DateiPanel *******
		this.dateiPanel = new JPanel( new GridLayout( 1, 2, 20, 20 ) );
		this.dateiPanel.setBackground( Color.BLACK );

		JPanel dateiLinkesPanel = new JPanel( new FlowLayout() );
		dateiLinkesPanel.add( new JLabel( "offline Spiel Liste" ) );

		JPanel dateiRechtesPanel = new JPanel( new GridLayout( 3, 1 ) );
		// Buttons
		neuesSpielStartenBtn.addActionListener( this );
		laufendsSpielBeendenBtn.addActionListener( this );
		neuesSpielAusInternBtn.addActionListener( this );

		laufendsSpielBeendenBtn.setEnabled( false );

		dateiRechtesPanel.add( neuesSpielStartenBtn );
		dateiRechtesPanel.add( laufendsSpielBeendenBtn );
		dateiRechtesPanel.add( neuesSpielAusInternBtn );

		// Zusammenfügen
		dateiPanel.add( dateiLinkesPanel );
		dateiPanel.add( dateiRechtesPanel );

		// ******* WeiteresPanel *******
		this.weiteresPanel = new JPanel( new GridLayout( 6, 1, 20, 20 ) );
		this.weiteresPanel.setBackground( Color.BLACK );

		// Buttons
		leisteEinblendenBtn.addActionListener( this );
		schwarzerBildschirmBtn.addActionListener( this );

		weiteresPanel.add( leisteEinblendenBtn );
		weiteresPanel.add( schwarzerBildschirmBtn );
		weiteresPanel.add( new JLabel( "Uhrzeit: ______" ) );
		weiteresPanel.add( new JLabel( "Aktuelle Frage Zeit ______" ) );
		weiteresPanel.add( new JLabel( "Gesamte Spielzeit: _______" ) );

		// Alles zusammenfügen
		mainPanel.add( monitorPanel );
		mainPanel.add( steuerungPanel );
		mainPanel.add( dateiPanel );
		mainPanel.add( weiteresPanel );

		cnt.add( mainPanel );
	}

	// * Ab hier die Methode für das Interface BackendWindow
	public void setFrontendScreenImage(VolatileImage screen) {
		Graphics g = monitorPanel.getGraphics();
		g.drawImage( screen, 0, 0, monitorPanel.getWidth(), monitorPanel.getHeight(), this );

	}

	// * Ab hier die Methode für das Interface ActionListener
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == antwort1KlickenBtn ) {
			Biblionaer.meineKonsole.println( "AdministratorSchirm: Klick auf Antwort1Btn", 4 );
			Biblionaer.meineSteuerung.klickAufAntwortFeld( 1 );
		}
		else if ( e.getSource() == antwort2KlickenBtn ) {
			Biblionaer.meineKonsole.println( "AdministratorSchirm: Klick auf Antwort2Btn", 4 );
			Biblionaer.meineSteuerung.klickAufAntwortFeld( 2 );
		}
		else if ( e.getSource() == antwort3KlickenBtn ) {
			Biblionaer.meineKonsole.println( "AdministratorSchirm: Klick auf Antwort3Btn", 4 );
			Biblionaer.meineSteuerung.klickAufAntwortFeld( 3 );
		}
		else if ( e.getSource() == antwort4KlickenBtn ) {
			Biblionaer.meineKonsole.println( "AdministratorSchirm: Klick auf Antwort4Btn", 4 );
			Biblionaer.meineSteuerung.klickAufAntwortFeld( 4 );
		}
		else if ( e.getSource() == auswahlBestaetigenBtn ) {

		}
		else if ( e.getSource() == fiftyJoker ) {
			Biblionaer.meineSteuerung.klickAufFiftyJoker();
			fiftyJoker.setEnabled( false );
		}
		else if ( e.getSource() == tippJokerBtn ) {
			Biblionaer.meineSteuerung.klickAufTippJoker();
			tippJokerBtn.setEnabled( false );
		}
		else if ( e.getSource() == puplikumsJokerBtn ) {
			Biblionaer.meineSteuerung.puplikumsJokerZeitAbgelaufen();
			puplikumsJokerBtn.setEnabled( false );
		}
		else if ( e.getSource() == neuesSpielStartenBtn ) {

		}
		else if ( e.getSource() == laufendsSpielBeendenBtn ) {

		}
		else if ( e.getSource() == neuesSpielAusInternBtn ) {

		}
		else if ( e.getSource() == leisteEinblendenBtn ) {

		}
		else if ( e.getSource() == schwarzerBildschirmBtn ) {
			schwarzerBildschirmBtnKlick();
		}
		else {
			Biblionaer.meineKonsole.println( "AdminSchirm mit unbekanntem Action Event: "
					+ e.getActionCommand(), 2 );
		}
	}

	// * Ab hier die Button-Klick-Methoden

	protected void schwarzerBildschirmBtnKlick() {
		if ( schwarzerBildschirmBtn.getText().equals( "schwarzer Bildschirm" ) ) {
			if ( Biblionaer.meinWindowController.getFrontendFenster() != null ) {
				// Wenn es ein Frontend gibt, dann schwärzen
				((FrontendWindow) Biblionaer.meinWindowController.getFrontendFenster())
						.setBildschirmSchwarz( true );
				schwarzerBildschirmBtn.setText( "Bilschirm wieder einblenden" );
			}
		}
		else {
			if ( Biblionaer.meinWindowController.getFrontendFenster() != null ) {
				// Wenn es ein Frontend gibt, dann wieder Bild herstellen
				((FrontendWindow) Biblionaer.meinWindowController.getFrontendFenster())
						.setBildschirmSchwarz( false );
			}
			// Egal ob es ein Frontend gibt oder nicht, Button immer wieder
			// herstellen.
			schwarzerBildschirmBtn.setText( "schwarzer Bildschirm" );
		}
	}

	// * Ab hier die Methoden zur Ansteuerung über die Steuerung
	public void playFrageFalsch() {
	// TODO Auto-generated method stub

	}

	public void playFrageRichtig() {
	// TODO Auto-generated method stub

	}

	public void playSpielGewonnen() {
	// TODO Auto-generated method stub

	}

	public void resetAlleJoker() {
		fiftyJoker.setEnabled( true );
		tippJokerBtn.setEnabled( true );
		puplikumsJokerBtn.setEnabled( true );
	}

	public void setAnimationAktiviert(boolean aktiviert) {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Falsch() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Markiert() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Normal() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Richtig() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Sichtbar(boolean sichtbar) {
		antwort1KlickenBtn.setEnabled( sichtbar );

	}

	public void setAntwortFeld2Falsch() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld2Markiert() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld2Normal() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld2Richtig() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld2Sichtbar(boolean sichtbar) {
		antwort2KlickenBtn.setEnabled( sichtbar );

	}

	public void setAntwortFeld3Falsch() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld3Markiert() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld3Normal() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld3Richtig() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld3Sichtbar(boolean sichtbar) {
		antwort3KlickenBtn.setEnabled( sichtbar );

	}

	public void setAntwortFeld4Falsch() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld4Markiert() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld4Normal() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld4Richtig() {
	// TODO Auto-generated method stub

	}

	public void setAntwortFeld4Sichtbar(boolean sichtbar) {
		antwort4KlickenBtn.setEnabled( sichtbar );
	}

	public void setAntwortFelderSichtbar(boolean sichtbar) {
		this.antwort1KlickenBtn.setEnabled( sichtbar );
		this.antwort2KlickenBtn.setEnabled( sichtbar );
		this.antwort3KlickenBtn.setEnabled( sichtbar );
		this.antwort4KlickenBtn.setEnabled( sichtbar );
	}

	public void setAntwortfelderFalsch() {
	// TODO Auto-generated method stub

	}

	public void setAntwortfelderMariert() {
	// TODO Auto-generated method stub

	}

	public void setAntwortfelderNormal() {
	// TODO Auto-generated method stub

	}

	public void setAntwortfelderRichtig() {
	// TODO Auto-generated method stub

	}

	public void setCountdownText(String text) {
		this.setStatusText( text );
	}

	public void setFiftyJokerBenutzt(boolean benutzt) {
		this.fiftyJoker.setEnabled( !benutzt );
	}

	public void setFiftyJokerSichtbar(boolean sichtbar) {
		this.fiftyJoker.setEnabled( sichtbar );
	}

	public void setFrage(Quizfrage frage, boolean mitAnimation) {
		if ( frage != null ) {
			this.antwort1KlickenBtn.setEnabled( true );
			this.antwort2KlickenBtn.setEnabled( true );
			this.antwort3KlickenBtn.setEnabled( true );
			this.antwort4KlickenBtn.setEnabled( true );

			if ( frage.getAntwort1() == null )
				this.antwort1KlickenBtn.setEnabled( false );

			if ( frage.getAntwort2() == null )
				this.antwort2KlickenBtn.setEnabled( false );

			if ( frage.getAntwort3() == null )
				this.antwort3KlickenBtn.setEnabled( false );

			if ( frage.getAntwort4() == null )
				this.antwort4KlickenBtn.setEnabled( false );

			this.antwort1KlickenBtn.setToolTipText( frage.getAntwort1() );
			this.antwort2KlickenBtn.setToolTipText( frage.getAntwort2() );
			this.antwort3KlickenBtn.setToolTipText( frage.getAntwort3() );
			this.antwort4KlickenBtn.setToolTipText( frage.getAntwort4() );
			this.bibelstelleLabel.setText( frage.getLoesungshinweis() );
		}
		else {
			this.antwort1KlickenBtn.setEnabled( false );
			this.antwort2KlickenBtn.setEnabled( false );
			this.antwort3KlickenBtn.setEnabled( false );
			this.antwort4KlickenBtn.setEnabled( false );

			this.antwort1KlickenBtn.setToolTipText( null );
			this.antwort2KlickenBtn.setToolTipText( null );
			this.antwort3KlickenBtn.setToolTipText( null );
			this.antwort4KlickenBtn.setToolTipText( null );
			this.bibelstelleLabel.setText( null );

			this.auswahlBestaetigenBtn.setEnabled( false );
		}

	}

	public void setFrageFeldSichtbar(boolean sichtbar) {
	// TODO Auto-generated method stub

	}

	public void setPublikumsJokerBenutzt(boolean benutzt) {
		this.puplikumsJokerBtn.setEnabled( !benutzt );
	}

	public void setPublikumsJokerSichtbar(boolean sichtbar) {
		this.puplikumsJokerBtn.setEnabled( sichtbar );

	}

	public void setStatusText(String text) {
	// TODO Auto-generated method stub

	}

	public void setTippJokerBenutzt(boolean benutzt) {
		this.tippJokerBtn.setEnabled( !benutzt );

	}

	public void setTippJokerSichtbar(boolean sichtbar) {
		this.tippJokerBtn.setEnabled( sichtbar );

	}

}
