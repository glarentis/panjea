/**
 *
 */
package it.eurotn.panjea.preventivi.rich.forms.tipoareapreventivo;

import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author mattia
 *
 */
public class TipoAreaPreventivoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "tipoAreaPreventivoForm";
	private static final String SEPARATOR_TIPO_AREA_PREVENTIVO = "tipoAreaPreventivo.areaPreventivo.label";
	private static final String SEPARATOR_STAMPA_AREA_PREVENTIVO = "tipoAreaPreventivo.stampa.label";
	private static final String SEPARATOR_EVASIONE_AREA_PREVENTIVO = "tipoAreaPreventivo.evasione.label";

	private JPanel pannelloLegendaMaschera;

	/**
	 *
	 * @param tipoAreaPreventivo
	 *            tipo are preventivo
	 */
	public TipoAreaPreventivoForm(final TipoAreaPreventivo tipoAreaPreventivo) {
		super(PanjeaFormModelHelper.createFormModel(tipoAreaPreventivo, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:50dlu,4dlu,right:pref,4dlu,left:default",
				"3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		JLabel tdcodlabel = builder.addLabel("tipoDocumento.codice", getComponentFactory().createLabel(""), 1, 2);
		tdcodlabel.setFont(new Font(tdcodlabel.getFont().getName(), Font.BOLD, tdcodlabel.getFont().getSize()));
		JTextField tdcod = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "codice", 3, 2);
		tdcod.setColumns(18);
		tdcod.setFont(new Font(tdcod.getFont().getName(), Font.BOLD, tdcod.getFont().getSize()));

		JLabel tddesclabel = builder.addLabel("tipoDocumento.descrizione", getComponentFactory().createLabel(""), 5, 2);
		tddesclabel.setFont(new Font(tddesclabel.getFont().getName(), Font.BOLD, tddesclabel.getFont().getSize()));
		JTextField tddesc = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "descrizione", 7, 2);
		tddesc.setColumns(18);
		tddesc.setFont(new Font(tddesc.getFont().getName(), Font.BOLD, tddesc.getFont().getSize()));
		builder.nextRow();

		builder.addHorizontalSeparator(getMessage(SEPARATOR_TIPO_AREA_PREVENTIVO), 7);
		builder.nextRow();

		builder.addPropertyAndLabel("dataDocLikeDataReg", 1, 6);
		builder.nextRow();

		builder.addHorizontalSeparator(getMessage(SEPARATOR_STAMPA_AREA_PREVENTIVO), 7);
		builder.nextRow();

		builder.addPropertyAndLabel("descrizionePerStampa", 1, 10, 5, 1);
		builder.nextRow();

		builder.addHorizontalSeparator(getMessage(SEPARATOR_EVASIONE_AREA_PREVENTIVO), 7);
		builder.nextRow();

		builder.addPropertyAndLabel("durataValiditaDefault");
		builder.nextRow();

		builder.addPropertyAndLabel("processaSuAccettazione", 1);
		builder.nextRow();

		builder.addLabel("tipoDocumentoEvasione", 1);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoDocumentoEvasione", new String[] {
				"tipoDocumento.codice", "tipoDocumento.descrizione" });
		SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3, 18, 5, 1);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
		builder.nextRow();

		builder.addPropertyAndLabel("noteSuDestinazione", 1, 20);
		builder.nextRow();

		builder.addPropertyAndLabel("tipoDocumentoEvasioneDescrizioneMaschera", 1, 22, 5);
		builder.nextRow();

		// pannello per visualizzare la legenda della maschera
		builder.setComponentAttributes("f,f");
		builder.addComponent(createPanelLegenda(), 1, 24, 7, 1);

		return builder.getPanel();
	}

	/**
	 * Crea la legenda per le variabili possibili per la maschera.
	 *
	 * @return maschera in formato html
	 */
	private String createLegendaTextForMaschera() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");
		stringBuffer.append("<B>");
		stringBuffer.append(getMessage("legenda").toUpperCase());
		stringBuffer.append("</B><BR><hr></hr>");
		stringBuffer.append("<ul>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$numeroDocumento$</B> = ");
		stringBuffer.append(getMessage("numeroDocumento"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$dataDocumento$</B> = ");
		stringBuffer.append(getMessage("dataDocumento"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$codiceTipoDocumento$</B> = ");
		stringBuffer.append(getMessage("codiceTipoDocumento"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$descrizioneTipoDocumento$</B> = ");
		stringBuffer.append(getMessage("descrizioneTipoDocumento"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$codiceEntita$</B> = ");
		stringBuffer.append(getMessage("codiceEntita"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$descrizioneEntita$</B> = ");
		stringBuffer.append(getMessage("descrizioneEntita"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$sede$</B> = ");
		stringBuffer.append(getMessage("descrizioneSede"));
		stringBuffer.append("</li>");

		stringBuffer.append("</ul>");
		stringBuffer.append("</html>");

		return stringBuffer.toString();
	}

	/**
	 *
	 * @return pannello con la legenda per la maschera.
	 */
	private JPanel createPanelLegenda() {
		pannelloLegendaMaschera = getComponentFactory().createPanel(new BorderLayout());
		JLabel labelLegenda = new JLabel(createLegendaTextForMaschera());
		pannelloLegendaMaschera.add(labelLegenda, BorderLayout.NORTH);
		return pannelloLegendaMaschera;
	}

}
