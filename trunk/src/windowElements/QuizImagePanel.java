package windowElements;

import interfaces.Drawable;
import interfaces.Movable;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import window.SinglePlayerSchirm;

public abstract class QuizImagePanel extends QuizPanel implements Drawable, Movable {

	public static final int			BLAU	= 0;
	public static final int			GRUEN	= 1;
	public static final int			GELB	= 2;

	protected BufferedImage[]		pics;

	protected int					angle;
	protected int					rotationx;
	protected int					rotationy;
	protected AffineTransform		at;

	public QuizImagePanel(BufferedImage[] i, double x, double y, long delay) {
		super( x, y, delay );
		pics = i;
		this.x = x;
		this.y = y;

		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		loop_from = 0;
		loop_to = pics.length - 1;
		at = new AffineTransform();
		rotationx = (int) (width / 2);
		rotationy = (int) (height / 2);

		createKlickPolygon();
	}

	protected abstract void createKlickPolygon();

	public QuizImagePanel(BufferedImage i, double x, double y, long delay) {
		super( x, y, delay );

		pics = new BufferedImage[1];
		pics[0] = i;
		this.x = x;
		this.y = y;
		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		loop_from = 0;
		loop_to = pics.length - 1;
		at = new AffineTransform();
		rotationx = (int) (width / 2);
		rotationy = (int) (height / 2);

		createKlickPolygon();
	}


	@Override
	public void drawObjects(Graphics g) {
		super.drawObjects( g );

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

	public void setAngle(int a) {
		angle = a;
	}

	public int getAngle() {
		return angle;
	}

	public Point getRotation() {
		return (new Point( rotationx, rotationy ));
	}

	public boolean checkOpaqueColorCollisions(QuizImagePanel s) {

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

}
