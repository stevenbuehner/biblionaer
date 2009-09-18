package interfaces;

import java.awt.image.VolatileImage;

public interface BackendWindow {

	/**
	 * Das FrontendWindow schickt einen Screenshot des eigenen Frames an das
	 * AdminPanel, damit dieser es darstellen kann.
	 */
	public void setFrontendScreenImage(VolatileImage screen);
}
