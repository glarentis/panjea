/**
 *
 */
package it.eurotn.panjea.pagamenti.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * Classe di dominio che rappresenta i codici di pagamento.
 * 
 * @author adriano
 * @version 1.0, 10/apr/07
 */
@NamedQueries({
		@NamedQuery(name = "CodicePagamento.caricaById", query = "select cp from CodicePagamento cp left join fetch cp.strutturePartita sp where cp.id = :paramId"),
		@NamedQuery(name = "CodicePagamento.caricaByCodice", query = "from CodicePagamento a where a.codiceAzienda = :paramCodiceAzienda and a.codicePagamento = :paramCodice "),
		@NamedQuery(name = "CodicePagamento.caricaByTipologiaPartita", query = "from CodicePagamento a where a.codiceAzienda = :paramCodiceAzienda and a.tipologiaPartita = :paramTipologiaPartita "),
		@NamedQuery(name = "CodicePagamento.caricaLikeByCodice", query = "from CodicePagamento a where a.codiceAzienda = :paramCodiceAzienda and (abilitato = true or :includiDisabilitati = true) and a.codicePagamento like :paramCodice order by a.codicePagamento"),
		@NamedQuery(name = "CodicePagamento.caricaLikeByDescrizione", query = "from CodicePagamento a where a.codiceAzienda = :paramCodiceAzienda and (abilitato = true or :includiDisabilitati = true) and a.descrizione like :paramDescrizione order by a.descrizione"),
		@NamedQuery(name = "CodicePagamento.caricaForPOS", query = "select cp from CodicePagamento cp where cp.codiceAzienda = :paramCodiceAzienda and cp.descrizionePos != null and cp.descrizionePos != ''"),
		@NamedQuery(name = "CodicePagamento.caricaAll", query = "from CodicePagamento a where a.codiceAzienda = :paramCodiceAzienda and (abilitato = true or :includiDisabilitati = true) order by a.codicePagamento") })
@Entity
@Audited
@Table(name = "part_codici_pagamento", uniqueConstraints = { @UniqueConstraint(columnNames = { "codiceAzienda",
		"codicePagamento" }) })
public class CodicePagamento extends EntityBase {
	/**
	 * Classe enum che definisce le tipologie di partite: NORMALE la strutturaPagamento genera delle rate che andranno
	 * gestite normalmente (emissione riba, chiusura con pag. .....) ACCONTO dovra' essere gestito con l'apposita
	 * procedura (ATTENZIONE : le rate generate con questa tipologia hanno l'importo negativo(-)) Questa tipologia di
	 * pagamento andra' a valorizzare apposito campo sull'areaPartite La tipologia LIQUIDAZIONE serve per generare la
	 * rata di liquidazione in automatico e deve esistere (UN SOLO CODICE PAGAMENTO!) in fase di liquidazione altrimenti
	 * solleva una specifica eccezione (CodicePagamentoNonTrovatoException)
	 * 
	 * @author vittorio
	 * @version 1.0, 28/Feb/2008
	 */
	public enum TipologiaPartita {
		NORMALE, ACCONTO, LIQUIDAZIONE
	}

	public enum TipoRicercaCodicePagamento {
		CODICE, DESCRIZIONE, TUTTO
	}

	private static final long serialVersionUID = 4726554604745457413L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@Column(length = 10, nullable = false)
	private String codicePagamento;

	@Column(length = 60)
	private String descrizione;

	@Column(length = 20)
	private String descrizionePos;

	@Column(precision = 19, scale = 2)
	private BigDecimal importoSpese;

	@Column(precision = 5, scale = 2)
	private BigDecimal percentualeSconto;

	private Integer giorniLimite;

	@Enumerated
	private TipologiaPartita tipologiaPartita;

	@Column
	private boolean abilitato;

	@ManyToMany(fetch = FetchType.LAZY)
	private List<StrutturaPartita> strutturePartita;

	private boolean contrassegno;

	@Column(precision = 5, scale = 2)
	private BigDecimal percentualeScontoCommerciale;

	@ManyToOne
	private TipoAreaPartita tipoAreaPartitaPredefinitaPerPagamenti;

	/**
	 * Costruttore.
	 */
	public CodicePagamento() {
		strutturePartita = new ArrayList<StrutturaPartita>();
	}

	/**
	 * @return Returns the codiceAzienda.
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the codicePagamento.
	 */
	public String getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the descrizionePos
	 */
	public String getDescrizionePos() {
		return descrizionePos;
	}

