package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.editors.fatturazione.AreaMagazzinoLitePM.StatoRigaAreaMagazzinoLitePM;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.binding.value.support.AbstractPropertyChangePublisher;

public class RisultatiRicercaFatturazioneModel extends AbstractPropertyChangePublisher {

	/**
	 * Comparator utilizzato per ordinare le {@link AreaMagazzinoLitePM}. L'ordinamento viene effetturato in base al
	 * tipo documento e per data registrazione dell'area.
	 * 
	 * @author fattazzo
	 * 
	 */
	private class RisultatiRicercaComparator implements Comparator<AreaMagazzinoLitePM> {

		@Override
		public int compare(AreaMagazzinoLitePM o1, AreaMagazzinoLitePM o2) {

			if (o1.getAreaMagazzinoLite().getDocumento().getTipoDocumento().getId()
					.compareTo(o2.getAreaMagazzinoLite().getDocumento().getTipoDocumento().getId()) != 0) {
				return o1.getAreaMagazzinoLite().getDocumento().getTipoDocumento().getId()
						.compareTo(o2.getAreaMagazzinoLite().getDocumento().getTipoDocumento().getId());
			} else {
				if (o1.getAreaMagazzinoLite().getDataRegistrazione()
						.compareTo(o2.getAreaMagazzinoLite().getDataRegistrazione()) != 0) {
					return o1.getAreaMagazzinoLite().getDataRegistrazione()
							.compareTo(o2.getAreaMagazzinoLite().getDataRegistrazione());
				} else {
					return o1.getAreaMagazzinoLite().getId().compareTo(o2.getAreaMagazzinoLite().getId());
				}
			}
		}

	}

	public static final String AREA_AGGIUNTA = "areaAggiunta";
	public static final String AREA_RIMOSSA = "areaRimossa";
	public static final String RIMUOVI_AREE_SELEZIONATE = "rimuoviAreeSelezionate";

	private Set<AreaMagazzinoLitePM> listAreePM;

	private final Set<AreaMagazzinoLitePM> listAreePMSelezionate;

	private ParametriRicercaFatturazione parametriRicercaFatturazione;

	/**
	 * Costruttoer di default.
	 */
	public RisultatiRicercaFatturazioneModel() {
		super();
		this.listAreePMSelezionate = new HashSet<AreaMagazzinoLitePM>();
	}

	/**
	 * Aggiunge l'area alla lista di aree selezionate.
	 * 
	 * @param areaMagazzinoLitePM
	 *            area da aggiungere
	 */
	public void aggiungiAreaSelezionata(AreaMagazzinoLitePM areaMagazzinoLitePM) {
		this.listAreePMSelezionate.add(areaMagazzinoLitePM);

		// lancia che l'area e' stata aggiunta
		firePropertyChange(AREA_AGGIUNTA, null, areaMagazzinoLitePM);
	}

	/**
	 * Restituisce tutte le aree magazzino lite contenute nel table model.
	 * 
	 * @return lista di aree magazzino lite
	 */
	public Set<AreaMagazzinoLite> getAreeMagazzinoLite() {

		Set<AreaMagazzinoLite> areeResult = new HashSet<AreaMagazzinoLite>();

		Set<AreaMagazzinoLitePM> areePM = getAreePM();
		for (AreaMagazzinoLitePM areaMagazzinoLitePM : areePM) {
			areeResult.add(areaMagazzinoLitePM.getAreaMagazzinoLite());
		}

		return areeResult;
	}

	/**
	 * Restituisce tutte le aree magazzino PM contenute nel table model.
	 * 
	 * @return lista di aree magazzino PM
	 */
	public Set<AreaMagazzinoLitePM> getAreePM() {
		return listAreePM;
	}

	/**
	 * Restituisce la lista ti tutte le aree selezionate.
	 * 
	 * @return lista di aree selezionate
	 */
	public Set<AreaMagazzinoLitePM> getAreePMSelezionate() {
		return listAreePMSelezionate;
	}

	/**
	 * Restituisce i parametri di fatturazione.
	 * 
	 * @return parametri di fatturazione
	 */
	public ParametriRicercaFatturazione getParametriRicercaFatturazione() {
		return parametriRicercaFatturazione;
	}

