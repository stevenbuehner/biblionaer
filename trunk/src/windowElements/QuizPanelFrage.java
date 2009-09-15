package windowElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Vector;

import quiz.Quizfrage;
import window.SinglePlayerSchirm;

public class QuizPanelFrage extends QuizPanel {
	protected Quizfrage	frage;

	public QuizPanelFrage(BufferedImage i, long delay, SinglePlayerSchirm p, Quizfrage frage) {
		super( i, 13, (double) i.getHeight() * -1, delay, p );

		this.frage = frage;

		this.dx = 200;
		this.dy = 200;
		this.endPositionX = 13;
		this.endPositionY = 100;

		this.createKlickPolygon();
	}

	public QuizPanelFrage(BufferedImage[] i, long delay, SinglePlayerSchirm p, Quizfrage frage) {
		super( i, 13, (double) i[0].getHeight() * -1, delay, p );

		this.frage = frage;

		this.dx = 200;
		this.dy = 200;
		this.endPositionX = 13;
		this.endPositionY = 100;

		this.createKlickPolygon();
	}

	@Override
	public boolean collidedWith(QuizPanel s) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFrage(Quizfrage frage) {
		this.frage = frage;
	}

	public Quizfrage getFrage() {
		return this.frage;
	}

	/**
	 * String = "" => Es wird ein leerer Kasten gezeichnet; String = null => Es
	 * wird kein Kasten gezeichnet
	 */
	@Override
	public void drawObjects(Graphics g) {
		if ( this.frage != null && this.visible ) {

			super.drawObjects( g );

			/*
			 * Rechteckgröße Komplett: 656 x 153 Fragefeld: ab Pos: 53/79 mit
			 * einer Breite von 552
			 */

			// Alte Schrift zwischenspeichern um sie später zurückzusetzen
			Font restoreFont = g.getFont();
			Color restoreColor = g.getColor();
			g.setFont( new Font( "Arial", Font.BOLD, 14 ) );

			// ------------- AUSGABE DER FRAGE -------------
			int offsetFrageX = 53;
			int offsetFrageY = 79;
			int breiteZurVerfuegungFrage = 552; // 552
			int zeilenOffset = 0; // Anzahl der Zeilen die uebersprungen werden
			// sollen (Maximal drei Zeilen Zeichnen)

			Vector<String> blub = wrapText( frage.getFragestellung(), breiteZurVerfuegungFrage, g
					.getFontMetrics() );
			g.setColor( Color.white );

			// ZeilenOffset berechnen (zum zentrieren in Y-Richtung)
			if ( blub.size() <= 2 ) {
				zeilenOffset = 1;
			}

			for (int i = 0; i < blub.size(); i++) {

				// Den String jetzt zentrieren

				int offsetNachZentrierung = (breiteZurVerfuegungFrage - g.getFontMetrics()
						.stringWidth( blub.get( i ) ))
						/ 2 + offsetFrageX;

				g.drawString( blub.get( i ), (int) x + offsetNachZentrierung, (int) y
						+ offsetFrageY + (i + zeilenOffset)
						* (g.getFontMetrics().getHeight() + g.getFontMetrics().getLeading()) );

			}

			// ------------- AUSGABE DES SCHWIERIGKEITSGRADES -------------
			int offsetSchwierigkeitsgradX = 247;
			int offsetSchwierigkeitsgradY = 27;
			int breiteZurVerfuegungSchwierigkeitsgrad = 162; // 162
			int offsetNachZentrierungSG = (breiteZurVerfuegungSchwierigkeitsgrad - g
					.getFontMetrics().stringWidth( frage.getSchwierigkeitsGradInMio() ))
					/ 2 + offsetSchwierigkeitsgradX;
			g.setFont( new Font( "Arial", Font.PLAIN, 18 ) );

			g.drawString( frage.getSchwierigkeitsGradInMio(), (int) x + offsetNachZentrierungSG,
					(int) y + offsetSchwierigkeitsgradY );

			// ------------- AUSGABE DER QUIZFRAGEN-ID -------------
			if ( this.frage.getId() > 0 ) {

				g.setFont( new Font( "Arial", Font.PLAIN, 9 ) );
				g.drawString( Long.toString( this.frage.getId() ), (int) this.x + 600,
						(int) this.y + 108 );
			}

			// paintShape( g );

			// Font und Farbe wieder auf die urspruenglichen Werte zuruecksetzen
			g.setFont( restoreFont );
			g.setColor( restoreColor );
		}
		else {
			// tue nichts
		}

	}

	@Override
	protected void createKlickPolygon() {
		Polygon klickFlaeche = new Polygon();

		klickFlaeche.addPoint( 16, 106 );
		klickFlaeche.addPoint( 52, 62 );
		klickFlaeche.addPoint( 603, 61 );
		klickFlaeche.addPoint( 639, 105 );
		klickFlaeche.addPoint( 603, 149 );
		klickFlaeche.addPoint( 49, 150 );
		klickFlaeche.addPoint( 16, 107 );

		this.klickFlaeche = klickFlaeche;

	}

	@Override
	public boolean feldAngeklickt(int xKlick, int yKlick) {

		if ( this.klickFlaeche == null || this.frage == null ) {
			return false;
		}

		// Funktioniert beim Logo nich nicht
		return this.klickFlaeche.contains( xKlick - this.x, yKlick - this.y );
	}

}
