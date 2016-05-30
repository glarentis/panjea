package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaGenerazioneDocumentoFatturazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.math.BigDecimal;
import java.util.Date;

public class RigaMagazzinoFatturazione {

	private Integer idArea;
	private Integer versionArea;
	private Date dataRegistrazioneArea;
	private String descrizioneSedeArea;
	private Integer codiceEntitaArea;
	private String denominazioneEntitaArea;

	private AreaMagazzino areaMagazzino;

	private boolean raggruppamentoBolle;

	private boolean ereditaDatiCommerciali;

	private Integer idSedeEntita;

	private Integer versionSedeEntita;

	private Integer idSedeEntitaPrincipale;

	private Integer versionSedeEntitaPrincipale;

	private SedeEntita sedeEntita;

	private SedeEntita sedeEntitaPrincipale;
	private Integer idEntita;
	private Integer versionEntita;
	private EntitaLite entita;
	private Integer idSedeMagazzino;
	private Integer versionSedeMagazzino;

	private ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino;
	private Integer idSedeMagazzinoPrincipale;
	private Integer versionSedeMagazzinoPrincipale;

	private ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale;
	private SedeMagazzino sedeMagazzino;
	private TipoDocumento tipoDocumentoPerFatturazione;
	private String tipoDocumentoPerFatturazioneDescrizioneMaschera;
	private Integer idTipoAreaMagazzinoPerFatturazione;

	private Integer versionTipoAreaMagazzinoPerFatturazione;
	private TipoAreaMagazzino tipoAreaMagazzinoPerFatturazione;
	private Date dataDocumento;
	private CodiceDocumento numeroDocumento;

	private String codiceTipoDocumento;
	private String descrizioneTipoDocumento;
	private Integer idCodicePagamento;
	private Integer versionCodicePagamento;

	private BigDecimal importoSpese;
	private CodicePagamento codicePagamento;

	private boolean addebitoSpeseIncasso;

	private BigDecimal speseIncasso;

	private Integer annoMovimento;

	private String codiceValuta;

	private RigaMagazzino rigaMagazzino;

	private int hashCode = Integer.MIN_VALUE;

	/**
	 * Costruttore.
	 * 
	 */
	public RigaMagazzinoFatturazione() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (this.getRigaMagazzino() != null && this.getRigaMagazzino().isNew()) {
			return false;
		}
		if (!(obj instanceof RigaMagazzinoFatturazione)) {
			return false;
		}
		// Se è una classe proxy il nome della classe diventa xxx_$$_proxy,
		// quindi uso la startWith
		if (!obj.getClass().getCanonicalName().startsWith(this.getClass().getCanonicalName())) {
			return false;
		}
		return (this.getRigaMagazzino().getId().equals(((RigaMagazzinoFatturazione) obj).getRigaMagazzino().getId()));
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @return the areaMagazzino
	 */
	public AreaMagazzino getAreaMagazzino() {
		if (areaMagazzino == null) {
			areaMagazzino = new AreaMagazzino();
			areaMagazzino.setId(idArea);
			areaMagazzino.setVersion(versionArea);
			areaMagazzino.setDataRegistrazione(dataRegistrazioneArea);
			areaMagazzino.getDocumento().setCodice(numeroDocumento);
			areaMagazzino.getDocumento().getTipoDocumento().setCodice(codiceTipoDocumento);
			areaMagazzino.getDocumento().getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
			areaMagazzino.getDocumento().setDataDocumento(dataDocumento);
			areaMagazzino.getDocumento().setEntita(new ClienteLite());
			areaMagazzino.getDocumento().getEntita().setCodice(codiceEntitaArea);
			areaMagazzino.getDocumento().getEntita().getAnagrafica().setDenominazione(denominazioneEntitaArea);
			areaMagazzino.getDocumento().setSedeEntita(new SedeEntita());
			areaMagazzino.getDocumento().getSedeEntita().setId(idSedeEntita);
			areaMagazzino.getDocumento().getSedeEntita().setVersion(versionSedeEntita);
			areaMagazzino.getDocumento().getSedeEntita().getSede().setDescrizione(descrizioneSedeArea);
			areaMagazzino.getDocumento().getSedeEntita().setEntita(new Cliente());
			areaMagazzino.getDocumento().getSedeEntita().getEntita().setCodice(codiceEntitaArea);
			areaMagazzino.getDocumento().getSedeEntita().getEntita().getAnagrafica()
					.setDenominazione(denominazioneEntitaArea);
			areaMagazzino.setTipoAreaMagazzino(new TipoAreaMagazzino());
			areaMagazzino.getTipoAreaMagazzino().setTipoDocumentoPerFatturazioneDescrizioneMaschera(
					tipoDocumentoPerFatturazioneDescrizioneMaschera);
		}

		return areaMagazzino;
	}

