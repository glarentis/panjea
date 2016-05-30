package it.eurotn.panjea.partite.rich.commands;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.partite.rich.tabelle.righestruttura.ParametriGeneraRigheStrutturaPartite;

import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;

public class GeneraRigheStrutturaCommand extends ActionCommand {

	private IPartiteBD partiteBD = null;
	private List<RigaStrutturaPartite> righeStruttura = null;
	private static final String COMMAND_ID = "generaRigheStrutturaCommand";
	private String partiteBDId = null;
	private ParametriGeneraRigheStrutturaPartite parametriGeneraRigheStrutturaPartite = null;
	private StrutturaPartita strutturaPartita;

	/**
	 * Costruttore di default.
	 */
	public GeneraRigheStrutturaCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		righeStruttura = getPartiteBD().creaRigheStrutturaPartite(strutturaPartita,
				parametriGeneraRigheStrutturaPartite.getNumeroRate(),
				parametriGeneraRigheStrutturaPartite.getIntervallo());
	}

	/**
	 * @return partiteBD
	 */
	public IPartiteBD getPartiteBD() {
		if (partiteBD == null) {
			partiteBD = (IPartiteBD) Application.instance().getApplicationContext().getBean(partiteBDId);
		}
		return partiteBD;
	}

	/**
	 * @return the righeStruttura
	 */
	public List<RigaStrutturaPartite> getRigheStruttura() {
		return righeStruttura;
	}

	/**
	 * @return the strutturaPartita
	 */
	public StrutturaPartita getStrutturaPartita() {
		return strutturaPartita;
	}

	/**
	 * @param parametriGeneraRigheStrutturaPartite
	 *            the parametriGeneraRigheStrutturaPartite to set
	 */
	public void setParametriGeneraRigheStrutturaPartite(
			ParametriGeneraRigheStrutturaPartite parametriGeneraRigheStrutturaPartite) {
		this.parametriGeneraRigheStrutturaPartite = parametriGeneraRigheStrutturaPartite;
	}

	/**
	 * @param partiteBDId
	 *            the partiteBDId to set
	 */
	public void setPartiteBDId(String partiteBDId) {
		this.partiteBDId = partiteBDId;
	}

	/**
	 * @param strutturaPartita
	 *            the strutturaPartita to set
	 */
	public void setStrutturaPartita(StrutturaPartita strutturaPartita) {
		this.strutturaPartita = strutturaPartita;
	}

}
