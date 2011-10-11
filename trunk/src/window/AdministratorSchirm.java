package window;

import importer.XmlToSpiel;
import interfaces.BackendWindow;
import interfaces.FrontendWindow;
import interfaces.QuizFenster;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lokaleSpiele.QuizFileModel;
import main.Biblionaer;
import quiz.Quizfrage;
import timer.SekuendlicherZeitgeber;
import timer.UhrzeitSekUpdate;

public class AdministratorSchirm extends JFrame implements QuizFenster, BackendWindow, ActionListener {

	private static final long			serialVersionUID			= 1L;

	// verwendete Icons zum Zwischenspeichern
	protected ImageIcon					checkIcon					= new ImageIcon( "src/img/check.png" );
	protected ImageIcon					xIcon						= new ImageIcon( "src/img/x.png" );
	protected ImageIcon					quitIcon					= new ImageIcon( "src/img/quit.png" );
	protected ImageIcon					blackIcon					= new ImageIcon( "src/img/black.png" );

	// Wird ueber Spielgestartet() und speilBeendet() gesetzt
	private boolean						cache_spielLaeuft			= false;

	private GraphicsDevice				device						= null;
	private boolean						isFullScreen				= false;

	// links oben
	protected JPanel					monitorPanel				= null;
	// rechts oben
	protected JPanel					steuerungPanel				= null;
	// links unten
	protected JPanel					dateiPanel					= null;
	// rechts unten
	protected JPanel					weiteresPanel				= null;

	protected JPanel					antwort1Panel, antwort2Panel, antwort3Panel, antwort4Panel = null;

	// SteuerungPanel Material
	protected JButton					auswahlBestaetigenBtn		= new JButton( "einloggen", checkIcon );
	protected JButton					antwort1KlickenBtn			= new JButton( "Antwort A" );
	protected JButton					antwort2KlickenBtn			= new JButton( "Antwort B" );
	protected JButton					antwort3KlickenBtn			= new JButton( "Antwort C" );
	protected JButton					antwort4KlickenBtn			= new JButton( "Antwort D" );
	protected JLabel					bibelstelleLabel			= new JLabel( "Bibelstelle" );

	protected JButton					fiftyJokerBtn				= new JButton( "50:50 Joker" );
	protected JButton					tippJokerBtn				= new JButton( "Tipp Joker" );
	protected JButton					publikumsJokerBtn			= new JButton( "Puplikums Joker" );

	protected JButton					laufendsSpielBeendenBtn		= new JButton( "laufendes Spiel beenden", quitIcon );

	// DateiPanel Material
	protected JTable					spielListeTable				= new JTable( new QuizFileModel() );
	protected JButton					angeklicktesSpielStartenBtn	= new JButton( "dieses Spiel starten" );
	protected JButton					angeklickesSpielLoeschenBtn	= new JButton( "dieses Spiel löschen" );
	protected JButton					neuesSpielAusInternBtn		= new JButton(
																			"neues Spiel aus dem Internet importieren" );

	// WeiteresPanel Material
	protected JButton					schwarzerBildschirmBtn		= new JButton( "schwarzer Bildschirm", blackIcon );

	// Zeitliche Komponenten
	protected JLabel					uhrzeit						= new JLabel( "Uhrzeit:" );
	protected JLabel					aktuelleFragenZeit			= new JLabel( "Aktuelle Frage:" );
	protected JLabel					gesamtSpielZeit				= new JLabel( "Gesamtspielzeit: " );
	private SimpleDateFormat			dateFormat					= new SimpleDateFormat( "HH:mm:ss" );
	protected SekuendlicherZeitgeber	sekZeit						= new SekuendlicherZeitgeber( this );

