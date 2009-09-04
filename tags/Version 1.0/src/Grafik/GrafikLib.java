package Grafik;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class GrafikLib {

	static GrafikLib							instance;
	private static GraphicsEnvironment			ge;
	private static GraphicsConfiguration		gc;
	private static HashMap<URL, BufferedImage>	sprites;

	private GrafikLib() {
		// Initialisieren der Lib

		// GraphicsConfiguration fuer VolatileImage
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

		sprites = new HashMap<URL, BufferedImage>();
	}

	public static GrafikLib getInstance() {
		if ( instance == null ) {
			instance = new GrafikLib();
		}
		return instance;
	}

	public BufferedImage getSprite(String path) {
		URL location = getURLfromRessource( path );
		return getSprite( location );
	}

	public BufferedImage getSprite(URL location) {

		BufferedImage pic = null;

		pic = sprites.get( location );

		if ( pic != null ) {
			return pic;
		}

		try {
			pic = ImageIO.read( location ); // ueber ImageIO die GIF lesen
		}
		catch (IOException e1) {
			System.out.println( "Fehler beim Image laden: " + e1 );
			return null;
		}

		BufferedImage better = gc.createCompatibleImage( pic.getWidth(), pic.getHeight(),
				Transparency.BITMASK );
		Graphics g = better.getGraphics();
		g.drawImage( pic, 0, 0, null );

		sprites.put( location, better );

		return better;
	}

	public BufferedImage[] getSprite(String path, int column, int row) {
		URL location = getURLfromRessource( path );

		return getSprite( location, column, row );
	}

	public BufferedImage[] getSprite(URL location, int column, int row) {

		BufferedImage source = null;

		source = sprites.get( location );

		if ( source == null ) {
			try {
				source = ImageIO.read( location ); // Über ImageIO die GIF lesen
			}
			catch (IOException e1) {
				System.out.println( e1 );
				return null;
			}

			sprites.put( location, source );

		}

		BufferedImage better = gc.createCompatibleImage( source.getWidth(), source.getHeight(),
				Transparency.BITMASK );
		Graphics g = better.getGraphics();
		g.drawImage( source, 0, 0, null );

		int width = source.getWidth() / column;
		int height = source.getHeight() / row;

		BufferedImage[] pics = new BufferedImage[column * row];
		int count = 0;

		for (int n = 0; n < row; n++) {
			for (int i = 0; i < column; i++) {
				pics[count] = source.getSubimage( i * width, n * height, width, height );
				count++;
			}
		}

		return pics;

	}

	public URL getURLfromRessource(String path) {
		return getClass().getClassLoader().getResource( path );
	}

}
