package it.eurotn.panjea.magazzino.manager.documento.exporter;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.CassaOlivettiExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CassaOlivettiExporter")
public class CassaOlivettiExporter implements IExporter {

	private static Logger logger = Logger.getLogger(CassaOlivettiExporter.class);
	public static final String JNDI_NAME = "Panjea.CassaOlivettiExporter";

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
	public void esporta(AreaMagazzino areaMagazzino) {

		// carico il percorso e nome del file di esportazione
		String fileExportName = caricaPathFileExport();

		// il nome del file è xxxx$.xxx quindi sostituisco il carattere $ con
		// l'utente loggato
		fileExportName = fileExportName.replace("$", getJecPrincipal().getUserName());

		try {
			// Create file
			File tmpFile = File.createTempFile("scont", ".txt");
			StringBuilder sb = new StringBuilder();
			List<RigaArticolo> righeArticolo = rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);
			for (RigaArticolo rigaArticolo : righeArticolo) {
				sb.append("VK;");

				// descrizione articolo ( max 16 caratteri )
				String descrizioneArticolo = rigaArticolo.getArticolo().getDescrizione().trim();
				if (descrizioneArticolo.length() > 16) {
					descrizioneArticolo = descrizioneArticolo.substring(0, 16);
				}
				sb.append(descrizioneArticolo + ";");

				// quantità formattata #####0.00
				sb.append(new DecimalFormat("#####0.00").format(rigaArticolo.getQta()) + ";");

				// prezzo ivato formattato ########0.00
				sb.append(new DecimalFormat("########0.00").format(rigaArticolo.getPrezzoIvatoInValutaAzienda()) + ";");

				// reparto
				sb.append(rigaArticolo.getCodiceIva().getCodiceEsportazioneDocumento() + "\r\n");
			}

			FileUtils.writeStringToFile(tmpFile, sb.toString());
			tmpFile.setWritable(true, false);
			FileUtils.moveFile(tmpFile, new File(fileExportName));
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
