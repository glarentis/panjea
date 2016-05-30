/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.OpenEditorCommand;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author Leonardo
 */
public class NewEntitaCommand extends OpenEditorCommand {

	private Entita entita;

	private AnagraficaLite anagraficaLite;

	private String anagraficaBDId;
	private IAnagraficaBD anagraficaBD;
	private String aziendaCorrenteId;
	private AziendaCorrente aziendaCorrente = null;

	@Override
	public void afterPropertiesSet() {
		org.springframework.util.Assert.notNull(entita, "Entita must be set");
		super.afterPropertiesSet();
	}

	/**
	 * 
	 * @return nuova entità creata
	 */
	protected Entita createNewEntita() {
		Entita entitaNew = null;
		try {
			// creo una nuova entità perchè solo alla lettura dell'xml viene fatta una new altrimenti mi trovo dei dati
			// sporchi al primo salvataggio di un'entità
			entitaNew = (Entita) Class.forName(entita.getClass().getName()).newInstance();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		// Se ho settato una istanza di anagrafica la metto sulla entit�
		if (anagraficaLite != null) {
			// carica Anagrafica
			Anagrafica anagrafica = getAnagraficaBD().caricaAnagrafica(anagraficaLite.getId());
			entitaNew.setAnagrafica(anagrafica);
		}
		// Inizializzazione di Stato da AziendaCorrente
		entitaNew.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setNazione(getAziendaCorrente().getNazione());
		return entitaNew;
	}

	@Override
	protected void doExecuteCommand() {
		entita = createNewEntita();
		LifecycleApplicationEvent event = new OpenEditorEvent(entita);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @return Returns the anagraficaBD.
	 */
	protected IAnagraficaBD getAnagraficaBD() {
		if (anagraficaBD == null) {
			anagraficaBD = (IAnagraficaBD) Application.instance().getApplicationContext().getBean(anagraficaBDId);
		}
		return anagraficaBD;
	}

	/**
	 * @return Returns the anagraficaLite.
	 */
	public AnagraficaLite getAnagraficaLite() {
		return anagraficaLite;
	}

	/**
	 * @return azienda corrente
	 */
	protected AziendaCorrente getAziendaCorrente() {
		if (aziendaCorrente == null) {
			aziendaCorrente = (AziendaCorrente) Application.instance().getApplicationContext()
					.getBean(aziendaCorrenteId);
		}
		return aziendaCorrente;
	}

	/**
	 * 
	 * @return azienda corrente id
	 */
	public String getAziendaCorrenteId() {
		return aziendaCorrenteId;
	}

	/**
	 * @return the entita
	 */
	public Entita getEntita() {
		return entita;
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName(getId());
	}

	/**
	 * @param anagraficaBDId
	 *            The anagraficaBDId to set.
	 */
	public void setAnagraficaBDId(String anagraficaBDId) {
		this.anagraficaBDId = anagraficaBDId;
	}

	/**
	 * @param anagraficaLite
	 *            The anagraficaLite to set.
	 */
	public void setAnagraficaLite(AnagraficaLite anagraficaLite) {
		this.anagraficaLite = anagraficaLite;
	}

	/**
	 * 
	 * @param aziendaCorrenteId
	 *            The aziendaCorrenteId to set
	 */
	public void setAziendaCorrenteId(String aziendaCorrenteId) {
		this.aziendaCorrenteId = aziendaCorrenteId;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(Entita entita) {
		this.entita = entita;
	}

}
