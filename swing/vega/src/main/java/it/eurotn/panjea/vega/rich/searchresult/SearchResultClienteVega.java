package it.eurotn.panjea.vega.rich.searchresult;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.SearchResultEntita;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.vega.rich.bd.IVegaBD;

public class SearchResultClienteVega extends SearchResultEntita {
	private static Logger logger = Logger.getLogger(SearchResultClienteVega.class);
	private IVegaBD vegaBD;

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { "codiceEsterno", Entita.PROP_CODICE, "anagrafica.denominazione",
				"anagrafica.sedeAnagrafica.indirizzo", "anagrafica.partiteIVA",
				"anagrafica.sedeAnagrafica.datiGeografici.cap.descrizione",
				"anagrafica.sedeAnagrafica.datiGeografici.descrizioneLocalita",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo1.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo2.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo3.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.livelloAmministrativo4.nome",
				"anagrafica.sedeAnagrafica.datiGeografici.descrizioneNazione", "anagrafica.sedeAnagrafica.telefono",
				"anagrafica.sedeAnagrafica.fax", "anagrafica.codiceFiscale" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	@Override
	protected Collection<EntitaLite> getData(Map<String, Object> params) {
		return vegaBD.caricaEntitaConCodiceEsternoDaConfermare();
	}

	public IVegaBD getVegaBD() {
		return vegaBD;
	}

	@Override
	public Object reloadObject(EntitaLite entitaLite) {
		Entita entita = (Entita) super.reloadObject(entitaLite);
		if (entita.getCodiceEsterno().startsWith("#")) {
			// Cambio anche ad entitaLite così mi cambia anche nella tabella per
			// riferimento.
			entitaLite.setCodiceEsterno(entitaLite.getCodiceEsterno().replace("#", ""));
			entita.setCodiceEsterno(entitaLite.getCodiceEsterno().replace("#", ""));
			try {
				entita = anagraficaBD.salvaEntita(entita);
			} catch (AnagraficheDuplicateException e) {
				logger.error("-->errore nel salvare l'entita imp. da vega con codice confermato "
						+ entita.getCodiceEsterno(), e);
				throw new RuntimeException("-->errore nel salvare l'entita imp. da vega con codice confermato "
						+ entita.getCodiceEsterno(), e);
			}
		}
		return entita;
	}

	public void setVegaBD(IVegaBD vegaBD) {
		this.vegaBD = vegaBD;
	}

}
