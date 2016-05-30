package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fattazzo
 */
public class AreaOrdineRicerca implements Serializable {

	private static final long serialVersionUID = -246604627995219829L;

	private int idAreaOrdine;

	private String numeroOrdineCliente;
	private Documento documento;

	private EntitaDocumento entitaDocumento;

	private TipoAreaOrdine tipoAreaOrdine; // utilizzata perchè nei risultati ricerca devo stampare i documenti e
	// mi serve il nome del report
	private DepositoLite depositoOrigine;
	private Date dataRegistrazione;
	private boolean evaso;
	private StatoAreaOrdine statoAreaOrdine;
	private String note;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore.
	 */
	public AreaOrdineRicerca() {
		documento = new Documento();
		depositoOrigine = new DepositoLite();
		entitaDocumento = new EntitaDocumento();
		tipoAreaOrdine = new TipoAreaOrdine();
		sedeEntita = new SedeEntita();
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param areaOrdine
	 *            area ardine
	 */
	public AreaOrdineRicerca(final AreaOrdine areaOrdine) {
		documento = new Documento();
		depositoOrigine = new DepositoLite();
		entitaDocumento = new EntitaDocumento();
		sedeEntita = new SedeEntita();
		tipoAreaOrdine = new TipoAreaOrdine();

		setAzienda(areaOrdine.getDocumento().getCodiceAzienda());

		setIdAreaOrdine(areaOrdine.getId());
		setDataRegistrazione(areaOrdine.getDataRegistrazione());
		setEvaso(areaOrdine.isEvaso());

		if (areaOrdine.getAreaOrdineNote() != null) {
			setNota(areaOrdine.getAreaOrdineNote().getNoteTestata());
		}

		setStatoAreaOrdine(areaOrdine.getStatoAreaOrdine());

		setIdDocumento(areaOrdine.getDocumento().getId());
		setCodiceDocumento(areaOrdine.getDocumento().getCodice());
		setDataDocumento(areaOrdine.getDocumento().getDataDocumento());
		setTotaleDocumento(areaOrdine.getDocumento().getTotale().getImportoInValutaAzienda());

		setIdTipoDocumento(areaOrdine.getDocumento().getTipoDocumento().getId());
		setCodiceTipoDocumento(areaOrdine.getDocumento().getTipoDocumento().getCodice());
		setDescrizioneTipoDocumento(areaOrdine.getDocumento().getTipoDocumento().getDescrizione());

		if (areaOrdine.getRiferimentiOrdine() != null) {
			setNumeroOrdineCliente(areaOrdine.getRiferimentiOrdine().getNumeroOrdine());
		}

		if (areaOrdine.getDepositoOrigine() != null) {
			setCodiceDepositoOrigine(areaOrdine.getDepositoOrigine().getCodice());
			setDescrizioneDepositoOrigine(areaOrdine.getDepositoOrigine().getDescrizione());
		}

		if (areaOrdine.getDocumento().getEntita() != null) {
			setIdEntita(areaOrdine.getDocumento().getEntita().getId());
			setCodiceEntita(areaOrdine.getDocumento().getEntita().getCodice());
			setDenominazioneEntita(areaOrdine.getDocumento().getEntita().getAnagrafica().getDenominazione());
			setTipoEntita(areaOrdine.getDocumento().getEntita().creaProxyEntita().getTipo());
		}
		if (areaOrdine.getDocumento().getSedeEntita() != null) {
			setIdSede(areaOrdine.getDocumento().getSedeEntita().getId());
			setCodiceSede(areaOrdine.getDocumento().getSedeEntita().getCodice());
			setDescrizioneSede(areaOrdine.getDocumento().getSedeEntita().getSede().getDescrizione());
			setDescrizioneLocalitaSede(areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici()
					.getDescrizioneLocalita());
			setIndirizzoSede(areaOrdine.getDocumento().getSedeEntita().getSede().getIndirizzo());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AreaOrdineRicerca other = (AreaOrdineRicerca) obj;
		if (idAreaOrdine != other.idAreaOrdine) {
			return false;
		}
		return true;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the depositoOrigine
	 */
	public DepositoLite getDepositoOrigine() {
		return depositoOrigine;
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the entitaDocumento
	 */
	public EntitaDocumento getEntitaDocumento() {
		return entitaDocumento;
	}

	/**
	 * @return the id
	 */
	public int getIdAreaOrdine() {
		return idAreaOrdine;
	}

	/**
	 * @return Returns the nota.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return Returns the numeroOrdineCliente.
	 */
	public String getNumeroOrdineCliente() {
		return numeroOrdineCliente;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the statoAreaOrdine
	 */
	public StatoAreaOrdine getStatoAreaOrdine() {
		return statoAreaOrdine;
	}

	/**
	 * @return the tipoAreaOrdine
	 */
	public TipoAreaOrdine getTipoAreaOrdine() {
		return tipoAreaOrdine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idAreaOrdine;
		return result;
	}

	/**
	 * @return the evaso
	 */
	public boolean isEvaso() {
		return evaso;
	}

	/**
	 * @param azienda
	 *            azienda del documento
	 */
	public void setAzienda(String azienda) {
		if (azienda != null) {
			entitaDocumento.setDescrizione(azienda);
		}
	}

	/**
	 * @param codiceDepositoOrigine
	 *            the codiceDepositoOrigine to set
	 */
	public void setCodiceDepositoOrigine(String codiceDepositoOrigine) {
		depositoOrigine.setCodice(codiceDepositoOrigine);
	}

	/**
	 * @param codiceDocumento
	 *            the codiceDocumento to set
	 */
	public void setCodiceDocumento(CodiceDocumento codiceDocumento) {
		documento.setCodice(codiceDocumento);
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		entitaDocumento.setCodice(codiceEntita);
	}

	/**
	 * @param codiceSede
	 *            codice della sede
	 */
	public void setCodiceSede(String codiceSede) {
		sedeEntita.setCodice(codiceSede);
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		documento.getTipoDocumento().setCodice(codiceTipoDocumento);
		tipoAreaOrdine.getTipoDocumento().setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		documento.setDataDocumento(dataDocumento);
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param denominazione
	 *            the denominazione to set
	 */
	public void setDenominazioneEntita(String denominazione) {
		if (denominazione != null) {
			entitaDocumento.setDescrizione(denominazione);
		}
	}

	/**
	 * @param descrizioneDepositoOrigine
	 *            the descrizioneDepositoOrigine to set
	 */
	public void setDescrizioneDepositoOrigine(String descrizioneDepositoOrigine) {
		depositoOrigine.setDescrizione(descrizioneDepositoOrigine);
	}

	/**
	 * @param descrizioneLocalita
	 *            the descrizioneLocalita to set
	 */
	public void setDescrizioneLocalitaSede(String descrizioneLocalita) {
		if (sedeEntita.getSede().getDatiGeografici().getLocalita() == null) {
			sedeEntita.getSede().getDatiGeografici().setLocalita(new Localita());
		}
		sedeEntita.getSede().getDatiGeografici().getLocalita().setDescrizione(descrizioneLocalita);
	}

	/**
	 * @param descrizioneSedeEntita
	 *            the descrizioneSedeEntita to set
	 */
	public void setDescrizioneSede(String descrizioneSedeEntita) {
		sedeEntita.getSede().setDescrizione(descrizioneSedeEntita);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
		tipoAreaOrdine.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param evaso
	 *            the evaso to set
	 * @uml.property name="evaso"
	 */
	public void setEvaso(boolean evaso) {
		this.evaso = evaso;
	}

	/**
	 * @param idAreaOrdine
	 *            the id to set
	 */
	public void setIdAreaOrdine(int idAreaOrdine) {
		this.idAreaOrdine = idAreaOrdine;
	}

	/**
	 * @param idDocumento
	 *            id del documento
	 */
	public void setIdDocumento(Integer idDocumento) {
		documento.setId(idDocumento);
	}

	/**
	 * @param idEntita
	 *            id entita
	 */
	public void setIdEntita(Integer idEntita) {
		entitaDocumento.setId(idEntita);
	}

	/**
	 * @param idSede
	 *            id sede
	 */
	public void setIdSede(Integer idSede) {
		sedeEntita.setId(idSede);

	}

	/**
	 * @param idTipoAreaOrdine
	 *            the id to set
	 */
	public void setIdTipoAreaOrdine(int idTipoAreaOrdine) {
		tipoAreaOrdine.setId(idTipoAreaOrdine);
	}

	/**
	 * @param idTipoDocumento
	 *            id Tipo Documento
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		documento.getTipoDocumento().setId(idTipoDocumento);
	}

	/**
	 * @param indirizzoSedeEntita
	 *            the indirizzoSedeEntita to set
	 */
	public void setIndirizzoSede(String indirizzoSedeEntita) {
		sedeEntita.getSede().setIndirizzo(indirizzoSedeEntita);
	}

	/**
	 * @param noteParam
	 *            The nota to set.
	 */
	public void setNota(String noteParam) {
		this.note = noteParam;
	}

	/**
	 * @param numeroOrdineCliente
	 *            The numeroOrdineCliente to set.
	 */
	public void setNumeroOrdineCliente(String numeroOrdineCliente) {
		this.numeroOrdineCliente = numeroOrdineCliente;
	}

	/**
	 * @param statoAreaOrdine
	 *            the statoAreaOrdine to set
	 */
	public void setStatoAreaOrdine(StatoAreaOrdine statoAreaOrdine) {
		this.statoAreaOrdine = statoAreaOrdine;
	}

	/**
	 * @param tipoEntitaParam
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntitaParam) {
		entitaDocumento.setTipoEntita(tipoEntitaParam);
	}

	/**
	 * @param totaleDocumento
	 *            the totaleDocumento to set
	 */
	public void setTotaleDocumento(BigDecimal totaleDocumento) {
		documento.getTotale().setImportoInValutaAzienda(totaleDocumento);
	}
}
