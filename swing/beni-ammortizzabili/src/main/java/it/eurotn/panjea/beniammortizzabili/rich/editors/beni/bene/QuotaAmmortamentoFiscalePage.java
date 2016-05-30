/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Aracno
 * @version 1.0, 16/ott/06
 * 
 */
public class QuotaAmmortamentoFiscalePage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "quotaAmmortamentoFiscalePage";

	/**
	 * Costruttore.
	 * 
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public QuotaAmmortamentoFiscalePage(final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PAGE_ID, new QuotaAmmortamentoFiscaleForm(new QuotaAmmortamentoFiscale(), beniAmmortizzabiliBD));
		org.springframework.util.Assert.notNull(beniAmmortizzabiliBD, "beniAmmortizzabiliBD must be not null");
		this.setShowTitlePane(false);
	}

	@Override
	protected JComponent createToobar() {
		// faccio ritornare un jpanel vuoto per evitare che mi vengano inserite
		// la toolbar dei comandi e quella degli errori visto che il dettaglio
		// della quota è in sola lettura e quindi non ci sono comandi
		return new JPanel();
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return null;
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

}
