package windowElements;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VollbildOberflaeche extends JFrame implements ActionListener {

	private static final long	serialVersionUID	= 1L;
	private GraphicsDevice		device;
	private DisplayMode			originalDM;
	private JButton				exit				= new JButton( "Exit" );
	JButton						swip				= new JButton( "swip" );
	private boolean				isFullScreen		= false;

	public VollbildOberflaeche(GraphicsDevice pDevice) {
		super( pDevice.getDefaultConfiguration() );
		this.device = pDevice;
		setTitle( "Wer wird Biblionär - Vollbildmodus" );
		originalDM = device.getDisplayMode();

		setDefaultCloseOperation( EXIT_ON_CLOSE );
		exit.addActionListener( this );

		initComponents( this.getContentPane() );
	}

	private void initComponents(Container c) {
		setContentPane( c );
		c.setLayout( new BorderLayout() );

		JPanel currentPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		c.add( currentPanel, BorderLayout.NORTH );

		// Exit
		JPanel exitPanel = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		currentPanel.add( exitPanel );
		exitPanel.add( exit );

		this.swip.addActionListener( this );
		exitPanel.add( this.swip );

	}

	public boolean getIsFullScreen() {
		return isFullScreen;
	}

	/**
	 * @param pFullScreen
	 * @return success
	 */
	public boolean setFullScreen(boolean pFullScreen) {

		/*
		 * Wenn pFullScreen == true, dann versuche wenn erlaubt auf
		 * Fullscreen-Modus zu gehen.
		 */
		if ( pFullScreen ) {
			isFullScreen = device.isFullScreenSupported();
			if ( isFullScreen ) {
				// Full-screen mode
				setUndecorated( true );
				setResizable( false );
				device.setFullScreenWindow( this );
				validate();
				return true;
			}
			else {
				return false;
			}

		}

		/*
		 * Wenn pFullScreen == false oder Fullscreen-Modus nicht Supported ist,
		 * dann gehe in den Window-Modus
		 */
		if ( isFullScreen == false || pFullScreen == false ) {
			// Windowed mode
			isFullScreen = false;
			setResizable( true );
			device.setFullScreenWindow( null );
			validate();
			pack();
			setLocationRelativeTo( null ); // zentrieren
			setVisible( true );
		}
		return true;

	}

	public void actionPerformed(ActionEvent ev) {

		// Wieder die alte Aufloesung einstellen ...
		Object source = ev.getSource();
		if ( source == exit ) {
			try {
				device.setDisplayMode( originalDM );
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			System.exit( 0 ); // Exit muss immer gehen, auch wenn display-Mode
			// Fehlschlaegt
		}
		if ( source == this.swip ) {
			this.setFullScreen( !isFullScreen );
		}
	}

	public static void main(String[] args) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		// REMIND : Multi-monitor full-screen mode not yet supported
		for (int i = 0; i < 1 /* devices.length */; i++) {
			VollbildOberflaeche test = new VollbildOberflaeche( devices[i] );
			test.setFullScreen( true );
		}
	}

}
