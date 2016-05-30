package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class SituazioneEffetto implements Serializable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum StatoCarrello {

		SELEZIONABILE, AGGIUNTO_CARRELLO, NON_SELEZIONABILE, DA_AGGIUNGERE
	}

	private static final long serialVersionUID = 94825935636496508L;

	private Effetto effetto;
	private Integer idEffetto;
	private Integer versionEffetto;
	private Date dataValuta;
	private DataValutaEffetto dataValutaEffetto;
	private BigDecimal importo;
	private String codiceValuta;
	private Date dataPagamento;
	private boolean insoluto;

	private Integer idAreaEffetto;

	private Integer versionAreaEffetto;
	private Date dataScadenza;

	private RapportoBancarioAzienda rapportoBancario;

	private Integer idRapportoBancario;
	private Integer versionRapportoBancario;
	private String descrizioneRapportoBancario;
	private BigDecimal speseInsolutoRapportoBancario;

	private Documento documento;

	private String codiceTipoDocumento;

	private String descrizioneTipoDocumento;
	private Integer tipoEntitaTipoDocumento;
	private Integer idDocumento;
	private String numeroDocumento;
	private Date dataDocumento;
	private String codiceTipoDocumentoDistinta;

	private String descrizioneTipoDocumentoDistinta;
	private Integer tipoEntitaTipoDocumentoDistinta;
	private Integer idDocumentoDistinta;
	private String numeroDocumentoDistinta;
	private Date dataDocumentoDistinta;
	private Integer idAreaDistinta;
	private Integer versionAreaDistinta;
	private EntitaLite entita;

	private Integer idEntitaRata;
	private Integer codiceEntitaRata;
	private String denominazioneEntitaRata;
	private String tipoEntitaRata;
	private StatoCarrello statoCarrello;

	/**
	 * Costruttore.
	 */
	public SituazioneEffetto() {
		super();
		this.tipoEntitaRata = "C";
		statoCarrello = StatoCarrello.SELEZIONABILE;
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
		SituazioneEffetto other = (SituazioneEffetto) obj;
		if (idEffetto == null) {
			if (other.idEffetto != null) {
				return false;
			}
		} else if (!idEffetto.equals(other.idEffetto)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the areaEffetti
	 */
	public AreaEffetti getAreaEffetto() {

		AreaEffetti areaEffetti = new AreaEffetti();

		if (idAreaDistinta != null) {
			areaEffetti.setId(idAreaDistinta);
			areaEffetti.setVersion(versionAreaDistinta);
		} else {
			areaEffetti.setId(idAreaEffetto);
			areaEffetti.setVersion(versionAreaEffetto);
		}
		areaEffetti.setDocumento(getDocumento());

		return areaEffetti;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the dataValutaEffetto
	 */
	public DataValutaEffetto getDataValutaEffetto() {
		if (dataValutaEffetto == null) {

			if (dataValuta != null) {
				dataValutaEffetto = new DataValutaEffetto(dataValuta);
			}
		}

		return dataValutaEffetto;
	}

	/**
	 * @return the documento
	 */
	private Documento getDocumento() {
		if (documento == null) {
			documento = new Documento();
			TipoDocumento tipoDocumento = new TipoDocumento();

			if (idDocumentoDistinta != null) {
				tipoDocumento.setCodice(codiceTipoDocumentoDistinta);
				tipoDocumento.setDescrizione(descrizioneTipoDocumentoDistinta);
				TipoEntita te = TipoEntita.values()[tipoEntitaTipoDocumentoDistinta];
				tipoDocumento.setTipoEntita(te);

				documento.setId(idDocumentoDistinta);
				CodiceDocumento codiceDocumento = new CodiceDocumento();
				codiceDocumento.setCodice(numeroDocumentoDistinta);
				documento.setCodice(codiceDocumento);
				documento.setDataDocumento(dataDocumentoDistinta);
			} else {
				if (idDocumento != null) {
					tipoDocumento.setCodice(codiceTipoDocumento);
					tipoDocumento.setDescrizione(descrizioneTipoDocumento);
					TipoEntita te = TipoEntita.values()[tipoEntitaTipoDocumento];
					tipoDocumento.setTipoEntita(te);

					documento.setId(idDocumento);
					CodiceDocumento codiceDocumento = new CodiceDocumento();
					codiceDocumento.setCodice(numeroDocumento);
					documento.setCodice(codiceDocumento);
					documento.setDataDocumento(dataDocumento);
				} else {
					throw new UnsupportedOperationException("Nessun documento recupoerato");
				}
			}

			documento.setTipoDocumento(tipoDocumento);
		}

		return documento;
	}

	/**
	 * @return the effetto
	 */
	public Effetto getEffetto() {
		if (effetto == null) {
			effetto = new Effetto();
			effetto.setId(idEffetto);
			effetto.setVersion(versionEffetto);
			effetto.setDataValuta(dataValuta);
			effetto.setDataScadenza(dataScadenza);

			Importo importoEffetto = new Importo(codiceValuta, BigDecimal.ONE);
			importoEffetto.setImportoInValuta(importo);
			importoEffetto.setImportoInValutaAzienda(importo);
			effetto.setImporto(importoEffetto);

			if (dataPagamento != null) {
				effetto.setStatoEffetto(StatoEffetto.CHIUSO);
			}
			if (dataPagamento == null) {
				effetto.setStatoEffetto(StatoEffetto.PRESENTATO);
			}
			if (insoluto) {
				effetto.setStatoEffetto(StatoEffetto.INSOLUTO);
			}

			Documento doc = getDocumento();
			doc.setRapportoBancarioAzienda(getRapportoBancario());

			AreaEffetti areaEffetti = new AreaEffetti();
			areaEffetti.setId(idAreaEffetto);
			areaEffetti.setVersion(versionAreaEffetto);
			areaEffetti.setDocumento(doc);

			effetto.setAreaEffetti(areaEffetti);

		}

		return effetto;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		if (entita == null) {
			if ("C".equals(tipoEntitaRata)) {
				entita = new ClienteLite();
			} else if ("F".equals(tipoEntitaRata)) {
				entita = new FornitoreLite();
			} else {
				throw new UnsupportedOperationException("Tipo entita " + tipoEntitaRata + " non gestita.");
			}
			entita.setCodice(codiceEntitaRata);
			entita.setId(idEntitaRata);

			AnagraficaLite anagraficaLite = new AnagraficaLite();
			anagraficaLite.setDenominazione(denominazioneEntitaRata);
			entita.setAnagrafica(anagraficaLite);
		}

		return entita;
	}

	/**
	 * @return the idAreaEffetto
	 */
	public Integer getIdAreaEffetto() {
		return idAreaEffetto;
	}

	/**
	 * @return the rapportoBancario
	 */
	public RapportoBancarioAzienda getRapportoBancario() {
		if (rapportoBancario == null) {
			rapportoBancario = new RapportoBancarioAzienda();
			rapportoBancario.setId(idRapportoBancario);
			rapportoBancario.setVersion(versionRapportoBancario);
			rapportoBancario.setDescrizione(descrizioneRapportoBancario);
			rapportoBancario.setSpeseInsoluto(speseInsolutoRapportoBancario);
		}

		return rapportoBancario;
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
	public StatoCarrello getStatoCarrelloFromStatoEffetto() {
		if (dataPagamento == null || getDataValutaEffetto() != null) {
			return StatoCarrello.SELEZIONABILE;
		} else {
			return StatoCarrello.NON_SELEZIONABILE;
		}
	}

	/**
	 * @return the versionAreaEffetto
	 */
	public Integer getVersionAreaEffetto() {
		return versionAreaEffetto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEffetto == null) ? 0 : idEffetto.hashCode());
		return result;
	}

	/**
	 * @param codiceEntitaRata
	 *            the codiceEntitaRata to set
	 */
	public void setCodiceEntitaRata(Integer codiceEntitaRata) {
		this.codiceEntitaRata = codiceEntitaRata;
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.codiceTipoDocumento = codiceTipoDocumento;
	}

	/**
	 * @param codiceTipoDocumentoDistinta
	 *            the codiceTipoDocumentoDistinta to set
	 */
	public void setCodiceTipoDocumentoDistinta(String codiceTipoDocumentoDistinta) {
		this.codiceTipoDocumentoDistinta = codiceTipoDocumentoDistinta;
	}

	/**
	 * @param codiceValuta
	 *            the codiceValuta to set
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataDocumentoDistinta
	 *            the dataDocumentoDistinta to set
	 */
	public void setDataDocumentoDistinta(Date dataDocumentoDistinta) {
		this.dataDocumentoDistinta = dataDocumentoDistinta;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param dataValuta
	 *            the dataValuta to set
	 */
	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}

	/**
	 * @param dataValutaEffetto
	 *            the dataValuta to set
	 */
	public void setDataValutaEffetto(DataValutaEffetto dataValutaEffetto) {
	}

	/**
	 * @param denominazioneEntitaRata
	 *            the denominazioneEntitaRata to set
	 */
	public void setDenominazioneEntitaRata(String denominazioneEntitaRata) {
		this.denominazioneEntitaRata = denominazioneEntitaRata;
	}

	/**
	 * @param descrizioneRapportoBancario
	 *            the descrizioneRapportoBancario to set
	 */
	public void setDescrizioneRapportoBancario(String descrizioneRapportoBancario) {
		this.descrizioneRapportoBancario = descrizioneRapportoBancario;
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.descrizioneTipoDocumento = descrizioneTipoDocumento;
	}

	/**
	 * @param descrizioneTipoDocumentoDistinta
	 *            the descrizioneTipoDocumentoDistinta to set
	 */
	public void setDescrizioneTipoDocumentoDistinta(String descrizioneTipoDocumentoDistinta) {
		this.descrizioneTipoDocumentoDistinta = descrizioneTipoDocumentoDistinta;
	}

	/**
	 * @param idAreaDistinta
	 *            the idAreaDistinta to set
	 */
	public void setIdAreaDistinta(Integer idAreaDistinta) {
		this.idAreaDistinta = idAreaDistinta;
	}

	/**
	 * @param idAreaEffetto
	 *            the idAreaEffetto to set
	 */
	public void setIdAreaEffetto(Integer idAreaEffetto) {
		this.idAreaEffetto = idAreaEffetto;
	}

	/**
	 * @param idDocumento
	 *            the idDocumento to set
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @param idDocumentoDistinta
	 *            the idDocumentoDistinta to set
	 */
	public void setIdDocumentoDistinta(Integer idDocumentoDistinta) {
		this.idDocumentoDistinta = idDocumentoDistinta;
	}

	/**
	 * @param idEffetto
	 *            the idEffetto to set
	 */
	public void setIdEffetto(Integer idEffetto) {
		this.idEffetto = idEffetto;
	}

	/**
	 * @param idEntitaRata
	 *            the idEntitaRata to set
	 */
	public void setIdEntitaRata(Integer idEntitaRata) {
		this.idEntitaRata = idEntitaRata;
	}

	/**
	 * @param idRapportoBancario
	 *            the idRapportoBancario to set
	 */
	public void setIdRapportoBancario(Integer idRapportoBancario) {
		this.idRapportoBancario = idRapportoBancario;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @param insoluto
	 *            the insoluto to set
	 */
	public void setInsoluto(boolean insoluto) {
		this.insoluto = insoluto;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param numeroDocumentoDistinta
	 *            the numeroDocumentoDistinta to set
	 */
	public void setNumeroDocumentoDistinta(String numeroDocumentoDistinta) {
		this.numeroDocumentoDistinta = numeroDocumentoDistinta;
	}

	/**
	 * @param speseInsolutoRapportoBancario
	 *            The speseInsolutoRapportoBancario to set.
	 */
	public void setSpeseInsolutoRapportoBancario(BigDecimal speseInsolutoRapportoBancario) {
		this.speseInsolutoRapportoBancario = speseInsolutoRapportoBancario;
	}

	/**
	 * @param statoCarrello
	 *            the statoCarrello to set
	 */
	public void setStatoCarrello(StatoCarrello statoCarrello) {
		this.statoCarrello = statoCarrello;
	}

	/**
	 * @param tipoEntitaRata
	 *            the tipoEntitaRata to set
	 */
	public void setTipoEntitaRata(String tipoEntitaRata) {
		this.tipoEntitaRata = tipoEntitaRata;
	}

	/**
	 * @param versionAreaDistinta
	 *            the versionAreaDistinta to set
	 */
	public void setVersionAreaDistinta(Integer versionAreaDistinta) {
		this.versionAreaDistinta = versionAreaDistinta;
	}

	/**
	 * @param versionAreaEffetto
	 *            the versionAreaEffetto to set
	 */
	public void setVersionAreaEffetto(Integer versionAreaEffetto) {
		this.versionAreaEffetto = versionAreaEffetto;
	}

	/**
	 * @param versionEffetto
	 *            the versionEffetto to set
	 */
	public void setVersionEffetto(Integer versionEffetto) {
		this.versionEffetto = versionEffetto;
	}

	/**
	 * @param versionRapportoBancario
	 *            the versionRapportoBancario to set
	 */
	public void setVersionRapportoBancario(Integer versionRapportoBancario) {
		this.versionRapportoBancario = versionRapportoBancario;
	}

}
