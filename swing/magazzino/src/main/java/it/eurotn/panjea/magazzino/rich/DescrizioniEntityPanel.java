/**
 * 
 */
package it.eurotn.panjea.magazzino.rich;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.IDescrizioneFactory;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.DescrizioneLinguaForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.springframework.binding.form.CommitListener;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.layout.TableLayoutBuilder;

/**
 * Gestisce tutte le descrizioni in lingua di un oggetto IDescrizioneFactory.
 * 
 * @author fattazzo
 */
public class DescrizioniEntityPanel extends JScrollPane implements PropertyChangeListener {

	/**
	 * 
	 * CommitListener per aggiornare i valori presente all'interno della Map del FormModel. Per la lingua aziendale
	 * viene aggiornata la proprieta' all'interno del FormModel
	 * 
	 * @author adriano
	 * @version 1.0, 17/nov/2008
	 * 
	 */
	@SuppressWarnings("unchecked")
	private class DescrizioneLinguaCommitListener implements CommitListener {

		@Override
		public void postCommit(FormModel paramFormModel) {
			// nothing to do
			// IDescrizioneLingua descrizioneLingua = (IDescrizioneLingua) paramFormModel.getFormObject();

			IDescrizioneLingua descrizioneLingua = (IDescrizioneLingua) descrizioniLinguaFormModel.get(
					((IDescrizioneLingua) paramFormModel.getFormObject()).getCodiceLingua()).getFormObject();

			if ((DescrizioniEntityPanel.this.propertyNameLinguaAzienda != null)
					&& (DescrizioniEntityPanel.this.aziendaCorrente != null)
					&& (DescrizioniEntityPanel.this.aziendaCorrente.getLingua().equals(descrizioneLingua
							.getCodiceLingua()))) {
				// aggiorna la descrizione estesa in lingua aziendale
				DescrizioniEntityPanel.this.formModel.getValueModel(
						DescrizioniEntityPanel.this.propertyNameLinguaAzienda).setValue(
						descrizioneLingua.getDescrizione());
				logger.debug("--> Exit preCommit ");
				return;
			}
			Map<String, IDescrizioneLingua> map = (Map<String, IDescrizioneLingua>) DescrizioniEntityPanel.this.formModel
					.getValueModel(DescrizioniEntityPanel.this.propertyName).getValue();
			if (map == null) {
				map = new HashMap<String, IDescrizioneLingua>();
				DescrizioniEntityPanel.this.formModel.getValueModel(DescrizioniEntityPanel.this.propertyName).setValue(
						map);
			}
			if ((descrizioneLingua.getDescrizione() == null) || (descrizioneLingua.getDescrizione().isEmpty())) {
				logger.debug("--> rimuove l'oggetto  dalla map " + descrizioneLingua);
				map.remove(descrizioneLingua.getCodiceLingua());
			} else {
				logger.debug("--> aggiunge l'oggetto nella map " + descrizioneLingua);
				map.put(descrizioneLingua.getCodiceLingua(), descrizioneLingua);
			}
		}

		/**
		 * PreCommit incaricato di rimuovere l'oggetto IDescrizioneLingua del ChildFormModel se l'attributo descrizione
		 * è null.
		 * 
		 * @param paramFormModel
		 *            paramFormModel
		 */
		@Override
		public void preCommit(FormModel paramFormModel) {
			logger.debug("--> Enter preCommit");

			logger.debug("--> Exit preCommit");
		}
	}

	private static final long serialVersionUID = -8292504472502471116L;

	private static Logger logger = Logger.getLogger(DescrizioniEntityPanel.class);
	private final FormModel formModel;
	private final String propertyName;
	private final String propertyNameLinguaAzienda;

	private final IAnagraficaTabelleBD anagraficaTabelleBD;
	protected List<Lingua> lingueAziendali;

	protected List<IDescrizioneLingua> listDescrizioniLingue;

	protected Map<String, HierarchicalFormModel> descrizioniLinguaFormModel;

	protected JPanel rootPanel = new JPanel();

	private final AziendaCorrente aziendaCorrente;

	protected DescrizioneLinguaCommitListener descrizioneLinguaCommitListener = new DescrizioneLinguaCommitListener();