	/**
	 * Controlla se le aree selezionate sono valide per la data di riferimento.
	 * 
	 * @param dataRiferimento
	 *            data di riferimento
	 * @return <code>false</code> se almeno un'area non è valida, <code>true</code> altrimenti
	 */
	public boolean isAreeSelezionateValid(Date dataRiferimento) {

		if (dataRiferimento == null) {
			return false;
		}

		Calendar calendarDataArea = Calendar.getInstance();

		Calendar calendarDataRiferimento = Calendar.getInstance();
		calendarDataRiferimento.setTime(dataRiferimento);

		for (AreaMagazzinoLitePM area : listAreePMSelezionate) {

			calendarDataArea.setTime(area.getAreaMagazzinoLite().getDocumento().getDataDocumento());

			if (calendarDataArea.getTime().after(dataRiferimento)
					|| calendarDataArea.get(Calendar.YEAR) != calendarDataRiferimento.get(Calendar.YEAR)) {
				return false;
			}
		}

		if (listAreePMSelezionate.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * Rimuove l'area scelta dalla lista.
	 * 
	 * @param areaMagazzinoLitePM
	 *            area da rimuovere da rimuovere
	 * @return true se viene rimossa
	 */
	public boolean removeAreaSelezionata(AreaMagazzinoLitePM areaMagazzinoLitePM) {
		boolean areaRemoved = getAreePMSelezionate().remove(areaMagazzinoLitePM);

		if (getAreePMSelezionate().size() == 0) {
			updateAreeASelezionabili();
		}

		if (areaRemoved) {
			// lancio che l'area e' stata rimossa
			firePropertyChange(AREA_RIMOSSA, null, areaMagazzinoLitePM);
		}
		return areaRemoved;
	}

	/**
	 * Rimuove tutte le aree selezionate.
	 */
	public void rimuoviAreeSelezionate() {
		// aggiorna tutte le aree che non hanno errori di rules validation a SELEZIONABILE
		updateAreeASelezionabili();

		getAreePMSelezionate().clear();

		// lancio l'evento di rimozione delle aree selezionate
		firePropertyChange(RIMUOVI_AREE_SELEZIONATE, false, true);
	}

	/**
	 * 
	 * @param aree
	 *            aree magazzino
	 */
	public void setAreeMagazzinoLite(List<AreaMagazzinoLite> aree) {
		Set<AreaMagazzinoLitePM> areePM = new HashSet<AreaMagazzinoLitePM>();
		for (AreaMagazzinoLite areaMagazzinoLite : aree) {

			areePM.add(new AreaMagazzinoLitePM(areaMagazzinoLite));
		}
		setAreePM(areePM);

		// verifico se la rata e' gia' stata usata
		updateAreeAdAggiunteCarrello();
	}

	/**
	 * 
	 * @param listaAreePM
	 *            aree PM
	 */
	public void setAreePM(Set<AreaMagazzinoLitePM> listaAreePM) {
		this.listAreePM = new TreeSet<AreaMagazzinoLitePM>(new RisultatiRicercaComparator());
		this.listAreePM.addAll(listaAreePM);
	}

	/**
	 * 
	 * @param parametriRicercaFatturazione
	 *            the parametriRicercaFatturazione to set
	 */
	public void setParametriRicercaFatturazione(ParametriRicercaFatturazione parametriRicercaFatturazione) {
		this.parametriRicercaFatturazione = parametriRicercaFatturazione;
	}

	/**
	 * Aggiorna lo stato dei risultati ricerca aree confrontando le aree gia' presenti.
	 */
	public void updateAreeAdAggiunteCarrello() {
		List<AreaMagazzinoLitePM> areeDaAggiornare = new ArrayList<AreaMagazzinoLitePM>();
		// nuovo array con la lista di aree dei risultati ricerca
		areeDaAggiornare.addAll(getAreePM());
		// lascio nella nuova lista solo le aree che ho gia' selezionato
		areeDaAggiornare.retainAll(getAreePMSelezionate());

		// aggiorno lo stato di ogni area nella tabella con le aree risultanti dalla ricerca
		for (AreaMagazzinoLitePM areaMagazzinoLitePM : areeDaAggiornare) {
			updateStatoAreaPM(areaMagazzinoLitePM, StatoRigaAreaMagazzinoLitePM.AGGIUNTO_CARRELLO);
		}
	}

	/**
	 * Aggiorna le aree che non hanno errori di rules validation a SELEZIONABILI.
	 */
	private void updateAreeASelezionabili() {
		for (AreaMagazzinoLitePM areaPM : getAreePM()) {
			if (areaPM.getAreaMagazzinoLite().getRigheNonValide().isEmpty()) {
				areaPM.setStatoRigaAreaMagazzinoLitePM(StatoRigaAreaMagazzinoLitePM.SELEZIONABILE);
			}
		}
	}

	/**
	 * Aggiorna lo stato delle aree passate come parametro allo stato di riferimento se queste sono selezionabili.
	 * 
	 * @param areaPM
	 *            lista di aree
	 * @param statoRigaAreaMagazzinoLitePM
	 *            stato di riferimento
	 */
	public void updateStatoAreaPM(AreaMagazzinoLitePM areaPM, StatoRigaAreaMagazzinoLitePM statoRigaAreaMagazzinoLitePM) {
		for (AreaMagazzinoLitePM areaMagazzinoLitePM : getAreePM()) {
			if (areaMagazzinoLitePM.getAreaMagazzinoLite().getId().equals(areaPM.getAreaMagazzinoLite().getId())) {
				// reset di selected per l'area se non e' selezionabile
				if (statoRigaAreaMagazzinoLitePM != StatoRigaAreaMagazzinoLitePM.SELEZIONABILE) {
					areaMagazzinoLitePM.setSelected(false);
				}
				areaMagazzinoLitePM.setStatoRigaAreaMagazzinoLitePM(statoRigaAreaMagazzinoLitePM);
				break;
			}
		}
	}
}