	public AdministratorSchirm(String fenstername, GraphicsDevice device, boolean vollbildModus) {
		super( fenstername );
		this.device = device;
		this.isFullScreen = vollbildModus;

		// getInstalledLookAndFeels();
		try {
			// UIManager.setLookAndFeel(
			// UIManager.getCrossPlatformLookAndFeelClassName() );
		}
		catch (Exception e) {
			Biblionaer.meineKonsole.println( "Look and Feel nicht gefunden! - " + e.getMessage(), 3 );
		}

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

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

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
		this.steuerungPanel.setBackground( null );

		// TopLeisten-Panel
		JPanel steuerungTopLeistePanel = new JPanel( new FlowLayout() );
		// Button
		auswahlBestaetigenBtn.addActionListener( this );
		fiftyJokerBtn.addActionListener( this );
		tippJokerBtn.addActionListener( this );
		publikumsJokerBtn.addActionListener( this );
		laufendsSpielBeendenBtn.addActionListener( this );

		// auswahlBestaetigenBtn.setEnabled( false );
		fiftyJokerBtn.setEnabled( false );
		tippJokerBtn.setEnabled( false );
		publikumsJokerBtn.setEnabled( false );
		laufendsSpielBeendenBtn.setEnabled( false );

		// steuerungTopLeistePanel.add( auswahlBestaetigenBtn );
		steuerungTopLeistePanel.add( fiftyJokerBtn );
		steuerungTopLeistePanel.add( tippJokerBtn );
		steuerungTopLeistePanel.add( publikumsJokerBtn );
		steuerungTopLeistePanel.add( laufendsSpielBeendenBtn );

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

		this.antwort1Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort1Panel.add( this.antwort1KlickenBtn );
		steuerungQuizPanel.add( this.antwort1Panel );

		this.antwort2Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort2Panel.add( this.antwort2KlickenBtn );
		steuerungQuizPanel.add( this.antwort2Panel );

		this.antwort3Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort3Panel.add( this.antwort3KlickenBtn );
		steuerungQuizPanel.add( this.antwort3Panel );

		this.antwort4Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort4Panel.add( this.antwort4KlickenBtn );
		steuerungQuizPanel.add( this.antwort4Panel );

		steuerungQuizPanel.add( new JLabel( "Bibelstelle: " ) );
		steuerungQuizPanel.add( this.bibelstelleLabel );

		steuerungPanel.add( steuerungTopLeistePanel );
		steuerungPanel.add( steuerungQuizPanel );

		// ******* DateiPanel *******
		this.dateiPanel = new JPanel( new GridLayout( 2, 1, 20, 20 ) );
		// this.dateiPanel.setBackground( Color.BLACK );

		JPanel dateiLinkesPanel = new JPanel( new FlowLayout() );
		// dateiLinkesPanel.add( new JLabel( "offline Spiel Liste" ) );
		spielListeTable.setToolTipText( "Spielanzahl: " + spielListeTable.getModel().getRowCount() );
		spielListeTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		spielListeTable.setToolTipText( "Diese fertigen Spiele sind auf Deinem Rechner installiert" );
		spielListeTable.setAlignmentX( Component.LEFT_ALIGNMENT );
		// spielListeTable.setColumnSelectionAllowed( false );
		spielListeTable.getTableHeader().setReorderingAllowed( false );
		spielListeTable.getTableHeader().setResizingAllowed( false );
		spielListeTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 300 );
		spielListeTable.getColumnModel().getColumn( 1 ).setPreferredWidth( 200 );
		// keine Mehrfachauswahl
		spielListeTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

		if ( spielListeTable.getRowCount() > 0 ) {
			// markiere zu Beginn die erste Zeile
			spielListeTable.getSelectionModel().setSelectionInterval( 0, 0 );
		}

