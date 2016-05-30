package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.IdentificativoLocalita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.binding.DatiGeograficiBindingForm;
import it.eurotn.rich.binding.datigeografici.Lvl1PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.Lvl2PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.Lvl3PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.Lvl4PropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.NazionePropertyChangeListener;
import it.eurotn.rich.binding.datigeografici.SuddivisioniAmministrativeControlController;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.springframework.binding.form.FormModel;

public abstract class IdentificativoLocalitaForm extends PanjeaAbstractForm {

	/**
	 * Al cambio di una proprietà di località, aggiorna l'oggetto dati Geografici, usato come supporto per il sub-form
	 * di CapForm e LocalitaForm.<br>
	 * L'oggetto dati geografici serve per filtrare i cap o le località delle searchText con i dati geografici correnti.
	 * 
	 * @see it.eurotn.panjea.anagrafica.rich.editors.datigeografici.LocalitaInserimentoForm
	 * @see it.eurotn.panjea.anagrafica.rich.editors.datigeografici.CapInserimentoForm
	 * @author leonardo
	 */
	private class DatiGeograficiLocalitaChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// nel caso utilizzo la page per il singolo inserimento dalla searchText e non nell'editor dei dati
			// geografici, preparo un nuovo oggetto
			if (datiGeografici == null) {
				datiGeografici = new DatiGeografici();
			}
			Object obj = evt.getNewValue();
			if (obj instanceof LivelloAmministrativo1) {
				datiGeografici.setLivelloAmministrativo1((LivelloAmministrativo1) obj);
				datiGeografici
						.setLivelloAmministrativo2((LivelloAmministrativo2) getValue(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH));
				datiGeografici
						.setLivelloAmministrativo3((LivelloAmministrativo3) getValue(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH));
				datiGeografici
						.setLivelloAmministrativo4((LivelloAmministrativo4) getValue(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH));
			} else if (obj instanceof LivelloAmministrativo2) {
				datiGeografici
						.setLivelloAmministrativo1((LivelloAmministrativo1) getValue(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH));
				datiGeografici.setLivelloAmministrativo2((LivelloAmministrativo2) obj);
				datiGeografici
						.setLivelloAmministrativo3((LivelloAmministrativo3) getValue(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH));
				datiGeografici
						.setLivelloAmministrativo4((LivelloAmministrativo4) getValue(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH));
			} else if (obj instanceof LivelloAmministrativo3) {
				datiGeografici
						.setLivelloAmministrativo1((LivelloAmministrativo1) getValue(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH));
				datiGeografici
						.setLivelloAmministrativo2((LivelloAmministrativo2) getValue(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH));
				datiGeografici.setLivelloAmministrativo3((LivelloAmministrativo3) obj);
				datiGeografici
						.setLivelloAmministrativo4((LivelloAmministrativo4) getValue(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH));
			} else if (obj instanceof LivelloAmministrativo4) {
				datiGeografici
						.setLivelloAmministrativo1((LivelloAmministrativo1) getValue(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH));
				datiGeografici
						.setLivelloAmministrativo2((LivelloAmministrativo2) getValue(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH));
				datiGeografici
						.setLivelloAmministrativo3((LivelloAmministrativo3) getValue(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH));
				datiGeografici.setLivelloAmministrativo4((LivelloAmministrativo4) obj);
			}
			setDatiGeografici(datiGeografici);
		}

	}

	protected class ObjectChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Nazione nazione = (Nazione) getValueModel(DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH).getValue();
			suddivisioniAmministrativeControlController.updateLivelliVisibility(nazione);
		}
	}

	/**
	 * Property change che aspetta un booleano per attivare o disattivare i listeners associati al form model dei dati
	 * geografici.
	 * 
	 * @author leonardo
	 */
	protected class ReadOnlyPropertyChange implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Boolean b = (Boolean) evt.getNewValue();
			if (b) {
				deactivateChangeListeners();
			} else {
				activateChangeListeners();
			}
		}
	}

	protected DatiGeografici datiGeografici = null;
	private IDatiGeograficiBD datiGeograficiBD;

	private NazionePropertyChangeListener nazionePropertyChangeListener = null;
	private Lvl1PropertyChangeListener lvl1PropertyChangeListener = null;
	private Lvl2PropertyChangeListener lvl2PropertyChangeListener = null;
	private Lvl3PropertyChangeListener lvl3PropertyChangeListener = null;
	private Lvl4PropertyChangeListener lvl4PropertyChangeListener = null;
	private DatiGeograficiLocalitaChangeListener datiGeograficiChangeListener = null;
	protected SuddivisioniAmministrativeControlController suddivisioniAmministrativeControlController;

	/**
	 * Costruttore.
	 * 
	 * @param identificativoLocalita
	 *            identificativoLocalita
	 * @param formId
	 *            formId
	 * @param datiGeograficiBD
	 *            datiGeograficiBD
	 */
	public IdentificativoLocalitaForm(final IdentificativoLocalita identificativoLocalita, final String formId,
			final IDatiGeograficiBD datiGeograficiBD) {
		super(PanjeaFormModelHelper.createFormModel(identificativoLocalita, false, formId));
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new ReadOnlyPropertyChange());
		this.datiGeograficiBD = datiGeograficiBD;
	}

	/**
	 * Aggiunge al form model i listeners sulle proprietà del dato geografico in modo da collegare i dati tra loro.
	 */
	public void activateChangeListeners() {
		getValueModel(DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH).addValueChangeListener(
				getNazionePropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH).addValueChangeListener(
				getLvl1PropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH).addValueChangeListener(
				getLvl2PropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH).addValueChangeListener(
				getLvl3PropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH).addValueChangeListener(
				getLvl4PropertyChangeListener());

		// aggiorno datiGeografici al cambio di ogni livello Amministrativo altrimenti i filtri per limitare la ricerca
		// di cap e località non vengono aggiornati
		getValueModel(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH).addValueChangeListener(
				getDatiGeograficiChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH).addValueChangeListener(
				getDatiGeograficiChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH).addValueChangeListener(
				getDatiGeograficiChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH).addValueChangeListener(
				getDatiGeograficiChangeListener());
	}

	/**
	 * Rimuove dal form model i listeners sulle proprietà del dato geografico in modo da scollegare i dati tra loro.
	 */
	public void deactivateChangeListeners() {
		getValueModel(DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH).removeValueChangeListener(
				getNazionePropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH).removeValueChangeListener(
				getLvl1PropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH).removeValueChangeListener(
				getLvl2PropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH).removeValueChangeListener(
				getLvl3PropertyChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH).removeValueChangeListener(
				getLvl4PropertyChangeListener());

		getValueModel(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH).removeValueChangeListener(
				getDatiGeograficiChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH).removeValueChangeListener(
				getDatiGeograficiChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH).removeValueChangeListener(
				getDatiGeograficiChangeListener());
		getValueModel(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH).removeValueChangeListener(
				getDatiGeograficiChangeListener());
	}

	/**
	 * @return the datiGeografici
	 */
	public DatiGeografici getDatiGeografici() {
		return datiGeografici;
	}

	/**
	 * @return the datiGeograficiBD
	 */
	public IDatiGeograficiBD getDatiGeograficiBD() {
		return datiGeograficiBD;
	}

	/**
	 * @return DatiGeograficiChangeListener
	 */
	private PropertyChangeListener getDatiGeograficiChangeListener() {
		if (datiGeograficiChangeListener == null) {
			datiGeograficiChangeListener = new DatiGeograficiLocalitaChangeListener();
		}
		return datiGeograficiChangeListener;
	}

	/**
	 * @return Lvl1PropertyChangeListener
	 */
	private Lvl1PropertyChangeListener getLvl1PropertyChangeListener() {
		if (lvl1PropertyChangeListener == null) {
			lvl1PropertyChangeListener = new Lvl1PropertyChangeListener(null, getFormModel());
		}
		return lvl1PropertyChangeListener;
	}

	/**
	 * @return Lvl2PropertyChangeListener
	 */
	private Lvl2PropertyChangeListener getLvl2PropertyChangeListener() {
		if (lvl2PropertyChangeListener == null) {
			lvl2PropertyChangeListener = new Lvl2PropertyChangeListener(null, getFormModel());
		}
		return lvl2PropertyChangeListener;
	}

	/**
	 * @return Lvl3PropertyChangeListener
	 */
	private Lvl3PropertyChangeListener getLvl3PropertyChangeListener() {
		if (lvl3PropertyChangeListener == null) {
			lvl3PropertyChangeListener = new Lvl3PropertyChangeListener(null, getFormModel());
		}
		return lvl3PropertyChangeListener;
	}

	/**
	 * @return Lvl1PropertyChangeListener
	 */
	private Lvl4PropertyChangeListener getLvl4PropertyChangeListener() {
		if (lvl4PropertyChangeListener == null) {
			lvl4PropertyChangeListener = new Lvl4PropertyChangeListener(null, getFormModel());
		}
		return lvl4PropertyChangeListener;
	}

	/**
	 * @return NazionePropertyChangeListener
	 */
	private NazionePropertyChangeListener getNazionePropertyChangeListener() {
		if (nazionePropertyChangeListener == null) {
			nazionePropertyChangeListener = new NazionePropertyChangeListener(null, getFormModel());
		}
		return nazionePropertyChangeListener;
	}

	/**
	 * Inizializza i property change e i loro reciproci riferimenti.
	 */
	protected void initPropertyChangeReferences() {
		Map<String, PropertyChangeListener> listeners = new HashMap<String, PropertyChangeListener>();
		listeners.put(DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH, getNazionePropertyChangeListener());
		listeners.put(DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH, getLvl1PropertyChangeListener());
		listeners.put(DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH, getLvl2PropertyChangeListener());
		listeners.put(DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH, getLvl3PropertyChangeListener());
		listeners.put(DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH, getLvl4PropertyChangeListener());

		getNazionePropertyChangeListener().setListeners(listeners);
		getLvl1PropertyChangeListener().setListeners(listeners);
		getLvl2PropertyChangeListener().setListeners(listeners);
		getLvl3PropertyChangeListener().setListeners(listeners);
		getLvl4PropertyChangeListener().setListeners(listeners);

		getNazionePropertyChangeListener().setSuddivisioniAmministrativeControlController(
				suddivisioniAmministrativeControlController);
		getLvl1PropertyChangeListener().setSuddivisioniAmministrativeControlController(
				suddivisioniAmministrativeControlController);
		getLvl2PropertyChangeListener().setSuddivisioniAmministrativeControlController(
				suddivisioniAmministrativeControlController);
		getLvl3PropertyChangeListener().setSuddivisioniAmministrativeControlController(
				suddivisioniAmministrativeControlController);
		getLvl4PropertyChangeListener().setSuddivisioniAmministrativeControlController(
				suddivisioniAmministrativeControlController);
	}

	/**
	 * @param datiGeografici
	 *            datiGeografici to set
	 */
	public abstract void setDatiGeografici(DatiGeografici datiGeografici);

	/**
	 * @param datiGeograficiBD
	 *            the datiGeograficiBD to set
	 */
	public void setDatiGeograficiBD(IDatiGeograficiBD datiGeograficiBD) {
		this.datiGeograficiBD = datiGeograficiBD;
	}

}
