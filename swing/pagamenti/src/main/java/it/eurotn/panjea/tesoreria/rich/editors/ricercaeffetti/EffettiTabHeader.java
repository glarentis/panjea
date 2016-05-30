package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;

import javax.swing.JLabel;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideTabbedPane;

public class EffettiTabHeader extends JideTabbedPane {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 * 
	 */
	public EffettiTabHeader() {

		addTab("Generale", new JLabel());
		addTab("Accrediti", new JLabel());
		addTab("Insoluti", new JLabel());
		addTab("Anticipi", new JLabel());
		setTabShape(JideTabbedPane.SHAPE_BOX);
		setBoldActiveTab(true);
		ITesoreriaBD tesoreriaBD = RcpSupport.getBean("tesoreriaBD");
		try {
			tesoreriaBD.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.ANTICIPO);
		} catch (Exception e) {
			setToolTipTextAt(3, "Tipo documento base non definito.");
			setEnabledAt(3, false);
		}

		try {
			tesoreriaBD.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.INSOLUTO);
		} catch (Exception e) {
			setToolTipTextAt(2, "Tipo documento base non definito.");
			setEnabledAt(2, false);
		}
	}
}
