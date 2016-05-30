package it.eurotn.panjea.magazzino.util;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contiene i dati statistici per un articolo e deposito.
 *
 * @author giangi
 * @version 1.0, 14/set/2010
 */
public class StatisticaArticolo implements Serializable {
	private static final long serialVersionUID = 999193916857813469L;
	public static final String TUTTI_KEY = "TOTALE DEPOSITI";

	private Integer idArticolo;

	private DepositoLite depositoLite;
	private BigDecimal importoAcquistato;
	private Integer idDocumentoInventario;
	private Date dataInventario;
	private Double qtaAcquistato;
	private BigDecimal importoVenduto;
	private Double qtaVenduto;
	private BigDecimal importoResoAcquistato;
	private Double qtaResoAcquistato;
	private BigDecimal importoResoVenduto;
	private Double qtaResoVenduto;
	private BigDecimal importoCaricoAltro;
	private Double qtaCaricoAltro;
	private BigDecimal importoScaricoAltro;
	private Double qtaScaricoAltro;
	private BigDecimal importoCaricoTrasferimento;
	private Double qtaCaricoTrasferimento;
	private BigDecimal importoScaricoTrasferimento;
	private Double qtaScaricoTrasferimento;
	private Double qtaCaricoProduzione;
	private Double qtaScaricoProduzione;

	private Map<Date, DisponibilitaArticolo> disponibilita;

	private BigDecimal importoInventario;

	private Double qtaInventario;

	private Double giacenza;

	private Date dataUltimaVendita;

	private Integer idUltimoDocumentoVendita;

	private Date dataUltimoAcquisto;
	private Integer idUltimoDocumentoAcquisto;
	private BigDecimal ultimoCosto;
	private Integer idDocumentoUltimoCosto;
	private Date dataUltimoCosto;
	private IndiceGiacenzaArticolo indiceRotazione;

	/**
	 * Costruttore.
	 */
	public StatisticaArticolo() {
		depositoLite = new DepositoLite();
		importoAcquistato = BigDecimal.ZERO;
		importoCaricoAltro = BigDecimal.ZERO;
		importoCaricoTrasferimento = BigDecimal.ZERO;
		importoResoAcquistato = BigDecimal.ZERO;
		importoResoVenduto = BigDecimal.ZERO;
		importoScaricoAltro = BigDecimal.ZERO;
		importoScaricoTrasferimento = BigDecimal.ZERO;
		importoVenduto = BigDecimal.ZERO;

		giacenza = 0.0;
		qtaAcquistato = 0.0;
		qtaCaricoAltro = 0.0;
		qtaCaricoTrasferimento = 0.0;
		qtaResoAcquistato = 0.0;
		qtaResoVenduto = 0.0;
		qtaScaricoAltro = 0.0;
		qtaScaricoTrasferimento = 0.0;
		qtaVenduto = 0.0;
		qtaCaricoProduzione = 0.0;
		qtaScaricoProduzione = 0.0;

		disponibilita = new HashMap<Date, DisponibilitaArticolo>();
	}

