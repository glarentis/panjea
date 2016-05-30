/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.documento;

import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.ColorComboBoxBinder;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 *
 * @author adriano
 * @version 1.0, 17/mag/07
 */
public class TipoDocumentoForm extends PanjeaAbstractForm {

	private class ClasseTipoDocumentoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			IClasseTipoDocumento classeTipoDocumento = (IClasseTipoDocumento) evt.getNewValue();

			// se la classeTipoDocumento prevede l'area iva allora devo
			// presentare i controlli
			// per poterla abilitare
			if (classeTipoDocumento != null) {

				// verifica se l'area iva e' disponibile e aggiorna di
				// conseguenza la visibilita' dei controlli
				// collegati
				boolean areaIvaDisponibile = classeTipoDocumento.getTipiAree().contains(
						IClasseTipoDocumento.TIPO_AREA_IVA);
				for (JComponent jComponent : componentsRigheIva) {
					jComponent.setVisible(areaIvaDisponibile);
				}

				// verifica se la classeTipoDocumento prevede la possibilita' di
				// essere nota di credito e aggiorna di
				// conseguenza la visibilita' dei controlli collegati
				boolean notaCreditoDisponibile = classeTipoDocumento.getTipiCaratteristiche().contains(
						IClasseTipoDocumento.TIPO_CARATTERISTICA_NOTA_CREDITO);
				for (JComponent jComponent : componentsNotaCredito) {
					jComponent.setVisible(notaCreditoDisponibile);
				}
				for (JComponent jComponent : componentsNotaAddebito) {
					jComponent.setVisible(notaCreditoDisponibile);
				}

				// AreaIntra
				boolean intraDisponibile = classeTipoDocumento.getTipiCaratteristiche().contains(
						IClasseTipoDocumento.TIPO_CARATTERISTICA_INTRA);
				if (componentsIntra != null) {
					for (JComponent component : componentsIntra) {
						component.setVisible(intraDisponibile);
					}
				}

				// Lotti
				boolean lottiDisponibile = classeTipoDocumento.getTipiCaratteristiche().contains(
						IClasseTipoDocumento.TIPO_CARATTERISTICA_LOTTI);
				if (componentsLotti != null) {
					for (JComponent component : componentsLotti) {
						component.setVisible(lottiDisponibile);
					}
				}

			}
		}

	}

	private class NotaCreditoAddebitoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				return;
			}

			// rifaccio il set perchè non posso avere un tipo documento storno e addebito. Sull'oggetto di dominio nei
			// metodi setter quando ne viene abilitato uno viene disabilitato l'altro. Per questo il value model non lo
			// vede e devo forzare l'aggiornamento con un setValue.
			getValueModel("notaCreditoEnable").setValueSilently(getValueModel("notaCreditoEnable").getValue(), this);
			getValueModel("notaAddebitoEnable").setValueSilently(getValueModel("notaAddebitoEnable").getValue(), this);
		}

	}

	public static final String COMBO_BOX_VALUE_MANUALE = "protocollo.manuale";
	private static final String FORM_ID = "tipoDocumentoForm";

	private IAnagraficaBD anagraficaBD = null;
	private final List<String> listProtocolliDescrizioneEstesa = null;

	private JComponent[] componentsRigheIva = null;
	private JComponent[] componentsNotaCredito = null;
	private JComponent[] componentsNotaAddebito = null;
	private JComponent[] componentsIntra = null;
	private JComponent[] componentsLotti = null;

	private NotaCreditoAddebitoPropertyChange notaCreditoAddebitoPropertyChange;

	/**
	 * Costruttore.
	 *
	 * @param tipoDocumento
	 *            tipo documento da gestire
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public TipoDocumentoForm(final TipoDocumento tipoDocumento, final IAnagraficaBD anagraficaBD) {
		super(PanjeaFormModelHelper.createFormModel(tipoDocumento, false, FORM_ID), FORM_ID);
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,80dlu, 20dlu, right:pref,4dlu,fill:default, 4dlu, fill:default:grow,4dlu,left:default, fill:default:grow",
				"3dlu,default,3dlu,default,3dlu,default,3dlu,fill:default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("abilitato", 1, 2, 3);
		builder.nextRow();

		builder.addLabel("classeTipoDocumentoInstance", 1, 4);
		builder.addBinding(
				bf.createI18NBoundComboBox("classeTipoDocumentoInstance",
						new ValueHolder(anagraficaBD.caricaClassiTipoDocumento()), "class.name"), 3, 4, 3, 1);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("codice", 1)[1]).setColumns(4);
		((JTextField) builder.addPropertyAndLabel("descrizione", 5)[1]).setColumns(30);
		builder.nextRow();

		builder.addLabel("registroProtocollo", 1);
		builder.addBinding(bf.createBoundSearchText("registroProtocollo", null, Protocollo.class), 3, 8);

		builder.addLabel("patternNumeroDocumento", 5, 8);
		String[] variabiliPatternCodiceDocumento = anagraficaBD.getVariabiliPatternCodiceDocumento();
		List<String> variabiliList = new ArrayList<String>();
		for (String variabile : variabiliPatternCodiceDocumento) {
			variabiliList.add(VariabiliCodiceManager.VAR_SEPARATOR + variabile + VariabiliCodiceManager.VAR_SEPARATOR);
		}
		ValueHolder valueHolder = new ValueHolder(variabiliList);
		builder.addBinding(bf.createBoundCodeEditor("patternNumeroDocumento", valueHolder, true, true, false), 7, 8);
		builder.nextRow();

		builder.addPropertyAndLabel("tipoEntita", 1, 10, 3);
		builder.nextRow();

		componentsRigheIva = builder.addPropertyAndLabel("righeIvaEnable", 1, 12, 3);
		builder.nextRow();

		componentsNotaCredito = builder.addPropertyAndLabel("notaCreditoEnable", 1, 14);
		builder.nextRow();

		componentsNotaAddebito = builder.addPropertyAndLabel("notaAddebitoEnable", 5, 14);
		builder.nextRow();

		builder.addPropertyAndLabel("richiediDocumentoCollegato", 1, 16, 3);
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente("panjeaIntra")) {
			componentsIntra = builder.addPropertyAndLabel("gestioneIntra", 1, 18, 3);
			builder.nextRow();
		}

		builder.addPropertyAndLabel("gestioneContratto", 1, 20, 3);
		builder.nextRow();

		HashMap<String, Object> context = new HashMap<String, Object>();
		builder.addLabel("colore", 1, 22);
		builder.addBinding(new ColorComboBoxBinder().bind(getFormModel(), "colore", context), 3, 22);
		builder.nextRow();

		if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
			componentsLotti = builder.addPropertyAndLabel("gestioneLotti", 1, 24, 3);
			builder.nextRow();
		}

		this.addFormValueChangeListener("classeTipoDocumentoInstance", new ClasseTipoDocumentoPropertyChange());

		this.notaCreditoAddebitoPropertyChange = new NotaCreditoAddebitoPropertyChange();
		this.getValueModel("notaAddebitoEnable").addValueChangeListener(notaCreditoAddebitoPropertyChange);
		this.getValueModel("notaCreditoEnable").addValueChangeListener(notaCreditoAddebitoPropertyChange);

		return builder.getPanel();
	}

	@Override
	public void dispose() {

		this.getValueModel("notaAddebitoEnable").removeValueChangeListener(notaCreditoAddebitoPropertyChange);
		this.getValueModel("notaCreditoEnable").removeValueChangeListener(notaCreditoAddebitoPropertyChange);
		notaCreditoAddebitoPropertyChange = null;

		super.dispose();
	}

	/**
	 * Sovrascrivo il setFormObject per risolvere il problema sulla comboBox dei protocolli.<br>
	 * Viene mostrato codice + descrizione e questo genera una differenza di value tra l'oggetto che viene settato nel
	 * form (String codice protocollo) e il valore selezionato della combo (String codice+descrizione) che genera un
	 * valueChanged che mostra la popup della combobox sul salvataggio.<br>
	 * Aggiorno il formObject impostando come protocollo il protocollo che uso in visualizzazione prima dell'esecuzione
	 * della setFormObject per evitare di lanciare il valueChanged.
	 *
	 * @param formObject
	 *            form object
	 */
	@Override
	public void setFormObject(Object formObject) {
		if (listProtocolliDescrizioneEstesa != null) {

			String codiceProtocollo = ((TipoDocumento) formObject).getRegistroProtocollo();
			if (codiceProtocollo != null) {
				for (String protEsteso : listProtocolliDescrizioneEstesa) {
					if (protEsteso.startsWith(codiceProtocollo)) {
						((TipoDocumento) formObject).setRegistroProtocollo(protEsteso);
						break;
					}
				}
			}

		}
		super.setFormObject(formObject);
	}

}
