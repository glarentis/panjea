package it.eurotn.panjea.anagrafica.rich.search.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa.NumeroLivelloAmministrativo;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class SuddivisioneAmministrativaSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(SuddivisioneAmministrativaSearchObject.class);
	private IDatiGeograficiBD datiGeograficiBD;
	private NumeroLivelloAmministrativo livelloAmministrativo = null;

	@Override
	public Object createNewInstance() {
		if (livelloAmministrativo != null) {
			switch (livelloAmministrativo) {
			case LVL1:
			default:
				LivelloAmministrativo1 livelloAmministrativo1 = new LivelloAmministrativo1();
				return livelloAmministrativo1;
			case LVL2:
				LivelloAmministrativo2 livelloAmministrativo2 = new LivelloAmministrativo2();
				return livelloAmministrativo2;
			case LVL3:
				LivelloAmministrativo3 livelloAmministrativo3 = new LivelloAmministrativo3();
				return livelloAmministrativo3;
			case LVL4:
				LivelloAmministrativo4 livelloAmministrativo4 = new LivelloAmministrativo4();
				return livelloAmministrativo4;
			}
		}
		return new LivelloAmministrativo1();
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		if (parameters.containsKey(NumeroLivelloAmministrativo.class.getName())) {
			livelloAmministrativo = (NumeroLivelloAmministrativo) parameters.get(NumeroLivelloAmministrativo.class
					.getName());
			logger.debug("--> filtro livello amministrativo " + livelloAmministrativo);
		}
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
		DatiGeografici datiGeografici = new DatiGeografici();
		datiGeografici.setNazione(nazione);
		datiGeografici.setLivelloAmministrativo1(livello1);
		datiGeografici.setLivelloAmministrativo2(livello2);
		datiGeografici.setLivelloAmministrativo3(livello3);
		datiGeografici.setLivelloAmministrativo4(livello4);
		List<SuddivisioneAmministrativa> lvl1s = datiGeograficiBD.caricaSuddivisioniAmministrative(datiGeografici,
				livelloAmministrativo, valueSearch);
		return lvl1s;
	}

	/**
	 * @return the datiGeograficiBD
	 */
	public IDatiGeograficiBD getDatiGeograficiBD() {
		return datiGeograficiBD;
	}

	/**
	 * @return the livelloAmministrativo
	 */
	public NumeroLivelloAmministrativo getLivelloAmministrativo() {
		return livelloAmministrativo;
	}

	@Override
	public void openDialogPage(Object object) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		if (parameters.containsKey(NumeroLivelloAmministrativo.class.getName())) {
			livelloAmministrativo = (NumeroLivelloAmministrativo) parameters.get(NumeroLivelloAmministrativo.class
					.getName());
			logger.debug("--> filtro livello amministrativo " + livelloAmministrativo);
		}
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

		if (livelloAmministrativo != null) {
			switch (livelloAmministrativo) {
			case LVL1:
				LivelloAmministrativo1 livelloAmministrativo1 = (LivelloAmministrativo1) object;
				livelloAmministrativo1.setNazione(nazione);
				break;
			case LVL2:
				LivelloAmministrativo2 livelloAmministrativo2 = (LivelloAmministrativo2) object;
				livelloAmministrativo2.setSuddivisioneAmministrativaPrecedente(livello1);
				livelloAmministrativo2.setNazione(nazione);
				break;
			case LVL3:
				LivelloAmministrativo3 livelloAmministrativo3 = (LivelloAmministrativo3) object;
				livelloAmministrativo3.setSuddivisioneAmministrativaPrecedente(livello2);
				livelloAmministrativo3.setNazione(nazione);
				break;
			case LVL4:
				LivelloAmministrativo4 livelloAmministrativo4 = (LivelloAmministrativo4) object;
				livelloAmministrativo4.setSuddivisioneAmministrativaPrecedente(livello3);
				livelloAmministrativo4.setNazione(nazione);				
				break;
			default:
				break;
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

	/**
	 * @param livelloAmministrativo
	 *            the livelloAmministrativo to set
	 */
	public void setLivelloAmministrativo(NumeroLivelloAmministrativo livelloAmministrativo) {
		this.livelloAmministrativo = livelloAmministrativo;
	}

}
