/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.export.exporter.nagel;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fattazzo
 * 
 */
public class NagelAreaRendicontazioneBeanExporter {

	private AreaMagazzino areaMagazzino;

	private String note1 = "";
	private String note2 = "";
	private String note3 = "";

	/**
	 * Costruttore.
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 */
	public NagelAreaRendicontazioneBeanExporter(final AreaMagazzino areaMagazzino) {
		super();
		this.areaMagazzino = areaMagazzino;

		String noteTestata = StringUtils.defaultString(areaMagazzino.getAreaMagazzinoNote().getNoteTestata());

		String[] subs = noteTestata.split("(?<=\\G.{50})");

		note1 = subs.length > 0 ? subs[0] : "";
		note2 = subs.length > 1 ? subs[1] : "";
		note3 = subs.length > 2 ? subs[2] : "";
	}

	/**
	 * @return the areaMagazzino
	 */
	public AreaMagazzino getAreaMagazzino() {
		return areaMagazzino;
	}

	/**
	 * @return the cap
	 */
	public String getCap() {
		String result = "";

		if (areaMagazzino.getDocumento().getSedeEntita() != null
				&& areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCap() != null) {
			result = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCap()
					.getDescrizione();
		}

		return result;
	}

	/**
	 * @return the destinatario
	 */
	public String getDestinatario() {
		String result = "";

		if (areaMagazzino.getDocumento().getEntita() != null) {
			result = areaMagazzino.getDocumento().getEntita().getAnagrafica().getDenominazione();
		}

		return result;
	}

	/**
	 * @return the indirizzo
	 */
	public String getIndirizzo() {
		String result = "";

		if (areaMagazzino.getDocumento().getSedeEntita() != null) {
			result = areaMagazzino.getDocumento().getSedeEntita().getSede().getIndirizzo();
		}

		return result;
	}

	/**
	 * @return the localita
	 */
	public String getLocalita() {
		String result = "";

		if (areaMagazzino.getDocumento().getSedeEntita() != null
				&& areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getLocalita() != null) {
			result = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getLocalita()
					.getDescrizione();
		}

		return result;
	}

	/**
	 * @return the nazione
	 */
	public String getNazione() {
		String result = "";

		if (areaMagazzino.getDocumento().getSedeEntita() != null) {
			result = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCodiceNazione();
		}

		return result;
	}

	/**
	 * @return the note1
	 */
	public String getNote1() {
		return note1;
	}

	/**
	 * @return the note2
	 */
	public String getNote2() {
		return note2;
	}

	/**
	 * @return the note3
	 */
	public String getNote3() {
		return note3;
	}

	/**
	 * @param areaMagazzino
	 *            the areaMagazzino to set
	 */
	public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
		this.areaMagazzino = areaMagazzino;
	}

	/**
	 * @param cap
	 *            the cap to set
	 */
	public void setCap(String cap) {
	}

	/**
	 * @param destinatario
	 *            the destinatario to set
	 */
	public void setDestinatario(String destinatario) {
	}

	/**
	 * @param indirizzo
	 *            the indirizzo to set
	 */
	public void setIndirizzo(String indirizzo) {
	}

	/**
	 * @param localita
	 *            the localita to set
	 */
	public void setLocalita(String localita) {
	}

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(String nazione) {
	}

	/**
	 * @param note1
	 *            the note1 to set
	 */
	public void setNote1(String note1) {
	}

	/**
	 * @param note2
	 *            the note2 to set
	 */
	public void setNote2(String note2) {
	}

	/**
	 * @param note3
	 *            the note3 to set
	 */
	public void setNote3(String note3) {
	}
}
