package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

public class StatoEvasioneOrdineLabel extends JLabel {

	private static final long serialVersionUID = 5204535383337118279L;

	private static final Icon EVASO_ICON = RcpSupport.getIcon("true");

	private static final String EVASO_TEXT = RcpSupport.getMessage("evaso");

	/**
	 * Costruttore.
	 * 
	 */
	public StatoEvasioneOrdineLabel() {
		super();
		setHorizontalTextPosition(SwingConstants.LEFT);

		this.setIcon(EVASO_ICON);
		this.setText(EVASO_TEXT);
	}

	/**
	 * Aggiorna lo stato della label in base all'area ordine di riferimento.
	 * 
	 * @param areaOrdine
	 *            area ordine
	 */
	public void aggiornaStato(AreaOrdine areaOrdine) {

		setVisible(areaOrdine != null && areaOrdine.isEvaso());
	}

}
