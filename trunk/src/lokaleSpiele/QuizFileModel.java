package lokaleSpiele;

import importer.XmlToSpiel;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

import main.Biblionaer;

// TODO Beim erstmaligen Laden des Spieles unter Windows (ohne gespeicherte
// Spiele) sind die heruntergeladenen Spiele nicht anklickbar / auswählbar
public class QuizFileModel extends AbstractTableModel {

	private static final long	serialVersionUID		= -7040042367775652371L;
	public static String		speicherOrtFuerSpiele	= "Biblionaer";
	public static String		dateiEndungFuerSpiele	= ".bqxml";

	private String				spalten[]				= new String[] { "Name", "Datum" };

	private Class<?>			types[]					= new Class[] { String.class, String.class };

	private Object				data[][];
	private File				dateiPfad[];
	private boolean				editable[];

	public QuizFileModel() {
		// System herausfinden und falls noch nicht vorhanden den Ordner anlegen

		setFileStats( getSpeicherortHomeSpiele() );

	}

	public static File getSpeicherortHomeSpiele() {
		String subDir = null;
		File homeDir = FileSystemView.getFileSystemView().getHomeDirectory();

		if ( isMac() ) {
			subDir = "Library/Application Support/" + QuizFileModel.speicherOrtFuerSpiele;
		}

		else if ( isWindows() ) {
			homeDir = new File( System.getProperty( "user.home" ) );
			subDir = "Application Data/" + QuizFileModel.speicherOrtFuerSpiele;

		}
		else if ( isUnix() ) {
			subDir = "." + QuizFileModel.speicherOrtFuerSpiele;
		}
		else {
			subDir = QuizFileModel.speicherOrtFuerSpiele;
		}

		File dir = new File( homeDir, subDir );
		if ( dir.exists() ) {
			if ( dir.isDirectory() ) {
				Biblionaer.meineKonsole.println( "Quiz Home-Dir ist: '" + dir.getAbsolutePath() + "'", 4 );
			}
		}
		else {
			// Dann muss es wohl noch erstellt werden
			if ( dir.mkdirs() ) {
				Biblionaer.meineKonsole.println( "Verzeichnis zur Quizablage wurde erstellt.", 3 );
			}
			else {
				Biblionaer.meineKonsole.println( "Verzeichnis zur Quizablage konte nicht erstellt werden.", 2 );
			}
		}

		return dir;
	}

	public static File getSpeicherortLokaleSpiel() {
		File dir = new File( "src/lokaleSpiele" );
		return dir;
	}

