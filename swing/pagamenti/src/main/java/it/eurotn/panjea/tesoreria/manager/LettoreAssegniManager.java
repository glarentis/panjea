package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.preferences.GeneralSettingsPM;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.tesoreria.rich.commands.OpenPreviewAssegnoCommand;
import it.eurotn.panjea.tesoreria.util.ImmagineAssegno;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.rules.closure.Closure;

import bancor.K2s.K2sJ;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.spring.richclient.docking.editor.WorkspaceView;

public class LettoreAssegniManager implements InitializingBean {

	private class TimerLetturaActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			timerScan.stop();
			try {
				pool();
			} finally {
				timerScan.start();
			}
		}
	}

	private static Logger logger = Logger.getLogger(LettoreAssegniManager.class);

	public static final String K2SJ_DLL_NAME = "K2sJ.dll";
	public static final String K2SJ_JAR_NAME = "K2sJ.jar";

	public static final String ENABLE_LETTORE_ASSEGNI_EVENT = "enableLettoreAssegniEvent";
	public static final String DISABLE_LETTORE_ASSEGNI_EVENT = "disableLettoreAssegniEvent";
	public static final String LETTORE_ASSEGNI_NOT_INITIALIZED = "lettoreAssegniNotInitialized";
	public static final String LETTORE_ASSEGNI_INITIALIZED = "lettoreAssegniInitialized";
	public static final String LETTORE_ASSEGNI_NOT_CALIBRATED = "lettoreAssegniNotCalibrated";
	public static final String LETTORE_ASSEGNI_DOCUMENT_NOT_INSERTES = "lettoreAssegniDocumentNotInserted";
	public static final String LETTORE_ASSEGNI_SCAN_ERROR = "lettoreAssegniScanError";

	private K2sJ lettoreAssegniK2sJ = null;

	private Timer timerScan;

	private String errMsg;

	private SettingsManager settingsManager;

	private IPartiteBD partiteBD;

	private TipoAreaPartita tipoAreaPartitaAssegno = null;

	private String abi;
	private String cab;
	private String numeroAssegno;

	private String tmpDir = System.getProperty("java.io.tmpdir");

	private boolean lettoreInitialized = false;

	private WorkspaceView workspaceView;

	@Override
	public void afterPropertiesSet() throws Exception {

		Boolean letturaAssegniEnable = settingsManager.getUserSettings().getBoolean(GeneralSettingsPM.LETTURA_ASSEGNI);

		fireApplicationEvent((letturaAssegniEnable) ? ENABLE_LETTORE_ASSEGNI_EVENT : DISABLE_LETTORE_ASSEGNI_EVENT,
				this);

		if (letturaAssegniEnable) {
			start();
		}
	}

	/**
	 * Data l'URL del jar che contiene la dll, questa viene scaricata e salvata nella directory dell'utente.
	 *
	 * @param jarK2SJUrl
	 *            URL del jar che contiene la dll
	 */
	private void downloadAndStoreDll(URL jarK2SJUrl) {
		try {
			// apro il jar contenente la dll e decomprimo quest'ultima nella directory .panjea dell'utente.
			InputStream is = jarK2SJUrl.openStream();

			byte[] buf = new byte[1024];
			ZipInputStream zip = new ZipInputStream(is);
			ZipEntry ze = null;

			while ((ze = zip.getNextEntry()) != null) {
				String entryName = ze.getName();
				// trovata la dll la decomprimo
				if (entryName.equals(K2SJ_DLL_NAME)) {
					int n;
					FileOutputStream fileoutputstream;
					fileoutputstream = new FileOutputStream(PanjeaSwingUtil.getHome().resolve(entryName).toFile());

					while ((n = zip.read(buf, 0, 1024)) > -1) {
						fileoutputstream.write(buf, 0, n);
					}

					fileoutputstream.close();
					zip.closeEntry();
				}
			}
		} catch (IOException e) {
			logger.error("Errore durante il salvataggio della dll nella directory dell'utente.", e);
			e.printStackTrace();
		}
	}

	/**
	 * Lancia un'evento all'applicazione con nome e valore specificato.
	 *
	 * @param eventName
	 *            nome dell'evento
	 * @param eventValue
	 *            valore dell'evento
	 */
	private void fireApplicationEvent(String eventName, Object eventValue) {
		LifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(eventName, eventValue);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @return the tipoAreaPartitaAssegno
	 */
	public TipoAreaPartita getTipoAreaPartitaAssegno() {
		if (tipoAreaPartitaAssegno == null) {
			// carico i tipi area partita per pagamenti e prendo la prima che ha come tipo operazione GESTIONE_ASSEGNO
			List<TipoAreaPartita> tipiAreePartita = partiteBD.caricaTipiAreaPartitaPerPagamenti("codice", null,
					TipoPartita.ATTIVA, false, true);

			for (TipoAreaPartita tipoAreaPartita : tipiAreePartita) {
				if (TipoOperazione.GESTIONE_ASSEGNO == tipoAreaPartita.getTipoOperazione()) {
					tipoAreaPartitaAssegno = tipoAreaPartita;
					break;
				}
			}
		}

		return tipoAreaPartitaAssegno;
	}

	/**
	 * @return the workspaceView
	 */
	public WorkspaceView getWorkspaceView() {
		if (workspaceView == null) {
			workspaceView = Application.instance().getWindowManager().getActiveWindow().getPage()
					.getView("workspaceView");
		}
		return workspaceView;
	}

	/**
	 * Esegue la init della class K2sJ caricando la dll nel modo appropriato.
	 *
	 * @return <code>true</code> se l'init è andata a buon fine
	 */
	private boolean initializeK2SJ() {

		// try {
		// Path dllPath = PanjeaSwingUtil.getHome().resolve(K2SJ_DLL_NAME);
		//
		// // Carico la dll a seconda che mi trovi in webstart o lanciato da eclipse.
		// try {
		// BasicService jnlpBasicService = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
		// String panjeaJnlpAddress = jnlpBasicService.getCodeBase().toExternalForm();
		// panjeaJnlpAddress = panjeaJnlpAddress.concat(K2SJ_JAR_NAME);
		//
		// // controllo se la dll esiste altrimenti la scarico
		// if (!Files.exists(dllPath)) {
		// try {
		// // leggo la dll e la salvo nella cartella dell'utente.
		// URL dllUrl = new URL(panjeaJnlpAddress);
		// downloadAndStoreDll(dllUrl);
		// } catch (MalformedURLException e1) {
		// logger.error("Errore nel creare l'url per l'indirizzo " + panjeaJnlpAddress, e1);
		// throw new RuntimeException("Errore nel creare l'url per l'indirizzo " + panjeaJnlpAddress, e1);
		// }
		// }
		// } catch (UnavailableServiceException e) {
		// // in questo caso mi cerco nel classpath dove si trova il jar di K2sJ e sostituisco l'estenzione jar con
		// // dll
		// // per ottenere il percorso corretto della libreria.
		// URL urlJar = K2sJ.class.getResource("K2sJ.class");
		// dllPath = urlJar.getFile().split("!")[0];
		// dllPath = dllPath.replace("file:/", "");
		// dllPath = dllPath.replace(K2SJ_JAR_NAME, K2SJ_DLL_NAME);
		// }
		// lettoreAssegniK2sJ = new K2sJ(dllPath);
		// return true;
		// } catch (Exception e) {
		// return false;
		// }
		return false;
	}

	/**
	 * Esegue i controlli necessari e l'eventuale scan del documento.
	 */
	public void pool() {

		if (!lettoreInitialized) {
			lettoreInitialized = initializeK2SJ();
			return;
		}

		try {

			if (!lettoreAssegniK2sJ.Init()) {
				errMsg = lettoreAssegniK2sJ.ErrorMsg();
				fireApplicationEvent(LETTORE_ASSEGNI_NOT_INITIALIZED, errMsg);
				return;
			}

			fireApplicationEvent(LETTORE_ASSEGNI_INITIALIZED, this);

			if (!lettoreAssegniK2sJ.IsCalibrated()) {
				fireApplicationEvent(LETTORE_ASSEGNI_NOT_CALIBRATED, this);
				return;
			}

			if (!lettoreAssegniK2sJ.DocInserted()) {
				fireApplicationEvent(LETTORE_ASSEGNI_DOCUMENT_NOT_INSERTES, this);
				return;
			}

			lettoreAssegniK2sJ.SetColor(K2sJ.C_RGB);
			lettoreAssegniK2sJ.SetDpi(100, 100);
			lettoreAssegniK2sJ.OCRZoneClear();
			if (!lettoreAssegniK2sJ.OCRZoneAdd(150, K2sJ.SIDE_FRONT, -1028, -66, 449, 39, K2sJ.OCR_CHAR_CMC7, false)) {
				return;
			}

			if (!lettoreAssegniK2sJ.Scan()) {
				errMsg = lettoreAssegniK2sJ.ErrorMsg();
				fireApplicationEvent(LETTORE_ASSEGNI_SCAN_ERROR, errMsg);
				return;
			}

			// leggo la zona ocr per avere numero assegno abi e cab
			numeroAssegno = "";
			abi = "";
			cab = "";
			int i;
			for (i = 0; i < lettoreAssegniK2sJ.OCRNZone(); i++) {
				String zoneOCR = lettoreAssegniK2sJ.OCRCodeline(i);
				if (zoneOCR.length() == 22) {
					// zona composta da: carattere di controllo - num assegno - carattere di controllo - abicab -
					// carattere di controllo
					numeroAssegno = zoneOCR.substring(1, 11);
					abi = "0" + zoneOCR.substring(12, 16);
					cab = zoneOCR.substring(16, 21);
				} else {
					logger.error("Zona OCR non corretta: " + zoneOCR);
				}
			}
			lettoreAssegniK2sJ.OCRFree();

			lettoreAssegniK2sJ.SaveJPEG(K2sJ.SIDE_FRONT, 60, tmpDir + File.separator + "Front.jpg");
			lettoreAssegniK2sJ.SaveJPEG(K2sJ.SIDE_REAR, 60, tmpDir + File.separator + "Rear.jpg");
			lettoreAssegniK2sJ.ImgFree();
		} finally {
			lettoreAssegniK2sJ.Close();
		}

		Boolean foundImages = false;

		File fileFront = new File(tmpDir + File.separator + "Front.jpg");
		File fileRear = new File(tmpDir + File.separator + "Rear.jpg");

		foundImages = fileFront.exists() && fileRear.exists();

		if (foundImages) {

			final ImmagineAssegno immagineAssegno = new ImmagineAssegno(fileFront, fileRear);
			fileFront.delete();
			fileRear.delete();

			if (getTipoAreaPartitaAssegno() == null) {
				throw new RuntimeException(new TipoDocumentoBaseException(
						new String[] { TipoOperazione.GESTIONE_ASSEGNO.name() }));
			}

			Closure finishClosure = new Closure() {

				@Override
				public Object call(Object obj) {

					PageComponent[] pages = getWorkspaceView().getPageComponent(ParametriRicercaRate.class);

					// se ho gi� un editor aperto prendo l'oggetto che sta gestendo e gli aggiorno solo i parametri
					// creazione area chiusure, altrimenti creo nuovi tutti i parametri
					ParametriRicercaRate parametriRicercaRate;
					if (pages.length > 0) {
						parametriRicercaRate = (ParametriRicercaRate) ((AbstractEditor) pages[0]).getEditorInput();
					} else {
						parametriRicercaRate = new ParametriRicercaRate();
						parametriRicercaRate.setEffettuaRicerca(false);
					}

					ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = new ParametriCreazioneAreaChiusure();
					parametriCreazioneAreaChiusure.setAbi(abi);
					parametriCreazioneAreaChiusure.setCab(cab);
					parametriCreazioneAreaChiusure.setNumeroAssegno(numeroAssegno);
					parametriCreazioneAreaChiusure.setImmagine(immagineAssegno);
					parametriCreazioneAreaChiusure.setTipoAreaPartita(tipoAreaPartitaAssegno);
					parametriRicercaRate.setParametriCreazioneAreaChiusure(parametriCreazioneAreaChiusure);

					LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaRate);
					Application.instance().getApplicationContext().publishEvent(event);
					return null;
				}
			};

			OpenPreviewAssegnoCommand previewAssegnoCommand = new OpenPreviewAssegnoCommand(finishClosure);
			previewAssegnoCommand.addParameter(OpenPreviewAssegnoCommand.IMMAGINE_ASSEGNO_PARAMETER, immagineAssegno);
			previewAssegnoCommand.execute();

		}
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

	/**
	 * @param settingsManager
	 *            the settingsManager to set
	 */
	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	/**
	 * Lancia l'evento di abilitazione del controllo del lettore assegni e fa partire il timer per il monitoraggio dei
	 * documenti da digitalizzare.
	 */
	public void start() {

		fireApplicationEvent(ENABLE_LETTORE_ASSEGNI_EVENT, true);

		timerScan = new Timer(2000, new TimerLetturaActionListener());
		timerScan.start();
	}

	/**
	 * Ferma il timer di scansione del lettore e lancia il messaggio di stop all'applicazione.
	 */
	public void stop() {

		timerScan.stop();

		fireApplicationEvent(ENABLE_LETTORE_ASSEGNI_EVENT, false);
	}
}
