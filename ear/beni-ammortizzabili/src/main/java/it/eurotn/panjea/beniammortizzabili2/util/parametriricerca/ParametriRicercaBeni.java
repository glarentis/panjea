package it.eurotn.panjea.beniammortizzabili2.util.parametriricerca;

import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;

public class ParametriRicercaBeni {

	private Ubicazione ubicazione;

	private Gruppo gruppo;

	private Specie specie;

	private SottoSpecie sottoSpecie;

	private FornitoreLite fornitoreLite;

	private boolean visualizzaFigli;

	private boolean stampaRaggruppamento;

	private boolean visualizzaEliminati;

	private Integer anno;

	private boolean raggruppaUbicazione;

	{
		this.anno = new Integer(0);

		this.raggruppaUbicazione = true;
	}

	/**
	 * Costruttore di default.
	 */
	public ParametriRicercaBeni() {
		super();
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the fornitoreLite
	 */
	public FornitoreLite getFornitoreLite() {
		return fornitoreLite;
	}

	/**
	 * @return the gruppo
	 */
	public Gruppo getGruppo() {
		return gruppo;
	}

	/**
	 * Restituisce i parametri per la stampa aquisto beni, formattati in html per stampare su report.
	 * 
	 * @return html dei parametri
	 */
	public String getHtmlParametersAcquistoBeni() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");

		if (getAnno().compareTo(new Integer(0)) > 0) {
			stringBuffer.append("<b> Anno: </b> ");
			stringBuffer.append(getAnno());
		}

		if (getSpecie() != null) {
			stringBuffer.append("<b> Specie: </b> ");
			stringBuffer.append(getSpecie().getCodice() + " - " + getSpecie().getDescrizione());
		}

		if (getSottoSpecie() != null) {
			stringBuffer.append("<b> Sottospecie: </b> ");
			stringBuffer.append(getSottoSpecie().getCodice() + " - " + getSottoSpecie().getDescrizione());
		}

		if (getFornitoreLite() != null) {
			stringBuffer.append("<b> Fornitore: </b> ");
			stringBuffer.append(getFornitoreLite().getCodice() + " - "
					+ getFornitoreLite().getAnagrafica().getDenominazione());
		}

		if (isVisualizzaEliminati()) {
			stringBuffer.append("<b> Visualizzazione beni eliminati </b> ");
		}

		if (isVisualizzaFigli()) {
			stringBuffer.append("<b> Visualizzazione beni figli </b> ");
		}
		stringBuffer.append("</html>");

		return stringBuffer.toString();
	}

	/**
	 * Restituisce i parametri per la stampa rubrica beni, formattati in html per stampare su report.
	 * 
	 * @return html dei parametri
	 */
	public String getHtmlParametersRubrica() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");

		if (getUbicazione() != null) {
			stringBuffer.append("<b> Ubicazione: </b> ");
			stringBuffer.append(getUbicazione().getCodice() + " - " + getUbicazione().getDescrizione());
		}

		if (getSpecie() != null) {
			stringBuffer.append("<b> Specie: </b> ");
			stringBuffer.append(getSpecie().getCodice() + " - " + getSpecie().getDescrizione());
		}

		if (getSottoSpecie() != null) {
			stringBuffer.append("<b> Sottospecie: </b> ");
			stringBuffer.append(getSottoSpecie().getCodice() + " - " + getSottoSpecie().getDescrizione());
		}

		if (isVisualizzaEliminati()) {
			stringBuffer.append("<b> Visualizzazione beni eliminati</b> ");
		}

		if (isVisualizzaFigli()) {
			stringBuffer.append("<b> Visualizzazione beni figli </b> ");
		}

		stringBuffer.append("</html>");
		return stringBuffer.toString();
	}

	/**
	 * Restituisce i parametri per la stampa situazione beni, formattati in html per stampare su report.
	 * 
	 * @return html dei parametri
	 */
	public String getHtmlParametersSituazioneBeni() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");

		if (getFornitoreLite() != null) {
			stringBuffer.append("<b> Fornitore: </b> ");
			stringBuffer.append(getFornitoreLite().getCodice() + " - "
					+ getFornitoreLite().getAnagrafica().getDenominazione());
		}

		if (isVisualizzaFigli()) {
			stringBuffer.append("<b> Visualizzazione beni figli </b> ");
		}

		stringBuffer.append("</html>");
		return stringBuffer.toString();
	}

	/**
	 * @return the sottoSpecie
	 */
	public SottoSpecie getSottoSpecie() {
		return sottoSpecie;
	}

	/**
	 * @return the specie
	 */
	public Specie getSpecie() {
		return specie;
	}

	/**
	 * @return the ubicazione
	 */
	public Ubicazione getUbicazione() {
		return ubicazione;
	}

	/**
	 * @return the raggruppaUbicazione
	 */
	public boolean isRaggruppaUbicazione() {
		return raggruppaUbicazione;
	}

	/**
	 * @return the stampaRaggruppamento
	 */
	public boolean isStampaRaggruppamento() {
		return stampaRaggruppamento;
	}

	/**
	 * @return the visualizzaEliminati
	 */
	public boolean isVisualizzaEliminati() {
		return visualizzaEliminati;
	}

	/**
	 * @return the visualizzaFigli
	 */
	public boolean isVisualizzaFigli() {
		return visualizzaFigli;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param fornitoreLite
	 *            the fornitoreLite to set
	 */
	public void setFornitoreLite(FornitoreLite fornitoreLite) {
		this.fornitoreLite = fornitoreLite;
	}

	/**
	 * @param gruppo
	 *            the gruppo to set
	 */
	public void setGruppo(Gruppo gruppo) {
		this.gruppo = gruppo;
	}

	/**
	 * @param raggruppaUbicazione
	 *            the raggruppaUbicazione to set
	 */
	public void setRaggruppaUbicazione(boolean raggruppaUbicazione) {
		this.raggruppaUbicazione = raggruppaUbicazione;
	}

	/**
	 * @param sottoSpecie
	 *            the sottoSpecie to set
	 */
	public void setSottoSpecie(SottoSpecie sottoSpecie) {
		this.sottoSpecie = sottoSpecie;
	}

	/**
	 * @param specie
	 *            the specie to set
	 */
	public void setSpecie(Specie specie) {
		this.specie = specie;
	}

	/**
	 * @param stampaRaggruppamento
	 *            the stampaRaggruppamento to set
	 */
	public void setStampaRaggruppamento(boolean stampaRaggruppamento) {
		this.stampaRaggruppamento = stampaRaggruppamento;
	}

	/**
	 * @param ubicazione
	 *            the ubicazione to set
	 */
	public void setUbicazione(Ubicazione ubicazione) {
		this.ubicazione = ubicazione;
	}

	/**
	 * @param visualizzaEliminati
	 *            the visualizzaEliminati to set
	 */
	public void setVisualizzaEliminati(boolean visualizzaEliminati) {
		this.visualizzaEliminati = visualizzaEliminati;
	}

	/**
	 * @param visualizzaFigli
	 *            the visualizzaFigli to set
	 */
	public void setVisualizzaFigli(boolean visualizzaFigli) {
		this.visualizzaFigli = visualizzaFigli;
	}

}
