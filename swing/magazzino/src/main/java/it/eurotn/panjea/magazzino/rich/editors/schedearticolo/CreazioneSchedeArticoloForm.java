/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.ArticoliRicercaTablePage;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo.CustomFilter;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class CreazioneSchedeArticoloForm extends PanjeaAbstractForm {

	private class AnnoMesePropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getValueModel("articoli").setValue(new TreeSet<ArticoloRicerca>());
			articoliRicercaTablePage.setRows(new TreeSet<ArticoloRicerca>());
		}

	}

	public static final String FORM_ID = "creazioneSchedeArticoloForm";
	public static final String RICERCA_ARTICOLI_COMMAND_ID = FORM_ID + ".aggiungiArticoliCommand";

	private static final String CUSTOM_FILTER_JNDI_NAME = "Panjea.MagazzinoSchedeArticoloManager";

	private AziendaCorrente aziendaCorrente;

	private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand;

	private ArticoliRicercaTablePage articoliRicercaTablePage;

	private AnnoMesePropertyChangeListener annoMesePropertyChangeListener;

	/**
	 * Costruttore.
	 */
	public CreazioneSchedeArticoloForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneSchedeArticoli(), false, FORM_ID), FORM_ID);
		this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:40dlu, 10dlu, right:pref,4dlu,fill:40dlu,left:20dlu, fill:220dlu,left:pref",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,fill:default:grow");
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

		builder.addPropertyAndLabel("note", 1, 4, 8);

		builder.nextRow();
		JPanel articoliPanel = getComponentFactory().createPanel(new BorderLayout());
		articoliPanel.setBorder(BorderFactory.createTitledBorder("Articoli selezionati"));
		articoliPanel.add(getRicercaAvanzataArticoliCommand().createButton(), BorderLayout.NORTH);
		articoliRicercaTablePage = new ArticoliRicercaTablePage();
		articoliRicercaTablePage.setMagazzinoAnagraficaBD((IMagazzinoAnagraficaBD) RcpSupport
				.getBean(MagazzinoAnagraficaBD.BEAN_ID));
		articoliPanel.add(articoliRicercaTablePage.getTable().getComponent(), BorderLayout.CENTER);
		articoliRicercaTablePage.getTable().getTable().addKeyListener(new KeyAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					List<ArticoloRicerca> articoliRicerca = articoliRicercaTablePage.getTable().getSelectedObjects();
					if (articoliRicerca != null && articoliRicerca.size() > 0) {
						ParametriCreazioneSchedeArticoli parametri = ((ParametriCreazioneSchedeArticoli) getFormModel()
								.getFormObject());
						parametri.getArticoli().removeAll(articoliRicerca);
						getValueModel("articoli").setValue(parametri.getArticoli());
						articoliRicercaTablePage.setRows(
								(Collection<ArticoloRicerca>) getValueModel("articoli").getValue());
						CreazioneSchedeArticoloForm.this.getFormModel().validate();
					}
				}
			};
		});
		builder.addComponent(articoliPanel, 1, 10, 9, 1);

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(getDescrizioneLabel(), BorderLayout.NORTH);
		rootPanel.add(builder.getPanel(), BorderLayout.CENTER);
		return rootPanel;
	}

	@Override
	protected Object createNewObject() {
		ParametriCreazioneSchedeArticoli parametri = new ParametriCreazioneSchedeArticoli();
		parametri.setAnno(aziendaCorrente.getAnnoMagazzino());
		parametri.setMese(Calendar.getInstance().get(Calendar.MONTH) + 1);

		articoliRicercaTablePage.setRows(new TreeSet<ArticoloRicerca>());
		return parametri;
	}

	/**
	 * @return the annoMesePropertyChangeListener
	 */
	public AnnoMesePropertyChangeListener getAnnoMesePropertyChangeListener() {
		if (annoMesePropertyChangeListener == null) {
			annoMesePropertyChangeListener = new AnnoMesePropertyChangeListener();
		}

		return annoMesePropertyChangeListener;
	}

	/**
	 * Restituisce la label della descrizione del form.
	 * 
	 * @return label
	 */
	private JLabel getDescrizioneLabel() {
		JLabel label = new JLabel("Inserire i parametri per la creazione delle schede articolo.");
		label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		Font f = label.getFont();
		label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		return label;
	}

	/**
	 * @return the ricercaAvanzataArticoliCommand
	 */
	private RicercaAvanzataArticoliCommand getRicercaAvanzataArticoliCommand() {
		if (ricercaAvanzataArticoliCommand == null) {
			ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliCommand(RICERCA_ARTICOLI_COMMAND_ID);
			ricercaAvanzataArticoliCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@SuppressWarnings("unchecked")
				@Override
				public void postExecution(ActionCommand command) {
					List<ArticoloRicerca> articoliRicerca = ((RicercaAvanzataArticoliCommand) command)
							.getArticoliSelezionati();
					if (articoliRicerca != null && articoliRicerca.size() > 0) {
						((Set<ArticoloRicerca>) getValueModel("articoli").getValue()).addAll(articoliRicerca);
						articoliRicercaTablePage.setRows(
								(Collection<ArticoloRicerca>) getValueModel("articoli").getValue());
						CreazioneSchedeArticoloForm.this.getFormModel().validate();
					}
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					CustomFilter customFilter = new ParametriRicercaArticolo().new CustomFilter();
					customFilter.setJndiFilterBeanName(CUSTOM_FILTER_JNDI_NAME);
					customFilter.getFilterParameterMap().put("annoScheda", getValueModel("anno").getValue());
					customFilter.getFilterParameterMap().put("meseScheda", getValueModel("mese").getValue());
					command.addParameter(RicercaAvanzataArticoliCommand.CUSTOM_FILTER_PARAM, customFilter);
					return true;
				}
			});
		}

		return ricercaAvanzataArticoliCommand;
	}

	@Override
	public void setFormObject(Object formObject) {

		getValueModel("anno").removeValueChangeListener(getAnnoMesePropertyChangeListener());
		getValueModel("mese").removeValueChangeListener(getAnnoMesePropertyChangeListener());

		super.setFormObject(formObject);

		if (formObject != null) {
			ParametriCreazioneSchedeArticoli parametri = (ParametriCreazioneSchedeArticoli) formObject;
			articoliRicercaTablePage.setRows(parametri.getArticoli());
		}

		getValueModel("anno").addValueChangeListener(getAnnoMesePropertyChangeListener());
		getValueModel("mese").addValueChangeListener(getAnnoMesePropertyChangeListener());
	}

}
