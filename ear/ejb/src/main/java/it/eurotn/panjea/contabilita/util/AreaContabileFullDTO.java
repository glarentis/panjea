package it.eurotn.panjea.contabilita.util;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO che contiene un'area contabile, le righe contabile, l'area iva, l'area partite. Questo oggetto viene usato
 * durante il caricamento di un documento per evitare di eseguire piu' chiamate verso il server per caricare tutti i
 * dati di cui si ha bisogno. implemento la IDefProperty per avere uno standard di riconoscimento dell'oggetto nei forms
 * per spring-rcp
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@AuditableProperties(properties = { "areaContabile", "areaContabile.documento", "areaRate", "areaIva" })
public class AreaContabileFullDTO implements IDefProperty, Serializable {

	private static final long serialVersionUID = -1050042505656845193L;

	private AreaContabile areaContabile;

	private List<RigaContabile> righeContabili;

	private AreaIva areaIva;

	private AreaRate areaRate;

	/**
	 * indicatore da utilizzare esclusivamente lato presentazione, per eseguirte i controlli sui componenti legati ad
	 * {@link AreaRate} <br>
	 * e per la loro validazione. <br>
	 * Viene valorizzato al momento del caricamento di {@link AreaContabileFullDTO} controllando che l'attributo
	 * {@link AreaRate} <br>
	 * non sia istanziato vuoto
	 */
	private boolean areaRateEnabled = false;

	private AreaTesoreria areaTesoreria = null;

	private AreaMagazzinoLite areaMagazzinoLite;

	{
		this.areaContabile = new AreaContabile();
		this.areaRate = new AreaRate();
		this.areaIva = new AreaIva();
		this.righeContabili = new ArrayList<RigaContabile>();
	}

	/**
	 * Costruttore.
	 */
	public AreaContabileFullDTO() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (areaContabile != null) {
			if (obj instanceof AreaContabileFullDTO) {
				return areaContabile.equals(((AreaContabileFullDTO) obj).getAreaContabile());
			} else if (obj instanceof AreaContabile) {
				return areaContabile.equals(obj);
			}
		}
		return false;
	}

	/**
	 * @return AreaContabile
	 */
	public AreaContabile getAreaContabile() {
		return areaContabile;
	}

	/**
	 * @return AreaIva
	 */
	public AreaIva getAreaIva() {
		return areaIva;
	}

	/**
	 * @return the areaMagazzinoLite
	 */
	public AreaMagazzinoLite getAreaMagazzinoLite() {
		return areaMagazzinoLite;
	}

	/**
	 * @return the areaRate
	 */
	public AreaRate getAreaRate() {
		return areaRate;
	}

	/**
	 * @return the areaTesoreria
	 */
	public AreaTesoreria getAreaTesoreria() {
		return areaTesoreria;
	}

	@Override
	public String getDomainClassName() {
		return "it.eurotn.panjea.contabilita.domain.AreaContabile";
	}

	@Override
	public Integer getId() {
		return areaContabile.getId();
	}

	/**
	 * @return List<RigaContabile>
	 */
	public List<RigaContabile> getRigheContabili() {
		return righeContabili;
	}

	@Override
	public Integer getVersion() {
		return areaContabile.getVersion();
	}

	@Override
	public int hashCode() {
		if (getId() != null) {
			String hashStr = this.getClass().getName() + ":" + getId();
			return hashStr.hashCode();
		}
		return super.hashCode();
	}

	/**
	 * Inizializza le proprietà in base all'area contabile passata come parametro.
	 *
	 * @param areaContabileFullDTO
	 *            areaContabileFullDTO
	 * @return AreaContabileFullDTO area contabile
	 */
	public AreaContabileFullDTO initializedNewObject(AreaContabileFullDTO areaContabileFullDTO) {

		areaContabile.setDataRegistrazione(areaContabileFullDTO.getAreaContabile().getDataRegistrazione());
		if (areaContabileFullDTO.getAreaContabile().getTipoAreaContabile() != null
				&& areaContabileFullDTO.getAreaContabile().getTipoAreaContabile().isDataDocLikeDataReg()) {
			areaContabile.getDocumento().setDataDocumento(
					areaContabileFullDTO.getAreaContabile().getDataRegistrazione());
		}

		areaContabile.setTipoAreaContabile(areaContabileFullDTO.getAreaContabile().getTipoAreaContabile());
		areaRateEnabled = areaContabileFullDTO.isAreaRateEnabled();
		return areaContabileFullDTO;
	}

	/**
	 * @return areaIvaEnable
	 */
	public boolean isAreaIvaEnable() {
		return areaContabile.getDocumento().getTipoDocumento().isRigheIvaEnable();
	}

	/**
	 * @return areaIvaValid
	 */
	public boolean isAreaIvaValid() {
		return (areaIva != null && areaIva.getDatiValidazioneRighe().isValid());
	}

	/**
	 * @return areaMagazzinoPresente
	 */
	public boolean isAreaMagazzinoPresente() {
		return areaMagazzinoLite != null;
	}

	/**
	 * @return areaRateEnabled
	 */
	public boolean isAreaRateEnabled() {
		return areaRateEnabled;
	}

	/**
	 * @return the areaTesoreriaPresente
	 */
	public boolean isAreaTesoreriaPresente() {
		return areaTesoreria != null;
	}

	@Override
	public boolean isNew() {
		return areaContabile.isNew();
	}

	/**
	 * @param areaContabile
	 *            the areaContabile to set
	 */
	public void setAreaContabile(AreaContabile areaContabile) {
		this.areaContabile = areaContabile;
	}

	/**
	 * @param areaIva
	 *            the areaIva to set
	 */
	public void setAreaIva(AreaIva areaIva) {
		this.areaIva = areaIva;
	}

	/**
	 * @param areaMagazzinoLite
	 *            the areaMagazzinoLite to set
	 */
	public void setAreaMagazzinoLite(AreaMagazzinoLite areaMagazzinoLite) {
		this.areaMagazzinoLite = areaMagazzinoLite;
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
	 * @param areaTesoreria
	 *            the areaTesoreria to set
	 */
	public void setAreaTesoreria(AreaTesoreria areaTesoreria) {
		this.areaTesoreria = areaTesoreria;
	}

	/**
	 * @param righeContabili
	 *            the righeContabili to set
	 */
	public void setRigheContabili(List<RigaContabile> righeContabili) {
		this.righeContabili = righeContabili;
	}
}
