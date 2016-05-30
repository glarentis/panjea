package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Wrappa la classe {@link ParametriCalcoloPrezzi} aggiungendo i parametri utili per bindarla al form.
 * 
 * @author giangi
 */
public class ParametriCalcoloPrezziPM implements Cloneable, Serializable {

	private static final long serialVersionUID = 3136666574904752085L;
	private ArticoloLite articolo;
	private EntitaLite entita;
	private SedeEntita sedeEntita;
	private Date data;
	private Listino listino;
	private Listino listinoAlternativo;
	private ProvenienzaPrezzo provenienzaPrezzo;

	private TipoMezzoTrasporto tipoMezzoTrasporto;

	private String codiceValuta;

	private Integer idZonaGeografica;

	private AgenteLite agente;

	private boolean effettuaRicerca;

	private CodicePagamento codicePagamento;

	/**
	 * Costruttore.
	 */
	public ParametriCalcoloPrezziPM() {
		provenienzaPrezzo = ProvenienzaPrezzo.LISTINO;
		data = Calendar.getInstance().getTime();
	}

	@Override
	public ParametriCalcoloPrezziPM clone() {
		ParametriCalcoloPrezziPM parametriCalcoloPrezziPM = new ParametriCalcoloPrezziPM();
		parametriCalcoloPrezziPM.setArticolo(getArticolo());
		parametriCalcoloPrezziPM.setEntita(getEntita());
		parametriCalcoloPrezziPM.setData(getData());
		parametriCalcoloPrezziPM.setListino(getListino());
		parametriCalcoloPrezziPM.setListinoAlternativo(getListinoAlternativo());
		parametriCalcoloPrezziPM.setProvenienzaPrezzo(getProvenienzaPrezzo());
		parametriCalcoloPrezziPM.setTipoMezzoTrasporto(getTipoMezzoTrasporto());
		parametriCalcoloPrezziPM.setIdZonaGeografica(getIdZonaGeografica());
		parametriCalcoloPrezziPM.setCodiceValuta(getCodiceValuta());
		parametriCalcoloPrezziPM.setAgente(getAgente());
		parametriCalcoloPrezziPM.setCodicePagamento(getCodicePagamento());
		return parametriCalcoloPrezziPM;
	}

