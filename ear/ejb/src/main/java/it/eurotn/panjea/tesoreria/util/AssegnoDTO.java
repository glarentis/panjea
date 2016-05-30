package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno.StatoAssegno;

import java.io.Serializable;
import java.util.Date;

public class AssegnoDTO implements Serializable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum StatoCarrello {
		SELEZIONABILE, AGGIUNTO_CARRELLO, NON_SELEZIONABILE, DA_AGGIUNGERE
	}

	private static final long serialVersionUID = -7929807949878143530L;

	private AreaAssegno areaAssegno = null;
	private StatoCarrello statoCarrello = null;
	private EntitaDocumento entitaDocumento = null;
	private EntitaLite entita = null;
	private StatoAssegno statoAssegno = null;

	/**
	 * Costruttore.
	 */
	public AssegnoDTO() {
		super();
		this.areaAssegno = new AreaAssegno();
		this.entitaDocumento = new EntitaDocumento();
		this.entita = new ClienteLite();
		this.entita.setAnagrafica(new AnagraficaLite());
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
		AssegnoDTO other = (AssegnoDTO) obj;
		if (areaAssegno == null) {
			if (other.areaAssegno != null) {
				return false;
			}
		} else if (!areaAssegno.equals(other.areaAssegno)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the AreaAssegno
	 */
	public AreaAssegno getAreaAssegno() {
		return areaAssegno;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the entitaDocumento
	 */
	public EntitaDocumento getEntitaDocumento() {
		return entitaDocumento;
	}

	/**
	 * @return the statoAssegno
	 */
	public StatoAssegno getStatoAssegno() {
		return statoAssegno;
	}

	/**
	 * @return the statoCarrello
	 */
	public StatoCarrello getStatoCarrello() {
		return statoCarrello;
	}

	/**
	 * @return stato del carrello in base allo stato della rata
	 */
	public StatoCarrello getStatoCarrelloFromStatoAssegno() {
		if (getStatoAssegno() == StatoAssegno.CHIUSO) {
			return StatoCarrello.NON_SELEZIONABILE;
		} else {
			return StatoCarrello.SELEZIONABILE;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaAssegno == null) ? 0 : areaAssegno.hashCode());
		return result;
	}

	/**
	 * @param abi
	 *            the abi to set
	 */
	public void setAbi(String abi) {
		this.areaAssegno.setAbi(abi);
	}

	/**
	 * @param cab
	 *            the cab to set
	 */
	public void setCab(String cab) {
		areaAssegno.setCab(cab);
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		if (entitaDocumento.getTipoEntita().equals(TipoEntita.AZIENDA)) {
			this.entitaDocumento.setDescrizione(codiceAzienda);
		}
	}

	/**
	 * @param codiceEntitaRata
	 *            the codiceEntitaRata to set
	 */
	public void setCodiceEntitaRata(Integer codiceEntitaRata) {
		this.entita.setCodice(codiceEntitaRata);
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.areaAssegno.getDocumento().getTipoDocumento().setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.areaAssegno.getDocumento().setDataDocumento(dataDocumento);
	}

	/**
	 * @param denominazioneEntitaRata
	 *            the denominazioneEntitaRata to set
	 */
	public void setDenominazioneEntitaRata(String denominazioneEntitaRata) {
		this.entita.getAnagrafica().setDenominazione(denominazioneEntitaRata);
	}

	/**
	 * @param descrizioneEntitaDocumento
	 *            the descrizioneEntitaDocumento to set
	 */
	public void setDescrizioneEntitaDocumento(String descrizioneEntitaDocumento) {
		if (entitaDocumento.getTipoEntita().equals(TipoEntita.BANCA)) {
			this.entitaDocumento.setDescrizione(descrizioneEntitaDocumento);
		}
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.areaAssegno.getDocumento().getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idAssegno
	 *            the idAssegno to set
	 */
	public void setIdAssegno(Integer idAssegno) {
		this.areaAssegno.setId(idAssegno);
	}

	/**
	 * @param idDocumento
	 *            the idDocumento to set
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.areaAssegno.getDocumento().setId(idDocumento);
	}

	/**
	 * @param idEntitaDocumento
	 *            the idEntitaDocumento to set
	 */
	public void setIdEntitaDocumento(Integer idEntitaDocumento) {
		this.entitaDocumento.setId(idEntitaDocumento);
	}

	/**
	 * @param idEntitaRata
	 *            the idEntitaRata to set
	 */
	public void setIdEntitaRata(Integer idEntitaRata) {
		this.entita.setId(idEntitaRata);
	}

	/**
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.areaAssegno.getDocumento().getTipoDocumento().setId(idTipoDocumento);
	}

	/**
	 * @param maxDataPagamento
	 *            the maxDataPagamento to set
	 */
	public void setMaxDataPagamento(Date maxDataPagamento) {
		if (maxDataPagamento == null && entitaDocumento.getTipoEntita().equals(TipoEntita.AZIENDA)) {
			this.statoAssegno = StatoAssegno.IN_LAVORAZIONE;
		} else {
			this.statoAssegno = StatoAssegno.CHIUSO;
		}
		this.statoCarrello = getStatoCarrelloFromStatoAssegno();
	}

	/**
	 * @param numeroAssegno
	 *            the numeroAssegno to set
	 */
	public void setNumeroAssegno(String numeroAssegno) {
		this.areaAssegno.setNumeroAssegno(numeroAssegno);
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
		this.areaAssegno.getDocumento().setCodice(numeroDocumento);
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		areaAssegno.setRapportoBancarioAzienda(rapportoBancarioAzienda);
	}

	/**
	 * @param statoAssegno
	 *            the statoAssegno to set
	 */
	public void setStatoAssegno(StatoAssegno statoAssegno) {
		this.statoAssegno = statoAssegno;
	}

	/**
	 * @param statoCarrello
	 *            the statoCarrello to set
	 */
	public void setStatoCarrello(StatoCarrello statoCarrello) {
		this.statoCarrello = statoCarrello;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.entitaDocumento.setTipoEntita(tipoEntita);
	}

	/**
	 * @param totaleDocumento
	 *            the totaleDocumento to set
	 */
	public void setTotaleDocumento(Importo totaleDocumento) {
		this.areaAssegno.getDocumento().setTotale(totaleDocumento);
	}

}