	/**
	 * @return the giorniLimite
	 */
	public Integer getGiorniLimite() {
		return giorniLimite;
	}

	/**
	 * @return Returns the importoSpese.
	 */
	public BigDecimal getImportoSpese() {
		return importoSpese;
	}

	/**
	 * @return the percentualeSconto
	 */
	public BigDecimal getPercentualeSconto() {
		return percentualeSconto;
	}

	/**
	 * @return the percentualeScontoCommerciale
	 */
	public BigDecimal getPercentualeScontoCommerciale() {
		if (percentualeScontoCommerciale == null) {
			return BigDecimal.ZERO;
		}

		return percentualeScontoCommerciale;
	}

	/**
	 * @return the strutturePartita
	 */
	public List<StrutturaPartita> getStrutturePartita() {
		return strutturePartita;
	}

	/**
	 * @return the tipoAreaPartitaPredefinitaPerPagamenti
	 */
	public TipoAreaPartita getTipoAreaPartitaPredefinitaPerPagamenti() {
		return tipoAreaPartitaPredefinitaPerPagamenti;
	}

	/**
	 * @return the tipologiaRata
	 */
	public TipologiaPartita getTipologiaPartita() {
		return tipologiaPartita;
	}

	/**
	 * @return the abilitato
	 */
	public boolean isAbilitato() {
		return abilitato;
	}

	/**
	 * @return the contrassegno
	 */
	public boolean isContrassegno() {
		return contrassegno;
	}

	/**
	 * @param abilitato
	 *            the abilitato to set
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codicePagamento
	 *            The codicePagamento to set.
	 */
	public void setCodicePagamento(String codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param contrassegno
	 *            the contrassegno to set
	 */
	public void setContrassegno(boolean contrassegno) {
		this.contrassegno = contrassegno;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param descrizionePos
	 *            the descrizionePos to set
	 */
	public void setDescrizionePos(String descrizionePos) {
		this.descrizionePos = descrizionePos;
	}

	/**
	 * @param giorniLimite
	 *            the giorniLimite to set
	 */
	public void setGiorniLimite(Integer giorniLimite) {
		this.giorniLimite = giorniLimite;
	}

	/**
	 * @param importoSpese
	 *            The importoSpese to set.
	 */
	public void setImportoSpese(BigDecimal importoSpese) {
		this.importoSpese = importoSpese;
	}

	/**
	 * @param percentualeSconto
	 *            the percentualeSconto to set
	 */
	public void setPercentualeSconto(BigDecimal percentualeSconto) {
		this.percentualeSconto = percentualeSconto;
	}

	/**
	 * @param percentualeScontoCommerciale
	 *            the percentualeScontoCommerciale to set
	 */
	public void setPercentualeScontoCommerciale(BigDecimal percentualeScontoCommerciale) {
		this.percentualeScontoCommerciale = percentualeScontoCommerciale;
	}

	/**
	 * @param strutturePartita
	 *            the strutturePartita to set
	 */
	public void setStrutturePartita(List<StrutturaPartita> strutturePartita) {
		this.strutturePartita = strutturePartita;
	}

	/**
	 * @param tipoAreaPartitaPredefinitaPerPagamenti
	 *            the tipoAreaPartitaPredefinitaPerPagamenti to set
	 */
	public void setTipoAreaPartitaPredefinitaPerPagamenti(TipoAreaPartita tipoAreaPartitaPredefinitaPerPagamenti) {
		this.tipoAreaPartitaPredefinitaPerPagamenti = tipoAreaPartitaPredefinitaPerPagamenti;
	}

	/**
	 * @param tipologiaPartita
	 *            the tipologiaPartita to set
	 */
	public void setTipologiaPartita(TipologiaPartita tipologiaPartita) {
		this.tipologiaPartita = tipologiaPartita;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CodicePagamento[");
		buffer.append("abilitato = ").append(abilitato);
		buffer.append(", codiceAzienda = ").append(codiceAzienda);
		buffer.append(", codicePagamento = ").append(codicePagamento);
		buffer.append(", descrizione = ").append(descrizione);
		buffer.append(", importoSpese = ").append(importoSpese);
		buffer.append(", percentualeSconto = ").append(percentualeSconto);
		buffer.append(", tipologiaPartita = ").append(tipologiaPartita);
		buffer.append("]");
		return buffer.toString();
	}

}
