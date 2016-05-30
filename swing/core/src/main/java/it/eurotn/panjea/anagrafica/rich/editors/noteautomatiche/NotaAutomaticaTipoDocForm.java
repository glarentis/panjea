package it.eurotn.panjea.anagrafica.rich.editors.noteautomatiche;

import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.documenti.TipoDocumentoSearchObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.list.ListInsertableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;
import it.eurotn.rich.components.intellihint.IntelliHintDecorator;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JEditorPane;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class NotaAutomaticaTipoDocForm extends PanjeaAbstractForm {

	private class ClasseTipoDocListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				return;
			}
			getFormModel().getValueModel("tipoDocumento").setValue(null);
		}
	}

	private class EntitaListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getFormModel().getFieldMetadata("sedeEntita").setReadOnly(sedeEntitaPredefinita != null);
			getFormModel().getFieldMetadata("entita").setReadOnly(entitaPredefinita != null);

			if (!getFormModel().isReadOnly() && sedeEntitaPredefinita == null) {
				getFormModel().getValueModel("sedeEntita").setValue(null);
			}

		}
	}

	private class TipoDocumentoListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			getFormModel().getFieldMetadata("entita").setReadOnly(entitaPredefinita != null);

			if (!getFormModel().isReadOnly() && entitaPredefinita == null) {
				getFormModel().getValueModel("entita").setValue(null);
			}
		}
	}

	public class TutteClassiTipiDocumento extends AbstractClasseTipoDocumento {
		@Override
		public List<String> getTipiAree() {
			return null;
		}

		@Override
		public List<String> getTipiCaratteristiche() {
			return null;
		}
	}

	public static final String FORM_ID = "NotaAutomaticaTipoDocForm";

	private IAnagraficaTabelleBD anagraficaTabelleBD;
	private PluginManager pluginManager;
	private IAnagraficaBD anagraficaBD;
	private HTMLEditorPane noteEditorPane;

	private EntitaLite entitaPredefinita;
	private SedeEntita sedeEntitaPredefinita;
	private ClasseTipoDocListener classeTipoDocListener;
	private TipoDocumentoListener tipoDocumentoListener;
	private EntitaListener entitaListener;

	private JEditorPane wysEditor = null;

	/**
	 * Costruttore.
	 */
	public NotaAutomaticaTipoDocForm() {
		super(PanjeaFormModelHelper.createFormModel(new NotaAutomatica(), false, FORM_ID), FORM_ID);
		this.anagraficaTabelleBD = RcpSupport.getBean("anagraficaTabelleBD");
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

		// aggiungo la proprietà finta per poter filtrare i tipi documento delle sole entità che possono avere le note
		Set<TipoEntita> tipiEntita = new TreeSet<TipoEntita>();
		tipiEntita.add(TipoEntita.AGENTE);
		tipiEntita.add(TipoEntita.CLIENTE);
		tipiEntita.add(TipoEntita.FORNITORE);
		tipiEntita.add(TipoEntita.VETTORE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaValueModel), Set.class, true, null);
		getFormModel().add("tipiEntitaTipiDoc", tipiEntitaValueModel, tipiEntitaData);
	}

	/**
	 * @return TableEditableBinding
	 */
	private TableEditableBinding<ArticoloLite> createArticoliNotaTableBinding() {

		ArticoliNotaAutomaticaTableModel articoliNotaAutomaticaTableModel = new ArticoliNotaAutomaticaTableModel(
				(NotaAutomatica) getFormObject());
		ListInsertableBinding<ArticoloLite> articoliNotaAutomaticaBinding = new ListInsertableBinding<ArticoloLite>(
				getFormModel(), "articoli", Set.class, articoliNotaAutomaticaTableModel, new String[] { "codice",
						"descrizione" });
		return articoliNotaAutomaticaBinding;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:pref,5dlu,left:pref,5dlu,left:pref,5dlu,left:pref,5dlu,left:pref,5dlu,left:pref,left:default:grow,5dlu,default",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered()
		builder.setLabelAttributes("r, c");
		builder.setComponentAttributes("f, f");

		// ROW 1: classe e tipo documento
		builder.addLabel("classe", 1, 2);
		ComboBoxBinding bindingClasseTipoDoc = (ComboBoxBinding) bf.createI18NBoundComboBox(
				"classeTipoDocumentoInstance", new ValueHolder(anagraficaBD.caricaClassiTipoDocumento()), "class.name");
		bindingClasseTipoDoc.setEmptySelectionValue(new TutteClassiTipiDocumento());
		builder.addBinding(bindingClasseTipoDoc, 3, 2);

		builder.addLabel("tipoDocumento", 5, 2);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoDocumento", new String[] { "codice", "descrizione" },
				new String[] { "classeTipoDocumento", "tipiEntitaTipiDoc" }, new String[] {
						TipoDocumentoSearchObject.PARAM_CLASSE_TIPO_DOC, TipoDocumentoSearchObject.PARAM_TIPI_ENTITA });
		SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 7, 2, 6, 1);
		tipoDocumentoSearchPanel.getTextFields().get("codice").setColumns(4);
		tipoDocumentoSearchPanel.getTextFields().get("descrizione").setColumns(18);

		// ROW 2: entita e sede
		builder.addLabel("entita", 1, 4);
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoDocumento.tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		builder.addBinding(bindingEntita, 3, 4);
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(4);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(18);

		builder.addLabel("sedeEntita", 5, 4);
		Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", null, new String[] { "entita" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 7, 4, 6, 1);
		searchPanelSede.getTextFields().get(null).setColumns(22);

		// ROW 3: data inizio, fine, ripeti
		builder.setComponentAttributes("l, c");
		builder.addPropertyAndLabel("dataInizio", 1, 6);
		builder.addPropertyAndLabel("dataFine", 5, 6);

		builder.addPropertyAndLabel("ripetiAnnualmente", 9, 6);

		// ROW 4: nota
		builder.setLabelAttributes("r, t");
		builder.setComponentAttributes("f, f");
		builder.addLabel("nota", 1, 8);
		Binding noteBinding = bf.createBoundHTMLEditor("nota");
		noteEditorPane = (HTMLEditorPane) noteBinding.getControl().getComponent(0);
		wysEditor = noteEditorPane.getWysEditor();

		IntelliHintDecorator intelliHintDecoratorCodice = new IntelliHintDecorator();
		intelliHintDecoratorCodice.attachHintelliHint(wysEditor, new Closure() {

			@Override
			public Object call(Object argument) {
				List<String> list = new ArrayList<String>();

				List<NotaAnagrafica> caricaNoteAnagrafica = anagraficaTabelleBD.caricaNoteAnagrafica();
				for (NotaAnagrafica notaAnagrafica : caricaNoteAnagrafica) {
					list.add(notaAnagrafica.getCodice());
				}
				return list;
			}
		}, "$");

		builder.addBinding(noteBinding, 3, 8, 10, 1);

		if (pluginManager.isPresente(PluginManager.PLUGIN_MAGAZZINO)) {
			TableEditableBinding<ArticoloLite> articoliNotaTableBinding = createArticoliNotaTableBinding();
			JComponent articoliNotaTable = articoliNotaTableBinding.getControl();
			articoliNotaTable.setPreferredSize(new Dimension(300, articoliNotaTable.getHeight()));
			// articoli collegati alla nota
			builder.addComponent(articoliNotaTable, 14, 1, 1, 8);
		}
		installListener();

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		NotaAutomatica notaAutomatica = new NotaAutomatica();
		if (entitaPredefinita != null) {
			notaAutomatica.setEntita(entitaPredefinita);
		}
		if (sedeEntitaPredefinita != null) {
			notaAutomatica.setSedeEntita(sedeEntitaPredefinita);
		}
		return notaAutomatica;
	}

	@Override
	public void dispose() {
		getFormModel().getValueModel("classeTipoDocumentoInstance").removeValueChangeListener(classeTipoDocListener);
		getFormModel().getValueModel("tipoDocumento").removeValueChangeListener(tipoDocumentoListener);
		getFormModel().getValueModel("entita").removeValueChangeListener(entitaListener);
		super.dispose();
	}

	/**
	 * Install all listener.
	 */
	private void installListener() {
		classeTipoDocListener = new ClasseTipoDocListener();
		getFormModel().getValueModel("classeTipoDocumentoInstance").addValueChangeListener(classeTipoDocListener);

		tipoDocumentoListener = new TipoDocumentoListener();
		getFormModel().getValueModel("tipoDocumento").addValueChangeListener(tipoDocumentoListener);

		entitaListener = new EntitaListener();
		getFormModel().getValueModel("entita").addValueChangeListener(entitaListener);
	}

	/**
	 * @param entitaPredefinita
	 *            the entitaPredefinita to set
	 */
	public void setEntitaPredefinita(EntitaLite entitaPredefinita) {
		this.entitaPredefinita = entitaPredefinita;
		this.sedeEntitaPredefinita = null;
	}

	/**
	 * @param sedeEntitaPredefinita
	 *            the sedeEntitaPredefinita to set
	 */
	public void setSedeEntitaPredefinita(SedeEntita sedeEntitaPredefinita) {
		this.sedeEntitaPredefinita = sedeEntitaPredefinita;
		this.entitaPredefinita = sedeEntitaPredefinita.getEntita().getEntitaLite();
	}
}
