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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import lokaleSpiele.QuizFileModel;
import main.Biblionaer;
import quiz.Quizfrage;
import timer.SekuendlicherZeitgeber;

public class AdministratorSchirm extends JFrame implements QuizFenster, BackendWindow, ActionListener, Observer {

	private static final long			serialVersionUID					= 1L;

	// verwendete Icons zum Zwischenspeichern
	protected ImageIcon					checkIcon							= new ImageIcon( "src/img/check.png" );
	protected ImageIcon					xIcon								= new ImageIcon( "src/img/x.png" );
	protected ImageIcon					gameQuitIcon						= new ImageIcon( "src/img/gameQuit.png" );
	protected ImageIcon					blackIcon							= new ImageIcon( "src/img/black.png" );
	protected ImageIcon					trashIcon							= new ImageIcon( "src/img/trash.png" );
	protected ImageIcon					playIcon							= new ImageIcon( "src/img/play.png" );
	protected ImageIcon					pauseIcon							= new ImageIcon( "src/img/pause.png" );
	protected ImageIcon					downloadIcon						= new ImageIcon( "src/img/download.png" );

	protected ImageIcon					starIcon							= new ImageIcon( "src/img/star.png" );
	protected ImageIcon					appQuitIcon							= new ImageIcon( "src/img/appQuit.png" );
	protected ImageIcon					settingsIcon						= new ImageIcon( "src/img/settings.png" );

	private static Color				buttonColorRichtig					= Color.GREEN.darker();

	// Wird ueber Spielgestartet() und speilBeendet() gesetzt
	private boolean						cache_spielLaeuft					= false;

	private GraphicsDevice				device								= null;
	private boolean						isFullScreen						= false;

	// links oben
	protected JPanel					monitorPanel						= null;
	// links unten
	protected JPanel					dateiPanel							= null;

	protected JPanel					antwort1Panel, antwort2Panel, antwort3Panel, antwort4Panel = null;

	// SteuerungPanel Material
	protected JButton					auswahlBestaetigenBtn				= new JButton( "einloggen", checkIcon );
	protected JButton					antwort1KlickenBtn					= new JButton( "Antwort A" );
	protected JButton					antwort2KlickenBtn					= new JButton( "Antwort B" );
	protected JButton					antwort3KlickenBtn					= new JButton( "Antwort C" );
	protected JButton					antwort4KlickenBtn					= new JButton( "Antwort D" );
	protected JLabel					fragestellung						= new JLabel(
																					"<html><center>Fragestellung</center></html>" );
	protected JLabel					bibelstelleLabel					= new JLabel( "Bibelstelle" );

	protected JButton					fiftyJokerBtn						= new JButton( "50:50 Joker" );
	protected JButton					tippJokerBtn						= new JButton( "Tipp Joker" );
	protected JButton					publikumsJokerBtn					= new JButton( "Puplikums Joker" );

	protected JButton					laufendsSpielBeendenBtn				= new JButton(
																					"<html>Spiel beenden</html>",
																					gameQuitIcon );

	// DateiPanel Material
	protected JTable					spielListeTable						= new JTable( new QuizFileModel() );
	protected JButton					angeklicktesSpielStartenBtn			= new JButton( playIcon );
	protected JButton					angeklickesSpielLoeschenBtn			= new JButton( trashIcon );
	protected JButton					neuesSpielAusInternHerunterladenBtn	= new JButton( downloadIcon );
	protected JButton					neuesStandardSpielStartenBtn		= new JButton( starIcon );
	protected JButton					spielPausierenBtn					= new JButton(
																					"<html>Spiel pausieren</html>",
																					pauseIcon );

	// WeiteresPanel Material
	protected int						countKonsoleLineNumbers				= 0;
	protected int						maxKonsoleLineNumbers				= 100;
	protected JTextArea					konsolenOutputTxtArea				= new JTextArea();
	protected JButton					schwarzerBildschirmBtn				= new JButton(
																					"<html>schwarzer Bildschirm</html>",
																					blackIcon );
	protected JButton					einstellungenBtn					= new JButton( "Einstellungen",
																					settingsIcon );
	protected JButton					programmBeenden						= new JButton(
																					"<html>Programm beenden</html>",
																					appQuitIcon );

