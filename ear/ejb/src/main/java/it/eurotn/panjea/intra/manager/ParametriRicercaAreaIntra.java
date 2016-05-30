package it.eurotn.panjea.intra.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "para_ricerca_area_intra")
public class ParametriRicercaAreaIntra extends AbstractParametriRicerca {
	private static final long serialVersionUID = 1615162537496778730L;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni")) })
	private Periodo periodoRegistrazione;

	private Documento documentoCorrente;

	@Column(length = 6)
	private String codiceNazioneAzienda;

	/**
	 * @return the codiceNazioneAzienda
	 */
	public String getCodiceNazioneAzienda() {
		return codiceNazioneAzienda;
	}

	/**
	 * @return Returns the documentoCorrente.
	 */
	public Documento getDocumentoCorrente() {
		return documentoCorrente;
	}

	/**
	 * @return Returns the periodoRegistrazione.
	 */
	public Periodo getPeriodoRegistrazione() {
		if (periodoRegistrazione == null) {
			periodoRegistrazione = new Periodo();
			periodoRegistrazione.setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
		}
		return periodoRegistrazione;
	}

	/**
	 * @param codiceNazioneAzienda
	 *            the codiceNazioneAzienda to set
	 */
	public void setCodiceNazioneAzienda(String codiceNazioneAzienda) {
		this.codiceNazioneAzienda = codiceNazioneAzienda;
	}

	/**
	 * 
	 * @param documento
	 *            documento correntemente settato
	 */
	public void setDocumentoCorrente(Documento documento) {
		this.documentoCorrente = documento;

	}

	/**
	 * @param periodoRegistrazione
	 *            The periodoRegistrazione to set.
	 */
	public void setPeriodoRegistrazione(Periodo periodoRegistrazione) {
		this.periodoRegistrazione = periodoRegistrazione;
	}

}
