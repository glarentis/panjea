package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.GenerazioneImportazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoAggiornaManager;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.DocumentoFatturatoNonPrevistoException;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.OperatoreSenzaTipoDocumentoFattura;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.DocumentiImportazioneManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.FatturazioneImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.ImportazioneEvaDtsManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.OrdiniCollegatiManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.PagamentiManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.RifornimentoImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.RigaImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Rifornimenti;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.DocumentiImportazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentiImportazioneManager")
public class DocumentiImportazioneManagerBean implements DocumentiImportazioneManager {

    private static final Logger LOGGER = Logger.getLogger(DocumentiImportazioneManagerBean.class);

    @EJB
    private GenerazioneImportazioneManager generazioneImportazioneManager;

    @EJB
    private ImportazioneEvaDtsManager importazioneEvaDtsManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private PagamentiManager pagamentiManager;

    @EJB
    private RifornimentoImportazioneBuilder rifornimentoImportazioneBuilder;

    @EJB
    private OrdiniCollegatiManager ordiniCollegatiManager;

    @EJB
    private FatturazioneImportazioneBuilder fatturazioneImportazioneBuilder;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private RigaImportazioneBuilder rigaImportazioneBuilder;

    @EJB
    private AreaRifornimentoAggiornaManager areaRifornimentoAggiornaManager;

    /**
     * @return userName loggato
     */
    protected String getUserName() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getUserName();
    }

    @Override
    public String importa(String codiceOperatore, ImportazioneXml importazioneXml) throws ImportazioneException {
        LOGGER.debug("--> Enter importa");
        int numRifornimenti = 0;
        int numFatture = 0;
        int numRilevamenti = 0;
        StringBuilder sbException = new StringBuilder();
        Map<Integer, Rifornimenti> rifornimenti = importazioneXml.getRifornimenti();
        for (Rifornimenti rifornimento : rifornimenti.values()) {
            try {
                AreaRifornimento areaRifornimento = rifornimentoImportazioneBuilder.importa(importazioneXml,
                        codiceOperatore, rifornimento);
                numRifornimenti++;

                pagamentiManager.creaPagamenti(areaRifornimento);

                if (importazioneEvaDtsManager.importaEvaDts(areaRifornimento, importazioneXml,
                        rifornimento.getProgressivo())) {
                    numRilevamenti++;
                }

                if (rifornimento.getProgressivoRichiesta() > 0) {
                    ordiniCollegatiManager.collega(rifornimento.getProgressivoRichiesta(), areaRifornimento);
                }

                if (rifornimento.getNumeroFattura() > 0) {
                    try {
                        fatturazioneImportazioneBuilder.importaFattura(rifornimento.getNumeroFattura(),
                                areaRifornimento, rifornimento, importazioneXml);
                        numFatture++;
                    } catch (OperatoreSenzaTipoDocumentoFattura e) {
                        sbException.append(e.getMessage());
                        sbException.append("\n");
                    } catch (DocumentoDuplicateException e) {
                        sbException.append("Fattura n° " + rifornimento.getNumeroFattura()
                                + " già presente...blocco importazione.");
                        break;
                    }
                }
            } catch (DocumentoFatturatoNonPrevistoException e) {
                sbException.append(e.getMessage());
                sbException.append("\n");
            }

        }
        StringBuilder log = new StringBuilder();
        log.append("Num. rif. importati: " + numRifornimenti + "\n");
        if (numFatture > 0) {
            log.append("Num. fatture importate: " + numFatture + "\n");
        }
        if (numRilevamenti > 0) {
            log.append("Num. rilevazioni evaDts importate : " + numRilevamenti + "\n");
        }
        if (sbException.length() > 0) {
            throw new ImportazioneException(sbException.toString());
        }
        LOGGER.debug("--> Exit importa");
        return log.toString();
    }
}
