package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;

import java.io.Serializable;

public class CriteriaRicercaBeniAmmortizzabili implements Serializable {

	public static final String PROP_ANNO = "anno";
	public static final String PROP_ANNOAMMORTAMENTO = "annoAmmortamento";
	public static final String PROP_UBICAZIONE = "ubicazione";
	public static final String PROP_SPECIEINIZIALE = "specieIniziale";
	public static final String PROP_SPECIEFINALE = "specieFinale";
	public static final String PROP_SOTTOSPECIEINIZIALE = "sottoSpecieIniziale";
	public static final String PROP_SOTTOSPECIEFINALE = "sottoSpecieFinale";
	public static final String PROP_FORNITORE = "fornitore";
	public static final String PROP_AMMORTAMENTOINCORSO = "ammortamentoInCorso";
	public static final String PROP_VISUALIZZAZIONEFIGLI = "visualizzazioneFigli";
	public static final String PROP_BENIELIMINATI = "beniEliminati";

	private static final long serialVersionUID = 1L;

	public static final String NOTHING = "";
	public static final String EQUAL = "=";
	public static final String LIKE = "like";
	public static final String LESSEQUAL = "<=";
	public static final String LESS = "<";
	public static final String GREATEQUAL = ">=";
	public static final String GREAT = ">";

	private Integer anno;
	private Integer annoAmmortamento;

	private Ubicazione ubicazione;

	private Specie specieIniziale;
	private Specie specieFinale;

	private SottoSpecie sottoSpecieIniziale;
	private SottoSpecie sottoSpecieFinale;

	private FornitoreLite fornitore;
	private boolean ammortamentoInCorso;
	private boolean visualizzazioneFigli;
	private boolean beniEliminati;

	/**
	 * Costruttore di default.
	 */
	public CriteriaRicercaBeniAmmortizzabili() {
		initialize();
	}

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return Returns the annoAmmortamento.
	 */
	public Integer getAnnoAmmortamento() {
		return annoAmmortamento;
	}

	/**
	 * @return Returns the fornitore.
	 */
	public FornitoreLite getFornitore() {
		return fornitore;
	}

	/**
	 * @return Returns the sottoSpecieFinale.
	 */
	public SottoSpecie getSottoSpecieFinale() {
		return sottoSpecieFinale;
	}

	/**
	 * @return Returns the sottoSpecieIniziale.
	 */
	public SottoSpecie getSottoSpecieIniziale() {
		return sottoSpecieIniziale;
	}

	/**
	 * @return Returns the specieFinale.
	 */
	public Specie getSpecieFinale() {
		return specieFinale;
	}

	/**
	 * @return Returns the specieIniziale.
	 */
	public Specie getSpecieIniziale() {
		return specieIniziale;
	}

	/**
	 * @return Returns the ubicazione.
	 */
	public Ubicazione getUbicazione() {
		return ubicazione;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		ubicazione = new Ubicazione();
		specieFinale = new Specie();
		specieIniziale = new Specie();
		sottoSpecieIniziale = new SottoSpecie();
		sottoSpecieFinale = new SottoSpecie();
		fornitore = new FornitoreLite();
		ammortamentoInCorso = true;
		visualizzazioneFigli = true;
		beniEliminati = false;
	}

	/**
	 * @return Returns the ammortementoInCorso.
	 */
	public boolean isAmmortamentoInCorso() {
		return ammortamentoInCorso;
	}

	/**
	 * @return Returns the beniEliminati.
	 */
	public boolean isBeniEliminati() {
		return beniEliminati;
	}

	/**
	 * @return Ritorna se � stato definito un intervallo riguardante la sottoSpecie
	 */
	public boolean isIntervalloSottoSpecie() {
		return ((sottoSpecieIniziale != null) || (sottoSpecieFinale != null));
	}

	/**
	 * 
	 * @return Ritorna se � stato definito un intervallo riguardante la specie
	 */
	public boolean isIntervalloSpecie() {
		return (specieIniziale != null) || (specieFinale != null);
	}

	/**
	 * @return Returns the visualizzazioneFigli.
	 */
	public boolean isVisualizzazioneFigli() {
		return visualizzazioneFigli;
	}

	/**
	 * @param ammortementoInCorso
	 *            The ammortementoInCorso to set.
	 */
	public void setAmmortamentoInCorso(boolean ammortementoInCorso) {
		this.ammortamentoInCorso = ammortementoInCorso;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param annoAmmortamento
	 *            The annoAmmortamento to set.
	 */
	public void setAnnoAmmortamento(Integer annoAmmortamento) {
		this.annoAmmortamento = annoAmmortamento;
	}

	/**
	 * @param beniEliminati
	 *            The beniEliminati to set.
	 */
	public void setBeniEliminati(boolean beniEliminati) {
		this.beniEliminati = beniEliminati;
	}

	/**
	 * @param fornitore
	 *            The fornitore to set.
	 */
	public void setFornitore(FornitoreLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 * @param sottoSpecieFinale
	 *            The sottoSpecieFinale to set.
	 */
	public void setSottoSpecieFinale(SottoSpecie sottoSpecieFinale) {
		this.sottoSpecieFinale = sottoSpecieFinale;
	}

	/**
	 * @param sottoSpecieIniziale
	 *            The sottoSpecieIniziale to set.
	 */
	public void setSottoSpecieIniziale(SottoSpecie sottoSpecieIniziale) {
		this.sottoSpecieIniziale = sottoSpecieIniziale;
	}

	/**
	 * @param specieFinale
	 *            The specieFinale to set.
	 */
	public void setSpecieFinale(Specie specieFinale) {
		this.specieFinale = specieFinale;
	}

	/**
	 * @param specieIniziale
	 *            The specieIniziale to set.
	 */
	public void setSpecieIniziale(Specie specieIniziale) {
		this.specieIniziale = specieIniziale;
	}

	/**
	 * @param ubicazione
	 *            The ubicazione to set.
	 */
	public void setUbicazione(Ubicazione ubicazione) {
		this.ubicazione = ubicazione;
	}

	/**
	 * @param visualizzazioneFigli
	 *            The visualizzazioneFigli to set.
	 */
	public void setVisualizzazioneFigli(boolean visualizzazioneFigli) {
		this.visualizzazioneFigli = visualizzazioneFigli;
	}

}