	/**
	 * Indica se il pannello creato non contiene descrizioni in lingua.
	 */
	private boolean empty = true;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formmodel
	 * @param propertyName
	 *            propertyName
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 */
	public DescrizioniEntityPanel(final FormModel formModel, final String propertyName,
			final IAnagraficaTabelleBD anagraficaTabelleBD) {
		this(formModel, propertyName, null, anagraficaTabelleBD, null);
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formmodel
	 * @param propertyName
	 *            propertyName
	 * @param propertyNameLinguaAzienda
	 *            propertyNameLinguaAzienda
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public DescrizioniEntityPanel(final FormModel formModel, final String propertyName,
			final String propertyNameLinguaAzienda, final IAnagraficaTabelleBD anagraficaTabelleBD,
			final AziendaCorrente aziendaCorrente) {
		super();
		this.aziendaCorrente = aziendaCorrente;
		this.formModel = formModel;
		this.propertyName = propertyName;
		this.propertyNameLinguaAzienda = propertyNameLinguaAzienda;
		this.anagraficaTabelleBD = anagraficaTabelleBD;
		this.descrizioniLinguaFormModel = new HashMap<String, HierarchicalFormModel>();
		// recupero tutte le lingue aziendali
		creaListLingueAziendali();

		createControl();

		this.formModel.addPropertyChangeListener(this);
		this.formModel.getValueModel(propertyName).addValueChangeListener(this);

		this.setBorder(null);
	}

	/**
	 * Crea le descrizioni in lingua.
	 */
	@SuppressWarnings("unchecked")
	protected void creaListDescrizioniLingua() {
		this.listDescrizioniLingue = new ArrayList<IDescrizioneLingua>();

		Object lingue = this.formModel.getValueModel(this.propertyName).getValue();
		Set<String> linguePresenti = new HashSet<String>();
		if (lingue != null) {
			// le descrizioni che esistono le aggiungo alla lista
			this.listDescrizioniLingue.addAll(((Map<String, IDescrizioneLingua>) lingue).values());
			linguePresenti.addAll(((Map<String, IDescrizioneLingua>) lingue).keySet());
		}

		// aggiungo alla lista le descrizioni delle lingue mancanti
		this.listDescrizioniLingue.addAll(createDescrizioniLinguaMancanti(lingueAziendali, linguePresenti));

		// ordino la lista delle lingue
		ordinaDescrizioni();

		// verifico se sono presenti le descrizioni in lingua
		this.empty = listDescrizioniLingue.isEmpty();

	}

	/**
	 * Inizializza pa lista delle lingue aziendali.
	 */
	protected void creaListLingueAziendali() {
		logger.debug("--> Enter creaListLingueAziendali");
		// recupera la List di Lingue Aziendali
		lingueAziendali = anagraficaTabelleBD.caricaLingue();
		logger.debug("--> Exit creaListLingueAziendali");
	}

	/**
	 * creazione dei controlli per le {@link IDescrizioneLingua}.
	 */
	protected void createControl() {
		logger.debug("--> Enter createDescrizioniLinguaForm");
		TableLayoutBuilder builder = new TableLayoutBuilder(new JPanel());

		for (IDescrizioneLingua descrizioneLingua : getListDescrizioniLingue()) {
			DescrizioneLinguaForm descrizioneLinguaForm = new DescrizioneLinguaForm(formModel, descrizioneLingua);
			builder.cell(descrizioneLinguaForm.getControl());
			builder.row("4");

			descrizioneLinguaForm.getFormModel().addCommitListener(new DescrizioneLinguaCommitListener());
			descrizioniLinguaFormModel.put(descrizioneLingua.getCodiceLingua(), descrizioneLinguaForm.getFormModel());

			((HierarchicalFormModel) formModel).addChild(descrizioneLinguaForm.getFormModel());
		}
		this.getViewport().add(builder.getPanel());
	}

	/**
	 * Creata tutte le descrizioni lingua mancanti.
	 * 
	 * @param lingueAziendaliCorrenti
	 *            Lista di lingue gestite dall'azienda
	 * @param linguePresenti
	 *            Codici delle lingue già presenti
	 * @return Lista di <code>IDescrizioneLingua</code> mancanti
	 */
	protected List<IDescrizioneLingua> createDescrizioniLinguaMancanti(List<Lingua> lingueAziendaliCorrenti,
			Set<String> linguePresenti) {
		List<IDescrizioneLingua> listDescrizioniMancanti = new ArrayList<IDescrizioneLingua>();
		for (Lingua lingua : lingueAziendaliCorrenti) {
			if (!linguePresenti.contains(lingua.getCodice())) {
				IDescrizioneLingua descrizioneLingua = ((IDescrizioneFactory) this.formModel.getFormObject())
						.createDescrizioneLingua();
				descrizioneLingua.setCodiceLingua(lingua.getCodice());
				listDescrizioniMancanti.add(descrizioneLingua);
			}
		}
		return listDescrizioniMancanti;
	}

	/**
	 * 
	 * @return aziendaCorrente
	 */
	public AziendaCorrente getAziendaCorrente() {
		return aziendaCorrente;
	}

	/**
	 * @return formModel
	 */
	public FormModel getFormModel() {
		return formModel;
	}

	/**
	 * @return listDescrizioniLingue
	 */
	public List<IDescrizioneLingua> getListDescrizioniLingue() {
		if (listDescrizioniLingue == null) {
			creaListDescrizioniLingua();
		}
		return listDescrizioniLingue;
	}

	/**
	 * @return propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return Returns the propertyNameLinguaAzienda.
	 */
	public String getPropertyNameLinguaAzienda() {
		return propertyNameLinguaAzienda;
	}

	/**
	 * @return empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * Ordina la lista delle lingue in ordine alfabetico.
	 */
	private void ordinaDescrizioni() {
		Collections.sort(this.getListDescrizioniLingue(), new Comparator<IDescrizioneLingua>() {

			@Override
			public int compare(IDescrizioneLingua o1, IDescrizioneLingua o2) {

				Locale localeO1 = new Locale(o1.getCodiceLingua());
				Locale localeO2 = new Locale(o1.getCodiceLingua());

				return localeO1.getDisplayLanguage().compareTo(localeO2.getDisplayLanguage());
			}

		});
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter DescrizioniEntityPanel.propertyChange " + evt.getPropertyName() + " "
				+ evt.getNewValue());
		if (evt.getPropertyName().equals(FormModel.ENABLED_PROPERTY)) {
			Boolean formModelEnabled = (Boolean) evt.getNewValue();
			setEnabled(formModelEnabled);
		} else if (FormModel.READONLY_PROPERTY.equals(evt.getPropertyName())) {
			Boolean formModelReadOnly = (Boolean) evt.getNewValue();
			setEnabled(!formModelReadOnly);
		} else if (evt.getPropertyName().equals(ValueModel.VALUE_PROPERTY)) {
			updateControl();
		}
		logger.debug("--> Exit DescrizioniEntityPanel.propertyChange");
	}

