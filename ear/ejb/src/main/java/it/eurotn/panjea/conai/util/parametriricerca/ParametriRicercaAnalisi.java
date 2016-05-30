package it.eurotn.panjea.conai.util.parametriricerca;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "para_ricerca_analisi_conai")
public class ParametriRicercaAnalisi extends AbstractParametriRicerca implements Serializable, ITableHeader {

	private static final long serialVersionUID = 6902878328102614385L;

	@ManyToOne(optional = true)
	private ClienteLite cliente;

	private Periodo periodo;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoAreaMagazzino> tipiAreaMagazzino = null;

	@Transient
	private boolean effettuaRicerca;

	{
		this.cliente = null;

		this.periodo = new Periodo();
		this.effettuaRicerca = false;

		if (tipiAreaMagazzino == null) {
			this.tipiAreaMagazzino = new HashSet<TipoAreaMagazzino>();
		}
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAnalisi() {
		super();
	}

	/**
	 * @return Returns the cliente.
	 */
	public ClienteLite getCliente() {
		return cliente;
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
		if (cliente != null && cliente.getId() != null) {
			values.add(new TableHeaderObject("cliente", cliente));
		}
		if (tipiAreaMagazzino != null && !tipiAreaMagazzino.isEmpty()) {
			values.add(new TableHeaderObject("tipiDocumento", tipiAreaMagazzino));
		}

		if (values.isEmpty()) {
			return null;
		} else {
			return values;
		}
	}

	/**
	 * @return Returns the periodo.
	 */
	public Periodo getPeriodo() {
		if (periodo == null) {
			this.periodo = new Periodo();
		}
		return periodo;
	}

	/**
	 * @return Returns the tipiAreaMagazzino.
	 */
	public Set<TipoAreaMagazzino> getTipiAreaMagazzino() {
		return tipiAreaMagazzino;
	}

	/**
	 * @return Returns the effettuaRicerca.
	 */
	@Override
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param cliente
	 *            The cliente to set.
	 */
	public void setCliente(ClienteLite cliente) {
		this.cliente = cliente;
	}

	/**
	 * @param effettuaRicerca
	 *            The effettuaRicerca to set.
	 */
	@Override
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param periodo
	 *            The periodo to set.
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @param tipiAreaMagazzino
	 *            The tipiAreaMagazzino to set.
	 */
	public void setTipiAreaMagazzino(Set<TipoAreaMagazzino> tipiAreaMagazzino) {
		this.tipiAreaMagazzino = tipiAreaMagazzino;
	}
}
