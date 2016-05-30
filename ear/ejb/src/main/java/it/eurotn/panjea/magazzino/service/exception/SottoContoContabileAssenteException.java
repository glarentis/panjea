package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;

import java.util.ArrayList;
import java.util.List;

/**
 * Lanciato quando non trovo nessuna combinazione valida per
 * categoriacontabileArticolo-categoriacontabileDeposito-categoriacontabileSedeMagazzino.
 * 
 * @author giangi
 */
public class SottoContoContabileAssenteException extends Exception {

	/**
	 * @uml.property name="articoli"
	 */
	private final List<ArticoloLite> articoli;
	/**
	 * @uml.property name="articolo"
	 * @uml.associationEnd
	 */
	private final ArticoloLite articolo;
	/**
	 * @uml.property name="deposito"
	 * @uml.associationEnd
	 */
	private final DepositoLite deposito;
	/**
	 * @uml.property name="sedeEntita"
	 * @uml.associationEnd
	 */
	private final SedeEntita sedeEntita;

	/**
	 * @uml.property name="categoriaContabileArticolo"
	 */
	private final String categoriaContabileArticolo;
	/**
	 * @uml.property name="categoriaContabileDeposito"
	 */
	private final String categoriaContabileDeposito;
	/**
	 * @uml.property name="categoriaContabileSedeMagazzino"
	 */
	private String categoriaContabileSedeMagazzino;

	private static final long serialVersionUID = -4264722397360083267L;

	/**
	 * Costruttore.
	 */
	public SottoContoContabileAssenteException() {
		super();

		this.categoriaContabileArticolo = "";
		this.categoriaContabileDeposito = "";
		this.categoriaContabileSedeMagazzino = "";

		this.articoli = new ArrayList<ArticoloLite>();
		this.deposito = null;
		this.sedeEntita = null;
		this.articolo = null;
	}

	/**
	 * Costruttore.
	 * 
	 * @param categoriaContabileArticolo
	 *            categoria contabile articolo
	 * @param categoriaContabileDeposito
	 *            categoria contabile deposito
	 * @param categoriaContabileSedeMagazzino
	 *            categoria contabile sede
	 * @param articoli
	 *            articoli
	 * @param deposito
	 *            deposito
	 * @param sedeEntita
	 *            sede
	 */
	public SottoContoContabileAssenteException(final CategoriaContabileArticolo categoriaContabileArticolo,
			final CategoriaContabileDeposito categoriaContabileDeposito,
			final CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino, final List<ArticoloLite> articoli,
			final DepositoLite deposito, final SedeEntita sedeEntita) {

		if (categoriaContabileArticolo == null) {
			this.categoriaContabileArticolo = "tutti";
		} else {
			this.categoriaContabileArticolo = categoriaContabileArticolo.getCodice();
		}

		if (categoriaContabileDeposito == null) {
			this.categoriaContabileDeposito = "tutti";
		} else {
			this.categoriaContabileDeposito = categoriaContabileDeposito.getCodice();
		}

		if (categoriaContabileSedeMagazzino == null) {
			this.categoriaContabileSedeMagazzino = "tutti";
		} else {
			this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino.getCodice();
		}

		this.articoli = articoli;
		this.deposito = deposito;
		this.sedeEntita = sedeEntita;
		this.articolo = null;
	}

	/**
	 * Costruttore.
	 * 
	 * @param categoriaContabileArticolo
	 *            categoria contabile articolo
	 * @param categoriaContabileDeposito
	 *            categoria contabile deposito
	 * @param categoriaContabileSedeMagazzino
	 *            categoria contabile sede
	 * @param articolo
	 *            articolo
	 * @param deposito
	 *            deposito
	 * @param sedeEntita
	 *            sede
	 */
	public SottoContoContabileAssenteException(final String categoriaContabileArticolo,
			final String categoriaContabileDeposito, final String categoriaContabileSedeMagazzino,
			final ArticoloLite articolo, final DepositoLite deposito, final SedeEntita sedeEntita) {

		this.categoriaContabileArticolo = categoriaContabileArticolo;
		this.categoriaContabileDeposito = categoriaContabileDeposito;
		this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino;

		this.articolo = articolo;
		this.deposito = deposito;
		this.sedeEntita = sedeEntita;
		this.articoli = null;
	}

	/**
	 * @return the articoli
	 * @uml.property name="articoli"
	 */
	public List<ArticoloLite> getArticoli() {
		return articoli;
	}

	/**
	 * @return the articolo
	 * @uml.property name="articolo"
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the categoriaContabileArticolo
	 * @uml.property name="categoriaContabileArticolo"
	 */
	public String getCategoriaContabileArticolo() {
		return categoriaContabileArticolo;
	}

	/**
	 * @return the categoriaContabileDeposito
	 * @uml.property name="categoriaContabileDeposito"
	 */
	public String getCategoriaContabileDeposito() {
		return categoriaContabileDeposito;
	}

	/**
	 * @return the categoriaContabileSedeMagazzino
	 * @uml.property name="categoriaContabileSedeMagazzino"
	 */
	public String getCategoriaContabileSedeMagazzino() {
		return categoriaContabileSedeMagazzino;
	}

	/**
	 * @return the deposito
	 * @uml.property name="deposito"
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return the sedeEntita
	 * @uml.property name="sedeEntita"
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @param categoriaContabileSedeMagazzino
	 *            setter of categoriaContabileSedeMagazzino
	 * @uml.property name="categoriaContabileSedeMagazzino"
	 */
	public void setCategoriaContabileSedeMagazzino(String categoriaContabileSedeMagazzino) {
		this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino;
	}
}
