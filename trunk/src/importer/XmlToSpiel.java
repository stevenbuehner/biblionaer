package importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import quiz.Quizfrage;
import quiz.Spiel;

public class XmlToSpiel {

	private Spiel	meinSpiel;
	private int		anzahlFragen;

	/**
	 * Verwende das StandardSpiel, immer das Selbe
	 */
	public XmlToSpiel() {
		String spielPfad = "src/importer/quiz.bqxml";

		this.meinSpiel = null;
		this.anzahlFragen = 0;
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();

		InputStream in;
		try {
			in = new FileInputStream( spielPfad );
			doc = builder.build( in );
		}
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.documentToSpiel( doc );
		}

	}

	public XmlToSpiel(File pPathToXMLFile) {
		this.meinSpiel = null;
		this.anzahlFragen = 0;
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();

		try {
			doc = builder.build( pPathToXMLFile );
		}
		catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.documentToSpiel( doc );
		}

	}

	public XmlToSpiel(URL pLinkToXmlFile) {

		this.meinSpiel = null;
		this.anzahlFragen = 0;

		SAXBuilder builder = new SAXBuilder();
		Document doc = null;

		// document derBuilder = new SAXBuilder().build( "mein.xml" );
		try {
			// InputStream in = pLinkToXmlFile.openStream();
			doc = builder.build( pLinkToXmlFile );
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (JDOMException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		finally {
			this.documentToSpiel( doc );
		}
	}

	private void documentToSpiel(Document doc) {
		// initialisieren

		Element root = null;

		if ( doc != null ) {
			root = doc.getRootElement();

			// Anzahl der Fragenelemente zaehlen
			List<Quizfrage> zumZaehlen = root.getChildren( "Frage" );

			this.anzahlFragen = zumZaehlen.size();

			if ( this.anzahlFragen > 0 ) {
				this.meinSpiel = new Spiel( this.anzahlFragen );
				// System.out.println("Es gibt " + anzahlFragen + " Fragen.");

				// Alle XML-Fragen in das Quizfragenobjekt konvertieren
				int i = 0;
				Element pXmlFrage;
				while ((pXmlFrage = root.getChild( "Frage" )) != null) {
					meinSpiel.setFrage( i++, xmlFrageToQuizfrage( pXmlFrage ) );
					root.removeChild( "Frage" );
				}
			}
			else {
				// Wenn ich keine Fragen zum importieren habe, dann erstelle
				// erst gar kein neues Spiel
				this.meinSpiel = null;
			}
		}
	}

	protected Quizfrage xmlFrageToQuizfrage(Element pXmlFrage) {
		Quizfrage quizfrage = new Quizfrage();

		quizfrage.setFragestellung( pXmlFrage.getChild( "Fragestellung" ).getValue() );
		quizfrage.setAntwort1( pXmlFrage.getChild( "Antwort1" ).getValue() );
		quizfrage.setAntwort2( pXmlFrage.getChild( "Antwort2" ).getValue() );
		quizfrage.setAntwort3( pXmlFrage.getChild( "Antwort3" ).getValue() );
		quizfrage.setAntwort4( pXmlFrage.getChild( "Antwort4" ).getValue() );
		quizfrage.setLoesungshinweis( pXmlFrage.getChild( "Bibelstelle" ).getValue() );
		quizfrage.setRichtigeAntwort( Integer.valueOf( pXmlFrage.getChild( "richtigeAntwort" )
				.getValue() ) );
		quizfrage.setSchwierigkeitsGrad( Integer.valueOf( pXmlFrage.getChild( "SchwierigkeitZahl" )
				.getValue() ) );
		quizfrage.setId( Long.valueOf( pXmlFrage.getChild( "ID" ).getValue() ) );

		return quizfrage;
	}

	public int getAnzahlFragen() {
		return this.anzahlFragen;
	}

	public Spiel getSpiel() {
		return meinSpiel;
	}

}
