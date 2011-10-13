package quiz;

import java.util.Random;

import main.Biblionaer;

public class Spiel {

	protected Quizfrage fragen[];
	protected int aktuelleFrage;

	// Die ID, bei der der Joker verwendet wurde - Wenn unbenutzt dann '-1'
	protected long fiftyJoker;
	protected long puplikumsJoker;
	protected long tippJoker;

	// Spielstart und Spielende
	protected long spielstart = 0;
	protected long spielende = 0;

	protected long aktuelleFrageStartzeit = 0;

	public Spiel() {
		this(15); // Default-Wert
	}

	public Spiel(int pAnzahlFragen) {
		// 15 Quizfragen erstellen

		if (pAnzahlFragen > 0) {
			this.fragen = new Quizfrage[pAnzahlFragen];
		} else {
			// Wenn keine Fragen erstellt werden beim initialisieren, dann soll
			// auch kein Fragenobjekt erstellen
			this.fragen = null;
		}

		// '-1' steht fär unbenutzt
		this.aktuelleFrage = 1; // oder auch Schwierigkeitsgrad, geht von 1-15
		this.fiftyJoker = -1;
		this.puplikumsJoker = -1;
		this.tippJoker = -1;

	}

	public boolean fiftyJokerSchonVerwendet() {
		return (this.fiftyJoker < 0) ? false : true;
	}

	public void setFiftyJokerVerwendet(boolean pVerwendet) {
		if (pVerwendet) {
			fiftyJoker = fragen[aktuelleFrage - 1].getId();
		} else {
			fiftyJoker = -1;
		}
	}

	public boolean puplikumsJokerSchonVerwendet() {
		return (this.puplikumsJoker < 0) ? false : true;
	}

	public void setPuplikumsJokerSchonVerwendet(boolean pVerwendet) {
		if (pVerwendet) {
			puplikumsJoker = fragen[aktuelleFrage - 1].getId();
		} else {
			puplikumsJoker = -1;
		}
	}

	public boolean tippJokerSchonVerwendet() {
		return (this.tippJoker < 0) ? false : true;
	}

	public void setTippJokerSchonVerwendet(boolean pVerwendet) {
		if (pVerwendet) {
			tippJoker = fragen[aktuelleFrage - 1].getId();
		} else {
			tippJoker = -1;
		}
	}

	public void setEnde() {
		this.spielende = System.currentTimeMillis();
	}

	public void setEnde(long pEnde) {
		this.spielende = pEnde;
	}

	public long getEnde() {
		return spielende;
	}

	public void setBegin() {
		this.spielstart = System.currentTimeMillis();
	}

	public void setBegin(long pBegin) {
		this.spielstart = pBegin;
	}

	public long getBegin() {
		return spielstart;
	}

	public void setAktuelleFrageStartzeit() {
		this.aktuelleFrageStartzeit = System.currentTimeMillis();
	}

	public long getAktuelleFrageStartzeit() {
		return this.aktuelleFrageStartzeit;
	}

	/**
	 * @return ob Erfolgreich, wenn ja dann true
	 */
	public boolean starteSpiel() {
		if (fragen.length > 0) {
			this.aktuelleFrage = 1;
			setBegin();
			setEnde(0);
			setAktuelleFrageStartzeit();

			fiftyJoker = -1;
			puplikumsJoker = -1;
			tippJoker = -1;

			return true;
		}

		return false;
	}

	/**
	 * @return Wenn erfolgreich, dann true
	 */
	public boolean setNaechsteFrage() {
		if (aktuelleFrage < fragen.length) {
			this.aktuelleFrage++;
			fragen[aktuelleFrage - 1].setBegin();
			this.setAktuelleFrageStartzeit();
			return true;
		}

		return false;
	}

