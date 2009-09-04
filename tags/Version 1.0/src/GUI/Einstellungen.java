package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.Biblionaer;
import quiz.Steuerung;

public class Einstellungen extends JFrame implements ActionListener, KeyListener {

	private JTabbedPane			mainTabPane;

	// Proxy
	private JTextField			proxyHost;
	private JTextField			proxyPort;
	private JCheckBox			chkb;

	// Quiz
	private JTextField			xmlQuelle;
	private JFormattedTextField	tippJokerZeitInSekunden;
	private JFormattedTextField	puplikumsJokerZeitInSekunden;

	// Debugging
	private JCheckBox			quizIdAnzeigen;
	private JCheckBox			quizPingAnzeigen;
	private JFormattedTextField	quizKonsolenModus;
	private JComboBox			quizKonsolenAusgabeModus;

	private int					cheat				= 0;
	private boolean				zeigeQuizfrageID	= true;

	public String getProxyHost() {
		return proxyHost.getText();
	}

	public void setProxyHost(String pProxyHost) {
		this.proxyHost.setText( pProxyHost );
	}

	public String getProxyPort() {
		return proxyPort.getText();
	}

	public void setProxyPort(String pProxyPort) {
		this.proxyPort.setText( pProxyPort );
	}

	public String getXMLquelle() {
		return xmlQuelle.getText();
	}

	public void setXMLquelle(String pXMLquelle) {
		this.xmlQuelle.setText( pXMLquelle );
	}

	private static final long	serialVersionUID	= 5848161982883056362L;
	protected Steuerung			meineSteuerung;

	public Steuerung getMeineSteuerung() {
		return meineSteuerung;
	}

	public void setMeineSteuerung(Steuerung meineSteuerung) {
		this.meineSteuerung = meineSteuerung;
	}

	public void setTippJokerZeitInSekunden(int tippJokerZeitInSekunden) {
		this.tippJokerZeitInSekunden.setText( Integer.toString( tippJokerZeitInSekunden ) );
	}

	public int getTippJokerZeitInSekunden() {
		return Integer.valueOf( tippJokerZeitInSekunden.getText() );
	}

	public void setPuplikumsJokerZeitInSekunden(int tippJokerZeitInSekunden) {
		this.puplikumsJokerZeitInSekunden.setText( Integer.toString( tippJokerZeitInSekunden ) );
	}

	public int getPuplikumsJokerZeitInSekunden() {
		return Integer.valueOf( puplikumsJokerZeitInSekunden.getText() );
	}

	public void setQuizIdAnzeigen(boolean anzeigen) {
		quizIdAnzeigen.setSelected( anzeigen );
	}

	public boolean getQuizIdAnzeigen() {
		return quizIdAnzeigen.isSelected();
	}

	public void setPingAnzeigen(boolean anzeigen) {
		quizPingAnzeigen.setSelected( anzeigen );
	}

	public boolean getPingAnzeigen() {
		return quizPingAnzeigen.isSelected();
	}

	/**
	 * Der KonsolenModus ist eine Priorisierung der Ausgaben zwischen 1-4. Je
	 * nach wichtigkeit werden Meldungen ausgegeben. 1 ist am wichtigsten.
	 * 
	 * @param konsolenModus
	 *        Zahl von 1 bis 4
	 */
	public void setKonsolenModus(int konsolenModus) {
		quizKonsolenModus.setText( Integer.toString( konsolenModus ) );
	}

	/**
	 * Gibt den aktuell eingestellten Konsolenmodus zurück
	 * 
	 * @return KonsolenModus
	 */
	public int getKonsolenModus() {
		return Integer.valueOf( quizKonsolenModus.getText() );
	}

