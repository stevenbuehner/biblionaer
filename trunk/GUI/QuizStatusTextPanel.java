package GUI;

import java.awt.Color;
import java.awt.Font;

public class QuizStatusTextPanel extends QuizTextPanel {

	protected QuizStatusTextPanel(int xPos, int yPos, int width) {
		super( xPos, yPos, width );

		this.setSchreibFarbe( Color.red );
		this.setSchreibFont( new Font( "Arial", Font.PLAIN, 20 ) );

		this.textAusrichtung = Font.CENTER_BASELINE;
	}

}
