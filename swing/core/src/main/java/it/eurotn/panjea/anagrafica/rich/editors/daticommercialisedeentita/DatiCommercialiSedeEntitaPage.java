/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.daticommercialisedeentita;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.forms.anagraficadaticommericialiForm.AnagraficaDatiCommercialiForm;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.core.SecurityControllable;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.StyledLabelBuilder;

/**
 * Contiene i dati commerciali riferiti ad una sedeEntità.<br>
 * Questa pagina ha un comportamento diverso da quello standard perchè deve contenere <br>
 * dati provenienti da vari plugin, senza sapere quali plugin sono abilitati.<br>
 * La pagina contiene i dati standard che stanno sull'anagrafica,poi una lista di page<br>
 * che i vari plugin iniettano.<br>
 * Quando ho una save lancio le varie save delle pagine iniettate.<br>
 * Il form principale è il form dei dati commerciali presenti in anagrafica e quindi <br>
 * sempre presenti perchè anagrafica è il plugin principale.
 *
 * @author giangi
 */
public class DatiCommercialiSedeEntitaPage extends FormBackedDialogPageEditor implements SecurityControllable {

	private static final String PAGE_ID = "datiCommercialiSedeEntitaPage";

	private static final String OVERLAY_DATI_EREDITATI_MESSAGE = PAGE_ID + ".overlay.message";

	private static Logger logger = Logger.getLogger(DatiCommercialiSedeEntitaPage.class);

	private List<FormBackedDialogPageEditor> pagine = new ArrayList<FormBackedDialogPageEditor>();
	private boolean authorized;
	private String securityControllerId;
	private static final String CARD_DATI_EREDITATI = "datiCommercialiEreditati";
	private static final String CARD_DATI_STANDARD = "datiCommercialiStandard";
	private DefaultOverlayable defaultOverlayable;
	private JPanel cardLayout;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore.
	 */
	public DatiCommercialiSedeEntitaPage() {
		super(PAGE_ID, new AnagraficaDatiCommercialiForm());
	}

	@Override
	public JComponent createControl() {
		cardLayout = getComponentFactory().createPanel(new CardLayout());
		JPanel pannello = getComponentFactory().createPanel(new VerticalLayout(0));

		JComponent datiCommercialiSedePageComponent = super.createControl();
		GuiStandardUtils.attachBorder(datiCommercialiSedePageComponent, BorderFactory.createEmptyBorder(0, 0, -10, 10));
		pannello.add(datiCommercialiSedePageComponent);

		for (FormBackedDialogPageEditor pagina : pagine) {
			JComponent pageComponent = pagina.getForm().getControl();
			JPanel pagePanel = new JPanel(new BorderLayout());
			GuiStandardUtils.attachBorder(pageComponent, BorderFactory.createEmptyBorder(0, 10, 0, 10));
			pagePanel.add(pageComponent, BorderLayout.CENTER);
			if (!(pagina.getForm().getFormObject() instanceof SedeMagazzino)) {
				pagePanel.add(pagina.getAuditCommand().createButton(), BorderLayout.EAST);
			}
			pannello.add(pagePanel);
			this.getForm().getFormModel().addChild(pagina.getForm().getFormModel());
		}

		JPanel panelDatiEreditati = getComponentFactory().createPanel();

		defaultOverlayable = new DefaultOverlayable(panelDatiEreditati);
		defaultOverlayable.addOverlayComponent(StyledLabelBuilder
				.createStyledLabel(getMessage(OVERLAY_DATI_EREDITATI_MESSAGE)));
		cardLayout.add(pannello, CARD_DATI_STANDARD);
		cardLayout.add(defaultOverlayable, CARD_DATI_EREDITATI);

		getForm().addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Object sede = evt.getNewValue();
				CardLayout layout = (CardLayout) cardLayout.getLayout();

				if (isVisible()) {
					if (sede != null && sedeEntita.isEreditaDatiCommerciali()) {
						layout.show(cardLayout, CARD_DATI_EREDITATI);
					} else {
						layout.show(cardLayout, CARD_DATI_STANDARD);
					}
				} else {
					defaultOverlayable.remove(0);
					defaultOverlayable.addOverlayComponent(StyledLabelBuilder
							.createStyledLabel("I suoi permessi attuali non permettono visualizzare questa pagina"));
					layout.show(cardLayout, CARD_DATI_EREDITATI);
				}
			}
		});

		return cardLayout;
	}

	@Override
	protected Object doSave() {
		for (FormBackedDialogPageEditor pagina : pagine) {
			pagina.onSave();
		}
		return sedeEntita;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		logger.debug("--> Enter getCommand");
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
		logger.debug("--> Exit getCommand");
		return commands;
	}

	/**
	 *
	 * @return lista di pagine contenute nella pagina principale
	 */
	public List<FormBackedDialogPageEditor> getPagine() {
		return pagine;
	}

	@Override
	public String getSecurityControllerId() {

		return securityControllerId;
	}

	@Override
	public boolean isAuthorized() {
		return authorized;
	}

	@Override
	public boolean isEnabled() {
		return isAuthorized();
	}

	@Override
	public void loadData() {
		getForm().setFormObject(sedeEntita);
		// i dati della sede magazzino sono gia settati
		for (IPageLifecycleAdvisor pagina : pagine) {
			pagina.loadData();
		}
	}

	@Override
	public ILock onLock() {
		ILock lock = super.onLock();
		// HACK devo ciclare le page perche' all'interno perche' come child
		// sembra
		// che non ricevano la variazione del read only da verificare
		for (FormBackedDialogPageEditor page : pagine) {
			page.getForm().getFormModel().setReadOnly(false);
		}
		return lock;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		boolean canOpen = true;
		// se trovo una delle page inaccessibile, rendo inaccessibile l'accesso
		// alla pagina che le contiene
		for (IPageLifecycleAdvisor pagina : pagine) {
			canOpen = pagina.onPrePageOpen();
			if (!canOpen) {
				break;
			}
		}
		return canOpen;
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setAuthorized(boolean flag) {
		boolean wasAuthorized = isAuthorized();
		if (hasChanged(wasAuthorized, authorized)) {
			firePropertyChange("authorized", wasAuthorized, authorized);
		}

	}

	@Override
	public void setFormObject(Object object) {
		sedeEntita = (SedeEntita) object;
		super.setFormObject(object);
		for (IPageLifecycleAdvisor pagina : pagine) {
			pagina.setFormObject(object);
		}
		loadData();
	}

	/**
	 *
	 * @param pagine
	 *            pagine da inserire e concatenare.
	 */
	public void setPagine(List<FormBackedDialogPageEditor> pagine) {
		this.pagine = pagine;
		for (FormBackedDialogPageEditor formBackedDialogPageEditor : pagine) {
			if (formBackedDialogPageEditor.getForm().getFormObject() instanceof SedeMagazzino) {
				setAuditCommand(formBackedDialogPageEditor.getAuditCommand());
			}
		}
	}

	@Override
	public void setSecurityControllerId(String s) {
		securityControllerId = s;
	}

}
