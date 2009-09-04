package GUI;

import interfaces.Drawable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Vector;

import main.Biblionaer;

public class QuizTextPanel implements Drawable {

	protected String	text;
	protected int		xPos			= 0;
	protected int		yPos			= 0;

	protected int		textAusrichtung	= Font.CENTER_BASELINE;

	protected int		width			= -1;									// Keine
	// Breite
	// verwenden

	protected Color		schreibFarbe	= Color.green;
	protected Font		schreibFont		= new Font( "Arial", Font.PLAIN, 12 );

	public String getText() {
		return text;
	}

	public void setText(String derText) {
		this.text = derText;
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int pos) {
		xPos = pos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int pos) {
		yPos = pos;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Color getSchreibFarbe() {
		return schreibFarbe;
	}

	public void setSchreibFarbe(Color schreibFarbe) {
		this.schreibFarbe = schreibFarbe;
	}

	public Font getSchreibFont() {
		return schreibFont;
	}

	public void setSchreibFont(Font schreibFont) {
		this.schreibFont = schreibFont;
	}

	public QuizTextPanel(String pText, int xPos, int yPos) {
		this( xPos, yPos );
		this.text = pText;
	}

	public QuizTextPanel(String pText, int xPos, int yPos, int width) {
		this( xPos, yPos, width );
		this.text = pText;
	}

	/**
	 * Kann nur intern aufgerufen werden
	 * 
	 * @param xPos
	 * @param yPos
	 */
	protected QuizTextPanel(int xPos, int yPos) {
		this( xPos, yPos, -1 );
	}

	/**
	 * Kann nur intern aufgerufen werden
	 * 
	 * @param xPos
	 * @param yPos
	 * @param width
	 */
	protected QuizTextPanel(int xPos, int yPos, int width) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
	}

	public void drawObjects(Graphics g) {
		if ( text != null ) {

			Color backC = g.getColor();
			Font backF = g.getFont();

			if ( schreibFarbe != null )
				g.setColor( schreibFarbe );

			if ( schreibFont != null )
				g.setFont( schreibFont );

			// Nur dann eine Betrachtung der Breite machen, wenn width > 0
			if ( width > 1 ) {
				Vector<String> derTextVektor = new Vector<String>();
				derTextVektor = QuizPanel.wrapText( this.text, this.width, g.getFontMetrics() );

				for (int i = 0; i < derTextVektor.size(); i++) {
					this.zeichneTeilstring( this.xPos, this.yPos + i
							* (g.getFontMetrics().getHeight() + g.getFontMetrics().getLeading()),
							derTextVektor.get( i ), g );

				}
				// g.drawRect( this.xPos, this.yPos, this.width, 50 );
			}
			else {
				g.drawString( text, xPos, yPos );
			}

			g.setColor( backC );
			g.setFont( backF );

		}
	}

	protected void zeichneTeilstring(int xPos, int yPos, String text, Graphics g) {
		int breiteDesParent = Biblionaer.meinHauptfenster.backgroundQuizRahmen.getWidth();

		switch (this.textAusrichtung) {
			case Font.CENTER_BASELINE:
				// Biblionaer.meineKonsole.println(
				// "Ausgabe des Tipps zentriert", 4 );
				g.drawString( text, (breiteDesParent - g.getFontMetrics().stringWidth( text )) / 2,
						yPos );
				break;
			case Font.LAYOUT_LEFT_TO_RIGHT:
				// Biblionaer.meineKonsole.println(
				// "Ausgabe des Tipps linksbündig", 4 );
				g.drawString( text, xPos, yPos );
				break;
			default:
				// Biblionaer.meineKonsole.println(
				// "Ausgabe des Tipps ohne Angabe => Linksbündig", 4 );
				g.drawString( text, xPos, yPos );
				break;
		}
	}
}
