package it.eurotn.panjea.lotti.rich.editors.righemagazzino;

import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.StampaEtichetteDaAreaMagazzinoCommand;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import net.atlanticbb.tantlinger.ui.DefaultAction;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideSplitButton;

public class StampaEtichetteLottiDaAreaMagazzinoCommand extends StampaEtichetteDaAreaMagazzinoCommand implements
		InitializingBean {

	private class MenuItemAction extends DefaultAction {

		private static final long serialVersionUID = 7816440781836630002L;

		@Override
		protected void execute(ActionEvent evt) throws Exception {

			lottiArticoliStampaEtichette = lottiArticoliMenuItem.isSelected();

			settingsManager.getUserSettings().setBoolean(LOTTI_ARTICOLI_ETICHETTE_SETTINGS,
					lottiArticoliStampaEtichette);

			StampaEtichetteLottiDaAreaMagazzinoCommand.this.execute();
		}

	}

	public static final String COMMAND_ID = "stampaEtichetteLottiDaAreaMagazzinoCommand";

	private static final String LOTTI_ARTICOLI_ETICHETTE_SETTINGS = "lottiArticoliStampaEtichette";

	private JRadioButtonMenuItem articoliMenuItem;
	private JRadioButtonMenuItem lottiArticoliMenuItem;

	private JideSplitButton button;

	private ILottiBD lottiBD;

	private SettingsManager settingsManager;

	private boolean lottiArticoliStampaEtichette = false;

	private MenuItemAction menuItemAction;

	/**
	 * Costruttore.
	 */
	public StampaEtichetteLottiDaAreaMagazzinoCommand() {
		super(COMMAND_ID);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		this.menuItemAction = new MenuItemAction();

		articoliMenuItem = new JRadioButtonMenuItem(menuItemAction);
		articoliMenuItem.setText("Articoli");
		articoliMenuItem.setSelected(true);
		articoliMenuItem.setName("EtichetteArticoli");

		lottiArticoliMenuItem = new JRadioButtonMenuItem(menuItemAction);
		lottiArticoliMenuItem.setText("Lotti articoli");
		lottiArticoliMenuItem.setName("EtichetteLotti");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(articoliMenuItem);
		buttonGroup.add(lottiArticoliMenuItem);
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {

		button = new JideSplitButton();
		button.setName(COMMAND_ID);

		button.add(articoliMenuItem);
		button.add(lottiArticoliMenuItem);

		button.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				StampaEtichetteLottiDaAreaMagazzinoCommand.this.execute();
			}
		});
		RcpSupport.configure(this);
		button.setIcon(this.getIcon());
		button.setToolTipText(RcpSupport.getMessage(COMMAND_ID + ".caption"));

		// carico dalle settings il comportamento standard definito dall'utente
		try {
			lottiArticoliStampaEtichette = settingsManager.getUserSettings().getBoolean(
					LOTTI_ARTICOLI_ETICHETTE_SETTINGS);
		} catch (SettingsException e) {
			logger.error("--> errore durante il recupero delle settings per la stampa etichette", e);
			throw new RuntimeException("errore durante il recupero delle settings per la stampa etichette", e);
		}

		lottiArticoliMenuItem.setSelected(lottiArticoliStampaEtichette);

		return button;
	}

	@Override
	protected void doExecuteCommand() {

		if (lottiArticoliMenuItem.isSelected()) {
			AreaMagazzino areaMagazzino = (AreaMagazzino) getParameter(PARAM_AREA_MAGAZZINO, null);

			List<RigaLotto> righe = lottiBD.caricaRigheLotto(areaMagazzino);
			addParameter(PARAM_LIST_RIGHE_MAGAZZINO, righe);
		}

		super.doExecuteCommand();
	}

	@Override
	protected void onButtonAttached(AbstractButton paramButton) {
		paramButton.setName(COMMAND_ID);
		super.onButtonAttached(paramButton);
	}

	/**
	 * @param lottiBD
	 *            The lottiBD to set.
	 */
	public void setLottiBD(ILottiBD lottiBD) {
		this.lottiBD = lottiBD;
	}

	/**
	 * @param settingsManager
	 *            The settingsManager to set.
	 */
	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
}
