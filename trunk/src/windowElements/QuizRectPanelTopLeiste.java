package windowElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public class QuizRectPanelTopLeiste extends QuizPanel {

	protected Rectangle2D.Double	rectZeichnung	= new Rectangle2D.Double();
	protected Color					backgroundColor	= new Color( 59, 111, 241 );

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public QuizRectPanelTopLeiste(double x, double y, double width, double height, long delay) {
		super( x, y, delay );
		this.endPositionX = x;
		this.endPositionY = y;

		this.rectZeichnung.x = x;
		this.rectZeichnung.y = y;
		this.rectZeichnung.width = width;
		this.rectZeichnung.height = height;
	}

	@Override
	public void doLogic(long delta) {
	// Wir haben (noch) keine Animation hier
	}

	@Override
	public void drawObjects(Graphics g) {
		super.drawObjects( g );

		Graphics2D g2 = (Graphics2D) g;
		int randDicke = 4;

		// Sichern von Farbe und Schriftart
		Color sichColor = g.getColor();
		Font sichFont = g.getFont();
		Stroke sichStroke = g2.getStroke();

		// Damit es nicht so eckig ausschaut :-)
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setStroke( new BasicStroke( randDicke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER ) );

		g2.setColor( backgroundColor );

		// Zeichenoperation
		g2.fillRect( (int) rectZeichnung.x, (int) rectZeichnung.y, (int) rectZeichnung.width,
				(int) rectZeichnung.height );

		// Wiederherstellen der urspränglichen Einstellungen
		g.setFont( sichFont );
		g.setColor( sichColor );
		g2.setStroke( sichStroke );
	}

}
