package quiz;

public class Quizfrage {

	private long	id;
	private int		schwierigkeitsGrad;
	private String	fragestellung;
	private String	antwort1;
	private String	antwort2;
	private String	antwort3;
	private String	antwort4;
	private String	loesungshinweis;
	private int		richtigeAntwort;

	// Randdaten zur Frage
	private boolean	frageKomplett;
	private long	begin	= 0;
	private long	ende	= 0;

	// Konstrukturen
	public Quizfrage() {
		this( -1 );
	}

	public Quizfrage(int pId) {
		this( pId, -1, null, null, null, null, null, null, -1 );
	}

	public Quizfrage(int pId, int pSchwierigkkeitsGrad, String pFragestellung, String pAntwort1,
			String pAntwort2, String pAntwort3, String pAntwort4, String pLoesungshinweis,
			int pRichtigeAntwort) {

		this.id = pId;
		this.schwierigkeitsGrad = pSchwierigkkeitsGrad;
		this.fragestellung = pFragestellung;
		this.antwort1 = pAntwort1;
		this.antwort2 = pAntwort2;
		this.antwort3 = pAntwort3;
		this.antwort4 = pAntwort4;
		this.loesungshinweis = pLoesungshinweis;
		this.richtigeAntwort = pRichtigeAntwort;

		pruefeObFrageKomplett();

		this.setBegin( 0 );
		this.setEnde( 0 );
	}

	// Getter und Setter
	public long getId() {
		return id;
	}

	public void setId(long pId) {
		if ( pId < 1 ) {
			System.out
					.println( "Achtung, die ID zu setzende ID ist kleiner als 1. Stimmt das wirklich? Sie ist: "
							+ pId );
		}
		this.id = pId;
	}

	public void setSchwierigkeitsGrad(int pSchwierigkeitsGrad) {
		if ( pSchwierigkeitsGrad > 20 ) {
			System.out.println( "Achtung, der Schwierigkeitsgrad ist " + pSchwierigkeitsGrad
					+ " - und damit grš§er als 15!!" );
		}
		this.schwierigkeitsGrad = pSchwierigkeitsGrad;
	}

	public int getSchwierigkeitsGrad() {

		return schwierigkeitsGrad;
	}

	public String getSchwierigkeitsGradInMio() {

		String rueckgabe = "";
		String waehrungAnhang = " Û";

		switch (schwierigkeitsGrad) {
			case 1:
				rueckgabe = "50" + waehrungAnhang;
				break;
			case 2:
				rueckgabe = "100" + waehrungAnhang;
				break;
			case 3:
				rueckgabe = "200" + waehrungAnhang;
				break;
			case 4:
				rueckgabe = "300" + waehrungAnhang;
				break;
			case 5:
				rueckgabe = "500" + waehrungAnhang;
				break;
			case 6:
				rueckgabe = "1.000" + waehrungAnhang;
				break;
			case 7:
				rueckgabe = "2.000" + waehrungAnhang;
				break;
			case 8:
				rueckgabe = "4.000" + waehrungAnhang;
				break;
			case 9:
				rueckgabe = "8.000" + waehrungAnhang;
				break;
			case 10:
				rueckgabe = "16.000" + waehrungAnhang;
				break;
			case 11:
				rueckgabe = "32.000" + waehrungAnhang;
				break;
			case 12:
				rueckgabe = "64.000" + waehrungAnhang;
				break;
			case 13:
				rueckgabe = "125.000" + waehrungAnhang;
				break;
			case 14:
				rueckgabe = "500.000" + waehrungAnhang;
				break;
			case 15:
				rueckgabe = "1.000.000" + waehrungAnhang;
				break;
			default:
				rueckgabe = "unbekannt";
				break;
		}
		return rueckgabe;
	}

	public String getFragestellung() {
		return fragestellung;
	}

	public void setFragestellung(String pFragestellung) {
		this.fragestellung = pFragestellung;
	}

	public String getAntwort1() {
		return antwort1;
	}

	public void setAntwort1(String pAntwort1) {
		this.antwort1 = pAntwort1;
	}

	public String getAntwort2() {
		return antwort2;
	}

	public void setAntwort2(String pAntwort2) {
		this.antwort2 = pAntwort2;
	}

	public String getAntwort3() {
		return antwort3;
	}

	public void setAntwort3(String pAntwort3) {
		this.antwort3 = pAntwort3;
	}

	public String getAntwort4() {
		return antwort4;
	}

	public void setAntwort4(String pAntwort4) {
		this.antwort4 = pAntwort4;
	}

	public String getLoesungshinweis() {
		return loesungshinweis;
	}

	public void setLoesungshinweis(String pLoesungshinweis) {
		this.loesungshinweis = pLoesungshinweis;
	}

	public int getRichtigeAntwort() {
		return richtigeAntwort;
	}

	public void setRichtigeAntwort(int pRichtigeAntwort) {
		if ( pRichtigeAntwort < 1 || pRichtigeAntwort > 4 ) {
			System.out
					.println( "Achtung! Der Wert 'richtigeAntwort' wude au§erhalb des zulŠssigen Bereiches auf '"
							+ pRichtigeAntwort + "' gesetzt, kann das wirklich sein?" );
		}
		this.richtigeAntwort = pRichtigeAntwort;
	}

	public void setEnde() {
		this.ende = System.nanoTime();
	}

	public void setEnde(long pEnde) {
		this.ende = pEnde;
	}

	public long getEnde() {
		return ende;
	}

	public void setBegin() {
		this.begin = System.nanoTime();
	}

	public void setBegin(long pBegin) {
		this.begin = pBegin;
	}

	public long getBegin() {
		return begin;
	}

	// Objektbezogene Abfragen
	public boolean pruefeObFrageKomplett() {
		if ( this.id > 0 && this.schwierigkeitsGrad > 0 && this.fragestellung != null
				&& this.antwort1 != null && this.antwort2 != null && this.antwort3 != null
				&& this.antwort4 != null && this.loesungshinweis != null
				&& this.richtigeAntwort >= 1 && this.richtigeAntwort <= 4 ) {
			this.frageKomplett = true;
		}
		else {
			this.frageKomplett = false;
		}
		return this.frageKomplett;
	}

	/**
	 * Gibt NACH dem beenden der Frage die Dauer zurŸck. Wenn begin oder ende
	 * noch nicht gesezt sind, gibt die Funktion -1 zurŸck.
	 * 
	 * @return long zeitDifferenz
	 */
	public long frageDauerInSekunden() {
		if ( this.ende == 0 || this.begin == 0 ) {
			return -1;
		}
		else {
			return (this.ende - this.begin);
		}
	}

	/**
	 * Gibt, wenn begin gesetzt ist, die verstrichene Zeit bis zum aktuellen
	 * Zeitpunkt zurŸck Wenn begin noch nicht gesetzt ist, gibt die Funktion -1
	 * zurŸck.
	 * 
	 * @return long zeitDifferenz
	 */
	public long frageDauerBisJetztInSekunden() {
		if ( this.begin == 0 ) {
			return -1;
		}
		else {
			return (System.nanoTime() - this.begin);
		}
	}

}
