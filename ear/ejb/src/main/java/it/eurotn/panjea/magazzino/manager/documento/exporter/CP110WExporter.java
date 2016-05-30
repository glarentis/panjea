package it.eurotn.panjea.magazzino.manager.documento.exporter;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
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
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(mappedName = "Panjea.CP110W")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CP110W")
public class CP110WExporter implements IExporter {

	private static Logger logger = Logger.getLogger(CP110WExporter.class);
	public static final String JNDI_NAME = "Panjea.CP110W";

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
		for (RigaArticolo rigaArticolo : righeArticolo) {
			rigaEsportaCassa.append("#");
			// out.write("#");

			// descrizione articolo ( max 20 caratteri )
			String descrizioneArticolo = rigaArticolo.getArticolo().getDescrizione().trim();
			descrizioneArticolo = descrizioneArticolo + spazi;
			descrizioneArticolo = descrizioneArticolo.substring(0, 20);
			rigaEsportaCassa.append(descrizioneArticolo);

			// prezzo ivato
			String prezzo = new DecimalFormat("000000.00").format(rigaArticolo.getPrezzoIvatoInValutaAzienda());
			// tolgo i punti e le virgole
			prezzo = prezzo.replace(".", "").replace(",", "");
			if (prezzo.length() > 8) {
				throw new EsportaDocumentoCassaException("Il prezzo non puo superare gli 8 caratteri",
						descrizioneArticolo);

			}
			rigaEsportaCassa.append(prezzo);

			// segno
			rigaEsportaCassa.append("+");

			// reparto
			// aggiungo i spazi al inzio e dopo prendo un substring lungo 2
			// partendo dalla fine.
			String reparto = new String();
			reparto = spazi + rigaArticolo.getCodiceIva().getCodiceEsportazioneDocumento();
			rigaEsportaCassa.append(reparto.substring((reparto.length() - 2), reparto.length()));

			// quantità formattata
			BigDecimal quantitaBigDec = BigDecimal.valueOf(rigaArticolo.getQta());
			String quantita = new DecimalFormat("0000.00").format(quantitaBigDec);
			// tolgo i punti e le virgole
			quantita = quantita.replace(".", "").replace(",", "");
			if (quantita.length() > 6) {
				throw new EsportaDocumentoCassaException("La quantita non puo superare i 6 caratteri",
						descrizioneArticolo);

			}
			rigaEsportaCassa.append(quantita + "\r\n");
		}

		try {
			// Create e scrivo il file
			File tmpFile = File.createTempFile("scont", ".txt");
			tmpFile.setWritable(true, false);
			FileWriter fstream = new FileWriter(tmpFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(rigaEsportaCassa.toString());
			// Close the output stream
			out.close();
			tmpFile.renameTo(new File(fileExportName));
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
