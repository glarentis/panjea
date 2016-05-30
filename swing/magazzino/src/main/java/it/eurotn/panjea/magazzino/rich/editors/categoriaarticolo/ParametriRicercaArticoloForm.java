/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo.StatoArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.QuickFilterField;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.SelectAllUtils;

/**
 * @author leonardo
 */
public class ParametriRicercaArticoloForm extends PanjeaAbstractForm implements Focussable {

	private class FiltroArticoloQuickFilterField extends QuickFilterField {

		private static final long serialVersionUID = -8678527570251327439L;

		private JidePopupMenu jidePopupMenu = null;

		@Override
		public void applyFilter(String paramString) {
			if (applyFilter) {
				getValueModel("filtro").setValue(paramString);
			}
		}

		@Override
		protected JidePopupMenu createContextMenu() {
			if (jidePopupMenu == null) {
				jidePopupMenu = new JidePopupMenu();

				String condizioniDisponibili = StringUtils.replaceChars(ParametriRicercaArticolo.PATTERN_OPERATORE,
						"|", " ");

				jidePopupMenu.add(new JMenuItem("Ricerca semplice: codice/descrizione dell'articolo"));
				jidePopupMenu
				.add(new JMenuItem("Ricerca contenuto: usare * seguito dalla parte di codice/descrizione"));
				jidePopupMenu.add(new JMenuItem(
						"Ricerca contenuto singolo carattere: usare _ nella parte di codice/descrizione"));
				jidePopupMenu.add(new JMenuItem("Ricerca da codice a codice: separare con | i due codici"));
				jidePopupMenu.add(new JMenuItem("Ricerca per codice a barre: #barcode#"));
				jidePopupMenu.add(new JMenuItem("Ricerca per codice interno: £codiceInterno£"));
				jidePopupMenu.add(new JMenuItem("Ricerca per codice entita: @codiceEntita@"));
				jidePopupMenu
				.add(new JMenuItem(
						"Ricerca degli articoli con attributo: $attributo$ ( è possibile utilizzare * o _ per la ricerca del contenuto )"));
				jidePopupMenu.add(new JMenuItem(
						"Ricerca degli articoli con valore attributo che soddisfa la condizione: $attributo$>=10"));
				jidePopupMenu.add(new JMenuItem("    condizioni disponibili: " + condizioniDisponibili));
				jidePopupMenu
				.add(new JMenuItem(
						"Ricerca combinata: la combinazione dei casi sopra, separare con uno spazio ogni filtro aggiunto"));
			}
			return jidePopupMenu;
		}

		@Override
		public Icon getFilterIcon() {
			return RcpSupport.getIcon("severity.info");
		}
	}

