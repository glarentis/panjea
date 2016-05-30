package it.eurotn.panjea.fornodoro.sync;

import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.exception.FileSemaforoPresente;
import it.eurotn.panjea.fornodoro.FornoDOroCostanti;
import it.eurotn.panjea.fornodoro.importazione.AbstractPreferenceSettings;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

public class FornodoroDistintaCaricoSyncInterceptor extends AbstractPreferenceSettings {

	private static Logger logger = Logger.getLogger(FornodoroDistintaCaricoSyncInterceptor.class);

	@Resource
	protected SessionContext sessionContext;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private PreferenceService preferenceService;

	@EJB
	private ArticoloManager articoloManager;

	/**
	 * Genera il file semaforo. Rilancia un errore se il file semaforo esiste.
	 *
	 * @param pathFile
	 *            path del file semaforo
	 * @throws FileSemaforoPresente
	 *             rilanciato se il file semaforo esiste già
	 */
	private void creaFileSemaforo(String pathFile) throws FileSemaforoPresente {
		File file = new File(pathFile);
		if (file.exists()) {
			sessionContext.setRollbackOnly();
			throw new FileSemaforoPresente(pathFile);
		}
		FileWriter outFile = null;
		try {
			outFile = new FileWriter(file);
			PrintWriter out = new PrintWriter(outFile);
			out.print("SEMAFORO");
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("-->errore nello scrivere il file semaforo " + pathFile, e);
		}
	}

	/**
	 * Esporta le distinte di carico a Gulliver.
	 *
	 * @param ctx
	 *            context dell'interceptor
	 * @return risultato della catena degli interceptor
	 * @throws Exception
	 *             eccezione generica
	 */
	@AroundInvoke
	public Object esporta(InvocationContext ctx) throws Exception {
		@SuppressWarnings("unchecked")
		List<DistintaCarico> distinteSelezionate = (List<DistintaCarico>) ctx.proceed();

		List<RigaDistintaCarico> righeDaEsportare = new ArrayList<RigaDistintaCarico>();
		for (DistintaCarico distinta : distinteSelezionate) {
			righeDaEsportare.addAll(distinta.getRigheEvasione());
		}

		if (righeDaEsportare.size() == 0) {
			return distinteSelezionate;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Inizio ad esportare le distinte. Righe da esportare: " + righeDaEsportare.size());
		}
		StreamFactory factory = getStreamFactory("DISTINTETEMPLATE.xml");
		if (factory == null) {
			throw new IllegalArgumentException(
					"Errore nel cercare il template DISTINTA+EXPORT.XML di cosaro per l'esportazione");
		}
		String filePath = caricaPreference("fornodoroFileExport");
		if (filePath == null) {
			logger.debug("--> chiave nelle preference fornodoroFileExport non trovato.");
			sessionContext.setRollbackOnly();
			return null;
		}

		logger.debug("--> esporto i dati delle bilance nel file " + filePath);

		creaFileSemaforo(filePath.replace("txt", "sem"));

		BeanWriter out = null;
		try {
			out = factory.createWriter("distinta", new File(filePath));
			Map<String, Articolo> articoliCaricati = new HashMap<String, Articolo>();
			for (RigaDistintaCarico rigaDistintaCarico : righeDaEsportare) {
				if (!articoliCaricati.containsKey(rigaDistintaCarico.getArticolo().getCodice())) {
					Articolo articolo = articoloManager.caricaArticolo(rigaDistintaCarico.getArticolo()
							.creaProxyArticolo(), false);
					articolo.getAttributiArticolo().size();
					articoliCaricati.put(articolo.getCodice(), articolo);
				}

				// per gli articoli a peso variabile ( quelli con u.m. in KG ) devo forzare l'unità di misura in "PZ" e prendere come valore della quantità quello dell'attributo "pezzi".
				// per gli articoli a peso fisso ( quelli con u.m. diversa da KG ) devo forzare l'unità di misura in "CT" e prendere come valore della quantità con quello dell'attributi "colli".
				String um = "";
				Double qta = rigaDistintaCarico.getQtaDaEvadere();
				if("KG".equals(rigaDistintaCarico.getArticolo().getUnitaMisura().getCodice())) {
					um = "PZ";
					qta = trasformaQtaDaAttributo(rigaDistintaCarico.getRigaArticolo(), FornoDOroCostanti.PEZZI_ATTRIBUTO);
				} else {
					um = "CT";
					qta = trasformaQtaDaAttributo(rigaDistintaCarico.getRigaArticolo(), FornoDOroCostanti.COLLI_ATTRIBUTO);
				}

				RigaOrdineBilance rigaOrdineBilance = new RigaOrdineBilance(rigaDistintaCarico,
						articoliCaricati.get(rigaDistintaCarico.getArticolo().getCodice()), um);
				rigaOrdineBilance.setQtaDaEvadere(qta);
				out.write(rigaOrdineBilance);
			}
		} catch (Exception ex) {
			panjeaMessage.send("Errore nel generare il file per le bilance" + ex.getMessage(),
					new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			sessionContext.setRollbackOnly();
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		panjeaMessage.send("Spedite " + righeDaEsportare.size() + " righe alla bilancia", new String[] { "evasione" },
				8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		return distinteSelezionate;
	}

	/**
	 *
	 * @param rigaArticolo
	 *            riga dell'ordine associata
	 * @param codiceAttributo
	 *            codiceAttributo contenente la qta
	 * @return qta presente nell'attributo della riga
	 */
	private Double trasformaQtaDaAttributo(RigaArticolo rigaArticolo, String codiceAttributo) {
		logger.debug("--> Enter trasformaQtaDaAttributo");
		Double qta = 0.0;
		NumberFormat nb = NumberFormat.getInstance(Locale.ITALIAN);
		for (AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
			if (attributoRiga.getTipoAttributo().getCodice().equals(codiceAttributo)) {
				try {
					qta = nb.parse(attributoRiga.getValore() == null ? "0,0" : attributoRiga.getValore()).doubleValue();
				} catch (ParseException nfe) {
					logger.error("-->errore nel recuperare la qta dall'attributo " + codiceAttributo, nfe);
				} catch (NullPointerException np) {
					logger.error("-->errore nel recuperare la qta dall'attributo " + codiceAttributo, np);
				}
				break;
			}
		}
		logger.debug("--> Exit trasformaQtaDaAttributo con qta " + qta + " per l'attributo " + codiceAttributo);
		return qta;
	}
}
