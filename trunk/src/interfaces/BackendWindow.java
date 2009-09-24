package interfaces;

import java.awt.image.VolatileImage;

import quiz.Quizfrage;

public interface BackendWindow {

	/**
	 * Das FrontendWindow schickt einen Screenshot des eigenen Frames an das
	 * AdminPanel, damit dieser es darstellen kann.
	 */
	public void setFrontendScreenImage(VolatileImage screen);

	/**
	 * Diese Funktion wird nur zur Darstellung (zBsp im Backendfenster)
	 * benštigt. Oder gegebenenfalls um herauszufinden, ob Ÿberhaupt ein
	 * Tipp-Joker zu dieser Frage existiert.
	 * 
	 * @param frage
	 */
	public void setFrageKomplett(Quizfrage frage);
}
