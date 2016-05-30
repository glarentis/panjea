package it.eurotn.panjea.lotti.exception;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RimanenzaLottiNonValidaException extends LottiException {

	private static final long serialVersionUID = 6470617845635468640L;

	private List<Lotto> lotti;

	private Set<RigaLotto> righeLotto;

	private RigaArticolo rigaArticolo;

	/**
	 * Costruttore.
	 * 
	 * @param rigaArticolo
	 *            riga di riferimento
	 * 
	 */
	public RimanenzaLottiNonValidaException(final RigaArticolo rigaArticolo) {
		super();
		this.lotti = new ArrayList<Lotto>();
		this.righeLotto = new HashSet<RigaLotto>();
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * Costruttore.
	 * 
	 * @param rigaArticolo
	 *            riga di riferimento
	 * @param righeLotto
	 *            righe lotti
	 */
	public RimanenzaLottiNonValidaException(final RigaArticolo rigaArticolo, final Set<RigaLotto> righeLotto) {
		super();
		this.righeLotto = righeLotto;
		this.lotti = new ArrayList<Lotto>();
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * Aggiunge un {@link Lotto} con rimanenza non valida.
	 * 
	 * @param lotto
	 *            {@link Lotto}
	 */
	public void aggiungiLotto(Lotto lotto) {
		lotti.add(lotto);
	}

	@Override
	public String getHTMLMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("Rimanenza insufficiente per i seguenti lotti dell'articolo ");
		sb.append("<b>");
		sb.append(rigaArticolo.getArticolo().getCodice());
		sb.append(" - ");
		sb.append(rigaArticolo.getArticolo().getDescrizione());
		sb.append("</b>:<br><br>");
		sb.append("<table>");
		sb.append("</table>");
		sb.append("</html>");
		return sb.toString();
	}

	/**
	 * @return the lotti
	 */
	public List<Lotto> getLotti() {
		return lotti;
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * @return Returns the righeLotto.
	 */
	public Set<RigaLotto> getRigheLotto() {
		return righeLotto;
	}
}
