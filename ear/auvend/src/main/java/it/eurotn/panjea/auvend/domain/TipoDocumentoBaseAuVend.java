/**
 * 
 */
package it.eurotn.panjea.auvend.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * Classe per la definizione dei {@link TipoAreaMagazzino} da utilizzare a seconda delle operazioni. <br>
 * i tipi operazione RECUPERO_CARICO e RECUPERO_RIFORNIMENTO possono essere presenti una sola volta. <br>
 * Il tipo operazione RECUPERO_FATTURA puo' avere invece più ricorrenze con diversi tipi area magazzino; in questo caso <br>
 * il deposito sorgente deve essere obbligatoriamente settato all'interno del tipo area magazzino perche' sarà quello
 * preso come <br>
 * riferimento per la creazione delle area magazzino durante il recupero
 * 
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
@Entity
@Table(name = "avend_tipi_documento_base")
@NamedQueries({
		@NamedQuery(name = "TipoDocumentoBaseAuVend.caricaByAzienda", query = "from TipoDocumentoBaseAuVend t where t.codiceAzienda = :paramCodiceAzienda "),
		@NamedQuery(name = "TipoDocumentoBaseAuVend.caricaByTipoOperazione", query = "from TipoDocumentoBaseAuVend t where t.codiceAzienda = :paramCodiceAzienda and t.tipoOperazione = :paramTipoOperazione ") })
public class TipoDocumentoBaseAuVend extends EntityBase {

	/**
	 * enum per la definizione delle operazioni possibili con AuVend.
	 * 
	 * 
	 * @author adriano
	 * @version 1.0, 23/dic/2008
	 * 
	 */
	public enum TipoOperazione {
		RECUPERO_CARICO, RECUPERO_RIFORNIMENTO, RECUPERO_FATTURA, RECUPERO_POZZETTO, RECUPERO_GENERICO, RECUPERO_RIFORNIMENTI_DA_FATTURARE, RECUPERO_RIPARAZIONI_CONTO_TERZI
	}

	private static final long serialVersionUID = 1L;;

	@Column(length = 10, nullable = false)
	@Index(name = "codice_azienda")
	private String codiceAzienda;

	@ManyToOne
	private TipoAreaMagazzino tipoAreaMagazzino;

	@Column
	private TipoOperazione tipoOperazione;

	@Column(length = 10)
	private String causaleAuvend;

	// indica se utilizzare come deposito di origine per panjea il caricatore del documento di auvent e non il deposito.
	private Boolean depositoCaricatore;

	/**
	 * 
	 * Costruttore.
	 */
	public TipoDocumentoBaseAuVend() {
		depositoCaricatore = true;
	}

	/**
	 * @return Returns the causaleAuvend.
	 */
	public String getCausaleAuvend() {
		return causaleAuvend;
	}

	/**
	 * @return Returns the codiceAzienda.
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the depositoCaricatore.
	 */
	public Boolean getDepositoCaricatore() {
		if (depositoCaricatore == null) {
			depositoCaricatore = false;
		}
		return depositoCaricatore;
	}

	/**
	 * @return Returns the tipoAreaMagazzino.
	 */
	public TipoAreaMagazzino getTipoAreaMagazzino() {
		return tipoAreaMagazzino;
	}

	/**
	 * @return Returns the tipoOperazione.
	 */
	public TipoOperazione getTipoOperazione() {
		return tipoOperazione;
	}

	/**
	 * Chiamato dopo aver caricato il bean.
	 */
	@SuppressWarnings("unused")
	@PostLoad
	private void init() {
	}

	/**
	 * @param causaleAuvend
	 *            The causaleAuvend to set.
	 */
	public void setCausaleAuvend(String causaleAuvend) {
		this.causaleAuvend = causaleAuvend;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param depositoCaricatore
	 *            The depositoCaricatore to set.
	 */
	public void setDepositoCaricatore(Boolean depositoCaricatore) {
		this.depositoCaricatore = depositoCaricatore;
	}

	/**
	 * @param tipoAreaMagazzino
	 *            The tipoAreaMagazzino to set.
	 */
	public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
		this.tipoAreaMagazzino = tipoAreaMagazzino;
	}

	/**
	 * @param tipoOperazione
	 *            The tipoOperazione to set.
	 */
	public void setTipoOperazione(TipoOperazione tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 * 
	 * @return a <code>String</code> come risultato di questo oggetto
	 */
	@Override
	public String toString() {
		StringBuffer retValue = new StringBuffer();
		retValue.append("TipoDocumentoBaseAuVend[ ").append(super.toString()).append(" codiceAzienda = ")
				.append(this.codiceAzienda).append(" tipoAreaMagazzino = ")
				.append(this.tipoAreaMagazzino != null ? this.tipoAreaMagazzino.getId() : null)
				.append(" tipoOperazione = ").append(this.tipoOperazione).append(" ]");
		return retValue.toString();
	}

}