		ListSelectionModel rowSM = spielListeTable.getSelectionModel();
		rowSM.addListSelectionListener( new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if ( e.getValueIsAdjusting() )
					return; // if you don't want to handle intermediate
							// selections

				ListSelectionModel rowSM = (ListSelectionModel) e.getSource();
				int selectedIndex = rowSM.getMinSelectionIndex();

				if ( selectedIndex >= 0 ) {
					AdministratorSchirm.this.angeklickesSpielLoeschenBtn.setEnabled( true );
					AdministratorSchirm.this.angeklicktesSpielStartenBtn.setEnabled( true );
				}
				else {
					AdministratorSchirm.this.angeklickesSpielLoeschenBtn.setEnabled( false );
					AdministratorSchirm.this.angeklicktesSpielStartenBtn.setEnabled( false );
				}
			}
		} );

		// Zeilen und Spaltenabstand:
		spielListeTable.setIntercellSpacing( new Dimension( 2, 2 ) );
		// spielListeTable.getTableHeader().setVisible( true );
		dateiLinkesPanel.add( spielListeTable );

		JPanel dateiLinksUntenPanel = new JPanel( new GridLayout( 0, 1 ) );
		// Buttons
		angeklicktesSpielStartenBtn.addActionListener( this );
		angeklickesSpielLoeschenBtn.addActionListener( this );
		neuesSpielAusInternBtn.addActionListener( this );

		angeklicktesSpielStartenBtn.setToolTipText( "Das in der Liste ausgewählte Spiel starten/spielen" );
		angeklickesSpielLoeschenBtn.setToolTipText( "Das in der Liste ausgewählte Spiel undwiderruflich löschen" );
		neuesSpielAusInternBtn.setToolTipText( "Ein neues Spiel mit 15 Fragen aus dem Internet herunterladen" );

		dateiLinksUntenPanel.add( angeklicktesSpielStartenBtn );
		dateiLinksUntenPanel.add( neuesSpielAusInternBtn );
		dateiLinksUntenPanel.add( angeklickesSpielLoeschenBtn );

		// Zusammenfügen
		dateiPanel.add( dateiLinkesPanel );
		dateiPanel.add( dateiLinksUntenPanel );

		// ******* Weiteres Panel *******
		this.weiteresPanel = new JPanel( new GridLayout( 6, 1, 20, 20 ) );
		this.weiteresPanel.setBackground( null );

		// Buttons
		schwarzerBildschirmBtn.addActionListener( this );

		weiteresPanel.add( this.schwarzerBildschirmBtn );
		weiteresPanel.add( this.uhrzeit );
		weiteresPanel.add( this.aktuelleFragenZeit );
		weiteresPanel.add( this.gesamtSpielZeit );

		// Alles zusammenfägen
		mainPanel.add( monitorPanel );
		mainPanel.add( steuerungPanel );
		mainPanel.add( dateiPanel );
		mainPanel.add( weiteresPanel );

		cnt.add( mainPanel );

		this.sekZeit.starteZeitgeber();
		this.sekuendlicherZeitgeber();
	}

	/**
	 * Die Funktion soll ein GridBagConstraints-Objekt erstellen, die Werte
	 * zuweisen und dem Container dieses Constraint-Objekt zuteilen. Mit einer
	 * Komponente ist also eine Einschränkung verbunden. Zusätzlich soll die
	 * Methode die Komponenten in den Container legen. Quelle:
	 * com/javatutor/insel/ui/layout/GridBagLayoutDemo.java
	 * 
	 * @param cont
	 * @param gbl
	 * @param c
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param weightx
	 * @param weighty
	 */
	static void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height,
			double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints( c, gbc );
		cont.add( c );
	}

	// * Ab hier die Methode für das Interface BackendWindow
	public void setFrontendScreenImage(VolatileImage screen) {
		Graphics g = monitorPanel.getGraphics();
		g.drawImage( screen, 0, 0, monitorPanel.getWidth(), monitorPanel.getHeight(), this );
	}

	protected void windowClosed() {
		// TODO Diese Funktion muss noch eingebunden werden!!!

		this.sekZeit.stoppeZeitgeber();
	}

	// * Ab hier die Methode für das Interface ActionListener
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == this.sekZeit.timer ) {
			this.sekuendlicherZeitgeber();
		}
		else if ( e.getSource() == antwort1KlickenBtn ) {
			adminRequest_klickAntwort1();
		}
		else if ( e.getSource() == antwort2KlickenBtn ) {
			adminRequest_klickAntwort2();
		}
		else if ( e.getSource() == antwort3KlickenBtn ) {
			adminRequest_klickAntwort3();
		}
		else if ( e.getSource() == antwort4KlickenBtn ) {
			adminRequest_klickAntwort4();
		}
		else if ( e.getSource() == auswahlBestaetigenBtn ) {
			adminRequest_bestaetigeAusgewalteAntwort();
		}
		else if ( e.getSource() == fiftyJokerBtn ) {
			this.adminRequest_setFiftyJokerAktiviert( !this.fiftyJokerBtn.getToolTipText().equals( "Joker verwenden" ) );
		}
		else if ( e.getSource() == tippJokerBtn ) {
			this.adminRequest_setTippJokerAktiviert( !this.tippJokerBtn.getToolTipText().equals( "Joker verwenden" ) );
		}
		else if ( e.getSource() == publikumsJokerBtn ) {
			this.adminRequest_setPublikumsJokerAktiviert( !this.publikumsJokerBtn.getToolTipText().equals(
					"Joker verwenden" ) );
		}
		else if ( e.getSource() == angeklicktesSpielStartenBtn ) {
			if ( this.spielListeTable.getSelectedRow() >= 0 ) {
				File quizSpeicherort = ((QuizFileModel) this.spielListeTable.getModel())
						.getQuizFileLocationAt( this.spielListeTable.getSelectedRow() );
				Biblionaer.meineSteuerung.starteNeuesSpiel( quizSpeicherort );
			}
		}
		else if ( e.getSource() == angeklickesSpielLoeschenBtn ) {
			if ( this.spielListeTable.getSelectedRow() >= 0 ) {

				if ( !((QuizFileModel) spielListeTable.getModel()).removeQuizFile( this.spielListeTable
						.getSelectedRow() ) ) {
					Biblionaer.meineKonsole.println(
							"Es trat ein Fehler im TabelModel auf, beim läschen der SpielDatei", 2 );
				}
				else {
					Biblionaer.meineKonsole.println( "SpielDatei erfolgreich gelöscht", 4 );
				}
			}
		}
		else if ( e.getSource() == laufendsSpielBeendenBtn ) {
			// Startdialog
			int returnOptionDialog = JOptionPane.showOptionDialog(
					(Component) Biblionaer.meinWindowController.getBackendFenster(),
					"Bist Du dir sicher, dass Du dieses Spiel beenden möchtest?", "Warnung", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.NO_OPTION );

			if ( returnOptionDialog == JOptionPane.OK_OPTION ) {
				Biblionaer.meineSteuerung.spielBeenden();
				this.spielButtonsAktivieren( false );
			}

		}
		else if ( e.getSource() == neuesSpielAusInternBtn ) {
			// Biblionaer.meineSteuerung.actionPerformed( new ActionEvent( this,
			// 1,
			// "Neues Spiel aus dem Internet" ) );
			this.neuesSpielImportieren();
		}
		else if ( e.getSource() == schwarzerBildschirmBtn ) {
			schwarzerBildschirmBtnKlick();
		}
		else {
			Biblionaer.meineKonsole.println( "AdminSchirm mit unbekanntem Action Event: " + e.getActionCommand(), 2 );
		}
	}

	// * Ab hier die Button-Klick-Methoden

	protected void schwarzerBildschirmBtnKlick() {
		if ( schwarzerBildschirmBtn.getText().equals( "schwarzer Bildschirm" ) ) {
			if ( Biblionaer.meinWindowController.getFrontendFenster() != null ) {
				// Wenn es ein Frontend gibt, dann schwärzen
				((FrontendWindow) Biblionaer.meinWindowController.getFrontendFenster()).setBildschirmSchwarz( true );
				schwarzerBildschirmBtn.setText( "Bilschirm wieder einblenden" );
			}
		}
		else {
			if ( Biblionaer.meinWindowController.getFrontendFenster() != null ) {
				// Wenn es ein Frontend gibt, dann wieder Bild herstellen
				((FrontendWindow) Biblionaer.meinWindowController.getFrontendFenster()).setBildschirmSchwarz( false );
			}
			// Egal ob es ein Frontend gibt oder nicht, Button immer wieder
			// herstellen.
			schwarzerBildschirmBtn.setText( "schwarzer Bildschirm" );
		}
	}

	protected void neuesSpielImportieren() {
		try {
			XmlToSpiel dasXMLImporterFile = new XmlToSpiel( new URL( Biblionaer.meineEinstellungen.getXMLquelle() ) );

			// Finde den nächsten freien Speichernamen
			int i = 0;
			File saveTo = null;
			while (saveTo == null && i < 50) {
				i++;
				saveTo = new File( QuizFileModel.getSpeicherortSpiele().getAbsolutePath() + "/neuesSpiel_"
						+ Integer.toString( i ) + ".bqxml" );

				if ( saveTo.exists() ) {
					saveTo = null;
				}
			}

			if ( i >= 50 ) {
				Biblionaer.meineKonsole.println( "Es wurde nach " + Integer.toString( i )
						+ " versuchen abgebrochen, das Spiel zu speichern.", 2 );
			}
			else {
				if ( dasXMLImporterFile.getAnzahlFragen() > 0 ) {
					dasXMLImporterFile.saveSpielToFile( saveTo );
					Biblionaer.meineKonsole.println( "Es wurde noch ein neues Spiel angelegt.", 3 );
				}
				else {
					Biblionaer.meineKonsole.println(
							"Es wurde kein neues Spiel importiert, weil nur "
									+ Integer.toString( dasXMLImporterFile.getAnzahlFragen() )
									+ " Fragen zur heruntergeladen wurden.", 2 );
				}
			}
		}
		catch (MalformedURLException e) {
			Biblionaer.meineKonsole.println(
					"Beim Versuch ein neues Spiel herunterzuladen (im AdministratorSchirm), ist die falsche URL verwendet worden.\n"
							+ e.getMessage(), 1 );
		}
		catch (IOException e2) {
			Biblionaer.meineKonsole.println(
					"Es trat ein Fehler beim speichern eines heruntergeladenen Spieles (im AdministratorSchirm) auf:\n"
							+ e2.getMessage(), 1 );

		}
		finally {
			((QuizFileModel) this.spielListeTable.getModel()).refreshInhalte();
		}

	}

	protected void spielButtonsAktivieren(boolean spielLauft) {
		this.laufendsSpielBeendenBtn.setEnabled( spielLauft );
		this.angeklicktesSpielStartenBtn.setEnabled( !spielLauft );

		this.fiftyJokerBtn.setEnabled( spielLauft );
		this.tippJokerBtn.setEnabled( spielLauft );
		this.publikumsJokerBtn.setEnabled( spielLauft );

		this.antwort1KlickenBtn.setEnabled( spielLauft );
		this.antwort2KlickenBtn.setEnabled( spielLauft );
		this.antwort3KlickenBtn.setEnabled( spielLauft );
		this.antwort4KlickenBtn.setEnabled( spielLauft );
	}

	// * Ab hier die Methoden zur Ansteuerung äber die Steuerung
	public void playFrageFalsch() {
		this.spielButtonsAktivieren( false );
	}

	public void playFrageRichtig() {
		// TODO Auto-generated method stub

	}

	public void playStarteSpiel() {
		spielButtonsAktivieren( true );
	}

	public void playSpielGewonnen() {
		this.spielButtonsAktivieren( false );
	}

	public void resetAlleJoker() {
		this.setFiftyJokerBenutzt( false );
		this.setTippJokerBenutzt( false );
		this.setPublikumsJokerBenutzt( false );
	}

	/**
	 * Eingabe des Admins ueberschreibt den bisherigen Spielstatus IMMER!
	 * 
	 * @param value
	 */
	private void adminRequest_setFiftyJokerAktiviert(boolean value) {
		if ( value ) {
			Biblionaer.meineSteuerung.resetFiftyJoker();
		}
		else {
			Biblionaer.meineSteuerung.klickAufFiftyJoker();
		}
	}

	/**
	 * Eingabe des Admins ueberschreibt den bisherigen Spielstatus IMMER!
	 * 
	 * @param value
	 */
	private void adminRequest_setTippJokerAktiviert(boolean value) {
		if ( value ) {
			Biblionaer.meineSteuerung.resetTippJoker();
		}
		else {
			Biblionaer.meineSteuerung.klickAufTippJoker();
		}
	}

	/**
	 * Eingabe des Admins ueberschreibt den bisherigen Spielstatus IMMER!
	 * 
	 * @param value
	 */
	private void adminRequest_setPublikumsJokerAktiviert(boolean value) {
		if ( value ) {
			Biblionaer.meineSteuerung.resetPublikumsJoker();
		}
		else {
			Biblionaer.meineSteuerung.klickAufPuplikumsJoker();
		}
	}

	private void adminRequest_klickAntwort1() {
		if ( this.antwort1KlickenBtn.getBackground().equals( Color.YELLOW ) ) {
			Biblionaer.meinWindowController.setAntwortFeld1Normal();
			this.auswahlBestaetigenBtn.setEnabled( false );
			this.antwort1Panel.remove( this.auswahlBestaetigenBtn );
			this.antwort2KlickenBtn.setEnabled( true );
			this.antwort3KlickenBtn.setEnabled( true );
			this.antwort4KlickenBtn.setEnabled( true );
		}
		else {
			Biblionaer.meinWindowController.setAntwortFeld1Markiert();
			this.antwort1Panel.add( this.auswahlBestaetigenBtn );
			this.auswahlBestaetigenBtn.setEnabled( true );
			this.antwort2KlickenBtn.setEnabled( false );
			this.antwort3KlickenBtn.setEnabled( false );
			this.antwort4KlickenBtn.setEnabled( false );
		}
	}

	private void adminRequest_klickAntwort2() {
		if ( this.antwort2KlickenBtn.getBackground().equals( Color.YELLOW ) ) {
			Biblionaer.meinWindowController.setAntwortFeld2Normal();
			this.auswahlBestaetigenBtn.setEnabled( false );
			this.antwort2Panel.remove( this.auswahlBestaetigenBtn );
			this.antwort1KlickenBtn.setEnabled( true );
			this.antwort3KlickenBtn.setEnabled( true );
			this.antwort4KlickenBtn.setEnabled( true );
		}
		else {
			Biblionaer.meinWindowController.setAntwortFeld2Markiert();
			this.antwort2Panel.add( this.auswahlBestaetigenBtn );
			this.auswahlBestaetigenBtn.setEnabled( true );
			this.antwort1KlickenBtn.setEnabled( false );
			this.antwort3KlickenBtn.setEnabled( false );
			this.antwort4KlickenBtn.setEnabled( false );
		}
	}

	private void adminRequest_klickAntwort3() {
		if ( this.antwort3KlickenBtn.getBackground().equals( Color.YELLOW ) ) {
			Biblionaer.meinWindowController.setAntwortFeld3Normal();
			this.auswahlBestaetigenBtn.setEnabled( false );
			this.antwort3Panel.remove( this.auswahlBestaetigenBtn );
			this.antwort1KlickenBtn.setEnabled( true );
			this.antwort2KlickenBtn.setEnabled( true );
			this.antwort4KlickenBtn.setEnabled( true );
		}
		else {
			Biblionaer.meinWindowController.setAntwortFeld3Markiert();
			this.antwort3Panel.add( this.auswahlBestaetigenBtn );
			this.auswahlBestaetigenBtn.setEnabled( true );
			this.antwort1KlickenBtn.setEnabled( false );
			this.antwort2KlickenBtn.setEnabled( false );
			this.antwort4KlickenBtn.setEnabled( false );
		}
	}

	private void adminRequest_klickAntwort4() {
		if ( this.antwort4KlickenBtn.getBackground().equals( Color.YELLOW ) ) {
			Biblionaer.meinWindowController.setAntwortFeld4Normal();
			this.auswahlBestaetigenBtn.setEnabled( false );
			this.antwort4Panel.remove( this.auswahlBestaetigenBtn );
			this.antwort1KlickenBtn.setEnabled( true );
			this.antwort2KlickenBtn.setEnabled( true );
			this.antwort3KlickenBtn.setEnabled( true );
		}
		else {
			Biblionaer.meinWindowController.setAntwortFeld4Markiert();
			this.antwort4Panel.add( this.auswahlBestaetigenBtn );
			this.auswahlBestaetigenBtn.setEnabled( true );
			this.antwort1KlickenBtn.setEnabled( false );
			this.antwort2KlickenBtn.setEnabled( false );
			this.antwort3KlickenBtn.setEnabled( false );
		}
	}

	private void adminRequest_bestaetigeAusgewalteAntwort() {
		int auswahl = 0;

		if ( this.antwort1KlickenBtn.getBackground().equals( Color.YELLOW ) )
			auswahl = 1;
		else if ( this.antwort2KlickenBtn.getBackground().equals( Color.YELLOW ) )
			auswahl = 2;
		else if ( this.antwort3KlickenBtn.getBackground().equals( Color.YELLOW ) )
			auswahl = 3;
		else if ( this.antwort4KlickenBtn.getBackground().equals( Color.YELLOW ) )
			auswahl = 4;

		if ( auswahl != 0 ) {
			Biblionaer.meineSteuerung.klickAufAntwortFeld( auswahl );
		}
		else {
			Biblionaer.meineKonsole.println(
					"FEHLER: Nichts wurde ausgewaehlt, aber trotzdem der Bestaetigungs-Button gedrueckt", 2 );
		}
	}

	private void sekuendlicherZeitgeber() {
		this.uhrzeit.setText( "Uhrzeit: " + this.dateFormat.format( new Date() ) + " Uhr" );

		if ( this.cache_spielLaeuft ) {
			this.aktuelleFragenZeit.setText( "Aktuelle Frage: "
					+ String.valueOf( Biblionaer.meineSteuerung.frageDauerBisJetztInSekunden() ) + " Sek" );
			this.gesamtSpielZeit.setText( "Gesamtspieldauer: "
					+ Biblionaer.meineSteuerung.spielDauerBisJetztInSekunden() + " Sek" );
		}
	}

	public void setAnimationAktiviert(boolean aktiviert) {
		// TODO Auto-generated method stub

	}

	public void setAntwortFeld1Falsch() {
		// Nichts zu tun
	}

	public void setAntwortFeld1Markiert() {
		this.antwort1KlickenBtn.setBackground( Color.YELLOW );
		// this.antwort1KlickenBtn.setOpaque( true );
		// this.antwort1KlickenBtn.setBorderPainted( false );
	}

	public void setAntwortFeld1Normal() {
		this.antwort1KlickenBtn.setBackground( null );
	}

	public void setAntwortFeld1Richtig() {
		// Nichts zu tun
	}

	public void setAntwortFeld1Sichtbar(boolean sichtbar) {
		antwort1KlickenBtn.setEnabled( sichtbar );
	}

	public void setAntwortFeld2Falsch() {
		// Nichts zu tun
	}

	public void setAntwortFeld2Markiert() {
		this.antwort2KlickenBtn.setBackground( Color.YELLOW );
	}

	public void setAntwortFeld2Normal() {
		this.antwort2KlickenBtn.setBackground( null );
	}

	public void setAntwortFeld2Richtig() {
		// Nichts zu tun
	}

	public void setAntwortFeld2Sichtbar(boolean sichtbar) {
		antwort2KlickenBtn.setEnabled( sichtbar );
	}

	public void setAntwortFeld3Falsch() {
		// Nichts zu tun
	}

	public void setAntwortFeld3Markiert() {
		this.antwort3KlickenBtn.setBackground( Color.YELLOW );
	}

	public void setAntwortFeld3Normal() {
		this.antwort3KlickenBtn.setBackground( null );
	}

	public void setAntwortFeld3Richtig() {
		// Nichts zu tun
	}

	public void setAntwortFeld3Sichtbar(boolean sichtbar) {
		antwort3KlickenBtn.setEnabled( sichtbar );
	}

	public void setAntwortFeld4Falsch() {
		// Nichts zu tun
	}

	public void setAntwortFeld4Markiert() {
		this.antwort4KlickenBtn.setBackground( Color.YELLOW );
	}

	public void setAntwortFeld4Normal() {
		this.antwort4KlickenBtn.setBackground( null );
	}

	public void setAntwortFeld4Richtig() {
		// Nichts zu tun
	}

	public void setAntwortFeld4Sichtbar(boolean sichtbar) {
		antwort4KlickenBtn.setEnabled( sichtbar );
	}

	public void setAntwortFelderSichtbar(boolean sichtbar) {
		this.setAntwortFeld1Sichtbar( sichtbar );
		this.setAntwortFeld2Sichtbar( sichtbar );
		this.setAntwortFeld3Sichtbar( sichtbar );
		this.setAntwortFeld4Sichtbar( sichtbar );

		// Nur wenn unsichtbar, dann auch nichts anklicken lassen
		if ( sichtbar == false )
			this.auswahlBestaetigenBtn.setEnabled( false );
	}

	public void setAntwortfelderFalsch() {
		this.setAntwortFeld1Falsch();
		this.setAntwortFeld2Falsch();
		this.setAntwortFeld3Falsch();
		this.setAntwortFeld4Falsch();

		this.auswahlBestaetigenBtn.setEnabled( false );
	}

	public void setAntwortfelderMariert() {
		this.setAntwortFeld1Markiert();
		this.setAntwortFeld2Markiert();
		this.setAntwortFeld3Markiert();
		this.setAntwortFeld4Markiert();

		this.auswahlBestaetigenBtn.setEnabled( false );
	}

	public void setAntwortfelderNormal() {
		this.setAntwortFeld1Normal();
		this.setAntwortFeld2Normal();
		this.setAntwortFeld3Normal();
		this.setAntwortFeld4Normal();

		this.auswahlBestaetigenBtn.setEnabled( false );
	}

	public void setAntwortfelderRichtig() {
		this.setAntwortFeld1Richtig();
		this.setAntwortFeld2Richtig();
		this.setAntwortFeld3Richtig();
		this.setAntwortFeld4Richtig();

		this.auswahlBestaetigenBtn.setEnabled( false );
	}

	public void setCountdownText(String text) {
		this.setStatusText( text );
	}

	public void setFiftyJokerBenutzt(boolean benutzt) {
		if ( benutzt ) {
			this.fiftyJokerBtn.setIcon( this.xIcon );
			this.fiftyJokerBtn.setToolTipText( "Joker wieder freischalten" );
		}
		else {
			this.fiftyJokerBtn.setIcon( this.checkIcon );
			this.fiftyJokerBtn.setToolTipText( "Joker verwenden" );
		}
	}

	public void setFiftyJokerSichtbar(boolean sichtbar) {
		this.fiftyJokerBtn.setEnabled( sichtbar );
	}

	public void setFrageAnzuzeigen(Quizfrage frage, boolean mitAnimation) {
		// Wird hier nicht implementiert, wir nutzen stattdessen die Funktion
		// setFrageKomplett(Quizfrage frage)
	}

	public void setFrageKomplett(Quizfrage frage) {
		// Wird hier implementiert, weil es ein Backendfenster ist

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
		if ( benutzt ) {
			// this.publikumsJokerBtn.setBackground( Color.RED );
			this.publikumsJokerBtn.setIcon( this.xIcon );
			this.publikumsJokerBtn.setToolTipText( "Joker wieder freischalten" );
		}
		else {
			// this.publikumsJokerBtn.setBackground( Color.GREEN );
			this.publikumsJokerBtn.setIcon( this.checkIcon );
			this.publikumsJokerBtn.setToolTipText( "Joker verwenden" );
		}
	}

	public void setPublikumsJokerSichtbar(boolean sichtbar) {
		this.publikumsJokerBtn.setEnabled( sichtbar );

	}

	public void setStatusText(String text) {
		// TODO Auto-generated method stub

	}

	public void setTippJokerBenutzt(boolean benutzt) {
		if ( benutzt ) {
			this.tippJokerBtn.setIcon( this.xIcon );
			this.tippJokerBtn.setToolTipText( "Joker wieder freischalten" );
		}
		else {
			this.tippJokerBtn.setIcon( this.checkIcon );
			this.tippJokerBtn.setToolTipText( "Joker verwenden" );
		}
	}

	public void setTippJokerSichtbar(boolean sichtbar) {
		this.tippJokerBtn.setEnabled( sichtbar );
	}

	public void killYourSelf() {
		this.setVisible( false );

		try {
			// Damit auch ja Operationen an den Administrator-Schirm geschickt
			// wurden, bevor er geläscht wird
			Thread.sleep( 100 );
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispose();
	}

	public void spielBeendet() {
		this.antwort1KlickenBtn.setEnabled( false );
		this.antwort2KlickenBtn.setEnabled( false );
		this.antwort3KlickenBtn.setEnabled( false );
		this.antwort4KlickenBtn.setEnabled( false );

		this.fiftyJokerBtn.setEnabled( false );
		this.tippJokerBtn.setEnabled( false );
		this.publikumsJokerBtn.setEnabled( false );

		ListSelectionModel rowSM = spielListeTable.getSelectionModel();

		if ( rowSM.getMinSelectionIndex() >= 0 ) {
			this.spielListeTable.setEnabled( true );
			this.angeklickesSpielLoeschenBtn.setEnabled( true );
		}
		else {
			// this.spielListeTable.setEnabled( false );
			// this.angeklickesSpielLoeschenBtn.setEnabled( false );
		}
		this.neuesSpielAusInternBtn.setEnabled( true );

		this.cache_spielLaeuft = false;
	}

	public void spielGestartet() {
		this.laufendsSpielBeendenBtn.setEnabled( true );

		this.angeklickesSpielLoeschenBtn.setEnabled( false );
		this.angeklicktesSpielStartenBtn.setEnabled( false );

		this.neuesSpielAusInternBtn.setEnabled( false );
		this.spielListeTable.setEnabled( false );
		this.cache_spielLaeuft = true;

		// Die letzte eingeloggte Frage entfernen
		this.antwort1Panel.remove( this.auswahlBestaetigenBtn );
		this.antwort2Panel.remove( this.auswahlBestaetigenBtn );
		this.antwort3Panel.remove( this.auswahlBestaetigenBtn );
		this.antwort4Panel.remove( this.auswahlBestaetigenBtn );
	}

}
