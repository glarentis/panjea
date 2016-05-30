package it.eurotn.panjea.ordini.rich.forms.tipoareaordine;

import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
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

public class TipoAreaOrdineForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "tipoAreaOrdineForm";
	private static final String SEPARATOR_TIPO_AREA_ORDINE = "tipoAreaOrdine.areaOrdine.label";
	private static final String SEPARATOR_STAMPA_AREA_ORDINE = "tipoAreaOrdine.stampa.label";
	private static final String SEPARATOR_EVASIONE_AREA_ORDINE = "tipoAreaOrdine.evasione.label";

	private JPanel pannelloLegendaMascheraTipoDocEvasione;

	/**
	 * 
	 * @param tipoAreaOrdine
	 *            tipoAreaOrdine da settare su init
	 */
	public TipoAreaOrdineForm(final TipoAreaOrdine tipoAreaOrdine) {
		super(PanjeaFormModelHelper.createFormModel(tipoAreaOrdine, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:50dlu, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default",
				"3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		JLabel tdcodlabel = builder.addLabel("tipoDocumento.codice", getComponentFactory().createLabel(""), 1, 2);
		tdcodlabel.setFont(new Font(tdcodlabel.getFont().getName(), Font.BOLD, tdcodlabel.getFont().getSize()));
		JTextField tdcod = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "codice", 3, 2, 3, 1);
		tdcod.setColumns(18);
		tdcod.setFont(new Font(tdcod.getFont().getName(), Font.BOLD, tdcod.getFont().getSize()));

		JLabel tddesclabel = builder.addLabel("tipoDocumento.descrizione", getComponentFactory().createLabel(""), 9, 2);
		tddesclabel.setFont(new Font(tddesclabel.getFont().getName(), Font.BOLD, tddesclabel.getFont().getSize()));
		JTextField tddesc = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "descrizione", 11, 2);
		tddesc.setColumns(18);
		tddesc.setFont(new Font(tddesc.getFont().getName(), Font.BOLD, tddesc.getFont().getSize()));
		builder.nextRow();

		builder.addHorizontalSeparator(getMessage(SEPARATOR_TIPO_AREA_ORDINE), 11);
		builder.nextRow();

		builder.addPropertyAndLabel("dataDocLikeDataReg", 1, 6, 3);
		builder.nextRow();

		builder.addPropertyAndLabel("depositoOrigineBloccato", 1, 8);
		Binding bindingDepOrigine = bf.createBoundSearchText("depositoOrigine",
				new String[] { "codice", "descrizione" });
		builder.addLabel("depositoOrigine", 5, 8);
		SearchPanel searchPanelDeposito = (SearchPanel) builder.addBinding(bindingDepOrigine, 7, 8, 5, 1);
		searchPanelDeposito.getTextFields().get("codice").setColumns(5);
		searchPanelDeposito.getTextFields().get("descrizione").setColumns(18);
		builder.nextRow();

		builder.addPropertyAndLabel("ordineProduzione", 1, 10);
		builder.nextRow();

		builder.addHorizontalSeparator(getMessage(SEPARATOR_STAMPA_AREA_ORDINE), 11);
		builder.nextRow();

		builder.setLabelAttributes("r, t");

		builder.addPropertyAndLabel("descrizionePerStampa", 1, 16, 9, 1);
		builder.nextRow();

		builder.addHorizontalSeparator(getMessage(SEPARATOR_EVASIONE_AREA_ORDINE), 11);
		builder.nextRow();

		builder.addPropertyAndLabel("noteSuDestinazione", 1, 20);
		builder.nextRow();

		builder.addPropertyAndLabel("tipoDocumentoEvasioneDescrizioneMaschera", 1, 22, 9);
		builder.nextRow();

		// pannello per visualizzare la legenda della maschera per il tipo
		// documento di evasione
		builder.setComponentAttributes("f,f");
		builder.addComponent(createPanelLegendaFatturazione(), 1, 24, 11, 1);

		return builder.getPanel();
	}

	/**
	 * Crea la legenda per le variabili possibili per la maschera di evasione.
	 * 
	 * @return maschera in formato html
	 */
	private String createLegendaTextForMascheraEvasione() {
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

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$areaSpedizione$</B> = ");
		stringBuffer.append(getMessage("Rullo di spedizione"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$numeroOrdineRiferimento$</B> = ");
		stringBuffer.append(getMessage("riferimentiOrdine.numeroOrdine"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$modalitaRicezioneOrdineRiferimento$</B> = ");
		stringBuffer.append(getMessage("riferimentiOrdine.modalitaRicezione"));
		stringBuffer.append("</li>");

		stringBuffer.append("<li>");
		stringBuffer.append("<B>$dataDocumentoOrdineRiferimento$</B> = ");
		stringBuffer.append(getMessage("riferimentiOrdine.dataOrdine"));
		stringBuffer.append("</li>");

		stringBuffer.append("</ul>");
		stringBuffer.append("</html>");

		return stringBuffer.toString();
	}

	/**
	 * 
	 * @return pannello con la legenda per la maschera di rifatturazione.
	 */
	private JPanel createPanelLegendaFatturazione() {
		pannelloLegendaMascheraTipoDocEvasione = getComponentFactory().createPanel(new BorderLayout());
		JLabel labelLegenda = new JLabel(createLegendaTextForMascheraEvasione());
		pannelloLegendaMascheraTipoDocEvasione.add(labelLegenda, BorderLayout.NORTH);
		return pannelloLegendaMascheraTipoDocEvasione;
	}
}
