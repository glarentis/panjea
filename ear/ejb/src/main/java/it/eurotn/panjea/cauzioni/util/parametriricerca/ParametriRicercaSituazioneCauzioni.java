package it.eurotn.panjea.cauzioni.util.parametriricerca;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "para_ricerca_situazione_cauzioni")
public class ParametriRicercaSituazioneCauzioni extends AbstractParametriRicerca implements ITableHeader {

	private static final long serialVersionUID = -5116835135413980998L;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	@ManyToOne(optional = true)
	private ArticoloLite articolo;

	private Periodo periodo;

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaSituazioneCauzioni() {
		super();
		this.periodo = new Periodo();
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	@Override
	public List<TableHeaderObject> getHeaderValues() {
		List<TableHeaderObject> values = new ArrayList<TableHeaderObject>();

		if (periodo.getDataIniziale() != null) {
			values.add(new TableHeaderObject("dataIniziale", periodo.getDataIniziale()));
		}
		if (periodo.getDataFinale() != null) {
			values.add(new TableHeaderObject("dataFinale", periodo.getDataFinale()));
		}
		if (entita != null && entita.getId() != null) {
			values.add(new TableHeaderObject("entita", entita));
		}

		if (articolo != null && articolo.getId() != null) {
			values.add(new TableHeaderObject("articolo", articolo));
		}

		if (values.isEmpty()) {
			return null;
		} else {
			return values;
		}
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param periodo
	 *            the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

}
