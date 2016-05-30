package it.eurotn.panjea.anagrafica.rich.commands;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita.FieldSearch;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;

/**
 * Command search entità (cliente, fornitore, ecc.) nel commands-context.xml si definisce un nuovo bean in cui si devono
 * definire le proprietà entita e searchViewClass, indispensabili per differenziare l'entità scelta da cercare.
 * 
 * @author Leonardo
 */
public class SearchEntitaCommand extends OpenEditorCommand {

	private String entita;

	// la classe legata all'entità per aprire la search result relativa
	private String searchViewClass;

	private Map<String, TipoEntita> mapTipiEntita;

	/**
	 * Verifica che le proprietà commandId (nel metodo setCommandId() viene chiamato setId()) e la proprietà entita sono
	 * state avvalorate.
	 */
	@Override
	public void afterPropertiesSet() {
		org.springframework.util.Assert.notNull(entita, "Entita must be set");
		org.springframework.util.Assert.notNull(searchViewClass, "SearchViewClass must be set");
		setId("search" + entita + "Command");

		mapTipiEntita = new HashMap<String, TipoEntita>();
		mapTipiEntita.put(AgenteLite.class.getName(), TipoEntita.AGENTE);
		mapTipiEntita.put(ClienteLite.class.getName(), TipoEntita.CLIENTE);
		mapTipiEntita.put(ClientePotenzialeLite.class.getName(), TipoEntita.CLIENTE_POTENZIALE);
		mapTipiEntita.put(FornitoreLite.class.getName(), TipoEntita.FORNITORE);
		mapTipiEntita.put(VettoreLite.class.getName(), TipoEntita.VETTORE);

		super.afterPropertiesSet();
	}

	/**
	 * Mostra un nuovo dialogo di ricerca dell'entità scelta tramite la proprietà <code>entità</code>.
	 */
	@Override
	protected void doExecuteCommand() {
		ParametriRicercaEntita parametri = new ParametriRicercaEntita();
		parametri.setTipoEntita(mapTipiEntita.get(searchViewClass));
		parametri.setAbilitato(Boolean.TRUE);
		parametri.setFieldSearch(FieldSearch.NONE);

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("parametri", parametri);

		ApplicationPage applicationPage = Application.instance().getActiveWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage).openResultView(searchViewClass, mapParam);
	}

	/**
	 * @return the entita
	 */
	public String getEntita() {
		return entita;
	}

	/**
	 * @return the searchViewClass
	 */
	public String getSearchViewClass() {
		return searchViewClass;
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName(entita + "." + getId());
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(String entita) {
		this.entita = entita;
	}

	/**
	 * @param searchViewClass
	 *            the searchViewClass to set
	 */
	public void setSearchViewClass(String searchViewClass) {
		this.searchViewClass = searchViewClass;
	}

}
