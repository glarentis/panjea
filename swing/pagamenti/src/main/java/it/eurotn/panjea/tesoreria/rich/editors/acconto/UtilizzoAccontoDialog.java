package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

public class UtilizzoAccontoDialog extends TitledPageApplicationDialog {

	private final ITesoreriaBD tesoreriaBD;
	private final AreaAcconto areaAcconto;

	private final SettingsManager settingsManager;

	/**
	 * Costruttore.
	 * 
	 * @param tesoreriaBD
	 *            tesoreriaBD
	 * @param areaAcconto
	 *            area acconto
	 */
	public UtilizzoAccontoDialog(final ITesoreriaBD tesoreriaBD, final AreaAcconto areaAcconto) {
		super(new UtilizzoAccontoPage(tesoreriaBD, areaAcconto));
		settingsManager = RcpSupport.getBean("settingManagerLocal");
		setPreferredSize(new Dimension(900, 500));
		this.tesoreriaBD = tesoreriaBD;
		this.areaAcconto = areaAcconto;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new Object[] { getFinishCommand() };
	}

	@Override
	protected String getTitle() {
		return RcpSupport.getMessage(UtilizzoAccontoPage.PAGE_ID + ".title");
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();
		try {
			((UtilizzoAccontoPage) getDialogPage()).restoreState(settingsManager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore durante il recupero delle settings", e);
			throw new RuntimeException("errore durante il recupero delle settings", e);
		}
	}

	@Override
	protected boolean onFinish() {
		UtilizzoAccontoPage page = (UtilizzoAccontoPage) getDialogPage();
		boolean importoAccontoValido = page.isImportoAccontoValido();

		if (importoAccontoValido) {
			List<Pagamento> pagamenti = new ArrayList<Pagamento>();

			for (SituazioneRataUtilizzoAcconto rataUtilizzoAcconto : page.getSituazioneRataUtilizzoAcconto()) {
				if (rataUtilizzoAcconto.getImportoAcconto().compareTo(BigDecimal.ZERO) > 0) {

					Pagamento pagamento = new Pagamento();
					pagamento.setRata(rataUtilizzoAcconto.getSituazioneRata().getRata());
					pagamento.getImporto().setImportoInValuta(rataUtilizzoAcconto.getImportoAcconto());

					pagamenti.add(pagamento);
				}
			}
			if (pagamenti.size() > 0) {
				List<AreaAcconto> acconti = new ArrayList<AreaAcconto>();
				acconti.add(areaAcconto);
				tesoreriaBD.creaPagamentiConAcconto(pagamenti, acconti);
			}
		}
		return importoAccontoValido;
	}

	@Override
	protected void onWindowClosing() {
		super.onWindowClosing();
		try {
			((UtilizzoAccontoPage) getDialogPage()).saveState(settingsManager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore durante il recupero delle settings", e);
			throw new RuntimeException("errore durante il recupero delle settings", e);
		}
	}
}