	/**
	 *
	 * @param statisticaArticolo
	 *            statistica da aggiungere alla statistica corrente
	 */
	public void add(StatisticaArticolo statisticaArticolo) {
		importoAcquistato = importoAcquistato.add(statisticaArticolo.getImportoAcquistato());
		importoCaricoAltro = importoCaricoAltro.add(statisticaArticolo.getImportoCaricoAltro());
		importoCaricoTrasferimento = importoCaricoTrasferimento.add(statisticaArticolo.getImportoCaricoTrasferimento());
		importoResoAcquistato = importoResoAcquistato.add(statisticaArticolo.getImportoResoAcquistato());
		importoResoVenduto = importoResoVenduto.add(statisticaArticolo.getImportoResoVenduto());
		importoScaricoAltro = importoScaricoAltro.add(statisticaArticolo.getImportoScaricoAltro());
		importoScaricoTrasferimento = importoScaricoTrasferimento.add(statisticaArticolo
				.getImportoScaricoTrasferimento());
		importoVenduto = importoVenduto.add(statisticaArticolo.getImportoVenduto());

		giacenza += statisticaArticolo.getGiacenza() == null ? 0 : statisticaArticolo.getGiacenza();

		qtaAcquistato += statisticaArticolo.qtaAcquistato;
		qtaCaricoAltro += statisticaArticolo.qtaCaricoAltro;
		qtaCaricoTrasferimento += statisticaArticolo.qtaCaricoTrasferimento;
		qtaResoAcquistato += statisticaArticolo.qtaResoAcquistato;
		qtaResoVenduto += statisticaArticolo.qtaResoVenduto;
		qtaScaricoAltro += statisticaArticolo.qtaScaricoAltro;
		qtaScaricoTrasferimento += statisticaArticolo.qtaScaricoTrasferimento;
		qtaVenduto += statisticaArticolo.qtaVenduto == null ? 0 : statisticaArticolo.qtaVenduto;
		qtaCaricoProduzione += statisticaArticolo.getQtaCaricoProduzione();
		qtaScaricoProduzione += statisticaArticolo.getQtaScaricoProduzione();

		if (dataUltimoAcquisto == null) {
			dataUltimoAcquisto = statisticaArticolo.getDataUltimoAcquisto();
			idUltimoDocumentoAcquisto = statisticaArticolo.getIdUltimoDocumentoAcquisto();
		} else if (statisticaArticolo.getDataUltimoAcquisto() != null
				&& dataUltimoAcquisto.compareTo(statisticaArticolo.getDataUltimoAcquisto()) == -1) {
			dataUltimoAcquisto = statisticaArticolo.getDataUltimoAcquisto();
			idUltimoDocumentoAcquisto = statisticaArticolo.getIdUltimoDocumentoAcquisto();
		}

		if (dataUltimaVendita == null) {
			dataUltimaVendita = statisticaArticolo.getDataUltimaVendita();
			idUltimoDocumentoVendita = statisticaArticolo.getIdUltimoDocumentoVendita();
		} else if (statisticaArticolo.getDataUltimaVendita() != null
				&& dataUltimaVendita.compareTo(statisticaArticolo.getDataUltimaVendita()) == -1) {
			dataUltimaVendita = statisticaArticolo.getDataUltimaVendita();
			idUltimoDocumentoVendita = statisticaArticolo.getIdUltimoDocumentoVendita();
		}

		if (statisticaArticolo.getDataUltimoCosto() != null) {
			if (dataUltimoCosto == null
					|| (dataUltimoCosto != null && dataUltimoCosto.compareTo(statisticaArticolo.getDataUltimoCosto()) == -1)) {
				dataUltimoCosto = statisticaArticolo.getDataUltimoCosto();
				ultimoCosto = statisticaArticolo.getUltimoCosto();
				idDocumentoUltimoCosto = statisticaArticolo.getIdDocumentoUltimoCosto();
			}
		}
		aggiungiDisponibilita(statisticaArticolo.getDisponibilita());
	}

	/**
	 * Aggiunge le disponibilità giornaliere, aggiunge diso. se esiste già quella data oppure aggiunge la disponibilità.
	 *
	 * @param list
	 *            lista di disponibilità da aggiungere
	 */
	private void aggiungiDisponibilita(Collection<DisponibilitaArticolo> list) {
		for (DisponibilitaArticolo disponibilitaArticoloDaAggiungere : list) {
			if (disponibilita.containsKey(disponibilitaArticoloDaAggiungere.getDataConsegna())) {
				DisponibilitaArticolo dispCorrente = disponibilita.get(disponibilitaArticoloDaAggiungere
						.getDataConsegna());
				dispCorrente.add(disponibilitaArticoloDaAggiungere);
			} else {
				disponibilita.put(disponibilitaArticoloDaAggiungere.getDataConsegna(),
						disponibilitaArticoloDaAggiungere);
			}
		}
	}

	/**
	 * @return Returns the dataInventario.
	 */
	public Date getDataInventario() {
		return dataInventario;
	}

	/**
	 * @return Returns the dataUltimaVendita.
	 * @uml.property name="dataUltimaVendita"
	 */
	public Date getDataUltimaVendita() {
		return dataUltimaVendita;
	}

	/**
	 * @return Returns the dataUltimoAcquisto.
	 * @uml.property name="dataUltimoAcquisto"
	 */
	public Date getDataUltimoAcquisto() {
		return dataUltimoAcquisto;
	}

	/**
	 * @return Returns the dataUltimoCosto.
	 * @uml.property name="dataUltimoCosto"
	 */
	public Date getDataUltimoCosto() {
		return dataUltimoCosto;
	}

