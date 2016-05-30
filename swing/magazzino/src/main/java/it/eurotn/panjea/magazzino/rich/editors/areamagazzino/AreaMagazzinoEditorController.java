package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.anagrafica.rich.commands.documento.DocumentiDocumentoCommand;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.iva.rich.editors.righeiva.RigheIvaTablePage;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.RigheMagazzinoTablePage;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

/**
 * La classe gestisce l'iterazione fra le varie pagine dell'editor (e della compositePage).<br/>
 *
 * @author giangi
 *
 */
public class AreaMagazzinoEditorController {
	/**
	 * PropertyChange chiamato quando la parte iva viene validata.
	 */
	private class AreaIvaValidataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (RigheIvaTablePage.VALIDA_AREA_IVA.equals(evt.getPropertyName())) {
				compositePage.setActivePage(areaMagazzinoPage);
				areaMagazzinoPage.setTabForm(3);
			}
		}
	}

	/**
	 * PropertyChange chiamato quando la testata (area contabile) viene validata, in questo caso quando viene salvata.
	 */
	private class AreaMagazzinoValidataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (AreaMagazzinoPage.VALIDA_AREA_MAGAZZINO.equals(evt.getPropertyName())) {
				AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) areaMagazzinoPage.getForm()
						.getFormObject();
				if (areaMagazzinoFullDTO.getVersion() != null && areaMagazzinoFullDTO.getVersion() == 0) {
					compositePage.setActivePage(righeMagazzinoTablePage);
					righeMagazzinoTablePage.getEditFrame().setCurrentPage(new RigaArticoloDTO());
					righeMagazzinoTablePage.getEditFrame().getQuickInsertCommand().setSelected(true);

				}
			}
		}
	}

	/**
	 * PropertyChange chiamato quando la parte partita viene validata.
	 */
	private class AreaPartitaValidataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (VALIDA_AREA_RATE.equals(evt.getPropertyName())) {
				compositePage.setActivePage(areaMagazzinoPage);
				areaMagazzinoPage.setTabForm(3);
			}
		}
	}

	private class RigheMagazzinoRefreshListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			compositePage.setActivePage(areaMagazzinoPage);
			areaMagazzinoPage.setTabForm(0);
		}

	}

	public class RigheMagazzinoValidataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (RigheMagazzinoTablePage.VALIDA_RIGHE_MAGAZZINO.equals(evt.getPropertyName())) {
				// Se ho pluginIntra visualizzo l'area Intra
				AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) areaMagazzinoPage.getForm()
						.getFormObject();

				PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
				if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
					AziendaCorrente azienda = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
					if (areaMagazzinoFullDTO.getAreaMagazzino().getDocumento()
							.isAreaIntraAbilitata(azienda.getNazione().getCodice())) {
						ActionCommand intraCommand = (ActionCommand) Application.instance().getActiveWindow()
								.getCommandManager().getCommand("areaIntraCommand");
						intraCommand.addParameter("documento", areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
						intraCommand.execute();
					}
				}

				if (pluginManager.isPresente(PluginManager.PLUGIN_FATTURAZIONE_PA)) {
					ActionCommand fatturaPACommand = RcpSupport.getCommand("openAreaFatturaElettronicaTypeCommand");
					fatturaPACommand.addParameter("paramAreaMagazzino", areaMagazzinoFullDTO.getAreaMagazzino());
					fatturaPACommand.execute();
				}

				// se il tipo documento prevede la richiesta del documento collegato e la sto inserendo e non
				// modificando apro il dialogo per laq selezione
				boolean richiestaDocCollegato = areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino()
						.getTipoDocumento().isRichiediDocumentoCollegato();
				Integer versioneAM = areaMagazzinoFullDTO.getAreaMagazzino().getVersion();
				if (richiestaDocCollegato && versioneAM != null && versioneAM == 1) {
					DocumentiDocumentoCommand command = new DocumentiDocumentoCommand();
					command.setDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
					command.execute();
				}

				boolean creaNuovaArea = righeMagazzinoTablePage.getAzioneDopoConfermaCommand().isSelected();
				if (creaNuovaArea && evt.getOldValue() != null) {
					areaMagazzinoPage.getEditorNewCommand().execute();
				} else {
					areaMagazzinoPage.setTabForm(3);
				}
			}
		}
	}

	/**
	 * PropertyChange chiamato quando vengono totalizzate le righe magazzino.
	 */
	private class TotalizzazioneRigheMagazzinoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (RigheMagazzinoTablePage.TOTALIZZAZIONE_RIGHE_MAGAZZINO.equals(evt.getPropertyName())) {
				areaMagazzinoPage.setTabForm(3);
			}
		}
	}

	// L'area partite potre non averla nel classPath. Devo ridefinirmi le
	// costanti per non avere una classNotFound
	private static final String RATE_PAGE_ID = "rateTablePage";
	private static final String VALIDA_AREA_RATE = "validaAreaRate";

	private RigheIvaTablePage righeIvaTablePage = null;
	private RigheMagazzinoTablePage righeMagazzinoTablePage = null;
	private AreaMagazzinoPage areaMagazzinoPage = null;
	private AbstractTreeTableDialogPageEditor areaRatePage = null;
	private PropertyChangeListener areaIvaValidaChangeListener = null;
	private PropertyChangeListener areaMagazzinoValidaChangeListener = null;
	private PropertyChangeListener areaRateValidaChangeListener = null;
	private PropertyChangeListener righeMagazzinoValidaChangeListener = null;
	private PropertyChangeListener totalizzazioneRigheMagazzinoChangeListener = null;
	private PropertyChangeListener righeMagazzinoRefreshOnNewAreaMagazzinoListener = null;
	private JecCompositeDialogPage compositePage = null;

	/**
	 *
	 * Costruttore.
	 *
	 * @param compositePage
	 *            composite page
	 * @param settingsManager
	 *            {@link SettingsManager}
	 */
	public AreaMagazzinoEditorController(final JecCompositeDialogPage compositePage,
			final SettingsManager settingsManager) {
		areaIvaValidaChangeListener = new AreaIvaValidataChangeListener();
		areaMagazzinoValidaChangeListener = new AreaMagazzinoValidataChangeListener();
		areaRateValidaChangeListener = new AreaPartitaValidataChangeListener();
		righeMagazzinoValidaChangeListener = new RigheMagazzinoValidataChangeListener();
		totalizzazioneRigheMagazzinoChangeListener = new TotalizzazioneRigheMagazzinoChangeListener();
		righeMagazzinoRefreshOnNewAreaMagazzinoListener = new RigheMagazzinoRefreshListener();
		this.compositePage = compositePage;
	}

	/**
	 * Aggiunge una pagina.
	 *
	 * @param page
	 *            pagina da aggiungere
	 */
	public void addPage(DialogPage page) {
		if (AreaMagazzinoPage.PAGE_ID.equals(page.getId())) {
			areaMagazzinoPage = (AreaMagazzinoPage) page;
			areaMagazzinoPage.addPropertyChangeListener(AreaMagazzinoPage.VALIDA_AREA_MAGAZZINO,
					areaMagazzinoValidaChangeListener);

		} else if (RigheMagazzinoTablePage.PAGE_ID.equals(page.getId())) {
			righeMagazzinoTablePage = (RigheMagazzinoTablePage) page;
			righeMagazzinoTablePage.addPropertyChangeListener(RigheMagazzinoTablePage.VALIDA_RIGHE_MAGAZZINO,
					righeMagazzinoValidaChangeListener);
			righeMagazzinoTablePage.addPropertyChangeListener(RigheMagazzinoTablePage.TOTALIZZAZIONE_RIGHE_MAGAZZINO,
					totalizzazioneRigheMagazzinoChangeListener);
			righeMagazzinoTablePage.addPropertyChangeListener(
					RigheMagazzinoTablePage.REFRESH_RIGHE_ON_NEW_AREA_MAGAZZINO,
					righeMagazzinoRefreshOnNewAreaMagazzinoListener);
		} else if (RigheIvaTablePage.PAGE_ID.equals(page.getId())) {
			righeIvaTablePage = (RigheIvaTablePage) page;
			areaIvaValidaChangeListener = new AreaIvaValidataChangeListener();
			righeIvaTablePage.addPropertyChangeListener(RigheIvaTablePage.VALIDA_AREA_IVA, areaIvaValidaChangeListener);
		} else if (RATE_PAGE_ID.equals(page.getId())) {
			areaRatePage = (AbstractTreeTableDialogPageEditor) page;
			areaRatePage.addPropertyChangeListener(VALIDA_AREA_RATE, areaRateValidaChangeListener);
		}

	}

	/**
	 * Esegue la dispose del controller.
	 */
	public void dispose() {
		righeIvaTablePage.removePropertyChangeListener(areaIvaValidaChangeListener);
		areaMagazzinoPage.removePropertyChangeListener(areaMagazzinoValidaChangeListener);
		areaRatePage.removePropertyChangeListener(areaRateValidaChangeListener);
		righeMagazzinoTablePage.removePropertyChangeListener(righeMagazzinoValidaChangeListener);
		righeMagazzinoTablePage.removePropertyChangeListener(totalizzazioneRigheMagazzinoChangeListener);
		righeMagazzinoTablePage.removePropertyChangeListener(righeMagazzinoRefreshOnNewAreaMagazzinoListener);

		areaIvaValidaChangeListener = null;
		areaMagazzinoValidaChangeListener = null;
		areaRateValidaChangeListener = null;
		righeMagazzinoValidaChangeListener = null;
		totalizzazioneRigheMagazzinoChangeListener = null;
		righeMagazzinoRefreshOnNewAreaMagazzinoListener = null;

		areaMagazzinoPage = null;
		areaRatePage = null;
		compositePage = null;
		righeIvaTablePage = null;
		righeMagazzinoTablePage = null;
	}
}
