package interfaces;

import quiz.Quizfrage;

public interface QuizFenster {

	/**
	 * Setze die Frage, die angezeigt werden soll. Ist eine Komponente der Frage
	 * null, wird das ganze Feld nicht angezeigt Ist eine Komponente nur leer
	 * initialisiert, wird das Feld selbst angezeigt, aber ohne Inhalt.
	 * 
	 * @param frage
	 * @param mitAnimation
	 */
	public void setFrage(Quizfrage frage, boolean mitAnimation);

	/**
	 * Ob der Spielverlauf animiert werden soll
	 * 
	 * @param aktiviert
	 */
	public void setAnimationAktiviert(boolean aktiviert);

	/**
	 * Das Element Fragefeld (und Schwerierigkeitsgrad) anzeigen oder
	 * ausblenden.
	 * 
	 * @param anzeigen
	 */
	public void setFrageFeldSichtbar(boolean sichtbar);

	/**
	 * Setze alle Antwortfelder auf sichtbar.
	 */
	public void setAntwortenSichtbar(boolean sichtbar);

	/**
	 * Alle Antwortfelder auf einmal anzeigen oder ausblenden.
	 * 
	 * @param anzeigen
	 */
	public void setAntwortFelderSichtbar(boolean sichtbar);

	/**
	 * Das Antwortfeld 1 anzeigen oder ausblenden.
	 * 
	 * @param anzeigen
	 */
	public void setAntwortFeld1Sichtbar(boolean sichtbar);

	/**
	 * Das Antwortfeld 2 anzeigen oder ausblenden.
	 * 
	 * @param anzeigen
	 */
	public void setAntwortFeld2Sichtbar(boolean sichtbar);

	/**
	 * Das Antwortfeld 3 anzeigen oder ausblenden.
	 * 
	 * @param anzeigen
	 */
	public void setAntwortFeld3Sichtbar(boolean sichtbar);

	/**
	 * Das Antwortfeld 4 anzeigen oder ausblenden.
	 * 
	 * @param anzeigen
	 */
	public void setAntwortFeld4Sichtbar(boolean sichtbar);

	/**
	 * Setze alle Antwortfelder auf den Status markiert.
	 */
	public void setAntwortfelderMariert();

	/**
	 * Markiere das Antwortfeld. Markiert bedeutet angeklickt, aber noch nicht
	 * als richtig oder falsch bestätigen. Wie das genau ausschauen soll, legt
	 * das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld1Markiert();

	/**
	 * Markiere das Antwortfeld. Markiert bedeutet angeklickt, aber noch nicht
	 * als richtig oder falsch bestätigen. Wie das genau ausschauen soll, legt
	 * das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld2Markiert();

	/**
	 * Markiere das Antwortfeld. Markiert bedeutet angeklickt, aber noch nicht
	 * als richtig oder falsch bestätigen. Wie das genau ausschauen soll, legt
	 * das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld3Markiert();

	/**
	 * Markiere das Antwortfeld. Markiert bedeutet angeklickt, aber noch nicht
	 * als richtig oder falsch bestätigen. Wie das genau ausschauen soll, legt
	 * das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld4Markiert();

	/**
	 * Setze alle Antwortfelder auf den Status "richtige Antwort".
	 */
	public void setAntwortfelderRichtig();

	/**
	 * Zeige das Antwortfeld an, als richtig ausgewählte Option. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld1Richtig();

	/**
	 * Zeige das Antwortfeld an, als richtig ausgewählte Option. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld2Richtig();

	/**
	 * Zeige das Antwortfeld an, als richtig ausgewählte Option. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld3Richtig();

	/**
	 * Zeige das Antwortfeld an, als richtig ausgewählte Option. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld4Richtig();

	/**
	 * Setze alle Antwortfelder auf den Status "falsche Antwort".
	 */
	public void setAntwortfelderFalsch();