	/**
	 * @return the codicePagamento
	 */
	public CodicePagamento getCodicePagamento() {
		if (codicePagamento == null) {
			codicePagamento = new CodicePagamento();
			codicePagamento.setId(idCodicePagamento);
			codicePagamento.setVersion(versionCodicePagamento);
			codicePagamento.setImportoSpese(importoSpese);
		}

		return codicePagamento;
	}

	/**
	 * @return the codiceTipoDocumento
	 */
	public String getCodiceTipoDocumento() {
		return codiceTipoDocumento;
	}

	/**
	 * @return the codiceValuta
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		if (entita == null) {
			entita = new ClienteLite();
			entita.setId(idEntita);
			entita.setVersion(versionEntita);
		}

		return entita;
	}

	/**
	 * @return the idArea
	 */
	public Integer getIdArea() {
		return idArea;
	}

	/**
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the rigaMagazzino
	 */
	public RigaMagazzino getRigaMagazzino() {
		return rigaMagazzino;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		if (sedeEntita == null) {
			sedeEntita = new SedeEntita();
			sedeEntita.setId(idSedeEntita);
			sedeEntita.setVersion(versionSedeEntita);
		}

		return sedeEntita;
	}

	/**
	 * @return the sedeEntitaPrincipale
	 */
	public SedeEntita getSedeEntitaPrincipale() {
		if (sedeEntitaPrincipale == null) {
			sedeEntitaPrincipale = new SedeEntita();
			sedeEntitaPrincipale.setId(idSedeEntitaPrincipale);
			sedeEntitaPrincipale.setVersion(versionSedeEntitaPrincipale);
		}

		return sedeEntitaPrincipale;
	}

	/**
	 * @return the sedeMagazzino
	 */
	public SedeMagazzino getSedeMagazzino() {
		if (sedeMagazzino == null) {
			sedeMagazzino = new SedeMagazzino();
			if (!ereditaDatiCommerciali) {
				sedeMagazzino.setId(idSedeMagazzino);
				sedeMagazzino.setVersion(versionSedeMagazzino);
				sedeMagazzino
						.setTipologiaGenerazioneDocumentoFatturazione(tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino);
			} else {
				sedeMagazzino.setId(idSedeMagazzinoPrincipale);
				sedeMagazzino.setVersion(versionSedeMagazzinoPrincipale);
				sedeMagazzino
						.setTipologiaGenerazioneDocumentoFatturazione(tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale);
			}
		}
		return sedeMagazzino;
	}

	/**
	 * @return the speseIncasso
	 */
	public BigDecimal getSpeseIncasso() {
		return speseIncasso;
	}

	/**
	 * @return the tipoAreaMagazzinoPerFatturazione
	 */
	public TipoAreaMagazzino getTipoAreaMagazzinoPerFatturazione() {
		if (tipoAreaMagazzinoPerFatturazione == null) {
			tipoAreaMagazzinoPerFatturazione = new TipoAreaMagazzino();
			tipoAreaMagazzinoPerFatturazione.setId(idTipoAreaMagazzinoPerFatturazione);
			tipoAreaMagazzinoPerFatturazione.setVersion(versionTipoAreaMagazzinoPerFatturazione);
			tipoAreaMagazzinoPerFatturazione.setTipoDocumento(tipoDocumentoPerFatturazione);
		}
		return tipoAreaMagazzinoPerFatturazione;
	}

