package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneDepositoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneManager;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino.ProvenienzaPrezzoManutenzioneListino;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;

public class ProvenienzaPrezzoValorizzazioneDepositoQueryBuilder extends ProvenienzaPrezzoQueryBuilder {

	/**
	 * @param parametri
	 *            parametri manutenzione listino
	 * @return parametri per la valorizzazione creati dalla manutenzione listino
	 */
	protected ParametriRicercaValorizzazione creaParametriValorizzazione(ParametriRicercaManutenzioneListino parametri) {
		ParametriRicercaValorizzazione parametriRicercaValorizzazione = new ParametriRicercaValorizzazione();

		// non mi interessa il calcolo della giacenza
		parametriRicercaValorizzazione.setCalcolaGiacenza(false);

		// di default non ho tutte le categorie selezionate e ho al max un deposito solo nell'ultimo costo per deposito
		parametriRicercaValorizzazione.setTutteCategorie(false);
		parametriRicercaValorizzazione.setTuttiDepositi(false);

		// lista di categorie
		List<Categoria> cats = new ArrayList<Categoria>();
		for (CategoriaLite catLite : parametri.getCategorie()) {
			Categoria cat = new Categoria();
			cat.setId(catLite.getId());
			cats.add(cat);
		}
		parametriRicercaValorizzazione.setCategorie(cats);

		// se ultimo costo deposito o azienda
		if (parametri.getProvenienzaPrezzoManutenzioneListino().equals(
				ProvenienzaPrezzoManutenzioneListino.ULTIMO_COSTO_DEPOSITO)) {
			List<Deposito> deps = new ArrayList<Deposito>();
			DepositoLite depLite = parametri.getDeposito();
			Deposito dep = new Deposito();
			dep.setId(depLite.getId());
			deps.add(dep);
			parametriRicercaValorizzazione.setDepositi(deps);
			parametriRicercaValorizzazione.setModalitaValorizzazione(EModalitaValorizzazione.ULTIMO_COSTO_DEPOSITO);
		} else if (parametri.getProvenienzaPrezzoManutenzioneListino().equals(
				ProvenienzaPrezzoManutenzioneListino.ULTIMO_COSTO_AZIENDA)) {
			parametriRicercaValorizzazione.setModalitaValorizzazione(EModalitaValorizzazione.ULTIMO_COSTO_AZIENDA);
		} else if (parametri.getProvenienzaPrezzoManutenzioneListino().equals(
				ProvenienzaPrezzoManutenzioneListino.COSTO_MEDIO_PONDERATO)) {
			List<Deposito> deps = new ArrayList<Deposito>();
			DepositoLite depLite = parametri.getDeposito();
			Deposito dep = new Deposito();
			dep.setId(depLite.getId());
			deps.add(dep);
			parametriRicercaValorizzazione.setDepositi(deps);
			parametriRicercaValorizzazione.setModalitaValorizzazione(EModalitaValorizzazione.COSTO_MEDIO_PONDERATO);
			// in questo caso mi interessa il calcolo della giacenza perchè per calcolare il costo medio ponderato
			// servono i valori degli importi carico e scarico
			parametriRicercaValorizzazione.setCalcolaGiacenza(true);
		}

		// imposto la data attuale come data per trovare l'ultimo costo
		parametriRicercaValorizzazione.setData(new Date());
		return parametriRicercaValorizzazione;
	}

	@Override
	public String getPrezzoSql(ParametriRicercaManutenzioneListino parametri) {
		ParametriRicercaValorizzazione parametriRicercaValorizzazione = creaParametriValorizzazione(parametri);

		InitialContext ctn;
		MagazzinoValorizzazioneManager valorizzazioneManager = null;
		try {
			ctn = new InitialContext();
			valorizzazioneManager = (MagazzinoValorizzazioneManager) ctn.lookup("Panjea.MagazzinoValorizzazioneManager");
		} catch (Exception e) {
			throw new RuntimeException("Errore durante il lookup del manager della valorizzazione", e);
		}

		MagazzinoValorizzazioneDepositoManager valorizzazioneDepositoManager = valorizzazioneManager.getValorizzazioneDepositoManager(parametriRicercaValorizzazione.getModalitaValorizzazione());
		String valDepQuery = valorizzazioneDepositoManager.getSqlString(parametriRicercaValorizzazione, parametri.getDeposito().getId());

		StringBuffer sb = new StringBuffer();
		sb.append("update maga_riga_manutenzione_listino inner join (");
		sb.append(valDepQuery);
		sb.append(") as valDep on valDep.idArticolo=maga_riga_manutenzione_listino.articolo_id set ");
		sb.append(" valoreOriginale=valDep.costo ");
		sb.append(" where maga_riga_manutenzione_listino.numero=");
		sb.append(parametri.getNumeroInserimento());
		sb.append(" and userManutenzione='");
		sb.append(parametri.getUserManutenzione());
		sb.append("'");

		return sb.toString();
	}

}
