package it.eurotn.panjea.ordini.domain;

import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.RigaTestataDTO;
import it.eurotn.util.PanjeaEJBUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity(name = "RigaTestataOrdine")
@Audited
@DiscriminatorValue("T")
public class RigaTestata extends RigaOrdine {
	private static final long serialVersionUID = 3437442802077363321L;

	private Date dataAreaMagazzinoCollegata;

	private String codiceTipoDocumentoCollegato;

	private String descrizione;

	@Override
	protected RigaOrdineDTO creaIstanzaRigaOrdineDTO() {
		return new RigaTestataDTO();
	}

	/**
	 * Genera la descrizione per la riga testata in base all'area collegata.
	 * 
	 * @return descrizione generata
	 */
	public String generaDescrizioneTestata() {
		String maschera;

		maschera = areaPreventivoCollegata.getTipoAreaPreventivo().getTipoDocumentoEvasioneDescrizioneMaschera();

		// la descrizione della sede viene avvvalorata solo se la sede non è quella principale
		String descrizioneSede = "";
		if (areaPreventivoCollegata.getDocumento().getSedeEntita() != null
				&& !areaPreventivoCollegata.getDocumento().getSedeEntita().getTipoSede().isSedePrincipale()) {
			descrizioneSede = areaPreventivoCollegata.getDocumento().getSedeEntita().getSede().getDescrizione();
		}
		maschera = maschera.replace("$sede$", descrizioneSede);

		maschera = maschera
				.replace("$numeroDocumento$", areaPreventivoCollegata.getDocumento().getCodice().getCodice());
		maschera = maschera.replace("$dataDocumento$",
				new SimpleDateFormat("dd/MM/yyyy").format(areaPreventivoCollegata.getDocumento().getDataDocumento()));
		maschera = maschera.replace("$codiceTipoDocumento$", areaPreventivoCollegata.getDocumento().getTipoDocumento()
				.getCodice());
		maschera = maschera.replace("$descrizioneTipoDocumento$", areaPreventivoCollegata.getDocumento()
				.getTipoDocumento().getDescrizione());
		if (areaPreventivoCollegata.getDocumento().getEntita() != null) {
			maschera = maschera.replace("$codiceEntita$", areaPreventivoCollegata.getDocumento().getEntita()
					.getCodice().toString());
			maschera = maschera.replace("$descrizioneEntita$", areaPreventivoCollegata.getDocumento().getEntita()
					.getAnagrafica().getDenominazione());
		}
		return maschera;
	}

	/**
	 * @return the codiceTipoDocumentoCollegato
	 */
	public String getCodiceTipoDocumentoCollegato() {
		return codiceTipoDocumentoCollegato;
	}

	/**
	 * @return the dataAreaMagazzinoCollegata
	 */
	public Date getDataAreaMagazzinoCollegata() {
		return dataAreaMagazzinoCollegata;
	}

	/**
	 * 
	 * @return descrizione testata
	 */
	public String getDescrizione() {
		return descrizione;
	}

	@Override
	public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");
		stringBuffer.append("<b>");
		stringBuffer.append(getDescrizione());
		stringBuffer.append("</b>");
		if (stampaNote && getNoteRiga() != null) {
			stringBuffer.append("<br>");
			stringBuffer.append("<i>");
			stringBuffer.append(getNoteRiga());
			stringBuffer.append("</i>");
		}
		stringBuffer.append("</html>");
		return stringBuffer.toString();
	}

	@Override
	public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua, boolean escludiTagHtml) {
		String descrizioneRiga = getDescrizioneRiga(stampaAttributi, stampaNote, lingua);
		if (escludiTagHtml) {
			descrizioneRiga = PanjeaEJBUtil.removeHtml(descrizioneRiga);
		}
		return descrizioneRiga;
	}

	@Override
	public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua,
			boolean escludiTagHtml, boolean stampaConai) {
		return getDescrizioneRiga(stampaAttributi, stampaNote, lingua, escludiTagHtml);
	}
	
	/**
	 * @param codiceTipoDocumentoCollegato
	 *            the codiceTipoDocumentoCollegato to set
	 */
	public void setCodiceTipoDocumentoCollegato(String codiceTipoDocumentoCollegato) {
		this.codiceTipoDocumentoCollegato = codiceTipoDocumentoCollegato;
	}

	/**
	 * @param dataAreaMagazzinoCollegata
	 *            the dataAreaMagazzinoCollegata to set
	 */
	public void setDataAreaMagazzinoCollegata(Date dataAreaMagazzinoCollegata) {
		this.dataAreaMagazzinoCollegata = dataAreaMagazzinoCollegata;
	}

	/**
	 * 
	 * @param descrizione
	 *            descrizione testata
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
