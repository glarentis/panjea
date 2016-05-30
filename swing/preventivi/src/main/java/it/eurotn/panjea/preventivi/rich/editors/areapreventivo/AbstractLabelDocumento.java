package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

public abstract class AbstractLabelDocumento<E extends IAreaDocumentoTestata> extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * id icona.
	 */
	private final Icon labelIcon;

	/**
	 * id testo.
	 */
	private final String labelText;

	/**
	 * 
	 * @param idIcona
	 *            chiave da utilizzare per cercare l'icona tramite RcpSupport
	 * @param idTesto
	 *            chiave da utilizzare per cercare il testo tramite RcpSupport
	 */
	public AbstractLabelDocumento(final String idIcona, final String idTesto) {
		labelIcon = RcpSupport.getIcon(idIcona);
		labelText = RcpSupport.getMessage(idTesto);
		setHorizontalTextPosition(SwingConstants.LEFT);
		this.setIcon(labelIcon);
		this.setText(labelText);
	}

	/**
	 * @param areaDocumento
	 *            areaDocumento
	 * @return true se la label deve essere visualizzata.
	 */
	protected abstract boolean isDaVisualizzare(E areaDocumento);

	/**
	 * Aggiorna lo stato della label in base all'area ordine di riferimento.
	 * 
	 * @param areaDocumento
	 *            area ordine
	 */
	public void visualizzaONascondi(E areaDocumento) {
		setVisible(isDaVisualizzare(areaDocumento));
	}
}