	// Implement the methods of the TableModel interface we're interested
	// in. Only getRowCount(), getColumnCount() and getValueAt() are
	// required. The other methods tailor the look of the table.
	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return spalten.length;
	}

	public String getColumnName(int index) {
		return spalten[index];
	}

	public Class<?> getColumnClass(int index) {
		return types[index];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int colIndex) {

		if ( colIndex == 0 && rowIndex >= 0 && this.editable.length > rowIndex ) {
			return this.editable[rowIndex];
		}

		return false;
	}

	public boolean isRowDeletable(int rowIndex) {
		return this.isCellEditable( rowIndex, 0 );
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex) {
		Biblionaer.meineKonsole.println(
				"Neuer Wert für '" + this.getValueAt( rowIndex, colIndex ) + "' ist '" + value.toString() + "'", 3 );
		this.renameQuizFile( rowIndex, value.toString() );
	}

	/**
	 * Gibt abhängig zur übergebenen Zeile, den Pfad zurück, wo dieses File
	 * gespeichert ist.
	 * 
	 * @param row
	 * @return PfadZurDatei
	 */
	public File getQuizFileLocationAt(int row) {
		return (File) dateiPfad[row];

	}

	// Our own method for setting/changing the current directory
	// being displayed. This method fills the data set with file info
	// from the given directory. It also fires an update event so this
	// method could also be called after the table is on display.
	public void setFileStats(File dir) {

		String filesHomeDir[] = dir.list( new FilenameFilter() {
			public boolean accept(File f, String s) {
				return new File( f, s ).isFile() && s.toLowerCase().endsWith( QuizFileModel.dateiEndungFuerSpiele );
			}
		} );

		File filesLokalDir[] = getSpeicherortLokaleSpiel().listFiles( new FilenameFilter() {
			public boolean accept(File f, String s) {
				return new File( f, s ).isFile() && s.toLowerCase().endsWith( QuizFileModel.dateiEndungFuerSpiele );
			}
		} );

		int anzahlDateien = 0;
		anzahlDateien += (filesHomeDir != null) ? filesHomeDir.length : 0;
		anzahlDateien += (filesLokalDir != null) ? filesLokalDir.length : 0;

		this.data = new Object[anzahlDateien][this.spalten.length];
		this.dateiPfad = new File[anzahlDateien];
		this.editable = new boolean[anzahlDateien];
		File rootPfadToHomeDir = getSpeicherortHomeSpiele();
		Date tempDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yy hh:mm" );

		// Für alle Spiele die auf der Festplatte im Home-Directory gespeichert
		// sind
		int i = 0;
		if ( filesHomeDir != null ) {
			for (i = 0; i < filesHomeDir.length; i++) {

				// Dateipfad
				this.dateiPfad[i] = new File( rootPfadToHomeDir, filesHomeDir[i] );

				// Dateiname
				this.data[i][0] = filesHomeDir[i].substring( 0, filesHomeDir[i].length() - 6 );

				// Letztes Änderungsdatum
				tempDate.setTime( dateiPfad[i].lastModified() );
				this.data[i][1] = dateFormat.format( tempDate ) + " Uhr";

				this.editable[i] = true;

			}
		}

		// Für alle Spiele die mit der Software als Bundle ausgeliefert werden
		if ( filesLokalDir != null ) {
			for (int j = 0; j < filesLokalDir.length; j++) {

				// Dateipfad
				this.dateiPfad[i] = filesLokalDir[j];

				// Dateiname
				this.data[i][0] = filesLokalDir[j].getName().substring( 0, filesLokalDir[j].getName().length() - 6 );

				// Letztes Änderungsdatum
				tempDate.setTime( filesLokalDir[j].lastModified() );
				this.data[i][1] = " - ";

				this.editable[i] = false;
				i++;
			}
		}

		// Just in case anyone's listening...
		fireTableDataChanged();
	}

	public boolean addQuizFile(XmlToSpiel xmlImporterFile) {

		try {
			XmlToSpiel dasXMLImporterFile = xmlImporterFile;

			// Finde den nächsten freien Speichernamen
			int i = 0;
			File saveTo = null;
			while (saveTo == null && i < 50) {
				i++;
				saveTo = new File( getSpeicherortHomeSpiele().getAbsolutePath() + "/neuesSpiel_" + Integer.toString( i )
						+ ".bqxml" );

				if ( saveTo.exists() ) {
					saveTo = null;
				}
			}

			if ( i >= 50 ) {
				Biblionaer.meineKonsole.println( "Es wurde nach " + Integer.toString( i )
						+ " versuchen abgebrochen, das Spiel zu speichern.", 2 );
			}
			else {
				if ( dasXMLImporterFile.getAnzahlFragen() > 0 ) {
					dasXMLImporterFile.saveSpielToFile( saveTo );
					Biblionaer.meineKonsole.println( "Es wurde noch ein neues Spiel angelegt.", 3 );
				}
				else {
					Biblionaer.meineKonsole.println(
							"Es wurde kein neues Spiel importiert, weil nur "
									+ Integer.toString( dasXMLImporterFile.getAnzahlFragen() )
									+ " Fragen heruntergeladen wurden.", 2 );
				}
			}
		}
		catch (MalformedURLException e) {
			Biblionaer.meineKonsole.println(
					"Beim Versuch ein neues Spiel herunterzuladen (im AdministratorSchirm), ist die falsche URL verwendet worden.\n"
							+ e.getMessage(), 1 );
			return false;
		}
		catch (IOException e2) {
			Biblionaer.meineKonsole.println(
					"Es trat ein Fehler beim speichern eines heruntergeladenen Spieles (im AdministratorSchirm) auf:\n"
							+ e2.getMessage(), 1 );
			return false;

		}
		finally {
			this.refreshInhalte();
		}

		return true;
	}

	public boolean removeQuizFile(int row) {

		if ( !this.isRowDeletable( row ) ) {
			// Datei gehört vermutlich zu den lokalen Spielen (sollen nicht
			// gelöscht werden)
			Biblionaer.meineKonsole.println( "Datei wurde nicht gelöscht: Es besteht keine Berechtigung dazu!", 2 );
			return false;
		}

		if ( row <= dateiPfad.length ) {
			if ( dateiPfad[row].exists() ) {
				dateiPfad[row].delete();
				if ( dateiPfad[row].exists() ) {
					Biblionaer.meineKonsole.println( "Die Datei '" + dateiPfad[row].getName()
							+ "' wurde NICHT gelöscht.", 4 );
					return false;
				}
				else {
					this.refreshInhalte();
					Biblionaer.meineKonsole.println( "Die Datei '" + dateiPfad[row].getName() + "' wurde gelöscht.", 4 );
					return true;
				}
			}
		}
		Biblionaer.meineKonsole.println( "Es wurde nichts gelöscht: Die zu löchende Datei existiert nicht!", 4 );
		return false;
	}

	/**
	 * Bennent die Datei in der angegebenen Reihe um zu neuerName
	 * 
	 * @param row
	 * @param neuerName
	 */
	protected void renameQuizFile(int row, String neuerName) {
		if ( row <= dateiPfad.length && dateiPfad[row].exists() ) {
			if ( neuerName != null && neuerName.length() > 0 ) {

				File neuerDateiname = new File( dateiPfad[row].getParent(), neuerName
						+ QuizFileModel.dateiEndungFuerSpiele );

				if ( neuerDateiname.equals( dateiPfad[row] ) ) {
					Biblionaer.meineKonsole.println( "Umbennen nicht nötig: Gleicher Dateiname", 4 );
					return;
				}

				if ( !neuerDateiname.exists() ) {
					dateiPfad[row].renameTo( neuerDateiname );
					this.refreshInhalte();
				}
				else {
					Biblionaer.meineKonsole.println( "Umbennen nicht möglich: Die Datei existiert bereits!", 2 );
				}
			}
			else {
				Biblionaer.meineKonsole
						.println(
								"Umbennen nicht möglich: Der Dateiname darf nicht null seind und muss mindestens 1 Zeichen lang sein!",
								2 );
			}
		}
		else {
			Biblionaer.meineKonsole.println( "Umbennen nicht möglich: Diese Reihe existiert nicht in der Tabelle", 2 );
		}
	}

	public void refreshInhalte() {
		setFileStats( getSpeicherortHomeSpiele() );
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
