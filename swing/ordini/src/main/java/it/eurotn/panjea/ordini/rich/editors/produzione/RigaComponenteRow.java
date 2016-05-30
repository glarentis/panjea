package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;

import java.util.Objects;

public class RigaComponenteRow extends RigaDistintaEvasaRow {

	private Double qtaOriginale;

	/**
	 * Costruttore.
	 *
	 * @param rigaArticolo riga componente
	 */
	public RigaComponenteRow(final RigaArticolo rigaArticolo) {
		super(rigaArticolo);
		this.qtaOriginale = rigaArticolo.getQta();
	}

	/**
	 * @return the rigaArticolo
	 */
	public RigaArticolo getRigaComponente() {
		return rigaArticolo;
	}

	/**
	 * @return <code>true</code> se la quantità è stata cambiata rispetto alla riga originale
	 */
	public boolean isQtaChanged() {
		return !Objects.equals(qtaOriginale, rigaArticolo.getQta());
	}

}
