package window;

import main.Biblionaer;

public class Konsole {

	/*
	 * 0 = nichts ausgeben, außer Sachen mit Prio 0; 1 = Nur Wichtige
	 * Fehlermeldungen; 2 = Alle Fehlermeldungen; 3 = Sei Gesprächig; 4 =
	 * Degbugging ausgaben;
	 */

	// 1 = System.out.println
	// 2 = in File
	public int getAusgabeZustand() {
		return Biblionaer.meineEinstellungen.getKonsolenModus();
	}

	public void setAusgabeZustand(int ausgabeZustand) {
		Biblionaer.meineEinstellungen.setKonsolenModus( ausgabeZustand );
	}

	public Konsole() {
		// Wenn beim Start des Programms auf der Parameter -D übergeben wurde,
		// dann debuggen wir
		String prop = System.getProperty( "DEBUG" );
		if ( Boolean.getBoolean( prop ) ) {
			Biblionaer.meineEinstellungen.setKonsolenModus( 4 );
		}
	}

	public void println(String pAusgabe) {
		switch (Biblionaer.meineEinstellungen.getKonsolenAusgabeModus()) {
			case 0:
				// Keine Ausgabe
				break;
			case 1:
				// Ausgabe über die System-Konsole
				this.SystemPrintLine( "["
						+ Integer.toString( Biblionaer.meineEinstellungen.getKonsolenModus() )
						+ "] " + pAusgabe );
				break;
			case 2:
				// Ausgabe in ein File
				this.FilePrintLine( "["
						+ Integer.toString( Biblionaer.meineEinstellungen.getKonsolenModus() )
						+ "]" + pAusgabe );
				break;
			default:
				// Gibt es nicht!
				break;
		}
	}

	public void println(String pAusgabe, int pPrio) {

		if ( pPrio <= 0 ) {
			if ( Biblionaer.meineEinstellungen.getKonsolenModus() == 1
					|| Biblionaer.meineEinstellungen.getKonsolenModus() == 2 ) {
				this.println( pAusgabe );
			}
			else {
				System.out.println( pAusgabe );
			}
		}
		else if ( pPrio <= Biblionaer.meineEinstellungen.getKonsolenModus() ) {
			this.println( pAusgabe );
		}
	}

	public void print(String pAusgabe) {
		switch (Biblionaer.meineEinstellungen.getKonsolenModus()) {
			case 0:
				// Keine Ausgabe
				break;
			case 1:
				// Ausgabe über die System-Konsole
				this.SystemPrint( pAusgabe );
				break;
			case 2:
				// Ausgabe in ein File
				this.FilePrint( pAusgabe );
				break;
			default:
				// Gibt es nicht!
				break;
		}
	}

	public void SystemPrintLine(String pAusgabe) {
		System.out.println( pAusgabe );
	}

	public void SystemPrint(String pAusgabe) {
		System.out.print( pAusgabe );
	}

	public void FilePrintLine(String pAusgabe) {
	// wird noch später implementiert
	}

	public void FilePrint(String pAusgabe) {
	// wird noch später implementiert
	}

}
