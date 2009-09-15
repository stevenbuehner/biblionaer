package windowElements;

import java.awt.Color;
import java.awt.Font;

public class QuizStatusTextPanel extends QuizTextPanel {

	public QuizStatusTextPanel(int xPos, int yPos, int width) {
		super( null, xPos, yPos, width );

		this.setSchreibFarbe( Color.red );
		this.setSchreibFont( new Font( "Arial", Font.PLAIN, 20 ) );

		this.textAusrichtung = Font.CENTER_BASELINE;
	}

}
