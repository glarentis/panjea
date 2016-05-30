package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.partite.rich.commands.GeneraRigheStrutturaCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

public class RigheStrutturaTablePage extends AbstractTablePageEditor<RigaStrutturaPartite> {

	private class GeneraRigheActionCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {
			GeneraRigheStrutturaCommand generaRigheStrutturaCmd = (GeneraRigheStrutturaCommand) command;
			List<RigaStrutturaPartite> righe = generaRigheStrutturaCmd.getRigheStruttura();
			logger.debug("--> " + righe.toString());
			strutturaPartita.setRigheStrutturaPartita(righe);
			RigheStrutturaTablePage.this.firePropertyChange(OBJECT_CHANGED, null, strutturaPartita);
			setRows(righe);
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			GeneraRigheStrutturaCommand generaRigheStrutturaCmd = (GeneraRigheStrutturaCommand) command;
			ParametriGeneraRigheStrutturaPartite parametriGeneraRigheStrutturaPartite = (ParametriGeneraRigheStrutturaPartite) parametriGeneraRigheStrutturaPartiteForm
					.getFormObject();
			generaRigheStrutturaCmd.setParametriGeneraRigheStrutturaPartite(parametriGeneraRigheStrutturaPartite);
			generaRigheStrutturaCmd.setStrutturaPartita(strutturaPartita);
			return true;
		}

	}

	public static final String PAGE_ID = "righeStrutturaTablePage";
	private static Logger logger = Logger.getLogger(RigheStrutturaTablePage.class.getName());
	private GeneraRigheStrutturaCommand generaRigheStrutturaCommand = null;
	private ParametriGeneraRigheStrutturaPartiteForm parametriGeneraRigheStrutturaPartiteForm = null;
	private StrutturaPartita strutturaPartita = null;

	private IPartiteBD partiteBD = null;

	/**
	 * Costruttore.
	 */
	public RigheStrutturaTablePage() {
		super(PAGE_ID, new RigheStrutturaTableModel());
		setShowTitlePane(false);
	}

	/**
	 * create i controlli per generare le rate.
	 * 
	 * @return JPanel
	 */
	private JComponent createGeneraRateControls() {
		parametriGeneraRigheStrutturaPartiteForm = new ParametriGeneraRigheStrutturaPartiteForm();
		parametriGeneraRigheStrutturaPartiteForm.setGeneraRigheStrutturaCommand(getGeneraRigheCommand());
		JComponent paramFormControl = parametriGeneraRigheStrutturaPartiteForm.getControl();
		return paramFormControl;
	}

	/**
	 * Crea command per generare le righe struttura.
	 * 
	 * @return GeneraRigheStrutturaCommand
	 */
	private GeneraRigheStrutturaCommand getGeneraRigheCommand() {
		if (generaRigheStrutturaCommand == null) {
			generaRigheStrutturaCommand = (GeneraRigheStrutturaCommand) Application.instance().getActiveWindow()
					.getCommandManager().getCommand("generaRigheStrutturaCommand");
			generaRigheStrutturaCommand.addCommandInterceptor(new GeneraRigheActionCommandInterceptor());
		}
		return generaRigheStrutturaCommand;
	}

	@Override
	public JComponent getHeaderControl() {
		return createGeneraRateControls();
	}

	/**
	 * @return the partiteBD
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
	}

	@Override
	public Collection<RigaStrutturaPartite> loadTableData() {
		List<RigaStrutturaPartite> strutture = new ArrayList<RigaStrutturaPartite>();
		if (strutturaPartita != null) {
			this.strutturaPartita = partiteBD.caricaStrutturaPartita(strutturaPartita.getId());
			strutture = strutturaPartita.getRigheStrutturaPartita();
		}
		return strutture;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Sovrascrivo per fare in modo di poter modificare le rate generate che non sono ancora salvate.<br>
	 * Nel metodo <code>it.eurotn.panjea.partite.rich.tabelle.righestruttura.RigaStrutturaPage.doSave()</code> viene
	 * restituita l'istanza modificata del form, ma non viene salvata; arriva cmq un oggetto da modificare, lo gestisco
	 * quindi per la modifica nella tabella allo stesso modo.<br>
	 * Dato che non ho un oggetto salvato appena generate le rate,l'aggiornamento nella tabella deve essere eseguito per
	 * numero rata e non per id rata.
	 * 
	 * @param evt
	 *            event da verificare
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof IDefProperty) {
			IDefProperty objectDefProperty = (IDefProperty) evt.getNewValue();
			getTable().replaceOrAddRowObject(null, (RigaStrutturaPartite) objectDefProperty, null);
		}
	}

	@Override
	public Collection<RigaStrutturaPartite> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		strutturaPartita = (StrutturaPartita) object;

		RigaStrutturaPage rigaStrutturaPage = (RigaStrutturaPage) getEditPages().get(
				EditFrame.DEFAULT_OBJECT_CLASS_NAME);
		rigaStrutturaPage.setStrutturaPartita(strutturaPartita);
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}
}
