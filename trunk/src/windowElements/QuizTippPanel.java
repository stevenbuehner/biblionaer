package windowElements;

import java.awt.Color;
import java.awt.Font;

import quiz.Quizfrage;

public class QuizTippPanel extends QuizTextPanel {

	/**
	 * Defaultkonstruktor, der schon initialisiert ist
	 * 
	 * @param pFrage
	 */
	public QuizTippPanel(Quizfrage pFrage) {
		super( null, 65, 275, 550 );
		this.setSchreibFarbe( Color.black );
		this.setSchreibFont( new Font( "Arial", Font.BOLD, 14 ) );

		if ( pFrage != null ) {
			text = pFrage.getLoesungshinweis();
		}
		else {
			text = null;
		}

	}

	public QuizTippPanel(Quizfrage pFrage, int xPos, int yPos) {
		super( null, xPos, yPos );

		if ( pFrage != null ) {
			text = pFrage.getLoesungshinweis();
		}
		else {
			text = null;
		}

	}

	public QuizTippPanel(Quizfrage pFrage, int xPos, int yPos, int width) {
		super( null, xPos, yPos, width );

		if ( pFrage != null ) {
			text = pFrage.getLoesungshinweis();
		}
		else {
			text = null;
		}
	}

	public void setFrage(Quizfrage pFrage) {
		if ( pFrage != null ) {
			this.text = pFrage.getLoesungshinweis();
		}
		else {
			this.text = null;
		}
	}
}
