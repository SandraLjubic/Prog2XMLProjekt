package Anwendung;

/*
 * Data Transfer Object
 */
public class LineDTO {

	private String oz;
	private String posArt;
	private String kurztext;
	private String menge;
	private String einheit;
	private String loehne;
	private String stoffe;
	private String betriebskosten;
	private String geraetekosten;
	private String sonstiges;
	private String nachunternehmer;
	private String ep;
	private String gp;

	public LineDTO(String oz, String posArt, String kurztext, String menge, String einheit, String loehne,
			String stoffe, String betriebskosten, String geraetekosten, String sonstiges, String nachunternehmer,
			String ep, String gp) {
		super();
		this.oz = oz;
		this.posArt = posArt;
		this.kurztext = kurztext;
		this.menge = menge;
		this.einheit = einheit;
		this.loehne = loehne;
		this.stoffe = stoffe;
		this.betriebskosten = betriebskosten;
		this.geraetekosten = geraetekosten;
		this.sonstiges = sonstiges;
		this.nachunternehmer = nachunternehmer;
		this.ep = ep;
		this.gp = gp;
	}

	@Override
	public String toString() {
		return oz + " | " + posArt + " | " + kurztext + " | " + menge + " | " + einheit;
	}

	public String getOz() {
		return oz;
	}

	public void setOz(String oz) {
		this.oz = oz;
	}

	public String getPosArt() {
		return posArt;
	}

	public void setPosArt(String posArt) {
		this.posArt = posArt;
	}

	public String getKurztext() {
		return kurztext;
	}

	public void setKurztext(String kurztext) {
		this.kurztext = kurztext;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getEinheit() {
		return einheit;
	}

	public void setEinheit(String einheit) {
		this.einheit = einheit;
	}

	public String getLoehne() {
		return loehne;
	}

	public void setLoehne(String loehne) {
		this.loehne = loehne;
	}

	public String getStoffe() {
		return stoffe;
	}

	public void setStoffe(String stoffe) {
		this.stoffe = stoffe;
	}

	public String getBetriebskosten() {
		return betriebskosten;
	}

	public void setBetriebskosten(String betriebskosten) {
		this.betriebskosten = betriebskosten;
	}

	public String getGeraetekosten() {
		return geraetekosten;
	}

	public void setGeraetekosten(String geraetekosten) {
		this.geraetekosten = geraetekosten;
	}

	public String getSonstiges() {
		return sonstiges;
	}

	public void setSonstiges(String sonstiges) {
		this.sonstiges = sonstiges;
	}

	public String getNachunternehmer() {
		return nachunternehmer;
	}

	public void setNachunternehmer(String nachunternehmer) {
		this.nachunternehmer = nachunternehmer;
	}

	public String getEp() {
		return ep;
	}

	public void setEp(String ep) {
		this.ep = ep;
	}

	public String getGp() {
		return gp;
	}

	public void setGp(String gp) {
		this.gp = gp;
	}

}