	/**
	 * Der KonsolenAusgabeModus gibt an, an welches Medium die Ausgaben der
	 * Konsole weitergeleitet werden. Man kann wählen zwischen "keiner Ausgabe",
	 * die Ausgabe über "das Systen" und einer Ausgabe in einer "Datei".
	 * Definiert werden die drei Zustände über eine Intger Zahl von 0-2. 0 =
	 * keine Ausgabe; 1 = Systemausgabe, 2 = Dateiausgabe
	 * 
	 * @param konsolenModus
	 */
	public void setKonsolenAusgabeModus(int konsolenModus) {

		int anzItems = quizKonsolenAusgabeModus.getItemCount();

		switch (konsolenModus) {
			case 0: // keine Ausgabe
				for (int i = 0; i < anzItems; i++) {
					if ( quizKonsolenAusgabeModus.getItemAt( i ).equals( "Keine" ) ) {
						quizKonsolenAusgabeModus.setSelectedIndex( i );
					}
				}
				break;
			case 1:// Ausgabe im System
				for (int i = 0; i < anzItems; i++) {
					if ( quizKonsolenAusgabeModus.getItemAt( i ).equals( "System" ) ) {
						quizKonsolenAusgabeModus.setSelectedIndex( i );
					}
				}
				break;
			case 2:// Ausgabe in Datei
				for (int i = 0; i < anzItems; i++) {
					if ( quizKonsolenAusgabeModus.getItemAt( i ).equals( "Datei" ) ) {
						quizKonsolenAusgabeModus.setSelectedIndex( i );
					}
				}
				break;
			default:// Ungültige Ausahl => Ausgabe im System
				for (int i = 0; i < anzItems; i++) {
					if ( quizKonsolenAusgabeModus.getItemAt( i ).equals( "Keine" ) ) {
						quizKonsolenAusgabeModus.setSelectedIndex( i );
					}
				}
				break;
		}
	}

	/**
	 * Gibt den aktuellen Konsolenmodus als Integer-Zahl zurück.
	 * 
	 * @return AusgabenKonsolenModus
	 */
	public int getKonsolenAusgabeModus() {
		String auswahl = (String) quizKonsolenAusgabeModus.getSelectedItem();
		int rueckgabe = 0;

		if ( auswahl.equals( "System" ) ) {
			rueckgabe = 1;
		}
		else if ( auswahl.equals( "Datei" ) ) {
			rueckgabe = 2;
		}
		else if ( auswahl.equals( "Keine" ) ) {
			rueckgabe = 0;
		}

		return rueckgabe;
	}

	public boolean isZeigeQuizfrageID() {
		return zeigeQuizfrageID;
	}

	public void setZeigeQuizfrageID(boolean zeigeQuizfrageID) {
		this.zeigeQuizfrageID = zeigeQuizfrageID;
	}

	public Einstellungen() {
		// TODO Auto-generated constructor stub
		this( "Einstellungen" );
	}

	public Einstellungen(String pFenstername) {
		this( pFenstername, null );
	}

	public Einstellungen(String pFenstername, Steuerung pSteuerung) {
		super( pFenstername );
		meineSteuerung = pSteuerung;

		this.init();
	}

