package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.anagrafica;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class InserimentoTipoAreaMagazzinoForm extends AbstractFocussableForm {

	public class TipiAreaMagazzinoCollegateChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			tamValueHolder.refresh();
		}

	}

	private RefreshableValueHolder tamValueHolder;
	private FormModel formModelParent;

	private List<TipoAreaMagazzino> tipiAreaMagazzino;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param tipoEsportazioneForm
	 *            form del tipo esportazione
	 */
	public InserimentoTipoAreaMagazzinoForm(final PanjeaAbstractForm tipoEsportazioneForm) {
		super(PanjeaFormModelHelper.createFormModel(new TipoAreaMagazzinoPM(), false, "tipoEsportazioneForm"));
		tipoEsportazioneForm.getFormModel().getValueModel("tipiAreeMagazzino")
				.addValueChangeListener(new TipiAreaMagazzinoCollegateChangeListener());

		this.formModelParent = tipoEsportazioneForm.getFormModel();
		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		this.tipiAreaMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino("tipoDocumento.codice", null, false);

	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:150dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		Binding tamBinding = bf.createBoundComboBox("tipoAreaMagazzino", getTipiAreaMagazzino(),
				"tipoDocumento.descrizione");
		builder.addBinding(tamBinding, 1);
		setFocusControl(tamBinding.getControl());
		return builder.getPanel();
	}

	/**
	 * 
	 * @return lista di tipiAreeMagazzino da poter selezionare
	 */
	private RefreshableValueHolder getTipiAreaMagazzino() {
		tamValueHolder = new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object arg0) {

				List<TipoAreaMagazzino> tamSelezionabili = new ArrayList<TipoAreaMagazzino>(tipiAreaMagazzino);

				// tolgo gli agenti che ho già assegnato al capo area perchè il set comprende quelli già salvati
				// precedentemente e quelli appena inseriti e quindi non ancora associati
				@SuppressWarnings("unchecked")
				List<TipoAreaMagazzino> tamAssegnati = (List<TipoAreaMagazzino>) formModelParent.getValueModel(
						"tipiAreeMagazzino").getValue();
				if (tamAssegnati != null) {
					tamSelezionabili.removeAll(tamAssegnati);
				}

				return tamSelezionabili;
			}
		});
		tamValueHolder.refresh();
		return tamValueHolder;
	}
}