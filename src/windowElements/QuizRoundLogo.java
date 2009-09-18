package windowElements;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import window.SinglePlayerSchirm;

public class QuizRoundLogo extends QuizImagePanel {

	public QuizRoundLogo(BufferedImage i, double x, double y, long delay) {
		super( i, x, y, delay );

		this.endPositionX = x;
		this.endPositionY = y;

		this.startPositionX = x;
		this.startPositionY = y;
	}

	public QuizRoundLogo(BufferedImage[] i, double x, double y, long delay) {
		this( i[0], x, y, delay); // Auf zum Superkonstruktor
	}

	@Override
	protected void createKlickPolygon() {
		// TODO Auto-generated method stub
		Rectangle2D.Double klickFlaeche = new Rectangle2D.Double( 0, 0, this.width, this.height );

		this.klickFlaeche = klickFlaeche;
	}
}
