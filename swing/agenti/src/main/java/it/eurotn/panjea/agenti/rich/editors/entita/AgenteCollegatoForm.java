package it.eurotn.panjea.agenti.rich.editors.entita;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class AgenteCollegatoForm extends AbstractFocussableForm {

	public class AgentiCollegatiChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			agentiValueHolder.refresh();
		}

	}

	private RefreshableValueHolder agentiValueHolder;
	private FormModel formModelParent;

	private List<Entita> agenti;

	/**
	 * Costruttore.
	 *
	 * @param anagraficaBD
	 *            bd anagrafica
	 * @param agenteForm
	 *            form dell'agente corrente
	 */
	public AgenteCollegatoForm(final IAnagraficaBD anagraficaBD, final PanjeaAbstractForm agenteForm) {
		super(PanjeaFormModelHelper.createFormModel(new AgenteCollegatoPM(), false, "agenteCollegatoForm"));
		agenteForm.getFormModel().getValueModel("agenti").addValueChangeListener(new AgentiCollegatiChangeListener());
		agenteForm.addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				agenti = caricaAgenti(anagraficaBD);
			}
		});
		this.formModelParent = agenteForm.getFormModel();
		this.agenti = caricaAgenti(anagraficaBD);
	}

	private List<Entita> caricaAgenti(IAnagraficaBD anagraficaBD) {
		ParametriRicercaEntita par = new ParametriRicercaEntita();
		par.setTipoEntita(TipoEntita.AGENTE);
		List<Entita> result = new ArrayList<Entita>();
		List<EntitaLite> entitaLiteTrovate = anagraficaBD.ricercaEntita(par);
		for (EntitaLite entitaLite : entitaLiteTrovate) {
			result.add(entitaLite.creaProxyEntita());
		}
		return result;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:100dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		Binding agenteBinding = bf.createBoundComboBox("agente", getAgenti(), "anagrafica.denominazione");
		builder.addBinding(agenteBinding, 1);
		setFocusControl(agenteBinding.getControl());
		return builder.getPanel();
	}

	/**
	 *
	 * @return lista di agenti da poter selezionare
	 */
	private RefreshableValueHolder getAgenti() {
		agentiValueHolder = new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object arg0) {

				List<Entita> agentiSelezionabili = new ArrayList<Entita>(agenti);

				// tolgo gli agenti che ho già assegnato al capo area perchè il set comprende quelli già salvati
				// precedentemente e quelli appena inseriti e quindi non ancora associati
				@SuppressWarnings("unchecked")
				Set<Entita> agentiAssegnati = (Set<Entita>) formModelParent.getValueModel("agenti").getValue();
				agentiSelezionabili.removeAll(agentiAssegnati);

				return agentiSelezionabili;
			}
		});
		agentiValueHolder.refresh();
		return agentiValueHolder;
	}
}