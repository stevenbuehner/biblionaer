package windowElements;

import interfaces.Drawable;
import interfaces.Movable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public abstract class QuizPanel extends Rectangle2D.Double implements Movable, Drawable {
	long				delay;						// Bewegungsgeschwindigkeit
	protected boolean	visible			= true;

	protected Shape		klickFlaeche	= null;

	protected double	dx;
	protected double	dy;

	protected int		currentpic		= 0;
	protected long		animation		= 0;
	protected int		loop_from		= 0;
	protected int		loop_to			= 0;

	protected double	startPositionX;
	protected double	startPositionY;
	protected double	endPositionX;				// FŸr
	// Bewegungen
	protected double	endPositionY;				// FŸr
	// Bewegungen

	protected boolean	remove			= false;

	public QuizPanel(double x, double y, long delay) {
		this.startPositionX = x;
		this.startPositionY = y;
		this.delay = delay;
	}

	public void doLogic(long delta) {

		if ( delay == -1 )
			return;

		animation += (delta / 1000000);
		if ( animation > delay ) {
			animation = 0;
			computeAnimation();
		}
	}

	public void move(long delta) {

		if ( dx != 0 ) {
			if ( (dx > 0 && endPositionX > x) || (dx < 0 && endPositionX < x) ) {
				x += dx * (delta / 1e9); // Nur bewegen, wenn es nicht schon an
				// der Endposition ist
			}
			else {
				x = endPositionX;
			}
		}

		if ( dy != 0 ) {
			if ( (dy > 0 && endPositionY > y) || (dy < 0 && endPositionY < y) ) {
				y += dy * (delta / 1e9); // Nur bewegen, wenn es nicht schon an
				// der Endposition ist
			}
			else {
				y = endPositionY;
			}
		}
	}

	public void drawObjects(Graphics g) {
		if ( !visible )
			return;
	}

	/**
	 * Format the intro text.
	 * 
	 * @param text
	 *        The intro text.
	 * @return The lines of the formatted intro text as an array.
	 */
	public static Vector<String> wrapText(String text, int width, FontMetrics fm) {

		Vector<String> zerstueckelterText = new Vector<String>();

		int zeigerAnf = 0;
		int zeigerEnd = 1;

		String derString = text.trim(); // Leerzeichen entfernen
		String substring;

		if ( fm.stringWidth( derString ) <= width ) {
			// Der haeufigste Fall ist, dass gar nicht umgebrochen werden muss.
			// Den Fangen wir gleich zu Begin ab:
			zerstueckelterText.add( derString.trim() );
			return zerstueckelterText;
		}

		// Ab hier die normale Zerlegung anhand und Unterteilung immer bei den
		// Leerzeichen
		while (zeigerEnd < derString.length()) {

			substring = derString.substring( zeigerAnf, zeigerEnd );
			while (fm.stringWidth( substring.trim() ) <= width && zeigerEnd < text.length()) {
				zeigerEnd++;
				substring = derString.substring( zeigerAnf, zeigerEnd );
			}

			if ( zeigerEnd <= text.length() && fm.stringWidth( substring.trim() ) <= width ) {
				// Wenn der Text kleiner als Width ist gib ihn gleich
				// zurŸck.
				zerstueckelterText.add( substring.trim() );
				return zerstueckelterText;
			}
			else {
				// Wenn der Text noch weiter geht, dann muss noch nach
				// Leerzeichen gesucht werden
				int letztesLeerzeichen = substring.lastIndexOf( ' ' );
				if ( letztesLeerzeichen == -1 ) {
					// Kein Leerzeichen, dann wird der Text halt hart gebrochen

				}
				else {
					zeigerEnd = letztesLeerzeichen + zeigerAnf; // Da zeigerEnd
					// in Bezug auf
					// den Substring
					// ist!!!
					substring = derString.substring( zeigerAnf, zeigerEnd );
					zeigerAnf = zeigerEnd;
				}
			}
			zerstueckelterText.add( substring.trim() );

		}
		return zerstueckelterText;
	}

	protected void paintShape(Graphics g) {
		// Nur zum debuggen
		if ( klickFlaeche == null )
			return;

		g.setColor( Color.black );

		if ( klickFlaeche instanceof Polygon ) {
			Polygon zeichenPoly = new Polygon();
			for (int i = 0; i < ((Polygon) this.klickFlaeche).npoints; i++) {
				zeichenPoly.addPoint( (int) (((Polygon) this.klickFlaeche).xpoints[i] + this.x),
						(int) (((Polygon) this.klickFlaeche).ypoints[i] + this.y) );
				g.fillPolygon( zeichenPoly );
			}
		}
		else if ( klickFlaeche instanceof Rectangle2D ) {
			// g.fillRectÊ(ÊÊ(int) ((Rectangle2D) klickFlaeche).getX(),
			// ((Rectangle2D) klickFlaeche).getY(), ((Rectangle2D)
			// klickFlaeche).getWidth(), ((Rectangle2D)
			// klickFlaeche).getHeight() );
		}
	}

	public boolean feldAngeklickt(int xKlick, int yKlick) {

		if ( this.klickFlaeche == null ) {
			return false;
		}

		// Funktioniert beim Logo nicht
		return this.klickFlaeche.contains( xKlick - this.x, yKlick - this.y );
	}

	protected void computeAnimation() {

		currentpic++;

		if ( currentpic > loop_to ) {
			currentpic = loop_from;
		}
	}

	public void setLoop(int from, int to) {
		loop_from = from;
		loop_to = to;
		currentpic = from;
	}

	public void resetAnimation() {
		this.x = startPositionX;
		this.y = startPositionY;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean sichtbar) {
		this.visible = sichtbar;
	}

	public void setVerticalSpeed(double d) {
		dy = d;
	}

	public void setHorizontalSpeed(double d) {
		dx = d;
	}

	public double getVerticalSpeed() {
		return dy;
	}

	public double getHorizontalSpeed() {
		return dx;
	}

	public void setX(double i) {
		x = i;
	}

	public void setY(double i) {
		y = i;
	}

}
