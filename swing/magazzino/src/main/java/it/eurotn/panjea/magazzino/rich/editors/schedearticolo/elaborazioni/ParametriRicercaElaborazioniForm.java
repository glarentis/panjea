/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo.elaborazioni;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaElaborazioniForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaElaborazioniForm";

	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaElaborazioniForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaElaborazioni(), false, FORM_ID), FORM_ID);
		this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:40dlu, 10dlu, right:pref,4dlu,fill:40dlu,left:20dlu, fill:220dlu,left:pref",
				"4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("mese", 1);
		JTextField meseField = (JTextField) bf.createBinding("mese").getControl();
		meseField.setHorizontalAlignment(JTextField.RIGHT);
		builder.addComponent(meseField, 3);

		builder.addLabel("anno", 5);
		JTextField annoField = (JTextField) bf.createBinding("anno").getControl();
		annoField.setHorizontalAlignment(JTextField.RIGHT);
		builder.addComponent(annoField, 7);
		builder.nextRow();

		builder.addPropertyAndLabel("nota", 1, 4, 7);

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(getDescrizioneLabel(), BorderLayout.NORTH);
		rootPanel.add(builder.getPanel(), BorderLayout.CENTER);
		return rootPanel;
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaElaborazioni parametri = new ParametriRicercaElaborazioni();
		parametri.setAnno(aziendaCorrente.getAnnoMagazzino());
		parametri.setMese(Calendar.getInstance().get(Calendar.MONTH) + 1);

		return parametri;
	}

	/**
	 * Restituisce la label della descrizione del form.
	 * 
	 * @return label
	 */
	private JLabel getDescrizioneLabel() {
		JLabel label = new JLabel("Visualizzazione delle elaborazioni effettuate e in corso");
		label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		Font f = label.getFont();
		label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		return label;
	}

}