	/**
	 * Zeige das Antwortfeld an, als falsch ausgewählte Antwort. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld1Falsch();

	/**
	 * Zeige das Antwortfeld an, als falsch ausgewählte Antwort. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld2Falsch();

	/**
	 * Zeige das Antwortfeld an, als falsch ausgewählte Antwort. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld3Falsch();

	/**
	 * Zeige das Antwortfeld an, als falsch ausgewählte Antwort. Wie das genau
	 * ausschauen soll, legt das Fenster selbst fest. Dadurch lassen sich
	 * verschiedene "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld4Falsch();

	/**
	 * Setze alle Antwortfelder auf den Standard-Status. Diese Aktion kommt
	 * einem Reset gleich.
	 */
	public void setAntwortfelderNormal();

	/**
	 * Zeige das Antwortfeld normal, also im Standardmodus an. So wie es
	 * ausschaut, wenn noch nichts angeklickt wurde. Wie das genau ausschauen
	 * soll, legt das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld1Normal();

	/**
	 * Zeige das Antwortfeld normal, also im Standardmodus an. So wie es
	 * ausschaut, wenn noch nichts angeklickt wurde. Wie das genau ausschauen
	 * soll, legt das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld2Normal();

	/**
	 * Zeige das Antwortfeld normal, also im Standardmodus an. So wie es
	 * ausschaut, wenn noch nichts angeklickt wurde. Wie das genau ausschauen
	 * soll, legt das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld3Normal();

	/**
	 * Zeige das Antwortfeld normal, also im Standardmodus an. So wie es
	 * ausschaut, wenn noch nichts angeklickt wurde. Wie das genau ausschauen
	 * soll, legt das Fenster selbst fest. Dadurch lassen sich verschiedene
	 * "Fensterklassen" mit unterschiedlichem aussehen realisieren.
	 */
	public void setAntwortFeld4Normal();

	/**
	 * Joker ein- bzw. ausblenden
	 * 
	 * @param sichtbar
	 */
	public void setPublikumsJokerSichtbar(boolean sichtbar);

	/**
	 * Joker ein- bzw. ausblenden
	 * 
	 * @param sichtbar
	 */
	public void setFiftyJokerSichtbar(boolean sichtbar);

	/**
	 * Joker ein- bzw. ausblenden
	 * 
	 * @param sichtbar
	 */
	public void setTippJokerSichtbar(boolean sichtbar);

	/**
	 * Joker als bereits verwendet markieren (true), bzw. als als unbenutzt
	 * markieren (false).
	 * 
	 * @param benutzt
	 */
	public void setPublikumsJokerBenutzt(boolean benutzt);

	/**
	 * Joker als bereits verwendet markieren (true), bzw. als als unbenutzt
	 * markieren (false).
	 * 
	 * @param benutzt
	 */
	public void setFiftyJokerBenutzt(boolean benutzt);

	/**
	 * Joker als bereits verwendet markieren (true), bzw. als als unbenutzt
	 * markieren (false).
	 * 
	 * @param benutzt
	 */
	public void setTippJokerBenutzt(boolean benutzt);

	/**
	 * Alle Joker als unbenutzt markieren und einblenden.
	 */
	public void resetAlleJoker();

	/**
	 * Denn Statustext setzen.
	 * 
	 * @param text
	 */
	public void setStatusText(String text);

	/**
	 * Den Countdowntext setzen. Intern kann diese Funktion auch als SatusText
	 * angezeigt werden. Durch diese Funktion bleibt es aber dem Entwickler der
	 * grafischen Oberfläche überlassen, wie und wo er den Countdown einblendet.
	 * 
	 * @param text
	 */
	public void setCountdownText(String text);

	/**
	 * Ist eine Antwort richtig eingegeben worden, kann in der Oberfläche hier
	 * nun ein Sound oder eine Animation implementiert werden.
	 */
	public void playFrageRichtig();

	/**
	 * Ist eine Antwort falsch eingegeben worden, kann in der Oberfläche hier
	 * nun ein Sound oder eine Animation implementiert werden.
	 */
	public void playFrageFalsch();

	/**
	 * Wenn das Spiel komplett gewonnen wurde, wird diese Funktion aufgerufen.
	 * Hier können jetzt Blinkeffekte oder Soundeffekte eingebettet werden.
	 */
	public void playSpielGewonnen();

}
