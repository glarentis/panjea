/**
 * 
 */
package it.eurotn.panjea.magazzino.rich;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.IDescrizioneEstesaFactory;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.DescrizioneLinguaEstesaForm;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class DescrizioniEstesaEntityPanel extends DescrizioniEntityPanel {

	private static final long serialVersionUID = 7168605849151128370L;
	private static Logger logger = Logger.getLogger(DescrizioniEstesaEntityPanel.class);
	private Map<String, DescrizioneLinguaEstesaForm> descrizioniLinguaEstesaForm;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formModel
	 * @param propertyName
	 *            propertyName
	 * @param propertyNameLinguaAzienda
	 *            propertyNameLinguaAzienda
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public DescrizioniEstesaEntityPanel(final FormModel formModel, final String propertyName,
			final String propertyNameLinguaAzienda, final IAnagraficaTabelleBD anagraficaTabelleBD,
			final AziendaCorrente aziendaCorrente) {
		super(formModel, propertyName, propertyNameLinguaAzienda, anagraficaTabelleBD, aziendaCorrente);
	}

	/**
	 * Restituisce la stringa passata come parametro con la prima lettera maiuscola e le rimanenti minuscole.
	 * 
	 * @param word
	 *            stringa da formattare
	 * @return stringa formattata
	 */
	private String capitalizeWord(String word) {

		String firstLetter = word.substring(0, 1); // Get first letter
		String remainder = word.substring(1); // Get remainder of word.
		String capitalized = firstLetter.toUpperCase() + remainder.toLowerCase();

		return capitalized;
	}

	@Override
	protected void createControl() {
		logger.debug("--> Enter createControl");

		JPanel rootPanel = new JPanel(new VerticalLayout(10));
		descrizioniLinguaEstesaForm = new HashMap<String, DescrizioneLinguaEstesaForm>();

		for (IDescrizioneLingua descrizioneLingua : getListDescrizioniLingue()) {

			Locale locale = new Locale(descrizioneLingua.getCodiceLingua());

			DescrizioneLinguaEstesaForm descrizioneLinguaEstesaForm = new DescrizioneLinguaEstesaForm(
					(HierarchicalFormModel) getFormModel(), descrizioneLingua);
			descrizioniLinguaEstesaForm.put(descrizioneLingua.getCodiceLingua(), descrizioneLinguaEstesaForm);
			descrizioniLinguaFormModel.put(descrizioneLingua.getCodiceLingua(),
					descrizioneLinguaEstesaForm.getChildFormModel());
			descrizioneLinguaEstesaForm.getChildFormModel().addCommitListener(descrizioneLinguaCommitListener);

			JLabel linguaLabel = new JLabel(capitalizeWord(locale.getDisplayLanguage()),
					RcpSupport.getIcon(descrizioneLingua.getCodiceLingua()), SwingConstants.LEFT);

			JPanel linguaPanel = new JPanel(new BorderLayout());
			linguaPanel.add(linguaLabel, BorderLayout.NORTH);
			linguaPanel.add(descrizioneLinguaEstesaForm.getControl(), BorderLayout.CENTER);

			rootPanel.add(linguaPanel);
		}

		this.getViewport().add(rootPanel);
	}

	@Override
	protected List<IDescrizioneLingua> createDescrizioniLinguaMancanti(List<Lingua> lingueAziendali,
			Set<String> linguePresenti) {
		List<IDescrizioneLingua> listDescrizioniMancanti = new ArrayList<IDescrizioneLingua>();

		// se non è presente aggiungo la lingua dell'azienda
		if (!linguePresenti.contains(getAziendaCorrente().getLingua())) {
			IDescrizioneLingua descrizioneLingua = ((IDescrizioneEstesaFactory) getFormModel().getFormObject())
					.createDescrizioneLinguaEstesa();
			descrizioneLingua.setCodiceLingua(getAziendaCorrente().getLingua());
			listDescrizioniMancanti.add(descrizioneLingua);
		}

		// aggiungo le altre lingue se mancano
		for (Lingua lingua : lingueAziendali) {
			if (!linguePresenti.contains(lingua.getCodice())) {
				IDescrizioneLingua descrizioneLingua = ((IDescrizioneEstesaFactory) getFormModel().getFormObject())
						.createDescrizioneLinguaEstesa();
				descrizioneLingua.setCodiceLingua(lingua.getCodice());
				listDescrizioniMancanti.add(descrizioneLingua);
			}
		}

		return listDescrizioniMancanti;
	}

	@Override
	protected void updateControl() {
		logger.debug("--> Enter updateControl");
		// if (getAziendaCorrente() != null) {
		// FormModel childFormModel = descrizioniLinguaFormModel.get(getAziendaCorrente().getLingua());
		// childFormModel.getValueModel("descrizione").setValue(
		// getFormModel().getValueModel("descrizioneLinguaAziendale").getValue());
		// }
		creaListDescrizioniLingua();
		@SuppressWarnings("unchecked")
		Map<String, IDescrizioneLingua> lingue = (Map<String, IDescrizioneLingua>) getFormModel().getValueModel(
				getPropertyName()).getValue();
		for (IDescrizioneLingua descrizioneLingua : getListDescrizioniLingue()) {
			if (descrizioniLinguaFormModel.containsKey(descrizioneLingua.getCodiceLingua())) {
				FormModel formModel = descrizioniLinguaFormModel.get(descrizioneLingua.getCodiceLingua());
				formModel.setFormObject(descrizioneLingua);
				// if ( (getAziendaCorrente() != null)
				// && (getAziendaCorrente().getLingua().equals(descrizioneLingua.getCodiceLingua()))) {
				// getFormModel().getValueModel("descrizioneEstesaLinguaAziendale").setValue(
				// descrizioneLingua.getDescrizione());
				// } else
				if ((descrizioneLingua.getDescrizione() == null) || (descrizioneLingua.getDescrizione().isEmpty())) {
					lingue.remove(descrizioneLingua.getCodiceLingua());
				} else {
					lingue.put(descrizioneLingua.getCodiceLingua(), descrizioneLingua);
				}
			}
		}
		logger.debug("--> Exit updateControl");
	}
}
