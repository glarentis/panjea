package it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

public class ArticoliAlternativiDialog extends PanjeaTitledPageApplicationDialog {

	private ArticoliAlternativiCompositePage articoliAlternativiPage;

	private ArticoloRicerca articoloSelezionato = null;
	private Articolo articolo;

	/**
	 *
	 * @param articolo
	 *            articolo da sostituire
	 */
	public ArticoliAlternativiDialog(final Articolo articolo) {
		super();
		this.articolo = articolo;
		articoliAlternativiPage = RcpSupport.getBean(ArticoliAlternativiCompositePage.BEAN_ID);
		setDialogPage(articoliAlternativiPage);
	}

	@Override
	protected JComponent createTitledDialogContentPane() {
		// Devo creare i controlli prima di passare l'articolo alla setFormObject altrimenti non ripassa l'editor object
		// alle pagine interne
		articoliAlternativiPage.getControl();
		articoliAlternativiPage.setFormObject(articolo);
		articoliAlternativiPage.loadData();

		setPreferredSize(new Dimension(900, 800));
		articoliAlternativiPage.setSelectCommandExecutor(new ActionCommand() {
			@Override
			protected void doExecuteCommand() {
				Object entitySelezionato = getParameter(JideTableWidget.SELECTED_VALUE_PARAM);
				// l'entity può essere un articoloAlternativo o un articolo ricerca,
				// adatto la cosa.
				if (entitySelezionato instanceof ArticoloAlternativo) {
					entitySelezionato = ((ArticoloAlternativo) entitySelezionato).getArticoloAlternativo();
				}
				articoloSelezionato = new ArticoloRicerca();
				PanjeaSwingUtil.copyProperties(articoloSelezionato, entitySelezionato);
				getFinishCommand().execute();
			}
		});
		return super.createTitledDialogContentPane();
	}

	/**
	 * @return Returns the articoloSelezionato.
	 */
	public ArticoloRicerca getArticoloSelezionato() {
		return articoloSelezionato;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new AbstractCommand[] { getCancelCommand() };
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();

		SettingsManager manager = (SettingsManager) Application.services().getService(SettingsManager.class);
		try {
			articoliAlternativiPage.restoreState(manager.getUserSettings());
		} catch (SettingsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCancel() {
		super.onCancel();

		saveState();
	}

	@Override
	protected boolean onFinish() {
		saveState();
		return true;
	}

	private void saveState() {
		SettingsManager settingsManager = (SettingsManager) Application.services().getService(SettingsManager.class);
		try {
			articoliAlternativiPage.saveState(settingsManager.getUserSettings());
			settingsManager.getUserSettings().save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
