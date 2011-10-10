package timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

import javax.swing.JLabel;

public class UhrzeitSekUpdate {

	private JLabel JLabelToUpdate;
	private Timer derTimer;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public UhrzeitSekUpdate(JLabel JLabelToUpdate) {
		this.JLabelToUpdate = JLabelToUpdate;
		this.derTimer = new Timer(1000, new UhrzeitSekUpdateListener());
		derTimer.setRepeats(true); // Standard
	}

	public void updateJLabel() {
		this.JLabelToUpdate.setText("Uhrzeit: " + this.dateFormat.format(new Date()) + " Uhr");
	}

	public void starteZeitgeber() {
		if (!derTimer.isRunning()) {
			derTimer.start();
		}
	}

	public void stoppeZeitgeber() {
		if (derTimer.isRunning()) {
			derTimer.stop();
		}
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	class UhrzeitSekUpdateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			updateJLabel();
		}
	}

}
