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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import lokaleSpiele.QuizFileModel;
import main.Biblionaer;
import quiz.Quizfrage;

public class AdministratorSchirm extends JFrame implements QuizFenster, BackendWindow,
		ActionListener {

	private static final long	serialVersionUID			= 1L;

	// links oben
	protected JPanel			monitorPanel;
	// rechts oben
	protected JPanel			steuerungPanel;
	// links unten
	protected JPanel			dateiPanel;
	// rechts unten
	protected JPanel			weiteresPanel;

	private GraphicsDevice		device;
	private boolean				isFullScreen				= false;

	// SteuerungPanel Material
	JButton						auswahlBestaetigenBtn		= new JButton( "Auswahl Bestätigen" );
	JButton						antwort1KlickenBtn			= new JButton( "Antwort A" );
	JButton						antwort2KlickenBtn			= new JButton( "Antwort B" );
	JButton						antwort3KlickenBtn			= new JButton( "Antwort C" );
	JButton						antwort4KlickenBtn			= new JButton( "Antwort D" );
	JLabel						bibelstelleLabel			= new JLabel( "Bibelstelle" );

	JButton						fiftyJokerBtn				= new JButton( "50:50 Joker" );
	JButton						tippJokerBtn				= new JButton( "Tipp Joker" );
	JButton						puplikumsJokerBtn			= new JButton( "Puplikums Joker" );

	// DateiPanel Material
	JTable						spielListeTable				= new JTable( new QuizFileModel() );
	JButton						angeklicktesSpielStartenBtn	= new JButton( "Neues Spiel starten" );
	JButton						angeklickesSpielLoeschenBtn	= new JButton( "Dieses Spiel löschen" );
	JButton						laufendsSpielBeendenBtn		= new JButton(
																	"laufendes Spiel beenden" );
	JButton						neuesSpielAusInternBtn		= new JButton(
																	"Neues Spiel aus dem Internet importieren" );

	// WeiteresPanel Material
	JButton						leisteEinblendenBtn			= new JButton( "Leiste einblenden" );
	JButton						schwarzerBildschirmBtn		= new JButton( "schwarzer Bildschirm" );

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
		fiftyJokerBtn.addActionListener( this );
		tippJokerBtn.addActionListener( this );
		puplikumsJokerBtn.addActionListener( this );

		auswahlBestaetigenBtn.setEnabled( false );
		fiftyJokerBtn.setEnabled( false );
		tippJokerBtn.setEnabled( false );
		puplikumsJokerBtn.setEnabled( false );

		steuerungTopLeistePanel.add( auswahlBestaetigenBtn );
		steuerungTopLeistePanel.add( fiftyJokerBtn );
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
		// dateiLinkesPanel.add( new JLabel( "offline Spiel Liste" ) );
		spielListeTable.setToolTipText( "Spielanzahl: " + spielListeTable.getModel().getRowCount() );
		spielListeTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		spielListeTable
				.setToolTipText( "Diese fertigen Spiele sind auf diesem Rechner installiert" );
		spielListeTable.setAlignmentX( Component.LEFT_ALIGNMENT );
		// spielListeTable.setColumnSelectionAllowed( false );
		spielListeTable.getTableHeader().setReorderingAllowed( false );
		spielListeTable.getTableHeader().setResizingAllowed( false );
		spielListeTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 150 );
		// Zeilen und Spaltenabstand:
		spielListeTable.setIntercellSpacing( new Dimension( 2, 2 ) );
		// spielListeTable.getTableHeader().setVisible( true );
		dateiLinkesPanel.add( spielListeTable );

		JPanel dateiRechtesPanel = new JPanel( new GridLayout( 3, 1 ) );
		// Buttons
		angeklicktesSpielStartenBtn.addActionListener( this );
		laufendsSpielBeendenBtn.addActionListener( this );
		neuesSpielAusInternBtn.addActionListener( this );
		angeklickesSpielLoeschenBtn.addActionListener( this );

		laufendsSpielBeendenBtn.setEnabled( false );

		dateiRechtesPanel.add( angeklicktesSpielStartenBtn );
		dateiRechtesPanel.add( laufendsSpielBeendenBtn );
		dateiRechtesPanel.add( neuesSpielAusInternBtn );
		dateiRechtesPanel.add( angeklickesSpielLoeschenBtn );

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
		else if ( e.getSource() == fiftyJokerBtn ) {
			Biblionaer.meineSteuerung.klickAufFiftyJoker();
			fiftyJokerBtn.setEnabled( false );
		}
		else if ( e.getSource() == tippJokerBtn ) {
			Biblionaer.meineSteuerung.klickAufTippJoker();
			tippJokerBtn.setEnabled( false );
		}
		else if ( e.getSource() == puplikumsJokerBtn ) {
			Biblionaer.meineSteuerung.puplikumsJokerZeitAbgelaufen();
			puplikumsJokerBtn.setEnabled( false );
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

				if ( !((QuizFileModel) spielListeTable.getModel())
						.removeQuizFile( this.spielListeTable.getSelectedRow() ) ) {
					Biblionaer.meineKonsole.println(
							"Es trat ein Fehler im TabelModel auf, beim löschen der SpielDatei", 2 );
				}
				else {
					Biblionaer.meineKonsole.println( "SpielDatei erfolgreich gelöscht", 4 );
				}
			}
		}
		else if ( e.getSource() == laufendsSpielBeendenBtn ) {
			Biblionaer.meineSteuerung.spielBeenden();
			this.spielButtonsAktivieren( false );
		}
		else if ( e.getSource() == neuesSpielAusInternBtn ) {
			// Biblionaer.meineSteuerung.actionPerformed( new ActionEvent( this,
			// 1,
			// "Neues Spiel aus dem Internet" ) );
			this.neuesSpielImportieren();
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

	protected void neuesSpielImportieren() {
		try {
			XmlToSpiel dasXMLImporterFile = new XmlToSpiel( new URL( Biblionaer.meineEinstellungen
					.getXMLquelle() ) );

			// Finde den nächsten freien Speichernamen
			int i = 0;
			File saveTo = null;
			while (saveTo == null && i < 50) {
				i++;
				saveTo = new File( QuizFileModel.getSpeicherortSpiele().getAbsolutePath()
						+ "/neuesSpiel_" + Integer.toString( i ) + ".bqxml" );

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
			Biblionaer.meineKonsole
					.println(
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
		this.puplikumsJokerBtn.setEnabled( spielLauft );

		this.antwort1KlickenBtn.setEnabled( spielLauft );
		this.antwort2KlickenBtn.setEnabled( spielLauft );
		this.antwort3KlickenBtn.setEnabled( spielLauft );
		this.antwort4KlickenBtn.setEnabled( spielLauft );
	}

	// * Ab hier die Methoden zur Ansteuerung über die Steuerung
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
		fiftyJokerBtn.setEnabled( true );
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
		this.fiftyJokerBtn.setEnabled( !benutzt );
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

	public void killYourSelf() {
		this.setVisible( false );

		try {
			// Damit auch ja Operationen an den Administrator-Schirm geschickt
			// wurden, bevor er gelöscht wird
			Thread.sleep( 100 );
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispose();
	}

}
