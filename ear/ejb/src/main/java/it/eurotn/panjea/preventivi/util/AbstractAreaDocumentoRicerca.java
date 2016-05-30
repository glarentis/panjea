package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public abstract class AbstractAreaDocumentoRicerca<E extends ITipoAreaDocumento, T extends IAreaDocumentoTestata>
		implements Serializable {

	private static final long serialVersionUID = -8345804857501354361L;

	private int idAreaDocumento;

	private Documento documento;

	private EntitaDocumento entitaDocumento;

	private Date dataRegistrazione;

	private String note;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore.
	 */
	public AbstractAreaDocumentoRicerca() {
		documento = new Documento();
		entitaDocumento = new EntitaDocumento();
		sedeEntita = new SedeEntita();
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param areaPreventivo
	 *            area preventivo
	 */
	public AbstractAreaDocumentoRicerca(final T areaPreventivo) {
		this();

		setAzienda(areaPreventivo.getDocumento().getCodiceAzienda());

		setIdAreaPreventivo(areaPreventivo.getId().intValue());
		setDataRegistrazione(areaPreventivo.getDataRegistrazione());

		if (areaPreventivo.getAreaDocumentoNote() != null) {
			setNota(areaPreventivo.getAreaDocumentoNote().getNoteTestata());
		}

		setIdDocumento(areaPreventivo.getDocumento().getId());
		setCodiceDocumento(areaPreventivo.getDocumento().getCodice());
		setDataDocumento(areaPreventivo.getDocumento().getDataDocumento());
		setTotaleDocumento(areaPreventivo.getDocumento().getTotale().getImportoInValutaAzienda());

		setIdTipoDocumento(areaPreventivo.getDocumento().getTipoDocumento().getId());
		setCodiceTipoDocumento(areaPreventivo.getDocumento().getTipoDocumento().getCodice());
		setDescrizioneTipoDocumento(areaPreventivo.getDocumento().getTipoDocumento().getDescrizione());

		if (areaPreventivo.getDocumento().getEntita() != null) {
			setIdEntita(areaPreventivo.getDocumento().getEntita().getId());
			setCodiceEntita(areaPreventivo.getDocumento().getEntita().getCodice());
			setDenominazioneEntita(areaPreventivo.getDocumento().getEntita().getAnagrafica().getDenominazione());
			setTipoEntita(areaPreventivo.getDocumento().getEntita().creaProxyEntita().getTipo());
		}
	}

	/**
	 * 
	 * @return nuova area documento;
	 */
	protected abstract T creaAreaDocumento();

	/**
	 * 
	 * @param areaRicerca
	 *            areaRicerca
	 * @return areaPreventivo
	 */
	public T creaProxyAreaPreventivo(AbstractAreaDocumentoRicerca<?, ?> areaRicerca) {
		T areaDocumento = creaAreaDocumento();
		areaDocumento.setId(areaRicerca.idAreaDocumento);
		areaDocumento.setDocumento(areaRicerca.documento);
		areaDocumento.setTipoArea(areaRicerca.getTipoAreaDocumento());
		return areaDocumento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		AbstractAreaDocumentoRicerca<?, ?> other = (AbstractAreaDocumentoRicerca<?, ?>) obj;
		return this.idAreaDocumento == other.idAreaDocumento;
	}

	/**
	 * @return il codice documento
	 */
	public CodiceDocumento getCodice() {
		return documento != null ? documento.getCodice() : null;
	}

	/**
	 * @return the dataRegistrazione
	 * @uml.property name="dataRegistrazione"
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the documento
	 * @uml.property name="documento"
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the entitaDocumento
	 * @uml.property name="entitaDocumento"
	 */
	public EntitaDocumento getEntitaDocumento() {
		return entitaDocumento;
	}

	/**
	 * @return the id
	 */
	public int getIdAreaDocumento() {
		return idAreaDocumento;
	}

	/**
	 * @return Returns the nota.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the tipoAreaPreventivo
	 */
	public abstract E getTipoAreaDocumento();

	@Override
	public int hashCode() {
		return idAreaDocumento;
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
	 * 
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
		getTipoAreaDocumento().getTipoDocumento().setCodice(codiceTipoDocumento);
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
	 * @uml.property name="dataRegistrazione"
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
		getTipoAreaDocumento().getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idAreaPreventivo
	 *            the id to set
	 */
	public void setIdAreaPreventivo(int idAreaPreventivo) {
		this.idAreaDocumento = idAreaPreventivo;
	}

	/**
	 * 
	 * @param idDocumento
	 *            id del documento
	 */
	public void setIdDocumento(Integer idDocumento) {
		documento.setId(idDocumento);
	}

	/**
	 * 
	 * @param idEntita
	 *            id entita
	 */
	public void setIdEntita(Integer idEntita) {
		entitaDocumento.setId(idEntita);
	}

	/**
	 * 
	 * @param idSede
	 *            id sede
	 */
	public void setIdSede(Integer idSede) {
		sedeEntita.setId(idSede);

	}

	/**
	 * @param idTipoAreaDocumento
	 *            the id to set
	 */
	public abstract void setIdTipoAreaDocumento(int idTipoAreaDocumento);

	/**
	 * 
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
