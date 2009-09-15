package windowElements;

import interfaces.Drawable;
import interfaces.Movable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import window.SinglePlayerSchirm;

public abstract class QuizPanel extends Rectangle2D.Double implements Drawable, Movable {

	public static final int			BLAU			= 0;
	public static final int			GRUEN			= 1;
	public static final int			GELB			= 2;

	long							delay;						// Bewegungsgeschwindigkeit
	long							animation		= 0;
	protected SinglePlayerSchirm	meinHauptfenster;
	protected BufferedImage[]		pics;
	protected int					currentpic		= 0;
	protected boolean				visible			= true;

	protected Shape					klickFlaeche	= null;

	protected double				dx;
	protected double				dy;

	protected double				startPositionX;
	protected double				startPositionY;
	protected double				endPositionX;				// FŸr
	// Bewegungen
	protected double				endPositionY;				// FŸr
	// Bewegungen

	int								loop_from;
	int								loop_to;

	int								angle;
	int								rotationx;
	int								rotationy;
	AffineTransform					at;

	public boolean					remove			= false;

	public QuizPanel(BufferedImage[] i, double x, double y, long delay, SinglePlayerSchirm p) {
		pics = i;
		this.x = x;
		this.y = y;
		this.startPositionX = x;
		this.startPositionY = y;
		this.delay = delay;
		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		meinHauptfenster = p; // Hier wohl unnoetig
		loop_from = 0;
		loop_to = pics.length - 1;
		at = new AffineTransform();
		rotationx = (int) (width / 2);
		rotationy = (int) (height / 2);

		createKlickPolygon();
	}

	protected abstract void createKlickPolygon();

	public QuizPanel(BufferedImage i, double x, double y, long delay, SinglePlayerSchirm p) {
		pics = new BufferedImage[1];
		pics[0] = i;
		this.x = x;
		this.y = y;
		this.startPositionX = x;
		this.startPositionY = y;
		this.delay = delay;
		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		meinHauptfenster = p;
		loop_from = 0;
		loop_to = pics.length - 1;
		at = new AffineTransform();
		rotationx = (int) (width / 2);
		rotationy = (int) (height / 2);

		createKlickPolygon();
	}

	public void drawObjects(Graphics g) {
		if(!visible)
			return;
		
		Graphics2D g2 = (Graphics2D) g;
		if ( angle != 0 ) {
			at.rotate( Math.toRadians( angle ), x + rotationx, y + rotationy );
			g2.setTransform( at );
			g2.drawImage( pics[currentpic], (int) x, (int) y, null );
			at.rotate( -Math.toRadians( angle ), x + rotationx, y + rotationy );
			g2.setTransform( at );
		}
		else {
			g.drawImage( pics[currentpic], (int) x, (int) y, null );
		}
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

	private void computeAnimation() {

		currentpic++;

		if ( currentpic > loop_to ) {
			currentpic = loop_from;
		}

	}

	public abstract boolean collidedWith(QuizPanel s);

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setAngle(int a) {
		angle = a;
	}

	public int getAngle() {
		return angle;
	}

	public void setLoop(int from, int to) {
		loop_from = from;
		loop_to = to;
		currentpic = from;
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

	public Point getRotation() {
		return (new Point( rotationx, rotationy ));
	}

	public boolean checkOpaqueColorCollisions(QuizPanel s) {

		Rectangle2D.Double cut = (Double) this.createIntersection( s );
		if ( (cut.width < 1) || (cut.height < 1) ) {
			return false;
		}

		// Rechtecke in Bezug auf die jeweiligen Images
		Rectangle2D.Double sub_me = getSubRec( this, cut );
		Rectangle2D.Double sub_him = getSubRec( s, cut );

		BufferedImage img_me = pics[currentpic].getSubimage( (int) sub_me.x, (int) sub_me.y,
				(int) sub_me.width, (int) sub_me.height );
		BufferedImage img_him = s.pics[s.currentpic].getSubimage( (int) sub_him.x, (int) sub_him.y,
				(int) sub_him.width, (int) sub_him.height );

		for (int i = 0; i < img_me.getWidth(); i++) {
			for (int n = 0; n < img_him.getHeight(); n++) {

				int rgb1 = img_me.getRGB( i, n );
				int rgb2 = img_him.getRGB( i, n );

				if ( isOpaque( rgb1 ) && isOpaque( rgb2 ) ) {
					return true;
				}

			}
		}

		return false;
	}

	protected Rectangle2D.Double getSubRec(Rectangle2D.Double source, Rectangle2D.Double part) {

		// Rechtecke erzeugen
		Rectangle2D.Double sub = new Rectangle2D.Double();

		// get X - compared to the Rectangle
		if ( source.x > part.x ) {
			sub.x = 0;
		}
		else {
			sub.x = part.x - source.x;
		}

		if ( source.y > part.y ) {
			sub.y = 0;
		}
		else {
			sub.y = part.y - source.y;
		}

		sub.width = part.width;
		sub.height = part.height;

		return sub;
	}

	protected boolean isOpaque(int rgb) {

		int alpha = (rgb >> 24) & 0xff;

		if ( alpha == 0 ) {
			return false;
		}

		return true;

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

	public void resetAnimation() {
		this.x = startPositionX;
		this.y = startPositionY;
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

		// Funktioniert beim Logo nich nicht
		return this.klickFlaeche.contains( xKlick - this.x, yKlick - this.y );
	}

}
