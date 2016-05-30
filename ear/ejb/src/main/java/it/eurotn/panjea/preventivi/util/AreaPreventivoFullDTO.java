package it.eurotn.panjea.preventivi.util;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.util.interfaces.IAreaDTO;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AuditableProperties(properties = { "areaPreventivo", "areaPreventivo.documento" })
public class AreaPreventivoFullDTO implements Serializable, IDefProperty, IAreaDTO<AreaPreventivo> {
	private static final long serialVersionUID = 1L;

	private AreaPreventivo areaPreventivo;
	private AreaRate areaRate;
	private List<RigaPreventivo> righePreventivo;

	/**
	 * indicatore da utilizzare esclusivamente lato presentazione, per eseguire i controlli sui componenti legati ad
	 * {@link AreaRate} <br>
	 * e per la loro validazione. <br>
	 * Viene valorizzato al momento del caricamento di {@link AreaMagazzinoFullDTO} controllando che l'attributo
	 * {@link AreaRate} <br>
	 * non sia istanziato vuoto
	 */
	private boolean areaRateEnabled = false;

	{
		areaPreventivo = new AreaPreventivo();
		areaRate = new AreaRate();
		righePreventivo = new ArrayList<RigaPreventivo>();
	}

	/**
	 * Equals basato sull'id dell'area Magazzino.<br/>
	 * <b>NB.</b>Anche se viene testato con un {@link AreaMagazzino} torna true
	 * 
	 * @param obj
	 *            oggetto da testare
	 * @return true se gli Id degli oggetti sono uguali
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass() && !obj.getClass().getName().equals(AreaPreventivo.class.getName())) {
			return false;
		}

		AreaPreventivo areaPreventivoEquals = null;
		if (obj.getClass() == AreaPreventivo.class) {
			areaPreventivoEquals = (AreaPreventivo) obj;
		} else {
			areaPreventivoEquals = ((AreaPreventivoFullDTO) obj).getAreaPreventivo();
		}

		return getAreaPreventivo().equals(areaPreventivoEquals);
	}

	@Override
	public AreaPreventivo getAreaDocumento() {
		return getAreaPreventivo();
	}

	/**
	 * @return the areaOrdine
	 */
	public AreaPreventivo getAreaPreventivo() {
		return areaPreventivo;
	}

	/**
	 * @return the areaRate
	 */
	@Override
	public AreaRate getAreaRate() {
		return areaRate;
	}

	@Override
	public String getDomainClassName() {
		return areaPreventivo.getDomainClassName();
	}

	@Override
	public Integer getId() {
		return areaPreventivo.getId();
	}

	/**
	 * Restituisce una nuova {@link AreaOrdine} inizializzata dai valori della area ordine corrente.
	 * 
	 * @return {@link AreaOrdine}
	 */
	public AreaPreventivoFullDTO getInitializedNewObject() {
		AreaPreventivoFullDTO areaPreventivoFullDTO = new AreaPreventivoFullDTO();
		AreaPreventivo areaPreventivoNew = new AreaPreventivo();
		areaPreventivoNew.setTipoAreaPreventivo(this.areaPreventivo.getTipoAreaPreventivo());
		areaPreventivoNew.setDataRegistrazione(this.areaPreventivo.getDataRegistrazione());
		areaPreventivoFullDTO.setAreaPreventivo(areaPreventivoNew);
		areaPreventivoFullDTO.setAreaRateEnabled(isAreaRateEnabled());
		return areaPreventivoFullDTO;
	}

	/**
	 * @return righeOrdine
	 */
	public List<RigaPreventivo> getRighePreventivo() {
		return righePreventivo;
	}

	@Override
	public Integer getVersion() {
		return areaPreventivo.getVersion();
	}

	@Override
	public int hashCode() {
		return (areaPreventivo == null) ? 0 : areaPreventivo.hashCode();
	}

	/**
	 * @return the areaRateEnabled
	 */
	@Override
	public boolean isAreaRateEnabled() {
		return areaRateEnabled;
	}

	@Override
	public boolean isNew() {
		return areaPreventivo.isNew();
	}

	@Override
	public void setAreaDocumento(AreaPreventivo areaDocumento) {
		setAreaPreventivo(areaDocumento);
	}

	/**
	 * @param areaPreventivo
	 *            the areaPreventivo to set
	 */
	public void setAreaPreventivo(AreaPreventivo areaPreventivo) {
		this.areaPreventivo = areaPreventivo;
	}

	/**
	 * @param areaRate
	 *            the areaRate to set
	 */
	public void setAreaRate(AreaRate areaRate) {
		this.areaRate = areaRate;
	}

	/**
	 * @param areaRateEnabled
	 *            the areaRateEnabled to set
	 */
	public void setAreaRateEnabled(boolean areaRateEnabled) {
		this.areaRateEnabled = areaRateEnabled;
	}

	/**
	 * @param righePreventivo
	 *            the righePreventivo to set
	 */
	public void setRighePreventivo(List<RigaPreventivo> righePreventivo) {
		this.righePreventivo = righePreventivo;
	}

}