	/**
	 * @return Returns the depositoLite.
	 * @uml.property name="depositoLite"
	 */
	public DepositoLite getDepositoLite() {
		return depositoLite;
	}

	/**
	 * @return Returns the disponibilita.
	 */
	public List<DisponibilitaArticolo> getDisponibilita() {
		return new ArrayList<DisponibilitaArticolo>(disponibilita.values());
	}

	/**
	 * @return Returns the giacenza.
	 * @uml.property name="giacenza"
	 */
	public Double getGiacenza() {
		return giacenza;
	}

	/**
	 * @return the idArticolo
	 */
	public Integer getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idDocumentoInventario.
	 */
	public Integer getIdDocumentoInventario() {
		return idDocumentoInventario;
	}

	/**
	 * @return Returns the idDocumentoUltimoCosto.
	 * @uml.property name="idDocumentoUltimoCosto"
	 */
	public Integer getIdDocumentoUltimoCosto() {
		return idDocumentoUltimoCosto;
	}

	/**
	 * @return Returns the idUltimoDocumentoAcquisto.
	 * @uml.property name="idUltimoDocumentoAcquisto"
	 */
	public Integer getIdUltimoDocumentoAcquisto() {
		return idUltimoDocumentoAcquisto;
	}

	/**
	 * @return Returns the idUltimoDocumentoVendita.
	 * @uml.property name="idUltimoDocumentoVendita"
	 */
	public Integer getIdUltimoDocumentoVendita() {
		return idUltimoDocumentoVendita;
	}

	/**
	 * @return Returns the importoAcquistato.
	 * @uml.property name="importoAcquistato"
	 */
	public BigDecimal getImportoAcquistato() {
		return importoAcquistato;
	}

	/**
	 * @return Returns the importoCaricoAltro.
	 * @uml.property name="importoCaricoAltro"
	 */
	public BigDecimal getImportoCaricoAltro() {
		return importoCaricoAltro;
	}

	/**
	 * @return Returns the importoCaricoTrasferimento.
	 * @uml.property name="importoCaricoTrasferimento"
	 */
	public BigDecimal getImportoCaricoTrasferimento() {
		return importoCaricoTrasferimento;
	}

	/**
	 * @return Returns the importoInventario.
	 * @uml.property name="importoInventario"
	 */
	public BigDecimal getImportoInventario() {
		return importoInventario;
	}

	/**
	 * @return Returns the importoResoAcquistato.
	 * @uml.property name="importoResoAcquistato"
	 */
	public BigDecimal getImportoResoAcquistato() {
		return importoResoAcquistato;
	}

	/**
	 * @return Returns the importoResoVenduto.
	 * @uml.property name="importoResoVenduto"
	 */
	public BigDecimal getImportoResoVenduto() {
		return importoResoVenduto;
	}

	/**
	 * @return Returns the importoScaricoAltro.
	 * @uml.property name="importoScaricoAltro"
	 */
	public BigDecimal getImportoScaricoAltro() {
		return importoScaricoAltro;
	}

	/**
	 * @return Returns the importoScaricoTrasferimento.
	 * @uml.property name="importoScaricoTrasferimento"
	 */
	public BigDecimal getImportoScaricoTrasferimento() {
		return importoScaricoTrasferimento;
	}

	/**
	 * @return Returns the importoVenduto.
	 * @uml.property name="importoVenduto"
	 */
	public BigDecimal getImportoVenduto() {
		return importoVenduto;
	}

	/**
	 * @return Returns the indiceRotazione.
	 */
	public IndiceGiacenzaArticolo getIndiceRotazione() {
		if (indiceRotazione == null) {
			return new IndiceGiacenzaArticolo(0.0, 0.0, null, null);
		}
		return indiceRotazione;
	}

	/**
	 * @return Returns the qtaAcquistato.
	 * @uml.property name="qtaAcquistato"
	 */
	public Double getQtaAcquistato() {
		return qtaAcquistato;
	}

	/**
	 * @return Returns the qtaCaricoAltro.
	 * @uml.property name="qtaCaricoAltro"
	 */
	public Double getQtaCaricoAltro() {
		return qtaCaricoAltro;
	}

	/**
	 * @return Returns the qtaCaricoProduzione.
	 */
	public Double getQtaCaricoProduzione() {
		return qtaCaricoProduzione;
	}