	/**
	 * Gibt NACH dem beenden des Spiels die Dauer zuräck. Wenn spielbeginn oder
	 * spielende noch nicht gesezt sind, gibt die Funktion -1 zuräck.
	 * 
	 * @return long zeitDifferenz
	 */
	public long spielDauerInSekunden() {
		if (this.spielende == 0 || this.spielstart == 0) {
			return -1;
		} else {
			return (this.spielende - this.spielstart) / 1000;
		}
	}

	/**
	 * Gibt, wenn spielbegin gesetzt ist, die verstrichene Zeit bis zum
	 * aktuellen Zeitpunkt zuräck Wenn spielgbein noch nicht gesetzt ist, gibt
	 * die Funktion -1 zuräck.
	 * 
	 * @return long zeitDifferenz
	 */
	public long spielDauerBisJetztInSekunden() {
		if (this.spielstart == 0) {
			return -1;
		} else {
			return (System.currentTimeMillis() - this.spielstart) / 1000;
		}
	}

	public long frageDauerBisJetztInSekunden() {
		if (this.spielstart == 0) {
			return -1;
		} else {
			return (System.currentTimeMillis() - this.aktuelleFrageStartzeit) / 1000;
		}
	}

	public void setFrage(int pIndex, Quizfrage pFrage) {
		try {
			fragen[pIndex] = pFrage;
			// this.echoFrage( pFrage );
		} catch (Exception e) {
			Biblionaer.meineKonsole.println("Klasse Spiel konnte die Frage in Index " + pIndex
					+ " nicht setzen und warf eine Exception!!!");
		}
	}

