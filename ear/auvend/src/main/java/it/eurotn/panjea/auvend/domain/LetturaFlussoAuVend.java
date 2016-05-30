/**
 * 
 */
package it.eurotn.panjea.auvend.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Deposito;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Classe di dominio che descrive l'ultima operazione di lettura dei movimento di carico da AuVend per ciascun
 * {@link Deposito} interessato.
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
@Entity
@Table(name = "avend_letturaflusso")
@NamedQueries({ @NamedQuery(name = "LetturaFlussoAuVend.caricaByAzienda", query = "select lettura from LetturaFlussoAuVend lettura where lettura.deposito.sedeDeposito.azienda.codice = :paramCodiceAzienda") })
public class LetturaFlussoAuVend extends EntityBase {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@Fetch(FetchMode.JOIN)
	private Deposito deposito;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaLetturaFlussoCarichi;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaLetturaFlussoMovimenti;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaLetturaFlussoFatture;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaLetturaFlussoFatturazioneRifornimenti;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaLetturaFlussoRiparazioneContoTerzi;
	

	/**
	 * @return Returns the ultimaLetturaFlussoRiparazioneContoTerzi.
	 */
	public Date getUltimaLetturaFlussoRiparazioneContoTerzi() {
		return ultimaLetturaFlussoRiparazioneContoTerzi;
	}

	/**
	 * @param ultimaLettura
	 *            The ultimaLetturaFlussoRiparazioneContoTerzi to set.
	 */
	public void setUltimaLetturaFlussoRiparazioneContoTerzi(Date ultimaLettura) {
		this.ultimaLetturaFlussoRiparazioneContoTerzi = ultimaLettura;
	}

	/**
	 * @return Returns the deposito.
	 */
	public Deposito getDeposito() {
		return deposito;
	}

	/**
	 * @return Returns the ultimaLetturaFlussoCarichi.
	 */
	public Date getUltimaLetturaFlussoCarichi() {
		return ultimaLetturaFlussoCarichi;
	}

	/**
	 * @return the ultimaLetturaFlussoFatturazioneRifornimenti
	 */
	public Date getUltimaLetturaFlussoFatturazioneRifornimenti() {
		return ultimaLetturaFlussoFatturazioneRifornimenti;
	}

	/**
	 * @return Returns the ultimaLetturaFlussoFatture.
	 */
	public Date getUltimaLetturaFlussoFatture() {
		return ultimaLetturaFlussoFatture;
	}

	/**
	 * @return Returns the ultimaLetturaFlussoRifornimenti.
	 */
	public Date getUltimaLetturaFlussoMovimenti() {
		return ultimaLetturaFlussoMovimenti;
	}

	/**
	 * @param deposito
	 *            The deposito to set.
	 */
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	/**
	 * @param ultimaLetturaFlussoCarichi
	 *            The ultimaLetturaFlussoCarichi to set.
	 */
	public void setUltimaLetturaFlussoCarichi(Date ultimaLetturaFlussoCarichi) {
		this.ultimaLetturaFlussoCarichi = ultimaLetturaFlussoCarichi;
	}

	/**
	 * @param ultimaLetturaFlussoFatturazioneRifornimenti
	 *            the ultimaLetturaFlussoFatturazioneRifornimenti to set
	 */
	public void setUltimaLetturaFlussoFatturazioneRifornimenti(Date ultimaLetturaFlussoFatturazioneRifornimenti) {
		this.ultimaLetturaFlussoFatturazioneRifornimenti = ultimaLetturaFlussoFatturazioneRifornimenti;
	}

	/**
	 * @param ultimaLetturaFlussoFatture
	 *            The ultimaLetturaFlussoFatture to set.
	 */
	public void setUltimaLetturaFlussoFatture(Date ultimaLetturaFlussoFatture) {
		this.ultimaLetturaFlussoFatture = ultimaLetturaFlussoFatture;
	}

	/**
	 * @param ultimaLetturaFlussoMovimenti
	 *            The ultimaLetturaFlussoMovimenti to set.
	 */
	public void setUltimaLetturaFlussoMovimenti(Date ultimaLetturaFlussoMovimenti) {
		this.ultimaLetturaFlussoMovimenti = ultimaLetturaFlussoMovimenti;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {
		StringBuffer retValue = new StringBuffer();
		retValue.append("LetturaFlussoAuVend[ ").append(super.toString());
		retValue.append(" deposito = ").append(this.deposito != null ? this.deposito.getId() : null);
		retValue.append(" ultimaLetturaFlussoCarichi = ").append(this.ultimaLetturaFlussoCarichi);
		retValue.append(" ultimaLetturaFlussoFatture = ").append(this.ultimaLetturaFlussoFatture);
		retValue.append(" ultimaLetturaFlussoMovimenti = ").append(this.ultimaLetturaFlussoMovimenti);
		retValue.append(" ]");
		return retValue.toString();
	}

}