	@Override
	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			String hashStr = this.getClass().getName();
			try {
				if (null != this.getRigaMagazzino() && null != this.getRigaMagazzino().getId()) {
					hashStr += ":" + this.getRigaMagazzino().getId().hashCode();
				}
			} catch (Exception ex) {
				System.err.println(ex);
			}
			this.hashCode = hashStr.hashCode();
		}
		return this.hashCode;
	}

	/**
	 * @return the addebitoSpeseIncasso
	 */
	public boolean isAddebitoSpeseIncasso() {
		return addebitoSpeseIncasso;
	}

	/**
	 * @return the raggruppamentoBolle
	 */
	public boolean isRaggruppamentoBolle() {
		return raggruppamentoBolle;
	}

	/**
	 * @param addebitoSpeseIncasso
	 *            the addebitoSpeseIncasso to set
	 */
	public void setAddebitoSpeseIncasso(boolean addebitoSpeseIncasso) {
		this.addebitoSpeseIncasso = addebitoSpeseIncasso;
	}

	/**
	 * @param annoMovimento
	 *            the annoMovimento to set
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @param codiceEntitaArea
	 *            the codiceEntitaArea to set
	 */
	public void setCodiceEntitaArea(Integer codiceEntitaArea) {
		this.codiceEntitaArea = codiceEntitaArea;
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.codiceTipoDocumento = codiceTipoDocumento;
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
	 * @param denominazioneEntitaArea
	 *            the denominazioneEntitaArea to set
	 */
	public void setDenominazioneEntitaArea(String denominazioneEntitaArea) {
		this.denominazioneEntitaArea = denominazioneEntitaArea;
	}

	/**
	 * @param descrizioneSedeArea
	 *            the descrizioneSedeArea to set
	 */
	public void setDescrizioneSedeArea(String descrizioneSedeArea) {
		this.descrizioneSedeArea = descrizioneSedeArea;
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.descrizioneTipoDocumento = descrizioneTipoDocumento;
	}

	/**
	 * @param ereditaDatiCommerciali
	 *            the ereditaDatiCommerciali to set
	 */
	public void setEreditaDatiCommerciali(boolean ereditaDatiCommerciali) {
		this.ereditaDatiCommerciali = ereditaDatiCommerciali;
	}

	/**
	 * @param idArea
	 *            the idArea to set
	 */
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	/**
	 * @param idCodicePagamento
	 *            the idCodicePagamento to set
	 */
	public void setIdCodicePagamento(Integer idCodicePagamento) {
		this.idCodicePagamento = idCodicePagamento;
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
	}

	/**
	 * @param idSedeEntita
	 *            the idSedeEntita to set
	 */
	public void setIdSedeEntita(Integer idSedeEntita) {
		this.idSedeEntita = idSedeEntita;
	}

	/**
	 * @param idSedeEntitaPrincipale
	 *            the idSedeEntitaPrincipale to set
	 */
	public void setIdSedeEntitaPrincipale(Integer idSedeEntitaPrincipale) {
		this.idSedeEntitaPrincipale = idSedeEntitaPrincipale;
	}

	/**
	 * @param idSedeMagazzino
	 *            the idSedeMagazzino to set
	 */
	public void setIdSedeMagazzino(Integer idSedeMagazzino) {
		this.idSedeMagazzino = idSedeMagazzino;
	}

	/**
	 * @param idSedeMagazzinoPrincipale
	 *            the idSedeMagazzinoPrincipale to set
	 */
	public void setIdSedeMagazzinoPrincipale(Integer idSedeMagazzinoPrincipale) {
		this.idSedeMagazzinoPrincipale = idSedeMagazzinoPrincipale;
	}

	/**
	 * @param idTipoAreaMagazzinoPerFatturazione
	 *            the idTipoAreaMagazzinoPerFatturazione to set
	 */
	public void setIdTipoAreaMagazzinoPerFatturazione(Integer idTipoAreaMagazzinoPerFatturazione) {
		this.idTipoAreaMagazzinoPerFatturazione = idTipoAreaMagazzinoPerFatturazione;
	}

	/**
	 * @param importoSpese
	 *            the importoSpese to set
	 */
	public void setImportoSpese(BigDecimal importoSpese) {
		this.importoSpese = importoSpese;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param raggruppamentoBolle
	 *            the raggruppamentoBolle to set
	 */
	public void setRaggruppamentoBolle(boolean raggruppamentoBolle) {
		this.raggruppamentoBolle = raggruppamentoBolle;
	}

	/**
	 * @param rigaMagazzino
	 *            the rigaMagazzino to set
	 */
	public void setRigaMagazzino(RigaMagazzino rigaMagazzino) {
		this.rigaMagazzino = rigaMagazzino;
	}

	/**
	 * @param speseIncasso
	 *            the speseIncasso to set
	 */
	public void setSpeseIncasso(BigDecimal speseIncasso) {
		this.speseIncasso = speseIncasso;
	}

	/**
	 * @param tipoDocumentoPerFatturazione
	 *            the tipoDocumentoPerFatturazione to set
	 */
	public void setTipoDocumentoPerFatturazione(TipoDocumento tipoDocumentoPerFatturazione) {
		this.tipoDocumentoPerFatturazione = tipoDocumentoPerFatturazione;
	}

	/**
	 * @param tipoDocumentoPerFatturazioneDescrizioneMaschera
	 *            the tipoDocumentoPerFatturazioneDescrizioneMaschera to set
	 */
	public void setTipoDocumentoPerFatturazioneDescrizioneMaschera(
			String tipoDocumentoPerFatturazioneDescrizioneMaschera) {
		this.tipoDocumentoPerFatturazioneDescrizioneMaschera = tipoDocumentoPerFatturazioneDescrizioneMaschera;
	}

	/**
	 * @param tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino
	 *            the tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino to set
	 */
	public void setTipologiaGenerazioneDocumentoFatturazioneSedeMagazzino(
			ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino) {
		this.tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino = tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino;
	}

	/**
	 * @param tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale
	 *            the tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale to set
	 */
	public void setTipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale(
			ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale) {
		this.tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale = tipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale;
	}

	/**
	 * @param versionArea
	 *            the versionArea to set
	 */
	public void setVersionArea(Integer versionArea) {
		this.versionArea = versionArea;
	}

	/**
	 * @param versionCodicePagamento
	 *            the versionCodicePagamento to set
	 */
	public void setVersionCodicePagamento(Integer versionCodicePagamento) {
		this.versionCodicePagamento = versionCodicePagamento;
	}

	/**
	 * @param versionEntita
	 *            the versionEntita to set
	 */
	public void setVersionEntita(Integer versionEntita) {
		this.versionEntita = versionEntita;
	}

	/**
	 * @param versionSedeEntita
	 *            the versionSedeEntita to set
	 */
	public void setVersionSedeEntita(Integer versionSedeEntita) {
		this.versionSedeEntita = versionSedeEntita;
	}

	/**
	 * @param versionSedeEntitaPrincipale
	 *            the versionSedeEntitaPrincipale to set
	 */
	public void setVersionSedeEntitaPrincipale(Integer versionSedeEntitaPrincipale) {
		this.versionSedeEntitaPrincipale = versionSedeEntitaPrincipale;
	}

	/**
	 * @param versionSedeMagazzino
	 *            the versionSedeMagazzino to set
	 */
	public void setVersionSedeMagazzino(Integer versionSedeMagazzino) {
		this.versionSedeMagazzino = versionSedeMagazzino;
	}

	/**
	 * @param versionSedeMagazzinoPrincipale
	 *            the versionSedeMagazzinoPrincipale to set
	 */
	public void setVersionSedeMagazzinoPrincipale(Integer versionSedeMagazzinoPrincipale) {
		this.versionSedeMagazzinoPrincipale = versionSedeMagazzinoPrincipale;
	}

	/**
	 * @param versionTipoAreaMagazzinoPerFatturazione
	 *            the versionTipoAreaMagazzinoPerFatturazione to set
	 */
	public void setVersionTipoAreaMagazzinoPerFatturazione(Integer versionTipoAreaMagazzinoPerFatturazione) {
		this.versionTipoAreaMagazzinoPerFatturazione = versionTipoAreaMagazzinoPerFatturazione;
	}
}
