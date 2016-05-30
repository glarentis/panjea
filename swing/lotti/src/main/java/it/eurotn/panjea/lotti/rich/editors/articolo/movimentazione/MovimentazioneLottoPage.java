package it.eurotn.panjea.lotti.rich.editors.articolo.movimentazione;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.bd.LottiBD;
import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

public class MovimentazioneLottoPage extends AbstractDialogPage implements Observer {

	public static final String PAGE_ID = "movimentazioneLottoPage";

	private ILottiBD lottiBD;

	private final SettingsManager settingsManager;

	private JideTableWidget<MovimentazioneLotto> tableWidget;
	private JComponent tableComponent;

	private Integer numeroDecimaliQuantita = 2;

	private TipoLotto tipoLotto = TipoLotto.NESSUNO;
	private Lotto lotto;
	private LottoInterno lottoInterno;

	private ActionCommand openDocumentoCommand;

	/**
	 * Costruttore.
	 */
	public MovimentazioneLottoPage() {
		super(PAGE_ID);
		this.lottiBD = RcpSupport.getBean(LottiBD.BEAN_ID);
		settingsManager = RcpSupport.getBean("settingManagerLocal");
	}

	/**
	 * Carica la movimentazione lotti.
	 */
	private void caricaMovimentazioneLotto() {

		List<MovimentazioneLotto> listMovimentazione = null;

		switch (tipoLotto) {
		case LOTTO:
			listMovimentazione = lottiBD.caricaMovimentazioneLotto(lotto);
			break;
		case LOTTO_INTERNO:
			listMovimentazione = lottiBD.caricaMovimentazioneLottoInterno(lottoInterno);
			break;
		default:
			listMovimentazione = Collections.emptyList();
			break;
		}

		tableWidget.setRows(listMovimentazione);
		((MovimentazioneLottoTableModel) TableModelWrapperUtils.getActualTableModel(tableWidget.getTable().getModel()))
				.setNumeroDecimaliQta(numeroDecimaliQuantita);
	}

	@Override
	protected JComponent createControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		MovimentazioneLottoTableModel tableModel = new MovimentazioneLottoTableModel(tipoLotto);
		tableWidget = new JideTableWidget<MovimentazioneLotto>("movimentazioneLottoTable" + tipoLotto.name(),
				tableModel);
		tableWidget.getTable();
		tableWidget.setPropertyCommandExecutor(getOpenDocumentoCommand());
		tableComponent = tableWidget.getComponent();
		rootPanel.add(tableComponent, BorderLayout.CENTER);
		rootPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
				"Movimentazione lotto"));

		return rootPanel;
	}

	/**
	 * 
	 * @return command per aprire il documento.
	 */
	private ActionCommand getOpenDocumentoCommand() {
		if (openDocumentoCommand == null) {
			openDocumentoCommand = new OpenDocumentoCommand();
			openDocumentoCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {

				}

				@Override
				public boolean preExecution(ActionCommand command) {
					command.addParameter(OpenDocumentoCommand.RIGA_MOVIMENTAZIONE_PARAM,
							tableWidget.getSelectedObject());
					return true;
				}
			});

		}
		return openDocumentoCommand;
	}

	/**
	 * Esegue il salvataggio delle impostazioni dalle settigns.
	 * 
	 * @param settings
	 *            settigns
	 */
	public void restoreState(Settings settings) {
		tableWidget.restoreState(settings);
	}

	/**
	 * Esegue il salvataggio delle impostazioni.
	 * 
	 * @param settings
	 *            settings
	 */
	public void saveState(Settings settings) {
		tableWidget.saveState(settings);
	}

	/**
	 * @param numeroDecimaliQuantita
	 *            the numeroDecimaliQuantita to set
	 */
	public void setNumeroDecimaliQuantita(Integer numeroDecimaliQuantita) {
		this.numeroDecimaliQuantita = numeroDecimaliQuantita;
	}

	/**
	 * @param tipoLotto
	 *            the tipoLotto to set
	 */
	public void setTipoLotto(TipoLotto tipoLotto) {
		this.tipoLotto = tipoLotto;

		updateControl();
	}

	@Override
	public void update(Observable o, Object arg) {

		StatisticaLotto statisticaLotto = (StatisticaLotto) arg;

		lotto = statisticaLotto != null ? statisticaLotto.getLotto() : null;
		lottoInterno = statisticaLotto != null ? statisticaLotto.getLottoInterno() : null;
		caricaMovimentazioneLotto();
	}

	/**
	 * Aggiorna i controlli.
	 */
	private void updateControl() {
		try {
			tableWidget.saveState(settingsManager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore durante il salvataggio delle settings", e);
			throw new RuntimeException("errore durante il salvataggio delle settings", e);
		}

		MovimentazioneLottoTableModel tableModel = new MovimentazioneLottoTableModel(tipoLotto);
		tableWidget = new JideTableWidget<MovimentazioneLotto>("movimentazioneLottoTable" + tipoLotto.name(),
				tableModel);
		tableWidget.setPropertyCommandExecutor(getOpenDocumentoCommand());
		tableComponent.removeAll();
		tableComponent.add(tableWidget.getComponent());

		try {
			tableWidget.restoreState(settingsManager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore durante il recupero delle settings", e);
			throw new RuntimeException("errore durante il recupero delle settings", e);
		}

		caricaMovimentazioneLotto();
	}
}
