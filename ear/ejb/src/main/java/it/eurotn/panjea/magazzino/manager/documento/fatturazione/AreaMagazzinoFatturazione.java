package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AreaMagazzinoFatturazione {

	private final AreaMagazzino areaMagazzino;

	private final boolean raggruppamentoBolle;

	private final SedeEntita sedeEntita;

	private final SedeEntita sedeEntitaPrincipale;
	private final EntitaLite entita;

	private final SedeMagazzino sedeMagazzino;

	private final TipoAreaMagazzino tipoAreaMagazzinoPerFatturazione;

	private final Date dataDocumento;

	private final CodiceDocumento numeroDocumento;

	private final String codiceTipoDocumento;

	private final CodicePagamento codicePagamento;

	private final boolean addebitoSpeseIncasso;

	private final BigDecimal speseIncasso;

	private final Integer annoMovimento;

	private final String codiceValuta;

	private final List<RigaMagazzino> righeMagazzino;

	/**
	 * Costruttore.
	 * 
	 * @param riga
	 *            {@link RigaMagazzinoFatturazione} da cui generare l'area
	 */
	public AreaMagazzinoFatturazione(final RigaMagazzinoFatturazione riga) {
		super();

		this.areaMagazzino = riga.getAreaMagazzino();

		this.raggruppamentoBolle = riga.isRaggruppamentoBolle();

		this.sedeEntita = riga.getSedeEntita();
		this.sedeEntitaPrincipale = riga.getSedeEntitaPrincipale();

		this.sedeMagazzino = riga.getSedeMagazzino();

		this.entita = riga.getEntita();

		this.tipoAreaMagazzinoPerFatturazione = riga.getTipoAreaMagazzinoPerFatturazione();

		this.dataDocumento = riga.getDataDocumento();
		this.numeroDocumento = riga.getNumeroDocumento();
		this.codiceTipoDocumento = riga.getCodiceTipoDocumento();

		this.codicePagamento = riga.getCodicePagamento();

		this.addebitoSpeseIncasso = riga.isAddebitoSpeseIncasso();
		this.speseIncasso = riga.getSpeseIncasso();
		this.annoMovimento = riga.getAnnoMovimento();
		this.codiceValuta = riga.getCodiceValuta();

		this.righeMagazzino = new ArrayList<RigaMagazzino>();
	}

	/**
	 * Aggiunge una {@link RigaMagazzino} all'area.
	 * 
	 * @param riga
	 *            riga magazzino
	 */
	public void addRigaMagazzino(RigaMagazzino riga) {
		RigaMagazzino rigaMagazzino = riga;
		rigaMagazzino.setAreaMagazzino(this.areaMagazzino);
		this.righeMagazzino.add(rigaMagazzino);
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
		return areaMagazzino;
	}

	/**
	 * @return the codicePagamento
	 */
	public CodicePagamento getCodicePagamento() {
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
		return entita;
	}

	/**
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the righeMagazzino
	 */
	public List<RigaMagazzino> getRigheMagazzino() {
		return righeMagazzino;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the sedeEntitaPrincipale
	 */
	public SedeEntita getSedeEntitaPrincipale() {
		return sedeEntitaPrincipale;
	}

	/**
	 * @return the sedeMagazzino
	 */
	public SedeMagazzino getSedeMagazzino() {
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
		return tipoAreaMagazzinoPerFatturazione;
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
}