	/**
	 * @author giangi
	 * @version 1.0, 15/apr/2014
	 */
	private class FiltroKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.isControlDown() || e.isAltDown() || e.isShiftDown() || e.isAltGraphDown() || e.isMetaDown()) {
				// premuto tasto funzione, non eseguo nessuna operazione sulla
				// searchtext");
				return;
			}
			// non deve essere intercettato il keyevent se il popup di hints è visibile altrimenti non posso scorrere i
			// valori proposti
			if (!hints.isHintsPopupVisible()) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					if (downCommand != null) {
						downCommand.execute();
						e.consume();
					}
					break;
				case KeyEvent.VK_UP:
					if (upCommand != null) {
						upCommand.execute();
						e.consume();
					}
					break;
				default:
					return;
				}
			}
		}
	}

	private class PropertyPersisteValueChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Boolean calcolaGiacenza = (Boolean) getValueModel("calcolaGiacenza").getValue();

			SettingsManager settings = (SettingsManager) Application.services().getService(SettingsManager.class);
			try {
				settings.getUserSettings().setBoolean(getFormModel().getId() + "calcolaGiacenza", calcolaGiacenza);
			} catch (SettingsException e) {
				e.printStackTrace();
			}

		}

	}

	private static final int SEARCH_DELAY_TIME = 250;

	private static final String FORM_ID = "parametriRicercaArticoliForm";

	private static final String FORMMODEL_ID = "parametriRicercaArticoliFormModel";

	private FiltroKeyListener filtroKeyListener = null;

	private JTextField filtroTextField = null;

	private FiltroArticoloQuickFilterField quickFilterField = null;
	private JLabel labelFiltro = null;

	private JecListDataIntelliHints hints;

	private AbstractCommand upCommand;
	private AbstractCommand downCommand;

	private boolean applyFilter;

	/**
	 * Costruttore.
	 *
	 * @param formModelParentId
	 *            form model parent
	 */
	public ParametriRicercaArticoloForm(final String formModelParentId) {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaArticolo(), false, formModelParentId
				+ FORMMODEL_ID), FORM_ID);
		PanjeaSwingUtil.addValueModelToForm(TipoEntita.CLIENTE, this.getFormModel(), TipoEntita.class,
				"tipiEntitaCliente", true);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:default:grow, 5dlu, right:pref,4dlu,left:pref, 5dlu, right:pref,4dlu,left:pref, 5dlu",
				"2dlu,default,2dlu,default,2dlu,default,2dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean("magazzinoAnagraficaBD");
		List<TipoAttributo> tipiAttributo = magazzinoAnagraficaBD.caricaTipiAttributo();
		final List<String> variabili = new ArrayList<String>();
		for (TipoAttributo tipoAttributo : tipiAttributo) {
			variabili.add(tipoAttributo.getCodiceFormula());
		}

		quickFilterField = new FiltroArticoloQuickFilterField();
		quickFilterField.setSearchingDelay(SEARCH_DELAY_TIME);
		filtroTextField = quickFilterField.getTextField();
		filtroTextField.addKeyListener(getFiltroKeyListener());

		labelFiltro = builder.addLabel("filtro", 1);
		// sulla jtextfield non posso definire mnemonics via properties, devo aggiungerli a mano sui componenti
		labelFiltro.setDisplayedMnemonic('f');
		labelFiltro.setLabelFor(quickFilterField);
		builder.addComponent(quickFilterField, 3, 2, 5, 1);

		SelectAllUtils.install(filtroTextField);
		hints = new JecListDataIntelliHints(filtroTextField, variabili);

		builder.addPropertyAndLabel("includiArticoliCategorieFiglie", 9);
		builder.nextRow();
		builder.addPropertyAndLabel("statoArticolo");
		builder.addPropertyAndLabel("calcolaGiacenza", 5);
		builder.addPropertyAndLabel("soloDistinte", 9);

		SettingsManager settings = (SettingsManager) Application.services().getService(SettingsManager.class);
		Boolean calcolaGiacenza = false;
		try {
			calcolaGiacenza = settings.getUserSettings().getBoolean(getFormModel().getId() + "calcolaGiacenza");
		} catch (SettingsException e) {
			e.printStackTrace();
		}

		getValueModel("calcolaGiacenza").setValue(calcolaGiacenza);
		getValueModel("statoArticolo").setValue(StatoArticolo.ABILITATO);
		commit();
		getValueModel("calcolaGiacenza").addValueChangeListener(new PropertyPersisteValueChange());
		builder.nextRow();
		builder.addLabel("assortimento", 1);
		SearchPanel searchAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("cliente", new String[] {
				"codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaCliente" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }, EntitaLite.class), 3, 6, 5, 1);
		searchAgente.getTextFields().get("codice").setColumns(6);
		return builder.getPanel();
	}

	@Override
	public void dispose() {
		if (filtroTextField != null) {
			filtroTextField.removeKeyListener(getFiltroKeyListener());
		}
		super.dispose();
	}

	/**
	 * @return filtroKeyListener
	 */
	private FiltroKeyListener getFiltroKeyListener() {
		if (filtroKeyListener == null) {
			filtroKeyListener = new FiltroKeyListener();
		}
		return filtroKeyListener;
	}

	@Override
	public void grabFocus() {
		quickFilterField.getTextField().requestFocusInWindow();
	}

	/**
	 * @return se il filtro detiene il focus; utile nel caso in cui con il mouse clicco direttamente nel campo di testo
	 *         o se uso lo mnemonic per il filtro
	 */
	public boolean isFiltroFocusOwner() {
		boolean isOwner = false;
		if (this.isControlCreated()) {
			// se uso lo mnemonic associato la label è detentrice del focus, se clicco con il mouse sul componente
			// invece è la textField, per sicurezza metto anche la quickfilter
			isOwner = quickFilterField.isFocusOwner() || labelFiltro.isFocusOwner() || filtroTextField.isFocusOwner();
		}
		return isOwner;
	}

	/**
	 * @param downCommand
	 *            The downCommand to set.
	 */
	public void setDownCommand(AbstractCommand downCommand) {
		this.downCommand = downCommand;
	}

	@Override
	public void setFormObject(Object formObject) {
		ParametriRicercaArticolo parametriRicercaArticolo = (ParametriRicercaArticolo) formObject;

		// reimposto filtro e flag per includere articoli delle categorie figlie, se voglio annullare questi valori,
		// azzero i parametri con appposito command
		// String filtro = (String) getValue("filtro");
		// boolean includiCategorieFiglie = (Boolean) getValue("includiArticoliCategorieFiglie");
		// parametriRicercaArticolo.setFiltro(filtro);
		// parametriRicercaArticolo.setIncludiArticoliCategorieFiglie(includiCategorieFiglie);
		if (parametriRicercaArticolo.getIdCategoria() != null && parametriRicercaArticolo.getIdCategoria() == -1) {
			parametriRicercaArticolo.setIdCategoria(null);
		}
		applyFilter = false;
		quickFilterField.setSearchingDelay(0);
		quickFilterField.setText(parametriRicercaArticolo.getFiltro());
		quickFilterField.setSearchingDelay(SEARCH_DELAY_TIME);
		applyFilter = true;
		// super.setFormObject(parametriRicercaArticolo);
	}

	/**
	 * @param upCommand
	 *            The upCommand to set.
	 */
	public void setUpCommand(AbstractCommand upCommand) {
		this.upCommand = upCommand;
	}
}