	public Quizfrage getFrage(int pIndex) {
		try {
			return fragen[pIndex - 1];
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Klasse Spiel konnte die Frage in Index " + pIndex
					+ " nicht finden und warf eine Exception!!!");
		}
		return null;
	}

	public Quizfrage getAktuelleFrage() {
		try {
			return fragen[this.aktuelleFrage - 1];
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Klasse Spiel konnte die Frage in Index " + this.aktuelleFrage
					+ " nicht finden und warf eine Exception!!!");
		}
		return null;
	}

	public Quizfrage getAktuelleFrageAnzuzeigen() {

		if (fragen != null) {
			// Kopiere Objekt
			Quizfrage rueckgabe = new Quizfrage();

			rueckgabe.setFragestellung(fragen[aktuelleFrage - 1].getFragestellung());
			rueckgabe.setSchwierigkeitsGrad(fragen[aktuelleFrage - 1].getSchwierigkeitsGrad());

			rueckgabe.setAntwort1(fragen[aktuelleFrage - 1].getAntwort1());
			rueckgabe.setAntwort2(fragen[aktuelleFrage - 1].getAntwort2());
			rueckgabe.setAntwort3(fragen[aktuelleFrage - 1].getAntwort3());
			rueckgabe.setAntwort4(fragen[aktuelleFrage - 1].getAntwort4());

			rueckgabe.setBegin(fragen[aktuelleFrage - 1].getBegin());

			// Wenn die ID angezeigt werden soll, zeige sie an
			if (Biblionaer.meineEinstellungen.getQuizIdAnzeigen()) {
				rueckgabe.setId(fragen[aktuelleFrage - 1].getId());
			}

			if (fiftyJoker == fragen[aktuelleFrage - 1].getId()) {
				// Zwei Antworten per Zufall herausfiltern
				int anzHerausgefiltert = 0;

				Random generator = new Random();

				while (anzHerausgefiltert < 2) {
					int zufall = generator.nextInt(3) + 1;
					System.out.println("Zufallszahl: " + zufall);
					if (zufall != fragen[aktuelleFrage - 1].getRichtigeAntwort()) {
						switch (zufall) {
						case 1:
							if (rueckgabe.getAntwort1() != null) {
								anzHerausgefiltert++;
								rueckgabe.setAntwort1(null);
							}
							break;
						case 2:
							if (rueckgabe.getAntwort2() != null) {
								anzHerausgefiltert++;
								rueckgabe.setAntwort2(null);
							}
							break;
						case 3:
							if (rueckgabe.getAntwort3() != null) {
								anzHerausgefiltert++;
								rueckgabe.setAntwort3(null);
							}
							break;
						case 4:
							if (rueckgabe.getAntwort4() != null) {
								anzHerausgefiltert++;
								rueckgabe.setAntwort4(null);
							}
							break;
						default:
							Biblionaer.meineKonsole.println("FEHLER: Versuche Antwort " + zufall
									+ " zu entfernen. Die gibt es aber nicht!", 1);
							break;
						}
					}
				}
			}
			if (puplikumsJoker == fragen[aktuelleFrage - 1].getId()) {

			}
			if (tippJoker == fragen[aktuelleFrage - 1].getId()) {
				rueckgabe.setLoesungshinweis(fragen[aktuelleFrage - 1].getLoesungshinweis());
			}

			return rueckgabe;
		}

		return null;
	}

	public int getAnzahlFragenImSpiel() {
		return fragen.length;
	}

	/**
	 * @return Integerwert der aktuellen Frage
	 */
	public int getAktuellenSchwierigkeitsgrad() {
		return this.fragen[aktuelleFrage - 1].getSchwierigkeitsGrad();
	}

	public void setAktuelleFrage(int pAktuelleFrage) {
		if (this.aktuelleFrage != pAktuelleFrage) {
			this.aktuelleFrage = pAktuelleFrage;
			this.setAktuelleFrageStartzeit();
		}
	}

	public boolean laeufDasSpiel() {
		if (spielstart > 0 && spielende == 0) {
			return true;
		}
		return false;
	}

	public boolean istGeradeLetzteFrage() {
		if (this.aktuelleFrage == this.fragen.length) {
			return true;
		} else {
			return false;
		}
	}

	public boolean istAktuelleAntwort(int antwortNummer) {

		if (fragen.length < aktuelleFrage) {
			Biblionaer.meineKonsole.println(
					"Spiel.istAktuelleAntwort kann nicht ausgefährt werden, Indizierungsfehler. Angefordert wurde die ID: "
							+ this.aktuelleFrage, 1);
			return false;
		}

		if (fragen[aktuelleFrage - 1].getRichtigeAntwort() == antwortNummer) {
			return true;
		} else {
			return false;
		}
	}

	public int getAktuelleRichtigeAntwort() {
		if (fragen.length < aktuelleFrage) {
			Biblionaer.meineKonsole.println(
					"Spiel.getAktuelleRichtigeAntwort kann nicht ausgefährt werden, Indizierungsfehler. Angefordert wurde die ID: "
							+ this.aktuelleFrage, 1);
			return -1;
		}

		return fragen[aktuelleFrage - 1].getRichtigeAntwort();
	}

	public void aktuelleFrageBeantwortet() {
		if (fragen.length < aktuelleFrage) {
			Biblionaer.meineKonsole.println(
					"Spiel.aktuelleFrageRichtigBeantwortet kann nicht ausgefährt werden, Indizierungsfehler. Angefordert wurde die ID: "
							+ this.aktuelleFrage, 1);
			return;
		}

		fragen[aktuelleFrage - 1].setEnde();
	}

	/**
	 * Debugging FUnktion ... gibt die äbergebene Frage auf der Konsole aus
	 */
	public static void echoFrage(Quizfrage pFrage) {
		System.out.println("ID: " + pFrage.getId());
		System.out.println("Frage: " + pFrage.getFragestellung());
		System.out.println("Antwort1: " + pFrage.getAntwort1());
		System.out.println("Antwort2: " + pFrage.getAntwort2());
		System.out.println("Antwort3: " + pFrage.getAntwort3());
		System.out.println("Antwort4: " + pFrage.getAntwort4());
		System.out.println("Tipp: " + pFrage.getLoesungshinweis());
		System.out.println("richtige Antw: " + pFrage.getRichtigeAntwort());
		System.out.println("Schweriegkeitsgrad: " + pFrage.getSchwierigkeitsGrad());
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	}

}
