package windowElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class QuizPolygonPanelAntwort extends QuizPanel {

	protected Polygon				polyBackground;

	protected Color					backgroundColorNormal	= new Color( 59, 111, 241 );
	protected Color					backgroundColorMarkiert	= new Color( 150, 126, 18 );
	protected Color					backgroundColorFalsch	= new Color( 150, 23, 18 );
	protected Color					backgroundColorRichtig	= new Color( 24, 150, 42 );

	protected Color					lineColorNormal			= Color.WHITE;
	protected Color					lineColorMarkiert		= Color.WHITE;
	protected Color					lineColorFalsch			= Color.WHITE;
	protected Color					lineColorRichtig		= Color.WHITE;

	protected Font					textFont				= new Font( "Arial", Font.PLAIN, 20 );
	protected Color					textColor				= Color.WHITE;

	protected Color					backgroundColorSelected	= backgroundColorNormal;
	protected Color					lineColorSelected		= lineColorNormal;

	protected String				antwortText				= null;
	protected String				lblText					= "";
	protected Rectangle2D.Double	rectAntwortText			= new Rectangle2D.Double();

	public QuizPolygonPanelAntwort(int x, int y, int width, int height, String label, long delay) {
		super( x, y, delay );
		this.lblText = label;

		int space = (int) (width * 0.05);
		int innerWidth = width - space * 2;
		int innerHeight = ((int) height / 2);

		// Kasten
		this.polyBackground = new Polygon();
		polyBackground.addPoint( x, y + innerHeight );
		polyBackground.addPoint( x + space, y );
		polyBackground.addPoint( x + space + innerWidth, y );
		polyBackground.addPoint( x + space * 2 + innerWidth, y + innerHeight );
		polyBackground.addPoint( x + space + innerWidth, y + innerHeight * 2 );
		polyBackground.addPoint( x + space, y + innerHeight * 2 );
		polyBackground.addPoint( x, y + innerHeight );

		// Rechteck initialisieren, innerhalb dem nachher der Text geschrieben
		// werden kann
		rectAntwortText.x = x + space;
		rectAntwortText.y = y;
		rectAntwortText.height = height;
		rectAntwortText.width = innerWidth;

	}

	@Override
	public void doLogic(long delta) {
	// Wir haben keine Animation hier
	}

	@Override
	public void drawObjects(Graphics g) {
		super.drawObjects( g );

		if ( this.antwortText != null ) {
			Graphics2D g2 = (Graphics2D) g;
			int randDicke = 3;

			// Sichern von Farbe und Schriftart
			Color sichColor = g.getColor();
			Font sichFont = g.getFont();
			Stroke sichStroke = g2.getStroke();

			g2.setColor( backgroundColorSelected );
			g2.fillPolygon( this.polyBackground );

			g2.setColor( lineColorSelected );
			g2
					.setStroke( new BasicStroke( randDicke, BasicStroke.CAP_ROUND,
							BasicStroke.JOIN_MITER ) );
			g2.drawPolygon( this.polyBackground );

			// Hier muss dann der Text eingezeichnet werden
			int zeilenOffsetInPixel = 0; // Maximal drei Zeilen Zeichnen

			g2.setFont( textFont );
			g2.setColor( textColor );

			Vector<String> blub = wrapText( this.lblText + this.antwortText,
					(int) this.rectAntwortText.width, g2.getFontMetrics() );

			if ( blub.size() < 2 ) {
				zeilenOffsetInPixel = (int) (rectAntwortText.getHeight() / 2 - (g2.getFontMetrics()
						.getHeight() + g2.getFontMetrics().getLeading()) / 3 * 2);
			}
			else {
				zeilenOffsetInPixel = 5;
			}

			for (int i = 1; i <= blub.size(); i++) {
				g2.drawString( blub.get( i - 1 ), (int) this.rectAntwortText.x,
						(int) this.rectAntwortText.y
								+ i
								* (g2.getFontMetrics().getHeight() + g2.getFontMetrics()
										.getLeading()) + zeilenOffsetInPixel );
			}

			// Wiederherstellen der ursprŸnglichen Einstellungen
			g.setFont( sichFont );
			g.setColor( sichColor );
			g2.setStroke( sichStroke );
		}
	}

	public Color getBackgroundColor() {
		return backgroundColorNormal;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColorNormal = backgroundColor;
	}

	public void setStatusNormal() {
		this.lineColorSelected = this.lineColorNormal;
		this.backgroundColorSelected = this.backgroundColorNormal;
	}

	public void setStatusMarkiert() {
		this.lineColorSelected = this.lineColorMarkiert;
		this.backgroundColorSelected = this.backgroundColorMarkiert;
	}

	public void setStatusFalsch() {
		this.lineColorSelected = this.lineColorFalsch;
		this.backgroundColorSelected = this.backgroundColorFalsch;
	}

	public void setStatusRichtig() {
		this.lineColorSelected = this.lineColorRichtig;
		this.backgroundColorSelected = this.backgroundColorRichtig;
	}

	public String getAusgabeString() {
		return antwortText;
	}

	public void setAusgabeString(String ausgabeString) {
		this.antwortText = ausgabeString;
	}

	public String getLblText() {
		return lblText;
	}

}
