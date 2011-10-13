package importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import main.Biblionaer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import quiz.Quizfrage;
import quiz.Spiel;

public class XmlToSpiel {

	private Spiel meinSpiel;
	private int anzahlFragen;

	// Redundante abspeicherung des ganzen als Dokument
	private Document doc = null;

	/**
	 * Verwende das StandardSpiel, immer das Selbe
	 */
	public XmlToSpiel() {
		String spielPfad = "src/lokaleSpiele/Test.bqxml";

		this.meinSpiel = null;
		this.anzahlFragen = 0;
		doc = null;
		SAXBuilder builder = new SAXBuilder();

		InputStream in;
		try {
			in = new FileInputStream(spielPfad);
			doc = builder.build(in);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.documentToSpiel();
		}

	}

	public XmlToSpiel(File pPathToXMLFile) {
		this.meinSpiel = null;
		this.anzahlFragen = 0;
		doc = null;
		SAXBuilder builder = new SAXBuilder();

		try {
			doc = builder.build(pPathToXMLFile);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.documentToSpiel();
		}

	}

	public XmlToSpiel(URL pLinkToXmlFile) {

		this.meinSpiel = null;
		this.anzahlFragen = 0;

		SAXBuilder builder = new SAXBuilder();
		doc = null;

		// document derBuilder = new SAXBuilder().build( "mein.xml" );
		try {
			// InputStream in = pLinkToXmlFile.openStream();
			doc = builder.build(pLinkToXmlFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JDOMParseException e1) {
			Biblionaer.meineKonsole.println("Die empfangene Quiz-Datei ist kein valides XML-File!");
		} catch (JDOMException e2) {
			e2.printStackTrace();
		} finally {
			this.documentToSpiel();
		}
	}

	private void documentToSpiel() {
		// initialisieren

		Element root = null;

		if (doc != null) {
			root = doc.getRootElement();

			// Anzahl der Fragenelemente zaehlen
			List<Element> fragen = root.getChildren("Frage");
			this.anzahlFragen = fragen.size();

			if (this.anzahlFragen > 0) {
				this.meinSpiel = new Spiel(this.anzahlFragen);
				// System.out.println("Es gibt " + anzahlFragen + " Fragen.");

				// Alle XML-Fragen in das Quizfragenobjekt konvertieren
				int i = 0;
				
				for (java.util.Iterator<Element> dieFrage = fragen.iterator(); dieFrage.hasNext();) {
					meinSpiel.setFrage(i++, xmlFrageToQuizfrage(dieFrage.next()));
				}
			} else {
				// Wenn ich keine Fragen zum importieren habe, dann erstelle
				// erst gar kein neues Spiel
				this.meinSpiel = null;
			}
		}
	}

	protected Quizfrage xmlFrageToQuizfrage(Element pXmlFrage) {
		Quizfrage quizfrage = new Quizfrage();

		quizfrage.setFragestellung(pXmlFrage.getChild("Fragestellung").getValue());
		quizfrage.setAntwort1(pXmlFrage.getChild("Antwort1").getValue());
		quizfrage.setAntwort2(pXmlFrage.getChild("Antwort2").getValue());
		quizfrage.setAntwort3(pXmlFrage.getChild("Antwort3").getValue());
		quizfrage.setAntwort4(pXmlFrage.getChild("Antwort4").getValue());
		quizfrage.setLoesungshinweis(pXmlFrage.getChild("Bibelstelle").getValue());
		quizfrage.setRichtigeAntwort(Integer.valueOf(pXmlFrage.getChild("richtigeAntwort").getValue()));
		quizfrage.setSchwierigkeitsGrad(Integer.valueOf(pXmlFrage.getChild("SchwierigkeitZahl").getValue()));
		quizfrage.setId(Long.valueOf(pXmlFrage.getChild("ID").getValue()));

		return quizfrage;
	}

	public int getAnzahlFragen() {
		return this.anzahlFragen;
	}

	public Spiel getSpiel() {
		return meinSpiel;
	}

	public void saveSpielToFile(File path) throws IOException {
		XMLOutputter out = new XMLOutputter();
		out.output(doc, new FileOutputStream(path));
	}

}
