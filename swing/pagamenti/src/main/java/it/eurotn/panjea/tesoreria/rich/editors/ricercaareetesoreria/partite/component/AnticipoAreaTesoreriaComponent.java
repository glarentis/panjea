package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component;

import it.eurotn.panjea.tesoreria.domain.RigaAnticipo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Date;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pane.CollapsiblePane;

public class AnticipoAreaTesoreriaComponent extends CollapsiblePane {

	private static final long serialVersionUID = 5591137492744084639L;

	private final RigaAnticipo anticipo;

	/**
	 * Costruttore.
	 * 
	 * @param anticipo
	 *            anticipo da gestire
	 */
	public AnticipoAreaTesoreriaComponent(final RigaAnticipo anticipo) {
		super();
		this.anticipo = anticipo;
		setStyle(CollapsiblePane.DROPDOWN_STYLE);
		setEmphasized(true);
		setLayout(new BorderLayout());
		collapse(true);
		setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

		setBackground(new Color(204, 204, 214));
		setTitleLabelComponent(getTitleEffettoComponent());
		// add(getDetailEffettoComponent(), BorderLayout.CENTER);
	}

	/**
	 * Crea i componenti da posizionare nel titolo.
	 * 
	 * @return componenti creati
	 */
	private JComponent getTitleEffettoComponent() {

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setOpaque(false);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		JPanel datiPanel = new JPanel(new BorderLayout());
		datiPanel.setOpaque(false);

		JLabel labelData = new JLabel(ObjectConverterManager.toString(this.anticipo.getDataValuta()),
				RcpSupport.getIcon(Date.class.getName()), SwingConstants.LEFT);
		datiPanel.add(labelData, BorderLayout.WEST);

		DecimalFormat format = new DecimalFormat("#,##0.00");
		JLabel labelImporto = new JLabel(format.format(this.anticipo.getImportoAnticipato()),
				RcpSupport.getIcon(this.anticipo.getAreaAnticipo().getDocumento().getTotale().getCodiceValuta()),
				SwingConstants.RIGHT);
		labelImporto.setHorizontalTextPosition(SwingConstants.LEFT);
		datiPanel.add(labelImporto, BorderLayout.EAST);

		rootPanel.add(datiPanel, BorderLayout.CENTER);

		return rootPanel;
	}
}
