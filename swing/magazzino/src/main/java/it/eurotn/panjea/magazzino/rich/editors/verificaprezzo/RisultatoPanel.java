package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.plugin.PluginManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class RisultatoPanel extends JPanel {

	private static final long serialVersionUID = -8073906988725449675L;
	private PluginManager pluginManager;

	/**
	 * Pannello per descrivere un risultato di uno scaglione.
	 * 
	 * @param risultatoPrezzo
	 *            risultatoPrezzo
	 * @param variazione
	 *            variazione
	 * @param risultatoProvvigioni
	 *            provvigioni
	 * @param prezzoNetto
	 *            prezzoNetto
	 * @param prezzoIvato
	 *            prezzoIvato
	 * @param ultimoCosto
	 *            ultimoCosto
	 * @param ricarico
	 *            ricarico
	 * @param percRicarico
	 *            percRicarico
	 * @param percScontoMassimo
	 *            percScontoMassimo
	 * @param unitaMisura
	 *            unitaMisura
	 */
	public RisultatoPanel(final RisultatoPrezzo<BigDecimal> risultatoPrezzo, final RisultatoPrezzo<Sconto> variazione,
			final RisultatoPrezzo<BigDecimal> risultatoProvvigioni, final String prezzoNetto, final String prezzoIvato,
			final String ultimoCosto, final String ricarico, final String percRicarico, final String percScontoMassimo,
			final String unitaMisura) {

		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

		String um = unitaMisura;
		if (um == null) {
			um = "";
		}
		JLabel labelPrezzo = new JLabel("<html><b><FONT size=4> Prezzo [" + um + "]</b></html>");
		JLabel labelPrezzoNetto = new JLabel("<html><b><FONT size=4>Prezzo netto</b></html>");
		JLabel labelUltimoCosto = new JLabel("<html><b><FONT size=4>Ultimo costo</b></html>");
		JLabel labelScontoMax = new JLabel("<html><b><FONT size=4>Sconto Massimo</b></html>");
		JLabel labelVariazione = new JLabel("<html><b><FONT size=4>Variazione</b></html>");
		JLabel labelPrezzoIvato = new JLabel("<html><b><FONT size=4>Prezzo ivato</b></html>");
		JLabel labelRicarico = new JLabel("<html><b><FONT size=4>Ricarico</b></html>");
		JLabel labelProvvigione = new JLabel("<html><b><FONT size=4>Provvigione</b></html>");
		JLabel labelPrezzoValore = new JLabel();
		JLabel labelPrezzoNettoValore = new JLabel("<HTML><b>" + prezzoNetto + "</b></HTML>");
		labelPrezzoNettoValore.setName("labelPrezzoNettoValore");
		JLabel labelUltimoCostoValore = new JLabel(ultimoCosto);
		labelUltimoCostoValore.setName("labelUltimoCostoValore");
		JLabel labelScontoMaxValore = new JLabel(percScontoMassimo + " %");
		labelScontoMaxValore.setName("labelScontoMaxValore");
		JLabel labelPrezzoIvatoValore = new JLabel();
		labelPrezzoIvatoValore.setName("labelPrezzoIvatoValore");
		JLabel labelVariazioneValore = new JLabel("nessuna");
		labelVariazioneValore.setName("labelVariazioneValore");
		JLabel labelRicaricoValore = new JLabel();
		labelRicaricoValore.setName("labelRicaricoValore");
		JLabel labelProvvigioneValore = new JLabel();
		labelProvvigioneValore.setName("labelProvvigioneValore");

		if (risultatoPrezzo != null) {
			labelRicaricoValore.setText(ricarico + " (" + percRicarico + ")");
		}

		if (variazione != null) {
			labelVariazioneValore.setText(variazione.getStringValue());
			labelVariazioneValore.setToolTipText(variazione.getStringValue());
		}

		if (prezzoIvato != null) {
			labelPrezzoIvatoValore.setText("<HTML><b>" + prezzoIvato + "</b></HTML>");
		}

		FormLayout formLayout = new FormLayout("left:pref,5 dlu,70 dlu,1 dlu,left:pref,5 dlu,70 dlu",
				"default,default,default,default,default");
		setLayout(formLayout);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		CellConstraints cc = new CellConstraints();
		add(labelPrezzo, cc.xy(1, 1));
		add(labelPrezzoValore, cc.xy(3, 1));
		add(labelPrezzoNetto, cc.xy(1, 3));
		add(labelPrezzoNettoValore, cc.xy(3, 3));
		add(labelUltimoCosto, cc.xy(1, 4));
		add(labelUltimoCostoValore, cc.xy(3, 4));
		add(labelScontoMax, cc.xy(1, 5));
		add(labelScontoMaxValore, cc.xy(3, 5));
		add(labelVariazione, cc.xy(5, 1));
		add(labelVariazioneValore, cc.xy(7, 1));
		add(labelPrezzoIvato, cc.xy(5, 3));
		add(labelPrezzoIvatoValore, cc.xy(7, 3));
		add(labelRicarico, cc.xy(5, 4));
		add(labelRicaricoValore, cc.xy(7, 4));

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			add(labelProvvigione, cc.xy(5, 5));
			add(labelProvvigioneValore, cc.xy(7, 5));

			if (risultatoProvvigioni != null) {
				labelProvvigioneValore.setText(formatta(risultatoProvvigioni.getValue(), 2));
			} else {
				labelProvvigioneValore.setText("");
			}
		} else {
			add(new JLabel(" "), cc.xy(5, 5));
		}

		if (risultatoPrezzo != null) {
			labelPrezzoValore.setText(formatta(risultatoPrezzo.getValue(), risultatoPrezzo.getNumeroDecimali()));
		} else {
			labelPrezzoValore.setText("");
		}
	}

	/**
	 * formatta un bigDecimal .
	 * 
	 * @param value
	 *            valore da formattare
	 * @param numeroDecimali
	 *            numero decimali
	 * @return stringa formattata
	 */
	private String formatta(BigDecimal value, int numeroDecimali) {
		Format format = new DecimalFormat(VerificaPrezzoPage.NUMBER_FORMAT + "."
				+ VerificaPrezzoPage.STR_ZERI.substring(0, numeroDecimali));
		if (value != null) {
			return format.format(value);
		}

		return "";
	}
}