	/**
	 * @return Returns the qtaCaricoTrasferimento.
	 * @uml.property name="qtaCaricoTrasferimento"
	 */
	public Double getQtaCaricoTrasferimento() {
		return qtaCaricoTrasferimento;
	}

	/**
	 * @return Returns the qtaInventario.
	 * @uml.property name="qtaInventario"
	 */
	public Double getQtaInventario() {
		return qtaInventario;
	}

	/**
	 * @return Returns the qtaResoAcquistato.
	 * @uml.property name="qtaResoAcquistato"
	 */
	public Double getQtaResoAcquistato() {
		return qtaResoAcquistato;
	}

	/**
	 * @return Returns the qtaResoVenduto.
	 * @uml.property name="qtaResoVenduto"
	 */
	public Double getQtaResoVenduto() {
		return qtaResoVenduto;
	}

	/**
	 * @return Returns the qtaScaricoAltro.
	 * @uml.property name="qtaScaricoAltro"
	 */
	public Double getQtaScaricoAltro() {
		return qtaScaricoAltro;
	}

	/**
	 * @return Returns the qtaScaricoProduzione.
	 */
	public Double getQtaScaricoProduzione() {
		return qtaScaricoProduzione;
	}

	/**
	 * @return Returns the qtaScaricoTrasferimento.
	 * @uml.property name="qtaScaricoTrasferimento"
	 */
	public Double getQtaScaricoTrasferimento() {
		return qtaScaricoTrasferimento;
	}

	/**
	 * @return Returns the qtaVenduto.
	 * @uml.property name="qtaVenduto"
	 */
	public Double getQtaVenduto() {
		return qtaVenduto;
	}

	/**
	 * @return Returns the ultimoCosto.
	 * @uml.property name="ultimoCosto"
	 */
	public BigDecimal getUltimoCosto() {
		return ultimoCosto;
	}

	/**
	 * @return <code>true</code> se nuova
	 */
	public boolean isNew() {
		return idArticolo == null && depositoLite.isNew();
	}

	/**
	 *
	 * @param codice
	 *            codice del deposito
	 */
	public void setCodiceDeposito(String codice) {
		depositoLite.setCodice(codice);
	}

	/**
	 *
	 * @param codiceTipoDeposito
	 *            .
	 */
	public void setCodiceTipoDeposito(String codiceTipoDeposito) {
		depositoLite.getTipoDeposito().setCodice(codiceTipoDeposito);
	}

	/**
	 * @param dataInventario
	 *            The dataInventario to set.
	 */
	public void setDataInventario(Date dataInventario) {
		this.dataInventario = dataInventario;
	}

	/**
	 * @param dataUltimaVendita
	 *            The dataUltimaVendita to set.
	 * @uml.property name="dataUltimaVendita"
	 */
	public void setDataUltimaVendita(Date dataUltimaVendita) {
		this.dataUltimaVendita = dataUltimaVendita;
	}

	/**
	 * @param dataUltimoAcquisto
	 *            The dataUltimoAcquisto to set.
	 * @uml.property name="dataUltimoAcquisto"
	 */
	public void setDataUltimoAcquisto(Date dataUltimoAcquisto) {
		this.dataUltimoAcquisto = dataUltimoAcquisto;
	}

	/**
	 * @param dataUltimoCosto
	 *            The dataUltimoCosto to set.
	 * @uml.property name="dataUltimoCosto"
	 */
	public void setDataUltimoCosto(Date dataUltimoCosto) {
		this.dataUltimoCosto = dataUltimoCosto;
	}

	/**
	 * @param depositoLite
	 *            the depositoLite to set
	 */
	public void setDepositoLite(DepositoLite depositoLite) {
		this.depositoLite = depositoLite;
	}

	/**
	 *
	 * @param descrizione
	 *            descrizione del deposito
	 */
	public void setDescrizioneDeposito(String descrizione) {
		depositoLite.setDescrizione(descrizione);
	}

	/**
	 * @param disponibilita
	 *            The disponibilita to set.
	 */
	public void setDisponibilita(List<DisponibilitaArticolo> disponibilita) {
		this.disponibilita = new HashMap<Date, DisponibilitaArticolo>();
		for (DisponibilitaArticolo disponibilitaArticolo : disponibilita) {
			this.disponibilita.put(disponibilitaArticolo.getDataConsegna(), disponibilitaArticolo);
		}
	}

