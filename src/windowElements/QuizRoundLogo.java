package windowElements;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import window.SinglePlayerSchirm;

public class QuizRoundLogo extends QuizPanel {

	public QuizRoundLogo(BufferedImage i, double x, double y, long delay, SinglePlayerSchirm p) {
		super( i, x, y, delay, p );

		this.endPositionX = x;
		this.endPositionY = y;

		this.startPositionX = x;
		this.startPositionY = y;
	}

	public QuizRoundLogo(BufferedImage[] i, double x, double y, long delay, SinglePlayerSchirm p) {
		this( i[0], x, y, delay, p ); // Auf zum Superkonstruktor
	}

	@Override
	public boolean collidedWith(QuizPanel s) {
		// Wird hier nicht benoetigt
		return false;
	}

	@Override
	protected void createKlickPolygon() {
		// TODO Auto-generated method stub
		Rectangle2D.Double klickFlaeche = new Rectangle2D.Double( 0, 0, this.width, this.height );
		
		this.klickFlaeche = klickFlaeche;
	}
}
