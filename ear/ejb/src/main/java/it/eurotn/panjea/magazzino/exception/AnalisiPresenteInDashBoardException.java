/**
 *
 */
package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard;

import java.util.List;

/**
 * Generata per indicare in quali {@link DashBoard} è contenuta l'analisi.
 *
 * @author fattazzo
 *
 */
public class AnalisiPresenteInDashBoardException extends Exception {

	private static final long serialVersionUID = 5451165420860260695L;

	private List<DashBoard> dashBoards;

	private Integer idAnalisi;

	/**
	 * Costruttore.
	 *
	 * @param dashBoards
	 *            dashboard che contengono l'analisi
	 * @param idAnalisi
	 *            id dell'analisi
	 */
	public AnalisiPresenteInDashBoardException(final List<DashBoard> dashBoards, final Integer idAnalisi) {
		super();
		this.dashBoards = dashBoards;
		this.idAnalisi = idAnalisi;
	}

	/**
	 * @return the dashBoards
	 */
	public List<DashBoard> getDashBoards() {
		return dashBoards;
	}

	/**
	 * @return the idAnalisi
	 */
	public Integer getIdAnalisi() {
		return idAnalisi;
	}

}