	/**
	 * @param giacenza
	 *            The giacenza to set.
	 * @uml.property name="giacenza"
	 */
	public void setGiacenza(Double giacenza) {
		this.giacenza = giacenza;
	}

	/**
	 * @param idArticolo
	 *            the idArticolo to set
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 *
	 * @param id
	 *            id del deposito
	 */
	public void setIdDeposito(Integer id) {
		depositoLite.setId(id);
	}

	/**
	 * @param idDocumentoInventario
	 *            The idDocumentoInventario to set.
	 */
	public void setIdDocumentoInventario(Integer idDocumentoInventario) {
		this.idDocumentoInventario = idDocumentoInventario;
	}

	/**
	 * @param idDocumentoUltimoCosto
	 *            The idDocumentoUltimoCosto to set.
	 * @uml.property name="idDocumentoUltimoCosto"
	 */
	public void setIdDocumentoUltimoCosto(Integer idDocumentoUltimoCosto) {
		this.idDocumentoUltimoCosto = idDocumentoUltimoCosto;
	}

	/**
	 * @param idUltimoDocumentoAcquisto
	 *            The idUltimoDocumentoAcquisto to set.
	 * @uml.property name="idUltimoDocumentoAcquisto"
	 */
	public void setIdUltimoDocumentoAcquisto(Integer idUltimoDocumentoAcquisto) {
		this.idUltimoDocumentoAcquisto = idUltimoDocumentoAcquisto;
	}

	/**
	 * @param idUltimoDocumentoVendita
	 *            The idUltimoDocumentoVendita to set.
	 * @uml.property name="idUltimoDocumentoVendita"
	 */
	public void setIdUltimoDocumentoVendita(Integer idUltimoDocumentoVendita) {
		this.idUltimoDocumentoVendita = idUltimoDocumentoVendita;
	}

	/**
	 * @param importoAcquistato
	 *            The importoAcquistato to set.
	 * @uml.property name="importoAcquistato"
	 */
	public void setImportoAcquistato(BigDecimal importoAcquistato) {
		this.importoAcquistato = importoAcquistato;
	}

	/**
	 * @param importoCaricoAltro
	 *            The importoCaricoAltro to set.
	 * @uml.property name="importoCaricoAltro"
	 */
	public void setImportoCaricoAltro(BigDecimal importoCaricoAltro) {
		this.importoCaricoAltro = importoCaricoAltro;
	}

	/**
	 * @param importoCaricoTrasferimento
	 *            The importoCaricoTrasferimento to set.
	 * @uml.property name="importoCaricoTrasferimento"
	 */
	public void setImportoCaricoTrasferimento(BigDecimal importoCaricoTrasferimento) {
		this.importoCaricoTrasferimento = importoCaricoTrasferimento;
	}

	/**
	 * @param importoInventario
	 *            The importoInventario to set.
	 * @uml.property name="importoInventario"
	 */
	public void setImportoInventario(BigDecimal importoInventario) {
		this.importoInventario = importoInventario;
	}

	/**
	 * @param importoResoAcquistato
	 *            The importoResoAcquistato to set.
	 * @uml.property name="importoResoAcquistato"
	 */
	public void setImportoResoAcquistato(BigDecimal importoResoAcquistato) {
		this.importoResoAcquistato = importoResoAcquistato;
	}

	/**
	 * @param importoResoVenduto
	 *            The importoResoVenduto to set.
	 * @uml.property name="importoResoVenduto"
	 */
	public void setImportoResoVenduto(BigDecimal importoResoVenduto) {
		this.importoResoVenduto = importoResoVenduto;
	}

	/**
	 * @param importoScaricoAltro
	 *            The importoScaricoAltro to set.
	 * @uml.property name="importoScaricoAltro"
	 */
	public void setImportoScaricoAltro(BigDecimal importoScaricoAltro) {
		this.importoScaricoAltro = importoScaricoAltro;
	}

	/**
	 * @param importoScaricoTrasferimento
	 *            The importoScaricoTrasferimento to set.
	 * @uml.property name="importoScaricoTrasferimento"
	 */
	public void setImportoScaricoTrasferimento(BigDecimal importoScaricoTrasferimento) {
		this.importoScaricoTrasferimento = importoScaricoTrasferimento;
	}

