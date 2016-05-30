/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Aracno,Leonardo
 * @version 1.0, 09/ott/06
 * 
 */
public class ValutazioneBenePage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(ValutazioneBenePage.class);

	private static final String PAGE_ID = "valutazioniBeneTablePage.valutazioneBenePage";
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD = null;
	private BeneAmmortizzabile beneAmmortizzabile = null;

	/**
	 * Costruttore.
	 * 
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public ValutazioneBenePage(final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PAGE_ID, new ValutazioneBeneForm(new BeneAmmortizzabile(), beniAmmortizzabiliBD));
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	protected Object doDelete() {
		beniAmmortizzabiliBD.cancellaValutazioneBene((ValutazioneBene) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ValutazioneBene valutazioneBene = (ValutazioneBene) getBackingFormPage().getFormObject();
		valutazioneBene.setBene(beneAmmortizzabile);
		logger.debug("--> mi accingo al salvataggio della valutazione " + valutazioneBene + " associata al bene "
				+ beneAmmortizzabile);
		ValutazioneBene valutazioneBeneSalvata = beniAmmortizzabiliBD.salvaValutazioneBene(valutazioneBene);
		logger.debug("--> salvata la valutazione " + valutazioneBene + " associata al bene " + beneAmmortizzabile);
		return valutazioneBeneSalvata;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param beneAmmortizzabile
	 *            the beneAmmortizzabile to set
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
		((ValutazioneBeneForm) getBackingFormPage()).setBeneAmmortizzabile(beneAmmortizzabile);
	}
}
