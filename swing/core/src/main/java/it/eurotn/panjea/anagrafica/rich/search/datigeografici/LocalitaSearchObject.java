package it.eurotn.panjea.anagrafica.rich.search.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class LocalitaSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(LocalitaSearchObject.class);
	private static final String SEARCH_ID = "localitaSearchObject";
	private IDatiGeograficiBD datiGeograficiBD;

	/**
	 * Costruttore.
	 */
	public LocalitaSearchObject() {
		super(SEARCH_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		Nazione nazione = null;
		if (parameters.containsKey(Nazione.class.getName())) {
			nazione = (Nazione) parameters.get(Nazione.class.getName());
			logger.debug("--> filtro nazione " + (nazione != null ? nazione.getCodice() : "n.d."));
		}
		LivelloAmministrativo1 livello1 = null;
		if (parameters.containsKey(LivelloAmministrativo1.class.getName())) {
			livello1 = (LivelloAmministrativo1) parameters.get(LivelloAmministrativo1.class.getName());
			logger.debug("--> filtro livello1 " + (livello1 != null ? livello1.getNome() : "n.d."));
		}
		LivelloAmministrativo2 livello2 = null;
		if (parameters.containsKey(LivelloAmministrativo2.class.getName())) {
			livello2 = (LivelloAmministrativo2) parameters.get(LivelloAmministrativo2.class.getName());
			logger.debug("--> filtro livello2 " + (livello2 != null ? livello2.getNome() : "n.d."));
		}
		LivelloAmministrativo3 livello3 = null;
		if (parameters.containsKey(LivelloAmministrativo3.class.getName())) {
			livello3 = (LivelloAmministrativo3) parameters.get(LivelloAmministrativo3.class.getName());
			logger.debug("--> filtro livello3 " + (livello3 != null ? livello3.getNome() : "n.d."));
		}
		LivelloAmministrativo4 livello4 = null;
		if (parameters.containsKey(LivelloAmministrativo4.class.getName())) {
			livello4 = (LivelloAmministrativo4) parameters.get(LivelloAmministrativo4.class.getName());
			logger.debug("--> filtro livello4 " + (livello4 != null ? livello4.getNome() : "n.d."));
		}
		Cap cap = null;
		if (parameters.containsKey(Cap.class.getName())) {
			cap = (Cap) parameters.get(Cap.class.getName());
			logger.debug("--> filtro cap " + (cap != null ? cap.getDescrizione() : "n.d."));
		}

		DatiGeografici datiGeografici = new DatiGeografici();
		datiGeografici.setNazione(nazione);
		datiGeografici.setLivelloAmministrativo1(livello1);
		datiGeografici.setLivelloAmministrativo2(livello2);
		datiGeografici.setLivelloAmministrativo3(livello3);
		datiGeografici.setLivelloAmministrativo4(livello4);
		datiGeografici.setCap(cap);
		Localita localita = new Localita();
		localita.setDescrizione(valueSearch);
		datiGeografici.setLocalita(localita);

		List<Localita> listLocalita = datiGeograficiBD.caricaLocalita(datiGeografici);
		return listLocalita;
	}

	/**
	 * @return the datiGeograficiBD
	 */
	public IDatiGeograficiBD getDatiGeograficiBD() {
		return datiGeograficiBD;
	}

	@Override
	public Object getValueObject(Object object) {
		Localita localita = (Localita) object;
		if (localita.getId() != null) {
			localita = datiGeograficiBD.caricaLocalita(localita.getId());
		}
		return localita;
	}

	@Override
	public void openDialogPage(Object object) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		Nazione nazione = null;
		if (parameters.containsKey(Nazione.class.getName())) {
			nazione = (Nazione) parameters.get(Nazione.class.getName());
			logger.debug("--> filtro nazione " + (nazione != null ? nazione.getCodice() : "n.d."));
		}
		LivelloAmministrativo1 livello1 = null;
		if (parameters.containsKey(LivelloAmministrativo1.class.getName())) {
			livello1 = (LivelloAmministrativo1) parameters.get(LivelloAmministrativo1.class.getName());
			logger.debug("--> filtro livello1 " + (livello1 != null ? livello1.getNome() : "n.d."));
		}
		LivelloAmministrativo2 livello2 = null;
		if (parameters.containsKey(LivelloAmministrativo2.class.getName())) {
			livello2 = (LivelloAmministrativo2) parameters.get(LivelloAmministrativo2.class.getName());
			logger.debug("--> filtro livello2 " + (livello2 != null ? livello2.getNome() : "n.d."));
		}
		LivelloAmministrativo3 livello3 = null;
		if (parameters.containsKey(LivelloAmministrativo3.class.getName())) {
			livello3 = (LivelloAmministrativo3) parameters.get(LivelloAmministrativo3.class.getName());
			logger.debug("--> filtro livello3 " + (livello3 != null ? livello3.getNome() : "n.d."));
		}
		LivelloAmministrativo4 livello4 = null;
		if (parameters.containsKey(LivelloAmministrativo4.class.getName())) {
			livello4 = (LivelloAmministrativo4) parameters.get(LivelloAmministrativo4.class.getName());
			logger.debug("--> filtro livello4 " + (livello4 != null ? livello4.getNome() : "n.d."));
		}
		Cap cap = null;
		if (parameters.containsKey(Cap.class.getName())) {
			cap = (Cap) parameters.get(Cap.class.getName());
			logger.debug("--> filtro cap " + (cap != null ? cap.getDescrizione() : "n.d."));
		}
		if (object instanceof Localita) {
			Localita localita = (Localita) object;
			localita.setNazione(nazione);
			localita.setLivelloAmministrativo1(livello1);
			localita.setLivelloAmministrativo2(livello2);
			localita.setLivelloAmministrativo3(livello3);
			localita.setLivelloAmministrativo4(livello4);
			if (cap != null) {
				localita.getCap().add(cap);
			}
		}
		super.openDialogPage(object);
	}

	/**
	 * @param datiGeograficiBD
	 *            the datiGeograficiBD to set
	 */
	public void setDatiGeograficiBD(IDatiGeograficiBD datiGeograficiBD) {
		this.datiGeograficiBD = datiGeograficiBD;
	}

}
