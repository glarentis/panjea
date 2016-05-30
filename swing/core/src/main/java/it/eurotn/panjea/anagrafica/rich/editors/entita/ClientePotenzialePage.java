package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica.AnagraficaPage;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

public class ClientePotenzialePage extends AnagraficaPage implements InitializingBean {

	private String confermaClientePotenzialeCommandId = null;

	private ConfermaClientePotenzialeCommand confermaClientePotenzialeCommand = null;

	/**
	 * 
	 * @param entita
	 *            entità delal pagina
	 * @param anagraficaBD
	 *            bd della pagina
	 * 
	 */
	public ClientePotenzialePage(final Entita entita, final IAnagraficaBD anagraficaBD) {
		super(entita, anagraficaBD);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(confermaClientePotenzialeCommandId,
				"confermaClientePotenzialeCommandId cannot be null");
		confermaClientePotenzialeCommand = new ConfermaClientePotenzialeCommand(this);
	}

	@Override
	protected Object doSave() {
		Entita entitaSave = (Entita) super.doSave();
		confermaClientePotenzialeCommand.setIdEntita(entitaSave.getId());
		return entitaSave;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = super.getCommand();
		abstractCommands = (AbstractCommand[]) PanjeaSwingUtil.resizeArray(abstractCommands,
				abstractCommands.length + 1);
		abstractCommands[abstractCommands.length - 1] = confermaClientePotenzialeCommand;
		return abstractCommands;
	}

	/**
	 * @return the confermaClientePotenzialeCommandId
	 */
	public String getConfermaClientePotenzialeCommandId() {
		return confermaClientePotenzialeCommandId;
	}

	@Override
	public void onNew() {
		super.onNew();
		confermaClientePotenzialeCommand.setIdEntita(null);
	}

	/**
	 * @param confermaClientePotenzialeCommandId
	 *            the confermaClientePotenzialeCommandId to set
	 */
	public void setConfermaClientePotenzialeCommandId(String confermaClientePotenzialeCommandId) {
		this.confermaClientePotenzialeCommandId = confermaClientePotenzialeCommandId;
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
		if (object != null && object instanceof Entita) {
			confermaClientePotenzialeCommand.setIdEntita(((Entita) object).getId());
		}
	}

}
