/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
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
 * @author Aracno
 * @version 1.0, 10/ott/06
 */
public class VenditeBeneTablePage extends AbstractTablePageEditor<VenditaBene> {

	private static final String PAGE_ID = "venditeBeneTablePage";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private BeneAmmortizzabile beneAmmortizzabile;
	private JLabel beneTestataLabel;

	/**
	 * Costruttore.
	 */
	public VenditeBeneTablePage() {
		super(PAGE_ID, new String[] { VenditaBene.PROP_DATA_VENDITA, VenditaBene.PROP_NUMERO_PROTOCOLLO_VENDITA,
				VenditaBene.PROP_IMPORTO_FATTURA_VENDITA, VenditaBene.PROP_DATA_FATTURA_VENDITA }, VenditaBene.class);
	}

	@Override
	public JComponent getHeaderControl() {
		beneTestataLabel = getComponentFactory().createLabel("");
		GuiStandardUtils.attachBorder(beneTestataLabel);
		return beneTestataLabel;
	}

	@Override
	public Collection<VenditaBene> loadTableData() {
		return beniAmmortizzabiliBD.caricaVenditeBene(beneAmmortizzabile);
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
			String titolo = messageSourceAccessor.getMessage("beneAmmortizzabile.null.vendite.messageDialog.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage(
					"beneAmmortizzabile.null.vendite.messageDialog.message", new Object[] {}, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);
		beneTestataLabel.setVisible(true);
		beneTestataLabel.setText(ObjectConverterManager.toString(object));
		beneTestataLabel.setIcon(RcpSupport.getIcon(((BeneAmmortizzabile) object).getClass().getName()));
	}

	@Override
	public Collection<VenditaBene> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public void setFormObject(Object object) {
		beneAmmortizzabile = (BeneAmmortizzabile) object;
		VenditaBenePage vbp = ((VenditaBenePage) getEditPages().get((EditFrame.DEFAULT_OBJECT_CLASS_NAME)));
		vbp.setBeneAmmortizzabile(beneAmmortizzabile);
	}

}