	protected void init() {

		Container contentPane = this.getContentPane();
		JPanel mainPanel = new JPanel( new BorderLayout( 6, 3 ) );
		mainTabPane = new JTabbedPane( JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT );
		JLabel lbl;

		// Proxy Einstellungen:
		JPanel proxyPanel = new JPanel( new GridLayout( 0, 2, 6, 3 ) );

		lbl = new JLabel( "Proxy: " );
		proxyPanel.add( lbl );
		proxyHost = new JTextField( "proxy", 20 ); // Proxy-Host Defaultwert
		proxyHost.setAutoscrolls( true );
		proxyHost.setToolTipText( "Hostadresse des Proxy" );
		proxyPanel.add( proxyHost );
		// Für Cheatmodus
		proxyHost.addKeyListener( this );

		lbl = new JLabel( "Port: " );
		proxyPanel.add( lbl );
		proxyPort = new JTextField( "3128", 10 ); // Proxy-Port Defaultwert
		proxyPort.setAutoscrolls( false );
		proxyPort.setToolTipText( "Port des Proxy" );
		proxyPanel.add( proxyPort );

		lbl = new JLabel( "" );
		proxyPanel.add( lbl );
		chkb = new JCheckBox( "Proxy aktivieren", true ); // Proxy
		// aktivieren,
		// Defaultwert
		chkb.setToolTipText( "Die Proxyeinstellungen aktivieren" );
		chkb.addActionListener( this );
		proxyPanel.add( chkb );

		// Alles rund um das Quiz
		JPanel quizEinstellungen = new JPanel( new GridLayout( 0, 2, 6, 3 ) );
		lbl = new JLabel( "XML-Datenquelle:" );
		quizEinstellungen.add( lbl );
		xmlQuelle = new JTextField( "http://stivi.spacequadrat.de/getFrageAlsXML.php?quiz", 30 ); // XML-Quelle
		xmlQuelle.setToolTipText( "Quelle aus der das XML-File geladen werden soll." );
		quizEinstellungen.add( xmlQuelle );

		// Tipp-Joker
		lbl = new JLabel( "Zeit für den Tipp-Joker (in Sekunden)" );
		quizEinstellungen.add( lbl );
		tippJokerZeitInSekunden = new JFormattedTextField( NumberFormat.getInstance() );
		tippJokerZeitInSekunden.setText( "30" ); // Default-Sekunden-Wert
		quizEinstellungen.add( tippJokerZeitInSekunden );

		// Puplikums-Joker
		lbl = new JLabel( "Zeit für den Puplikums-Joker (in Sekunden)" );
		quizEinstellungen.add( lbl );
		puplikumsJokerZeitInSekunden = new JFormattedTextField( NumberFormat.getInstance() );
		puplikumsJokerZeitInSekunden.setText( "20" ); // Default-Sekunden-Wert
		quizEinstellungen.add( puplikumsJokerZeitInSekunden );

		// Debugging-Anzeige
		JPanel quizDebugging = new JPanel( new GridLayout( 0, 2, 6, 3 ) );
		// Id anzeigen
		lbl = new JLabel( "ID im Quiz anzeigen" );
		quizDebugging.add( lbl );
		quizIdAnzeigen = new JCheckBox( "anzeigen", true );
		quizIdAnzeigen
				.setToolTipText( "Zeigt die ID der Frage auf der rechten Seite an. (ist erst bei der nächsten Frage aktiv)" );
		quizDebugging.add( quizIdAnzeigen );

		// Ping anzeigen
		lbl = new JLabel( "Ping anzeigen" );
		quizDebugging.add( lbl );
		quizPingAnzeigen = new JCheckBox( "Ping anzeigen", true );
		quizPingAnzeigen
				.setToolTipText( "Zeigt die Verzögerung des Computers im rechten oberen Eck des Fensters an. Je kleiner die Zahl, desto besser." );
		quizDebugging.add( quizPingAnzeigen );

		// Konsolenmodus
		lbl = new JLabel( "Konsolenmodus (1-4)" );
		quizDebugging.add( lbl );
		quizKonsolenModus = new JFormattedTextField( NumberFormat.getInstance() );
		quizKonsolenModus.setText( "4" );
		quizKonsolenModus
				.setToolTipText( "1 = wichtigste Ausgaben, 2 = normale Ausgabe, 3 = Debugg Modus, 4 = geschwätziger Modus" );
		quizDebugging.add( quizKonsolenModus );

		// Konsolen-Ausgabemodus
		lbl = new JLabel( "Konsolenausgabemodus" );
		quizDebugging.add( lbl );
		quizKonsolenAusgabeModus = new JComboBox();
		quizKonsolenAusgabeModus.addItem( "Keine" );
		quizKonsolenAusgabeModus.addItem( "System" );
		quizKonsolenAusgabeModus.addItem( "Datei" );
		quizKonsolenAusgabeModus.setSelectedIndex( 1 ); // Standard Ausgabe per
		// System
		quizKonsolenAusgabeModus.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox selectedChoice = (JComboBox) e.getSource();
				if ( "System".equals( selectedChoice.getSelectedItem() ) ) {
					Biblionaer.meineEinstellungen.setKonsolenAusgabeModus( 1 );
				}
				else if ( "Keine".equals( selectedChoice.getSelectedItem() ) ) {
					Biblionaer.meineEinstellungen.setKonsolenAusgabeModus( 0 );
				}
				else if ( "Datei".equals( selectedChoice.getSelectedItem() ) ) {
					Biblionaer.meineEinstellungen.setKonsolenAusgabeModus( 2 );
				}
			}
		} );
		quizDebugging.add( quizKonsolenAusgabeModus );
		/*
		 * Hier fehlt jetzt noch die Rückkopplung ... wenn einer über die setter
		 * was ändert, muss auch die Combobox geändert werden ...
		 */

		// Button-Panel
		JPanel buttonPanel = new JPanel( new FlowLayout() );

		JButton ok = new JButton( "ok" );
		ok.setActionCommand( "Einstellungen speichern" );
		ok.addActionListener( this );
		buttonPanel.add( ok );

		// Einbinden der Panels
		// mainPanel.add( proxyPanel, BorderLayout.NORTH );
		mainTabPane.addTab( "Proxy", proxyPanel );
		// mainPanel.add( quizEinstellungen, BorderLayout.CENTER );
		mainTabPane.addTab( "Quiz", quizEinstellungen );
		mainTabPane.addTab( "Debugging", quizDebugging );
		mainPanel.add( mainTabPane );
		mainPanel.add( buttonPanel, BorderLayout.SOUTH );
		contentPane.add( mainPanel );

		this.pack(); // Fenster minimieren
		this.setResizable( false );
		this.setLocationRelativeTo( null ); // mittig ausrichten
		this.setVisible( false ); // nicht sichbar beim Start
	}

	public void proxyAktiviert(boolean pAktiviert) {
		if ( pAktiviert ) {
			proxyHost.setEnabled( true );
			proxyPort.setEnabled( true );
		}
		else {
			proxyHost.setEnabled( false );
			proxyPort.setEnabled( false );
		}

	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == this.chkb ) {
			if ( ((JCheckBox) e.getSource()).isSelected() ) {
				System.setProperty( "proxyHost", this.getProxyHost() );
				System.setProperty( "proxyPort", this.getProxyPort() );
				System.setProperty( "proxySet", "true" ); // Proxy aktivieren
				Biblionaer.meineKonsole.println( "Proxy wurde aktiviert", 3 );
			}
			else {
				System.setProperty( "proxySet", "false" ); // Proxy aktivieren
				Biblionaer.meineKonsole.println( "Proxy wurde deaktiviert", 3 );
			}
		}
		else if ( e.getActionCommand().equals( "Einstellungen speichern" ) ) {
			// Proxy-Änderungen eintragen und wenn kein Fehler, dann übernehmen
			// ...
			this.setVisible( false );
		}
	}

	public boolean darfGechetetWerden() {
		if ( this.cheat == 3 )
			return true;
		else
			return false;
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

		if ( e.getKeyCode() == KeyEvent.VK_S && this.cheat == 0 ) {
			this.cheat = 1;
			Biblionaer.meineKonsole.println( "Cheat status 1", 3 );
		}

		else if ( e.getKeyCode() == KeyEvent.VK_U && this.cheat == 1 ) {
			this.cheat = 2;
			Biblionaer.meineKonsole.println( "Cheat status 2", 3 );

		}

		else if ( e.getKeyCode() == KeyEvent.VK_M && this.cheat == 2 ) {
			this.cheat = 3;
			Biblionaer.meineKonsole.println( "Cheat status 3", 3 );
			Biblionaer.meineKonsole.println( "Cheat aktiviert", 3 );
		}

		else if ( this.cheat != 3 ) {
			this.cheat = 0;
		}
	}

	public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub

	}
}
