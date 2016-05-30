package it.eurotn.panjea.tesoreria.manager.flussocbi;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.log4j.Logger;

public abstract class AbstractFlussoExporter {

	private static Logger logger = Logger.getLogger(AbstractFlussoExporter.class.getName());
	private File file;
	private Writer writer;
	protected int nRecords;
	private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

	/**
	 * Crea la descrizione di esportazione dei pagamenti in base alla maschera definita sul tipo area partita.
	 * 
	 * @param pagamenti
	 *            pagamenti da valorizzare
	 * @param areaMagazzinoManager
	 *            areaMagazzinoManager
	 * @return descrizione creata
	 */
	protected final String creaDescrizioneDaMascheraFlusso(final Set<Pagamento> pagamenti,
			final AreaMagazzinoManager areaMagazzinoManager) {
		logger.debug("--> Enter valorizzaRiferimenti");
		String riferimenti = "";
		for (Pagamento pagamentoCorrente : pagamenti) {
			String kTemp = pagamentoCorrente.getRata().getAreaRate().getTipoAreaPartita().getMascheraFlussoBanca() == null ? ""
					: pagamentoCorrente.getRata().getAreaRate().getTipoAreaPartita().getMascheraFlussoBanca();

			// recupero e sostituisco i dati necesari
			String codiceTipoDocumento = pagamentoCorrente.getRata().getAreaRate().getDocumento().getTipoDocumento()
					.getCodice().toString();
			// i codici utilizati fanno referenza a i codici in tipoAreaPartitaForm
			kTemp = kTemp.replace("$codiceTipoDocumento$", codiceTipoDocumento);
			String descrizioneTipoDocumento = pagamentoCorrente.getRata().getAreaRate().getDocumento()
					.getTipoDocumento().getDescrizione();
			kTemp = kTemp.replace("$descrizioneTipoDocumento$", descrizioneTipoDocumento);
			String nRata = pagamentoCorrente.getRata().getNumeroRata().toString();
			kTemp = kTemp.replace("$numeroRata$", nRata);
			String data = format.format(pagamentoCorrente.getRata().getAreaRate().getDocumento().getDataDocumento());
			String num = pagamentoCorrente.getRata().getAreaRate().getDocumento().getCodice().getCodice();
			kTemp = kTemp.replace("$dataDocumento$", data);
			kTemp = kTemp.replace("$numeroDocumento$", num);

			AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(pagamentoCorrente
					.getRata().getAreaRate().getDocumento());
			if (areaMagazzino != null) {

				String cig = areaMagazzino.getDocumento().getSedeEntita().getCig();
				kTemp = kTemp.replace("$CIG$", cig == null ? "" : "CIG: " + cig);

				String cup = areaMagazzino.getDocumento().getSedeEntita().getCup();
				kTemp = kTemp.replace("$CUP$", cup == null ? "" : "CUP: " + cup);
			}
			// aggiunge una vigola per separare un pagamento del altro
			if (riferimenti != "") {
				riferimenti = riferimenti + ", ";
			}
			riferimenti = riferimenti + kTemp;
		}

		logger.debug("--> Exit valorizzaRiferimenti");
		return riferimenti;
	}

	/**
	 * 
	 * @param areaChiusure
	 *            area da esportare
	 * @throws RapportoBancarioPerFlussoAssenteException
	 *             lanciata quando ci sono dei rapporti bancari entità assenti sulla rata.
	 */
	protected abstract void creaFlusso(AreaChiusure areaChiusure) throws RapportoBancarioPerFlussoAssenteException;

	/**
	 * Crea un file in locale e ritorna il path del file creato.
	 * 
	 * @param areaChiusure
	 *            area da esportare
	 * @return path del file creato sul server per poterlo recuperare in un secondo momento
	 * @throws RapportoBancarioPerFlussoAssenteException
	 *             lanciata quando ci sono dei rapporti bancari entità assenti sulla rata
	 */
	public String esportaFlusso(AreaChiusure areaChiusure) throws RapportoBancarioPerFlussoAssenteException {
		logger.debug("--> Enter esportaFlusso");
		fileTempCrea();
		creaFlusso(areaChiusure);
		fileTempClose();
		logger.debug("--> Exit esportaFlusso");
		return file.getAbsolutePath();
	}

	/**
	 * Chiude il file temporaneo.
	 */
	private void fileTempClose() {
		logger.debug("--> Enter fileTempClose");
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit fileTempClose");
	}

	/**
	 * Crea il file temporaneo da esportare sul client. Metodo di trasmissione da definire
	 */
	private void fileTempCrea() {
		logger.debug("--> Enter fileTempCrea");
		try {
			file = File.createTempFile("FlussoCBI", ".txt");
			logger.debug("--> CREATO il FILE " + file.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit fileTempCrea");
	}

	/**
	 * Scrive una riga nel file.
	 * 
	 * @param line
	 *            linea da scrivere
	 */
	protected final void fileWriteLine(final String line) {
		logger.debug("--> Enter fileWriteLine");
		logger.debug("--> SCRIVO " + line);
		try {
			nRecords++;
			writer.write(line + "\n");
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit fileWriteLine");
	}
}