	// Zeitliche Komponenten
	protected JLabel					uhrzeit								= new JLabel( "Uhrzeit:" );
	protected JLabel					aktuelleFragenZeit					= new JLabel( "Aktuelle Frage:" );
	protected JLabel					gesamtSpielZeit						= new JLabel( "Gesamtspielzeit: " );
	private SimpleDateFormat			dateFormat							= new SimpleDateFormat( "HH:mm:ss" );
	protected SekuendlicherZeitgeber	sekZeit								= new SekuendlicherZeitgeber( this );

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

		// Backend Fenster schließen => Programm beenden
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				Biblionaer.meineSteuerung.programmBeendenRequest();
			}
		} );

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

		this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

	}

	private void doInitialisations() {
		Container cnt = this.getContentPane();

		// ******* GesamtPanel *******
		JPanel mainPanel = new JPanel( new GridLayout( 2, 2, 5, 5 ) );
		mainPanel.setBackground( Color.LIGHT_GRAY );

		// ******* MonitorPanel (Links Oben) *******
		this.monitorPanel = new JPanel( new FlowLayout() );
		this.monitorPanel.setBackground( Color.BLACK );

		// ******* Quiz-SteuerungPanel (Rechts Oben) *******
		GridBagLayout gbl_quizSteuerung = new GridBagLayout();
		JPanel quizSteuerungPanel = new JPanel( gbl_quizSteuerung );

		// Button
		auswahlBestaetigenBtn.addActionListener( this );
		fiftyJokerBtn.addActionListener( this );
		tippJokerBtn.addActionListener( this );
		publikumsJokerBtn.addActionListener( this );

		fiftyJokerBtn.setEnabled( false );
		tippJokerBtn.setEnabled( false );
		publikumsJokerBtn.setEnabled( false );

		auswahlBestaetigenBtn.setToolTipText( "Diese Antwort einloggen" );

		JPanel jokerPanel = new JPanel( new FlowLayout() );
		jokerPanel.add( fiftyJokerBtn );
		jokerPanel.add( tippJokerBtn );
		jokerPanel.add( publikumsJokerBtn );

		addComponent( quizSteuerungPanel, gbl_quizSteuerung, jokerPanel, 0, 0, 2, 1, 0, 0, null );

		// größere Schrift für Fragestellung zum besseren Ablesen
		Font fsFont = this.fragestellung.getFont();
		this.fragestellung.setFont( new Font( fsFont.getFontName(), fsFont.getStyle(), 20 ) );
		this.fragestellung.setHorizontalAlignment( SwingConstants.CENTER );
		addComponent( quizSteuerungPanel, gbl_quizSteuerung, this.fragestellung, 0, 1, 2, 1, 1.0, 1.0, new Insets( 20,
				5, 20, 5 ) );

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
		addComponent( quizSteuerungPanel, gbl_quizSteuerung, antwort1Panel, 0, 2, 1, 1, 0.1, 0.2, new Insets( 2, 5, 2,
				2 ) );

		this.antwort2Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort2Panel.add( this.antwort2KlickenBtn );
		addComponent( quizSteuerungPanel, gbl_quizSteuerung, antwort2Panel, 1, 2, 1, 1, 0.1, 0.2, new Insets( 2, 2, 2,
				5 ) );

		this.antwort3Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort3Panel.add( this.antwort3KlickenBtn );
		addComponent( quizSteuerungPanel, gbl_quizSteuerung, antwort3Panel, 0, 3, 1, 1, 0.1, 0.2, new Insets( 2, 5, 2,
				2 ) );

		this.antwort4Panel = new JPanel( new GridLayout( 1, 0 ) );
		this.antwort4Panel.add( this.antwort4KlickenBtn );
		addComponent( quizSteuerungPanel, gbl_quizSteuerung, antwort4Panel, 1, 3, 1, 1, 0.1, 0.2, new Insets( 2, 2, 2,
				5 ) );

		addComponent( quizSteuerungPanel, gbl_quizSteuerung, new JLabel( "Bibelstelle (Tipp-Joker): " ), 0, 4, 1, 1, 0,
				0, new Insets( 5, 5, 5, 5 ) );
		addComponent( quizSteuerungPanel, gbl_quizSteuerung, bibelstelleLabel, 1, 4, 1, 1, 0, 0.0, new Insets( 5, 0, 5,
				5 ) );

		// ******* DateiPanel (Links Unten) *******
		GridBagLayout gbl_dateiPanel = new GridBagLayout();
		this.dateiPanel = new JPanel( gbl_dateiPanel );

		addComponent( this.dateiPanel, gbl_dateiPanel, new JLabel( "Gespeicherte Spiele:" ), 0, 0, 3, 1, 1.0, 0,
				new Insets( 5, 5, 5, 5 ) );

		spielListeTable.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
		spielListeTable.setToolTipText( "Diese fertigen Spiele sind auf Deinem Rechner installiert" );
		spielListeTable.setAlignmentX( Component.LEFT_ALIGNMENT );
		spielListeTable.getTableHeader().setReorderingAllowed( false );
		spielListeTable.getTableHeader().setResizingAllowed( false );
		spielListeTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 260 );
		// spielListeTable.getColumnModel().getColumn( 1 ).setPreferredWidth(
		// 140 );

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
					AdministratorSchirm.this.angeklicktesSpielStartenBtn.setEnabled( true );
				}
				else {
					AdministratorSchirm.this.angeklicktesSpielStartenBtn.setEnabled( false );
				}

				if ( ((QuizFileModel) AdministratorSchirm.this.spielListeTable.getModel())
						.isRowDeletable( selectedIndex ) ) {
					AdministratorSchirm.this.angeklickesSpielLoeschenBtn.setEnabled( true );
				}
				else {
					AdministratorSchirm.this.angeklickesSpielLoeschenBtn.setEnabled( false );
				}
			}
		} );

		this.spielListeTable.getModel().addTableModelListener( new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				spielListeTable.setRowSelectionAllowed( true );
				if ( spielListeTable.getRowCount() == 1 ) {
					// markiere zu Beginn die erste Zeile
					spielListeTable.getSelectionModel().setSelectionInterval( 0, 0 );
				}
			}
		} );

		// Zeilen und Spaltenabstand:
		spielListeTable.setIntercellSpacing( new Dimension( 2, 2 ) );
		spielListeTable.getTableHeader().setVisible( true );

		JScrollPane dateiScrollPane = new JScrollPane( this.spielListeTable );
		dateiScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		dateiScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

		addComponent( this.dateiPanel, gbl_dateiPanel, dateiScrollPane, 0, 1, 2, 6, 1.0, 1.0, new Insets( 0, 5, 5, 0 ) );

		// Buttons
		angeklicktesSpielStartenBtn.addActionListener( this );
		angeklicktesSpielStartenBtn.setToolTipText( "Das in der Liste ausgewählte Spiel starten/spielen" );

		angeklickesSpielLoeschenBtn.addActionListener( this );
		angeklickesSpielLoeschenBtn.setToolTipText( "Das in der Liste ausgewählte Spiel unwiderruflich löschen" );

		neuesSpielAusInternHerunterladenBtn.addActionListener( this );
		neuesSpielAusInternHerunterladenBtn
				.setToolTipText( "Ein neues Spiel mit 15 Fragen aus dem Internet herunterladen" );

		neuesStandardSpielStartenBtn.addActionListener( this );
		neuesStandardSpielStartenBtn.setToolTipText( "Das mitgelieferte Spiel spielen." );

		// Löschtbutton initial enablen, falls möglich
		enableLoeschButtonIfPossible();

		addComponent( this.dateiPanel, gbl_dateiPanel, this.angeklicktesSpielStartenBtn, 2, 1, 1, 1, 0, 0, new Insets(
				0, 2, 2, 2 ) );
		addComponent( this.dateiPanel, gbl_dateiPanel, this.angeklickesSpielLoeschenBtn, 2, 2, 1, 1, 0, 0, new Insets(
				0, 2, 2, 2 ) );
		addComponent( this.dateiPanel, gbl_dateiPanel, this.neuesSpielAusInternHerunterladenBtn, 2, 3, 1, 1, 0, 0,
				new Insets( 0, 2, 2, 2 ) );
		addComponent( this.dateiPanel, gbl_dateiPanel, this.neuesStandardSpielStartenBtn, 2, 4, 1, 1, 0, 0, new Insets(
				0, 2, 2, 2 ) );

		// ******* Konsole & Screencontrole Panel (Rechts Unten) *******
		GridBagLayout gbl = new GridBagLayout();
		JPanel screenControlePanel = new JPanel( gbl );

		// Konsole mit Textarea
		this.konsolenOutputTxtArea.setText( "" );
		this.konsolenOutputTxtArea.setEditable( false );
		this.konsolenOutputTxtArea.setLineWrap( true );

		JScrollPane konsolenPane = new JScrollPane( this.konsolenOutputTxtArea );
		Biblionaer.meineKonsole.addObserver( this );

		// Buttons
		this.schwarzerBildschirmBtn.addActionListener( this );
		this.schwarzerBildschirmBtn
				.setToolTipText( "Zwischen normalem und schwarzem Anzeigebildschirm hin- und herschalten" );
		this.schwarzerBildschirmBtn.setHorizontalAlignment( SwingConstants.LEFT );

		this.laufendsSpielBeendenBtn.addActionListener( this );
		this.laufendsSpielBeendenBtn.setToolTipText( "Das aktuell laufende Spiel beenden" );
		this.laufendsSpielBeendenBtn.setHorizontalAlignment( SwingConstants.LEFT );
		this.laufendsSpielBeendenBtn.setEnabled( false );

		this.spielPausierenBtn.addActionListener( this );
		this.spielPausierenBtn.setToolTipText( "Werbepause einblenden" );
		this.spielPausierenBtn.setHorizontalAlignment( SwingConstants.LEFT );
		this.spielPausierenBtn.setEnabled( false );

		this.einstellungenBtn.addActionListener( this );
		this.einstellungenBtn.setToolTipText( "Einstellungs-Fenster öffnen" );
		this.einstellungenBtn.setHorizontalAlignment( SwingConstants.LEFT );

		this.programmBeenden.addActionListener( this );
		this.programmBeenden.setToolTipText( "Das komplette Programm beenden" );
		this.programmBeenden.setHorizontalAlignment( SwingConstants.LEFT );

		addComponent( screenControlePanel, gbl, new JLabel( "Konsole: " ), 0, 0, 2, 1, 0, 0, new Insets( 5, 5, 5, 5 ) );
		addComponent( screenControlePanel, gbl, konsolenPane, 0, 1, 2, 9, 1.0, 1.0, new Insets( 0, 5, 5, 0 ) );
		addComponent( screenControlePanel, gbl, this.schwarzerBildschirmBtn, 2, 1, 2, 1, 0, 0.2,
				new Insets( 0, 5, 1, 5 ) );
		addComponent( screenControlePanel, gbl, this.spielPausierenBtn, 2, 2, 2, 1, 0, 0.2, new Insets( 1, 5, 1, 5 ) );
		addComponent( screenControlePanel, gbl, this.laufendsSpielBeendenBtn, 2, 3, 2, 1, 0, 0.2, new Insets( 1, 5, 1,
				5 ) );
		addComponent( screenControlePanel, gbl, this.einstellungenBtn, 2, 4, 2, 1, 0, 0.2, new Insets( 1, 5, 1, 5 ) );
		addComponent( screenControlePanel, gbl, this.programmBeenden, 2, 5, 2, 1, 0, 0.2, new Insets( 1, 5, 1, 5 ) );

		addComponent( screenControlePanel, gbl, new JLabel( "Statistik:" ), 2, 6, 2, 1, 0, 0, new Insets( 30, 5, 1, 5 ) );
		addComponent( screenControlePanel, gbl, this.uhrzeit, 2, 7, 2, 1, 0, 0, new Insets( 1, 5, 1, 5 ) );
		addComponent( screenControlePanel, gbl, this.aktuelleFragenZeit, 2, 8, 2, 1, 0, 0, new Insets( 1, 5, 1, 5 ) );
		addComponent( screenControlePanel, gbl, this.gesamtSpielZeit, 2, 9, 2, 1, 0, 0, new Insets( 1, 5, 5, 5 ) );

		// Alles zusammenfügen
		mainPanel.add( this.monitorPanel );
		mainPanel.add( quizSteuerungPanel );
		mainPanel.add( this.dateiPanel );
		mainPanel.add( screenControlePanel );

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
			double weightx, double weighty, Insets randAbstaende) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;

		if ( randAbstaende != null ) {
			gbc.insets = randAbstaende;
		}
		gbl.setConstraints( c, gbc );
		cont.add( c );
	}

	// * Ab hier die Methode für das Interface BackendWindow
	long	lastTime	= 0;

	public void setFrontendScreenImage(VolatileImage screen) {
		long thisTime = System.currentTimeMillis();

		if ( thisTime - lastTime < 50 ) {
			return;
		}
		else {
			this.lastTime = thisTime;

			Graphics g = monitorPanel.getGraphics();

			/*
			 * Image scaled = screen.getScaledInstance(monitorPanel.getWidth(),
			 * monitorPanel.getHeight(), BufferedImage.SCALE_SMOOTH);
			 * g.drawImage(scaled, 0, 0, monitorPanel.getWidth(),
			 * monitorPanel.getHeight(), this);
			 */
			g.drawImage( screen, 0, 0, monitorPanel.getWidth(), monitorPanel.getHeight(), this );
		}
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
			int deleteSelection = this.spielListeTable.getSelectedRow();
			if ( deleteSelection >= 0 ) {

				if ( !((QuizFileModel) spielListeTable.getModel()).removeQuizFile( deleteSelection ) ) {
					Biblionaer.meineKonsole.println(
							"Es trat ein Fehler im TabelModel auf, beim löschen der SpielDatei", 2 );
				}
				else {
					Biblionaer.meineKonsole.println( "SpielDatei erfolgreich gelöscht", 4 );

					// Nach dem löschen, das nachfolgende Spiel auswählen
					if ( this.spielListeTable.getRowCount() > deleteSelection ) {
						spielListeTable.getSelectionModel().setSelectionInterval( deleteSelection, deleteSelection );
					}
					else if ( this.spielListeTable.getRowCount() > 0 ) {
						spielListeTable.getSelectionModel().setSelectionInterval( deleteSelection - 1,
								deleteSelection - 1 );
					}
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
		else if ( e.getSource() == this.neuesSpielAusInternHerunterladenBtn ) {
			// Biblionaer.meineSteuerung.actionPerformed( new ActionEvent( this,
			// 1,
			// "Neues Spiel aus dem Internet" ) );
			this.neuesSpielImportieren();
		}
		else if ( e.getSource() == this.neuesStandardSpielStartenBtn ) {
			Biblionaer.meineSteuerung.starteNeuesSpiel( getClass().getClassLoader().getResource(
					"lokaleSpiele/ein Testspiel.bqxml" ) );
		}
		else if ( e.getSource() == this.schwarzerBildschirmBtn ) {
			schwarzerBildschirmBtnKlick();
		}
		else if ( e.getSource() == this.einstellungenBtn ) {
			Biblionaer.meineEinstellungen.setVisible( true );
		}
		else if ( e.getSource() == this.programmBeenden ) {
			Biblionaer.meineSteuerung.programmBeendenRequest();
		}
		else {
			Biblionaer.meineKonsole.println( "AdminSchirm mit unbekanntem Action Event: " + e.getActionCommand(), 2 );
		}
	}

	// * Ab hier die Button-Klick-Methoden

	protected void schwarzerBildschirmBtnKlick() {
		if ( schwarzerBildschirmBtn.getText().equals( "<html>schwarzer Bildschirm</html>" ) ) {
			if ( Biblionaer.meinWindowController.getFrontendFenster() != null ) {
				// Wenn es ein Frontend gibt, dann schwärzen
				((FrontendWindow) Biblionaer.meinWindowController.getFrontendFenster()).setBildschirmSchwarz( true );
				schwarzerBildschirmBtn.setText( "<html>Bilschirm wieder einblenden</html>" );
			}
		}
		else {
			if ( Biblionaer.meinWindowController.getFrontendFenster() != null ) {
				// Wenn es ein Frontend gibt, dann wieder Bild herstellen
				((FrontendWindow) Biblionaer.meinWindowController.getFrontendFenster()).setBildschirmSchwarz( false );
			}
			// Egal ob es ein Frontend gibt oder nicht, Button immer wieder
			// herstellen.
			schwarzerBildschirmBtn.setText( "<html>schwarzer Bildschirm</html>" );
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
				saveTo = new File( QuizFileModel.getSpeicherortHomeSpiele().getAbsolutePath() + "/neuesSpiel_"
						+ Integer.toString( i ) + ".bqxml" );

				if ( saveTo.exists() ) {
					saveTo = null;
				}
			}

			if ( i >= 50 ) {
				Biblionaer.meineKonsole.println( "Es wurde nach " + Integer.toString( i )
						+ " versuchen abgebrochen, das Spiel zu speichern.", 2 );

				JOptionPane
						.showOptionDialog(
								this,
								"Du hast bereits zu viele Spiele gespeichert. Bitte lösche erst ein paar, bevor Du weitere herunterlädst",
								"Fehler", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null,
								JOptionPane.OK_OPTION );

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
									+ " Fragen neu heruntergeladen wurden.", 2 );
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
		this.spielPausierenBtn.setEnabled( false ); // Noch nicht implementiert

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

			switch (auswahl) {
				case 1:
					this.antwort1Panel.remove( auswahlBestaetigenBtn );
					break;
				case 2:
					this.antwort2Panel.remove( auswahlBestaetigenBtn );
					break;
				case 3:
					this.antwort3Panel.remove( auswahlBestaetigenBtn );
					break;
				case 4:
					this.antwort4Panel.remove( auswahlBestaetigenBtn );
					break;
			}
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

	private void enableLoeschButtonIfPossible() {
		// Löschtbutton initial enablen, falls möglich
		QuizFileModel qfm = (QuizFileModel) this.spielListeTable.getModel();
		if ( qfm.isRowDeletable( spielListeTable.getSelectionModel().getMinSelectionIndex() ) ) {
			this.angeklickesSpielLoeschenBtn.setEnabled( true );
		}
		else {
			this.angeklickesSpielLoeschenBtn.setEnabled( false );
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

			// html-Tags sind nötig für den automatischen Zeilenumbruch
			this.antwort1KlickenBtn.setText( "<html>A: " + frage.getAntwort1() + "</html>" );
			this.antwort2KlickenBtn.setText( "<html>B: " + frage.getAntwort2() + "</html>" );
			this.antwort3KlickenBtn.setText( "<html>C: " + frage.getAntwort3() + "</html>" );
			this.antwort4KlickenBtn.setText( "<html>D: " + frage.getAntwort4() + "</html>" );
			this.bibelstelleLabel.setText( "<html>" + frage.getLoesungshinweis() + "</html>" );

			// Mehrzeilige Fragestellungen werden umgebrochen und zentriert
			this.fragestellung.setText( "<html><center>" + frage.getFragestellung() + "</center></html>" );

			// Wenn eine Fragestellung nur einzeilig ist, soll sie auch
			// zentriert werden!
			this.fragestellung.setHorizontalAlignment( SwingConstants.CENTER );

			// Den Text für alle Buttons schwarz setzen, um anschließend die
			// richtige Antwort grün zu markieren
			this.antwort1KlickenBtn.setForeground( Color.BLACK );
			this.antwort2KlickenBtn.setForeground( Color.BLACK );
			this.antwort3KlickenBtn.setForeground( Color.BLACK );
			this.antwort4KlickenBtn.setForeground( Color.BLACK );

			// richtiges Grün markieren
			switch (frage.getRichtigeAntwort()) {
				case 1:
					this.antwort1KlickenBtn.setForeground( buttonColorRichtig );
					break;
				case 2:
					this.antwort2KlickenBtn.setForeground( buttonColorRichtig );
					break;
				case 3:
					this.antwort3KlickenBtn.setForeground( buttonColorRichtig );
					break;
				case 4:
					this.antwort4KlickenBtn.setForeground( buttonColorRichtig );
					break;
				default:
			}
		}
		else {
			this.antwort1KlickenBtn.setEnabled( false );
			this.antwort2KlickenBtn.setEnabled( false );
			this.antwort3KlickenBtn.setEnabled( false );
			this.antwort4KlickenBtn.setEnabled( false );

			this.fragestellung.setText( null );
			this.bibelstelleLabel.setText( null );

			this.auswahlBestaetigenBtn.setEnabled( false );
		}
	}

	public void setFrageFeldSichtbar(boolean sichtbar) {}

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

	public void setStatusText(String text) {}

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
			e.printStackTrace();
		}
		this.dispose();
	}

	public void spielBeendet() {
		this.antwort1KlickenBtn.setText( "" );
		this.antwort2KlickenBtn.setText( "" );
		this.antwort3KlickenBtn.setText( "" );
		this.antwort4KlickenBtn.setText( "" );
		this.fragestellung.setText( "" );
		this.bibelstelleLabel.setText( "" );

		ListSelectionModel rowSM = spielListeTable.getSelectionModel();

		if ( rowSM.getMinSelectionIndex() >= 0 ) {
			this.angeklicktesSpielStartenBtn.setEnabled( true );
		}
		else {
			this.angeklicktesSpielStartenBtn.setEnabled( false );
		}

		enableLoeschButtonIfPossible();

		this.spielListeTable.setEnabled( true );
		this.neuesSpielAusInternHerunterladenBtn.setEnabled( true );
		this.neuesStandardSpielStartenBtn.setEnabled( true );

		this.spielButtonsAktivieren( false );
		this.cache_spielLaeuft = false;
	}

	public void spielGestartet() {
		this.angeklickesSpielLoeschenBtn.setEnabled( false );
		this.angeklicktesSpielStartenBtn.setEnabled( false );
		this.neuesStandardSpielStartenBtn.setEnabled( false );

		this.neuesSpielAusInternHerunterladenBtn.setEnabled( false );
		this.spielListeTable.setEnabled( false );
		this.cache_spielLaeuft = true;

		// Die letzte eingeloggte Frage entfernen
		this.antwort1Panel.remove( this.auswahlBestaetigenBtn );
		this.antwort2Panel.remove( this.auswahlBestaetigenBtn );
		this.antwort3Panel.remove( this.auswahlBestaetigenBtn );
		this.antwort4Panel.remove( this.auswahlBestaetigenBtn );

		this.spielButtonsAktivieren( true );
	}

	public void update(Observable beobachtbarer, Object text) {
		if ( beobachtbarer == Biblionaer.meineKonsole ) {
			this.konsolenOutputTxtArea.append( text.toString() + "\n" );
			this.countKonsoleLineNumbers++;

			// Lösche Zeilen, wenn es mehr alsmaxKonsoleLineNumbers sind
			int toManyLines = this.countKonsoleLineNumbers - this.maxKonsoleLineNumbers;
			if ( toManyLines > 0 ) {
				int max = 0;

				for (int i = toManyLines; i >= 0; i--) {
					max = this.konsolenOutputTxtArea.getText().indexOf( "\n" );
				}

				this.konsolenOutputTxtArea.replaceRange( "", 0, max + 1 );
				this.countKonsoleLineNumbers = this.maxKonsoleLineNumbers;
			}
		}
	}
}
