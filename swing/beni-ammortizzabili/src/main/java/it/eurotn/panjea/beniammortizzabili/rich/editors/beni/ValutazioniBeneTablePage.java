/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

/**
 * 
 * @author Aracno,Leonardo
 * @version 1.0, 09/ott/06
 * 
 */
public class ValutazioniBeneTablePage extends AbstractTablePageEditor<ValutazioneBene> {

	private static final String PAGE_ID = "valutazioniBeneTablePage";

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private BeneAmmortizzabile beneAmmortizzabile;
	private JLabel beneTestataLabel;

	/**
	 * Costruttore.
	 * 
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public ValutazioniBeneTablePage(final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PAGE_ID, new String[] { ValutazioneBene.PROP_ANNO, ValutazioneBene.PROP_IMPORTO_VALUTAZIONE_BENE,
				ValutazioneBene.PROP_IMPORTO_VALUTAZIONE_FONDO, ValutazioneBene.PROP_NOTE }, ValutazioneBene.class);
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public JComponent getHeaderControl() {
		super.getHeaderControl();
		beneTestataLabel = getComponentFactory().createLabel("");
		GuiStandardUtils.attachBorder(beneTestataLabel);
		return beneTestataLabel;

	}

	@Override
	public Collection<ValutazioneBene> loadTableData() {
		return beniAmmortizzabiliBD.caricaValutazioniBene(beneAmmortizzabile);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (beneAmmortizzabile.isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("beneAmmortizzabile.null.valutazioni.messageDialog.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage(
					"beneAmmortizzabile.null.valutazioni.messageDialog.message", new Object[] {}, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);
		beneTestataLabel.setVisible(true);
		beneTestataLabel.setText(ObjectConverterManager.toString(object));
		beneTestataLabel.setIcon(RcpSupport.getIcon(((BeneAmmortizzabile) object).getClass().getName()));
	}

	@Override
	public Collection<ValutazioneBene> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
		beneAmmortizzabile = (BeneAmmortizzabile) object;
		ValutazioneBenePage vbp = ((ValutazioneBenePage) getEditPages().get((EditFrame.DEFAULT_OBJECT_CLASS_NAME)));
		vbp.setBeneAmmortizzabile(beneAmmortizzabile);
	}
}