	/**
	 * @return the agente
	 */
	public AgenteLite getAgente() {
		return agente;
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the codicePagamento
	 */
	public CodicePagamento getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return the codiceValuta
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return stringa in html che descrive i parametri settati
	 */
	public String getHtmlParameters() {
		StringBuffer sb = new StringBuffer();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		sb.append("<HTML>");
		sb.append("<B>Data:</B> ");
		sb.append(format.format(data));
		sb.append("<BR/>");
		if (listino.getId() != null) {
			sb.append("<B>Listino:</B> ");
			sb.append(listino.getCodice());
			sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
		}
		if (listinoAlternativo.getId() != null) {
			sb.append("<B>Listino alternativo:</B> ");
			sb.append(listinoAlternativo.getCodice());
		}
		if (entita.getId() != null) {
			if (sb.indexOf("Listino") > 0) {
				sb.append("<BR/>");
			}
			sb.append("<B>Entità: </B> ");
			sb.append(entita.getCodice());
			sb.append(" - ");
			sb.append(entita.getAnagrafica().getDenominazione());
			sb.append(" - ");
			sb.append(sedeEntita.getSede().getIndirizzo());
			sb.append(" - ");
			sb.append(sedeEntita.getSede().getDatiGeografici().getDescrizioneLocalita());
		}
		if (agente.getId() != null) {
			sb.append("<B>Agente:</B> ");
			sb.append(agente.getCodice());
			sb.append(" - ");
			sb.append(agente.getAnagrafica().getDenominazione());
		}
		sb.append("</HTML>");
		return sb.toString();
	}

	/**
	 * @return the idZonaGeografica
	 */
	public Integer getIdZonaGeografica() {
		return idZonaGeografica;
	}

	/**
	 * @return the listino
	 */
	public Listino getListino() {
		return listino;
	}

	/**
	 * @return Returns the listinoAlternativo.
	 */
	public Listino getListinoAlternativo() {
		return listinoAlternativo;
	}

	/**
	 * 
	 * @return oggetto avvalorato per il calcolo del prezzo
	 */
	public ParametriCalcoloPrezzi getParametriCalcoloPrezzo() {
		Integer idSedeEntita = null;
		Integer idListino = null;
		Integer idListinoAlternativo = null;
		Integer idArticolo = null;
		Integer idTipoMezzo = null;
		Integer idAgente = null;
		BigDecimal percScontoCommerciale = null;

		if (sedeEntita != null && sedeEntita.getId() != null) {
			idSedeEntita = sedeEntita.getId();
		}

		if (listino != null) {
			idListino = listino.getId();
		}

		if (listinoAlternativo != null) {
			idListinoAlternativo = listinoAlternativo.getId();
		}

		if (articolo != null) {
			idArticolo = articolo.getId();
		}

		if (tipoMezzoTrasporto != null) {
			idTipoMezzo = tipoMezzoTrasporto.getId();
		}
		ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo = ProvenienzaPrezzoArticolo.DOCUMENTO;
		if (articolo != null) {
			provenienzaPrezzoArticolo = articolo.getProvenienzaPrezzoArticolo();
		}

		if (agente != null) {
			idAgente = agente.getId();
		}

		if (codicePagamento != null) {
			percScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
		}

		return new ParametriCalcoloPrezzi(idArticolo, data, idListino, idListinoAlternativo, null, idSedeEntita, null,
				null, provenienzaPrezzo, idTipoMezzo, idZonaGeografica, provenienzaPrezzoArticolo, codiceValuta,
				idAgente, percScontoCommerciale);
	}

	/**
	 * @return the provenienzaPrezzo
	 */
	public ProvenienzaPrezzo getProvenienzaPrezzo() {
		return provenienzaPrezzo;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the tipoMezzoTrasporto
	 */
	public TipoMezzoTrasporto getTipoMezzoTrasporto() {
		return tipoMezzoTrasporto;
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param agente
	 *            the agente to set
	 */
	public void setAgente(AgenteLite agente) {
		this.agente = agente;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param codicePagamento
	 *            the codicePagamento to set
	 */
	public void setCodicePagamento(CodicePagamento codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param codiceValuta
	 *            the codiceValuta to set
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param idZonaGeografica
	 *            the idZonaGeografica to set
	 */
	public void setIdZonaGeografica(Integer idZonaGeografica) {
		this.idZonaGeografica = idZonaGeografica;
	}

	/**
	 * @param listino
	 *            the listino to set
	 */
	public void setListino(Listino listino) {
		this.listino = listino;
	}

	/**
	 * @param listinoAlternativo
	 *            The listinoAlternativo to set.
	 */
	public void setListinoAlternativo(Listino listinoAlternativo) {
		this.listinoAlternativo = listinoAlternativo;
	}

	/**
	 * @param provenienzaPrezzo
	 *            the provenienzaPrezzo to set
	 */
	public void setProvenienzaPrezzo(ProvenienzaPrezzo provenienzaPrezzo) {
		this.provenienzaPrezzo = provenienzaPrezzo;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param tipoMezzoTrasporto
	 *            the tipoMezzoTrasporto to set
	 */
	public void setTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
		this.tipoMezzoTrasporto = tipoMezzoTrasporto;
	}

	@Override
	public String toString() {
		return "ParametriCalcoloPrezziPM [articolo=" + articolo + ", entita=" + entita + ", sedeEntita=" + sedeEntita
				+ ", data=" + data + ", listino=" + listino + ", listinoAlternativo=" + listinoAlternativo
				+ ", provenienzaPrezzo=" + provenienzaPrezzo + ", tipoMezzoTrasporto=" + tipoMezzoTrasporto
				+ ", idZonaGeografica=" + idZonaGeografica + ", effettuaRicerca=" + effettuaRicerca + "]";
	}
}
