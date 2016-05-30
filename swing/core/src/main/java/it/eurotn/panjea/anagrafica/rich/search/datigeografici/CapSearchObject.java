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

public class CapSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(CapSearchObject.class);
	private static final String SEARCH_ID = "capSearchObject";
	private IDatiGeograficiBD datiGeograficiBD;

	/**
	 * Costruttore.
	 */
	public CapSearchObject() {
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
		Localita localita = null;
		if (parameters.containsKey(Localita.class.getName())) {
			localita = (Localita) parameters.get(Localita.class.getName());
			logger.debug("--> filtro localita " + (localita != null ? localita.getDescrizione() : "n.d."));
		}

		DatiGeografici datiGeografici = new DatiGeografici();
		datiGeografici.setNazione(nazione);
		datiGeografici.setLivelloAmministrativo1(livello1);
		datiGeografici.setLivelloAmministrativo2(livello2);
		datiGeografici.setLivelloAmministrativo3(livello3);
		datiGeografici.setLivelloAmministrativo4(livello4);
		datiGeografici.setLocalita(localita);
		Cap cap = new Cap();
		cap.setDescrizione(valueSearch);
		datiGeografici.setCap(cap);

		List<Cap> listCap = datiGeograficiBD.caricaCap(datiGeografici);
		return listCap;
	}

	/**
	 * @return the datiGeograficiBD
	 */
	public IDatiGeograficiBD getDatiGeograficiBD() {
		return datiGeograficiBD;
	}

	@Override
	public Object getValueObject(Object object) {
		Cap cap = (Cap) object;
		if (cap.getId() != null) {
			cap = datiGeograficiBD.caricaCap(cap.getId());
		}
		return cap;
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
		Localita localita = null;
		if (parameters.containsKey(Localita.class.getName())) {
			localita = (Localita) parameters.get(Localita.class.getName());
			logger.debug("--> filtro localita " + (localita != null ? localita.getDescrizione() : "n.d."));
		}
		if (object instanceof Cap) {
			Cap cap = (Cap) object;
			cap.setNazione(nazione);
			cap.setLivelloAmministrativo1(livello1);
			cap.setLivelloAmministrativo2(livello2);
			cap.setLivelloAmministrativo3(livello3);
			cap.setLivelloAmministrativo4(livello4);
			if (localita != null) {
				cap.getLocalita().add(localita);
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