	/**
	 * 
	 */
	protected void updateControl() {
		logger.debug("--> Enter updateControl");
		creaListDescrizioniLingua();
		@SuppressWarnings("unchecked")
		Map<String, IDescrizioneLingua> lingue = (Map<String, IDescrizioneLingua>) this.formModel.getValueModel(
				this.propertyName).getValue();
		for (IDescrizioneLingua descrizioneLingua : getListDescrizioniLingue()) {
			if (descrizioniLinguaFormModel.containsKey(descrizioneLingua.getCodiceLingua())) {
				FormModel formModelLingua = descrizioniLinguaFormModel.get(descrizioneLingua.getCodiceLingua());
				formModelLingua.setFormObject(descrizioneLingua);
				if ((aziendaCorrente != null)
						&& (aziendaCorrente.getLingua().equals(descrizioneLingua.getCodiceLingua()))) {
					this.formModel.getValueModel("descrizioneEstesaLinguaAziendale").setValue(
							descrizioneLingua.getDescrizione());
				} else if ((descrizioneLingua.getDescrizione() == null)
						|| (descrizioneLingua.getDescrizione().isEmpty())) {
					if (lingue != null) {
						lingue.remove(descrizioneLingua.getCodiceLingua());
					}
				} else {
					lingue.put(descrizioneLingua.getCodiceLingua(), descrizioneLingua);
				}
			}
		}
		logger.debug("--> Exit updateControl");
	}
}