	/**
	 * @param importoVenduto
	 *            The importoVenduto to set.
	 * @uml.property name="importoVenduto"
	 */
	public void setImportoVenduto(BigDecimal importoVenduto) {
		this.importoVenduto = importoVenduto;
	}

	/**
	 * @param indiceRotazione
	 *            The indiceRotazione to set.
	 */
	public void setIndiceRotazione(IndiceGiacenzaArticolo indiceRotazione) {
		this.indiceRotazione = indiceRotazione;
	}

	/**
	 *
	 * @param principale
	 *            deposito principale to set
	 */
	public void setPrincipaleDeposito(boolean principale) {
		depositoLite.setPrincipale(principale);
	}

	/**
	 * @param qtaAcquistato
	 *            The qtaAcquistato to set.
	 * @uml.property name="qtaAcquistato"
	 */
	public void setQtaAcquistato(Double qtaAcquistato) {
		this.qtaAcquistato = qtaAcquistato;
	}

	/**
	 * @param qtaCaricoAltro
	 *            The qtaCaricoAltro to set.
	 * @uml.property name="qtaCaricoAltro"
	 */
	public void setQtaCaricoAltro(Double qtaCaricoAltro) {
		this.qtaCaricoAltro = qtaCaricoAltro;
	}

	/**
	 * @param qtaCaricoProduzione
	 *            The qtaCaricoProduzione to set.
	 */
	public void setQtaCaricoProduzione(Double qtaCaricoProduzione) {
		this.qtaCaricoProduzione = qtaCaricoProduzione;
	}

	/**
	 * @param qtaCaricoTrasferimento
	 *            The qtaCaricoTrasferimento to set.
	 * @uml.property name="qtaCaricoTrasferimento"
	 */
	public void setQtaCaricoTrasferimento(Double qtaCaricoTrasferimento) {
		this.qtaCaricoTrasferimento = qtaCaricoTrasferimento;
	}

	/**
	 * @param qtaInventario
	 *            The qtaInventario to set.
	 * @uml.property name="qtaInventario"
	 */
	public void setQtaInventario(Double qtaInventario) {
		this.qtaInventario = qtaInventario;
	}

	/**
	 * @param qtaResoAcquistato
	 *            The qtaResoAcquistato to set.
	 * @uml.property name="qtaResoAcquistato"
	 */
	public void setQtaResoAcquistato(Double qtaResoAcquistato) {
		this.qtaResoAcquistato = qtaResoAcquistato;
	}

	/**
	 * @param qtaResoVenduto
	 *            The qtaResoVenduto to set.
	 * @uml.property name="qtaResoVenduto"
	 */
	public void setQtaResoVenduto(Double qtaResoVenduto) {
		this.qtaResoVenduto = qtaResoVenduto;
	}

	/**
	 * @param qtaScaricoAltro
	 *            The qtaScaricoAltro to set.
	 * @uml.property name="qtaScaricoAltro"
	 */
	public void setQtaScaricoAltro(Double qtaScaricoAltro) {
		this.qtaScaricoAltro = qtaScaricoAltro;
	}

	/**
	 * @param qtaScaricoProduzione
	 *            The qtaScaricoProduzione to set.
	 */
	public void setQtaScaricoProduzione(Double qtaScaricoProduzione) {
		this.qtaScaricoProduzione = qtaScaricoProduzione;
	}

	/**
	 * @param qtaScaricoTrasferimento
	 *            The qtaScaricoTrasferimento to set.
	 * @uml.property name="qtaScaricoTrasferimento"
	 */
	public void setQtaScaricoTrasferimento(Double qtaScaricoTrasferimento) {
		this.qtaScaricoTrasferimento = qtaScaricoTrasferimento;
	}

	/**
	 * @param qtaVenduto
	 *            The qtaVenduto to set.
	 * @uml.property name="qtaVenduto"
	 */
	public void setQtaVenduto(Double qtaVenduto) {
		this.qtaVenduto = qtaVenduto;
	}

	/**
	 * @param ultimoCosto
	 *            The ultimoCosto to set.
	 * @uml.property name="ultimoCosto"
	 */
	public void setUltimoCosto(BigDecimal ultimoCosto) {
		this.ultimoCosto = ultimoCosto;
	}
}
