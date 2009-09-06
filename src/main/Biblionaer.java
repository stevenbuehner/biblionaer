package main;

import quiz.Steuerung;
import GUI.Einstellungen;
import GUI.Hauptfenster;
import GUI.Konsole;
import GUI.SinglePlayerSchirm;

public class Biblionaer {

	public static Steuerung		meineSteuerung;
	public static Einstellungen	meineEinstellungen;
	public static Hauptfenster	meinHauptfenster;
	public static Konsole		meineKonsole;

	public static void main(String[] args) {
		// Steuerung erstellen und initiieren

		meineSteuerung = new Steuerung();

		// Alle anderen Fenster und Objekte erstellen
		meineEinstellungen = new Einstellungen( "Einstellungen", meineSteuerung );
		meinHauptfenster = new SinglePlayerSchirm( "Hauptfenster", 678, 549, meineSteuerung );
		meineKonsole = new Konsole( meineSteuerung );

		// Steuerung mit den nötigen Objekten verknüpfen
		meineSteuerung.setEinstellungen( meineEinstellungen );
		meineSteuerung.setHauptfenster( meinHauptfenster );
		meineSteuerung.setKonsole( meineKonsole );

		// Weiter Einstellungen
		// meinHauptfenster.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		// Dialog öffnen und Standarspiel starten
		meineSteuerung.erstAufrufDerSteuerung();
	}
}
