package main;

import quiz.Steuerung;
import window.Einstellungen;
import window.Konsole;
import window.SinglePlayerSchirm;
import window.WindowController;

/**
 * Diese Klasse ist eine statische Bibliothek zur Verwaltung der Referenzen auf
 * die wichtigsten Objekte
 * 
 * @author steven
 */
public class Biblionaer {

	public static Steuerung			meineSteuerung;
	public static Einstellungen		meineEinstellungen;
	public static WindowController	meinWindowController;
	public static Konsole			meineKonsole;

	public static void main(String[] args) {
		// Steuerung erstellen und initiieren

		meineSteuerung = new Steuerung();

		// Alle anderen Fenster und Objekte erstellen
		meineEinstellungen = new Einstellungen( "Einstellungen", meineSteuerung );
		meinWindowController = new WindowController();
		meinWindowController.addFrontendFenster( new SinglePlayerSchirm( "Hauptfenster", 678, 549,
				meineSteuerung ) );

		meineKonsole = new Konsole( meineSteuerung );

		// Steuerung mit den nötigen Objekten verknüpfen
		meineSteuerung.setEinstellungen( meineEinstellungen );
		meineSteuerung.setWindowController( meinWindowController );
		meineSteuerung.setKonsole( meineKonsole );

		// Weiter Einstellungen
		// meinHauptfenster.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		// Dialog öffnen und Standarspiel starten
		meineSteuerung.erstAufrufDerSteuerung();
	}
}
