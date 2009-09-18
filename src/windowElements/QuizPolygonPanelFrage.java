package windowElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import quiz.Quizfrage;

public class QuizPolygonPanelFrage extends QuizPanel {

	protected Polygon				polyFrage			= new Polygon();
	protected Polygon				polySchweriegkeit	= new Polygon();

	protected Rectangle2D.Double	rectFrage			= new Rectangle2D.Double();
	protected Rectangle2D.Double	rectSchwierigkeit	= new Rectangle2D.Double();

	protected Color					backgroundColor		= new Color( 59, 111, 241 );
	protected Color					lineColor			= Color.WHITE;

	Quizfrage						frage				= null;

	public QuizPolygonPanelFrage(int x, int y, int width, int heightGesamt,
			int heightSchwierigkeitsgrad, int heightFrageKasten, long delay) {
		super( x, y, delay );
		this.x = x;
		this.y = y;

		// Berechnung Schweriegkeitskasten (Relativ)
		rectSchwierigkeit.y = y;
		rectSchwierigkeit.width = (width * 0.25);
		rectSchwierigkeit.x = x + (width - rectSchwierigkeit.width) / 2;
		rectSchwierigkeit.height = heightSchwierigkeitsgrad;
		int space2 = (int) (rectSchwierigkeit.width * 0.05); // 5% Steigung
		int innerWidth2 = (int) rectSchwierigkeit.width - space2 * 2;
		int innerHeight2 = heightSchwierigkeitsgrad / 2;

		// Berechnung Fragekasten (Relativ)
		rectFrage.x = x;
		rectFrage.y = y + heightGesamt - heightFrageKasten;
		int space1 = (int) (width * 0.05);
		int innerWidth1 = width - space1 * 2;
		int innerHeight1 = ((int) (heightGesamt - (innerHeight2 + innerHeight2 / 2)) / 2);
		rectFrage.width = width;
		rectFrage.height = heightFrageKasten;

		// Anlegen Fragekasten (Absolut)
		polyFrage.addPoint( (int) rectFrage.x, (int) rectFrage.y + innerHeight1 );
		polyFrage.addPoint( (int) rectFrage.x + space1, (int) rectFrage.y );
		polyFrage.addPoint( (int) rectFrage.x + space1 + innerWidth1, (int) rectFrage.y );
		polyFrage.addPoint( (int) rectFrage.x + space1 * 2 + innerWidth1, (int) rectFrage.y
				+ innerHeight1 );
		polyFrage.addPoint( (int) rectFrage.x + space1 + innerWidth1, (int) rectFrage.y
				+ innerHeight1 * 2 );
		polyFrage.addPoint( (int) rectFrage.x + space1, (int) rectFrage.y + innerHeight1 * 2 );
		polyFrage.addPoint( (int) rectFrage.x, (int) rectFrage.y + innerHeight1 );

		// Anlegen Schwierigkeitskasten (Absolut)
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x,
				(int) (rectSchwierigkeit.y + innerHeight2) );
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x + space2, (int) rectSchwierigkeit.y );
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x + space2 + innerWidth2,
				(int) rectSchwierigkeit.y );
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x + space2 * 2 + innerWidth2,
				(int) rectSchwierigkeit.y + innerHeight2 );
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x + space2 + innerWidth2,
				(int) rectSchwierigkeit.y + innerHeight2 * 2 );
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x + space2, (int) rectSchwierigkeit.y
				+ innerHeight2 * 2 );
		polySchweriegkeit.addPoint( (int) rectSchwierigkeit.x, (int) rectSchwierigkeit.y
				+ innerHeight2 );

		// Kasten anpassen, auf den Bereich in den der Text rein darf.
		rectFrage.x = rectFrage.x + space1;
		// rectFrage.y = rectFrage.y;
		rectFrage.width = rectFrage.width - space1 * 2;
		// rectFrage.height = rectFrage.height;

		rectSchwierigkeit.x = rectSchwierigkeit.x + space2;
		// rectSchwierigkeit.y = rectSchwierigkeit.y;
		rectSchwierigkeit.width = rectSchwierigkeit.width - space2 * 2;
		// rectSchwierigkeit.height = rectSchwierigkeit.height;

	}

	@Override
	public void doLogic(long delta) {
	// Wir haben keine Animation hier
	}

	@Override
	public void drawObjects(Graphics g) {
		super.drawObjects( g );

		if ( frage != null ) {

			Graphics2D g2 = (Graphics2D) g;
			int randDicke = 4;

			// Sichern von Farbe und Schriftart
			Color sichColor = g.getColor();
			Font sichFont = g.getFont();
			Stroke sichStroke = g2.getStroke();

			// Damit es nicht so eckig ausschaut :-)
			g2
					.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON );
			g2
					.setStroke( new BasicStroke( randDicke, BasicStroke.CAP_ROUND,
							BasicStroke.JOIN_MITER ) );

			if ( this.frage.getFragestellung() != null ) {
				g2.setColor( backgroundColor );
				g2.fillPolygon( this.polyFrage );

				g2.setColor( lineColor );
				g2.drawPolygon( this.polyFrage );

				// Hier muss dann der Text eingezeichnet werden
				// ------------- AUSGABE DER FRAGE -------------
				int offsetFrageX = (int) 0;
				int offsetFrageY = (int) 0;
				int breiteZurVerfuegungFrage = (int) this.rectFrage.width;
				int zeilenOffset = 1; // Anzahl der Zeilen die uebersprungen
				// werden
				// sollen (Maximal drei Zeilen Zeichnen)

				Vector<String> blub = wrapText( frage.getFragestellung(), breiteZurVerfuegungFrage,
						g.getFontMetrics() );
				g2.setColor( Color.white );
				g2.setFont( new Font( "Arial", Font.BOLD, 20 ) ); // Später soll
				// er die
				// Schriftgröße
				// selbst
				// bestimmen.

				// ZeilenOffset berechnen (zum zentrieren in Y-Richtung)
				if ( blub.size() <= 2 ) {
					zeilenOffset = 3;
				}

				for (int i = 0; i < blub.size(); i++) {

					// Den String jetzt zentrieren

					int offsetNachZentrierung = (breiteZurVerfuegungFrage - g2.getFontMetrics()
							.stringWidth( blub.get( i ) ))
							/ 2 + offsetFrageX;

					g2.drawString( blub.get( i ), (int) rectFrage.x + offsetNachZentrierung,
							(int) rectFrage.y
									+ offsetFrageY
									+ (i + zeilenOffset)
									* (g2.getFontMetrics().getHeight() + g2.getFontMetrics()
											.getLeading()) );

				}

			}
			if ( this.frage.getSchwierigkeitsGradInMio() != null ) {
				g2.setColor( backgroundColor );
				g2.fillPolygon( this.polySchweriegkeit );

				g2.setColor( lineColor );
				g2.drawPolygon( this.polySchweriegkeit );

				// ------------- AUSGABE DES SCHWIERIGKEITSGRADES -------------
				g2.setFont( new Font( "Arial", Font.PLAIN, 25 ) );

				int offsetSchwierigkeitsgradX = (int) 0;
				int offsetSchwierigkeitsgradY = (int) 0;

				int offsetNachZentrierungSGX = (int) ((rectSchwierigkeit.width - g2
						.getFontMetrics().stringWidth( frage.getSchwierigkeitsGradInMio() )) / 2 + offsetSchwierigkeitsgradX);
				int offsetNachZentrierungSGY = (int) ((rectSchwierigkeit.height
						- g2.getFontMetrics().getHeight() - g2.getFontMetrics().getDescent()) / 2);

				g2.drawString( frage.getSchwierigkeitsGradInMio(), (int) rectSchwierigkeit.x
						+ offsetNachZentrierungSGX, (int) rectSchwierigkeit.y + 2
						* offsetNachZentrierungSGY );
			}

			// Wiederherstellen der ursprünglichen Einstellungen
			g.setFont( sichFont );
			g.setColor( sichColor );
			g2.setStroke( sichStroke );
		}
	}

	public void setFrage(Quizfrage frage) {
		this.frage = frage;
	}

}
