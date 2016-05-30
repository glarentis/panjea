package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

public class RigheLottiNonValideException extends LottiException {

	private static final long serialVersionUID = 5097053169981710039L;

	private RigaArticolo rigaArticolo;

	/**
	 * Costruttore.
	 * 
	 * @param rigaArticolo
	 *            riga articolo con righe lotti non valide
	 */
	public RigheLottiNonValideException(final RigaArticolo rigaArticolo) {
		super();
		this.rigaArticolo = rigaArticolo;
	}

	@Override
	public String getHTMLMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>La quantità dei lotti non è valida per l'articolo<br><br><b>");
		sb.append(rigaArticolo.getArticolo().getCodice());
		sb.append(" - ");
		sb.append(rigaArticolo.getArticolo().getDescrizione());
		sb.append("</b><br><br>");
		sb.append("Quantità riga: ");
		sb.append(rigaArticolo.getQta());
		sb.append("<br>");
		sb.append("Quantità lotti: ");
		sb.append(rigaArticolo.getQtaRigheLotto());
		sb.append("</html>");

		return sb.toString();
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

}
