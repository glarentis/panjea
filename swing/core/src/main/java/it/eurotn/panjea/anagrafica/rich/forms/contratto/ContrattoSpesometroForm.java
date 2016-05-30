/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.forms.contratto;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.editors.contratto.DocumentiContrattoTablePage;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class ContrattoSpesometroForm extends PanjeaAbstractForm {

	/**
	 * 
	 * @author leonardo
	 */
	private class ContrattoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ContrattoSpesometro contratto = (ContrattoSpesometro) evt.getNewValue();
			// se il toggle command è selezionato carico i documenti del contratto, altrimenti non faccio nulla
			if (getDocumentiContrattoToggleCommand().isSelected()) {
				updateTableRowsDocumenti(contratto);
			}
			updateDocumentiContrattoControlVisibility(contratto);
		}

	}

	/**
	 * @author leonardo
	 */
	private class DocumentiContrattoToggleCommand extends JideToggleCommand {

		public static final String COMMAND_ID = "documentiContrattoToggleCommand";

		/**
		 * Costruttore.
		 */
		public DocumentiContrattoToggleCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void onDeselection() {
			super.onDeselection();
			if (documentiContrattoTablePage != null) {
				documentiContrattoTablePage.getControl().setVisible(false);
				updateTableRowsDocumenti(null);
			}
		}

		@Override
		protected void onSelection() {
			super.onSelection();
			if (documentiContrattoTablePage != null) {
				documentiContrattoTablePage.getControl().setVisible(true);
				updateTableRowsDocumenti((ContrattoSpesometro) getFormObject());
			}
		}

	}

	private static final String FORM_ID = "contrattoSpesometroForm";

	private IAnagraficaTabelleBD anagraficaTabelleBD = null;
	private EntitaLite entita = null;
	private DocumentiContrattoToggleCommand documentiContrattoToggleCommand = null;
	private ContrattoChangeListener contrattoChangeListener = null;
	private DocumentiContrattoTablePage documentiContrattoTablePage = null;

	/**
	 * Costruttore.
	 */
	public ContrattoSpesometroForm() {
		super(PanjeaFormModelHelper.createFormModel(new ContrattoSpesometro(), false, FORM_ID));

		// Aggiungo il value model che mi servirà solamente nella search text delle entità
		// per escludere le entità potenziali
		ValueModel entitaPotenzialiInRicercaValueModel = new ValueHolder(Boolean.FALSE);
		DefaultFieldMetadata entitaPotenzialimetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaPotenzialiInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("entitaPotenzialiPerRicerca", entitaPotenzialiInRicercaValueModel, entitaPotenzialimetaData);

		// Aggiungo il value model che mi servirà solamente nella search text delle entità
		// per cercare solo le entità abilitate
		ValueModel tipoEntitaValueModel = new ValueHolder(TipoEntita.CLIENTE);
		DefaultFieldMetadata tipoEntitaMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipoEntitaValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", tipoEntitaValueModel, tipoEntitaMetaData);

		// Aggiungo il value model che mi servirà solamente nella search text delle entità
		// per cercare solo le entità abilitate
		ValueModel entitaAbilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata entitaAbilitateMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaAbilitateInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("entitaAbilitateInRicerca", entitaAbilitateInRicercaValueModel, entitaAbilitateMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:pref, 10dlu, right:pref,4dlu,left:pref, 10dlu, right:pref,4dlu,left:pref, 200dlu, 10dlu, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("codice", 1, 2, 5);
		builder.nextRow();

		builder.addPropertyAndLabel("dataInizio", 1);
		builder.addPropertyAndLabel("dataFine", 5);
		builder.nextRow();

		builder.addLabel("entita", 1, 6);
		Binding bindEntita = getEntitaBinding(bf);
		builder.addBinding(bindEntita, 3, 6, 5, 1);
		builder.nextRow();

		builder.addPropertyAndLabel("note", 1, 8, 5);
		builder.nextRow();

		builder.addComponent(getDocumentiContrattoToggleCommand().createButton(), 9, 2);
		JComponent tableControlDocumenti = getDocumentiContrattoTablePage().getControl();
		builder.addComponent(tableControlDocumenti, 11, 2, 4, 10);

		addFormObjectChangeListener(getContrattoChangeListener());

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ContrattoSpesometro contrattoSpesometro = new ContrattoSpesometro();
		contrattoSpesometro.setEntita(entita);
		return contrattoSpesometro;
	}

	@Override
	public void dispose() {
		removeFormObjectChangeListener(getContrattoChangeListener());
		super.dispose();
	}

	/**
	 * @return ContrattoChangeListener
	 */
	private ContrattoChangeListener getContrattoChangeListener() {
		if (contrattoChangeListener == null) {
			contrattoChangeListener = new ContrattoChangeListener();
		}
		return contrattoChangeListener;
	}

	/**
	 * @return DocumentiContrattoTablePage
	 */
	private DocumentiContrattoTablePage getDocumentiContrattoTablePage() {
		if (documentiContrattoTablePage == null) {
			documentiContrattoTablePage = new DocumentiContrattoTablePage();
			documentiContrattoTablePage.setAnagraficaTabelleBD(anagraficaTabelleBD);
			documentiContrattoTablePage.getControl().setVisible(false);
			documentiContrattoTablePage.setContrattoSpesometroForm(this);
		}
		return documentiContrattoTablePage;
	}

	/**
	 * @return DocumentiContrattoToggleCommand
	 */
	private DocumentiContrattoToggleCommand getDocumentiContrattoToggleCommand() {
		if (documentiContrattoToggleCommand == null) {
			documentiContrattoToggleCommand = new DocumentiContrattoToggleCommand();
		}
		return documentiContrattoToggleCommand;
	}

	/**
	 * Crea e restituisce il SearchTextBinding per l' entita aggiungendo il pulsante per la richiesta della situazione
	 * rate.
	 * 
	 * @param bf
	 *            il binding factory
	 * @return Binding
	 */
	private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita",
						"entitaPotenzialiPerRicerca", "entitaAbilitateInRicerca" }, new String[] {
						EntitaByTipoSearchObject.TIPOENTITA_KEY, EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI,
						EntitaByTipoSearchObject.FILTRO_ENTITA_ABILITATO });
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(6);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(25);
		return bindingEntita;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);
		ContrattoSpesometro contratto = (ContrattoSpesometro) formObject;
		ParametriRicercaDocumento parametriRicercaDocumento = new ParametriRicercaDocumento();
		parametriRicercaDocumento.setEntita(contratto.getEntita());
		getDocumentiContrattoTablePage().setFormObject(parametriRicercaDocumento);
	}

	/**
	 * Aggiorna lo stato visibility dei componenti per i documenti collegati.<br>
	 * Se il contratto è nuovo non posso collegare documenti quindi nascondo i componenti.
	 * 
	 * @param contratto
	 *            il contratto per valutare se visualizzare o nascondere i componenti
	 */
	public void updateDocumentiContrattoControlVisibility(ContrattoSpesometro contratto) {
		boolean isNew = contratto == null;
		getDocumentiContrattoTablePage().getControl().setVisible(
				!isNew && getDocumentiContrattoToggleCommand().isSelected());
		getDocumentiContrattoToggleCommand().setVisible(!isNew);

	}

	/**
	 * Aggiorna la lista di documenti collegati al contratto corrente nella tabella (documentiTableWidget) o una lista
	 * vuota se il contratto è vuoto o null.
	 * 
	 * @param contratto
	 *            il contratto di cui caricare i documenti collegati
	 */
	public void updateTableRowsDocumenti(ContrattoSpesometro contratto) {
		List<Documento> documenti = new ArrayList<Documento>();
		if (contratto != null && contratto.getId() != null) {
			if (contratto.getId() != null) {
				documenti = ContrattoSpesometroForm.this.anagraficaTabelleBD
						.caricaDocumentiContratto(contratto.getId());
			}
		}
		getDocumentiContrattoTablePage().setRows(documenti);
	}
}
