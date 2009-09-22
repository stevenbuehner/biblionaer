package lokaleSpiele;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

import main.Biblionaer;

public class QuizFileModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID		= -7040042367775652371L;
	private String				speicherOrtFuerSpiele	= "Biblionaer";

	String						titles[]				= new String[] { "Quizname", "Laufzeit" };

	Class						types[]					= new Class[] { String.class, String.class };

	Object						data[][];

	public QuizFileModel() {
		// System herausfinden und falls noch nicht vorhanden den Ordner anlegen
		this( FileSystemView.getFileSystemView().getHomeDirectory() );
	}

	public QuizFileModel(String dir) {
		// System herausfinden und falls noch nicht vorhanden den Ordner anlegen
		this( new File( dir ) );
	}

	public QuizFileModel(File pwd) {
		// System herausfinden und falls noch nicht vorhanden den Ordner anlegen

		String subDir = null;

		if ( isMac() ) {
			subDir = "Library/Application Support/" + this.speicherOrtFuerSpiele;
		}

		else if ( isWindows() ) {
			subDir = "Application Data/" + this.speicherOrtFuerSpiele;

		}
		else if ( isUnix() ) {
			subDir = "." + this.speicherOrtFuerSpiele;
		}
		else {
			subDir = this.speicherOrtFuerSpiele;
		}

		File dir = new File( pwd, subDir );
		if ( dir.exists() ) {
			if ( dir.isDirectory() ) {
				Biblionaer.meineKonsole.println( "Quiz Home-Dir ist: '" + dir.getAbsolutePath()
						+ "'", 4 );
			}
		}
		else {
			// Dann muss es wohl noch erstellt werden
			if ( dir.mkdirs() ) {
				Biblionaer.meineKonsole.println( "Verzeichnis zur Quizablage wurde erstellt.", 3 );
			}
			else {
				Biblionaer.meineKonsole.println(
						"Verzeichnis zur Quizablage konte nicht erstellt werden.", 2 );

			}
		}

		setFileStats( dir );

	}

	// Implement the methods of the TableModel interface we're interested
	// in. Only getRowCount(), getColumnCount() and getValueAt() are
	// required. The other methods tailor the look of the table.
	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return titles.length;
	}

	public String getColumnName(int c) {
		return titles[c];
	}

	public Class getColumnClass(int c) {
		return types[c];
	}

	public Object getValueAt(int r, int c) {
		return data[r][c];
	}

	// Our own method for setting/changing the current directory
	// being displayed. This method fills the data set with file info
	// from the given directory. It also fires an update event so this
	// method could also be called after the table is on display.
	public void setFileStats(File dir) {

		String files[] = dir.list( new FilenameFilter() {
			public boolean accept(File f, String s) {
				return new File( f, s ).isFile() && s.toLowerCase().endsWith( ".bqxml" );
			}
		} );

		data = new Object[files.length][titles.length];

		for (int i = 0; i < files.length; i++) {
			File tmp = new File( files[i] );
			data[i][0] = files[i];
			data[i][1] = "unused";
		}

		// Just in case anyone's listening...
		fireTableDataChanged();
	}

	public static boolean isWindows() {

		String os = System.getProperty( "os.name" ).toLowerCase();
		// windows
		return (os.indexOf( "win" ) >= 0);

	}

	public static boolean isMac() {

		String os = System.getProperty( "os.name" ).toLowerCase();
		// Mac
		return (os.indexOf( "mac" ) >= 0);

	}

	public static boolean isUnix() {

		String os = System.getProperty( "os.name" ).toLowerCase();
		// linux or unix
		return (os.indexOf( "nix" ) >= 0 || os.indexOf( "nux" ) >= 0);

	}
}
