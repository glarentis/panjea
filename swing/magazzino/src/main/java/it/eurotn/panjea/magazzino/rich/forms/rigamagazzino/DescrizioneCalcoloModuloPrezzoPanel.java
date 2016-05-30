/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.ContrattoModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.ListinoModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.UltimoCostoModuloPrezzoCalculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

/**
 * Classe che visualizza una <code>DescrizioneCalcoloModuloPrezzo</code> opportunamente formattata.
 * 
 * @author fattazzo
 * 
 */
public class DescrizioneCalcoloModuloPrezzoPanel<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	private final IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	private final RisultatoModuloPrezzo<T> risultatoModuloPrezzo;

	/**
	 * 
	 * @param risultatoModuloPrezzo
	 *            risultato da renderizzare nel pannello.
	 */
	public DescrizioneCalcoloModuloPrezzoPanel(final RisultatoModuloPrezzo<T> risultatoModuloPrezzo) {
		super(new BorderLayout());
		this.risultatoModuloPrezzo = risultatoModuloPrezzo;

		if (ListinoModuloPrezzoCalculator.TIPO_MODULO.equals(this.risultatoModuloPrezzo.getTipoModulo())
				|| UltimoCostoModuloPrezzoCalculator.TIPO_MODULO.equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			this.setBackground(new Color(255, 240, 245));
		} else if (ContrattoModuloPrezzoCalculator.TIPO_MODULO.equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			this.setBackground(new Color(224, 255, 255));
		}

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		createPanelControl();
	}

	/**
	 * creazione dei controlli.
	 */
	private void createPanelControl() {
		JPanel datiPanel = new JPanel(new BorderLayout());

		datiPanel.add(getImageModulo(), BorderLayout.WEST);
		JPanel panel = new JPanel(new VerticalLayout(2));
		panel.add(getHeaderControl());
		panel.add(getDetailControl());
		panel.setOpaque(false);
		datiPanel.add(panel, BorderLayout.CENTER);
		datiPanel.setOpaque(false);

		this.add(datiPanel, BorderLayout.CENTER);

		this.add(getStrategiaControl(), BorderLayout.EAST);
	}

	/**
	 * 
	 * @return controlli del detail
	 */
	private JComponent getDetailControl() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html><U>Valore:</U> ");
		stringBuffer.append(this.risultatoModuloPrezzo.getStringValue());
		stringBuffer.append("<br>");
		stringBuffer.append("<U>Soglia quantità:</U> ");
		stringBuffer.append(this.risultatoModuloPrezzo.getQuantita());
		stringBuffer.append("<br>");
		stringBuffer.append("<U>Abilitato:</U> ");
		stringBuffer.append(this.risultatoModuloPrezzo.isAbilitato());
		stringBuffer.append("</html>");

		panel.add(new JLabel(stringBuffer.toString()));
		panel.setOpaque(false);

		return panel;
	}

	/**
	 * 
	 * @return controlli per l'header
	 */
	private JComponent getHeaderControl() {
		JPanel panel = new JPanel(new BorderLayout());

		String tipoModulo = new String("");
		String descrizione = "";
		if ("LISTINO".equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			tipoModulo = messageSource.getMessage(Listino.class.getName(), new Object[] {}, Locale.getDefault());
			descrizione = "<html>" + tipoModulo.toUpperCase() + "<br>" + "Codice: "
					+ this.risultatoModuloPrezzo.getCodiceModulo() + "<br>" + "Descrizione: "
					+ this.risultatoModuloPrezzo.getDescrizioneModulo() + "</html>";
		} else if ("CONTRATTO".equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			tipoModulo = messageSource.getMessage(Contratto.class.getName(), new Object[] {}, Locale.getDefault());
			descrizione = "<html>" + tipoModulo.toUpperCase() + "<br>" + "Codice: "
					+ this.risultatoModuloPrezzo.getCodiceModulo() + "<br>" + "Descrizione: "
					+ this.risultatoModuloPrezzo.getDescrizioneModulo() + "</html>";
		} else if (UltimoCostoModuloPrezzoCalculator.TIPO_MODULO.equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			tipoModulo = messageSource.getMessage("ultimoCosto", new Object[] {}, Locale.getDefault());
			descrizione = "<html>" + tipoModulo.toUpperCase() + "<br>" + "Num documento: "
					+ this.risultatoModuloPrezzo.getCodiceModulo() + "<br>" + "Data: "
					+ this.risultatoModuloPrezzo.getDescrizioneModulo() + "</html>";
		}

		JLabel labelHeader = new JLabel(descrizione);
		Font font = new Font(labelHeader.getFont().getName(), Font.BOLD, labelHeader.getFont().getSize());
		labelHeader.setFont(font);

		panel.add(labelHeader, BorderLayout.WEST);
		panel.setOpaque(false);
		return panel;
	}

	/**
	 * 
	 * @return immagine per il modulo
	 */
	private JComponent getImageModulo() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel labelImageModulo = new JLabel("");

		if ("LISTINO".equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			labelImageModulo.setIcon(iconSource.getIcon("listinoModuloPrezzo"));
		} else if ("CONTRATTO".equals(this.risultatoModuloPrezzo.getTipoModulo())) {
			/*
			 * if (this.risultatoModuloPrezzo.get.contains("%")) {
			 * labelImageModulo.setIcon(iconSource.getIcon("contrattoModuloPrezzoSconto")); } else {
			 * labelImageModulo.setIcon(iconSource.getIcon("contrattoModuloPrezzoImporto")); }
			 */
		}

		labelImageModulo.setOpaque(false);

		panel.add(labelImageModulo, BorderLayout.CENTER);
		panel.setOpaque(false);

		return panel;
	}

	/**
	 * 
	 * @return controlli per la strategia
	 */
	private JComponent getStrategiaControl() {
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
		if (this.risultatoModuloPrezzo.getStrategia() == null) {
			return new JLabel("DISABLE");
		}

		String descrizione = messageSource.getMessage(this.risultatoModuloPrezzo.getStrategia().getClass().getName()
				+ "." + this.risultatoModuloPrezzo.getStrategia().name(), new Object[] {}, Locale.getDefault());

		JLabel strategialabel = new JLabel(descrizione);
		JLabel label = new JLabel("<html><FONT size=6>" + descrizione.substring(0, 1) + "</font></html>");

		JPanel panelIniziale = new JPanel(new BorderLayout());
		panelIniziale.add(label, BorderLayout.EAST);
		panelIniziale.setOpaque(false);

		JPanel panelDescrizione = new JPanel(new BorderLayout());
		panelDescrizione.add(strategialabel, BorderLayout.EAST);
		panelDescrizione.setOpaque(false);

		rootPanel.add(panelIniziale, BorderLayout.NORTH);
		rootPanel.setOpaque(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(panelDescrizione, BorderLayout.NORTH);
		panel.setOpaque(false);
		rootPanel.add(panel, BorderLayout.CENTER);

		return rootPanel;
	}
}
