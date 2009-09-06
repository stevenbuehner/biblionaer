package GUI;

import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class QuizPanelJoker extends QuizPanel {

	public QuizPanelJoker(BufferedImage[] i, double x, double y, Hauptfenster p) {
		super( i, x, y, 10, p );

		loop_from = 0;
		loop_to = 0;
	}

	public QuizPanelJoker(BufferedImage i, double x, double y, Hauptfenster p) {
		super( i, x, y, 10, p );

		loop_from = 0;
		loop_to = 0;
	}

	public void setJokerEnabled(boolean pAktiv) {
		if ( pAktiv ) {
			loop_from = 0;
			loop_to = 0;
		}
		{
			loop_from = 2;
			loop_to = 2;
		}
	}

	public void setPressedIfPossible() {
		// Nur den Button reindrücken lassen, wenn er nicht schon deaktiviert
		// ist

		if ( loop_from == 0 ) {
			loop_from = 1;
			loop_to = 1;
		}
	}

	public void setReleasedIfPossible() {
		if ( loop_from == 1 ) {
			loop_from = 0;
			loop_to = 0;
		}
	}

	public void resetJoker() {
		loop_from = 0;
		loop_to = 0;
	}

	@Override
	public boolean collidedWith(QuizPanel s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void createKlickPolygon() {
		// TODO Auto-generated method stub

		Polygon klickFlaeche = new Polygon();

		klickFlaeche.addPoint( 4, 25 );
		klickFlaeche.addPoint( 17, 8 );
		klickFlaeche.addPoint( 37, 3 );
		klickFlaeche.addPoint( 59, 6 );
		klickFlaeche.addPoint( 77, 17 );
		klickFlaeche.addPoint( 83, 33 );
		klickFlaeche.addPoint( 69, 50 );
		klickFlaeche.addPoint( 49, 55 );
		klickFlaeche.addPoint( 22, 51 );
		klickFlaeche.addPoint( 7, 40 );

		this.klickFlaeche = klickFlaeche;
	}

}
