package Anwendung;

/*
 * Data Transfer Object
 * 
 */
public class LineDTO {

	private String oz;
	private String kurztext;
	private String menge;
	private String einheit;


	public LineDTO(String oz, String kurztext, String menge, String einheit) {
		super();
		this.oz = oz;
		this.kurztext = kurztext;
		this.menge = menge;
		this.einheit = einheit;

	}

	@Override
	public String toString() {
		return oz + " " + kurztext + " " + menge + " " + einheit;
	}

	public String getOz() {
		return oz;
	}

	public void setOz(String oz) {
		this.oz = oz;
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

}
