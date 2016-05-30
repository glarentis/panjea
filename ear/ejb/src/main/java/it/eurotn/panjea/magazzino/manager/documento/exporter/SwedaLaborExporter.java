package it.eurotn.panjea.magazzino.manager.documento.exporter;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Esportazione del file per lo scontrino per il modello di cassa SWEDA.
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(mappedName = "Panjea.ESWEDA")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SwedaLabor")
public class SwedaLaborExporter implements IExporter {

	private static Logger logger = Logger.getLogger(SwedaLaborExporter.class);
	public static final String JNDI_NAME = "Panjea.SwedaLabor";

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext sessionContext;

	/**
	 * @uml.property name="rigaMagazzinoManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected RigaMagazzinoManager rigaMagazzinoManager;
	private static String spazi = new String("                    ");

	/**
	 * Carica dalle impostazione il valore della chiave "documentoExporter" che contiene il percorso e nome del file su
	 * cui si dovrà esportare il documento.
	 * 
	 * @return percorso e nome del file di esportazione
	 */
	private String caricaPathFileExport() {
		logger.debug("--> Enter caricaPathFileExport");
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
		query.setParameter("paramChiave", "documentoExporter");
		Preference preference = null;
		try {
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key documentoExporter", e);
			throw new RuntimeException("Errore ricerca preference con key documentoExporter", e);
		}

		return preference.getValore();
	}

	@Override
	public void esporta(AreaMagazzino areaMagazzino) throws EsportaDocumentoCassaException {

		// carico il percorso e nome del file di esportazione
		String fileExportName = caricaPathFileExport();

		// il nome del file è xxxx$.xxx quindi sostituisco il carattere $ con
		// l'utente loggato
		fileExportName = fileExportName.replace("$", getJecPrincipal().getUserName());

		StringBuffer rigaEsportaCassa = new StringBuffer();

		List<RigaArticolo> righeArticolo = rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);
		rigaEsportaCassa.append("KXCL" + "\r\n");
		rigaEsportaCassa.append("KXCL" + "\r\n");
		rigaEsportaCassa.append("KXAT" + "\r\n");
		for (RigaArticolo rigaArticolo : righeArticolo) {
			// descrizione articolo ( max 16 caratteri )
			String descrizioneArticolo = rigaArticolo.getArticolo().getDescrizione().trim();
			descrizioneArticolo = descrizioneArticolo + spazi;
			descrizioneArticolo = descrizioneArticolo.substring(0, 16);

			String quantita = new DecimalFormat("#0.00").format(rigaArticolo.getQta());
			quantita = quantita.replace(".", ",");
			// formato la quantita del articollo
			if (rigaArticolo.getQta() != 1) {
				String[] quantitaParts = quantita.split("\\,");
				if (quantitaParts[0].length() > 6) {
					throw new EsportaDocumentoCassaException(
							"La parte intera della quantità non può superare le 6 cifre", descrizioneArticolo);
				}
				// if (quantitaParts[0].equals("000000")) {
				// quantitaParts[0] = "0";
				// }
				// prendo la parte decimale della quantita(quantitaParts[1])
				// controllo che sia diversa di zero
				// secondo sia il caso aggiungo la parte decimale o meno
				if (Integer.parseInt(quantitaParts[1]) != 0) {
					rigaEsportaCassa.append("KX" + quantitaParts[0] + "." + "\r\n");
					rigaEsportaCassa.append("KX" + quantitaParts[1] + "*" + "\r\n");
				} else {
					rigaEsportaCassa.append("KX" + quantitaParts[0] + "*\r\n");
				}
			}

			// CONTINUAZIONE DELLA RIGA
			rigaEsportaCassa.append("KX");

			// prezzo ivato
			String prezzo = new DecimalFormat("#.00").format(rigaArticolo.getPrezzoIvatoInValutaAzienda());
			// tolgo i punti e le virgole
			prezzo = prezzo.replace(".", "").replace(",", "");
			if (prezzo.length() > 9) {
				throw new EsportaDocumentoCassaException("Prezzo non puo superare i 9 caratteri", descrizioneArticolo);
			}

			rigaEsportaCassa.append(prezzo + "R");
			// reparto
			String reparto = new String();
			reparto = spazi + rigaArticolo.getCodiceIva().getCodiceEsportazioneDocumento();
			rigaEsportaCassa.append(reparto.substring((reparto.length() - 1), reparto.length()));
			// descrizione articollo e nuova riga
			rigaEsportaCassa.append(descrizioneArticolo + "\r\n");
			
		}
		rigaEsportaCassa.append("KXT1" + "\r\n");
		try {
			// Create e scrivo il file
			FileWriter fstream = new FileWriter(fileExportName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(rigaEsportaCassa.toString());
			// Close the output stream
			out.close();
			// file semaforo
			String fileSemaforoName = new String();
			fileSemaforoName = fileExportName.substring(0, fileExportName.indexOf(".")) + ".SEM";
			FileWriter fstreamSemaforo = new FileWriter(fileSemaforoName);
			BufferedWriter outSemaforo = new BufferedWriter(fstreamSemaforo);
			outSemaforo.write("");
			// Close the output stream semaforo
			outSemaforo.close();

		} catch (Exception e) {
			throw new RuntimeException("Errore nell'esportazione del file ", e);
		}
	}

	/**
	 * 
	 * @return principal loggato
	 */
	private JecPrincipal getJecPrincipal() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal());
	}
}
