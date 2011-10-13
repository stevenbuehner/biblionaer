package windowElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class QuizPanelAntwort extends QuizImagePanel {

	public final int SPEED = 300;
	private String ausgabeString = null;

	public QuizPanelAntwort(BufferedImage i, double xPos, double yPos, double xZielPos, double yZielPos, long delay,
			String pAusgabeString) {
		super(i, xPos, yPos, delay);

		this.endPositionX = xZielPos;
		this.endPositionY = yZielPos;

		ausgabeString = pAusgabeString;

		if (x < xZielPos) {
			dx = SPEED;
		} else {
			dx = -SPEED;
		}
	}

	public QuizPanelAntwort(BufferedImage[] i, double xPos, double yPos, double xZielPos, double yZielPos, long delay,
			String pAusgabeString) {
		super(i, xPos, yPos, delay);

		this.endPositionX = xZielPos;
		this.endPositionY = yZielPos;

		ausgabeString = pAusgabeString;

		if (x < xZielPos) {
			dx = SPEED;
		} else {
			dx = -SPEED;
		}
	}

	@Override
	public void drawObjects(Graphics g) {
		if (visible) {
			// Zeichne Grafik
			super.drawObjects(g);

			// Zeichne Text
			if (this.ausgabeString != null) {
				/*
				 * Rechteckgrääe komplett: 336 x 45 Antwortfeld: ab Pos: 44/14
				 * mit einer maximalen Breite von 260px
				 */

				int offsetAntwortX = 44;
				int offsetAntwortY = 16;
				int breiteZurVerfuegungInAntwortFeld = 242; // 242
				int zeilenOffsetInPixel = 0; // Maximal drei Zeilen Zeichnen

				// Alte Schrift zwischenspeichern um sie später zuräckzusetzen
				Font restoreFont = g.getFont();
				Color restoreColor = g.getColor();

				g.setFont(new Font("Arial", Font.PLAIN, 14));
				g.setColor(Color.white);

				Vector<String> blub = wrapText(this.ausgabeString, breiteZurVerfuegungInAntwortFeld, g.getFontMetrics());

				if (blub.size() < 2) {
					zeilenOffsetInPixel = 8;
				}

				for (int i = 0; i < blub.size(); i++) {
					g.drawString(blub.get(i), (int) x + offsetAntwortX, (int) y + offsetAntwortY + i
							* g.getFontMetrics().getHeight() + zeilenOffsetInPixel + g.getFontMetrics().getLeading());
				}

				// Font und Farbe wieder auf die urspruenglichen Werte
				// zuruecksetzen
				g.setFont(restoreFont);
				g.setColor(restoreColor);
			}
		}
	}

	public String getAusgabeString() {
		return ausgabeString;
	}

	public void setAusgabeString(String pAusgabeString) {
		ausgabeString = pAusgabeString;
	}

	public void zeigeBlau() {
		this.setLoop(0, 0);
	}

	public void zeigeGruen() {
		this.setLoop(1, 1);
	}

	public void zeigeGelb() {
		this.setLoop(2, 2);
	}

	public void zeigeBlauFuer(int pMillisekunden) {
		currentpic = 0;
		animation = 0;
		delay = pMillisekunden;
	}

	public void zeigeGruenFuer(int pMillisekunden) {
		currentpic = 1;
		animation = 0;
		delay = pMillisekunden;
	}

	public void zeigeGelbFuer(int pMillisekunden) {
		currentpic = 2;
		animation = 0;
		delay = pMillisekunden;
	}

	@Override
	protected void createKlickPolygon() {
		Polygon klickFlaeche2 = new Polygon();

		// relativ zum Objektnullpunkt
		klickFlaeche2.addPoint(22, 24);
		klickFlaeche2.addPoint(38, 4);
		klickFlaeche2.addPoint(300, 4);
		klickFlaeche2.addPoint(316, 24);
		klickFlaeche2.addPoint(299, 43);
		klickFlaeche2.addPoint(38, 44);
		klickFlaeche2.addPoint(21, 24);

		this.klickFlaeche = klickFlaeche2;
	}

	@Override
	public boolean feldAngeklickt(int xKlick, int yKlick) {

		if (this.klickFlaeche == null || this.ausgabeString == null) {
			return false;
		}

		// Funktioniert beim Logo nich nicht
		return this.klickFlaeche.contains(xKlick - this.x, yKlick - this.y);
	}
}
