/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.GenerazioneCodiceArticoloData;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class CategoriaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "categoriaForm";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	private AziendaCorrente aziendaCorrente = null;
	private PluginManager pluginManager = null;
	private RefreshableValueHolder attributiValueHolder = null;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param formId
	 *            form id
	 */
	public CategoriaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);
		this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	}

	private RefreshableValueHolder createAttributiValueHolder() {
		return new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object argument) {
				Categoria categoria = ((Categoria) getFormObject());

				List<String> variabili = new ArrayList<String>();

				variabili.add(TipoAttributo.SEPARATORE_CODICE_FORMULA + GenerazioneCodiceArticoloData.NUMERATORE
						+ TipoAttributo.SEPARATORE_CODICE_FORMULA);
				if (categoria.getAttributiCategoria() != null) {
					for (AttributoCategoria attributoCategoria : categoria.getAttributiCategoria()) {
						variabili.add(attributoCategoria.getTipoAttributo().getCodiceFormula());
					}
				}
				return variabili;
			}
		});
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:default:grow",
				"2dlu,default, 2dlu,default, 2dlu,fill:15dlu, 2dlu,fill:15dlu, 2dlu,default, 2dlu,default, 2dlu,fill:15dlu, 2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("codice");
		builder.nextRow();

		JLabel descrizioneLabel = (JLabel) builder.addPropertyAndLabel("descrizione")[0];
		descrizioneLabel.setIcon(RcpSupport.getIcon(aziendaCorrente.getLingua()));
		descrizioneLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		builder.nextRow();

		builder.addLabel("generazioneCodiceArticoloData.mascheraCodiceArticolo");
		attributiValueHolder = createAttributiValueHolder();
		attributiValueHolder.refresh();
		builder.addBinding(bf.createBoundCodeEditor("generazioneCodiceArticoloData.mascheraCodiceArticolo",
				attributiValueHolder, true, false, false), 3);
		builder.nextRow();

		builder.addLabel("generazioneCodiceArticoloData.mascheraDescrizioneArticolo");
		builder.addBinding(bf.createBoundCodeEditor("generazioneCodiceArticoloData.mascheraDescrizioneArticolo",
				attributiValueHolder, true, false, false), 3);
		builder.nextRow();

		builder.addLabel("numeratore");
		builder.addBinding(
				bf.createBoundSearchText("generazioneCodiceArticoloData.numeratore", null, Protocollo.class), 3);
		builder.nextRow();

		builder.addPropertyAndLabel("generazioneCodiceArticoloData.numCaratteriNumeratore", 1);
		builder.nextRow();

		List<TipoAttributo> tipiAttributo = magazzinoAnagraficaBD.caricaTipiAttributo();
		List<String> variabili = new ArrayList<String>();
		for (TipoAttributo tipoAttributo : tipiAttributo) {
			variabili.add(tipoAttributo.getCodiceFormula());
		}
		variabili.add("result = ");
		variabili.add(TipoAttributo.SEPARATORE_CODICE_FORMULA + "qta" + TipoAttributo.SEPARATORE_CODICE_FORMULA);
		ValueHolder variabiliValueHolder = new ValueHolder(variabili);

		builder.addLabel("formulaPredefinitaComponente", 1);
		builder.addBinding(
				bf.createBoundCodeEditor("formulaPredefinitaComponente", variabiliValueHolder, true, false, false), 3);
		builder.nextRow();

		if (pluginManager.isPresente(PluginManager.PLUGIN_CAUZIONI)) {
			builder.addPropertyAndLabel("cauzione");
		}
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		Categoria categoria = new Categoria();
		return categoria;
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);
		attributiValueHolder.refresh();
	}
}
